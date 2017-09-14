package com.trekkon.patigeni.model;

import com.google.android.gms.maps.model.LatLng;

public class Waypoint extends MapElement {
	public LegMap legMapA;
	public LegMap legMapB;

	public Waypoint(LegMap legMapA, LegMap legMapB, LatLng p) {
		super(p);
		this.legMapA = legMapA;
		this.legMapB = legMapB;
	}

	public Waypoint(LatLng p) {
		super(p);
	}
}