package com.trekkon.patigeni.services;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by iwan on 7/27/17.
 */
public class PatigeniInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = PatigeniInstanceIDService.class.getName();
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */

    @Override
    public void onTokenRefresh() {
        Intent intent = new Intent("tokenReceiver");
        // Get updated InstanceID token.
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        try {

            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            Log.i("Firbase id login", "Refreshed token: " + refreshedToken);
            Log.i(TAG, "Refreshed token: " + refreshedToken);
            final LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
            intent.putExtra("token",refreshedToken);
            broadcastManager.sendBroadcast(intent);
            // TODO: Implement this method to send any registration to your app's servers.
            sendRegistrationToServer(refreshedToken);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Send push token to server
     * @param token
     */
    private void sendRegistrationToServer(String token) {

    }


}
