package com.haui.japanesequiz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.haui.japanese.adapter.ExamGridAdapter;
import com.haui.japanese.model.ListQuiz;
import com.haui.japanse.cache.QuizListCache;

public class ListExamActivity extends ActionBarActivity {

	GridView gridViewListExam;
	int type;
	ListQuiz listQuiz;
	QuizListCache quizListCache;
	ExamGridAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.color.blue));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		setContentView(R.layout.activity_list_exam);
		Bundle bd = getIntent().getExtras();
		type = bd.getInt("type");
		quizListCache = new QuizListCache(getApplicationContext());
		listQuiz = quizListCache.getListQuiz();
		gridViewListExam = (GridView) findViewById(R.id.gridViewListExam);
		adapter = new ExamGridAdapter(listQuiz.getListQuiz(), type,
				getApplicationContext());
		gridViewListExam.setAdapter(adapter);
		gridViewListExam.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				if(adapter.getItem(position)==null){
					if(type==3){
						Intent itListen=new Intent(ListExamActivity.this, QuizListenActivity.class);
						itListen.putExtra("year",listQuiz.getListQuiz().get(position) );
						itListen.putExtra("type", type);
						startActivity(itListen);
					}else{
						Intent itText=new Intent(ListExamActivity.this, QuizActivity.class);
						itText.putExtra("year",listQuiz.getListQuiz().get(position) );
						itText.putExtra("type", type);
						startActivity(itText);
					}
				}
				
				String xx=listQuiz.getListQuiz().get(position)+"-"+type;
				Toast.makeText(getApplicationContext(), "id "+adapter.getItem(position), Toast.LENGTH_SHORT).show();

			}
		});
	}

}
