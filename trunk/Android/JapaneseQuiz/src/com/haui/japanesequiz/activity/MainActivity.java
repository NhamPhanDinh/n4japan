package com.haui.japanesequiz.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.haui.japanese.adapter.MainGridViewAdapter;
import com.haui.japanese.controller.DownloadService;
import com.haui.japanese.controller.JsonParse;
import com.haui.japanese.model.ListQuiz;
import com.haui.japanese.sqlite.DBCache;
import com.haui.japanese.util.CommonUtils;
import com.haui.japanese.util.Variable;
import com.haui.japanese.view.DialogNotify;
import com.haui.japanse.cache.QuizListCache;
import com.haui.japanse.cache.VersionCache;

public class MainActivity extends ActionBarActivity {

	GridView gridViewMain;
	MainGridViewAdapter adapter;
	VersionCache versionCache;
	QuizListCache quizlistCache;
	String newVersion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		DBCache db = new DBCache(this);
		versionCache = new VersionCache(getApplicationContext());
		quizlistCache = new QuizListCache(getApplicationContext());

		if (CommonUtils.isOnline(getApplicationContext())) {
			DownloadService download = new DownloadService(
					getApplicationContext(), Variable.LINK_VERSION,
					"Version.json") {

				@Override
				public void onDownloadComplete() {
					File file = getmFile();
					newVersion = JsonParse.getVersion(file);
					if (versionCache.getVersion().equals(newVersion)) {
						versionCache.saveVersion(newVersion);
						dowloadListQuiz();

					}else{
						if(quizlistCache.getListQuiz()==null){
							dowloadListQuiz();
						}
						initView();
					}
				}

			};
		} else {

			if(quizlistCache.getListQuiz()==null){
				DialogNotify dialog = new DialogNotify(
						MainActivity.this,
						"Thông báo",
						"Cần có kết nối mạng để tải dữ liệu lần đầu. Hãy bật kết nối mạng",
						"Đóng ứng dụng", "Hủy") {

					@Override
					public void onOKClick() {
						finish();

					}

					@Override
					public void onCancelClick() {
						

					}
				};
			}else{
				initView();
			}
			

		}

		
	}

	void dowloadListQuiz() {
		DownloadService dowloadListQuiz = new DownloadService(
				getApplicationContext(), Variable.LINK_LISTQUIZ, "list.json") {

			@Override
			public void onDownloadComplete() {
				File fileListQuiz = getmFile();
				ListQuiz newListQuiz = JsonParse.getListQuiz(fileListQuiz);
				quizlistCache.saveQuizList(newListQuiz);

			}
		};
		
	}

	void initView() {
		gridViewMain = (GridView) findViewById(R.id.gridViewMain);
		List<String> listTitle = Arrays.asList(Variable.TITLE);
		List<Integer> listBackgroud = Arrays.asList(Variable.BACKGROUD);
		List<Drawable> listIcon = new ArrayList<Drawable>();
		listIcon.add(getDrawable(R.drawable.watch));
		listIcon.add(getDrawable(R.drawable.wrong_mark));
		listIcon.add(getDrawable(R.drawable.right_mark));
		listIcon.add(getDrawable(R.drawable.ic_action_actions));

		adapter = new MainGridViewAdapter(listTitle, listBackgroud, listIcon,
				MainActivity.this);
		gridViewMain.setAdapter(adapter);
		gridViewMain.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					Intent it1 = new Intent(MainActivity.this,
							ListExamActivity.class);
					it1.putExtra("type", 1);
					startActivity(it1);
					break;
				case 1:
					Intent it2 = new Intent(MainActivity.this,
							ListExamActivity.class);
					it2.putExtra("type", 2);
					startActivity(it2);
					break;
				case 2:
					Intent it3 = new Intent(MainActivity.this,
							ListExamActivity.class);
					it3.putExtra("type", 3);
					startActivity(it3);
					break;
				case 3:

					break;
				}

			}
		});

	}

	Drawable getDrawable(int rs) {
		return getResources().getDrawable(rs);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * btnQuiz = (Button) findViewById(R.id.btnQuiz);
	 * btnQuiz.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { Intent it = new
	 * Intent(MainActivity.this, QuizActivity.class); it.putExtra("load", true);
	 * it.putExtra("positionInit", 0); startActivity(it);
	 * 
	 * } });
	 * 
	 * btnQuizNoLoad = (Button) findViewById(R.id.btnQuizNoLoad);
	 * btnQuizNoLoad.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { Intent it = new
	 * Intent(MainActivity.this, QuizListenActivity.class); it.putExtra("load",
	 * true); it.putExtra("positionInit", 0); startActivity(it);
	 * 
	 * } });
	 */

}
