package com.it51buy.mojian.msg;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.it51buy.mojian.App;

/**
 * 接受和发送消息的类需要继承此类
 *
 * @author XiongWen
 */
public abstract class AppMessage {

    /**
     * 消息Key
     */
    public static final String KEY_FRAGMENT_TAG = "KEY_FRAGMENT_TAG";

    /**
     * 需要处理的事件Key
     */
    public static final String ACTION_TYPE = "ACTION_TYPE";


    public AppMessage() {
        subscribe();
    }

    /**
     * 订阅消息
     */
    protected abstract void subscribe();

    /**
     * 处理消息
     *
     * @param bundle
     */
    protected abstract void applyDataChange(Bundle bundle);

    /**
     * 关心消息类型
     *
     * @param msgType
     */
    protected final void careAboutMsgType(String... msgType) {
        if (null == msgType || 0 == msgType.length) {
            return;
        }

        IntentFilter intentFilter = new IntentFilter();
        for (String msg : msgType) {
            intentFilter.addAction(msg);
        }

        LocalBroadcastManager.getInstance(App.getInstance()).registerReceiver(mReceiver, intentFilter);
    }

    /**
     * 发送消息
     *
     * @param bundle
     */
    public static final void sendMsg(Bundle bundle) {
        Intent intent = new Intent();
        intent.putExtra(KEY_FRAGMENT_TAG, bundle);
        LocalBroadcastManager.getInstance(App.getInstance()).sendBroadcast(intent);
    }

    /**
     * 接受消息的广播
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getBundleExtra(KEY_FRAGMENT_TAG);
            applyDataChange(bundle);
        }
    };

}