package com.trekkon.patigeni.helper;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.trekkon.patigeni.utils.CommonUtils;

/**
 * Created by Iwan N on 18/08/17.
 */
public class MessageHelper {
    public static void show(Context context, String message){
        CommonUtils.ToastUtil(context, message);
//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showOnThread(final Context context, final String message){
        Activity activity = (Activity) context;
        if (activity != null && !activity.isFinishing()) {
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    MessageHelper.show(context, message);
                }
            });
        }
    }
}
