package com.gattaca.team.db.sensor.optimizing;


import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class SensorPoint_1_hour implements RealmModel {
    @PrimaryKey
    private long time;
    private double value;
    private int channel = 0;

    public static String getNamedFieldTime() {
        return SensorPoint_1_hour.class.getSimpleName() + ".time";
    }

    public static String getNamedFieldValue() {
        return SensorPoint_1_hour.class.getSimpleName() + ".value";
    }

    public static String getNamedFieldChannel() {
        return SensorPoint_1_hour.class.getSimpleName() + ".channel";
    }

    public long getTime() {
        return time;
    }

    public SensorPoint_1_hour setTime(long time) {
        this.time = time;
        return this;
    }

    public double getValue() {
        return value;
    }

    public SensorPoint_1_hour setValue(float value) {
        this.value = value;
        return this;
    }

    public int getChannel() {
        return channel;
    }

    public SensorPoint_1_hour setChannel(int channel) {
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
