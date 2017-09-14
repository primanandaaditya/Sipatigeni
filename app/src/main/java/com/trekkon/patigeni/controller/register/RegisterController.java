package com.trekkon.patigeni.controller.register;

/**
 * Created by Primananda on 7/12/2017.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;


import com.trekkon.patigeni.controller.BaseResult;
import com.trekkon.patigeni.model.RegisterModel;
import com.trekkon.patigeni.retrofit.register.RegisterInterface;
import com.trekkon.patigeni.retrofit.register.RegisterUtils;
import com.trekkon.patigeni.constants.BaseCode;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Primananda on 7/6/2017.
 */
public class RegisterController implements RegisterRequest {

    private BaseResult baseResult;
    private RegisterInterface registerInterface;
    Context _context;
    ProgressDialog progressDialog;



    public RegisterController(BaseResult baseResult, Context context){
        this.baseResult = baseResult;
        this._context = context;
    }


    @Override
    public void register(String name,String email, String password) {

        progressDialog = new ProgressDialog(_context);
        progressDialog.setMessage(BaseCode.PROGRESS_TITLE);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();

        if ( TextUtils.isEmpty(name) ||  TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            progressDialog.dismiss();
            baseResult.showValidationError();
        }else{

            registerInterface = RegisterUtils.getRegisterInterface();

            registerInterface.register(name,email,password).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<RegisterModel>() {
                        @Override
                        public void onCompleted() {
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onError(Throwable e) {
                            progressDialog.dismiss();
                            Toast.makeText(_context,e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(RegisterModel registerModel) {

                            if (registerModel.getError() != null){
                                if ( !registerModel.getError().equals(BaseCode.BOOL_LOGIN_RESULT)){
                                    baseResult.onSuccess();

                                }else{
                                    baseResult.onError(registerModel.getMessage());
                                }
                            }else{
                                baseResult.onError(registerModel.getMessage());
                            }

                        }
                    });


        }


    }
}
