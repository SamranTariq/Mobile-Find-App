package com.example.owner;

import android.Manifest;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

import static com.example.owner.GENERIC.RESULT_ENABLE;

public class appermiss extends AppCompatActivity {
    private static final String TAG = "";
    Button startsigninactivity;
    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appermiss);
        startsigninactivity = (Button) findViewById(R.id.startsigninactivity);
        startsigninactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Device_Administration_Permission();
            }
        });
    }
    /****************Device ADministration permission*/
    private void Device_Administration_Permission() {
        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentN = new ComponentName(appermiss.this, Myadmin.class);
        boolean active = devicePolicyManager.isAdminActive(componentN);
        if (active) {
            Log.d(TAG, "Active");
            Intent intent=new Intent(appermiss.this,signin.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentN);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "why we need this permission?");
            startActivityForResult(intent, RESULT_ENABLE);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       /* if(requestCode==1){

            if(resultCode==RESULT_OK){

                Toast.makeText(this, "Gps opened", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(appermiss.this,signin.class));
                //if user allows to open gps
                Log.d("result ok",data.toString());

            }else if(resultCode==RESULT_CANCELED){

                Toast.makeText(this, "refused to open gps",
                        Toast.LENGTH_SHORT).show();
                createLocationRequest();
                // in case user back press or refuses to open gps
                Log.d("result cancelled",data.toString());
            }
        }*/
        switch (requestCode){
            case RESULT_ENABLE:
                if (resultCode == Activity.RESULT_OK){
                    Toast.makeText(appermiss.this, "Enable administer permission", Toast.LENGTH_SHORT).show();
                    TedPermission.with(this)
                            .setPermissionListener(permissionlistener)
                            .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                            .setPermissions(Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.CAMERA,Manifest.permission.PROCESS_OUTGOING_CALLS,Manifest.permission.SEND_SMS,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                            .check();

                }else {
                    Toast.makeText(appermiss.this, "Problem in enable administer permission", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                if(resultCode==RESULT_OK){

                    Toast.makeText(this, "Gps opened", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(appermiss.this,signin.class));
                    //if user allows to open gps
                    Log.d("result ok",data.toString());

                }else if(resultCode==RESULT_CANCELED){

                    Toast.makeText(this, "refused to open gps",
                            Toast.LENGTH_SHORT).show();
                    createLocationRequest();
                    // in case user back press or refuses to open gps
                    Log.d("result cancelled",data.toString());
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
      //  super.onActivityResult(requestCode, resultCode, data);
    }
    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(appermiss.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            createLocationRequest();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(appermiss.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };
    protected void createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());



        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...

                Toast.makeText(appermiss.this, "Gps already open",
                        Toast.LENGTH_LONG).show();
                startActivity(new Intent(appermiss.this,signin.class));
                Log.d("location settings",locationSettingsResponse.toString());
            }
        });

        task.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(appermiss.this,
                                1);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }
}






/* Listen
"Do you wanna see a magic"
if (yes):
    "Block me"
    thnko();
else
    "Doi it, it's important and (fun also)"

thnko():
   "Many Thanks"
 */





































