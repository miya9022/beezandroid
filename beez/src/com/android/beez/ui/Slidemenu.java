package com.android.beez.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.support.v4.content.IntentCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.android.beez.FavouriteActivity;
import com.android.beez.ListNewsByDataActivity;
import com.android.beez.MenuActivity;
import com.android.beez.NewsListActivity;
import com.android.beez.R;
import com.android.beez.SuggestActivity;
import com.android.beez.adapter.NewsAdapter;
import com.android.beez.api.NewsSourceApiClient;
import com.android.beez.app.AppController;
import com.android.beez.model.NewsBeez;
import com.android.beez.utils.Params;
import com.android.beez.utils.ShowMessage;
import com.android.volley.Response;
import com.android.volley.VolleyError;

public class Slidemenu implements OnItemClickListener {
	private static Slidemenu instance = null;
	private MenuActivity parentActivity;
	private View view;
	private SlidingMenu menu = null;
	private HashMap<String, SlidingMenu> menuList = new HashMap<String, SlidingMenu>();

	public static Slidemenu getInstance() {
		if (instance == null) {
			instance = new Slidemenu();
		}

		return instance;
	}

	private Slidemenu() {
	}

	public void showMenu() {
		if (menuList.get(parentActivity.TAG) != null) {
			menuList.get(parentActivity.TAG).showMenu();
		}
	}
	public void hideMenu() {
		if (menuList.get(parentActivity.TAG) != null) {
			menuList.get(parentActivity.TAG).toggle(true);
		}
	}
	public boolean isShowMneu() {
		if (menuList.get(parentActivity.TAG) != null) {
			return menuList.get(parentActivity.TAG).isMenuShowing();
		}
		return false;
	}
	public  void clearMenuActivity(MenuActivity ac) {
		menuList.remove(ac.TAG);
	}

