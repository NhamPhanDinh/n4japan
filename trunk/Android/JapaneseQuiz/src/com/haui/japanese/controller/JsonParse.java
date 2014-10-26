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

import com.google.gson.JsonObject;
import com.haui.japanese.model.ListQuiz;
import com.haui.japanese.model.Question;
import com.haui.japanese.util.FileUntils;
import com.haui.japanese.util.Variable;

public class JsonParse {

	public static String getVersion(File file) {
		String jsonString = FileUntils.readFileText(file);
		try {
			JSONObject obj = new JSONObject(jsonString);
			String version = obj.getString("version");
			FileUntils.deleteFile(file);
			return version;
		} catch (JSONException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

	}

	public static String getPass(File file) {
		String pass = FileUntils.readFileText(file);
		FileUntils.deleteFile(file);
		return pass;
	}

	public static ListQuiz getListQuiz(File file) {
		String jsonString = FileUntils.readFileText(file);
		ListQuiz listQuiz = new ListQuiz();
		List<Integer> listYear = new ArrayList<Integer>();
		try {
			JSONArray jsonArr = new JSONArray(jsonString);
			for (int i = 0; i < jsonArr.length(); i++) {
				JSONObject obj = jsonArr.getJSONObject(i);
				int year = obj.getInt("list");
				listYear.add(year);
			}
			listQuiz.setListQuiz(listYear);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listQuiz;
	}

	public static List<Question> listQuestion(File file, int year, int type) {
		String link = null;
		String title = null;
		if (type == 1) {
			title = "Voc";
			link = Variable.HOST_DATA +year+ "/Vocabulary/";
		} else if (type == 2) {
			title = "Gra";
			link = Variable.HOST_DATA + year+"/Grammar/";
		}else if(type==3){
			link = Variable.HOST_DATA + year+"/Listerning/";
		}

		String jsonString = FileUntils.readFileText(file);
		List<Question> listQuestion = new ArrayList<Question>();
		try {
			JSONArray jsonArray = new JSONArray(jsonString);
			for (int i = 0; i < jsonArray.length(); i++) {
				Question qt = new Question();
				JSONObject obj = jsonArray.getJSONObject(i);
				qt.setId(i);
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
				String linkImg=obj.getString("image");
				if(!linkImg.equals("null")){
					linkImg=link+linkImg+".png";
				}
				qt.setImage(linkImg);
				listQuestion.add(qt);
			}

		} catch (JSONException e) {
			listQuestion = null;
			e.printStackTrace();
			Log.e("json", e.toString());
		}
		FileUntils.deleteFile(file);
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
