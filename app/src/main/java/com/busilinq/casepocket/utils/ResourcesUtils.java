package com.busilinq.casepocket.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;

import java.io.InputStream;

/**
 * 描述：统工具类，跟Android系统相关的工具类
 *
 * @author lanzhu1993
 * @version 1.0
 * @date 2015-09-03
 * @since 1.0
 */
public class ResourcesUtils {
    public static final String RES_TYPE_LAYOUT = "layout";
    public static final String RES_TYPE_DRAWABLE = "drawable";
    public static final String RES_TYPE_STRING = "string";
    public static final String RES_TYPE_COLOR = "color";
    public static final String RES_TYPE_ID = "id";
    public static final String RES_TYPE_STYLE = "style";

    private ResourcesUtils() {
    }


    public static int getLayoutResId(Context context, String name) {
        return getResourceID(context, RES_TYPE_LAYOUT, name);
    }

    public static int getDrawableResId(Context context, String name) {
        return getResourceID(context, RES_TYPE_DRAWABLE, name);
    }

    public static int getStringResId(Context context, String name) {
        return getResourceID(context, RES_TYPE_STRING, name);
    }

    public static int getColorResId(Context context, String name) {
        return getResourceID(context, RES_TYPE_COLOR, name);
    }

    public static int getIdResId(Context context, String name) {
        return getResourceID(context, RES_TYPE_ID, name);
    }

    public static int getStyleResId(Context context, String name) {
        return getResourceID(context, RES_TYPE_STYLE, name);
    }

    /**
     * 获取资源Id号
     *
     * @param context 上下文
     * @param type    类型
     * @param name    文件名
     * @return 资源号
     */
    public static int getResourceID(Context context, String type, String name) {
        Resources resource = context.getResources();
        String pkgName = context.getPackageName();
        return resource.getIdentifier(name, type, pkgName);
    }

    /**
     * 获取渐变drawable
     *
     * @return Drawable
     */
    public static Drawable getDrawable(int a, int r, int g, int b) {
        final Drawable drawable = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT, new int[]{
                Color.TRANSPARENT,
                Color.argb(a, r, g, b)
        });
        return drawable;
    }

    /**
     * 通过Id获取视图实例对象
     *
     * @param context 运行时
     * @param resId   资源Id
     * @return View
     */
    public static <T extends View> T findViewById(Context context, int resId) {
        return (T) LayoutInflater.from(context).inflate(resId, null);
    }


    /**
     * 将资源id转换成drawable
     *
     * @param context
     * @param resId
     * @return
     */
    public static Drawable resourceToDrawable(Context context, int resId) {
        return context.getResources().getDrawable(resId);
    }

    /**
     * 将资源id转换成bitmap
     *
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap resourceToBitmap(Context context, int resId) {
        Resources r = context.getResources();
        InputStream is = r.openRawResource(resId);
        BitmapDrawable bmpDraw = new BitmapDrawable(is);
        return bmpDraw.getBitmap();
    }
}
