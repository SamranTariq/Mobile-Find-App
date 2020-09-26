package com.example.owner;

import android.Manifest;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

public class GENERIC extends Fragment{
    ListView list;
    View view;
    Button buttonforimage;
    Integer[] image={ R.drawable.info,
            R.drawable.money,
            R.drawable.camera,
            R.drawable.ic_phonelink_lock_black_24dp,
            R.drawable.icon_press
    };
    String[] name={"\nSHOW  MY  SAVED  INFORMATION\t\t\t\t\t\t\t\t⊙",
            "\nAFFORDABLE  INTERNET  PACKAGE'S\t\t\t\t\t\t⊙",
            "YOU  ABLE  TO  UPLOAD  YOUR  IMAGE  ONLY  ONE  TIME(be careful)",
            "MUST  ENTER  MINIMUM  4  DIGIT  ON  UNLOCK  ATTEMPTS",
            "\nMAKE  SURE  YOU  CHECK  YOUR  SPAM  FOLDER"};
    private static final String TAG = "GENERIC";
    EditText editTextone,editTexttwo,editTextthree;
    Button save;
    SharedPreferences preferencesdetail;
    public static final int RESULT_ENABLE= 1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main, container, false);
        SharedPreferences  snakpreference= getActivity().getSharedPreferences("passworddialogue2", MODE_PRIVATE);
        boolean firstStart = snakpreference.getBoolean("firstStart", true);
        if (firstStart) {
          //  showpopup();
        }
       // view=getActivity().findViewById(android.R.id.content);
        save=(Button)view.findViewById(R.id.button1);
        buttonforimage=(Button)view.findViewById(R.id.buttonimage);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean enabled = pref.getBoolean("isEnabled",true);
        buttonforimage.setEnabled(enabled);
        buttonforimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.imagealert);
                builder.setMessage("you able to upload your image only one time\nNow if you hit ok then you won't able to click this button again.").setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                               // Toast.makeText(getActivity(),"Please Upload your Image And 'Then Upload Your Detail' Otherwise Application Will be Crashed.",Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(getActivity(),UserImageActivity.class);
                                startActivity(intent);
                                save.setEnabled(true);
                                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                pref.edit().putBoolean("isEnabled",false).commit();
                                buttonforimage.setEnabled(false);
                            }
                        })
                        .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                            public void onClick(final DialogInterface dialog, final int id) {
                                dialog.cancel();
                            }
                        });
                final AlertDialog alert = builder.create();
                alert.show();
            }
        });
        //listview
        list=(ListView)view.findViewById(R.id.listview);
        listview adapter=new listview(getActivity(),image,name);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    preferencesdetail = getActivity().getSharedPreferences("DETAIL_OWNER", MODE_PRIVATE);
                    String getname = preferencesdetail.getString("username", "");
                    String getphone=preferencesdetail.getString("phone","");
                    String getemail=preferencesdetail.getString("email","");
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("\t\t\t\t\t\t\tYour Detail....\n\n\t-\t\t"+getname+"\n\t-\t\t"+getphone+"\n\t-\t\t"+getemail).setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, final int id) {
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert = builder.create();
                    alert.show();
                }else if (position==1){
                    Toast.makeText(getActivity(),"Internet packge information here:)",Toast.LENGTH_LONG).show();
                }else if (position==2){
                    ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
                    if (activeNetwork != null) {
                        // connected to the internet
                        if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                            WifiManager manager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                            WifiInfo wifiInfo = manager.getConnectionInfo();
                            if(wifiInfo.getSupplicantState() == SupplicantState.COMPLETED){
                                String ssid = wifiInfo.getSSID();
                                Toast.makeText(getActivity(),ssid,Toast.LENGTH_LONG).show();  //service set identifier
                            }
                        } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                            // connected to the mobile network
                            Toast.makeText(getActivity(),"Mobile Data Connected",Toast.LENGTH_LONG).show();
                        }
                    } else {
                        // not connected to the internet
                        Toast.makeText(getActivity(),"No Internet",Toast.LENGTH_LONG).show();
                    }
                }else if (position==3){
                }else if (position==4){
                    TelephonyManager telephonyManager = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    final String imei = telephonyManager.getDeviceId();
                    Toast.makeText(getActivity(),imei,Toast.LENGTH_LONG).show();
                }
            }
        });
        //ASSIGN ID
        editTextone=(EditText)view.findViewById(R.id.edittext1);
        editTexttwo=(EditText)view.findViewById(R.id.edittext2);
        editTextthree=(EditText)view.findViewById(R.id.edittext3);
        //DISABLE LAST THREE EDITTEXT
        editTexttwo.setEnabled(false);
        editTextthree.setEnabled(false);
        //TASK ON EDITTEXT ONE
        editTextone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    editTexttwo.setEnabled(false);
                }
                else
                    editTexttwo.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //TASK ON EDITTEXT TWO
        editTexttwo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    editTextthree.setEnabled(false);
                }
                else
                    editTextthree.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //TASK ON EDITTEXT THREE
        editTextthree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextone.getText().toString().equals("") || editTexttwo.getText().toString().equals("") || editTextthree.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter all field first", Toast.LENGTH_LONG).show();
                } else
                {
                    String name=editTextone.getText().toString();
                    String phone=editTexttwo.getText().toString();
                    String email=editTextthree.getText().toString();
                    preferencesdetail = getActivity().getSharedPreferences("DETAIL_OWNER", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferencesdetail.edit();
                    editor.putString("username",name);
                    editor.putString("phone",phone);
                    editor.putString("email",email);
                    editor.commit();
                    editTextone.setText("");
                    editTexttwo.setText("");
                    editTextthree.setText("");
                    //Toast.makeText(getActivity(),"Go On Superior Side",Toast.LENGTH_LONG).show();
                    Snackbar.make(view,"GO ON SUPERIOR SIDE-:)",Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
            }
            }
        });
    return view;
    }

    private void showpopup() {
        new AlertDialog.Builder(getActivity())
                .setTitle("Neccessary")
                .setMessage("Please use |password| instead of \n|patteren or pin| otherwise owner won't able to work on wrong attempt.")
                .setPositiveButton("Already Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNegativeButton("Change Password Type", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
                startActivity(intent);
            }
        })
                .create().show();
        SharedPreferences snakpreference = getActivity().getSharedPreferences("passworddialogue2", MODE_PRIVATE);
        SharedPreferences.Editor editor = snakpreference.edit();
        editor.putBoolean("firstStart", false);
        editor.apply();
    }
    @Override
    public void onResume() {
        super.onResume();
    }
}