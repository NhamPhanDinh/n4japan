package com.haui.japanesequiz.activity;

import java.io.File;
import java.util.List;

import com.haui.japanese.controller.DownloadFile;
import com.haui.japanese.controller.JsonParse;
import com.haui.japanese.model.Question;
import com.haui.japanese.util.CommonUtils;
import com.haui.japanese.util.FileUntils;
import com.haui.japanese.util.Variable;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class Test extends ActionBarActivity {

	TextView tvTest;
	DownloadManager downloadManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		tvTest = (TextView) findViewById(R.id.tvTest);
		
		File direct = new File(Variable.FILE_DIRECTORY);
		if (!direct.exists()) {
			direct.mkdirs();
		}

		downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		Uri Download_Uri = Uri
				.parse("http://jlpt-n4.esy.es/web/Data/2006/Listerning/Lis2006.mp3");
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
		request.setDestinationInExternalPublicDir("/jnpt", "Lis2006.mp3");

		// Enqueue a new download and same the referenceId
		downloadManager.enqueue(request);


}

}
