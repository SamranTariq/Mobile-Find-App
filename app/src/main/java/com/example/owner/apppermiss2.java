package com.example.owner;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class apppermiss2 extends AppCompatActivity {
    Button startsigninactivity2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apppermiss2);
        startsigninactivity2=(Button)findViewById(R.id.startsigninactivity2);
        startsigninactivity2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(apppermiss2.this,signin.class);
                startActivity(intent);
            }
        });
    }
}
