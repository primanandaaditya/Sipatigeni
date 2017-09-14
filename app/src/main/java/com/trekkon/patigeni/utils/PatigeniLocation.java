package com.trekkon.patigeni.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.trekkon.patigeni.helper.BaseFunction;
import com.trekkon.patigeni.model.LocationModel;
import com.trekkon.patigeni.services.BroadcastAction;


/**
 * This class responsible to get the location changed and calculate the distance
 * traveled
 * 
 * @author nikmuhammad
 * 
 */

public class PatigeniLocation {

	public static final String TAG = PatigeniLocation.class.getSimpleName();

	protected Context mContext;

	protected boolean mIsRunning = false;
	protected boolean mWaitForGpsLock = true;

	protected float mDistanceTraveled = 0F;

	protected Location mCurrentLocation;
	protected double mLongitude;
	protected double mLatitude;

	protected double mAccuracy;
	protected String mJourneyId;
	protected int mRouteNo;

	protected GpsInReceiver mReceiver;

	// Constructor
	public PatigeniLocation(Context context) {

		this.mContext = context;
		this.mReceiver = new GpsInReceiver();
		this.mContext.registerReceiver(this.mReceiver, new IntentFilter(
				BroadcastAction.ACTION_IN));

	}

	public void stop() {
		this.mContext.unregisterReceiver(this.mReceiver);
	}

	public void OnLocationChanged(Location location) {

		// calculate distance traveled
		if (mIsRunning) {
			if (mCurrentLocation != null) {
				mDistanceTraveled += (location.distanceTo(mCurrentLocation) / 1000);
				mAccuracy = location.getAccuracy();
				mLongitude = location.getLongitude();
				mLatitude = location.getLatitude();
				mJourneyId = BaseFunction.GetUserID(mContext);
				Log.i("mDistanceTraveled", String.valueOf(mDistanceTraveled));
			}

			// store previous location
			mCurrentLocation = location;

			if (mWaitForGpsLock) {
				Log.d(TAG, "GPS locked");
				mWaitForGpsLock = false;
				startSpeedoMeter();
			}

			broadCast();
		}
	}

	// execute the action for start button
	private void startSpeedoMeter() {
		if (this.mIsRunning) {
			// do something
		}
	}

	// execute the action for stop button
	private void stopSpeedoMeter() {
		if (!this.mIsRunning) {
			// do something
			
			this.broadCast();
		}
	}

	private void broadCast() {
		LocationModel mModel = new LocationModel(this.mDistanceTraveled, this.mLongitude, this.mLatitude, this.mJourneyId,  this.mAccuracy);


		Intent intent = new Intent();
		intent.setAction(BroadcastAction.ACTION_OUT);
		intent.putExtra(BroadcastAction.EXTRA_SPEED_STATE, mIsRunning);
		intent.putExtra(BroadcastAction.EXTRA_MODEL, mModel);
		Log.i("broadcast", "broadcast running " + String.valueOf(this.mLongitude));
		this.mContext.sendBroadcast(intent);
	}

	class GpsInReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle extra = intent.getExtras();
			if (extra != null) {
				boolean state = extra
						.getBoolean(BroadcastAction.EXTRA_SPEED_STATE);
				Log.i(TAG, "Start SpeedoMeter: " + mIsRunning);

				if (state != mIsRunning) {
					mIsRunning = state;
					if (mIsRunning) {
						startSpeedoMeter();
					} else {
						stopSpeedoMeter();
					}
				}
			}

		}

	}

}
