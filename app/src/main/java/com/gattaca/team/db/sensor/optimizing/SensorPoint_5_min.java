package com.gattaca.team.db.sensor.optimizing;


import java.util.List;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class SensorPoint_5_min implements RealmModel {
    @PrimaryKey
    private long time;
    private double value;
    private int channel = 0;

    public static String getNamedFieldTime() {
        return SensorPoint_5_min.class.getSimpleName() + ".time";
    }

    public static String getNamedFieldValue() {
        return SensorPoint_5_min.class.getSimpleName() + ".value";
    }

    public static String getNamedFieldChannel() {
        return SensorPoint_5_min.class.getSimpleName() + ".channel";
    }

    public long getTime() {
        return time;
    }

    public SensorPoint_5_min setTime(long time) {
        this.time = time;
        return this;
    }

    public double getValue() {
        return value;
    }

    public SensorPoint_5_min setValue(List<Float> value) {
        for (float bpm : value) {
            this.value += bpm;
        }
        this.value /= value.size();
        return this;
    }

    public int getChannel() {
        return channel;
    }

    public SensorPoint_5_min setChannel(int channel) {
        this.channel = channel;
        return this;
    }

    @Override
    public String toString() {
        return getNamedFieldTime() + "=" + getTime() + "\t"
                + getNamedFieldChannel() + "=" + getChannel() + "\t"
                + getNamedFieldValue() + "=" + getValue();
    }
}
