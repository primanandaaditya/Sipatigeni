package com.trekkon.patigeni.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TitikApiModel {

    @SerializedName("titik")
    @Expose
    private List<Titik> titik = null;
    @SerializedName("success")
    @Expose
    private Integer success;

    public List<Titik> getTitik() {
        return titik;
    }

    public void setTitik(List<Titik> titik) {
        this.titik = titik;
    }

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

}