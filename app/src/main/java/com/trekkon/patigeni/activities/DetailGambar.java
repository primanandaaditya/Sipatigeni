package com.trekkon.patigeni.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.trekkon.patigeni.R;
import com.trekkon.patigeni.helper.BaseFunction;
import com.trekkon.patigeni.helper.DatabaseHandler;

import java.io.File;

public class DetailGambar extends AppCompatActivity {


    ImageView gambar;
    RadioGroup radioApi;
    RadioButton radioButton;
    RadioButton radioKebakaran,radioBukanKebakaran, radioRagu;
    Button btnHapus, btnSimpan;
    int idx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_gambar);

        FindID();
        GetIntent();
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int choice) {
            switch (choice) {
                case DialogInterface.BUTTON_POSITIVE:

                    DatabaseHandler databaseHandler = new DatabaseHandler(DetailGambar.this);
                    databaseHandler.deleteGambar(idx);
                    Toast.makeText(DetailGambar.this, "Gambar telah terhapus",Toast.LENGTH_SHORT).show();
                    finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:


                    break;
            }
        }
    };




    void FindID(){
        gambar=(ImageView)findViewById(R.id.gambar);
        radioApi=(RadioGroup)findViewById(R.id.radioApi);
        radioKebakaran=(RadioButton)findViewById(R.id.radioKebakaran);
        radioBukanKebakaran=(RadioButton)findViewById(R.id.radioBukanKebakaran);
        radioRagu=(RadioButton)findViewById(R.id.radioRagu);

        btnHapus=(Button)findViewById(R.id.btnHapus);
        btnSimpan=(Button)findViewById(R.id.btnSimpan);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keterangan = "";
                DatabaseHandler databaseHandler = new DatabaseHandler(DetailGambar.this);

                int radioTerpilih = radioApi.getCheckedRadioButtonId();
                radioButton=(RadioButton)findViewById(radioTerpilih);


                if (radioButton.getText().equals("Kebakaran")){
                    keterangan="1";
                }else if (radioButton.getText().equals("Bukan kebakaran")){
                    keterangan="0";
                }else if (radioButton.getText().equals("Ragu-ragu")){
                    keterangan="2";
                }


                databaseHandler.UpdateKeterangan(keterangan, idx);
                Toast.makeText(DetailGambar.this,"Gambar telah diedit",Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DetailGambar.this);
                builder.setMessage("Hapus gambar ini?")
                        .setPositiveButton("Ya", dialogClickListener)
                        .setNegativeButton("Tidak", dialogClickListener).show();




            }
        });

    }

    void GetIntent(){
        Intent intent = getIntent();
        BaseFunction.LoadFileIntoImageView(gambar,intent.getStringExtra("fileGambar"));

        idx=intent.getIntExtra("idx",0);


        switch (intent.getStringExtra("keterangan")){
            case "1":
                radioApi.check(R.id.radioKebakaran);
                break;

            case "0":
                radioApi.check(R.id.radioBukanKebakaran);
                break;

            case "2":
                radioApi.check(R.id.radioRagu);
                break;

            default:

                break;
        }


    }
}
