package com.android.beez.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.android.beez.model.NewsBeez;
import com.android.beez.utils.Params;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class BeezDatabase extends SqliteDatabase{
	public BeezDatabase(Context context, String dbName) {
		super(context, dbName);
	}

	public ArrayList<Map<String, String>> getPlaylist(){
		/**
		 * CREATE TABLE PLAYLIST(
		 * ID INTEGER, 
		 * TITLE VARCHAR(200), 
		 * PRIMARY KEY(ID))
		 */
		final String SELECT_PLAYLIST = "SELECT ID, TITLE FROM PLAYLIST";
		Cursor cursor = query(SELECT_PLAYLIST);
		if (cursor == null) return null;
		
		ArrayList<Map<String, String>> playlist = new ArrayList<Map<String, String>>();
		if (cursor.moveToFirst()) {
			do {
				
				Map<String, String> item = new HashMap<String, String>();
				item.put("ID", String.valueOf(cursor.getInt(0)));
				item.put("TITLE", cursor.getString(1));				
				playlist.add(item);
			} while (cursor.moveToNext());
		}
		close();
		if(cursor != null)
	        cursor.close();
		return playlist;
	}
	
	public ArrayList<NewsBeez> getHistory(){
		/**
		 * CREATE TABLE HISTORY(
		 * ID INTEGER, 
		 * PLAYLIST_ID INTEGER, 
		 * SONG_ID VARCHAR(20), 
		 * TITLE VARCHAR(200), 
		 * ARTIST VARCHAR(200), 
		 * COVER VARCHAR(200), 
		 * URL VARCHAR(300), 
		 * PRIMARY KEY(ID))
		 */		 
		final String SELECT_SONG_OF_PLAYLIST = "SELECT * FROM HISTORY ORDER BY ID DESC";
		Cursor cursor = query(SELECT_SONG_OF_PLAYLIST);
		if (cursor == null) return null;
		
		ArrayList<NewsBeez> list = new ArrayList<NewsBeez>();
		if (cursor.moveToFirst()) {
			do {
				NewsBeez nb = new NewsBeez();
				nb.setId(cursor.getString(0));
				nb.setTitle(cursor.getString(2));
				nb.setHeadline(cursor.getString(3));
				nb.setHeadline_img(cursor.getString(4));
				nb.setOrigin_url(cursor.getString(5));
				nb.setApp_domain(cursor.getString(6));
				nb.setCate_id(cursor.getString(7));
				nb.setShort_link(cursor.getString(8));
				nb.setTime(cursor.getString(9));
				nb.setView(cursor.getInt(10));
				list.add(nb);
			} while (cursor.moveToNext());
		}
		close();
		if(cursor != null)
	        cursor.close();
		return list;
	}
	
	public ArrayList<NewsBeez> getSongsOfPlaylist(int playlistId){
		/**
		 * CREATE TABLE SONG(
		 * ID INTEGER, 
		 * PLAYLIST_ID INTEGER, 
		 * SONG_ID VARCHAR(20), 
		 * TITLE VARCHAR(200), 
		 * ARTIST VARCHAR(200), 
		 * COVER VARCHAR(200), 
		 * URL VARCHAR(300), 
		 * PRIMARY KEY(ID))
		 */		 
		final String SELECT_SONG_OF_PLAYLIST = "SELECT * FROM SONG WHERE PLAYLIST_ID = " + String.valueOf(playlistId);
		Cursor cursor = query(SELECT_SONG_OF_PLAYLIST);
		if (cursor == null) return null;
		
		ArrayList<NewsBeez> list = new ArrayList<NewsBeez>();
		if (cursor.moveToFirst()) {
			do {
				NewsBeez nb = new NewsBeez();
				nb.setId(cursor.getString(0));
				nb.setTitle(cursor.getString(2));
				nb.setHeadline(cursor.getString(3));
				nb.setHeadline_img(cursor.getString(4));
				nb.setOrigin_url(cursor.getString(5));
				nb.setApp_domain(cursor.getString(6));
				nb.setCate_id(cursor.getString(7));
				nb.setShort_link(cursor.getString(8));
				nb.setTime(cursor.getString(9));
				nb.setView(cursor.getInt(10));
				list.add(nb);
			} while (cursor.moveToNext());
		}
		close();
		if(cursor != null)
	        cursor.close();
		return list;
	}
	
//	public Map<String, NewsBeez> getAllFavoritedSongs(){
//		Cursor cursor = query("SELECT * FROM SONG");
//		if (cursor == null) return null;
//		
//		Map<String, NewsBeez> list = new HashMap<String, NewsBeez>();
//		if (cursor.moveToFirst()) {
//			do {
//				NewsBeez nb = new NewsBeez();
//				nb.setId(cursor.getString(0));
//				nb.setTitle(cursor.getString(2));
//				nb.setHeadline(cursor.getString(3));
//				nb.setHeadline_img(cursor.getString(4));
//				nb.setOrigin_url(cursor.getString(5));
//				nb.setApp_domain(cursor.getString(6));
//				nb.setCate_id(cursor.getString(7));
//				nb.setShort_link(cursor.getString(8));
//				nb.setTime(cursor.getString(9));
//				nb.setView(cursor.getInt(10));
//				list.put(s.getSongName(), s);
//			} while (cursor.moveToNext());
//		}
//		close();
//		if(cursor != null)
//	        cursor.close();
//		return list;
//	}
	
	public long addDisplayList(String title){
		ContentValues values = new ContentValues();
		values.put(Params.TITLE, title);
		return insert(Params.DISPLAY_LIST, values);		
	}
	
	public long addHistory(NewsBeez news){		
		ContentValues values = new ContentValues();
		values.put(Params.ID, news.getId());
		values.put(Params.TITLE, news.getTitle());
		values.put(Params.HEADLINE, news.getHeadline());
		values.put(Params.HEADLINE_IMG, news.getHeadline_img());
		values.put(Params.ORIGIN_URL, news.getOrigin_url());
		values.put(Params.APP_DOMAIN, news.getApp_domain());
		values.put(Params.CATE_ID, news.getCate_id());
		values.put(Params.TIME, news.getTime());
		values.put(Params.VIEW, news.getView());
		
//		String albumId=song.getAlbumId();
//		if(albumId==null || albumId.length()==0) albumId="-1";
//		values.put("ALBUM_ID", albumId);
		return insert(Params.HISTORY, values);
	}
	
	public long addNewsToPlaylist(NewsBeez news){
		ContentValues values = new ContentValues();
		values.put(Params.ID, news.getId());
		values.put(Params.TITLE, news.getTitle());
		values.put(Params.HEADLINE, news.getHeadline());
		values.put(Params.HEADLINE_IMG, news.getHeadline_img());
		values.put(Params.ORIGIN_URL, news.getOrigin_url());
		values.put(Params.APP_DOMAIN, news.getApp_domain());
		values.put(Params.CATE_ID, news.getCate_id());
		values.put(Params.TIME, news.getTime());
		values.put(Params.VIEW, news.getView());
		return insert("SONG", values);
	}
	
	public void changeDisplayListTitle(String newTitle, int playlistId){
		ContentValues newVals = new ContentValues();
		newVals.put(Params.TITLE, newTitle);
		update(Params.DISPLAY_LIST, newVals, "ID=" + String.valueOf(playlistId));
	}
	
	public void deleteDisplaylist(int displaylistId){
		delete(Params.DISPLAY_LIST, "ID = " + String.valueOf(displaylistId));
		
		ArrayList<NewsBeez> songs = getSongsOfPlaylist(displaylistId);
		Iterator<NewsBeez> it = songs.iterator();
		while(it.hasNext()){
			NewsBeez nb = it.next();
			deleteNewsFromPlaylist(nb.id);
		}
	}
	
	public void deleteNewsFromPlaylist(String newsId){
		delete(Params.NEWS, "ID =" + newsId);
	}
	
	public void deletNewsFromDisplaylist(NewsBeez news){
		deleteNewsFromPlaylist(news.getId());
	}
	
	public void deleteNewsFromDisplaylist(NewsBeez song, int displaylistId){
		delete(Params.NEWS, "ID = " + song.getId() + " AND "+ Params.DISPLAY_LIST_ID +" = " + String.valueOf(displaylistId));
	}
	
	public void deleteNewsFromHistory(NewsBeez song){
		deleteNewsFromHistory(song.getId());
	}
	
	public void deleteNewsFromHistory(String songId){
		delete(Params.HISTORY, "ID=" + songId);
	}
	
	public void deleteOldestNewsFromHistory(){
		final String SELECT_SONG_OF_PLAYLIST = "SELECT * FROM HISTORY WHERE TIMESTAMP IN (SELECT MIN(TIMESTAMP) FROM HISTORY)";
		Cursor cursor = query(SELECT_SONG_OF_PLAYLIST);
		if (cursor == null) return;		
		if (cursor.moveToFirst()) {
			do {
				deleteNewsFromHistory(cursor.getString(0));				
			} while (cursor.moveToNext());
		}
		close();
		if(cursor != null)
	        cursor.close();
	}
	
	public int countDisplaylist(){
		Cursor cursor = query("SELECT * FROM PLAYLIST");
		int cnt = (cursor == null)?0:cursor.getCount();
		close();
		if(cursor != null)
	        cursor.close();
		return cnt;
	}
	
	public int countNewsOfDisplaylist(int playlistId){
		Cursor cursor = query("SELECT * FROM SONG WHERE PLAYLIST_ID=" + String.valueOf(playlistId));
		int cnt = (cursor == null)?0:cursor.getCount();
		close();
		if(cursor != null)
	        cursor.close();
		return cnt;
	}
	
	public int countSongsOfPlaylist(){
		Cursor cursor = query("SELECT * FROM SONG");
		int cnt = (cursor == null)?0:cursor.getCount();
		close();
		if(cursor != null)
	        cursor.close();
		return cnt;
	}
	
	public int countSongsOfHistory(){
		Cursor cursor = query("SELECT * FROM HISTORY");
		int cnt = (cursor == null)?0:cursor.getCount();
		close();
		if(cursor != null)
	        cursor.close();
		return cnt;
	}
	
	public boolean checkExistPlaylist(int playlistId){
		Cursor cursor = query("SELECT * FROM PLAYLIST WHERE ID = " + String.valueOf(playlistId));
		boolean ret = (cursor != null && cursor.getCount() > 0);
		close();
		if(cursor != null)
	        cursor.close();
		return ret;
	}
	
	public boolean checkExistPlaylist(String title){
		Cursor cursor = query("SELECT * FROM PLAYLIST WHERE TITLE = '" + title.replace("'", "''").replace("\"", "\"\"") + "'");
		boolean ret = (cursor != null && cursor.getCount() > 0);
		close();
		if(cursor != null)
	        cursor.close();
		return ret;
	}	
	
	public boolean checkExistHistory(int songId){
		Cursor cursor = query("SELECT * FROM HISTORY WHERE ID = " + String.valueOf(songId));
		boolean ret = (cursor != null && cursor.getCount() > 0);
		close();
		if(cursor != null)
	        cursor.close();
		return ret;
	}
	
	public boolean checkExistHistory(String songTitle){
		Cursor cursor = query("SELECT * FROM HISTORY WHERE TITLE = '" + songTitle.replace("'", "''").replace("\"", "\"\"") + "'");
		boolean ret = (cursor != null && cursor.getCount() > 0);
		close();
		if(cursor != null)
	        cursor.close();
		return ret;
	}
	
	public boolean checkExistSongInPlaylist(String title, int playlistId){
		Cursor cursor = query("SELECT * FROM SONG WHERE TITLE = '" + title.replace("'", "''").replace("\"", "\"\"") + "' AND PLAYLIST_ID =" + String.valueOf(playlistId));
		boolean ret = (cursor != null && cursor.getCount() > 0);
		close();
		if(cursor != null)
	        cursor.close();
		return ret;
	}
	
	public boolean checkExistSongInPlaylist(String title){
		Cursor cursor = query("SELECT * FROM SONG WHERE TITLE = '" + title.replace("'", "''").replace("\"", "\"\"") + "'");
		boolean ret = (cursor != null && cursor.getCount() > 0);
		close();
		if(cursor != null)
	        cursor.close();
		return ret;
	}
}
