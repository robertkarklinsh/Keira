package com.gattaca.team.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.gattaca.team.R;
import com.gattaca.team.db.RealmController;
import com.gattaca.team.db.sensor.optimizing.SensorPoint_5_min;
import com.gattaca.team.root.AppUtils;
import com.gattaca.team.ui.model.impl.BpmModel;
import com.gattaca.team.ui.view.Bpm;

import java.util.ArrayList;
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
        final List<SensorPoint_5_min> data = RealmController.getStubSessionBpm30();
        final BpmModel model = new BpmModel();
        final int step = 30 / 5; //STUB!
        final ArrayList<Float> tmp = new ArrayList<>(data.size() / step);
        int stepping = 0;
        for (SensorPoint_5_min item : data) {
            tmp.add(item.getValue());
            if (++stepping >= step) {
                model.addPoint(AppUtils.convertListToAvrValue(tmp), item.getTime());
                tmp.clear();
                stepping = 0;
            }
        }
        if (!tmp.isEmpty()) {
            model.addPoint(AppUtils.convertListToAvrValue(tmp), data.get(data.size() - 1).getTime());
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
