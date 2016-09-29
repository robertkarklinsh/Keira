package com.gattaca.team.service.tracker;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.gattaca.team.ui.activity.MainActivity;

/**
 * Created by artvl on 29.09.16.
 */
public class TimeNotification extends BroadcastReceiver {
    NotificationManager nm;
    @Override
    public void onReceive(Context context, Intent intent) {
//        nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification notification = new Notification.Builder.(R.drawable.icon, "Test", System.currentTimeMillis());
////Интент для активити, которую мы хотим запускать при нажатии на уведомление
//        Intent intentTL = new Intent(context, MainActivity.class);
//        notification.setLatestEventInfo(context, "Test", "Do something!",
//                PendingIntent.getActivity(context, 0, intentTL,
//                        PendingIntent.FLAG_CANCEL_CURRENT));
//        notification.flags = Notification.DEFAULT_LIGHTS | Notification.FLAG_AUTO_CANCEL;
//        nm.notify(1, notification);
//// Установим следующее напоминание.
//        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
//                intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    AlarmManager am;
    private void restartNotify() {
//        am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(this, TimeNotification.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0,
//                intent, PendingIntent.FLAG_CANCEL_CURRENT );
//// На случай, если мы ранее запускали активити, а потом поменяли время,
//// откажемся от уведомления
//        am.cancel(pendingIntent);
//// Устанавливаем разовое напоминание
//        am.set(AlarmManager.RTC_WAKEUP, stamp.getTime(), pendingIntent);
    }
}