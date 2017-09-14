package com.trekkon.patigeni.dao;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.trekkon.patigeni.model.Titik;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Trekkon on 24/08/2017.
 */

public class DbAdapter extends BaseDao {

    public DbAdapter(Context context) {
        super(context);
    }

    @Override
    public void insert(Object obj) throws Exception {

    }

    @Override
    public void update(Object obj) throws Exception {

    }

    @Override
    public Object viewEntity(String[] args) throws Exception {
        return null;
    }

    public ArrayList<Titik> getHotSpot(String userId) throws Exception {
        ArrayList<Titik> hotList = new ArrayList<Titik>();
        Log.i("user id on dao", userId);
        Cursor c=null;
        try{
            open();
            String sql = "select distinct t.hotspot_id, t.latitude, t.longitude, t.timestamp_original, t.tingkat_kepercayaan," ;
            sql += " t.by_human, t.timestamp_received, t.status " ;
            sql += " from Task t " ;
            sql += " where t.contact_id ='" + userId + "'";
//            sql += " and cast(t.hotspot_id as varchar) = '2332'";
            sql += " order by t.status desc, t.tingkat_kepercayaan desc, t.timestamp_received";

            c=db.rawQuery(sql, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                Titik p = new Titik();
                p.setHotspotId(c.getString(0));
                p.setHotSpotLatitude(c.getString(1));
                p.setHotSpotLongitude(c.getString(2));
                p.setTimestampOrginal(c.getString(3));
                p.setTingkatKepercayaan(c.getString(4));
                p.setByHuman(c.getString(5));
                p.setTimestampReceived(c.getString(6));
                p.setStatus(c.getString(7));
                hotList.add(p);
                c.moveToNext();
            }
            c.close();

        }catch(Exception e){
            throw e;
        }finally{
            close();
        }
        return hotList;
    }
}
