package com.busilinq.casepocket.presenter;

import com.busilinq.casepocket.bean.CaseInfo;
import com.busilinq.casepocket.modle.CaseApi;
import com.busilinq.casepocket.viewinterface.IGalleryView;

import cn.bmob.v3.listener.UpdateListener;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/8
 * author: shiquan.lu
 */

public class GalleryPresenter extends BasePresenter<IGalleryView> {


    CaseApi caseApi;

    @Override
    public void attachView(IGalleryView view) {
        super.attachView(view);
        caseApi = CaseApi.getCaseApi();
    }

    public void delete(CaseInfo caseInfo, String caseObjectId, UpdateListener listener){
        addSubscription(caseApi.delete(caseInfo,caseObjectId,listener));
    }

}
