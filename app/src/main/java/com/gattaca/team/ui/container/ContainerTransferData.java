package com.gattaca.team.ui.container;

import com.gattaca.team.ui.model.IContainerModel;

public final class ContainerTransferData {
    final private MainMenu menuItemForOpen;
    final private IContainerModel modelForSubContainer;

    public ContainerTransferData(MainMenu menuItemForOpen) {
        this(menuItemForOpen, null);
    }

    public ContainerTransferData(MainMenu menuItemForOpen, IContainerModel modelForSubContainer) {
        this.menuItemForOpen = menuItemForOpen;
        this.modelForSubContainer = modelForSubContainer;
    }

    public MainMenu getMenuItemForOpen() {
        return this.menuItemForOpen;
    }

    public IContainerModel getModelForSubContainer() {
        return modelForSubContainer;
    }
}
