package com.haui.japanse.cache;

import java.io.Serializable;

public interface ICache {

	/**
	 * Save obj to sharepreference
	 * 
	 * @param t
	 */
	public void saveData(Object obj);

	/**
	 * get data from sharepreference
	 * 
	 * @param key
	 * @return
	 */
	public Object getData(String key, Class clas);

}
