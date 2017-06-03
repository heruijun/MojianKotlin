package com.hrj.mojian.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.hrj.mojian.AppPreference;
import com.hrj.mojian.R;
import com.hrj.mojian.adapter.ListViewInvalidAppAdapter;
import com.hrj.mojian.bean.MJBean;

import java.util.List;

public class InvalidAppActivity extends Activity implements OnClickListener {

	private ListView mListView;
	private ProgressBar mProgressBar;
	private View mBackView;
	private ListViewInvalidAppAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invalid_app);
		initAdapter();
		initView();
		loadData();
	}

	private void initAdapter() {
		mAdapter = new ListViewInvalidAppAdapter(this);
		mAdapter.setNotifyOnChange(false);
	}

	private void initView() {
		mListView = (ListView) findViewById(R.id.listview);
		mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
		mBackView = findViewById(R.id.top_bar);

		mListView.setAdapter(mAdapter);
		mBackView.setOnClickListener(this);
	}

	private void loadData() {
		if (mAdapter.getCount() > 0) {
			return;
		}

		if (mAsyncTask.getStatus() == Status.RUNNING) {
			return;
		}

		mAsyncTask.execute();
	}

	private AsyncTask<Void, Void, List<ResolveInfo>> mAsyncTask = new AsyncTask<Void, Void, List<ResolveInfo>>() {

		PackageManager packageManager;

		@Override
		protected void onPreExecute() {
			mProgressBar.setVisibility(View.VISIBLE);
		};

		@Override
		protected List<ResolveInfo> doInBackground(Void... params) {
			packageManager = getPackageManager();
			final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
			mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
			final List<ResolveInfo> apps = packageManager.queryIntentActivities(mainIntent, 0);
			return apps;
		}

		@Override
		protected void onPostExecute(List<ResolveInfo> result) {
			if (null != result) {
				for (ResolveInfo info : result) {
					String appName = info.loadLabel(packageManager).toString();
					int packageIcon = info.getIconResource();
					String packageName = info.activityInfo.packageName;

					if (appName == null || appName.equals("") || packageName == null || packageName.equals("") || packageIcon == 0) {
						continue;
					}

					boolean isUsable = AppPreference.getBoolean(packageName, true);
					MJBean mjItem = new MJBean(MJBean.APP_TYPE, appName, packageName, isUsable);
					mAdapter.add(mjItem);
				}
			}

			mAdapter.notifyDataSetChanged();
			mProgressBar.setVisibility(View.GONE);
		};

	};

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	private void back() {
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.top_bar:
			back();
			break;

		default:
			break;
		}
	}
}
