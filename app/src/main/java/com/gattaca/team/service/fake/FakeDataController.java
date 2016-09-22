package com.gattaca.team.service.fake;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.format.DateUtils;

import com.gattaca.team.annotation.FakeMessage;
import com.gattaca.team.db.RealmController;
import com.gattaca.team.db.sensor.RR;
import com.gattaca.team.db.sensor.SensorPointData;
import com.gattaca.team.root.MainApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public final class FakeDataController extends HandlerThread implements Handler.Callback {
    private static FakeDataController instance;
    private Handler handler;

    private FakeDataController() {
        super("FakeDataController");
        start();
        waitUntilReady();
    }

    public static FakeDataController getInstance() {
        if (instance == null) {
            instance = new FakeDataController();
        }
        return instance;
    }

    public static void startGeneration() {
        getInstance().changeState(FakeMessage.Start);
    }

    private synchronized void waitUntilReady() {
        this.handler = new Handler(this.getLooper(), this);
    }

    private void changeState(@FakeMessage int state) {
        handler.sendEmptyMessage(state);
    }

    @Override
    public boolean handleMessage(Message msg) {
        final @FakeMessage int reason = msg.what;
        switch (reason) {
            case FakeMessage.Start:
                final int max = 100500;
                final long startTime = System.currentTimeMillis() - DateUtils.DAY_IN_MILLIS;
                final int timeOffset = 3;
                InputStream in = null;
                BufferedReader reader = null;
                final ArrayList<SensorPointData> sensorList = new ArrayList<>();
                final ArrayList<RR> rrList = new ArrayList<>();

                try {
                    in = MainApplication.getContext().getAssets().open("session/samples.csv");
                    reader = new BufferedReader(new InputStreamReader(in));
                    String mLine = reader.readLine();
                    int line = 0;
                    String[] splits;

                    while (mLine != null) {
                        splits = mLine.split(",");
                        for (int i = 1; i < splits.length; i++) {
                            sensorList.add(new SensorPointData()
                                    .setChannel(i - 1)
                                    .setTime(startTime + line * timeOffset)
                                    .setValue(Double.valueOf(splits[i])));
                        }
                        line++;
                        mLine = reader.readLine();

                        if (sensorList.size() >= max) {
                            RealmController.saveList(sensorList);
                            sensorList.clear();
                        }
                    }
                    in.close();
                    reader.close();

                    in = MainApplication.getContext().getAssets().open("session/rr.txt");
                    reader = new BufferedReader(new InputStreamReader(in));
                    mLine = reader.readLine();

                    while (mLine != null) {
                        splits = mLine.split("\t");
                        rrList.add(new RR()
                                .setTime(startTime + Integer.valueOf(splits[0]) * timeOffset)
                                .setType(splits[1]));
                        mLine = reader.readLine();

                        if (rrList.size() >= max) {
                            RealmController.saveList(rrList);
                            rrList.clear();
                        }
                    }
                } catch (Exception tx) {
                    tx.printStackTrace();
                } finally {
                    if (!sensorList.isEmpty()) {
                        RealmController.saveList(sensorList);
                    }
                    if (!rrList.isEmpty()) {
                        RealmController.saveList(rrList);
                    }
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
                changeState(FakeMessage.Finish);
                break;
            case FakeMessage.Finish:
                //AppPref.FakeGeneration.set(true);
                break;
        }
        MainApplication.uiBusPost(reason);
        return true;
    }
}
