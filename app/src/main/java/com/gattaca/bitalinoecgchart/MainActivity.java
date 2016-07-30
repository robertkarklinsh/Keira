package com.gattaca.bitalinoecgchart;

import android.os.Bundle;

import com.gattaca.bitalinoecgchart.navigation.NaviagableActivity;

public class MainActivity extends NaviagableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);


    }
}
