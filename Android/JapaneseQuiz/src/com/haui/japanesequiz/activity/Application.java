package com.haui.japanesequiz.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class Application extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.color.blue));
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

}
