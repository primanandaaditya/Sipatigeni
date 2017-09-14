package com.trekkon.patigeni.model;

import com.google.android.gms.maps.model.LatLng;
import com.trekkon.patigeni.activities.MapsActivity;


public class LocationMonitor extends RunnableInterface{
	public static final long UPDATE_TIME = 500;

	public static final int PAUSE_EVENT = 0;
	public static final int KILL_EVENT = 1;
	public static final int UN_PAUSE_EVENT = 2;
	public static final int UPDATE_POS = 3;
	public static final int BEGIN_EVENT = 4;

	public long totalTime;
	public LegMap currentLegMap;
	public double distanceRan;
	public double distanceLeft;
	public MapDataManager mdm;
	public MapsActivity mA;
	public LatLng currentPos;
	private boolean running;
	private boolean paused;

	public LocationMonitor(MapsActivity mA, MapDataManager mdm){
		this.mA = mA;
		this.mdm = mdm;
		totalTime= 0;

		if(mdm != null) {
			distanceLeft = mdm.getTotalDistance();

			currentPos = mdm.currentPos;

			running = true;
			paused = false;
		} else {
			//Uh oh, need to error out here ...
			running = false;
		}

		distanceRan = 0.0f;
	}

	@Override
	public void run() {
		long lastTime = System.currentTimeMillis();
		while(running) {
			if(!paused) {
				long curTime = System.currentTimeMillis();
				tick(curTime - lastTime);
				lastTime = curTime;
			}

			handleEvents();
			try {
				Thread.sleep(100);
			} catch(Exception e) { /* ... */}
		}
	}

	@Override
	public void tick(long time) {
		totalTime += time;
	}

	@Override
	public void handleEvents() {
		while(eventQueue.peekEvent() != null) {
			Event e = eventQueue.pollEvent();

			switch(e.type) {
			case PAUSE_EVENT:
				paused = true;
				break;
			case KILL_EVENT:
				running = false;
				break;
			case UN_PAUSE_EVENT:
				paused = false;
				break;
			case UPDATE_POS:
				if(!paused) {
					mdm.updatePosition(e.position);
					double dist = MapDataManager.distance(e.position, currentPos);
					distanceRan += dist;
					distanceLeft -= dist;
					currentPos = e.position;
					updateCurrentLeg();
				}
				break;
			default:
				break;
			}
		}
	}

	private void updateCurrentLeg() {
		LegMap onLegMap = mdm.getLeg(currentPos);

		if(onLegMap != null && onLegMap != currentLegMap) {
			mA.updateCurrentLeg(onLegMap);
		}

		currentLegMap = onLegMap;
	}
}