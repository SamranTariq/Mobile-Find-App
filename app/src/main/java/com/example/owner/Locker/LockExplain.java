package com.example.owner.Locker;

import android.app.AppOpsManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.owner.Locker.activities.pwd.CreatePwdActivity;
import com.example.owner.Locker.base.AppConstants;
import com.example.owner.Locker.services.BackgroundManager;
import com.example.owner.Locker.services.LoadAppListService;
import com.example.owner.Locker.utils.SpUtil;
import com.example.owner.R;
import com.example.owner.appermiss;

import java.util.List;

public class LockExplain extends AppCompatActivity {
    private static final int RESULT_ACTION_USAGE_ACCESS_SETTINGS = 1;
    private static final int RESULT_ACTION_ACCESSIBILITY_SETTINGS = 3;

    Button permission;
    TextView allow;
    Animation animBlink;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock_explain);
        permission=(Button)findViewById(R.id.lockstart);
        allow=(TextView)findViewById(R.id.textView16);
        allow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animBlink = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.blink);
                permission.startAnimation(animBlink);
            }
        });
        BackgroundManager.getInstance().init(this).startService(LoadAppListService.class);
        SpUtil.getInstance().putBoolean(AppConstants.LOCK_STATE, false);
        permission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ispermission = SpUtil.getInstance().getBoolean(AppConstants.LOCK_IS_FIRST_LOCK,true);
                if (ispermission) {
                    ShowDialog();
                } else {
                    Intent intent = new Intent(LockExplain.this, appermiss.class);
                    intent.putExtra(AppConstants.LOCK_PACKAGE_NAME, AppConstants.APP_PACKAGE_NAME);
                    intent.putExtra(AppConstants.LOCK_FROM, AppConstants.LOCK_FROM_LOCK_MAIN_ACITVITY);
                    startActivity(intent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                }
            }
        });
    }

    private void ShowDialog() {
        if (!isStatAccessPermissionSet(LockExplain.this) && isNoOption(LockExplain.this)) {
            dialog = new Dialog(new ContextThemeWrapper(LockExplain.this, R.style.Theme_AppCompat_Dialog_Alert));
            dialog.setContentView(R.layout.customdailogelock);
            TextView dialogButton = (TextView) dialog.findViewById(R.id.btn_permission);
            // if button is clicked, close the custom dialog
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        Intent intent = null;
                        intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        startActivityForResult(intent, RESULT_ACTION_USAGE_ACCESS_SETTINGS);
                    }
                }

            });
            dialog.show();
        } else {
            LockMainActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_ACTION_USAGE_ACCESS_SETTINGS) {
            if (isStatAccessPermissionSet(LockExplain.this)) {
           //     prefs.edit().putBoolean(AppConstants.LOCK_IS_FIRST_LOCK,false);
                LockMainActivity();
            } else {
                Toast.makeText(this,"Denied",Toast.LENGTH_LONG).show();
                finish();
            }
        }
        if (requestCode == RESULT_ACTION_ACCESSIBILITY_SETTINGS) {
            LockMainActivity();
        }
    }

    public static boolean isStatAccessPermissionSet(@NonNull Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                PackageManager packageManager = context.getPackageManager();
                ApplicationInfo info = packageManager.getApplicationInfo(context.getPackageName(), 0);
                AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, info.uid, info.packageName);
                return appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, info.uid, info.packageName) == AppOpsManager.MODE_ALLOWED;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return true;
        }
    }
    public static boolean isNoOption(@NonNull Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            PackageManager packageManager = context.getPackageManager();
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            return list.size() > 0;
        }
        return false;
    }
    private void LockMainActivity() {
        Intent intent2 = new Intent(LockExplain.this, CreatePwdActivity.class);
        startActivity(intent2);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dialog.dismiss();
    }
}