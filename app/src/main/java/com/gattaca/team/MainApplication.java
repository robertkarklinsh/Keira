package com.gattaca.team;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.gattaca.team.prefs.SharedPrefHelper;
import com.gattaca.team.service.IServiceConnection;
import com.gattaca.team.service.bitalino.BitalinoConnection;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class MainApplication extends Application {
    private static IServiceConnection serviceConnectionImpl;
    private static Context context;
    private static Bus bus = new Bus(ThreadEnforcer.ANY);
    private ActivityLifecycleCallbacks activityCallback = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityStarted(Activity activity) {
            busRegister(activity);
        }

        @Override
        public void onActivityStopped(Activity activity) {
            busUnregister(activity);
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    };

    public static void busRegister(Object obj) {
        bus.register(obj);
    }
    public static void busUnregister(Object obj) {
        bus.unregister(obj);
    }
    public static void busPost(Object obj) {
        bus.post(obj);
    }

    public static IServiceConnection getServiceConnectionImpl() {
        return serviceConnectionImpl;
    }

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
        serviceConnectionImpl = new BitalinoConnection();
        SharedPrefHelper.getInstance(context, "app");
        registerActivityLifecycleCallbacks(activityCallback);
    }
}
