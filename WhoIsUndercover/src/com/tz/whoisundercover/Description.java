package com.tz.whoisundercover;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class Description extends Activity{
	private ListView playerList;
	private int indexWoDi = 0;
	
	private void initUI(){
		playerList = (ListView)findViewById(R.id.playerList);
	}
	
	private void initPlayers(){
		Intent intent = getIntent();
		// 1. 获取玩家信息数据; 
		indexWoDi = intent.getIntExtra("indexWoDi", 1);
		int playerNum = intent.getIntExtra("playerNum", 7);
		// 2. 依据玩家数据构造列表框; 
		ArrayList<String> arr = new ArrayList<String>();
		String tail = getResources().getString(R.string.num_player);
		for (int i = 0; i < playerNum; ++i){
			arr.add("" + (i + 1) +  tail); 
		}
		ArrayAdapter<String> aa = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, arr);
		playerList.setAdapter(aa);
	}
	
	private void addVote(TextView view){
		String line = view.getText().toString();
		PlayerInfo player = new PlayerInfo(line, this);
		player.addVote();
		view.setText(player.toString());
	}
	
	boolean checkVote(TextView view){
		return false;
	}
	
	void killPlayer(long indexPlayer, TextView view){
		// TODO 处理玩家死亡情况; 
		Log.e("tz", "Player Died " + indexPlayer);
	}
	
	private void initListener(){
		playerList.setOnItemClickListener(new ListView.OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long id) {
				if (!(view instanceof TextView)){
					return ;
				}
				TextView tv = (TextView)view;
				//Log.e("tz", "" + tv.getText().toString() + ", " + id);
				addVote(tv);
				if (checkVote(tv)){
					killPlayer(id + 1, tv);
				}
			}
			
		});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.description);
		initUI();
		initPlayers();
		initListener();
	}

}
