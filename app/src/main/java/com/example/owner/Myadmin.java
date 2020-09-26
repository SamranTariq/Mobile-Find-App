package com.example.owner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.os.Build;
import android.widget.Toast;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.UserHandle;
import android.util.Log;
import android.widget.Toast;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class Myadmin extends DeviceAdminReceiver{

    static final String TAG = "DemoDeviceAdminReceiver";
    private static final String CHANNEL_ID = "channel_id01";
    private static final int NOTIFICATION_ID = 1;
    @Override
    public void onEnabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Toast.makeText(context,"Permission Enabled",Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onEnabled");
    }
    @Override
    public void onDisabled(Context context, Intent intent) {
        super.onEnabled(context, intent);
        Toast.makeText(context,"Permission Disabled",Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onEnabled");
    }
    @SuppressLint("WrongConstant")
    @Override
    public void onPasswordFailed(final Context context, Intent intent, UserHandle userHandle) {
        super.onPasswordFailed(context, intent, userHandle);
        SharedPreferences preferencesspiner = context.getSharedPreferences("SPINER_POSITION", Context.MODE_PRIVATE);
        String failedattempts = preferencesspiner.getString("limit", "1");
        int fail = Integer.parseInt(failedattempts);
        //password get attempts
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        int failed = devicePolicyManager.getCurrentFailedPasswordAttempts();
        if (failed >= fail) {
            Log.d(TAG,"failed");
            if (isNetworkAvailable(context)) {
               // Toast.makeText(context,"serviice done",Toast.LENGTH_LONG).show();
                if(Build.VERSION.SDK_INT >25){
                    //context.startForegroundService(new Intent(context, loc.class));

                    context.startForegroundService(new Intent(context, loc.class));
                }else{
                    context.startService(new Intent(context, loc.class));
                }
                /*  Intent i = new Intent(context, loc.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);*/


            }else {
              /*  Toast.makeText(context,"stat camera view",Toast.LENGTH_LONG).show();
                Intent i=new Intent(context,CameraView.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);*/
                if(Build.VERSION.SDK_INT >25){
                    context.startForegroundService(new Intent(context, SMS.class));
                }else{
                    context.startService(new Intent(context, SMS.class));
                }
               /* Intent i = new Intent(context, SMS.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);*/
            }
        }
    }

        @Override
    public void onPasswordSucceeded(Context context, Intent intent, UserHandle user) {
        super.onPasswordSucceeded(context,intent,user);
    }/*
    private boolean connected(final Context context){
        Log.d(TAG,"connected");
        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activenetworkinfo=connectivityManager.getActiveNetworkInfo();
        Log.d(TAG,"connected2");
        return activenetworkinfo != null && activenetworkinfo.isConnected();
    }*/
    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}