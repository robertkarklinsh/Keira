package com.gattaca.team.ui.container.impl;

import android.app.Activity;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
        TrackerModel model = this.getModel();
        RelativeLayout relativeLayout = (RelativeLayout)this.getRootView();
//        rv.addView();

        RecyclerView rv = (RecyclerView) relativeLayout.findViewById(R.id.my_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setItemAnimator(new DefaultItemAnimator());



    }

    @Override
    public void bindView() {
        //TODO: implements

    }
}
