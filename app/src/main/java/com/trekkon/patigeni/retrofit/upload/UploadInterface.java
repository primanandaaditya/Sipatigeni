package com.trekkon.patigeni.retrofit.upload;

import com.trekkon.patigeni.model.UploadModel;
import com.trekkon.patigeni.constants.URL;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

/**
 * Created by Primananda on 7/5/2017.
 */

public interface UploadInterface {

    @Multipart
    @POST(URL.UPLOAD_URL)
    rx.Observable<UploadModel> upload(
            @Header("Authorization") String Authorization,
            @PartMap Map<String, RequestBody> map
    );

}
