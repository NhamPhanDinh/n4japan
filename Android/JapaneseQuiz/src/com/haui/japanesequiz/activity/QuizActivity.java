package com.haui.japanesequiz.activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
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
import android.view.animation.Interpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.haui.japanese.adapter.QuizViewPagerAdapter;
import com.haui.japanese.broadcast.MenuClickBroadCast;
import com.haui.japanese.broadcast.PagerSelectBroadCast;
import com.haui.japanese.controller.JsonParse;
import com.haui.japanese.model.Question;
import com.haui.japanese.model.DoQuiz;
import com.haui.japanese.util.CommonUtils;
import com.haui.japanese.view.DialogNotify;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.viewpagerindicator.TitlePageIndicator;

public class QuizActivity extends ActionBarActivity {

	SlidingMenu sm;
	CanvasTransformer mTransformer;
	ViewPager pager;
	TitlePageIndicator indicator;
	QuizViewPagerAdapter pagerAdapter;
	// List<Question> listQuestion;
	public static TextView tvScore;
	TextView tvSumaryAnswer, tvTime;
	Timer timer;
	Handler handler;
	long time = 180000;
	int lastSelect;
	// Question questionSelect = null;
	boolean nhan = false;

	/**
	 * Biến cờ cho biết có load lại dữ liệu lên hay không
	 */
	boolean loadData = false;
	int positionInitPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quiz);
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.color.blue));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle bd = getIntent().getExtras();
		loadData = bd.getBoolean("load");
		positionInitPager = bd.getInt("positionInit", 0);

		if (loadData) {
			loadQuestion();
			loadCustomView();
		}

		initView();
		loadSlideMenu();
	}

	/**
	 * Load câu hỏi từ sdcard
	 */
	void loadQuestion() {
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/Download/Gra2006.json");
		Toast.makeText(getApplicationContext(), file.getPath(),
				Toast.LENGTH_LONG).show();
		DoQuiz.exam.listQuestion = JsonParse.listQuestion(file);
		DoQuiz.exam.scoreWrong = 0;
		DoQuiz.exam.time = 0;
	}

	/**
	 * Khởi tạo các view
	 */
	void initView() {
		pager = (ViewPager) findViewById(R.id.pager);
		indicator = (TitlePageIndicator) findViewById(R.id.indicator);

		tvSumaryAnswer = (TextView) findViewById(R.id.tvSumaryAnswer);
		tvTime = (TextView) findViewById(R.id.tvTime);
		if (!loadData) {
			tvTime.setVisibility(View.GONE);
		} else {
			tvTime.setVisibility(View.VISIBLE);
		}

		tvSumaryAnswer.setText(DoQuiz.exam.sumaryAnswer + "/"
				+ DoQuiz.exam.listQuestion.size());
		pagerAdapter = new QuizViewPagerAdapter(getSupportFragmentManager(),
				DoQuiz.exam.listQuestion, loadData);
		pager.setAdapter(pagerAdapter);
		pager.setCurrentItem(positionInitPager);
		indicator.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int pos) {
				// TODO Auto-generated method stub
				if(pos==(DoQuiz.exam.listQuestion.size()-1)){
					if(	DoQuiz.exam.sumaryAnswer ==DoQuiz.exam.listQuestion.size()){
						finishQuiz();
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
				tvTime.setText(CommonUtils.getTimeString(time));
				if (time > 0) {
					time -= 1000;
					DoQuiz.exam.time += 1000;

				} else {
				timer.cancel();
				Toast.makeText(getApplicationContext(), "Hết thời gian hiển thị kết quả thi",Toast.LENGTH_SHORT).show();
				DoQuiz.exam.listQuestion.get(lastSelect).setCompare(true);
				timer.cancel();
				Intent itResult = new Intent(QuizActivity.this,
						ResultQuiz.class);
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

		handler = new Handler();
		timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				UpdateTIME();

			}

		}, 1000, 1000);
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
		DialogNotify dialog = new DialogNotify(QuizActivity.this,
				"Kết thúc bài thi", "Bài thi sẽ được kết thúc và chấm điểm",
				"OK", "Bỏ qua") {

			@Override
			public void onOKClick() {
				DoQuiz.exam.listQuestion.get(lastSelect).setCompare(true);
				timer.cancel();
				Intent itResult = new Intent(QuizActivity.this,
						ResultQuiz.class);
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
				finishQuiz();
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
			if(	DoQuiz.exam.sumaryAnswer ==DoQuiz.exam.listQuestion.size()){
				finishQuiz();
			}
			nhan = true;
			super.onReceive(context, intent);
		}

	};

	private static Interpolator interp = new Interpolator() {
		@Override
		public float getInterpolation(float t) {
			t -= 1.0f;
			return t * t * t + 1.0f;
		}
	};
}
