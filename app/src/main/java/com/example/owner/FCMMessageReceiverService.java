package com.example.owner;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.preference.PreferenceManager;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.util.Base64;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.owner.Locker.base.AppConstants;
import com.example.owner.Locker.services.BackgroundManager;
import com.example.owner.Locker.services.LockService;
import com.example.owner.Locker.utils.SpUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.example.owner.App.FCM_CHANNEL_ID;

public class FCMMessageReceiverService extends FirebaseMessagingService{
    private static final String TAG = "FCM";
    private static final String CHANNEL_ID = "channel_id01";
    private static final int NOTIFICATION_ID = 1;
    Bitmap bitmap;
    DevicePolicyManager deviceManger;
    ActivityManager activityManager;
    ComponentName compName;
    public String body2;
    StringBuffer stringBuffer11;
    SharedPreferences preferencesdetail;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG,"on message recieved");
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG,"message recieved from"+remoteMessage.getFrom());
        if (remoteMessage.getNotification()!=null){
            String title=remoteMessage.getNotification().getTitle();
            String body=remoteMessage.getNotification().getBody();

            Notification notification=new NotificationCompat.Builder(this,FCM_CHANNEL_ID)
                    .setSmallIcon(R.drawable.icon)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setColor(Color.RED)
                    .build();
            NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            manager.notify(1002,notification);
        }
        if (remoteMessage.getData()!=null){
            Log.d(TAG,"data="+remoteMessage.getData());
            body2=remoteMessage.getData().get("body");
            onDestroy();
            //startActivity(new Intent(this, luncher.class));
         /*   mHandler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message message) {
                    // This is where you do your work in the UI thread.
                    // Your worker tells you in the message what to do.
                    Toast.makeText(FCMMessageReceiverService.this,"ok",Toast.LENGTH_LONG);
                }
            };*/
        }
//        Toast.makeText(this,"lock",Toast.LENGTH_LONG).show();
  //      Looper.prepare();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        /*deviceManger = (DevicePolicyManager)getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager)getSystemService(
                Context.ACTIVITY_SERVICE);
        compName = new ComponentName(this, Myadmin.class);
        boolean active = deviceManger.isAdminActive(compName);
        if (active) {
            deviceManger.lockNow();
        }*/
    }
    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
        Log.d(TAG,"on message delete");
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG,"on newtoken");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences preferences2 = getSharedPreferences("BODY_SAVE_FOR_BOOT", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = preferences2.edit();
        editor2.putString("bodysave",body2);
        editor2.commit();
        String data="great match!";
        String internetconnection="internet connection!";
        String sms="sms oh!";
        String unlock="unlock mobile!";
        String handed="Stop unlock!";
        String hourlock = "HourStop Mobile Lock!";
        String Screen="Screen Lock!";
        String hourscreen="HourStop Screen Lock!";
        String reset="Default Reset!";
        String ring="Ring Mobile!";
        String stop="Stop Ring!";
        String locationsend="Location Send!";
        String stoplocation="Stop Location!";
        String battery="Battery Check!";
        String connectedwifi="Connected Wifi!";
        String wifirange="Range Wifi!";
        String factoryreset="Stop Factory Reset!";
        String hourfactoryreset="HourStop Factory Reset!";
        String lockmobileapps = "Locked Application's";
        String unlockmobileapps = "UNLocked Them";
        String livelaod = "liveonload!";/*prevent faactory ret on boot*/
        SharedPreferences prfs2 = getSharedPreferences("BODY_SAVE_FOR_BOOT", Context.MODE_PRIVATE);
        String body = prfs2.getString("bodysave", "");
        if (data.equals(body)){
            deviceManger = (DevicePolicyManager)getSystemService(
                    Context.DEVICE_POLICY_SERVICE);
            activityManager = (ActivityManager)getSystemService(
                    Context.ACTIVITY_SERVICE);
            compName = new ComponentName(this, Myadmin.class);
            boolean active = deviceManger.isAdminActive(compName);
            if (active) {
                deviceManger.lockNow();
            }
        }else if(sms.equals(body)){
            RemoteViews collapsedView = new RemoteViews(getPackageName(),
                    R.layout.notification_collapsed);
            RemoteViews expandedView = new RemoteViews(getPackageName(),
                    R.layout.notification_expand);
            PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP
                    | PowerManager.ON_AFTER_RELEASE, "INFO");
            wl.acquire();
            KeyguardManager km = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock kl = km.newKeyguardLock("name");
            kl.disableKeyguard();
            Log.d("tag","****************************************************k*************************************");
            SharedPreferences preferencesdetail = getSharedPreferences("DETAIL_OWNER", Context.MODE_PRIVATE);
            String getname = preferencesdetail.getString("username", "");
            String getphone=preferencesdetail.getString("phone","No detail to show");
            String getemail=preferencesdetail.getString("email","open -->owner->Generic Tab");
        /*StringBuffer stringBuffer11 = new StringBuffer();
        //    stringBuffer.append("id ::"+cursor.getString(0)+"\n");
        stringBuffer11.append(getname + "\n");
        stringBuffer11.append("\n" + getphone + "\n");
        stringBuffer11.append("\n" + getemail + "\n\n\n\n\n\n");*/
            crateNotificationChannel();
            NotificationCompat.Builder builder=new NotificationCompat.Builder(this, CHANNEL_ID).setColorized(true).setColor(Color.parseColor("#FF0000"));
            builder.setSmallIcon(R.drawable.ic_notification);
            //builder.setContentTitle("Owner Information");
            //builder.setContentText(stringBuffer11.toString());
            collapsedView.setTextViewText(R.id.text_view_collapsed_0, getname+" contact Detail(Mobile_Owner)");
            collapsedView.setTextViewText(R.id.text_view_collapsed_1, "✆ "+getphone);
            collapsedView.setTextViewText(R.id.text_view_collapsed_2, "⬇ "+getemail);
            expandedView.setTextViewText(R.id.text_view_collapsed_0, getname+" contact Detail(Mobile_Owner)");
            expandedView.setTextViewText(R.id.text_view_collapsed_1, "✆ "+getphone);
            expandedView.setTextViewText(R.id.text_view_collapsed_2, "✉︎ "+getemail);
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            Log.d("tag","******************************************image database start******************");
            SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(this);
            String previouslyEncodedImage = shre.getString("image_data", "");

            if( !previouslyEncodedImage.equalsIgnoreCase("") ){
                byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            }
            builder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());
            builder.setColor(2);
            expandedView.setImageViewBitmap(R.id.image_view_expanded,bitmap);
            builder.setCustomContentView(collapsedView);
            builder.setCustomBigContentView(expandedView);
            //builder.setLargeIcon(bitmap);
