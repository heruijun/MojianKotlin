package com.hrj.mojian.view;

import android.content.Intent;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.hrj.mojian.R;
import com.hrj.mojian.activity.AddMJActivity;
import com.hrj.mojian.adapter.BasePagerAdapter.TabItem;
import com.hrj.mojian.adapter.ListViewAddMJAdapter;
import com.hrj.mojian.bean.MJBean;
import com.hrj.mojian.utils.Define;

public class AddAdvancedView implements TabItem {

	private View mRoot;
	private AddMJActivity mContext;
	private ListView mListView;
	private ListViewAddMJAdapter mAdapter;

	public AddAdvancedView(AddMJActivity context) {
		mContext = context;
		initAdapter();
		initData();
		initRootView();
	}

	private void initAdapter() {
		mAdapter = new ListViewAddMJAdapter(mContext);
		mAdapter.setNotifyOnChange(false);
	}

	private void initData() {
		mAdapter.add(new MJBean(MJBean.SYSTEM_TYPE, "call_phone", "icon_ring"));
		mAdapter.add(new MJBean(MJBean.SYSTEM_TYPE, "sms", "icon_rotation_land"));
	}

	private void initRootView() {
		if (null != mRoot) {
			return;
		}
		mRoot = LayoutInflater.from(mContext).inflate(R.layout.listview_add_mj, null);
		mListView = (ListView) mRoot.findViewById(R.id.application_list);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(mItemClickListener);
	}

	private OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			MJBean mjBean = mAdapter.getItem(position);
			if (mjBean.getNameId() == R.string.call_phone) {
				Intent intent = new Intent(Intent.ACTION_PICK, Phone.CONTENT_URI);
				mContext.startActivityForResult(intent, Define.REQUEST_BY_PHONE);
			} else {
				Intent intent = new Intent(Intent.ACTION_PICK, Phone.CONTENT_URI);
				mContext.startActivityForResult(intent, Define.REQUEST_BY_SMS);
			}
		}
	};

	@Override
	public View getContentView() {
		return mRoot;
	}

	@Override
	public String getPageTitle() {
		return mContext.getString(R.string.advanced);
	}

	@Override
	public void onPageSelected() {
	}

}
