package com.gattaca.bitalinoecgchart.tracker.db;

import com.gattaca.bitalinoecgchart.tracker.data.ItemType;
import com.gattaca.bitalinoecgchart.tracker.data.TrackerItemContainer;
import com.gattaca.team.R;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by Artem on 12.09.2016.
 */
public class Task extends RealmObject implements TrackerItemContainer{
    String name;
    int time;
    String units;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    private RealmList<TaskAction> actions;

    public RealmList<TaskAction> getActions() {
        return actions;
    }
    @Override
    public ItemType getType() {
        return ItemType.TASK;
    }

    @Override
    public int getIcon() {
        return R.drawable.walking_icon;
    }

    @Override
    public String getBlackText() {
        return name;
    }

    @Override
    public String getGrayText() {
        return time + " " + units;
    }
}
