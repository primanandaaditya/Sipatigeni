package com.trekkon.patigeni.retrofit.updateuser;

import com.trekkon.patigeni.constants.URL;
import com.trekkon.patigeni.model.LoginModel;
import com.trekkon.patigeni.model.UpdateUserModel;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Trekkonz on 08/09/2017.
 */

public interface UpdateUserInterface {

    @FormUrlEncoded
    @POST(URL.UPDATE_USER_URL)
    rx.Observable<UpdateUserModel> updateUser(
            @Field("iduser") String iduser,
            @Field("name") String name,
            @Field("password") String password,
            @Field("email") String email
            );
}
