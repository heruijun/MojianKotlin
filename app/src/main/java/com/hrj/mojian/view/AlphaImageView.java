package com.hrj.mojian.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.hrj.mojian.utils.Define;

public class AlphaImageView extends android.support.v7.widget.AppCompatImageView {

	private int mAlpha = Define.ENABLEDALPHA;

	public AlphaImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public AlphaImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AlphaImageView(Context context) {
		super(context);
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.saveLayerAlpha(0, 0, canvas.getWidth(), canvas.getHeight(),
				mAlpha, Canvas.ALL_SAVE_FLAG);
		super.draw(canvas);
	}

	@Override
	public void refreshDrawableState() {
		if (isEnabled()) {
			mAlpha = isPressed() ? Define.PRESSALPHA : Define.ENABLEDALPHA;
		} else {
			mAlpha = Define.NOTENABLEDALPHA;
		}
		super.refreshDrawableState();
		invalidate();
		
		refreshState();
	}
	
	protected void refreshState(){
	}
}
