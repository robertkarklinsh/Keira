package com.gattaca.team.service.bitalino;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.gattaca.team.MainApplication;
import com.gattaca.team.prefs.AppPref;
import com.gattaca.team.service.IServiceConnection;
import com.squareup.otto.Subscribe;

public class BitalinoConnection extends HandlerThread implements IServiceConnection, Handler.Callback {
    private static BitalinoConnection instance;
    private Handler handler;
    private State state = State.NoConnection;

    BitalinoConnection() {
        super("BitalinoConnection");
    }

    public static BitalinoConnection init() {
        instance = new BitalinoConnection();
        instance.start();
        instance.waitUntilReady();
        return instance;
    }

    public static void startConnection() {
        instance.handler.sendEmptyMessage(State.Search.ordinal());
    }

    synchronized void waitUntilReady() {
        this.handler = new Handler(this.getLooper(), this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        final Message newState = new Message();
        final State requestedState = State.values()[msg.what];
        Log.d(getClass().getSimpleName(), "handleMessage " + requestedState);

        switch (requestedState) {
            case NoConnection:
                //TODO: default state
                break;
            case Search:
                final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
                if (btAdapter == null) {
                    newState.what = State.Error.ordinal();
                    newState.obj = "BluetoothAdapter is null";
                    instance.handler.sendMessage(newState);
                } else if (!btAdapter.isEnabled()) {
                    newState.what = State.Error.ordinal();
                    newState.obj = "BluetoothAdapter is not enabled";
                    instance.handler.sendMessage(newState);
                } else {
                    if (btAdapter.isDiscovering()) {
                        btAdapter.cancelDiscovery();
                    }
                    if (AppPref.BitalinoMac.getStr() == null) {
                        MainApplication.busRegister(this);
                        MainApplication.getContext().registerReceiver(BtDiscaveryReceiver.getInstance(), new IntentFilter(BluetoothDevice.ACTION_FOUND));
                        Log.d(getClass().getSimpleName(), " discovery starting with " + btAdapter.startDiscovery());
                    } else {
                        instance.handler.sendEmptyMessage(State.Connection.ordinal());
                    }
                }
                break;
            case Connection:
                //TODO: connect to device by mac address
                break;
            case TransferData:
                //TODO: get data from device
                break;
            case Error:
                // throw error body to bus
                MainApplication.busPost(msg.obj);
                break;
        }
        return true;
    }

    @Subscribe
    public void discaveryDevice(BluetoothDevice device) {
        Log.d(getClass().getSimpleName(), "discaveryDevice " + device.getName());
        if ("bitalino".equals(device.getName())) {
            AppPref.BitalinoMac.set(device.getAddress());
            final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
            btAdapter.cancelDiscovery();
            MainApplication.getContext().unregisterReceiver(BtDiscaveryReceiver.getInstance());
            MainApplication.busUnregister(this);

            instance.handler.sendEmptyMessage(State.Connection.ordinal());
        }
    }


    enum State {
        NoConnection,
        Search,
        Connection,
        TransferData,
        Error
    }
}
