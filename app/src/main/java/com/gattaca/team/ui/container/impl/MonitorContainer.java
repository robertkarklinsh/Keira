package com.gattaca.team.ui.container.impl;

import android.app.Activity;
import android.view.View;

import com.gattaca.team.R;
import com.gattaca.team.root.MainApplication;
import com.gattaca.team.ui.container.ActivityTransferData;
import com.gattaca.team.ui.container.IContainer;
import com.gattaca.team.ui.model.impl.MonitorModel;

public final class MonitorContainer extends IContainer<MonitorModel> implements View.OnClickListener {
    public MonitorContainer(final Activity screen) {
        super(screen, MonitorModel.class, R.id.container_monitor_id);
    }

    @Override
    public int getMenuItemActions() {
        return R.menu.monitor_toolbar_actions;
    }

    @Override
    public void onMenuItemSelected(final int id) {
        switch (id) {
            case R.id.toolbar_action_change_graph_view:
                MainApplication.showToastNotImplemented();
                break;
        }
    }

    @Override
    protected void reDraw() {
        //TODO: implements
    }

    @Override
    public void bindView() {
        getRootView().findViewById(R.id.monitor_main_action_ecg).setOnClickListener(this);
        getRootView().findViewById(R.id.monitor_main_action_pulse).setOnClickListener(this);
    }

    @Override
    public void changeCurrentVisibilityState(final boolean isHide) {
        /*if (isHide && getRootView().getVisibility() == View.VISIBLE) {
            RootSensorListener.stopRaw();
        } else if (!isHide && getRootView().getVisibility() == View.GONE) {
            RootSensorListener.generateRaw();
        }*/
        super.changeCurrentVisibilityState(isHide);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.monitor_main_action_ecg:
                MainApplication.uiBusPost(new ActivityTransferData(ActivityTransferData.AvailableActivity.ADT));
                break;
            case R.id.monitor_main_action_pulse:
                MainApplication.uiBusPost(new ActivityTransferData(ActivityTransferData.AvailableActivity.BPM));
                break;
        }
    }
}
