package com.gattaca.team.db.sensor;


import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class BpmRed implements RealmModel {
    @PrimaryKey
    private long time;
    private float valueTop;
    private float valueBottom;

    public static String getNamedFieldTime() {
        return "time";
    }

    public long getTime() {
        return time;
    }

    public BpmRed setTime(long time) {
        this.time = time;
        return this;
    }

    public float getValueTop() {
        return valueTop;
    }

    public BpmRed setValueTop(float valueTop) {
        this.valueTop = valueTop;
        return this;
    }

    public float getValueBottom() {
        return valueBottom;
    }

    public BpmRed setValueBottom(float valueBottom) {
        this.valueBottom = valueBottom;
        return this;
    }
}
