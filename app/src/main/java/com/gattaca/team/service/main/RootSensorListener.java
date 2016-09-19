package com.gattaca.team.service.main;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.gattaca.team.root.MainApplication;
import com.gattaca.team.service.IServiceConnection;
import com.gattaca.team.service.bitalino.BitalinoConnection;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

public final class RootSensorListener extends HandlerThread implements Handler.Callback {
    private static RootSensorListener instance;
    private Bus bus = new Bus(ThreadEnforcer.ANY);
    private IServiceConnection serviceConnectionImpl = new BitalinoConnection();
    private Handler handler;

    RootSensorListener() {
        super("RootSensorListener");
        start();
        waitUntilReady();
    }

    public static void postSensorData(final SensorData data) {
        getInstance().bus.post(data);
    }

    public static RootSensorListener getInstance() {
        if (instance == null) {
            instance = new RootSensorListener();
        }
        return instance;
    }

    synchronized void waitUntilReady() {
        this.handler = new Handler(this.getLooper(), this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (What.values()[msg.what]) {
            case DataTick:
                //TODO: logic for work with data
                MainApplication.uiBusPost(msg.obj);
                break;
        }
        return true;
    }

    @Subscribe
    public void tickSensorData(SensorData data) {
        final Message m = new Message();
        m.what = What.DataTick.ordinal();
        m.obj = data;
        handler.sendMessage(m);
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < data.countTicks(); i++) {
            builder.append("\ntimestump=").append(data.getTimeStump(i));
            for (int j = 0; j < data.getChannels(); j++) {
                builder.append("#").append(j).append("=").append(data.getVoltByChannel(i, j)).append("   ");
            }
            builder.append("\n");
        }
        Log.i(getClass().getSimpleName(), builder.toString());
    }

    enum What {
        DataTick
    }
}
