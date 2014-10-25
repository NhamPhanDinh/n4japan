package com.haui.japanese.model;

import java.io.Serializable;

public class Question implements Serializable {

	int id;
	String year;
	int group_id;
	String group_name;
	String pharagraph;
	String question;
	int answer_type;
	String answer_1;
	String answer_2;
	String answer_3;
	String answer_4;
	int true_answer;
	String image;
	int answer_choose;
	boolean isAnswerTrue;
	boolean isCompare;

	public Question() {
		super();
		answer_choose = -1;
		isAnswerTrue = false;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public int getGroup_id() {
		return group_id;
	}

	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getPharagraph() {
		return pharagraph;
	}

	public void setPharagraph(String pharagraph) {
		this.pharagraph = pharagraph;
	}

	public int getAnswer_type() {
		return answer_type;
	}

	public void setAnswer_type(int answer_type) {
		this.answer_type = answer_type;
	}

	public String getAnswer_1() {
		return answer_1;
	}

	public void setAnswer_1(String answer_1) {
		this.answer_1 = answer_1;
	}

	public String getAnswer_2() {
		return answer_2;
	}

	public void setAnswer_2(String answer_2) {
		this.answer_2 = answer_2;
	}

	public String getAnswer_3() {
		return answer_3;
	}

	public void setAnswer_3(String answer_3) {
		this.answer_3 = answer_3;
	}

	public String getAnswer_4() {
		return answer_4;
	}

	public void setAnswer_4(String answer_4) {
		this.answer_4 = answer_4;
	}

	public int getTrue_answer() {
		return true_answer;
	}

	public void setTrue_answer(int true_answer) {
		this.true_answer = true_answer;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getAnswer_choose() {
		return answer_choose;
	}

	public void setAnswer_choose(int answer_choose) {
		this.answer_choose = answer_choose;
	}

	public boolean isAnswerTrue() {
		return (this.true_answer == this.answer_choose) ? true : false;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	@Override
	public String toString() {
		return "Question [year=" + year + ", group_id=" + group_id
				+ ", group_name=" + group_name + ", pharagraph=" + pharagraph
				+ ", question=" + question + ", answer_type=" + answer_type
				+ ", answer_1=" + answer_1 + ", answer_2=" + answer_2
				+ ", answer_3=" + answer_3 + ", answer_4=" + answer_4
				+ ", true_answer=" + true_answer + ", image=" + image
				+ ", answer_choose=" + answer_choose + ", isAnswerTrue="
				+ isAnswerTrue + "]";
	}

	public boolean isCompare() {
		return isCompare;
	}

	public void setCompare(boolean isCompare) {
		this.isCompare = isCompare;
	}

}
