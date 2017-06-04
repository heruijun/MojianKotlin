package com.hrj.mojian.bean

import android.graphics.Bitmap
import android.graphics.BitmapFactory

import com.hrj.mojian.utils.Define
import com.hrj.mojian.utils.Platform

import java.io.Serializable

class MJContact(var name: String?, var number: String?, private var fileName: String?, var type: Int) : Serializable {

    fun setFileName(fileName: String) {
        this.fileName = fileName
    }

    val bitmap: Bitmap
        get() {
            var bitmap: Bitmap? = BitmapFactory.decodeFile(fileName)
            if (null == bitmap) {
                if (Define.REQUEST_BY_PHONE == type) {
                    bitmap = BitmapFactory.decodeResource(Platform.getResources(), Platform.getDrawableId("icon_ring"))
                } else {
                    bitmap = BitmapFactory.decodeResource(Platform.getResources(), Platform.getDrawableId("icon_rotation_land"))
                }
            }
            return bitmap!!
        }

    companion object {
        private const val serialVersionUID = 1L
    }

}
