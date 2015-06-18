package com.android.beez.model;

import java.io.Serializable;

public class AppDomainBeez implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1813076236119307343L;
	
	private String id;
	private String name;
	private String app_id;
	private String app_domain;
	
	public AppDomainBeez() {
		super();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getApp_domain() {
		return app_domain;
	}
	public void setApp_domain(String app_domain) {
		this.app_domain = app_domain;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
