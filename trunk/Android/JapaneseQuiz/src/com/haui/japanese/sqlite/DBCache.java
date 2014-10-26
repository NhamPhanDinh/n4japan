package com.haui.japanese.sqlite;

import com.google.gson.Gson;
import com.haui.japanese.model.Exam;
import com.haui.japanese.util.CommonUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DBCache extends SQLiteOpenHelper {

	public static final String DBNAME = "datacache";
	public static final String DB_TABLE = "data";
	public static final String TB_ID = "id";
	public static final String TB_EXAM = "exam";
	public static final int DB_VERSION = 1;
	public static final String CREATE = "create table " + DB_TABLE + "("
			+ TB_ID + " TEXT PRIMARY KEY," + TB_EXAM + " TEXT)";

	Context context;

	public DBCache(Context context) {
		super(context, DBNAME, null, DB_VERSION);
		this.context = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
		onCreate(db);
	}

	public Exam getExam(String id) {
		String sql = "select * from " + DB_TABLE + " where " + TB_ID + "= '"
				+ id + "'";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor c = db.rawQuery(sql, null);
		if (c.getCount() == 0) {
			return null;
		} else {
			c.moveToFirst();
			String data = c.getString(c.getColumnIndex(TB_EXAM));
			Gson gson = new Gson();
			Exam exam = gson.fromJson(data, Exam.class);
			return exam;
		}
	}

	public long delete(String id) {
		SQLiteDatabase db = this.getWritableDatabase();
		return db.delete(DB_TABLE, TB_ID + "=" + id, null);
	}

	public long inserOrUpdate(String id, Exam exam) {
		SQLiteDatabase db = this.getWritableDatabase();
		Gson gson = new Gson();
		String json = gson.toJson(exam);
		ContentValues cv = new ContentValues();
		cv.put(TB_ID, id);
		cv.put(TB_EXAM, json);

		String sql = "select * from " + DB_TABLE + " where " + TB_ID + "= '"
				+ id + "'";
		Cursor c = db.rawQuery(sql, null);
		if (c.getCount() == 0) {
			return db.insert(DB_TABLE, null, cv);
		} else {
			return db.update(DB_TABLE, cv, TB_ID + "='" + id + "'", null);
		}

	}

}
