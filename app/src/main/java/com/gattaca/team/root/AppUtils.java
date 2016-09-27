package com.gattaca.team.root;

import android.text.format.DateUtils;

import com.gattaca.team.annotation.GraphPeriod;

import java.util.Calendar;
import java.util.List;

public final class AppUtils {

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
}
