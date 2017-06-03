package com.it51buy.mojian.view;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

import com.it51buy.mojian.R;
import com.it51buy.mojian.adapter.BasePagerAdapter;
import com.it51buy.mojian.adapter.BasePagerAdapter.TabItem;

public class BaseTabView {

	private PagerSlidingTabStrip mTabStrip;
	private ViewPager mPager;
	private BasePagerAdapter mPagerAdapter;

	public BaseTabView(View fragView) {
		initAdapter();
		initView(fragView);
	}

	private void initAdapter() {
		mPagerAdapter = new BasePagerAdapter();
	}

	private void initView(View fragView) {
		mTabStrip = (PagerSlidingTabStrip) fragView.findViewById(R.id.tabs);
		mPager = (ViewPager) fragView.findViewById(R.id.pager);
		mPager.setAdapter(mPagerAdapter);
		mTabStrip.setViewPager(mPager);
		mTabStrip.setTextSize(fragView.getResources().getDimensionPixelOffset(R.dimen.normal_text_size));
		mTabStrip.setIndicatorColorResource(R.color.tab_indicator_color);
		mTabStrip.setOnPageChangeListener(mPageChangeListener);
	}

	public void addTabItem(TabItem... tabItems) {
		for (TabItem tabItem : tabItems) {
			mPagerAdapter.addTab(tabItem);
		}
		mPagerAdapter.notifyDataSetChanged();
		mTabStrip.notifyDataSetChanged();
	}

	private OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {

		private int curPos;

		@Override
		public void onPageSelected(int position) {
			curPos = position;
		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			if (ViewPager.SCROLL_STATE_IDLE == state) {
				TabItem tab = mPagerAdapter.getTab(curPos);
				tab.onPageSelected();
			}
		}
	};

}
