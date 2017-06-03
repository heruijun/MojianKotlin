package com.hrj.mojian.eventcenter;

import java.util.HashMap;

public class EC {
    private static EC ec;
    private HashMap<EventName, ECRunnable> ecList = new HashMap<EventName, ECRunnable>();

    public static EC getInstence() {
        if (ec == null) {
            ec = new EC();
            ec.loadEvents();
        }
        return ec;
    }

    public interface ECRunnable {
        public void run(Object[] outValues, Object[] inValues);
    }

    public void regedit(EventName eventName, ECRunnable run) {
        ecList.put(eventName, run);
    }

    public void unRegedit(EventName eventName, ECRunnable run) {
        if (ecList.get(eventName) == null) {
            return;
        }
        ecList.remove(eventName);
    }

    public void dispatch(EventName eventName, Object... inValues) {
        dispatch(null, eventName, inValues);
    }

    public void dispatch(Object[] outValues, EventName eventName, Object... inValues) {
        ECRunnable ecRunnable = ecList.get(eventName);
        if (ecRunnable != null) {
            ecRunnable.run(outValues, inValues);
        }
    }

    private void loadEvents() {
    }

    public void onDestroy() {
        ecList.clear();
        ec = null;
    }
}