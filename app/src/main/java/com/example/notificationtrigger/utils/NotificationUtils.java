package com.example.notificationtrigger.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.example.notificationtrigger.MainActivity;
import com.example.notificationtrigger.R;
import com.example.notificationtrigger.sync.TriggerIntentService;
import com.example.notificationtrigger.sync.TriggerTasks;

public class NotificationUtils {

    private static final String NOTIFICATION_CHANNEL_ID = "notif-channel";
    private static final String NOTIFICATION_CHANNEL_NAME = "Primary";
    private static final int PENDING_INTENT_ID = 1991;
    private static final int NOTIFICATION_ID = 500;
    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 24;

    public static void clearAllNotifications(Context context){
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static void createNotification(Context context){
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                    );
            notificationManager.createNotificationChannel(notificationChannel);
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID);
        builder.setAutoCancel(true)
                .setContentText(context.getString(R.string.notification_text))
                .setContentTitle(context.getString(R.string.notification_title))
                .setContentIntent(contentIntent(context))
                .setSmallIcon(R.drawable.ic_launcher_background)
                .addAction(dismissNotification(context));

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    private static PendingIntent contentIntent(Context context){
        Intent toMainActivity = new Intent(context, MainActivity.class);
        return PendingIntent.getActivity(
                context,
                PENDING_INTENT_ID,
                toMainActivity,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static NotificationCompat.Action dismissNotification (Context context){
        // accessed
        // TriggerIntentService constructor is not triggered
        Intent intent = new Intent(context, TriggerIntentService.class);
        intent.setAction(TriggerTasks.DISMISS_NOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getService(
                context,
                ACTION_IGNORE_PENDING_INTENT_ID,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Action dismissNotificationAction = new NotificationCompat.Action(
                R.drawable.ic_launcher_foreground,
                context.getString(R.string.notification_action_dismiss),
                pendingIntent);
        return dismissNotificationAction;
    }
}
