package com.trekkon.patigeni.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;

import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.util.DirectionConverter;

import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.trekkon.patigeni.R;
import com.trekkon.patigeni.constants.BaseCode;
import com.trekkon.patigeni.controller.BaseResult;
import com.trekkon.patigeni.controller.direction.GoogleDirectionResult;
import com.trekkon.patigeni.controller.updatestatus.UpdateStatusController;
import com.trekkon.patigeni.dao.JourneyDao;
import com.trekkon.patigeni.helper.DatabaseHandler;
import com.trekkon.patigeni.helper.SubmitGambar;
import com.trekkon.patigeni.model.Event;
import com.trekkon.patigeni.model.GambarModel;
import com.trekkon.patigeni.model.GoogleMapDirections.GoogleMapDirectionModel;
import com.trekkon.patigeni.model.GoogleMapDirections.Route;
import com.trekkon.patigeni.helper.BaseFunction;
import com.trekkon.patigeni.constants.URL;
import com.trekkon.patigeni.model.LegMap;
import com.trekkon.patigeni.model.LocationModel;
import com.trekkon.patigeni.model.LocationMonitor;
import com.trekkon.patigeni.model.MapDataManager;
import com.trekkon.patigeni.model.Titik;
import com.trekkon.patigeni.model.Trip;
import com.trekkon.patigeni.model.Waypoint;
import com.trekkon.patigeni.services.ConnectionService;
import com.trekkon.patigeni.services.ILocationCallback;
import com.trekkon.patigeni.services.LocationLibrary;
import com.trekkon.patigeni.utils.CommonUtils;
import com.trekkon.patigeni.utils.SessionManagement;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;


