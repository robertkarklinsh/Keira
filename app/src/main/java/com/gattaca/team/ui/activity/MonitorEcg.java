package com.gattaca.team.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.gattaca.team.R;
import com.gattaca.team.ui.view.PressureBar;

import java.util.Random;

public class MonitorEcg extends AppCompatActivity {

    PressureBar pressureBar, pressureBar2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_ecg);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pressureBar = (PressureBar) findViewById(R.id.bar);
        pressureBar2 = (PressureBar) findViewById(R.id.bar2);
        pressureBar2.setType(PressureBar.PressureType.diastolic);
        findViewById(R.id.random).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pressureBar.setValue(40 + new Random().nextInt(170));
                pressureBar2.setValue(30 + new Random().nextInt(130));
            }
        });
        findViewById(R.id.random).performClick();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
