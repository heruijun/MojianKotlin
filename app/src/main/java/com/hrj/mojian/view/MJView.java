package com.hrj.mojian.view;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hrj.mojian.App;
import com.hrj.mojian.R;
import com.hrj.mojian.bean.MJBean;
import com.hrj.mojian.bean.MJItem;
import com.hrj.mojian.eventcenter.ECBase;
import com.hrj.mojian.eventcenter.EventName;

public class MJView {

    private ImageView mj_1_img;
    private ImageView mj_2_img;
    private ImageView mj_3_img;
    private ImageView mj_4_img;

    private TextView mj_1_txt;
    private TextView mj_2_txt;
    private TextView mj_3_txt;
    private TextView mj_4_txt;

    private MJItem[] mjItems;

    private Context mContext;

    public MJView(View root) {
        mContext = root.getContext();
        initView(root);
        initData();
        initEvent();
    }

    private void initView(View root) {
        mj_1_img = (ImageView) root.findViewById(R.id.mj_1_img);
        mj_2_img = (ImageView) root.findViewById(R.id.mj_2_img);
        mj_3_img = (ImageView) root.findViewById(R.id.mj_3_img);
        mj_4_img = (ImageView) root.findViewById(R.id.mj_4_img);

        mj_1_txt = (TextView) root.findViewById(R.id.mj_1_txt);
        mj_2_txt = (TextView) root.findViewById(R.id.mj_2_txt);
        mj_3_txt = (TextView) root.findViewById(R.id.mj_3_txt);
        mj_4_txt = (TextView) root.findViewById(R.id.mj_4_txt);
    }

    private void initData() {
        mjItems = App.getInstance().getMJItems();

        MJItem mjItem = mjItems[0];
        refreshView(mj_1_img, mj_1_txt, mjItem);

        mjItem = mjItems[1];
        refreshView(mj_2_img, mj_2_txt, mjItem);

        mjItem = mjItems[2];
        refreshView(mj_3_img, mj_3_txt, mjItem);

        mjItem = mjItems[3];
        refreshView(mj_4_img, mj_4_txt, mjItem);
    }

    @SuppressWarnings("deprecation")
    private void refreshView(ImageView imageView, TextView textView, MJItem mjItem) {
        if (null == mjItem) {
            imageView.setImageDrawable(null);
            textView.setText(null);
            return;
        }

        switch (mjItem.getMjBean().getType()) {
            case MJBean.SYSTEM_TYPE:
            case MJBean.BASE_TYPE:
                imageView.setImageDrawable(mContext.getResources().getDrawable(mjItem.getMjBean().getImageId()));
                textView.setText(mjItem.getMjBean().getNameId());
                break;

            case MJBean.APP_TYPE:
                try {
                    imageView.setImageDrawable(mContext.getPackageManager().getApplicationIcon(mjItem.getMjBean().getPackageName()));
                } catch (NameNotFoundException e) {
                    imageView.setImageDrawable(new BitmapDrawable());
                }
                textView.setText(mjItem.getMjBean().getAppName());
                break;

            case MJBean.CONTACT_TYPE:
                imageView.setImageBitmap(mjItem.getMjBean().getContact().getBitmap());
                textView.setText(mjItem.getMjBean().getContact().getName());
                break;

            default:
                break;
        }
    }

    private void refreshView(int index, MJItem mjItem) {
        if (0 == index) {
            refreshView(mj_1_img, mj_1_txt, mjItem);
        } else if (1 == index) {
            refreshView(mj_2_img, mj_2_txt, mjItem);
        } else if (2 == index) {
            refreshView(mj_3_img, mj_3_txt, mjItem);
        } else {
            refreshView(mj_4_img, mj_4_txt, mjItem);
        }
    }

    private void initEvent() {
        new MJIsSettedEvent();
        new MJRemoveEvent();
        new MJSetEvent();
        new MJReplaceEvent();
    }

    /**
     * 判断某一魔键是否设置功能，需要魔键位置（0，1，2，3，4）
     */
    private class MJIsSettedEvent extends ECBase {

        @Override
        public void run(Object[] outValues, Object[] inValues) {
            if (1 != inValues.length) {
                return;
            }

            int index = (Integer) inValues[0];
            if (null == mjItems[index]) {
                outValues[0] = false;
            } else {
                outValues[0] = true;
            }
        }

        @Override
        public EventName pushEventName() {
            return EventName.mj_is_setted;
        }

    }

    /**
     * 清除魔键功能，需要魔键位置
     */
    private class MJRemoveEvent extends ECBase {

        @Override
        public void run(Object[] outValues, Object[] inValues) {
            if (1 != inValues.length) {
                return;
            }

            int index = (Integer) inValues[0];
            refreshView(index, null);
            mjItems[index] = null;
            App.getInstance().setMjItems(mjItems);
        }

        @Override
        public EventName pushEventName() {
            return EventName.mj_remove;
        }

    }

    /**
     * 替换魔键功能，参数：1、old魔键的位置，2、new魔键的位置
     */
    private class MJReplaceEvent extends ECBase {

        @Override
        public void run(Object[] outValues, Object[] inValues) {
            if (2 != inValues.length) {
                return;
            }

            int oldIndex = (Integer) inValues[0];
            int newIndex = (Integer) inValues[1];

            MJItem oldMjItem = mjItems[oldIndex];
            MJItem newMjItem = mjItems[newIndex];
            mjItems[oldIndex] = newMjItem;
            mjItems[newIndex] = oldMjItem;

            refreshView(oldIndex, mjItems[oldIndex]);
            refreshView(newIndex, mjItems[newIndex]);

            App.getInstance().setMjItems(mjItems);
        }

        @Override
        public EventName pushEventName() {
            return EventName.mj_replace;
        }

    }

    /**
     * 设置魔键功能，需要传两个参数：1、魔键位置，2、被设置的魔键
     */
    private class MJSetEvent extends ECBase {

        @Override
        public void run(Object[] outValues, Object[] inValues) {
            if (2 != inValues.length) {
                return;
            }

            int index = (Integer) inValues[0];
            MJItem mjItem = (MJItem) inValues[1];
            refreshView(index, mjItem);

            mjItems[index] = mjItem;
            App.getInstance().setMjItems(mjItems);
        }

        @Override
        public EventName pushEventName() {
            return EventName.mj_set;
        }

    }

}