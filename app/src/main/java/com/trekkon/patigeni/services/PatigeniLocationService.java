package com.trekkon.patigeni.services;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.trekkon.patigeni.R;
import com.trekkon.patigeni.activities.MainActivity;
import com.trekkon.patigeni.activities.MapsActivity;
import com.trekkon.patigeni.dao.JourneyDao;
import com.trekkon.patigeni.helper.BaseFunction;
import com.trekkon.patigeni.model.LocationModel;
import com.trekkon.patigeni.utils.PatigeniLocation;
import com.trekkon.patigeni.utils.SessionManagement;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PatigeniLocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
	private static final String TAG = PatigeniLocationService.class.getSimpleName();
	public static final String BROADCAST_INTERVAL = "broadcast_interval";
	public static final Integer BROADCAST_INTERVAL_DEFAULT = 1;
	protected static final int SECONDS = 1000;
	protected static final int MINUTES = 5 * SECONDS;
	protected static final int FASTEST_INTERVAL = 10 * SECONDS;
	protected LocationRequest locationRequest = null;
	//	protected LocationClient locationClient = null;
	protected GoogleApiClient mGoogleClient;
	protected PatigeniLocation patigeniLocation;
	LocationManager locationManager;
	private Location lastLocation;
	JourneyDao journeyDao;
	String addressString = "", action, fbId;

	double latitude, lastLatitude; // latitude
	double longitude, lastLongitude; // longitude
	float accuracy = 0.f;
	float distance = 0.f;
	float distance1 = 0.f;
	double theSpeed = 0;
	long gTime;
	int FORE_ID = 1335;
	Notification notif;
	PendingIntent pIntent;
	//	private LocationClient lc;
	boolean paused, isGPSEnabled = false, isNetworkEnabled = false;
	Location location = null; // location
	int routeNoToBePinned;
	boolean isUpdatePinned = false, isPinnedDown = false;

	private static final float MIN_LAST_READ_ACCURACY = 500.0f;
	private Location mBestReading;
	private static final long ONE_MIN = 1000 * 60;
	private static final long TWO_MIN = ONE_MIN * 2;
	private static final long FIVE_MIN = ONE_MIN * 5;
	String userId;
	int hotspotId;
	SessionManagement session;
	MapsActivity mAct;
	double dDistance;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "onCreate");
		userId = BaseFunction.GetUserID(this);
		this.patigeniLocation = new PatigeniLocation(this);
		this.lastLocation= new Location("");

		this.locationRequest = new LocationRequest();
		if (hasFineLocationPermission())
			this.locationRequest
					.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		else if (hasCoarseLocationPermission())
			this.locationRequest
					.setPriority(LocationRequest.PRIORITY_LOW_POWER);
		else
			this.locationRequest.setPriority(LocationRequest.PRIORITY_NO_POWER);

		this.locationRequest.setFastestInterval(FASTEST_INTERVAL);
		this.locationRequest.setInterval(FASTEST_INTERVAL);
