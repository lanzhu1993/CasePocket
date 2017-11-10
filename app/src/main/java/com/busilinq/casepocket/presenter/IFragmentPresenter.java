package com.busilinq.casepocket.presenter;


import com.busilinq.casepocket.viewinterface.IBaseFragmentView;

import rx.Subscription;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/4/5
 * author: shiquan.lu
 */

public interface IFragmentPresenter<V extends IBaseFragmentView> {

    void attachView(V view);

    void detachView();

    void cancel();

    void addSubscription(Subscription s);
}
