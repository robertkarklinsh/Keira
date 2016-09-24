package com.gattaca.team.ui.container.impl;

import android.app.Activity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.gattaca.bitalinoecgchart.tracker.data.TopContainer;
import com.gattaca.bitalinoecgchart.tracker.model.Week;
import com.gattaca.bitalinoecgchart.tracker.ui.TopItem;
import com.gattaca.bitalinoecgchart.tracker.v2.ModelDao;
import com.gattaca.team.R;
import com.gattaca.team.root.MainApplication;
import com.gattaca.team.ui.container.ContainerTransferData;
import com.gattaca.team.ui.container.IContainer;
import com.gattaca.team.ui.container.MainMenu;
import com.gattaca.team.ui.model.impl.TrackerModel;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;

import java.util.Calendar;
import java.util.GregorianCalendar;

import io.realm.Realm;

public final class TrackerContainer extends IContainer<TrackerModel> {
    FastAdapter mFastAdapter;// = new FastAdapter();
    ItemAdapter mItemAdapter;// = new ItemAdapter();
    ModelDao modelDao = new ModelDao();
    RecyclerView recyclerView;
    Realm realm;
    GregorianCalendar calendar = new GregorianCalendar();
    Week week ;
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
                MainApplication.uiBusPost(new ContainerTransferData(MainMenu.Monitor));
                break;
        }
    }

    @Override
    protected void reDraw() {
        mItemAdapter.clear();
        TopItem topItem = new TopItem()
                .withModelDao(modelDao).linkToRecycleView(recyclerView)
                .withTopContainer(TopContainer.createFromWeek(week));
        mItemAdapter.add(topItem);
        mItemAdapter.add(modelDao.getAllItemList());

    }

    @Override
    public void bindView() {
        //TODO: implements

        //TODO: implements
        realm = Realm.getDefaultInstance();
        if(calendar == null) {
            calendar = new GregorianCalendar();
        }
        int weekNum = calendar.get(Calendar.WEEK_OF_YEAR);
        week = realm.where(Week.class).equalTo(Week.getNamedFieldWeekNum(), weekNum).findFirst();
        modelDao = new ModelDao(week);
        RelativeLayout relativeLayout = (RelativeLayout)this.getRootView();

        if (mFastAdapter == null) {
            mFastAdapter = new FastAdapter();
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
