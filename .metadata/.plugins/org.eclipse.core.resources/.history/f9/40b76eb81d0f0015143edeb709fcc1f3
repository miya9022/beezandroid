package com.android.beez.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;
import com.android.beez.R;
import com.android.beez.api.*;
import com.android.beez.db.BeezDatabase;
import com.News.arc.Newsarc.loadimage.ImageCache;
import com.News.arc.Newsarc.loadimage.ImageFetcher;
import com.android.beez.model.NewsBeez;


public class AppController extends Application {
	
	static final String TAG = AppController.class.toString();
	
	public static final String CREATE_NEWS_TABLE = "CREATE TABLE NEWS(ID INTEGER, PLAYLIST_ID INTEGER, NEWS_ID VARCHAR(20), TITLE VARCHAR(200), HEADLINE VARCHAR(200), HEADLINE_IMG VARCHAR(200), ORIGIN_URL VARCHAR(300), APP_DOMAIN VARCHAR(300), PRIMARY KEY(ID))";
	public static final String CREATE_HISTORY = "CREATE TABLE HISTORY(ID INTEGER, PLAYLIST_ID INTEGER, SONG_ID VARCHAR(20), TITLE VARCHAR(200), ARTIST VARCHAR(200), COVER VARCHAR(200), URL VARCHAR(300), ALBUM_ID VARCHAR(300), TIMESTAMP DATETIME DEFAULT CURRENT_TIMESTAMP, PRIMARY KEY(ID))";
	public static final String CREATE_DISPLAYLIST_TABLE = "CREATE TABLE DISPLAYLIST(ID INTEGER, TITLE VARCHAR(200), PRIMARY KEY(ID))";
	
	//public static final String MSG_SHARE_FB = "ã€�News Arcã€‘ç„¡æ–™ã�§éŸ³æ¥½ã�Œè�´ã��æ”¾é¡Œã�®ã‚¢ãƒ—ãƒªï¼�ãƒ€ã‚¦ãƒ³ãƒ­ãƒ¼ãƒ‰ã�—ã�¦ã€�ä½¿ã�£ã�¦ã�¿ã�¦ã�­â™ª\n {DOWNLOAD_URL}";
//	public static final String MSG_SHARE_LINE = "ã€�News Arcã€‘ç„¡æ–™ã�§éŸ³æ¥½ã�Œè�´ã��æ”¾é¡Œã�®ã‚¢ãƒ—ãƒªï¼�ãƒ€ã‚¦ãƒ³ãƒ­ãƒ¼ãƒ‰ã�—ã�¦ã€�ä½¿ã�£ã�¦ã�¿ã�¦ã�­â™ª\n {DOWNLOAD_URL}";
//	public static final String MSG_SHARE_TWITTER = "ã€�News Arcã€‘ç„¡æ–™ã�§éŸ³æ¥½ã�Œè�´ã��æ”¾é¡Œã�®ã‚¢ãƒ—ãƒªï¼�ãƒ€ã‚¦ãƒ³ãƒ­ãƒ¼ãƒ‰ã�—ã�¦ã€�ä½¿ã�£ã�¦ã�¿ã�¦ã�­â™ª\n {DOWNLOAD_URL}";
//	public static final String MSG_SHARE_FB = "ç„¡æ–™ã�§éŸ³æ¥½ã�Œæ¥½ã�—ã‚�ã‚‹News Arcï¼�ç›®ã‚’é–‰ã�˜ã�¦ã€�ã‚†ã�£ã��ã‚Šã�¨éŸ³æ¥½ã‚’è�žã�„ã�¦ã�¿ã�¦ã€‚ãƒ€ã‚¦ãƒ³ãƒ­ãƒ¼ãƒ‰ã‚’ã�—ã�¦ã�¿ã‚ˆã�†ï¼�\n {DOWNLOAD_URL}";
	/**
     * A singleton instance of the application class for easy access in other places
     */
	private static AppController instance;	
	public static synchronized AppController getInstance(){
		return instance;
	}
	
