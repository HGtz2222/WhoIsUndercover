package com.tz.whoisundercover;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class Description extends Activity{
	private ListView playerList;
	private TextView tv_title;
	private int indexWoDi = 0;
	private Title voteMgr;
	private PlayerVoteInfo playerInfo;
	private SimpleAdapter sa;
	
	private void refreshTitle(){
		tv_title.setText(voteMgr.toString());
	}
	
	private void refreshPlayerList(){
		sa.notifyDataSetChanged();
	}
	
	private void addVote(int playerIndex) {
		playerInfo.addVote(playerIndex);
		refreshPlayerList();
		voteMgr.addVote();
		refreshTitle();
	}
	
	private int getDiedPlayer(int playerIndex){
		int curPlayerVoteCnt = playerInfo.getVoteCnt(playerIndex);
		int curVoteCnt = voteMgr.getCurVote();
		int totalVoteCnt = voteMgr.getTotalVote();
		// 1. 检测当前玩家的票数是否达到半数; 
		if (curPlayerVoteCnt > totalVoteCnt / 2){
			return playerIndex;
		}
		// 2. 检查当前票数是否达到总票数; 
		if (curVoteCnt >= totalVoteCnt){
			// 返回票数最多的玩家; 
			int maxPlayerIndex = playerInfo.getMaxVoteCntPlayerIndex();
			return maxPlayerIndex != -1 ? maxPlayerIndex : -2;  // maxPlayerIndex == -1, 说明出现平局情况; 
		}
		return -1;
	}

	private void killPlayer(int playerIndex){
		Log.e("tz", "KillPlayer " + playerIndex);
		if (playerIndex == indexWoDi){
			// TODO 卧底阵亡, 该轮游戏结束;
			gameOver();
		}else{
			// 平民阵亡; 
			killPerson(playerIndex);
			if (checkWoDiWin()){
				clearAllVote();
			}else{
				gameOver();
			}
		}
	}
	
	private boolean checkWoDiWin() {
		return voteMgr.getTotalVote() <= 2;
	}

	private void killPerson(int playerIndex) {
		// 1. 将列表中的玩家改为阵亡状态; 
		playerInfo.kill(playerIndex);
		// 2. 总玩家数 - 1;
		voteMgr.kill();
	}
	
	private void clearAllVote(){
		playerInfo.clearAllVote();
		voteMgr.clearAllVote();
		refreshPlayerList();
		refreshTitle();
	}

	private void gameOver() {
		Log.e("tz", "Game Over");
	}

	private void initUI(){
		playerList = (ListView)findViewById(R.id.playerList);
		tv_title = (TextView)findViewById(R.id.tv_playerName);
	}
	
	private void initPlayers(){
		Intent intent = getIntent();
		// 1. 获取玩家信息数据; 
		indexWoDi = intent.getIntExtra("indexWoDi", 1);
		int playerNum = intent.getIntExtra("playerNum", 7);
		// 2. 依据玩家数据构造列表框; 
		playerInfo = new PlayerVoteInfo(playerNum);
		sa = new SimpleAdapter(this, playerInfo.getData(), R.layout.vlist, 
				new String[]{"playerName", "voteCnt", "piao"}, 
				new int[]{R.id.tv_playerName, R.id.tv_voteCnt, R.id.tv_piao});
		playerList.setAdapter(sa);
		// 3. 设置投票数目; 
		String titleBeg = getResources().getString(R.string.please_vote);
		voteMgr = new Title(titleBeg, playerNum);
		refreshTitle();
	}
	
	private void initListener(){
		playerList.setOnItemClickListener(new ListView.OnItemClickListener(){
			private ArrayList<Integer> diedPlayerList = new ArrayList<Integer>();
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int pos,
					long id) {
				int playerIndex = (int)id + 1;
				// 1. 先判定是否当前玩家已经阵亡; 
				if (diedPlayerList.indexOf(playerIndex) != -1){
					return ; 
				}
				// 2. 给玩家增加票数; 
				addVote(playerIndex);
				// 3. 检测场上投票结果; 
				int diedPlayerIndex = getDiedPlayer(playerIndex);
				// 4. 死亡判定; 
				if (diedPlayerIndex == -2){
					// 平局! 重新投票; 
					clearAllVote();
					return ; 
				}
				if (diedPlayerIndex == -1){
					// 本轮投票尚未结束
					return ; 
				}
				// 5. 杀死玩家;
				killPlayer(playerIndex);
				// 6. 插入死亡玩家到列表中; 
				diedPlayerList.add(Integer.valueOf(playerIndex));
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
