package com.busilinq.casepocket.presenter;

import com.busilinq.casepocket.bean.EvaluationInfo;
import com.busilinq.casepocket.modle.ActiveApi;
import com.busilinq.casepocket.viewinterface.IActiveFragmentView;

import cn.bmob.v3.listener.SQLQueryListener;
import rx.Subscription;

/**
 * 描述：
 * <p>
 * 创建时间： 2017/11/22  10:49
 * author :shiquan.lu
 * email: 1113799552@qq.com
 */

public class ActiveFragmentPresenter extends BaseFragmentPresenter<IActiveFragmentView> {

    ActiveApi activeApi;

    @Override
    public void attachView(IActiveFragmentView view) {
        super.attachView(view);
        activeApi = ActiveApi.getInstance();
    }

    public void queryEvaluationInfo(int begin, int limit, SQLQueryListener<EvaluationInfo> listener){
        Subscription subscription = activeApi.queryEvaluationInfo(begin, limit, listener);
        addSubscription(subscription);
    }
}
