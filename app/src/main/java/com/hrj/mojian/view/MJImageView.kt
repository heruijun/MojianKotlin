package com.hrj.mojian.view

import android.content.ClipData
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.View.OnLongClickListener

import com.hrj.mojian.eventcenter.EC
import com.hrj.mojian.eventcenter.EventName
import com.hrj.mojian.logic.MJDragLogic
import com.hrj.mojian.utils.Define

class MJImageView : android.support.v7.widget.AppCompatImageView, OnLongClickListener {

    private var mType: String? = null

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    private fun init() {
        mType = tag as String
        setOnDragListener(MJDragLogic(this))
        setOnLongClickListener(this)
    }

    override fun onLongClick(v: View): Boolean {
        if (!canDrag()) {
            return true
        }

        EC.getInstence().dispatch(EventName.update_operation_bar_view, true, false, false, true)

        val data = ClipData.newPlainText(Define.FROM_MJ, mType.toString())
        val mDragInProgress = v.startDrag(data, View.DragShadowBuilder(v), v, 0)

        if (!mDragInProgress) {
            EC.getInstence().dispatch(EventName.update_operation_bar_view, false, false, false, false)
        }

        return true
    }

    private fun canDrag(): Boolean {
        val out = arrayOf<Boolean>(false)
        EC.getInstence().dispatch(out, EventName.mj_is_setted, Integer.parseInt(mType))
        return out[0]
    }


}
