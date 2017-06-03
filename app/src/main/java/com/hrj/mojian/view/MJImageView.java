package com.hrj.mojian.view;

import android.content.ClipData;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnLongClickListener;

import com.hrj.mojian.eventcenter.EC;
import com.hrj.mojian.eventcenter.EventName;
import com.hrj.mojian.logic.MJDragLogic;
import com.hrj.mojian.utils.Define;

public class MJImageView extends android.support.v7.widget.AppCompatImageView implements OnLongClickListener {

    private String mType;

    public MJImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public MJImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MJImageView(Context context) {
        super(context);
        init();
    }

    private void init() {
        mType = (String) getTag();
        setOnDragListener(new MJDragLogic(this));
        setOnLongClickListener(this);
    }

    @Override
    public boolean onLongClick(View v) {
        if (!canDrag()) {
            return true;
        }

        EC.getInstence().dispatch(EventName.update_operation_bar_view, true, false, false, true);

        ClipData data = ClipData.newPlainText(Define.FROM_MJ, String.valueOf(mType));
        boolean mDragInProgress = v.startDrag(data, new View.DragShadowBuilder(v), v, 0);

        if (!mDragInProgress) {
            EC.getInstence().dispatch(EventName.update_operation_bar_view, false, false, false, false);
        }

        return true;
    }

    private boolean canDrag() {
        Boolean[] out = new Boolean[]{null};
        EC.getInstence().dispatch(out, EventName.mj_is_setted, Integer.parseInt(mType));
        return out[0];
    }


}
