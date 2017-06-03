package com.it51buy.mojian.view;

import android.content.ClipData;
import android.content.Context;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;

import com.it51buy.mojian.R;
import com.it51buy.mojian.bean.MJBean;
import com.it51buy.mojian.eventcenter.EC;
import com.it51buy.mojian.eventcenter.EventName;
import com.it51buy.mojian.utils.Define;

public class DotImageView extends ImageView implements OnLongClickListener, OnDragListener {

	public DotImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public DotImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public DotImageView(Context context) {
		super(context);
		init();
	}

	private void init() {
		setOnLongClickListener(this);
		setOnDragListener(this);
	}

	@Override
	public boolean onLongClick(View v) {
		MJBean mjItem = (MJBean) v.getTag();
		
		if(mjItem.getNameId() == R.string.add_function){
			return true;
		}
		
		int type = mjItem.getType();

		boolean isBaseType = false;
		if (MJBean.BASE_TYPE == type) {
			EC.getInstence().dispatch(EventName.update_operation_bar_view, true, true, false, false);
			isBaseType = true;
		}

		ClipData data = ClipData.newPlainText(Define.FROM_OTHER, String.valueOf(mjItem.hashCode()));
		boolean mDragInProgress = v.startDrag(data, new View.DragShadowBuilder(v), v, 0);

		if (mDragInProgress) {
			if (!isBaseType) {
				EC.getInstence().dispatch(EventName.update_operation_bar_view, true, false, true, true);
			}
		} else {
			EC.getInstence().dispatch(EventName.update_operation_bar_view, false, false, false, false);
		}
		return true;
	}

	@Override
	public boolean onDrag(View v, DragEvent event) {
		// Only worry about drag events if this is us being dragged
		if (event.getLocalState() != this) {
			return false;
		}

		boolean result = true;

		switch (event.getAction()) {
		case DragEvent.ACTION_DRAG_STARTED:
			break;
		case DragEvent.ACTION_DRAG_LOCATION:
			break;
		case DragEvent.ACTION_DRAG_ENTERED:
			break;
		case DragEvent.ACTION_DRAG_EXITED:
			break;
		case DragEvent.ACTION_DROP:
			break;
		case DragEvent.ACTION_DRAG_ENDED:
			EC.getInstence().dispatch(EventName.update_operation_bar_view, false, false, false, false);
			break;
		default:
			result = false;
			break;
		}
		return result;
	}

}
