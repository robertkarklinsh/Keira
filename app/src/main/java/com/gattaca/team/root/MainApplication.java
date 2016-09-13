package com.gattaca.team.root;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.crashlytics.android.Crashlytics;
import com.gattaca.team.prefs.SharedPrefHelper;
import com.gattaca.team.service.IServiceConnection;
import com.gattaca.team.service.bitalino.BitalinoConnection;
import com.squareup.otto.Bus;

import io.fabric.sdk.android.Fabric;

public final class MainApplication extends Application {
    private static IServiceConnection serviceConnectionImpl;
    private static Context context;
    private static Bus uiBus = new Bus();
    private ActivityLifecycleCallbacks activityCallback = new ActivityLifecycleCallbacks() {
        @Override
        public void onActivityStarted(Activity activity) {
            uiBus.register(activity);
        }

        @Override
        public void onActivityStopped(Activity activity) {
            uiBus.unregister(activity);
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

    public static void uiBusPost(Object obj) {
        uiBus.post(obj);
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
        Fabric.with(this, new Crashlytics());
        context = this.getApplicationContext();
        serviceConnectionImpl = new BitalinoConnection();
        SharedPrefHelper.getInstance(context, "app");
        registerActivityLifecycleCallbacks(activityCallback);
    }
}
