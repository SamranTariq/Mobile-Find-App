package com.example.owner;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static android.content.ContentValues.TAG;

public class loc extends Service {
    Bitmap bitmap;
    String getname,getphone,getemail;
    String locationcoordinates;
    private static final String CHANNEL_ID = "channel_id01";
    private static final int NOTIFICATION_ID = 1;
    LocationManager locationManager1;
    LocationManager locationManager;
    SharedPreferences preferencesdetail;
    String s1, s2, lo, la;
    String myEmailString, passString, sendToEmailString, subjectString, textString;
    StringBuffer stringBuffer11;
    private Boolean flag = false;
    SQLiteDatabase sqLiteDatabase;
    String id1, latitude1, longitude1;
    private LocationListener locationListener = null;
    //The capture service
    private APictureCapturingService pictureService;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    String voice2text; //added
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sendmail();
        return START_STICKY;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        preferencesdetail = getSharedPreferences("DETAIL_OWNER", Context.MODE_PRIVATE);
        getname = preferencesdetail.getString("username", "");
        getphone=preferencesdetail.getString("phone","No detail to show");
        getemail=preferencesdetail.getString("email","open -->owner->Generic Tab");
        locationManager1 = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

/*private void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }*/
/*
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onLocationChanged(Location location) {
        s1 = ("" + location.getLatitude());
        s2 = ("" + location.getLongitude());
        try {
            Log.d(TAG, "on try");
            id1 = "1";
            latitude1 = s1;
            longitude1 = s2;
            if (id1.equals("")) {
                Toast.makeText(this, "key is empty", Toast.LENGTH_LONG).show();
                return;
            }
            Cursor cursorupdate = sqLiteDatabase.rawQuery(" Select * From getloc Where id='" + id1 + "'", null);
            if (cursorupdate.moveToNext()) {
                sqLiteDatabase.execSQL("Update getloc set id='" + id1 + "',latitude='" + latitude1 + "',longitude='" + longitude1 + "'Where id='" + id1 + "'");
                Toast.makeText(this, "recordupdate", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "recordupdate failed", Toast.LENGTH_LONG).show();
            }
            sendmail();
        } catch (Exception e) {
            Toast.makeText(this, "h", Toast.LENGTH_LONG).show();
        }
    }*/

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void sendmail() {
        if (Build.VERSION.SDK_INT >= 26) {

            //if more than 26
            if(Build.VERSION.SDK_INT > 26){
                String CHANNEL_ONE_ID = ".com.example.owner6";
                String CHANNEL_ONE_NAME = "mail5";
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
                        .setContentText("Mobile Protected By Owner...")
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
                    .setContentText("Mobile Protected By Owner...")
                    .setSmallIcon(R.drawable.appicon)
                    .setOngoing(true).build();

            startForeground(104, notification);
        }
        Log.d(TAG, "mailsendstart");
        crateNotificationChannel();
        RemoteViews collapsedView = new RemoteViews(getPackageName(),
                R.layout.notification_collapsed);
        RemoteViews expandedView = new RemoteViews(getPackageName(),
                R.layout.notification_expand);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this, CHANNEL_ID).setColorized(true).setColor(Color.parseColor("#FF0000"));
        builder.setSmallIcon(R.drawable.ic_notification);
        collapsedView.setTextViewText(R.id.text_view_collapsed_0, getname+" contact Detail(Mobile_Owner)");
        collapsedView.setTextViewText(R.id.text_view_collapsed_1, "✆ "+getphone);
        collapsedView.setTextViewText(R.id.text_view_collapsed_2, "⬇ "+getemail);
        expandedView.setTextViewText(R.id.text_view_collapsed_0, getname+" contact Detail(Mobile_Owner)");
        expandedView.setTextViewText(R.id.text_view_collapsed_1, "✆ "+getphone);
        expandedView.setTextViewText(R.id.text_view_collapsed_2, "✉︎ "+getemail);
        //  builder.setContentTitle("Please Contact Me..");
        // builder.setContentText(stringBuffer11.toString());
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        //Pic in small and in large size
        //Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.owner);
        Log.d(TAG,"******************************************image database start******************");
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
        // builder.setLargeIcon(bitmap);
        // builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).bigLargeIcon(null));
        //End of image notify process
        Log.d(TAG,"******************End of image database**********************");
        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
        flag = displayGpsStatus();
        if (flag) {
            Log.v(TAG, "onClick");
            locationListener = new loc.MyLocationListener();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager1.requestLocationUpdates(LocationManager
                    .GPS_PROVIDER, 5000, 10, locationListener);
        } else {
            Log.d(TAG,"your gps off");
        }
    }

    private Notification updateNotification() {

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, DontLockService.class), 0);

        return new NotificationCompat.Builder(this)
                .setContentTitle("Owner")
                .setTicker("Ticker")
                .setContentText("Mobile Protected By Owner...")
                .setSmallIcon(R.drawable.appicon)
                .setContentIntent(pendingIntent)
                .setOngoing(true).build();
    }

    //*location using GPS */
    private Boolean displayGpsStatus() {
        Log.d(TAG,"6");
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }

    public class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {
            double longitude = loc.getLongitude();
            Log.v(TAG, String.valueOf(longitude));
            double latitude = loc.getLatitude();
            Log.v(TAG, String.valueOf(latitude));
            locationcoordinates = latitude+","+longitude;
            SendEmailTask sendEmailTask = new SendEmailTask();
            sendEmailTask.execute();
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
            Log.d(TAG,"1");
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
            Log.d(TAG,"2");
        }

        @Override
        public void onStatusChanged(String provider,
                                    int status, Bundle extras) {
            // TODO Auto-generated method stub
        }

    }

    class SendEmailTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("Email sending", "sending start");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(loc.this,
                        GetBackCoreService.class));
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(5000);
                SharedPreferences preferencesdetail = getSharedPreferences("DETAIL_OWNER", Context.MODE_PRIVATE);
                String getemail=preferencesdetail.getString("email","itssamrantariq@gmail.com");
                try {
                    Sendmail sender = new Sendmail("iowner88@gmail.com", "iowner97944");
                    Log.i("Email sending", "*********************************step*******************");
                    sender.addAttachment(Environment.getExternalStorageDirectory().getPath() + "/test.jpg");
                    Log.i("Email sending", "*********************step one******************");
                    sender.sendMail("SOMEONE TRIES TO UNLOCK YOUR MOBILE_PHONE",
                            "\n\nSomeone tries to unlock your phone☠.\nⒾ ❂ⓦⓝⓔⓡ  App (っ◔◡◔)っ showed your information first time.\nNow they tries again so we track your mobile\nLocation☥ and send you 'Latitude and Longitude' \nCopy these value's and click on 'link below' and search using these value's and find mobile\nplace and seen direction/location on map.\n\n\n" +locationcoordinates+ "\n\n\nClick on link   https://www.google.com/maps \n\n\n\n THANKS FOR USING  Ⓘ ❂ⓦⓝⓔⓡ  app😊",
                            "iowner88@gmail.com",
                            getemail);
                    Log.i("Email sending", "*******************go*************************");
                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Log.i("Email sending", "1");
            stopForeground(true);
            stopSelf();
            //finish();
        }
    }

    private void crateNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "My Notification";
            String description = "My Notification description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationChannel.setDescription(description);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}