//        builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null));
            //End of image notify process
            Log.d("tag","******************End of image database**********************");
            NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
        }else if (unlock.equals(body)){
            // Unlock the screen
            PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                    | PowerManager.ACQUIRE_CAUSES_WAKEUP
                    | PowerManager.ON_AFTER_RELEASE, "INFO");
            wl.acquire();

            KeyguardManager km = (KeyguardManager)getSystemService(Context.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock kl = km.newKeyguardLock("name");
            kl.disableKeyguard();
         //   dataHelper=new DataHelper(this);
            SharedPreferences prfs = getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
            String pass=prfs.getString("password","");
            crateNotificationChannel();
            NotificationCompat.Builder builder=new NotificationCompat.Builder(this, CHANNEL_ID).setColorized(true).setColor(Color.parseColor("#FF0000"));
            builder.setSmallIcon(R.drawable.ic_notification);
            builder.setContentTitle("Good Luck");
            builder.setContentText("Password You Provide OWNER:)    "+pass);
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            Log.d(TAG,"******************************************image database start******************");
            SharedPreferences shre = PreferenceManager.getDefaultSharedPreferences(this);
            String previouslyEncodedImage = shre.getString("image_data", "");

            if( !previouslyEncodedImage.equalsIgnoreCase("") ){
                byte[] b = Base64.decode(previouslyEncodedImage, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            }
            builder.setLargeIcon(bitmap);
            builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null));
            //End of image notify process
            Log.d(TAG,"******************End of image database**********************");
            NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
        }else if (handed.equals(body)){
            //stopService(new Intent(this, mobileringservice.class));
            stopService(new Intent(this, screenlockservice.class));
            if(Build.VERSION.SDK_INT >25){
                startForegroundService(new Intent(this, DontLockService.class));
                deviceManger = (DevicePolicyManager)getSystemService(
                        Context.DEVICE_POLICY_SERVICE);
                activityManager = (ActivityManager)getSystemService(
                        Context.ACTIVITY_SERVICE);
                compName = new ComponentName(this, Myadmin.class);
                boolean active = deviceManger.isAdminActive(compName);
                if (active) {
                    deviceManger.lockNow();
                }
            }else{
                startService(new Intent(this, DontLockService.class));
                deviceManger = (DevicePolicyManager)getSystemService(
                        Context.DEVICE_POLICY_SERVICE);
                activityManager = (ActivityManager)getSystemService(
                        Context.ACTIVITY_SERVICE);
                compName = new ComponentName(this, Myadmin.class);
                boolean active = deviceManger.isAdminActive(compName);
                if (active) {
                    deviceManger.lockNow();
                }
            }
        }else if (hourlock.equals(body)){
            stopService(new Intent(this, screenlockservice.class));
            if(Build.VERSION.SDK_INT >25){
                startForegroundService(new Intent(this, hourdontlockservice.class));
                deviceManger = (DevicePolicyManager)getSystemService(
                        Context.DEVICE_POLICY_SERVICE);
                activityManager = (ActivityManager)getSystemService(
                        Context.ACTIVITY_SERVICE);
                compName = new ComponentName(this, Myadmin.class);
                boolean active = deviceManger.isAdminActive(compName);
                if (active) {
                    deviceManger.lockNow();
                }
            }else{
                startService(new Intent(this, hourdontlockservice.class));
                deviceManger = (DevicePolicyManager)getSystemService(
                        Context.DEVICE_POLICY_SERVICE);
                activityManager = (ActivityManager)getSystemService(
                        Context.ACTIVITY_SERVICE);
                compName = new ComponentName(this, Myadmin.class);
                boolean active = deviceManger.isAdminActive(compName);
                if (active) {
                    deviceManger.lockNow();
                }
            }
        }else if (Screen.equals(body)){
            deviceManger = (DevicePolicyManager)getSystemService(
                    Context.DEVICE_POLICY_SERVICE);
            activityManager = (ActivityManager)getSystemService(
                    Context.ACTIVITY_SERVICE);
            compName = new ComponentName(FCMMessageReceiverService.this, Myadmin.class);
            boolean active = deviceManger.isAdminActive(compName);
            if (active) {
                deviceManger.lockNow();
            }
           // stopService(new Intent(this, mobileringservice.class));
            stopService(new Intent(this, DontLockService.class));
            if(Build.VERSION.SDK_INT >25){
                startForegroundService(new Intent(this, screenlockservice.class));
            }else{
                startService(new Intent(this, screenlockservice.class));
            }
        }else if (hourscreen.equals(body)){
            deviceManger = (DevicePolicyManager)getSystemService(
                    Context.DEVICE_POLICY_SERVICE);
            activityManager = (ActivityManager)getSystemService(
                    Context.ACTIVITY_SERVICE);
            compName = new ComponentName(FCMMessageReceiverService.this, Myadmin.class);
            boolean active = deviceManger.isAdminActive(compName);
            if (active) {
                deviceManger.lockNow();
            }
            // stopService(new Intent(this, mobileringservice.class));
            stopService(new Intent(this, DontLockService.class));
            if(Build.VERSION.SDK_INT >25){
                startForegroundService(new Intent(this, hourscreenlockservice.class));
            }else{
                startService(new Intent(this, hourscreenlockservice.class));
            }
        }else if (reset.equals(body)){
            stopService(new Intent(this,hourdontlockservice.class));
            stopService(new Intent(this,hourscreenlockservice.class));
            stopService(new Intent(this, fivehourfactryresetservice.class));
            stopService(new Intent(this, factoryresetservice.class));
            stopService(new Intent(this, DontLockService.class));
            stopService(new Intent(this, screenlockservice.class));
        }else if (ring.equals(body)){
           // stopService(new Intent(this, DontLockService.class));
           // stopService(new Intent(this, screenlockservice.class));
            if(Build.VERSION.SDK_INT >25){
                startForegroundService(new Intent(this, mobileringservice.class));
            }else{
                startService(new Intent(this, mobileringservice.class));
            }
        }else if (stop.equals(body)){
            stopService(new Intent(this, mobileringservice.class));
            //t.stop();
        }else if (locationsend.equals(body)){
            if(Build.VERSION.SDK_INT >25){
                startForegroundService(new Intent(this, locationservice.class));
            }else{
                startService(new Intent(this, locationservice.class));
            }
        }else if (stoplocation.equals(body)){
            stopService(new Intent(this, locationservice.class));
        }else if (battery.equals(body)){
            crateNotificationChannel();
            NotificationCompat.Builder builder=new NotificationCompat.Builder(this, CHANNEL_ID).setColorized(true);
            builder.setSmallIcon(R.drawable.appicon);
            builder.setContentText("Request For Battery Status");
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
          //  Toast.makeText(this,"ok",Toast.LENGTH_LONG).show();
            if(Build.VERSION.SDK_INT >25){
                startForegroundService(new Intent(this, Battery.class));
            }else{
                startService(new Intent(this, Battery.class));
            }
        }else if (connectedwifi.equals(body)){
            crateNotificationChannel();
            NotificationCompat.Builder builder=new NotificationCompat.Builder(this, CHANNEL_ID).setColorized(true);
            builder.setSmallIcon(R.drawable.appicon);
            builder.setContentText("Request For Network Status");
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            if (activeNetwork != null) {
                // connected to the internet
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                    WifiManager manager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wifiInfo = manager.getConnectionInfo();
                    if(wifiInfo.getSupplicantState() == SupplicantState.COMPLETED){
                        String ssid = wifiInfo.getSSID();
                        Calendar c = Calendar.getInstance();
                        System.out.println("Current time => "+c.getTime());
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String formattedDate = df.format(c.getTime());
                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Wifi").child("connected").setValue(ssid);
                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Wifi").child("LastTime").setValue(formattedDate);
                    }
                } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    // connected to the mobile network
                    String mobiledata="Mobile Data Connected";
                    Calendar c = Calendar.getInstance();
                    System.out.println("Current time => "+c.getTime());
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String formattedDate = df.format(c.getTime());
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Wifi").child("connected").setValue(mobiledata);
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Wifi").child("LastTime").setValue(formattedDate);
                }
            } else {
                // not connected to the internet
                String nointernet="Mobile Not Connected To Internet";
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => "+c.getTime());
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = df.format(c.getTime());
                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Wifi").child("connected").setValue(nointernet);
                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Wifi").child("LastTime").setValue(formattedDate);
            }
        }else if (wifirange.equals(body)){
            crateNotificationChannel();
            NotificationCompat.Builder builder=new NotificationCompat.Builder(this, CHANNEL_ID).setColorized(true);
            builder.setSmallIcon(R.drawable.appicon);
            builder.setContentText("Request For Nearby Networks");
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
            if(Build.VERSION.SDK_INT >25){
                startForegroundService(new Intent(this, wifiscan.class));
            }else{
                startService(new Intent(this, wifiscan.class));
            }
        }else if (factoryreset.equals(body)){
            deviceManger = (DevicePolicyManager)getSystemService(
                    Context.DEVICE_POLICY_SERVICE);
            activityManager = (ActivityManager)getSystemService(
                    Context.ACTIVITY_SERVICE);
            compName = new ComponentName(FCMMessageReceiverService.this, Myadmin.class);
            boolean active = deviceManger.isAdminActive(compName);
            if (active) {
                deviceManger.lockNow();
            }
            // stopService(new Intent(this, mobileringservice.class));
            stopService(new Intent(this, DontLockService.class));
            if(Build.VERSION.SDK_INT >25){
                startForegroundService(new Intent(this, factoryresetservice.class));
            }else{
                startService(new Intent(this, factoryresetservice.class));
            }
        }else if (hourfactoryreset.equals(body)){
            deviceManger = (DevicePolicyManager)getSystemService(
                    Context.DEVICE_POLICY_SERVICE);
            activityManager = (ActivityManager)getSystemService(
                    Context.ACTIVITY_SERVICE);
            compName = new ComponentName(FCMMessageReceiverService.this, Myadmin.class);
            boolean active = deviceManger.isAdminActive(compName);
            if (active) {
                deviceManger.lockNow();
            }
            // stopService(new Intent(this, mobileringservice.class));
            stopService(new Intent(this, DontLockService.class));
            if(Build.VERSION.SDK_INT >25){
                startForegroundService(new Intent(this, fivehourfactryresetservice.class));
            }else{
                startService(new Intent(this, fivehourfactryresetservice.class));
            }
        }else if (internetconnection.equals(body)){
            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Internet").child("connection").setValue("Your Android Device Connected With Internet.");
        }else if (lockmobileapps.equals(body)){
            SpUtil.getInstance().putBoolean(AppConstants.LOCK_STATE, true);
            BackgroundManager.getInstance().init(FCMMessageReceiverService.this).stopService(LockService.class);
            BackgroundManager.getInstance().init(FCMMessageReceiverService.this).startService(LockService.class);

            BackgroundManager.getInstance().init(FCMMessageReceiverService.this).startAlarmManager();
        }else if (unlockmobileapps.equals(body)){
            SpUtil.getInstance().putBoolean(AppConstants.LOCK_STATE, false);
            BackgroundManager.getInstance().init(FCMMessageReceiverService.this).stopService(LockService.class);
            BackgroundManager.getInstance().init(FCMMessageReceiverService.this).stopAlarmManager();
        }else if (livelaod.equals(body)){
            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Internet").child("connection").setValue("Your Android Device Connected With Internet.");
        }
        else {
            Log.d(TAG,"else");
        }
    }

    private void crateNotificationChannel() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            CharSequence name="My Notification";
            String description="My Notification description";
            int importance= NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel=new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);
            NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}


/*t = new Thread(new Runnable(){
                @Override
                public void run(){
                    while(!Thread.currentThread().isInterrupted()){
                        // do stuff
                        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
                        am.setStreamVolume(AudioManager.STREAM_MUSIC,am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),0);
                        MediaPlayer player= MediaPlayer.create(FCMMessageReceiverService.this, Settings.System.DEFAULT_ALARM_ALERT_URI);
                        player.setLooping(true);
                        player.start();
                    }
                }
            });
            t.start();

// Schedule task to terminate thread in 1 minute
            ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
            exec.schedule(new Runnable(){
                @Override
                public void run(){
                    t.interrupt();
                }
            }, 5, TimeUnit.MINUTES);*/




