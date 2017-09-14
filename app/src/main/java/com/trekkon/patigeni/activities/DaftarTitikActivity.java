package com.trekkon.patigeni.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.trekkon.patigeni.R;
import com.trekkon.patigeni.adapter.RV_Titik;
import com.trekkon.patigeni.controller.titik.TitikController;
import com.trekkon.patigeni.controller.titik.TitikResult;
import com.trekkon.patigeni.model.TitikApiModel;
import com.trekkon.patigeni.helper.BaseFunction;

import java.io.IOException;

public class DaftarTitikActivity extends SuperActivity implements TitikResult {

    Button btnMenuUtama, btnRefresh;
    RecyclerView rv;
    //Timer timer;


    TitikController titikController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_titik);

        BaseFunction.PeriksaGPS(DaftarTitikActivity.this);
        FindID();

    }

    void ProsesDataTitik() throws IOException {


        try {
            String id = BaseFunction.GetUserID(DaftarTitikActivity.this);

            titikController = new TitikController(this,DaftarTitikActivity.this);
            titikController.Titik(id);
        } catch (Throwable e) {
            throw new IOException(e.toString());
        }


    }

    void FindID(){

        btnRefresh=(Button)findViewById(R.id.btn_Refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ProsesDataTitik();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        rv=(RecyclerView)findViewById(R.id.rv);
        try {
            ProsesDataTitik();
        } catch (IOException e) {
            e.printStackTrace();
        }

        rv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        btnMenuUtama=(Button) findViewById(R.id.btnMenuUtama);
        btnMenuUtama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DaftarTitikActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void showValidationError() {

    }

    @Override
    public void onSuccess(TitikApiModel titikApiModel) {


        rv.setLayoutManager(new LinearLayoutManager(DaftarTitikActivity.this));
        rv.setAdapter(new RV_Titik(titikApiModel.getTitik(),R.layout.rv_titik,getApplicationContext()));

    }

    @Override
    public void onError(String errMessage) {
        showToast(DaftarTitikActivity.this, errMessage);
    }










}

