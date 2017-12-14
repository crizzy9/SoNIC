package com.example.android.materialdesigncodelab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;

public class ImageUriDatabase {
	private static final String CREATE_DATABASE = "create table URI_TABLE (_id integer primary key autoincrement," + "_uri text not null);";
	private static final String DATABASE_NAME = "IMAGE_URI_DATABSE";
	private static final String DATABASE_TABLE_URI = "URI_TABLE";
	private static final int DATABASE_VERSION = 1;
	
	public static final String ENTITY_ID = "_id";
	public static final String PATH_NAME = "_uri";
	
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	
	public ImageUriDatabase(Context context) {
		DBHelper = new DatabaseHelper(context);
	}
	
	private void open(){
		if(db!=null){
			if(!db.isOpen()){
				db = DBHelper.getWritableDatabase();
			}
		}
		else{
			db = DBHelper.getWritableDatabase();
		}
	}
	
	public void insertUri(String uri){
		open();
		ContentValues values = new ContentValues();
		values.put(PATH_NAME, uri);
		db.insert(DATABASE_TABLE_URI, null, values);
	}
	
	public Cursor getallUri(){
		Cursor cursor = null;
		open();
		cursor = db.query(DATABASE_TABLE_URI, new String[]{}, null, null, null, null, ENTITY_ID + " DESC");
		if(cursor!= null){
			cursor.moveToFirst();
		}
		return cursor;
	}
	
	
	public static boolean checkExternalStorage(){
		boolean mExternalStorageAvailable = false;		
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    mExternalStorageAvailable = true;
		}
		else {
		    mExternalStorageAvailable = false;
		}
		return mExternalStorageAvailable;
	}
	
	public static String getDatabasePath() {
		String dbPath = DATABASE_NAME;
		if(checkExternalStorage()){
			File direct = new File(Environment.getExternalStorageDirectory()+ "/ImageUri");
			if (!direct.exists()) {
				if (direct.mkdir());
			}
			dbPath =direct.getAbsolutePath()+"/"+DATABASE_NAME;
		}
		return dbPath;
	}
	
	public static class DatabaseHelper extends SQLiteOpenHelper {
		public DatabaseHelper(Context context) {
			super(context, getDatabasePath(), null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_DATABASE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS titles");
			onCreate(db);
		}
	}
}
