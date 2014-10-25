package com.haui.japanse.cache;

import com.haui.japanese.model.ListQuiz;
import com.haui.japanese.util.Variable;

import android.content.Context;

public class QuizListCache extends CacheImpl{

	public QuizListCache(Context mContext) {
		super(mContext);
		setKey(Variable.CACHE_QUIZ_LIST);
	}
	
	
	public void saveQuizList(ListQuiz listQuiz){
		saveData(listQuiz);
	}
	
	public ListQuiz getListQuiz(){
		return (ListQuiz) getData(getKey(), ListQuiz.class);
	}

}
