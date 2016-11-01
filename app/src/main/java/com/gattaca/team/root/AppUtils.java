package com.gattaca.team.root;

import android.text.format.DateUtils;
import android.util.Log;

import com.gattaca.team.annotation.GraphPeriod;
import com.gattaca.team.annotation.ModuleName;
import com.gattaca.team.annotation.NotifyType;
import com.gattaca.team.db.event.NotifyEventObject;
import com.gattaca.team.db.sensor.optimizing.BpmPoint_15_min;
import com.gattaca.team.db.sensor.optimizing.BpmPoint_1_hour;
import com.gattaca.team.db.sensor.optimizing.BpmPoint_30_min;
import com.gattaca.team.db.sensor.optimizing.BpmPoint_5_min;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.realm.RealmModel;

public final class AppUtils {
    private static Bus customBus = new Bus(ThreadEnforcer.ANY);
    private static BpmPoint_1_hour bpmPoint_1_hour = null;
    private static BpmPoint_5_min bpmPoint_5_min = null;
    private static BpmPoint_15_min bpmPoint_15_min = null;
    private static BpmPoint_30_min bpmPoint_30_min = null;

    static long time;
    static long number = 0;
    static Calendar calendar = Calendar.getInstance();

    static {
        time = calendar.getTimeInMillis() * 100;
    }

    public static float convertListToAvrValue(List<Float> values) {
        float value = 0;
        for (float a : values) {
            value += a;
        }
        value /= values.size();
        return value;
    }

    public static float getCollapseTimeForPeriod(@GraphPeriod long period) {
        return period / AppConst.pointsGraph;
    }

    public static long createTimeFrom(final long time) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return cal.get(Calendar.HOUR_OF_DAY) * DateUtils.HOUR_IN_MILLIS +
                cal.get(Calendar.MINUTE) * DateUtils.MINUTE_IN_MILLIS +
                cal.get(Calendar.SECOND) * DateUtils.SECOND_IN_MILLIS +
                cal.get(Calendar.MILLISECOND);

    }

    public static NotifyEventObject checkBpmAndGenerateEvent(final int bpm, final long time) {
        NotifyEventObject a = null;
        if (bpm < 40) {
            a = (new NotifyEventObject()
                    .setModuleNameResId(ModuleName.Monitor)
                    .setEventType(NotifyType.BPM_less_40)
                    .setCount(bpm)
                    .setTime(time))
                    .setPrimaryKey(AppUtils.generateUniqueId());
        } else if (bpm < 45) {
            a = (new NotifyEventObject()
                    .setModuleNameResId(ModuleName.Monitor)
                    .setEventType(NotifyType.BPM_less_50_more_40)
                    .setCount(bpm)
                    .setTime(time))
                    .setPrimaryKey(AppUtils.generateUniqueId());
        } else if (bpm > 125) {
            a = new NotifyEventObject()
                    .setModuleNameResId(ModuleName.Monitor)
                    .setEventType(NotifyType.BPM_more_100)
                    .setCount(bpm)
                    .setTime(time)
                    .setPrimaryKey(AppUtils.generateUniqueId());
        }
        if (a != null) {
            Log.e("UTILS", "generate event " + a.getEventType());
            MainApplication.uiBusPost(a);
//            AppUtils.postToBus(a);
        }
        return a;
    }

    public static List<RealmModel> collapseCHeck(long time, double timeRR, float bpm, final boolean autoCollapse) {
        final List<RealmModel> a = new ArrayList<>();
        if (autoCollapse) {
            if (bpmPoint_5_min != null) {
                bpmPoint_5_min.collapsePoints();
                a.add(bpmPoint_5_min);
                bpmPoint_5_min = null;
            }
            if (bpmPoint_15_min != null) {
                bpmPoint_15_min.collapsePoints();
                a.add(bpmPoint_15_min);
                bpmPoint_15_min = null;
            }
            if (bpmPoint_30_min != null) {
                bpmPoint_30_min.collapsePoints();
                a.add(bpmPoint_30_min);
                bpmPoint_30_min = null;
            }
            if (bpmPoint_1_hour != null) {
                bpmPoint_1_hour.collapsePoints();
                a.add(bpmPoint_1_hour);
                bpmPoint_1_hour = null;
            }
        } else {
            if (bpmPoint_5_min == null) {
                bpmPoint_5_min = new BpmPoint_5_min();
            }
            if (bpmPoint_5_min.addPoint(time, timeRR, bpm)) {
                a.add(bpmPoint_5_min);
                bpmPoint_5_min = null;
            }
            if (bpmPoint_15_min == null) {
                bpmPoint_15_min = new BpmPoint_15_min();
            }
            if (bpmPoint_15_min.addPoint(time, timeRR, bpm)) {
                a.add(bpmPoint_15_min);
                bpmPoint_15_min = null;
            }
            if (bpmPoint_30_min == null) {
                bpmPoint_30_min = new BpmPoint_30_min();
            }
            if (bpmPoint_30_min.addPoint(time, timeRR, bpm)) {
                a.add(bpmPoint_30_min);
                bpmPoint_30_min = null;
            }
            if (bpmPoint_1_hour == null) {
                bpmPoint_1_hour = new BpmPoint_1_hour();
            }
            if (bpmPoint_1_hour.addPoint(time, timeRR, bpm)) {
                a.add(bpmPoint_1_hour);
                bpmPoint_1_hour = null;
            }
        }
        return a;
    }

    public static void registerBus(Object o) {
        customBus.register(o);

    }

    public static void unregisterBus(Object o) {
        customBus.unregister(o);
    }

    public static void postToBus(Object o) {
        customBus.post(o);
    }

    public static long generateUniqueId() {
        long currentTime = calendar.getTimeInMillis() * 100;
        long res;
        if (currentTime == time) {
            res = currentTime + number;
            number ++;
            return res;
        } else {
            time = currentTime;
            number = 0;
            return currentTime;
        }
    }

    public static int generateUniqueDayId(int day, int week) {
        int year = calendar.get(Calendar.YEAR);
        return year*1000 + week * 7 + day;
    }

    public static int getCurrentHour() {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
    public static int getCurrentMinute() {
        return calendar.get(Calendar.MINUTE);
    }
}
