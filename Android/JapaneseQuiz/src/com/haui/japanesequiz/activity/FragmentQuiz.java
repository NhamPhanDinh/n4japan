package com.haui.japanesequiz.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.haui.japanese.broadcast.ChooseAnswerBroadCast;
import com.haui.japanese.model.Question;
import com.haui.japanese.model.QuestionList;
import com.haui.japanese.view.ResizableImageView;
import com.haui.japanesequiz.activity.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class FragmentQuiz extends Fragment {

	Question question;
	TextView tvQuestion, tvPharagrahp;
	ResizableImageView imgQuestion;
	RadioGroup dapan;
	RadioButton dapan1, dapan2, dapan3, dapan4;
	ImageLoader loader;
	DisplayImageOptions option;
	int position;
	boolean isCheckAnswer = false;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_question, container, false);
		initView(v);
		/*Toast.makeText(getActivity(), "on createview" + isCheckAnswer + " "+position,
				Toast.LENGTH_SHORT).show();*/
		if (isCheckAnswer) {
			dapan1.setClickable(false);
			dapan2.setClickable(false);
			dapan3.setClickable(false);
			dapan4.setClickable(false);
			int traloi = QuestionList.listQuestion.get(position)
					.getTrue_answer();
			switch (traloi) {

			case 1:
				dapan1.setTextColor(Color.BLUE);
				break;
			case 2:
				dapan2.setTextColor(Color.BLUE);
				break;
			case 3:
				dapan3.setTextColor(Color.BLUE);
				break;
			case 4:
				dapan4.setTextColor(Color.BLUE);
				break;
			}
		}else{
			dapan1.setClickable(true);
			dapan2.setClickable(true);
			dapan3.setClickable(true);
			dapan4.setClickable(true);
		}

		return v;
	}

	public static FragmentQuiz newInstance(int position) {
		FragmentQuiz fragment = new FragmentQuiz();
		fragment.setQuestion(QuestionList.listQuestion.get(position));
		fragment.setPosition(position);
		return fragment;

	}

	void initView(View v) {

		IntentFilter filter = new IntentFilter("com.haui.japanese.ANSWER_CLICK");
		getActivity().registerReceiver(broadCast, filter);

		tvQuestion = (TextView) v.findViewById(R.id.tvQuestion);
		imgQuestion = (ResizableImageView) v.findViewById(R.id.imgQuestion);
		tvPharagrahp = (TextView) v.findViewById(R.id.tvPharagrahp);
		dapan = (RadioGroup) v.findViewById(R.id.dapan);
		dapan1 = (RadioButton) v.findViewById(R.id.dapan1);
		dapan2 = (RadioButton) v.findViewById(R.id.dapan2);
		dapan3 = (RadioButton) v.findViewById(R.id.dapan3);
		dapan4 = (RadioButton) v.findViewById(R.id.dapan4);

		loader = com.nostra13.universalimageloader.core.ImageLoader
				.getInstance();
		loader.init(ImageLoaderConfiguration.createDefault(getActivity()));

		option = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_launcher)
				.showImageOnFail(R.drawable.ic_launcher)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		//
		if (question.getPharagraph().trim().equals("")) {
			tvPharagrahp.setVisibility(View.GONE);
		} else {
			tvPharagrahp.setText(question.getPharagraph());
		}

		tvQuestion.setText(question.getQuestion());
		if (question.getImage() != null || !question.getImage().equals("null")) {
			// loader.displayImage(question.getImage(), imgQuestion);
		} else {
			imgQuestion.setVisibility(View.GONE);
		}

		dapan1.setText(question.getAnswer_1());
		dapan2.setText(question.getAnswer_2());
		dapan3.setText(question.getAnswer_3());
		dapan4.setText(question.getAnswer_4());

		dapan.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId) {
				case R.id.dapan1:
					question.setAnswer_choose(1);
					break;
				case R.id.dapan2:
					question.setAnswer_choose(2);
					break;
				case R.id.dapan3:
					question.setAnswer_choose(3);
					break;
				case R.id.dapan4:
					question.setAnswer_choose(4);
					break;
				}
				Intent intent = new Intent("com.haui.japanese.ANSWER_CLICK");
				intent.putExtra("fragment", 0);
				QuestionList.listQuestion.set(position, question);
				getActivity().sendBroadcast(intent);

				if (!QuestionList.listQuestion.get(position).isCompare()) {
					Intent intent2 = new Intent("com.haui.japanese.PAGER_CLICK");
					intent2.putExtra("fragment", 0);
					intent2.putExtra("position", position);
					getActivity().sendBroadcast(intent2);
				}

			}
		});
	}

	ChooseAnswerBroadCast broadCast = new ChooseAnswerBroadCast() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (!isCheckAnswer && intent.getExtras().getInt("position")==position) {

				int fragment = intent.getExtras().getInt("fragment");
				if (fragment == 1) {
					/*Toast.makeText(getActivity(), "check answer "+position,
							Toast.LENGTH_SHORT).show();*/
					if (QuestionList.listQuestion.get(position)
							.getAnswer_choose() != -1) {
						dapan1.setClickable(false);
						dapan2.setClickable(false);
						dapan3.setClickable(false);
						dapan4.setClickable(false);
						int traloi = QuestionList.listQuestion.get(position)
								.getTrue_answer();
						switch (traloi) {

						case 1:
							dapan1.setTextColor(Color.BLUE);
							break;
						case 2:
							dapan2.setTextColor(Color.BLUE);
							break;
						case 3:
							dapan3.setTextColor(Color.BLUE);
							break;
						case 4:
							dapan4.setTextColor(Color.BLUE);
							break;
						}
					}
					isCheckAnswer = true;
				}

			}

			super.onReceive(context, intent);
		}

	};

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		/*Toast.makeText(getActivity(), "onsavestate", Toast.LENGTH_SHORT).show();*/
	}

}
