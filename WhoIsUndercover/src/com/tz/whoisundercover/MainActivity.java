package com.tz.whoisundercover;

import net.youmi.android.AdManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	private Button btnStartGame;
	private Button btnSeeRules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initUI();
        initAd();
    }

	private void initAd(){
		AdManager.getInstance(this).init("195128cd9e70d210", "86ad3be86e654891", false);
		//SpotManager.getInstance(this).loadSpotAds();// TODO ��ʱ�رղ������; 
	}

    private void initUI() {
    	btnStartGame = (Button)findViewById(R.id.btn_start_game);
    	btnStartGame.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Log.e("tz", "��ʼ��Ϸ!");
				Ring.ring(MainActivity.this);
				Intent intent = new Intent(MainActivity.this, ConfigGame.class);
				startActivity(intent);
			}
    		
    	});
    	
    	btnSeeRules = (Button)findViewById(R.id.btn_rules);
    	btnSeeRules.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Log.e("tz", "�鿴����");
				Ring.ring(MainActivity.this);
				Intent intent = new Intent(MainActivity.this, RulesGame.class);
				startActivity(intent);
			}
    		
    	});
	}
}
