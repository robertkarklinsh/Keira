package com.gattaca.team.db.event;


import com.gattaca.team.annotation.ModuleName;
import com.gattaca.team.annotation.NotifyType;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class NotifyEventObject implements RealmModel {
    private
    @PrimaryKey
    long time;
    private float count;
    private boolean fake = true;
    private
    @ModuleName
    int moduleNameResId;
    private
    @NotifyType
    int eventType;

    public static String getNamedFieldTime() {
        return "time";
    }

    public static String getNamedFieldCount() {
        return "count";
    }

    public static String getNamedFieldType() {
        return "eventType";
    }

    public static String getNamedFieldFake() {
        return "fake";
    }


    public NotifyEventObject realData() {
        this.fake = false;
        return this;
    }

    public long getTime() {
        return this.time;
    }

    public NotifyEventObject setTime(long time) {
        this.time = time;
        return this;
    }

    public
    @ModuleName
    int getModuleNameResId() {
        return this.moduleNameResId;
    }

    public NotifyEventObject setModuleNameResId(@ModuleName int moduleNameResId) {
        this.moduleNameResId = moduleNameResId;
        return this;
    }

    public float getCount() {
        return this.count;
    }

    public NotifyEventObject setCount(float count) {
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
