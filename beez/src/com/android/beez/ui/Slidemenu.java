package com.android.beez.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.android.beez.MenuActivity;
import com.android.beez.NewsListActivity;
import com.android.beez.R;
import com.android.beez.app.AppController;

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
		ListView menuItemList = (ListView) this.view
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

	private class SlidemenuAdapter extends BaseAdapter {
		private LayoutInflater inflater;

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

		private ArrayList<MenuItem> data = new ArrayList<MenuItem>();

		public SlidemenuAdapter(Context context) {
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

		@Override
		public int getCount() {
			return data.size();
		}

		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		@Override
		public long getItemId(int position) {
			return ((MenuItem) getItem(position)).getId();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			SlidingmenuItemHolder holder = null;

			MenuItem item = data.get(position);

			if (view == null) {
				holder = new SlidingmenuItemHolder();
				view = inflater.inflate(R.layout.slidingmenu_item, null);
				holder.text = (TextView) view
						.findViewById(R.id.slidingmenu_item_text);
				holder.image = (ImageView) view
						.findViewById(R.id.slidingmenu_item_thumb);
				
				if(item.getType() == MenuItem.TYPE_BUTTON){
					
				}
				
//				holder.toggle = (ToggleButton) view
//						.findViewById(R.id.slidingmenu_item_toggle);
//				holder.check = (CheckBox) view
//						.findViewById(R.id.slidingmenu_item_check);

//				if (item.getType() == MenuItem.TYPE_TOGGLE) {
//					holder.toggle.setVisibility(View.VISIBLE);
//					holder.check.setVisibility(View.GONE);
//					holder.toggle
//							.setOnClickListener(new View.OnClickListener() {
//								@Override
//								public void onClick(View v) {
//									boolean val = ((ToggleButton) v)
//											.isChecked();
//
//									SharedPreferences shared = AppController
//											.getInstance()
//											.getSharedPreferences();
//									Editor e = shared.edit();
//									e.putBoolean(((ToggleButton) v).getTag()
//											.toString(), val);
//									e.commit();
//								}
//							});
//				} else if (item.getType() == MenuItem.TYPE_CHECK) {
//					holder.toggle.setVisibility(View.GONE);
//					holder.check.setVisibility(View.VISIBLE);
//				} else {
//					holder.toggle.setVisibility(View.GONE);
//					holder.check.setVisibility(View.GONE);
//				}

				view.setTag(holder);
			} else {
				holder = (SlidingmenuItemHolder) view.getTag();
			}

			holder.text.setText(item.getName());
			holder.text.setTag(item.getTag());

			holder.image.setBackgroundResource(item.getThumbIcon());
			holder.image.setTag(item.getTag());

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

			return view;
		}
	}

	private static class SlidingmenuItemHolder {
		TextView text;
		ImageView image;
//		ToggleButton toggle;
		CheckBox check;
	}
}