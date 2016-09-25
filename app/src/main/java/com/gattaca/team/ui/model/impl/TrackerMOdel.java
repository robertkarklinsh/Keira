package com.gattaca.team.ui.model.impl;

import com.gattaca.bitalinoecgchart.tracker.db.Week;
import com.gattaca.team.ui.model.IContainerModel;

public final class TrackerModel implements IContainerModel {
    private Week week;
    public Week getWeek(){
        return week;
    }

}
