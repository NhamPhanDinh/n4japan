package com.haui.japanese.model;

import java.io.Serializable;
import java.util.List;

/**
 * Đối tượng dùng để lưu trữ thông tin của một bài thi
 * 
 * @author EO
 * 
 */
public class Exam implements Serializable {
	public String id = null;
	public List<Question> listQuestion = null;
	public int scoreWrong = 0;
	public int scoreRight = 0;
	public long time = 0;
	public int sumaryAnswer = 0;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Question> getListQuestion() {
		return listQuestion;
	}

	public void setListQuestion(List<Question> listQuestion) {
		this.listQuestion = listQuestion;
	}

	public int getScoreWrong() {
		return scoreWrong;
	}

	public void setScoreWrong(int scoreWrong) {
		this.scoreWrong = scoreWrong;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getSumaryAnswer() {
		return sumaryAnswer;
	}

	public void setSumaryAnswer(int sumaryAnswer) {
		this.sumaryAnswer = sumaryAnswer;
	}

	public int getScoreRight() {
		return scoreRight;
	}

	public void setScoreRight(int scoreRight) {
		this.scoreRight = scoreRight;
	}

	@Override
	public String toString() {
		return "Exam [id=" + id + ", listQuestion=" + listQuestion
				+ ", scoreWrong=" + scoreWrong + ", time=" + time
				+ ", sumaryAnswer=" + sumaryAnswer + "]";
	}

}
