package com.gattaca.team.db.sensor;


import com.gattaca.team.annotation.RRType;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class RR implements RealmModel {
    private long time;
    private
    @RRType
    String type = RRType.N;

    public static String getNamedFieldTime() {
        return RR.class.getSimpleName() + ".time";
    }

    public static String getNamedFieldType() {
        return RR.class.getSimpleName() + ".type";
    }

    public
    @RRType
    String getType() {
        return type;
    }

    public RR setType(@RRType String type) {
        this.type = type;
        return this;
    }

    public long getTime() {
        return time;
    }

    public RR setTime(long time) {
        this.time = time;
        return this;
    }

    @Override
    public String toString() {
        return getNamedFieldTime() + "=" + getTime() + "\t"
                + getNamedFieldType() + "=" + getType();
    }
}
