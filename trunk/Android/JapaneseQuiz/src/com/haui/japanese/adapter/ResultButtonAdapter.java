package com.haui.japanese.adapter;

import java.util.List;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.haui.japanese.model.Question;
import com.haui.japanesequiz.activity.MainActivity;
import com.haui.japanesequiz.activity.QuizActivity;
import com.haui.japanesequiz.activity.R;

public class ResultButtonAdapter extends BaseAdapter {

	List<Question> list;
	ActionBarActivity mContext;
	LayoutInflater inflater;
	int year;
	int type;

	public ResultButtonAdapter(List<Question> list, ActionBarActivity mContext,int year,int type) {
		super();
		this.list = list;
		this.mContext = mContext;
		this.year=year;
		this.type=type;
		inflater=(LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Question qt = list.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.button_result, null);
		}
		
		Button btnResult=(Button) convertView.findViewById(R.id.btnResult);
		btnResult.setText("Câu "+(list.get(position).getId()+1));
	//	btnResult.setCompoundDrawablesWithIntrinsicBounds(null, null, null, mContext.getResources().getDrawable(R.drawable.exclamation_mark));
		if(qt.getAnswer_choose()==-1){
			btnResult.setCompoundDrawablesWithIntrinsicBounds(null, null, null, mContext.getResources().getDrawable(R.drawable.exclamation_mark) );
		}else{
			if(qt.isAnswerTrue()){
				btnResult.setCompoundDrawablesWithIntrinsicBounds(null, null, null, mContext.getResources().getDrawable(R.drawable.right_mark) );
			}else{
				btnResult.setCompoundDrawablesWithIntrinsicBounds(null, null, null, mContext.getResources().getDrawable(R.drawable.wrong_mark) );
			}
		}
		
		btnResult.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent it = new Intent(mContext, QuizActivity.class);
				it.putExtra("load", false);
				it.putExtra("positionInit", qt.getId());
				it.putExtra("year", year);
				it.putExtra("type", type);
				mContext.startActivity(it);
				
			}
		});

		return convertView;
	}

}
