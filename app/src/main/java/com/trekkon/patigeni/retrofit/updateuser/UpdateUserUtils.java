package com.trekkon.patigeni.retrofit.updateuser;

import com.trekkon.patigeni.constants.URL;
import com.trekkon.patigeni.retrofit.RetrofitClient;
import com.trekkon.patigeni.retrofit.login.LoginInterface;

/**
 * Created by Trekkonz on 08/09/2017.
 */

public class UpdateUserUtils {

    private UpdateUserUtils() {}

    public static final String BASE_URL = URL.BASE_URL;

    public static UpdateUserInterface getUpdateUserInterface() {

        return RetrofitClient.getClient(BASE_URL).create(UpdateUserInterface.class);
    }
}
