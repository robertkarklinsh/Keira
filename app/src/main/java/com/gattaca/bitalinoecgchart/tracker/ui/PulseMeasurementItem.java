package com.gattaca.bitalinoecgchart.tracker.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gattaca.bitalinoecgchart.tracker.ViewHoldersCollection;
import com.gattaca.team.R;
import com.gattaca.team.db.tracker.PulseMeasurement;

import java.util.List;

/**
 * Created by Artem on 23.09.2016.
 */
public class PulseMeasurementItem extends Item {
    @Override
    void bindCustomView(ViewHoldersCollection.DrugItemViewHolder holder, List payloads) {
        PulseMeasurement pulseMeasurement = (PulseMeasurement) itemContainer;
        ViewGroup viewGroup = (ViewGroup) holder.mView.getParent();
        Context context = holder.mView.getContext();
        LinearLayout itemHeader = (LinearLayout) holder.mView.findViewById(R.id.tracker_item_text_holder);
        LinearLayout itemImages = (LinearLayout) holder.mView.findViewById(R.id.tracker_item_image_holder);
        itemImages.removeAllViews();
        ((TextView) itemHeader.findViewById(R.id.tracker_item_text_black)).setText(pulseMeasurement.getBlackText());
        ((TextView) itemHeader.findViewById(R.id.tracker_item_text_gray)).setText(pulseMeasurement.getGrayText());
        ((ImageView) itemHeader.findViewById(R.id.tracker_item_text_icon)).setImageResource(pulseMeasurement.getIcon());

        LinearLayout progress = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.tracker_measurement_pulse, viewGroup);
//        ((ImageView) progress.findViewById(R.id.tryDrawable)).setImageDrawable(new PressureDrawable(0,0,0));
        itemImages.addView(progress);

    }
}
