package com.gattaca.team.ui.tracker.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gattaca.team.R;
import com.gattaca.team.db.tracker.PressureMeasurement;
import com.gattaca.team.root.MainApplication;
import com.gattaca.team.ui.utils.ActivityTransferData;
import com.gattaca.team.ui.tracker.ViewHoldersCollection;
import com.gattaca.team.ui.view.PressureBar;

import java.util.List;
import java.util.Locale;

/**
 * Created by Artem on 23.09.2016.
 */
public class PressureMeasurementItem extends Item {
    @Override
    void bindCustomView(ViewHoldersCollection.DrugItemViewHolder holder, List payloads) {
        PressureMeasurement pressureMeasurement = (PressureMeasurement) itemContainer;
        ViewGroup viewGroup = (ViewGroup) holder.mView.getParent();
        Context context = holder.mView.getContext();
        holder.mView.setOnClickListener(new PressureAllClickListener(pressureMeasurement));
        LinearLayout itemHeader = (LinearLayout) holder.mView.findViewById(R.id.tracker_item_text_holder);

        LinearLayout itemImages = (LinearLayout) holder.mView.findViewById(R.id.tracker_item_image_holder);
        itemImages.removeAllViews();
        ((TextView) itemHeader.findViewById(R.id.tracker_item_text_black)).setText(pressureMeasurement.getBlackText());
        ((TextView) itemHeader.findViewById(R.id.tracker_item_text_gray)).setText(pressureMeasurement.getGrayText());
        ((ImageView) itemHeader.findViewById(R.id.tracker_item_text_icon)).setImageResource(pressureMeasurement.getIcon());
        itemHeader.findViewById(R.id.tracker_item_pressure_clock).setVisibility(View.VISIBLE);
        itemHeader.findViewById(R.id.tracker_item_pressure_time).setVisibility(View.VISIBLE);
        ((TextView) itemHeader.findViewById(R.id.tracker_item_pressure_time)).setText(
                String.format(Locale.ROOT,"%02d:%02d", pressureMeasurement.getHours(), pressureMeasurement.getMinutes()));

        LinearLayout progress;
        if (pressureMeasurement.isCompleted()) {
            progress = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.tracker_measurement_pressure_filled, viewGroup);
            PressureBar systolic = (PressureBar) progress.findViewById(R.id.tracker_pressure_systolic);
            PressureBar diastolic = (PressureBar) progress.findViewById(R.id.tracker_pressure_diastolic);
            TextView pulse = (TextView) progress.findViewById(R.id.tracker_pressure_pulse) ;
            pulse.setText("Пульс: " + pressureMeasurement.getPulse() + " BPM");
            systolic.setValue(pressureMeasurement.getSystolic());
            diastolic.setValue(pressureMeasurement.getDiastolic());
        } else {
            progress = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.tracker_measurement_pressure_non_filled, viewGroup);
//            progress.setOnClickListener((View v) -> {
//                Toast.makeText(context,"Not yet",Toast.LENGTH_SHORT).show();
//            });
        }
            itemImages.addView(progress);

    }

    private class PressureAllClickListener implements View.OnClickListener {

        PressureMeasurement pressureMeasurement;

        public PressureAllClickListener(PressureMeasurement pressureMeasurement) {
            this.pressureMeasurement = pressureMeasurement;
        }

        @Override
        public void onClick(View v) {
            MainApplication.uiBusPost(new ActivityTransferData(ActivityTransferData.AvailableActivity.PM_INFO, pressureMeasurement.getPrimaryKey()));
        }
    }

}
