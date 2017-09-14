package com.trekkon.patigeni.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import android.view.Window;

import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.trekkon.patigeni.PatigeniApp;
import com.trekkon.patigeni.dao.DbAdapter;
import com.trekkon.patigeni.fragment.TaskListFragment;
import com.trekkon.patigeni.helper.BaseFunction;
import com.trekkon.patigeni.utils.CommonUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Iwan on 07-Aug-17.
 */

public abstract class SuperActivity extends AppCompatActivity {
    SuperActivityToast superToast;
    private Bundle bundle;
    private String doAction = "";
    private Bundle extras;
    CommonUtils util;
    DbAdapter hotspotDao = null;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        util = new CommonUtils(SuperActivity.this);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart(){
        super.onStart();
        extras = getIntent().getExtras();
//        showToast(this, String.valueOf(isActivityRunning));

        if(extras != null){
            hotspotDao = new DbAdapter(SuperActivity.this);
            doAction = extras.getString("action");
            if (doAction!=null) {
                Log.i("action", doAction);
                if (doAction.contains("delete")) {
                    String userId = extras.getString("userId");
                    if (userId != BaseFunction.GetUserID(SuperActivity.this)) {
                        try {
                            hotspotDao.delete("Task", "cast(hotspot_id as varchar) = ? ", new String[]{extras.getString("taskId")});
                            showToast(SuperActivity.this, "Titik Api " + extras.getString("taskId") + " telah diambil oleh " + extras.getString("userName"));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {

                    showDialog(this, "notification", "latitude : " + extras.getString("latitude") +
                            "\nlongitude : " + extras.getString("longitude") +
                            "\ntimestamp : " + extras.getString("timestamp") +
                            "\ntingkat kepercayaan : " + extras.getString("tingkatKepercayaan"), "OK", "", 1);
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String currentDateandTime = df.format(Calendar.getInstance().getTime());
                    ContentValues cValue = new ContentValues();
                    cValue.put("contact_id", BaseFunction.GetUserID(SuperActivity.this));
                    cValue.put("hotspot_id", extras.getString("taskId"));
                    cValue.put("latitude", Double.parseDouble(extras.getString("latitude")));
                    cValue.put("longitude", Double.parseDouble(extras.getString("longitude")));
                    cValue.put("by_human", "Y");
                    cValue.put("status", "R");
                    cValue.put("tingkat_kepercayaan", extras.getString("tingkatKepercayaan"));
                    cValue.put("timestamp_original", extras.getString("timestamp"));
                    cValue.put("timestamp_received", currentDateandTime);
                    try {
                        hotspotDao.insert("Task", cValue);
                    } catch (Exception e) {
//                        MessageHelper.show(getActivity(), "Tidak Bisa Insert Database\n" + e.toString());
                        Log.i("error", e.toString());
                        e.printStackTrace();
                    }

                    TaskListFragment fragment = new TaskListFragment();
                    fragment.setRecyclerView(BaseFunction.GetUserID(SuperActivity.this), fragment.getRv());
                    setIntent(new Intent());
                }
            }
            /*
            * Insert this payload data into offline database
            *
            * */




        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getExtras() != null) {
            bundle = intent.getExtras();
            Log.i("bundle", String.valueOf(bundle));
        }
    }

    public PatigeniApp getApp() {
        return PatigeniApp.getInstance();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void showToast(Context context, String toastText){
        CommonUtils.ToastUtil(context, toastText);
    }

    public void showDialog(Context context, String textTitle, String textContent, String textPos, String textNeg, int dialogType){
        CommonUtils.showDialog(context, textTitle, textContent, textPos, textNeg, dialogType);
        /*if(dialogType==1){
            setIntent(null);
        }*/

    }
}
