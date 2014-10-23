package com.haui.japanesequiz.activity;

import java.io.File;
import java.util.List;

import com.haui.japanese.controller.JsonParse;
import com.haui.japanese.model.Question;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class Test extends ActionBarActivity {

	TextView tvTest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		tvTest = (TextView) findViewById(R.id.tvTest);
		File file=new File(Environment.getExternalStorageDirectory()+"/Download/json.json");
		String st=JsonParse.readJsonString(file);
		List<Question> list=JsonParse.listQuestion(file);
		tvTest.setText(list.toString());
	}

}
