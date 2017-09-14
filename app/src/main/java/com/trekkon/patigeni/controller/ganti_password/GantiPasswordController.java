package com.trekkon.patigeni.controller.ganti_password;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;


import com.trekkon.patigeni.constants.BaseCode;
import com.trekkon.patigeni.controller.BaseResult;
import com.trekkon.patigeni.model.ForgotPasswordModel;
import com.trekkon.patigeni.retrofit.ganti_password.GantiPasswordInterface;
import com.trekkon.patigeni.retrofit.ganti_password.GantiPasswordUtils;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Primananda on 8/4/2017.
 */

public class GantiPasswordController implements GantiPasswordRequest {


    private BaseResult baseResult;
    private GantiPasswordInterface gantiPasswordInterface;
    Context _context;
    ProgressDialog progressDialog;

    public GantiPasswordController(BaseResult baseResult, Context context){
        this.baseResult = baseResult;
        this._context = context;
    }


    @Override
    public void gantiPassword(String email, String password_baru) {


        progressDialog = new ProgressDialog(_context);
        progressDialog.setMessage(BaseCode.PROGRESS_TITLE);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password_baru)){
            progressDialog.dismiss();
            baseResult.showValidationError();
        }else{

            gantiPasswordInterface = GantiPasswordUtils.getGantiPasswordInterface();
            gantiPasswordInterface.gantiPassword(email,password_baru).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<ForgotPasswordModel>() {
                        @Override
                        public void onCompleted() {
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onError(Throwable e) {
                            progressDialog.dismiss();
                            //Toast.makeText(_context,"Tidak ditemukan titik api", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(ForgotPasswordModel forgotPasswordModel) {

                            if (forgotPasswordModel.getError() != null){
                                if ( !forgotPasswordModel.getError().equals(BaseCode.BOOL_LOGIN_RESULT)){
                                    //Toast.makeText(_context,loginModel.getUid(),Toast.LENGTH_SHORT).show();
                                    baseResult.onSuccess();
                                    //sessionManagement.createLoginSession("0", loginModel.getUid(), loginModel.getUser().getName(), loginModel.getUser().getEmail());
                                }else{
                                    baseResult.onError("Error");
                                }
                            }else{
                                baseResult.onError("Error");
                            }

                        }
                    });


        }


    }
}