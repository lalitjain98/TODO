package com.example.lalit.todo;

import android.app.PendingIntent;
import android.content.Intent;
import android.icu.util.Calendar;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Date;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        DelayAsyncTask delayAsyncTask = new DelayAsyncTask(new DelayAsyncTask.OnSleepListener() {
            @Override
            public void onSleep() {
                Intent intent = new Intent(StartActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        delayAsyncTask.execute();
//        Date date = new Date(System.currentTimeMillis());
//        try {
//            date.setTime(date.getTime()+5000);
//            while(System.currentTimeMillis() != date.getTime() ){
//                Log.d("Time", String.valueOf(System.currentTimeMillis()));
//            }
//        } catch (Exception e) {
//
//            e.printStackTrace();
//        }

    }
}
