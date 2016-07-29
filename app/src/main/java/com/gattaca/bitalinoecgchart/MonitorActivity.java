package com.gattaca.bitalinoecgchart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MonitorActivity extends AppCompatActivity {

    RealTimeChart chart;
    BitalinoUniversal bitalino = null;
    Thread startBitalinoThread = null;
    static final int PERIOD = 50;
    static final String TAG = MonitorActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor);

        chart = new RealTimeChart(this);
        chart.init();

        bitalino = new BitalinoUniversal(this, 2);
        startBitalinoThread = bitalino.start();

        Thread realTimeThread = new Thread(new Runnable() {
            SimpleECG currentECG = null;
            int i;

            @Override
            public void run() {
                Log.e(TAG, "I am in realTime.");
                try {
                    startBitalinoThread.join();
                } catch (InterruptedException e) {}
                Log.e(TAG, "Now we ready to go!");

                if (bitalino.isConnected.get() == true) {
                    while (true) {
                        currentECG = bitalino.get();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                chart.addData((float) currentECG.get());
                            }
                        });
                    }
                } else {
                    for (i = 0; ; i = (i + 1) % PERIOD) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                chart.addData((float)Math.sin(2.0 * Math.PI * i / PERIOD));
                            }
                        });
                        try {
                            Thread.sleep(25);
                        } catch (InterruptedException e) {}
                    }
                }
            }
        });
        realTimeThread.start();
    }
}
