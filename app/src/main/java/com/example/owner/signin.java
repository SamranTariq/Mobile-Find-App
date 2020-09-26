package com.example.owner;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

import static com.example.owner.GENERIC.RESULT_ENABLE;

public class signin extends AppCompatActivity {
    private static final String TAG = "";
    Button forfingerprintactivity;
    Button forsignupactivity;
    Button forforgetpassword;
    Button term,privacy,onlogin;
    Button whatis;
    CheckBox checkBox;
    EditText showhide,emaillogin;
    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentN;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
     //   Device_Administration_Permission();
      //  tad();
        progressBar=(ProgressBar)findViewById(R.id.progressbar);
        fAuth=FirebaseAuth.getInstance();
        showhide=(EditText)findViewById(R.id.passwordhideshow);
        emaillogin=(EditText)findViewById(R.id.emaillogin);
        checkBox=(CheckBox)findViewById(R.id.show_hide_password);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.isChecked()){
                    showhide.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else {
                    showhide.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        onlogin=(Button)findViewById(R.id.startfragment);
        forfingerprintactivity=(Button)findViewById(R.id.startfigerprint);
        forsignupactivity=(Button)findViewById(R.id.startsignup);
        forforgetpassword=(Button)findViewById(R.id.startforgetpassword);
        term=(Button)findViewById(R.id.termandservice);
        privacy=(Button)findViewById(R.id.privacypolices);
        whatis=(Button)findViewById(R.id.whatis);
        whatis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(signin.this,appintro2.class);
                startActivity(intent);
            }
        });
        forsignupactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(signin.this,signup.class);
                startActivity(intent);
            }
        });
        forforgetpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2=new Intent(signin.this,acforpass.class);
                startActivity(intent2);
            }
        });
        forfingerprintactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3=new Intent(signin.this,FingerPrintAuthentication.class);
                startActivity(intent3);
            }
        });
        term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(signin.this,"Link for TERM AND CONDITION's",Toast.LENGTH_LONG).show();
            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(signin.this,"Link for privacy polices",Toast.LENGTH_LONG).show();
            }
        });
        onlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=emaillogin.getText().toString().trim();
                String password=showhide.getText().toString().trim();
                if (TextUtils.isEmpty(email)){
                    emaillogin.setError("Email Required:)");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    showhide.setError("Password Required:)");
                    return;
                }
                if (password.length()<6){
                    showhide.setError("Length is minimum 6 character!!");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            if (fAuth.getCurrentUser().isEmailVerified()){
                                Log.d("Result","one");
                                TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                                final String imei;
                                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    imei = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                                    Toast.makeText(signin.this, imei, Toast.LENGTH_LONG).show();
                                } else {
                                    final TelephonyManager mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                                    if (ActivityCompat.checkSelfPermission(signin.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(signin.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 2);
                                        // TODO: Consider calling
                                        //    ActivityCompat#requestPermissions
                                        // here to request the missing permissions, and then overriding
                                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                        //                                          int[] grantResults)
                                        // to handle the case where the user grants the permission. See the documentation
                                        // for ActivityCompat#requestPermissions for more details.
                                        return;
                                    }
                                    if (mTelephony.getDeviceId() != null) {
                                        imei = mTelephony.getDeviceId();
                                        Toast.makeText(signin.this, imei, Toast.LENGTH_LONG).show();
                                    } else {
                                        imei = Settings.Secure.getString(
                                                getContentResolver(),
                                                Settings.Secure.ANDROID_ID);
                                        Toast.makeText(signin.this, imei, Toast.LENGTH_LONG).show();
                                    }
                                }
                                Log.d("Result","teon");
                                DatabaseReference samedevicechecck=FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                Log.d("Result","three");
                                samedevicechecck.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Log.d("Result","four");
                                        String emailcreateddeviceimei= dataSnapshot.child("IMEI").child("no").getValue().toString();
                                        if(!emailcreateddeviceimei.equals(imei)){
                                            Log.d("Result","five");
                                            AlertDialog.Builder builder = new AlertDialog.Builder(signin.this, R.style.MyDialog);
                                            builder.setMessage("This account belong to another device\nPlease use your own device account or create one if don't have :)").setCancelable(false)
                                                    .setTitle("Be Careful")
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        public void onClick(final DialogInterface dialog, final int id) {
                                                            dialog.cancel();
                                                        }
                                                    });
                                            AlertDialog alert = builder.create();
                                            alert.show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                        else {
                                            Log.d("Result","six");
                                            SharedPreferences prfs = getSharedPreferences("FCM_TOKEN", Context.MODE_PRIVATE);
                                            final String token = prfs.getString("token", "");
                                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Token").setValue(token);
                                            Intent view = new Intent(signin.this,Tabactivity.class);
                                            startActivity(view);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }else {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(signin.this);
                                builder.setMessage("Please Verify your email first!\n").setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(final DialogInterface dialog, final int id) {
                                                dialog.cancel();
                                            }
                                        })
                                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                            public void onClick(final DialogInterface dialog, final int id) {
                                            }
                                        });
                                final AlertDialog alert = builder.create();
                                alert.show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }else {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(signin.this);
                            builder.setMessage("User not created!\n" + task.getException().getMessage()).setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(final DialogInterface dialog, final int id) {
                                            dialog.cancel();
                                        }
                                    })
                                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                        public void onClick(final DialogInterface dialog, final int id) {
                                        }
                                    });
                            final AlertDialog alert = builder.create();
                            alert.show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });
    }

    private void Device_Administration_Permission() {
        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentN = new ComponentName(signin.this, Myadmin.class);
        boolean active = devicePolicyManager.isAdminActive(componentN);
        if (active) {
            Log.d(TAG, "Active");
            Intent intent=new Intent(signin.this,signin.class);
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
        switch (requestCode){
            case RESULT_ENABLE:
                if (resultCode == Activity.RESULT_OK){
                    Toast.makeText(signin.this, "Enable administer permission", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(signin.this, "Problem in enable administer permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void tad(){
        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.CAMERA,Manifest.permission.PROCESS_OUTGOING_CALLS,Manifest.permission.SEND_SMS,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }
    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(signin.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(signin.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }


    };
}
