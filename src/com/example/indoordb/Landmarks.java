package com.example.indoordb;

import java.util.HashMap;
import java.util.Map;

import com.google.android.gms.maps.model.Marker;

public class Landmarks {
	
private Map<String,Marker> markers;
	
	public Landmarks(){
		markers = new HashMap<String, Marker>();
	}
	
	public void addMarker(String id, Marker marker){
		markers.put(id, marker);
	}
	
	public void removeMarker(String id){
	//	markers.get(id).remove();
		markers.remove(id);
	}
	

}
