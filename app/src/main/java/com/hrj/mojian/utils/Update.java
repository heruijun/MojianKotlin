package com.hrj.mojian.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;

import com.hrj.mojian.R;

public class Update {

	private static Update mInstance;
	private Dialog mDialog;

	private Update() {

	}

	public static Update getInstance() {
		if (null == mInstance) {
			mInstance = new Update();
		}
		return mInstance;
	}

	public void checkUpdate(Context context) {
		if (!NetUtil.isUsingNetwork(context)) {
			MJToast.show(R.string.no_network, Toast.LENGTH_SHORT);
			return;
		}
		getDialog(context).show();
	}

	private Dialog getDialog(final Context context) {
		if (null == mDialog) {
			mDialog = new AlertDialog.Builder(context, R.style.dialog).setTitle(R.string.update_dialog_message).setPositiveButton(R.string.update_dialog_ok, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					AppUtil.downloadAppBySystem(context, "http://gdown.baidu.com/data/wisegame/91319a5a1dfae322/baidu_16785426.apk");
				}
			}).setNegativeButton(R.string.update_dialog_cancel, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					mDialog = null;
				}
			}).create();
		}
		return mDialog;
	}

}
