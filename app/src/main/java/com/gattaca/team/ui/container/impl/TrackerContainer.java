package com.gattaca.team.ui.container.impl;

import android.app.Activity;

import android.widget.RelativeLayout;

import com.gattaca.team.R;
import com.gattaca.team.root.MainApplication;
import com.gattaca.team.ui.container.IContainer;
import com.gattaca.team.ui.container.MainMenu;
import com.gattaca.team.ui.model.impl.TrackerModel;

public final class TrackerContainer extends IContainer<TrackerModel> {
    public TrackerContainer(final Activity screen) {
        super(screen, TrackerModel.class, R.id.container_tracker_id);
    }

    @Override
    public int getMenuItemActions() {
        return R.menu.tracker_toolbar_actions;
    }

    @Override
    public void onMenuItemSelected(final int id) {
        switch (id) {
            case R.id.toolbar_action_change_week:
                MainApplication.showToastNotImplemented();
                break;
            case R.id.toolbar_action_open_monitor:
                MainApplication.uiBusPost(MainMenu.Monitor);
                break;
        }
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
