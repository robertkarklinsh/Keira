package com.gattaca.team.ui.container;

import com.gattaca.team.R;

public enum MenuItem {
    Notification(R.mipmap.ic_launcher, R.string.navigation_item_1),
    Tracker(R.mipmap.ic_launcher, R.string.navigation_item_2),
    Monitor(R.mipmap.ic_launcher, R.string.navigation_item_3),
    DataBank(R.mipmap.ic_launcher, R.string.navigation_item_4);

    final int nameId, iconId;

    MenuItem(final int iconId, final int nameId) {
        this.nameId = nameId;
        this.iconId = iconId;
    }

    public int getNameId() {
        return nameId;
    }

    public int getIconId() {
        return iconId;
    }
}
