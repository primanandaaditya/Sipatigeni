package com.trekkon.patigeni.model;

/**
 * Created by iwan 27/07/17.
 */
public class Enums {
    public enum SP {
        USER,
        TOKEN,
        TOKEN_EXP,
        OS_VERSION,
        FIREBASE_ID,
        VERSION_NAME,
        VERSION_CODE,
        DEVICE_TOKEN,
    }


    public enum FRAGMENT_PAGE {
        LOGIN,
        SAFETY,
        NAVIGATION,
        EMERGENCY_CALL_GROUP,
        EMERGENCY_CALL_DEALER,
        EMERGENCY_CALL_POLICE,
        EMERGENCY_CALL_AMBULANCE,
        EMERGENCY_CALL_INSURANCE,
        EMERGENCY_CALL_OTHERS,
        DEALER_APPOINTMENT,
        GAS_STATION_MAP,
        FAVOURITE_PLACE,
        CAR_LOG,
        CAR_PROFILE,
        DRIVING_TIPS,
        USER_PROFILE,
        MAINTENANCE,
        SERVICE_HISTORY

    }

    public enum BUNDLE_KEY{
        PAGE_FRAGMENT, // destination fragment on ContainerActivity
        MAIN_FRAGMENT, // destination fragment on LoginActivity
        ORIGIN_FRAGMENT,
        SELECTED_POI,
        KEYWORD
    }
}