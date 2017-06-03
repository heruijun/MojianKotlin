package com.hrj.mojian.adapter;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.hrj.mojian.AppPreference;
import com.hrj.mojian.R;
import com.hrj.mojian.bean.MJBean;

public class ListViewInvalidAppAdapter extends ArrayAdapter<MJBean> {

	private Context mContext;
	private LayoutInflater mInflater;

	public ListViewInvalidAppAdapter(Context context) {
		super(context, 0);
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (null == convertView) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.listview_invalid_app_item, parent, false);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.text = (TextView) convertView.findViewById(R.id.text);
			holder.toggle = (ToggleButton) convertView.findViewById(R.id.toggle);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final MJBean mjBean = getItem(position);
		refreshView(holder, mjBean);

		holder.toggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				AppPreference.setBoolean(mjBean.getPackageName(), isChecked);
			}
		});

		return convertView;
	}

	@SuppressWarnings("deprecation")
	private void refreshView(ViewHolder holder, MJBean mjBean) {
		try {
			holder.image.setImageDrawable(mContext.getPackageManager().getApplicationIcon(mjBean.getPackageName()));
		} catch (NameNotFoundException e) {
			holder.image.setImageDrawable(new BitmapDrawable());
		}
		holder.text.setText(mjBean.getAppName());
		holder.toggle.setChecked(mjBean.isUsable());
	}

	private static class ViewHolder {
		ImageView image;
		TextView text;
		ToggleButton toggle;
	}

}
