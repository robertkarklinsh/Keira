package com.gattaca.team.db;

import android.app.Activity;
import android.app.Application;

import com.gattaca.team.db.event.NotifyEventObject;
import com.gattaca.team.db.sensor.RR;
import com.gattaca.team.db.sensor.SensorPointData;
import com.gattaca.team.db.sensor.Session;
import com.gattaca.team.db.sensor.optimizing.SensorPoint_1_hour;
import com.gattaca.team.db.sensor.optimizing.SensorPoint_5_min;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmResults;
import io.realm.Sort;

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
        realm.delete(SensorPoint_1_hour.class);
        realm.delete(SensorPoint_5_min.class);
        realm.delete(Session.class);
        realm.commitTransaction();
    }
    public static void save(RealmModel item) {
        // final long time = System.currentTimeMillis();
        final Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(item);
        realm.commitTransaction();

        //  Log.d(RealmController.class.getSimpleName(), "time=" + (System.currentTimeMillis() - time) + " ms");
    }

    public static void saveList(List<? extends RealmModel> list) {
        //final long time = System.currentTimeMillis();
        final Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(list);
        realm.commitTransaction();

        //Log.d(RealmController.class.getSimpleName(), "size= " + list.size() + " time=" + (System.currentTimeMillis() - time) + " ms");
        //for (RealmModel item : list) {
        // Log.d(RealmController.class.getSimpleName(), item.toString());
        //}
    }

    public static RealmResults<NotifyEventObject> getAllEvents() {
        return Realm
                .getDefaultInstance()
                .where(NotifyEventObject.class)
                .findAll()
                .sort(NotifyEventObject.getNamedFieldTime(), Sort.DESCENDING);
    }

    public static RealmResults<Session> getAllSessions() {
        return Realm.getDefaultInstance()
                .where(Session.class)
                .findAll();
    }

    public static RealmResults<SensorPoint_5_min> getStubSessionBpm30() {
        return Realm.getDefaultInstance()
                .where(SensorPoint_5_min.class)
                .findAll();
    }
}
