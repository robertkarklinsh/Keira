package com.gattaca.team.ui.container.impl;

import android.app.Activity;

import com.gattaca.team.R;
import com.gattaca.team.ui.container.IListContainer;
import com.gattaca.team.ui.container.list.item.DataBankListItem;
import com.gattaca.team.ui.model.impl.DataBankModel;

public final class DataBankContainer extends IListContainer<DataBankModel, DataBankListItem> {
    public DataBankContainer(final Activity screen) {
        super(screen, R.id.container_data_bank_id);
    }


}
