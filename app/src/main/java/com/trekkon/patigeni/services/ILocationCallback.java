package com.trekkon.patigeni.services;

import android.location.Location;

import com.trekkon.patigeni.model.LocationModel;

public interface ILocationCallback {
	
	/**
	 * Call back on location changed
	 * 
	 * @param location
	 */
	void OnLocationChanged(Location location);
	
	/**
	 * Call back on data changed
	 * 
	 * @param model
	 */
	void OnDataChanged(LocationModel model);

}
