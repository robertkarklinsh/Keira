package com.gattaca.bitalinoecgchart.tracker.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gattaca.bitalinoecgchart.tracker.ViewHoldersCollection;
import com.gattaca.bitalinoecgchart.tracker.data.ProgressBarItemContainer;
import com.gattaca.team.R;

import java.util.List;

/**
 * Created by Artem on 23.09.2016.
 */
public class TrackerItem extends Item {
    @Override
    void bindCustomView(ViewHoldersCollection.DrugItemViewHolder holder, List payloads) {
        ProgressBarItemContainer measurementItemContainer = (ProgressBarItemContainer) itemContainer;
        ViewGroup viewGroup = (ViewGroup) holder.mView.getParent();
        Context context = holder.mView.getContext();
        LinearLayout itemHeader = (LinearLayout) holder.mView.findViewById(R.id.tracker_item_text_holder);
        LinearLayout itemImages = (LinearLayout) holder.mView.findViewById(R.id.tracker_item_image_holder);
        itemImages.removeAllViews();
        ((TextView) itemHeader.findViewById(R.id.tracker_item_text_black)).setText(measurementItemContainer.getBlackText());
        ((TextView) itemHeader.findViewById(R.id.tracker_item_text_gray)).setText(measurementItemContainer.getGrayText());
        ((ImageView) itemHeader.findViewById(R.id.tracker_item_text_icon)).setImageResource(measurementItemContainer.getIcon());
        LinearLayout progress = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.tracker_measurement_progess, viewGroup);
        itemImages.addView(progress);

    }
}
