package com.trekkon.patigeni.controller.titik;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.Toast;


import com.trekkon.patigeni.PatigeniApp;
import com.trekkon.patigeni.helper.MessageHelper;
import com.trekkon.patigeni.model.TitikApiModel;
import com.trekkon.patigeni.retrofit.titik.TitikInterface;
import com.trekkon.patigeni.retrofit.titik.TitikUtils;
import com.trekkon.patigeni.constants.BaseCode;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Primananda on 7/22/2017.
 */

public class TitikController implements TitikRequest {

    private TitikResult titikResult;
    private TitikInterface titikInterface;
    Context _context;
    ProgressDialog progressDialog;

    public TitikController(TitikResult titikResult, Context context){
        this.titikResult = titikResult;
        this._context = context;
    }

    public TitikController( Context context){
        this._context = context;
    }




    @Override
    public void Titik(String userid) {

        progressDialog = new ProgressDialog(_context);
        progressDialog.setMessage(BaseCode.PROGRESS_TITLE);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();

        if (TextUtils.isEmpty(userid) ){
            progressDialog.dismiss();
            titikResult.showValidationError();
        }else{

            titikInterface = TitikUtils.getTitikInterface();
            titikInterface.titik(userid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<TitikApiModel>() {
                        @Override
                        public void onCompleted() {
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onError(Throwable e) {
                            progressDialog.dismiss();
                            MessageHelper.showOnThread(_context, e.getMessage());
                        }

                        @Override
                        public void onNext(TitikApiModel titikApiModel) {

                            if (titikApiModel.getSuccess() != null){

                                if (titikApiModel.getSuccess().equals(1)){
                                    titikResult.onSuccess(titikApiModel);

                                }else{
                                    titikResult.onError("error");
                                }
                            }else{
                                titikResult.onError("error");
                            }

                        }
                    });


        }

    }


}
