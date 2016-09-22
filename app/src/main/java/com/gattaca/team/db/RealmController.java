package com.gattaca.team.db;

import android.app.Activity;
import android.app.Application;

import com.gattaca.team.db.event.NotifyEventObject;
import com.gattaca.team.db.sensor.RR;
import com.gattaca.team.db.sensor.SensorPointData;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;

public final class RealmController {
    private static RealmController instance;
    //private final Realm realm;

    /**
     * =================================================================================================
     * Default constructor for work with controller
     * ===============================================================================================
     */
    private RealmController(Application application) {
        Realm.setDefaultConfiguration(
                new RealmConfiguration.Builder(application)
                        .name("gattaka.application")
                        .schemaVersion(0)
                        .deleteRealmIfMigrationNeeded()
                        .build());
    }

    static RealmController getInstance() {
        return instance;
    }

    /**
     * =================================================================================================
     * attach controller to
     * ===============================================================================================
     */
    public static RealmController with(Activity activity) {
        if (instance == null) {
            instance = new RealmController(activity.getApplication());
        }
        return instance;
    }

    public static RealmController with(Application application) {
        if (instance == null) {
            instance = new RealmController(application);
        }
        return instance;
    }


    /**=================================================================================================
     * Private methods for using package objects ONLY
     * ===============================================================================================*/

    /**
     * =================================================================================================
     * Static methods for work with controller
     * ===============================================================================================
     */
    public static void clearAll() {
        final Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(RR.class);
        realm.delete(NotifyEventObject.class);
        realm.delete(SensorPointData.class);
        realm.commitTransaction();
    }
    public static void save(RealmModel item) {
        final long time = System.currentTimeMillis();
        final Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(item);
        realm.commitTransaction();

        //  Log.d(RealmController.class.getSimpleName(), "time=" + (System.currentTimeMillis() - time) + " ms");
    }

    public static void saveList(List<? extends RealmModel> list) {
        final long time = System.currentTimeMillis();
        final Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(list);
        realm.commitTransaction();

        //Log.d(RealmController.class.getSimpleName(), "size= " + list.size() + " time=" + (System.currentTimeMillis() - time) + " ms");
        //for (RealmModel item : list) {
        // Log.d(RealmController.class.getSimpleName(), item.toString());
        //}
    }
}
