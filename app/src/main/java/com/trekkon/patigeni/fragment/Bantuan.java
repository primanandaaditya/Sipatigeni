package com.trekkon.patigeni.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.trekkon.patigeni.R;
import com.trekkon.patigeni.constants.URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class Bantuan extends Fragment {


    WebView wv;

    public Bantuan() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bantuan, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        wv=(WebView)getActivity().findViewById(R.id.wv);
        wv.loadUrl(URL.FORUM_URL);
    }
}
