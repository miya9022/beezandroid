package com.android.beez.model;

import java.io.Serializable;

public class NewsBeez implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 754189098001845039L;
	private String id;
	private String title;
	private String headline;
	private String headline_img;
	
	private String origin_url;
	private String app_domain;
	private String cate_id;
	private String short_link;
	private String time;
	
	private String name;
	private String app_id;
	private boolean isCate;
	
	public boolean isCate() {
		return isCate;
	}

	public void setCate(boolean isCate) {
		this.isCate = isCate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getApp_id() {
		return app_id;
	}

	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}

	private int rank;
	private int view = 0;
		
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
