package com.busilinq.casepocket.bean;

import cn.bmob.v3.BmobObject;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/5
 * author: shiquan.lu
 */

public class CaseInfo extends BmobObject {

    private String caseObjectID;
    private String hospital;
    private String department;
    private String description;
    private String createTime;
    private String imagePath;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getCaseObjectID() {
        return caseObjectID;
    }

    public void setCaseObjectID(String caseObjectID) {
        this.caseObjectID = caseObjectID;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

}
