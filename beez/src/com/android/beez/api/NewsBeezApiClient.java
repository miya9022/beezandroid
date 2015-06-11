package com.android.beez.api;

import com.android.beez.app.AppController;
import com.android.beez.utils.Params;
import com.android.volley.Response.*;
import com.android.volley.toolbox.StringRequest;

import android.content.Context;

public class NewsBeezApiClient extends BaseNewsSourceApiClient {

	public NewsBeezApiClient(String baseUrl, String apiBaseUrl, Context ctx) {
		super(baseUrl, apiBaseUrl);
		this.context = ctx;
	}

	@Override
	public String showListNews(Listener<String> listener, ErrorListener errorListener) {
		StringBuilder sb = new StringBuilder(this.apiBaseUrl);
		sb.append(Params.LISTPOST);
		String apiUri = sb.toString();

		StringRequest req = new StringRequest(apiUri, listener, errorListener);
		AppController.getInstance().addToRequestQueue(req);
		
		return null;
	}

	@Override
	public String searchByTime(String time, Listener<String> listener,
			ErrorListener errorlistener) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
