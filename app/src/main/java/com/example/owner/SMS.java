package com.example.owner;

import android.Manifest;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.Settings;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

public class SMS extends Service {
    Context context1;
    TextView textView;
    public static final String SHARED_PREFS = "sharedprefs";
    private static final String CHANNEL_ID = "channel_id01";
    private static final int NOTIFICATION_ID = 1;
    public static final String KEY = "key";
    private LocationManager locationManager1=null;
    private LocationListener locationListener = null;
    private Boolean flag = false;
    private final static int SEND_SMS_PERMISSION_REQUEST_CODE = 111;
    String myEmailString, passString, sendToEmailString, subjectString, textString;
    LocationManager locationManager;
    String id1, latitude1, longitude1;
    static final String TAG = "loc";
    String gps;
    Bitmap bitmap;
    SharedPreferences preferencesdetail;
    String la,lo;
    String phone;
    StringBuffer stringBuffer11;
    String getemail,getname,getphone;
    String msg,phonno;

    @Override
    public void onCreate() {
        preferencesdetail = getSharedPreferences("DETAIL_OWNER", Context.MODE_PRIVATE);
        getname = preferencesdetail.getString("username", "");
        getphone=preferencesdetail.getString("phone","No detail to show");
        getemail=preferencesdetail.getString("email","open -->owner->Generic Tab");
      /*  stringBuffer11 = new StringBuffer();
        //    stringBuffer.append("id ::"+cursor.getString(0)+"\n");
        stringBuffer11.append(getname + "\n");
        stringBuffer11.append("\n" + getphone + "\n");
        stringBuffer11.append("\n" + getemail + "\n\n\n\n\n\n");*/
        //textView.setText(stringBuffer11.toString());
        //*For Offline location Using GPS *//
        locationManager1= (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        //* End for GPS Location *//

        //    DatabaseHelper mydb = new DatabaseHelper(this);
        phone=getphone;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mailsend();
        return START_STICKY;
    }

    private boolean checkPermission(String permission){
        int checkPermission = ContextCompat.checkSelfPermission(this,permission);
        return checkPermission == PackageManager.PERMISSION_GRANTED;
    }
    void mailsend() {
        if (Build.VERSION.SDK_INT >= 26) {
            //if more than 26
            if(Build.VERSION.SDK_INT > 26){
                String CHANNEL_ONE_ID = ".com.example.owner5";
                String CHANNEL_ONE_NAME = "sms5";
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

                startForeground(103, notification);
            }
            //if version 26
            else{
                startForeground(103, updateNotification());

            }
        }
        //if less than version 26
        else{
            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle("Owner")
                    .setContentText("Mobile Protected By Owner...")
                    .setSmallIcon(R.drawable.appicon)
                    .setOngoing(true).build();

            startForeground(103, notification);
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
        //  StyleableToast.makeText(this, "TAKE PHOTO" + stringBuffer11.toString() + "10sec wait", R.style.exampleToast).show();
        flag = displayGpsStatus();
        if (flag) {
            Log.v(TAG, "onClick");
            locationListener = new MyLocationListener();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager1.requestLocationUpdates(LocationManager
                    .GPS_PROVIDER, 5000, 40, locationListener);
        } else {
            Log.d(TAG,"your gps off");
        }
        //}
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
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*----------Listener class to get coordinates ------------- */
    class MyLocationListener implements LocationListener{

        @Override
        public void onLocationChanged(Location loc) {
            double longitude = loc.getLongitude();
            Log.v(TAG, String.valueOf(longitude));
            double latitude = loc.getLatitude();
            Log.v(TAG, String.valueOf(latitude));

            String s = latitude+","+longitude;
            Log.d("LOCATION",s);
            Log.d(TAG,"User not connected to internet");
            msg="SOMEONE TRY TO OPEN your friend's mobile.Click on link https://www.google.com/maps  use Values to search location on link VALUE'S="  + s;
            //  String msg="hlo";
            Log.d(TAG,"sms");
            phonno=phone;
            Log.d(TAG,"phone");
            //  Toast.makeText(context1,"gggggg",Toast.LENGTH_LONG).show();
            if (!TextUtils.isEmpty(msg) && !TextUtils.isEmpty(phonno)) {
                if (checkPermission(Manifest.permission.SEND_SMS)) {
                    messagesend messagesend=new messagesend();
                    messagesend.doInBackground();
                    Log.d(TAG,"sendsms");
                } else {
                    Toast.makeText(context1, "permission denied", Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(context1, "enter sms and phone no to send sms ", Toast.LENGTH_LONG).show();
            }
            //* end of SMS send *//
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
    //*end of location using GPS *//
    class messagesend extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("sms sending", "sending start");
        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Log.d(TAG,"smsssssmss");
                SmsManager smsManager=SmsManager.getDefault();
                smsManager.sendTextMessage(phonno,null,msg,null,null);
                Log.i("sms sending", "send");
                stopForeground(true);
                stopSelf();
               // stopService(new Intent(SMS.this, SMS.class));
                Log.d("STOP","Servicestop");
            } catch (Exception e) {
                Log.i("sms sending", "cannot send");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
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