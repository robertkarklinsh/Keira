package com.gattaca.team.service.bitalino;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;

import com.bitalino.comm.BITalinoDevice;
import com.bitalino.comm.BITalinoException;
import com.bitalino.comm.BITalinoFrame;
import com.gattaca.team.prefs.AppPref;
import com.gattaca.team.root.MainApplication;
import com.gattaca.team.service.ErrorCode;
import com.gattaca.team.service.IServiceConnection;
import com.gattaca.team.service.SensorData;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

public final class BitalinoConnection extends HandlerThread implements IServiceConnection, Handler.Callback {
    public static Bus bus = new Bus(ThreadEnforcer.ANY);
    private final Object block = new Object();
    private Handler handler;
    private State state;
    private BluetoothSocket sock;
    private BITalinoDevice bitalino;
    private boolean forceStopAnyProcess = false;

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
    public void fakeGeneration() {
        handler.sendEmptyMessage(State.FaceGeneration.ordinal());
    }

    @Override
    public void stopConnection() {
        forceStopAnyProcess = true;
        synchronized (block) {
            block.notify();
        }
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
            case FaceGeneration:
                final long timeOffset = DateUtils.SECOND_IN_MILLIS / SensorData.HZ; // ms per data tick
                InputStream in = null;
                BufferedReader reader = null;
                long tmpTime, currentTime = System.currentTimeMillis();
                SensorData sensorData;
                int tmpLines = 0;
                try {
                    in = MainApplication.getContext().getAssets().open("bitalino.txt");
                    reader = new BufferedReader(new InputStreamReader(in));
                    String mLine = reader.readLine();
                    int line = 0, sig;
                    String[] splits;
                    final BITalinoFrame[] frames = new BITalinoFrame[SensorData.FRAMES_COUNT];

                    while (!forceStopAnyProcess && mLine != null) {
                        splits = mLine.split("\t");
                        sig = 0;
                        for (String p : splits) {
                            if (frames[line] == null) {
                                frames[line] = new BITalinoFrame();
                            }
                            frames[line].setAnalog(sig++, Integer.valueOf(p));
                        }
                        mLine = reader.readLine();

                        if (line++ != 0 && line % SensorData.FRAMES_COUNT == 0) {
                            line = 0;
                            tmpTime = currentTime + timeOffset * SensorData.FRAMES_COUNT;
                            sensorData = new SensorData(currentTime, tmpTime);
                            for (BITalinoFrame frame : frames) {
                                sensorData.addFrame(frame);
                            }
                            currentTime = tmpTime;
                            MainApplication.uiBusPost(sensorData);
                        }
                        synchronized (block) {
                            block.wait(timeOffset);
                        }
                        tmpLines++;
                    }
                } catch (Exception tx) {
                    tx.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                Log.e(getClass().getSimpleName(), "tries were " + tmpLines);
                break;
            case NoConnection:
                //TODO: default state
                forceStopAnyProcess = false;
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
                        bus.register(this);
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
                    bitalino = new BITalinoDevice(SensorData.HZ, new int[]{0, 1, 2, 3, 4, 5});
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
                        final BITalinoFrame[] frames = bitalino.read(SensorData.FRAMES_COUNT);
                        final long endTime = System.currentTimeMillis();
                        handler.sendEmptyMessage(State.TransferData.ordinal());

                        sensorData = new SensorData(startTime, endTime);
                        for (BITalinoFrame frame : frames) {
                            sensorData.addFrame(frame);
                        }
                        MainApplication.uiBusPost(sensorData);
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
                MainApplication.uiBusPost(msg.obj);
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
            bus.unregister(this);
            BluetoothAdapter.getDefaultAdapter().cancelDiscovery();

            handler.sendEmptyMessage(State.Connection.ordinal());
        }
    }


    enum State {
        FaceGeneration,
        NoConnection,
        Search,
        Connection,
        TransferData,
        StopConnection,
        Error
    }
}
