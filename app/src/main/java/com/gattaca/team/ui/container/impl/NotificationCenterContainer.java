package com.gattaca.team.ui.container.impl;

import android.app.Activity;

import com.gattaca.team.R;
import com.gattaca.team.ui.container.IContainer;
import com.gattaca.team.ui.model.impl.NotificationCenterModel;

public final class NotificationCenterContainer extends IContainer<NotificationCenterModel> {
    public NotificationCenterContainer(final Activity screen) {
        super(screen, NotificationCenterModel.class, R.id.container_notification_center_id);
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
