package com.gattaca.team.root;

import android.text.format.DateUtils;

import com.gattaca.team.annotation.GraphPeriod;

import java.util.Calendar;
import java.util.List;

public final class AppUtils {

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
