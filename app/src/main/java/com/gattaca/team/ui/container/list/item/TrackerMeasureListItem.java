package com.gattaca.team.ui.container.list.item;

import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;

import com.gattaca.team.R;
import com.gattaca.team.ui.view.tracker.ITick;
import com.gattaca.team.ui.view.tracker.TimeLine;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

public final class TrackerMeasureListItem
        extends AbstractItem<
        TrackerMeasureListItem,
        TrackerMeasureListItem.ViewHolder> {
    @Override
    public int getType() {
        return R.id.list_item_tracker_measure;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.tracker_measurement;
    }

    @Override
    public void bindView(ViewHolder viewHolder, List payloads) {
        super.bindView(viewHolder, payloads);
        viewHolder.timeLine.setValue(DateUtils.SECOND_IN_MILLIS * 290);
        viewHolder.progress.setValue(DateUtils.SECOND_IN_MILLIS * 290);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TimeLine timeLine;
        ITick progress;

        public ViewHolder(View view) {
            super(view);
            timeLine = (TimeLine) view.findViewById(R.id.timeLine);
            progress = (ITick) view.findViewById(R.id.progress);
        }
    }
}
