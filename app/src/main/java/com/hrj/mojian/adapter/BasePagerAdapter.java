package com.hrj.mojian.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import java.util.ArrayList;
import java.util.List;

public class BasePagerAdapter extends PagerAdapter {

	protected List<TabItem> mTabItems = new ArrayList<TabItem>();

	@Override
	public int getCount() {
		return mTabItems.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public int getItemPosition(Object object) {
		Object tag = object;
		if (tag != null) {
			for (int i = 0; i < mTabItems.size(); i++) {
				if (tag.equals(mTabItems.get(i).getContentView())) {
					return i;
				}
			}
		}
		return POSITION_NONE;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		TabItem v = mTabItems.get(position);
		View view = v.getContentView();
		if (view != null && view.getParent() != null) {
			((ViewGroup) view.getParent()).removeView(view);
		}
		container.addView(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	public void addTab(TabItem view) {
		addTabAt(view, mTabItems.size());
	}

	public TabItem getTab(int pos) {
		return mTabItems.get(pos);
	}

	public boolean contains(TabItem tabItem) {
		return mTabItems.contains(tabItem);
	}

	public int getPosTabItem(TabItem tabItem) {
		return mTabItems.indexOf(tabItem);
	}

	public void addTabAt(TabItem view, int pos) {
		mTabItems.add(pos, view);
	}

	public void removeTab(int pos) {
		mTabItems.remove(pos);
	}

	public void removeTab(TabItem tabItem) {
		mTabItems.remove(tabItem);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return mTabItems.get(position).getPageTitle();
	}

	public static interface TabItem {
		/**
		 * 
		 * @return Content of tab, can not be null
		 */
		public View getContentView();

		/**
		 * 
		 * @return String resrouce id
		 */
		public String getPageTitle();
		
		public void onPageSelected();
	}
}
