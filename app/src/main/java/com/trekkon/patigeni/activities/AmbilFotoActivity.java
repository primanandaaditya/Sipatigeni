package com.trekkon.patigeni.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.trekkon.patigeni.R;
import com.trekkon.patigeni.controller.BaseResult;
import com.trekkon.patigeni.controller.upload.UploadController;
import com.trekkon.patigeni.helper.ResizeImage;
import com.trekkon.patigeni.helper.DatabaseHandler;

import com.trekkon.patigeni.model.GambarModel;
import com.trekkon.patigeni.constants.BaseCode;
import com.trekkon.patigeni.helper.BaseFunction;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.EasyPermissions;

public class AmbilFotoActivity extends SuperActivity implements EasyPermissions.PermissionCallbacks, BaseResult {

    UploadController uploadController;
    Button btnBatal,btnSimpan;
    ImageView gambar1,gambar2,gambar3,gambar4;

    String[] mediaPath = {"","","",""};
    boolean[] image_selected = {false,false,false,false};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambil_foto);

        FindID();

    }


    void SimpanGambarKeSQLite(String[] mediapath, int indeks){

        if (TextUtils.isEmpty(mediapath[indeks]) || mediapath[indeks].equals("") ) {

        }else{

            DatabaseHandler databaseHandler = new DatabaseHandler(AmbilFotoActivity.this);
            GambarModel gambarModel = new GambarModel();
            gambarModel.setIdTitik("Titik 1");
            gambarModel.setFilegambar(mediapath[indeks]);
            gambarModel.setIndeks(String.valueOf(indeks));
            gambarModel.setLat(2.343343);
            gambarModel.setLong(101.3434343);
            gambarModel.setSos("Green");
            gambarModel.setKeterangan("Keterangan");

            databaseHandler.addGambar(gambarModel);

        }


        }

    void UploadGambar(String[] mediapath, int Index){
        if (TextUtils.isEmpty(mediapath[Index]) || mediapath[Index].equals("") ){

        }else{

            try {
                //kode dibawah khusus untuk upload image saja
                String image_ready;
                String nama_foto= String.valueOf(Index) + BaseFunction.RenamePhoto(AmbilFotoActivity.this) ;
                Map<String, RequestBody> map = new HashMap<>();

                //string path yang digunakan untuk sqlite nanti
                //adalah mediaPath[]
                File file = new File(mediapath[Index]);
                image_ready= ResizeImage.resizeAndCompressImageBeforeSend(getApplicationContext(), file.getPath(), nama_foto);
                File file_image_ready = new File(image_ready);

                RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file_image_ready);
                map.put("file\"; filename=\"" + file_image_ready.getName() + "\"", requestBody);

                uploadController = new UploadController(this, AmbilFotoActivity.this);
                uploadController.Upload("token", map);
            } catch (Throwable e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        try {
            // When an Image is picked
            Uri selectedImage;
            Cursor cursor;
            int columnIndex;

            if (requestCode == 1 && resultCode == RESULT_OK && null != data) {

                selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath[0] = cursor.getString(columnIndex);
                gambar1.setImageBitmap(BitmapFactory.decodeFile(mediaPath[0]));
                image_selected[0]=true;
                cursor.close();

            } else if (requestCode == 2 && resultCode == RESULT_OK && null != data) {

                selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath[1] = cursor.getString(columnIndex);
                gambar2.setImageBitmap(BitmapFactory.decodeFile(mediaPath[1]));
                image_selected[1]=true;
                cursor.close();

            } else if (requestCode == 3 && resultCode == RESULT_OK && null != data) {

                selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath[2] = cursor.getString(columnIndex);
                gambar3.setImageBitmap(BitmapFactory.decodeFile(mediaPath[2]));
                image_selected[2]=true;
                cursor.close();

            } else if (requestCode == 4 && resultCode == RESULT_OK && null != data) {

                selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath[3] = cursor.getString(columnIndex);
                gambar4.setImageBitmap(BitmapFactory.decodeFile(mediaPath[3]));
                image_selected[3]=true;
                cursor.close();

            }



        } catch (Exception e) {
            Toast.makeText(AmbilFotoActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }


    void FindID(){
        gambar1=(ImageView)findViewById(R.id.gambar1);
        gambar2=(ImageView)findViewById(R.id.gambar2);
        gambar3=(ImageView)findViewById(R.id.gambar3);
        gambar4=(ImageView)findViewById(R.id.gambar4);

        gambar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFoto(1);
            }
        });

        gambar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFoto(2);
            }
        });

        gambar3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFoto(3);
            }
        });

        gambar4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFoto(4);
            }
        });

        btnBatal=(Button)findViewById(R.id.btnBatal);
        btnSimpan=(Button)findViewById(R.id.btnSimpan);

        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //Posting();
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //UploadGambar(mediaPath,0);
                //UploadGambar(mediaPath,1);
                //UploadGambar(mediaPath,2);
                //UploadGambar(mediaPath,3);


                ProgressDialog progressDialog;
                progressDialog = new ProgressDialog(AmbilFotoActivity.this);
                progressDialog.setTitle(BaseCode.PROGRESS_TITLE);
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.show();

                SimpanGambarKeSQLite(mediaPath,0);
                SimpanGambarKeSQLite(mediaPath,1);
                SimpanGambarKeSQLite(mediaPath,2);
                SimpanGambarKeSQLite(mediaPath,3);

                progressDialog.dismiss();


                finish();

            }
        });
    }

    void DialogFoto(int i){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, i);
    }

    void Coba(){
        DatabaseHandler databaseHandler = new DatabaseHandler(AmbilFotoActivity.this);

        GambarModel gm = new GambarModel();
        gm.setIdx(5);

        databaseHandler.deleteGambar(gm);

        List<GambarModel> gambarModels = databaseHandler.getAllgambar();
        for (int i =0; i < gambarModels.size(); i ++){
            GambarModel gambarModel = gambarModels.get(i);
            Toast.makeText(AmbilFotoActivity.this, String.valueOf(gambarModel.getIdx()), Toast.LENGTH_SHORT).show();
        }
    }

    void Posting(){
        DatabaseHandler databaseHandler = new DatabaseHandler(AmbilFotoActivity.this);
        List<GambarModel> gambarModels = databaseHandler.getAllgambar();
        GambarModel gambarModel;

        if (gambarModels.size() == 0){

        }else{
            String lokasi = "";

            ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(AmbilFotoActivity.this);
            progressDialog.setTitle(BaseCode.PROGRESS_TITLE);
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);

            progressDialog.show();

            for (int i = 0; i < gambarModels.size(); i++){
                gambarModel = gambarModels.get(i);
                lokasi = gambarModel.getFilegambar();


                if (TextUtils.isEmpty(lokasi) || lokasi.equals("")){

                }else{

                    try {
                        //kode dibawah khusus untuk upload image saja
                        String image_ready;
                        String nama_foto= String.valueOf(i) + BaseFunction.RenamePhoto(AmbilFotoActivity.this) ;
                        Map<String, RequestBody> map = new HashMap<>();

                        //string path yang digunakan untuk sqlite nanti
                        //adalah mediaPath[]
                        File file = new File(lokasi);
                        image_ready= ResizeImage.resizeAndCompressImageBeforeSend(getApplicationContext(), file.getPath(), nama_foto);
                        File file_image_ready = new File(image_ready);

                        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file_image_ready);
                        map.put("file\"; filename=\"" + file_image_ready.getName() + "\"", requestBody);

                        uploadController = new UploadController(this, AmbilFotoActivity.this);
                        uploadController.Upload("token", map);
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }

                }
            }

            progressDialog.dismiss();

        }



    }

    void HapusTabelGambar(){
        DatabaseHandler databaseHandler = new DatabaseHandler(AmbilFotoActivity.this);
        databaseHandler.DeleteTableGambar();
        databaseHandler.close();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

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
