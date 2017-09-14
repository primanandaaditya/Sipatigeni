package com.trekkon.patigeni.controller.direction;

import android.app.ProgressDialog;
import android.content.Context;

import com.trekkon.patigeni.constants.BaseCode;
import com.trekkon.patigeni.constants.URL;
import com.trekkon.patigeni.model.GoogleMapDirections.GoogleMapDirectionModel;
import com.trekkon.patigeni.retrofit.direction.GoogleDirectionInterface;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Trekkonz on 8/16/2017.
 */

public class GoogleDirectionController implements GoogleDirectionRequest {

    private GoogleDirectionResult googleDirectionResult;
    Context _context;
    GoogleDirectionInterface googleDirectionInterface;
    ProgressDialog progressDialog;

    public GoogleDirectionController(GoogleDirectionResult googleDirectionResult, Context context){
        this.googleDirectionResult=googleDirectionResult;
        this._context = context;
    }


    @Override
    public void getJSON(HashMap<String, String> params) {

        progressDialog = new ProgressDialog(_context);
        progressDialog.setMessage(BaseCode.PROGRESS_TITLE);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL.GOOGLE_MAP_DIRECTION_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GoogleDirectionInterface googleDirectionInterface = retrofit.create(GoogleDirectionInterface.class);
        Call<GoogleMapDirectionModel> googleMapDirectionModelCall = googleDirectionInterface.getJSON(params);
        googleMapDirectionModelCall.enqueue(new Callback<GoogleMapDirectionModel>() {
            @Override
            public void onResponse(Call<GoogleMapDirectionModel> call, Response<GoogleMapDirectionModel> response) {
                progressDialog.dismiss();

                if (response.body().getStatus().equals(BaseCode.STATUS_OK)){
                    googleDirectionResult.onSuccess(response.body());
                }else{
                    googleDirectionResult.onError();
                }


            }

            @Override
            public void onFailure(Call<GoogleMapDirectionModel> call, Throwable t) {
                progressDialog.dismiss();
                googleDirectionResult.onError();
            }
        });


    }
}
