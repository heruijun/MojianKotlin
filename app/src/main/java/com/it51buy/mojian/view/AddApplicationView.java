package com.it51buy.mojian.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.it51buy.mojian.R;
import com.it51buy.mojian.activity.AddMJActivity;
import com.it51buy.mojian.adapter.BasePagerAdapter.TabItem;
import com.it51buy.mojian.adapter.ListViewAddMJAdapter;
import com.it51buy.mojian.bean.MJBean;
import com.it51buy.mojian.eventcenter.EC;
import com.it51buy.mojian.eventcenter.EventName;

import java.util.List;

public class AddApplicationView implements TabItem {

	private View mRoot;
	private AddMJActivity mContext;
	private ProgressBar mProgressBar;
	private ListView mListView;
	private ListViewAddMJAdapter mAdapter;

	public AddApplicationView(AddMJActivity context) {
		mContext = context;
		initAdapter();
		initRootView();
		loadData();
	}

	private void initAdapter() {
		mAdapter = new ListViewAddMJAdapter(mContext);
		mAdapter.setNotifyOnChange(false);
	}

	private void initRootView() {
		if (null != mRoot) {
			return;
		}
		mRoot = LayoutInflater.from(mContext).inflate(R.layout.listview_add_mj, null);
		mProgressBar = (ProgressBar) mRoot.findViewById(R.id.application_progressbar);
		mListView = (ListView) mRoot.findViewById(R.id.application_list);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(mItemClickListener);
	}

	private OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			MJBean oldItem = mContext.getMJItem();
			MJBean newItem = mAdapter.getItem(position);
			if (null == oldItem) {
				EC.getInstence().dispatch(EventName.add_function_item_to_home, newItem);
			} else {
				EC.getInstence().dispatch(EventName.replace_function_item_from_home, oldItem, newItem);
			}

			mContext.back();
		}
	};

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
			packageManager = mContext.getPackageManager();
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

					MJBean mjItem = new MJBean(MJBean.APP_TYPE, appName, packageName);
					mAdapter.add(mjItem);
				}
			}

			mAdapter.notifyDataSetChanged();
			mProgressBar.setVisibility(View.GONE);
		};

	};

	@Override
	public View getContentView() {
		return mRoot;
	}

	@Override
	public String getPageTitle() {
		return mContext.getString(R.string.application);
	}

	@Override
	public void onPageSelected() {
		loadData();
	}

}
