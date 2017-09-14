package com.trekkon.patigeni.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.trekkon.patigeni.R;
import com.trekkon.patigeni.model.GambarModel;

import java.io.File;
import java.util.List;

/**
 * Created by Primananda on 7/19/2017.
 */

public class RV_Gambar extends BaseAdapter {


    private Activity activity;
    private LayoutInflater inflater;
    private List<GambarModel> gambarModels;


    public RV_Gambar(Activity activity, List<GambarModel> gambarModels) {
        this.activity = activity;
        this.gambarModels = gambarModels;
    }

    @Override
    public int getCount() {
        return gambarModels.size();
    }

    @Override
    public Object getItem(int location) {
        return gambarModels.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.rv_pending, null);



        TextView tvFile = (TextView) convertView.findViewById(R.id.tvFile);
        TextView tvLat = (TextView) convertView.findViewById(R.id.tvLat);
        TextView tvLang = (TextView) convertView.findViewById(R.id.tvLong);
        ImageView gambar = (ImageView) convertView.findViewById(R.id.gambar);


        GambarModel gambarModel = gambarModels.get(position);

        tvFile.setText("File : " + gambarModel.getFilegambar());
        tvLat.setText("Lat : " + String.valueOf(gambarModel.getLat()));
        tvLang.setText("Long : " + String.valueOf(gambarModel.getLong()));

        File imgFile = new  File(gambarModel.getFilegambar());

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(gambarModel.getFilegambar());
            gambar.setImageBitmap(myBitmap);
        }


        //thumbNail.setImageUrl(m.getImage(), imageLoader);

        return convertView;
    }
}