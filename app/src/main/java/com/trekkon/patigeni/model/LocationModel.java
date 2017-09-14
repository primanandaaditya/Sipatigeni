package com.trekkon.patigeni.model;

import java.io.Serializable;

public class LocationModel implements Serializable {

	private static final long serialVersionUID = 1L;

	protected float mDistanceTraveled;

	public LocationModel(float distanceTraveled) {
		this.mDistanceTraveled = distanceTraveled;
	}


	protected double mLongitude;
	protected double mLatitude;
	protected double mAccuracy;
	protected String mJourneyId;
	protected int mRouteNo;
	protected int gTime;
	protected String mDateTime;

	public LocationModel() {

	}

	public LocationModel(float distanceTraveled, double longitude, double latitude, String journeyId,  double accuracy) {
		this.mDistanceTraveled = distanceTraveled;
		this.mLongitude = longitude;
		this.mLatitude = latitude;

		this.mJourneyId = journeyId;
		this.mAccuracy = accuracy;

	}

	public void updateModel(float distanceTraveled) {
		this.mDistanceTraveled = distanceTraveled;
	}

	public float getmDistanceTraveled() {
		return mDistanceTraveled;
	}

	public void setmDistanceTraveled(float mDistanceTraveled) {
		this.mDistanceTraveled = mDistanceTraveled;
	}

	public double getmLongitude() {
		return mLongitude;
	}

	public void setmLongitude(double mLongitude) {
		this.mLongitude = mLongitude;
	}

	public double getmLatitude() {
		return mLatitude;
	}

	public void setmLatitude(double mLatitude) {
		this.mLatitude = mLatitude;
	}

	public int getmRouteNo() {
		return mRouteNo;
	}

	public void setmRouteNo(int mRouteNo) {
		this.mRouteNo = mRouteNo;
	}

	public String getmJourneyId() {
		return mJourneyId;
	}

	public void setmJourneyId(String mJourneyId) {
		this.mJourneyId = mJourneyId;
	}

	public double getmAccuracy() {
		return mAccuracy;
	}

	public void setmAccuracy(double mAccuracy) {
		this.mAccuracy = mAccuracy;
	}

	public String getmDateTime() {
		return mDateTime;
	}

	public void setmDateTime(String mDateTime) {
		this.mDateTime = mDateTime;
	}

	public int getgTime() {
		return gTime;
	}

	public void setgTime(int gTime) {
		this.gTime = gTime;
	}
}
