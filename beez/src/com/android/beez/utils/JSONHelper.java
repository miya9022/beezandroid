package com.android.beez.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.beez.model.NewsBeez;

public class JSONHelper {

	public static ArrayList<NewsBeez> parseJSONNewsBeez(String data){
		try {
			JSONObject jsonObject = new JSONObject(data);
			String code = jsonObject.getString(Params.CODE);
			if (!Params.OK.equals(code)){
				return null;
			}
			
			String headline_img = null;
			JSONArray jsonItems =jsonObject.getJSONObject(data).getJSONArray(Params.DATA);
			if (jsonItems.length() <= 0){
				return null;
			}
			
			ArrayList<NewsBeez> newsList = new ArrayList<NewsBeez>();			
			for(int i=0; i<jsonItems.length(); i++){
				JSONObject item = jsonItems.getJSONObject(i);
				headline_img = item.optString(Params.HEADLINE_IMG, "NULL");
				JSON2Object j2o = new JSON2Object(NewsBeez.class, item);
				NewsBeez news = (NewsBeez)j2o.parse();
				if (headline_img != null){
					news.setHeadline_img(headline_img);
				}
				newsList.add(news);
			}
			return newsList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public static ArrayList<NewsBeez> parseJsonSearchSingle2News(String data){
		try {
			JSONObject jsonObject = new JSONObject(data);
			String code = jsonObject.getString(Params.CODE);
			if (!Params.OK.equals(code)){
				return null;
			}
			
			String headline_img = null;
			JSONArray jsonItems =jsonObject.getJSONArray(Params.DATA);
			if (jsonItems.length() <= 0){
				return null;
			}
			
			ArrayList<NewsBeez> newsList = new ArrayList<NewsBeez>();			
			for(int i=0; i<jsonItems.length(); i++){
				JSONObject item = jsonItems.getJSONObject(i);
				headline_img = item.optString(Params.HEADLINE_IMG,"NULL");
				JSON2Object j2o = new JSON2Object(NewsBeez.class, item);
				NewsBeez news = (NewsBeez)j2o.parse();
				if (headline_img != null){
					news.setHeadline_img(headline_img);
				}
				newsList.add(news);
			}
			return newsList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
