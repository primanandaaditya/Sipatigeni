package com.trekkon.patigeni.utils;

import android.content.Context;
import android.graphics.Color;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;
import com.trekkon.patigeni.R;
import com.trekkon.patigeni.services.ConnectionService;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static android.content.Context.LOCATION_SERVICE;


/**
 * Created by Iwan on 07-Aug-17.
 */

public class CommonUtils {
    Context context;
    private View.OnClickListener[] listeners;
    static boolean isConnected;

    public CommonUtils(Context ctx) {
        this.context=ctx;
    }
    public static void ToastUtil(Context mContext, String toastText){
        SuperActivityToast.create(mContext, new Style(), Style.TYPE_STANDARD)
//                .setButtonText("UNDO")
                .setProgressBarColor(Color.WHITE)
                .setText(toastText)
                .setDuration(Style.DURATION_MEDIUM)
                .setFrame(Style.FRAME_LOLLIPOP)
                .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_BLUE))
                .setTextSize(Style.TEXTSIZE_MEDIUM)
//                .setIconResource(R.drawable.ic_notifications_black_24dp)
                .setAnimations(Style.ANIMATIONS_FADE).show();
    }

    public static void showDialog(Context context, String textTitle, String textContent, String textPositive,
                                  String textNegative, final int dialogTYpe){
        new MaterialDialog.Builder(context)
                .title(textTitle)
                .content(textContent)
                .positiveText(textPositive)
                .negativeText(textNegative)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        dialog.dismiss();

//                        ToastUtil(context, "testt");
                    }
                })
                .show();

    }

    public static  String getIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException e) {
            // Log.e(Constants.LOG_TAG, e.getMessage(), e);
        }
        return null;
    }

    public static boolean checkGps(Context context){
        boolean isEnabled = false;
        LocationManager locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return isEnabled;
    }

    public  boolean createFolder(String dbPath, String dbName) throws Exception {
        boolean emptyFolder =false;
        File f = new File(dbPath);
        Log.i("dbPath", dbPath);
        if(!f.isDirectory()) {
            f.mkdirs();
            emptyFolder=true;
            Log.i("koosng", "kosong");
        }

        File fdb = new File(dbPath+dbName);
        if(!fdb.exists()) {
            emptyFolder=true;
        }

        return emptyFolder;
    }

    public static boolean checkConnection() {
        boolean isRes;
        isConnected = ConnectionService.isConnected();
        Log.i("Connection", String.valueOf(isConnected));
        isRes = isConnected;
        return  isRes;
    }

    public void copyDatabaseFromAssets(Context mContext,String dbPath, String dbName, String compressName) throws IOException {
        Log.i("copyDatabaseFromAssets", "copying database from assets...");
//        Log.i("path", dbPath +  dbName);
        InputStream fin=null;
        ZipInputStream zin=null;
        FileOutputStream out=null;

        try{
            fin = mContext.getAssets().open(compressName);
            zin = new ZipInputStream(fin);
            ZipEntry ze = null;
            byte[] buffer=new byte[1024];
            while ((ze = zin.getNextEntry()) != null) {
                final int maxSize = (int) ze.getSize();
                final File outFile = new File(dbPath, ze.getName());
                out = new FileOutputStream(outFile);
                writeExtractedFileToDisk(zin,out);
            }

        } catch (FileNotFoundException ex) {
//			SQLiteAssetException se = new SQLiteAssetException("Missing " + dbPath + " file in assets or target folder not writable");
            //se.setStackTrace(fe.getStackTrace());
            Log.i("fileNotFoundExcepton2", ex.toString());
            ex.toString();
            throw ex;
        } catch (IOException e) {
//			SQLiteAssetException se = new SQLiteAssetException("Unable to extract " + dbPath + " to data directory");
            //se.setStackTrace(e.getStackTrace());
            e.toString();
            Log.i("fileNotFoundExcepton3", e.toString());
            throw e;
        }finally{
            out.close();
            zin.close();
        }
    }

    private void writeExtractedFileToDisk(ZipInputStream zin,FileOutputStream fout) throws IOException {

        byte[] buffer = new byte[1024];
        BufferedOutputStream bos = new BufferedOutputStream(fout, buffer.length);
        int size;

        while ((size = zin.read(buffer, 0, buffer.length)) != -1) {
            bos.write(buffer, 0, size);
        }

        bos.flush();
        bos.close();

    }

    public String getIMEI(Context context){
        TelephonyManager telephonyManager;
        telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getDeviceId();
    }

    public String getPackageName(){
        return context.getPackageName();
    }

    public View.OnClickListener[] getListeners() {
        return listeners;
    }

    public void setListeners(View.OnClickListener[] listeners) {
        this.listeners = listeners;
    }


}