//		this.locationClient = new LocationClient(this, this, this);
		mGoogleClient = new GoogleApiClient.Builder(this)
				.addApi(LocationServices.API)
				.addApi(Places.GEO_DATA_API)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.build();
		session = new SessionManagement(this);
		mAct = new MapsActivity();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(TAG, "onStartCommand");

		if (!hasFineLocationPermission()
				&& !hasCoarseLocationPermission()
				|| GooglePlayServicesUtil.isGooglePlayServicesAvailable(this) != ConnectionResult.SUCCESS) {
			Log.e(TAG, getString(R.string.gpsServiceStartError));
			this.stopSelf();
			return START_STICKY;
		}

		if (intent != null) {
			if (this.mGoogleClient.isConnected() || this.mGoogleClient.isConnecting()) {
				this.mGoogleClient.disconnect();
			}
			this.locationRequest.setInterval(5000);
			this.mGoogleClient.connect();
		}
		Intent noty_intent = new Intent(this, MapsActivity.class);
		noty_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		pIntent = PendingIntent.getActivity(this, 0, noty_intent, 0);
		notif = new Notification.Builder(getApplicationContext())
				.setContentTitle("Trip ke Titik Api")
				.setContentText(addressString).setSmallIcon(R.mipmap.walk)
				.setContentIntent(pIntent).setAutoCancel(true).setOngoing(true)
				.build();

		startForeground(FORE_ID, notif);
		journeyDao = new JourneyDao(getBaseContext());
		return START_STICKY;
	}

	@Override
	public void onDestroy() {

		if (this.patigeniLocation != null) {
			this.patigeniLocation.stop();
		}

		if (this.mGoogleClient.isConnected()) {
			//			locationManager.removeUpdates(listener);
			//			 LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleClient, locationRequest);
		}
		if (locationManager != null) {
			locationManager.removeUpdates((android.location.LocationListener) this);
			locationManager = null;
		}

		this.mGoogleClient.disconnect();
		super.onDestroy();

	}

	// Google Play Location Services

	@Override
	public void onConnected(Bundle dataBundle) {

		Log.i(TAG, "Play: onConnected");
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			// TODO: Consider calling
			return;
		}
		LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleClient, locationRequest, this);


		if (lastLocation != null) {
			Log.i("Crrnt position services", String.valueOf(lastLocation.getLatitude()) + "," + String.valueOf(lastLocation.getLongitude()));
			//			Toast.makeText(this, "Current position services : " + String.valueOf(lastLocation.getLatitude())+","+String.valueOf(lastLocation.getLongitude()) , Toast.LENGTH_SHORT).show();
		} else {
			lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleClient);
