package com.busilinq.casepocket.presenter;

import com.busilinq.casepocket.bean.CaseInfo;
import com.busilinq.casepocket.bean.ImagePath;
import com.busilinq.casepocket.modle.CaseApi;
import com.busilinq.casepocket.viewinterface.IBaseFragmentView;
import com.busilinq.casepocket.viewinterface.ICaseFragmentView;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import rx.Subscription;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/5
 * author: shiquan.lu
 */

public class CaseFragmentPresenter extends BaseFragmentPresenter<ICaseFragmentView> {

    private CaseApi caseApi;

    @Override
    public void attachView(ICaseFragmentView view) {
        super.attachView(view);
        caseApi = CaseApi.getCaseApi();
    }

    public void findCaseInfo(String caseObjectId , FindListener<CaseInfo> listener){
       addSubscription(caseApi.findCaseInfo(caseObjectId,listener));
    }


    public void findImageInfo(String caseObjectId ,FindListener<ImagePath> listener){
        addSubscription(caseApi.findImageInfo(caseObjectId,listener));
    }

}
