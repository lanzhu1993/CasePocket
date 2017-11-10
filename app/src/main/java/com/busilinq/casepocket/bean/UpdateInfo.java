package com.busilinq.casepocket.bean;

import cn.bmob.v3.BmobObject;

/**
 * 描述：更新
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/26
 * author: shiquan.lu
 */

public class UpdateInfo extends BmobObject{

    private String oldVersion;
    private String newVersion;
    private String apkUrl;
    private String message;


    public String getOldVersion() {
        return oldVersion;
    }

    public void setOldVersion(String oldVersion) {
        this.oldVersion = oldVersion;
    }

    public String getNewVersion() {
        return newVersion;
    }

    public void setNewVersion(String newVersion) {
        this.newVersion = newVersion;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
