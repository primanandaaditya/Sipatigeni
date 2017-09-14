package com.trekkon.patigeni.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.trekkon.patigeni.R;
import com.trekkon.patigeni.activities.MainActivity;
import com.trekkon.patigeni.activities.MapsActivity;
import com.trekkon.patigeni.controller.BaseResult;
import com.trekkon.patigeni.controller.updatestatus.UpdateStatusController;
import com.trekkon.patigeni.helper.BaseFunction;
import com.trekkon.patigeni.helper.DatabaseHandler;
import com.trekkon.patigeni.model.TabelStatus;
import com.trekkon.patigeni.model.Titik;
import com.trekkon.patigeni.utils.SessionManagement;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.widget.ImageView;

/**
 * Created by Primananda on 7/22/2017.
 */

public class RV_Titik extends RecyclerView.Adapter<RV_Titik.RV_TitikViewHolder> {

    private List<Titik> titiks;
    private int rowLayout;
    private Context context;




    public static class RV_TitikViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, BaseResult {

        UpdateStatusController updateStatusController;
        LinearLayout scheduleLayout;
        TextView rv_lat, rv_long, rv_id_notif, rv_titik, rv_pic, rv_akurasi, rv_status;
        ImageView gambar;
        List<Titik> titiks = new ArrayList<Titik>();
        Context context;
        private SessionManagement sessionManagement;

        public RV_TitikViewHolder(View v, Context context, List<Titik> titiks) {
            super(v);
            this.titiks = titiks;
            this.context = context;
            v.setOnClickListener(this);

            scheduleLayout=(LinearLayout)v.findViewById(R.id.layout);
            rv_lat=(TextView)v.findViewById(R.id.rv_lat);
            rv_long = (TextView)v.findViewById(R.id.rv_long);
            rv_id_notif=(TextView)v.findViewById(R.id.rv_id_notif);
            rv_titik=(TextView)v.findViewById(R.id.rv_titik);
            rv_status=(TextView)v.findViewById(R.id.rv_pic);
            gambar=(ImageView)v.findViewById(R.id.gambar);
            rv_akurasi = (TextView)v.findViewById(R.id.txtAkurasi);

        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            sessionManagement = new SessionManagement(v.getContext());
            Titik titik = this.titiks.get(position);

//            if (this.titiks.get(position).getPIC().equals("0")){

                //Intent intent = new Intent(v.getContext(), MapsActivity.class);
                //intent.putExtra("lat",titik.getHotSpotLatitude());
                //intent.putExtra("long",titik.getHotSpotLongitude());
                //intent.putExtra("idnotif",titik.getHotspotId());

                //v.getContext().startActivity(intent);


                //update status
                /*updateStatusController = new UpdateStatusController(this, v.getContext());
                updateStatusController.update(BaseFunction.GetUserID(v.getContext()),titik.getHotspotId());*/

                //BaseFunction.TampilkanMap(v.getContext(), new LatLng(Double.parseDouble(titik.getHotSpotLatitude()), Double.parseDouble(titik.getHotSpotLongitude())));
//                BaseFunction.DialogTitikApi(v.getContext(), new LatLng(Double.parseDouble(titik.getHotSpotLatitude()), Double.parseDouble(titik.getHotSpotLongitude())), titik.getHotspotId());


            //Intent intent = new Intent(v.getContext(), MapsActivity.class);

            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //intent.putExtra("taskId", Integer.parseInt(titik.getHotspotId()));
            //intent.putExtra("latitude", Double.parseDouble(titik.getHotSpotLatitude()));
            //intent.putExtra("longitude",Double.parseDouble(titik.getHotSpotLongitude()));
            //intent.putExtra("tingkatKepercayaan", titik.getTingkatKepercayaan());
            sessionManagement.setCurrentHotspot(Integer.parseInt(titik.getHotspotId()));

            updateStatusController = new UpdateStatusController(this, v.getContext());
            updateStatusController.update(BaseFunction.GetUserID(v.getContext()),titik.getHotspotId());

            BaseFunction.DialogTitikApi(v.getContext(), new LatLng(Double.parseDouble(titik.getHotSpotLatitude()), Double.parseDouble(titik.getHotSpotLongitude())), titik.getHotspotId());

            //v.getContext().startActivity(intent);

//            }else{
//
//
//                Toast.makeText(v.getContext(),"Titik ini sudah diambil orang lain. Silakan ambil titik panas lain.",Toast.LENGTH_SHORT).show();
//            }


            //((Activity)v.getContext()).finish();

        }

