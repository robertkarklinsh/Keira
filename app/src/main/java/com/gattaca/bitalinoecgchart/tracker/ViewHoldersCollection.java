package com.gattaca.bitalinoecgchart.tracker;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gattaca.bitalinoecgchart.R;

/**
 * Created by Artem on 27.08.2016.
 */
public class ViewHoldersCollection {


    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        static int type = 0;


        public HeaderViewHolder(View mView) {
            super(mView);
            this.mView = mView;
        }
    }

    public static class ExpandViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        static int type = 1;

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
        static int type = 2;


        public DrugItemViewHolder(View mView) {
            super(mView);
            this.mView = mView;
            imgHolder = mView.findViewById(R.id.tracker_item_image_holder);
            textHolder =  mView.findViewById(R.id.tracker_item_text_holder);

        }

        public void setVisibility(boolean value) {
            if (value) {
                imgHolder.setVisibility(View.VISIBLE);
                textHolder.setVisibility(View.VISIBLE);
                imgHolder.setActivated(true);
                textHolder.setActivated(true);
            } else {
                imgHolder.setVisibility(View.GONE);
                textHolder.setVisibility(View.GONE);
                imgHolder.setActivated(false);
                textHolder.setActivated(false);
            }
        }
    }


}