public class MapsActivity extends AbstractRuntimeActivity implements OnMapReadyCallback, GoogleDirectionResult, BaseResult,
        ILocationCallback, GoogleMap.OnInfoWindowClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,
        ConnectionService.ConnectionServiceListener {

    private static final int MY_REQUEST_CODE =5 ;
    UpdateStatusController updateStatusController;
    protected static MapDataManager mdm;
    private GoogleMap mMap;

    File hasilKamera;

    Button btnAmbilFoto;
    Button btnRotate;
    LatLng pertama, kedua;
    String mediaPath;
    //LatLng ketiga, keempat;
    Marker m1, m2;
    //Spinner spTipe;
    String i_lat, i_long, i_idnotif;
    ProgressDialog progressDialog;
    TextView tvJarak, tvDurasi;
    Bundle extras;
    Double latitude, longitude, akurasi;
    static int taskId = 0;
    private Location currentLoc;
    GoogleApiClient mGoogleClient;
    boolean paused = false, isConnected;
    private LocationMonitor lm;
    ArrayList<Polyline> lines;
    private Semaphore lock;
    private LegMap selectedLegMap, lastLegMap;
    private boolean needRefresh;
    protected LocationLibrary mLocationLibrary;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    JourneyDao journeyDao;
    ArrayList<Trip> trip;
    int routeNoAsExtras;
    CommonUtils util;
    TextView barTitle;
    private int actionButton = 1;
    boolean isStarted = false;
    private FloatingActionButton fabCamera;
    private FloatingActionButton fabGallery;
    final static int TAKE_CAMERA = 1;
    final static int TAKE_GALLERY = 2;
    LocationManager manager=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        requestPermission();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(MapsActivity.this);
        editor = prefs.edit();
        journeyDao = new JourneyDao(MapsActivity.this);
        util = new CommonUtils(MapsActivity.this);
        extras = getIntent().getExtras();
        if (extras != null) {
            taskId = extras.getInt("taskId");
            latitude = extras.getDouble("latitude");
            longitude = extras.getDouble("longitude");
            editor.putInt("ongoingTaskId", taskId);
            editor.putFloat("ongoingLatitude", Float.parseFloat(String.valueOf(latitude)));
            editor.putFloat("ongoingLongitude", Float.parseFloat(String.valueOf(longitude)));
            editor.commit();
            setTaskId(taskId);
        } else {
            taskId = prefs.getInt("ongoingTaskId", 0);
            latitude = Double.parseDouble(String.valueOf(prefs.getFloat("ongoingLatitude", 0)));
            longitude = Double.parseDouble(String.valueOf(prefs.getFloat("ongoingLongitude", 0)));
            Log.i("taskId on Pref", String.valueOf(taskId));
            setTaskId(taskId);
        }


        Double my_lat = BaseFunction.GetMyLat(MapsActivity.this);
        Double my_long = BaseFunction.GetMyLong(MapsActivity.this);
        pertama = new LatLng(my_lat, my_long);
        kedua = new LatLng(latitude, longitude);
        BaseFunction.PeriksaGPS(MapsActivity.this);
        updateStatusController = new UpdateStatusController(this, MapsActivity.this);
        paused = false;
        needRefresh = false;
        lines = new ArrayList<Polyline>();
        lock = new Semaphore(1);


        mGoogleClient = new GoogleApiClient
                .Builder(MapsActivity.this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        FindID();
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            Toast.makeText(MapsActivity.this, "No GPS available", Toast.LENGTH_SHORT).show();
            buildAlertMessageNoGps();
        }
        if (mdm == null) {
            mdm = new MapDataManager();
        }

        lm = new LocationMonitor(MapsActivity.this, mdm);
//        updatePosition();
    }

    private void buildAlertMessageNoGps() {

        new MaterialDialog.Builder(this)
                .title("Peringatan")
                .content("GPS tidak aktif.\nAktifkan?")
                .positiveText("Ya")
                .negativeText("Tidak")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        CommonUtils.ToastUtil(MapsActivity.this, "Anda Tidak Bisa Melakukan Kunjungan ke Titik Api");
//                        btnAmbilFoto.setEnabled(false);
                        return;
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                    }
                })
                .show();

    }

    @Override
    protected void onStart() {
        mGoogleClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleClient.disconnect();
        super.onStop();
    }


    void GambarJalur() {
        pertama = new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude());
        GoogleDirection.withServerKey(URL.KEY_GM_WEB)
                .from(pertama)
                .to(kedua)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {

                        if (direction.getStatus().equals(RequestResult.OK)) {
                            com.akexorcist.googledirection.model.Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);
                            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();

                            String distance, duration;

                            Info distanceInfo = leg.getDistance();
                            Info durationInfo = leg.getDuration();
                            distance = distanceInfo.getText();
                            duration = durationInfo.getText();

                            LatLngBounds.Builder builder = new LatLngBounds.Builder();

                            builder.include(pertama);
                            builder.include(kedua);
                            LatLngBounds bounds = builder.build();

                            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
                            mMap.animateCamera(cu, new GoogleMap.CancelableCallback() {
                                public void onCancel() {
                                }

                                public void onFinish() {
                                    CameraUpdate zout = CameraUpdateFactory.zoomBy(0);
                                    mMap.animateCamera(zout);
                                }
                            });

                            PolylineOptions po = DirectionConverter.createPolyline(MapsActivity.this, directionPositionList, 5, Color.RED);
                            mMap.addPolyline(po);

                            tvDurasi.setText("Durasi : " + duration);
                            tvJarak.setText("Jarak : " + distance);

                            m1 = mMap.addMarker(new MarkerOptions().position(pertama).title("Anda disini").icon(BitmapDescriptorFactory.fromResource(R.mipmap.walk2)));
                            m2 = mMap.addMarker(new MarkerOptions().position(kedua).title("Lokasi titik api").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                            m1.showInfoWindow();
                            m2.showInfoWindow();

                            updatePosition();

                        } else {
                            Toast.makeText(MapsActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        Toast.makeText(MapsActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void FindID() {

        tvDurasi = (TextView) findViewById(R.id.tvDurasi);
        tvJarak = (TextView) findViewById(R.id.tvJarak);
        barTitle = (TextView) findViewById(R.id.toolbar_title);
        barTitle.setText("Titik Api ID : " + String.valueOf(getTaskId()));
        fabCamera = (FloatingActionButton)findViewById(R.id.fabCamera);
        fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MulaiKamera();
            }
        });

        fabGallery = (FloatingActionButton)findViewById(R.id.fabGallery);
        fabGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFoto();
            }
        });

        btnAmbilFoto = (Button) findViewById(R.id.btnAmbilFoto);
        try {
            Titik titik = journeyDao.getTask(String.valueOf(taskId));
            if (titik.getStatus().equals("X")) {
                actionButton = 2;
                btnAmbilFoto.setText("Selesai");


                mLocationLibrary = new LocationLibrary(MapsActivity.this, new ILocationCallback() {
                    @Override
                    public void OnLocationChanged(Location location) {

                    }

                    @Override
                    public void OnDataChanged(LocationModel model) {

                    }
                });
                lm.addEvent(new Event(LocationMonitor.UN_PAUSE_EVENT, new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude())));
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleClient,(MapsActivity.this));
                paused = false;
                isStarted=true;
                if(mLocationLibrary!=null) {
                    mLocationLibrary.startSpeedo();
                }
            } else if (titik.getStatus().equals("Z")){
                actionButton = 3;
                btnAmbilFoto.setText("Kirim");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        btnAmbilFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            Toast.makeText(MapsActivity.this, "No GPS available", Toast.LENGTH_SHORT).show();
                    buildAlertMessageNoGps();
                    return;
                }
                if (actionButton == 1) {
                    try {
                        boolean belumSelesai = journeyDao.countOngoingTask();
                        if (belumSelesai) {
                            CommonUtils.showDialog(MapsActivity.this, "Perhatian!", "Ada Kunjungan Titik Api yang belum selesai\nSelesaikan dulu!", "OK", "", 1);
                            return;
                        } else {
                            updateStatusController.update(BaseFunction.GetUserID(MapsActivity.this), String.valueOf(getTaskId()));
                            ContentValues cVal = new ContentValues();
                            cVal.put("status", "X");
                            try {
                                journeyDao.update("Task", cVal, "cast(hotspot_id as varchar) = ?", new String[]{String.valueOf(getTaskId())});
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            mLocationLibrary = new LocationLibrary(MapsActivity.this, new ILocationCallback() {
                                @Override
                                public void OnLocationChanged(Location location) {

                                }

                                @Override
                                public void OnDataChanged(LocationModel model) {

                                }
                            });
                            mLocationLibrary.startSpeedo();

                            if (mdm == null) {
                                mdm = new MapDataManager();
                            }
                            lock = new Semaphore(1);
//                            lm = new LocationMonitor(MapsActivity.this, mdm);
                            Thread t = new Thread(lm);
                            t.start();
                            needRefresh = false;
                            lm.addEvent(new Event(LocationMonitor.UPDATE_POS, new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude())));
                            paused = false;

                            lines = new ArrayList<Polyline>();


                            editor.putBoolean("trackme", true);
                            editor.commit();
                            Log.i("trackme value", String.valueOf(prefs.getBoolean("trackme", false)));
                            actionButton = 2;
                            btnAmbilFoto.setText("Selesai");
                            isStarted = true;

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (actionButton == 2) {

//                        CommonUtils.showDialog(MapsActivity.this, "Perhatian!", "Tidak ada foto yang belum dikirim", "OK", "", 1);
                        /*Intent intent = new Intent(MapsActivity.this, DetailGambar.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();*/
                        /*Toast.makeText(MainActivity.this, "Tidak ada foto yang belum dikirim", Toast.LENGTH_SHORT).show();*/
                        new MaterialDialog.Builder(MapsActivity.this)
                                .title("Perhatian")
                                .content("Ingin Selesaikan Trip ini?")
                                .positiveText("Ya")
                                .negativeText("Tidak")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        ContentValues cVal = new ContentValues();
                                        cVal.put("status", "Z");
                                        try {
                                            journeyDao.update("Task", cVal, "cast(hotspot_id as varchar) = ?", new String[]{String.valueOf(getTaskId())});
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        lm.addEvent(new Event(LocationMonitor.KILL_EVENT, new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude())));
//                                        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleClient,(MapsActivity.this));
                                        paused = true;
                                        if(mLocationLibrary!=null) {
                                            mLocationLibrary.stopSpeedo();
                                        }
                                        actionButton = 3;
                                        btnAmbilFoto.setText("Kirim");
                                        isStarted = false;
                                        CommonUtils.ToastUtil(MapsActivity.this, "Trip " + String.valueOf(getTaskId()) + " Telah Selesai");
                                    }
                                })

                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        // TODO
                                    }
                                }).show();



                } else  if(actionButton==3){
                   /* DatabaseHandler databaseHandler = new DatabaseHandler(MapsActivity.this);
                    long jml = databaseHandler.getGambarCount();
                    if (jml == 0) {
                        CommonUtils.showDialog(MapsActivity.this, "Perhatian!", "Tidak ada foto yang belum dikirim", "OK", "", 1);
                    } else {*/


                        Intent intent = new Intent(MapsActivity.this, PendingActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();


                    //kirim gambar
                    //SubmitGambar submitGambar = new SubmitGambar(MapsActivity.this);
                    //submitGambar.Submit();

//                    }
                }

            }
        });
        btnRotate = (Button) findViewById(R.id.btnRotate);
        btnRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String label = "Titik Panas";
                String uriBegin = "geo:" + String.valueOf(BaseFunction.GetMyLat(MapsActivity.this)) + "," + String.valueOf(BaseFunction.GetMyLong(MapsActivity.this));
                //String uriBegin = "geo:" + String.valueOf(BaseFunction.GetMyLat(MapsActivity.this)) + "," + String.valueOf(BaseFunction.GetMyLong(MapsActivity.this)) ;
                String query = kedua.latitude + "," + kedua.longitude + "(" + label + ")";
                String encodedQuery = Uri.encode(query);
                String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
                Uri uri = Uri.parse(uriString);
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                startActivity(intent);*/
                final Intent info = new Intent(MapsActivity.this, MainActivity.class);
                startActivity(info);
                finish();

            }
        });

    }

    @Override
    public void onBackPressed() {
        /*final Intent info = new Intent(CheckOutActivity.this, PjpListCheckOutActivity.class);
        startActivity(info);
        finish();*/
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        progressDialog = new ProgressDialog(MapsActivity.this);
        progressDialog.setTitle("Peta Lokasi");
        progressDialog.setMessage(BaseCode.PROGRESS_TITLE);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(MapsActivity.this);

        if (status == ConnectionResult.SUCCESS) {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        } else {
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
            return;
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (checkConnection()) {
                    GambarJalur();
                } else {
                    updatePosition();
                }

//                updatePosition();
                progressDialog.dismiss();
            }
        }, 4000);   //5 seconds


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);

        btnAmbilFoto.setEnabled(true);
        btnRotate.setEnabled(true);
