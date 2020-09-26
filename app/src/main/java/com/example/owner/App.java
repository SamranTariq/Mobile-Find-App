package com.example.owner;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import com.example.owner.Locker.activities.lock.GestureUnlockActivity;
import com.example.owner.Locker.base.BaseActivity;
import com.example.owner.Locker.utils.SpUtil;

import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.List;

public class App extends LitePalApplication {
    public static final String FCM_CHANNEL_ID = "FCM_CHANNEL_ID";
    private static App application;
    private static List<BaseActivity> activityList;

    public static App getInstance() {
        Log.d("goback", String.valueOf(application));
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel fcmChannel= new NotificationChannel(
                    FCM_CHANNEL_ID,"FCM_Channel", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(fcmChannel);
        }
        SpUtil.getInstance().init(application);
        activityList = new ArrayList<>();
    }

    public void doForCreate(BaseActivity activity) {
        activityList.add(activity);
        Log.d("goback", String.valueOf(activityList));
    }

    public void doForFinish(BaseActivity activity) {
        activityList.remove(activity);
    }

    public void clearAllActivity() {
        try {
            for (BaseActivity activity : activityList) {
                if (activity != null && !clearAllWhiteList(activity))
                    activity.clear();
            }
            activityList.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean clearAllWhiteList(BaseActivity activity) {
        return activity instanceof GestureUnlockActivity;
    }
}