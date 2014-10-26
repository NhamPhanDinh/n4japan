package com.haui.japanese.controller;

import java.io.File;
import java.io.IOException;

import com.haui.japanese.util.CommonUtils;
import com.haui.japanese.util.Variable;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

public class DownloadService extends Service {

	DownloadManager downloadManager;
	NotificationManager nm;
	NotificationCompat.Builder notify;
	int idNotify;

	String url;
	File mFile;
	String fileName;

	int isOpen = 1;

	@Override
	public void onCreate() {
		super.onCreate();

	}

	void hideNotify() {
		nm.cancel(idNotify);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// Utils.showToastCustom(getApplicationContext(), "GetData Service");
		Bundle bd = intent.getExtras();
		this.url = bd.getString("url");
		this.fileName = bd.getString("filename");
		downLoad(this.url, this.fileName);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		/* Utils.showToastCustom(getApplicationContext(), "onBind"); */
		return null;
	}

	public void downLoad(String directLink, String fileName) {

		downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		Uri Download_Uri = Uri
				.parse(directLink);
		DownloadManager.Request request = new DownloadManager.Request(
				Download_Uri);

		// Restrict the types of networks over which this download may proceed.
		request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
				| DownloadManager.Request.NETWORK_MOBILE);
		// Set whether this download may proceed over a roaming connection.
		request.setAllowedOverRoaming(false);
		// Set the title of this download, to be displayed in notifications (if
		// enabled).
		request.setTitle("My Data Download");
		// Set a description of this download, to be displayed in notifications
		// (if enabled)
		request.setDescription("Android Data download using DownloadManager.");
		// Set the local destination for the downloaded file to a path within
		// the application's external files directory
		request.setDestinationInExternalFilesDir(this,
				Environment.DIRECTORY_DOWNLOADS, fileName);

		// Enqueue a new download and same the referenceId
		downloadManager.enqueue(request);

		IntentFilter itFilter = new IntentFilter(
				DownloadManager.ACTION_DOWNLOAD_COMPLETE);

		registerReceiver(broadcast, itFilter);

	}

	BroadcastReceiver broadcast = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			mFile = new File(Variable.FILE_DIRECTORY + fileName);
			CommonUtils.showToast(getApplicationContext(), "download complete");
			stopSelf();
		}
	};

}
