package com.haui.japanese.model;

import java.util.List;

/**
 * Class lưu 1 danh sách các đề thi
 * 
 * @author EO
 * 
 */
public class ListQuiz {

	List<Integer> listQuiz;

	public List<Integer> getListQuiz() {
		return listQuiz;
	}

	public void setListQuiz(List<Integer> listQuiz) {
		this.listQuiz = listQuiz;
	}

	@Override
	public String toString() {
		return "ListQuiz [listQuiz=" + listQuiz + "]";
	}

}
