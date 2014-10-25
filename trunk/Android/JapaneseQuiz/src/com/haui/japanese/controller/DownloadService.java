package com.haui.japanese.controller;

import java.io.File;

import com.haui.japanese.util.Variable;

import android.app.DownloadManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

public abstract class DownloadService {

	String url;
	Context mContext;
	File mFile;
	String fileName;
	NotificationManager nm;
	
	public abstract void onDownloadComplete();

	public DownloadService(Context mContext, String url, String fileName) {
		super();
		this.url = url;
		this.mContext = mContext;
		this.fileName = fileName;
		downLoadFile();
		nm=(NotificationManager) mContext.getSystemService(mContext.NOTIFICATION_SERVICE);
	}

	public void downLoadFile() {

		File direct = new File(Variable.FILE_DIRECTORY);
		if (!direct.exists()) {
			direct.mkdirs();
		}

		DownloadManager.Request request = new DownloadManager.Request(
				Uri.parse(this.url));
		request.setDescription("Đang tải...");
		request.setTitle("Tải xuống dữ liệu");

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			request.allowScanningByMediaScanner();
			request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		}
		request.setDestinationInExternalPublicDir("/jnpt", fileName);

		DownloadManager manager = (DownloadManager) mContext
				.getSystemService(Context.DOWNLOAD_SERVICE);
		manager.enqueue(request);

		IntentFilter itFilter = new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE);
		mContext.registerReceiver(broadcast, itFilter);
	}

	BroadcastReceiver broadcast = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			mFile = new File(Variable.FILE_DIRECTORY, fileName);
			//nm.cancelAll();
			onDownloadComplete();
		}
	};

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public File getmFile() {
		return mFile;
	}

}
