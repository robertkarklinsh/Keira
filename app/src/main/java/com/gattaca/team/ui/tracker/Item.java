package com.gattaca.team.ui.tracker;

import android.support.v7.widget.RecyclerView;

import com.gattaca.team.ui.tracker.data.TrackerItemContainer;
import com.gattaca.team.R;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;

import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Artem on 14.09.2016.
 */
public abstract class Item extends AbstractItem<Item, ViewHoldersCollection.DrugItemViewHolder> {

    protected RecyclerView.Adapter adapter;
    protected TrackerItemContainer itemContainer;
    static GregorianCalendar calendar = new GregorianCalendar();

    public Item withItemContainer(TrackerItemContainer itemContainer) {
        this.itemContainer = itemContainer;
        return this;
    }




    @Override
    public int getType() {
        return R.id.tracker_item;
    }

    abstract void bindCustomView(ViewHoldersCollection.DrugItemViewHolder holder, List payloads);

    @Override
    public int getLayoutRes() {
        return R.layout.tracker_list_entity;
    }

    @Override
    public void bindView(ViewHoldersCollection.DrugItemViewHolder holder, List payloads) {
        super.bindView(holder, payloads);
        bindCustomView(holder, payloads);

    }

    @Override
    public ViewHolderFactory<? extends ViewHoldersCollection.DrugItemViewHolder> getFactory() {
        return ViewHoldersCollection.DrugItemViewHolder::new;
    }
}
