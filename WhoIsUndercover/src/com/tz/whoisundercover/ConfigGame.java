package com.tz.whoisundercover;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ConfigGame extends Activity{
	private WordSet wordSet;
	
	private TextView tv_playerNum;
	private Button btn_playerNumPrev;
	private Button btn_playerNumNext;

	private TextView tv_wordType;
	private Button btn_wordTypePrev;
	private Button btn_wordTypeNext;
	
	private Button btn_startGameNow;
	
	private int getPlayerNum(){
		return Integer.parseInt(tv_playerNum.getText().toString());
	}
	
	private void setPlayerNum(int num){
		tv_playerNum.setText(""+num);
	}
	
	private String getWordType(){
		return tv_wordType.getText().toString();
	}
	
	private void setWordType(String str){
		tv_wordType.setText(str);
	}
	
	private void messageBox(String msg){
		Toast t = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
		t.setGravity(Gravity.CENTER, 0, 0);
		t.show();
	}
	
	private void initUI(){
		tv_playerNum = (TextView)findViewById(R.id.textView_playerNum);
		btn_playerNumPrev = (Button)findViewById(R.id.btn_playerNumPrev);
		btn_playerNumNext = (Button)findViewById(R.id.btn_playerNumNext);
		tv_wordType = (TextView)findViewById(R.id.textView_wordType);
		btn_wordTypePrev = (Button)findViewById(R.id.btn_typePrev);
		btn_wordTypeNext = (Button)findViewById(R.id.btn_typeNext);
		btn_startGameNow = (Button)findViewById(R.id.btn_startGameNow);
	}
	
	private void initListener(){
		btn_playerNumPrev.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				int num = getPlayerNum();
				if (num <= 4){
					String tip = getResources().getString(R.string.tip_playerNum_less);
					messageBox(tip);
					return ; 
				}
				-- num;
				setPlayerNum(num);
			}
			
		});
		
		btn_playerNumNext.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				int num = getPlayerNum();
				if (num >= 20){
					String tip = getResources().getString(R.string.tip_playerNum_large);
					messageBox(tip);
					return ; 
				}
				++ num;
				setPlayerNum(num);
			}
			
		});
		
		btn_wordTypePrev.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				String curType = getWordType();
				String prevType = wordSet.getPrevType(curType);
				setWordType(prevType);
			}
			
		});
		
		btn_wordTypeNext.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				String curType = getWordType();
				String nextType = wordSet.getNextType(curType);
				setWordType(nextType);
			}
			
		});
		
		btn_startGameNow.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Log.e("tz", "¿ªÊ¼ÓÎÏ·");
				Ring.ring(ConfigGame.this);
				Intent intent = new Intent(ConfigGame.this, InitId.class);
				intent.putExtra("playerNum", getPlayerNum());
				intent.putExtra("wordType", getWordType());
				startActivity(intent);
			}
		});
	}

	private void initData() {
		wordSet = new WordSet(this);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config_game);
		initUI();
		initListener();
		initData();
	}


}
