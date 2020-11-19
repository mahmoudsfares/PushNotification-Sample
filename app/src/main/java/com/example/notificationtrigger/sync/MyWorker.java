package com.example.notificationtrigger.sync;

import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class MyWorker extends Worker {

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    /*
    this is a simulation for background work, a countdown starts at a given number and this number decrements every second.
    the process runs off of the main thread.
     */
    public Result doWork() {
        // this is like an intent bundle, if you want to pass data from the activity to the Worker
        // in this app, the passed int is the number where we start countdown
        Data inputData = getInputData();
        int number = inputData.getInt("number", -1);

        for(int i = number; i >= 0; i--){
            Log.d("logmsg", i+"");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return Result.failure();
            }
        }

        TriggerTasks.executeTask(getApplicationContext(), TriggerTasks.TRIGGER_NOTIFICATION);
        return Result.success();
    }
}
