package com.hrj.mojian.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

object MJToast {
    private var mToast: Toast? = null
    private var mHandle: Handler? = null
    private var mShowMsgRunnable: ShowMessageRunnable? = null

    @SuppressLint("ShowToast")
    fun init(context: Context) {
        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT)
        mHandle = Handler(Looper.getMainLooper())
        mShowMsgRunnable = ShowMessageRunnable()
    }

    fun show(message: CharSequence, duration: Int) {
        mToast!!.cancel()
        mHandle!!.removeCallbacks(mShowMsgRunnable)
        mShowMsgRunnable!!.setMsgInfo(message, duration, Gravity.CENTER, 0, 0)
        mHandle!!.post(mShowMsgRunnable)
    }

    fun show(stringId: Int, duration: Int) {
        mToast!!.cancel()
        mHandle!!.removeCallbacks(mShowMsgRunnable)
        mShowMsgRunnable!!.setMsgInfo(stringId, duration, Gravity.CENTER, 0, 0)
        mHandle!!.post(mShowMsgRunnable)
    }

    val toastString: String?
        get() {
            if (mToast != null) {
                val v = mToast!!.view
                if (v is ViewGroup) {
                    val textview = v.getChildAt(0) as TextView
                    return textview.text.toString()
                }
            }
            return null
        }

    fun clearToast() {
        if (mToast != null) {
            mToast!!.setText("")
        }
    }

    internal class ShowMessageRunnable : Runnable {
        private var mResid: Int = 0
        private var mDuration: Int = 0
        private var mGravity: Int = 0
        private var mOffsetX: Int = 0
        private var mOffsetY: Int = 0
        private var mMsg: CharSequence? = null

        fun setMsgInfo(resId: Int, duration: Int, gravity: Int, x: Int, y: Int) {
            this.mResid = resId
            this.mDuration = duration
            this.mGravity = gravity
            this.mOffsetX = x
            this.mOffsetY = y
            mMsg = null
        }

        fun setMsgInfo(msg: CharSequence, duration: Int, gravity: Int, x: Int, y: Int) {
            this.mMsg = msg
            this.mDuration = duration
            this.mGravity = gravity
            this.mOffsetX = x
            this.mOffsetY = y
        }

        override fun run() {
            if (mMsg == null) {
                mToast!!.setText(mResid)
            } else {
                mToast!!.setText(mMsg)
            }

            mToast!!.duration = mDuration
            mToast!!.setGravity(mGravity, mOffsetX, mOffsetY)
            mToast!!.show()
        }
    }

    fun destroy() {
        mToast = null
        if (mHandle != null) {
            mHandle!!.removeCallbacks(mShowMsgRunnable)
            mShowMsgRunnable = null
            mHandle = null
        }
    }
}
