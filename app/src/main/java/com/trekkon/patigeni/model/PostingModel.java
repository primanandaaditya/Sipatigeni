package com.trekkon.patigeni.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostingModel {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("info")
    @Expose
    private String info;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}