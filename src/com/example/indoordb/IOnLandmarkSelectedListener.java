package com.example.indoordb;

import com.google.android.gms.maps.model.Marker;

public interface IOnLandmarkSelectedListener {
	
	public void onLandmarkSelected(Marker landmark);
	public void onLandmarkRemoved(Marker landmark);

}
