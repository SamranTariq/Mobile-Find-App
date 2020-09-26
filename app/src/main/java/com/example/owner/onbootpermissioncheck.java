package com.example.owner;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import static com.example.owner.GENERIC.RESULT_ENABLE;

public class  onbootpermissioncheck extends AppCompatActivity {
    private static final String TAG = "";
    boolean boolean_permission;
    Button startsigninactivity;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_STORAGE_REQUEST_CODE = 101;
    private static final int MY_PHONR_REQUEST_CODE = 102;
    private static final int MY_SMS_REQUEST_CODE = 103;
    private static final int MY_STORAGE_WRITE_CODE=104;
    private boolean askedForOverlayPermission;
    private static final int MY_LOCATION_PERMISSION=105;
    private int OVERLAY_PERMISSION_CODE;
    private final static int SEND_SMS_PERMISSION_REQUEST_CODE = 111;

    private DevicePolicyManager devicePolicyManager;
    private ActivityManager activityManager;
    private ComponentName componentN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Device_Administration_Permission();
    }
    /***************overlay permission *************/
    private void overlay_permission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(onbootpermissioncheck.this)) {
                askedForOverlayPermission = true;
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent,OVERLAY_PERMISSION_CODE);
            }
        }
    }

    /****************Phonr prmission********************
     private void Phone_permission() {
     if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED) ||
     (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED)) {

     if ((ActivityCompat.shouldShowRequestPermissionRationale(onbootpermissioncheck.this, android.Manifest.permission.PROCESS_OUTGOING_CALLS))) {
     } else {
     ActivityCompat.requestPermissions(onbootpermissioncheck.this, new String[]{android.Manifest.permission.PROCESS_OUTGOING_CALLS},
     REQUEST_PERMISSIONS);

     }

     if ((ActivityCompat.shouldShowRequestPermissionRationale(onbootpermissioncheck.this, Manifest.permission.PROCESS_OUTGOING_CALLS))) {
     } else {
     ActivityCompat.requestPermissions(onbootpermissioncheck.this, new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS},
     REQUEST_PERMISSIONS);

     }
     } else {
     boolean_permission = true;
     overlay_permission();
     }
     }
     /****************SMS PERMission*****************
     private void SMS_Permission() {
     if (checkPermission(Manifest.permission.SEND_SMS)){
     Log.d(TAG,"true");
     }else {
     ActivityCompat.requestPermissions(onbootpermissioncheck.this,new String[]{Manifest.permission.SEND_SMS},SEND_SMS_PERMISSION_REQUEST_CODE);
     Toast.makeText(onbootpermissioncheck.this,"Now Enter wrong password and Test App one Time",Toast.LENGTH_LONG).show();
     }
     }
     private boolean checkPermission(String permission){
     int checkPermission = ContextCompat.checkSelfPermission(onbootpermissioncheck.this,permission);
     return checkPermission == PackageManager.PERMISSION_GRANTED;
     }*/
    /****************Device ADministration permission*/
    private void Device_Administration_Permission() {
        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentN = new ComponentName(onbootpermissioncheck.this, Myadmin.class);
        boolean active = devicePolicyManager.isAdminActive(componentN);
        if (active) {
            Log.d(TAG, "Active");
            Toast.makeText(onbootpermissioncheck.this,"Activated:)",Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentN);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "why we need this permission?");
            startActivityForResult(intent, RESULT_ENABLE);
        }
    }
/*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case SEND_SMS_PERMISSION_REQUEST_CODE:
                if (grantResults.length>0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED))
                {
                    Log.d(TAG,"true");
                    Phone_permission();
                }
                break;
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == OVERLAY_PERMISSION_CODE) {
            askedForOverlayPermission = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(onbootpermissioncheck.this)){
                    Toast.makeText(onbootpermissioncheck.this, "ACTION_MANAGE_OVERLAY_PERMISSION Permission Granted", Toast.LENGTH_SHORT).show();
                    if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        Log.d(TAG,"camera enabled ");
                    }else
                    {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                    }

                }
            }
        }
        switch (requestCode){
            case RESULT_ENABLE:
                if (resultCode == Activity.RESULT_OK){
                    Toast.makeText(onbootpermissioncheck.this, "Enable administer permission", Toast.LENGTH_SHORT).show();
                    overlay_permission();
                }else {
                    Toast.makeText(onbootpermissioncheck.this, "Problem in enable administer permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_CAMERA_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                    if (checkSelfPermission(Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, MY_PHONR_REQUEST_CODE);
                    }
                } else {
                    Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                    }
                }
                break;
            case MY_STORAGE_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(onbootpermissioncheck.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_LOCATION_PERMISSION);

                    }
                    /*Intent intent=new Intent(onbootpermissioncheck.this,signin.class);
                    startActivity(intent);*/
                    Toast.makeText(this, "Storage permission granted", Toast.LENGTH_LONG).show();
                } else {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_STORAGE_REQUEST_CODE);
                    }
                }
                break;
            case MY_PHONR_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, MY_SMS_REQUEST_CODE);
                    }
                    Toast.makeText(this, "Phone permission granted", Toast.LENGTH_LONG).show();
                } else {
                    if (checkSelfPermission(Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, MY_PHONR_REQUEST_CODE);
                    }
                }
                break;
            case MY_SMS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "SMS permission granted", Toast.LENGTH_LONG).show();
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_STORAGE_REQUEST_CODE);
                    }
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_STORAGE_REQUEST_CODE);
                    }
                }else {
                    if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, MY_SMS_REQUEST_CODE);
                    }
                }
                break;
            case MY_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(onbootpermissioncheck.this,"Granted:)",Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(onbootpermissioncheck.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_LOCATION_PERMISSION);

                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

}