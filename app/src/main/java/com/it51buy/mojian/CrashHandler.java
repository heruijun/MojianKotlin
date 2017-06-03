package com.it51buy.mojian;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

import com.it51buy.mojian.utils.MJLog;

public class CrashHandler implements UncaughtExceptionHandler {
	
	private static final String TAG = "CrashHandler";
	
	protected Context handler = null;

	public CrashHandler(Context handler) {
		this.handler = handler;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable throwable) {
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				Toast toast = Toast.makeText(handler, R.string.app_unknownError, Toast.LENGTH_LONG);
				toast.setGravity(Gravity.CENTER, 0, 0);
				toast.show();
				Looper.loop();
			}
		}.start();

		final String cause = throwable.getClass().getSimpleName();
		MJLog.e(TAG, cause, throwable);
		FileLog.e(TAG, cause, throwable);
	}

}
