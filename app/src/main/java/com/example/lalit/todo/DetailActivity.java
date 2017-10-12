package com.example.lalit.todo;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.SystemClock;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DetailActivity extends AppCompatActivity {
    EditText titleView, descriptionView;
    TextView dateView, timeView;
    Button dateSetterButtonView, timeSetterButtonView;
    int id;
    Intent intent;
    int DETAIL_MODE;
    Long epochAlarmTime = 0L;
//    public static final String MY_ALARM_ACTION = "my_alarm_action";
//    LocalBroadcastManager broadcastManager;
//    BroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        intent= getIntent();
        DETAIL_MODE = intent.getIntExtra(Constants.KEY_DETAIL_MODE,Constants.KEY_ADD);
        if(DETAIL_MODE == Constants.KEY_EDIT){
            id = intent.getIntExtra(Constants.KEY_ID,0);
            TodoOpenHelper todoOpenHelper = TodoOpenHelper.getInstance(this);
            SQLiteDatabase todoDB = todoOpenHelper.getReadableDatabase();
            Cursor cursor = todoDB.query(Contracts.TODO_TABLE_NAME,null,Contracts.TODO_ID + " = ? ",new String[]{String.valueOf(id)},null,null,null);
            cursor.moveToFirst();
            String title = cursor.getString(cursor.getColumnIndex(Contracts.TODO_TITLE));
            //long dateCreated = cursor.getLong(cursor.getColumnIndex(Contracts.TODO_DATE_CREATED));
            String description = cursor.getString(cursor.getColumnIndex(Contracts.TODO_DESCRIPTION));
            Long long_db_date = cursor.getLong(cursor.getColumnIndex(Contracts.TODO_DATE_ALARM));
//            String string_db_date = String.valueOf(long_db_date);
//            //Long db_date = 0L;
//            //Log.d("db",string_db_date);
//            if(!string_db_date.equals((String.valueOf(0L)))){
//                db_date = Long.parseLong(string_db_date);
//            }
            Date date = null ;
            String alarmDateOld = "";
            //Log.d("Alarm", string_db_date + "   " +db_date);
            if(long_db_date != 0L ){
                date = new Date(long_db_date);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                alarmDateOld = dateFormat.format(date);
                Log.d("Date",alarmDateOld);
                Log.d("Epoch time",cursor.getString(cursor.getColumnIndex(Contracts.TODO_DATE_ALARM)));
            }
            cursor.close();
            titleView = (EditText)findViewById(R.id.newTitle);
            descriptionView = (EditText)findViewById(R.id.newDescription);
            titleView.setText(title);
            descriptionView.setText(description);
            dateView = (TextView) findViewById(R.id.dateView);
            timeView = (TextView) findViewById(R.id.timeView);
            if(long_db_date != 0L){
                dateView.setText(alarmDateOld.substring(0,10));
                timeView.setText(alarmDateOld.substring(11));
            }
        }
        dateSetterButtonView = (Button) findViewById(R.id.dateSetterButtonView);
        dateSetterButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog;
                Calendar mCurrentDate = Calendar.getInstance();
                int year = mCurrentDate.get(Calendar.YEAR);
                int monthOfYear = mCurrentDate.get(Calendar.MONTH);
                int dayOfMonth = mCurrentDate.get(Calendar.DAY_OF_MONTH);
                dialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                        dateView = (TextView) findViewById(R.id.dateView);
                        dateView.setText(date+"/"+(month+1)+"/"+year);
                    }
                },year,monthOfYear,dayOfMonth);
                dialog.show();
                /*dialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int montth, int date) {
                        dateView.setText(date+"/"+(montth+1)+"/"+year);
                    }
                });*/
            }
        });
        timeSetterButtonView = (Button) findViewById(R.id.timeSetterButtonView);
        timeSetterButtonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mCurrentTime = Calendar.getInstance();
                int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mCurrentTime.get(Calendar.MINUTE);
                TimePickerDialog dialog = new TimePickerDialog(view.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        timeView = (TextView) findViewById(R.id.timeView);
                        timeView.setText(hour +":"+minute);
                    }
                },hour,minute,true);
                dialog.show();
            }
        });
        AlarmManager alarmManager = ((AlarmManager) getSystemService(ALARM_SERVICE));
        Intent alarmIntent = new Intent(DetailActivity.this,AlarmReceiver.class);
        alarmIntent.putExtra(Constants.KEY_TODO_ALARM_TITLE,alarmTitle);
