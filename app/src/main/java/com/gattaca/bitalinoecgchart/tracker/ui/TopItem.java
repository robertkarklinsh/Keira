package com.gattaca.bitalinoecgchart.tracker.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gattaca.bitalinoecgchart.tracker.ViewHoldersCollection;
import com.gattaca.bitalinoecgchart.tracker.data.TopContainer;
import com.gattaca.team.R;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;

import java.util.List;

/**
 * Created by Artem on 14.09.2016.
 */
public class TopItem extends AbstractItem<TopItem, ViewHoldersCollection.TopViewHolder> {

    TopContainer topContainer = TopContainer.example();

    public TopItem withTopContainer(TopContainer topContainer) {
        this.topContainer = topContainer;
        return this;
    }

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
        ViewGroup viewGroup = (ViewGroup) holder.mView.getParent();
        Context context = holder.mView.getContext();
        LinearLayout tabs = (LinearLayout) holder.mView.findViewById(R.id.tracker_custom_tabs);
        tabs.removeAllViews();
        for (int i = 0; i < topContainer.getDays().size(); i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            LinearLayout item = (LinearLayout) LayoutInflater.from(context)
                    .inflate(R.layout.tracker_custom_tab_layout, viewGroup, true);
            item.setLayoutParams(params);
            ((TextView) item.findViewById(R.id.tracker_custom_tab_text)).setText(topContainer.getDays().get(i).getName());
            //TODO
            if (i == topContainer.getSelected()) {
                ((ImageView) item.findViewById(R.id.tracker_custom_tab_img)).setImageResource(R.drawable.dial_ex);
            } else {
                ((ImageView) item.findViewById(R.id.tracker_custom_tab_img)).setImageResource(R.drawable.dial);

            }
            tabs.addView(item);
        }
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
