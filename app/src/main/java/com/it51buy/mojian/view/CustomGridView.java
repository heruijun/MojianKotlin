package com.it51buy.mojian.view;

import android.content.Context;
import android.content.res.Configuration;
import android.widget.GridView;

import com.it51buy.mojian.R;
import com.it51buy.mojian.utils.DisplayUtil;

public class CustomGridView extends GridView {

	private int mPorNumColumn;
	private int mLandNumColumn;

	public CustomGridView(Context context, int porNumColumn, int landNumColumn) {
		super(context);
		setSelector(getResources().getDrawable(android.R.color.transparent));
		setHorizontalSpacing(getDimen(R.dimen.home_gridview_horizontalSpacing));
		setVerticalSpacing(getDimen(R.dimen.home_gridview_verticalSpacing));
		setPadding(getDimen(R.dimen.home_viewpager_padding_left), 0, getDimen(R.dimen.home_viewpager_padding_left), 0);
		setStretchMode(STRETCH_COLUMN_WIDTH);
		mPorNumColumn = porNumColumn;
		mLandNumColumn = landNumColumn;
		initNumColumns();
	}

	private int getDimen(int resourceId) {
		return getResources().getDimensionPixelOffset(resourceId);
	}

	@Override
	protected void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		initNumColumns();
	}

	private void initNumColumns() {
		setNumColumns(DisplayUtil.isLand(getContext()) ? mLandNumColumn : mPorNumColumn);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
