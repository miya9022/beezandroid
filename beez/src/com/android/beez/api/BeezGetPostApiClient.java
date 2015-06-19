package com.android.beez.api;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.Log;

import com.android.beez.app.AppController;
import com.android.beez.utils.Params;
import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

public class BeezGetPostApiClient extends BaseNewsSourceApiClient{
	String postId;
	public BeezGetPostApiClient(String baseUrl, String apiBaseUrl, Context context) {
		super(baseUrl, apiBaseUrl);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public String showListNews(Listener<String> listener,
			ErrorListener errorlistener) {
		StringBuilder sb = new StringBuilder(this.apiBaseUrl);
		sb.append(Params.POST);
		String apiUri = sb.toString();
		StringRequest req = new StringRequest(Request.Method.POST,apiUri, listener, errorlistener);
		AppController.getInstance().getRequestQueue().add(req);
		return null;
	}

	@Override
	public String searchByTime(String time, Listener<String> listener,
			ErrorListener errorlistener) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String showRecommendPosts(Listener<String> listener,
			ErrorListener errorlistener) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String LoadDataById(Listener<String> listener,
			ErrorListener errorlistener) {
		// TODO Auto-generated method stub
		return null;
	}

}
