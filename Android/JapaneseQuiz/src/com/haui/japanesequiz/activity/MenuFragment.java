package com.haui.japanesequiz.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.haui.japanese.adapter.GridViewAdapter;
import com.haui.japanese.broadcast.ChooseAnswerBroadCast;

public class MenuFragment extends Fragment {

	GridViewAdapter adapter;
	Button btnFinishQuizMenu;

	public MenuFragment() {

	}

	GridView mGridView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.menu_slide, container, false);
		IntentFilter filter = new IntentFilter("com.haui.japanese.ANSWER_CLICK");
		getActivity().registerReceiver(broadCast, filter);

		mGridView = (GridView) v.findViewById(R.id.gridViewMenu);
		btnFinishQuizMenu=(Button) v.findViewById(R.id.btnFinishQuizMenu);
		btnFinishQuizMenu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//gửi thông điệp kết thúc quiz
				Intent broadcast = new Intent("com.haui.japanese.MENU_CLICK");
				broadcast.putExtra("position", -1);
				getActivity().sendBroadcast(broadcast);
				
			}
		});
		loadList();
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// Gửi thông báo vị trị click sang QuizActivity để viewpager
				// chuyển đến câu hỏi tương ứng
				Intent broadcast = new Intent("com.haui.japanese.MENU_CLICK");
				broadcast.putExtra("position", position);
				getActivity().sendBroadcast(broadcast);

			}
		});
		return v;
	}

	/**
	 * Tải lại danh sách hiển thị gridview
	 */
	void loadList() {
		adapter = new GridViewAdapter(getActivity());
		mGridView.setAdapter(adapter);
	}

	/**
	 * Broadcast nhận thông báo cập nhật hiển thị gridview từ các acitiviy và
	 * fragment khác
	 */
	ChooseAnswerBroadCast broadCast = new ChooseAnswerBroadCast() {

		@Override
		public void onReceive(Context context, Intent intent) {

			loadList();
			super.onReceive(context, intent);
		}

	};

}
