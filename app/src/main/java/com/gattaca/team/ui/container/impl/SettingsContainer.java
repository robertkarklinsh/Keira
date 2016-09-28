package com.gattaca.team.ui.container.impl;

import android.app.Activity;
import android.view.View;
import android.widget.Button;

import com.gattaca.team.R;
import com.gattaca.team.annotation.SessionState;
import com.gattaca.team.service.main.RootSensorListener;
import com.gattaca.team.ui.container.IContainer;
import com.gattaca.team.ui.model.impl.MonitorModel;

public final class SettingsContainer extends IContainer<MonitorModel> implements View.OnClickListener {
    Button changeState;

    public SettingsContainer(final Activity screen) {
        super(screen, R.id.container_settings_id);
    }

    @Override
    public void bindView() {
        changeState = (Button) getRootView().findViewById(R.id.stateChange);
        changeState.setOnClickListener(this);
    }

    @Override
    protected void reDraw() {
        changeText(RootSensorListener.isInProgress());
    }

    @Override
    public void onClick(View v) {
        changeText(!changeState.isSelected());
    }

    void changeText(boolean state) {
        changeState.setSelected(state);
        changeState.setText(!state ? SessionState.Start : SessionState.Finish);
    }
}
