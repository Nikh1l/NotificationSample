package com.example.nikhil.notificationsample;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private NotificationManager mNotificationManager;

    private Button mNotification,mUpdate,mCancel;


    private static final int NOTIFICATION_ID = 0;
    private static final String mUrl = "https://developer.android.com/design/patterns/notifications.html";
    private static final String ACTION_UPDATE_NOTIFICATION = "com.example.nikhil.notificationsample.ACTION_UPDATE_NOTIFICATION";
    private static final String ACTION_CANCEL_NOTIFICATION = "com.example.nikhil.notificationsample.ACTION_CANCEL_NOTIFICATION";



    private NotificationReceiver mReceiver = new NotificationReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNotification = findViewById(R.id.notify);
        mUpdate = findViewById(R.id.update);
        mCancel = findViewById(R.id.cancel);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        //Toggling the buttons
        mNotification.setEnabled(true);
        mUpdate.setEnabled(false);
        mCancel.setEnabled(false);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_UPDATE_NOTIFICATION);
        intentFilter.addAction(ACTION_CANCEL_NOTIFICATION);
        registerReceiver(mReceiver,intentFilter);

        mNotification.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });

        mUpdate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                updateNotification();
            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelNotification();
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sendNotification() {
        //Toggling the buttons
        mNotification.setEnabled(false);
        mUpdate.setEnabled(true);
        mCancel.setEnabled(true);


        //creating cancel intent
        Intent cancelIntent = new Intent(ACTION_CANCEL_NOTIFICATION);
        //PendingIntent for the same
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(this,NOTIFICATION_ID,cancelIntent,PendingIntent.FLAG_ONE_SHOT);

        //creating an update Intent
        Intent updateIntent = new Intent(ACTION_UPDATE_NOTIFICATION);
        //create pendingIntent for the same
        PendingIntent updatePendingIntent = PendingIntent.getBroadcast(this,NOTIFICATION_ID,updateIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        //creating an intent to open the url
        Intent learnMoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
        //Create pendingIntent for the same
        PendingIntent learnMorePendingIntent = PendingIntent.getActivity(this,NOTIFICATION_ID,learnMoreIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        //creating an Intent for the main activity
        Intent notificationIntent = new Intent(this,MainActivity.class);
        //PendingIntent wrapper for main activity intent created before
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,NOTIFICATION_ID,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        //Create this first and then add pendingIntent and intent
        //For oreo
        String channelID = "some_channel_id";
        CharSequence channelName = "Some Channel";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel notificationChannel = new NotificationChannel(channelID,channelName,importance);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        mNotificationManager.createNotificationChannel(notificationChannel);

        //For N and below..
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this,channelID)
                .setAutoCancel(true)
                .setContentTitle("Type the title here")
                .setContentText("Set the notification text here")
                .setSmallIcon(R.drawable.ic_high_priority)
                .setContentIntent(notificationPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .addAction(R.drawable.ic_learn_more,"Learn More",learnMorePendingIntent)
                .setDeleteIntent(cancelPendingIntent);

        notifyBuilder.addAction(R.drawable.ic_update,"Update",updatePendingIntent);

        Notification myNotification = notifyBuilder.build();
        mNotificationManager.notify(NOTIFICATION_ID,myNotification);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateNotification() {

        //Toggling the buttons
        mNotification.setEnabled(false);
        mUpdate.setEnabled(false);
        mCancel.setEnabled(true);

        //Creating Bitmap for the image
        Bitmap androidImage = BitmapFactory.decodeResource(getResources(),R.drawable.not_1);


        //creating cancel intent
        Intent cancelIntent = new Intent(ACTION_CANCEL_NOTIFICATION);
        //PendingIntent for the same
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(this,NOTIFICATION_ID,cancelIntent,PendingIntent.FLAG_ONE_SHOT);


        //creating an intent to open the url
        Intent learnMoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(mUrl));
        //Create pendingIntent for the same
        PendingIntent learnMorePendingIntent = PendingIntent.getActivity(this,NOTIFICATION_ID,learnMoreIntent,PendingIntent.FLAG_ONE_SHOT);


        //creating an Intent
        Intent notificationIntent = new Intent(this,MainActivity.class);
        //PendingIntent wrapper for notification intent created before
        PendingIntent notificationPendingIntent = PendingIntent.getActivity(this,NOTIFICATION_ID,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        //Create this first and then add pendingIntent and intent
        //For oreo
        String channelID = "some_channel_id";
        CharSequence channelName = "Some Channel";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel notificationChannel = new NotificationChannel(channelID,channelName,importance);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        mNotificationManager.createNotificationChannel(notificationChannel);

        //For N and below..
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this,channelID)
                .setContentTitle("Type the title here")
                .setContentText("Set the notification text here")
                .setSmallIcon(R.drawable.ic_high_priority)
                .setContentIntent(notificationPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(androidImage).setBigContentTitle("Notification Updated"))
                .addAction(R.drawable.ic_learn_more,"Learn More",learnMorePendingIntent)
                .setDeleteIntent(cancelPendingIntent);
        Notification myNotification = notifyBuilder.build();
        mNotificationManager.notify(NOTIFICATION_ID,myNotification);
    }


    private void cancelNotification() {

        //Toggling the buttons
        mNotification.setEnabled(true);
        mUpdate.setEnabled(false);
        mCancel.setEnabled(false);

        mNotificationManager.cancel(NOTIFICATION_ID);
    }

    public class NotificationReceiver extends BroadcastReceiver{


        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action){
                case ACTION_CANCEL_NOTIFICATION:
                    cancelNotification();
                    break;
                case ACTION_UPDATE_NOTIFICATION:
                    updateNotification();
                    break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
