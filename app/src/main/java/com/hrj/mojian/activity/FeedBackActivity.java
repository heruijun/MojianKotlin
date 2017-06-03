package com.hrj.mojian.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.hrj.mojian.R;

public class FeedBackActivity extends Activity implements OnClickListener, TextWatcher {

	private EditText mInfoEditText;
	private EditText mContactEditText;
	private Button mLimitButton;
	private View mSendView;
	private View mBackView;
	private static final int SIZE = 200;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_back);
		initView();
	}

	private void initView() {
		mInfoEditText = (EditText) findViewById(R.id.info_edit);
		mContactEditText = (EditText) findViewById(R.id.contact_edit);
		mLimitButton = (Button) findViewById(R.id.limit);
		mSendView = findViewById(R.id.send);
		mBackView = findViewById(R.id.back);

		mSendView.setOnClickListener(this);
		mBackView.setOnClickListener(this);
		mInfoEditText.addTextChangedListener(this);
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
		case R.id.send:
			break;

		case R.id.back:
			back();
			break;

		default:
			break;
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}

	@Override
	public void afterTextChanged(Editable s) {
		mLimitButton.setText(String.valueOf(SIZE - s.length()));
	}
}
