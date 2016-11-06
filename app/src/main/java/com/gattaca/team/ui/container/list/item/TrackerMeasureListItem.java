package com.gattaca.team.ui.container.list.item;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gattaca.team.R;
import com.gattaca.team.root.MainApplication;
import com.gattaca.team.ui.utils.ActivityTransferData;
import com.gattaca.team.ui.view.tracker.ITick;
import com.gattaca.team.ui.view.tracker.TimeLine;
import com.mikepenz.fastadapter.items.AbstractItem;

import java.util.List;

public final class TrackerMeasureListItem
        extends AbstractItem<
        TrackerMeasureListItem,
        TrackerMeasureListItem.ViewHolder> {

    public String header;
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
        viewHolder.toMonitor.setOnClickListener(view -> MainApplication.uiBusPost(new ActivityTransferData(ActivityTransferData.AvailableActivity.BPM)));
//        viewHolder.timeLine.setValue(DateUtils.SECOND_IN_MILLIS * 290);
//        viewHolder.progress.setValue(DateUtils.SECOND_IN_MILLIS * 290);
//        viewHolder.timeLine.setValue(DateUtils.SECOND_IN_MILLIS *0);
//        viewHolder.progress.setValue(DateUtils.SECOND_IN_MILLIS *0);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TimeLine timeLine;
        ITick progress;
        TextView toMonitor;
        public ViewHolder(View view) {
            super(view);
            timeLine = (TimeLine) view.findViewById(R.id.timeLine);
            progress = (ITick) view.findViewById(R.id.progress);
            toMonitor = (TextView) view.findViewById(R.id.tracker_pulse_to_graph);
        }
    }

    public TrackerMeasureListItem withHeader(String header) {
        this.header = header;
        return this;
    }
    public int getHeaderPosition() {
        for (int i = 0 ; i < 7; i ++) {
            if (header != null && days[i].equals(header.toLowerCase())) {
                return i;
            }
        }
        return -1;
    }
    String[] days = new String[] {"пн","вт","ср","чт","пт","сб","вс"};
}
