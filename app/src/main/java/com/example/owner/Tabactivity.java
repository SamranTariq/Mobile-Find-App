package com.example.owner;

import android.Manifest;
import android.animation.Animator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.owner.Locker.activities.lock.GestureCreateActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.List;

import static com.example.owner.GENERIC.RESULT_ENABLE;

public class Tabactivity extends AppCompatActivity implements ExampleDialog.ExampleDialogListener,NavigationView.OnNavigationItemSelectedListener {
    Dialog dialog;
    FloatingActionButton floatingActionButton,fab1,fab2;
    boolean isFABOpen;
    ConstraintLayout constraintLayout;
    private DrawerLayout drawer;
    View fabBGLayout;
    LinearLayout fablayout1,fablayout2;
    Dialog myDialog;
    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentN;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuon, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String sub = "I Owner";
            String body = "";
            intent.putExtra(Intent.EXTRA_SUBJECT, sub);
            intent.putExtra(Intent.EXTRA_TEXT, body);
            startActivity(Intent.createChooser(intent, "Share Using"));
        } else if (id == R.id.help) {
//showdialog();
            dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialogemail);
            constraintLayout = dialog.findViewById(R.id.activity_main_done);
            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            Toast.makeText(this, "TOCH ON SCREEN TO GO BACK", Toast.LENGTH_LONG).show();
            dialog.show();
        } else if (id == R.id.about) {
            Intent intent = new Intent(Tabactivity.this, About.class);
            startActivity(intent);
        } else if (id == R.id.contact) {
            Intent intent = new Intent(Tabactivity.this, Contact.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (isFABOpen) {
            closeFABMenu();
        }else {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent a = new Intent(Intent.ACTION_MAIN);
                            a.addCategory(Intent.CATEGORY_HOME);
                            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(a);
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabactivity);
        fablayout1=(LinearLayout)findViewById(R.id.fabLayout1);
        fablayout2=(LinearLayout)findViewById(R.id.fabLayout2);
        fabBGLayout = findViewById(R.id.fabBGLayout);
        fabBGLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeFABMenu();
            }
        });
        floatingActionButton=(FloatingActionButton)findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fabminione);
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Tabactivity.this,Track_Device.class));
                overridePendingTransition(0, 0);
            }
        });
        fab2 = (FloatingActionButton) findViewById(R.id.fabminitwo);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Tabactivity.this,TestOwner.class));
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isFABOpen){
                    showFABMenu();
                }else{
                    closeFABMenu();
                }
            }
        });
        Animation pulse = AnimationUtils.loadAnimation(this, R.anim.pulse_anim);
        floatingActionButton.startAnimation(pulse);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try
        {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }catch (Exception e){

        }
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setCheckedItem(R.id.home);
        SharedPreferences avatarpreference = getSharedPreferences("avatar1", MODE_PRIVATE);
        boolean firstStart0 = avatarpreference.getBoolean("firstStart0", true);
        if (firstStart0) {
            avatar();
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
    /*     LinearLayout layout = ((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(0));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.weight =0.5f; // e.g. 0.5f
        layout.setLayoutParams(layoutParams);*/
/*        tabLayout.addTab(tabLayout.newTab().setText(""));
        tabLayout.getTabAt(0).setIcon(R.drawable.icon2);*/
        tabLayout.addTab(tabLayout.newTab().setText("GENERIC"));
        tabLayout.addTab(tabLayout.newTab().setText("SUPERIOR"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        pageAdapter pageAdapter = new pageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
    //    viewPager.setCurrentItem(1);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void applyTexts(String password) {

    }

    /*@Override
    public void onLocationChanged(Location location) {
        String s1 = ("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());
        Toast.makeText(this,"h" + s1,Toast.LENGTH_LONG).show();
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String s = (s1 + "\n" + addresses.get(0).getAddressLine(0) + ", " +
                    addresses.get(0).getAddressLine(1) + ", " + addresses.get(0).getAddressLine(2));
            Toast.makeText(this,"hr" + s,Toast.LENGTH_LONG).show();
        } catch (Exception e) {

        }
    }*/
    private void avatar() {
        String avatar = "/static/images/user.png";
        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Images").child("imageuri").setValue(avatar);
        SharedPreferences avatarpreference = getSharedPreferences("avatar1", MODE_PRIVATE);
        SharedPreferences.Editor editor = avatarpreference.edit();
        editor.putBoolean("firstStart0", false);
        editor.apply();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.applockpattren:
                startActivity(new Intent(Tabactivity.this, GestureCreateActivity.class));
                break;
            case R.id.hideapp:
                final AlertDialog.Builder builder2 = new AlertDialog.Builder(this,R.style.imagealert);
                builder2.setMessage("\tHide App Code\t\t-->\t\t*564#\n\tUnhide App Code\t\t-->\t\t*654#").setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert2 = builder2.create();
                alert2.show();
                break;
            case R.id.permission:
                TedPermission.with(this)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.CAMERA,Manifest.permission.PROCESS_OUTGOING_CALLS,Manifest.permission.SEND_SMS,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_PHONE_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                        .check();
                break;
            case R.id.info:
                SharedPreferences preferencesdetail = getSharedPreferences("DETAIL_OWNER", MODE_PRIVATE);
                String getname = preferencesdetail.getString("username", "");
                String getphone=preferencesdetail.getString("phone","");
                String getemail=preferencesdetail.getString("email","");
                final AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.imagealert);
                builder.setMessage("\t\t\t\t\t\t\tYour Detail....\n\n\t-\t\t"+getname+"\n\t-\t\t"+getphone+"\n\t-\t\t"+getemail).setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
                break;
            case R.id.emailrecieved:
                preferencesdetail = getSharedPreferences("DETAIL_OWNER", Context.MODE_PRIVATE);
                String email=preferencesdetail.getString("email","");
                Snackbar.make(drawer,"Here"+email,Snackbar.LENGTH_INDEFINITE).setAction("Gotcha", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                }).show();
                break;
            case R.id.rateus:
                Toast.makeText(this, "Available Soon", Toast.LENGTH_SHORT).show();
                break;
            case R.id.help:
                Toast.makeText(this, "Available Soon", Toast.LENGTH_SHORT).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Device_Administration_Permission();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(Tabactivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }

    };
    /****************Device ADministration permission*/
    private void Device_Administration_Permission() {
        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentN = new ComponentName(Tabactivity.this, Myadmin.class);
        boolean active = devicePolicyManager.isAdminActive(componentN);
        if (active) {
            Toast.makeText(Tabactivity.this,"Activated",Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentN);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "why we need this permission?");
            startActivityForResult(intent, RESULT_ENABLE);
        }
    }

    private void showFABMenu(){
        isFABOpen = true;
        floatingActionButton.animate().rotationBy(-45);
        fablayout1.animate().translationY(-getResources().getDimension(R.dimen.standard_55));
        fablayout2.animate().translationY(-getResources().getDimension(R.dimen.standard_105)).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                fablayout1.setVisibility(View.VISIBLE);
                fablayout2.setVisibility(View.VISIBLE);
                fabBGLayout.setVisibility(View.VISIBLE);
                fab1.animate().rotationBy(360);
                fab2.animate().rotationBy(360);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void closeFABMenu(){
        isFABOpen=false;
        fabBGLayout.setVisibility(View.GONE);
        floatingActionButton.animate().rotation(0);
        fablayout1.animate().translationY(0);
        fablayout2.animate().translationY(0).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if (!isFABOpen) {
                    fablayout1.setVisibility(View.GONE);
                    fablayout2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }
}
