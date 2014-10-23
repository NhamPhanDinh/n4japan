package com.haui.japanesequiz.activity;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.haui.japanesequiz.activity.R;
import com.haui.japanese.adapter.GridViewAdapter;
import com.haui.japanese.broadcast.ChooseAnswerBroadCast;
import com.haui.japanese.broadcast.MenuClickBroadCast;
import com.haui.japanese.model.Question;

public class MenuFragment extends Fragment {

	GridViewAdapter adapter;

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
		loadList();
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			/*	Toast.makeText(getActivity(), position + "", Toast.LENGTH_SHORT)
						.show();*/
				Intent broadcast = new Intent("com.haui.japanese.MENU_CLICK");
				broadcast.putExtra("position", position);
				getActivity().sendBroadcast(broadcast);

			}
		});
		return v;
	}

	void loadList() {
		adapter = new GridViewAdapter(getActivity());
		mGridView.setAdapter(adapter);
	}

	ChooseAnswerBroadCast broadCast = new ChooseAnswerBroadCast() {

		@Override
		public void onReceive(Context context, Intent intent) {
		//	Bundle bd = intent.getExtras();
			//int position = bd.getInt("position");
		//	Question question = (Question) bd.getSerializable("question");
			//list.set(position, question);
		//	Toast.makeText(getActivity(), "reload menu slide", Toast.LENGTH_SHORT).show();
			loadList();
			super.onReceive(context, intent);
		}

	};

}
