package com.trekkon.patigeni.retrofit.titik;


import com.trekkon.patigeni.model.TitikApiModel;
import com.trekkon.patigeni.constants.URL;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Primananda on 7/22/2017.
 */

public interface TitikInterface {
    @FormUrlEncoded
    @POST(URL.TITIK_URL)
    rx.Observable<TitikApiModel> titik(

            @Field("iduser") String iduser);
}
