package com.trekkon.patigeni.retrofit.ganti_password;


import com.trekkon.patigeni.constants.URL;
import com.trekkon.patigeni.retrofit.RetrofitClient;

/**
 * Created by Primananda on 8/4/2017.
 */

public class GantiPasswordUtils {

    private GantiPasswordUtils() {}

    public static GantiPasswordInterface getGantiPasswordInterface() {

        return RetrofitClient.getClient(URL.GANTI_PASSWORD_URL).create(GantiPasswordInterface.class);
    }
}
