package com.example.owner;

import android.annotation.SuppressLint;
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
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class wifiscan extends Service
{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startRunningInForeground();
        @SuppressLint("WifiManagerLeak") WifiManager WifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        registerReceiver(receiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        WifiManager.startScan();
        return START_STICKY;
    }

    private void startRunningInForeground() {

        //if more than or equal to 26
        if (Build.VERSION.SDK_INT >= 26) {

            //if more than 26
            if(Build.VERSION.SDK_INT > 26){
                String CHANNEL_ONE_ID = "owner.example.wifi";
                String CHANNEL_ONE_NAME = "wifi service";
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
                        .setContentText("Nearby Wifi Requested....")
                        .setSmallIcon(R.drawable.appicon)
                        .setLargeIcon(icon)
                        .build();

                Intent notificationIntent = new Intent(getApplicationContext(), DontLockService.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                notification.contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);

                startForeground(105, notification);
            }
            //if version 26
            else{
                startForeground(105, updateNotification());

            }
        }
        //if less than version 26
        else{
            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle("Owner")
                    .setContentText("Nearby Wifi Requested....")
                    .setSmallIcon(R.drawable.appicon)
                    .setOngoing(true).build();

            startForeground(105, notification);
        }
    }

    private Notification updateNotification() {

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, DontLockService.class), 0);

        return new NotificationCompat.Builder(this)
                .setContentTitle("Owner")
                .setTicker("Ticker")
                .setContentText("Nearby Wifi Requested....")
                .setSmallIcon(R.drawable.appicon)
                .setContentIntent(pendingIntent)
                .setOngoing(true).build();
    }
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                    StringBuilder sb = new StringBuilder();
                    WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                    List<ScanResult> wifiList = wifiManager.getScanResults();

                    for (int i = 0; i < wifiList.size(); i++) {
                        sb.append(i+1+". ");
                        sb.append((wifiList.get(i)).SSID);
                        sb.append("<br>");
                    }
                    String nwifi=String.valueOf(sb);
                    //Toast.makeText(context,nwifi,Toast.LENGTH_LONG).show();
                    Calendar c = Calendar.getInstance();
                    System.out.println("Current time => "+c.getTime());
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = df.format(c.getTime());
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ScanWifi").child("Nearby").setValue(nwifi);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("ScanWifi").child("LastTime").setValue(formattedDate);

                }
                ///  Toast.makeText(context, "Current battery level: " + percent + "%", Toast.LENGTH_SHORT).show();
                //Toast.makeText(context, "Current battery level: " + level + "%", Toast.LENGTH_SHORT).show();
                stopService(new Intent(wifiscan.this, wifiscan.class));
        }
    };
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
