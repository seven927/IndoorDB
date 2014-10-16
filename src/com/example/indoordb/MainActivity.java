package com.example.indoordb;


import com.google.android.gms.maps.model.Marker;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.view.View.OnClickListener;

public class MainActivity extends Activity implements IOnLandmarkSelectedListener {
	
	private static final String URI_CONTENT = "content://com.example.indoordb.provider/location";
	
	private EditText etxtLocation;
	private EditText etxtFloor;
	private EditText etxtId;
	private EditText etxtRoom;
	private Button btnInsert;
	private Button btnClose;
	private RelativeLayout relative;
	
	ContentResolver cResolver;
	
	private DBhelper helper;
	private SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		helper=new DBhelper(this);
		db=helper.getWritableDatabase();
		cResolver=this.getContentResolver();
		
		relative=(RelativeLayout)findViewById(R.id.listLayoutFrame);
		etxtLocation = (EditText)findViewById(R.id.etxtLatlng);
		etxtFloor = (EditText)findViewById(R.id.etxtFloor);
		etxtId = (EditText)findViewById(R.id.etxtId);
		etxtRoom = (EditText)findViewById(R.id.etxtRoom);
		btnInsert = (Button)findViewById(R.id.btnInsert);
		btnClose =  (Button)findViewById(R.id.btnDone);
		
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        IndoorMapFragment mapFragment = new IndoorMapFragment();
        fragmentTransaction.add(R.id.mapLayoutFrame, mapFragment);
        fragmentTransaction.commit();
        
        relative.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				v.requestFocus();
				return true;
			}
		});
        
        btnInsert.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Uri insertUri = Uri.parse(URI_CONTENT);
				String id=etxtId.getText().toString();
				String floor=etxtFloor.getText().toString();
				String room=etxtRoom.getText().toString();
				String latlng=etxtLocation.getText().toString();
				if(id.equals("")||floor.equals("")||room.equals("")||latlng.equals("")){
					Toast.makeText(getApplicationContext(), "Please insert a marker and enter the room number", Toast.LENGTH_SHORT).show();
					return;
				}
				ContentValues values = new ContentValues();
				values.put("room", etxtRoom.getText().toString());
				values.put("latlng",etxtLocation.getText().toString());
				cResolver.insert(insertUri, values);
				Toast.makeText(getApplicationContext(), "Successfully inserted", Toast.LENGTH_SHORT).show();
			}
        	
        });
        
        btnClose.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				db.close();
				Intent intent = new Intent(getApplicationContext(), DisplayActivity.class);
				startActivity(intent);
				//Toast.makeText(getApplicationContext(), "Successfully closed", Toast.LENGTH_SHORT).show();
            }
        	
        });
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onLandmarkSelected(Marker landmark) {
		// TODO Auto-generated method stub
		
		etxtId.setText(landmark.getTitle());  
		Double lat = landmark.getPosition().latitude;
		Double lng = landmark.getPosition().longitude;
		String latlng = lat.toString() + "," + lng.toString();
		etxtLocation.setText(latlng);
		etxtFloor.setText("3");
		etxtRoom.setText("");
		
	}

	@Override
	public void onLandmarkRemoved(Marker landmark) {
		// TODO Auto-generated method stub
		etxtId.setText("");
		etxtLocation.setText("");
		etxtFloor.setText("");
		etxtRoom.setText("");
		
	}

}
