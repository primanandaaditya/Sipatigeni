package com.trekkon.patigeni.constants;

/**
 * Created by Primananda on 7/6/2017.
 */

public class BaseCode {

    public static final boolean BOOL_LOGIN_RESULT = true;
    public static boolean BOOL_UPLOAD_BERHASIL = true;
    public static String TOKEN_UPLOAD_AUTHORIZATION = "token";

    public static final String PROGRESS_TITLE = "Sedang memproses...";
    public static final int GPS_TIME_OUT = 3000;
    public static final String STATUS_OK = "OK";
    private  int FIRST_TIME_RUN = 0;

    public int setFirstRun(){
        return FIRST_TIME_RUN + 1;
    }

    public int getFIRST_TIME_RUN() {
        return FIRST_TIME_RUN;
    }
}
