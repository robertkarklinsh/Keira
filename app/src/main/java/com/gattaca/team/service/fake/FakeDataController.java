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
import com.gattaca.team.db.sensor.Session;
import com.gattaca.team.db.sensor.optimizing.SensorPoint_1_hour;
import com.gattaca.team.db.sensor.optimizing.SensorPoint_5_min;
import com.gattaca.team.prefs.AppPref;
import com.gattaca.team.root.AppUtils;
import com.gattaca.team.root.MainApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmModel;

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
                final int max = 40000;
                final long startTime = System.currentTimeMillis() - DateUtils.DAY_IN_MILLIS;
                final int timeOffset = 3;
                final List<RealmModel> rawRealm = new ArrayList<>();
                final ArrayList<Long> PcTimesAgain = new ArrayList<>();
                final ArrayList<Float> bpm5 = new ArrayList<>();
                final ArrayList<Float> bpm60 = new ArrayList<>();

                InputStream in = null;
                BufferedReader reader = null;
                String mLine;
                String[] splits;

                int pc_count_per_session = 0,
                        pointsCount = 0,
                        ppCounts = 0,
                        eventPcCounts = 0,
                        eventBpmCounts = 0,
                        rr5 = 0,
                        rr60 = 0,
                        currentRrValue,
                        prevRrValue = 0;
                long time;
                float bpm;
                double timeRR, time5 = 0, time60 = 0;
                boolean block = false;

                try {
                    in = MainApplication.getContext().getAssets().open("session/samples.csv");
                    reader = new BufferedReader(new InputStreamReader(in));
                    mLine = reader.readLine();

                    while (mLine != null) {
                        splits = mLine.split(",");
                        pointsCount++;
                        for (int i = 1; i < splits.length; i++) {
                            rawRealm.add(new SensorPointData()
                                    .setChannel(i - 1)
                                    .setTime(startTime + pointsCount * timeOffset)
                                    .setValue(Double.valueOf(splits[i])));
                        }
                        mLine = reader.readLine();

                        if (rawRealm.size() >= max) {
                            RealmController.saveList(rawRealm);
                            rawRealm.clear();
                        }
                    }
                    in.close();
                    reader.close();

                    in = MainApplication.getContext().getAssets().open("session/rr.txt");
                    reader = new BufferedReader(new InputStreamReader(in));
                    mLine = reader.readLine();

                    while (mLine != null) {
                        splits = mLine.split("\t");
                        ppCounts++;
                        currentRrValue = Integer.valueOf(splits[0]);
                        time = startTime + currentRrValue * timeOffset;
                        rawRealm.add(new RR()
                                .setTime(time)
                                .setType(splits[1]));

                        // Search PC
                        if (!splits[1].equals(RRType.N)) {
                            pc_count_per_session++;
                            PcTimesAgain.add(time);

                            if (PcTimesAgain.size() == 3) {
                                eventPcCounts++;
                                rawRealm.add(new NotifyEventObject()
                                        .setModuleNameResId(ModuleName.Monitor)
                                        .setEventType(NotifyType.PC_3)
                                        .setTime(PcTimesAgain.get(1)));
                                PcTimesAgain.clear();
                            }
                        } else {
                            if (PcTimesAgain.size() == 2) {
                                eventPcCounts++;
                                rawRealm.add(new NotifyEventObject()
                                        .setModuleNameResId(ModuleName.Monitor)
                                        .setEventType(NotifyType.PC_2)
                                        .setTime(PcTimesAgain.get(1)));
                            }
                            PcTimesAgain.clear();
                        }

                        // only 1 event move 30 bits per session
                        if (!block && pc_count_per_session == 31) {
                            block = true;
                            eventPcCounts++;
                            rawRealm.add(new NotifyEventObject()
                                    .setModuleNameResId(ModuleName.Monitor)
                                    .setEventType(NotifyType.PC_more_limit_per_hour)
                                    .setTime(time));
                        }
                        // Search BPM
                        timeRR = Math.abs(currentRrValue - prevRrValue) * timeOffset;
                        time5 += timeRR;
                        time60 += timeRR;
                        bpm = (float) (60000 / timeRR);

                        if (bpm < 40) {
                            eventBpmCounts++;
                            rawRealm.add(new NotifyEventObject()
                                    .setModuleNameResId(ModuleName.Monitor)
                                    .setEventType(NotifyType.BPM_less_40)
                                    .setCount(bpm)
                                    .setTime(time));
                        } else if (bpm < 45) {
                            eventBpmCounts++;
                            rawRealm.add(new NotifyEventObject()
                                    .setModuleNameResId(ModuleName.Monitor)
                                    .setEventType(NotifyType.BPM_less_50_more_40)
                                    .setCount(bpm)
                                    .setTime(time));
                        } else if (bpm > 120) {
                            eventBpmCounts++;
                            rawRealm.add(new NotifyEventObject()
                                    .setModuleNameResId(ModuleName.Monitor)
                                    .setEventType(NotifyType.BPM_more_100)
                                    .setCount(bpm)
                                    .setTime(time));
                        }

                        if (time5 >= 1667) {
                            time5 -= 1667;
                            rr5++;
                            rawRealm.add(new SensorPoint_5_min()
                                    .setValue(AppUtils.convertListToAvrValue(bpm5))
                                    .setTime(time));
                            bpm5.clear();
                        } else {
                            bpm5.add(bpm);
                        }
                        if (time60 >= 20000) {
                            time60 -= 20000;
                            rr60++;
                            rawRealm.add(new SensorPoint_1_hour()
                                    .setValue(AppUtils.convertListToAvrValue(bpm60))
                                    .setTime(time));
                            bpm60.clear();
                        } else {
                            bpm60.add(bpm);
                        }

                        mLine = reader.readLine();


                        if (rawRealm.size() >= max) {
                            RealmController.saveList(rawRealm);
                            rawRealm.clear();
                        }
                        prevRrValue = currentRrValue;
                    }

                    if (time5 > 0) {
                        rr5++;
                        rawRealm.add(new SensorPoint_5_min()
                                .setValue(AppUtils.convertListToAvrValue(bpm5))
                                .setTime(startTime + prevRrValue * timeOffset));
                    }
                    if (time60 > 0) {
                        rr5++;
                        rawRealm.add(new SensorPoint_1_hour()
                                .setValue(AppUtils.convertListToAvrValue(bpm60))
                                .setTime(startTime + prevRrValue * timeOffset));
                    }
                    if (!rawRealm.isEmpty()) {
                        RealmController.saveList(rawRealm);
                        rawRealm.clear();
                    }

                    RealmController.save(new Session()
                            .setTimeStart(startTime)
                            .setTimeFinish(startTime + pointsCount * timeOffset));

                    changeState(FakeMessage.Finish);

                    Log.i(getClass().getSimpleName(), "Sensor data count = " + pointsCount + "\nRR interval data count is " + ppCounts);
                    Log.i(getClass().getSimpleName(), "Events:\npc count = " + eventPcCounts + "\nbpm count = " + eventBpmCounts);
                    Log.i(getClass().getSimpleName(), "Clustered:\n5 min = " + rr5 + "\n60 min = " + rr60);
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
