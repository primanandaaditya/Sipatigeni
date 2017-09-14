package com.trekkon.patigeni.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.trekkon.patigeni.R;
import com.trekkon.patigeni.helper.BaseFunction;
import com.trekkon.patigeni.helper.TrekkonImageView;
import com.trekkon.patigeni.model.GambarModel;

import java.io.File;
import java.util.List;


/**
 * Created by Primananda on 8/5/2017.
 */

public class PendingGridViewAdapter extends BaseAdapter {

    Context context;
    List<GambarModel> gambarModels;
    private static LayoutInflater inflater = null;

    public PendingGridViewAdapter(Context context, List<GambarModel> gambarModels) {
        this.context = context;
        this.gambarModels=gambarModels;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return gambarModels.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null)
            convertView = inflater.inflate(R.layout.grid_pending, null);


        //ImageView gambar = (ImageView) convertView.findViewById(R.id.gambar);
        TrekkonImageView trekkonImageView = (TrekkonImageView) convertView.findViewById(R.id.tiv);

        GambarModel gambarModel = new GambarModel();
        gambarModel=gambarModels.get(position);

        File imgFile = new  File(gambarModel.getFilegambar());

        try {
            if(imgFile.exists()){
                // jangan di-uncomment Bitmap myBitmap = BitmapFactory.decodeFile(gambarModel.getFilegambar());
                //gambar.setImageBitmap(BaseFunction.KompresGambar(gambarModel.getFilegambar()));
                trekkonImageView.setImageBitmap(BaseFunction.KompresGambar(gambarModel.getFilegambar()));
                trekkonImageView.setIdx(gambarModel.getIdx());
                trekkonImageView.setFileGambar(gambarModel.getFilegambar());
                trekkonImageView.setIdNotif(gambarModel.getIdTitik());
                trekkonImageView.setIndeks(gambarModel.getIndeks());
                trekkonImageView.setSlat(gambarModel.getLat());
                trekkonImageView.setSlong(gambarModel.getLong());
                trekkonImageView.setKeterangan(gambarModel.getKeterangan());
                trekkonImageView.setSos(gambarModel.getSos());

            }
        } catch (Throwable e) {
            //gambar.setImageResource(R.mipmap.ic_sudah_diambil);
            trekkonImageView.setImageResource(R.mipmap.ic_camera);
            trekkonImageView.setIdx(gambarModel.getIdx());
            trekkonImageView.setFileGambar(gambarModel.getFilegambar());
            trekkonImageView.setIdNotif(gambarModel.getIdTitik());
            trekkonImageView.setIndeks(gambarModel.getIndeks());
            trekkonImageView.setSlat(gambarModel.getLat());
            trekkonImageView.setSlong(gambarModel.getLong());
            trekkonImageView.setKeterangan(gambarModel.getKeterangan());
            trekkonImageView.setSos(gambarModel.getSos());
        }

        return convertView;
    }


}
