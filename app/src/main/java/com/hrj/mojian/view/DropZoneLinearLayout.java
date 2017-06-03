package com.hrj.mojian.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.hrj.mojian.logic.MJDragLogic;

public class DropZoneLinearLayout extends LinearLayout {

	public DropZoneLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public DropZoneLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DropZoneLinearLayout(Context context) {
		super(context);
		init();
	}

	private void init() {
		setOnDragListener(new MJDragLogic(this));
	}
}
