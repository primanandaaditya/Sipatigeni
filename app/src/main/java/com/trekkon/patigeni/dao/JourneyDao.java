package com.trekkon.patigeni.dao;

import java.util.ArrayList;
import com.google.android.gms.maps.model.LatLng;
import com.trekkon.patigeni.model.LocationModel;
import com.trekkon.patigeni.model.Titik;
import com.trekkon.patigeni.model.TitikApiModel;
import com.trekkon.patigeni.model.Trip;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

public class JourneyDao extends BaseDao{

	public JourneyDao(Context context) {
		super(context);
	}

	@Override
	public void insert(Object obj) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Object obj) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Object viewEntity(String[] args) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}



	public int getMaxRouteNo(int hotspotId) throws Exception{
		Cursor c=null;
		//int maxNo = (Integer) null;
		int maxRouteNo = 0;

		open();
		try{
			String sql = "SELECT max(route_no) as maxRouteNo from Task_journey where hotspot_id ='" + hotspotId + "'";
			c = db.rawQuery(sql, null);
			int gc = c.getCount();
			if (c.moveToFirst()) {
				maxRouteNo = c.getInt(c.getColumnIndex("maxRouteNo"));

			} else {
				maxRouteNo = 0;
			}

		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			if (c != null) c.close();
			close();
		}

		return maxRouteNo;

	}

	public LocationModel getJourneyLastRow() throws Exception{
		LocationModel Lm = null;
		Cursor c = null;
		try{
			open();
			String sql=" SELECT longitude, latitude, route_no, hotspot_id, journey_timestamp FROM Task_journey  ";
			sql +=" order by route_no DESC limit 1 ";
			c= db.rawQuery(sql, null);
			c.moveToFirst();
			while (!c.isAfterLast()) {
				Lm = new LocationModel();
				Lm.setmLongitude(c.getFloat(0));
				Lm.setmLatitude(c.getFloat(1));
				Lm.setmRouteNo(c.getInt(2));
				Lm.setmJourneyId(c.getString(3));
				Lm.setmDateTime(c.getString(4));
				c.moveToNext();
			}


		}catch(Exception e){
			throw e;
		}finally{
			if (c!=null) c.close();
			close();
		}

		return Lm;
	}

	public boolean countJourneyDetail(String taskId) throws Exception{
		boolean isAvail=false;
		String strVal, sql;
		Cursor mCount = null;
		try{
			open();
			sql = "select count(*) from Task_journey where cast(hotspot_id as varchar) = '" + taskId + "'";
			mCount= db.rawQuery( sql, null );
			mCount.moveToFirst();
			if (mCount.getInt(0) != 0){
				isAvail=true;
			}
			mCount.close();
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{

			close();
		}
		return isAvail;

	}

	public ArrayList<LatLng> getAllPoint() throws Exception{
		ArrayList<LatLng> latLngPoints = new ArrayList<LatLng>();
		Cursor c = null;
		try{
			open();
			String sql = "select latitude, longitude  ";
			sql +="FROM Task_journey order by journey_timestamp";
			c=db.rawQuery(sql, null);
			c.moveToFirst();
			while (!c.isAfterLast()) {
				LatLng position = new LatLng(c.getDouble(0), c.getDouble(1));
				latLngPoints.add(position);
				c.moveToNext();
			}
			c.close();

		}catch(Exception e){
			throw e;
		}finally{
			close();
		}
		return latLngPoints;

	}

	public boolean checkJourneyClosed(String taskId) throws Exception {
		boolean isAvail=false;
		String strVal, sql;
		Cursor mCount = null;

		try{

			open();
			sql = "select status from Task where  cast(hotspot_id as varchar) = '" + taskId + "'"  ;
			Log.i("hotspot id", taskId);

			mCount=db.rawQuery(sql, null);

			if (mCount.moveToFirst()) {
				strVal = mCount.getString(mCount.getColumnIndex("status"));
				Log.i("status = ", taskId + " - " + strVal);
				isAvail = strVal.equals("C");
			} else {
				isAvail=false;
			}
			//			}else {
			//				isAvail=false;
			//
			//			}
			mCount.close();

		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			if (mCount != null) mCount.close();
			close();
		}
		return isAvail;
	}

	public ArrayList<Trip> getTrip(String hotspotId) throws Exception{
		ArrayList<Trip> tr = new ArrayList<Trip>();
		Cursor c = null;
		try{
			open();
			String sql = "select jd.route_no, jd.latitude, jd.longitude,  ";
			sql += "jd.accuracy, jd.distance,jd.journey_timestamp, jd.hotspot_id  ";
			sql += " FROM Task_journey jd, Task j where j.hotspot_id = jd.hotspot_id and cast(j.hotspot_id as varchar) = '" + hotspotId + "'";
			sql += " order by jd.route_no  ";

			c=db.rawQuery(sql, null);
			c.moveToFirst();
			while (!c.isAfterLast()) {
				Trip trip = new Trip();
				trip.routeNo = c.getInt(0);
				trip.detailLatitude = c.getDouble(1);
				trip.detailLongitude = c.getDouble(2);
				trip.detailAccuracy = c.getDouble(3);
				trip.detailDistance = c.getDouble(4);
				trip.detailTimeStamp = c.getString(5);
				trip.journey_id = c.getString(6);
				tr.add(trip);
				c.moveToNext();
			}
			c.close();

		}catch(Exception e){
			throw e;
		}finally{
			close();
		}

		return tr;

	}






	public long insertTable(String tableName, ContentValues cVal) throws Exception {
		long rowId = 1;
		try{
			w.lock();
			open();
			db.beginTransaction();
			db.insert(tableName, null, cVal);
			db.setTransactionSuccessful();
			rowId = 1;

		}catch(Exception e){
			rowId = -1;
			throw e;
		}finally{
			db.endTransaction();
			close();
			w.unlock();
		}
		Log.i("rsult insert", String.valueOf(rowId));
		return rowId;
	}

	public void delete(String tableName, String whereClause, String[] whereArgs) throws Exception {
		w.lock();
		try{
			open();
			db.beginTransaction();
			db.delete(tableName, whereClause, whereArgs);
			db.setTransactionSuccessful();
		}catch(Exception e){
			throw e;
		}finally{
			db.endTransaction();
			close();
			w.unlock();
		}

	}

	public long insertJourney(String tableName, ContentValues cVal) throws Exception {
		long rowId = 1;
		try{
			w.lock();
			open();
			db.beginTransaction();
			db.insert(tableName, null, cVal);
			db.setTransactionSuccessful();
			rowId = 1;

		}catch(Exception e){
			rowId = -1;
			throw e;
		}finally{
			db.endTransaction();
			close();
			w.unlock();
		}

		Log.i("rsult insert", String.valueOf(rowId));
		return rowId;
	}

	public Titik getTask(String taskId) throws Exception{
		Titik Lm = null;
		Cursor c = null;
		try{
			open();
			String sql=" SELECT latitude, longitude, by_human, tingkat_kepercayaan, status FROM Task where cast(hotspot_id as varchar) = '" + taskId + "'";
			c= db.rawQuery(sql, null);
			c.moveToFirst();
			while (!c.isAfterLast()) {
				Lm = new Titik();
				Lm.setHotSpotLatitude(c.getString(0));
				Lm.setHotSpotLongitude(c.getString(1));
				Lm.setByHuman(c.getString(2));
				Lm.setTingkatKepercayaan(c.getString(3));
				Lm.setStatus(c.getString(4));
				c.moveToNext();
			}


		}catch(Exception e){
			throw e;
		}finally{
			if (c!=null) c.close();
			close();
		}

		return Lm;
	}

	public boolean countOngoingTask() throws Exception{
		boolean isAvail=false;
		String strVal, sql;
		Cursor mCount = null;
		try{
			open();
			sql = "select count(*) from Task where status = 'X'";
			mCount= db.rawQuery( sql, null );
			mCount.moveToFirst();
			if (mCount.getInt(0) != 0){
				isAvail=true;
			}
			mCount.close();
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{

			close();
		}
		return isAvail;

	}

}
