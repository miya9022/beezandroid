package com.android.beez;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.beez.api.BeezGetPostApiClient;
import com.android.beez.api.CustomRequest;
import com.android.beez.api.NewsSourceApiClient;
import com.android.beez.app.AppController;
import com.android.beez.model.BeezPost;
import com.android.beez.model.NewsBeez;
import com.android.beez.ui.Actionbar;
import com.android.beez.ui.Slidemenu;
import com.android.beez.utils.JSON2Object;
import com.android.beez.utils.Params;
import com.android.beez.utils.ShowMessage;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.analytics.GoogleAnalytics;

import android.app.Activity;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MoreContentActivity extends MenuActivity {
	private boolean noMoreData = false;
	
	protected void verificationSuccess(JSONObject data) {
		try {
			JSONObject j2o = data;
			String code = j2o.getString("code");
			if(Params.ERROR_10010.equals(code)){
				ShowMessage.showDialogUpdateApp(this);
				return;
			}if(!"OK".equals(code)){
				noMoreData = true;
				return;
			}
			
			String strData = j2o.getString(Params.DATA);
			if(strData == null){
				noMoreData = true;
				return;
			}
			Log.d("anhntcheck",strData);
			
			
		
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	protected void verificationFailed(VolleyError data) {
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more_content);
		Slidemenu.getInstance().clearMenuActivity(this);
//		RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
		JSONObject obj = new JSONObject();
		try {
			obj.put("id", "19333");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,"http://stagingbeez.codelovers.vn/api/api/post/post",obj,
			    new Response.Listener<JSONObject>() {
			        @Override
			        public void onResponse(JSONObject response) {
			             Log.d("anhntcheck",response.toString());
			        }
			    },
			    new Response.ErrorListener() {
			        @Override
			        public void onErrorResponse(VolleyError error) {
			              Log.e("anhntcheck",error.toString());
			        }
			    });

//		requestQueue.add(jsObjRequest);
//		
	}

	private void init(Intent intent) {
		WebView webview = (WebView) findViewById(R.id.webView);
		String id = intent.getStringExtra(Params.ID);
		String customHtml = "       <h2>\\\n            <span data-bind=\"text: ($index()   1) &amp;#039;. &amp;#039;\">1. <\\/span>\\\n            <span data-bind=\"text: image_title\">Three Lucky Bastards Who Aren&amp;#039;t You Win The Powerball <\\/span>\\\n        <\\/h2>\\\n        <img class=\"img-responsive\" data-bind=\"attr: { src: image_src }\" src=\"\">\\\n            <div data-bind=\"if: type() == &amp;#039;from_file&amp;#039;\"><\\/div>    \\\n            <div data-bind=\"if: type() == &amp;#039;from_url&amp;#039;\">\\\n                <img class=\"img-responsive\" data-bind=\"attr: { src: from_url() },visible: from_url()\" src=\"http:\\/\\/r2-store.distractify.netdna-cdn.com\\/postimage\\/201502\\/7\\/69a10713161ceaa726ce9df04baf14e1_650x.jpg\">\\\n            <\\/div>\\\n            <div data-bind=\"if: type() == &amp;#039;youtube&amp;#039;\"><\\/div>\\\n        <p data-bind=\"text: image_caption\">After swelling to roughly 564 million the Powerball bubble finally burst last night when three lucky winners picked the winning numbers. The winning tickets were sold in North Carolina, Texas, and Puerto Rico which means Puerto Rico can finally afford to become a state.<\\/p>     \\\n        <h2>\\\n            <span data-bind=\"text: ($index()   1) &amp;#039;. &amp;#039;\">2. <\\/span>\\\n            <span data-bind=\"text: image_title\">Obama Asks Congress For War Powers To Fight ISIS <\\/span>\\\n        <\\/h2>\\\n        <img class=\"img-responsive\" data-bind=\"attr: { src: image_src }\" src=\"\">\\\n            <div data-bind=\"if: type() == &amp;#039;from_file&amp;#039;\"><\\/div>    \\\n            <div data-bind=\"if: type() == &amp;#039;from_url&amp;#039;\">\\\n                <img class=\"img-responsive\" data-bind=\"attr: { src: from_url() },visible: from_url()\" src=\"http:\\/\\/r2-store.distractify.netdna-cdn.com\\/postimage\\/201502\\/7\\/025cb0bf9419ba3edea60824e5ac4bfa_650x.png\">\\\n            <\\/div>\\\n            <div data-bind=\"if: type() == &amp;#039;youtube&amp;#039;\"><\\/div>\\\n        <p data-bind=\"text: image_caption\">Obama sent a request to Congress yesterday for formal authorization to fight ISIS\\/ISIL, the Islamic extremist group in Syria and Iraq. This would obviously be number one on the list if not for the fact that the war powers are retroactive. The U.S. has already been bombing ISIS for 6 months.<\\/p>     \\\n        <h2>\\\n            <span data-bind=\"text: ($index()   1) &amp;#039;. &amp;#039;\">3. <\\/span>\\\n            <span data-bind=\"text: image_title\">Bored New Hampshire Cops Want To Arrest Punxatawney Phil <\\/span>\\\n        <\\/h2>\\\n        <img class=\"img-responsive\" data-bind=\"attr: { src: image_src }\" src=\"\">\\\n            <div data-bind=\"if: type() == &amp;#039;from_file&amp;#039;\"><\\/div>    \\\n            <div data-bind=\"if: type() == &amp;#039;from_url&amp;#039;\">\\\n                <img class=\"img-responsive\" data-bind=\"attr: { src: from_url() },visible: from_url()\" src=\"https:\\/\\/fbcdn-sphotos-a-a.akamaihd.net\\/hphotos-ak-xpa1\\/v\\/t1.0-9\\/10256117_795387120535945_2367062496297207506_n.jpg?oh=e4bc49a99813125559a1f681277a4e5d&amp;amp;oe=555456F0&amp;amp;__gda__=1435333704_89c21e4eae87f22c63c434ef51a62cfb\">\\\n            <\\/div>\\\n            <div data-bind=\"if: type() == &amp;#039;youtube&amp;#039;\"><\\/div>\\\n        <p data-bind=\"text: image_caption\">Police in Merrimack New Hampshire have issued a “warrant” for the arrest of Punxatawney Phil, groundhog prognosticator extraordinaire, citing excessive snowfalls. Nevermind that Phil’s home of Gobbler’s Knob, PA is well outside of the the New Hampshire cops’ jurisdiction, and that animal prison doesn’t exist, let’s remember that a long winter is exactly what Phil predicted. Don’t hate the groundhog hombres, hate the game.<\\/p>     \\\n        <h2>\\\n            <span data-bind=\"text: ($index()   1) &amp;#039;. &amp;#039;\">4. <\\/span>\\\n            <span data-bind=\"text: image_title\">Congress Passes Keystone XL Pipeline Legislation<\\/span>\\\n        <\\/h2>\\\n        <img class=\"img-responsive\" data-bind=\"attr: { src: image_src }\" src=\"\">\\\n            <div data-bind=\"if: type() == &amp;#039;from_file&amp;#039;\"><\\/div>    \\\n            <div data-bind=\"if: type() == &amp;#039;from_url&amp;#039;\">\\\n                <img class=\"img-responsive\" data-bind=\"attr: { src: from_url() },visible: from_url()\" src=\"http:\\/\\/r2-store.distractify.netdna-cdn.com\\/postimage\\/201502\\/7\\/0733918d058d4ed15e868465a687f00f_650x.jpg\">\\\n            <\\/div>\\\n            <div data-bind=\"if: type() == &amp;#039;youtube&amp;#039;\"><\\/div>\\\n        <p data-bind=\"text: image_caption\">The House of Representatives passed legislation Wednesday approving construction of the controversial Keystone XL pipeline. The current bill has already been approved by the Senate and will go to President Obama who is expected to veto the measure which has faced fierce opposition from environmental groups.<\\/p>     \\\n        <h2>\\\n            <span data-bind=\"text: ($index()   1) &amp;#039;. &amp;#039;\">5. <\\/span>\\\n            <span data-bind=\"text: image_title\">Bob Simon of CBS News Dies In Car Crash<\\/span>\\\n        <\\/h2>\\\n        <img class=\"img-responsive\" data-bind=\"attr: { src: image_src }\" src=\"\">\\\n            <div data-bind=\"if: type() == &amp;#039;from_file&amp;#039;\"><\\/div>    \\\n            <div data-bind=\"if: type() == &amp;#039;from_url&amp;#039;\">\\\n                <img class=\"img-responsive\" data-bind=\"attr: { src: from_url() },visible: from_url()\" src=\"http:\\/\\/r2-store.distractify.netdna-cdn.com\\/postimage\\/201502\\/7\\/b5a20ad37fd7c6522bfa2f067bc5cb98_650x.jpg\">\\\n            <\\/div>\\\n            <div data-bind=\"if: type() == &amp;#039;youtube&amp;#039;\"><\\/div>\\\n        <p data-bind=\"text: image_caption\">Longtime CBS News reporter and “60 Minutes” correspondent Bob Simon has died following a car crash. The 73 year-old journalist was reportedly a passenger when his car collided with another vehicle at a stop light. It’s suspected that the driver of Simon’s vehicle may have had a heart attack, leading to the tragedy.<\\/p>     \\\n";
		webview.loadData(customHtml, "text/html", "UTF-8");
		
	}

	@Override
	protected void onStart() {
		super.onStart();
		// Get an Analytics tracker to report app starts & uncaught exceptions
		// etc.
		GoogleAnalytics.getInstance(this).reportActivityStart(this);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Actionbar.getInstance().appendTo(this);
		Slidemenu.getInstance().appendTo(this);
		Actionbar.getInstance().showSlidingMenu(View.VISIBLE);
		Actionbar.getInstance().showBack(View.INVISIBLE);
	}
}
