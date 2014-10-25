package com.haui.japanese.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CommonUtils {
	public static String getTimeString(long time) {
		SimpleDateFormat format = new SimpleDateFormat("mm:ss");
		return format.format(new Date(time));
	}

	public static boolean isOnline(Context ct) {
		ConnectivityManager conMgr = (ConnectivityManager) ct
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

		if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()
				|| !netInfo.isConnectedOrConnecting()) {
			return false;
		} else {
		}
		return true;
	}
}
