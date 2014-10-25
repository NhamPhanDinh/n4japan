package com.haui.japanesequiz.activity;

import java.io.File;
import java.util.List;

import com.haui.japanese.controller.JsonParse;
import com.haui.japanese.model.Question;
import com.haui.japanese.util.FileUntils;

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
		File file = new File(Environment.getExternalStorageDirectory()
				+ "/Download/Voc2006.zip");
		File folder=new File(Environment.getExternalStorageDirectory()+"/Download");
		FileUntils.ExtractFile(file, folder, "Voc2006.zip");
		tvTest.setText(JsonParse.getListQuiz(file).toString());
		// List<Question> list = JsonParse.listQuestion(file);
	}

}
