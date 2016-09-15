package com.gattaca.bitalinoecgchart.tracker.ui;

import com.gattaca.bitalinoecgchart.tracker.ViewHoldersCollection;
import com.gattaca.team.R;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;

import java.util.List;

/**
 * Created by Artem on 14.09.2016.
 */
public class FooterItem extends AbstractItem<FooterItem, ViewHoldersCollection.ExpandViewHolder> {

    @Override
    public int getType() {
        return R.id.tracker_footer;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.tracker_footer;
    }

    @Override
    public void bindView(ViewHoldersCollection.ExpandViewHolder holder, List payloads) {
        super.bindView(holder, payloads);
    }

    @Override
    public ViewHolderFactory<? extends ViewHoldersCollection.ExpandViewHolder> getFactory() {
        return ViewHoldersCollection.ExpandViewHolder::new;
    }
}
