package com.example.owner;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SUPERIOR extends Fragment{
    ListView list;
    Button hide,mobilepassword;
    SharedPreferences preferencesdetail;
    private int OVERLAY_PERMISSION_CODE;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_STORAGE_REQUEST_CODE = 101;
    private static final int MY_PHONR_REQUEST_CODE = 102;
    private static final int MY_SMS_REQUEST_CODE = 103;
    private static final int MY_STORAGE_WRITE_CODE=104;
    private boolean askedForOverlayPermission;
    private static final int MY_LOCATION_PERMISSION=105;
    private final static int SEND_SMS_PERMISSION_REQUEST_CODE = 111;
    Integer[] image={ R.drawable.question,
            R.drawable.ic_done_all_black_24dp,
            R.drawable.icon_email,
            R.drawable.number,
            R.drawable.icon_location,
            R.drawable.internet,
            R.drawable.icon_delete
    };
    String[] name={"\nAll done, I wanna test\t\t\t\t\t\t\t\t\t\t\t\t\t\t⊙",
            "\nMake sure Allow all permission's\t\t\t\t\t\t\t\t⊙",
            "\nEmail Where I Received Picture\t\t\t\t\t\t\t\t\t⊙",
            "\nSelected Attempts(previous_time)\t\t\t\t\t\t\t⊙",
            "\nMake sure device location enable all time",
            "\nIf internet not available we send location via SMS",
            "\nCLICK TO DISABLE OR UNINSTALL 'OWNER' APP"};
    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentN;
    Dialog myDialog;
    private static final String TAG = "superiorTest";
    Spinner spinner;
    String limit;
    public static int REQUEST_PERMISSIONS = 1;
    boolean boolean_permission;
    View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activitymaintwo, container, false);
        /******* Start Save Mobile Password Process*******/
        mobilepassword=(Button)view.findViewById(R.id.passwordsave);
        mobilepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
        /******* End Save Mobile Password Process*******/
        list=(ListView)view.findViewById(R.id.listspeerior);
        listview adapter=new listview(getActivity(),image,name);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==6){
                    devicePolicyManager=(DevicePolicyManager)getActivity().getSystemService(Context.DEVICE_POLICY_SERVICE);
                    componentN=new ComponentName(getActivity(),Myadmin.class);
                    boolean active=devicePolicyManager.isAdminActive(componentN);
                        if (active){
                            devicePolicyManager.removeActiveAdmin(componentN);
                            Intent intent=new Intent(Intent.ACTION_DELETE);
                            intent.setData(Uri.parse("package:com.example.owner"));
                            startActivity(intent);
                        }else
                        {
                            Intent intent=new Intent(Intent.ACTION_DELETE);
                            intent.setData(Uri.parse("package:com.example.owner"));
                            startActivity(intent);
                        }
                }else if (position==1){
                    //Device_Administration_Permission();
                }else if (position==2){
                    preferencesdetail = getActivity().getSharedPreferences("DETAIL_OWNER", Context.MODE_PRIVATE);
                    String getemail=preferencesdetail.getString("email","");
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("\t\t\t\t\t\t\tEmail....\n\n\t\t\t-\t"+getemail).setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, final int id) {
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert = builder.create();
                    alert.show();
                }else if (position==3){
                    SharedPreferences preferencesspiner = getActivity().getSharedPreferences("SPINER_POSITION", Context.MODE_PRIVATE);
                    String limit=preferencesspiner.getString("limit","No Number Selected");
                    final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("\n\t\t\t\t\t\t\t\t\t\tLimit\t=\t"+limit).setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, final int id) {
                                    dialog.cancel();
                                }
                            });
                    final AlertDialog alert = builder.create();
                    alert.show();
                }else if (position==0){
                    Snackbar snackbar = null;
                    snackbar.make(view,"Now Enter Wrong Password and check both state(with internet and without internet)",Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Snackbar snackbar1=null;
                            snackbar1.make(v,"AND Don't worry only first time it will take some seconds-:)",Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                }
                            }).show();
                        }
                    }).show();
                }else if (position==4){
                }
            }
        });
        spinner = (Spinner) view.findViewById(R.id.spinners);
        List<String> list = new ArrayList<String>();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_selectable_list_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                limit = parent.getItemAtPosition(position).toString();
                SharedPreferences.Editor editor = getActivity().getPreferences(0).edit();
                int selectedPosition = spinner.getSelectedItemPosition();
                editor.putInt("spinnerSelection", selectedPosition);
                editor.apply();
                /***save limit in sp***/
                SharedPreferences preferencesspiner = getActivity().getSharedPreferences("SPINER_POSITION", Context.MODE_PRIVATE);
                SharedPreferences.Editor editorspiner = preferencesspiner.edit();
                editorspiner.putString("limit",limit);
                editorspiner.commit();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //spinnr postion
        SharedPreferences prefs = getActivity().getPreferences(0);
        spinner.setSelection(prefs.getInt("spinnerSelection",0));
        /*****Popup hide app*****/
        myDialog = new Dialog(getActivity());
        hide=(Button)view.findViewById(R.id.hide);
        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(getActivity())) {
                        askedForOverlayPermission = true;
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getActivity().getPackageName()));
                        startActivityForResult(intent,OVERLAY_PERMISSION_CODE);
                    }
                }
                ShowPopup(v);
            }
        });
        /************************/
        return view;
    }
    /****************Device ADministration permission*/
    /*private void Device_Administration_Permission() {
        devicePolicyManager = (DevicePolicyManager) getActivity().getSystemService(Context.DEVICE_POLICY_SERVICE);
        componentN = new ComponentName(getActivity(), Myadmin.class);
        boolean active = devicePolicyManager.isAdminActive(componentN);
        if (active) {
            Log.d(TAG, "Active");
            Toast.makeText(getActivity(),"Activated:)",Toast.LENGTH_LONG).show();
        } else {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentN);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "why we need this permission?");
            startActivityForResult(intent, RESULT_ENABLE);
        }
    }*/
    /***********Password popup***********/
    public void openDialog() {
        ExampleDialog exampleDialog = new ExampleDialog();
        exampleDialog.show(getActivity().getSupportFragmentManager(), "example dialog");
    }

    /*****Popup hide app*****/
    public void ShowPopup(View v) {
        TextView txtclose;
        Button btnFollow;
        myDialog.setContentView(R.layout.popup);
        txtclose =(TextView) myDialog.findViewById(R.id.txtclose);
        btnFollow = (Button) myDialog.findViewById(R.id.btnfollow);
        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"VIDEO LINK FOR DEMO",Toast.LENGTH_LONG).show();
            }
        });
        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
