package com.gattaca.team.service.fake;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.format.DateUtils;

import com.gattaca.team.annotation.FakeMessage;
import com.gattaca.team.annotation.ModuleName;
import com.gattaca.team.annotation.NotifyType;
import com.gattaca.team.annotation.RRType;
import com.gattaca.team.db.RealmController;
import com.gattaca.team.db.event.NotifyEventObject;
import com.gattaca.team.db.sensor.BpmGreen;
import com.gattaca.team.db.sensor.BpmRed;
import com.gattaca.team.db.sensor.RR;
import com.gattaca.team.db.sensor.Session;
import com.gattaca.team.prefs.AppPref;
import com.gattaca.team.root.AppUtils;
import com.gattaca.team.root.MainApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.RealmModel;

public final class FakeDataController extends HandlerThread implements Handler.Callback {
    public static final int timeOffset = 3;
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
                final Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, 12);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                cal.add(Calendar.DAY_OF_YEAR, -1);
                final long startTime = cal.getTimeInMillis();
                final List<RealmModel> rawRealm = new ArrayList<>();
                List<RealmModel> list = null;
                final ArrayList<Long> PcTimesAgain = new ArrayList<>();

                InputStream in = null;
                BufferedReader reader = null;
                String mLine;
                String[] splits;
                NotifyEventObject event;

                int pc_count_per_session = 0,
                        pointsCount = 0,
                        idx = 0,
                        currentRrValue,
                        prevRrValue = 0;
                long time;
                float bpm;
                double timeRR;
                boolean block = false;

                try {/*
                    in = MainApplication.getContext().getAssets().open("session/samples.csv");
                    reader = new BufferedReader(new InputStreamReader(in));
                    mLine = reader.readLine();

                    while (mLine != null) {
                        splits = mLine.split(",");
                        pointsCount++;
                        for (int i = 1; i < 2; i++) {
                            rawRealm.add(new SensorPointData()
                                    //.setChannel(i - 1)
                                    .setSample(pointsCount)
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
*/
                    in = MainApplication.getContext().getAssets().open("session/rr.txt");
                    reader = new BufferedReader(new InputStreamReader(in));
                    mLine = reader.readLine();

                    while (mLine != null) {
                        splits = mLine.split("\t");
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
                                rawRealm.add(new NotifyEventObject()
                                        .setModuleNameResId(ModuleName.Monitor)
                                        .setEventType(NotifyType.PC_3)
                                        .setTime(PcTimesAgain.get(1)));
                                PcTimesAgain.clear();
                            }
                        } else {
                            if (PcTimesAgain.size() == 2) {
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
                            rawRealm.add(new NotifyEventObject()
                                    .setModuleNameResId(ModuleName.Monitor)
                                    .setEventType(NotifyType.PC_more_limit_per_hour)
                                    .setTime(time));
                        }
                        // Search BPM
                        timeRR = Math.abs(currentRrValue - prevRrValue) * timeOffset;
                        bpm = (float) (60000 / timeRR);
                        event = AppUtils.checkBpmAndGenerateEvent((int) bpm, time);
                        if (event != null) {
                            rawRealm.add(event);
                        }

                        list = AppUtils.collapseCHeck(time, timeRR, bpm, false);
                        if (!list.isEmpty()) {
                            rawRealm.addAll(list);
                        }

                        mLine = reader.readLine();

                        if (rawRealm.size() >= max) {
                            RealmController.saveList(rawRealm);
                            rawRealm.clear();
                        }
                        prevRrValue = currentRrValue;
                    }

                    list = AppUtils.collapseCHeck(0, 0, 0, true);
                    if (!list.isEmpty()) {
                        rawRealm.addAll(list);
                    }

                    time = startTime + prevRrValue * timeOffset;
                    RealmController.save(new Session()
                            .setTimeStart(startTime)
                            .setTimeFinish(time));

                    in.close();
                    reader.close();
                    in = MainApplication.getContext().getAssets().open("bpm/healthy_male.csv");
                    reader = new BufferedReader(new InputStreamReader(in));
                    reader.readLine();
                    mLine = reader.readLine();

                    while (mLine != null) {
                        splits = mLine.split(",");
                        long tmpTime = 30 * DateUtils.SECOND_IN_MILLIS + idx++ * DateUtils.MINUTE_IN_MILLIS;
                        rawRealm.add(new BpmGreen()
                                .setTime(tmpTime)
                                .setValueBottom(Float.valueOf(splits[6]))
                                .setValueTop(Float.valueOf(splits[97])));
                        rawRealm.add(new BpmRed()
                                .setTime(tmpTime)
                                .setValueBottom(Float.valueOf(splits[2]))
                                .setValueTop(Float.valueOf(splits[100])));

                        mLine = reader.readLine();

                        if (rawRealm.size() >= max) {
                            RealmController.saveList(rawRealm);
                            rawRealm.clear();
                        }
                    }

                    changeState(FakeMessage.Finish);
                } catch (Exception tx) {
                    tx.printStackTrace();
                } finally {
                    if (!rawRealm.isEmpty()) {
                        RealmController.saveList(rawRealm);
                        rawRealm.clear();
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
                break;
            case FakeMessage.Finish:
                AppPref.FakeGeneration.set(true);
                break;
        }
        MainApplication.uiBusPost(reason);
        return true;
    }
}
