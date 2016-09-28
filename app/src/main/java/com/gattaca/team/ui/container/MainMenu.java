package com.gattaca.team.ui.container;

import com.gattaca.team.R;

public enum MainMenu {
    Notification(R.mipmap.ic_launcher, R.string.navigation_item_1),
    Tracker(R.mipmap.ic_launcher, R.string.navigation_item_2),
    Monitor(R.mipmap.ic_launcher, R.string.navigation_item_3),
    DataBank(R.mipmap.ic_launcher, R.string.navigation_item_4),
    Settings(R.mipmap.ic_launcher, R.string.navigation_item_5);

    final int nameId, iconId;

    MainMenu(final int iconId, final int nameId) {
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
