package com.gattaca.team.ui.tracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gattaca.team.ui.tracker.data.TopContainer;
import com.gattaca.team.ui.tracker.v2.ModelDao;
import com.gattaca.team.R;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;

import java.util.List;

/**
 * Created by Artem on 14.09.2016.
 */
public class TopItem extends AbstractItem<TopItem, ViewHoldersCollection.TopViewHolder> {

    private static final ImgHolder ZERO_PERCENT = new ImgHolder(R.drawable.zero_percent_big, R.drawable.zero_percent_small);
    private static final ImgHolder TWENTY_FIVE_PERCENT = new ImgHolder(R.drawable.twentyfive_percent_big, R.drawable.twentyfive_percent_small);
    private static final ImgHolder FIFTY_PERCENT = new ImgHolder(R.drawable.fifty_percent_big, R.drawable.fifty_percent_small);
    private static final ImgHolder SEVENTY_FIVE_PERCENT = new ImgHolder(R.drawable.seventyfive_percent_big, R.drawable.seventyfive_percent_small);
    private static final ImgHolder COMPLETED = new ImgHolder(R.drawable.completed_big, R.drawable.completed_small);

    TopContainer topContainer = TopContainer.example();
    RecyclerView recyclerView;
    int previousSelected = -1;
    ImageView previousSelectedView;
    TopContainer.Day previousSelectedDay;
    ModelDao modelDao;

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

            if (i == topContainer.getSelected()) {
                previousSelected = i;
                previousSelectedDay = topContainer.getDays().get(i);
                previousSelectedView = ((ImageView) item.findViewById(R.id.tracker_custom_tab_img));

                ((ImageView) item.findViewById(R.id.tracker_custom_tab_img)).setImageResource(dayIconHandler(topContainer.getDays().get(i), true));
            } else {
                ((ImageView) item.findViewById(R.id.tracker_custom_tab_img)).setImageResource(dayIconHandler(topContainer.getDays().get(i), false));

            }

            tabs.addView(item);
        }
        TextView drugs = (TextView)holder.mView.findViewById(R.id.tracker_top_drugs);
        TextView tasks = (TextView)holder.mView.findViewById(R.id.tracker_top_tasks);
        TextView measurements = (TextView)holder.mView.findViewById(R.id.tracker_top_measurments);

        for (TopContainer.Day day : topContainer.getDays()) {
            if (day.getNum() == ModelDao.currentDayOfWeek()) {
                drugs.setText(day.getCompletedDrugs() + " из " + day.getDrugCount());
                tasks.setText(day.getCompletedTasks() + " из " + day.getTasksCount());
                measurements.setText(day.getCompletedMeasurements() + " из " + day.getMeasurementCount());
            }

        }

    }


    static int dayIconHandler(TopContainer.Day day, boolean selected) {
        if (day.isFuture()) {
            return R.drawable.circle_inactive_day;
        }
        return selected ? imageSelector(day).big : imageSelector(day).small;
    }


    static private ImgHolder imageSelector(TopContainer.Day day) {
        if (day.getPercent() == 0) {
            return ZERO_PERCENT;
        } else {
            if (day.getPercent() < 38) {
                return TWENTY_FIVE_PERCENT;
            } else if (day.getPercent() < 63) {
                return FIFTY_PERCENT;
            } else if (day.getPercent() == 100) {
                return COMPLETED;
            } else {
                return SEVENTY_FIVE_PERCENT;
            }
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
            previousSelectedView.setImageResource(dayIconHandler(previousSelectedDay, false));
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

    private static class ImgHolder {
        int big;
        int small;

        public ImgHolder(int big, int small) {
            this.big = big;
            this.small = small;
        }

        public int getBig() {
            return big;
        }

        public int getSmall() {
            return small;
        }
    }
}
