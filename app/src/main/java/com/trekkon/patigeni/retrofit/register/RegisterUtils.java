package com.trekkon.patigeni.retrofit.register;


import com.trekkon.patigeni.retrofit.RetrofitClient;
import com.trekkon.patigeni.constants.URL;

/**
 * Created by Primananda on 7/12/2017.
 */

public class RegisterUtils {

    private RegisterUtils() {}

    public static final String BASE_URL = URL.BASE_URL;

    public static RegisterInterface getRegisterInterface() {

        return RetrofitClient.getClient(BASE_URL).create(RegisterInterface.class);
    }
}
