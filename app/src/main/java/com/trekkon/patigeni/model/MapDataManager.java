package com.trekkon.patigeni.model;

import java.util.ArrayList;

import android.location.Location;



import com.google.android.gms.maps.model.LatLng;



public class MapDataManager {
	//	Need to find suitible distance threshold
	private static final double DIST_THRESHOLD = 0.5f;

	private ArrayList<Waypoint> waypoints;
	private ArrayList<LegMap> legMaps;
	public LatLng currentPos;

	public MapDataManager() {
		waypoints = new ArrayList<Waypoint>();
		legMaps = new ArrayList<LegMap>();
		currentPos = new LatLng(0.0f, 0.0f);
	}

	public ArrayList<Waypoint> getWaypoints() {
		return waypoints;
	}

	public ArrayList<LegMap> getLegMaps() {
		return legMaps;
	}

	public void addWaypoint(Waypoint w) {
		waypoints.add(w);

		if(w.legMapA != null) {
			if(!legMaps.contains(w.legMapA)) {
				legMaps.add(w.legMapA);
			}
		}

		if(w.legMapB != null) {
			if(!legMaps.contains(w.legMapB)) {
				legMaps.add(w.legMapB);
			}
		}
	}

	public void addLeg(LegMap l) {
		if(!waypoints.contains(l.wayA)) {
			waypoints.add(l.wayA);
		}

		if(!waypoints.contains(l.wayB)) {
			waypoints.add(l.wayB);
		}

		if(!legMaps.contains(l)) {
			legMaps.add(l);
		}
	}

	public boolean removeElement(MapElement e) {
		if(e instanceof Waypoint) {
			return removeWaypoint((Waypoint) e);
		} else {
			return removeLeg((LegMap) e);
		}
	}

	public boolean removeLeg(LegMap l) {
		if(l.wayA != null) {
			if(l.wayA.legMapA == l) {
				l.wayA.legMapA = null;
			} else {
				l.wayA.legMapB = null;
			}
		}

		if(l.wayB != null) {
			if(l.wayB.legMapA == l) {
				l.wayB.legMapA = null;
			} else {
				l.wayB.legMapB = null;
			}
		}

		return legMaps.remove(l);
	}

	public boolean removeWaypoint(Waypoint w) {
		if(w.legMapA != null) {
			if(w.legMapA.wayA == w) {
				w.legMapA.wayA = null;
			} else {
				w.legMapA.wayB = null;
			}
		}

		if(w.legMapB != null) {
			if(w.legMapB.wayA == w) {
				w.legMapB.wayA = null;
			} else {
				w.legMapB.wayB = null;
			}
		}

		return waypoints.remove(w);
	}

	public MapElement getElement(LatLng p) {
		Waypoint closestWaypoint = getWaypoint(p);
		LegMap closestLegMap = getLeg(p);

		double wayDist = distance(p, closestWaypoint.centerPoint);
		double legDist = distance(p, closestLegMap.centerPoint);

		MapElement r = (wayDist > legDist)  ? closestLegMap : closestWaypoint;

		if(distance(r.centerPoint, p) < DIST_THRESHOLD) return r;
		else return null;
	}

	public Waypoint getWaypoint(LatLng p)  {
		Waypoint r = null;
		double lastDist = 0.0f;
		/*
		 * 
		 * 

		for(int i = 0; i < waypoints.size(); i++) {
			if(lastDist == 0.0f) {
				r = waypoints.get(i);
				lastDist = distance(p, r.centerPoint);

				continue;
			}
			double newDist = distance(p, waypoints.get(i).centerPoint);

			if(newDist < lastDist) {
				lastDist = newDist;
				r = waypoints.get(i);
			}
		}
		*/
		for(int i=0; i<waypoints.size(); i++){
			Waypoint temp = waypoints.get(i);
			if(temp.centerPoint.equals(p)){
				return temp;
			}
		}
		return r;
	}

	public LegMap getLeg(LatLng p) {
		LegMap r = null;
		double lastDist = 0.0f;

		for(int i = 0; i < legMaps.size(); i++) {
			//	If r is null, find the closest point in the first leg
			if(r == null) {
				r = legMaps.get(i);
				lastDist = -1.0f;

				for(int j = 0; j < r.points.size(); j++) {
					if(lastDist == -1.0f) {
						lastDist = distance(p, r.points.get(j));
						continue;
					}

					double t = distance(p, r.points.get(j));

					if(lastDist > t) {
						lastDist = t;
					}
				}

				continue;
			}

			double newDist = -1.0f;

			//	Find closest point in this leg
			for(int j = 0; j < legMaps.get(i).points.size(); j++) {
				if(newDist == -1.0f) {
					newDist = distance(p, legMaps.get(i).points.get(j));
					continue;
				}

				double t = distance(p, legMaps.get(i).points.get(j));

				if(t < newDist) {
					newDist = t;
				}
			}

			if(newDist < lastDist && newDist != -1) {
				lastDist = newDist;
				r = legMaps.get(i);
			}
		}

		return r;
	}

	public void updatePosition(LatLng p) {
		currentPos = p;
	}

	public double getTotalDistance() {
		double totalDistance = 0.0f;

		for(LegMap l : legMaps) {
			if(l.points.size() <= 0) continue;

			LatLng lastPos = l.points.get(0);
			for(int i = 1; i < l.points.size(); i++) {
				double thisDist = distance(lastPos, l.points.get(i));
				totalDistance += thisDist;
				lastPos = l.points.get(i);
			}
		}

		return totalDistance;
	}

	public void printMapData() {
		for(int i = 0; i < waypoints.size(); i++) {
			Waypoint ww = waypoints.get(i);
			System.out.printf("\tWaypoint at (%f, %f) with Legs: \n\t\t", ww.centerPoint.latitude, ww.centerPoint.longitude);
		

			for(int j = 0; ww.legMapA != null && j < ww.legMapA.points.size(); j++) {
				System.out.printf("(%f, %f) ", ww.legMapA.points.get(j).latitude, ww.legMapA.points.get(j).longitude);
			}

			System.out.printf("\n\t\t");

			for(int j = 0; ww.legMapB != null && j < ww.legMapB.points.size(); j++) {
				System.out.printf("(%f, %f) ", ww.legMapB.points.get(j).latitude, ww.legMapB.points.get(j).longitude);
			}

			System.out.printf("\n");
		}
	}


	public static double distance(LatLng a, LatLng b) {
		Location la = new Location("A");
		la.setLatitude(a.latitude);
		la.setLongitude(a.longitude);
		
		Location lb = new Location("B");
		lb.setLatitude(b.latitude);
		lb.setLongitude(b.longitude);
		
		return Math.abs(la.distanceTo(lb));
	}
}