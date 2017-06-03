package com.it51buy.mojian.eventcenter;

import com.it51buy.mojian.eventcenter.EC.ECRunnable;

public abstract class ECBase implements ECRunnable {
    public abstract EventName pushEventName();

    public ECBase() {
        EC.getInstence().regedit(pushEventName(), this);
    }
}