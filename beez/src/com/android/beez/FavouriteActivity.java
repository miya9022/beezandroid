package com.android.beez;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.beez.adapter.FavouriteAdapter;
import com.android.beez.api.NewsSourceApiClient;
import com.android.beez.app.AppController;
import com.android.beez.model.NewsBeez;
import com.android.beez.ui.Actionbar;
import com.android.beez.ui.Slidemenu;
import com.android.beez.utils.JSON2Object;
import com.android.beez.utils.Params;
import com.android.beez.utils.ShowMessage;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.analytics.GoogleAnalytics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

public class FavouriteActivity extends MenuActivity implements AbsListView.OnItemClickListener{
	
	private boolean nomoreData = false;
	private com.etsy.android.grid.StaggeredGridView gridView;
	private ArrayList<NewsBeez> newsList;
	private FavouriteAdapter adapter;
	private int rank = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favourite);
		Slidemenu.getInstance().clearMenuActivity(this);
		gridView = (com.etsy.android.grid.StaggeredGridView) findViewById(R.id.favourite_Gridview);
		LoadFavouritePosts(null);
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
				if(time.contains("day") || time.contains("hour") || time.contains("second")){
					newsList.add(news);
				}
				
				if(newsList.size() > 1){
					Collections.sort(newsList, new Comparator<NewsBeez>() {

						@Override
						public int compare(NewsBeez lhs, NewsBeez rhs) {
							return rhs.getView() - lhs.getView();
						}
						
					});
				}
			}
			//concurrent += quota_display;
			if(adapter == null){
				adapter = new FavouriteAdapter(this, newsList);
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
	
	protected void LoadFavouritePosts(View v){
		AppController.getInstance().showProgressDialog(this);
		NewsSourceApiClient apiClient = AppController.getInstance().getNewsApiClient();
		apiClient.showListNews(new Response.Listener<String>() {

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
		NewsBeez entry = newsList.get(position);
		Intent i = new Intent(FavouriteActivity.this, ViewContentActivity.class);
		i.putExtra(Params.TITLE, entry.getTitle());
		i.putExtra(Params.HEADLINE, entry.getHeadline());
		i.putExtra(Params.HEADLINE_IMG, entry.getHeadline_img());
		i.putExtra(Params.APP_DOMAIN, entry.getApp_domain());
		i.putExtra(Params.TIME, entry.getTime());
		i.putExtra(Params.VIEW, entry.getView());
		startActivity(i);
	}
}
