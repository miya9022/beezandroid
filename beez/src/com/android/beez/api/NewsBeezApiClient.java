package com.android.beez.api;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.android.beez.app.AppController;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import android.content.Context;

public class NewsBeezApiClient extends BaseNewsSourceApiClient {

	public NewsBeezApiClient(String baseUrl, String apiBaseUrl, Context ctx) {
		super(baseUrl, apiBaseUrl);
		this.context = ctx;
	}
		
}
