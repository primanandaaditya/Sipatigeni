package com.trekkon.patigeni.helper;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.trekkon.patigeni.activities.BatalkanPerjalanan;
import com.trekkon.patigeni.activities.LoginActivity;
import com.trekkon.patigeni.activities.MainActivity;
import com.trekkon.patigeni.activities.MapsActivity;
import com.trekkon.patigeni.activities.TakePhoto;
import com.trekkon.patigeni.constants.BaseCode;
import com.trekkon.patigeni.model.TabelStatus;
import com.trekkon.patigeni.utils.SessionManagement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class BaseFunction {

    public static GPSTracker gps;

    public static String getPassword(Context context){

        SessionManagement sessionManagement = new SessionManagement(context);
        return sessionManagement.getPassword();
    }

    public static void LoadFileIntoImageView(ImageView imageView, String path) {
        File file = new File(path);
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            imageView.setImageBitmap(bitmap);
        }
    }

    public static void Logout(Context context){
        SessionManagement sessionManagement = new SessionManagement(context);
        sessionManagement.logoutUser();


        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);

        ((Activity)context).finish();
    }

    public static void DialogTitikApi(final Context context, final LatLng latLng, final String idNotif) {
        final CharSequence[] items = {"Arahkan saya", "Ambil foto","Batalkan perjalanan",
                "Tutup"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Pilih tindakan");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


                DatabaseHandler databaseHandler = new DatabaseHandler(context);
                if (items[item].equals("Arahkan saya")) {
                    TampilkanMap(context, latLng);

                    //status on going

                    databaseHandler.deleteTabelStatus(idNotif);
                    TabelStatus tabelStatus = new TabelStatus();
                    tabelStatus.setIdTitik(idNotif);
                    tabelStatus.setKeterangan("1");
                    databaseHandler.addTabelStatus(tabelStatus);


                } else if (items[item].equals("Ambil foto")) {
                    Intent intent = new Intent(context, TakePhoto.class);
                    intent.putExtra("setLat", String.valueOf(latLng.latitude));
                    intent.putExtra("setLong", String.valueOf(latLng.longitude));
                    intent.putExtra("setIdNotif", idNotif);
                    context.startActivity(intent);



                } else if (items[item].equals("Tutup")) {
                    dialog.dismiss();

                } else if (items[item].equals("Batalkan perjalanan")){

                    //databaseHandler.deleteTabelStatus(idNotif);
                    //TabelStatus tabelStatus = new TabelStatus();
                    //tabelStatus.setIdTitik(idNotif);
                    //tabelStatus.setKeterangan("0");
                    //databaseHandler.addTabelStatus(tabelStatus);

                    if (databaseHandler.cekDetail(idNotif,context).equals(true)){
                        if (databaseHandler.getDetail(idNotif, context).equals("2")){
                            Toast.makeText(context,"Tidak bisa dibatalkan",Toast.LENGTH_SHORT).show();
                        }else{
                            Intent intent = new Intent(context, BatalkanPerjalanan.class);
                            intent.putExtra("idnotif",idNotif);
                            context.startActivity(intent);
                        }
                    }


                }
            }
        });
        builder.show();
    }


    public static void TampilkanMap(final Context context, LatLng latLng) {


        String currentLattitude=String.valueOf(BaseFunction.GetMyLat(context));
        String currentLongitude=String.valueOf(BaseFunction.GetMyLong(context));



        String url = "http://maps.google.com/maps?saddr="+currentLattitude+","+currentLongitude+"&daddr="+String.valueOf(latLng.latitude)+","+String.valueOf(latLng.longitude)+"&mode=driving";
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
        context.startActivity(intent);

        //String label = "Titik Panas";
        //String uriBegin = "geo:" + String.valueOf(BaseFunction.GetMyLat(context)) + "," + String.valueOf(BaseFunction.GetMyLong(context));
        //String query =  latLng.latitude + "," + latLng.longitude + "(" + label + ")";
        //String encodedQuery = Uri.encode(query);
        //String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
        //Uri uri = Uri.parse(uriString);
        //Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
        //context.startActivity(intent);


    }


    public static Bitmap KompresGambar(String lokasi){

        BitmapFactory.Options options;

        try {

            Bitmap bitmap = BitmapFactory.decodeFile(lokasi);
            return bitmap;
        } catch (OutOfMemoryError e) {
            try {
                options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                Bitmap bitmap = BitmapFactory.decodeFile(lokasi,options);
                return bitmap;
            } catch(Exception ex) {

            }
        }

        return null;
    }



    // Clear bitmap

    public static void bersihkanBitmap(Bitmap bm) {
        bm.recycle();
        System.gc();
    }

    // Read bitmap
    public static Bitmap bacaBitmap(Uri selectedImage, Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 5;
        AssetFileDescriptor fileDescriptor =null;
        try {
            fileDescriptor = context.getContentResolver().openAssetFileDescriptor(selectedImage,"r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        finally{
            try {
                bm = BitmapFactory.decodeFileDescriptor(fileDescriptor.getFileDescriptor(), null, options);
                fileDescriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm;
    }


    public static void PeriksaGPS(Context context){
        GPSTracker gps;
        gps = new GPSTracker(context);
        if(gps.canGetLocation()){

            //double latitude = gps.getLatitude();
            //double longitude = gps.getLongitude();

        }else{

            gps.showSettingsAlert();
        }
    }


    public static Double GetMyLat(Context context){
        Double hasil;

        GPSTracker gpsTracker = new GPSTracker(context);
        hasil=gpsTracker.getLatitude();

        return hasil;
    }


    public static Double GetMyLong(Context context){
        Double hasil;

        GPSTracker gpsTracker = new GPSTracker(context);
        hasil=gpsTracker.getLongitude();

        return hasil;
    }


    public static String GetUserID(Context context){
        SessionManagement sessionManagement= new SessionManagement(context);
        String hasil;

        try {
            hasil = sessionManagement.getUniqueId().toString();
            Log.i("GetUserId", hasil);
        } catch (Throwable e) {
            hasil= "2";
        }



        return  hasil;
    }

    public static List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    public static boolean PunyaIzin(Context context)
    {
        boolean hasil;
        PackageManager pm = context.getPackageManager();
        int hasPerm = pm.checkPermission(
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                context.getPackageName());
        hasil = hasPerm == PackageManager.PERMISSION_GRANTED;

        return  hasil;
    }


    public static String RenamePhoto(Context context){
        SessionManagement sessionManagement;
        sessionManagement = new SessionManagement(context);

        String nama_foto;
        Calendar c = Calendar.getInstance();

        nama_foto = GetUserID(context) +
                String.valueOf(c.get(Calendar.YEAR)) +
                String.valueOf(c.get(Calendar.MONTH)) +
                String.valueOf(c.get(Calendar.DATE)) +
                String.valueOf(c.get(Calendar.HOUR)) +
                String.valueOf(c.get(Calendar.MINUTE)) +
                String.valueOf(c.get(Calendar.SECOND)) +
                ".jpg"
        ;

        return nama_foto;
    }

}