package com.example.lalit.todo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import static com.example.lalit.todo.R.drawable.notify_icon;

/**
 * Created by jainl on 07-10-2017.
 */

public class AlarmReceiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra(Constants.KEY_TODO_ALARM_TITLE);
        //String time = intent.getStringExtra(Constants.KEY_TODO_ALARM_TIME);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.notify_icon)
                .setContentTitle(title)
                .setContentText("You might not want to miss this.");
        Intent intent1 = new Intent(context,MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,2,intent1,0);

        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_1","Urgent", NotificationManager.IMPORTANCE_HIGH);
            builder.setChannel(channel.getId());
            Notification notification = builder.build();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(1,notification);
        }
        else{
            Notification notification = builder.build();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1,notification);
        }
        //Log.d("Alarm",title + time);
    }
}
