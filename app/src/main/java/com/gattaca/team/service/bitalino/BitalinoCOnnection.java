package com.gattaca.team.service.bitalino;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.bitalino.comm.BITalinoDevice;
import com.bitalino.comm.BITalinoException;
import com.bitalino.comm.BITalinoFrame;
import com.gattaca.team.MainApplication;
import com.gattaca.team.prefs.AppPref;
import com.gattaca.team.service.ErrorCode;
import com.gattaca.team.service.IServiceConnection;
import com.gattaca.team.service.SensorData;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.UUID;

public class BitalinoConnection extends HandlerThread implements IServiceConnection, Handler.Callback {
    final private static int HZ = 100;
    final private static int FRAMES_COUNT = 100;
    private Handler handler;
    private State state;
    private BluetoothSocket sock;
    private BITalinoDevice bitalino;

    public BitalinoConnection() {
        super("BitalinoConnection");
        start();
        waitUntilReady();
    }

    @Override
    public void startConnection() {
        handler.sendEmptyMessage(State.Search.ordinal());
    }

    @Override
    public void stopConnection() {
        handler.sendEmptyMessage(State.StopConnection.ordinal());
    }

    synchronized void waitUntilReady() {
        this.handler = new Handler(this.getLooper(), this);
        this.handler.sendEmptyMessage(State.NoConnection.ordinal());

    }

    @Override
    public boolean handleMessage(Message msg) {
        final Message newState = new Message();
        state = State.values()[msg.what];
        Log.d(getClass().getSimpleName(), "new state is " + state);

        switch (state) {
            case NoConnection:
                //TODO: default state
                try {
                    MainApplication.getContext().unregisterReceiver(BtDiscaveryReceiver.getInstance());
                } catch (IllegalArgumentException e) {
                }
                break;
            case Search:
                final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
                if (btAdapter == null) {
                    newState.what = State.Error.ordinal();
                    newState.obj = ErrorCode.BT_absent;
                    handler.sendMessage(newState);
                } else if (!btAdapter.isEnabled()) {
                    newState.what = State.Error.ordinal();
                    newState.obj = ErrorCode.BT_not_enabled;
                    handler.sendMessage(newState);
                } else {
                    if (btAdapter.isDiscovering()) {
                        btAdapter.cancelDiscovery();
                    }
                    if (AppPref.BitalinoMac.getStr() == null) {
                        MainApplication.busRegister(this);
                        MainApplication.getContext().registerReceiver(BtDiscaveryReceiver.getInstance(), new IntentFilter(BluetoothDevice.ACTION_FOUND));
                        Log.d(getClass().getSimpleName(), " discovery starting with " + btAdapter.startDiscovery());
                    } else {
                        handler.sendEmptyMessage(State.Connection.ordinal());
                    }
                }
                break;
            case Connection:
                //TODO: connect to device by mac address
                final BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(AppPref.BitalinoMac.getStr());
                try {
                    sock = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                    sock.connect();
                    bitalino = new BITalinoDevice(HZ, new int[]{0, 1, 2, 3, 4, 5});
                    bitalino.open(sock.getInputStream(), sock.getOutputStream());
                    bitalino.start();
                    handler.sendEmptyMessage(State.TransferData.ordinal());
                } catch (IOException | BITalinoException e) {
                    e.printStackTrace();
                    newState.what = State.Error.ordinal();
                    newState.obj = ErrorCode.BT_get_data_error;
                    handler.sendMessage(newState);
                }
                break;
            case TransferData:
                if (sock != null && bitalino != null) {
                    try {
                        final long startTime = System.currentTimeMillis();
                        final BITalinoFrame[] frames = bitalino.read(FRAMES_COUNT);
                        final long endTime = System.currentTimeMillis();
                        handler.sendEmptyMessage(State.TransferData.ordinal());

                        final SensorData sensorData = new SensorData(startTime, endTime);
                        for (BITalinoFrame frame : frames) {
                            sensorData.addFrame(frame);
                        }
                        MainApplication.busPost(sensorData);
                    } catch (BITalinoException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case StopConnection:
                if (bitalino != null) {
                    try {
                        bitalino.stop();
                        bitalino = null;
                    } catch (BITalinoException e) {
                        e.printStackTrace();
                    }
                }
                if (sock != null) {
                    try {
                        sock.close();
                        sock = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                handler.sendEmptyMessage(State.NoConnection.ordinal());
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
            MainApplication.getContext().unregisterReceiver(BtDiscaveryReceiver.getInstance());
            MainApplication.busUnregister(this);
            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

            handler.sendEmptyMessage(State.Connection.ordinal());
        }
    }


    enum State {
        NoConnection,
        Search,
        Connection,
        TransferData,
        StopConnection,
        Error
    }
}
