package com.riact.ritimelog.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
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

    public static final String TABLE_SYNCED_DATA = "SYNCED_DETAILS";
    public static final String KEY_EMPLOYEE_CODE = "EMPLOYEE_CODE";
    public static final String KEY_REGISTERED_TIME = "REGISTERED_TIME";

    public static final String TABLE_TOSYNC_DATA = "TOSYNC_DETAILS";
    public static final String KEY_SYNC_URL = "URL";

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

        createTableQuerry="CREATE TABLE " + TABLE_SYNCED_DATA + "("
                + KEY_EMPLOYEE_CODE + "  TEXT, "+KEY_REGISTERED_TIME+" BIGINT"+")";
        db.execSQL(createTableQuerry);

        createTableQuerry="CREATE TABLE " + TABLE_TOSYNC_DATA + "("
                + KEY_EMPLOYEE_CODE + "  TEXT, "+KEY_REGISTERED_TIME+" BIGINT, "+KEY_SYNC_URL+" TEXT" +")";
        db.execSQL(createTableQuerry);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SITE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CURRENT_SITE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SYNCED_DATA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOSYNC_DATA);
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

        if(item.isEmpty())
        {
            return null;
        }
        else {

            return item.get(0);
        }

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

    public List<String> getEmployeeDtails(String siteCode)
    {
        List<String> item = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_EMPLOYEE +" WHERE site_code='"+siteCode+"'";
        SQLiteDatabase db = this.getWritableDatabase();

        try {


            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    item.add(cursor.getString(1));
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
        db.close();

    }

    public void addSyncedDetails(String empCode,long date)
    {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_EMPLOYEE_CODE, empCode);
            values.put(KEY_REGISTERED_TIME, date);
            db.insert(TABLE_SYNCED_DATA, null, values);
            db.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public List<List> getSyncedEmpDetails(long fromdate,long toDate)
    {
        List<String> empCode = new ArrayList<String>();
        List<String> date =  new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SYNCED_DATA +" where "+KEY_REGISTERED_TIME+" between "+fromdate +" and "+toDate;
        SQLiteDatabase db = this.getWritableDatabase();

        try {


            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    empCode.add(cursor.getString(0));
                    date.add(cursor.getString(1));
                } while (cursor.moveToNext());
            }
        }
        catch (Exception e)
        {

        }
        finally {
            db.close();
        }
        List<List> finalList = new ArrayList<List>();
        finalList.add(empCode);
        finalList.add(date);

        return finalList;
    }

    public void deleteSyncedEmpDetails(){

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SYNCED_DATA);

        String createTableQuerry="CREATE TABLE " + TABLE_SYNCED_DATA + "("
                + KEY_EMPLOYEE_CODE + "  TEXT, "+KEY_REGISTERED_TIME+" BIGINT"+")";
        db.execSQL(createTableQuerry);
        db.close();

    }

    public void addToSyncData(String empCode,long date,String url)
    {
        SQLiteDatabase db=null;

        try {
            db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_EMPLOYEE_CODE, empCode);
            values.put(KEY_REGISTERED_TIME, date);
            values.put(KEY_SYNC_URL, url);
            db.insert(TABLE_TOSYNC_DATA, null, values);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            db.close();
        }

    }
    public void deleteToSyncedEmpDetails(){

        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOSYNC_DATA);

        String createTableQuerry="CREATE TABLE " + TABLE_TOSYNC_DATA + "("
                + KEY_EMPLOYEE_CODE + "  TEXT, "+KEY_REGISTERED_TIME+" BIGINT, "+KEY_SYNC_URL+" TEXT" +")";
        db.execSQL(createTableQuerry);
        db.close();

    }

    public ArrayList<List<String>> getAllToSyncData()
    {
        ArrayList<List<String>> toSyncList = new ArrayList<List<String>>();
        List<String> empCodeList = new ArrayList<String>();
        List<String> dateList = new ArrayList<String>();
        List<String> urlList = new ArrayList<String>();

        String selectQuery = "SELECT  * FROM " + TABLE_TOSYNC_DATA;
        SQLiteDatabase db = this.getWritableDatabase();

        try {


            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    empCodeList.add(cursor.getString(0));
                    dateList.add(cursor.getString(1));
                    urlList.add(cursor.getString(2));
                } while (cursor.moveToNext());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

        }
        finally {
            db.close();
        }

        toSyncList.add(empCodeList);
        toSyncList.add(dateList);
        toSyncList.add(urlList);

        return toSyncList;
    }
}
