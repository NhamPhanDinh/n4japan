package com.haui.japanese.adapter;

import java.util.List;

import com.haui.japanesequiz.activity.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class MainGridViewAdapter extends BaseAdapter {

	List<String> listTitle;
	List<Integer> listBackgroud;
	List<Drawable> listIcon;
	Context mContext;
	LayoutInflater inflater;

	public MainGridViewAdapter(List<String> listTitle,
			List<Integer> listBackgroud, List<Drawable> listIcon,
			Context mContext) {
		super();
		this.listTitle = listTitle;
		this.listBackgroud = listBackgroud;
		this.listIcon = listIcon;
		this.mContext = mContext;
		inflater = (LayoutInflater) mContext
				.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listTitle.size();
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

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.button_main, null);

		}

		Button btn = (Button) convertView.findViewById(R.id.btnMain);
		btn.setBackgroundResource(listBackgroud.get(position));
		btn.setCompoundDrawablesWithIntrinsicBounds(null,
				listIcon.get(position), null, null);
		btn.setText(listTitle.get(position));
		btn.setTextColor(Color.WHITE);
		btn.setWidth(60);

		return convertView;
	}

}
