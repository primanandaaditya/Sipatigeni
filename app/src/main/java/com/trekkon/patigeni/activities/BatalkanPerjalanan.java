package com.trekkon.patigeni.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.trekkon.patigeni.R;
import com.trekkon.patigeni.helper.DatabaseHandler;
import com.trekkon.patigeni.model.TabelStatus;

public class BatalkanPerjalanan extends AppCompatActivity {


    Button btnYa,btnTidak;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_batalkan_perjalanan);
        this.setFinishOnTouchOutside(false);

        FindID();
    }

    void FindID(){
        btnTidak=(Button)findViewById(R.id.btnTidak);
        btnYa=(Button)findViewById(R.id.btnYa);

        btnTidak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnYa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=getIntent();
                String idNotif = intent.getStringExtra("idnotif");

                DatabaseHandler databaseHandler=new DatabaseHandler(BatalkanPerjalanan.this);
                databaseHandler.deleteTabelStatus(idNotif);
                TabelStatus tabelStatus = new TabelStatus();
                tabelStatus.setIdTitik(idNotif);
                tabelStatus.setKeterangan("0");
                databaseHandler.addTabelStatus(tabelStatus);
                finish();

            }
        });
    }
}
