package com.android.beez.adapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import com.android.beez.R;
import com.android.beez.app.AppController;
import com.android.beez.loadimage.ImageFetcher;
import com.android.beez.model.NewsBeez;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NewsAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<NewsBeez> entries;
	private int layoutResourceId;
	private Context context;
	private ImageFetcher imageFetcher;
	private AlertDialog alert;
	private int columWidth = 0;
	public int getColumWidth() {
		return columWidth;
	}

	public void setColumWidth(int columWidth) {
		this.columWidth = columWidth;
	}

	@Override
	public int getCount() {
		return entries != null ? entries.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		if (entries != null && entries.size() > 0) {
			return entries.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		if (entries != null && entries.size() > 0) {
			return (entries.get(position).getId() == null) ? 0 : Long
					.valueOf(entries.get(position).getId());
		}
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		NewsEntryHolder holder = null;
		final NewsBeez entry = entries.get(position);
		if (this.layoutResourceId == R.layout.item_listview) {
			if (view == null) {
				view = inflater.inflate(this.layoutResourceId, parent, false);
				holder = new NewsEntryHolder();
				holder.headline_img = (ImageView) view
						.findViewById(R.id.headline_img_by_app_domain);
				holder.title = (TextView) view
						.findViewById(R.id.title_by_app_domain);
				holder.app_domain = (TextView) view
						.findViewById(R.id.app_domain_source);
				holder.time = (TextView) view
						.findViewById(R.id.time_by_app_domain);
				holder.view = (TextView) view
						.findViewById(R.id.view_by_app_domain);
				view.setTag(holder);
			} else {
				holder = (NewsEntryHolder) view.getTag();
			}
			holder.title.setText(entry.getTitle());
			holder.time.setText(entry.getTime());
			holder.app_domain.setText(entry.getApp_domain());
			holder.view.setText(entry.getView() != 0 ? entry.getView()
					+ " view" : "NEW");
			imageFetcher.loadImage(entry.getHeadline_img(),
					holder.headline_img, null);
		} else {
			if (view == null) {
				view = inflater.inflate(this.layoutResourceId, parent, false);
				holder = new NewsEntryHolder();
				holder.gv_headline_img = (ImageView) view
						.findViewById(R.id.headline_img);
				holder.title = (TextView) view.findViewById(R.id.title);
				// holder.headline = (TextView)
				// view.findViewById(R.id.headline);

				holder.time = (TextView) view.findViewById(R.id.time);
				holder.app_domain = (TextView) view
						.findViewById(R.id.app_domain);
				holder.view = (TextView) view.findViewById(R.id.view);
				view.setTag(holder);

			} else {
				holder = (NewsEntryHolder) view.getTag();
			}

			holder.title.setText(entry.getTitle());
			holder.time.setText(entry.getTime());
			holder.app_domain.setText(entry.getApp_domain());
			holder.view.setText(entry.getView() != 0 ? entry.getView()
					+ " view" : "NEW");
			Bitmap temp = imageFetcher.processBitmap(entry.getHeadline_img());
			Bitmap resizedBitmap = Bitmap.createScaledBitmap(
					temp,
					columWidth,
					getBitmapHeight(temp.getWidth(), temp.getHeight(),
							columWidth), false);
			Bitmap rounded = getRoundedCornerBitmap(resizedBitmap);
			holder.gv_headline_img.setImageBitmap(rounded);
		}
		return view;
	}

	static class NewsEntryHolder {
		ImageView gv_headline_img;
		ImageView headline_img;
		TextView title;
		TextView headline;
		TextView time;
		TextView view;
		TextView app_domain;
	}

	public NewsAdapter(Context context, ArrayList<NewsBeez> entries,
			boolean viewContent) {
		super();
		this.entries = entries;
		this.layoutResourceId = viewContent ? R.layout.item_listview
				: R.layout.item_gridview;
		this.context = context;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imageFetcher = AppController.getInstance().getImageFetcher();
	}

	public NewsAdapter(Context context, ArrayList<NewsBeez> entries) {
		super();
		this.entries = entries;

		this.layoutResourceId = R.layout.item_gridview;
		this.context = context;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.imageFetcher = AppController.getInstance().getImageFetcher();
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

	public AlertDialog getAlert() {
		return alert;
	}

	public void setAlert(AlertDialog alert) {
		this.alert = alert;
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = 5;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public static Bitmap getBitmapFromURL(String src) {
		try {
			URL url = new URL(src);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Bitmap myBitmap = BitmapFactory.decodeStream(input);
			return myBitmap;
		} catch (IOException e) {
			// Log exception
			return null;
		}
	}

	public int getBitmapHeight(int rawWidth, int rawHeight, int columnWidth) {
		return rawHeight * columnWidth / rawWidth;
	}

}
