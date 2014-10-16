package com.example.indoordb;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MyProvider extends ContentProvider{
	
	
	private static final String myURI = "content://com.example.indoordb.provider/location";
	public static final Uri CONTENT_URI = Uri.parse(myURI);
	private DBhelper helper;
	private SQLiteDatabase db;
	
	private static final int LOCATION = 1;
	private static final int LOCATION_ID = 2;
	
	
	
	public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.com.example.provider.location";
	public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.example.provider.location";
	
	
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	static{
		sURIMatcher.addURI("com.example.indoordb.provider", "location", LOCATION);
		sURIMatcher.addURI("com.example.indoordb.provider", "location/#", LOCATION_ID);
	}
	
	
	
	@Override
	public int delete(Uri uri, String arg1, String[] arg2) {
		// TODO Auto-generated method stub
		db = helper.getWritableDatabase();
		int count=0;
		if(sURIMatcher.match(uri)!=LOCATION){
			throw new IllegalArgumentException("Unknown URI"+uri);
		}
		count=db.delete("location", arg1, arg2);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch(sURIMatcher.match(uri)){
		case LOCATION:
			return CONTENT_TYPE;
		case LOCATION_ID:
			return CONTENT_ITEM_TYPE;
		default:
            throw new IllegalArgumentException("Unknown URI"+uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		db = helper.getWritableDatabase();
		long rowId;
		if(sURIMatcher.match(uri)!=LOCATION){
			throw new IllegalArgumentException("Unknown URI"+uri);
		}
		rowId = db.insert("location",null,values);
		if(rowId>0){
			Uri noteUri=ContentUris.withAppendedId(CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(noteUri, null);
			return noteUri;
		}
		throw new IllegalArgumentException("Unknown URI"+uri);
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		helper=new DBhelper(getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		Cursor cursor;
		db = helper.getWritableDatabase();
		if(sURIMatcher.match(uri)!=LOCATION){
			throw new IllegalArgumentException("Unknown URI"+uri);
		}
		cursor = db.query("location",new String[]{"_id", "room", "latlng"}, selection ,selectionArgs,null,null,null,null);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

}
