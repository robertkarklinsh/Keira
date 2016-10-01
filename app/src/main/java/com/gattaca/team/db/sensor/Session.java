package com.gattaca.team.db.sensor;


import io.realm.RealmModel;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class Session implements RealmModel {
    @Ignore
    public final static int LAST_TIME_FLAG = -1;
    @PrimaryKey
    private long timeStart = System.currentTimeMillis();
    private long timeFinish = LAST_TIME_FLAG;

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

    public void finishSession() {
        this.timeFinish = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return getNamedFieldTimeStart() + "=" + getTimeStart() + "\t"
                + getNamedFieldTimeFinish() + "=" + getTimeFinish();
    }
}
