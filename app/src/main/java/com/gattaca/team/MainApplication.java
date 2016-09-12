package com.gattaca.team;


import android.app.Application;

import com.gattaca.team.service.bitalino.BitalinoConnection;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BitalinoConnection.init();
    }


}
