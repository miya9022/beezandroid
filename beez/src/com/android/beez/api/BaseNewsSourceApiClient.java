package com.android.beez.api;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;

import com.android.beez.app.AppController;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public abstract class BaseNewsSourceApiClient implements NewsSourceApiClient {
	protected String baseUrl;
	protected String apiBaseUrl;
	protected Context context;
	
	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getApiBaseUrl() {
		return apiBaseUrl;
	}

	public void setApiBaseUrl(String apiBaseUrl) {
		this.apiBaseUrl = apiBaseUrl;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public String createDigest(String source) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] data = source.getBytes();
			md.update(data);

			byte[] digest = md.digest();

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < digest.length; i++) {
				int b = (0xFF & digest[i]);
				if (b < 16)
					sb.append("0");
				sb.append(Integer.toHexString(b));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public BaseNewsSourceApiClient(String baseUrl, String apiBaseUrl) {
		super();
		this.baseUrl = baseUrl;
		this.apiBaseUrl = apiBaseUrl;
		
	}
	
	public String getSNSShareURL(Response.Listener<String> listener, Response.ErrorListener errorListener){
		StringBuilder sb = new StringBuilder(this.apiBaseUrl); 
		sb.append("/share");		
		String apiUri = sb.toString();
		
		StringRequest req = new StringRequest(apiUri, listener, errorListener);		
		AppController.getInstance().addToRequestQueue(req);
		
		return null;
	}
}
