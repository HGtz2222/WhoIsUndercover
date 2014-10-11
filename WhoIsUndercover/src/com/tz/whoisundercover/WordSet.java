package com.tz.whoisundercover;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.util.Log;

public class WordSet {
	private List<String> wordType;
	private String defaultWordType;
	private Context context;
	
	public WordSet(Context context){
		this.context = context;
		defaultWordType = context.getResources().getString(R.string.default_word_type);
		String[] arr = context.getResources().getStringArray(R.array.word_type);
		wordType = Arrays.asList(arr);
	}
	
	public String getPrevType(String curType) {
		int index = wordType.indexOf(curType);
		if (index == -1){
			Log.e("tz", "δ�ҵ���ǰ�Ĵ�������!");
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
			Log.e("tz", "δ�ҵ���ǰ�Ĵ�������!");
			return defaultWordType;
		}
		if (index == wordType.size() - 1){
			index = 0;
		}else{
			++index;
		}
		return wordType.get(index);
	}
	
	private int typeToId(String type){
		// 1. ����¼; 
		if (wordType.size() < 4){
			return 0;
		}
		ArrayList<Integer> table = new ArrayList<Integer>();
		table.add(0); // ռ��λ��, ʹtable��������wordType��������һһ��Ӧ; 
		table.add(R.array.word_dict_people);
		table.add(R.array.word_dict_food);
		table.add(R.array.word_dict_thing);
		// 2. �������;
		int index = 0;
		if (type.equals(defaultWordType)){
			index = RandomInt.roll(1, wordType.size() - 1);
		} else {
			index = wordType.indexOf(type);
		}
		// 3. ���ȡֵ; 
		return table.get(index);
	}

	public String[] getWordPair(String type){
		// 1. ��������ȡ�����Ĵ���(�ܴ���������); 
		int wordTypeId = typeToId(type);
		// 2. ���ش���; 
		String[] arr = context.getResources().getStringArray(wordTypeId);
		// 3. �����ȡ����;
		int index = RandomInt.rollUnique(0, arr.length / 2);
		String[] tmp = new String[]{arr[index * 2], arr[index * 2 + 1]}; 
		return tmp;
	}
}
