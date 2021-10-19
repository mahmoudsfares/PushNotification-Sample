package com.example.notificationtrigger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notificationtrigger.sync.MyWorker;
import com.example.notificationtrigger.utils.JobSchedulerUtils;

public class MainActivity extends AppCompatActivity {

    EditText minutesET;
    TextView minutesTV;
    TextView secondsTV;

    private long mEndTimeInMillis;
    private long mRemainingMillis;
    private boolean mTimerRunning;

    private CountDownTimer mCtdTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        minutesET = findViewById(R.id.et_minutes);
        minutesTV = findViewById(R.id.tv_remaining_minutes);
        secondsTV = findViewById(R.id.tv_remaining_seconds);

        if(mTimerRunning) startTimer(mRemainingMillis);
        else clearTimer();

        Button triggerBtn = findViewById(R.id.btn_trigger);
        triggerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int minutes = Integer.parseInt(minutesET.getText().toString());
                Toast.makeText(MainActivity.this,
                        "a notification will appear in " + minutes +
                                ((minutes != 1) ? " minutes" : " minute"),
                        Toast.LENGTH_SHORT)
                        .show();

                // in case of wanting the notification to be triggered at an exact time (DEPRECATED) , uncomment this

//                 JobSchedulerUtils.scheduleNotification(MainActivity.this, minutes);


                // in case of allowing some delay (RECOMMENDED), uncomment the following

//                // set data as extras when using Worker
//                Data data = new Data.Builder()
//                        .putInt("number", minutes*60)
//                        .build();
//
//                OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
//                        // add data bundle
//                        .setInputData(data)
//                        // add tag
//                        .addTag("timer")
//                        .build();
//                // use WorkManager by passing the WorkRequest
//                WorkManager.getInstance(MainActivity.this).enqueue(oneTimeWorkRequest);

                mRemainingMillis = (long) minutes *1000*60;
                startTimer(mRemainingMillis);
            }
        });
    }

    private void startTimer(long remainingMillis){

        if(mCtdTimer != null) mCtdTimer.cancel();
        mEndTimeInMillis =  System.currentTimeMillis() + remainingMillis;
        mCtdTimer = new CountDownTimer(remainingMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mRemainingMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                mTimerRunning = false;
            }
        }.start();

        mTimerRunning = true;
    }

    private void updateCountDownText() {
        int minutes = (int) (mRemainingMillis / 1000) / 60;
        int seconds = (int) (mRemainingMillis / 1000) % 60;
        minutesTV.setText(String.valueOf(minutes));
        secondsTV.setText(String.valueOf(seconds));
    }

    private void clearTimer(){
        minutesTV.setText("0");
        secondsTV.setText("0");
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("millisLeft", mRemainingMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTimeInMillis);
        editor.apply();
        if (mCtdTimer != null) {
            mCtdTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        mRemainingMillis = prefs.getLong("millisLeft", 0);
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        updateCountDownText();
        if (mTimerRunning) {
            mEndTimeInMillis = prefs.getLong("endTime", 0);
            mRemainingMillis = mEndTimeInMillis - System.currentTimeMillis();
            if (mRemainingMillis < 0) {
                mRemainingMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
            } else {
                startTimer(mRemainingMillis);
            }
        }
    }

}
