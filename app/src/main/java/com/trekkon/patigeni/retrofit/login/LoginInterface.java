package com.trekkon.patigeni.retrofit.login;


import com.trekkon.patigeni.model.LoginModel;
import com.trekkon.patigeni.constants.URL;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginInterface {

    @FormUrlEncoded
    @POST(URL.LOGIN_URL)
    rx.Observable<LoginModel> login(
            @Field("email") String email,
            @Field("password") String password,
            @Field("token_id") String token);
}
