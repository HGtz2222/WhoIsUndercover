package com.tz.whoisundercover;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;

public class MenuButton extends Button{
	private Paint linePaint;
	
	private void init(){
		Resources r = this.getResources();
		linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		linePaint.setColor(r.getColor(R.color.textColor));
		linePaint.setStrokeWidth(2.5f);
		linePaint.setStyle(Paint.Style.FILL_AND_STROKE);
	}
	
	public MenuButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public MenuButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MenuButton(Context context) {
		super(context);
		init();
	}

	@Override  
	protected void onDraw(Canvas canvas){
		int mMeasuredW = getMeasuredWidth();
		int mMeasuredH = getMeasuredHeight();
		int middle_x = mMeasuredW / 2;
		int middle_y = mMeasuredH / 2;
		// 1. 绘制中间的线; 
		int s_x1 = middle_x / 2; 
		int s_y1 = middle_y;
		int f_x1 = middle_x + middle_x / 2;
		int f_y1 = middle_y;
		canvas.drawLine(s_x1, s_y1, f_x1, f_y1, linePaint);
		// 2. 绘制上线; 
		int s_x2 = s_x1;
		int s_y2 = middle_y - middle_y / 3;
		int f_x2 = f_x1;
		int f_y2 = s_y2;
		canvas.drawLine(s_x2, s_y2, f_x2, f_y2, linePaint);
		// 3. 绘制下线; 
		int s_x3 = s_x1;
		int s_y3 = middle_y / 3 + middle_y;
		int f_x3 = f_x1;
		int f_y3 = s_y3;
		canvas.drawLine(s_x3, s_y3, f_x3, f_y3, linePaint);
	}
}
