package com.trekkon.patigeni.retrofit.direction;

import com.trekkon.patigeni.constants.URL;
import com.trekkon.patigeni.retrofit.RetrofitClient;

/**
 * Created by Trekkonz on 8/16/2017.
 */

public class GoogleDirectionUtils {

    private GoogleDirectionUtils() {}

    public static GoogleDirectionInterface getGoogleDirectionInterface() {

        return RetrofitClient.getClient(URL.GOOGLE_MAP_DIRECTION_URL).create(GoogleDirectionInterface.class);
    }
}
