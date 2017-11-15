package com.busilinq.casepocket.bean;

import cn.bmob.v3.BmobObject;

/**
 * 描述 ：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/11/10
 * author: shiquan.lu
 */

public class EvaluationReply extends BmobObject {

    private String erid;             //回复id
    private String erContent;    //回复内容
    private String erReplyuser;  //回复人姓名
    private String erReplytime;  //回复时间

    public String getErid() {
        return erid;
    }

    public void setErid(String erid) {
        this.erid = erid;
    }

    public String getErContent() {
        return erContent;
    }

    public void setErContent(String erContent) {
        this.erContent = erContent;
    }

    public String getErReplyuser() {
        return erReplyuser;
    }

    public void setErReplyuser(String erReplyuser) {
        this.erReplyuser = erReplyuser;
    }

    public String getErReplytime() {
        return erReplytime;
    }

    public void setErReplytime(String erReplytime) {
        this.erReplytime = erReplytime;
    }
}
