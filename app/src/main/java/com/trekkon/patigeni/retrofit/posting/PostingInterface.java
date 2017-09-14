package com.trekkon.patigeni.retrofit.posting;

import com.trekkon.patigeni.model.PostingModel;
import com.trekkon.patigeni.constants.URL;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PostingInterface {

    @FormUrlEncoded
    @POST(URL.POSTING_URL)
    rx.Observable<PostingModel> posting(
            @Field("status") String status,
            @Field("iduser") String iduser,
            @Field("idnotif") String idnotif,
            @Field("photo") String photo,
            @Field("lat") Double lat,
            @Field("long") Double klong);
}