//            alarmIntent.putExtra(Constants.KEY_TODO_ALARM_TIME,timeValue);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,alarmIntent,0);
        pendingIntent.cancel();
        Log.d("Alarm","Previous Alarm Cancelled");
    }

    String alarmTitle = null;
    public void saveButtonClicked(View view){
        titleView = (EditText)findViewById(R.id.newTitle);
        descriptionView = (EditText)findViewById(R.id.newDescription);
        String title = titleView.getEditableText().toString();
        alarmTitle = title;
        String description = descriptionView.getEditableText().toString();
        dateView = (TextView) findViewById(R.id.dateView);
        timeView = (TextView) findViewById(R.id.timeView);
        String alarmDate = dateView.getText().toString();
        String alarmTime = timeView.getText().toString();
        if (!(alarmDate.equals("") || alarmTime.equals(""))) {
            String[] dateValues = alarmDate.split("/");
            int year = Integer.parseInt(dateValues[2]);
            int month = Integer.parseInt(dateValues[1])-1;
            int dayOfMonth = Integer.parseInt(dateValues[0]);
            String[] timeValues = alarmTime.split(":");
            int hour = Integer.parseInt(timeValues[0]);
            int minute = Integer.parseInt(timeValues[1]);
            Calendar calendar = Calendar.getInstance();
            calendar.set(year,month,dayOfMonth,hour,minute);
            epochAlarmTime = calendar.getTimeInMillis();
            //Log
//            Calendar cal = Calendar.getInstance();
//            cal.setTimeInMillis(epochAlarmTime);
//            String timeValue = cal.getTime().toString();
//            Log.d("Alarm", alarmTitle+ " " + timeValue);
            AlarmManager alarmManager = ((AlarmManager) getSystemService(ALARM_SERVICE));
            Intent alarmIntent = new Intent(DetailActivity.this,AlarmReceiver.class);
            alarmIntent.putExtra(Constants.KEY_TODO_ALARM_TITLE,alarmTitle);
//            alarmIntent.putExtra(Constants.KEY_TODO_ALARM_TIME,timeValue);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,alarmIntent,0);
            alarmManager.set(AlarmManager.RTC_WAKEUP,epochAlarmTime,pendingIntent);
        }

        TodoOpenHelper todoOpenHelper = TodoOpenHelper.getInstance(this);
        SQLiteDatabase todoDB = todoOpenHelper.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Contracts.TODO_TITLE,title);
        contentValues.put(Contracts.TODO_DESCRIPTION,description);
        contentValues.put(Contracts.TODO_DATE_ALARM,epochAlarmTime);
        if(DETAIL_MODE == Constants.KEY_EDIT){
            todoDB.update(Contracts.TODO_TABLE_NAME,contentValues,Contracts.TODO_ID + " = ?", new String[]{String.valueOf(id)});
            setResult(3,intent);
        }
        else if(DETAIL_MODE == Constants.KEY_ADD){
            contentValues.put(Contracts.TODO_DATE_CREATED,System.currentTimeMillis());
            todoDB.insert(Contracts.TODO_TABLE_NAME,null,contentValues);
            setResult(4,intent);
        }
        finish();
    }

//    @Override
//    public void sendBroadcast(Intent intent) {
//        if(epochAlarmTime != null){
//            AlarmManager alarmManager = ((AlarmManager) getSystemService(ALARM_SERVICE));
//            Intent alarmIntent = new Intent(DetailActivity.this,AlarmReceiver.class);
//            alarmIntent.putExtra(Constants.KEY_TODO_ALARM_TITLE,alarmTitle);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,alarmIntent,0);
//            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,epochAlarmTime,pendingIntent);
//            Log.d("Alarm", alarmTitle+" "+new Date(epochAlarmTime).toString());
//        }
//    }
}