	private Properties properties;	
	/**
	 * Global request queue for Volley
	 */
	private RequestQueue requestQueue;
	
	/**
	 * Global Beez API client
	 */
	private NewsSourceApiClient NewsApiClient;
	
	/**
	 * Global Internet connection state detector 
	 */
	private ConnectionDetector internetConnectionState;
	
	/**
	 * Global Sqlite database connection
	 */
	private BeezDatabase database;
	
	private ImageFetcher imageFetcher;
	private NewsPlayer NewsPlayer;
	private Map<String, Object> sharedPreference;
	
	/**
	 * All favorited song list
	 */
	private Map<String, SongModel> favoritedSongs;
	
	/**
	 * Common use Progress dialog
	 */
	private ProgressDialog progressDialog;
	private Toast toast;
	@Override
	public void onCreate() {
		super.onCreate();
		
		properties = AppProperties.load(getAssets());
		if (properties == null){
			System.err.print("Couldn't find system.properties file, check your assets.");
			return;
		}
		toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
		checkInternet();
		String apiBaseUrl = properties.getProperty("api_base_url");
		String baseUrl = properties.getProperty("base_url");
		String dbName = properties.getProperty("db_name");
		
		// initialize Api client
		NewsApiClient = new NewsXiamiApiClient(baseUrl, apiBaseUrl,getApplicationContext());
		
		// initialize database
		ArrayList<String> tables = new ArrayList<String>(){
			private static final long serialVersionUID = 1L;
			{
				add(CREATE_DISPLAYLIST_TABLE);
				add(CREATE_NEWS_TABLE);
				add(CREATE_HISTORY);
			}
		};
		database = new BeezDatabase(getApplicationContext(), dbName);
		database.createDb(tables);
		
		// initialize Image cache and fetcher 
		ImageCache.ImageCacheParams cacheParams = new ImageCache.ImageCacheParams(this, "img");
		cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 25% of app memory
		imageFetcher = new ImageFetcher(this, 500);
		imageFetcher.addImageCache(null, cacheParams);
		imageFetcher.setImageFadeIn(false);
		
		// initialize in-app shared preference
		sharedPreference = new HashMap<String, Object>();
		
		// initialize all favorited songs
		favoritedSongs = database.getAllFavoritedSongs();
				
		// initlialize the singleton
		instance = this;
		
		SharedPreferences shared = getSharedPreferences();
		//update all share link. we can change version and link  any time, so update every time open app
//		if (!shared.contains("facebook_share_url")){
			final Editor editor = shared.edit();			
			((NewsXiamiApiClient)NewsApiClient).getSNSShareURL(new Response.Listener<String>(){
				@Override
				public void onResponse(String data) {				
					try{
						JSONObject jsonObject = new JSONObject(data);
						String code = jsonObject.getString("code");
						if (!"OK".equals(code)){							
							return;
						}
						
						JSONObject jsonShare = jsonObject.getJSONObject("data").getJSONObject("share");
						if (jsonShare != null){							
							editor.putString("facebook_share_url", jsonShare.getString("facebook"));
							editor.putString("line_share_url", jsonShare.getString("line"));
							editor.putString("twitter_share_url", jsonShare.getString("twitter"));
							editor.putString("link_share_url", jsonShare.getString("link"));
						}
						
						jsonShare = jsonObject.getJSONObject("data").getJSONObject("download");
						editor.putString("android_share_url", jsonShare.getString("android"));
						//editor.putString("ios_share_url", json.getString("ios"));
						editor.commit();
					} catch(Exception e){
						e.printStackTrace();
					}
				}			
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					
				}
			});
//		}
	}
	public void  updateFavariteSongs() {
		favoritedSongs = database.getAllFavoritedSongs();
	}
	/**
	 * Load system.properties in assets directory
	 * 
	 * @return
	 */
	public Properties getSystemProperties() {
		if (properties == null) {
			properties = AppProperties.load(getApplicationContext().getAssets());
		}
		return properties;
	}

	/**
	 * Get system property by key name
	 * 
	 * @param key
	 * @return
	 */
	public String getSystemProperties(String key) {
		Properties prop = getSystemProperties();
		if (prop == null) {
			return null;
		}
		if (prop.containsKey(key)) {
			return prop.getProperty(key);
		}
		return null;
	}

	
	/**
	 * Return AndroidID
	 * 
	 * @return
	 */
	public String getDeviceUDID() {
		return Settings.Secure.getString(getApplicationContext().getContentResolver(),
				Settings.Secure.ANDROID_ID);
	}
	
	/** 
	 * @return The Volley Request queue, the queue will be created if it is null
	 */
	public RequestQueue getRequestQueue(){
		// lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (requestQueue == null) {
        	requestQueue =  Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
	}
	
	/**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     * 
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     * 
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     * 
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }
    }
    
    public NewsSourceApiClient getNewsApiClient(){
    	return newsApiClient;
    }
    
    public NewsSourceApiClient getNewsApiClient(int sourceType){
    	String baseUrl = properties.getProperty("base_url");
    	String apiBaseUrl = properties.getProperty("api_base_url");		
		
    	if (NewsSourceApiClient.SOURCE_TYPE_XIAMI == sourceType){
    		newsApiClient = new NewsXiamiApiClient(baseUrl, apiBaseUrl,getApplicationContext());
    	}
    	return NewsApiClient;
    }

	public ImageFetcher getImageFetcher() {
		return imageFetcher;
	}

	public NewsPlayer getNewsPlayer() {
		if (NewsPlayer == null){
			NewsPlayer = new NewsPlayer(getApplicationContext());			
		}
		return NewsPlayer;
	}

	public Map<String, Object> getSharedMem() {
		if (sharedPreference == null){
			sharedPreference = new HashMap<String, Object>();			
		}
		return sharedPreference;
	}

	public NewsarcDatabase getDatabase() {
		return database;
	}
		
	public int decreasePlaylistAvailable(){
		SharedPreferences shared = getSharedPreferences("Newsarc_playing_options", MODE_PRIVATE);
		Editor editor = shared.edit();
		
		int default_quota = Integer.valueOf(properties.getProperty("playlist_quota", String.valueOf(100)));
		int quota = shared.getInt("playlist_available", default_quota);
				
		if (quota > 0){
			quota--;
		} 
				
		editor.putInt("playlist_available", quota);
		editor.commit();
		
		return quota;
	}
	
	public int increasePlaylistAvailable(){
		SharedPreferences shared = getSharedPreferences("Newsarc_playing_options", MODE_PRIVATE);
		Editor editor = shared.edit();
		
		int default_quota = getDisplayQuota();
		int quota = shared.getInt("playlist_available", default_quota);
		
		editor.putInt("playlist_available", ++quota);
		editor.commit();
		
		return quota;
	}
	public void increasePlaylistAvailable(int num){
		SharedPreferences shared = getSharedPreferences("Newsarc_playing_options", MODE_PRIVATE);
		Editor editor = shared.edit();
		
		int default_quota = getDisplayQuota();
		int quota = shared.getInt("playlist_available", default_quota);
		
		editor.putInt("playlist_available", quota+num);
		editor.commit();
	}
	
	public int increasePlaylistAvailableBySharing(){
		SharedPreferences shared = getSharedPreferences("Newsarc_playing_options", MODE_PRIVATE);
		Editor editor = shared.edit();
		
		int default_quota = getDisplayQuota();
		int quota = shared.getInt("playlist_available", default_quota);		
		int inc = Integer.valueOf(properties.getProperty("share_to_increase", String.valueOf(default_quota)));
		
		quota = quota + inc;
		
		editor.putInt("playlist_available", quota);
		editor.commit();
		
		return quota;
	}
	
	private int getDisplayQuota(){
		return Integer.valueOf(properties.getProperty("display_quota", String.valueOf(100)));
	}
	
	public int getPlaylistAvailable(){
		SharedPreferences shared = getSharedPreferences("Newsarc_playing_options", MODE_PRIVATE);				
		int default_quota = getDisplayQuota();
		int quota = shared.getInt("playlist_available", default_quota);
		return quota;
	}
	
	public SharedPreferences getSharedPreferences(){
		return getSharedPreferences("Newsarc_playing_options", MODE_PRIVATE);
	}
	
	private int getDisplaylistQuota(){
		return Integer.valueOf(properties.getProperty("playlist_quota", String.valueOf(100)));
	}
	
	public int getHistoryQuota(){
		return Integer.valueOf(properties.getProperty("history_quota", String.valueOf(50)));
	}
	
	public synchronized Tracker getTracker(){
		if (!AppController.getInstance().getSharedMem().containsKey("ga_tracker")){			
			String trackingId = getSystemProperties().getProperty("ga_tracking_id");
			
			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			analytics.setDryRun(false);
			analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
			analytics.enableAutoActivityReports(this);
			analytics.setLocalDispatchPeriod(30);
			
			Tracker tracker = analytics.newTracker(trackingId);
			tracker.enableAdvertisingIdCollection(true);
			tracker.enableExceptionReporting(true);
			tracker.enableAutoActivityTracking(true);
			
			AppController.getInstance().getSharedMem().put("ga_tracker", tracker);
		}
		
		return (Tracker)AppController.getInstance().getSharedMem().get("ga_tracker");
	}
	
	public void trackScreen(String screenName){
		try{
			Tracker tracker = getTracker();
			// Set screen name.
			tracker.setScreenName(screenName);

			// Send a screen view.
			tracker.send(new HitBuilders.ScreenViewBuilder().build());			
		} catch(Exception e){
			Log.d("DEBUG trackScreen Exception:", e.getMessage());
		}
	}
	
	public void trackError(Exception e){
		Tracker tracker = getTracker();
		tracker.send(new HitBuilders.ExceptionBuilder()
				.setDescription(Log.getStackTraceString(e.fillInStackTrace()))
				.setFatal(true).build());
	}
	
	public int countSongsOfPlaylist(){		
		return database.countSongsOfPlaylist();
	}

//	public void notifyFavoriteChanged(){
//		favoritedSongs = database.getAllFavoritedSongs();		
//	}
//	
//	public Map<String, SongModel> getFavoritedSongs() {
//		return favoritedSongs;
//	}
//	
//	public boolean checkFavorited(String title){
//		return favoritedSongs.containsKey(title);
//	}
//	public String getIdFavorite(String title){
//		String id=null;
//		try {
//			id=favoritedSongs.get(title).getId();
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		return id;
//	}

	public ConnectionDetector getInternetConnectionState() {
		if (internetConnectionState == null){
			internetConnectionState = new ConnectionDetector(getApplicationContext());
		}
		return internetConnectionState;
	}

	public void showProgressDialog(Activity parent){
		try {
		progressDialog = new ProgressDialog(parent,R.style.NewDialog);		
		progressDialog.setMessage(getApplicationContext().getString(R.string.msg_now_loading));
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setIndeterminateDrawable(parent.getResources().getDrawable(R.drawable.custom_progressbar_color));
		progressDialog.show();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void hideProgressDialog(){
		if (progressDialog != null){
			progressDialog.hide();
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
	public void showToast(String text){
		toast.setText(text);
		toast.show();
	}
	public boolean checkInternet(){
		internetConnectionState = new ConnectionDetector(getApplicationContext());
		if (!internetConnectionState.isConnectingToInternet()){
//			Toast.makeText(getApplicationContext(), 
//					getResources().getString(R.string.msg_no_network_connection_toast), 
//					Toast.LENGTH_SHORT).show();
			toast.setText(getResources().getString(R.string.msg_no_network_connection_toast));
			toast.show();
			return false;
		}
		return true;
	}
}