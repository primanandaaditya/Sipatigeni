package com.trekkon.patigeni.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;
import com.trekkon.patigeni.R;
import com.trekkon.patigeni.controller.BaseResult;
import com.trekkon.patigeni.services.ConnectionService;
import com.trekkon.patigeni.utils.CommonUtils;
import com.trekkon.patigeni.utils.SessionManagement;
import java.io.IOException;

public class SplashScreen extends SuperActivity implements BaseResult, ConnectionService.ConnectionServiceListener {
    private static final int MY_REQUEST_CODE = 5;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 4000;
    SessionManagement session;
    CommonUtils util;
    private  String sdcardPath , dbName, compressName;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        util = new CommonUtils(SplashScreen.this);
        cekDatabase();

        runtime_permissions();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                extras = getIntent().getExtras();
                session = new SessionManagement(getApplicationContext());
                session.checkLogin(extras);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    BroadcastReceiver tokenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String token = intent.getStringExtra("token");
            if(token != null) {
                //send token to your server or what you want to do
                // Get token
//                String token = FirebaseInstanceId.getInstance().getToken();
                Log.i("token id2", token);
                Toast.makeText(SplashScreen.this, token, Toast.LENGTH_SHORT).show();
                getApp().getPatigeniPreferences().setTokenId(SplashScreen.this,token);
//                PatigeniPreferences.setTokenId(token);
//                PatigeniApp.getInstance().setTokenID(token);
//                editor.putString(FCM_REGISTRATION_TOKEN, token);
//                editor.commit();
            }
        }
    };

    public void cekDatabase(){
        dbName = getResources().getString(R.string.database_name);
        compressName = getResources().getString(R.string.database_compress_name);
        sdcardPath = "/data/data/" + util.getPackageName() + "/databases/";
//        String sdcardPath = Environment.getExternalStorageDirectory().getPath()+ "/patigeni/database/";
        String internalMemoryPath = Environment.getDataDirectory().getPath();
        Log.i("sdcardPath", sdcardPath);
        util = new CommonUtils(SplashScreen.this);
        Log.i("dbName", dbName);

        try {
            if (util.createFolder(sdcardPath, dbName)){
                util.copyDatabaseFromAssets(SplashScreen.this, sdcardPath, dbName, compressName);
            } else {
                Log.i("ststuas dataasbe", "database udah ada");
            }

        } catch (IOException e) {
            CommonUtils.ToastUtil(SplashScreen.this, "Gagal Copy Database : " + e.toString());
        } catch (Exception e) {
            CommonUtils.ToastUtil(SplashScreen.this, "Gagal Copy Database : " + e.toString());
        }
    }

    @Override
    public void showValidationError() {

    }

    @Override
    public void onSuccess() {
        Toast.makeText(SplashScreen.this, "Berhasil", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String errMessage) {

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

    }

    private boolean checkConnection() {
        String messageToShow;
        boolean isConnected = ConnectionService.isConnected();
        Log.i("Connection", String.valueOf(isConnected));
        if (isConnected){
//            messageToShow = "Terhubung Internet";
        }else {
            messageToShow = "Tidak ada Internet";
        }
//        showToast(this, messageToShow);
//        showDialog(SplashScreen.this, "Tidak Bisa Lanjut", "Tidak Ada Koneksi Internet", "Ok", "", 1);
        return isConnected;
    }

    private boolean runtime_permissions() {
        if(Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, android.Manifest.permission.
                ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest
                .permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},100);
            return  true;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_REQUEST_CODE);
            }
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==100){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){

            }else{
                Toast.makeText(SplashScreen.this,"Permission not granted",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
