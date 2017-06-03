package com.it51buy.mojian.adapter;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.it51buy.mojian.R;
import com.it51buy.mojian.bean.MJBean;

public class ListViewAddMJAdapter extends ArrayAdapter<MJBean> {

	private Context mContext;
	private LayoutInflater mInflater;

	public ListViewAddMJAdapter(Context context) {
		super(context, 0);
		mContext = context;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (null == convertView) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.listview_add_mj_item, parent, false);
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.text = (TextView) convertView.findViewById(R.id.text);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		MJBean mjItem = getItem(position);
		refreshView(holder, mjItem);

		return convertView;
	}

	@SuppressWarnings("deprecation")
	private void refreshView(ViewHolder holder, MJBean mjBean) {
		int type = mjBean.getType();

		switch (type) {
		case MJBean.BASE_TYPE:
		case MJBean.SYSTEM_TYPE:
			holder.image.setImageDrawable(mContext.getResources().getDrawable(mjBean.getImageId()));
			holder.text.setText(mContext.getString(mjBean.getNameId()));
			break;

		case MJBean.APP_TYPE:
			try {
				holder.image.setImageDrawable(mContext.getPackageManager().getApplicationIcon(mjBean.getPackageName()));
			} catch (NameNotFoundException e) {
				holder.image.setImageDrawable(new BitmapDrawable());
			}
			holder.text.setText(mjBean.getAppName());
			break;

		case MJBean.CONTACT_TYPE:
			holder.image.setImageBitmap(mjBean.getContact().getBitmap());
			holder.text.setText(mjBean.getContact().getName());
			break;

		default:
			break;
		}
	}

	private static class ViewHolder {
		ImageView image;
		TextView text;
	}

}
