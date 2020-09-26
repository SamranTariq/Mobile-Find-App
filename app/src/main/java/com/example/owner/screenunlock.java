package com.example.owner;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


public class screenunlock extends BroadcastReceiver {
    private static final String TAG = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
            Toast.makeText(context,"ok",Toast.LENGTH_LONG).show();
            Log.d(TAG,"Screen on");
        }
        if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
            Log.d(TAG,"Screen off");
            Toast.makeText(context,"no",Toast.LENGTH_LONG).show();
        }
    }
}
