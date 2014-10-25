package com.haui.japanesequiz.activity;

import java.util.ArrayList;
import java.util.List;

import com.haui.japanese.adapter.ResultButtonAdapter;
import com.haui.japanese.model.DoQuiz;
import com.haui.japanese.model.Question;
import com.haui.japanese.util.CommonUtils;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class ResultQuiz extends ActionBarActivity implements OnClickListener {

	TextView tvResultTime, tvResultTitle, tvResultScore;
	Button btnTotal, btnCorrect, btnIncorrect, btnUnAnswer;
	GridView gridViewButton;
	ResultButtonAdapter adapter;
	List<Question> listQuestion;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.color.blue));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.activity_resutl_quiz);
		initView();

	}

	void initView() {
		tvResultTime = (TextView) findViewById(R.id.tvResultTime);
		tvResultTitle = (TextView) findViewById(R.id.tvResultTitle);
		tvResultScore = (TextView) findViewById(R.id.tvResultScore);
		btnTotal = (Button) findViewById(R.id.btnTotal);
		btnTotal.setOnClickListener(this);
		btnCorrect = (Button) findViewById(R.id.btnCorrect);
		btnCorrect.setOnClickListener(this);
		btnIncorrect = (Button) findViewById(R.id.btnIncorrect);
		btnIncorrect.setOnClickListener(this);
		btnUnAnswer = (Button) findViewById(R.id.btnUnAnswer);
		btnUnAnswer.setOnClickListener(this);
		gridViewButton = (GridView) findViewById(R.id.gridViewButton);
		listQuestion = DoQuiz.exam.listQuestion;
		int total = listQuestion.size();
		int unAnswer = 0;
		for (Question q : listQuestion) {
			if (q.getAnswer_choose() == -1) {
				unAnswer++;
			}
		}
		int correct = 0;
		for (Question q : listQuestion) {
			if (q.isAnswerTrue()) {
				correct++;
			}
		}

		int incorrect = total - unAnswer - correct;
		btnTotal.setText(total + "");
		btnCorrect.setText(correct + "");
		btnIncorrect.setText(incorrect + "");
		btnUnAnswer.setText(unAnswer + "");
		tvResultScore.setText(correct + "/" + total);
		tvResultTime.setText(CommonUtils.getTimeString(DoQuiz.exam.time));

		loadGridview(listQuestion);
	}

	void loadGridview(List<Question> list) {
		adapter = new ResultButtonAdapter(list, ResultQuiz.this);
		gridViewButton.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.menu_result_quiz, menu);
		SubMenu submenu = menu.addSubMenu("action");
		submenu.add(0, 10, Menu.NONE, "Trở về");
		submenu.add(0, 15, Menu.NONE, "Xem đáp án");
		submenu.add(0, 20, Menu.NONE, "Làm lại bài");
		MenuItem item = submenu.getItem();
		item.setIcon(R.drawable.ic_action_actions);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menuAction) {

		} else if (item.getItemId() == android.R.id.home) {
			finish();
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnTotal:
			List<Question> listTotal = listQuestion;
			loadGridview(listTotal);

			break;
		case R.id.btnCorrect:
			List<Question> listCorrect = new ArrayList<Question>();
			for (Question q : listQuestion) {
				if (q.isAnswerTrue()) {
					listCorrect.add(q);
				}
			}

			loadGridview(listCorrect);
			break;
		case R.id.btnIncorrect:
			List<Question> listInCorrect = new ArrayList<Question>();
			for (Question q : listQuestion) {
				if (!q.isAnswerTrue() && q.getAnswer_choose() != -1) {
					listInCorrect.add(q);
				}
			}

			loadGridview(listInCorrect);
			break;
		case R.id.btnUnAnswer:
			List<Question> listUnanswer = new ArrayList<Question>();
			for (Question q : listQuestion) {
				if (q.getAnswer_choose() == -1) {
					listUnanswer.add(q);
				}
			}

			loadGridview(listUnanswer);
			break;
		}

	}
}
