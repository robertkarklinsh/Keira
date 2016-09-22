package com.gattaca.team.db.event;


import com.gattaca.team.annotation.NotifyType;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

@RealmClass
public class NotifyEventObject implements RealmModel {
    private RealmList<NotifyEventTime> times = new RealmList<>();
    private int count;
    private boolean fake = true;
    private
    @NotifyType
    int eventType;

    public static String getNamedFieldTime() {
        return NotifyEventObject.class.getSimpleName() + ".times";
    }

    public static String getNamedFieldCount() {
        return NotifyEventObject.class.getSimpleName() + ".count";
    }

    public static String getNamedFieldType() {
        return NotifyEventObject.class.getSimpleName() + ".eventType";
    }

    public static String getNamedFieldFake() {
        return NotifyEventObject.class.getSimpleName() + ".fake";
    }


    public NotifyEventObject realData() {
        this.fake = false;
        return this;
    }

    public RealmList<NotifyEventTime> getTime() {
        return this.times;
    }

    public NotifyEventObject setTime(long time) {
        this.times.add(new NotifyEventTime().setTime(time));
        return this;
    }

    public int getCount() {
        return this.count;
    }

    public NotifyEventObject setCount(int count) {
        this.count = count;
        return this;
    }

    public
    @NotifyType
    int getEventType() {
        return eventType;
    }

    public NotifyEventObject setEventType(@NotifyType int eventType) {
        this.eventType = eventType;
        return this;
    }

    @Override
    public String toString() {
        return getNamedFieldTime() + "=" + getTime() + "\t"
                + getNamedFieldCount() + "=" + getCount() + "\t"
                + getNamedFieldType() + "=" + getEventType();
    }
}
