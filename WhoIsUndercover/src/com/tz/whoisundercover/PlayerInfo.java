package com.tz.whoisundercover;

import android.content.Context;
import android.util.Log;

public class PlayerInfo {
	int indexPlayer = 0;
	int voteCnt = 0;
	
	Context context; 
	
	public PlayerInfo(String str, Context context){
		this.context = context;
		String[] arr = str.split(" ");
		if (arr.length > 1){
			indexPlayer = Integer.parseInt(arr[0]);
			voteCnt = Integer.parseInt(arr[1]);
		}else if (arr.length > 0){
			indexPlayer = Integer.parseInt(arr[0]);
		}else{
			Log.e("tz", "Error text");
		}
	}

	@Override
	public String toString() {
		String middle = context.getResources().getString(R.string.num_player);
		String tail = context.getResources().getString(R.string.piao);
		return "" + indexPlayer + middle + "       " + voteCnt + tail;
	}

	public void addVote() {
		++ voteCnt;
	}
}
