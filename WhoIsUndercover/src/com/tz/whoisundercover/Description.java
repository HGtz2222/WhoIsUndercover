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
		// 1. ��ȡ�����Ϣ����; 
		indexWoDi = intent.getIntExtra("indexWoDi", 1);
		int playerNum = intent.getIntExtra("playerNum", 7);
		wordWoDi = intent.getStringExtra("wordWoDi");
		wordPingMin = intent.getStringExtra("wordPingMin");
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
				Ring.ring(Description.this);
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
					ResultMsg.winPingMin(Description.this, playerIndex, wordWoDi, wordPingMin);
					return ;
				}
				if (rt == 2){
					// ƽ������, �Ե׻�ʤ;
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
		// ʵ���������
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		// ��ȡҪǶ�������Ĳ���
		LinearLayout adLayout=(LinearLayout)findViewById(R.id.adLayout);
		// ����������뵽������
		adLayout.addView(adView);
		
		adView.setAdListener(new AdViewListener() {

		    @Override
		    public void onSwitchedAd(AdView adView) {
		        // �л���沢չʾ
		    	Log.e("tz", "�л����");
		    }

		    @Override
		    public void onReceivedAd(AdView adView) {
		        // ������ɹ�
		    	Log.e("tz", "������ɹ�");
		    }

		    @Override
		    public void onFailedToReceivedAd(AdView adView) {
		        // ������ʧ��
		    	Log.e("tz", "������ʧ��");
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
