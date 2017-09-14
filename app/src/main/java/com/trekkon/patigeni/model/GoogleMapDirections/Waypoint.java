package com.trekkon.patigeni.model.GoogleMapDirections;

import com.google.android.gms.maps.model.LatLng;
import com.trekkon.patigeni.model.MapElement;

public class Waypoint extends MapElement {
	public Leg legA;
	public Leg legB;

	public Waypoint(Leg legA, Leg legB, LatLng p) {
		super(p);
		this.legA = legA;
		this.legB = legB;
	}

	public Waypoint(LatLng p) {
		super(p);
	}
}