package com.it51buy.mojian.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;

import com.it51buy.mojian.R;

@Deprecated
public class BaseActivity extends Activity {

	public static final String TAG = BaseActivity.class.getSimpleName();

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return back();
		}
		return super.onKeyDown(keyCode, event);
	}

	protected boolean back() {
		getFragmentManager().popBackStackImmediate();
		if (getFragmentManager().getBackStackEntryCount() == 0) {
			finish();
		}
		return true;
	}

	public void show(String tag, Class<? extends Fragment> clz) {
		show(tag, clz, null);
	}

	public void show(String tag, Class<? extends Fragment> clz, Bundle args) {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment fragment = getFragmentManager().findFragmentByTag(tag);
		try {
			if (null == fragment) {
				fragment = Fragment.instantiate(this, clz.getName(), args);
				ft.add(R.id.root, fragment, tag);
			} else if (fragment.isDetached()) {
				ft.attach(fragment);
			}
		} finally {
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.addToBackStack(null);
			ft.commit();
		}
	}

}
