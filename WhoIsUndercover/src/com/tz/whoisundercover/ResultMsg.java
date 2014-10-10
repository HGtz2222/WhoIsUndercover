package com.tz.whoisundercover;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class ResultMsg {
	@SuppressWarnings("deprecation")
	private static Dialog makeDialog(Context context, int id){
		// 1. 创建对话框;
		Dialog dialog = new Dialog(context);
		dialog.setContentView(id);
		// 2. 根据当前屏幕大小, 设置对话框大小; 			
		WindowManager m = ((Activity)context).getWindowManager(); 
		Display d = m.getDefaultDisplay();
		Window dialogWindow = dialog.getWindow();
	    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
	    lp.height = (int)(d.getHeight() * 0.6);
	    lp.width = (int)(d.getWidth() * 0.8);
	    //Log.e("tz", "h, w" + lp.height + ", " + lp.width);
	    dialogWindow.setAttributes(lp);
	    // 3. 禁止后退; 
	    dialog.setCancelable(false);
	    return dialog;
	}
	

	public static void draw(Context context){
		final Dialog dialog = makeDialog(context, R.layout.dialog_result);
		// 1. 设置标题;
		String title = context.getResources().getString(R.string.result_draw_title);
		dialog.setTitle(title);
		// 2. 设置提示信息;
		TextView tv = (TextView)dialog.findViewById(R.id.tv_dialog_draw);
		tv.setText(R.string.no_person_died);
		// 3. 设置按钮; 
		final Activity a = (Activity)context;
		Button btn = (Button)dialog.findViewById(R.id.btn_draw);
		btn.setText(R.string.continue_vote);
		btn.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Log.e("tz", "click btn_draw");
				Ring.ring(a);
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	public static void winWoDi(Context context, int playerIndex, String wordWoDi, String wordPingMin){
		final Dialog dialog = makeDialog(context, R.layout.dialog_result);
		// 1. 设置标题;
		String title = context.getResources().getString(R.string.result_wodi_win_title);
		dialog.setTitle(title);
		// 2. 设置提示信息;
		TextView tv = (TextView)dialog.findViewById(R.id.tv_dialog_draw);
		String tail = context.getResources().getString(R.string.win_wodi);
		String wodici = context.getResources().getString(R.string.wodici);
		String pingminci = context.getResources().getString(R.string.pingminci);
		tv.setText("" + playerIndex + tail + wodici + wordWoDi + "\n" + pingminci + wordPingMin);
		// 3. 设置按钮; 
		final Description a = (Description)context;
		Button btn = (Button)dialog.findViewById(R.id.btn_draw);
		btn.setText(R.string.play_again);
		btn.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Log.e("tz", "click btn_draw");
				dialog.dismiss();
				Ring.ring(a);
				a.gameOver();
			}
		});
		dialog.show();
	}
	
	public static void winPingMin(Context context, int playerIndex, String wordWoDi, String wordPingMin){
		final Dialog dialog = makeDialog(context, R.layout.dialog_result);
		// 1. 设置标题;
		String title = context.getResources().getString(R.string.result_pingmin_win_title);
		dialog.setTitle(title);
		// 2. 设置提示信息;
		TextView tv = (TextView)dialog.findViewById(R.id.tv_dialog_draw);
		String tail = context.getResources().getString(R.string.win_pingmin);
		String wodici = context.getResources().getString(R.string.wodici);
		String pingminci = context.getResources().getString(R.string.pingminci);
		tv.setText("" + playerIndex + tail + wodici + wordWoDi + "\n" + pingminci + wordPingMin);
		// 3. 设置按钮; 
		final Description a = (Description)context;
		Button btn = (Button)dialog.findViewById(R.id.btn_draw);
		btn.setText(R.string.play_again);
		btn.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Log.e("tz", "click btn_draw");
				dialog.dismiss();
				Ring.ring(a);
				a.gameOver();
			}
		});
		dialog.show();
	}
	
	public static void diedPingMin(Context context, int playerIndex){
		final Dialog dialog = makeDialog(context, R.layout.dialog_result);
		// 1. 设置标题;
		String title = context.getResources().getString(R.string.pingmin_died);
		dialog.setTitle(title);
		// 2. 设置提示信息;
		TextView tv = (TextView)dialog.findViewById(R.id.tv_dialog_draw);
		String tail = context.getResources().getString(R.string.died_pingmin);
		tv.setText("" + playerIndex + tail);
		// 3. 设置按钮;
		final Activity a = (Activity)context;
		Button btn = (Button)dialog.findViewById(R.id.btn_draw);
		btn.setText(R.string.continue_vote);
		btn.setOnClickListener(new Button.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Log.e("tz", "click btn_draw");
				Ring.ring(a);
				dialog.dismiss();
			}
		});
		dialog.show();
	}
}
