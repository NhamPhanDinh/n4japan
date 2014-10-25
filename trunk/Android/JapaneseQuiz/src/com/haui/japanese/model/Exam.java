package com.haui.japanese.model;

import java.util.List;

public class Exam {
	public static String id = null;
	public static List<Question> listQuestion = null;
	public static int scoreWrong = 0;
	public static long time = 0;
	public static int sumaryAnswer = 0;

	public static String getId() {
		return id;
	}

	public static void setId(String id) {
		Exam.id = id;
	}

	public static List<Question> getListQuestion() {
		return listQuestion;
	}

	public static void setListQuestion(List<Question> listQuestion) {
		Exam.listQuestion = listQuestion;
	}

	public static int getScoreWrong() {
		return scoreWrong;
	}

	public static void setScoreWrong(int scoreWrong) {
		Exam.scoreWrong = scoreWrong;
	}

	public static long getTime() {
		return time;
	}

	public static void setTime(long time) {
		Exam.time = time;
	}

	public static int getSumaryAnswer() {
		return sumaryAnswer;
	}

	public static void setSumaryAnswer(int sumaryAnswer) {
		Exam.sumaryAnswer = sumaryAnswer;
	}

}
