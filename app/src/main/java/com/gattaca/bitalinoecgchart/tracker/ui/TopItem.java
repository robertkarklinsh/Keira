package com.gattaca.bitalinoecgchart.tracker.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gattaca.bitalinoecgchart.tracker.ViewHoldersCollection;
import com.gattaca.bitalinoecgchart.tracker.data.TopContainer;
import com.gattaca.bitalinoecgchart.tracker.v2.ModelDao;
import com.gattaca.team.R;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;

import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Artem on 14.09.2016.
 */
public class TopItem extends AbstractItem<TopItem, ViewHoldersCollection.TopViewHolder> {

    TopContainer topContainer = TopContainer.example();
    RecyclerView recyclerView;
    int previousSelected = -1;
    ImageView previousSelectedView;
    TopContainer.Day previousSelectedDay;
    ModelDao modelDao;
    GregorianCalendar calendar = new GregorianCalendar();

    public TopItem withTopContainer(TopContainer topContainer) {
        this.topContainer = topContainer;
        return this;
    }

    public TopItem linkToRecycleView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        return this;
    }

    public TopItem withModelDao(ModelDao modelDao) {
        this.modelDao = modelDao;
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

            item.findViewById(R.id.tracker_custom_tab_img).setOnClickListener(new TopItemClickListener(recyclerView, i, topContainer.getDays().get(i)));
            int currentDay = ModelDao.currentDayOfWeek();

            if (i == topContainer.getSelected()) {
                previousSelected = i;
                previousSelectedDay = topContainer.getDays().get(i);
                previousSelectedView = ((ImageView) item.findViewById(R.id.tracker_custom_tab_img));

                ((ImageView) item.findViewById(R.id.tracker_custom_tab_img)).setImageResource(dayIconHandler(topContainer.getDays().get(i),true));
            } else {
                ((ImageView) item.findViewById(R.id.tracker_custom_tab_img)).setImageResource(dayIconHandler(topContainer.getDays().get(i),false));

            }

            tabs.addView(item);
        }
    }


    static int dayIconHandler(TopContainer.Day day, boolean selected){
        if (day.isFuture()) {
            return R.drawable.circle_inactive_day;
        }
        if (selected) {
            return R.drawable.dial_ex;
        } else {
            return R.drawable.dial;
        }
    }


    private class TopItemClickListener implements View.OnClickListener {
        TopContainer.Day day;
        RecyclerView recyclerView;
        int position;

        public TopItemClickListener(RecyclerView recyclerView, int position, TopContainer.Day day) {
            this.recyclerView = recyclerView;
            this.position = position;
            this.day = day;
        }

        @Override
        public void onClick(View v) {
            if (recyclerView == null) {
                return;
            }
            TopItem.this.topContainer.setSelected(position);
            ((ImageView) v).setImageResource(dayIconHandler(day, true));
            previousSelectedView.setImageResource(dayIconHandler(previousSelectedDay,false));
            previousSelectedView = (ImageView) v;
            previousSelectedDay = day;
            previousSelected = position;
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    recyclerView.smoothScrollToPosition(modelDao.getFirstItemOfDay(position));
                }
            });

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
