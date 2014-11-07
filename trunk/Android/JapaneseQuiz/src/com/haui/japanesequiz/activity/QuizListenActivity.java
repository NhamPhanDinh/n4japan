package com.haui.japanesequiz.activity;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.haui.japanese.adapter.QuizViewListenAdapter;
import com.haui.japanese.adapter.QuizViewPagerAdapter;
import com.haui.japanese.broadcast.MenuClickBroadCast;
import com.haui.japanese.broadcast.PagerSelectBroadCast;
import com.haui.japanese.controller.DownloadFile;
import com.haui.japanese.controller.JsonParse;
import com.haui.japanese.model.DoQuiz;
import com.haui.japanese.model.Exam;
import com.haui.japanese.model.Question;
import com.haui.japanese.sqlite.DBCache;
import com.haui.japanese.util.CommonUtils;
import com.haui.japanese.util.FileUntils;
import com.haui.japanese.util.Variable;
import com.haui.japanese.view.DialogNotify;
import com.haui.japanse.cache.CountClickCache;
import com.haui.japanse.cache.PassExtractCache;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.viewpagerindicator.TitlePageIndicator;

public class QuizListenActivity extends Application {

	SlidingMenu sm;
	ImageView btnPlay;
	CanvasTransformer mTransformer;
	ViewPager pager;
	TitlePageIndicator indicator;
	QuizViewListenAdapter pagerAdapter;
	// List<Question> listQuestion;
	public static TextView tvScore;
	TextView tvSumaryAnswer, tvTimeCountDown;
	Timer timer;
	Handler handler;
	int time = 0;
	int totalTime;
	int lastSelect;
	boolean nhan = false;
	SeekBar seekbar;
	MediaPlayer mediaPlayer;
	long startTime = 0;

	int year;
	int type;
	PassExtractCache passExtractCache;
	DBCache db;
	ProgressDialog proLoadAudio;
	String title;
	String link;
	/**
	 * Biến cờ cho biết có load lại dữ liệu lên hay không
	 */
	boolean loadData = false;
	int positionInitPager;

