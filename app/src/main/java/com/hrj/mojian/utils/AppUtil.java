package com.hrj.mojian.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.widget.Toast;

import com.hrj.mojian.R;

import java.io.File;
import java.util.List;

public class AppUtil {
    /**
     * 打开指定程序包名的APP
     *
     * @param packageName
     */
    public static void openApp(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            MJToast.INSTANCE.show(R.string.no_open_app, Toast.LENGTH_SHORT);
            return;
        }

        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(pi.packageName);

        List<ResolveInfo> apps = pm.queryIntentActivities(resolveIntent, 0);
        ResolveInfo ri = apps.iterator().next();
        if (null != ri) {
            packageName = ri.activityInfo.packageName;
            String className = ri.activityInfo.name;

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName(packageName, className);
            intent.setComponent(cn);
            context.startActivity(intent);
        }
    }

    /**
     * 检查软件是否安装是否已安装
     */
    public static boolean hasInstalled(Context context, String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (Exception e) {
            packageInfo = null;
            MJLog.w("AppUtil", "hasInstalled? " + packageName + " not fouond." + e.getMessage());
        }
        return packageInfo != null;
    }

    /**
     * 系统下载
     *
     * @param context
     * @param url
     */
    public static void downloadAppBySystem(Context context, String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent downloadIntent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(downloadIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 安装
     */
    public static void installApk(Context context, String filePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File setupFile = new File(filePath);
        if (!FileUtil.isSDCardMounted()) {
            FileUtil.chmod("777", setupFile.getAbsolutePath());
        }
        intent.setDataAndType(Uri.fromFile(setupFile), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

}