package com.it51buy.mojian.logic;

import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;

import com.it51buy.mojian.R;
import com.it51buy.mojian.activity.AddMJActivity;
import com.it51buy.mojian.bean.MJBean;
import com.it51buy.mojian.bean.MJItem;
import com.it51buy.mojian.eventcenter.EC;
import com.it51buy.mojian.eventcenter.EventName;
import com.it51buy.mojian.utils.Define;
import com.it51buy.mojian.utils.MJLog;

public class MJDragLogic implements OnDragListener {

	private static final String TAG = MJDragLogic.class.getSimpleName();
	protected String mType = null;
	private boolean isEntered = false;
	private String mDragLabel = null;
	private String mDragText = null;
	private View mView;

	public MJDragLogic(View view) {
		mView = view;
		mType = (String) view.getTag();
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onDrag(View v, DragEvent event) {
		int action = event.getAction();
		boolean result = true;
		switch (action) {
		case DragEvent.ACTION_DRAG_STARTED:
			MJLog.d(TAG, "drag started in dropTarget");
			isEntered = false;
			break;

		case DragEvent.ACTION_DRAG_ENTERED:
			MJLog.d(TAG, "drag entered dropTarget");
			isEntered = true;
			if (getString(R.string.switching).equals(mType)) {
				mView.setBackgroundDrawable(mView.getResources().getDrawable(R.drawable.home_switch_drag_bg));
			} else if (getString(R.string.remove).equals(mType)) {
				mView.setBackgroundDrawable(mView.getResources().getDrawable(R.drawable.home_remove_drag_bg));
			} else if (getString(R.string.mj_1).equals(mType) || getString(R.string.mj_2).equals(mType) || getString(R.string.mj_3).equals(mType) || getString(R.string.mj_4).equals(mType)) {
				if (mView != event.getLocalState()) {
					((ViewGroup) mView.getParent()).animate().scaleX(1.3f);
					((ViewGroup) mView.getParent()).animate().scaleY(1.3f);
				}
			}
			break;

		case DragEvent.ACTION_DRAG_EXITED:
			MJLog.d(TAG, "drag exited dropTarget");
			isEntered = false;
			if (getString(R.string.switching).equals(mType) || getString(R.string.remove).equals(mType)) {
				mView.setBackgroundDrawable(null);
			} else if (getString(R.string.mj_1).equals(mType) || getString(R.string.mj_2).equals(mType) || getString(R.string.mj_3).equals(mType) || getString(R.string.mj_4).equals(mType)) {
				((ViewGroup) mView.getParent()).animate().scaleX(1f);
				((ViewGroup) mView.getParent()).animate().scaleY(1f);
			}
			break;

		case DragEvent.ACTION_DRAG_LOCATION:
			MJLog.d(TAG, "drag proceeding in dropTarget: " + event.getX() + ", " + event.getY());
			break;

		case DragEvent.ACTION_DROP:
			MJLog.d(TAG, "drag drop in dropTarget");
			mDragLabel = event.getClipDescription().getLabel().toString();
			mDragText = event.getClipData().getItemAt(0).getText().toString();
			MJLog.d(TAG, "Item Label is " + mDragLabel);
			MJLog.d(TAG, "Item text is " + mDragText);
			if (getString(R.string.switching).equals(mType) || getString(R.string.remove).equals(mType)) {
				mView.setBackgroundDrawable(null);
			} else if (getString(R.string.mj_1).equals(mType) || getString(R.string.mj_2).equals(mType) || getString(R.string.mj_3).equals(mType) || getString(R.string.mj_4).equals(mType)) {
				((ViewGroup) mView.getParent()).animate().scaleX(1f);
				((ViewGroup) mView.getParent()).animate().scaleY(1f);
			}
			break;

		case DragEvent.ACTION_DRAG_ENDED:
			MJLog.d(TAG, "drag ended in dropTarget");
			doOnEnded();
			break;

		default:
			MJLog.d(TAG, "other action in dropzone: " + action);
			result = false;
			break;
		}
		return result;
	}

	protected void doOnEnded() {
		EC.getInstence().dispatch(EventName.update_operation_bar_view, false, false, false, false);

		if (null == mType || !isEntered) {
			return;
		}

		MJLog.d(TAG, mType + " action drag result: " + isEntered);

		if (getString(R.string.remove).equals(mType)) {
			handleByRemove();
		} else if (getString(R.string.switching).equals(mType)) {
			handleBySwitching();
		} else {
			handleByMJ();
		}
	}

	private void handleByRemove() {
		if (mDragLabel.equals(Define.FROM_OTHER)) {
			MJBean[] mjItems = new MJBean[] { null };
			EC.getInstence().dispatch(mjItems, EventName.find_function_item_by_hashcode, Integer.parseInt(mDragText));
			EC.getInstence().dispatch(EventName.remove_function_item_from_home, mjItems[0]);
		} else {
			EC.getInstence().dispatch(EventName.mj_remove, Integer.parseInt(mDragText));
		}
	}

	private void handleBySwitching() {
		if (!mDragLabel.equals(Define.FROM_OTHER)) {
			return;
		}

		MJBean[] mjItems = new MJBean[] { null };
		EC.getInstence().dispatch(mjItems, EventName.find_function_item_by_hashcode, Integer.parseInt(mDragText));

		Bundle bundle = new Bundle();
		bundle.putInt(Define.BUNDLE_KEY_REPLACE_MJITEM, mjItems[0].hashCode());
		Intent intent = new Intent(mView.getContext(), AddMJActivity.class);
		intent.putExtras(bundle);
		mView.getContext().startActivity(intent);
	}

	private void handleByMJ() {
		if (mDragLabel.equals(Define.FROM_MJ)) {
			EC.getInstence().dispatch(EventName.mj_replace, Integer.parseInt(mDragText), Integer.parseInt(mType));
		} else {
			MJBean[] mjItems = new MJBean[] { null };
			EC.getInstence().dispatch(mjItems, EventName.find_function_item_by_hashcode, Integer.parseInt(mDragText));
			EC.getInstence().dispatch(EventName.mj_set, Integer.parseInt(mType), new MJItem(mjItems[0], true));
		}
	}

	private String getString(int stringId) {
		return mView.getContext().getString(stringId);
	}

}
