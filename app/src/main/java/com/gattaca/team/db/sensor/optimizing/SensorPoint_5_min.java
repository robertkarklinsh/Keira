package com.gattaca.team.db.sensor.optimizing;


import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class SensorPoint_5_min implements RealmModel {
    @PrimaryKey
    private long time;
    private float value;
    private int channel = 0;

    public static String getNamedFieldTime() {
        return "time";
    }

    public static String getNamedFieldValue() {
        return "value";
    }

    public static String getNamedFieldChannel() {
        return "channel";
    }

    public long getTime() {
        return time;
    }

    public SensorPoint_5_min setTime(long time) {
        this.time = time;
        return this;
    }

    public float getValue() {
        return value;
    }

    public SensorPoint_5_min setValue(float value) {
        this.value = value;
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