        @Override
        public void showValidationError() {
            Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSuccess() {
            //Toast.makeText(context,"Update OK",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(String errorMessage) {
            Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();
        }


    }

    public RV_Titik(List<Titik> titiks, int rowLayout, Context context) {
        this.titiks = titiks;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public RV_Titik.RV_TitikViewHolder onCreateViewHolder(final ViewGroup parent,
                                                          int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new RV_TitikViewHolder(view, context, titiks);

    }




    @Override
    public void onBindViewHolder(final RV_TitikViewHolder holder, final int position) {

        holder.setIsRecyclable(false);

        DatabaseHandler databaseHandler = new DatabaseHandler(context);

        if (databaseHandler.cekDetail(titiks.get(position).getHotspotId(),context).equals(true)){
            String status = databaseHandler.getDetail(titiks.get(position).getHotspotId(),context);

            switch (status){
                case "0":

                    holder.rv_status.setVisibility(View.VISIBLE);
                    holder.rv_status.setText("Dibatalkan");
                    break;
                case "1":

                    holder.rv_status.setVisibility(View.VISIBLE);
                    holder.rv_status.setText("Menuju lokasi");
                    break;
                case "2":

                    holder.rv_status.setVisibility(View.VISIBLE);
                    holder.rv_status.setText("Foto terkirim");
                    break;
            }
        }




        holder.rv_titik.setText("Task  :" + titiks.get(position).getHotspotId());
        holder.rv_lat.setText("Latitude  : " + titiks.get(position).getHotSpotLatitude());
        holder.rv_long.setText("Longitude : " + titiks.get(position).getHotSpotLongitude());
        holder.rv_akurasi.setText("Akurasi : " + titiks.get(position).getTingkatKepercayaan() + "%");
        holder.rv_id_notif.setText(titiks.get(position).getHotspotId());



        //DatabaseHandler databaseHandler = new DatabaseHandler(context);
        //TabelStatus tabelStatus = databaseHandler.getTabelStatus(idNotif,context);
        //if (tabelStatus.getKeterangan().equals("")){
        //    holder.rv_pic.setText("Something");
        //}
        //status = titiks.get(position).getStatus();
        //if (status.isEmpty() || status.equals("")){
        //    status = "R";
       // }
        //if(status.equals("X")){
         //   holder.rv_status.setVisibility(View.VISIBLE);
         //   holder.rv_status.setText("Sedang Dilaksanakan");
         //   holder.rv_status.setTextColor(Color.parseColor("#FCFF1A"));
        //} else if(status.equals("Z")) {
        //    holder.rv_status.setVisibility(View.VISIBLE);
        //    holder.rv_status.setText("Sudah Selesai, Belum dikirim");
        //    holder.rv_status.setTextColor(Color.parseColor("#D21614"));
        //}



        String tingkatKepercayaan = titiks.get(position).getTingkatKepercayaan();
        if (tingkatKepercayaan.isEmpty() || tingkatKepercayaan.equals("")){
            tingkatKepercayaan = "0";
        }
        if (Integer.parseInt(tingkatKepercayaan)<60 ) {
            holder.gambar.setImageResource(R.mipmap.flame40);
        } else if (Integer.parseInt(tingkatKepercayaan)>61 && Integer.parseInt(tingkatKepercayaan)<80){
            holder.gambar.setImageResource(R.mipmap.flame60);
        } else if (Integer.parseInt(tingkatKepercayaan)>81){
            holder.gambar.setImageResource(R.mipmap.flame80);
        }

    }

    @Override
    public int getItemCount() {
        return titiks.size();
    }





}