	private InterstitialAd interstitial;
	CountClickCache countClickCache;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz_listen);

		AdView mAdView = (AdView) findViewById(R.id.adView4);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);

		// Create the interstitial.
		interstitial = new InterstitialAd(this);
		interstitial
				.setAdUnitId(getResources().getString(R.string.admodeInApp));

		// Create ad request.

		// Begin loading your interstitial.
		interstitial.loadAd(adRequest);

		passExtractCache = new PassExtractCache(getApplicationContext());
		db = new DBCache(getApplicationContext());
		countClickCache = new CountClickCache(getApplicationContext());

		Bundle bd = getIntent().getExtras();
		loadData = bd.getBoolean("load");
		positionInitPager = bd.getInt("positionInit", 0);
		year = bd.getInt("year");
		type = bd.getInt("type");
		if (loadData) {

			title = "Lis";
			link = Variable.HOST_DATA + year + "/Listerning/";
			loadQuestion();

		} else {
			initView();
			loadSlideMenu();
		}

	}

	String layString(int id) {
		return getResources().getString(id);
	}

	public void displayInterstitial() {
		if (interstitial.isLoaded()) {
			interstitial.show();
		}
	}

	/**
	 * Load câu hỏi từ sdcard
	 */
	void loadQuestion() {

		String title = "Lis";
		String link = Variable.HOST_DATA + year + "/Listerning/";

		File file = new File(Variable.FILE_DIRECTORY + title + year + ".zip");
		final File fileJson = new File(Variable.FILE_DIRECTORY + title + year
				+ ".json");

		if (!file.exists()) {

			if (CommonUtils.isOnline(getApplicationContext())) {
				DownloadFile downloadZip = new DownloadFile(
						QuizListenActivity.this, link + title + year + ".zip",
						title + year + ".zip", layString(R.string.taicauhoi)) {

					@Override
					public void onDownloadComplete() {
						File fileDownload = getmFile();
						
						try {
							FileUntils.ExtractFile(fileDownload, new File(
									Variable.FILE_DIRECTORY), passExtractCache
									.getPass());
							dataToView(fileJson);
						} catch (Exception e) {
							//Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
							showDialogNoConnect();
							FileUntils.deleteFile(fileDownload);
						}

						
					/*	if (DoQuiz.exam.getListQuestion() == null) {
							showDialogNoConnect();
							FileUntils.deleteFile(fileDownload);
						} else {
							loadAudio();
						}
*/
					}
				};
				downloadZip.execute();
			} else {

				DialogNotify dialogNoConnect = new DialogNotify(
						QuizListenActivity.this,
						layString(R.string.load_data_no_Connect_title),
						layString(R.string.load_data_no_Connect_mgs),
						layString(R.string.closeact)) {

					@Override
					public void onOKClick() {
						finish();

					}

					@Override
					public void onCancelClick() {
						finish();

					}
				};

			}

		} else {
			if (!CommonUtils.isOnline(getApplicationContext())) {
				DialogNotify dialogNoConnect = new DialogNotify(
						QuizListenActivity.this, layString(R.string.msg),
						layString(R.string.image_no_connect),
						layString(R.string.closeact)) {

					@Override
					public void onOKClick() {

					}

					@Override
					public void onCancelClick() {

					}
				};
			}
			FileUntils.ExtractFile(file, new File(Variable.FILE_DIRECTORY),
					passExtractCache.getPass());
			dataToView(fileJson);

			//loadAudio();

		}

	}

	void showDialogNoConnect() {
		DialogNotify dialogLoadFail = new DialogNotify(QuizListenActivity.this,
				layString(R.string.load_data_no_Connect_title),
				layString(R.string.load_data_error),
				layString(R.string.closeact)) {

			@Override
			public void onOKClick() {
				finish();

			}

			@Override
			public void onCancelClick() {
				finish();

			}
		};
	}

	void dataToView(File file) {
		List<Question> questions = JsonParse.listQuestion(file, year, type);
		if (questions == null) {
			showDialogNoConnect();
		} else {
			DoQuiz.exam = new Exam();
			DoQuiz.exam.listQuestion = questions;
			DoQuiz.exam.scoreWrong = 0;
			DoQuiz.exam.scoreRight = 0;
			DoQuiz.exam.time = 0;
			DoQuiz.exam.sumaryAnswer = 0;
			loadAudio();
		}
	}

	void loadAudio() {

		File file = new File(Variable.FILE_DIRECTORY + title + year + ".mp3");
		if (!file.exists()) {

			DownloadFile downloadAudio = new DownloadFile(
					QuizListenActivity.this, link + title + year + ".mp3",
					title + year + ".mp3", layString(R.string.taiaudio)) {

				@Override
				public void onDownloadComplete() {
					File fileDownload = getmFile();
					mediaPlayer = MediaPlayer.create(QuizListenActivity.this,
							Uri.parse(fileDownload.getPath()));
					mediaPlayer.setLooping(false);
					loadCustomView();
					initView();
					loadSlideMenu();

				}
			};
			downloadAudio.execute();

		} else {

			mediaPlayer = MediaPlayer.create(QuizListenActivity.this,
					Uri.parse(file.getPath()));
			mediaPlayer.setLooping(false);
			loadCustomView();
			initView();
			loadSlideMenu();
		}

	}

	BroadcastReceiver broadcast = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			proLoadAudio.dismiss();
			File fileDownload = new File(Variable.FILE_DIRECTORY + title + year
					+ ".mp3");
			mediaPlayer = MediaPlayer.create(QuizListenActivity.this,
					Uri.parse(fileDownload.getPath()));
			mediaPlayer.setLooping(false);
			loadCustomView();
			initView();
			loadSlideMenu();

		}
	};

	/**
	 * Khởi tạo các view
	 */
	void initView() {

		btnPlay = (ImageView) findViewById(R.id.btnPlay);
		btnPlay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btnPlay.setBackgroundResource(R.drawable.pauseaudio);
				btnPlay.setClickable(false);
				btnPlay.setEnabled(false);
				mediaPlayer.start();
				totalTime = mediaPlayer.getDuration();
				tvTimeCountDown.setText(CommonUtils.getTimeString(totalTime));
				seekbar.setMax(totalTime);

				handler = new Handler();
				timer = new Timer();
				timer.schedule(new TimerTask() {

					@Override
					public void run() {
						UpdateTIME();

					}

				}, 1000, 1000);
			}
		});
		pager = (ViewPager) findViewById(R.id.pager);
		indicator = (TitlePageIndicator) findViewById(R.id.indicator);

		tvSumaryAnswer = (TextView) findViewById(R.id.tvSumaryAnswer);

		tvTimeCountDown = (TextView) findViewById(R.id.tvTimeCountDown);
		tvTimeCountDown.setText("");
		seekbar = (SeekBar) findViewById(R.id.seekbar);
		seekbar.setEnabled(false);
		seekbar.setProgress((int) startTime);
		if (!loadData) {
			tvTimeCountDown.setVisibility(View.GONE);
			btnPlay.setVisibility(View.GONE);
			seekbar.setVisibility(View.GONE);
		} else {
			tvTimeCountDown.setVisibility(View.VISIBLE);
			btnPlay.setVisibility(View.VISIBLE);
			seekbar.setVisibility(View.VISIBLE);
		}

		tvSumaryAnswer.setText(DoQuiz.exam.sumaryAnswer + "/"
				+ DoQuiz.exam.listQuestion.size());
		pagerAdapter = new QuizViewListenAdapter(getSupportFragmentManager(),
				DoQuiz.exam.listQuestion, loadData, QuizListenActivity.this);
		pager.setAdapter(pagerAdapter);
		pager.setCurrentItem(positionInitPager);
		indicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int pos) {
				// TODO Auto-generated method stub
				if (pos == (DoQuiz.exam.listQuestion.size() - 1)) {
					if (DoQuiz.exam.sumaryAnswer == DoQuiz.exam.listQuestion
							.size()) {
						if (loadData) {
							finishQuiz();
						}

					}
				}

				if (nhan) {
					// Gửi thông báo cho FragmentQuiz để cập nhật câu vừa làm
					Intent intent = new Intent("com.haui.japanese.ANSWER_CLICK");
					DoQuiz.exam.listQuestion.get(lastSelect).setCompare(true);
					intent.putExtra("fragment", 1);
					intent.putExtra("position", lastSelect);
					sendBroadcast(intent);
					nhan = false;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});

		indicator.setTextColor(getResources().getColor(R.color.black_nhat));
		indicator.setSelectedColor(getResources().getColor(R.color.black));
		indicator.setViewPager(pager);

		IntentFilter filter = new IntentFilter("com.haui.japanese.MENU_CLICK");
		registerReceiver(menuClickBroadCast, filter);

		IntentFilter filter2 = new IntentFilter("com.haui.japanese.PAGER_CLICK");
		registerReceiver(pagerClickBroadCast, filter2);

	}

	/**
	 * Handler hiển thị thời gian làm quiz
	 */

	private void UpdateTIME() {
		handler.post(new Runnable() {

			@Override
			public void run() {
				tvTimeCountDown.setText(CommonUtils.getTimeString(totalTime
						- time));
				if (time < totalTime - 1000) {
					time = mediaPlayer.getCurrentPosition();
					seekbar.setProgress(time);
					DoQuiz.exam.time += 1000;

				} else {
					timer.cancel();
					Toast.makeText(getApplicationContext(),
							layString(R.string.timeout), Toast.LENGTH_SHORT)
							.show();
					DoQuiz.exam.listQuestion.get(lastSelect).setCompare(true);
					timer.cancel();
					mediaPlayer.release();
					Intent itResult = new Intent(QuizListenActivity.this,
							ResultQuiz.class);
					itResult.putExtra("year", year);
					itResult.putExtra("type", type);
					startActivity(itResult);
					finish();
				}

			}
		});

	}

	/**
	 * khởi tạo view điểm trên actionbar
	 */
	void loadCustomView() {
		View v = getLayoutInflater().inflate(R.layout.custom_text_actionbar,
				null);
		LayoutParams layout = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, Gravity.RIGHT
						| Gravity.CENTER_VERTICAL);
		tvScore = (TextView) v.findViewById(R.id.tvScore);
		getSupportActionBar().setCustomView(v, layout);
		getSupportActionBar().setDisplayShowCustomEnabled(true);

	}

	/**
	 * Khởi tạo slide menu
	 */
	void loadSlideMenu() {
		try {

			mTransformer = new CanvasTransformer() {

				@Override
				public void transformCanvas(Canvas canvas, float percentOpen) {
					canvas.translate(
							0,
							canvas.getHeight()
									* (1 - interp.getInterpolation(percentOpen)));
				}
			};

			sm = new SlidingMenu(this);
			sm.setShadowWidth(R.dimen.shadow_width);
			// sm.setShadowDrawable(R.drawable.slidingmenu_shadow);
			sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
			sm.setFadeDegree(0.35f);
			sm.setTouchModeAbove(SlidingMenu.LEFT);
			sm.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
			sm.setBehindScrollScale(0.0f);
			sm.setBehindCanvasTransformer(mTransformer);
			sm.setMenu(R.layout.menu_frame);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.menu_frame, new MenuFragment()).commit();
		} catch (Exception e) {
			Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG)
					.show();
		}
	}

	void finishQuiz() {
		DialogNotify dialog = new DialogNotify(QuizListenActivity.this,
				layString(R.string.titleketthuc),
				layString(R.string.msgkethuc), layString(R.string.ok),
				layString(R.string.cancel)) {

			@Override
			public void onOKClick() {
				DoQuiz.exam.listQuestion.get(lastSelect).setCompare(true);
				Exam exam = new Exam();
				exam.setId(year + "-" + type);
				exam.setListQuestion(DoQuiz.exam.listQuestion);
				exam.setScoreWrong(DoQuiz.exam.scoreWrong);
				exam.setSumaryAnswer(DoQuiz.exam.sumaryAnswer);
				exam.setTime(DoQuiz.exam.time);

				db.inserOrUpdate(year + "-" + type, exam);
				try {
					if (mediaPlayer.isPlaying()) {
						mediaPlayer.stop();
						mediaPlayer.release();
						timer.cancel();
					}

				} catch (Exception e) {
				}
				Intent itResult = new Intent(QuizListenActivity.this,
						ResultQuiz.class);
				itResult.putExtra("year", year);
				itResult.putExtra("type", type);
				startActivity(itResult);
				finish();

			}

			@Override
			public void onCancelClick() {
				// TODO Auto-generated method stub

			}
		};
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_quiz, menu);
		return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			sm.toggle();
		} else if (item.getItemId() == R.id.menuCheck) {

			int count = countClickCache.getCount();
			if (count == 4) {
				displayInterstitial();
			}
			countClickCache.saveIncreateCount();

			if (loadData) {
				finishQuiz();
			} else {
				finish();
			}
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Broastcast nhận sự kiện click từ bên slide menu
	 */
	MenuClickBroadCast menuClickBroadCast = new MenuClickBroadCast() {

		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bd = intent.getExtras();
			sm.toggle();
			int positiion = bd.getInt("position");
			if (positiion == -1) {
				if (loadData) {
					finishQuiz();
				} else {
					finish();
				}
			} else {
				pager.setCurrentItem(positiion);
			}
			super.onReceive(context, intent);
		}

	};

	/**
	 * Broadcast nhận sự kiện từ FragmentQuiz tham số truyền sang là vị trị câu
	 * hỏi vừa click
	 */
	PagerSelectBroadCast pagerClickBroadCast = new PagerSelectBroadCast() {

		@Override
		public void onReceive(Context context, Intent intent) {
			lastSelect = intent.getExtras().getInt("position");
			int numerAnswer = 0;
			for (Question q : DoQuiz.exam.listQuestion) {
				if (q.getAnswer_choose() != -1) {
					numerAnswer++;
				}
			}
			DoQuiz.exam.sumaryAnswer = numerAnswer;
			tvSumaryAnswer.setText(numerAnswer + "/"
					+ DoQuiz.exam.listQuestion.size());
			if (DoQuiz.exam.sumaryAnswer == DoQuiz.exam.listQuestion.size()) {
				finishQuiz();
			}
			nhan = true;
			super.onReceive(context, intent);
		}

	};

	@Override
	public void onBackPressed() {
		if (loadData) {
			finishQuiz();
		} else {
			finish();
		}

	}

	private static Interpolator interp = new Interpolator() {
		@Override
		public float getInterpolation(float t) {
			t -= 1.0f;
			return t * t * t + 1.0f;
		}
	};
}
