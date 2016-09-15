package com.gattaca.bitalinoecgchart.tracker.ui;

import android.widget.TextView;

import com.gattaca.bitalinoecgchart.tracker.ViewHoldersCollection;
import com.gattaca.team.R;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;

import java.util.List;

/**
 * Created by Artem on 14.09.2016.
 */
public class HeaderItem extends AbstractItem<HeaderItem, ViewHoldersCollection.HeaderViewHolder> {
    private int position = 0;
    Boolean[] expanded;
    String name;

    public HeaderItem withExpanded(Boolean[] expanded){
        this.expanded = expanded;
        return this;
    }

    public HeaderItem withName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public int getType() {
        return R.id.tracker_header;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.tracker_header;
    }

    @Override
    public void bindView(ViewHoldersCollection.HeaderViewHolder holder, List payloads) {
        super.bindView(holder, payloads);

        TextView textView = (TextView) ((ViewHoldersCollection.HeaderViewHolder) holder).mView.findViewById(R.id.tracker_header_text);
        textView.setText(name);
    }

    @Override
    public ViewHolderFactory<? extends ViewHoldersCollection.HeaderViewHolder> getFactory() {
        return ViewHoldersCollection.HeaderViewHolder::new;
    }
}
