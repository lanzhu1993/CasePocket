package com.busilinq.casepocket.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * 描述 ：话题列表
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/11/10
 * author: shiquan.lu
 */

public class EvaluationInfo extends BmobObject {

    private String attachments; // 评论图片列表
    private String content;     // 内容信息
    private String userName;    //用户昵称
    private String evalutionId; //评论id == 当前用户的id
    private String avater;//头像


    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEvalutionId() {
        return evalutionId;
    }

    public void setEvalutionId(String evalutionId) {
        this.evalutionId = evalutionId;
    }

    public String getAvater() {
        return avater;
    }

    public void setAvater(String avater) {
        this.avater = avater;
    }




}