	public void appendTo(MenuActivity parentActivity) {
		this.parentActivity = parentActivity;
		if (menuList.get(parentActivity.TAG) != null) {
			return;
		}
		LayoutInflater inflater = (LayoutInflater) this.parentActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.view = inflater.inflate(R.layout.slidingmenu, null);
		menu = new SlidingMenu(this.parentActivity);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setMode(SlidingMenu.LEFT);
		menu.setBehindOffset((int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 50, this.parentActivity
						.getResources().getDisplayMetrics()));
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this.parentActivity, SlidingMenu.SLIDING_WINDOW);
		menu.setMenu(this.view);
		menuList.put(parentActivity.TAG, menu);
		ExpandableListView menuItemList = (ExpandableListView) this.view
				.findViewById(R.id.slidingmenu);
		menuItemList.setAdapter(new SlidemenuAdapter(this.parentActivity));
		menuItemList.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		TextView v = (TextView) view.findViewById(R.id.slidingmenu_item_text);
		if("Home".equals(v.getTag().toString())){
			Intent intent = new Intent(this.parentActivity, NewsListActivity.class);
			parentActivity.startActivity(intent);
		} else if("Favourite".equals(v.getTag().toString())){
			Intent intent = new Intent(this.parentActivity, FavouriteActivity.class);
			parentActivity.startActivity(intent);
		} else if("Suggest".equals(v.getTag().toString())){
			Intent intent = new Intent(this.parentActivity, SuggestActivity.class);
			parentActivity.startActivity(intent);
		} 
//		if ("PlayInBackground".equals(v.getTag().toString())) {
//			// Do nothing
//		} else if ("InviteFriend".equals(v.getTag().toString())) {
//			Intent intent = new Intent(this.parentActivity,
//					InviteActivity.class);
//			intent.putExtra("back_stack_activity", this.parentActivity
//					.getClass().getSimpleName());
//			// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
//			// IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
//
//			parentActivity.startActivity(intent);
//			// parentActivity.finish();
//			parentActivity.overridePendingTransition(R.anim.diagslide_enter,
//					R.anim.diagslide_leave);
//
//		} else if ("ReviewApp".equals(v.getTag().toString())) {
//			final String appPackageName = this.parentActivity.getPackageName();
//			try {
//				this.parentActivity.startActivity(new Intent(
//						Intent.ACTION_VIEW, Uri.parse("market://details?id="
//								+ appPackageName)));
//			} catch (android.content.ActivityNotFoundException anfe) {
//				this.parentActivity
//						.startActivity(new Intent(
//								Intent.ACTION_VIEW,
//								Uri.parse("http://play.google.com/store/apps/details?id="
//										+ appPackageName)));
//			}
//			parentActivity.overridePendingTransition(R.anim.diagslide_enter,
//					R.anim.diagslide_leave);
//		}
		// else if ("Exit".equals(v.getTag().toString())){
		// AppController.getInstance().getSharedMem().put("app_exit", true);
		//
		// InterstitialAds ads = new InterstitialAds();
		// ads.setAdsTitle(this.parentActivity.getResources().getString(R.string.msg_confirm_close_app));
		// ads.setOkTitle(this.parentActivity.getResources().getString(R.string.btn_accept));
		// ads.show(this.parentActivity, 6);
		// }
	}

	public View getView() {
		return view;
	}

	public Activity getParentActivity() {
		return parentActivity;
	}

	public void setParentActivity(MenuActivity parentActivity) {
		this.parentActivity = parentActivity;
	}

	private class SlidemenuAdapter extends BaseExpandableListAdapter  {
		private LayoutInflater inflater;
		private String dataType;
		
		private Context context;
		private ArrayList<MenuItem> data = new ArrayList<MenuItem>();
		private HashMap<MenuItem, ArrayList<NewsBeez>> menu_child;

		public SlidemenuAdapter(Context context, ArrayList<MenuItem> data, HashMap<MenuItem, ArrayList<String>> menu_child) {
			super();
			String menuItems[] = context.getResources().getStringArray(
					R.array.slidingmenu_items);
			String menuItemTags[] = context.getResources().getStringArray(
					R.array.slidingmenu_item_tags);
			String menuItemTypes[] = context.getResources().getStringArray(
					R.array.slidingmenu_item_types);

			this.inflater = LayoutInflater.from(context);
			for (int i = 0; i < menuItems.length; i++) {
				int type = MenuItem.TYPE_BUTTON;
				if ("toggle".equals(menuItemTypes[i])) {
					type = MenuItem.TYPE_TOGGLE;
				} else if ("check".equals(menuItemTypes[i])) {
					type = MenuItem.TYPE_CHECK;
				} else {
					type = MenuItem.TYPE_BUTTON;
				}
				data.add(new MenuItem(i, 0, menuItems[i], menuItemTags[i], type));
			}
		}

		private class MenuItem {
			public final static int TYPE_TOGGLE = 1;
			public final static int TYPE_CHECK = 2;
			public final static int TYPE_BUTTON = 0;

			private long id;
			private int thumbIcon;
			private String name;
			private String tag;
			private int type = TYPE_BUTTON;

			public MenuItem(long id, int thumbIcon, String name, String tag,
					int type) {
				super();
				this.id = id;
				this.thumbIcon = thumbIcon;
				this.name = name;
				this.tag = tag;
				this.type = type;
			}

			public int getThumbIcon() {
				return thumbIcon;
			}

			public String getName() {
				return name;
			}

			public long getId() {
				return id;
			}

			public String getTag() {
				return tag;
			}

			public int getType() {
				return type;
			}
		}

		public SlidemenuAdapter(Context context) {
			super();
			String menuItems[] = context.getResources().getStringArray(
					R.array.slidingmenu_items);
			String menuItemTags[] = context.getResources().getStringArray(
					R.array.slidingmenu_item_tags);
			String menuItemTypes[] = context.getResources().getStringArray(
					R.array.slidingmenu_item_types);

			this.inflater = LayoutInflater.from(context);
			for (int i = 0; i < menuItems.length; i++) {
				int type = MenuItem.TYPE_BUTTON;
				if ("toggle".equals(menuItemTypes[i])) {
					type = MenuItem.TYPE_TOGGLE;
				} else if ("check".equals(menuItemTypes[i])) {
					type = MenuItem.TYPE_CHECK;
				} else {
					type = MenuItem.TYPE_BUTTON;
				}
				data.add(new MenuItem(i, 0, menuItems[i], menuItemTags[i], type));
			}
			NewsSourceApiClient apiClient = AppController.getInstance().getNewsApiClient();
			apiClient.LoadDataById(new Response.Listener<String>() {

				@Override
				public void onResponse(String data) {
					menu_child = new HashMap<MenuItem, ArrayList<NewsBeez>>();
					try{
						JSONObject jsonObject = new JSONObject(data);
						String code = jsonObject.getString("code");
						if(Params.ERROR_10010.equals(code)){
							ShowMessage.showDialogUpdateApp(getInstance().getParentActivity());
							return;
						}
						if (!"OK".equals(code)) {
							return;
						}
						
						String strData = jsonObject.getString(Params.DATA);
						if(strData == null){
							return;
						}
						
						JSONObject jsonItemsObject = new JSONObject(strData);
						String strSite = jsonItemsObject.getString(Params.SITE);
						if(strSite == null){
							return;
						}
						
						String strCategory = jsonItemsObject.getString(Params.CATEGORY);
						if(strCategory == null){
							return;
						}
						
						JSONArray jsonItemsCate = new JSONArray(strCategory);
						if (jsonItemsCate.length() <= 0) {
							return;
						}
						ArrayList<NewsBeez> cateList = new ArrayList<NewsBeez>();
						for(int i = 0; i < jsonItemsCate.length(); i++){
							JSONObject jobject = jsonItemsCate.getJSONObject(i);
							NewsBeez cate_beez = new NewsBeez();
							cate_beez.setId(jobject.optString(Params.ID, "NULL"));
							cate_beez.setTitle(jobject.optString(Params.TITLE, "NULL"));
							cate_beez.setName(jobject.optString(Params.NAME, "NULL"));
							cate_beez.setCate(true);
							cateList.add(cate_beez);
						}
						JSONArray jsonItemsDomain = new JSONArray(strSite);
						if (jsonItemsDomain.length() <= 0) {
							return;
						}
						ArrayList<NewsBeez> domainList = new ArrayList<NewsBeez>();
						for(int i = 0; i < jsonItemsDomain.length(); i++){
							JSONObject jobject = jsonItemsDomain.getJSONObject(i);
							NewsBeez domain_beez = new NewsBeez();
							domain_beez.setId(jobject.optString(Params.ID, "NULL"));
							domain_beez.setName(jobject.optString(Params.NAME, "NULL"));
							domain_beez.setApp_id(jobject.optString(Params.APP_ID, "NULL"));
							domain_beez.setApp_domain(jobject.optString(Params.APP_DOMAIN, "NULL"));
							domain_beez.setCate(false);
							domainList.add(domain_beez);
						}
						menu_child.put(SlidemenuAdapter.this.data.get(3), domainList);
						menu_child.put(SlidemenuAdapter.this.data.get(4), cateList);
					} catch(Exception ex){
						ex.printStackTrace();
					}
				}
				
			}, new Response.ErrorListener() {

				@Override
				public void onErrorResponse(VolleyError arg0) {
					
				}
				
			});
		}
		
		@Override
		public int getGroupCount() {
			return this.data.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return this.menu_child == null ? 0 : this.menu_child.get(this.data.get(groupPosition)).size();
		}

		@Override
		public Object getGroup(int groupPosition) {
			return this.data.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return this.menu_child.get(this.data.get(groupPosition)).get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			View view = convertView;
			SlidingmenuItemHolder holder = null;
			final MenuItem item = data.get(groupPosition);
			if (view == null) {
				holder = new SlidingmenuItemHolder();
				view = inflater.inflate(R.layout.slidingmenu_item, null);
				holder.text = (TextView) view
						.findViewById(R.id.slidingmenu_item_text);
				holder.image = (ImageView) view
						.findViewById(R.id.slidingmenu_item_thumb);
				
				view.setTag(holder);
			} else {
				holder = (SlidingmenuItemHolder) view.getTag();
			}
			
			holder.text.setText(item.getName());
			holder.text.setTag(item.getTag());
			holder.text.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if("Home".equals(v.getTag().toString())){
						Intent intent = new Intent(getParentActivity(), NewsListActivity.class);
						parentActivity.startActivity(intent);
					} else if("Favourite".equals(v.getTag().toString())){
						Intent intent = new Intent(getParentActivity(), FavouriteActivity.class);
						parentActivity.startActivity(intent);
					} else if("Suggest".equals(v.getTag().toString())){
						Intent intent = new Intent(getParentActivity(), SuggestActivity.class);
						parentActivity.startActivity(intent);
					} 
				}
				
			});

			holder.image.setBackgroundResource(item.getThumbIcon());
			holder.image.setTag(item.getTag());
			return view;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			View view = convertView;
			SlidingmenuItemHolder holder = null;
			final NewsBeez dataItem = (NewsBeez) getChild(groupPosition, childPosition);
			if(view == null){
				view = inflater.inflate(R.layout.item_gv_app, null);
				holder = new SlidingmenuItemHolder();
				holder.text = (TextView) view.findViewById(R.id.app_domain_item_text);
				view.setTag(holder);
			} else {
				holder = (SlidingmenuItemHolder) view.getTag();
			}
			holder.text.setText(dataItem.getName());
			holder.text.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(getParentActivity(), ListNewsByDataActivity.class);
					if(dataItem.isCate() == false){
						i.putExtra(Params.DATA_TYPE, "domain");
						i.putExtra(Params.ID, dataItem.getId());
						i.putExtra(Params.NAME, dataItem.getName());
						i.putExtra(Params.APP_ID, dataItem.getApp_id());
						i.putExtra(Params.APP_DOMAIN, dataItem.getApp_domain());
					} else {
						i.putExtra(Params.DATA_TYPE, "category");
						i.putExtra(Params.ID, dataItem.getId());
						i.putExtra(Params.TITLE, dataItem.getTitle());
						i.putExtra(Params.NAME, dataItem.getName());
					}
					getParentActivity().startActivity(i);
				}
			});
			return view;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

