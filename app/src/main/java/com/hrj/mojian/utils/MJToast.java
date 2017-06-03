package com.hrj.mojian.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MJToast {
    private static Toast mToast;
    private static Handler mHandle;
    private static ShowMessageRunnable mShowMsgRunnable;

    @SuppressLint("ShowToast")
    public static void init(Context context) {
        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        mHandle = new Handler(Looper.getMainLooper());
        mShowMsgRunnable = new ShowMessageRunnable();
    }

    public static void show(final CharSequence message, final int duration) {
        mToast.cancel();
        mHandle.removeCallbacks(mShowMsgRunnable);
        mShowMsgRunnable.setMsgInfo(message, duration, Gravity.CENTER, 0, 0);
        mHandle.post(mShowMsgRunnable);
    }

    public static void show(final int stringId, final int duration) {
        mToast.cancel();
        mHandle.removeCallbacks(mShowMsgRunnable);
        mShowMsgRunnable.setMsgInfo(stringId, duration, Gravity.CENTER, 0, 0);
        mHandle.post(mShowMsgRunnable);
    }

    public static String getToastString() {
        if (mToast != null) {
            View v = mToast.getView();
            if (v instanceof ViewGroup) {
                TextView textview = (TextView) ((ViewGroup) v).getChildAt(0);
                return textview.getText().toString();
            }
        }
        return null;
    }

    public static void clearToast() {
        if (mToast != null) {
            mToast.setText("");
        }
    }

    static class ShowMessageRunnable implements Runnable {
        private int mResid;
        private int mDuration;
        private int mGravity;
        private int mOffsetX;
        private int mOffsetY;
        private CharSequence mMsg;

        public void setMsgInfo(int resId, int duration, int gravity, int x, int y) {
            this.mResid = resId;
            this.mDuration = duration;
            this.mGravity = gravity;
            this.mOffsetX = x;
            this.mOffsetY = y;
            mMsg = null;
        }

        public void setMsgInfo(CharSequence msg, int duration, int gravity, int x, int y) {
            this.mMsg = msg;
            this.mDuration = duration;
            this.mGravity = gravity;
            this.mOffsetX = x;
            this.mOffsetY = y;
        }

        @Override
        public void run() {
            if (mMsg == null) {
                mToast.setText(mResid);
            } else {
                mToast.setText(mMsg);
            }

            mToast.setDuration(mDuration);
            mToast.setGravity(mGravity, mOffsetX, mOffsetY);
            mToast.show();
        }
    }

    public static void destroy() {
        mToast = null;
        if (mHandle != null) {
            mHandle.removeCallbacks(mShowMsgRunnable);
            mShowMsgRunnable = null;
            mHandle = null;
        }
    }
}
