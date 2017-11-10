package com.busilinq.casepocket.viewinterface;

import com.busilinq.casepocket.bean.CaseInfo;

import cn.bmob.v3.listener.SaveListener;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/3
 * author: shiquan.lu
 */

public interface IAddCaseView  extends IBaseView{

    void showDateDialog();

    void initCalendar();

    void initRelationData();

    void uploadImage();

    void getDataFromServer();

    void save(CaseInfo caseInfo, String objectId, String hospital, String department, String time, String imagepath, String description, SaveListener<String> listener);

}
