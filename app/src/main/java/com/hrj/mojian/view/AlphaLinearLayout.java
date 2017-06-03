package com.hrj.mojian.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.hrj.mojian.utils.Define;

public class AlphaLinearLayout extends LinearLayout {

	private int mAlpha = Define.ENABLEDALPHA;
	
	public AlphaLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		loadAttrs(attrs);
	}

	public AlphaLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		loadAttrs(attrs);
	}

	public AlphaLinearLayout(Context context) {
		super(context);
	}
	
	private void loadAttrs(AttributeSet attrs) {
		if (attrs != null) {
			boolean enable = attrs.getAttributeBooleanValue(null, "enabled", true);
			setEnabled(enable);
		}
	}
	
	@Override
	public void refreshDrawableState() {
		if (isEnabled()) {
			setAlpha(isPressed() ? Define.PRESSALPHA : Define.ENABLEDALPHA);
		} else {
			setAlpha(Define.NOTENABLEDALPHA);
		}
		super.refreshDrawableState();
	}
	
	public void setAlpha(int alpha){
		mAlpha = alpha;
		invalidate();
	}
	
	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		canvas.save();
		canvas.saveLayerAlpha(0, 0, canvas.getWidth(), canvas.getHeight(), mAlpha, Canvas.ALL_SAVE_FLAG);
		boolean result = super.drawChild(canvas, child, drawingTime);
		canvas.restore();
		return result;
	}

}
