package com.trekkon.patigeni.controller.updateuser;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;

import com.trekkon.patigeni.constants.BaseCode;
import com.trekkon.patigeni.controller.BaseResult;
import com.trekkon.patigeni.model.ForgotPasswordModel;
import com.trekkon.patigeni.model.UpdateUserModel;
import com.trekkon.patigeni.retrofit.ganti_password.GantiPasswordUtils;
import com.trekkon.patigeni.retrofit.login.LoginInterface;
import com.trekkon.patigeni.retrofit.updateuser.UpdateUserInterface;
import com.trekkon.patigeni.retrofit.updateuser.UpdateUserUtils;
import com.trekkon.patigeni.utils.SessionManagement;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Trekkonz on 08/09/2017.
 */

public class UpdateUserController implements UpdateUserRequest {

    private BaseResult baseResult;
    private UpdateUserInterface updateUserInterface;
    Context _context;
    ProgressDialog progressDialog;


    public UpdateUserController(BaseResult baseResult, Context context){
        this.baseResult = baseResult;
        this._context = context;
    }

    @Override
    public void updateUser(String iduser, String name, String password, String email) {
        progressDialog = new ProgressDialog(_context);
        progressDialog.setMessage(BaseCode.PROGRESS_TITLE);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();

        if (TextUtils.isEmpty(iduser) || TextUtils.isEmpty(name) || TextUtils.isEmpty(password) || TextUtils.isEmpty(email) ){
            progressDialog.dismiss();
            baseResult.showValidationError();
        }else{

            updateUserInterface = UpdateUserUtils.getUpdateUserInterface();
            updateUserInterface.updateUser(iduser, name, password, email).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<UpdateUserModel>() {
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
                        public void onNext(UpdateUserModel updateUserModel) {

                            if (updateUserModel.getSuccess() != null){
                                if ( updateUserModel.getSuccess().equals(true)){
                                    baseResult.onSuccess();
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
