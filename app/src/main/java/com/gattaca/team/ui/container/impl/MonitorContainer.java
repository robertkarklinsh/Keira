package com.gattaca.team.ui.container.impl;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.gattaca.team.R;
import com.gattaca.team.db.RealmController;
import com.gattaca.team.db.sensor.EmulatedBpm;
import com.gattaca.team.root.MainApplication;
import com.gattaca.team.service.main.RootSensorListener;
import com.gattaca.team.ui.container.ActivityTransferData;
import com.gattaca.team.ui.container.IContainer;
import com.gattaca.team.ui.model.impl.MonitorModel;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public final class MonitorContainer extends IContainer<MonitorModel> implements View.OnClickListener, RealmChangeListener<RealmResults<EmulatedBpm>> {
    private TextView bpm;
    private RealmResults<EmulatedBpm> results;

    public MonitorContainer(final Activity screen) {
        super(screen, R.id.container_monitor_id);
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
        if (RootSensorListener.isInProgress()) {
            bpm.setVisibility(View.VISIBLE);
            results = RealmController.getEmulatedBpm();
            results.addChangeListener(this);
        } else {
            bpm.setVisibility(View.GONE);
        }
    }

    @Override
    public void bindView() {
        getRootView().findViewById(R.id.monitor_main_action_ecg).setOnClickListener(this);
        getRootView().findViewById(R.id.monitor_main_action_pulse).setOnClickListener(this);
        bpm = (TextView) getRootView().findViewById(R.id.monitor_main_bpm_value);
    }

    @Override
    public void changeCurrentVisibilityState(final boolean isHide) {
        /*if (isHide && getRootView().getVisibility() == View.VISIBLE) {
            RootSensorListener.stopRaw();
        } else if (!isHide && getRootView().getVisibility() == View.GONE) {
            RootSensorListener.generateRaw();
        }*/
        if (results != null) {
            results.removeChangeListener(this);
            results = null;
        }
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

    @Override
    public void onChange(RealmResults<EmulatedBpm> element) {
        bpm.setText("" + (int) element.get(element.size() - 1).getValue());
    }
}
