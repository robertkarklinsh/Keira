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
import com.gattaca.team.db.sensor.RR;
import com.gattaca.team.db.sensor.SensorPointData;
import com.gattaca.team.db.sensor.Session;
import com.gattaca.team.db.sensor.emulate.EmulatedBpm_15Min;
import com.gattaca.team.db.sensor.emulate.EmulatedBpm_5Min;
import com.gattaca.team.db.sensor.optimizing.BpmPoint_15_min;
import com.gattaca.team.db.sensor.optimizing.BpmPoint_5_min;
import com.gattaca.team.root.AppUtils;
import com.gattaca.team.root.MainApplication;
import com.gattaca.team.service.IServiceConnection;
import com.gattaca.team.service.analysis.PanTompkins;
import com.gattaca.team.service.bitalino.BitalinoConnection;
import com.gattaca.team.service.events.NotifyEvent;
import com.gattaca.team.service.fake.FakeDataController;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import java.util.ArrayList;
import java.util.List;

public final class RootSensorListener extends HandlerThread implements Handler.Callback {
    final static int BpmMin = 20;
    final static int BpmMax = 300;
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
            algoritms[i] = new PanTompkins(100);
        }
        start();
        waitUntilReady();
    }

    public static void startRaw() {
        getInstance().bus.register(getInstance());
        getInstance().serviceConnectionImpl.startConnection();
    }

    public static void stopRaw() {
        RealmController.finishLastSession();
        getInstance().handler.sendEmptyMessage(What.EmulateDataStop.ordinal());
        getInstance().serviceConnectionImpl.stopConnection();
        try {
            getInstance().bus.unregister(getInstance());
        } catch (IllegalArgumentException e) {
        }
    }

    public static void generateRaw() {
        getInstance().handler.sendEmptyMessage(What.EmulateDataStart.ordinal());
    }

    public static void generateRaw2() {
        getInstance().handler.sendEmptyMessage(What.EmulateDataPanTomkinsStart.ordinal());
    }

    public static void generateBitalinoRaw() {
        getInstance().bus.register(getInstance());
        getInstance().serviceConnectionImpl.fakeGeneration();
    }

    public static boolean isInProgress() {
        return getInstance().emulating || getInstance().serviceConnectionImpl.isInProgress();
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
        Message m = new Message();
        switch (What.values()[msg.what]) {
            case DataTick:
                if (RealmController.getLastSession() == null) {
                    Log.d("RealmController", "open session");
                    RealmController.save(new Session());
                }
                SensorData.FormattedSensorItem item;
                final SensorData data = SensorData.class.cast(msg.obj);
                final List<SensorPointData> sensorPointData = new ArrayList<>();
                data.resetCursor();
                do {
                    for (int i = 0; i < data.getMaxChannelCount(); i++) {
                        item = data.getItem(i);
                        doAlgorithm(i, item.getValue(), item.getTime());
                        item.setNewValue(algoritms[i].y[3]); // FIXME: y[3] - is it correct ?
                        sensorPointData.add(new SensorPointData()
                                .setChannel(i)
                                .setTime(item.getTime())
                                .setValue(algoritms[i].y[3]));
                    }
                } while (data.nextCursor());
                RealmController.saveList(sensorPointData);
                MainApplication.uiBusPost(data);
                break;
            case EmulateDataPanTomkinsStart:
                m.what = What.EmulateDataPanTomkinsStart.ordinal();
                SensorPointData point = RealmController.getStubSensorPoint(msg.arg1);
                if (point == null) {
                    point = RealmController.getStubSensorPoint(0);
                    m.arg1 = 0;
                } else {
                    m.arg1 = msg.arg1 + 1;
                }
                doAlgorithm(point.getChannel(), point.getValue(), point.getTime());
                handler.sendMessageDelayed(m, FakeDataController.timeOffset);
                break;
            case EmulateDataStart:
                emulating = true;
                RealmController.clearEmulate();
                m.what = What.EmulateData5.ordinal();
                m.arg1 = 0;
                handler.sendMessage(m);
                m = new Message();
                m.what = What.EmulateData15.ordinal();
                m.arg1 = 0;
                handler.sendMessage(m);
                break;
            case EmulateData5:
                if (emulating) {
                    final List<BpmPoint_5_min> fakes5 = RealmController.getStubSessionBpm5();
                    RealmController.save(EmulatedBpm_5Min.createFromBpm(fakes5.get(msg.arg1)));
                    m.what = msg.what;
                    m.arg1 = msg.arg1 + 1;
                    if (m.arg1 == fakes5.size() - 1) {
                        m.arg1 = 0;
                    }
                    handler.sendMessageDelayed(m, (long) AppUtils.getCollapseTimeForPeriod(GraphPeriod.period_5min));
                }
                break;
            case EmulateData15:
                if (emulating) {
                    final List<BpmPoint_15_min> fakes15 = RealmController.getStubSessionBpm15();
                    RealmController.save(EmulatedBpm_15Min.createFromBpm(fakes15.get(msg.arg1)));
                    m.what = msg.what;
                    m.arg1 = msg.arg1 + 1;
                    if (m.arg1 == fakes15.size() - 1) {
                        m.arg1 = 0;
                    }
                    handler.sendMessageDelayed(m, (long) AppUtils.getCollapseTimeForPeriod(GraphPeriod.period_15min));
                }
                break;
            case EmulateDataStop:
                emulating = false;
                handler.removeMessages(What.EmulateData5.ordinal());
                handler.removeMessages(What.EmulateData15.ordinal());
                handler.removeMessages(What.EmulateData30.ordinal());
                handler.removeMessages(What.EmulateDataPanTomkinsStart.ordinal());
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

    private void doAlgorithm(final int channel, double value, long time) {
        algoritms[channel].next(value, time);
        if (PanTompkins.QRS.qrsCurrent.segState == PanTompkins.QRS.SegmentationStatus.FINISHED) {
            //FIXME: it's not work!!!
            Log.d(getClass().getSimpleName(), "found R");
          /*  RRtype = checkQRS(PanTompkins.QRS.qrsCurrent);
            if (time >= 40 && time <= 130) {
                RealmController.save(new RR()
                        .setType(RRtype)
                        .setTime(PanTompkins.QRS.qrsCurrent.rTimestamp));
                Log.e(getClass().getSimpleName(), "save RR type=" + RRtype + "\ttime=" + PanTompkins.QRS.qrsCurrent.rTimestamp);
            } else {
                Log.e(getClass().getSimpleName(), "skip from " + time);
            }
            *//**//*if (!RRtype.equals(RRType.N)) {
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
            }*//**//**/
            //long timeRR = PanTompkins.QRS.qrsCurrent.rTimestamp - PanTompkins.QRS.qrsPrevious.rTimestamp;
            final int bpm = (int) algoritms[channel].heartRateStats.mean;
            if (bpm > BpmMin && bpm < BpmMax) {
                RealmController.save(new RR()
                        .setType(RRType.N)
                        .setTime(PanTompkins.QRS.qrsCurrent.rTimestamp));

                RealmController.save(AppUtils.checkBpmAndGenerateEvent(bpm, time));

                RealmController.saveList(AppUtils.collapseCHeck(time,
                        PanTompkins.QRS.qrsCurrent.rTimestamp - PanTompkins.QRS.qrsPrevious.rTimestamp,
                        bpm,
                        false));
            }
            PanTompkins.QRS.qrsCurrent.segState = PanTompkins.QRS.SegmentationStatus.PROCESSED;
        }
    }

    private enum What {
        DataTick,
        DetectPC,
        EmulateDataStop,
        EmulateDataStart,
        EmulateDataPanTomkinsStart,
        EmulateData5,
        EmulateData15,
        EmulateData30
    }
}
