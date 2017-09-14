package com.trekkon.patigeni.services;

/**
 * Created by windupurnomo on 28/06/16.
 */
public class ServiceManager {
    private static ServiceManager instance = new ServiceManager();



    private ServiceManager(){

    }


    public static ServiceManager getInstance(){
        return instance;
    }


}
