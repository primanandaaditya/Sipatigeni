package com.trekkon.patigeni.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GambarModel {

    @SerializedName("idx")
    @Expose
    private Integer idx;
    @SerializedName("id_titik")
    @Expose
    private String idTitik;
    @SerializedName("filegambar")
    @Expose
    private String filegambar;
    @SerializedName("indeks")
    @Expose
    private String indeks;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("long")
    @Expose
    private Double _long;
    @SerializedName("sos")
    @Expose
    private String sos;
    @SerializedName("keterangan")
    @Expose
    private String keterangan;

    public GambarModel(){

    }

    public GambarModel(int idx, String idTitik, String filegambar, String indeks, Double lat, Double _long, String sos, String keterangan){

        this.idx= idx;
        this.idTitik=idTitik;
        this.filegambar=filegambar;
        this.indeks=indeks;
        this.lat=lat;
        this._long=_long;
        this.sos=sos;
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

    public String getFilegambar() {
        return filegambar;
    }

    public void setFilegambar(String filegambar) {
        this.filegambar = filegambar;
    }

    public String getIndeks() {
        return indeks;
    }

    public void setIndeks(String indeks) {
        this.indeks = indeks;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLong() {
        return _long;
    }

    public void setLong(Double _long) {
        this._long = _long;
    }

    public String getSos() {
        return sos;
    }

    public void setSos(String sos) {
        this.sos = sos;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

}

