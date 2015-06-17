package com.android.beez;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.beez.adapter.FavouriteAdapter;
import com.android.beez.adapter.SuggestAdapter;
import com.android.beez.api.NewsSourceApiClient;
import com.android.beez.app.AppController;
import com.android.beez.model.NewsBeez;
import com.android.beez.ui.Actionbar;
import com.android.beez.ui.InterstitialAds;
import com.android.beez.ui.Slidemenu;
import com.android.beez.utils.JSON2Object;
import com.android.beez.utils.Params;
import com.android.beez.utils.ShowMessage;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.analytics.GoogleAnalytics;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

public class SuggestActivity extends MenuActivity implements AbsListView.OnItemClickListener{
	
	private boolean nomoreData = false;
	private com.etsy.android.grid.StaggeredGridView gridView;
	private ArrayList<NewsBeez> newsList;
	private SuggestAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suggest);
		Slidemenu.getInstance().clearMenuActivity(this);
		gridView = (com.etsy.android.grid.StaggeredGridView) findViewById(R.id.suggest_Gridview);
		LoadSuggestPosts(null);
		gridView.setOnItemClickListener(this);
	}
	
	protected void onShowListResponse(String data) {
		try{
			JSONObject jsonObject = new JSONObject(data);
			String code = jsonObject.getString("code");
			if(Params.ERROR_10010.equals(code)){
				ShowMessage.showDialogUpdateApp(this);
				return;
			}
			if (!"OK".equals(code)) {
				nomoreData = true;
				return;
			}
			
			String strData = jsonObject.getString(Params.DATA);
			if(strData == null){
				nomoreData = true;
				return;
			}
			
			JSONArray jsonItems = new JSONArray(strData);
			if (jsonItems.length() <= 0) {
				nomoreData = true;
				return;
			}
			newsList = new ArrayList<NewsBeez>();
			for (int i = 0; i < jsonItems.length(); i++) {
				JSONObject item = jsonItems.getJSONObject(i);
				JSON2Object j2o = new JSON2Object(NewsBeez.class, item);
				NewsBeez news = (NewsBeez) j2o.parse();
				String title = item.optString(Params.TITLE, "NULL");
				String headline = item.optString(Params.HEADLINE, "NULL");
				String headline_img = item.optString(Params.HEADLINE_IMG, "NULL");
				String time = item.optString(Params.TIME, "NULL");
				String app_domain = item.optString(Params.APP_DOMAIN, "NULL");
				int view = item.optInt(Params.VIEW, 0);
				if (headline_img != null){
					news.setTitle(title);
					news.setHeadline_img(headline_img);
					news.setHeadline(headline);
					news.setTime(time);
					news.setApp_domain(app_domain);
					news.setView(view);
				}
				newsList.add(news);
			}
			//concurrent += quota_display;
			if(adapter == null){
				adapter = new SuggestAdapter(this, newsList);
				gridView.setAdapter(adapter); 
			} else {
				if (newsList.size() > 0) {
					adapter.getEntries().addAll(newsList);
				}
			}
		} catch(Exception ex){
			ex.printStackTrace();
			nomoreData = true;
		}
	}
	
	protected void onShowListErrorResponse(VolleyError error) {
		nomoreData = true;
	}
	
	protected void LoadSuggestPosts(View v){
		AppController.getInstance().showProgressDialog(this);
		NewsSourceApiClient apiClient = AppController.getInstance().getNewsApiClient();
		apiClient.showRecommendPosts(new Response.Listener<String>() {

			@Override
			public void onResponse(String data) {
				onShowListResponse(data);
				AppController.getInstance().hideProgressDialog();
				
			}
			
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				onShowListErrorResponse(arg0);
				AppController.getInstance().hideProgressDialog();
				
			}
			
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Get an Analytics tracker to report app starts & uncaught exceptions
		// etc.
		GoogleAnalytics.getInstance(this).reportActivityStart(this);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Actionbar.getInstance().appendTo(this);
		Slidemenu.getInstance().appendTo(this);
		Actionbar.getInstance().showSlidingMenu(View.VISIBLE);
		Actionbar.getInstance().showBack(View.INVISIBLE);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
	}
}
