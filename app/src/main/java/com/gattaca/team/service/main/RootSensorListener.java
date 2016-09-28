package com.gattaca.team.service.main;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import com.gattaca.team.annotation.GraphPeriod;
import com.gattaca.team.annotation.NotifyType;
import com.gattaca.team.annotation.RRType;
import com.gattaca.team.db.RealmController;
import com.gattaca.team.db.event.NotifyEventObject;
import com.gattaca.team.db.sensor.EmulatedBpm;
import com.gattaca.team.db.sensor.SensorPointData;
import com.gattaca.team.db.sensor.optimizing.BpmPoint_5_min;
import com.gattaca.team.root.AppUtils;
import com.gattaca.team.root.MainApplication;
import com.gattaca.team.service.IServiceConnection;
import com.gattaca.team.service.analysis.PanTompkins;
import com.gattaca.team.service.bitalino.BitalinoConnection;
import com.gattaca.team.service.events.NotifyEvent;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import java.util.ArrayList;
import java.util.List;

public final class RootSensorListener extends HandlerThread implements Handler.Callback {
    private static RootSensorListener instance;
    private final Object block = new Object();
    private Bus bus = new Bus(ThreadEnforcer.ANY);
    private IServiceConnection serviceConnectionImpl = new BitalinoConnection();
    private Handler handler;
    private PanTompkins[] algoritms;
    private boolean emulating = false;

    private RootSensorListener() {
        super("RootSensorListener");
        final int[] simpleRates = serviceConnectionImpl.getChannelsBitRate();
        algoritms = new PanTompkins[simpleRates.length];
        for (int i = 0; i < simpleRates.length; i++) {
            algoritms[i] = new PanTompkins(simpleRates[i]);
        }
        start();
        waitUntilReady();
    }

    public static void startRaw() {
        getInstance().bus.register(getInstance());
        getInstance().serviceConnectionImpl.startConnection();
    }

    public static void stopRaw() {
        getInstance().emulating = false;
        getInstance().serviceConnectionImpl.stopConnection();
        try {
            getInstance().bus.unregister(getInstance());
        } catch (IllegalArgumentException e) {
        }
    }

    public static void generateRaw() {
        getInstance().handler.sendEmptyMessage(What.EmulateData.ordinal());
    }

    public static boolean isInProgress() {
        return getInstance().emulating;
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

    private static void generateEvent(@NotifyType int eventType, final List<PanTompkins.QRS> list) {
        final NotifyEvent event = new NotifyEvent(eventType);
        final NotifyEventObject notifyEventObject = new NotifyEventObject().setEventType(eventType);
        for (PanTompkins.QRS qrs : list) {
            event.addTime(qrs.rTimestamp);
            notifyEventObject.setTime(qrs.rTimestamp);
        }
        MainApplication.uiBusPost(event);
        RealmController.save(notifyEventObject);
    }

    private synchronized void waitUntilReady() {
        this.handler = new Handler(this.getLooper(), this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        Log.d(getClass().getSimpleName(), "state is " + What.values()[msg.what]);
        switch (What.values()[msg.what]) {
            case DataTick:
                //@RRType String RRtype;
                SensorData.FormattedSensorItem item;
                final SensorData data = SensorData.class.cast(msg.obj);
                final List<SensorPointData> sensorPointData = new ArrayList<>();
                data.resetCursor();
                do {
                    for (int i = 0; i < data.getMaxChannelCount(); i++) {
                        item = data.getItem(i);
                        algoritms[i].next(item.getValue(), item.getTime());
                        item.setNewValue(algoritms[i].y[3]); // FIXME: y[3] - is it correct ?
                        sensorPointData.add(new SensorPointData()
                                .setChannel(i)
                                .setTime(item.getTime())
                                .setValue(algoritms[i].y[3]));


                        //FIXME: it's not work!!!
                        /*if (PanTompkins.QRS.qrsCurrent.segState == PanTompkins.QRS.SegmentationStatus.FINISHED) {
                            RRtype = checkQRS(PanTompkins.QRS.qrsCurrent);
                            long time = PanTompkins.QRS.qrsCurrent.rTimestamp - PanTompkins.QRS.qrsPrevious.rTimestamp;
                            if (time >= 40 && time <= 130) {
                                RealmController.save(new RR()
                                        .setType(RRtype)
                                        .setTime(PanTompkins.QRS.qrsCurrent.rTimestamp));
                                Log.e(getClass().getSimpleName(), "save RR type=" + RRtype + "\ttime=" + PanTompkins.QRS.qrsCurrent.rTimestamp);
                            } else {
                                Log.e(getClass().getSimpleName(), "skip from " + time);
                            }
                            *//*if (!RRtype.equals(RRType.N)) {
                                if (algoritms[i].countPC.size() == 2) {
                                    generateEvent(NotifyType.PC_3, algoritms[i].countPC);
                                    algoritms[i].countPC.clear();
                                } else {
                                    algoritms[i].countPC.add(PanTompkins.QRS.qrsCurrent);
                                }
                            } else {
                                if (algoritms[i].countPC.size() == 2) {
                                    generateEvent(NotifyType.PC_2, algoritms[i].countPC);
                                }
                                algoritms[i].countPC.clear();
                            }*//*
                            PanTompkins.QRS.qrsCurrent.segState = PanTompkins.QRS.SegmentationStatus.PROCESSED;
                        }*/
                    }
                } while (data.nextCursor());
                RealmController.saveList(sensorPointData);
                MainApplication.uiBusPost(data);
                break;
            case EmulateData:
                emulating = true;
                RealmController.clearEmulate();
                final List<BpmPoint_5_min> fakes = RealmController.getStubSessionBpm5();
                int idx = 0;
                while (emulating) {
                    RealmController.save(EmulatedBpm.createFromBpm(fakes.get(idx)));
                    if (++idx == fakes.size() - 1) {
                        idx = 0;
                    }
                    synchronized (block) {
                        try {
                            block.wait((long) AppUtils.getCollapseTimeForPeriod(GraphPeriod.period_5min));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
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
    }

    private
    @RRType
    String checkQRS(PanTompkins.QRS current) {
        if (current == null) return RRType.N;
        switch (current.classification) {
            case APC:
                return RRType.A;
            case APC_ABERRANT:
                return RRType.a;
            case PVC:
                return RRType.V;
            case PVC_ABERRANT:
                return RRType.V;
            case PREMATURE:
                return RRType.J;
            default:
                return RRType.N;
        }
    }

    private enum What {
        DataTick,
        DetectPC,
        EmulateData
    }
}
