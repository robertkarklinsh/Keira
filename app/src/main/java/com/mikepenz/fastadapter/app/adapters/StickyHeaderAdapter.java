package com.mikepenz.fastadapter.app.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gattaca.team.R;
import com.gattaca.team.ui.tracker.ui.Item;
import com.mikepenz.fastadapter.AbstractAdapter;
import com.mikepenz.fastadapter.IItem;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.security.SecureRandom;
import java.util.List;

/**
 * Created by mikepenz on 30.12.15.
 * This is a FastAdapter adapter implementation for the awesome Sticky-Headers lib by timehop
 * https://github.com/timehop/sticky-headers-recyclerview
 */
public class StickyHeaderAdapter extends AbstractAdapter implements StickyRecyclerHeadersAdapter {
    @Override
    public long getHeaderId(int position) {
        IItem item = getItem(position);

        if (item instanceof Item && ((Item) item).header != null && !((Item) item).header.isEmpty())
        {
            return ((Item) item).header.charAt(0);
        }
        //in our sample we want a separate header per first letter of our items
        //this if is not necessary for your code, we only use it as this sticky header is reused for different item implementations
//        if (item instanceof SimpleItem && ((SimpleItem) item).header != null) {
//            return ((SimpleItem) item).header.charAt(0);
//        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        //we create the view for the header
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tracker_header, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        LinearLayout linearLayout = (LinearLayout) holder.itemView;

        IItem item = getItem(position);
        if (item instanceof Item && ((Item) item).header != null && !((Item) item).header.isEmpty())
        {
            ((TextView)linearLayout.findViewById(R.id.tracker_header_text)).setText(String.valueOf (((Item) item).header));
        }
    }

    //just to prettify things a bit
    private int getRandomColor() {
        SecureRandom rgen = new SecureRandom();
        return Color.HSVToColor(150, new float[]{
                rgen.nextInt(359), 1, 1
        });
    }

    /**
     * REQUIRED FOR THE FastAdapter. Set order to < 0 to tell the FastAdapter he can ignore this one.
     **/

    /**
     * @return
     */
    @Override
    public int getOrder() {
        return -100;
    }

    @Override
    public int getAdapterItemCount() {
        return 0;
    }

    @Override
    public List<IItem> getAdapterItems() {
        return null;
    }

    @Override
    public IItem getAdapterItem(int position) {
        return null;
    }

    @Override
    public int getAdapterPosition(IItem item) {
        return -1;
    }

//    @Override
//    public int getAdapterPosition(long identifier) {
//        return -1;
//    }

    @Override
    public int getGlobalPosition(int position) {
        return -1;
    }

}
