package com.gattaca.team.service.fake;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.gattaca.team.prefs.AppPref;
import com.gattaca.team.root.MainApplication;

public final class FakeDataController extends HandlerThread implements Handler.Callback {
    private static FakeDataController instance;
    private Handler handler;

    private FakeDataController() {
        super("FakeDataController");
        start();
        waitUntilReady();
    }

    public static FakeDataController getInstance() {
        if (instance == null) {
            instance = new FakeDataController();
        }
        return instance;
    }

    public static void startGeneration() {
        getInstance().changeState(FakeMessage.Start);
    }

    private synchronized void waitUntilReady() {
        this.handler = new Handler(this.getLooper(), this);
    }

    private void changeState(@FakeMessage int state) {
        handler.sendEmptyMessage(FakeMessage.Start);
    }

    @Override
    public boolean handleMessage(Message msg) {
        final @FakeMessage int reason = msg.what;
        switch (reason) {
            case FakeMessage.Start:
                //TODO: add logic
                changeState(FakeMessage.Finish);
                break;
            case FakeMessage.Finish:
                AppPref.FakeGeneration.set(true);
                break;
        }
        MainApplication.uiBusPost(reason);
        return true;
    }
}
