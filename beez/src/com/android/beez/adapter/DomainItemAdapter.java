package com.android.beez.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.beez.R;
import com.android.beez.adapter.NewsAdapter.NewsEntryHolder;
import com.android.beez.model.AppDomainBeez;
import com.android.beez.model.NewsBeez;
import com.etsy.android.grid.util.DynamicHeightImageView;

public class DomainItemAdapter extends BaseAdapter{
	private LayoutInflater inflater;
	private ArrayList<AppDomainBeez> entries;
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
		DomainEntryHolder holder = new DomainEntryHolder();
		final AppDomainBeez entry = entries.get(position); 
		if(view == null){
			holder.app_view = (TextView) view.findViewById(R.id.app_domain_item_text);
			view.setTag(holder);
		} else {
			holder = (DomainEntryHolder) view.getTag();
		}
		holder.app_view.setText(entry.getName());
		return null;
	}
	
	public LayoutInflater getInflater() {
		return inflater;
	}

	public void setInflater(LayoutInflater inflater) {
		this.inflater = inflater;
	}

	public ArrayList<AppDomainBeez> getEntries() {
		return entries;
	}

	public void setEntries(ArrayList<AppDomainBeez> entries) {
		this.entries = entries;
	}

	public int getLayoutResourceId() {
		return layoutResourceId;
	}

	public void setLayoutResourceId(int layoutResourceId) {
		this.layoutResourceId = layoutResourceId;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public DomainItemAdapter(Context context, ArrayList<AppDomainBeez> entries) {
		super();
		this.entries = entries;
		this.layoutResourceId = R.layout.item_gv_app;
		this.context = context;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	static class DomainEntryHolder{
		TextView app_view;
	}
}
