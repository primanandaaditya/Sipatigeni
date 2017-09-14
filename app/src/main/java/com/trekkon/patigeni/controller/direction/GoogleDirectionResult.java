package com.trekkon.patigeni.controller.direction;

import com.trekkon.patigeni.model.GoogleMapDirections.GoogleMapDirectionModel;

/**
 * Created by Trekkonz on 8/16/2017.
 */

public interface GoogleDirectionResult {

    void showValidationError();

    void onSuccess(GoogleMapDirectionModel googleMapDirectionModel);

    void onError();
}
