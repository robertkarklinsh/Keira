package com.gattaca.team.db;

import android.app.Activity;
import android.app.Application;
import android.text.format.DateUtils;
import android.util.Log;

import com.gattaca.team.db.event.NotifyEventObject;
import com.gattaca.team.db.sensor.BpmGreen;
import com.gattaca.team.db.sensor.BpmRed;
import com.gattaca.team.db.sensor.RR;
import com.gattaca.team.db.sensor.SensorPointData;
import com.gattaca.team.db.sensor.Session;
import com.gattaca.team.db.sensor.emulate.EmulatedBpm_15Min;
import com.gattaca.team.db.sensor.emulate.EmulatedBpm_30Min;
import com.gattaca.team.db.sensor.emulate.EmulatedBpm_5Min;
import com.gattaca.team.db.sensor.optimizing.BpmPoint_15_min;
import com.gattaca.team.db.sensor.optimizing.BpmPoint_1_hour;
import com.gattaca.team.db.sensor.optimizing.BpmPoint_30_min;
import com.gattaca.team.db.sensor.optimizing.BpmPoint_5_min;
import com.gattaca.team.db.tracker.Week;

import java.util.GregorianCalendar;
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
        Log.e("RealmController", "=== clear realm data ===");
        final Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(RR.class);
        realm.delete(NotifyEventObject.class);
        realm.delete(SensorPointData.class);
        realm.delete(BpmPoint_1_hour.class);
        realm.delete(BpmPoint_5_min.class);
        realm.delete(BpmPoint_15_min.class);
        realm.delete(BpmPoint_30_min.class);
        realm.delete(Session.class);
        realm.delete(BpmGreen.class);
        realm.commitTransaction();
        clearEmulate();
    }

    public static void removeSession(long timeStart) {
        Log.d("RealmController", "close session");
        RealmController.clearEmulate();
        final Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final Session session = realm
                .where(Session.class)
                .equalTo(Session.getNamedFieldTimeStart(), timeStart)
                .findFirst();
        session.deleteFromRealm();
        realm.commitTransaction();
    }

    public static void finishLastSession() {
        Log.d("RealmController", "close session");
        RealmController.clearEmulate();
        final Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final Session session = RealmController.getLastSession();
        session.finishSession();
        if (session.getTimeFinish() - session.getTimeStart() < 2 * DateUtils.SECOND_IN_MILLIS) {
            session.deleteFromRealm();
            Log.d("RealmController", "remove session because low time");
        }
        realm.commitTransaction();
    }

    public static void save(RealmModel item) {
        if (item != null) {
            final Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(item);
            realm.commitTransaction();
            Log.v(item.getClass().getSimpleName(), "saved " + item.toString());
        }
    }

    public static void saveList(List<? extends RealmModel> list) {
        if (list != null && !list.isEmpty()) {
            final Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(list);
            realm.commitTransaction();

            for (RealmModel a : list) {
                if (!(a instanceof SensorPointData)) {
                    Log.v(a.getClass().getSimpleName(), "saved " + a.toString());
                }
            }
        }
    }

    public static void clearEmulate() {
        Log.e("RealmController", "=== clear emulate data ===");
        final Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(EmulatedBpm_5Min.class);
        realm.delete(EmulatedBpm_15Min.class);
        realm.delete(EmulatedBpm_30Min.class);
        realm.commitTransaction();
    }

    public static RealmResults<EmulatedBpm_5Min> getEmulatedBpm() {
        return Realm
                .getDefaultInstance()
                .where(EmulatedBpm_5Min.class)
                .findAll();
    }

    public static RealmResults<EmulatedBpm_15Min> getEmulated15Bpm() {
        return Realm
                .getDefaultInstance()
                .where(EmulatedBpm_15Min.class)
                .findAll();
    }

    public static RealmResults<BpmPoint_5_min> getAllBpm5From(final long from, final long to) {
        return Realm.getDefaultInstance()
                .where(BpmPoint_5_min.class)
                .greaterThanOrEqualTo(BpmPoint_5_min.getNamedFieldTime(), from)
                .lessThanOrEqualTo(BpmPoint_5_min.getNamedFieldTime(), to)
                .findAll()
                .sort(BpmPoint_5_min.getNamedFieldTime(), Sort.ASCENDING);
    }

    public static RealmResults<BpmPoint_15_min> getAllBpm15From(final long from, final long to) {
        return Realm.getDefaultInstance()
                .where(BpmPoint_15_min.class)
                .greaterThanOrEqualTo(BpmPoint_15_min.getNamedFieldTime(), from)
                .lessThanOrEqualTo(BpmPoint_5_min.getNamedFieldTime(), to)
                .findAll()
                .sort(BpmPoint_15_min.getNamedFieldTime(), Sort.ASCENDING);
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
                .notEqualTo(Session.getNamedFieldTimeFinish(), Session.LAST_TIME_FLAG)
                .findAll()
                .sort(Session.getNamedFieldTimeStart(), Sort.DESCENDING);
    }


    public static RealmResults<BpmPoint_30_min> getAllBpm30From(long from, long to) {
        return Realm.getDefaultInstance()
                .where(BpmPoint_30_min.class)
                .greaterThanOrEqualTo(BpmPoint_30_min.getNamedFieldTime(), from)
                .lessThanOrEqualTo(BpmPoint_30_min.getNamedFieldTime(), to)
                .findAll()
                .sort(BpmPoint_30_min.getNamedFieldTime(), Sort.ASCENDING);
    }

    public static RealmResults<BpmGreen> getBpmGreenZone(long from, long to) {
        return Realm.getDefaultInstance()
                .where(BpmGreen.class)
                .greaterThanOrEqualTo(BpmGreen.getNamedFieldTime(), from)
                .lessThanOrEqualTo(BpmGreen.getNamedFieldTime(), to)
                .findAll()
                .sort(BpmGreen.getNamedFieldTime(), Sort.ASCENDING);
    }

    public static RealmResults<BpmRed> getBpmRedZone(long from, long to) {
        return Realm.getDefaultInstance()
                .where(BpmRed.class)
                .greaterThanOrEqualTo(BpmGreen.getNamedFieldTime(), from)
                .lessThanOrEqualTo(BpmGreen.getNamedFieldTime(), to)
                .findAll()
                .sort(BpmGreen.getNamedFieldTime(), Sort.ASCENDING);
    }

    public static SensorPointData getStubSensorPoint(int sample) {
        return Realm.getDefaultInstance()
                .where(SensorPointData.class)
                .equalTo(SensorPointData.getNamedFieldSample(), sample)
                .findFirst();
    }

    public static Realm getRealm() {
        return Realm.getDefaultInstance();
    }

    public static Week getCurrentWeek() {
        return Realm.getDefaultInstance()
                .where(Week.class)
                .equalTo(Week.getNamedFieldWeekNum(), new GregorianCalendar().get(GregorianCalendar.WEEK_OF_YEAR)).findFirst();
    }

    public static Session getLastSession() {
        return Realm.getDefaultInstance()
                .where(Session.class)
                .equalTo(Session.getNamedFieldTimeFinish(), Session.LAST_TIME_FLAG)
                .findFirst();
    }

    public static Session getSessionFrom(final long time) {
        return Realm.getDefaultInstance()
                .where(Session.class)
                .equalTo(Session.getNamedFieldTimeStart(), time)
                .findFirst();
    }
}
