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

    private List<String> attachments; // 评论图片列表
    private String content;                  // 内容信息
    private String creatTime;                // 评论时间
    private String evaluationId;                // 评论id
    private String userName;
    private List<EvaluationReply> evaluatereplys;//回复列表内容

    public List<String> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<String> attachments) {
        this.attachments = attachments;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(String evaluationId) {
        this.evaluationId = evaluationId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<EvaluationReply> getEvaluatereplys() {
        return evaluatereplys;
    }

    public void setEvaluatereplys(List<EvaluationReply> evaluatereplys) {
        this.evaluatereplys = evaluatereplys;
    }

}
