package com.trekkon.patigeni.retrofit.posting;


import com.trekkon.patigeni.retrofit.RetrofitClient;
import com.trekkon.patigeni.constants.URL;

/**
 * Created by Primananda on 7/19/2017.
 */

public class PostingUtils {

    private PostingUtils() {}


    public static PostingInterface getPostingInterface() {

        return RetrofitClient.getClient(URL.BASE_URL).create(PostingInterface.class);
    }
}