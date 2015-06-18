package com.android.beez.adapter;

import java.util.ArrayList;

import com.android.beez.R;
import com.android.beez.model.NewsBeez;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CateItemAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<NewsBeez> entries;
	private int layoutResourceId;
	private Context context;
	
	@Override
	public int getCount() {
		return entries!=null ? entries.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		if (entries != null && entries.size()>0){
			return entries.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		if (entries != null && entries.size()>0){
			return (entries.get(position).getId()==null)?
					0:Long.valueOf(entries.get(position).getId());
		}
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if(view == null){
			
		}
		return null;
	}
	
	public CateItemAdapter(Context context, ArrayList<NewsBeez> entries) {
		super();
		this.entries = entries;
		this.layoutResourceId = R.layout.item_gv_app;
		this.context = context;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
}
