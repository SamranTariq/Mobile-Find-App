package com.example.owner;

import android.content.Context;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFireBaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FCM";
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String token=FirebaseInstanceId.getInstance().getToken();
        Log.d("Token","*************Token***********************"+token);
        SharedPreferences preferences = getSharedPreferences("FCM_TOKEN", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token",token);
        editor.commit();
    }
}
