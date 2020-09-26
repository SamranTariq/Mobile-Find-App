package com.example.owner;

import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import static com.example.owner.GENERIC.RESULT_ENABLE;


public class onboot extends BroadcastReceiver {
    DevicePolicyManager deviceManger;
    ActivityManager activityManager;
    ComponentName compName;
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences prfs2 = context.getSharedPreferences("BODY_SAVE_FOR_BOOT", Context.MODE_PRIVATE);
        String body = prfs2.getString("bodysave", "");
        String handed = "Stop unlock!";
        String Screen = "Screen Lock!";
        if (handed.equals(body)) {
            if (Build.VERSION.SDK_INT > 25) {
                context.startForegroundService(new Intent(context, DontLockService.class));
           //     context.startActivity(new Intent(context,onbootpermissioncheck.class));
            }else{
                context.startService(new Intent(context, DontLockService.class));
             //   context.startActivity(new Intent(context,onbootpermissioncheck.class));
            }
        }else if (Screen.equals(body)){
            if(Build.VERSION.SDK_INT >25){
                context.startForegroundService(new Intent(context, screenlockservice.class));
               // context.startActivity(new Intent(context,onbootpermissioncheck.class));
            }else{
                context.startService(new Intent(context, screenlockservice.class));
                //context.startActivity(new Intent(context,onbootpermissioncheck.class));
            }
        }else {
            deviceManger = (DevicePolicyManager)context.getSystemService(
                    Context.DEVICE_POLICY_SERVICE);
            activityManager = (ActivityManager)context.getSystemService(
                    Context.ACTIVITY_SERVICE);
            compName = new ComponentName(context, Myadmin.class);
            boolean active = deviceManger.isAdminActive(compName);
            if (active) {
                Toast.makeText(context,"activated",Toast.LENGTH_LONG).show();
            }
        }
    }
/*
    private void permissions(Context context) {
        if (Build.VERSION.SDK_INT > 25) {
            context.startForegroundService(new Intent(context, usercheckonboot.class));
        }else{
            context.startService(new Intent(context, DontLockService.class));
        }
    }
*/
}
