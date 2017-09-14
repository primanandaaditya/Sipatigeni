package com.trekkon.patigeni.activities;

import java.util.ArrayList;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.trekkon.patigeni.model.LegMap;
import com.trekkon.patigeni.model.MapDataManager;
import com.trekkon.patigeni.model.Waypoint;


public abstract class AbstractRuntimeActivity extends FragmentActivity {
	protected static MapDataManager mdm;
	abstract void recreateMap(ArrayList<Waypoint> waypoints, ArrayList<LegMap> legMaps);
}
