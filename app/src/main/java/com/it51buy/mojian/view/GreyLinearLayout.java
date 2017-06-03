package com.it51buy.mojian.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class GreyLinearLayout extends LinearLayout {

	private Paint mPaint = new Paint();

	public GreyLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public GreyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public GreyLinearLayout(Context context) {
		super(context);
		init();
	}

	private void init() {
		mPaint.setColor(getResources().getColor(android.R.color.transparent));
	}

	public void setEnable(boolean enable) {
		mPaint.setColor(enable ? getResources().getColor(android.R.color.transparent) : getResources().getColor(android.R.color.holo_blue_light));
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), mPaint, Canvas.ALL_SAVE_FLAG);
		super.onDraw(canvas);
	}

}
