package com.haui.japanesequiz.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.haui.japanese.adapter.ResultButtonAdapter;
import com.haui.japanese.model.DoQuiz;
import com.haui.japanese.model.Question;
import com.haui.japanese.util.CommonUtils;

public class ResultQuiz extends Application implements OnClickListener {

	TextView tvResultTime, tvResultTitle, tvResultScore;
	Button btnTotal, btnCorrect, btnIncorrect, btnUnAnswer;
	GridView gridViewButton;
	ResultButtonAdapter adapter;
	List<Question> listQuestion;
	int year;
	int type;
	AlertDialog.Builder dialogMenu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_resutl_quiz);
		Bundle bd = getIntent().getExtras();

		year = bd.getInt("year");
		type = bd.getInt("type");

		switch (type) {
		case 1:
			getSupportActionBar().setTitle(
					Html.fromHtml("<b><font color='#ffffff'>KQ Vocabulary "
							+ year + "  </font></b>"));
			break;
		case 2:
			getSupportActionBar().setTitle(
					Html.fromHtml("<b><font color='#ffffff'>KQ Grammar " + year
							+ "  </font></b>"));
			break;
		case 3:
			getSupportActionBar().setTitle(
					Html.fromHtml("<b><font color='#ffffff'>KQ Listening "
							+ year + "  </font></b>"));
			break;
		}

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
		boolean pass;
		int phanTram = (correct * 100) / total;
		if (phanTram >= 60) {
			pass = true;
		} else {
			pass = false;
		}

		btnTotal.setText(total + "");
		btnCorrect.setText(correct + "");
		btnIncorrect.setText(incorrect + "");
		btnUnAnswer.setText(unAnswer + "");
		tvResultScore.setText(correct + "/" + total);
		tvResultTime.setText(CommonUtils.getTimeString(DoQuiz.exam.time));

		if (pass) {
			tvResultTitle.setBackgroundResource(R.drawable.pass);
			tvResultScore.setTextColor(Color.GREEN);
		} else {
			tvResultTitle.setBackgroundResource(R.drawable.fail);
			tvResultScore.setTextColor(Color.RED);
		}

		loadGridview(listQuestion);
	}

	void loadGridview(List<Question> list) {
		adapter = new ResultButtonAdapter(list, ResultQuiz.this, year, type);
		gridViewButton.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_result_quiz, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menuAction) {

			dialogMenu = new AlertDialog.Builder(ResultQuiz.this);
			dialogMenu.setItems(
					getResources().getStringArray(R.array.menuResult),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case 0:
								finish();
								break;
							case 1:
								Intent it = new Intent(ResultQuiz.this,
										QuizActivity.class);
								it.putExtra("load", false);
								it.putExtra("positionInit", 0);
								it.putExtra("year", year);
								it.putExtra("type", type);
								startActivity(it);
								break;
							case 2:

								if (type == 3) {
									Intent itListen = new Intent(
											ResultQuiz.this,
											QuizListenActivity.class);
									itListen.putExtra("year", year);
									itListen.putExtra("type", type);
									itListen.putExtra("load", true);
									startActivity(itListen);
								} else {
									Intent itText = new Intent(ResultQuiz.this,
											QuizActivity.class);
									itText.putExtra("year", year);
									itText.putExtra("type", type);
									itText.putExtra("load", true);
									startActivity(itText);
								}
								finish();
								break;
							}

						}
					});
			dialogMenu.show();
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
