package com.trekkon.patigeni.controller.upload;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * Created by Primananda on 7/5/2017.
 */

public interface UploadRequest {

    void Upload(String authorization, Map<String, RequestBody> map);

}
