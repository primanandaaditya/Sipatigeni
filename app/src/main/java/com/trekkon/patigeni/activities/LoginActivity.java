package com.trekkon.patigeni.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.iid.FirebaseInstanceId;
import com.jakewharton.processphoenix.ProcessPhoenix;
import com.trekkon.patigeni.PatigeniApp;
import com.trekkon.patigeni.R;
import com.trekkon.patigeni.controller.BaseResult;
import com.trekkon.patigeni.controller.login.LoginController;
import com.trekkon.patigeni.model.LoginModel;
import com.trekkon.patigeni.retrofit.login.LoginInterface;
import com.trekkon.patigeni.retrofit.login.LoginUtils;
import com.trekkon.patigeni.services.ConnectionService;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginActivity extends SuperActivity implements BaseResult, ConnectionService.ConnectionServiceListener {

    LoginController loginController;
    LoginInterface loginInterface;
    Button btnLogin, btnMasuk;
    EditText etPassword, etEmail;
    TextView tvForget;
    GridView gridView;
    private SharedPreferences sp;
    String tokenId;
    boolean isConnected, isGetFCMId=false;
    Context mContext;
    TextView ttt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        mContext = this.getApplicationContext();


        loginController = new LoginController(this, LoginActivity.this);
        ttt=(TextView)findViewById(R.id.ttt);
        btnLogin=(Button)findViewById(R.id.btnLogin);

        etEmail=(EditText)findViewById(R.id.etEmail);
        etPassword=(EditText)findViewById(R.id.etPassword);
        tvForget = (TextView)findViewById(R.id.textViewForgetPassword);
        //tvForget.setText(Html.fromHtml("<u>Forget Password</u>"));


        tvForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(MainActivity.this,"Go to Webview or send API request", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, GantiPassword.class);
                startActivity(intent);

            }
        });
        checkConnection();

       /* btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnected){
                    showDialog(LoginActivity.this, "Tidak Bisa Lanjut", "Tidak Ada Koneksi Internet", "Ok", "", 1);
                    finish();
                } else {
                    if(!isGetFCMId){
                        LocalBroadcastManager.getInstance(LoginActivity.this).registerReceiver(tokenReceiver, new IntentFilter("tokenReceiver"));
                        // Get token
                        String token = FirebaseInstanceId.getInstance().getToken();
                        getApp().getPatigeniPreferences().setTokenId(LoginActivity.this,token);
                    } else {
                        tokenId = getApp().getPatigeniPreferences().getTokenId(LoginActivity.this);
                        Log.i("token id login", tokenId);
                    }

                    //sendPost(etEmail.getText().toString(), etPassword.getText().toString());
                    Log.i("email, password, token", etEmail.getText().toString() + ", " + etPassword.getText().toString() + ", " + tokenId);
                    loginController.login(etEmail.getText().toString(), etPassword.getText().toString(), tokenId );
                }

            }
        });*/


    }

    void Login(){
        loginController = new LoginController(this, LoginActivity.this);
        loginController.login(etEmail.getText().toString(),etPassword.getText().toString(),tokenId);

    }

    public void onSend(View v){
        if(!isConnected){
//            showDialog(LoginActivity.this, "Tidak Bisa Lanjut", "Tidak Ada Koneksi Internet", "Ok", "", 1);
            btnLogin.setEnabled(false);
            btnLogin.setBackgroundColor(Color.parseColor("#e0e0d1"));
        } else {
            if(isGetFCMId){
                LocalBroadcastManager.getInstance(LoginActivity.this).registerReceiver(tokenReceiver, new IntentFilter("tokenReceiver"));
                // Get token
                String token = FirebaseInstanceId.getInstance().getToken();
                getApp().getPatigeniPreferences().setTokenId(LoginActivity.this,token);
                tokenId = getApp().getPatigeniPreferences().getTokenId(LoginActivity.this);
            } else {
                tokenId = getApp().getPatigeniPreferences().getTokenId(LoginActivity.this);
                Log.i("token id login", tokenId);
            }

            //sendPost(etEmail.getText().toString(), etPassword.getText().toString());
            Log.i("email, password, token", etEmail.getText().toString() + ", " + etPassword.getText().toString() + ", " + tokenId);
            loginController.login(etEmail.getText().toString(), etPassword.getText().toString(), tokenId );
        }

    }
    private void checkConnection() {
        String messageToShow;
        isConnected = ConnectionService.isConnected();
        Log.i("Connection", String.valueOf(isConnected));
        if (isConnected){
            messageToShow = "Terhubung Internet";
            btnLogin.setEnabled(true);
            btnLogin.setBackgroundResource(R.drawable.login_button);
            FindID();
        }else {
            messageToShow = "Tidak ada Internet";
            btnLogin.setEnabled(false);
            btnLogin.setBackgroundColor(Color.parseColor("#e0e0d1"));
        }
        showToast(this, messageToShow);
    }

    BroadcastReceiver tokenReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(isConnected){
                isGetFCMId=true;
                String token = intent.getStringExtra("token");
                if(token != null) {
                    //send token to your server or what you want to do
                    // Get token
//                String token = FirebaseInstanceId.getInstance().getToken();

                    Log.i("token id2", token);
                    Toast.makeText(LoginActivity.this, token, Toast.LENGTH_SHORT).show();

                    getApp().getPatigeniPreferences().setTokenId(LoginActivity.this,token);


//                PatigeniPreferences.setTokenId(token);
//                PatigeniApp.getInstance().setTokenID(token);
//                editor.putString(FCM_REGISTRATION_TOKEN, token);
//                editor.commit();
                }
            }

        }
    };

    void FindID(){
        LocalBroadcastManager.getInstance(this).registerReceiver(tokenReceiver, new IntentFilter("tokenReceiver"));
        // Get token
       String tokenIdFirst = getApp().getPatigeniPreferences().getTokenId(this);
        Log.i("get Toke ID FISRT",tokenIdFirst );



        String token = FirebaseInstanceId.getInstance().getToken();
        if (tokenIdFirst==""||token == null){
            getApp().getPatigeniPreferences().setTokenId(this, "1");
            Intent i = new Intent(this, SplashScreen.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ProcessPhoenix.triggerRebirth(this, i);
        }
        Log.i("token id1", token);
        tokenId=token;

        btnMasuk=(Button)findViewById(R.id.btnMasuk);
        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });
        //Toast.makeText(LoginActivity.this, token, Toast.LENGTH_SHORT).show();
        //etEmail.setText(token);
