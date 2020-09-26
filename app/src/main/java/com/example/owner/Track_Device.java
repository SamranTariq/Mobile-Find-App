package com.example.owner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

public class Track_Device extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track__device);
        final AlertDialog.Builder builder = new AlertDialog.Builder(Track_Device.this,R.style.TranslucentDialog);
        builder.setTitle("").setMessage("Available Soon...").setCancelable(false)
                .setPositiveButton("Fine \uD83D\uDE0F", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        finish();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}