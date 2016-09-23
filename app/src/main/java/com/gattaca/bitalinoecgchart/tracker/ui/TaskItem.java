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
import com.gattaca.bitalinoecgchart.tracker.data.TaskItemContainer;
import com.gattaca.team.R;

import java.util.List;

/**
 * Created by Artem on 23.09.2016.
 */
public class TaskItem extends Item {
    @Override
    void bindCustomView(ViewHoldersCollection.DrugItemViewHolder holder, List payloads) {
        TaskItemContainer taskItemContainer = (TaskItemContainer) itemContainer;
        ViewGroup viewGroup = (ViewGroup) holder.mView.getParent();
        Context context = holder.mView.getContext();
        LinearLayout itemHeader = (LinearLayout) holder.mView.findViewById(R.id.tracker_item_text_holder);
        LinearLayout itemImages = (LinearLayout) holder.mView.findViewById(R.id.tracker_item_image_holder);
        itemImages.removeAllViews();
        ((TextView) itemHeader.findViewById(R.id.tracker_item_text_black)).setText(taskItemContainer.getBlackText());
        ((TextView) itemHeader.findViewById(R.id.tracker_item_text_gray)).setText(taskItemContainer.getGrayText());
        ((ImageView) itemHeader.findViewById(R.id.tracker_item_text_icon)).setImageResource(taskItemContainer.getIcon());
        for (int i = 0 ; i < taskItemContainer.getTasks().size(); i ++) {
            Boolean task = taskItemContainer.getTasks().get(i);
            LinearLayout taskCircle = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.tracker_drug_circle, viewGroup);
            ((TextView) taskCircle.findViewById(R.id.custom_drug_button_text)).setText("");
            ((ImageView) taskCircle.findViewById(R.id.custom_drug_button_img)).setImageResource(
                    task ? R.drawable.circle_with_arrow : R.drawable.circle_grey
            );
            ImageView a = new ImageView(context);
            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(40, 1);
            a.setLayoutParams(params);
            taskCircle.setOnClickListener(new TaskItemClickListener(taskItemContainer,((ImageView) taskCircle.findViewById(R.id.custom_drug_button_img)),i));
            itemImages.addView(taskCircle);
            itemImages.addView(a);
        }
    }

    private class TaskItemClickListener implements View.OnClickListener {
        TaskItemContainer taskItemContainer;
        ImageView taskCircle;
        int position;


        public TaskItemClickListener(TaskItemContainer taskItemContainer, ImageView taskCircle, int position) {
            this.taskItemContainer = taskItemContainer;
            this.taskCircle = taskCircle;
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            taskItemContainer.getTasks().set(position, !taskItemContainer.getTasks().get(position));
            taskCircle.setImageResource(
                    taskItemContainer.getTasks().get(position) ? R.drawable.circle_with_arrow : R.drawable.circle_grey
            );
        }
    }
}
