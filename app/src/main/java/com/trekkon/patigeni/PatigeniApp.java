package com.trekkon.patigeni;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.trekkon.patigeni.model.User;
import com.trekkon.patigeni.services.ConnectionService;
import com.trekkon.patigeni.utils.CommonUtils;
import com.trekkon.patigeni.utils.PatigeniPreferences;
import io.fabric.sdk.android.Fabric;
//import android.support.multidex.MultiDex;

/**
 * Created by Iwan on 27-Jul-17.
 */

public class PatigeniApp extends Application {
    private static Context mContext;
    private static PatigeniApp appInstance;
    private User dbUser = new User();
    private static String tokenID;
    private PatigeniPreferences patigeniPref;


    public static synchronized PatigeniApp getInstance(){
        return appInstance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        appInstance = this;
        initializeUserPreferences();

    }

   /* @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);
    }*/

    public static Context getContext() {
        return mContext;
    }

    private void initializeUserPreferences() {
        patigeniPref = new PatigeniPreferences();
    }

    public void setConnectionListener(ConnectionService.ConnectionServiceListener listener) {
        ConnectionService.connectionServiceListener  =listener;
    }

    public User getDbUser() {
        return dbUser;
    }

    public void setDbUser(User dbUser) {
        this.dbUser = dbUser;
    }

    public String getTokenID() {
        return tokenID;
    }

    public void setTokenID(String tokenID) {
        PatigeniApp.tokenID = tokenID;
    }

    public PatigeniPreferences getPatigeniPreferences() {
        return patigeniPref;
    }

    public void showToast(Context c, String message){
        CommonUtils.ToastUtil(c, message);
    }
}
