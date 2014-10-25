package com.haui.japanse.cache;

import com.haui.japanese.util.Variable;

import android.content.Context;

public class VersionCache extends CacheImpl {

	public VersionCache(Context mContext) {
		super(mContext);
		setKey(Variable.CACHE_VERSION);
	}

	public void saveVersion(String version) {
		saveData(version);

	}

	public String getVersion() {
		if (getData(getKey(), String.class) != null) {
			String version = (String) getData(getKey(), String.class);
			return version;
		} else {
			return "0";
		}
	}

}
