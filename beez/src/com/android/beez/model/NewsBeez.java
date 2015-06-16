package com.android.beez.model;

import java.io.Serializable;

public class NewsBeez implements Serializable {
	public String id;
	public String title;
	public String headline;
	public String headline_img;
	
	public String origin_url;
	public String app_domain;
	public String cate_id;
	public String short_link;
	public String time;
	
	public int rank;
	public int view = 0;
		
	public NewsBeez() {
		super();
	}

	public NewsBeez(String id, String title, String headline,
			String headline_img, String origin_url, String app_domain) {
		super();
		this.id = id;
		this.title = title;
		this.headline = headline;
		this.headline_img = headline_img;
		this.origin_url = origin_url;
		this.app_domain = app_domain;
	}
	
	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getHeadline_img() {
		return headline_img;
	}

	public void setHeadline_img(String headline_img) {
		this.headline_img = headline_img;
	}

	public String getOrigin_url() {
		return origin_url;
	}

	public void setOrigin_url(String origin_url) {
		this.origin_url = origin_url;
	}

	public String getApp_domain() {
		return app_domain;
	}

	public void setApp_domain(String app_domain) {
		this.app_domain = app_domain;
	}

	public String getCate_id() {
		return cate_id;
	}

	public void setCate_id(String cate_id) {
		this.cate_id = cate_id;
	}

	public String getShort_link() {
		return short_link;
	}

	public void setShort_link(String short_link) {
		this.short_link = short_link;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getView() {
		return view;
	}

	public void setView(int view) {
		this.view = view;
	}
}
