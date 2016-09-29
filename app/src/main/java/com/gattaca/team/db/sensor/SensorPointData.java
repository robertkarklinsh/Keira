package com.gattaca.team.db.sensor;


import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class SensorPointData implements RealmModel {
    @PrimaryKey
    private long time;
    private double value;
    private int channel = 0;
    private int sample = 0;

    public static String getNamedFieldTime() {
        return "time";
    }

    public static String getNamedFieldSample() {
        return "sample";
    }

    public static String getNamedFieldChannel() {
        return SensorPointData.class.getSimpleName() + ".channel";
    }

    public long getTime() {
        return time;
    }

    public SensorPointData setTime(long time) {
        this.time = time;
        return this;
    }

    public double getValue() {
        return value;
    }

    public SensorPointData setValue(double value) {
        this.value = value;
        return this;
    }

    public int getChannel() {
        return channel;
    }

    public SensorPointData setChannel(int channel) {
        this.channel = channel;
        return this;
    }

    public RealmModel convert() {
        return this;
    }

    public int getSample() {
        return sample;
    }

    public SensorPointData setSample(int sample) {
        this.sample = sample;
        return this;
    }
}
