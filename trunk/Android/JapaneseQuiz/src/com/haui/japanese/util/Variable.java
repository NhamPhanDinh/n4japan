package com.haui.japanese.util;

import java.io.File;

import android.os.Environment;

import com.haui.japanesequiz.activity.R;

public class Variable {

	public static final String[] TITLE = { "Vocabulary", "Grammar",
			"Listening", "About" };
	public static final Integer[] BACKGROUD = { R.color.green, R.color.red,
			R.color.yellow_menu, R.color.blue };
	public static final String CACHE_FILE_NAME = "cache_data";
	public static final String CACHE_VERSION = "version";
	public static final String CACHE_QUIZ_LIST = "quiz_list";
	public static final String CACHE_PASS_EXTRACT = "pass_extract";
	public static final String FILE_DIRECTORY = Environment
			.getExternalStorageDirectory() + "/JNPT";
	public static final String HOST = "http://jlpt-n4.esy.es/web/";
	public static final String LINK_VERSION = "http://jlpt-n4.esy.es/web/Version/Version.json";
	public static final String LINK_PASS = "http://jlpt-n4.esy.es/web/passzip.txt";
	public static final String LINK_LISTQUIZ = "http://jlpt-n4.esy.es/web/List/list.json";
	public static final String LINK_DATA = "http://jlpt-n4.esy.es/web/Data/";
}