//        PatigeniApp.getInstance().setTokenID(token);
        getApp().getPatigeniPreferences().setTokenId(LoginActivity.this,token);


    }

    public void sendPost(String email, String password, String tokenId) {
        loginInterface = LoginUtils.getLoginInterface();
        loginInterface.login(email,password, tokenId ).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(LoginModel loginModel) {
                        Toast.makeText(LoginActivity.this, loginModel.getUid().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void showValidationError() {
        Toast.makeText(LoginActivity.this, "Invalid email/password", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess() {
        //Toast.makeText(LoginActivity.this, "Login sukses", Toast.LENGTH_SHORT).show();
        sp = getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onError(String errorMessage) {
        showDialog(this, "Alert", errorMessage, "Tutup", "", 1);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Log.i("Connection", String.valueOf(isConnected));
        String messageToShow;
        if (isConnected){
            messageToShow = "Terhubung Internet";
            btnLogin.setEnabled(true);
            if(!isGetFCMId){
                FindID();
            }
        } else {
            messageToShow = "Tidak ada Internet";
            btnLogin.setEnabled(false);
            btnLogin.setBackgroundColor(Color.parseColor("#e0e0d1"));
        }
        showToast(this, messageToShow);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // register connection status listener
        PatigeniApp.getInstance().setConnectionListener(this);
    }
}
