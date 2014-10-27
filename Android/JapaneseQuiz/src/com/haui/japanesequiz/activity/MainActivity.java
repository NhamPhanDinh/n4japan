package com.haui.japanesequiz.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.haui.japanese.adapter.MainGridViewAdapter;
import com.haui.japanese.controller.DownloadFile;
import com.haui.japanese.controller.JsonParse;
import com.haui.japanese.model.ListQuiz;
import com.haui.japanese.sqlite.DBCache;
import com.haui.japanese.util.CommonUtils;
import com.haui.japanese.util.Variable;
import com.haui.japanese.view.DialogNotify;
import com.haui.japanse.cache.PassExtractCache;
import com.haui.japanse.cache.QuizListCache;
import com.haui.japanse.cache.VersionCache;

public class MainActivity extends ActionBarActivity {

	GridView gridViewMain;
	MainGridViewAdapter adapter;
	VersionCache versionCache;
	QuizListCache quizlistCache;
	PassExtractCache passExtraCache;
	String newVersion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.color.blue));
		getSupportActionBar().setTitle(
				Html.fromHtml("<b><font color='#ffffff'>"
						+ getResources().getString(R.string.app_name)
						+ "</font></b>"));
		setContentView(R.layout.activity_main);

		AdView mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

		DBCache db = new DBCache(this);
		versionCache = new VersionCache(getApplicationContext());
		quizlistCache = new QuizListCache(getApplicationContext());
		passExtraCache = new PassExtractCache(getApplicationContext());

		// Kiểm tra nếu có mạng thì check version
		if (CommonUtils.isOnline(getApplicationContext())) {
			// Tiến hành download vesion
			DownloadFile download = new DownloadFile(MainActivity.this,
					Variable.LINK_VERSION, "Version.json",
					layString(R.string.kietradulieu)) {

				@Override
				public void onDownloadComplete() {
					File file = getmFile();
					newVersion = JsonParse.getVersion(file);

					// version mới khác version cũ thì sẽ lưu lại và tải lại
					// danh sách các năm
					if (!versionCache.getVersion().equals(newVersion)) {
						versionCache.saveVersion(newVersion);
						dowloadListQuiz();
					} else {

						// Nếu version giống nhau thì kiểm tra xem danh sách đề
						// các năm đã có chưa
						if (quizlistCache.getListQuiz() == null) {

							// nếu chưa có đề thì tiến hành download danh sách
							// đề
							dowloadListQuiz();
						}

					}

					initView();
				}

			};
			download.execute();
		} else {

			if (quizlistCache.getListQuiz() == null) {

				showDialogNoData();
			}

			initView();

		}

	}

	void showDialogNoData() {

		DialogNotify dialog = new DialogNotify(MainActivity.this,
				layString(R.string.msg), layString(R.string.nodata),
				layString(R.string.close), layString(R.string.cancel)) {

			@Override
			public void onOKClick() {
				finish();

			}

			@Override
			public void onCancelClick() {

			}
		};

	}

	void dowloadListQuiz() {
		DownloadFile dowloadListQuiz = new DownloadFile(MainActivity.this,
				Variable.LINK_LISTQUIZ, "list.json",
				layString(R.string.taidanhsach)) {

			@Override
			public void onDownloadComplete() {
				File fileListQuiz = getmFile();
				ListQuiz newListQuiz = JsonParse.getListQuiz(fileListQuiz);
				quizlistCache.saveQuizList(newListQuiz);
				downloadPass();
			}
		};
		dowloadListQuiz.execute();
	}

	void downloadPass() {
		DownloadFile downloadPass = new DownloadFile(MainActivity.this,
				Variable.LINK_PASS, "passzip.txt",
				layString(R.string.kietradulieu)) {

			@Override
			public void onDownloadComplete() {
				File filePass = getmFile();
				String pass = JsonParse.getPass(filePass);
				passExtraCache.saveData(pass);

			}
		};
		downloadPass.execute();
	}

	String layString(int id) {
		return getResources().getString(id);
	}

	void initView() {
		gridViewMain = (GridView) findViewById(R.id.gridViewMain);
		List<String> listTitle = Arrays.asList(Variable.TITLE);
		List<Integer> listBackgroud = Arrays.asList(Variable.BACKGROUD);
		List<Drawable> listIcon = new ArrayList<Drawable>();
		listIcon.add(getDrawable(R.drawable.voc));
		listIcon.add(getDrawable(R.drawable.grammar));
		listIcon.add(getDrawable(R.drawable.lis));
		listIcon.add(getDrawable(R.drawable.about));

		adapter = new MainGridViewAdapter(listTitle, listBackgroud, listIcon,
				MainActivity.this);
		gridViewMain.setAdapter(adapter);
		gridViewMain.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (quizlistCache.getListQuiz() == null && position != 3) {
					showDialogNoData();
				} else {
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

					}

				}
				if (position == 3) {
					AlertDialog.Builder dialog = new AlertDialog.Builder(
							MainActivity.this);
					dialog.setTitle(layString(R.string.info));
					dialog.setMessage(layString(R.string.infoContent) + " "
							+ versionCache.getVersion());
					dialog.setNegativeButton(layString(R.string.closeact),
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

								}
							});
					dialog.show();
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
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

}
