package com.haui.japanese.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class CommonUtils {

	/**
	 * Hiển thị thời gian theo time long
	 * 
	 * @param time
	 * @return
	 */
	public static String getTimeString(long time) {
		SimpleDateFormat format = new SimpleDateFormat("mm:ss");
		return format.format(new Date(time));
	}

	/**
	 * Kiểm tra trạng thái online của máy
	 * 
	 * @param ct
	 * @return
	 */
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

	/**
	 * Toast
	 * 
	 * @param context
	 * @param st
	 */
	public static void showToast(Context context, String st) {
		Toast.makeText(context, st, Toast.LENGTH_SHORT).show();
	}
}
