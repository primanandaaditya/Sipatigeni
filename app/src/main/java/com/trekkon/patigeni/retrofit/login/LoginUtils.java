package com.trekkon.patigeni.retrofit.login;


import com.trekkon.patigeni.retrofit.RetrofitClient;
import com.trekkon.patigeni.constants.URL;

/**
 * Created by Primananda on 7/5/2017.
 */

public class LoginUtils {

    private LoginUtils() {}

    public static final String BASE_URL = URL.BASE_URL;

    public static LoginInterface getLoginInterface() {

        return RetrofitClient.getClient(BASE_URL).create(LoginInterface.class);
    }
}

