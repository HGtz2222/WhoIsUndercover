package com.tz.whoisundercover;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

public class ResultMsg {
	public static void draw(Context context){
		String title = context.getResources().getString(R.string.result_msg_title);
		String drawStr = context.getResources().getString(R.string.vote_draw);
		String sure = context.getResources().getString(R.string.sure);
		Dialog dialog = new AlertDialog.Builder(context)
			.setTitle(title)
			.setMessage(drawStr)
			.setPositiveButton(sure, new OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			})
			.create();
		dialog.show();
	}
	
	public static void winWoDi(Context context, int playerIndex){
		
	}
	
	public static void winPingMin(Context context, int playerIndex){
		
	}
	
	public static void diedPingMin(Context context, int playerIndex){
		
	}
}
