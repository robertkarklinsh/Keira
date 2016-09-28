package com.gattaca.team.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.gattaca.team.R;
import com.gattaca.team.db.RealmController;
import com.gattaca.team.db.sensor.BpmGreen;
import com.gattaca.team.db.sensor.BpmRed;
import com.gattaca.team.db.sensor.EmulatedBpm;
import com.gattaca.team.db.sensor.optimizing.BpmPoint_30_min;
import com.gattaca.team.root.AppUtils;
import com.gattaca.team.service.main.RootSensorListener;
import com.gattaca.team.ui.model.impl.BpmModel;
import com.gattaca.team.ui.view.Bpm;

import java.util.List;

import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MonitorBpm extends AppCompatActivity implements RealmChangeListener<RealmResults<EmulatedBpm>> {
    private Bpm bpmGraph;
    private RealmResults<EmulatedBpm> results;

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
        long from = 0L, to = 0L;

        if (RootSensorListener.isInProgress()) {
            results = RealmController.getEmulatedBpm();
            results.addChangeListener(this);
            for (EmulatedBpm item : results) {
                model.addPoint(item.getValue(), item.getTime());
            }
            from = results.get(0).getTime();
            to = from + model.compileEndTime();
        } else {
            final List<BpmPoint_30_min> data = RealmController.getStubSessionBpm30();
            for (BpmPoint_30_min item : data) {
                model.addPoint(item.getValue(), item.getTime());
            }
            from = data.get(0).getTime();
            to = data.get(data.size() - 1).getTime();
        }

        final List<BpmGreen> green = RealmController.getStubSessionBpmGreen(
                AppUtils.createTimeFrom(from), AppUtils.createTimeFrom(to));
        for (BpmGreen item : green) {
            model.addGreenPoint(item.getValueTop(), item.getValueBottom(), item.getTime());
        }
        final List<BpmRed> red = RealmController.getStubSessionBpmRed(
                AppUtils.createTimeFrom(from), AppUtils.createTimeFrom(to));
        for (BpmRed item : red) {
            model.addRedPoint(item.getValueTop(), item.getValueBottom(), item.getTime());
        }
        bpmGraph.install(model);
    }

    protected void onStop() {
        super.onStop();
        if (results != null) {
            results.removeChangeListener(this);
            results = null;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChange(RealmResults<EmulatedBpm> element) {
        bpmGraph.addRealTimePoint(element.get(0).getValue(), element.get(0).getTime());
    }
}
