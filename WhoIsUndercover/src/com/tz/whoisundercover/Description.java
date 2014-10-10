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
		// 1. ��⵱ǰ��ҵ�Ʊ���Ƿ�ﵽ����; 
		if (curPlayerVoteCnt > totalVoteCnt / 2){
			return playerIndex;
		}
		// 2. ��鵱ǰƱ���Ƿ�ﵽ��Ʊ��; 
		if (curVoteCnt >= totalVoteCnt){
			// ����Ʊ���������; 
			int maxPlayerIndex = playerInfo.getMaxVoteCntPlayerIndex();
			return maxPlayerIndex != -1 ? maxPlayerIndex : -2;  // maxPlayerIndex == -1, ˵������ƽ�����; 
		}
		return -1;
	}

	private int killPlayer(int playerIndex){
		Log.e("tz", "KillPlayer " + playerIndex);
		if (playerIndex == indexWoDi){
			// �Ե�����; 
			return 1;
		}else{
			// ƽ������; 
			killPerson(playerIndex);
			if (checkWoDiWin()){
				// �Ե׻�ʤ; 
				return 2;
			}else{
				// ʤ��δ��, ����ͶƱ; 
				clearAllVote();
				return 0;
			}
		}
	}
	
	private boolean checkWoDiWin() {
		return voteMgr.getTotalVote() <= 2;
	}

	private void killPerson(int playerIndex) {
		// 1. ���б��е���Ҹ�Ϊ����״̬; 
		playerInfo.kill(playerIndex);
		// 2. ������� - 1;
		voteMgr.kill();
		refreshTitle();
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
		// 1. ��ȡ�����Ϣ����; 
		indexWoDi = intent.getIntExtra("indexWoDi", 1);
		int playerNum = intent.getIntExtra("playerNum", 7);
		// 2. ����������ݹ����б��; 
		playerInfo = new PlayerVoteInfo(playerNum);
		sa = new SimpleAdapter(this, playerInfo.getData(), R.layout.vlist, 
				new String[]{"playerName", "voteCnt", "piao"}, 
				new int[]{R.id.tv_playerName, R.id.tv_voteCnt, R.id.tv_piao});
		playerList.setAdapter(sa);
		// 3. ����ͶƱ��Ŀ; 
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
				// 1. ���ж��Ƿ�ǰ����Ѿ�����; 
				if (diedPlayerList.indexOf(playerIndex) != -1){
					return ; 
				}
				// 2. ���������Ʊ��; 
				addVote(playerIndex);
				// 3. ��ⳡ��ͶƱ���; 
				int diedPlayerIndex = getDiedPlayer(playerIndex);
				// 4. �����ж�; 
				if (diedPlayerIndex == -2){
					// ƽ��! ����ͶƱ; 
					clearAllVote();
					ResultMsg.draw(Description.this);
					return ; 
				}
				if (diedPlayerIndex == -1){
					// ����ͶƱ��δ����
					return ; 
				}
				// 5. ɱ�����; 
				int rt = killPlayer(diedPlayerIndex);
				// 6. �����������֮����ƺ�; 
				dealDiedPlayer(diedPlayerIndex, rt);
			}
			
			private void dealDiedPlayer(int playerIndex, int rt) {
				if (rt == 0){
					// ƽ������, ʤ��δ��, ��������Ҽ��������б�; 
					diedPlayerList.add(Integer.valueOf(playerIndex));
					ResultMsg.diedPingMin(Description.this, playerIndex);
					return ; 
				}
				if (rt == 1){
					// �Ե�����; 
					diedPlayerList.clear();
					ResultMsg.winPingMin(Description.this, playerIndex);
					return ;
				}
				if (rt == 2){
					// ƽ������, �Ե׻�ʤ;
					diedPlayerList.clear();
					ResultMsg.winWoDi(Description.this, indexWoDi);
					return ;
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