//			Log.i("Crrnt last position", String.valueOf(lastLocation.getLatitude()) + "," + String.valueOf(lastLocation.getLongitude()));
		}
	}

	@Override
	public void onConnectionSuspended(int i) {

	}


	@Override
	public void onConnectionFailed(ConnectionResult result) {
		Log.d(TAG, "Play: onConnectionFailed");
	}


	private boolean hasFineLocationPermission() {
		return this
				.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == PackageManager.PERMISSION_GRANTED;
	}

	private boolean hasCoarseLocationPermission() {
		return this
				.checkCallingOrSelfPermission("android.permission.ACCESS_COARSE_LOCATION") == PackageManager.PERMISSION_GRANTED;
	}

	@Override
	public void onLocationChanged(Location location) {
		Log.i("LOCATION", "My lat: " + location.getLatitude() + "\n" + "My lo n: " + location.getLongitude());
		Log.i(TAG, "Play: onLocationChanged");

		/*if (this.patigeniLocation != null) {
			this.patigeniLocation.OnLocationChanged(location);
		}*/

		LocationModel lastLocationModel;
		try {
			lastLocationModel = journeyDao.getJourneyLastRow();
			if (lastLocationModel != null) {
				Location mLastLocation = new Location("");
				mLastLocation.setLatitude(lastLocationModel.getmLatitude());
				mLastLocation.setLongitude(lastLocationModel.getmLongitude());
				theSpeed = Math.sqrt(Math.pow(location.getLongitude() - lastLocationModel.getmLongitude(), 2)
						+ Math.pow(location.getLatitude() - lastLocationModel.getmLatitude(), 2)
				) / (location.getTime() - lastLocationModel.getgTime());
				Log.i("last location", "lat : " + String.valueOf(lastLocationModel.getmLatitude()) + ", long : " + String.valueOf(lastLocationModel.getmLongitude()));
				Log.i("current LOCATION", "My lat: " + location.getLatitude() + "\n" + "My long: " + location.getLongitude());
				Log.i("current speed on service (On Location Changed)", String.valueOf((int) theSpeed));
				dDistance = distance(lastLocationModel.getmLatitude(), lastLocationModel.getmLongitude(), location.getLatitude(), location.getLongitude() );
				Log.i("jarak", String.valueOf(dDistance));
			/*} else {
				dDistance = 6;*/
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Intent noty_intent = new Intent(this, MapsActivity.class);

		noty_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		pIntent = PendingIntent.getActivity(this, 0, noty_intent, 0);


		lastLocation = location;
//		hotspotId = session.getCurrentHotspot();
		hotspotId = mAct.getTaskId();
		Log.i("hotspt ID on #1", String.valueOf(hotspotId));

		if (this.patigeniLocation != null) {
			//         	Toast.makeText(getApplicationContext(),  "mSPeedometer : location cahnegd", Toast.LENGTH_SHORT).show();
			this.patigeniLocation.OnLocationChanged(location);
			latitude = location.getLatitude();
			longitude = location.getLongitude();
			gTime = location.getTime();
			//						distance += (lastLocation.distanceTo(lastLocation) / 1000);
			accuracy = location.getAccuracy();
			Geocoder gc = new Geocoder(getBaseContext(), Locale.getDefault());
			try {
				List<Address> addresses = gc.getFromLocation(latitude, longitude, 1);
				StringBuilder sb = new StringBuilder();
				if (addresses.size() > 0) {
					Address address = addresses.get(0);
					for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
						if (address.getAddressLine(i) != null) {
							sb.append(address.getAddressLine(i)).append("\n");
						}
					}

					if (address.getLocality() != null) {
						sb.append(address.getLocality()).append("\n");
					}

					if (address.getPostalCode() != null) {
						sb.append(address.getPostalCode()).append("\n");
					}

					if (address.getCountryName() != null) {
						sb.append(address.getCountryName());
					}
					addressString = sb.toString();
				} else {
					addressString = String.valueOf((String.valueOf(longitude) + " , " + String.valueOf(latitude)));
				}

			} catch (IOException e) {
//				Log.i("error on sbbuilder", e.toString());
			}

			if (addressString.equals("")) {
				addressString = String.valueOf((String.valueOf(longitude) + " , " + String.valueOf(latitude)));
			}
			notif = new Notification.Builder(getApplicationContext())
					.setContentTitle("Trip ke Titik Api")
					.setContentText(addressString).setSmallIcon(R.mipmap.walk)
					.setContentIntent(pIntent).setAutoCancel(true).setOngoing(true)
					.build();

			startForeground(FORE_ID, notif);
			Toast.makeText(getApplicationContext(),  "accuracy = " + String.valueOf(accuracy), Toast.LENGTH_SHORT).show();
			if ((int) latitude != 0) {
//				if(dDistance > 5) {
					Toast.makeText(getApplicationContext(), "distance = " + String.valueOf(dDistance), Toast.LENGTH_SHORT).show();
				if (accuracy < 16){
					saveCoordinate(hotspotId, latitude, longitude, accuracy);
				}

//				}

			}
		}
	}

	private void saveCoordinate(int hotspotId, double latitude, double longitude, double accuracy) {
		LatLng locBefore, locCurrent;
		int routeNo = 0;
		double mRelativeDistance;
		Location curLocation;
		String journeyId;

       /* try {
            action = journeyDao.countJourney(userId);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/

		Calendar cal = Calendar.getInstance();
		SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateandTime = df.format(Calendar.getInstance().getTime());
		String currentDateandTimeS = dfs.format(Calendar.getInstance().getTime());

		ContentValues cVal = new ContentValues();
		try {


			//	if (distance > 30){
			try {
				routeNo = journeyDao.getMaxRouteNo(hotspotId) + 1;
			} catch (Exception e) {

				e.printStackTrace();
			}
			cVal.put("hotspot_id", hotspotId);
			cVal.put("route_no", routeNo);
			cVal.put("latitude", latitude);
			cVal.put("longitude", longitude);
			cVal.put("accuracy", accuracy);
			cVal.put("distance", dDistance);
			cVal.put("journey_timestamp", currentDateandTime);
			try {
				journeyDao.insert("Task_journey", cVal);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	private double distance(double lat1, double lon1, double lat2, double lon2) {
		double theta = lon1 - lon2;
		double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		dist = (dist * 1.609344)/1000;
		/*if (unit == "K") {
			dist = dist * 1.609344;
		} else if (unit == "N") {
			dist = dist * 0.8684;
		}*/
		return (dist);
	}


	private double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}


	private double rad2deg(double rad) {
		return (rad * 180.0 / Math.PI);
	}


}
