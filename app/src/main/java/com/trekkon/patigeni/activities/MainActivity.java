package com.trekkon.patigeni.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.trekkon.patigeni.R;

import com.trekkon.patigeni.controller.direction.GoogleDirectionResult;
import com.trekkon.patigeni.dao.DbAdapter;
import com.trekkon.patigeni.fragment.AmbilFoto;
import com.trekkon.patigeni.fragment.Artikel;
import com.trekkon.patigeni.fragment.Bantuan;
import com.trekkon.patigeni.fragment.ProfileFragment;
import com.trekkon.patigeni.fragment.TaskListFragment;
import com.trekkon.patigeni.fragment.TitikPanas;
import com.trekkon.patigeni.helper.BaseFunction;
import com.trekkon.patigeni.helper.GPSTracker;
import com.trekkon.patigeni.helper.DatabaseHandler;
import com.trekkon.patigeni.model.User;
import com.trekkon.patigeni.services.ConnectionService;
import com.trekkon.patigeni.utils.SessionManagement;
import com.trekkon.patigeni.model.GoogleMapDirections.GoogleMapDirectionModel;

import com.trekkon.patigeni.constants.URL;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import april.yun.ISlidingTabStrip;
import april.yun.other.JTabStyleDelegate;

import static april.yun.other.JTabStyleBuilder.STYLE_DEFAULT;
import static april.yun.other.JTabStyleBuilder.STYLE_DOTS;
import static april.yun.other.JTabStyleBuilder.STYLE_GRADIENT;
import static april.yun.other.JTabStyleBuilder.STYLE_ROUND;




public class MainActivity extends SuperActivity implements GoogleDirectionResult, ConnectionService.ConnectionServiceListener {

    GridView gridView;
    private SharedPreferences sp;
    private Bundle extras, extraFromMain;
    private User user;
    private String doAction = "";
    boolean isActivityRunning = true;
    private ISlidingTabStrip tabs_buttom;
    private String[] mTitles;
    private int[] mPressed;
    private int[] mNormal;
    private int[] mSelectors;
    private ViewPager pager;
    private PagerAdapter adapter;

    DbAdapter hotspotDao = null;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        extras = getIntent().getExtras();
        setContentView(R.layout.activity_dashboard);
        tabs_buttom = (ISlidingTabStrip)findViewById(R.id.tab_buttom);

        Toolbar toolbarTop = (Toolbar) findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbarTop.findViewById(R.id.toolbar_title);
        id = BaseFunction.GetUserID(MainActivity.this);
        setupTabStrips();
        setupViewpager();
        FindID();

