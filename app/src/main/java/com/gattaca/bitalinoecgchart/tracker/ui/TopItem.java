package com.gattaca.bitalinoecgchart.tracker.ui;

import com.gattaca.bitalinoecgchart.tracker.ViewHoldersCollection;
import com.gattaca.team.R;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;

import java.util.List;

/**
 * Created by Artem on 14.09.2016.
 */
public class TopItem extends AbstractItem<TopItem, ViewHoldersCollection.TopViewHolder> {

    int postion = 0;
    @Override
    public int getType() {
        return R.id.tracker_top;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.tracker_top;
    }

    @Override
    public void bindView(ViewHoldersCollection.TopViewHolder holder, List payloads) {
        super.bindView(holder, payloads);
//        holder.tabLayout.add
//        holder.get
    }

    @Override
    public ViewHolderFactory<? extends ViewHoldersCollection.TopViewHolder> getFactory() {
//        return new ViewHolderFactory<ViewHoldersCollection.TopViewHolder>() {
//            @Override
//            public ViewHoldersCollection.TopViewHolder create(View v) {
//                return new ViewHoldersCollection.TopViewHolder();
//            }
//        };


//        return  (View v) -> new ViewHoldersCollection.TopViewHolder(v);

        return ViewHoldersCollection.TopViewHolder::new;
    }
}
