package com.gattaca.team.ui.model;

import java.util.ArrayList;
import java.util.List;

public abstract class IListContainerModel<DataItem, ListItem> implements IContainerModel {
    final private List<DataItem> dataItems;
    final private List<ListItem> listItems;

    public IListContainerModel(List<DataItem> dataItems) {
        this.dataItems = new ArrayList<>(dataItems);
        this.listItems = new ArrayList<>(dataItems.size());
        convert();
    }

    protected abstract void convert();

    public final List<DataItem> getDataItems() {
        return this.dataItems;
    }

    public final List<ListItem> getListItems() {
        return this.listItems;
    }

    public boolean isEmpty() {
        return dataItems.isEmpty();
    }

    protected void addListItem(ListItem listItem) {
        this.listItems.add(listItem);
    }
}
