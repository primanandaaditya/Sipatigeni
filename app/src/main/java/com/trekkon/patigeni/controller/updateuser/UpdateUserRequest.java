package com.trekkon.patigeni.controller.updateuser;

/**
 * Created by Trekkonz on 08/09/2017.
 */

public interface UpdateUserRequest {
    void updateUser(String iduser, String name, String password, String email);
}
