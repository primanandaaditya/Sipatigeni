package com.trekkon.patigeni.model;

import com.google.android.gms.maps.model.LatLng;

public class Event {
	public int type;
	public LatLng position;

	public Event (int type, LatLng pos) {
		position = pos;
		this.type = type;
	}
}