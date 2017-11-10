package com.busilinq.casepocket.base;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;

import com.busilinq.casepocket.utils.AES;
import com.zhy.autolayout.config.AutoLayoutConifg;

import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.security.NoSuchAlgorithmException;

import cn.bmob.v3.Bmob;


public class BaseApplication extends Application {

    private static BaseApplication sInstance;

    public static String APPID ="b0b8396f9406010d28c2a93339d0bdb8";

    /** 图片存放根目录*/
    private final String mImageRootDir = Environment
            .getExternalStorageDirectory().getAbsolutePath() +"/CasePocket";
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        Bmob.initialize(sInstance,APPID);
        LitePal.initialize(this);
        createDestImageDir();
        AutoLayoutConifg.getInstance().useDeviceSize();
        try {
            AES.INSTANCE.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    /**
     * 创建图片存储路径
     */
    private void createDestImageDir() {
        File destDirFile = new File(mImageRootDir);
        if(!destDirFile.exists()){
            destDirFile.mkdirs();
        }
    }

    /** 获取Application */
    public static BaseApplication getInstance() {
        return sInstance;
    }

    /**
     * 获取当前运行的进程名
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
