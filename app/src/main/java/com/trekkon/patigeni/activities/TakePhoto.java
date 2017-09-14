package com.trekkon.patigeni.activities;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.trekkon.patigeni.R;
import com.trekkon.patigeni.adapter.PendingGridViewAdapter;
import com.trekkon.patigeni.adapter.RV_Gambar;
import com.trekkon.patigeni.constants.BaseCode;
import com.trekkon.patigeni.helper.BaseFunction;
import com.trekkon.patigeni.helper.DatabaseHandler;
import com.trekkon.patigeni.helper.GPSTracker;
import com.trekkon.patigeni.helper.TrekkonImageView;
import com.trekkon.patigeni.model.GambarModel;

import java.io.File;
import java.util.List;

public class TakePhoto extends AppCompatActivity {

    FloatingActionButton fab;
    Button btnSimpan;
    GridView gv;
    File hasilKamera;
    String mediaPath;

    RadioGroup radioApi;
    RadioButton radioButton;
    TrekkonImageView trekkonImageView;

    PendingGridViewAdapter pendingGridViewAdapter;
    final static int TAKE_CAMERA = 1;
    final static int TAKE_GALLERY = 2;
    List<GambarModel> gambarModels;
    RV_Gambar rv_gambar;
    GambarModel gambarModel;

    String getIdNotif, getLat, getLong;
    private static final int MY_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);


        requestPermission();
        GetIntent();
        FindID();
        RefreshListView();

    }

    @Override
    protected void onResume() {
        super.onResume();

        RefreshListView();
    }

    public void MulaiKamera() {


        Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File dir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        hasilKamera=new File(dir,  BaseFunction.RenamePhoto(TakePhoto.this));
        i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(hasilKamera));

        startActivityForResult(i, TAKE_CAMERA);

    }

    void DialogFoto(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, TAKE_GALLERY);

    }


    void SimpanGambarKeSQLite(String lokasi){

        if (TextUtils.isEmpty(lokasi)) {

        }else{

            String keterangan = "";
            int radioTerpilih = radioApi.getCheckedRadioButtonId();
            radioButton=(RadioButton)findViewById(radioTerpilih);


            if (radioButton.getText().equals("Kebakaran")){
                keterangan="1";
            }else if (radioButton.getText().equals("Bukan kebakaran")){
                keterangan="0";
            }else if (radioButton.getText().equals("Ragu-ragu")){
                keterangan="2";
            }

            DatabaseHandler databaseHandler = new DatabaseHandler(TakePhoto.this);
            GambarModel gambarModel = new GambarModel();
            gambarModel.setIdTitik(getIdNotif);
            gambarModel.setFilegambar(lokasi);
            gambarModel.setIndeks(String.valueOf(1));
            gambarModel.setLat(Double.parseDouble(getLat));
            gambarModel.setLong(Double.parseDouble(getLong));
            gambarModel.setSos("g");
            gambarModel.setKeterangan(keterangan);

            databaseHandler.addGambar(gambarModel);

        }


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Uri selectedImage;
        Cursor cursor;
        int columnIndex;
        Bitmap gambar = null;



        if (resultCode == Activity.RESULT_OK){
            switch (requestCode){

                case TAKE_GALLERY:
                    selectedImage = data.getData();
                    String[] filePathColumn1 = {MediaStore.Images.Media.DATA};

                    cursor = getContentResolver().query(selectedImage, filePathColumn1, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    columnIndex = cursor.getColumnIndex(filePathColumn1[0]);
                    mediaPath = cursor.getString(columnIndex);
                    //Toast.makeText(TakePhoto.this,mediaPath,Toast.LENGTH_SHORT).show();

                    gambar=BaseFunction.bacaBitmap(selectedImage,TakePhoto.this);
                    //gambar1.setImageBitmap(gambar);
                    SimpanGambarKeSQLite(mediaPath);
                    cursor.close();
                    RefreshListView();

                    break;




                case TAKE_CAMERA:

                    //gambar1.setImageURI(Uri.fromFile(gbrHasil[0]));
                    gambar=BaseFunction.bacaBitmap(Uri.fromFile(hasilKamera),TakePhoto.this);
                    //Toast.makeText(TakePhoto.this,hasilKamera.getAbsolutePath().toString(),Toast.LENGTH_SHORT).show();
                    SimpanGambarKeSQLite(hasilKamera.getAbsolutePath().toString());
                    //gambar1.setImageBitmap(gambar);
                    //mediaPath[0]=gbrHasil[0].getAbsolutePath();
                    RefreshListView();

                    break;



            }
        }

        //BaseFunction.bersihkanBitmap(gambar);


    }


    private void pilihGambar() {
        final CharSequence[] items = { "Dari kamera", "Dari galeri HP",
                "Batal" };
        AlertDialog.Builder builder = new AlertDialog.Builder(TakePhoto.this);
        builder.setTitle("Pilih Foto");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                if (items[item].equals("Dari kamera")) {

                    MulaiKamera();
                } else if (items[item].equals("Dari galeri HP")) {

                    DialogFoto();
                } else if (items[item].equals("Batal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    void RefreshListView(){
        DatabaseHandler db = new DatabaseHandler(this);
        gambarModels=db.getAllgambar();


        pendingGridViewAdapter=new PendingGridViewAdapter(TakePhoto.this, gambarModels);
        gv.setAdapter(pendingGridViewAdapter);
        pendingGridViewAdapter.notifyDataSetChanged();


    }

    void GetIntent(){
        Intent intent = getIntent();
        getIdNotif = intent.getStringExtra("setIdNotif");
        getLat = intent.getStringExtra("setLat");
        getLong = intent.getStringExtra("setLong");

        if (getIdNotif==null){
            getIdNotif="0";
        }

        if (getLat==null){
            getLat="0";
        }

        if (getLong==null){
            getLong="0";
        }


    }


    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(TakePhoto.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(TakePhoto.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

        }else{
            ActivityCompat.requestPermissions(TakePhoto.this,new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults.length == 2){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){

                }else{
                    Toast.makeText(TakePhoto.this, "Permission ditolak",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }





    void FindID(){
        radioApi=(RadioGroup)findViewById(R.id.radioApi);
        fab=(FloatingActionButton)findViewById(R.id.fab);

        gv=(GridView)findViewById(R.id.gv);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                trekkonImageView = (TrekkonImageView)view.findViewById(R.id.tiv);
                Intent intent = new Intent(TakePhoto.this, DetailGambar.class);
                intent.putExtra("fileGambar", trekkonImageView.getFileGambar());
                intent.putExtra("keterangan", trekkonImageView.getKeterangan());
                intent.putExtra("idx",trekkonImageView.getIdx());
                startActivity(intent);

            }
        });



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pilihGambar();

            }
        });

        btnSimpan=(Button)findViewById(R.id.btnSimpan);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHandler databaseHandler = new DatabaseHandler(TakePhoto.this);
                String keterangan = "";
                int radioTerpilih = radioApi.getCheckedRadioButtonId();
                radioButton=(RadioButton)findViewById(radioTerpilih);


                if (radioButton.getText().equals("Kebakaran")){
                    keterangan="1";
                }else if (radioButton.getText().equals("Bukan kebakaran")){
                    keterangan="0";
                }else if (radioButton.getText().equals("Ragu-ragu")){
                    keterangan="2";
                }
                databaseHandler.UpdateKeterangan(keterangan);
                finish();

                Intent intent = new Intent(TakePhoto.this, PendingActivity.class);
                intent.putExtra("idnotif",getIdNotif);
                startActivity(intent);


            }
        });

    }



}