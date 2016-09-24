package com.gattaca.bitalinoecgchart.tracker.model;

import com.gattaca.bitalinoecgchart.tracker.data.ItemType;
import com.gattaca.bitalinoecgchart.tracker.data.TrackerItemContainer;
import com.gattaca.team.R;

import io.realm.RealmObject;

/**
 * Created by Artem on 12.09.2016.
 */
public class Measurement extends RealmObject implements TrackerItemContainer{
    private boolean completed = false;
    private String name ;
    private String duration;
    private String units;

    public boolean isCompleted() {
        return completed;
    }

    @Override
    public ItemType getType() {
        return ItemType.MEASUREMENT;
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
        return duration + " " + units;
    }
}
