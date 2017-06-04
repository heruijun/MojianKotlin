package com.hrj.mojian

import android.content.Context
import android.os.Looper
import android.view.Gravity
import android.widget.Toast
import com.hrj.mojian.utils.MJLog
import java.lang.Thread.UncaughtExceptionHandler

class CrashHandler(handler: Context) : UncaughtExceptionHandler {

    protected var handler: Context? = null

    init {
        this.handler = handler
    }

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        object : Thread() {
            override fun run() {
                Looper.prepare()
                val toast = Toast.makeText(handler, R.string.app_unknownError, Toast.LENGTH_LONG)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
                Looper.loop()
            }
        }.start()

        val cause = throwable.javaClass.simpleName
        MJLog.e(TAG, cause, throwable)
        FileLog.e(TAG, cause, throwable)
    }

    companion object {

        private val TAG = "CrashHandler"
    }

}
