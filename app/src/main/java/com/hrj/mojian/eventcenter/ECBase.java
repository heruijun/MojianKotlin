package com.hrj.mojian.eventcenter;

import com.hrj.mojian.eventcenter.EC.ECRunnable;

public abstract class ECBase implements ECRunnable {
    public abstract EventName pushEventName();

    public ECBase() {
        EC.getInstence().regedit(pushEventName(), this);
    }
}