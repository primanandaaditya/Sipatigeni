package com.trekkon.patigeni.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TabelStatus {

    @SerializedName("idx")
    @Expose
    private Integer idx;
    @SerializedName("id_titik")
    @Expose
    private String idTitik;
    @SerializedName("keterangan")
    @Expose
    private String keterangan;

    public TabelStatus(){

    }

    public TabelStatus(int idx, String idTitik, String keterangan){

        this.idx= idx;
        this.idTitik=idTitik;
        this.keterangan=keterangan;

    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public String getIdTitik() {
        return idTitik;
    }

    public void setIdTitik(String idTitik) {
        this.idTitik = idTitik;
    }


    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

}

