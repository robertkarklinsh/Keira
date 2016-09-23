package com.gattaca.team.ui.container.impl;

import android.app.Activity;

import com.gattaca.team.R;
import com.gattaca.team.ui.container.IListContainer;
import com.gattaca.team.ui.container.list.DataBankListItem;
import com.gattaca.team.ui.model.impl.DataBankModel;

public final class DataBankContainer extends IListContainer<DataBankModel, DataBankListItem> {
    public DataBankContainer(final Activity screen) {
        super(screen, DataBankModel.class, R.id.container_data_bank_id);
    }
/*
    @Override
    public int getMenuItemActions() {
        return R.menu.data_bank_toolbar_actions;
    }

    @Override
    public void onMenuItemSelected(final int id) {
        switch (id) {
            case R.id.toolbar_action_add_new:

                break;
        }
    }*/
}
