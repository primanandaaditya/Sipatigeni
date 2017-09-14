package com.trekkon.patigeni.controller.updatestatus;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.trekkon.patigeni.constants.BaseCode;
import com.trekkon.patigeni.controller.BaseResult;
import com.trekkon.patigeni.model.UpdateStatusModel;
import com.trekkon.patigeni.retrofit.updatestatus.UpdateStatusInterface;
import com.trekkon.patigeni.retrofit.updatestatus.UpdateStatusUtils;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Trekkonz on 8/10/2017.
 */

public class UpdateStatusController implements UpdateStatusRequest {

    private BaseResult baseResult;
    private UpdateStatusInterface updateStatusInterface;
    Context _context;
    ProgressDialog progressDialog;

    public UpdateStatusController(BaseResult baseResult, Context context){
        this.baseResult = baseResult;
        this._context = context;
    }


    @Override
    public void update(String iduser, String idnotif) {

        progressDialog = new ProgressDialog(_context);
        progressDialog.setMessage(BaseCode.PROGRESS_TITLE);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();

        if ( TextUtils.isEmpty(iduser) ||  TextUtils.isEmpty(idnotif)){
            progressDialog.dismiss();
            baseResult.showValidationError();
        }else{

            updateStatusInterface= UpdateStatusUtils.getupdateStatusInterface();

            updateStatusInterface.update(iduser,idnotif).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<UpdateStatusModel>() {
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
                        public void onNext(UpdateStatusModel updateStatusModel) {

                            if (updateStatusModel.getSuccess() != null){
                                if ( updateStatusModel.getSuccess().equals(true)){
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