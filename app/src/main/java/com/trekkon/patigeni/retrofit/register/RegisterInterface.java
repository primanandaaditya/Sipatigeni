package com.trekkon.patigeni.retrofit.register;


import com.trekkon.patigeni.model.RegisterModel;
import com.trekkon.patigeni.constants.URL;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Primananda on 7/12/2017.
 */

public interface RegisterInterface {
    @FormUrlEncoded
    @POST(URL.REGISTER_URL)
    rx.Observable<RegisterModel> register(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password);
}
