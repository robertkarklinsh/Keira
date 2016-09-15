package com.gattaca.bitalinoecgchart.tracker.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gattaca.bitalinoecgchart.tracker.ViewHoldersCollection;
import com.gattaca.bitalinoecgchart.tracker.data.DrugItemContainer;
import com.gattaca.bitalinoecgchart.tracker.data.ProgressBarItemContainer;
import com.gattaca.bitalinoecgchart.tracker.data.TaskItemContainer;
import com.gattaca.bitalinoecgchart.tracker.data.TrackerItemContainer;
import com.gattaca.team.R;
import com.mikepenz.fastadapter.items.AbstractItem;
import com.mikepenz.fastadapter.utils.ViewHolderFactory;

import java.util.List;

/**
 * Created by Artem on 14.09.2016.
 */
public class Item extends AbstractItem<Item, ViewHoldersCollection.DrugItemViewHolder> {

    private TrackerItemContainer itemContainer;

    public Item withItemContainer(TrackerItemContainer itemContainer) {
        this.itemContainer = itemContainer;
        return this;
    }

    @Override
    public int getType() {
        return R.id.tracker_item;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.tracker_list_entity;
    }

    @Override
    public void bindView(ViewHoldersCollection.DrugItemViewHolder holder, List payloads) {
        super.bindView(holder, payloads);
        switch (itemContainer.getType()) {
            case DRUG: {
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

                for (int i = 0; i < receptions.size() - 1; i++) {
                    DrugItemContainer.Reception reception = receptions.get(i);
                    LinearLayout drugCircle = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.tracker_drug_circle, viewGroup);
                    ((TextView) drugCircle.findViewById(R.id.custom_drug_button_text)).setText(reception.getTime());
                    ((ImageView) drugCircle.findViewById(R.id.custom_drug_button_img)).setImageResource(
                            reception.getStatus() ? R.drawable.circle : R.drawable.circle_grey
                    );
//                    drugCircle.setOnClickListener(new DrugButtonListener());
                    itemImages.addView(drugCircle);
                    LinearLayout line = (LinearLayout) LayoutInflater.from(context)
                            .inflate(R.layout.tracker_drug_line, viewGroup);
                    itemImages.addView(line);
                }
                DrugItemContainer.Reception reception = receptions.get(receptions.size() - 1);
                LinearLayout drugCircle = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.tracker_drug_circle, viewGroup);
                ((TextView) drugCircle.findViewById(R.id.custom_drug_button_text)).setText(reception.getTime());
                ((ImageView) drugCircle.findViewById(R.id.custom_drug_button_img)).setImageResource(
                        reception.getStatus() ? R.drawable.circle : R.drawable.circle_grey
                );
                itemImages.addView(drugCircle);


                break;
            }
            case MEASUREMENT: {
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
                break;
            }
            case TASK: {
                TaskItemContainer taskItemContainer = (TaskItemContainer) itemContainer;
                ViewGroup viewGroup = (ViewGroup) holder.mView.getParent();
                Context context = holder.mView.getContext();
                LinearLayout itemHeader = (LinearLayout) holder.mView.findViewById(R.id.tracker_item_text_holder);
                LinearLayout itemImages = (LinearLayout) holder.mView.findViewById(R.id.tracker_item_image_holder);
                itemImages.removeAllViews();
                ((TextView) itemHeader.findViewById(R.id.tracker_item_text_black)).setText(taskItemContainer.getBlackText());
                ((TextView) itemHeader.findViewById(R.id.tracker_item_text_gray)).setText(taskItemContainer.getGrayText());
                ((ImageView) itemHeader.findViewById(R.id.tracker_item_text_icon)).setImageResource(taskItemContainer.getIcon());
                for (Boolean task : taskItemContainer.getTasks()) {
                    LinearLayout drugCircle = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.tracker_drug_circle, viewGroup);
                    ((TextView) drugCircle.findViewById(R.id.custom_drug_button_text)).setText("");
                    ((ImageView) drugCircle.findViewById(R.id.custom_drug_button_img)).setImageResource(
                            task ? R.drawable.circle_with_arrow : R.drawable.circle_grey
                    );
                    ImageView a = new ImageView(context);
                    RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(40, 1);
                    a.setLayoutParams(params);
//                    drugCircle.setOnClickListener(new TaskButtonListener());
                    itemImages.addView(drugCircle);
                    itemImages.addView(a);
                }
            }
        }

    }

    @Override
    public ViewHolderFactory<? extends ViewHoldersCollection.DrugItemViewHolder> getFactory() {
        return ViewHoldersCollection.DrugItemViewHolder::new;
    }
}
