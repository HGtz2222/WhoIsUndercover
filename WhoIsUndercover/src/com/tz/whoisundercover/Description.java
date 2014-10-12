package com.tz.whoisundercover;

import java.util.ArrayList;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.banner.AdViewListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
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
	private String wordWoDi;
	private String wordPingMin;
	private MenuButton menuBtn;
	
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

	private int killPlayer(int playerIndex){
		Log.e("tz", "KillPlayer " + playerIndex);
		if (playerIndex == indexWoDi){
			// 卧底阵亡; 
			return 1;
		}else{
			// 平民阵亡; 
			killPerson(playerIndex);
			if (checkWoDiWin()){
				// 卧底获胜; 
				return 2;
			}else{
				// 胜负未分, 下轮投票; 
				clearAllVote();
				return 0;
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
		refreshTitle();
	}
	
	private void clearAllVote(){
		playerInfo.clearAllVote();
		voteMgr.clearAllVote();
		refreshPlayerList();
		refreshTitle();
	}

	public void gameOver() {
		Log.e("tz", "Game Over");
		finish();
	}

	private void initUI(){
		playerList = (ListView)findViewById(R.id.playerList);
		tv_title = (TextView)findViewById(R.id.tv_playerName);
		menuBtn = (MenuButton)findViewById(R.id.btn_menu);
	}
	
	private void initPlayers(){
		Intent intent = getIntent();
		// 1. 获取玩家信息数据; 
		indexWoDi = intent.getIntExtra("indexWoDi", 1);
		int playerNum = intent.getIntExtra("playerNum", 7);
		wordWoDi = intent.getStringExtra("wordWoDi");
		wordPingMin = intent.getStringExtra("wordPingMin");
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
				Ring.ring(Description.this);
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
					ResultMsg.draw(Description.this);
					return ; 
				}
				if (diedPlayerIndex == -1){
					// 本轮投票尚未结束
					return ; 
				}
				// 5. 杀死玩家; 
				int rt = killPlayer(diedPlayerIndex);
				// 6. 处理玩家死亡之后的善后; 
				dealDiedPlayer(diedPlayerIndex, rt);
			}
			
			private void dealDiedPlayer(int playerIndex, int rt) {
				if (rt == 0){
					// 平民死亡, 胜负未分, 将死亡玩家加入死亡列表; 
					diedPlayerList.add(Integer.valueOf(playerIndex));
					ResultMsg.diedPingMin(Description.this, playerIndex);
					return ; 
				}
				if (rt == 1){
					// 卧底阵亡; 
					diedPlayerList.clear();
					ResultMsg.winPingMin(Description.this, playerIndex, wordWoDi, wordPingMin);
					return ;
				}
				if (rt == 2){
					// 平民阵亡, 卧底获胜;
					diedPlayerList.clear();
					ResultMsg.winWoDi(Description.this, indexWoDi, wordWoDi, wordPingMin);
					return ;
				}
			}
		});
		
		menuBtn.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				openOptionsMenu();
			}
		});
	}
	
	private void initAdBar(){
		// 实例化广告条
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		// 获取要嵌入广告条的布局
		LinearLayout adLayout=(LinearLayout)findViewById(R.id.adLayout);
		// 将广告条加入到布局中
		adLayout.addView(adView);
		
		adView.setAdListener(new AdViewListener() {

		    @Override
		    public void onSwitchedAd(AdView adView) {
		        // 切换广告并展示
		    	Log.e("tz", "切换广告");
		    }

		    @Override
		    public void onReceivedAd(AdView adView) {
		        // 请求广告成功
		    	Log.e("tz", "请求广告成功");
		    }

		    @Override
		    public void onFailedToReceivedAd(AdView adView) {
		        // 请求广告失败
		    	Log.e("tz", "请求广告失败");
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
		initAdBar();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, Menu.FIRST + 1, 1, getResources().getString(R.string.clear_vote));
		menu.add(Menu.NONE, Menu.FIRST + 2, 2, getResources().getString(R.string.restart));
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == Menu.FIRST + 1){
			clearAllVote();
		}
		if (id == Menu.FIRST + 2){
			ResultMsg.exit(Description.this);
		}
		return true;
	}

	@Override
	public void onBackPressed() {
		ResultMsg.exit(Description.this);
	}

}
