package com.tz.whoisundercover;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;

public class PlayerVoteInfo {
	private List<Map<String, Object>> data;
	private int playerNum;
	
	private List<Map<String, Object>> initData(){
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (int i = 1; i <= playerNum; ++i){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("playerName", "���"+i);
			map.put("voteCnt", Integer.valueOf(0));
			map.put("piao", "Ʊ");
			list.add(map);
		}
		return list;
	}
	
	public PlayerVoteInfo(int playerNum) {
		this.playerNum = playerNum;
		this.data = initData();
	}

	public List<Map<String, Object>> getData() {
		return data;
	}
	
	public int getVoteCnt(int playerIndex){
		int arrIndex = playerIndex - 1;
		HashMap<String, Object> map = (HashMap<String, Object>)data.get(arrIndex);
		Object obj = map.get("voteCnt");
		if (obj instanceof Integer){
			Integer cnt = (Integer)obj;
			return cnt.intValue();
		}
		return 0;
	}
	
	private void setVoteCnt(int playerIndex, int cnt){
		int arrIndex = playerIndex - 1;
		HashMap<String, Object> map = (HashMap<String, Object>)data.get(arrIndex);
		map.put("voteCnt", cnt);
	}

	public void addVote(int playerIndex) {
		if (checkDied(playerIndex)){
			return ; 
		}
		int cnt = getVoteCnt(playerIndex);
		++cnt;
		setVoteCnt(playerIndex, cnt);
	}

	public void kill(int playerIndex) {
		// ����������޸�Ϊ����״̬; 
		HashMap<String, Object> map = getMap(playerIndex);
		map.put("playerName", "ƽ������");
		map.put("piao", "");
		map.put("voteCnt", "");
	}
	
	private boolean checkDied(int playerIndex){
		HashMap<String, Object> map = getMap(playerIndex);
		String piao = (String)map.get("piao");
		return piao.equals("");
	}

	private HashMap<String, Object> getMap(int playerIndex) {
		int arrIndex = playerIndex - 1;
		HashMap<String, Object> map = (HashMap<String, Object>)data.get(arrIndex);
		return map;
	}

	public void clearAllVote() {
		for (int i = 0; i < playerNum; ++i){
			if (!checkDied(i + 1)){ // �������������; 
				setVoteCnt(i + 1, 0);	
			}
		}
	}

	public int getMaxVoteCntPlayerIndex() {
		int maxVoteCnt = 0;
		int maxVoteIndex = 0;
		// �ҵ���Ʊ������ͯЬ; 
		for (int playerIndex = 1; playerIndex <= playerNum; ++playerIndex){
			if (checkDied(playerIndex)){
				continue;
			}
			int curVoteCnt = getVoteCnt(playerIndex);
			if (curVoteCnt > maxVoteCnt){
				maxVoteCnt = curVoteCnt;
				maxVoteIndex = playerIndex;
			}
		}
		// �ж��Ƿ����ƽ��; 
		int maxCnt = 0;
		for (int playerIndex = 1; playerIndex <= playerNum; ++playerIndex){
			if (checkDied(playerIndex)){
				continue;
			}
			int curVoteCnt = getVoteCnt(playerIndex);
			if (curVoteCnt == maxVoteCnt){
				++maxCnt;
			}
		}
		if (maxCnt > 1){
			return -1;
		}
		return maxVoteIndex;
	}
}
