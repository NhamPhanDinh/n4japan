package com.haui.japanese.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.haui.japanese.model.Question;
import com.haui.japanesequiz.activity.FragmentQuiz;
import com.haui.japanesequiz.activity.FragmentQuizListen;

public class QuizViewListenAdapter extends FragmentPagerAdapter {


	List<Question> listQuestion;
	boolean loadData;

	public QuizViewListenAdapter(FragmentManager fm, List<Question> listQuestion,boolean loadData) {
		super(fm);
		this.listQuestion = listQuestion;
		this.loadData=loadData;
	}

	@Override
	public Fragment getItem(int pos) {
		// TODO Auto-generated method stub
		return FragmentQuizListen.newInstance(pos,loadData);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listQuestion.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return "Câu " + (listQuestion.get(position).getId()+1);
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		// TODO Auto-generated method stub

	}



}
