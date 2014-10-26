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
import android.webkit.WebView.FindListener;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.haui.japanese.broadcast.ChooseAnswerBroadCast;
import com.haui.japanese.model.Question;
import com.haui.japanese.model.DoQuiz;
import com.haui.japanese.util.CommonUtils;
import com.haui.japanese.view.ResizableImageView;
import com.haui.japanesequiz.activity.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class FragmentQuiz extends Fragment {

	Question question;
	TextView tvQuestion, tvPharagrahp, tvGroup;
	ResizableImageView imgQuestion;
	RadioGroup dapan;
	RadioButton dapan1, dapan2, dapan3, dapan4;
	ImageLoader loader;
	DisplayImageOptions option;
	int position;
	boolean isCheckAnswer = false;
	boolean loadData;
	ProgressBar progressImage;

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_question, container, false);
		initView(v);
		/*
		 * Toast.makeText(getActivity(), "on createview" + isCheckAnswer +
		 * " "+position, Toast.LENGTH_SHORT).show();
		 */
		if (!loadData) {

			showAnswer();

		} else {
			if (isCheckAnswer) {
				showAnswer();
			} else {
				dapan1.setClickable(true);
				dapan2.setClickable(true);
				dapan3.setClickable(true);
				dapan4.setClickable(true);
			}
		}

		return v;
	}

	void showAnswer() {
		dapan1.setClickable(false);
		dapan2.setClickable(false);
		dapan3.setClickable(false);
		dapan4.setClickable(false);

		// Tô đỏ lựa chọn
		int luaChon = DoQuiz.exam.listQuestion.get(position).getAnswer_choose();
		switch (luaChon) {

		case 1:
			dapan1.setTextColor(Color.RED);
			dapan1.setChecked(true);
			break;
		case 2:
			dapan2.setTextColor(Color.RED);
			dapan2.setChecked(true);
			break;
		case 3:
			dapan3.setTextColor(Color.RED);
			dapan3.setChecked(true);
			break;
		case 4:
			dapan4.setTextColor(Color.RED);
			dapan4.setChecked(true);
			break;
		default:

			break;
		}

		// tô xanh đáp án
		int traloi = DoQuiz.exam.listQuestion.get(position).getTrue_answer();
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

	public static FragmentQuiz newInstance(int position, boolean loadData) {
		FragmentQuiz fragment = new FragmentQuiz();
		fragment.setQuestion(DoQuiz.exam.listQuestion.get(position));
		fragment.setPosition(position);
		fragment.setLoadData(loadData);
		return fragment;

	}

	void initView(View v) {

		IntentFilter filter = new IntentFilter("com.haui.japanese.ANSWER_CLICK");
		getActivity().registerReceiver(broadCast, filter);

		progressImage = (ProgressBar) v.findViewById(R.id.progressImage);
		progressImage.setVisibility(View.VISIBLE);

		tvQuestion = (TextView) v.findViewById(R.id.tvQuestion);
		imgQuestion = (ResizableImageView) v.findViewById(R.id.imgQuestion);
		tvPharagrahp = (TextView) v.findViewById(R.id.tvPharagrahp);
		tvGroup = (TextView) v.findViewById(R.id.tvGroup);

		dapan = (RadioGroup) v.findViewById(R.id.dapan);
		dapan1 = (RadioButton) v.findViewById(R.id.dapan1);
		dapan2 = (RadioButton) v.findViewById(R.id.dapan2);
		dapan3 = (RadioButton) v.findViewById(R.id.dapan3);
		dapan4 = (RadioButton) v.findViewById(R.id.dapan4);

		loader = com.nostra13.universalimageloader.core.ImageLoader
				.getInstance();
		loader.init(ImageLoaderConfiguration.createDefault(getActivity()));

		option = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error)
				.resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.bitmapConfig(Bitmap.Config.RGB_565).considerExifParams(true)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		//
		if (question.getPharagraph().equals("null")) {
			tvPharagrahp.setVisibility(View.GONE);
		} else {
			tvPharagrahp.setVisibility(View.VISIBLE);
			tvPharagrahp.setText(question.getPharagraph());
		}

		if (question.getGroup_name().equals("null")) {
			tvGroup.setVisibility(View.GONE);
		} else {
			tvGroup.setVisibility(View.VISIBLE);
			tvGroup.setText(question.getGroup_name());
		}

		if (question.getQuestion().equals("null")) {
			tvQuestion.setVisibility(View.GONE);
		} else {
			tvQuestion.setVisibility(View.VISIBLE);
			tvQuestion.setText(question.getQuestion());
		}

		
		if (question.getImage().equals("null")) {
			imgQuestion.setVisibility(View.GONE);
			progressImage.setVisibility(View.GONE);

		} else {
			imgQuestion.setVisibility(View.VISIBLE);
			loader.displayImage(question.getImage(), imgQuestion,
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {
							progressImage.setVisibility(View.VISIBLE);

						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							progressImage.setVisibility(View.GONE);
							

						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							progressImage.setVisibility(View.GONE);

						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
							// TODO Auto-generated method stub

						}
					});
		}

		dapan1.setText(question.getAnswer_1());
		dapan2.setText(question.getAnswer_2());
		dapan3.setText(question.getAnswer_3());
		dapan4.setText(question.getAnswer_4());

		dapan.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// tăng biến số câu đã làm

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

				// Gửi thông báo cho MenuFragment để cập nhật lại các ô màu câu
				// hỏi
				Intent intent = new Intent("com.haui.japanese.ANSWER_CLICK");
				intent.putExtra("fragment", 0);
				DoQuiz.exam.listQuestion.set(position, question);
				getActivity().sendBroadcast(intent);

				// Kiểm tra nếu câu hỏi tại vị trí position nếu chưa được so
				// sánh đáp án thì gửi thông báo
				// gồm vị trí câu hỏi đến QuizActivity để tiến hành cập nhật
				if (!DoQuiz.exam.listQuestion.get(position).isCompare()) {
					Intent intent2 = new Intent("com.haui.japanese.PAGER_CLICK");
					intent2.putExtra("fragment", 0);
					intent2.putExtra("position", position);
					getActivity().sendBroadcast(intent2);
				}

			}
		});
	}

	/**
	 * Broadcast nhận sự kiện từ bên QuizActivity thông báo cho việc cập nhật
	 * giao diện câu hỏi (câu đã làm thì k được làm lại nữa)
	 */
	ChooseAnswerBroadCast broadCast = new ChooseAnswerBroadCast() {

		@Override
		public void onReceive(Context context, Intent intent) {

			// kiểm tra nếu chưa kiểm tra đáp án và tại câu hỏi vị trí trùng với
			// câu hỏi của fragment này thì tiến hành không cho click radio và
			// hiển thị ra đáp án
			if (!isCheckAnswer
					&& intent.getExtras().getInt("position") == position) {

				int fragment = intent.getExtras().getInt("fragment");
				if (fragment == 1) {
					if (DoQuiz.exam.listQuestion.get(position)
							.getAnswer_choose() != -1) {

						if (!question.isAnswerTrue()) {
							DoQuiz.exam.scoreWrong++;
							QuizActivity.tvScore.setText(DoQuiz.exam.scoreWrong
									+ "");
						}

						// vô hiệu hóa click các radio
						dapan1.setClickable(false);
						dapan2.setClickable(false);
						dapan3.setClickable(false);
						dapan4.setClickable(false);
						int traloi = DoQuiz.exam.listQuestion.get(position)
								.getTrue_answer();

						// kiểm tra đáp án để bôi xanh đáp án
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
	}

	public boolean isLoadData() {
		return loadData;
	}

	public void setLoadData(boolean loadData) {
		this.loadData = loadData;
	}

}
