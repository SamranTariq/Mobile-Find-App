package com.example.owner;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import java.util.Objects;

public class usercheckonboot extends Service
{
    BroadcastReceiver lockReceiver;
    //DataHelper dataHelper;
    DevicePolicyManager deviceManger;
    ActivityManager activityManager;
    ComponentName compName;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRunningInForeground();
        detectingDeterminateOfServiceCall(intent.getExtras());
        registerBroadcastReceivers();
        return START_STICKY;
    }

    private void startRunningInForeground() {

        //if more than or equal to 26
        if (Build.VERSION.SDK_INT >= 26) {

            //if more than 26
            if(Build.VERSION.SDK_INT > 26){
                String CHANNEL_ONE_ID = "sensor.example. geyerk1.inspect.screenservice";
                String CHANNEL_ONE_NAME = "Screen service";
                NotificationChannel notificationChannel = null;
                notificationChannel = new NotificationChannel(CHANNEL_ONE_ID,
                        CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_MIN);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.setShowBadge(true);
                notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                if (manager != null) {
                    manager.createNotificationChannel(notificationChannel);
                }

                Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);
                Notification notification = new Notification.Builder(getApplicationContext())
                        .setChannelId(CHANNEL_ONE_ID)
                        .setContentTitle("Device Lock Protected by Owner")
                        .setContentText("Now you can't able to open mobile lock")
                        .setSmallIcon(R.drawable.appicon)
                        .setLargeIcon(icon)
                        .build();

                Intent notificationIntent = new Intent(getApplicationContext(), DontLockService.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                notification.contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

                startForeground(101, notification);
            }
            //if version 26
            else{
                startForeground(101, updateNotification());

            }
        }
        //if less than version 26
        else{
            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle("Activity logger")
                    .setContentText("data recording on going")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setOngoing(true).build();

            startForeground(101, notification);
        }
    }

    private Notification updateNotification() {

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, DontLockService.class), 0);

        return new NotificationCompat.Builder(this)
                .setContentTitle("Activity log")
                .setTicker("Ticker")
                .setContentText("recording of data is on going")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .setOngoing(true).build();
    }

    private void detectingDeterminateOfServiceCall(Bundle b) {
        if(b != null){
            Log.i("screenService", "bundle not null");
            if(b.getBoolean("phone restarted")){
                // storeInternally("Phone restarted");
            }
        }else{
            Log.i("screenService", " bundle equals null");
        }
        documentServiceStart();
    }


    private void documentServiceStart() {
        Log.i("screenService", "started running");
    }


    private void registerBroadcastReceivers() {
        lockReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    switch (Objects.requireNonNull(intent.getAction())){
                        /*case Intent.ACTION_SCREEN_ON:
                            //or do something else
                            Toast.makeText(context,"onnnnn",Toast.LENGTH_LONG).show();
                            //storeInternally("Screen on");
                            break;
                        case Intent.ACTION_SCREEN_OFF:
                            //or do something else
                            //storeInternally("Screen off");
                            break;*/
                        case Intent.ACTION_USER_PRESENT:
                            //Toast.makeText(context,"presentnnnnn",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(usercheckonboot.this,onbootpermissioncheck.class));
                            break;
                    }
                }
            }
        };

        IntentFilter screenFilter = new IntentFilter();
        screenFilter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(lockReceiver, screenFilter);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(lockReceiver != null){
            try{
                unregisterReceiver(lockReceiver);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

   /* IntentFilter filter;
    BroadcastReceiver mReceiver = new reciver();
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private static BroadcastReceiver m_ScreenOffReceiver;

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }

    @Override
    public void onCreate()
    {
        registerScreenOffReceiver();
    }

    private void registerScreenOffReceiver() {
        Log.d(TAG, "ACTION_SCREEN_OFF");
        filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        registerReceiver(mReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }*/
}