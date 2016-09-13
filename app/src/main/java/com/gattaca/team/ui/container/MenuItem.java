package com.gattaca.team.ui.container;

import com.gattaca.team.R;
import com.gattaca.team.ui.container.impl.DataBankContainer;
import com.gattaca.team.ui.container.impl.MonitorContainer;
import com.gattaca.team.ui.container.impl.NotificationCenterContainer;
import com.gattaca.team.ui.container.impl.TrackerContainer;

public enum MenuItem {
    Notification(R.mipmap.ic_launcher, R.string.navigation_item_1, new NotificationCenterContainer()),
    Tracker(R.mipmap.ic_launcher, R.string.navigation_item_2, new TrackerContainer()),
    Monitor(R.mipmap.ic_launcher, R.string.navigation_item_3, new MonitorContainer()),
    DataBank(R.mipmap.ic_launcher, R.string.navigation_item_4, new DataBankContainer());

    final int nameId, iconId;
    final IContainer refIRootContainer;

    MenuItem(final int iconId, final int nameId, final IContainer refIRootContainer) {
        this.nameId = nameId;
        this.refIRootContainer = refIRootContainer;
        this.iconId = iconId;
    }

    public int getNameId() {
        return nameId;
    }

    public IContainer getIContainer() {
        return refIRootContainer;
    }

    public int getIconId() {
        return iconId;
    }
}
