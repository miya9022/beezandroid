package com.android.beez.db;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class SqliteDatabase{
	protected String dbName;
	protected SQLiteDatabase db;
	protected Context context;
	
	public SqliteDatabase(Context context, String dbName) {
		this.setDbName(dbName);
		this.context = context;
	}
	
	public void createDb(ArrayList<String> tables){
		if (this.dbName == null || "".equals(this.dbName)){
			return;
		}
		
		File dbFile = this.context.getDatabasePath(dbName);
		if (!dbFile.exists()){
			Iterator<String> it = tables.iterator();
			while(it.hasNext()) {
			    String sql = it.next();
			    this.exec(sql);		    
			}
		}		
	}
	
	public void exec(String sql){
		if (db == null || !db.isOpen()){
			db = this.context.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
		}
		
		db.execSQL(sql);
		db.close();
	}
	
	public Cursor query(String sql){
		if (db == null || !db.isOpen()){
			db = this.context.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
		}
		
		Cursor cursor = db.rawQuery(sql, null);
		return cursor;
	}
	
	public void delete(String table, String where){
		if (db == null || !db.isOpen()){
			db = this.context.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
		}
		
		db.delete(table, where, null);		
		db.close();
	}
	
	public void update(String table, ContentValues newValues, String where){
		if (db == null || !db.isOpen()){
			db = this.context.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
		}
		
		db.update(table, newValues, where, null);		
		db.close();
	}
	
	public long insert(String table, ContentValues values){
		if (db == null || !db.isOpen()){
			db = this.context.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
		}
		
		long id = db.insert(table, null, values);		
		db.close();
		
		return id;
	}
	
	public void close(){
		if (db != null && db.isOpen()){
			db.close();
		}
	}
	
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public SQLiteDatabase getDb() {
		return db;
	}

	public void setDb(SQLiteDatabase db) {
		this.db = db;
	}	
}
