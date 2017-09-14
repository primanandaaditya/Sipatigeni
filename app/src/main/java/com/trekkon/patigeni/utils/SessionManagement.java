package com.trekkon.patigeni.utils;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import com.google.gson.Gson;
import com.trekkon.patigeni.activities.DaftarTitikActivity;
import com.trekkon.patigeni.activities.MainActivity;
import com.trekkon.patigeni.activities.LoginActivity;
import com.trekkon.patigeni.model.TitikApiModel;

import java.util.HashMap;


public class SessionManagement {


    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;


    private static final String PREF_NAME = "Sipatigeni";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String JENIS_LOGIN="jenis_login";//server=0,facebook=1,google=2

    public static final String KEY_UNIQUE_ID = "uid";
    public static final String KEY_NAME = "name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_GPS = "GPS";
    public static final String KEY_WRITE_EXTERNAL_STORAGE="WES";
    public static final String FCM_REGISTRATION_TOKEN = "uid";
    public static final String HOTSPOT_ID = "titik_api";
    public static final String KEY_TITIK_API = "list_titik";

    public TitikApiModel titikApiModel;


    public SessionManagement(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void createLoginSession(String jenis_login, String unique_id, String name, String email, String password){


        editor.putBoolean(IS_LOGIN, true);
        //editor.putString(KEY_NAME, name);
        //editor.putString(KEY_PASSWORD, password);
        editor.putString(JENIS_LOGIN,jenis_login);
        editor.putString(KEY_UNIQUE_ID,unique_id);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, password);

        editor.commit();
    }

    public void SaveTitikApi(TitikApiModel titikApiModel){

        Gson gson = new Gson();
        String json = gson.toJson(titikApiModel);
        editor.putString(KEY_TITIK_API,json);
        editor.commit();
    }

    public TitikApiModel getTitikApi(){
        Gson gson = new Gson();
        String json = pref.getString(KEY_TITIK_API,"");
        titikApiModel = gson.fromJson(json, TitikApiModel.class);
        return titikApiModel;
    }



    public void GPS_On(){
        editor.putString(KEY_GPS, "1");
    }

    public void GPS_Off(){
        editor.putString(KEY_GPS,"0");
    }

    public void WES_ON(){
        editor.putBoolean(KEY_WRITE_EXTERNAL_STORAGE,true);
    }

    public void WES_OFF(){
        editor.putBoolean(KEY_WRITE_EXTERNAL_STORAGE,false);
    }


    public boolean Get_WES(){
        return pref.getBoolean(KEY_WRITE_EXTERNAL_STORAGE, false);
    }


    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(JENIS_LOGIN, pref.getString(JENIS_LOGIN,null));
        user.put(KEY_UNIQUE_ID, pref.getString(KEY_UNIQUE_ID,null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD,null));
        return user;
    }


    public void checkLogin(Bundle bundle){
        // Check login status
        if(!this.isLoggedIn()){

            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            _context.startActivity(i);

        }else{
            Intent i = new Intent(_context, MainActivity.class);
            if (bundle != null){
                    String doAction = bundle.getString("action");
                    if (doAction != null) {
						i.putExtra("action", doAction);
                        i.putExtra("latitude", bundle.getString("latitude"));
                        i.putExtra("longitude", bundle.getString("longitude"));
                        i.putExtra("timestamp", bundle.getString("timestamp"));
                        i.putExtra("tingkatKepercayaan", bundle.getString("tingkatKepercayaan"));
                    }
            }

            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }

    }

    private void getIntentData() {

    }

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);

    }

    public String getJenisLogin(){
        return pref.getString(JENIS_LOGIN,null);
    }

    public String getGPS()
    {
        return pref.getString(KEY_GPS,null);
    }

    public String getUniqueId(){
        return pref.getString(KEY_UNIQUE_ID,null);
    }

    public String getPassword(){

        return pref.getString(KEY_PASSWORD,null);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void setCurrentHotspot(int hotspotId){
        editor.putInt(HOTSPOT_ID, hotspotId);
    }

    public int getCurrentHotspot(){
        return pref.getInt(HOTSPOT_ID, 0);
    }

    public void setTitikApiModel (TitikApiModel titikApiModel){
        this.titikApiModel = titikApiModel;
    }

    public TitikApiModel getTitikApiModel(){
        return this.titikApiModel;
    }
}