package com.example.owner;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import android.view.WindowManager;

import com.github.javiersantos.bottomdialogs.BottomDialog;

public class luncher extends AppCompatActivity {
    //ImageView image, logo;
    ///Animation topAnim, bottomAnim;
    private static int SPLASH_SCREEN = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_luncher);
       /* image = (ImageView) findViewById(R.id.imageView);
        //Animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
//Set animation to elements
        image.setAnimation(topAnim);*/
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                connection_check();
            }
        },SPLASH_SCREEN);

    }

    private void connection_check() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        if (activeNetwork != null) {
            // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                WifiManager manager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = manager.getConnectionInfo();
                if(wifiInfo.getSupplicantState() == SupplicantState.COMPLETED){
                    Intent intent=new Intent(luncher.this,apintro.class);
                    startActivity(intent);
                    finish();
                }
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile network
                Intent intent=new Intent(luncher.this,apintro.class);
                startActivity(intent);
                finish();
            }
        } else {
            // not connected to the internet
            new BottomDialog.Builder(luncher.this)
                    .setTitle("Oops!! No Internet")
                    .setContent("It's look like your device not connected with internet, please connect device with internet to access the app.")
                    .setPositiveText("OK")
                    .setIcon(R.drawable.ok3)
                    .setCancelable(false)
                    .setPositiveBackgroundColorResource(R.color.black)
                    //.setPositiveBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary)
                    .setPositiveTextColorResource(android.R.color.white)
                    //.setPositiveTextColor(ContextCompat.getColor(this, android.R.color.colorPrimary)
                    .onPositive(new BottomDialog.ButtonCallback() {
                        @Override
                        public void onClick(BottomDialog dialog) {
                            dialog.dismiss();
                            finish();
                        }
                    }).show();
        }
         /*   AlertDialog.Builder builder = new AlertDialog.Builder(luncher.this, R.style.nointernet);
            builder.setMessage("Your Device not connected with internet please open internet to interect with me :) ").setCancelable(false)
                    .setTitle("Oops! mobile not connected")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, final int id) {
                            dialog.cancel();
                            finish();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();*/
        }
    }
