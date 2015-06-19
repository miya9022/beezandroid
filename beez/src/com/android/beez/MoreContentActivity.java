package com.android.beez;

import com.android.beez.api.ServiceHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.beez.api.BeezGetPostApiClient;
import com.android.beez.api.CustomRequest;
import com.android.beez.api.NewsSourceApiClient;
import com.android.beez.app.AppController;
import com.android.beez.model.BeezPost;
import com.android.beez.model.NewsBeez;
import com.android.beez.ui.Actionbar;
import com.android.beez.ui.Slidemenu;
import com.android.beez.utils.JSON2Object;
import com.android.beez.utils.Params;
import com.android.beez.utils.ShowMessage;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.GoogleAnalytics;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MoreContentActivity extends MenuActivity {
	private String id = "19333";
	private String url="http://stagingbeez.codelovers.vn/api/api/post/post";
	private ProgressDialog pDialog;
	private static final String TAG_CODE = "code";
	private static final String TAG_POST = "Post";
	private static final String TAG_POST_RELATED = "post_related";
	private static final String TAG_MESSAGE = "message";
	private static final String TAG_DATA = "data";
	private String shortLink;
	private String jsonStr;
	private String code;
	private String message;
//	private JSONArray data;
	private String post_related;
	private BeezPost beezPost;
	private List<NewsBeez> newsBeez;
	Intent intent;
	WebView webview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more_content);
		Slidemenu.getInstance().clearMenuActivity(this);
		intent = getIntent();
		init(intent);
		new GetContacts().execute();

		
	}
	private void init(Intent intent){
		 id = intent.getStringExtra(Params.ID);
		 webview = (WebView)findViewById(R.id.webView);
		 webview.getSettings().setDefaultTextEncodingName("utf-8");       
		 webview.getSettings().setJavaScriptEnabled(true);
		 
		 
//		 Toast.makeText(getApplicationContext(), id, Toast.LENGTH_LONG).show();
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
	
	/**
	 * Async task class to get json by making HTTP call
	 * */
	private class GetContacts extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// Showing progress dialog
//			pDialog = new ProgressDialog(MoreContentActivity.this);
//			pDialog.setMessage("Please wait...");
//			pDialog.setCancelable(false);
//			pDialog.show();

		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// Creating service handler class instance
			ServiceHandler sh = new ServiceHandler();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair(Params.ID, id));
			// Making a request to url and getting response
			jsonStr = sh.makeServiceCall(url, ServiceHandler.POST,params);
			Log.d("Response: ", "> " + jsonStr);
			if (jsonStr != null) {
				try {
					JSONObject jsonObj = new JSONObject(jsonStr);
					JSONObject data  = jsonObj.getJSONObject(TAG_DATA);
					JSONObject post = data.getJSONObject(TAG_POST);
					JSONArray post_releated = data.getJSONArray(TAG_POST_RELATED);
					beezPost = new BeezPost();
					beezPost.setId(post.getString(BeezPost.TAG_ID));
					beezPost.setTitle(post.getString(BeezPost.TAG_TITLE));
					beezPost.setHeadline(post.getString(BeezPost.TAG_HEADLINE));
					beezPost.setHeadline_img(post.getString(BeezPost.TAG_HEADLINE_IMG));
					beezPost.setCheck_img(post.getString(BeezPost.TAG_CHECK_IMG));
					beezPost.setOrigin_url(post.getString(BeezPost.TAG_ORIGIN_URL));
					beezPost.setShort_link(post.getString(BeezPost.TAG_SHORT_LINK));
					beezPost.setPost_id(post.getString(BeezPost.TAG_POST_ID));
					beezPost.setCate_id(post.getString(BeezPost.TAG_CATE_ID));
					beezPost.setCate_updated(post.getString(BeezPost.TAG_CATE_UPDATED));
					beezPost.setOrigin_pv(post.getString(BeezPost.TAG_ORIGIN_PV));
					beezPost.setRemark(post.getString(BeezPost.TAG_REMARK));
					beezPost.setPv(post.getString(BeezPost.TAG_PV));
					beezPost.setGod_pv(post.getString(BeezPost.TAG_GOD_PV));
					beezPost.setCt(post.getString(BeezPost.TAG_CT));
					beezPost.setTags(post.getString(BeezPost.TAG_TAGS));
					beezPost.setApp_id(post.getString(BeezPost.TAG_APP_ID));
					beezPost.setApp_domain(post.getString(BeezPost.TAG_APP_DOMAIN));
					beezPost.setFlag_recommend(post.getString(BeezPost.TAG_FLAG_RECOMMEND));
					beezPost.setRecommend_created(post.getString(BeezPost.TAG_RECOMMEND_CREATED));
					beezPost.setOrigin_post_date(post.getString(BeezPost.TAG_ORIGIN_POST_DATE));
					beezPost.setCreated(post.getString(BeezPost.TAG_CREATED));
					beezPost.setImg_content(post.getString(BeezPost.TAG_IMG_CONTENT));
					beezPost.setContent(post.getString(BeezPost.TAG_CONTENT));
					beezPost.setFooter_text(post.getString(BeezPost.TAG_FOOTER_TEXT));
					beezPost.setRss_time(post.getString(BeezPost.TAG_RSS_TIME));
					beezPost.setStatus(post.getString(BeezPost.TAG_STATUS));
					beezPost.setFb_shared(post.getString(BeezPost.TAG_FB_SHARED));
					beezPost.setPost_type(post.getString(BeezPost.TAG_POST_TYPE));
					
					newsBeez = new ArrayList<NewsBeez>();
					for(int i=0;i<post_releated.length();i++){
						NewsBeez tempNewBeez = new NewsBeez();
						JSONObject tempPost = post_releated.getJSONObject(i);
						tempNewBeez.setId(tempPost.getString("id"));
						tempNewBeez.setTitle(tempPost.getString("title"));
						tempNewBeez.setHeadline(tempPost.getString("headline"));
						tempNewBeez.setHeadline_img(tempPost.getString("headline_img"));
						tempNewBeez.setOrigin_url(tempPost.getString("origin_url"));
						tempNewBeez.setApp_domain(tempPost.getString("app_domain"));
						tempNewBeez.setCate_id(tempPost.getString("cate_id"));
						tempNewBeez.setShort_link(tempPost.getString("short_link"));
						tempNewBeez.setTime(tempPost.getString("time"));
						tempNewBeez.setView(tempPost.getInt("view"));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Log.e("ServiceHandler", "Couldn't get any data from the url");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(beezPost.getApp_id().equals("beez"))
				webview.loadData(beezPost.getContent(), "text/html; charset=utf-8",null);
			else
				webview.loadUrl(beezPost.getOrigin_url());
//				
		}

	}
}