        /*if(extras != null){
            doAction = extras.getString("action");
            Log.i("action", doAction);
            if (doAction.contains("showData")){
                showDialog(this,"notification", "latitude : " + extras.getString("latitude") +
                        "\nlongitude : " + extras.getString("longitude") +
                        "\ntimestamp : " + extras.getString("timestamp") +
                        "\ntingkat kepercayaan : " + extras.getString("tingkatKepercayaan"), "OK", "", 1);

            }
            setIntent(new Intent());
        }*/

    }

    @Override
    protected void onStart(){
        super.onStart();
        extras = getIntent().getExtras();
//        showToast(this, String.valueOf(isActivityRunning));

        if(extras != null) {
//            setIntent(new Intent());
            /*doAction = extras.getString("delete");
            Log.i("action", doAction);
            if (doAction.contains("delete")) {
                String userId = extras.getString("userId");
                if (userId != BaseFunction.GetUserID(MainActivity.this)) {
                    try {
                        hotspotDao.delete("Task", "cast(hotspot_id as varchar) = ? ", new String[]{extras.getString("taskId")});
                        showToast(MainActivity.this, "Titik Api " + extras.getString("taskId") + "telah diambil oleh " + extras.getString("taskId"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
            setIntent(new Intent());


            TaskListFragment fragment = new TaskListFragment();
            fragment.setRecyclerView(BaseFunction.GetUserID(MainActivity.this), fragment.getRv());*/

        }
        /*if(extras != null){
            doAction = extras.getString("action");
            Log.i("action", doAction);
            if (doAction.contains("showData")){
                showDialog(this,"notification", "latitude : " + extras.getString("latitude") +
                        "\nlongitude : " + extras.getString("longitude") +
                        "\ntimestamp : " + extras.getString("timestamp") +
                        "\ntingkat kepercayaan : " + extras.getString("tingkatKepercayaan"), "OK", "", 1);

            }
            setIntent(new Intent());
            *//*
            * Insert this payload data into offline database
            *
            * *//*
            hotspotDao = new DbAdapter(MainActivity.this);


            Calendar cal = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentDateandTime = df.format(Calendar.getInstance().getTime());
            ContentValues cValue = new ContentValues();
            cValue.put("contact_id", BaseFunction.GetUserID(MainActivity.this));
            cValue.put("hotspot_id", extras.getString("taskId"));
            cValue.put("latitude", Double.parseDouble(extras.getString("latitude")));
            cValue.put("longitude", Double.parseDouble(extras.getString("longitude")));
            cValue.put("by_human", "Y");
            cValue.put("status", "R");
            cValue.put("tingkat_kepercayaan", extras.getString("tingkatKepercayaan"));
            cValue.put("timestamp_original", extras.getString("timestamp"));
            cValue.put("timestamp_received", currentDateandTime);
            try {
                hotspotDao.insert("Task", cValue);
            } catch (Exception e) {
//                        MessageHelper.show(getActivity(), "Tidak Bisa Insert Database\n" + e.toString());
                Log.i("error", e.toString());
                e.printStackTrace();
            }

            TaskListFragment fragment = new TaskListFragment();
            fragment.setRecyclerView(BaseFunction.GetUserID(MainActivity.this), fragment.getRv());
        }*/

    }

    private void setupTabStrips(){
        setupStrip(tabs_buttom.getTabStyleDelegate(), STYLE_GRADIENT);
        tabs_buttom.getTabStyleDelegate().setFrameColor(Color.BLUE).setIndicatorColor(Color.GREEN)
                .setTabIconGravity(Gravity.TOP)
                .setIndicatorHeight(-6)
                .setDividerColor(Color.TRANSPARENT);
    }

    private void setupViewpager(){

        mTitles = getResources().getStringArray(R.array.tabs);
        mNormal = new int[]{R.mipmap.list_white, R.mipmap.ic_tab_profile, R.mipmap.ic_tab_moments };
        mPressed = new int[]{R.mipmap.list, R.mipmap.ic_tab_profile_h, R.mipmap.ic_tab_moments_h};
        mSelectors = new int[]{R.drawable.tab1, R.drawable.tab2, R.drawable.tab3};

        pager = (ViewPager)findViewById(R.id.pager);
        adapter = new PagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        final int pageMargin = (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        pager.setPageMargin(pageMargin);

        tabs_buttom.bindViewPager(pager);
    }

    void FindID(){
        user = getApp().getPatigeniPreferences().getUser(this);

        Log.i("User Name", user.getName());
        Log.i("Company Name", user.getCompanyName());
        Log.i("User Id", String.valueOf(user.getUserId()));
        Log.i("email",user.getEmail());
        Log.i("password", user.getPassword());

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected){

        } else {

        }
    }

    public interface OnDataPassedListener {
        void onDataPassed(User user);
    }

    void TakePhoto(){
        Intent intent = new Intent(MainActivity.this, TakePhoto.class);
        startActivity(intent);
    }



    void DaftarTitik(){
        Intent intent = new Intent(MainActivity.this, DaftarTitikActivity.class);
        startActivity(intent);
    }

    void Peta(){
        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
        startActivity(intent);
    }

    void Pending(){

        DatabaseHandler databaseHandler = new DatabaseHandler(MainActivity.this);
        long jml = databaseHandler.getGambarCount();
        if (jml == 0){
            Toast.makeText(MainActivity.this, "Tidak ada foto yang belum dikirim", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent = new Intent(MainActivity.this, PendingActivity.class);
            startActivity(intent);
        }

    }

    void AmbilFoto(){
        Intent intent = new Intent(MainActivity.this, AmbilFotoActivity.class);
        startActivity(intent);
    }

    void Logout(){
        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
        sessionManagement.logoutUser();
        finish();
    }

    void SiapkanGPS(){
        GPSTracker gps;
        gps = new GPSTracker(MainActivity.this);
        if(gps.canGetLocation()){
            Toast.makeText(MainActivity.this, "GPS sudah dinyalakan", Toast.LENGTH_SHORT).show();
        }else{
            gps.showSettingsAlert();
        }
    }


    @Override
    public void showValidationError() {

    }

    @Override
    public void onSuccess(GoogleMapDirectionModel googleMapDirectionModel) {


    }



    @Override
    public void onError() {

    }

    public void onResume() {
        super.onResume();
        isActivityRunning = true;
    }
    public void onPause() {
        super.onPause();
        isActivityRunning = false;
    }

    private void setupStrip(JTabStyleDelegate tabStyleDelegate, int type){
        tabStyleDelegate.setJTabStyle(type).setShouldExpand(true).setFrameColor(Color.parseColor("#45C01A"))
                .setTabTextSize(getDimen(R.dimen.tabstrip_textsize))
                .setTextColor(Color.parseColor("#45C01A"), Color.GRAY)
//                .setDividerWidth(6)
                //.setTextColor(R.drawable.tabstripbg)
                .setDividerColor(Color.parseColor("#45C01A")).setDividerPadding(0)
                .setUnderlineColor(Color.parseColor("#3045C01A")).setUnderlineHeight(0)
                .setIndicatorColor(Color.parseColor("#7045C01A"))
                .setIndicatorHeight(getDimen(R.dimen.sug_event_tabheight)).setFrameColor(Color.TRANSPARENT);
    }

    private int getDimen(int dimen){
        return (int)getResources().getDimension(dimen);
    }

    public class PagerAdapter extends FragmentPagerAdapter implements ISlidingTabStrip.IconTabProvider {

        public PagerAdapter(FragmentManager fm){
            super(fm);
        }


        @Override
        public CharSequence getPageTitle(int position){
            return mTitles[position%3];
        }


        @Override
        public int getCount(){
            return mTitles.length;
        }


        @Override
        public Fragment getItem(int position){
            Fragment fragment = new TaskListFragment();
            switch (position) {
                case 0:
                    TitikPanas fragment1 = new TitikPanas();
                    return fragment1;

                case 1:
                    ProfileFragment fragment2 = new ProfileFragment();
                    fragment2.onDataPassed(user);
                    return fragment2;
                case 2:
                    fragment = new Bantuan();
                    return fragment;
                default:
                    return fragment;
            }
        }


        @Override
        public int[] getPageIconResIds(int position){
            //return new int[]{mNormal[position%4],mPressed[position%4]};
            return null;
        }


        @Override
        public int getPageIconResId(int position){
            //		return mPressed[position];
            return mSelectors[position%3];
        }
    }
}
