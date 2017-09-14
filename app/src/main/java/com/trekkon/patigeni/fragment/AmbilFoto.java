package com.trekkon.patigeni.fragment;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.trekkon.patigeni.R;
import com.trekkon.patigeni.controller.BaseResult;
import com.trekkon.patigeni.controller.upload.UploadController;
import com.trekkon.patigeni.helper.GPSTracker;
import com.trekkon.patigeni.helper.ResizeImage;
import com.trekkon.patigeni.utils.CommonUtils;
import com.trekkon.patigeni.utils.SessionManagement;
import com.trekkon.patigeni.helper.BaseFunction;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AmbilFoto extends Fragment implements BaseResult {


    UploadController uploadController;
    Button btnClear, btnKirim;
    private static final int TAKE_PICTURE = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;

    GPSTracker gps;
    LinearLayout layout_lokasi;

    private Uri tempUri;
    private Uri imageUri;

    TextView tvLat,tvLang,tvLokasi;
    ImageView gambar;

    boolean image_selected;

    private Bitmap mImageBitmap;
    private String image_ready;
    private String nama_foto;
    String mediaPath;
    CommonUtils util;

    SessionManagement sessionManagement;


    public AmbilFoto() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.example.primananda.sipatigeni.fragment
        util = new CommonUtils(getActivity());
        return inflater.inflate(R.layout.fragment_ambil_foto, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //requestStoragePermission();
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        FindID();
        SiapkanGPS();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            // When an Image is picked
            if (requestCode == 0 && resultCode == RESULT_OK && null != data) {

                // Get the Image from data
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                mediaPath = cursor.getString(columnIndex);
                // Set the Image in ImageView for Previewing the Media
                gambar.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
                image_selected=true;
                cursor.close();

            } else {
                Toast.makeText(getActivity(), "You haven't picked Image/Video", Toast.LENGTH_LONG).show();
                image_selected=false;
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
        }
    }

    public void takePhoto(View view) {

        //ambil variabel id_user dari sessionmanagement


            nama_foto = BaseFunction.RenamePhoto(getActivity());

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File photo = new File(Environment.getExternalStorageDirectory(),  nama_foto);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(photo));
            imageUri = Uri.fromFile(photo);
            startActivityForResult(intent, TAKE_PICTURE);

    }

    void Upload_Image(){

            try {
                //kode dibawah khusus untuk upload image saja
                nama_foto=BaseFunction.RenamePhoto(getActivity());
                Map<String, RequestBody> map = new HashMap<>();

                File file = new File(mediaPath);
                image_ready= ResizeImage.resizeAndCompressImageBeforeSend(getActivity().getApplicationContext(), file.getPath(), nama_foto);
                File file_image_ready = new File(image_ready);

                RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file_image_ready);
                map.put("file\"; filename=\"" + file_image_ready.getName() + "\"", requestBody);

                uploadController = new UploadController(this, getActivity());
                uploadController.Upload("token", map);
            } catch (Throwable e) {
                e.printStackTrace();
            }


    }

    void SiapkanGPS(){

        gps = new GPSTracker(getActivity());
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            tvLat.setText(String.valueOf(latitude));
            tvLang.setText(String.valueOf(longitude));


        }else{

            gps.showSettingsAlert();
        }
    }

    void FindID(){

        image_selected=false;

        tvLat = (TextView)getActivity().findViewById(R.id.tvLat);
        tvLang=(TextView)getActivity().findViewById(R.id.tvLang);
        tvLokasi=(TextView)getActivity().findViewById(R.id.tvLokasi);



        layout_lokasi=(LinearLayout)getActivity().findViewById(R.id.layout_lokasi);
        layout_lokasi.setVisibility(View.GONE);

        tvLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layout_lokasi.getVisibility()==View.GONE) {
                    layout_lokasi.setVisibility(View.VISIBLE);
                }
                SiapkanGPS();
                tvLokasi.setText("Refresh lokasi");
            }
        });


        gambar = (ImageView)getActivity().findViewById(R.id.gambar);
        gambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //takePhoto(v);

                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 0);


            }
        });


        btnClear=(Button)getActivity().findViewById(R.id.btnClear);
        btnKirim=(Button)getActivity().findViewById(R.id.btnKirim);


        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gambar.setImageResource(R.mipmap.ic_ambil_foto);
                image_selected=false;
            }
        });

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image_selected==false){
                    Toast.makeText(getActivity(), "Foto belum dipilih", Toast.LENGTH_SHORT).show();
                }else{
                    Upload_Image();
                }
            }
        });
    }




    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    @Override
    public void showValidationError() {
        Toast.makeText(getActivity(), "Gambar gagal dikirim", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess() {
        Toast.makeText(getActivity(), "Gambar telah terkirim", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String errorMessage) {
//        Toast.makeText(getActivity(), "Gambar gagal dikirim", Toast.LENGTH_SHORT).show();
        CommonUtils.ToastUtil(getActivity(), "Gambar gagal dikirim" );
    }
}
