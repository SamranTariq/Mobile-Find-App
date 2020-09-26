package com.example.owner;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;

import com.example.owner.Locker.LockExplain;

public class apintro extends AppCompatActivity {

    Button startpermissionactivity;
    Animation animslide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apintro);
        SharedPreferences sharedPreferences=getSharedPreferences("PREFERENCE",MODE_PRIVATE);
        String FirstTime= sharedPreferences.getString("FirstTimeInstall","");
        if (checkPermission(Manifest.permission.SEND_SMS)){
            if (FirstTime.equals("Yes")){
                Intent intent=new Intent(apintro.this,signin.class);
                startActivity(intent);
                finish();
            }else {
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putString("FirstTimeInstall","Yes");
                editor.apply();
            }
        }else {
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("FirstTimeInstall","Yes");
            editor.apply();
        }
        startpermissionactivity=(Button)findViewById(R.id.startpermissionactivity);
        startpermissionactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          /*    animslide = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.move);
                startpermissionactivity.startAnimation(animslide);*/
                Intent intent=new Intent(apintro.this, LockExplain.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private boolean checkPermission(String permission){
        int checkPermission = ContextCompat.checkSelfPermission(this,permission);
        return checkPermission == PackageManager.PERMISSION_GRANTED;
    }
}