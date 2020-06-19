package com.example.notificationtrigger.sync;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;

public class TriggerIntentService extends IntentService {

    public TriggerIntentService() {
        super("TriggerIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();
        TriggerTasks.executeTask(this, action);
    }
}