//        updatePosition();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isStarted) {
            mLocationLibrary = new LocationLibrary(MapsActivity.this, this);
        }

        paused = false;
        setUpMapIfNeeded();
//        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        setUpLocationClientIfNeeded();
        mGoogleClient.connect();
    }

    private void setUpLocationClientIfNeeded() {
        if (mGoogleClient == null) {
            new GoogleApiClient
                    .Builder(MapsActivity.this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;
                }
            });
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
                try {
                    updatePosition();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @Override
    public void showValidationError() {
        Toast.makeText(MapsActivity.this, "Error", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError(String errorMessage) {

    }

    @Override
    public void onSuccess(GoogleMapDirectionModel googleMapDirectionModel) {

        Route route;

        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.geodesic(true);
        polylineOptions.color(Color.BLUE);
        polylineOptions.width(10);

        List<Route> routes = googleMapDirectionModel.getRoutes();


        for (int i = 0; i < routes.size(); i++) {
            route = routes.get(i);
            List<LatLng> latLngs = BaseFunction.decodePoly(route.getOverviewPolyline().getPoints());
            polylineOptions.addAll(latLngs);

        }
        mMap.addPolyline(polylineOptions);


        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(m1.getPosition());
        builder.include(m2.getPosition());
        LatLngBounds bounds = builder.build();

        //sempurnakan kamera dan animasi
        int padding = 10; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.moveCamera(cu);
        mMap.animateCamera(cu);

        //cari jarak dan durasi
        route = routes.get(0);
        String distance_text = route.getLegs().get(0).getDistance().getText() + "l";
        String distance_value = String.valueOf(route.getLegs().get(0).getDistance().getValue());
        tvJarak.setText(distance_text + "/" + distance_value + " meter");
        String duration = route.getLegs().get(0).getDuration().getText();
        tvDurasi.setText(duration);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isStarted) {
            this.mLocationLibrary.onDestroy();
        }

    }

    @Override
    public void onError() {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setFastestInterval(16);
        request.setInterval(5000);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleClient, request, this);

        if (currentLoc != null) {
            Log.i("Current position services", String.valueOf(currentLoc.getLatitude()) + "," + String.valueOf(currentLoc.getLongitude()));
            //			Toast.makeText(this, "Current position services : " + String.valueOf(nowLoc.getLatitude())+","+String.valueOf(nowLoc.getLongitude()) , Toast.LENGTH_SHORT).show();
        } else {
            currentLoc = LocationServices.FusedLocationApi.getLastLocation(mGoogleClient);
//            Log.i("Current last position services", String.valueOf(currentLoc.getLatitude()) + "," + String.valueOf(currentLoc.getLongitude()));
        }
        //		currentLoc = nowLoc;
        LatLng position = new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude());
        //		Toast.makeText(MapActivity.this,"nowLoc onConnected : " + String.valueOf(nowLoc.getLatitude()) + ", " + String.valueOf(nowLoc.getLongitude()),Toast.LENGTH_LONG).show();


        mMap.addMarker(new MarkerOptions().position(position).icon(BitmapDescriptorFactory.fromResource(R.mipmap.walk2)));
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(position, 17.0f)));

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    private GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
        @Override
        public void onMyLocationChange(Location location) {
            Marker mMarker;
            LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
            mMarker = mMap.addMarker(new MarkerOptions().position(loc));
            if (mMap != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
            }
        }
    };


    @Override
    public void OnLocationChanged(Location location) {
        Location loc1, loc2;
        double mRelativeDistance;
        Log.i("lokasi sekrang onDataChanged", String.valueOf(location.getLatitude() + " " + String.valueOf(location.getLongitude())));
        System.out.println("Location changed called");
        Log.i("paused?", String.valueOf(paused));
//        if (!paused) {
        CommonUtils.ToastUtil(MapsActivity.this, "Lokasi : " + String.valueOf(location.getLatitude()) + " , " + String.valueOf(location.getLongitude()));
        loc1 = new Location("");
        loc1.setLatitude(location.getLatitude());
        loc1.setLongitude(location.getLongitude());

        //			if((int)model.getmLatitude()!=0 && (int)mRelativeDistance > 10 ){
        if (currentLoc != null) {
            Log.i("model loc latitude", String.valueOf(location.getLatitude()));
            if (String.valueOf(location.getLatitude()) != "0.0") {
                String parseColor;
//                    mRelativeDistance = currentLoc.distanceTo(loc1);
                mRelativeDistance = distance(currentLoc.getLatitude(), currentLoc.getLongitude(), location.getLatitude(), location.getLongitude());
                Marker markTemp = mMap.addMarker(new MarkerOptions().position(new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.mipmap.walk2)));
//                    mMap.addMarker(new MarkerOptions().position(new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.mipmap.walk2)));
                markTemp.remove();
                CommonUtils.ToastUtil(MapsActivity.this, "distance di map saat polyline1 : " + String.valueOf(mRelativeDistance));
                if (mRelativeDistance > 200) {
                    parseColor = "#E1BBF2";
                    createDashedLine(mMap, new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude()), Color.parseColor(parseColor));
                } else {
                    parseColor = "#8E12CC";
                    if ((int) location.getLatitude() != 0 && (int) mRelativeDistance > 3 && (int) mRelativeDistance < 300) {
                        Log.i("distance di map saat polyline", String.valueOf(mRelativeDistance));
                        Log.i("accuracy di map saat polyline", String.valueOf(currentLoc.getAccuracy()));
                        CommonUtils.ToastUtil(MapsActivity.this, "distance di map saat polyline2 : " + String.valueOf(mRelativeDistance));
                        CommonUtils.ToastUtil(MapsActivity.this, "accuracy di map saat polyline : " + String.valueOf(currentLoc.getAccuracy()));
                        mMap.addPolyline(new PolylineOptions().add(new LatLng(location.getLatitude(), location.getLongitude()),
                                new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude()))
                                .width(9)
                                .color(Color.parseColor(parseColor))
                                .geodesic(true));
                    }
                }
            }

        }
