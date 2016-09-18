package com.gattaca.bitalinoecgchart.tracker;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.gattaca.team.R;

/**
 * Created by Artem on 27.08.2016.
 */
public class ViewHoldersCollection {


    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;


        public HeaderViewHolder(View mView) {
            super(mView);
            this.mView = mView;
        }
    }

    public static class ExpandViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;

        public ExpandViewHolder(View mView) {
            super(mView);
            this.mView = mView;
        }
    }

    public static class DrugItemViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        public View imgHolder;
        public View textHolder;
        public boolean isInitialized = false;


        public DrugItemViewHolder(View mView) {
            super(mView);
            this.mView = mView;
            imgHolder = mView.findViewById(R.id.tracker_item_image_holder);
            textHolder =  mView.findViewById(R.id.tracker_item_text_holder);

        }
    }

    public static class TopViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        public LinearLayout tabLayout;
        public LinearLayout ll;

        public boolean isInitialized = false;


        public TopViewHolder(View mView) {
            super(mView);
            this.mView = mView;
            tabLayout = (LinearLayout) mView.findViewById(R.id.tracker_custom_tabs);
            ll = (LinearLayout) mView.findViewById(R.id.tracker_top_ll);

        }
    }


}
