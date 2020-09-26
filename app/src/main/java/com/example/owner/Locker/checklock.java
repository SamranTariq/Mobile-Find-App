package com.example.owner.Locker;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

import com.example.owner.R;
import com.example.owner.Locker.activities.lock.GestureCreateActivity;
import com.example.owner.Locker.base.AppConstants;
import com.example.owner.Locker.services.BackgroundManager;
import com.example.owner.Locker.services.LockService;
import com.example.owner.Locker.utils.SpUtil;

public class checklock extends AppCompatActivity {
    private static final String TAG="checklock";
    Button lock,unlock,passwordchange;
    private static final int REQUEST_CHANGE_PWD = 3;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checklock);
        lock=findViewById(R.id.button);
        unlock=findViewById(R.id.button2);
        passwordchange=findViewById(R.id.button3);
        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtil.getInstance().putBoolean(AppConstants.LOCK_STATE, true);
                BackgroundManager.getInstance().init(checklock.this).stopService(LockService.class);
                BackgroundManager.getInstance().init(checklock.this).startService(LockService.class);

                BackgroundManager.getInstance().init(checklock.this).startAlarmManager();
            }
        });

        unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpUtil.getInstance().putBoolean(AppConstants.LOCK_STATE, false);
                BackgroundManager.getInstance().init(checklock.this).stopService(LockService.class);
                BackgroundManager.getInstance().init(checklock.this).stopAlarmManager();
            }
        });
        passwordchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(checklock.this, GestureCreateActivity.class);
                startActivityForResult(intent, REQUEST_CHANGE_PWD);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }
}






































