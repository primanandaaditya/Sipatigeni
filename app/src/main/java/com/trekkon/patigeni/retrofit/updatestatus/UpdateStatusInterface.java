package com.trekkon.patigeni.retrofit.updatestatus;


import com.trekkon.patigeni.constants.URL;
import com.trekkon.patigeni.model.UpdateStatusModel;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


/**
 * Created by Primananda on 7/28/2017.
 */

public interface UpdateStatusInterface {

    @FormUrlEncoded
    @POST(URL.UPDATE_STATUS_URL)
    rx.Observable<UpdateStatusModel> update(
            @Field("iduser") String email,
            @Field("idnotif") String password);

}
