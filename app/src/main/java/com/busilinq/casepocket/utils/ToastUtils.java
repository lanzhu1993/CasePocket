package com.busilinq.casepocket.utils;

import android.content.Context;
import android.widget.Toast;

import com.busilinq.casepocket.base.BaseApplication;


/**
 * 描述：吐司工具类
 * <p/>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/1/9
 * author: shiquan.lu
 */
public class ToastUtils {
    /**
     * short duration
     * the default value
     */
    public static final int TIME_SHORT = Toast.LENGTH_SHORT;
    /**
     * long duration
     */
    public static final int TIME_LONG = Toast.LENGTH_LONG;

    private static Toast sToast;
    private static Context sContext = BaseApplication.getInstance();

    /**
     * show toast
     *
     * @param resId content value's resource id
     */
    public static void showToast(int resId) {
        showToast(sContext.getResources().getString(resId));
    }

    /**
     * show toast
     *
     * @param text content value's string
     */
    public static void showToast(String text) {
        showToast(text, TIME_SHORT);
    }

    /**
     * show toast
     *
     * @param resId    content value's resource id
     * @param duration how long the toast should be show
     */
    public static void showToast(int resId, int duration) {
        showToast(sContext.getResources().getString(resId), duration);
    }

    /**
     * show toast
     *
     * @param text     content value's string
     * @param duration how long the toast should be show
     */
    public static void showToast(String text, int duration) {
        if (sToast == null) {
            sToast = Toast.makeText(sContext, text, duration);
        } else {
            sToast.setText(text);
        }
        sToast.show();
    }
}
