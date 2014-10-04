package com.tz.whoisundercover;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class InitId extends Activity{
	private TextView tv_wordScreen;
	private Button btn_seeWord;
	
	private int playerNum = 0;
	private int curIndex = 0;
	private String wordType;
	
	private void initUI(){
		tv_wordScreen = (TextView)findViewById(R.id.tv_wordScreen);
		btn_seeWord = (Button)findViewById(R.id.btn_seeWord);
		setCurPlayerTip(1);
	}
	
	private void initListener(){
		btn_seeWord.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				
			}
		});
	}
	
	private void initData(){
		Intent intent = getIntent();
		playerNum = intent.getIntExtra("playerNum", 7);
		wordType = intent.getStringExtra("wordType");
	}
	
	private void setCurPlayerTip(int num){
		String tipPassTo = getResources().getString(R.string.pass_to);
		String numPlayer = getResources().getString(R.string.num_player);
		tv_wordScreen.setText(tipPassTo + " " + num + " " + numPlayer);
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
