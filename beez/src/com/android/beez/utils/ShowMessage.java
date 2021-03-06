package com.android.beez.utils;

import java.io.File;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;

import com.android.beez.R;
import com.android.beez.app.AppController;

public class ShowMessage {
	private static BroadcastReceiver onComplete;

	public static void showDialogUpdateApp(final Context mContext) {
		try {

			if (mContext != null) {
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setTitle(mContext
						.getString(R.string.update_version_title));
				builder.setMessage(mContext
						.getString(R.string.update_version_warning));
				builder.setPositiveButton(mContext.getString(R.string.OK),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								SharedPreferences shared = AppController
										.getInstance().getSharedPreferences();
								if (shared.contains("android_share_url")) {
									final String fileName="musicarc.apk";
									String link = shared.getString(
											"android_share_url", "");
									if(link.contains("play.google.com")){
										Intent i = new Intent(
												Intent.ACTION_VIEW);
										i.setData(Uri.parse(link));
										mContext.startActivity(i);
										return;
									}
									downloadAndInstall(mContext,link,fileName);
								}
									
							}
						});
				builder.setNegativeButton(mContext.getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Canceled.
							}
						});
				builder.show();
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public static void showMessage(Context mContext, String message) {
		if (mContext != null) {
			Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
		}
	}
	
	private static void downloadAndInstall(final Context mContext,String link,String fileName){
		onComplete = new BroadcastReceiver() {
			public void onReceive(Context ctxt,
					Intent intent) {
				long id = intent
						.getExtras()
						.getLong(
								DownloadManager.EXTRA_DOWNLOAD_ID);
				DownloadManager dm = (DownloadManager) mContext
						.getSystemService(Context.DOWNLOAD_SERVICE);
				Intent promptInstall = new Intent(
						Intent.ACTION_VIEW);
				promptInstall.setDataAndType(
						dm.getUriForDownloadedFile(id),
						"application/vnd.android.package-archive");
				mContext.startActivity(promptInstall);
				promptInstall
						.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.unregisterReceiver(onComplete);
			}
		};
		Uri mUri = Uri.parse(link);
		DownloadManager.Request r = new DownloadManager.Request(
				mUri);
		r.setMimeType("application/vnd.android.package-archive");
		r.setDestinationInExternalPublicDir(
				Environment.DIRECTORY_DOWNLOADS,fileName);
		r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		DownloadManager dm = (DownloadManager) mContext
				.getSystemService(Context.DOWNLOAD_SERVICE);
		dm.enqueue(r);
		mContext.registerReceiver(
				onComplete,
				new IntentFilter(
						DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}

}
