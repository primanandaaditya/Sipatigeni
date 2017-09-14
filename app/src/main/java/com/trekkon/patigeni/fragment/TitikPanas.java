package com.trekkon.patigeni.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.trekkon.patigeni.R;
import com.trekkon.patigeni.activities.DaftarTitikActivity;
import com.trekkon.patigeni.activities.MainActivity;
import com.trekkon.patigeni.adapter.RV_Titik;
import com.trekkon.patigeni.controller.titik.TitikController;
import com.trekkon.patigeni.controller.titik.TitikResult;
import com.trekkon.patigeni.helper.BaseFunction;
import com.trekkon.patigeni.helper.DatabaseHandler;
import com.trekkon.patigeni.model.TabelStatus;
import com.trekkon.patigeni.model.Titik;
import com.trekkon.patigeni.model.TitikApiModel;
import com.trekkon.patigeni.utils.SessionManagement;

import java.io.IOException;
import java.util.List;

public class TitikPanas extends Fragment implements TitikResult {


    RecyclerView rv;
    TitikController titikController;
    private TitikApiModel titikApiModel;

    public TitikPanas() {
        // Required empty public constructor
    }

    public TitikApiModel getTitikApiModel(){
        return  this.titikApiModel;
    }

    public void setTitikApiModel(TitikApiModel titikApiModel){
        this.titikApiModel = titikApiModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_titik_panas, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FindID();

        DatabaseHandler databaseHandler = new DatabaseHandler(getActivity());
        Boolean b = databaseHandler.cekDetail("1",getActivity());
        //Toast.makeText(getActivity(),String.valueOf(b),Toast.LENGTH_SHORT).show();
        //TabelStatus tabelStatus;
        //List<TabelStatus> tabelStatuses = databaseHandler.getAllTabelStatus();
        //for (int i =0 ; i <= tabelStatuses.size()-1; i++){
        //    tabelStatus=tabelStatuses.get(i);
        //    Toast.makeText(getActivity(),tabelStatus.getIdTitik() + " " + tabelStatus.getKeterangan(),Toast.LENGTH_SHORT).show();
       //}

        //String hasil = databaseHandler.getDetail("2",getActivity());
        //Toast.makeText(getActivity(),hasil,Toast.LENGTH_SHORT).show();

    }

    void ProsesDataTitik() throws IOException {


        try {
            String id = BaseFunction.GetUserID(getActivity());

            titikController = new TitikController(this,getActivity());
            titikController.Titik(id);
        } catch (Throwable e) {
            throw new IOException(e.toString());
        }


    }

    void Reload(){

        //ambil data source recycleview
        //dari session management

        rv.setAdapter(null);

        SessionManagement sessionManagement = new SessionManagement(getActivity());

        if (sessionManagement.getTitikApi() != null){
            List<Titik> titiks = sessionManagement.getTitikApi().getTitik();
            rv.setLayoutManager(new LinearLayoutManager(getActivity()));
            rv.setAdapter(new RV_Titik(titiks,R.layout.rv_titik,getActivity().getApplicationContext()));
        }else{

        }




    }

    void FindID(){

        rv=(RecyclerView)getActivity().findViewById(R.id.rv);

        try {
        ProsesDataTitik();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    @Override
    public void onResume() {
        super.onResume();

        Reload();

    }

    @Override
    public void showValidationError() {

    }

    void Bersih(){
        rv.setAdapter(null);
    }

    @Override
    public void onSuccess(TitikApiModel titikApiModel) {

        //setTitikApiModel(titikApiModel);

        SessionManagement sessionManagement = new SessionManagement(getActivity());
        sessionManagement.SaveTitikApi(titikApiModel);

        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv.setAdapter(new RV_Titik(titikApiModel.getTitik(),R.layout.rv_titik,getActivity().getApplicationContext()));
    }

    @Override
    public void onError(String errMessage) {

    }
}
