package com.gattaca.team.service.tracker;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

import com.gattaca.team.R;
import com.gattaca.team.annotation.ModuleName;
import com.gattaca.team.annotation.NotifyType;
import com.gattaca.team.db.RealmController;
import com.gattaca.team.db.event.NotifyEventObject;
import com.gattaca.team.db.notification.NotificationRealm;
import com.gattaca.team.root.AppUtils;
import com.gattaca.team.ui.activity.MainActivity;
import com.gattaca.team.ui.tracker.v2.ModelDao;

import java.util.concurrent.atomic.AtomicLong;

import io.realm.Realm;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by artvl on 29.09.16.
 */
public class TimeNotification extends BroadcastReceiver {
    NotificationManager nm;
    static int notificationId = 1;


    public static long getTimeStamp(String type, long id) {
        final AtomicLong res = new AtomicLong(0L);
        Realm.getDefaultInstance().executeTransaction(realm -> {
                    NotificationRealm nr = realm.where(NotificationRealm.class).equalTo("type", type).equalTo("entityId", id).findFirst();
                    if (nr != null) {
                        res.set(nr.getTimeStamp());
                    }
                }
        );

        return res.get();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setSmallIcon(R.drawable.fucking_perncil);
        String notification = "нотификация";
        if (intent.hasExtra(NOTIFICATION_TEXT)) {
            notification = intent.getStringExtra(NOTIFICATION_TEXT);
        }
        mBuilder.setContentTitle(notification);
        Intent intentTL = new Intent(context, MainActivity.class);

        mBuilder.setContentIntent(PendingIntent.getActivity(context,
                0,
                intentTL,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT))
                .setAutoCancel(true);


        int mNotificationId = notificationId;
        notificationId++;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());

        NotifyEventObject neo = new NotifyEventObject().setModuleNameResId(ModuleName.Tracker).setTime(ModelDao.getTimeInMillis());
        neo.setEventType(NotifyType.Tracker_reminder).realData();
        RealmController.getRealm().executeTransaction((Realm r) -> {
            r.copyToRealmOrUpdate(neo);
        });
    }


    private final static String NOTIFICATION_TEXT = "nText";
    public static void setAlarm(Context context, long time, String action, long id) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent i = new Intent(context, TimeNotification.class);
        if (action.equals("intake")) {
            i.putExtra(NOTIFICATION_TEXT, "Примите лекарство");
        } else {
            i.putExtra(NOTIFICATION_TEXT, "Померяйте давление");
        }
        i.setAction("com.gattaca.team." + action + id);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        am.set(AlarmManager.RTC_WAKEUP, time, pi);

        NotificationRealm nr = new NotificationRealm();
        nr.setPrimaryKey(AppUtils.generateUniqueId());
        nr.setType(action);
        nr.setEntityId(id);
        nr.setTimeStamp(time);
        Realm.getDefaultInstance().copyToRealmOrUpdate(nr);


    }

    public static void removeAlarm(Context context, String action, long id) {
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, TimeNotification.class);
        i.setAction("com.gattaca.team." + action + id);
        Realm.getDefaultInstance().executeTransaction(realm -> {
            realm.where(NotificationRealm.class).equalTo("type", action).equalTo("entityId", id).findAll().deleteAllFromRealm();

        });

        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);

        am.cancel(pi);
    }


}