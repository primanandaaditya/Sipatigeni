package com.trekkon.patigeni.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.johnpersano.supertoasts.library.Style;
import com.github.johnpersano.supertoasts.library.SuperActivityToast;
import com.github.johnpersano.supertoasts.library.utils.PaletteUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.trekkon.patigeni.PatigeniApp;
import com.trekkon.patigeni.R;
import com.trekkon.patigeni.activities.DaftarTitikActivity;
import com.trekkon.patigeni.activities.MainActivity;
import com.trekkon.patigeni.activities.NotificationActivity;
import com.trekkon.patigeni.activities.SuperActivity;
import com.trekkon.patigeni.dao.JourneyDao;
import com.trekkon.patigeni.helper.BaseFunction;
import com.trekkon.patigeni.utils.CommonUtils;

import java.util.Map;


/**
 * Created by iwan on 7/27/17.
 */
public class PatigeniMessagingService extends FirebaseMessagingService {
    private static final String MESSAGE_TYPE = "messageType";
    private static final String CONTENT_TYPE = "contentType";
    private static final String TAG = PatigeniInstanceIDService.class.getName();
    private static final int MY_NOTIFICATION_ID = 0;
    Context ctx;
    SuperActivity activity;
    Handler mHandler;
    JourneyDao journeyDao;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        journeyDao = new JourneyDao(getApplicationContext());
        String title, body;
        Log.i(TAG, "From: " + remoteMessage.getFrom());
        Log.i(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
//        sendNotification(remoteMessage.getNotification().getBody());
        Bundle bundle = new Bundle();
        final Map map = remoteMessage.getData();
        Log.i("remoteMessage", map.toString());
        if (map != null) {
            String doAction = map.get("action").toString();

            Log.i("doAction", doAction);
            if (doAction != null) {

                if(doAction.contains("delete")){
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.putExtra("action", doAction);
                    i.putExtra("taskId", map.get("taskId").toString());
                    i.putExtra("userId", map.get("userId").toString());
                    i.putExtra("userName", map.get("userName").toString());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(i);
                    /*String userId = map.get("userId").toString();
                    if (userId != BaseFunction.GetUserID(getApplicationContext())){
                        try {
                            journeyDao.delete("Task", "cast(hotspot_id as varchar) = ? ", new String[]{map.get("taskId").toString()});
                            ToastUtil(getApplicationContext(), "Titik Api " + map.get("taskId").toString() + "telah diambil oleh " + map.get("userName").toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }*/
                } else {
                    Intent i = new Intent(getApplicationContext(), NotificationActivity.class);
                    i.putExtra("action", doAction);
                    i.putExtra("taskId", map.get("taskId").toString());
                    i.putExtra("latitude", map.get("latitude").toString());
                    i.putExtra("longitude", map.get("longitude").toString());
                    i.putExtra("timestamp", map.get("timestamp").toString());
                    i.putExtra("tingkatKepercayaan", map.get("tingkatKepercayaan").toString());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(i);
                }

            }
            /*bundle.putString("taskId", map.get("taskId").toString());
            bundle.putString("latitude", map.get("latitude").toString());
            bundle.putString("longitude", map.get("longitude").toString());
            bundle.putString("description", map.get("description").toString());
            bundle.putString("action", map.get("action").toString());
            bundle.putString("timestamp", map.get("timestamp").toString());
            bundle.putString("tingkatKepercayaan", map.get("tingkatKepercayaan").toString());*/
//            sendNotification(title, body, bundle);


        }


       /* if (bundle != null){

        }*/


    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
   /* private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 *//* Request code *//*, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .setContentTitle("FCM Message")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0  *//*ID of notification *//*, notificationBuilder.build());
    }*/
    private void sendNotification(String message, String messageBody, Bundle bundle) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, MY_NOTIFICATION_ID /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.fire48)
                .setContentTitle(message)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(MY_NOTIFICATION_ID /* ID of notification */, notificationBuilder.build());

    }

    public static void showDialog(final Context context, String textTitle, String textContent, String textPositive,
                                  String textNegative, final int dialogTYpe){
        new MaterialDialog.Builder(PatigeniApp.getContext())
                .title(textTitle)
                .content(textContent)
                .positiveText(textPositive)
                .negativeText(textNegative)

                /*.onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })*/
                .show();
    }

    public static void ToastUtil(Context mContext, String toastText){
        /*SuperActivityToast superActivityToast;
        superActivityToast = new SuperActivityToast(mContext, new Style(), Style.TYPE_STANDARD);*/


        SuperActivityToast.create(mContext, new Style(), Style.TYPE_STANDARD)

                .setProgressBarColor(Color.WHITE)
                .setText(toastText)
                .setDuration(Style.DURATION_VERY_LONG)
                .setFrame(Style.FRAME_LOLLIPOP)
                .setColor(PaletteUtils.getSolidColor(PaletteUtils.MATERIAL_GREEN))
                .setTextSize(Style.TEXTSIZE_MEDIUM)
//                .setIconResource(R.drawable.ic_notifications_black_24dp)
                .setGravity(Gravity.TOP)

                .setAnimations(Style.ANIMATIONS_FADE).show();
    }



}

