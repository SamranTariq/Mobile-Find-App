package com.example.owner;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class signup extends AppCompatActivity {
    Button forsignin1, signin2, forforget, startsignin2;
    EditText email, passwordone, passwordtwo, username;
    String Mail, pass, pass2, name;
    TextView emailwarning, passwordonewarning, passwordtwowarning, usernamewarning;
    CheckBox checkBox;
    FirebaseAuth fAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        fAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progress);
        email = (EditText) findViewById(R.id.emailedittext);
        emailwarning = (TextView) findViewById(R.id.emailnotify);
        passwordonewarning = (TextView) findViewById(R.id.passwordonewarning);
        passwordtwowarning = (TextView) findViewById(R.id.passwordtwowarning);
        passwordone = (EditText) findViewById(R.id.passwordone);
        passwordtwo = (EditText) findViewById(R.id.passwordtwo);
        username = (EditText) findViewById(R.id.username);
        usernamewarning = (TextView) findViewById(R.id.usernamewarning);
        checkBox = (CheckBox) findViewById(R.id.privacycheck);
        forforget = (Button) findViewById(R.id.startforget);
        signin2 = (Button) findViewById(R.id.startsignin2);
        forsignin1 = (Button) findViewById(R.id.startsignin);
        forforget.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup.this, acforpass.class);
                startActivity(intent);
            }
        });
        forsignin1.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (valiemail() & valipassword() & valiusername() & checkboxcheck()) {
                    final String email1 = email.getText().toString().trim();
                    String password1 = passwordone.getText().toString().trim();
                    final String user = username.getText().toString().trim();
                    final String modelname = Settings.Global.getString(getContentResolver(), Settings.Global.DEVICE_NAME);
                    final String devicename = getDeviceName();
                    //register

                    fAuth.createUserWithEmailAndPassword(email1, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                fAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            User user1 = new User(
                                                    user,
                                                    email1,
                                                    devicename,
                                                    modelname
                                            );
                                            FirebaseDatabase.getInstance().getReference("Users")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Detail")
                                                    .setValue(user1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    final String imei;
                                                    if (task.isSuccessful()) {
                                                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                                            imei = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                                                            Toast.makeText(signup.this, imei, Toast.LENGTH_LONG).show();
                                                        } else {
                                                            final TelephonyManager mTelephony = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                                                            if (ActivityCompat.checkSelfPermission(signup.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                                                                ActivityCompat.requestPermissions(signup.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 2);
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
                                                                Toast.makeText(signup.this, imei, Toast.LENGTH_LONG).show();
                                                            } else {
                                                                imei = Settings.Secure.getString(
                                                                        getContentResolver(),
                                                                        Settings.Secure.ANDROID_ID);
                                                                Toast.makeText(signup.this, imei, Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                        Calendar c = Calendar.getInstance();
                                                        System.out.println("Current time => "+c.getTime());
                                                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                        String formattedDate = df.format(c.getTime());
                                                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("IMEI").child("no").setValue(imei);
                                                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("IMEI").child("model").setValue(modelname);
                                                        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("request").setValue(formattedDate);
                                                        final AlertDialog.Builder builder = new AlertDialog.Builder(signup.this,R.style.mailalert);
                                                        builder.setTitle("USER REGISTER SUCCESSFULLY").setMessage("please check your email to verify your account and get access to the App.").setCancelable(false)
                                                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                    public void onClick(final DialogInterface dialog, final int id) {
                                                                        dialog.cancel();
                                                                        startActivity(new Intent(signup.this,signin.class));
                                                                    }
                                                                })
                                                                .setNegativeButton("Open GMail", new DialogInterface.OnClickListener() {
                                                                    public void onClick(final DialogInterface dialog, final int id) {
                                                                        dialog.cancel();
                                                                        try {
                                                                            Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
                                                                            startActivity(intent);
                                                                        }catch (ActivityNotFoundException e){
                                                                            Toast.makeText(signup.this,"No Gmail App Found",Toast.LENGTH_LONG).show();
                                                                        }
                                                                    }
                                                                });
                                                        final AlertDialog alert = builder.create();
                                                        alert.show();
                                                        email.setText("");
                                                        passwordone.setText("");
                                                        passwordtwo.setText("");
                                                        username.setText("");
                                                        progressBar.setVisibility(View.GONE);
                                                        //startActivity(new Intent(getApplicationContext(),Main3Activity.class));
                                                    }else {
                                                        final AlertDialog.Builder builder = new AlertDialog.Builder(signup.this);
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
                                        else {
                                            final AlertDialog.Builder builder = new AlertDialog.Builder(signup.this);
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
                            }else {
                                final AlertDialog.Builder builder = new AlertDialog.Builder(signup.this);
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
                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(signup.this,"Check Hint",Toast.LENGTH_LONG).show();
                }
            }
        });
        signin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(signup.this,signin.class);
                startActivity(intent);
            }
        });
    }

    private boolean checkboxcheck() {
        if (checkBox.isChecked()){
            return true;
        }else {
            return false;
        }
    }

    private boolean valiusername() {
        name=username.getText().toString().trim();
        if (name.isEmpty()){
            username.setError("Please Enter your name:)");
            /*
            usernamewarning.setText("Hint**UserName won't be Empty");
            usernamewarning.setTextSize(15);*/
            return false;
        }else if (name.length()<3){
           username.setError("User Name Must be greater then 2 character!");
            /* usernamewarning.setText("Hint**UserName contain miinimum 3 Letter");
            usernamewarning.setTextSize(15);*/
            return false;
        }else {
            return true;
        }
    }

    @SuppressLint("ResourceAsColor")
    private boolean valipassword() {
        pass=passwordone.getText().toString().trim();
        pass2=passwordtwo.getText().toString().trim();
        if(pass.isEmpty()){
           passwordone.setError("Password Won't be empty:)");
            /* passwordonewarning.setText("Hint**Password won't be empty");
            passwordonewarning.setTextSize(15);*/
            return false;
        }else if (pass.length() < 6){
            passwordone.setError("Password length should be minimum 6 Letter:)");
            return false;
        }else if (pass2.isEmpty()){
            passwordtwo.setError("Password won't be Empty:)");
            return false;
        }else if (!pass.equals(pass2)){
            passwordone.setError("Password Won't match!");
            return false;
        }else {
            return true;
        }

    }

    @SuppressLint("ResourceAsColor")
    private boolean valiemail() {
        Mail=email.getText().toString().trim();
        if (Mail.isEmpty()){
          email.setError("Please Enter your Email:)");
            /*  emailwarning.setText("Hint**Email Should Not Be Empty");
            emailwarning.setTextSize(15);*/
            return false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(Mail).matches()){
            email.setError("Email INVALID!!");
            return false;
        }
        else {
            return true;
        }
    }
    public String getDeviceName () {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }
    private String capitalize(String s) {
        if (s == null || s.length()==0){
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)){
            return s;
        }
        else {
            return Character.toUpperCase(first)+ s.substring(1);
        }
    }
}
