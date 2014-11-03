package com.haui.japanse.cache;

import com.google.android.gms.internal.co;
import com.haui.japanese.util.Variable;

import android.content.Context;

public class CountClickCache extends CacheImpl {

	public CountClickCache(Context mContext) {
		super(mContext);
		setKey(Variable.CACHE_COUNT_CLICK);
	}

	public void saveIncreateCount() {
		int count =getCount();
		if (count < 5) {
			saveData(new Integer(++count));
		} else {
			count = 0;
			saveData(new Integer(count));
		}

	}

	public int getCount() {
		Integer option = (Integer) getData(getKey(), Integer.class);
		if (option == null) {
			return 0;
		} else {
			return option.intValue();
		}
	}

}
