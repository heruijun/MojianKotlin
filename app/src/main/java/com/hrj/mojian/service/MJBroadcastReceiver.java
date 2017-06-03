package com.hrj.mojian.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MJBroadcastReceiver extends BroadcastReceiver {

	private static final String ACTION = "android.intent.action.BOOT_COMPLETED";

	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(ACTION)) {
			Intent i = new Intent(Intent.ACTION_RUN);
			i.setClass(context, MJService.class);
			context.startService(i);
		}
	}

}
