package com.gattaca.bitalinoecgchart.tracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gattaca.bitalinoecgchart.R;
import com.gattaca.bitalinoecgchart.tracker.data.DrugItemContainer;
import com.gattaca.bitalinoecgchart.tracker.data.ProgressBarItemContainer;
import com.gattaca.bitalinoecgchart.tracker.data.TaskItemContainer;
import com.gattaca.bitalinoecgchart.tracker.data.TrackerItemContainer;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private String[] mDataset;
    private RecyclerView recyclerView;

    private int tabPostion = -1;

    Boolean[] expanded = {false, false, false}; //0 -drug 1-measurements 2- tasks


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder


    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(String[] myDataset, RecyclerView recyclerView, int position) {
        position = position;
        mDataset = myDataset;
        this.recyclerView = recyclerView;

    }

    @Override
    public int getItemViewType(int position) {
        return TabMachine.getViewType(position, expanded);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder res = null;
        switch (viewType) {
            case 0: {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.tracker_header, parent, false);
                res = new ViewHoldersCollection.HeaderViewHolder(v);
                break;
            }
            case 1: {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.tracker_footer, parent, false);
                res = new ViewHoldersCollection.ExpandViewHolder(v);
                break;
            }
            case 2: {
                // create a new view
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.tracker_list_entity, parent, false);
                res = new ViewHoldersCollection.DrugItemViewHolder(v);
                break;
            }
        }


        return res;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        int type = TabMachine.getViewType(position, expanded);
        switch (type) {
            case 0:
                String headerName = TabMachine.getHeaderName(position, expanded);
                TextView textView = (TextView) ((ViewHoldersCollection.HeaderViewHolder) holder).mView.findViewById(R.id.tracker_header_text);
                textView.setText(headerName);
                break;
            case 1: {
                int pos = TabMachine.getExpandPosition(position, expanded);
                int count = TabMachine.expandCount(expanded, position);
                int startFromPos = TabMachine.getStartFromPos(expanded, position);
                int headerPosition = TabMachine.getHeaderPos(expanded, position);
                ((ViewHoldersCollection.ExpandViewHolder) holder).
                        mView
                        .setOnClickListener(new ExpandListener(pos, startFromPos, count, headerPosition));
                break;
            }
            case 2: {
                TrackerItemContainer container = TabMachine.getItemContainer(position, expanded);
                switch (container.getType()) {
                    case 1: {
                        DrugItemContainer drugItemContainer = (DrugItemContainer) container;
                        ViewGroup viewGroup = (ViewGroup) ((ViewHoldersCollection.DrugItemViewHolder) holder).mView.getParent();
                        Context context = ((ViewHoldersCollection.DrugItemViewHolder) holder).mView.getContext();
                        LinearLayout itemHeader = (LinearLayout) ((ViewHoldersCollection.DrugItemViewHolder) holder).mView.findViewById(R.id.tracker_item_text_holder);
                        LinearLayout itemImages = (LinearLayout) ((ViewHoldersCollection.DrugItemViewHolder) holder).mView.findViewById(R.id.tracker_item_image_holder);
                        itemImages.removeAllViews();
                        ((TextView) itemHeader.findViewById(R.id.tracker_item_text_black)).setText(drugItemContainer.getBlackText());
                        ((TextView) itemHeader.findViewById(R.id.tracker_item_text_gray)).setText(drugItemContainer.getGrayText());
                        ((ImageView) itemHeader.findViewById(R.id.tracker_item_text_icon)).setImageResource(drugItemContainer.getIcon());

                        List<DrugItemContainer.Reception> receptions = drugItemContainer.getReceptions();

                        for (int i = 0; i < receptions.size() - 1; i++) {
                            DrugItemContainer.Reception reception = receptions.get(i);
                            LinearLayout drugCircle = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.drug_circle, viewGroup);
                            ((TextView) drugCircle.findViewById(R.id.custom_drug_button_text)).setText(reception.getTime());
                            ((ImageView) drugCircle.findViewById(R.id.custom_drug_button_img)).setImageResource(
                                    reception.getStatus() ? R.drawable.circle : R.drawable.circle_grey
                            );
                            drugCircle.setOnClickListener(new DrugButtonListener());
                            itemImages.addView(drugCircle);
                            LinearLayout line = (LinearLayout) LayoutInflater.from(context)
                                    .inflate(R.layout.drug_line, viewGroup);
                            itemImages.addView(line);
                        }
                        DrugItemContainer.Reception reception = receptions.get(receptions.size() - 1);
                        LinearLayout drugCircle = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.drug_circle, viewGroup);
                        ((TextView) drugCircle.findViewById(R.id.custom_drug_button_text)).setText(reception.getTime());
                        ((ImageView) drugCircle.findViewById(R.id.custom_drug_button_img)).setImageResource(
                                reception.getStatus() ? R.drawable.circle : R.drawable.circle_grey
                        );
                        drugCircle.setOnClickListener(new DrugButtonListener());
                        itemImages.addView(drugCircle);
//                        ((ViewHoldersCollection.DrugItemViewHolder) holder).setVisibility(drugItemContainer.isVisible());

                        break;
                    }
                    case 2: {
                        ProgressBarItemContainer measurementItemContainer = (ProgressBarItemContainer) container;
                        ViewGroup viewGroup = (ViewGroup) ((ViewHoldersCollection.DrugItemViewHolder) holder).mView.getParent();
                        Context context = ((ViewHoldersCollection.DrugItemViewHolder) holder).mView.getContext();
                        LinearLayout itemHeader = (LinearLayout) ((ViewHoldersCollection.DrugItemViewHolder) holder).mView.findViewById(R.id.tracker_item_text_holder);
                        LinearLayout itemImages = (LinearLayout) ((ViewHoldersCollection.DrugItemViewHolder) holder).mView.findViewById(R.id.tracker_item_image_holder);
                        itemImages.removeAllViews();
                        ((TextView) itemHeader.findViewById(R.id.tracker_item_text_black)).setText(measurementItemContainer.getBlackText());
                        ((TextView) itemHeader.findViewById(R.id.tracker_item_text_gray)).setText(measurementItemContainer.getGrayText());
                        ((ImageView) itemHeader.findViewById(R.id.tracker_item_text_icon)).setImageResource(measurementItemContainer.getIcon());
                        LinearLayout progress = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.tracker_measurement_progess, viewGroup);
                        itemImages.addView(progress);
                        break;
                    }
                    case 3: {
                        TaskItemContainer taskItemContainer = (TaskItemContainer) container;
                        ViewGroup viewGroup = (ViewGroup) ((ViewHoldersCollection.DrugItemViewHolder) holder).mView.getParent();
                        Context context = ((ViewHoldersCollection.DrugItemViewHolder) holder).mView.getContext();
                        LinearLayout itemHeader = (LinearLayout) ((ViewHoldersCollection.DrugItemViewHolder) holder).mView.findViewById(R.id.tracker_item_text_holder);
                        LinearLayout itemImages = (LinearLayout) ((ViewHoldersCollection.DrugItemViewHolder) holder).mView.findViewById(R.id.tracker_item_image_holder);
                        itemImages.removeAllViews();
                        ((TextView) itemHeader.findViewById(R.id.tracker_item_text_black)).setText(taskItemContainer.getBlackText());
                        ((TextView) itemHeader.findViewById(R.id.tracker_item_text_gray)).setText(taskItemContainer.getGrayText());
                        ((ImageView) itemHeader.findViewById(R.id.tracker_item_text_icon)).setImageResource(taskItemContainer.getIcon());
                        for (Boolean task : taskItemContainer.getTasks()) {
                            LinearLayout drugCircle = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.drug_circle, viewGroup);
                            ((TextView) drugCircle.findViewById(R.id.custom_drug_button_text)).setText("");
                            ((ImageView) drugCircle.findViewById(R.id.custom_drug_button_img)).setImageResource(
                                    task ? R.drawable.circle : R.drawable.circle_grey
                            );
                            ImageView a = new ImageView(context);
                            RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(40, 1);
                            a.setLayoutParams(params);
                            drugCircle.setOnClickListener(new DrugButtonListener());
                            itemImages.addView(drugCircle);
                            itemImages.addView(a);
                        }
                    }
                }
                break;
            }
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return TabMachine.getAllCount(expanded);
    }

    class ExpandListener implements View.OnClickListener {

        private int arrPos = 0;
        private int startFromPosition;
        private int count;
        private int headerPostion = 0;

        public ExpandListener(int expandedPosition, int startFromPosition, int count, int headerPostion) {
            arrPos = expandedPosition;
            this.startFromPosition = startFromPosition + 1;
            this.count = count;
            this.headerPostion = headerPostion;
        }

        @Override
        public void onClick(View v) {
            expanded[arrPos] = !expanded[arrPos];;
            if (expanded[arrPos]) {
                notifyItemRangeInserted(startFromPosition, count);
            } else {
                notifyItemRangeRemoved(startFromPosition, count);
            }
            recyclerView.getLayoutManager().scrollToPosition(headerPostion);
        }
    }

    class DrugButtonListener implements View.OnClickListener {
        int flag = 1;

        @Override
        public void onClick(View v) {
            flag *= -1;

            LinearLayout k = (LinearLayout) v;
            ImageView img = (ImageView) k.findViewById(R.id.custom_drug_button_img);
            if (flag == -1) {
                img.setImageResource(R.drawable.circle_grey);
            } else {
                img.setImageResource(R.drawable.circle);
            }

        }

    }
}