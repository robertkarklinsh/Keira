package com.gattaca.team.db.sensor;


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
