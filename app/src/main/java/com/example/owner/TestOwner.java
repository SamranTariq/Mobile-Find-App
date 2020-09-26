package com.example.owner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.androidhiddencamera.CameraConfig;
import com.androidhiddencamera.CameraError;
import com.androidhiddencamera.HiddenCameraActivity;
import com.androidhiddencamera.HiddenCameraFragment;
import com.androidhiddencamera.HiddenCameraUtils;
import com.androidhiddencamera.config.CameraFacing;
import com.androidhiddencamera.config.CameraImageFormat;
import com.androidhiddencamera.config.CameraResolution;
import com.androidhiddencamera.config.CameraRotation;
import com.example.owner.Locker.base.AppConstants;
import com.example.owner.Locker.services.BackgroundManager;
import com.example.owner.Locker.services.LockService;
import com.example.owner.Locker.utils.SpUtil;
import com.example.owner.Test_Owner.GpsTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;


public class TestOwner extends HiddenCameraActivity implements OnMapReadyCallback {
    private MediaPlayer player;
    LocationManager locationManager1;
    LocationManager locationManager;
    private Boolean flag = false;
    File image;
    String locationcoordinates;
    private LocationListener locationListener = null;
    double longitude,latitude;
    private HiddenCameraFragment mHiddenCameraFragment;
    private GpsTracker gpsTracker;
    private static final int REQUEST_CODE = 101;
    ProgressBar progressBar;
    private static final int REQ_CODE_CAMERA_PERMISSION = 1253;
    private CameraConfig mCameraConfig;
    Animation fade_in;
    private static final String CHANNEL_ID = "channel_id01";
    private static final String TAG = "Test";
    private static final int NOTIFICATION_ID = 1;
    Bitmap bitmap;
    Button click;
    TextView sendmailtest,sendsms,lockapp,unlockapps;
    ImageView ring,stopring,notification,takepic,Imageviewhidden,passwordsend,mail,sms,lock,unlock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_owner);
        click=findViewById(R.id.takepicbutton);
        passwordsend=findViewById(R.id.passwordsend);
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.myMap);
        mapFragment.getMapAsync(this);
        progressBar=findViewById(R.id.cameraprogressbar);
        mCameraConfig = new CameraConfig()
                .getBuilder(this)
                .setCameraFacing(CameraFacing.FRONT_FACING_CAMERA)
                .setCameraResolution(CameraResolution.HIGH_RESOLUTION)
                .setImageFormat(CameraImageFormat.FORMAT_JPEG)
                .setImageRotation(CameraRotation.ROTATION_270)
                .build();
        //Check for the camera permission for the runtime
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            //Start camera preview
            startCamera(mCameraConfig);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    REQ_CODE_CAMERA_PERMISSION);
        }
        //Take a picture
        fade_in= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
        ring=findViewById(R.id.ringtest);
        stopring=findViewById(R.id.stopring);
        takepic=findViewById(R.id.takepic);
        Imageviewhidden=findViewById(R.id.Imageviewhidden);
        ring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopring.setEnabled(true);
                ring.startAnimation(fade_in);
                startmusic();
            }
        });
        stopring.setEnabled(false);
        stopring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopring.setEnabled(false);
                stopring.startAnimation(fade_in);
                stopmusic();
            }
        });
        notification=findViewById(R.id.notification);
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notification.startAnimation(fade_in);
                sendnotification();
            }
        });
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.startAnimation(fade_in);
                finish();
            }
        });
        passwordsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordsend.startAnimation(fade_in);
                sendpass();
            }
        });
        sendmailtest=findViewById(R.id.textViewsendgmail);
        mail=findViewById(R.id.imageViewmail);
        sendmailtest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendmailtest.setEnabled(false);
                mail.startAnimation(fade_in);
                progressBar.setVisibility(View.VISIBLE);
                takePicture();
            }
        });
        sendsms=findViewById(R.id.textView9);
        sms=findViewById(R.id.imageView1);
        sendsms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sms.startAnimation(fade_in);
                if(Build.VERSION.SDK_INT >25){
                    startForegroundService(new Intent(TestOwner.this, SMS.class));
                }else{
                    startService(new Intent(TestOwner.this, SMS.class));
                }
            }
        });
        lockapp=findViewById(R.id.textViewlockappone);
        lock=findViewById(R.id.imageViewlockapp);
        lockapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lock.startAnimation(fade_in);
                SpUtil.getInstance().putBoolean(AppConstants.LOCK_STATE, true);
                BackgroundManager.getInstance().init(TestOwner.this).stopService(LockService.class);
                BackgroundManager.getInstance().init(TestOwner.this).startService(LockService.class);

                BackgroundManager.getInstance().init(TestOwner.this).startAlarmManager();
            }
        });
        unlockapps=findViewById(R.id.textViewunlockappone);
        unlock=findViewById(R.id.imageViewunlockapp);
        unlockapps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unlock.startAnimation(fade_in);
                SpUtil.getInstance().putBoolean(AppConstants.LOCK_STATE, false);
                BackgroundManager.getInstance().init(TestOwner.this).stopService(LockService.class);
                BackgroundManager.getInstance().init(TestOwner.this).stopAlarmManager();
            }
        });
    }
    private void startmusic() {
        AudioManager am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC,am.getStreamMaxVolume(AudioManager.STREAM_MUSIC),0);
        player= MediaPlayer.create(TestOwner.this, Settings.System.DEFAULT_ALARM_ALERT_URI);
        player.setLooping(true);
        player.start();
    }
    private void stopmusic(){
        player.stop();
    }
    private void sendnotification() {
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
    }
    private void sendpass(){
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
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQ_CODE_CAMERA_PERMISSION) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera(mCameraConfig);
            } else {
                Toast.makeText(this, R.string.error_camera_permission_denied, Toast.LENGTH_LONG).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Override
    public void onImageCapture(@NonNull File imageFile) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        takepic.setImageBitmap(bitmap);
        image=imageFile;
        locationManager1 = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
    public void onCameraError(int errorCode) {
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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gpsTracker = new GpsTracker(TestOwner.this);
        if(gpsTracker.canGetLocation()){
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();
           /* String a= String.valueOf(latitude);
            if (String.valueOf(latitude)=="") {
                Toast.makeText(TestOwner.this,"ne",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(TestOwner.this,"nope",Toast.LENGTH_LONG).show();
            }*/
        }else{
            gpsTracker.showSettingsAlert();
        }
        LatLng latLng = new LatLng(latitude,longitude);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!");
        Log.d("hlo","ok");
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
        googleMap.addMarker(markerOptions);
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
        }

        @Override
        protected Void doInBackground(Void... params) {
            SharedPreferences preferencesdetail = getSharedPreferences("DETAIL_OWNER", Context.MODE_PRIVATE);
            String getemail=preferencesdetail.getString("email","itssamrantariq@gmail.com");
            try {
                Sendmail sender = new Sendmail("iowner88@gmail.com", "iowner97944");
                Log.i("Email sending", "*********************************step*******************");
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
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressBar.setVisibility(View.GONE);
            Log.i("Email sending", "1");
            //finish();
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
}