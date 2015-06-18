package com.android.beez.model;

import java.io.Serializable;

public class CategoryBeez implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4898695801976423415L;
	
	private String id;
	private String title;
	private String name;
	
	public CategoryBeez() {
		super();
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
