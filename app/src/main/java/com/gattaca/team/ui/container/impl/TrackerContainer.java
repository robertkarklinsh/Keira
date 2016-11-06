package com.gattaca.team.ui.container.impl;

import android.app.Activity;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.gattaca.team.R;
import com.gattaca.team.db.RealmController;
import com.gattaca.team.db.event.NotifyEventObject;
import com.gattaca.team.db.tracker.Week;
import com.gattaca.team.root.AppUtils;
import com.gattaca.team.root.MainApplication;
import com.gattaca.team.service.main.RootSensorListener;
import com.gattaca.team.ui.container.IContainer;
import com.gattaca.team.ui.container.list.item.TrackerMeasureListItem;
import com.gattaca.team.ui.container.list.lm.SnappingLinearLayoutManager;
import com.gattaca.team.ui.model.impl.TrackerModel;
import com.gattaca.team.ui.tracker.data.TopContainer;
import com.gattaca.team.ui.tracker.ui.TopItem;
import com.gattaca.team.ui.tracker.v2.ModelDao;
import com.gattaca.team.ui.tracker.v2.StubWeekCreator;
import com.gattaca.team.ui.utils.ActivityTransferData;
import com.gattaca.team.ui.view.tracker.MeasureBar;
import com.gattaca.team.ui.view.tracker.MeasureProgress;
import com.gattaca.team.ui.view.tracker.StubTrackerBit;
import com.gattaca.team.ui.view.tracker.TimeLine;
import com.gattaca.team.ui.view.tracker.TrackerBitsCount;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.mikepenz.fastadapter.FastAdapter;
import com.mikepenz.fastadapter.adapters.HeaderAdapter;
import com.mikepenz.fastadapter.adapters.ItemAdapter;
import com.mikepenz.fastadapter.app.adapters.StickyHeaderAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.Calendar;
import java.util.GregorianCalendar;

import io.realm.Realm;

public final class TrackerContainer extends IContainer<TrackerModel> {
    StickyHeaderAdapter mStickyHeaderAdapter;
    FastAdapter mFastAdapter;// = new FastAdapter();
    ItemAdapter mItemAdapter;// = new ItemAdapter();
    HeaderAdapter mHeaderAdapter;
    ModelDao modelDao;
    RecyclerView recyclerView;
    Realm realm;
    GregorianCalendar calendar = new GregorianCalendar();
    Week week;
    private CountDownTimer timer = null;

    static Boolean session = false;
    static Boolean sessionBitalino = false;



