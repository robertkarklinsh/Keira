package com.gattaca.team.ui.container.impl;

import android.app.Activity;

import com.gattaca.team.R;
import com.gattaca.team.ui.container.IContainer;
import com.gattaca.team.ui.model.impl.DataBankModel;

public final class DataBankContainer extends IContainer<DataBankModel> {
    public DataBankContainer(final Activity screen) {
        super(screen, DataBankModel.class, R.id.container_data_bank_id);
    }

    @Override
    protected void bindView() {
        //TODO: implements
    }

    @Override
    protected void reDraw() {
        //TODO: implements
    }
}
