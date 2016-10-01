package com.gattaca.team.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.MenuItem;

import com.gattaca.team.R;
import com.gattaca.team.annotation.GraphPeriod;
import com.gattaca.team.db.RealmController;
import com.gattaca.team.db.sensor.BpmGreen;
import com.gattaca.team.db.sensor.BpmRed;
import com.gattaca.team.db.sensor.Session;
import com.gattaca.team.db.sensor.emulate.EmulatedBpm_15Min;
import com.gattaca.team.db.sensor.emulate.EmulatedBpm_5Min;
import com.gattaca.team.db.sensor.optimizing.BpmPoint_15_min;
import com.gattaca.team.db.sensor.optimizing.BpmPoint_30_min;
import com.gattaca.team.db.sensor.optimizing.BpmPoint_5_min;
import com.gattaca.team.prefs.AppPref;
import com.gattaca.team.root.AppUtils;
import com.gattaca.team.service.main.RootSensorListener;
import com.gattaca.team.ui.model.impl.BpmModel;
import com.gattaca.team.ui.utils.ActivityTransferData;
import com.gattaca.team.ui.view.Bpm;

import java.util.List;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MonitorBpm extends AppCompatActivity {
    private Bpm bpmGraph;
    private RealmResults<EmulatedBpm_5Min> results5;
    private RealmResults<EmulatedBpm_15Min> results15;
    private RealmChangeListener<RealmResults<EmulatedBpm_15Min>> listener15min =
            new RealmChangeListener<RealmResults<EmulatedBpm_15Min>>() {
                @Override
                public void onChange(RealmResults<EmulatedBpm_15Min> element) {
                    bpmGraph.addRealTimePoint(element.get(0).getValue(), element.get(0).getTime());
                }
            };
    private RealmChangeListener<RealmResults<EmulatedBpm_5Min>> listener5min =
            new RealmChangeListener<RealmResults<EmulatedBpm_5Min>>() {
                @Override
                public void onChange(RealmResults<EmulatedBpm_5Min> element) {
                    if (bpmGraph.addRealTimePoint(element.get(0).getValue(), element.get(0).getTime())) {
                        // collapse to bigger graph
                        results5.removeChangeListener(this);
                        results5 = null;
                        final BpmModel model = new BpmModel(true);
                        graph15min(model);
                        bpmGraph.install(model.fillPoints());
                    }
                }
            };

    private static void addColorRegions(final BpmModel model, long from, long to) {
        final List<BpmGreen> green = RealmController.getBpmGreenZone(
                AppUtils.createTimeFrom(from), AppUtils.createTimeFrom(to));
        for (BpmGreen item : green) {
            model.addGreenPoint(item.getValueTop(), item.getValueBottom(), item.getTime());
        }
        final List<BpmRed> red = RealmController.getBpmRedZone(
                AppUtils.createTimeFrom(from), AppUtils.createTimeFrom(to));
        for (BpmRed item : red) {
            model.addRedPoint(item.getValueTop(), item.getValueBottom(), item.getTime());
        }
    }

    private void graphHistory(final BpmModel model) {
        long requestedTimeStartSession = -1, colorsFrom, colorsTo;
        try {
            requestedTimeStartSession = (long) ActivityTransferData.getBindData(getIntent());
        } catch (Exception e) {
        }

        if (requestedTimeStartSession > 0) {
            final Session session = RealmController.getSessionFrom(requestedTimeStartSession);
            if (session == null) {
                Log.e(getClass().getSimpleName(), "incorrect time stump = " + requestedTimeStartSession);
                return;
            }
            colorsFrom = session.getTimeStart();
            colorsTo = session.getTimeFinish();
            final long timeSize = colorsTo - colorsFrom;
            if (timeSize < GraphPeriod.period_5min) {
                colorsTo = colorsFrom + 5 * DateUtils.MINUTE_IN_MILLIS;
                final List<BpmPoint_5_min> data = RealmController.getAllBpm5From(colorsFrom, colorsTo);
                for (BpmPoint_5_min item : data) {
                    model.addPoint(item.getValue(), item.getTime());
                }
                model.setPeriod(GraphPeriod.period_5min);
            } else if (timeSize < GraphPeriod.period_15min) {
                colorsTo = colorsFrom + 15 * DateUtils.MINUTE_IN_MILLIS;
                final List<BpmPoint_15_min> data = RealmController.getAllBpm15From(colorsFrom, colorsTo);
                for (BpmPoint_15_min item : data) {
                    model.addPoint(item.getValue(), item.getTime());
                }
                model.setPeriod(GraphPeriod.period_15min);
            } else {
                colorsTo = colorsFrom + 30 * DateUtils.MINUTE_IN_MILLIS;
                final List<BpmPoint_30_min> data = RealmController.getAllBpm30From(colorsFrom, colorsTo);
                for (BpmPoint_30_min item : data) {
                    model.addPoint(item.getValue(), item.getTime());
                }
                model.setPeriod(GraphPeriod.period_30min);
            }
        } else {
            final List<BpmPoint_30_min> data = RealmController.getAllBpm30From(
                    AppPref.FakeSessionStart.getLong(),
                    AppPref.FakeSessionStart.getLong() + 30 * DateUtils.MINUTE_IN_MILLIS);
            for (BpmPoint_30_min item : data) {
                model.addPoint(item.getValue(), item.getTime());
            }
            colorsFrom = data.get(0).getTime();
            colorsTo = data.get(data.size() - 1).getTime();
            model.setPeriod(GraphPeriod.period_30min);
        }
        addColorRegions(model, colorsFrom, colorsTo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_bpm);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        bpmGraph = (Bpm) findViewById(R.id.bpm);
    }

    @Override
    protected void onStart() {
        super.onStart();
        final BpmModel model = new BpmModel(RootSensorListener.isInProgress());

        if (RootSensorListener.isInProgress()) {
            results5 = RealmController.getEmulatedBpm();

            if (results5.size() >= BpmModel.pointsInRealTimeMode) {
                results5 = null;
                graph15min(model);
            } else {
                graph5min(model);
            }
        } else {
            graphHistory(model);
        }

        bpmGraph.install(model);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (results5 != null) {
            results5.removeChangeListener(listener5min);
            results5 = null;
        }
        if (results15 != null) {
            results15.removeChangeListener(listener15min);
            results15 = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void graph5min(final BpmModel model) {
        Log.d(getClass().getSimpleName(), "graph5min");
        results5.addChangeListener(listener5min);
        for (EmulatedBpm_5Min item : results5) {
            model.addPoint(item.getValue(), item.getTime());
        }
        long from = RealmController.getLastSession().getTimeStart();

        addColorRegions(model, from, from + 5 * DateUtils.MINUTE_IN_MILLIS);
    }

    private void graph15min(final BpmModel model) {
        Log.d(getClass().getSimpleName(), "graph15min");
        results15 = RealmController.getEmulated15Bpm();
        results15.addChangeListener(listener15min);

        for (EmulatedBpm_15Min item : results15) {
            model.addPoint(item.getValue(), item.getTime());
        }
        long from = RealmController.getLastSession().getTimeStart();
        addColorRegions(model, from, from + 15 * DateUtils.MINUTE_IN_MILLIS);
    }

}
