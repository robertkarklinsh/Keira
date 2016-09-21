package com.gattaca.team.ui.container.impl;

import android.app.Activity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.gattaca.bitalinoecgchart.tracker.ui.TopItem;
import com.gattaca.bitalinoecgchart.tracker.v2.ModelDao;
import com.gattaca.team.R;
import com.gattaca.team.root.MainApplication;
import com.gattaca.team.ui.container.IContainer;
import com.gattaca.team.ui.container.MainMenu;
import com.gattaca.team.ui.model.impl.TrackerModel;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.IAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

public final class TrackerContainer extends IContainer<TrackerModel> {
    FastAdapter mFastAdapter;// = new FastAdapter();
    ItemAdapter mItemAdapter;// = new ItemAdapter();
    ModelDao modelDao = new ModelDao();
    RecyclerView recyclerView;
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
//        Boolean[] expanded = {false, false, false}; //0 -drug 1-measurements 2- tasks
//        TrackerModel model = this.getModel();
//        int count = TabMachine.getAllCount(expanded);
//        for (int i = 0; i < count; i++){
//            int type = TabMachine.getViewType(i, expanded);
//            switch (type) {
//                case 0 :
//                    mItemAdapter.add(new HeaderItem().withExpanded(expanded).withName(TabMachine.getHeaderName(i, expanded)));
//                    break;
//                case 1 :
//                    int pos = TabMachine.getExpandPosition(i, expanded);
//                    int expandCount = TabMachine.expandCount(expanded, i);
//                    int startFromPos = TabMachine.getStartFromPos(expanded, i);
//                    int headerPosition = TabMachine.getHeaderPos(expanded, i);
//
//                    mItemAdapter.add(new FooterItem().withOnItemClickListener(new ExpandListener(pos, startFromPos, expandCount, headerPosition,expanded)));
//                    break;
//                case 2:
//
//                    mItemAdapter.add(new Item().withItemContainer(TabMachine.getItemContainer(i, expanded)));
//                    break;
//                case 3:
//                    mItemAdapter.add(new TopItem());
//            }
//        }
//
        mItemAdapter.clear();
        TopItem topItem = new TopItem().withModelDao(modelDao).linkToRecycleView(recyclerView).withOnItemClickListener(new FastAdapter.OnClickListener<TopItem>() {
            @Override
            public boolean onClick(View v, IAdapter<TopItem> adapter, TopItem item, int position) {

                return false;
            }
        });
        mItemAdapter.add(topItem);
        mItemAdapter.add(modelDao.getAllItemList());

    }

    @Override
    public void bindView() {
        //TODO: implements

        //TODO: implements

        RelativeLayout relativeLayout = (RelativeLayout)this.getRootView();
//        rv.addView();
        if (mFastAdapter == null) {
            mFastAdapter = new FastAdapter() {
                int lastPosition = -1;
                @Override
                public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
                    super.onViewDetachedFromWindow(holder);
                    holder.itemView.clearAnimation();
                }
//                @Override
//                public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//                    super.onBindViewHolder(holder, position);
//                    Animation animation = AnimationUtils.loadAnimation(context,
//                            (position > lastPosition) ? R.anim.up_from_bottom
//                                    : R.anim.down_from_top);
//                    holder.itemView.startAnimation(animation);
//                    lastPosition = position;
//                }
            };
        }
        if (mItemAdapter == null) {
            mItemAdapter = new ItemAdapter();
        }
        RecyclerView rv = (RecyclerView) relativeLayout.findViewById(R.id.my_recycler_view);
        recyclerView = rv;
        rv.setLayoutManager(new SnappingLinearLayoutManager(context));

        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(mItemAdapter.wrap(mFastAdapter));




    }


}
