package com.gattaca.team.service.fake;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.format.DateUtils;
import android.util.Log;

import com.gattaca.team.annotation.FakeMessage;
import com.gattaca.team.annotation.ModuleName;
import com.gattaca.team.annotation.NotifyType;
import com.gattaca.team.annotation.RRType;
import com.gattaca.team.db.RealmController;
import com.gattaca.team.db.event.NotifyEventObject;
import com.gattaca.team.db.sensor.RR;
import com.gattaca.team.db.sensor.SensorPointData;
import com.gattaca.team.prefs.AppPref;
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
                RealmController.clearAll();
                final int max = 20000;
                final long startTime = System.currentTimeMillis() - DateUtils.DAY_IN_MILLIS;
                final int timeOffset = 3;
                long time;
                InputStream in = null;
                BufferedReader reader = null;
                final ArrayList<SensorPointData> sensorList = new ArrayList<>();
                final ArrayList<RR> rrList = new ArrayList<>();
                final ArrayList<NotifyEventObject> eventsList = new ArrayList<>();
                final ArrayList<Long> PcTimesAgain = new ArrayList<>();
                boolean block = false;

                try {
                    in = MainApplication.getContext().getAssets().open("session/samples.csv");
                    reader = new BufferedReader(new InputStreamReader(in));
                    String mLine = reader.readLine();
                    int line = 0,
                            pc_count_per_session = 0,
                            pointsCount = 0,
                            ppCounts = 0,
                            eventPcCounts = 0,
                            eventBpmCounts = 0;
                    String[] splits;

                    while (mLine != null) {
                        splits = mLine.split(",");
                        pointsCount++;
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

                    if (!sensorList.isEmpty()) {
                        RealmController.saveList(sensorList);
                        sensorList.clear();
                    }

                    in = MainApplication.getContext().getAssets().open("session/rr.txt");
                    reader = new BufferedReader(new InputStreamReader(in));
                    mLine = reader.readLine();

                    while (mLine != null) {
                        splits = mLine.split("\t");
                        ppCounts++;
                        time = startTime + Integer.valueOf(splits[0]) * timeOffset;
                        rrList.add(new RR()
                                .setTime(time)
                                .setType(splits[1]));

                        // Search PC
                        if (!splits[1].equals(RRType.N)) {
                            pc_count_per_session++;
                            PcTimesAgain.add(time);

                            if (PcTimesAgain.size() == 3) {
                                eventPcCounts++;
                                eventsList.add(new NotifyEventObject()
                                        .setModuleNameResId(ModuleName.Monitor)
                                        .setEventType(NotifyType.PC_3)
                                        .setTime(PcTimesAgain.get(1)));
                                PcTimesAgain.clear();
                            }
                        } else {
                            if (PcTimesAgain.size() == 2) {
                                eventPcCounts++;
                                eventsList.add(new NotifyEventObject()
                                        .setModuleNameResId(ModuleName.Monitor)
                                        .setEventType(NotifyType.PC_2)
                                        .setTime(PcTimesAgain.get(0) + (PcTimesAgain.get(1) - PcTimesAgain.get(0)) / 2));
                            }
                            PcTimesAgain.clear();
                        }

                        // only 1 event move 30 bits per session
                        if (!block && pc_count_per_session == 31) {
                            block = true;
                            eventPcCounts++;
                            eventsList.add(new NotifyEventObject()
                                    .setModuleNameResId(ModuleName.Monitor)
                                    .setEventType(NotifyType.PC_more_limit_per_hour)
                                    .setTime(time));
                        }
                        // Search BPM
                        int bpm = (int) (60 / Double.valueOf(splits[2]));
                        if (bpm < 40) {
                            eventBpmCounts++;
                            eventsList.add(new NotifyEventObject()
                                    .setModuleNameResId(ModuleName.Monitor)
                                    .setEventType(NotifyType.BPM_less_40)
                                    .setCount(bpm)
                                    .setTime(time));
                        } else if (bpm < 45) {
                            eventBpmCounts++;
                            eventsList.add(new NotifyEventObject()
                                    .setModuleNameResId(ModuleName.Monitor)
                                    .setEventType(NotifyType.BPM_less_50_more_40)
                                    .setCount(bpm)
                                    .setTime(time));
                        } else if (bpm > 120) {
                            eventBpmCounts++;
                            eventsList.add(new NotifyEventObject()
                                    .setModuleNameResId(ModuleName.Monitor)
                                    .setEventType(NotifyType.BPM_more_100)
                                    .setCount(bpm)
                                    .setTime(time));
                        }
                        mLine = reader.readLine();


                        if (rrList.size() >= max / 2) {
                            RealmController.saveList(rrList);
                            rrList.clear();
                        }
                        if (eventsList.size() >= max / 2) {
                            RealmController.saveList(eventsList);
                            eventsList.clear();
                        }
                    }

                    if (!rrList.isEmpty()) {
                        RealmController.saveList(rrList);
                        rrList.clear();
                    }
                    if (!eventsList.isEmpty()) {
                        RealmController.saveList(eventsList);
                        eventsList.clear();
                    }

                    changeState(FakeMessage.Finish);
                    Log.i(getClass().getSimpleName(), "Sensor data count is " + pointsCount);
                    Log.i(getClass().getSimpleName(), "RR interval data count is " + ppCounts);
                    Log.i(getClass().getSimpleName(), "Events pc count are " + eventPcCounts);
                    Log.i(getClass().getSimpleName(), "Events bpm count are " + eventBpmCounts);
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
                break;
            case FakeMessage.Finish:
                AppPref.FakeGeneration.set(true);
                break;
        }
        MainApplication.uiBusPost(reason);
        return true;
    }
}
