package com.trekkon.patigeni.retrofit.upload;


import com.trekkon.patigeni.retrofit.RetrofitClient;
import com.trekkon.patigeni.constants.URL;

/**
 * Created by Primananda on 7/5/2017.
 */

public class UploadUtils {

    private UploadUtils(){}

    public static UploadInterface getUploadInterface(){
        return RetrofitClient.getClient(URL.BASE_URL).create(UploadInterface.class);
    }
}
