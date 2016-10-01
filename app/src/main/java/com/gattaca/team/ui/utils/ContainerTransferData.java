package com.gattaca.team.ui.utils;

import com.gattaca.team.ui.model.IListContainerModel;

public final class ContainerTransferData {
    final private MainMenu menuItemForOpen;
    final private IListContainerModel modelForSubContainer;

    public ContainerTransferData(MainMenu menuItemForOpen) {
        this(menuItemForOpen, null);
    }

    public ContainerTransferData(MainMenu menuItemForOpen, IListContainerModel modelForSubContainer) {
        this.menuItemForOpen = menuItemForOpen;
        this.modelForSubContainer = modelForSubContainer;
    }

    public MainMenu getMenuItemForOpen() {
        return this.menuItemForOpen;
    }

    public IListContainerModel getModelForSubContainer() {
        return modelForSubContainer;
    }
}