//        }
       /* Location location = new Location("");
        location.setLatitude(model.getmLatitude());
        location.setLongitude(model.getmLongitude());*/
        currentLoc = location;
        lm.addEvent(new Event(LocationMonitor.UPDATE_POS, new LatLng(location.getLatitude(), location.getLongitude())));

        try {
            lock.acquire();
            //If we need a Refresh
            if (needRefresh) {
                log("Needed refresh");
                needRefresh = false;
                log("lastLegMap is " + (lastLegMap == null ? "null" : "not null"));
                log("selectedLegMap is " + (selectedLegMap == null ? "null" : "not null"));
                //And the leg has actually changed
                if (lastLegMap != selectedLegMap && selectedLegMap != null) {
                    boolean lastFound = false;
                    boolean selectedFound = false;
                    //Find both legs
                    for (int i = 0; i < lines.size(); i++) {
                        if (selectedFound && lastFound) {
                            break;
                        }

                        Polyline thisLine = lines.get(i);
                        //Find the match for the last leg that was selected
                        if (!lastFound && lastLegMap != null && (lastLegMap.points).equals(thisLine.getPoints())) {
                            log("Found last lg, reset color to blue");
                            thisLine.setColor(Color.BLUE);
                            lastFound = true;
                        }
                        //Find the match for the newly selected leg
                        if (!selectedFound && selectedLegMap != null && selectedLegMap.points.equals(thisLine.getPoints())) {
                            log("Found current leg reset color to red");
                            thisLine.setColor(Color.RED);
                            selectedFound = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.release();
        }
    }

    @Override
    public void OnDataChanged(LocationModel model) {
        /*Location loc1, loc2;
        double mRelativeDistance;
        Log.i("lokasi sekrang onDataChanged", String.valueOf(model.getmLatitude() + " " + String.valueOf(model.getmLongitude())));
        System.out.println("Location changed called");
        Log.i("paused?", String.valueOf(paused));
//        if (!paused) {
        CommonUtils.ToastUtil(MapsActivity.this, "Lokasi : " + String.valueOf(model.getmLatitude()) + " , " + String.valueOf(model.getmLongitude()));
        loc1 = new Location("");
        loc1.setLatitude(model.getmLatitude());
        loc1.setLongitude(model.getmLongitude());

        //			if((int)model.getmLatitude()!=0 && (int)mRelativeDistance > 10 ){
        if (currentLoc != null) {
            Log.i("model loc latitude", String.valueOf(model.getmLatitude()));
            if (String.valueOf(model.getmLatitude()) != "0.0") {
                String parseColor;
//                    mRelativeDistance = currentLoc.distanceTo(loc1);
                mRelativeDistance = distance(currentLoc.getLatitude(), currentLoc.getLongitude(), model.getmLatitude(), model.getmLongitude());
                Marker markTemp = mMap.addMarker(new MarkerOptions().position(new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.mipmap.walk2)));
//                    mMap.addMarker(new MarkerOptions().position(new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude())).icon(BitmapDescriptorFactory.fromResource(R.mipmap.walk2)));
                markTemp.remove();
                CommonUtils.ToastUtil(MapsActivity.this, "distance di map saat polyline1 : " + String.valueOf(mRelativeDistance));
                if (mRelativeDistance > 200) {
                    parseColor = "#E1BBF2";
                    createDashedLine(mMap, new LatLng(model.getmLatitude(), model.getmLongitude()), new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude()), Color.parseColor(parseColor));
                } else {
                    parseColor = "#8E12CC";
                    if ((int) model.getmLatitude() != 0 && (int) mRelativeDistance > 3 && (int) mRelativeDistance < 300) {
                        Log.i("distance di map saat polyline", String.valueOf(mRelativeDistance));
                        Log.i("accuracy di map saat polyline", String.valueOf(currentLoc.getAccuracy()));
                        CommonUtils.ToastUtil(MapsActivity.this, "distance di map saat polyline2 : " + String.valueOf(mRelativeDistance));
                        CommonUtils.ToastUtil(MapsActivity.this, "accuracy di map saat polyline : " + String.valueOf(currentLoc.getAccuracy()));
                        mMap.addPolyline(new PolylineOptions().add(new LatLng(model.getmLatitude(), model.getmLongitude()),
                                new LatLng(currentLoc.getLatitude(), currentLoc.getLongitude()))
                                .width(9)
                                .color(Color.parseColor(parseColor))
                                .geodesic(true));
                    }
                }
            }

        }
//        }
        Location location = new Location("");
        location.setLatitude(model.getmLatitude());
        location.setLongitude(model.getmLongitude());
        currentLoc = location;
        lm.addEvent(new Event(LocationMonitor.UPDATE_POS, new LatLng(model.getmLatitude(), model.getmLongitude())));

        try {
            lock.acquire();
            //If we need a Refresh
            if (needRefresh) {
                log("Needed refresh");
                needRefresh = false;
                log("lastLegMap is " + (lastLegMap == null ? "null" : "not null"));
                log("selectedLegMap is " + (selectedLegMap == null ? "null" : "not null"));
                //And the leg has actually changed
                if (lastLegMap != selectedLegMap && selectedLegMap != null) {
                    boolean lastFound = false;
                    boolean selectedFound = false;
                    //Find both legs
                    for (int i = 0; i < lines.size(); i++) {
                        if (selectedFound && lastFound) {
                            break;
                        }

                        Polyline thisLine = lines.get(i);
                        //Find the match for the last leg that was selected
                        if (!lastFound && lastLegMap != null && (lastLegMap.points).equals(thisLine.getPoints())) {
                            log("Found last lg, reset color to blue");
                            thisLine.setColor(Color.BLUE);
                            lastFound = true;
                        }
                        //Find the match for the newly selected leg
                        if (!selectedFound && selectedLegMap != null && selectedLegMap.points.equals(thisLine.getPoints())) {
                            log("Found current leg reset color to red");
                            thisLine.setColor(Color.RED);
                            selectedFound = true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.release();
        }*/
    }

    public static void createDashedLine(GoogleMap map, LatLng latLngOrig, LatLng latLngDest, int color) {
        double difLat = latLngDest.latitude - latLngOrig.latitude;
        double difLng = latLngDest.longitude - latLngOrig.longitude;

        double zoom = map.getCameraPosition().zoom;

        double divLat = difLat / (zoom * 2);
        double divLng = difLng / (zoom * 2);

        LatLng tmpLatOri = latLngOrig;

        for (int i = 0; i < (zoom * 2); i++) {
            LatLng loopLatLng = tmpLatOri;

            if (i > 0) {
                loopLatLng = new LatLng(tmpLatOri.latitude + (divLat * 0.4f), tmpLatOri.longitude + (divLng * 0.4f));
            }

            Polyline polyline = map.addPolyline(new PolylineOptions()
                    .add(loopLatLng)
                    .add(new LatLng(tmpLatOri.latitude + divLat, tmpLatOri.longitude + divLng))
                    .color(color)
                    .width(9));

            tmpLatOri = new LatLng(tmpLatOri.latitude + divLat, tmpLatOri.longitude + divLng);
        }
    }

    public static void log(String s) {
        System.out.println(s);
    }

    public void updateCurrentLeg(LegMap l) {
        try {
            log("Acquiring lock for updateLeg");
            lock.acquire();
            log("Lock Acquired leg is " + (l == null ? "null" : "Not null"));
            lastLegMap = selectedLegMap;
            selectedLegMap = l;
            needRefresh = true;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            lock.release();
        }
    }

    @Override
    void recreateMap(ArrayList<Waypoint> waypoints, ArrayList<LegMap> legMaps) {
        System.out.println(waypoints.size());
        for (int i = 0; i < waypoints.size(); i++) {
            mMap.addMarker(new MarkerOptions()
                    .position(waypoints.get(i).centerPoint)
                    .title("Waypoint")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        }
        //This should only be called once for this class, so we create a new array for our polylines
        lines = new ArrayList<Polyline>();
        System.out.println(legMaps.size());
        for (int i = 0; i < legMaps.size(); i++) {
            Polyline addLine = mMap.addPolyline(new PolylineOptions().addAll(legMaps.get(i).points).width(6).color(Color.BLUE));
            lines.add(addLine);
        }
    }

    public void updatePosition() {
//        mMap.clear();
        SessionManagement session = new SessionManagement(MapsActivity.this);
//        taskId = session.getCurrentHotspot();
        try {
            boolean isClosed = true;
            isClosed = journeyDao.checkJourneyClosed(String.valueOf(taskId));
            if (isClosed == false) {
                boolean isAlreadyTrip = true;
                isAlreadyTrip = journeyDao.countJourneyDetail(String.valueOf(taskId));
                if (isAlreadyTrip) {
                    getExistingTrip();
                    if(isStarted){
                        mLocationLibrary = new LocationLibrary(MapsActivity.this, this);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getExistingTrip() {
        progressDialog = new ProgressDialog(MapsActivity.this);
        progressDialog.setMessage("Restoring Your Trip...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    int routeNo = 0;
                    String strTimeTrip, strTimeTripMinus1;
                    Date dtTimeTrip, dtTimeTripMinus1;
                    SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    trip = journeyDao.getTrip(String.valueOf(taskId));
                    if (trip.size() > 0) {
                        for (int i = 0; i < trip.size() - 1; i++) {
                            routeNo = trip.get(i).routeNo;
                            final double dblJarak = trip.get(i + 1).detailDistance;
                            akurasi = trip.get(i).detailAccuracy;
                            if (akurasi < 20) {
                                if (i == 0) {
                                    LatLng src = new LatLng(trip.get(0).detailLatitude, trip.get(0).detailLongitude);
                                    routeNoAsExtras = trip.get(0).routeNo;
                                    String strJourneyNo = trip.get(0).journey_id.toString();
                                    String strRouteNo = String.valueOf(trip.get(0).routeNo);
                                    Log.i("str journey + str route No ke " + String.valueOf(i), strJourneyNo + "  " + strRouteNo);

                                } else {
                                    final LatLng src = new LatLng(trip.get(i).detailLatitude, trip.get(i).detailLongitude);
                                    final LatLng dest = new LatLng(trip.get(i + 1).detailLatitude, trip.get(i + 1).detailLongitude);
                                    routeNo = trip.get(i).routeNo;
                                    routeNoAsExtras = trip.get(i + 1).routeNo;
                                    strTimeTripMinus1 = trip.get(i).detailTimeStamp;
                                    strTimeTrip = trip.get(i + 1).detailTimeStamp;
                                    dtTimeTrip = df.parse(strTimeTrip);
                                    dtTimeTripMinus1 = df.parse(strTimeTripMinus1);

                                    long duration = dtTimeTrip.getTime() - dtTimeTripMinus1.getTime();
                                    long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
                                    Log.i("routeNo", "route No : " + String.valueOf(routeNo) + " routeNoMinus1 : " + String.valueOf(routeNoAsExtras));
                                    Log.i("datetimetrip", "dtTimeTrip : " + String.valueOf(dtTimeTrip) + " dtTimeTripMinus1 : " + String.valueOf(dtTimeTripMinus1));
                                    Log.i("duration & diffInMinutes ", "duration : " + String.valueOf(duration) + "  diffInMinutes : " + String.valueOf(diffInMinutes));

                                    // mMap is the Map Object
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            //										new PolylineOptions().addAll(points)
                                            String parseColor;
                                            if (dblJarak > 200) {
                                                parseColor = "#E1BBF2";
                                                createDashedLine(mMap, new LatLng(src.latitude, src.longitude), new LatLng(dest.latitude, dest.longitude), Color.parseColor(parseColor));

                                            } else {
                                                parseColor = "#E1BBF2";
                                                Polyline line = mMap.addPolyline(new PolylineOptions()
                                                        .add(new LatLng(src.latitude, src.longitude),
                                                                new LatLng(dest.latitude, dest.longitude)
                                                        )
                                                        .width(9)
                                                        .color(Color.parseColor(parseColor))
                                                        .geodesic(true)
                                                );
                                            }
                                        }
                                    });
                                    Log.i("add line", "line to psotion " + String.valueOf(routeNo));
                                    mMap.moveCamera(CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(dest, 14.0f)));
                                }
                            }

                        }
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 3000);
        //		progressDialog.dismiss();
    }

    @Override
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        MapsActivity.taskId = taskId;
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = (dist * 1.609344) / 1000;
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

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Log.i("Connection", String.valueOf(isConnected));
        String messageToShow;
        if (isConnected) {
            messageToShow = "Terhubung Internet";

        } else {
            messageToShow = "Tidak ada Internet";

        }
        CommonUtils.ToastUtil(MapsActivity.this, messageToShow);
    }

    private boolean checkConnection() {
        String messageToShow;
        isConnected = ConnectionService.isConnected();
        Log.i("Connection", String.valueOf(isConnected));
        if (isConnected) {
            messageToShow = "Terhubung Internet";

        } else {
            messageToShow = "Tidak ada Internet";

        }
        CommonUtils.ToastUtil(MapsActivity.this, messageToShow);
        return isConnected;
    }


    void SimpanGambarKeSQLite(String lokasi){

        if (TextUtils.isEmpty(lokasi)) {

        }else{

            String keterangan = "1";

            DatabaseHandler databaseHandler = new DatabaseHandler(MapsActivity.this);
            GambarModel gambarModel = new GambarModel();
            gambarModel.setIdTitik(String.valueOf(getTaskId()));
            gambarModel.setFilegambar(lokasi);
            gambarModel.setIndeks(String.valueOf(1));
            gambarModel.setLat(latitude);
            gambarModel.setLong(longitude);
            gambarModel.setSos("g");
            gambarModel.setKeterangan(keterangan);

            databaseHandler.addGambar(gambarModel);

        }


    }

    public void MulaiKamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        MY_REQUEST_CODE);
            }
        }

        Intent i=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File dir= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        hasilKamera=new File(dir,  BaseFunction.RenamePhoto(MapsActivity.this));
        i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(hasilKamera));

        startActivityForResult(i, TAKE_CAMERA);

    }

    void DialogFoto(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, TAKE_GALLERY);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri selectedImage;
        Cursor cursor;
        int columnIndex;
        Bitmap gambar;

        if (resultCode== Activity.RESULT_OK){

            switch (requestCode){

                case TAKE_CAMERA:

                    if (hasilKamera != null ) {
                        gambar = BaseFunction.bacaBitmap(Uri.fromFile(hasilKamera), MapsActivity.this);
                        SimpanGambarKeSQLite(hasilKamera.getAbsolutePath().toString());
                    }

                break;

                case TAKE_GALLERY:
                    selectedImage = data.getData();
                    String[] filePathColumn1 = {MediaStore.Images.Media.DATA};

                    cursor = getContentResolver().query(selectedImage, filePathColumn1, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    columnIndex = cursor.getColumnIndex(filePathColumn1[0]);
                    mediaPath = cursor.getString(columnIndex);
                    gambar=BaseFunction.bacaBitmap(selectedImage, MapsActivity.this);
                    SimpanGambarKeSQLite(mediaPath);
                    cursor.close();
                    break;
            }

        }

    }


    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.CAMERA)== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(MapsActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

        }else{
            ActivityCompat.requestPermissions(MapsActivity.this,new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == MY_REQUEST_CODE) {
            if (grantResults.length == 2){
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){

                }else{
                    Toast.makeText(MapsActivity.this, "Permission ditolak",Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }


}
