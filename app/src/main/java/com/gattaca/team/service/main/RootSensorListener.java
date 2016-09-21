package com.gattaca.team.service.main;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.gattaca.team.root.MainApplication;
import com.gattaca.team.service.IServiceConnection;
import com.gattaca.team.service.analysis.PanTompkins;
import com.gattaca.team.service.bitalino.BitalinoConnection;
import com.gattaca.team.service.events.NotifyEvent;
import com.gattaca.team.service.events.NotifyType;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import java.util.List;

public final class RootSensorListener extends HandlerThread implements Handler.Callback {
    private static RootSensorListener instance;
    private Bus bus = new Bus(ThreadEnforcer.ANY);
    private IServiceConnection serviceConnectionImpl = new BitalinoConnection();
    private Handler handler;
    private PanTompkins[] algoritms;

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
        getInstance().serviceConnectionImpl.startConnection();
    }

    public static void stopRaw() {
        getInstance().serviceConnectionImpl.stopConnection();
    }

    public static void generateRaw() {
        getInstance().serviceConnectionImpl.fakeGeneration();
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
        for (PanTompkins.QRS qrs : list) {
            event.addTime(qrs.rTimestamp);
        }
        MainApplication.uiBusPost(event);
    }

    private synchronized void waitUntilReady() {
        this.handler = new Handler(this.getLooper(), this);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (What.values()[msg.what]) {
            case DataTick:
                SensorData.FormattedSensorItem item;
                final SensorData data = SensorData.class.cast(msg.obj);
                data.resetCursor();
                do {
                    for (int i = 0; i < data.getMaxChannelCount(); i++) {
                        item = data.getItem(i);
                        algoritms[i].next(item.getValue(), item.getTime());
                        item.setNewValue(algoritms[i].y[3]); // FIXME: y[3] - is it correct ?

                        if (PanTompkins.QRS.qrsCurrent.segState == PanTompkins.QRS.SegmentationStatus.FINISHED) {
                            if (checkQRS(PanTompkins.QRS.qrsCurrent)) {
                                // TODO: add QRS time stump to DB for check its time count
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
                            }
                        }
                    }
                } while (data.nextCursor());
                MainApplication.uiBusPost(data);
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
        /*
        final StringBuilder builder = new StringBuilder();
        for (int i = 0; i < data.countTicks(); i++) {
            builder.append("\ntimestump=").append(data.getTimeStump(i));
            for (int j = 0; j < data.getChannels(); j++) {
                builder.append("#").append(j).append("=").append(data.getVoltByChannel(i, j)).append("   ");
            }
            builder.append("\n");
        }
        Log.i(getClass().getSimpleName(), builder.toString());*/
    }

    boolean checkQRS(PanTompkins.QRS current) {
        if (current == null) return false;
        switch (current.classification) {
            case APC:
            case APC_ABERRANT:
            case PVC:
            case PVC_ABERRANT:
            case PREMATURE:
                return true;
            default:
                return false;
        }
    }

    private enum What {
        DataTick,
        DetectPC,
    }
}
