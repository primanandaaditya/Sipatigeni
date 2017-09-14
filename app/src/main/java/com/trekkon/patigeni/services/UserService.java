package com.trekkon.patigeni.services;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;

import java.io.IOException;
import java.util.Date;


/**
 * Created by iwan on 27/07/17
 */
public interface UserService {
    Call login(String email, String password, Callback callback) throws IOException;
    Call facebookLogin(String fbId, String accessToken, String email,
                       String firstName, String lastName, String gender, Date birthDate, Callback callback) throws IOException;
    Call register(String email, String firstName, String lastName, Callback callback) throws IOException;
    Call registerDevice(String deviceToken, Callback callback);
    Call getCurrentUser(Callback callback);

}
