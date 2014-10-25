	package com.haui.japanse.cache;

import java.io.Serializable;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.haui.japanese.util.Variable;

public class CacheImpl implements ICache {

	Context mContext;
	SharedPreferences msPreferences;
	String key;

	public CacheImpl(Context mContext) {
		super();
		this.mContext = mContext;
		msPreferences = mContext.getSharedPreferences(Variable.CACHE_FILE_NAME,
				0);
	}

	@Override
	public void saveData(Object obj) {
		SharedPreferences.Editor editor = msPreferences.edit();
		Gson gson = new Gson();
		String json = gson.toJson(obj);
		editor.putString(key, json);
		editor.commit();

	}

	@Override
	public Object getData(String key, Class clas) {
		Gson gson = new Gson();
		String json = msPreferences.getString(key, null);
		if (json != null) {
			Object obj = gson.fromJson(json, clas);
			return obj;
		} else {
			return null;
		}

	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

}
