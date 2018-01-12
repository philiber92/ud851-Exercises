package com.example.android.background.utilities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.android.background.MainActivity;
import com.example.android.background.R;

/**
 * Utility class for creating hydration notifications
 */
public class NotificationUtils {

    private static final int WATER_INTENT_ID = 559;

    private static final int WATER_NOTIFICATION_ID = 762;

    private static final String WATER_OREO_CHANNEL_ID = "Water-Reminder";

    public static void remindUserBecauseCharging(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    WATER_OREO_CHANNEL_ID,
                    context.getString(R.string.channel_name),
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, WATER_OREO_CHANNEL_ID);

        String contentBody = context.getString(R.string.charging_reminder_notification_body);

        notificationBuilder
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_drink_notification)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.charging_reminder_notification_title))
                .setContentText(contentBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(contentBody))
                .setDefaults(NotificationCompat.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN &&
                Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(WATER_NOTIFICATION_ID, notificationBuilder.build());
    }


    public static PendingIntent contentIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(
                context,
                WATER_INTENT_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    private static Bitmap largeIcon(Context context) {
        return BitmapFactory.decodeResource(
                context.getResources(),
                R.drawable.ic_local_drink_black_24px
        );
    }


}
