package com.haui.japanese.adapter;

import java.util.List;

import com.haui.japanese.model.Question;
import com.haui.japanese.model.DoQuiz;
import com.haui.japanesequiz.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class GridViewAdapter extends BaseAdapter {

	Context mContext;
	LayoutInflater inflater;

	public GridViewAdapter(Context mContext) {
		super();
		this.mContext = mContext;
		inflater = (LayoutInflater) mContext
				.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return DoQuiz.exam.listQuestion.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Question question = DoQuiz.exam.listQuestion.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.menu_item_slide, null);
		}
		TextView btnGrid = (TextView) convertView.findViewById(R.id.tvGrid);
		
		if (question.getAnswer_choose() != -1) {
			btnGrid.setBackgroundResource(R.color.yellow_menu);
			if (question.isCompare()) {
				if (question.getAnswer_choose() == question.getTrue_answer()) {
					btnGrid.setBackgroundResource(R.color.green);
				} else {
					btnGrid.setBackgroundResource(R.color.red);
				}
			}

		}else{
			btnGrid.setBackgroundResource(R.color.blue_nhat);
		}
		btnGrid.setText((position + 1) + "");
		return convertView;
	}

}
