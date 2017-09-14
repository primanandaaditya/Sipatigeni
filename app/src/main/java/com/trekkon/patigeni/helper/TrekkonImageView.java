package com.trekkon.patigeni.helper;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Primananda on 8/7/2017.
 */

public class TrekkonImageView extends android.support.v7.widget.AppCompatImageView {

    int idx;
    String idNotif;
    Double slat, slong;
    String keterangan;
    String indeks, sos, fileGambar;


    public TrekkonImageView(Context context, int idx, String idNotif, Double slat, Double slong, String keterangan, String indeks, String sos, String fileGambar) {
        super(context);
        this.idx = idx;
        this.idNotif = idNotif;
        this.slat = slat;
        this.slong = slong;
        this.keterangan=keterangan;
        this.sos=sos;
        this.indeks=indeks;
        this.fileGambar=fileGambar;
    }

    public String getFileGambar(){
        return this.fileGambar;
    }

    public void setFileGambar(String fileGambar){
        this.fileGambar = fileGambar;
    }

    public void setSos(String sos){
        this.sos = sos;
    }

    public String getSos(){
        return this.sos;
    }

    public void setIndeks(String indeks){
        this.indeks = indeks;
    }
    public String getIndeks(){
        return this.indeks;
    }


    public int getIdx(){
        return idx;
    }

    public void setIdx(int indeks){
        this.idx = indeks;
    }

    public void setKeterangan(String keterangan){
        this.keterangan=keterangan;
    }

    public String getKeterangan(){
        return this.keterangan;
    }

    public void setSlong(Double slong){
        this.slong=slong;
    }

    public Double getSlong(){
        return this.slong;
    }


    public Double getSlat(){
        return  slat;
    }


    public void setSlat(Double slat){
        this.slat = slat;
    }


    public void setIdNotif(String idNotif){
        this.idNotif = idNotif;
    }

    public String getIdNotif(){
        return idNotif;
    }





    public TrekkonImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TrekkonImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth()); //Snap to width


    }
}