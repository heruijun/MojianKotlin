package com.it51buy.mojian.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import com.it51buy.mojian.App;
import com.it51buy.mojian.AppPreference;
import com.it51buy.mojian.R;
import com.it51buy.mojian.utils.Define;
import com.it51buy.mojian.utils.Update;

public class SettingActivity extends Activity implements OnClickListener, OnCheckedChangeListener {

	private View mBack;
	private ToggleButton mKeyVibration;
	private ToggleButton mTurnoverInvalid;
	private ToggleButton mNotification;
	private View mApplicationInvalid;
	private View mFunctionCheck;
	private View mCheckUpdate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initView();
	}

	private void initView() {
		mKeyVibration = (ToggleButton) findViewById(R.id.key_vibration_toggle);
		mTurnoverInvalid = (ToggleButton) findViewById(R.id.turnover_invalid_toggle);
		mNotification = (ToggleButton) findViewById(R.id.notification_toggle);

		mKeyVibration.setChecked(AppPreference.getBoolean(Define.PREFERENCES_KEY_VIBRATION, true));
		mTurnoverInvalid.setChecked(AppPreference.getBoolean(Define.PREFERENCES_TURNOVER_INVALID, true));
		mNotification.setChecked(AppPreference.getBoolean(Define.PREFERENCES_NOTIFICATION, true));

		mApplicationInvalid = findViewById(R.id.application_invalid_view);
		mFunctionCheck = findViewById(R.id.function_check_view);
		mCheckUpdate = findViewById(R.id.check_update_view);
		mBack = findViewById(R.id.back);

		mKeyVibration.setOnCheckedChangeListener(this);
		mTurnoverInvalid.setOnCheckedChangeListener(this);
		mNotification.setOnCheckedChangeListener(this);

		mApplicationInvalid.setOnClickListener(this);
		mFunctionCheck.setOnClickListener(this);
		mCheckUpdate.setOnClickListener(this);
		mBack.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		back();
	}

	private void back() {
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.check_update_view:
			Update.getInstance().checkUpdate(this);
			break;

		case R.id.application_invalid_view:
			Intent intent = new Intent(this, InvalidAppActivity.class);
			startActivity(intent);
			break;

		case R.id.back:
			back();
			break;

		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.key_vibration_toggle:
			AppPreference.setBoolean(Define.PREFERENCES_KEY_VIBRATION, mKeyVibration.isChecked());
			App.getInstance().setKeyVibrationToggle(mKeyVibration.isChecked());
			break;

		case R.id.turnover_invalid_toggle:
			AppPreference.setBoolean(Define.PREFERENCES_TURNOVER_INVALID, mTurnoverInvalid.isChecked());
			App.getInstance().setTurnoverInvalidToggle(mTurnoverInvalid.isChecked());
			break;

		case R.id.notification_toggle:
			AppPreference.setBoolean(Define.PREFERENCES_NOTIFICATION, mNotification.isChecked());
			App.getInstance().setNotificationToggle(mNotification.isChecked());
			break;

		default:
			break;
		}
	}

}
