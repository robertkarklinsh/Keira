package com.gattaca.bitalinoecgchart.tracker.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gattaca.bitalinoecgchart.tracker.ViewHoldersCollection;
import com.gattaca.bitalinoecgchart.tracker.data.DrugItemContainer;
import com.gattaca.team.R;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Artem on 23.09.2016.
 */
public class DrugItem extends Item {

    @Override
    void bindCustomView(ViewHoldersCollection.DrugItemViewHolder holder, List payloads) {
        DrugItemContainer drugItemContainer = (DrugItemContainer) itemContainer;
        ViewGroup viewGroup = (ViewGroup) holder.mView.getParent();
        Context context = holder.mView.getContext();
        LinearLayout itemHeader = (LinearLayout) holder.mView.findViewById(R.id.tracker_item_text_holder);
        LinearLayout itemImages = (LinearLayout) holder.mView.findViewById(R.id.tracker_item_image_holder);
        itemImages.removeAllViews();
        ((TextView) itemHeader.findViewById(R.id.tracker_item_text_black)).setText(drugItemContainer.getBlackText());
        ((TextView) itemHeader.findViewById(R.id.tracker_item_text_gray)).setText(drugItemContainer.getGrayText());
        ((ImageView) itemHeader.findViewById(R.id.tracker_item_text_icon)).setImageResource(drugItemContainer.getIcon());

        List<DrugItemContainer.Reception> receptions = drugItemContainer.getReceptions();

        DrugItemContainer.Reception receptionF = receptions.get(0);
        LinearLayout drugCircleF = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.tracker_drug_circle, viewGroup);
        ((TextView) drugCircleF.findViewById(R.id.custom_drug_button_text)).setText(receptionF.getTime());
        ((ImageView) drugCircleF.findViewById(R.id.custom_drug_button_img)).setImageResource(
                chooseDrugCircle(receptionF.getStatus(), receptionF.getHours(), receptionF.getMinutes())
        );
        drugCircleF.setOnClickListener(new DrugItemClickListener(receptionF, null, (ImageView) drugCircleF.findViewById(R.id.custom_drug_button_img)));
        itemImages.addView(drugCircleF);

        for (int i = 1; i < receptions.size(); i++) {
            DrugItemContainer.Reception reception = receptions.get(i);
            LinearLayout line = (LinearLayout) LayoutInflater.from(context)
                    .inflate(R.layout.tracker_drug_line_blue, viewGroup);
            ImageView lineImage = (ImageView) line.findViewById(R.id.tracker_drug_line);
            lineImage.setBackground(chooseDrugLine(reception.getStatus(), reception.getHours(), reception.getMinutes(),context));

            itemImages.addView(line);

            LinearLayout drugCircle = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.tracker_drug_circle, viewGroup);
            ((TextView) drugCircle.findViewById(R.id.custom_drug_button_text)).setText(reception.getTime());
            ((ImageView) drugCircle.findViewById(R.id.custom_drug_button_img)).setImageResource(
                    chooseDrugCircle(reception.getStatus(), reception.getHours(), reception.getMinutes())
            );
            drugCircle.setOnClickListener(new DrugItemClickListener(reception, lineImage, (ImageView) drugCircle.findViewById(R.id.custom_drug_button_img)));
            itemImages.addView(drugCircle);
        }
    }

    private Drawable chooseDrugLine(boolean status, int hour, int minute, Context context) {
        if (status) {
            return  context.getDrawable(R.drawable.drug_line_blue);

        } else {
            if (isExpired(status, hour, minute)) {
                return context.getDrawable(R.drawable.drug_line_orange);
            } else {
                return context.getDrawable(R.drawable.drug_line_grey);
            }
        }

    }

    private boolean isExpired(boolean status, int hour, int minute) {
        if (status) {
            return false;
        } else {
            int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
            int currentMinute = calendar.get(Calendar.MINUTE);
            if (currentHour <= hour) {
                if (currentHour == hour) {
                    if (currentMinute <= minute) {
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    //current hour less then we need
                    return false;
                }
            } else {
                return true;
            }
        }
    }

    private int chooseDrugCircle(boolean status, int hour, int minute) {
        if (status) {
            return R.drawable.circle;
        } else {
            return isExpired(status, hour, minute) ? R.drawable.circle_orrange : R.drawable.circle_grey;
        }
    }

    private class DrugItemClickListener implements View.OnClickListener {
        private DrugItemContainer.Reception reception;

        private ImageView drugLine;
        private ImageView drugCircle;

        public DrugItemClickListener(DrugItemContainer.Reception reception, ImageView drugLine, ImageView drugCircle) {
            this.reception = reception;
            this.drugLine = drugLine;
            this.drugCircle = drugCircle;
        }

        @Override
        public void onClick(View view) {
            reception.setStatus(!reception.getStatus());
            drugCircle.setImageResource(
                    chooseDrugCircle(reception.getStatus(), reception.getHours(), reception.getMinutes()));
            if (drugLine != null) {
                drugLine.setBackground(
                        chooseDrugLine(reception.getStatus(), reception.getHours(), reception.getMinutes(),view.getContext()));
            }
        }
    }

}
