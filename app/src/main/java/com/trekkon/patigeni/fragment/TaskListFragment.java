package com.trekkon.patigeni.fragment;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.trekkon.patigeni.R;
import com.trekkon.patigeni.activities.DaftarTitikActivity;
import com.trekkon.patigeni.activities.MainActivity;
import com.trekkon.patigeni.adapter.RV_Titik;
import com.trekkon.patigeni.controller.titik.TitikController;
import com.trekkon.patigeni.controller.titik.TitikResult;
import com.trekkon.patigeni.dao.BaseDao;
import com.trekkon.patigeni.dao.DbAdapter;
import com.trekkon.patigeni.helper.BaseFunction;
import com.trekkon.patigeni.helper.MessageHelper;
import com.trekkon.patigeni.model.Titik;
import com.trekkon.patigeni.model.TitikApiModel;
import com.trekkon.patigeni.model.User;
import com.trekkon.patigeni.utils.CommonUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Iwan on 16-Aug-17.
 */

public class TaskListFragment extends Fragment implements TitikResult, MainActivity.OnDataPassedListener {
    Button btnMenuUtama, btnRefresh;
    RecyclerView rv = null;
    TitikController titikController;
    Toolbar toolbar;
    String userName, companyName;
    TextView txtCompany;
    User user;
    MainActivity mainActivity;
    DbAdapter hotspotDao = null;
    String id;
    private Context mContext;
    CommonUtils util;
    private List<Titik> hotList =null;
    private List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();

    public RecyclerView getRv() {
        return rv;
    }

    public void setRv(RecyclerView rv) {
        this.rv = rv;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_hotspot_list, container, false);
        util = new CommonUtils(getActivity());
        txtCompany = (TextView) rootView.findViewById(R.id.txtCompany);
        id = BaseFunction.GetUserID(getActivity());
        txtCompany.setText("Titik Panas");
        btnRefresh = (Button) rootView.findViewById(R.id.btn_refresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (CommonUtils.checkConnection()){
                        ProsesDataTitik();
                    } else {
                        setRecyclerView(id, rv);
                    }

                } catch (IOException e) {
                    setRecyclerView(id, rv);
                    e.printStackTrace();
                }
            }
        });


        rv = (RecyclerView) rootView.findViewById(R.id.rv);

        try {
            if (CommonUtils.checkConnection()){
                ProsesDataTitik();
            } else {
                MessageHelper.showOnThread(getActivity(), "Tidak ada Koneksi Internet\nAnda Dalam Posisi Offline");
                setRecyclerView(id, rv);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ProsesDataTitik();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });*/


        rv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        toolbar = (Toolbar) rootView.findViewById(R.id.tool_bar);


        return rootView;
    }


    void ProsesDataTitik() throws IOException {
        try {

            titikController = new TitikController(this, getActivity());
            Log.i("id user", id);
            titikController.Titik(id);
        } catch (Throwable e) {
            Log.i("error", e.toString());
            throw new IOException(e.toString());
        }
    }


    @Override
    public void showValidationError() {

    }

    public void rvRefresh(){
        setRecyclerView(id,rv);
    }

    @Override
    public void onSuccess(TitikApiModel titikApiModel) {

       /* rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(new RV_Titik(titikApiModel.getTitik(), R.layout.rv_titik, getActivity()));*/
        List<Titik> listHotspot = titikApiModel.getTitik();
        mContext = getActivity();
        hotspotDao =new DbAdapter(mContext);
        if(listHotspot.size() != 0){
            for(int i= 0; i< listHotspot.size();i++){
                String pic = listHotspot.get(i).getPIC();
                if (pic != "1"){
                    Calendar cal = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String currentDateandTime = df.format(Calendar.getInstance().getTime());
                    ContentValues cValue =new ContentValues();
                    cValue.put("contact_id", BaseFunction.GetUserID(getActivity()));
                    cValue.put("hotspot_id", listHotspot.get(i).getHotspotId());
                    cValue.put("latitude", listHotspot.get(i).getHotSpotLatitude());
                    cValue.put("longitude", listHotspot.get(i).getHotSpotLongitude());
                    cValue.put("by_human", "Y");
                    cValue.put("status", "R");
                    cValue.put("tingkat_kepercayaan", listHotspot.get(i).getTingkatKepercayaan());
                    cValue.put("timestamp_original", listHotspot.get(i).getTimestampReceived());
                    cValue.put("timestamp_received", currentDateandTime);
                    try {
                        hotspotDao.insert("Task",cValue);
                    } catch (Exception e) {
//                        MessageHelper.show(getActivity(), "Tidak Bisa Insert Database\n" + e.toString());
                        Log.i("error", e.toString());
                        e.printStackTrace();
                    }

                    rv.setAdapter(new RV_Titik(listHotspot, R.layout.rv_titik, getActivity()));
                }
            }
        }
        setRecyclerView(id, rv);


    }

    @Override
    public void onError(String errMessage) {
//        MessageHelper.show(getActivity(),errMessage);
        setRecyclerView(id, rv);
    }

    @Override
    public void onDataPassed(User user) {
        companyName = user.getCompanyName();
    }

    public void setRecyclerView(String userId, RecyclerView recyclerView) {
        mContext = getActivity();
        hotspotDao =new DbAdapter(mContext);
        try {
            hotList = hotspotDao.getHotSpot(userId);

            rv.setLayoutManager(new LinearLayoutManager(getActivity()));
            rv.setAdapter(new RV_Titik(hotList, R.layout.rv_titik, getActivity()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
