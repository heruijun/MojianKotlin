package com.hrj.mojian.view;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hrj.mojian.App;
import com.hrj.mojian.R;
import com.hrj.mojian.activity.AddMJActivity;
import com.hrj.mojian.bean.MJBean;
import com.hrj.mojian.eventcenter.ECBase;
import com.hrj.mojian.eventcenter.EventName;
import com.hrj.mojian.utils.MJLog;
import com.hrj.mojian.utils.MJToast;

import java.util.ArrayList;
import java.util.LinkedList;

public class FunctionView {

    private static final String TAG = FunctionView.class.getSimpleName();

    private static final int PAGER_COUNT = 8;

    private final MJBean mCaptureItem = new MJBean(MJBean.BASE_TYPE, "capture", "home_capture");
    private final MJBean mRecordingItem = new MJBean(MJBean.BASE_TYPE, "sound_recording", "home_recording");
    private final MJBean mCameraItem = new MJBean(MJBean.BASE_TYPE, "open_camera", "home_camera");
    private final MJBean mElectricTorchItem = new MJBean(MJBean.BASE_TYPE, "open_flash_light", "home_electric_torch");
    private final MJBean mSweepItem = new MJBean(MJBean.BASE_TYPE, "qrcode", "home_sweep");
    private final MJBean mScreenShotItem = new MJBean(MJBean.BASE_TYPE, "screenshot", "home_screenshot");
    private final MJBean mRecentlyOpenedItem = new MJBean(MJBean.BASE_TYPE, "recently_opened", "home_recently_opened");
    private final MJBean mClearMemoryItem = new MJBean(MJBean.BASE_TYPE, "clear_memory", "home_clear_memory");
    private final MJBean mAddFunctionItem = new MJBean(MJBean.BASE_TYPE, "add_function", "icon_add");

    private ImageView mHomeView;
    private ViewPager mPager;
    private MPagerAdapter mPagerAdapter;
    private CirclePageIndicator mIndicator;
    private LinkedList<MJBean> mItems;
    private ArrayList<CustomGridView> mViews = new ArrayList<CustomGridView>();

    private Context mContext;
    private LayoutInflater mInflater;
    private int mCurPager;

    public FunctionView(View root) {
        mContext = root.getContext();
        mInflater = LayoutInflater.from(mContext);
        initAdapter();
        initData();
        initView(root);
        initEvent();
    }

    private void initAdapter() {
        mPagerAdapter = new MPagerAdapter();
    }

    private void initData() {
        mItems = App.getInstance().getFunctionItems();

        if (0 == mItems.size()) {
            mItems.add(mCaptureItem);
            mItems.add(mRecordingItem);
            mItems.add(mCameraItem);
            mItems.add(mElectricTorchItem);
            mItems.add(mSweepItem);
            mItems.add(mScreenShotItem);
            mItems.add(mRecentlyOpenedItem);
            mItems.add(mClearMemoryItem);
            mItems.add(mAddFunctionItem);
            App.getInstance().setFunctionItems(mItems);
        }

        int size = getPageSize();
        for (int i = 0; i < size; i++) {
            CustomGridView gridView = new CustomGridView(mContext, 4, 4);
            updateGridAdapter(gridView, i);
            mViews.add(gridView);
        }
    }

    private int getPageSize() {
        return mItems.size() % PAGER_COUNT == 0 ? mItems.size() / PAGER_COUNT : mItems.size() / PAGER_COUNT + 1;
    }

    private void updateGridAdapter(CustomGridView gridView, int page) {
        MGridAdapter adapter = (MGridAdapter) gridView.getAdapter();
        if (null == adapter) {
            adapter = new MGridAdapter();
            adapter.setNotifyOnChange(false);
            gridView.setAdapter(adapter);
        }
        adapter.clear();
        adapter.setData(page);
        adapter.notifyDataSetChanged();
    }

    private void initView(View root) {
        mPager = (ViewPager) root.findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mIndicator = (CirclePageIndicator) root.findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
        mIndicator.setOnPageChangeListener(mPageChangeListener);
        mHomeView = (ImageView) root.findViewById(R.id.home_view);
    }

