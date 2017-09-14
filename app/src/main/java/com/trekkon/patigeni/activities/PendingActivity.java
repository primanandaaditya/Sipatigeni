package com.trekkon.patigeni.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;


import com.trekkon.patigeni.R;
import com.trekkon.patigeni.adapter.PendingGridViewAdapter;
import com.trekkon.patigeni.adapter.RV_Gambar;
import com.trekkon.patigeni.controller.BaseResult;
import com.trekkon.patigeni.controller.posting.PostingController;
import com.trekkon.patigeni.controller.upload.UploadController;
import com.trekkon.patigeni.helper.ResizeImage;
import com.trekkon.patigeni.helper.DatabaseHandler;
import com.trekkon.patigeni.helper.SubmitGambar;
import com.trekkon.patigeni.helper.TrekkonImageView;
import com.trekkon.patigeni.model.TabelStatus;
import com.trekkon.patigeni.utils.SessionManagement;
import com.trekkon.patigeni.model.GambarModel;
import com.trekkon.patigeni.constants.BaseCode;
import com.trekkon.patigeni.helper.BaseFunction;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class PendingActivity extends AppCompatActivity implements BaseResult {

    ListView rv;
    GridView gv;
    Button btnBatal,btnKirim;
    String idnotif;

    UploadController uploadController;
    PostingController postingController;

    List<GambarModel> gambarModels;
    RV_Gambar rv_gambar;
    GambarModel gambarModel;

    TrekkonImageView trekkonImageView;
    PendingGridViewAdapter pendingGridViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_pending);

        FindID();

        DatabaseHandler db = new DatabaseHandler(this);
        gambarModels=db.getAllgambar();

        //rv_gambar = new RV_Gambar(PendingActivity.this,gambarModels);
        pendingGridViewAdapter=new PendingGridViewAdapter(PendingActivity.this, gambarModels);
        gv.setAdapter(pendingGridViewAdapter);
        pendingGridViewAdapter.notifyDataSetChanged();
        //rv.setAdapter(rv_gambar);
        //rv_gambar.notifyDataSetChanged();


    }

    void RefreshListView(){
        DatabaseHandler db = new DatabaseHandler(this);
        gambarModels=db.getAllgambar();

        //rv_gambar = new RV_Gambar(PendingActivity.this,gambarModels);
        pendingGridViewAdapter=new PendingGridViewAdapter(PendingActivity.this, gambarModels);
        gv.setAdapter(pendingGridViewAdapter);
        pendingGridViewAdapter.notifyDataSetChanged();


    }


    @Override
    protected void onResume() {
        super.onResume();
        RefreshListView();
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int choice) {
            switch (choice) {
                case DialogInterface.BUTTON_POSITIVE:

                    DatabaseHandler databaseHandler = new DatabaseHandler(PendingActivity.this);
                    databaseHandler.DeleteTableGambar();
                    RefreshListView();

                    Toast.makeText(PendingActivity.this,"Daftar foto telah terhapus",Toast.LENGTH_SHORT).show();
                    finish();

                    Intent intent = new Intent(PendingActivity.this, DaftarTitikActivity.class);
                    startActivity(intent);

                    break;
                case DialogInterface.BUTTON_NEGATIVE:


                    break;
            }
        }
    };


    void FindID(){
        gv=(GridView)findViewById(R.id.gv);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                trekkonImageView = (TrekkonImageView)view.findViewById(R.id.tiv);
                Intent intent = new Intent(PendingActivity.this, DetailGambar.class);
                intent.putExtra("fileGambar", trekkonImageView.getFileGambar());
                intent.putExtra("keterangan", trekkonImageView.getKeterangan());
                intent.putExtra("idx",trekkonImageView.getIdx());
                startActivity(intent);
            }
        });


        //rv=(ListView) findViewById(R.id.rv);
        btnBatal=(Button)findViewById(R.id.btnBatal);
        btnKirim=(Button)findViewById(R.id.btnKirim);

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubmitGambar submitGambar = new SubmitGambar(PendingActivity.this);
                submitGambar.Submit();

                Intent intent =  getIntent();
                idnotif = intent.getStringExtra("idnotif");



                DatabaseHandler databaseHandler = new DatabaseHandler(PendingActivity.this);
                databaseHandler.deleteTabelStatus(idnotif);
                TabelStatus tabelStatus = new TabelStatus();
                tabelStatus.setIdTitik(idnotif);
                tabelStatus.setKeterangan("2");
                databaseHandler.addTabelStatus(tabelStatus);


            }
        });

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();

                AlertDialog.Builder builder = new AlertDialog.Builder(PendingActivity.this);
                builder.setMessage("Hapus semua? Jika ya, maka Anda harus ke lokasi dan memotret lagi.")
                        .setPositiveButton("Ya", dialogClickListener)
                        .setNegativeButton("Tidak", dialogClickListener).show();




            }
        });
    }

    @Override
    public void showValidationError() {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError(String errorMessage) {

    }


}