package com.trekkon.patigeni.services;

import android.os.Build;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.RequestBody;
import com.trekkon.patigeni.utils.AppConfig;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Created by Iwan nmo on 28/07/17.
 */
public class UserServiceBean extends BaseService implements UserService {
    private static final String TAG = UserServiceBean.class.getSimpleName();

    @Override
    public Call login(String email, String password, Callback callback) throws IOException {
        String url =getServiceUrl("/auth/login");
        RequestBody body = new FormEncodingBuilder()
                .add("username", email)
                .add("password", password)
                .build();
        Call call = httpUtil.post(url, body);
        call.enqueue(callback);
        return  call;
    }

    @Override
    public Call facebookLogin(String fbId, String accessToken, String email,
                              String firstName, String lastName, String gender, Date birthDate, Callback callback) throws IOException {
        String url =getServiceUrl("/auth/fbLogin");
        RequestBody body = new FormEncodingBuilder()
                .add("fbId", fbId)
                .add("accessToken", accessToken)
                .add("email", email)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .add("birthday", String.valueOf(birthDate.getTime()))
                .add("gender", gender)
                .build();
        Call call = httpUtil.post(url, body);
        call.enqueue(callback);
        return  call;
    }

    @Override
    public Call register(String email, String firstName, String lastName, Callback callback) throws IOException {
        String url = getServiceUrl("/user/profile");
        RequestBody body = new FormEncodingBuilder()
                .add("email", email)
                .add("firstName", firstName)
                .add("lastName", lastName)
                .build();
        Call call = httpUtil.post(url, body);
        call.enqueue(callback);
        return  call;
    }

    @Override
    public Call registerDevice(String deviceToken, Callback callback) {
        String url = getServiceUrl("/user/device");
        RequestBody body = new FormEncodingBuilder()
                .add("uid", AppConfig.instance.getDeviceId())
                .add("deviceToken", deviceToken)
                .add("appID", String.valueOf(AppConfig.instance.getAppCode()))
                .add("appVersion", AppConfig.instance.getAppName())
                .add("osPlatform", "Android")
                .add("osVersion", Build.VERSION.RELEASE)
                .add("model", Build.MODEL)
                .add("manufacturer", Build.MANUFACTURER)
                .add("locale", Locale.getDefault().getLanguage())
                .add("timeZone", String.valueOf(TimeZone.getDefault()))
                .build();
        Call call = httpUtil.post(url, body);
        call.enqueue(callback);
        return  call;
    }

    @Override
    public Call getCurrentUser(Callback callback) {
        String url =getServiceUrl("/user/profile?uoid=" + AppConfig.instance.getCurrentUser().getName());
        Call call = httpUtil.get(url);
        call.enqueue(callback);
        return  call;
    }

   /* @Override
    public Call saveUser(User user, Callback callback) {
        String url = getServiceUrl("/user/profile");
        RequestBody body = new FormEncodingBuilder()
                .add("uoid", user.getOid())
                .add("email", user.getEmail())
                .add("phone", user.getPhone())
                .add("address", user.getAddress())
                .add("firstName", user.getFirstName())
                .add("lastName", user.getLastName())
                .build();
        Call call = httpUtil.put(url, body);
        call.enqueue(callback);
        return  call;
    }*/
}
