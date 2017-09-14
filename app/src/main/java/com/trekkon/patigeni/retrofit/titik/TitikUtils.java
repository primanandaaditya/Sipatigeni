package com.trekkon.patigeni.retrofit.titik;


import com.trekkon.patigeni.retrofit.RetrofitClient;
import com.trekkon.patigeni.constants.URL;

/**
 * Created by Primananda on 7/22/2017.
 */

public class TitikUtils {

    private TitikUtils() {}

    public static TitikInterface getTitikInterface() {
        return RetrofitClient.getClient(URL.BASE_URL).create(TitikInterface.class);
    }

}
