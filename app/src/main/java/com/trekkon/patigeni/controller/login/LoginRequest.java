package com.trekkon.patigeni.controller.login;

/**
 * Created by Primananda on 7/6/2017.
 */
public interface LoginRequest {

    void login(String email, String password, String tokenId);

}
