package com.hrj.mojian.view

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout

import com.hrj.mojian.utils.Define

class AlphaRelativeLayout : RelativeLayout {

    private var mAlpha = Define.ENABLEDALPHA

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        loadAttrs(attrs)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        loadAttrs(attrs)
    }

    constructor(context: Context) : super(context) {}

    private fun loadAttrs(attrs: AttributeSet?) {
        if (attrs != null) {
            val enable = attrs.getAttributeBooleanValue(null, "enabled", true)
            isEnabled = enable
        }
    }

    override fun refreshDrawableState() {
        if (isEnabled) {
            setAlpha(if (isPressed) Define.PRESSALPHA else Define.ENABLEDALPHA)
        } else {
            setAlpha(Define.NOTENABLEDALPHA)
        }
        super.refreshDrawableState()
    }

    fun setAlpha(alpha: Int) {
        mAlpha = alpha
        invalidate()
    }

    override fun drawChild(canvas: Canvas, child: View, drawingTime: Long): Boolean {
        canvas.save()
        canvas.saveLayerAlpha(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), mAlpha, Canvas.ALL_SAVE_FLAG)
        val result = super.drawChild(canvas, child, drawingTime)
        canvas.restore()
        return result
    }

}
