package com.busilinq.casepocket.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;


import com.busilinq.casepocket.R;

import java.util.List;

public class ToActivityUtil {
	
	  /**
     * 
     * @Description: 隐式启动,跳转
     * @param packageContext
     * @param action
     *            含操作的Intent
     */
    public static void startActivityIntentSafe(Context packageContext,
            Intent action) {
        // Verify it resolves
        PackageManager packageManager = packageContext.getPackageManager();
        List activities = packageManager.queryIntentActivities(action,
                PackageManager.MATCH_DEFAULT_ONLY);
        boolean isIntentSafe = activities.size() > 0;
 
        // Start an activity if it's safe
        if (isIntentSafe) {
            packageContext.startActivity(action);
        }
 
    }
 
    /**
     * @Description: 跳转
     * @param packageContext
     *            from,一般传XXXActivity.this
     * @param cls
     *            to,一般传XXXActivity.class
     */
    public static void toNextActivity(Activity packageContext, Class<?> cls) {
        Intent i = new Intent(packageContext, cls);
        packageContext.startActivity(i);
        packageContext.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }
 
    /**
     * @Description: 跳转,带参数的方法;需要其它的数据类型,再继续重载吧
     * @param packageContext
     * @param cls
     * @param keyvalues  需要传进去的String参数{{key1,values},{key2,value2}...}
     */
    public static void toNextActivity(Context packageContext, Class<?> cls,
            String[][] keyvalues) {
        Intent i = new Intent(packageContext, cls);
        for (String[] strings : keyvalues) {
            i.putExtra(strings[0], strings[1]);
        }
        packageContext.startActivity(i);
    }
 
    public static void toNextActivityAndFinish(Context packageContext,
            Class<?> cls) {
        Intent i = new Intent(packageContext, cls);
        packageContext.startActivity(i);
 
        ((Activity) packageContext).finish();
    }
 
    public static void finish(Activity activity) {
        activity.finish();
    }
    
    
    public static void showActivity(Activity activity ,Class<?> cls,boolean isFinish){
    	Intent i = new Intent(activity,cls);
    	toNextActivity(activity,cls);
    	if(isFinish){
    		finish(activity);
    	}
    }

    public static void showDataActivity(Activity activity , Class<?> cls, boolean isFinish, Bundle bundle){
        Intent i = new Intent(activity,cls);
        i.putExtras(bundle);
        activity.startActivity(i);
        activity.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        if(isFinish){
            finish(activity);
        }
    }

}
