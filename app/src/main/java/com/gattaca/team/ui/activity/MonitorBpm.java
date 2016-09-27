package com.gattaca.team.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.gattaca.team.R;
import com.gattaca.team.db.RealmController;
import com.gattaca.team.db.sensor.BpmGreen;
import com.gattaca.team.db.sensor.optimizing.BpmPoint_30_min;
import com.gattaca.team.ui.model.impl.BpmModel;
import com.gattaca.team.ui.view.Bpm;

import java.util.List;

public class MonitorBpm extends AppCompatActivity {
    private Bpm bpmGraph;

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
        final List<BpmPoint_30_min> data = RealmController.getStubSessionBpm30();
        final BpmModel model = new BpmModel();
        for (BpmPoint_30_min item : data) {
            model.addPoint(item.getValue(), item.getTime());
        }

        final List<BpmGreen> green = RealmController.getStubSessionBpmGreen(
                BpmGreen.createTimeFrom(data.get(0).getTime()),
                BpmGreen.createTimeFrom(data.get(data.size() - 1).getTime()));
        for (BpmGreen item : green) {
            model.addGreenPoint(item.getValueTop(), item.getValueBottom(), item.getTime());
        }
        bpmGraph.install(model);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
