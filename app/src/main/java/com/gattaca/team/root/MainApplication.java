package com.gattaca.team.root;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.gattaca.bitalinoecgchart.tracker.model.Week;
import com.gattaca.bitalinoecgchart.tracker.v2.StubWeekCreator;
import com.gattaca.team.R;
import com.gattaca.team.db.RealmController;
import com.gattaca.team.prefs.SharedPrefHelper;
import com.gattaca.team.service.main.RootSensorListener;
import com.squareup.otto.Bus;

import java.util.GregorianCalendar;

import io.fabric.sdk.android.Fabric;
import io.realm.Realm;

public final class MainApplication extends Application {
    private static Context context;
    private static Bus uiBus = new MainThreadBus();
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

    public static Context getContext() {
        return context;
    }

    public static void showToastNotImplemented() {
        showToast(context.getResources().getString(R.string.toast_not_implemented));
    }

    public static void showToast(final String text) {
        Toast t = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        t.setDuration(Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        context = this.getApplicationContext();
        SharedPrefHelper.getInstance(context, "app");
        registerActivityLifecycleCallbacks(activityCallback);
        RealmController.with(this);
        RootSensorListener.getInstance();
        Realm realm = Realm.getDefaultInstance();
        if (realm.where(Week.class)
                .equalTo(Week.getNamedFieldWeekNum(),new GregorianCalendar().get(GregorianCalendar.WEEK_OF_YEAR))
                .findFirst() == null) {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Week week = realm.createObject(Week.class, new GregorianCalendar().get(GregorianCalendar.WEEK_OF_YEAR));
                    new StubWeekCreator(week, realm).fillStubWeek();
                }
            });
        }
    }
}
