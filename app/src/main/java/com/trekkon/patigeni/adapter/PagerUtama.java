package com.trekkon.patigeni.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.trekkon.patigeni.fragment.AmbilFoto;
import com.trekkon.patigeni.fragment.Artikel;


/**
 * Created by Primananda on 7/8/2017.
 */

public class PagerUtama extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerUtama(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                AmbilFoto ambilFoto = new AmbilFoto();
                return ambilFoto;

            case 1:
                Artikel artikel = new Artikel();
                return artikel;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}