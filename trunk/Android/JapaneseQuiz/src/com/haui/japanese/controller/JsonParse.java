package com.haui.japanese.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.haui.japanese.model.Question;

public class JsonParse {

	public static String readJsonString(File file) {
		StringBuilder jsonString = new StringBuilder();

		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;

			while ((line = br.readLine()) != null) {
				jsonString.append(line);
				jsonString.append('\n');
			}
			br.close();
		} catch (IOException e) {
			Log.e("file", e.toString());
		}

		return jsonString.toString();
	}

	public static List<Question> listQuestion(File file) {
		String jsonString = readJsonString(file);
		List<Question> listQuestion = new ArrayList<Question>();
		try {
			JSONArray jsonArray = new JSONArray(jsonString);
			for (int i = 0; i < jsonArray.length(); i++) {
				Question qt = new Question();
				JSONObject obj = jsonArray.getJSONObject(i);
				qt.setYear(obj.getString("year"));
				qt.setGroup_id(obj.getInt("group_id"));
				qt.setGroup_name(obj.getString("group_name"));
				qt.setPharagraph(obj.getString("pharagraph"));
				qt.setQuestion(obj.getString("question"));
				qt.setAnswer_type(obj.getInt("answer_type"));
				if (qt.getAnswer_type() == 1) {
					qt = chuanHoaQuestion(obj.getString("answer_1"), qt);
				} else {
					qt.setAnswer_1(obj.getString("answer_1"));
					qt.setAnswer_2(obj.getString("answer_2"));
					qt.setAnswer_3(obj.getString("answer_3"));
					qt.setAnswer_4(obj.getString("answer_4"));
				}

				qt.setTrue_answer(obj.getInt("true_answer"));
				qt.setImage(obj.getString("image"));
				qt.setQuestion("question");
				listQuestion.add(qt);
			}

		} catch (JSONException e) {
			listQuestion = null;
			e.printStackTrace();
			Log.e("json", e.toString());
		}

		return listQuestion;
	}

	public static Question chuanHoaQuestion(String st, Question qt) {
		int index2 = st.indexOf("２");
		qt.setAnswer_1(st.substring(0, index2));
		// System.out.println(st.substring(0, index2));
		st = st.substring(index2);
		int index3 = st.indexOf("３");
		qt.setAnswer_2(st.substring(0, index3));
		// System.out.println(st.substring(0, index3));
		st = st.substring(index3);

		int index4 = st.indexOf("４");
		qt.setAnswer_3(st.substring(0, index4));
		// System.out.println(st.substring(0, index4));
		st = st.substring(index4);
		qt.setAnswer_4(st);
		return qt;
	}
}
