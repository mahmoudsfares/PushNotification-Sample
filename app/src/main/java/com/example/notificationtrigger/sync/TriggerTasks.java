package com.example.notificationtrigger.sync;

import android.content.Context;
import com.example.notificationtrigger.utils.NotificationUtils;

public class TriggerTasks {

    public static final String DISMISS_NOTIFICATION = "dismiss-notification";
    public static final String TRIGGER_NOTIFICATION = "trigger-notification";

    public static void executeTask(Context context, String action){
        if(DISMISS_NOTIFICATION.equals(action)){
            NotificationUtils.clearAllNotifications(context);
        }
        if(TRIGGER_NOTIFICATION.equals(action)){
            NotificationUtils.createNotification(context);
        }
    }
}
