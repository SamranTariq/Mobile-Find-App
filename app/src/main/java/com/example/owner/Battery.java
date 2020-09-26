package com.example.owner;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Battery extends Service
{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRunningInForeground();
        registerReceiver(receiver,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        return START_STICKY;
    }

    private void startRunningInForeground() {

        //if more than or equal to 26
        if (Build.VERSION.SDK_INT >= 26) {

            //if more than 26
            if(Build.VERSION.SDK_INT > 26){
                String CHANNEL_ONE_ID = "owner.example.battery";
                String CHANNEL_ONE_NAME = "battery service";
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
                        .setContentTitle("Owner")
                        .setContentText("Battery Status.....")
                        .setSmallIcon(R.drawable.appicon)
                        .setLargeIcon(icon)
                        .build();

                Intent notificationIntent = new Intent(getApplicationContext(), DontLockService.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                notification.contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

                startForeground(104, notification);
            }
            //if version 26
            else{
                startForeground(104, updateNotification());

            }
        }
        //if less than version 26
        else{
            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle("Owner")
                    .setContentText("Battery Status.....")
                    .setSmallIcon(R.drawable.appicon)
                    .setOngoing(true).build();

            startForeground(104, notification);
        }
    }

    private Notification updateNotification() {

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, DontLockService.class), 0);

        return new NotificationCompat.Builder(this)
                .setContentTitle("Owner")
                .setTicker("Ticker")
                .setContentText("Battery Status.....")
                .setSmallIcon(R.drawable.appicon)
                .setContentIntent(pendingIntent)
                .setOngoing(true).build();
    }
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(action.equals(Intent.ACTION_BATTERY_CHANGED)){
                int level   = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                int scale   = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
                int percent = (level*100)/scale;
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => "+c.getTime());
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());
                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Battery").child("Percent").setValue(percent);
                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Battery").child("LastTime").setValue(formattedDate);


                ///  Toast.makeText(context, "Current battery level: " + percent + "%", Toast.LENGTH_SHORT).show();
                //Toast.makeText(context, "Current battery level: " + level + "%", Toast.LENGTH_SHORT).show();
                stopService(new Intent(Battery.this, Battery.class));
            }
        }
    };
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
