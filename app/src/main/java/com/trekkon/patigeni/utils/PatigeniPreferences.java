package com.trekkon.patigeni.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.trekkon.patigeni.PatigeniApp;
import com.trekkon.patigeni.model.User;

/**
 * Created by Iwan on 07-Aug-17.
 */

public class PatigeniPreferences {
    private static final String PREFS = "PatigeniPreferences";
    private static final String PATIGENI_USER_KEY = "PATIGENI_USER_KEY";
    public static final String FCM_REGISTRATION_TOKEN = "FCM_REGISTRATION_TOKEN";
    private static String tokenId = "";
    private  SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private static final int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "Sipatigeni";

    public void saveUser(User u, Context c) {
        Gson gson = new Gson();
        String json = gson.toJson(u);
        PreferenceManager.getDefaultSharedPreferences(c)
                .edit()
                .putString(PATIGENI_USER_KEY, json)
                .apply();
    }

    public User getUser(Context c) {
        Gson gson = new Gson();
        String json = PreferenceManager.getDefaultSharedPreferences(c)
//                .edit()
                .getString(PATIGENI_USER_KEY, null);
//                .apply();
        return gson.fromJson(json, User.class);

    }

    public String getTokenId(Context c) {
        String json = PreferenceManager.getDefaultSharedPreferences(c)
//                .edit()
                .getString(FCM_REGISTRATION_TOKEN, "");
        return json;
    }

    public void setTokenId(Context c, String tokenId) {
        /*pref = PreferenceManager.getDefaultSharedPreferences(c);
        editor = pref.edit();
//        PatigeniPreferences.tokenId = tokenId;
        Log.i("token id in pref", tokenId);
        editor.putString(FCM_REGISTRATION_TOKEN, tokenId);
        editor.apply();*/
        Log.i("token id in pref", tokenId);
        PreferenceManager.getDefaultSharedPreferences(c)
                .edit()
                .putString(FCM_REGISTRATION_TOKEN, tokenId)
                .apply();
    }
}
