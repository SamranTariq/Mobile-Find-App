package com.example.owner;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.util.Base64;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.androidhiddencamera.CameraConfig;
import com.androidhiddencamera.CameraError;
import com.androidhiddencamera.HiddenCameraService;
import com.androidhiddencamera.HiddenCameraUtils;
import com.androidhiddencamera.config.CameraFacing;
import com.androidhiddencamera.config.CameraImageFormat;
import com.androidhiddencamera.config.CameraResolution;

import java.io.File;

import static android.content.ContentValues.TAG;

public class captureimage extends HiddenCameraService {
    File image;
    Bitmap bitmap;
    String locationcoordinates;
    private Boolean flag = false;
    SharedPreferences preferencesdetail;
    private LocationListener locationListener = null;
    LocationManager locationManager1;
    private static final String CHANNEL_ID = "channel_id01";
    private static final int NOTIFICATION_ID = 1;
    StringBuffer stringBuffer11;
    String getname,getphone,getemail;
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
      /*  stringBuffer11 = new StringBuffer();
        stringBuffer11.append(getname + "\n");
        stringBuffer11.append("\n" + getphone + "\n");
        stringBuffer11.append("\n" + getemail + "\n\n\n\n\n\n");*/
        locationManager1 = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

            if (HiddenCameraUtils.canOverDrawOtherApps(this)) {
                CameraConfig cameraConfig = new CameraConfig()
                        .getBuilder(this)
                        .setCameraFacing(CameraFacing.FRONT_FACING_CAMERA)
                        .setCameraResolution(CameraResolution.MEDIUM_RESOLUTION)
                        .setImageFormat(CameraImageFormat.FORMAT_JPEG)
                        .build();

                startCamera(cameraConfig);

                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(captureimage.this,
                                "Capturing image.", Toast.LENGTH_SHORT).show();

                        takePicture();
                    }
                }, 2000L);
            } else {

                //Open settings to grant permission for "Draw other apps".
                HiddenCameraUtils.openDrawOverPermissionSetting(this);
            }
        } else {

            //TODO Ask your parent activity for providing runtime permission
            Toast.makeText(this, "Camera permission not available", Toast.LENGTH_SHORT).show();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onImageCapture(@NonNull File imageFile) {
        Toast.makeText(this,
                "Captured image size is : " + imageFile.length(),
                Toast.LENGTH_SHORT)
                .show();
        image=imageFile;
        flag = displayGpsStatus();
        if (flag) {
            Log.v(TAG, "onClick");
            locationListener = new MyLocationListener();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager1.requestLocationUpdates(LocationManager
                    .GPS_PROVIDER, 5000, 10, locationListener);
        } else {
            Log.d(TAG,"your gps off");
        }
    }

    @Override
    public void onCameraError(@CameraError.CameraErrorCodes int errorCode) {
        switch (errorCode) {
            case CameraError.ERROR_CAMERA_OPEN_FAILED:
                //Camera open failed. Probably because another application
                //is using the camera
                Toast.makeText(this, R.string.error_cannot_open, Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_IMAGE_WRITE_FAILED:
                //Image write failed. Please check if you have provided WRITE_EXTERNAL_STORAGE permission
                Toast.makeText(this, R.string.error_cannot_write, Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_CAMERA_PERMISSION_NOT_AVAILABLE:
                //camera permission is not available
                //Ask for the camera permission before initializing it.
                Toast.makeText(this, R.string.error_cannot_get_permission, Toast.LENGTH_LONG).show();
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_OVERDRAW_PERMISSION:
                //Display information dialog to the user with steps to grant "Draw over other app"
                //permission for the app.
                HiddenCameraUtils.openDrawOverPermissionSetting(this);
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_FRONT_CAMERA:
                Toast.makeText(this, R.string.error_not_having_camera, Toast.LENGTH_LONG).show();
                break;
        }

        stopSelf();
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
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(new Intent(loc.this,
                        GetBackCoreService.class));
            }*/
        }

        @Override
        protected Void doInBackground(Void... params) {
            SharedPreferences preferencesdetail = getSharedPreferences("DETAIL_OWNER", Context.MODE_PRIVATE);
            String getemail=preferencesdetail.getString("email","");
            try {
                Sendmail sender = new Sendmail("iowner88@gmail.com", "iowner97944");
                Log.i("Email sending", "*********************************step*******************");
                //sender.addAttachment(Environment.getExternalStorageDirectory().getPath() + "/test.jpg");
                sender.addAttachment(String.valueOf(image));
                Log.i("Email sending", "*********************step one******************");
                sender.sendMail("SOMEONE TRIES TO UNLOCK YOUR MOBILE_PHONE",
                        "\n\nSomeone tries to unlock your phone☠.\nⒾ ❂ⓦⓝⓔⓡ  App (っ◔◡◔)っ showed your information first time.\nNow they tries again so we track your mobile\nLocation☥ and send you 'Latitude and Longitude' \nCopy these value's and click on 'link below' and search using these value's and find mobile\nplace and seen direction/location on map.\n\n\n" +locationcoordinates+ "\n\n\nClick on link   https://www.google.com/maps \n\n\n\n THANKS FOR USING  Ⓘ ❂ⓦⓝⓔⓡ  app😊",
                        "iowner88@gmail.com",
                        getemail);
                Log.i("Email sending", "*******************go*************************");
            } catch (Exception e) {
                Log.e("mylog", "Error: " + e.getMessage());
            }
          /*  new Timer().schedule(new TimerTask() {
                @Override
                public void run() {

                }
            }, 2000);*/
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            stopSelf();
            Log.i("Email sending", "1");
            stopForeground(true);
        }
    }
}
