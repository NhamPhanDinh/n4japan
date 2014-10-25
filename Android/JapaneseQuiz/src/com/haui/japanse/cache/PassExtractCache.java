package com.haui.japanse.cache;

import com.haui.japanese.util.Variable;

import android.content.Context;

public class PassExtractCache extends CacheImpl {

	public PassExtractCache(Context mContext) {
		super(mContext);
		setKey(Variable.CACHE_PASS_EXTRACT);
	}

	public void savePass(String pass) {
		saveData(pass);
	}

	public String getPass() {
		if (getData(getKey(), String.class) != null) {
			String pass = (String) getData(getKey(), String.class);
			return pass;
		} else {
			return null;
		}
	}

}
