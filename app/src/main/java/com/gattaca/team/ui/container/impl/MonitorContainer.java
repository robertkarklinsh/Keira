package com.gattaca.team.ui.container.impl;

import android.app.Activity;

import com.gattaca.team.R;
import com.gattaca.team.ui.container.IContainer;
import com.gattaca.team.ui.model.impl.MonitorModel;

public final class MonitorContainer extends IContainer<MonitorModel> {
    public MonitorContainer(final Activity screen) {
        super(screen, MonitorModel.class, R.id.container_monitor_id);
    }

    @Override
    protected void reDraw() {
        //TODO: implements
    }

    @Override
    public void bindView() {
        //TODO: implements

    }
}
