package com.gattaca.team.ui.container;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gattaca.team.ui.model.IListContainerModel;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

public abstract class IListContainer<Model extends IListContainerModel, ListItem extends AbstractItem>
        extends IContainer<Model> implements FastAdapter.OnClickListener<ListItem> {
    private ItemAdapter<ListItem> itemAdapter;

    public IListContainer(final Activity screen, Class<Model> cls, final int id) {
        super(screen, cls, id);
    }

    @Override
    protected void bindView() {
        final FastAdapter<ListItem> fastAdapter = new FastAdapter<>();
        itemAdapter = new ItemAdapter<>();
        final RecyclerView list = (RecyclerView) getRootView().findViewById(android.R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getRootView().getContext()));
        list.setAdapter(itemAdapter.wrap(fastAdapter));
        fastAdapter.withOnClickListener(this);
    }

    @Override
    protected void reDraw() {
        itemAdapter.clear();
        itemAdapter.add(getModel().getListItems());
    }

    @Override
    public boolean onClick(View v, IAdapter<ListItem> adapter, ListItem item, int position) {
        return false;
    }
}
