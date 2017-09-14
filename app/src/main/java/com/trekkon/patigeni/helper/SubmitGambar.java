package com.trekkon.patigeni.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.trekkon.patigeni.constants.BaseCode;
import com.trekkon.patigeni.controller.BaseResult;
import com.trekkon.patigeni.controller.posting.PostingController;
import com.trekkon.patigeni.controller.upload.UploadController;
import com.trekkon.patigeni.model.GambarModel;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import okhttp3.MediaType;
import okhttp3.RequestBody;

public class SubmitGambar implements BaseResult {

    static Context context;
    PostingController postingController;
    UploadController uploadController;
    ProgressDialog pd;

    public SubmitGambar(Context context){
        SubmitGambar.context = context;
    }


    public void Submit(){

        pd = new ProgressDialog(context);
        pd.setCancelable(true);
        pd.setMessage(BaseCode.PROGRESS_TITLE);
        pd.setTitle("Mengirimkan foto");
        pd.show();


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Posting();
                pd.dismiss();
                Toast.makeText(context,"Foto telah terkirim",Toast.LENGTH_SHORT).show();
                ((Activity)context).finish();
            }
        }, 3000);   //5 seconds



    }




    public void Posting(){

        boolean hapus = false;

        DatabaseHandler databaseHandler = new DatabaseHandler(context);
        List<GambarModel> gambarModels = databaseHandler.getAllgambar();
        GambarModel gambarModel;

        String lokasi, idnotif, status, image_ready, nama_foto;
        Map<String, RequestBody> map;
        final String userID = BaseFunction.GetUserID(context);
        int jml_record;
        Double lat, slong;
        File file, file_image_ready;

        if (gambarModels.size() == 0){

        }else{




            jml_record = gambarModels.size();
            //Handler handler = new Handler();

            //handler.postDelayed(new Runnable() {
            //    public void run() {
//
             //       Log.d("Info","Looping");
             //   }
            //}, 1000);

            for (int i = 0; i < jml_record; i++){




                gambarModel = gambarModels.get(i);
                if (gambarModel.getFilegambar().equals(null)){
                    lokasi="";
                }else{
                    lokasi=gambarModel.getFilegambar();
                }

                lat = gambarModel.getLat();
                slong = gambarModel.getLong();
                idnotif = gambarModel.getIdTitik();
                status = gambarModel.getKeterangan();


                if (TextUtils.isEmpty(lokasi) || lokasi.equals("") || lokasi==null){

                }else{


                    //kode dibawah khusus untuk upload image saja

                    nama_foto= String.valueOf(i) + BaseFunction.RenamePhoto(context) ;
                    map = new HashMap<>();

                    //string path yang digunakan untuk sqlite nanti
                    //adalah mediaPath[]
                    file = new File(lokasi);
                    image_ready= ResizeImage.resizeAndCompressImageBeforeSend(context, file.getPath(), nama_foto);
                    file_image_ready = new File(image_ready);

                    RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file_image_ready);
                    map.put("file\"; filename=\"" + file_image_ready.getName() + "\"", requestBody);

                    postingController = new PostingController(this, context);
                    postingController.posting(status,userID,idnotif,file_image_ready.getName(),lat,slong);

                    uploadController = new UploadController(this, context);
                    uploadController.Upload("token", map);

                }


            }

            databaseHandler.DeleteTableGambar();
            //RefreshListView();
            //progressDialog.dismiss();
            //pd.dismiss();

        }


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
