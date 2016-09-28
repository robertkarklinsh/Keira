package com.gattaca.team.db.tracker;

import com.gattaca.bitalinoecgchart.tracker.data.ItemType;
import com.gattaca.bitalinoecgchart.tracker.data.TrackerItemContainer;
import com.gattaca.team.R;

import io.realm.RealmModel;
import io.realm.annotations.RealmClass;

/**
 * Created by Artem on 12.09.2016.
 */

@RealmClass
public class PressureMeasurement implements RealmModel, TrackerItemContainer{
    private boolean completed = false;
    private String name ;
    private int hours;
    private int minutes;

    private int pulse;
    private int systolic;
    private int diastolic;

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public int getSystolic() {
        return systolic;
    }

    public void setSystolic(int systolic) {
        this.systolic = systolic;
    }

    public int getDiastolic() {
        return diastolic;
    }

    public void setDiastolic(int diastolic) {
        this.diastolic = diastolic;
    }

    public String getName() {
        return name;
    }



    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean isCompleted() {
        return completed;
    }

    @Override
    public ItemType getType() {
        return ItemType.PULSE_MEASUREMENT;
    }

    @Override
    public int getIcon() {
        return R.drawable.monitor_icon;
    }

    @Override
    public String getBlackText() {
        return name;
    }

    @Override
    public String getGrayText() {
        return "";
    }
}
