package com.dunglv.calendar.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class MyCircleView extends View {
	private int width = 0;
	private int height = 0;
	private int color;
	private Paint paint;

	public MyCircleView(Context context) {
		super(context);
	}

	public MyCircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyCircleView(Context context, int color) {
		super(context);
		this.color = color;
		paint = new Paint();
		paint.setColor(color);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		width = w;
		height = h;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (paint != null) {
			canvas.drawCircle(width / 2, height / 2, width / 2, paint);
			invalidate();
		}

	}
}