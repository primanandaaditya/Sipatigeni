package com.trekkon.patigeni.services;

import com.trekkon.patigeni.utils.AppConfig;
import com.trekkon.patigeni.utils.HttpUtil;

/**
 * Created by iwan on 7/27/17
 */
public abstract class BaseService {

    protected final HttpUtil httpUtil;
    private String baseURL;

    public BaseService() {
        httpUtil = new HttpUtil();
        baseURL = AppConfig.instance.getBaseUrl();
    }

    protected String getBaseUrl() {
        return baseURL;
    }

    protected String getServiceUrl(String servicePath) {
        return String.format("%s%s",getBaseUrl(), servicePath);
    }
}
