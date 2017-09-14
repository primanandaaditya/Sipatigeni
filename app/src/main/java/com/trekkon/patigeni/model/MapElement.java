package com.trekkon.patigeni.model;

import com.google.android.gms.maps.model.LatLng;

public abstract class MapElement {
	public LatLng centerPoint;

	public MapElement(LatLng p) {
		centerPoint = p;
	}
}