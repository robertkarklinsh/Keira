package com.gattaca.team.ui.model.impl;

import com.gattaca.team.db.sensor.Session;
import com.gattaca.team.ui.container.list.item.TrackerMeasureListItem;
import com.gattaca.team.ui.model.IListContainerModel;

import java.util.List;

public final class DataBankModel extends IListContainerModel<Session, TrackerMeasureListItem> {

    public DataBankModel(List<Session> list) {
        super(list);
    }

    @Override
    protected void convert() {
        for (Session item : getDataItems()) {
            addListItem(new TrackerMeasureListItem());
        }
    }
}
