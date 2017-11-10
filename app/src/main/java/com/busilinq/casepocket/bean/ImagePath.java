package com.busilinq.casepocket.bean;

import cn.bmob.v3.BmobObject;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/5
 * author: shiquan.lu
 */

public class ImagePath extends BmobObject {

    private String caseObjectID;
    private String imagePath;

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCaseObjectID() {
        return caseObjectID;
    }

    public void setCaseObjectID(String caseObjectID) {
        this.caseObjectID = caseObjectID;
    }

}
