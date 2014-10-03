package com.tz.whoisundercover;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RulesGame extends Activity{
	private Button btnReturnMainMenu;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rules_game);
		
		btnReturnMainMenu = (Button)findViewById(R.id.btn_return_main_menu);
		btnReturnMainMenu.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Ring.ring(RulesGame.this);
				finish();
			}
			
		});
	}
}
