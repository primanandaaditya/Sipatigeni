package com.trekkon.patigeni.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.trekkon.patigeni.R;


/**
 * Created by Primananda on 7/16/2017.
 */

public class SplashScreenGridView extends BaseAdapter {

    private Context mContext;
    private final String[] ntulisan = {"","", "", "","",""};
    private final int[] ngambar = {R.mipmap.ic_lapan,R.mipmap.ic_bmkg,R.mipmap.ic_nasa, R.mipmap.ic_del, R.mipmap.ic_1945, R.mipmap.ic_trekkon};


    // Constructor
    public SplashScreenGridView(Context c){
        mContext = c;
        //xtulisan=ntulisan;
        //xgambar=ngambar;
    }


    @Override
    public int getCount() {
        return ngambar.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.dashboard_gridview, null);
            TextView textView = (TextView) grid.findViewById(R.id.tulisan);
            ImageView imageView = (ImageView)grid.findViewById(R.id.gambar);
            textView.setText(ntulisan[position]);
            imageView.setImageResource(ngambar[position]);
        } else {
            grid = convertView;
        }

        return grid;
    }


}

