package com.hrj.mojian.eventcenter

import com.hrj.mojian.eventcenter.EC.ECRunnable

abstract class ECBase : ECRunnable {
    abstract fun pushEventName(): EventName

    init {
        EC.getInstence().regedit(pushEventName(), this)
    }
}