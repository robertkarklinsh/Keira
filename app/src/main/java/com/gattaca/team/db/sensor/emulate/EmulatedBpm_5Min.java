package com.gattaca.team.db.sensor.emulate;


import android.util.Log;

import com.gattaca.team.db.sensor.optimizing.BpmPoint_5_min;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class EmulatedBpm_5Min implements RealmModel {
    @PrimaryKey
    private long time;
    private float value;
    private int channel = 0;

    public static String getNamedFieldTime() {
        return "time";
    }

    public static EmulatedBpm_5Min createFromBpm(final BpmPoint_5_min src) {
        final EmulatedBpm_5Min a = new EmulatedBpm_5Min();
        a.setTime(src.getTime());
        a.setChannel(src.getChannel());
        a.setValue(src.getValue());
        Log.i(a.getClass().getSimpleName(), "Emulated point value=" + a.value);
        return a;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getChannel() {
        return channel;
    }

    public EmulatedBpm_5Min setChannel(int channel) {
        this.channel = channel;
        return this;
    }
}
