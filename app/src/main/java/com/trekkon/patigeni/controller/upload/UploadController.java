package com.trekkon.patigeni.controller.upload;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;


import com.trekkon.patigeni.controller.BaseResult;
import com.trekkon.patigeni.model.UploadModel;
import com.trekkon.patigeni.retrofit.upload.UploadInterface;
import com.trekkon.patigeni.retrofit.upload.UploadUtils;
import com.trekkon.patigeni.constants.BaseCode;
import com.trekkon.patigeni.utils.CommonUtils;

import java.util.Map;

import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Primananda on 7/8/2017.
 */

public class UploadController implements UploadRequest {

    private BaseResult baseResult;
    private UploadInterface uploadInterface;
    Context _context;
    //ProgressDialog progressDialog;
    CommonUtils util;

    public UploadController(BaseResult baseResult, Context context){
        this.baseResult=baseResult;
        this._context = context;
    }

    @Override
    public void Upload(String authorization, Map<String, RequestBody> map) {
        util = new CommonUtils(_context);
        //progressDialog = new ProgressDialog(_context);
        //progressDialog.setMessage(BaseCode.PROGRESS_TITLE);
        //progressDialog.setIndeterminate(false);
        //progressDialog.setCancelable(true);
        //progressDialog.show();

        if (TextUtils.isEmpty(authorization)){
            baseResult.showValidationError();
        }else{
            uploadInterface = UploadUtils.getUploadInterface();
            uploadInterface.upload(authorization,map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<UploadModel>() {
                        @Override
                        public void onCompleted() {
                            //progressDialog.dismiss();
                        }

                        @Override
                        public void onError(Throwable e) {
                            baseResult.onError(e.getMessage().toString());
                            //progressDialog.dismiss();
                            CommonUtils.ToastUtil(_context, e.getMessage().toString());
                        }

                        @Override
                        public void onNext(UploadModel uploadModel) {
                            //progressDialog.dismiss();
                            if (uploadModel.getSuccess().equals(BaseCode.BOOL_UPLOAD_BERHASIL)){
                                baseResult.onSuccess();
                            }else{
                                baseResult.onError(uploadModel.getMessage().toString());
                            }
                        }
                    });
        }

    }
}