    private void initEvent() {
        new FindFunctionItemByHashCodeEvent();
        new AddFunctionItemToHomeEvent();
        new RemoveFunctionItemFromHomeEvent();
        new ReplaceFunctionItemFromHomeEvent();
    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            mCurPager = position;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (ViewPager.SCROLL_STATE_IDLE == state) {
                if (mCurPager % 2 == 0) {
                    mHomeView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.home_bg_bird));
                } else {
                    mHomeView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.home_bg_illustration));
                }
            }
        }
    };

    private class MPagerAdapter extends PagerAdapter {

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            GridView gridView = mViews.get(position);
            container.addView(gridView);
            return gridView;
        };

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        };

    }

    private class MGridAdapter extends ArrayAdapter<MJBean> {

        public MGridAdapter() {
            super(mContext, 0);
        }

        public void setData(int page) {
            MJLog.d(TAG, "page: " + page);
            int start = page * PAGER_COUNT;
            int end = start + PAGER_COUNT;
            while (start < mItems.size() && start < end) {
                add(mItems.get(start));
                start++;
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (null == convertView) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.gridview_home_item, parent, false);
                holder.image = (ImageView) convertView.findViewById(R.id.image);
                holder.text = (TextView) convertView.findViewById(R.id.text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            MJBean mjItem = getItem(position);
            updateItemView(mjItem, holder);

            if (mjItem.getNameId() == R.string.add_function) {
                holder.image.setOnClickListener(mToAddMJFragmentClick);
            } else {
                holder.image.setOnClickListener(null);
            }

            return convertView;
        }

        private void updateItemView(MJBean mjBean, ViewHolder holder) {
            holder.image.setTag(mjBean);

            int type = mjBean.getType();

            switch (type) {
                case MJBean.BASE_TYPE:
                case MJBean.SYSTEM_TYPE:
                    holder.image.setImageDrawable(mContext.getResources().getDrawable(mjBean.getImageId()));
                    holder.text.setText(mjBean.getNameId());
                    break;

                case MJBean.APP_TYPE:
                    try {
                        holder.image.setImageDrawable(mContext.getPackageManager().getApplicationIcon(mjBean.getPackageName()));
                    } catch (NameNotFoundException e) {
                        holder.image.setImageDrawable(new BitmapDrawable());
                    }
                    holder.text.setText(mjBean.getAppName());
                    break;

                case MJBean.CONTACT_TYPE:
                    holder.image.setImageBitmap(mjBean.getContact().getBitmap());
                    holder.text.setText(mjBean.getContact().getName());
                    break;

                default:
                    break;
            }

        }

        private class ViewHolder {
            ImageView image;
            TextView text;
        }

    }

    private OnClickListener mToAddMJFragmentClick = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, AddMJActivity.class);
            mContext.startActivity(intent);
        }
    };

    /**
     * 通过hascode查找MJItem，参数：1、hashcode值
     */
    private class FindFunctionItemByHashCodeEvent extends ECBase {

        @Override
        public void run(Object[] outValues, Object[] inValues) {
            if (1 != inValues.length) {
                return;
            }

            int hashCode = (Integer) inValues[0];
            for (MJBean item : mItems) {
                if (hashCode == item.hashCode()) {
                    outValues[0] = item;
                    break;
                }
            }

        }

        @Override
        public EventName pushEventName() {
            return EventName.find_function_item_by_hashcode;
        }

    }

    /**
     * 增加MJItem到首页，参数：mjItem
     */
    private class AddFunctionItemToHomeEvent extends ECBase {

        @Override
        public void run(Object[] outValues, Object[] inValues) {
            if (1 != inValues.length) {
                return;
            }

            if (mItems.size() >= 32) {
                MJToast.show(R.string.no_add_more_mj, Toast.LENGTH_SHORT);
                return;
            }

            MJBean mjItem = (MJBean) inValues[0];
            mItems.add(mItems.size() - 1, mjItem);
            App.getInstance().setFunctionItems(mItems);

            notifyDataChange();
        }

        @Override
        public EventName pushEventName() {
            return EventName.add_function_item_to_home;
        }

    }

    /**
     * 移除首页MJItem，参数：mjItem
     */
    private class RemoveFunctionItemFromHomeEvent extends ECBase {

        @Override
        public void run(Object[] outValues, Object[] inValues) {
            if (1 != inValues.length) {
                return;
            }

            MJBean mjItem = (MJBean) inValues[0];
            mItems.remove(mjItem);
            App.getInstance().setFunctionItems(mItems);

            notifyDataChange();
        }

        @Override
        public EventName pushEventName() {
            return EventName.remove_function_item_from_home;
        }

    }

    /**
     * 替换首页MJItem，参数：1、被替换的mjItem，2、新的mjItem
     */
    private class ReplaceFunctionItemFromHomeEvent extends ECBase {

        @Override
        public void run(Object[] outValues, Object[] inValues) {
            if (2 != inValues.length) {
                return;
            }

            MJBean oldMJItem = (MJBean) inValues[0];
            MJBean newMJItem = (MJBean) inValues[1];
            int index = mItems.indexOf(oldMJItem);
            mItems.remove(oldMJItem);
            mItems.add(index, newMJItem);
            App.getInstance().setFunctionItems(mItems);

            notifyDataChange();
        }

        @Override
        public EventName pushEventName() {
            return EventName.replace_function_item_from_home;
        }

    }

    private void notifyDataChange() {
        int needSize = getPageSize();
        int nowSize = mViews.size();
        if (needSize > nowSize) {
            addGridView(needSize - nowSize);
        } else if (needSize < nowSize) {
            removeGridView(nowSize - needSize);
        }
        refreshAll();
    }

    private void addGridView(int count) {
        for (int i = 0; i < count; i++) {
            CustomGridView gridView = new CustomGridView(mContext, 4, 4);
            mViews.add(gridView);
        }
    }

    private void removeGridView(int count) {
        for (int i = 0; i < count; i++) {
            CustomGridView gridView = mViews.remove(mViews.size() - 1);
            MGridAdapter adapter = (MGridAdapter) gridView.getAdapter();
            if (null != adapter) {
                adapter.clear();
                adapter = null;
            }
            gridView = null;
        }
    }

    private void refreshAll() {
        int size = mViews.size();
        for (int i = 0; i < size; i++) {
            updateGridAdapter(mViews.get(i), i);
        }
        resetPagerAdapter();
    }

    private void resetPagerAdapter() {
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(mCurPager);
    }

}