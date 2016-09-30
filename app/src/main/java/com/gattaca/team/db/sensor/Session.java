package com.gattaca.team.db.sensor;


import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class Session implements RealmModel {
    @PrimaryKey
    private long timeStart;
    private long timeFinish = -1;

    public static String getNamedFieldTimeStart() {
        return "timeStart";
    }

    public static String getNamedFieldTimeFinish() {
        return "timeFinish";
    }

    public long getTimeStart() {
        return timeStart;
    }

    public Session setTimeStart(long timeStart) {
        this.timeStart = timeStart;
        return this;
    }

    public long getTimeFinish() {
        return timeFinish;
    }

    public Session setTimeFinish(long timeFinish) {
        this.timeFinish = timeFinish;
        return this;
    }


    @Override
    public String toString() {
        return getNamedFieldTimeStart() + "=" + getTimeStart() + "\t"
                + getNamedFieldTimeFinish() + "=" + getTimeFinish();
    }
}
