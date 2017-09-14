package com.trekkon.patigeni.listener;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


/**
 * Created by iwan on 27/07/17.
 */
public class FirebaseInstanceListener extends FirebaseInstanceIdService {

    private static final String TAG = FirebaseInstanceListener.class.getSimpleName();

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i(TAG, "Refreshed token: " + refreshedToken);
        /*try {
            FirebaseUtils.sendRegistrationToServer(this, refreshedToken);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
    // [END refresh_token]
}
