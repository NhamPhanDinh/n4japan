package com.haui.japanesequiz.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.haui.japanese.adapter.ExamGridAdapter;
import com.haui.japanese.model.DoQuiz;
import com.haui.japanese.model.ListQuiz;
import com.haui.japanese.sqlite.DBCache;
import com.haui.japanse.cache.QuizListCache;

public class ListExamActivity extends Application {

	GridView gridViewListExam;
	int type;
	ListQuiz listQuiz;
	QuizListCache quizListCache;
	ExamGridAdapter adapter;
	DBCache db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_list_exam);
		Bundle bd = getIntent().getExtras();
		type = bd.getInt("type");

		// so sánh type để set title cho phù hợp
		switch (type) {
		case 1:

			getSupportActionBar()
					.setTitle(
							Html.fromHtml("<b><font color='#ffffff'>Vocabulary</font></b>"));
			break;

		case 2:
			getSupportActionBar()
					.setTitle(
							Html.fromHtml("<b><font color='#ffffff'>Grammar</font></b>"));
			break;
		case 3:
			getSupportActionBar()
					.setTitle(
							Html.fromHtml("<b><font color='#ffffff'>Listening</font></b>"));
			break;
		}

		db = new DBCache(getApplicationContext());
		quizListCache = new QuizListCache(getApplicationContext());
		listQuiz = quizListCache.getListQuiz();
		gridViewListExam = (GridView) findViewById(R.id.gridViewListExam);
		loadGrid();
		gridViewListExam.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// Nếu exam chưa được làm thì sẽ chuyển sang phần làm mới
				if (adapter.getItem(position) == null) {
					if (type == 3) {
						Intent itListen = new Intent(ListExamActivity.this,
								QuizListenActivity.class);
						itListen.putExtra("year",
								listQuiz.getListQuiz().get(position));
						itListen.putExtra("type", type);
						itListen.putExtra("load", true);
						startActivity(itListen);
					} else {
						Intent itText = new Intent(ListExamActivity.this,
								QuizActivity.class);
						itText.putExtra("year",
								listQuiz.getListQuiz().get(position));
						itText.putExtra("type", type);
						itText.putExtra("load", true);
						startActivity(itText);
					}
				} else {

					// Nếu exam đã làm thì hiển thị kết quả đã làm trước đó
					DoQuiz.exam = db.getExam(listQuiz.getListQuiz().get(
							position)
							+ "-" + type);
					Intent itResult = new Intent(ListExamActivity.this,
							ResultQuiz.class);
					itResult.putExtra("year",
							listQuiz.getListQuiz().get(position));
					itResult.putExtra("type", type);
					startActivity(itResult);
				}

			}
		});
	}

	/**
	 * Load gridview
	 */
	void loadGrid() {
		adapter = new ExamGridAdapter(listQuiz.getListQuiz(), type,
				getApplicationContext());
		gridViewListExam.setAdapter(adapter);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		loadGrid();
		super.onResume();
	}

}
