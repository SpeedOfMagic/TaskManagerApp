package team13.taskmanagerapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import team13.taskmanagerapp.MainActivity;
import team13.taskmanagerapp.R;

/**
 * Created by anton on 23.01.2018.
 */

public class TimeNotification extends BroadcastReceiver {
    private static final int NOTIFY_ID = 101;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Calendar stamp = Calendar.getInstance();

        NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder notification = new Notification.Builder(context);

        notification.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_img_notif)
                .setContentTitle("TaskScheduler")
                .setContentText("Уведомление о новой задаче")
                .setWhen(stamp.getTimeInMillis())
                .setAutoCancel(true);

        nm.notify(NOTIFY_ID, notification.build());

        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + AlarmManager.INTERVAL_DAY, pendingIntent);
    }
}
