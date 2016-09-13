package com.gattaca.team.ui.container.impl;

import android.widget.RelativeLayout;

import com.gattaca.team.R;
import com.gattaca.team.ui.container.IContainer;
import com.gattaca.team.ui.model.impl.TrackerModel;

public final class TrackerContainer extends IContainer<TrackerModel> {
    public TrackerContainer() {
        super(TrackerModel.class, R.id.container_tracker_id);
    }

    @Override
    protected void reDraw() {
        //TODO: implements
        TrackerModel model = (TrackerModel) this.getModel();
        RelativeLayout rv = (RelativeLayout)this.getRootView();
//        rv.addView();


    }

    @Override
    public void bindView() {
        //TODO: implements

    }
}
