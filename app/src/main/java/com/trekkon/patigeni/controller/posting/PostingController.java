package com.trekkon.patigeni.controller.posting;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;


import com.trekkon.patigeni.controller.BaseResult;
import com.trekkon.patigeni.model.PostingModel;
import com.trekkon.patigeni.retrofit.posting.PostingInterface;
import com.trekkon.patigeni.retrofit.posting.PostingUtils;
import com.trekkon.patigeni.constants.BaseCode;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Primananda on 7/19/2017.
 */


public class PostingController implements PostingRequest {

    private BaseResult baseResult;
    private PostingInterface postingInterface;
    Context _context;
    //ProgressDialog progressDialog;

    public PostingController(BaseResult baseResult, Context context){
        this.baseResult = baseResult;
        this._context = context;
    }

    @Override
    public void posting(String status, String iduser, String idnotif, String photo, Double lat, Double klong) {


        //progressDialog = new ProgressDialog(_context);
        //progressDialog.setMessage(BaseCode.PROGRESS_TITLE);
        //progressDialog.setIndeterminate(false);
        //progressDialog.setCancelable(true);
        //progressDialog.show();

        if ( TextUtils.isEmpty(iduser) ||  TextUtils.isEmpty(idnotif) || TextUtils.isEmpty(photo)){
            //progressDialog.dismiss();
            baseResult.showValidationError();
        }else{

            postingInterface = PostingUtils.getPostingInterface();

            postingInterface.posting(status,iduser,idnotif,photo,lat,klong).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<PostingModel>() {
                        @Override
                        public void onCompleted() {
                            //progressDialog.dismiss();
                        }

                        @Override
                        public void onError(Throwable e) {
                            //progressDialog.dismiss();
                            Toast.makeText(_context,e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(PostingModel postingModel) {

                            if (postingModel.getError() != null){
                                if ( !postingModel.getError().equals(BaseCode.BOOL_LOGIN_RESULT)){
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
