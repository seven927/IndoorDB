package com.example.indoordb;

import java.util.HashSet;
import java.util.Iterator;

import com.google.android.gms.ads.doubleclick.c;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class DisplayActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor>{
	
	private static final Uri CONTENT_URI = Uri.parse("content://com.example.indoordb.provider/location");
	
	SimpleCursorAdapter mAdapter;
	ListView lv;
	ActionMode mActionMode;
	HashSet<String> roomset;
	ContentResolver cResolver;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display);
		roomset=new HashSet<String>();
		cResolver=getContentResolver();
		lv=(ListView)findViewById(android.R.id.list);	
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		lv.setMultiChoiceModeListener(mActionModeCallback);
		
		mAdapter= new SimpleCursorAdapter(this, android.R.layout.simple_list_item_multiple_choice, null, new String[]{"room"}, new int[]{android.R.id.text1}, 0);
		setListAdapter(mAdapter);
		getLoaderManager().initLoader(0, null, this);
	}
	
	

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		getLoaderManager().restartLoader(0, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		// TODO Auto-generated method stub
		return new CursorLoader(this,CONTENT_URI,new String[]{"_id", "room", "latlng"},null,null,null);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		// TODO Auto-generated method stub
		mAdapter.swapCursor(arg1);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		mAdapter.swapCursor(null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.display, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.action_insert:
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(intent);
			return true;	
		}
		return false;
	}
	
	private AbsListView.MultiChoiceModeListener mActionModeCallback = new AbsListView.MultiChoiceModeListener(){

		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			// TODO Auto-generated method stub
			switch (item.getItemId()) {
            case R.id.action_delete:
            	Thread thread = new Thread(new Runnable(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						deleteLocation();
					}});
            	thread.start();
                mode.finish(); // Action picked, so close the CAB
                return true;
			}
			return false;
		}

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// TODO Auto-generated method stub
			 MenuInflater inflater = mode.getMenuInflater();
		     inflater.inflate(R.menu.context, menu);
		     return true;
		}

		@Override
		public void onDestroyActionMode(ActionMode mode) {
			// TODO Auto-generated method stub
			mActionMode=null;
		}

		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onItemCheckedStateChanged(ActionMode mode, int position,
				long id, boolean checked) {
			// TODO Auto-generated method stub
			Cursor c=(Cursor)mAdapter.getItem(position);
			String room=c.getString(c.getColumnIndex("room"));
			if(checked)
				roomset.add(room);
			else
				roomset.remove(room);
		}
		
	};
	
	private void deleteLocation(){
		Iterator<String> it = roomset.iterator();
		while(it.hasNext()){
			cResolver.delete(CONTENT_URI, "room=?",new String[]{it.next()});
		}
		myhandler.sendMessage(Message.obtain(myhandler, 0));
	}
	
	Handler myhandler = new Handler(){
		public void handleMessage(Message msg){
			switch(msg.what){
			case 0:
				getLoaderManager().restartLoader(0, null, DisplayActivity.this);
			}
		}
	};
}