    public TrackerContainer(final Activity screen) {
        super(screen, R.id.container_tracker_id);
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
//            case R.id.toolbar_action_open_monitor:
//                MainApplication.uiBusPost(new ContainerTransferData(MainMenu.Monitor));
//                break;
        }
    }

    @Override
    protected void reDraw() {

        if (getModel() == null ||
                getModel().getWeek() == null ||
                getModel().getWeek().getDays() == null ||
                getModel().getWeek().getDays().size() == 0) {
            realm = RealmController.getRealm();
            if (realm.where(Week.class).equalTo(Week.getNamedFieldWeekNum(), new GregorianCalendar().get(GregorianCalendar.WEEK_OF_YEAR)).findFirst() == null) {
                realm.executeTransaction((Realm realm) -> {
                            try {
                                Week week = realm.createObject(Week.class, new GregorianCalendar().get(GregorianCalendar.WEEK_OF_YEAR));
                                StubWeekCreator swc = new StubWeekCreator(week, realm);
                                swc.fillStubWeek();
                            } catch (Exception e) {
                                Toast.makeText(MainApplication.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                );
            }
            week = realm.where(Week.class).equalTo(Week.getNamedFieldWeekNum(), new GregorianCalendar().get(GregorianCalendar.WEEK_OF_YEAR)).findFirst();
        } else {
            week = getModel().getWeek();
        }
        modelDao.setWeek(week);
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

        if (calendar == null) {
            calendar = new GregorianCalendar();
        }
        int weekNum = calendar.get(Calendar.WEEK_OF_YEAR);
        realm = Realm.getDefaultInstance();
//        realm.executeTransaction((Realm r) -> {
//            r.delete(Week.class);
//            r.delete(Day.class);
//            r.delete(Drug.class);
//            r.delete(Intake.class);
//            r.delete(PulseMeasurement.class);
//            r.delete(PressureMeasurement.class);
//            r.delete(Task.class);
//            r.delete(TaskAction.class);
//        });
        week = realm.where(Week.class).equalTo(Week.getNamedFieldWeekNum(), weekNum).findFirst();
        modelDao = new ModelDao(week);
        RelativeLayout relativeLayout = (RelativeLayout) this.getRootView();

        if (mFastAdapter == null) {
            mFastAdapter = new FastAdapter();
        }
        if (mItemAdapter == null) {
            mItemAdapter = new ItemAdapter();
        }
        if (mHeaderAdapter == null) {
            mHeaderAdapter = new HeaderAdapter();
        }
        if (mStickyHeaderAdapter == null) {
            mStickyHeaderAdapter = new StickyHeaderAdapter();
        }
        RecyclerView rv = (RecyclerView) relativeLayout.findViewById(R.id.my_recycler_view);
        recyclerView = rv;
        rv.setLayoutManager(new SnappingLinearLayoutManager(MainApplication.getContext()));

        rv.setAdapter(mStickyHeaderAdapter.wrap(mItemAdapter.wrap(mHeaderAdapter.wrap(mFastAdapter))));
        final StickyRecyclerHeadersDecoration decoration = new StickyRecyclerHeadersDecoration(mStickyHeaderAdapter);
        rv.addItemDecoration(decoration);


        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) relativeLayout.findViewById(R.id.multiple_actions);

        final FloatingActionButton actionAddDrug = (FloatingActionButton) relativeLayout.findViewById(R.id.tracker_add_drug);
        actionAddDrug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainApplication.uiBusPost(new ActivityTransferData(ActivityTransferData.AvailableActivity.ADT));
                menuMultipleActions.collapse();
            }
        });
        final FloatingActionButton actionAddPressure = (FloatingActionButton) relativeLayout.findViewById(R.id.tracker_add_pressure);
        actionAddPressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainApplication.uiBusPost(new ActivityTransferData(ActivityTransferData.AvailableActivity.APT));
                menuMultipleActions.collapse();
            }
        });

        final FloatingActionButton actionAddPulse = (FloatingActionButton) relativeLayout.findViewById(R.id.tracker_add_pulse);
        actionAddPulse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ADD item to model then redraw
                //tracker measurmet pulse layout
                if (sessionBitalino) {
                    Toast.makeText(MainApplication.getContext(), "Сначала нужно удалить Bitalino пульс",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!session) {
                    TrackerBitsCount.value = 0;
                    MeasureBar.value = 0;
                    MeasureBar.poinst.clear();
                    TimeLine.value = 0;
                    timer = new CountDownTimer(DateUtils.MINUTE_IN_MILLIS, DateUtils.SECOND_IN_MILLIS) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            AppUtils.postToBus(1);
                            AppUtils.postToBus(new StubTrackerBit());
                            if (millisUntilFinished + 2000 > DateUtils.MINUTE_IN_MILLIS) {
                                AppUtils.postToBus(new NotifyEventObject());
                            }
                        }

                        @Override
                        public void onFinish() {
                            timer.cancel();
                            if (session) {
                                timer.start();
                            } else {
                                timer = null;
                            }
                        }
                    };
                    session = true;
                    timer.start();
                    modelDao.setTmli(new TrackerMeasureListItem());
                    actionAddPulse.setTitle("Удалить пульс");

                } else {
                    session = false;
                    if (timer != null) {
                        timer.cancel();
                    }
                    timer = null;
                    modelDao.setTmli(null);
                    actionAddPulse.setTitle("Добавить пульс");
                }
                TrackerContainer.this.reDraw();
                menuMultipleActions.collapse();
            }
        });

        final FloatingActionButton actionAddPulseBitalino = (FloatingActionButton) relativeLayout.findViewById(R.id.tracker_add_pulse_bitalino);
        actionAddPulseBitalino.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ADD item to model then redraw
                //tracker measurmet pulse layout
                if (session)
                {
                    Toast.makeText(MainApplication.getContext(), "Сначала нужно удалить обычный пульс",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!sessionBitalino) {
                    TrackerBitsCount.value = 0;
                    MeasureBar.value = 0;
                    MeasureBar.poinst.clear();
                    TimeLine.value = 0;
                    MeasureProgress.events = 0;
                    MeasureProgress.resetView();
                    sessionBitalino = true;
                    RootSensorListener.startRaw();
                    timer = new CountDownTimer(DateUtils.MINUTE_IN_MILLIS, DateUtils.SECOND_IN_MILLIS) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            AppUtils.postToBus(1);

                        }

                        @Override
                        public void onFinish() {
                            timer.cancel();
                            if (sessionBitalino) {
                                timer.start();
                            } else {
                                timer = null;
                            }
                        }
                    };
                    timer.start();
                    modelDao.setTmli(new TrackerMeasureListItem());
                    actionAddPulseBitalino.setTitle("Удалить пульс из Bitalino");

                } else {
                    sessionBitalino = false;
                    if (timer != null) {
                        timer.cancel();
                    }
                    timer = null;
                    modelDao.setTmli(null);
                    RootSensorListener.stopRaw();
                    actionAddPulseBitalino.setTitle("Добавить пульс из Bitalino");
                }
                TrackerContainer.this.reDraw();
                menuMultipleActions.collapse();
            }
        });


    }


}
