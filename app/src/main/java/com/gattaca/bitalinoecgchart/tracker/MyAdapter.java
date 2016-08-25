package com.gattaca.bitalinoecgchart.tracker;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gattaca.bitalinoecgchart.R;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private String[] mDataset;
    private RecyclerView recyclerView;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        public TextView mTextView;
        public TextView details;
        public ViewHolder(View mViev) {
            super(mViev);
//            mTextView = (TextView) mViev.findViewById(R.id.xui1);
//            details = (TextView) mViev.findViewById(R.id.xui2);
//            details.setText("XUIXUI");
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyAdapter(String[] myDataset, RecyclerView recyclerView) {
        mDataset = myDataset;
        this.recyclerView = recyclerView;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drug_entity, parent, false);
        LinearLayout v1 = (LinearLayout) v.findViewById(R.id.drug_image_holder);
        LinearLayout v2 = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drug_circle, parent, false);
        LinearLayout v25 = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drug_line, parent, false);
        LinearLayout v3 = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drug_circle, parent, false);
        v2.setOnClickListener(new View.OnClickListener() {
            int flag = 1;
            @Override
            public void onClick(View v) {
                flag *= -1;

                LinearLayout k = (LinearLayout) v;
                ImageView img = (ImageView) k.findViewById(R.id.custom_drug_button_img);
                if (flag == -1) {
                    img.setBackground(parent.getContext().getDrawable(R.drawable.circle_grey));
                } else {
                    img.setBackground(parent.getContext().getDrawable(R.drawable.circle));
                }

            }
        });
        v1.addView(v2);
        v1.addView(v25);
        v1.addView(v3);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    private int mExpandedPosition = -1;
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder,final int position) {

        // - get element from your dataset at this position
        // - replace the contents
        // of the view with that element
//        holder.mTextView.setText(mDataset[position]);
//        final boolean isExpanded = position==mExpandedPosition;
//        holder.details.setVisibility(isExpanded?View.VISIBLE:View.GONE);
//        holder.itemView.setActivated(isExpanded);
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mExpandedPosition = isExpanded ? -1:position;
//                TransitionManager.beginDelayedTransition(recyclerView);
//                notifyDataSetChanged();
//            }
//        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}