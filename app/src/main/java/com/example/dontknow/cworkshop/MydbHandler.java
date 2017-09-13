package com.example.dontknow.cworkshop;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Dont know on 25-07-2017.
 */

public class MydbHandler extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION=1;
    public static final String DATABASE_NAME="CWorkshop.db";
    public static final String TABLE_NAME="OfflineEntry";
    public static final String COLUMN_ID="_id";
    public static final String COLUMN_FIRSTNMAE="_firstname";
    public static final String COLUMN_MIDNAME="_midname";
    public static final String COLUMN_LASTNAME="_lastname";
    public static final String COLUMN_EMAIL="_email";
    public static final String COLUMN_PHONE="_phone";
    public static final String COLUMN_COLLEGE="_college";
    public static final String COLUMN_YEAR="_year";
    public static final String COLUMN_IMAGE="_image";
    public static final String COLUMN_REMAINIG="_remaining";
    public static final String COLUMN_STATUS="_status";


    public MydbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String str= "CREATE TABLE " + TABLE_NAME + " (  "+ COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT ,"+
                COLUMN_FIRSTNMAE +" TEXT "+" , "+
                COLUMN_MIDNAME +" TEXT , "+
                COLUMN_LASTNAME+ " TEXT ,"+
                COLUMN_EMAIL +" TEXT ,"+
                COLUMN_PHONE+ " TEXT ,"+
                COLUMN_COLLEGE +" TEXT ,"+
                COLUMN_YEAR + " TEXT ,"+
                COLUMN_IMAGE +" TEXT ,"+
                COLUMN_REMAINIG + " TEXT ,"+
                COLUMN_STATUS + " TEXT );";
        db.execSQL(str);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(" DROP "+TABLE_NAME+ ";");
        onCreate(db);
    }

    public void add(CWDatabase cwDatabse)
    {
        ContentValues values =new ContentValues();
        values.put(COLUMN_FIRSTNMAE,cwDatabse.get_firstname());
        values.put(COLUMN_MIDNAME,cwDatabse.get_midname());
        values.put(COLUMN_LASTNAME,cwDatabse.get_lastname());
        values.put(COLUMN_EMAIL,cwDatabse.get_email());
        values.put(COLUMN_PHONE,cwDatabse.get_phone());
        values.put(COLUMN_COLLEGE,cwDatabse.get_college());
        values.put(COLUMN_YEAR,cwDatabse.get_year());
        values.put(COLUMN_IMAGE,cwDatabse.get_image());
        values.put(COLUMN_REMAINIG,cwDatabse.get_remaining());
        values.put(COLUMN_STATUS,cwDatabse.get_status());


        SQLiteDatabase db =getWritableDatabase();
        db.insert(TABLE_NAME,null,values);
        db.close();
    }

    public ArrayList<CWDatabase> getDatabase()
    {
        ArrayList<CWDatabase > data = new ArrayList<CWDatabase>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME +" WHERE  1 ;";
        Cursor cursor = db.rawQuery(query , null );
        cursor.moveToFirst();
        while (!cursor.isAfterLast())
        {
            data.add(new CWDatabase(cursor.getString(cursor.getColumnIndex("_firstname")),cursor.getString(cursor.getColumnIndex("_midname")),cursor.getString(cursor.getColumnIndex("_lastname")),cursor.getString(cursor.getColumnIndex("_email")),cursor.getString(cursor.getColumnIndex("_phone")),cursor.getString(cursor.getColumnIndex("_college")),cursor.getString(cursor.getColumnIndex("_year")),cursor.getString(cursor.getColumnIndex("_image")),cursor.getString(cursor.getColumnIndex("_remaining")),cursor.getString(cursor.getColumnIndex("_status"))));
            cursor.moveToNext();
        }
        db.close();
        return data;

    }
    public CWDatabase getCWdatabase(String mail,String phone,String remaining)
    {
        SQLiteDatabase db=getWritableDatabase();
        CWDatabase cwDatabase = null;
        String query = "SELECT * FROM "+TABLE_NAME+" WHERE "+COLUMN_EMAIL+" = \'"+mail+"\' AND "+COLUMN_PHONE+" = \'"+phone+"\' AND "+COLUMN_REMAINIG+" = \'"+remaining+"\' AND "+COLUMN_STATUS+" = "+"\"Not Uploaded\""+" ;";
        Cursor cursor = db.rawQuery(query, null );
        cursor.moveToFirst();
        if(cursor != null)
        {
            try {
                cwDatabase = new CWDatabase(cursor.getString(cursor.getColumnIndex("_firstname")),cursor.getString(cursor.getColumnIndex("_midname")),cursor.getString(cursor.getColumnIndex("_lastname")),cursor.getString(cursor.getColumnIndex("_email")),cursor.getString(cursor.getColumnIndex("_phone")),cursor.getString(cursor.getColumnIndex("_college")),cursor.getString(cursor.getColumnIndex("_year")),cursor.getString(cursor.getColumnIndex("_image")),cursor.getString(cursor.getColumnIndex("_remaining")),cursor.getString(cursor.getColumnIndex("_status")));
            }
            catch (Exception e){}
        }
        return cwDatabase;
    }

    public ArrayList<CWDatabase> getUploadDatabase()
    {
        ArrayList<CWDatabase > data = new ArrayList<CWDatabase>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME +" WHERE  "+COLUMN_STATUS+" = "+"\"Not Uploaded\""+" ;";
        Cursor cursor = db.rawQuery(query , null );
        cursor.moveToFirst();
        if(cursor != null)
        {
            try {
                data.add(new CWDatabase(cursor.getString(cursor.getColumnIndex("_firstname")),cursor.getString(cursor.getColumnIndex("_midname")),cursor.getString(cursor.getColumnIndex("_lastname")),cursor.getString(cursor.getColumnIndex("_email")),cursor.getString(cursor.getColumnIndex("_phone")),cursor.getString(cursor.getColumnIndex("_college")),cursor.getString(cursor.getColumnIndex("_year")),cursor.getString(cursor.getColumnIndex("_image")),cursor.getString(cursor.getColumnIndex("_remaining")),cursor.getString(cursor.getColumnIndex("_status"))));
            }
            catch (Exception e){
                }
        }
        /*while (!cursor.isAfterLast())
        {

            cursor.moveToNext();
        }*/
        db.close();
        return data;

    }
    public void setUploaded(CWDatabase cwDatabase)
    {
        String query = " UPDATE "+TABLE_NAME+ " SET " + COLUMN_STATUS +" = "+"\"UPLOADED\""+" WHERE "+COLUMN_EMAIL +" = '"+cwDatabase.get_email()+"' AND "+COLUMN_PHONE+" = '"+cwDatabase.get_phone()+"' AND "+COLUMN_MIDNAME+" = '"+cwDatabase.get_midname()+"' ; ";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
    }

}
