package com.riact.ritimelog.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by koushik on 13/8/17.
 */

public class DbHandler extends SQLiteOpenHelper {

    public static final int DB_VERSION=1;
    public static final String DATABASE_NAME = "RiTimeDB";

    public static final String TABLE_SITE = "SITE_DETAILS";
    public static final String KEY_SITE_DATA = "site_data";

    public static final String TABLE_EMPLOYEE = "EMPLOYEE_DETAILS";
    public static final String KEY_SITE_CODE = "site_code";
    public static final String KEY_EMPLOYEE_DATA = "employee_data";

    public static final String TABLE_CURRENT_SITE = "CURRENT_SITE";
    public static final String KEY_SITE = "SITE_CODE";


    public DbHandler(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuerry="CREATE TABLE " + TABLE_SITE + "("
                + KEY_SITE_DATA + "  TEXT"+")";
        db.execSQL(createTableQuerry);

        createTableQuerry="CREATE TABLE " + TABLE_EMPLOYEE + "("
                + KEY_SITE_CODE + "  TEXT,"+ KEY_EMPLOYEE_DATA+" TEXT"+")";
        db.execSQL(createTableQuerry);

        createTableQuerry="CREATE TABLE " + TABLE_CURRENT_SITE + "("
                + KEY_SITE + "  TEXT"+")";
        db.execSQL(createTableQuerry);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SITE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEE);
        onCreate(db);

    }

    public void addSiteDetails(String siteData)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SITE_DATA,siteData);
        db.insert(TABLE_SITE, null, values);
        db.close();
    }

    public String getSiteDetails()
    {

        List<String> item = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SITE;
        SQLiteDatabase db = this.getWritableDatabase();

        try {


            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    item.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
        }
        catch (Exception e)
        {

        }
        finally {
            db.close();
        }

        return item.get(0);

    }

    public void deleteSites(){

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SITE);

        String createTableQuerry="CREATE TABLE " + TABLE_SITE + "("
                + KEY_SITE_DATA + "  TEXT" +")";
        db.execSQL(createTableQuerry);

    }

    public void addEmployeeDetails(String siteCode,String employeeData){

        String selectQuery = "SELECT  * FROM " + TABLE_EMPLOYEE +" WHERE site_code='"+siteCode+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_EMPLOYEE_DATA,employeeData);
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                db.update(TABLE_EMPLOYEE, values, KEY_SITE_CODE+"='"+siteCode+"'",null);
            }
            else {
                values.put(KEY_SITE_CODE,siteCode);
                db.insert(TABLE_EMPLOYEE, null, values);

            }
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
        finally {
            db.close();
        }
    }

    public void addCurrentSite(String siteCode)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_SITE,siteCode);
        db.insert(TABLE_CURRENT_SITE, null, values);
        db.close();
    }

    public List<String> getCurrentSite()
    {
        List<String> item = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CURRENT_SITE;
        SQLiteDatabase db = this.getWritableDatabase();

        try {


            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    item.add(cursor.getString(0));
                } while (cursor.moveToNext());
            }
        }
        catch (Exception e)
        {

        }
        finally {
            db.close();
        }

        return item;
    }

    public void deleteCurrentSite(){

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRENT_SITE);

        String createTableQuerry="CREATE TABLE " + TABLE_CURRENT_SITE + "("
                + KEY_SITE + "  TEXT"+")";
        db.execSQL(createTableQuerry);

    }
}
