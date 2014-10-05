package com.tz.whoisundercover;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InitId extends Activity{
	private TextView tv_wordScreen;
	private Button btn_seeWord;
	
	private int playerNum = 0;
	private int curIndex = 0;
	private boolean isShowWord = false; 
	
	private WordSet wordSet;
	private String[] word;
	private int indexWoDi = 0; 
	
	private String getCurWord(){
		if (curIndex == indexWoDi){
			return word[1];
		}
		return word[0];
	}
	
	private void setCurPlayerWord(String word){
		tv_wordScreen.setText(word);
		String complete = getResources().getString(R.string.complete);
		btn_seeWord.setText(complete);
	}
	
	private void initUI(){
		tv_wordScreen = (TextView)findViewById(R.id.tv_wordScreen);
		btn_seeWord = (Button)findViewById(R.id.btn_seeWord);
		showOrder();
	}
	
	private void showWord(){
		String curWord = getCurWord();
		setCurPlayerWord(curWord);
	}
	
	private void showOrder(){
		++curIndex;
		setCurPlayerTip(curIndex);		
	}
	
	private void startDescription(){
		Intent intent = new Intent(this, Description.class);
		startActivity(intent);
		finish();
	}
	
	private void initListener(){
		btn_seeWord.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Ring.ring(InitId.this);
				if (!isShowWord){
					showWord();
					isShowWord = true;
				}else{
					if (curIndex >= playerNum){
						startDescription();
						return ; 
					}
					showOrder();
					isShowWord = false;
				}
			}
		});
	}
	
	private void initData(){
		// 1. 创建WordSet管理类; 
		wordSet = new WordSet(this);
		// 2. 获取游戏人数, 初始化卧底编号; 
		Intent intent = getIntent();
		playerNum = intent.getIntExtra("playerNum", 7);
		indexWoDi = makeIndexWoDi(playerNum);
		// 3. 根据类型, 获取词语; 
		String wordType = intent.getStringExtra("wordType");
		word = wordSet.getWordPair(wordType);
		Log.d("tz", "playerNum: " + playerNum + ", " + "indexWoDi: " + indexWoDi + ", "
				+ "wordType: " + wordType + ", " + "word: " + word[0] + ", " + word[1]);
	}
	
	private int makeIndexWoDi(int num) {
		Random r = new Random(System.currentTimeMillis());
		return r.nextInt(num) + 1; // 玩家是从1号开始计算的; 
	}

	private void setCurPlayerTip(int num){
		String tipPassTo = getResources().getString(R.string.pass_to);
		String numPlayer = getResources().getString(R.string.num_player);
		tv_wordScreen.setText(tipPassTo + " " + num + " " + numPlayer);
		String seeWordText = getResources().getString(R.string.see_word);
		btn_seeWord.setText(seeWordText);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.init_id);
		initData();
		initUI();
		initListener();
	}

}
 