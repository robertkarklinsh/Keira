package com.gattaca.team.ui.container.impl;

import android.app.Activity;

import com.gattaca.team.R;
import com.gattaca.team.root.MainApplication;
import com.gattaca.team.ui.container.IContainer;
import com.gattaca.team.ui.model.impl.MonitorModel;

public final class MonitorContainer extends IContainer<MonitorModel> {
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
        //TODO: implements

    }

    @Override
    public void changeCurrentVisibilityState(final boolean isHide) {
        super.changeCurrentVisibilityState(isHide);
        if (isHide) {
            MainApplication.getServiceConnectionImpl().stopConnection();
        } else {
            MainApplication.getServiceConnectionImpl().fakeGeneration();
        }
    }
}
