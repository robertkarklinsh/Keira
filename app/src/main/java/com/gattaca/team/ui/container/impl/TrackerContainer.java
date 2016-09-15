package com.gattaca.team.ui.container.impl;

import android.app.Activity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.gattaca.bitalinoecgchart.tracker.TabMachine;
import com.gattaca.bitalinoecgchart.tracker.ui.FooterItem;
import com.gattaca.bitalinoecgchart.tracker.ui.HeaderItem;
import com.gattaca.bitalinoecgchart.tracker.ui.Item;
import com.gattaca.team.R;
import com.gattaca.team.root.MainApplication;
import com.gattaca.team.ui.container.IContainer;
import com.gattaca.team.ui.container.MainMenu;
import com.gattaca.team.ui.model.impl.TrackerModel;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.IItem;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

public final class TrackerContainer extends IContainer<TrackerModel> {
    FastAdapter mFastAdapter = new FastAdapter();
    ItemAdapter mItemAdapter = new ItemAdapter();
    public TrackerContainer(final Activity screen) {
        super(screen, TrackerModel.class, R.id.container_tracker_id);
    }

    @Override
    public int getMenuItemActions() {
        return R.menu.tracker_toolbar_actions;
    }

    @Override
    public void onMenuItemSelected(final int id) {
        switch (id) {
            case R.id.toolbar_action_change_week:
                MainApplication.showToastNotImplemented();
                break;
            case R.id.toolbar_action_open_monitor:
                MainApplication.uiBusPost(MainMenu.Monitor);
                break;
        }
    }

    @Override
    protected void reDraw() {
        Boolean[] expanded = {false, false, false}; //0 -drug 1-measurements 2- tasks
        //TODO: implements
        TrackerModel model = this.getModel();
        RelativeLayout relativeLayout = (RelativeLayout)this.getRootView();
//        rv.addView();

        RecyclerView rv = (RecyclerView) relativeLayout.findViewById(R.id.my_recycler_view);
        rv.setLayoutManager(new LinearLayoutManager(context));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(mItemAdapter.wrap(mFastAdapter));

        int count = TabMachine.getAllCount(expanded);
        for (int i = 0; i < count; i++){
            int type = TabMachine.getViewType(i, expanded);
            switch (type) {
                case 0 :
                    mItemAdapter.add(new HeaderItem().withExpanded(expanded).withName(TabMachine.getHeaderName(i, expanded)))
                    ;
                    break;
                case 1 :
                    int pos = TabMachine.getExpandPosition(i, expanded);
                    int expandCount = TabMachine.expandCount(expanded, i);
                    int startFromPos = TabMachine.getStartFromPos(expanded, i);
                    int headerPosition = TabMachine.getHeaderPos(expanded, i);

                    mItemAdapter.add(new FooterItem().withOnItemClickListener(new ExpandListener(pos, startFromPos, expandCount, headerPosition,expanded)));
                    break;
                case 2:

                    mItemAdapter.add(new Item().withItemContainer(TabMachine.getItemContainer(i, expanded)));
                    break;
            }
        }



    }

    @Override
    public void bindView() {
        //TODO: implements
    }

    class ExpandListener implements FastAdapter.OnClickListener {
        Boolean[] expanded = {false, false, false};
        private int arrPos = 0;
        private int startFromPosition;
        private int count;
        private int headerPostion = 0;

        public ExpandListener(int expandedPosition, int startFromPosition, int count, int headerPostion, Boolean[] expanded) {
            arrPos = expandedPosition;
            this.startFromPosition = startFromPosition + 1;
            this.count = count;
            this.headerPostion = headerPostion;
        }

//        @Override
//        public void onClick(View v) {
//
////            recyclerView.getLayoutManager().scrollToPosition(headerPostion);
//        }

        @Override
        public boolean onClick(View v, IAdapter adapter, IItem item, int position) {
            expanded[arrPos] = !expanded[arrPos];
            if (expanded[arrPos]) {
                mItemAdapter.notifyItemRangeInserted(startFromPosition, count);
            } else {
                mItemAdapter.notifyItemRangeRemoved(startFromPosition, count);
            }
            return true;
        }
    }
}
