package com.gattaca.team.ui.container;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gattaca.team.ui.model.IListContainerModel;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;
import com.mikepenz.fastadapter.items.AbstractItem;

public abstract class IListContainer<Model extends IListContainerModel, ListItem extends AbstractItem>
        extends IContainer<Model> {

    private final FastItemAdapter<ListItem> fastAdapter = new FastItemAdapter<>();

    public IListContainer(final Activity screen, Class<Model> cls, final int id) {
        super(screen, cls, id);
    }

    @Override
    public void bindView() {
        final RecyclerView list = (RecyclerView) getRootView().findViewById(android.R.id.list);
        list.setLayoutManager(new LinearLayoutManager(getRootView().getContext()));
        list.setAdapter(fastAdapter);
    }

    @Override
    protected void reDraw() {
        fastAdapter.clear();
        fastAdapter.add(getModel().getListItems());
    }
}
