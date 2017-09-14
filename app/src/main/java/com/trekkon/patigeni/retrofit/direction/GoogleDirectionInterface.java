package com.trekkon.patigeni.retrofit.direction;

import com.trekkon.patigeni.model.GoogleMapDirections.GoogleMapDirectionModel;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Trekkonz on 8/16/2017.
 */

public interface GoogleDirectionInterface {

    @GET("json")
    Call<GoogleMapDirectionModel> getJSON(
            @QueryMap HashMap<String, String> params
    );
}
