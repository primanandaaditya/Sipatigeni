package com.trekkon.patigeni.retrofit.updatestatus;

import com.trekkon.patigeni.constants.URL;
import com.trekkon.patigeni.retrofit.RetrofitClient;

/**
 * Created by Trekkonz on 8/10/2017.
 */

public class UpdateStatusUtils {

    private UpdateStatusUtils() {}

    public static UpdateStatusInterface getupdateStatusInterface() {

        return RetrofitClient.getClient(URL.BASE_URL).create(UpdateStatusInterface.class);
    }
}
