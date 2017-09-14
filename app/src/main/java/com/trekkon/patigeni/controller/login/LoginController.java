package com.trekkon.patigeni.controller.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.trekkon.patigeni.PatigeniApp;
import com.trekkon.patigeni.controller.BaseResult;
import com.trekkon.patigeni.helper.BaseFunction;
import com.trekkon.patigeni.model.User;
import com.trekkon.patigeni.utils.SessionManagement;
import com.trekkon.patigeni.model.LoginModel;
import com.trekkon.patigeni.retrofit.login.LoginInterface;
import com.trekkon.patigeni.retrofit.login.LoginUtils;
import com.trekkon.patigeni.constants.BaseCode;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Primananda on 7/6/2017.
 */
public class LoginController implements LoginRequest {

    private BaseResult baseResult;
    private LoginInterface loginInterface;
    Context _context;
    ProgressDialog progressDialog;
    SessionManagement sessionManagement;
    String userId;


    public LoginController(BaseResult baseResult, Context context){
        this.baseResult = baseResult;
        this._context = context;
    }

    public PatigeniApp getApp() {
        return PatigeniApp.getInstance();
    }


    @Override
    public void login(String email, final String password, String tokenId) {

        sessionManagement = new SessionManagement(_context);

        progressDialog = new ProgressDialog(_context);
        progressDialog.setMessage(BaseCode.PROGRESS_TITLE);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            progressDialog.dismiss();
            baseResult.showValidationError();
        }else{

            loginInterface = LoginUtils.getLoginInterface();
            loginInterface.login(email,password, tokenId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<LoginModel>() {
                        @Override
                        public void onCompleted() {
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onError(Throwable e) {
                            progressDialog.dismiss();
                            handleLoginError(e);
//                            Toast.makeText(_context,e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(LoginModel loginModel) {
                            if (loginModel.getError() != null){

                                User user = new User();
                                userId = loginModel.getUid();
                                if ( !loginModel.getError().equals(BaseCode.BOOL_LOGIN_RESULT)){
                                    baseResult.onSuccess();
                                    sessionManagement.createLoginSession("0", loginModel.getUid(), loginModel.getUser().getName(), loginModel.getUser().getEmail(), password);

                                    user = loginModel.getUser();
                                    user.setUserId(Integer.parseInt(loginModel.getUid()));
                                    user.setPassword(password);
                                    user.setFcmToken(getApp().getPatigeniPreferences().getTokenId(_context));
                                    Log.i("test payload", "company : " + user.getCompanyName() +
                                            "password : " + password +
                                            " email: " + user.getEmail() +
                                            " Name : " + user.getName() +
                                            " id : " + userId +
                                            " parameter : " + user.getParameter() +
                                            " FirebSe Token : " + getApp().getPatigeniPreferences().getTokenId(_context));
                                    getApp().getPatigeniPreferences().saveUser(user, _context);
//                                    PatigeniApp.getInstance().setDbUser(user);
                                    Log.i("test payload", loginModel.getUid() + ", " + loginModel.getUser().getName().toString() + ", "
                                            + loginModel.getUser().getEmail().toString() + ", " + String.valueOf(loginModel.getUid()) + ", " + loginModel.getUser().getCompanyName());
                                }else{
                                    Log.i("Login result", loginModel.toString() + " " + loginModel.getErrorMessage());
                                    baseResult.onError(loginModel.getErrorMessage().toString());
                                }

                            }else{
                                Log.i("error", loginModel.getError().toString() + "" + loginModel.getErrorMessage());
                                baseResult.onError(loginModel.getErrorMessage());
                            }

                        }
                    });


        }


    }

    private void handleLoginError(final Throwable throwable) {
        Log.i("hndle login error", throwable.toString());
        getApp().showToast(_context, throwable.toString());
        progressDialog.dismiss();

    }


}
