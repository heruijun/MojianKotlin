package com.hrj.mojian.bean

import java.io.Serializable

class MJItem(var mjBean: MJBean?, var isEnable: Boolean) : Serializable {

    companion object {
        private const val serialVersionUID = 1L
    }

}
