package com.gattaca.team.db.event;


import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class NotifyEventTime implements RealmModel {
    private long time;

    public static String getNamedFieldTime() {
        return NotifyEventTime.class.getSimpleName() + ".time";
    }

    public long getTime() {
        return this.time;
    }

    public NotifyEventTime setTime(long time) {
        this.time = time;
        return this;
    }
}
