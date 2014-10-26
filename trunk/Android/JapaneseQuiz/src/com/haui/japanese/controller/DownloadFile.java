package com.haui.japanese.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.haui.japanese.util.Variable;

public abstract class DownloadFile extends AsyncTask<Void, String, File> {

	Context mContext;
	String url;
	File mFile;
	String fileName;
	ProgressDialog progress;
	String tite;

	public abstract void onDownloadComplete();

	public DownloadFile(Context mContext, String url, String fileName,
			String title) {
		super();
		this.mContext = mContext;
		this.url = url;
		this.fileName = fileName;
		progress = new ProgressDialog(mContext);
		this.tite = title;
		progress.setMessage(title);
		progress.setIndeterminate(false);
		progress.setMax(100);
		progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progress.setCancelable(false);
	}

	@Override
	protected void onPreExecute() {
		progress.show();
		super.onPreExecute();
	}

	@Override
	protected File doInBackground(Void... params) {
		int count;

		File direct = new File(Variable.FILE_DIRECTORY);
		if (!direct.exists()) {
			direct.mkdirs();
		}

		try {
			URL url = new URL(this.url);
			URLConnection conexion = url.openConnection();
			conexion.setConnectTimeout(300000);
			conexion.connect();

			int lenghtOfFile = conexion.getContentLength();

			InputStream input = new BufferedInputStream(url.openStream());
			OutputStream output = new FileOutputStream(Variable.FILE_DIRECTORY
					+ fileName);

			byte data[] = new byte[1024];

			long total = 0;

			while ((count = input.read(data)) != -1) {
				total += count;
				publishProgress("" + (int) ((total * 100) / lenghtOfFile));
				output.write(data, 0, count);
			}

			output.flush();
			output.close();
			input.close();
		} catch (Exception e) {
		}
		return new File(Variable.FILE_DIRECTORY + fileName);
	}

	@Override
	protected void onProgressUpdate(String... value) {
		progress.setProgress(Integer.parseInt(value[0]));
		super.onProgressUpdate(value);
	}

	@Override
	protected void onPostExecute(File result) {
		progress.dismiss();
		this.mFile = result;
		onDownloadComplete();
		super.onPostExecute(result);
	}

	public File getmFile() {
		return mFile;
	}

	public void setmFile(File mFile) {
		this.mFile = mFile;
	}

}
