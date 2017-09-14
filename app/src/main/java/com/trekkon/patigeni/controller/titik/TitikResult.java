package com.trekkon.patigeni.controller.titik;


import com.trekkon.patigeni.model.TitikApiModel;

/**
 * Created by Primananda on 7/22/2017.
 */

public interface TitikResult {
    void showValidationError();

    void onSuccess(TitikApiModel titikApiModel);

    void onError(String errMessage);
}
