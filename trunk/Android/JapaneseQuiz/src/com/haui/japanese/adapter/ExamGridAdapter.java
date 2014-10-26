package com.haui.japanese.adapter;

import java.util.List;

import com.haui.japanese.model.Exam;
import com.haui.japanese.model.Question;
import com.haui.japanese.sqlite.DBCache;
import com.haui.japanesequiz.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ExamGridAdapter extends BaseAdapter {

	List<Integer> listQuiz;
	Context mContext;
	LayoutInflater inflater;
	DBCache db;
	int type;

	public ExamGridAdapter(List<Integer> listQuiz, int type, Context mContext) {
		super();
		this.listQuiz = listQuiz;
		this.mContext = mContext;
		db = new DBCache(mContext);
		this.type = type;
		inflater = (LayoutInflater) mContext
				.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return listQuiz.size();
	}

	@Override
	public Object getItem(int position) {

		return db.getExam(listQuiz.get(position) + "-" + type);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int year = listQuiz.get(position);
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_exam, null);
		}

		TextView tvYearExam = (TextView) convertView
				.findViewById(R.id.tvYearExam);
		TextView tvRightExam = (TextView) convertView
				.findViewById(R.id.tvRightExam);
		TextView tvWrongExam = (TextView) convertView
				.findViewById(R.id.tvWrongExam);
		tvYearExam.setText(year + "");
		String id = year + "-" + type;
		Exam exam = db.getExam(id);

		if (exam == null) {
			tvRightExam.setVisibility(View.INVISIBLE);
			tvWrongExam.setVisibility(View.INVISIBLE);
			convertView.setBackgroundResource(R.drawable.exam_undone);
		} else {
			convertView.setBackgroundResource(R.drawable.exam_done);
			List<Question> listQuestion = exam.getListQuestion();
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

			tvRightExam.setVisibility(View.VISIBLE);
			tvWrongExam.setVisibility(View.VISIBLE);
			tvRightExam.setText(correct + "");
			tvWrongExam.setText(incorrect + "");
		}
		return convertView;
	}
}
