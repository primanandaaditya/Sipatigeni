package com.trekkon.patigeni.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trekkon.patigeni.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Artikel extends Fragment {


    public Artikel() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this com.example.primananda.sipatigeni.fragment
        return inflater.inflate(R.layout.fragment_artikel, container, false);
    }

}
