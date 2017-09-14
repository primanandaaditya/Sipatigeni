package com.trekkon.patigeni.controller.posting;

/**
 * Created by Primananda on 7/19/2017.
 */

public interface PostingRequest {

    void posting(String status, String iduser, String idnotif, String photo, Double lat, Double klong);
}

