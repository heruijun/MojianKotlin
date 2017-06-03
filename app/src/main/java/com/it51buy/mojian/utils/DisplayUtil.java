package com.it51buy.mojian.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

public class DisplayUtil {

    /**
     * 是否横屏
     *
     * @param context
     * @return
     */
    public static boolean isLand(Context context) {
        return Configuration.ORIENTATION_LANDSCAPE == context.getResources().getConfiguration().orientation;
    }

    /**
     * 是否是小米手机
     *
     * @param context
     * @return
     */
    public static boolean isMiPhone(Context context) {
        String str1 = Build.DISPLAY;
        if ((str1 != null) && (str1.toUpperCase().contains("MIUI"))) {
            return true;
        }

        String str2 = Build.MODEL;
        if ((str2 != null) && (str2.toUpperCase().contains("MI-ONE"))) {
            return true;
        }

        String str3 = Build.DEVICE;
        if ((str3 != null) && (str3.toUpperCase().contains("MIONE"))) {
            return true;
        }

        String str4 = Build.MANUFACTURER;
        if ((str4 != null) && (str4.toUpperCase().equalsIgnoreCase("XIAOMI"))) {
            return true;
        }

        String str5 = Build.PRODUCT;
        if ((str5 != null) && (str5.toUpperCase().contains("MIONE"))) {
            return true;
        }

        return false;
    }

}