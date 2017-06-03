package com.it51buy.mojian.view;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.it51buy.mojian.R;
import com.it51buy.mojian.activity.AddMJActivity;
import com.it51buy.mojian.adapter.BasePagerAdapter.TabItem;
import com.it51buy.mojian.adapter.ListViewAddMJAdapter;
import com.it51buy.mojian.bean.MJBean;
import com.it51buy.mojian.eventcenter.EC;
import com.it51buy.mojian.eventcenter.EventName;

public class AddSystemView implements TabItem {

	private View mRoot;
	private AddMJActivity mContext;
	private ListView mListView;
	private ListViewAddMJAdapter mAdapter;

	public AddSystemView(AddMJActivity context) {
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
		mAdapter.add(new MJBean(MJBean.SYSTEM_TYPE, "mobile_net", "icon_toggle_gprs"));
		mAdapter.add(new MJBean(MJBean.SYSTEM_TYPE, "hot_spot", "icon_toggle_hotspot"));
		mAdapter.add(new MJBean(MJBean.SYSTEM_TYPE, "wifi", "icon_toggle_wifi"));
		mAdapter.add(new MJBean(MJBean.SYSTEM_TYPE, "bluetooth", "icon_toggle_bluetooth"));
		mAdapter.add(new MJBean(MJBean.SYSTEM_TYPE, "data_synchronization", "icon_toggle_sync"));
		mAdapter.add(new MJBean(MJBean.SYSTEM_TYPE, "fly_mode","icon_toggle_airplane"));
		mAdapter.add(new MJBean(MJBean.SYSTEM_TYPE, "nfc", "icon_toggle_nfc"));
		mAdapter.add(new MJBean(MJBean.SYSTEM_TYPE, "brightness", "icon_toggle_screen_light"));
		mAdapter.add(new MJBean(MJBean.SYSTEM_TYPE, "auto_rotation", "icon_toggle_autorotate"));
		mAdapter.add(new MJBean(MJBean.SYSTEM_TYPE, "full_screen_mode", "icon_toggle_immersive"));
		mAdapter.add(new MJBean(MJBean.SYSTEM_TYPE, "flash_light", "icon_toggle_flash"));
		mAdapter.add(new MJBean(MJBean.SYSTEM_TYPE, "gps", "icon_toggle_gps"));
		mAdapter.add(new MJBean(MJBean.SYSTEM_TYPE, "mute", "icon_toggle_volume_off"));
		mAdapter.add(new MJBean(MJBean.SYSTEM_TYPE, "vibration_ringing", "icon_toggle_volume_vib"));
		mAdapter.add(new MJBean(MJBean.SYSTEM_TYPE, "haptic_feedback", "icon_double_tap"));
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
			MJBean oldItem  = mContext.getMJItem();
			MJBean newItem = mAdapter.getItem(position);
			if (null == oldItem) {
				EC.getInstence().dispatch(EventName.add_function_item_to_home, newItem);
			} else {
				EC.getInstence().dispatch(EventName.replace_function_item_from_home, oldItem, newItem);
			}

			mContext.back();
		}
	};

	@Override
	public View getContentView() {
		return mRoot;
	}

	@Override
	public String getPageTitle() {
		return mContext.getString(R.string.system);
	}

	@Override
	public void onPageSelected() {
	}

}
