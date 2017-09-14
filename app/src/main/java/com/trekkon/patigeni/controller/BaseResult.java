package com.trekkon.patigeni.controller;

public interface BaseResult {

    void showValidationError();

    void onSuccess();

    void onError(String errorMessage);

}
