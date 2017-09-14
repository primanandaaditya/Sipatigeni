package com.trekkon.patigeni.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.gson.Gson;
import com.trekkon.patigeni.R;
import com.trekkon.patigeni.model.Enums;
import com.trekkon.patigeni.model.User;


import java.io.File;

import static com.trekkon.patigeni.PatigeniApp.getContext;


/**
 * Created by iwan on 7/27/17
 */
public class AppConfig {

    public final static AppConfig instance = new AppConfig();
    private static SharedPreferences sharedPreferences;

    private AppConfig() {
        Context context = getContext();
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public String getBaseUrl() {
        return getContext().getString(R.string.server_url) + getContext().getString(R.string.context_path);
    }

    public String getDeviceId(){
        return Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public User getCurrentUser(){
        String userString = sharedPreferences.getString(Enums.SP.USER.name(), null);
        if (userString == null) return null;
        else{
            User user = new Gson().fromJson(userString, User.class);
            return user;
        }
    }

    public void updateCurrentUser(User user){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Enums.SP.USER.name(), new Gson().toJson(user));
        editor.commit();
    }

    public String getAppName(){
        try {
            PackageInfo pInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "unknown";
        }
    }

    public int getAppCode(){
        try {
            PackageInfo pInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            return pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }




}
