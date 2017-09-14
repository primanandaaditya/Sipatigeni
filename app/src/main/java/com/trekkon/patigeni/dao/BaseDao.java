package com.trekkon.patigeni.dao;

/**
 * Created by Trekkon on 22/08/2017.
 */


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.trekkon.patigeni.PatigeniApp;
import com.trekkon.patigeni.R;
import com.trekkon.patigeni.activities.SplashScreen;
import com.trekkon.patigeni.helper.MessageHelper;
import com.trekkon.patigeni.utils.CommonUtils;

public abstract class BaseDao extends SQLiteOpenHelper {

    protected static  String DB_PATH = "";
    //	protected static final String DB_PATH = "data/data/com.unilever.ais/databases/";
    protected static String DB_NAME = "patigeni.db";
    public static SQLiteDatabase db = null;

    public abstract void insert(Object obj) throws Exception;

    public abstract void update(Object obj) throws Exception;

    public abstract Object viewEntity(String[] args) throws Exception;

    public final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    public final Lock r = rwl.readLock();
    public final Lock w = rwl.writeLock();


    public BaseDao(Context context) {
        super(context, DB_NAME, null, 3);
    }

    public void insert(String tableName, ContentValues cVal) throws Exception {
        try {
            w.lock();
            open();
            db.beginTransaction();
            db.insert(tableName, null, cVal);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            throw e;
        } finally {
            db.endTransaction();
            close();
            w.unlock();
        }
    }

    public void update(String tableName, ContentValues cVal, String whereAs, String[] whereArgs)
            throws Exception {
        w.lock();
        try {
            open();
            db.beginTransaction();
            db.update(tableName, cVal, whereAs, whereArgs);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            throw e;
        } finally {
            db.endTransaction();
            close();
            w.unlock();
        }
    }

    public void delete(String tableName, String whereClause, String[] whereArgs)
            throws Exception {
        w.lock();
        try {
            open();
            db.beginTransaction();
            db.delete(tableName, whereClause, whereArgs);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            throw e;
        } finally {
            db.endTransaction();
            close();
            w.unlock();
        }

    }

    public static SQLiteDatabase open() throws Exception {
        try {
            CommonUtils util;
            util = new CommonUtils(PatigeniApp.getContext());
//            DB_PATH = "/data/data/" + util.getPackageName() + "/database/";
            DB_PATH = "/data/data/" + "com.trekkon.patigeni/databases/";
            DB_NAME = "patigeni.db";
            String path = DB_PATH + DB_NAME;
            MessageHelper.showOnThread(PatigeniApp.getContext(),path);
//            Log.i("database Patigeni", path);
            db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException ex) {
            throw ex;
        } catch (Exception e) {
            throw e;
        }
        return db;



    }


    @Override
    public synchronized void close() {
        if (db != null)
            db.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public boolean isAvail(String tableName, String param, String[] value) throws Exception {
        boolean isAvail = false;
        Cursor mCount = null;
        w.lock();
        try {
            open();
            mCount = db.rawQuery("select count(*) from " + tableName + " where " + param + " = ?", value);
            mCount.moveToFirst();
            if (mCount.getInt(0) != 0) {
                isAvail = true;
            }
            mCount.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (mCount != null) mCount.close();
            close();
            w.unlock();
        }
        return isAvail;
    }

}