//		@Override
//		public int getCount() {
//			return data.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return data.get(position);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return ((MenuItem) getItem(position)).getId();
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			View view = convertView;
//			SlidingmenuItemHolder holder = null;
//
//			final MenuItem item = data.get(position);
//
//			if (view == null) {
//				holder = new SlidingmenuItemHolder();
//				view = inflater.inflate(R.layout.slidingmenu_item, null);
//				holder.text = (TextView) view
//						.findViewById(R.id.slidingmenu_item_text);
//				holder.image = (ImageView) view
//						.findViewById(R.id.slidingmenu_item_thumb);
//				holder.toggle = (ToggleButton) view
//						.findViewById(R.id.slidingmenu_item_toggle);
//				holder.gridview = (CustomGridView) view
//						.findViewById(R.id.app_domain_gv);
//				if (item.getType() == MenuItem.TYPE_TOGGLE) {
//					holder.toggle.setVisibility(View.VISIBLE);
//					holder.gridview.setVisibility(View.VISIBLE);
//					boolean val = holder.toggle.isChecked();
//					if(val == false){
//						onLoadCate(view);
//					}
//				} else {
//					holder.toggle.setVisibility(View.GONE);
//					holder.gridview.setVisibility(View.GONE);
//				}
//				holder.toggle.setOnClickListener(new View.OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						
//					}
//				});
//				view.setTag(holder);
//			} else {
//				holder = (SlidingmenuItemHolder) view.getTag();
//			}
//
//			holder.text.setText(item.getName());
//			holder.text.setTag(item.getTag());
//
//			holder.image.setBackgroundResource(item.getThumbIcon());
//			holder.image.setTag(item.getTag());
//
//			SharedPreferences shared = AppController.getInstance()
//					.getSharedPreferences();
//			if (item.getType() == MenuItem.TYPE_TOGGLE) {
//				holder.toggle
//						.setChecked(shared.getBoolean(item.getTag(), true));
//				holder.toggle.setTag(item.getTag());
//			} else if (item.getType() == MenuItem.TYPE_CHECK) {
//				holder.check
//						.setSelected(shared.getBoolean(item.getTag(), true));
//				holder.check.setTag(item.getTag());
//			}
//
//			return view;
//		}

		
	}

	private static class SlidingmenuItemHolder {
		TextView text;
		ImageView image;
		ToggleButton toggle;
		CheckBox check;
	}
}