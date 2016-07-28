package com.gattaca.bitalinoecgchart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    RealTimeChart chart;
    BitalinoUniversal bitalino = null;
    Thread startBitalinoThread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chart = new RealTimeChart(this);
        chart.init();

        bitalino = new BitalinoUniversal(this, 2);
        startBitalinoThread = bitalino.start();

        Thread realTimeThread = new Thread(new Runnable() {
            SimpleECG currentECG = null;

            @Override
            public void run() {
                try {
                    startBitalinoThread.join();
                } catch (InterruptedException e) {}
                while(true) {
                    currentECG = bitalino.get();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            chart.addData((float) currentECG.get());
                        }
                    });
                }
            }
        });
        realTimeThread.start();
    }
}
