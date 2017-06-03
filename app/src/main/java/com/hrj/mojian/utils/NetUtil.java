package com.hrj.mojian.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.Build;

public class NetUtil {

    public static boolean isUsingNetwork(Context context) {
        boolean isUsingNetwork = false;
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            isUsingNetwork = isWifiConnected(context);
        }
        if (isMobileConnected(context)) {
            isUsingNetwork = true;
        }
        if (Build.VERSION.SDK_INT >= 13) {
            if (!isUsingNetwork && isEthernetConnected(context)) {
                isUsingNetwork = true;
            }
        }
        return isUsingNetwork;
    }

    /**
     * 检查wifi
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnected(Context context) {
        State state = getNetworkState(context, ConnectivityManager.TYPE_WIFI);
        return state == State.CONNECTED || state == State.CONNECTING;
    }

    /**
     * 检查移动网络
     *
     * @param context
     * @return
     */
    public static boolean isMobileConnected(Context context) {
        State state = getNetworkState(context, ConnectivityManager.TYPE_MOBILE);
        return state == State.CONNECTED || state == State.CONNECTING;
    }

    /**
     * 检查有线网络
     *
     * @param context
     * @return
     */
    public static boolean isEthernetConnected(Context context) {
        // API Level 13以上才支持ConnectivityManager.TYPE_ETHERNET = 9
        State state = getNetworkState(context, 9);
        return state == State.CONNECTED || state == State.CONNECTING;
    }

    public static State getNetworkState(Context context, int networkType) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getNetworkInfo(networkType);
        return info == null ? null : info.getState();
    }

}