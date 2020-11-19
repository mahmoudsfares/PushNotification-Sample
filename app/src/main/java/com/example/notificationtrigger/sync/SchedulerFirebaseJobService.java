package com.example.notificationtrigger.sync;

import android.content.Context;
import android.os.AsyncTask;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

public class SchedulerFirebaseJobService extends JobService {

    private AsyncTask mTask;
    @Override
    public boolean onStartJob(final JobParameters job) {

        mTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                // work to be done in background
                Context context = SchedulerFirebaseJobService.this;
                TriggerTasks.executeTask(context, TriggerTasks.TRIGGER_NOTIFICATION);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                jobFinished(job, false);
            }
        };
        mTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mTask != null) mTask.cancel(true);
        return false;
    }
}
