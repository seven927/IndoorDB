package com.example.indoordb;

import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.UrlTileProvider;

public class IndoorMapFragment extends MapFragment{
	
	IOnLandmarkSelectedListener landmarkListener;
	GoogleMap mMap;
	String mUrl = "http://diorama.ecs.umass.edu/TileTest/Knowlesf3Tilesxyz/{z}/{x}/{y}.png";
	double knowlesLat = 42.393271;
	double knowlesLng = -72.529314;
	int knowlesZoom = 20;
	public String title = "Title ";
	public int titleNumber = 0;
	
	private Landmarks landmarks;
	
	 public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        try {
	            landmarkListener = (IOnLandmarkSelectedListener) activity;
	        } catch (ClassCastException e) {
	            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
	        }
	    }
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			
			View view = super.onCreateView(inflater, container, savedInstanceState);
			return view;
		}
		
		@Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	        setupMap();
	        landmarks = new Landmarks();
		}
		
		private void setupMap(){

			mMap = getMap();
			mMap.setOnMapClickListener(new MyOnMapClickListener());
			mMap.setOnMarkerClickListener(new MyOnMarkerClickListener());
			mMap.setOnMapLongClickListener(new MyOnMapLongClickListener());
			
			changeMapPositionAndZoom(new LatLng(knowlesLat,knowlesLng), knowlesZoom);
			MyUrlTileProvider mTileProvider = new MyUrlTileProvider(256, 256, mUrl);
			mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mTileProvider).zIndex(0));
		
		}
		
		
		private void changeMapPositionAndZoom(LatLng moveToPosition, int zoomLevel){
			changeMapPosition(moveToPosition);
			changeMapZoom(zoomLevel);
		}
		
		private void changeMapPosition(LatLng moveToPosition){
			CameraUpdate center = CameraUpdateFactory.newLatLng(moveToPosition);
			mMap.moveCamera(center);
		}
		
		private void changeMapZoom(int zoomLevel){
			CameraUpdate zoom=CameraUpdateFactory.zoomTo(zoomLevel);
			mMap.animateCamera(zoom);
		}

		public class MyUrlTileProvider extends UrlTileProvider {

			private String baseUrl;

			public MyUrlTileProvider(int width, int height, String url) {
			    super(width, height);
			    this.baseUrl = url;
			}

			@Override
			public URL getTileUrl(int x, int y, int zoom) {
			    try {
			        return new URL(baseUrl.replace("{z}", ""+zoom).replace("{x}",""+x).replace("{y}",""+y));
			    } catch (MalformedURLException e) {
			        e.printStackTrace();
			    }
			    return null;
			}
		}
		
		
		public class MyOnMapClickListener implements OnMapClickListener{
			@Override
			public void onMapClick(LatLng position) {
				
				
				
			}
		}
		
		private class MyOnMarkerClickListener implements OnMarkerClickListener{
			
			@Override
			public boolean onMarkerClick(Marker marker) {
			
				// TODO Auto-generated method stub
				landmarkListener.onLandmarkRemoved(marker);
				landmarks.removeMarker(marker.getTitle());
				marker.remove();
				return false;
				
			}
		}
		
		private class MyOnMapLongClickListener implements OnMapLongClickListener{

			@Override
			public void onMapLongClick(LatLng position) {
				// TODO Auto-generated method stub
				
				String key = title + titleNumber;
				Marker marker = mMap.addMarker(new MarkerOptions().position(position).title(key));
				landmarks.addMarker(key, marker);
				titleNumber++;
				landmarkListener.onLandmarkSelected(marker);	
			}
			
		}
}
