package com.gattaca.team.db.sensor;


import android.text.format.DateUtils;

import java.util.Calendar;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class BpmGreen implements RealmModel {
    @PrimaryKey
    private long time;
    private float valueTop;
    private float valueBottom;


    public static String getNamedFieldTime() {
        return "time";
    }

    public static long createTimeFrom(final long time) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        return cal.get(Calendar.HOUR_OF_DAY) * DateUtils.HOUR_IN_MILLIS +
                cal.get(Calendar.MINUTE) * DateUtils.MINUTE_IN_MILLIS +
                cal.get(Calendar.SECOND) * DateUtils.SECOND_IN_MILLIS +
                cal.get(Calendar.MILLISECOND);

    }

    public long getTime() {
        return time;
    }

    public BpmGreen setTime(long time) {
        this.time = time;
        return this;
    }

    public float getValueTop() {
        return valueTop;
    }

    public BpmGreen setValueTop(float valueTop) {
        this.valueTop = valueTop;
        return this;
    }

    public float getValueBottom() {
        return valueBottom;
    }

    public BpmGreen setValueBottom(float valueBottom) {
        this.valueBottom = valueBottom;
        return this;
    }
}
