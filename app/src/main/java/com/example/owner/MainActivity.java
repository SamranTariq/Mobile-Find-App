/*package com.example.owner;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.drm.DrmStore;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.KeyEventDispatcher;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.muddzdev.styleabletoast.StyleableToast;

public class MainActivity extends AppCompatActivity{

    //Create list and set text or image on it
    ListView list;
    Integer[] image={   R.drawable.button,
                    R.drawable.d,
                    R.drawable.alert,
                    R.drawable.delete
                };
    String[] name={"Enable the DEVICE ADMINISTRATION PERMISSION first'",
            "When you want to update your detail use your ID \n Which 'SHOWN UP'",
    "Only first time use SAVE button for saving detail then When you change your detail 'CLICK ON UPDATE BUTTON' to save it",
    "\nClick to disable or uninstal 'IOwner' App"};

    DatabaseHelper mydb;
    EditText editText,editTextone,editTexttwo,editTextthree;
    TextView textView;
    Button save,detail,delete;
    private Switch one;
    public static final int RESULT_ENABLE= 11;
    private DevicePolicyManager devicePolicyManager;
    private ActivityManager activityManager;
    private ComponentName componentN;
    private static final int pic_id=123;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //listview
        list=(ListView)findViewById(R.id.listview);
        listview adapter=new listview(this,image,name);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==3){
                    devicePolicyManager.removeActiveAdmin(componentN);
                    Intent intent=new Intent(Intent.ACTION_DELETE);
                    intent.setData(Uri.parse("package:com.example.owner"));
                    startActivity(intent);
                }
            }
        });

        save=(Button)findViewById(R.id.button1);
        mydb=new DatabaseHelper(this);

        //HERE WE GET DEVICE ADMINISTRATION PERMISSION
        devicePolicyManager=(DevicePolicyManager)getSystemService(DEVICE_POLICY_SERVICE);
        activityManager=(ActivityManager)getSystemService(ACTIVITY_SERVICE);
        componentN=new ComponentName(this,Myadmin.class);
        one=(Switch) findViewById(R.id.switch1);
        editText=(EditText)findViewById(R.id.edittext);

        //Shared prefrence on save button
        SharedPreferences sharedPreferences=this.getSharedPreferences(this.getPackageName(),Context.MODE_PRIVATE);
        one.setChecked(sharedPreferences.getBoolean("one",false));
        save.setVisibility(sharedPreferences.getBoolean("issavevisible",true)? View.VISIBLE : View.GONE);


        one.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked==true){
                    Intent intent=new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,componentN);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,"why we need this permission?");
                    startActivityForResult(intent,RESULT_ENABLE);
                }else{
                    devicePolicyManager.removeActiveAdmin(componentN);
                }
            }
        });

        //ASSIGN ID
        textView=(TextView)findViewById(R.id.textshow);
        editTextone=(EditText)findViewById(R.id.edittext1);
        editTexttwo=(EditText)findViewById(R.id.edittext2);
        editTextthree=(EditText)findViewById(R.id.edittext3);
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
                if(s.toString().equals("")){
                    save.setEnabled(false);
                }
                else
                    save.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //FUNCTION ON SAVE BUTTON
        /*save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



     /*   //FUNCTION ON DELETE BUTTON
        delete=(Button)findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mydb.deletedata(1);
                Toast.makeText(MainActivity.this,"Delte succesfully",Toast.LENGTH_SHORT).show();
            }
        });

        //SHOW ID IN TEXT VIEW
        SQLiteDatabase database=mydb.getReadableDatabase();//id,
        Cursor cursor= database.rawQuery(" select id,name,phone_no,email from owner_detail",new String[]{});
        if (cursor.getCount()== 0){
            Toast.makeText(MainActivity.this,"Error:Nothing To Show",Toast.LENGTH_LONG).show();
            return;
        }
        StringBuffer stringBuffer=new StringBuffer();
        while (cursor.moveToNext()){
            stringBuffer.append(""+cursor.getString(0)+"\n");
        }
        textView.setText("Your 'ID' is = "+stringBuffer);

       // CustomAdapter customAdapter=new CustomAdapter();
       // listView.setAdapter(customAdapter);

    }


    //CHECK ADMINISTRATION PERMISSION ENABLE OR NOT
    @Override
    protected void onResume() {
        super.onResume();
        boolean inactive=devicePolicyManager.isAdminActive(componentN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case RESULT_ENABLE:
                if (resultCode== Activity.RESULT_OK){
                    Toast.makeText(MainActivity.this,"You have enable th device admin feature",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(this, "Problem in enable administer permission", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //shared preference and Sqlite on save button
    public void save(View view){
        SharedPreferences sharedPreferences=this.getSharedPreferences(this.getPackageName(),Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("one",true).apply();
        one.setChecked(true);
       sharedPreferences.edit().putBoolean("issavevisible",false).apply();
       save.setVisibility(View.INVISIBLE);

        Boolean isinserted = mydb.insertdata(editText.getText().toString(),
                editTextone.getText().toString(),
                editTexttwo.getText().toString(),
                editTextthree.getText().toString());
        if (isinserted = true)
            Toast.makeText(MainActivity.this, "insert succesfully", Toast.LENGTH_LONG).show();
        else
        {
            Toast.makeText(MainActivity.this, "insert not perform", Toast.LENGTH_LONG).show();
        }
    }
}*/