package com.trekkon.patigeni.retrofit.ganti_password;


import com.trekkon.patigeni.constants.URL;
import com.trekkon.patigeni.model.ForgotPasswordModel;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


/**
 * Created by Primananda on 8/4/2017.
 */

public interface GantiPasswordInterface {
    @FormUrlEncoded
    @POST(URL.GANTI_PASSWORD_URL)
    rx.Observable<ForgotPasswordModel> gantiPassword(
            @Field("email") String email,
            @Field("password_baru") String password);
}
