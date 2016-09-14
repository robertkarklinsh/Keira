package com.gattaca.team.ui.container.impl;

import android.app.Activity;

import com.gattaca.team.R;
import com.gattaca.team.ui.container.IContainer;
import com.gattaca.team.ui.model.impl.TrackerModel;

public final class TrackerContainer extends IContainer<TrackerModel> {
    public TrackerContainer(final Activity screen) {
        super(screen, TrackerModel.class, R.id.container_tracker_id);
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
