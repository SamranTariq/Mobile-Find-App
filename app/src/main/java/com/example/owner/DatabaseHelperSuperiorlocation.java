package com.example.owner;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DatabaseHelperSuperiorlocation extends SQLiteOpenHelper {
    static final String TAG = "Databasehelper";
    public static final String DATABASE_NAME="databaselocation";
    public static final String TABLE_NAME="getlocationtable";
    public static final String COL_1="id";
    public static final String COL_2="latitude";
    public static final String COL_3="longtitude";
    public DatabaseHelperSuperiorlocation( Context context) {
        super(context, DATABASE_NAME, null,4 );
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "(id TEXT PRIMARY KEY,latitude DOUBLE,longtitude DOUBLE)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public boolean insertdata(String id,String latitude,String longtitude){
        Log.d(TAG,"on insertdata");
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,latitude);
        contentValues.put(COL_3,longtitude);
        Long result=db.insert(TABLE_NAME,null,contentValues);

        if (result==-1)
            return false;
        else
            return true;
    }
    public boolean update(String id,String latitude,String longtitude){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,latitude);
        contentValues.put(COL_3,longtitude);
        sqLiteDatabase.update(TABLE_NAME,contentValues,"id=?",new String[]{id});
        return true;
    }
    public Integer deletedata(Integer id){
        SQLiteDatabase database=this.getWritableDatabase();
        return database.delete("passwordlimit","id=?",new String[]{id.toString()});
    }

}
