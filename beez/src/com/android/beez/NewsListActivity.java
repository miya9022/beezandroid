package com.android.beez;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.beez.adapter.NewsAdapter;
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
import com.gcm.GCMManager;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.origamilabs.library.views.StaggeredGridView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

public class NewsListActivity extends MenuActivity implements InterstitialAds.OnInterstitialAdsEventListener, 
	AbsListView.OnItemClickListener {
	private com.etsy.android.grid.StaggeredGridView gridView;
	
	private Button loadMore;
	private NewsAdapter adapter = null;
	private ArrayList<NewsBeez> newsList = null;
	private int quota_display = AppController.getInstance().getDisplayQuota();
	private Queue<NewsBeez> QueueDisplay = null;
	private int concurrent = 0;
	
	private boolean isTop = true;
	private boolean nomoreData = false;
	private boolean isScrollUp = true;
	private ImageButton imgb_scrolltop;
	private int page = 0;
	
	private final String default_img_url = "http://beez.club/img/38x38xfavicon.png.pagespeed.ic.lvWi7wDCqW.png";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
        Slidemenu.getInstance().clearMenuActivity(this);
//      GCMManager.getInstace().init(this);
        // load more
        loadMore = new Button(this);
        loadMore.setText(R.string.btn_more);
		loadMore.setBackgroundColor(R.drawable.mybutton);
		loadMore.setTextColor(getResources().getColor(R.color.white));
		loadMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				onButtonLoadMoreClick(v);
			}
		});
		
		// initiate staggered grid view
		gridView = (com.etsy.android.grid.StaggeredGridView) findViewById(R.id.staggeredGridview);
		gridView.addFooterView(loadMore);
        onButtonLoadMoreClick(null);
        gridView.setOnItemClickListener(this);
        
        // scroll to top
        imgb_scrolltop = (ImageButton) findViewById(R.id.scroll_top);
        imgb_scrolltop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(imgb_scrolltop.getVisibility() != View.GONE && gridView.getFirstVisiblePosition() > 0){
					if(adapter != null) gridView.setAdapter(adapter); 
				}
			}
		});
        gridView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(firstVisibleItem + visibleItemCount >= totalItemCount){
		            
		        }
				if(gridView.getFirstVisiblePosition() == 0){
					imgb_scrolltop.setVisibility(View.GONE);
				} else {
					imgb_scrolltop.setVisibility(View.VISIBLE);
				}
			}
		});
        
        if(nomoreData) loadMore.setVisibility(View.GONE);
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
				loadMore.setVisibility(View.GONE);
				return;
			}
			
			String strData = jsonObject.getString(Params.DATA);
			if(strData == null){
				nomoreData = true;
				loadMore.setVisibility(View.GONE);
				return;
			}
			
			JSONArray jsonItems = new JSONArray(strData);
			if (jsonItems.length() <= 0) {
				nomoreData = true;
				loadMore.setVisibility(View.GONE);
				return;
			}
			newsList = new ArrayList<NewsBeez>();
			int current_length = (jsonItems.length() < concurrent + quota_display) ? jsonItems.length() : concurrent + quota_display;
			if(nomoreData == false){
				for (int i = concurrent; i < current_length; i++) {
					JSONObject item = jsonItems.getJSONObject(i);
					JSON2Object j2o = new JSON2Object(NewsBeez.class, item);
					NewsBeez news = (NewsBeez) j2o.parse();
					String headline_img = item.optString(Params.HEADLINE_IMG, "NULL");
					String time = item.optString(Params.TIME, "NULL");
					String app_domain = item.optString(Params.APP_DOMAIN, "NULL");
					int view = item.optInt(Params.VIEW, 0);
					if (headline_img != null){
						news.setHeadline_img(headline_img);
						news.setTime(time);
						news.setApp_domain(app_domain);
						news.setView(view);
					} else {
						news.setHeadline_img(default_img_url);
					}
					
					if(newsList.size() < quota_display){
						newsList.add(news);
					}
					nomoreData = (i == jsonItems.length()-1) ? true: false;
				}
			}
			concurrent = (nomoreData == false) ? current_length : 0;
			if(adapter == null){
				adapter = new NewsAdapter(this, newsList, false);
				gridView.setAdapter(adapter); 
			} else {
				if (newsList.size() > 0) {
					adapter.getEntries().addAll(newsList);
					adapter.notifyDataSetChanged();
				}
			}
		} catch(Exception ex){
			ex.printStackTrace();
			nomoreData = true;
			loadMore.setVisibility(View.GONE);
		}
		if(nomoreData == true) loadMore.setVisibility(View.GONE);
	}
	
	protected void onShowListErrorResponse(VolleyError error) {
		nomoreData = true;
		loadMore.setVisibility(View.GONE);
	}
	
	protected void onButtonLoadMoreClick(View v){
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
		
		if(gridView.getFirstVisiblePosition() == 0){
			imgb_scrolltop.setVisibility(View.GONE);
		} else {
			imgb_scrolltop.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		NewsBeez entry = newsList.get(position);
		Intent i = new Intent(NewsListActivity.this, ViewContentActivity.class);
		i.putExtra(Params.TITLE, entry.getTitle());
		i.putExtra(Params.HEADLINE, entry.getHeadline());
		i.putExtra(Params.HEADLINE_IMG, entry.getHeadline_img());
		i.putExtra(Params.APP_DOMAIN, entry.getApp_domain());
		i.putExtra(Params.TIME, entry.getTime());
		i.putExtra(Params.VIEW, entry.getView());
		startActivity(i);
	}
}
