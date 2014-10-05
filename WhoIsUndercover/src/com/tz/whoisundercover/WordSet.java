package com.tz.whoisundercover;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.util.Log;

public class WordSet {
	private List<String> wordType;
	private String defaultWordType;
	
	public WordSet(Context context){
		defaultWordType = context.getResources().getString(R.string.default_word_type);
		String[] arr = context.getResources().getStringArray(R.array.word_type);
		wordType = Arrays.asList(arr);
	}
	
	public String getPrevType(String curType) {
		int index = wordType.indexOf(curType);
		if (index == -1){
			Log.e("tz", "未找到当前的词语类型!");
			return defaultWordType;
		}
		if (index == 0){
			index = wordType.size() - 1;
		}else{
			--index;
		}
		return wordType.get(index);
	}

	public String getNextType(String curType) {
		int index = wordType.indexOf(curType);
		if (index == -1){
			Log.e("tz", "未找到当前的词语类型!");
			return defaultWordType;
		}
		if (index == wordType.size() - 1){
			index = 0;
		}else{
			++index;
		}
		return wordType.get(index);
	}

	public String[] getWordPair(String type){
		String[] tmp = new String[]{"唐僧", "法海"}; 
		return tmp;
	}
}
