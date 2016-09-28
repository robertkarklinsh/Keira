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
public class PulseMeasurement implements RealmModel, TrackerItemContainer{
    private boolean completed = false;
    private String name ;
    private int duration;
    private String units;

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public String getUnits() {
        return units;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setUnits(String units) {
        this.units = units;
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
        return R.drawable.cardiogram_icon;
    }

    @Override
    public String getBlackText() {
        return name;
    }

    @Override
    public String getGrayText() {
        return duration + " " + units;
    }
}
