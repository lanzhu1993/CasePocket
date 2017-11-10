package com.busilinq.casepocket.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;


/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/3/23
 * author: shiquan.lu
 */

public class AppInfoUtils {

    private AppInfoUtils() {
    }

    /**
     * 获取应用名称
     *
     * @param context 上下文
     * @return
     */
    public static String getAppName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        String appName = (String) context.getPackageManager().getApplicationLabel(applicationInfo);
        return appName;
    }

    /**
     * 安装包信息
     *
     * @param context 上下文
     * @return packageInfo
     */
    public static PackageInfo getPackageInfo(Context context) {
        PackageInfo packageInfo = null;
        String packageName = context.getPackageName();
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return packageInfo;
        }
        return packageInfo;
    }

    /**
     * 获取app版本名
     *
     * @param context 上下文
     * @return String
     */
    public static String getVersionName(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            return packageInfo.versionName;
        }
        return "";
    }

    /**
     * 获取版本号
     *
     * @return 版本号大于零
     */
    public static int getVersionCode(Context context) {
        PackageInfo packageInfo = getPackageInfo(context);
        if (packageInfo != null) {
            return packageInfo.versionCode;
        }
        return 1;
    }

    /**
     * 启动app
     *
     * @param context
     * @param packageName
     */
    public static void startApp(Context context, String packageName) {
        Intent intent = new Intent();
        PackageManager packageManager = context.getPackageManager();
        intent = packageManager.getLaunchIntentForPackage(packageName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
