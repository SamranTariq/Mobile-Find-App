package com.example.owner;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class appintro2 extends AppCompatActivity {
    Button startpermissionactivity2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appintro2);
        startpermissionactivity2=(Button)findViewById(R.id.startpermissionactivity2);
        startpermissionactivity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(appintro2.this,apppermiss2.class);
                startActivity(intent);
            }
        });
    }
}