/*****************/
   /* private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.PROCESS_OUTGOING_CALLS))) {
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.PROCESS_OUTGOING_CALLS},
                        REQUEST_PERMISSIONS);

            }

            if ((ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.PROCESS_OUTGOING_CALLS))) {
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS},
                        REQUEST_PERMISSIONS);

            }
        } else {
            boolean_permission = true;


        }
    }*/
  /*  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_CODE) {
            askedForOverlayPermission = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(getActivity())){
                    Toast.makeText(getActivity(), "ACTION_MANAGE_OVERLAY_PERMISSION Permission Granted", Toast.LENGTH_SHORT).show();
                    if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                        Log.d(TAG,"camera enabled ");
                    }else
                    {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                    }

                }
            }
        }
        switch (requestCode){
            case RESULT_ENABLE:
                if (resultCode == Activity.RESULT_OK){
                    Toast.makeText(getActivity(), "Enable administer permission", Toast.LENGTH_SHORT).show();
                    overlay_permission();
                }else {
                    Toast.makeText(getActivity(), "Problem in enable administer permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    /***************overlay permission *************/
    private void overlay_permission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(getActivity())) {
                askedForOverlayPermission = true;
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getActivity().getPackageName()));
                startActivityForResult(intent,OVERLAY_PERMISSION_CODE);
            }
        }
    }
    /*@RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_CAMERA_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                    if (getActivity().checkSelfPermission(Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, MY_PHONR_REQUEST_CODE);
                    }
                } else {
                    //Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
                    if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
                    }
                }
                break;
            case MY_STORAGE_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_LOCATION_PERMISSION);

                    }
                } else {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_STORAGE_REQUEST_CODE);
                    }
                }
                break;
            case MY_PHONR_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (getActivity().checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, MY_SMS_REQUEST_CODE);
                    }
                   // Toast.makeText(this, "Phone permission granted", Toast.LENGTH_LONG).show();
                } else {
                    if (getActivity().checkSelfPermission(Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS}, MY_PHONR_REQUEST_CODE);
                    }
                }
                break;
            case MY_SMS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                   // Toast.makeText(this, "SMS permission granted", Toast.LENGTH_LONG).show();
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_STORAGE_REQUEST_CODE);
                    }
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_STORAGE_REQUEST_CODE);
                    }
                }else {
                    if (getActivity().checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, MY_SMS_REQUEST_CODE);
                    }
                }
                break;
            case MY_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(),"Granted:)",Toast.LENGTH_LONG).show();
                }else {
                    if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_LOCATION_PERMISSION);

                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }*/
}