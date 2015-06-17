package com.android.beez.adapter;

import java.util.ArrayList;
import java.util.Random;

import com.android.beez.R;
import com.android.beez.adapter.NewsAdapter.NewsEntryHolder;
import com.android.beez.app.AppController;
import com.android.beez.loadimage.ImageFetcher;
import com.android.beez.model.NewsBeez;
import com.etsy.android.grid.util.DynamicHeightImageView;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SuggestAdapter extends BaseAdapter{
	
	private LayoutInflater inflater;
	private ArrayList<NewsBeez> entries;
	private int layoutResourceId;
	private Context context;
	private ImageFetcher imageFetcher;
	private AlertDialog alert;
	private final Random mRandom;
	private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();
	
	private static final String TAG = "SuggestAdapter";
	
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
		PostsSuggestEntryHolder holder = null;
		final NewsBeez entry = entries.get(position);
		if(view == null){
			view = inflater.inflate(this.layoutResourceId, parent, false);
			holder = new PostsSuggestEntryHolder();
			holder.gv_headline_img = (DynamicHeightImageView) view.findViewById(R.id.headline_img);
			holder.title = (TextView) view.findViewById(R.id.title);
			//holder.headline = (TextView) view.findViewById(R.id.headline);
			holder.time = (TextView) view.findViewById(R.id.time);
			holder.app_domain = (TextView) view.findViewById(R.id.app_domain);
			holder.view = (TextView) view.findViewById(R.id.view);
			view.setTag(holder);
		} else {
			holder = (PostsSuggestEntryHolder)view.getTag();
		}
		
		holder.title.setText(entry.getTitle());
		//holder.headline.setText(entry.getHeadline());
		holder.time.setText(entry.getTime());
		holder.app_domain.setText(entry.getApp_domain());
		holder.view.setText(entry.getView() != 0 ? entry.getView() + " view" : "NEW");
		double positionHeight = getPositionRatio(position);
        holder.gv_headline_img.setHeightRatio(positionHeight);
        imageFetcher.setImageSize(holder.gv_headline_img.getWidth(),(int)Math.round(positionHeight));
		imageFetcher.loadImage(entry.getHeadline_img(), holder.gv_headline_img, null);
		return view;
	}
	
	static class PostsSuggestEntryHolder{
		DynamicHeightImageView gv_headline_img;
		TextView title;
		TextView headline;
		TextView time;
		TextView view;
		TextView app_domain;
		TextView ranking;
	}
	
	private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
            Log.d(TAG, "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }
 
    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5
                                                    // the width
    }
	
	public SuggestAdapter(Context context, ArrayList<NewsBeez> entries) {
		super();
		this.entries = entries;
		
		this.layoutResourceId = R.layout.item_gridview;
		this.context = context;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imageFetcher = AppController.getInstance().getImageFetcher();
		this.mRandom = new Random();
	}

	public LayoutInflater getInflater() {
		return inflater;
	}

	public void setInflater(LayoutInflater inflater) {
		this.inflater = inflater;
	}

	public ArrayList<NewsBeez> getEntries() {
		return entries;
	}

	public void setEntries(ArrayList<NewsBeez> entries) {
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

	public ImageFetcher getImageFetcher() {
		return imageFetcher;
	}

	public void setImageFetcher(ImageFetcher imageFetcher) {
		this.imageFetcher = imageFetcher;
	}

	public Random getmRandom() {
		return mRandom;
	}
	
	
}
