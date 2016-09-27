package com.gattaca.team.ui.model.impl;

import com.gattaca.team.db.tracker.Week;
import com.gattaca.team.ui.model.IContainerModel;

public final class TrackerModel implements IContainerModel {
    private Week week;

    public TrackerModel(Week week) {
        this.week = week;
    }

    public Week getWeek(){
        return week;
    }

}
