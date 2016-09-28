package com.gattaca.team.ui.container.impl;

import android.app.Activity;

import com.gattaca.team.R;
import com.gattaca.team.ui.container.IListContainer;
import com.gattaca.team.ui.container.list.item.NotifyCenterListItem;
import com.gattaca.team.ui.model.impl.NotificationCenterModel;

public final class NotificationCenterContainer
        extends IListContainer<NotificationCenterModel, NotifyCenterListItem> {

    public NotificationCenterContainer(final Activity screen) {
        super(screen, R.id.container_notification_center_id);
    }
}
