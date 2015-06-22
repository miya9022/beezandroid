package com.android.beez;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.xml.datatype.Duration;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.beez.adapter.NewsAdapter;
import com.android.beez.api.NewsSourceApiClient;
import com.android.beez.app.AppController;
import com.android.beez.loadimage.ImageCache;
import com.android.beez.loadimage.ImageFetcher;
import com.android.beez.model.NewsBeez;
import com.android.beez.ui.Actionbar;
import com.android.beez.ui.Slidemenu;
import com.android.beez.utils.JSON2Object;
import com.android.beez.utils.Params;
import com.android.beez.utils.ShowMessage;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewContentActivity extends MenuActivity {

	private ListView listView;
	private boolean nomoreData = false;
	private ArrayList<NewsBeez> newsList;
	private NewsAdapter adapter;
	private String app_domain_display;
	private String title_displayed;
	private int listViewHeight = 0;

	private String origin_url;
	//view holders
	private ImageView iv_headline_img;
	private TextView tv_title;
	private TextView tv_headline;
	private TextView tv_time;
	private TextView tv_app_domain;
	private TextView tv_view;
	private ImageFetcher ifetcher;
	private Button bt_viewmore;

	private ScrollView scrollView;
	private LinearLayout header_layout;

	private String post_id;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_content);
		Slidemenu.getInstance().clearMenuActivity(this);
		Intent intent = getIntent();
		init(intent);
		listView = (ListView) findViewById(R.id.listview);
		onLoadPostsByAppDomain(null);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				NewsBeez entry = newsList.get(position);
				Intent i = new Intent(ViewContentActivity.this, ViewContentActivity.class);
				i.putExtra(Params.TITLE, entry.getTitle());
				i.putExtra(Params.HEADLINE, entry.getHeadline());
				i.putExtra(Params.HEADLINE_IMG, entry.getHeadline_img());
				i.putExtra(Params.APP_DOMAIN, entry.getApp_domain());
				i.putExtra(Params.TIME, entry.getTime());
				i.putExtra(Params.VIEW, entry.getView());
				startActivity(i);
			}
		});
		
		listView.setOnTouchListener(new OnTouchListener() {

		    @SuppressLint("ClickableViewAccessibility") 
		    public boolean onTouch(View v, MotionEvent event) {
		        return (event.getAction() == MotionEvent.ACTION_MOVE);
		    }
		});
		
		
		header_layout = (LinearLayout) findViewById(R.id.header_layout);
	    
	    
	}

//	protected int getActualHeightOfListView(ListView lv){
//		NewsAdapter LvAdapter = (NewsAdapter) lv.getAdapter();
//		int listViewActualHeight = 0;
//	    for (int i = 0; i < LvAdapter.getCount(); i++) {
//	        View mView = LvAdapter.getView(i, null, lv);
//	        mView.measure(
//	                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
//	                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//	        listViewActualHeight += mView.getMeasuredHeight();
//	    }
//	    return listViewActualHeight;
//	}
	
	protected void init(Intent intent){
		
		// get info from intent
		String title = intent.getStringExtra(Params.TITLE);
		String headline = intent.getStringExtra(Params.HEADLINE);
		String headline_img = intent.getStringExtra(Params.HEADLINE_IMG);
		String app_domain = intent.getStringExtra(Params.APP_DOMAIN);
		String time = intent.getStringExtra(Params.TIME);
		origin_url = intent.getStringExtra(Params.ORIGIN_URL);
		post_id = intent.getStringExtra(Params.ID);
		Toast.makeText(getApplicationContext(), origin_url, Toast.LENGTH_LONG).show();
		int view = intent.getIntExtra(Params.VIEW, 0);
		if(!app_domain.isEmpty()){
			app_domain_display = app_domain;
			title_displayed = title;
		}
		
		// initiate views
		tv_title = (TextView) findViewById(R.id.title_clicked);
		tv_title.setText(title);
		tv_headline = (TextView) findViewById(R.id.headline_clicked);
		tv_headline.setText(headline);
		tv_time = (TextView) findViewById(R.id.time_clicked);
		tv_time.setText(time);
		tv_app_domain = (TextView) findViewById(R.id.app_domain_clicked);
		tv_app_domain.setText(app_domain);
		tv_view = (TextView) findViewById(R.id.view_by_clicked);
//		Typeface type = ViewContentActivity.get(this.getApplicationContext(),"fonts/AGENCYB.ttf"); 
//		tv_view.setTypeface(type);
		tv_view.setText(view + " view");
		
		//get image
		iv_headline_img = (ImageView) findViewById(R.id.headline_img_clicked);
		ifetcher = AppController.getInstance().getImageFetcher();
		ifetcher.loadImage(headline_img, iv_headline_img, null);
		
		//action view more button
		bt_viewmore = (Button) findViewById(R.id.view_more);
		bt_viewmore.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ViewContentActivity.this, MoreContentActivity.class);

				intent.putExtra(Params.ID, post_id);
				intent.putExtra(Params.ORIGIN_URL,origin_url);
				startActivity(intent);
			}
		});
	}
	
	private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();
	private static final String TAG = "Typefaces";
	
	public static Typeface get(Context c, String assetPath) {
        synchronized (cache) {
            if (!cache.containsKey(assetPath)) {
                try {
                    Typeface t = Typeface.createFromAsset(c.getAssets(),
                            assetPath);
                    cache.put(assetPath, t);
                } catch (Exception e) {
                    Log.e(TAG, "Could not get typeface '" + assetPath
                            + "' because " + e.getMessage());
                    return null;
                }
            }
            return cache.get(assetPath);
        }
    }
	
	protected void onShowListResponse(String data, String domain) {
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
				if(domain.equals(app_domain) && !title.trim().equals(title_displayed.trim())){
					newsList.add(news);
				}
			}
			//concurrent += quota_display;
			if(adapter == null){
				adapter = new NewsAdapter(this, newsList, true);
				listView.setAdapter(adapter); 
			} else {
				if (newsList.size() > 0) {
					adapter.getEntries().addAll(newsList);
					adapter.notifyDataSetChanged();
				}
			}
			
			for (int i = 0; i < adapter.getCount(); i++) {
		        View mView = adapter.getView(i, null, listView);
		        mView.measure(
		                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
		                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
		        listViewHeight += mView.getMeasuredHeight();
		    }
			scrollView.setMinimumHeight(listViewHeight+header_layout.getHeight());
		} catch(Exception ex){
			ex.printStackTrace();
			nomoreData = true;
		}
	}
	
	protected void onShowListErrorResponse(VolleyError error) {
		nomoreData = true;
	}
	
	protected void onLoadPostsByAppDomain(View v){
		AppController.getInstance().showProgressDialog(this);
		NewsSourceApiClient apiClient = AppController.getInstance().getNewsApiClient();
		apiClient.showListNews(new Response.Listener<String>() {

			@Override
			public void onResponse(String data) {
				onShowListResponse(data, app_domain_display);
				//listView.onRefreshComplete();
				AppController.getInstance().hideProgressDialog();
				
			}
			
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError arg0) {
				onShowListErrorResponse(arg0);
				//listView.onRefreshComplete();
				AppController.getInstance().hideProgressDialog();
				
			}
			
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		Actionbar.getInstance().appendTo(this);
		Slidemenu.getInstance().appendTo(this);
		Actionbar.getInstance().showSlidingMenu(View.VISIBLE);
		Actionbar.getInstance().showBack(View.INVISIBLE);
	}
}
