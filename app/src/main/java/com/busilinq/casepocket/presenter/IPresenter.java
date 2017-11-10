package com.busilinq.casepocket.presenter;



import com.busilinq.casepocket.viewinterface.IBaseView;

import rx.Subscription;

public interface IPresenter<V extends IBaseView> {
    void attachView(V view);

    void detachView();

    void cancel();

    void addSubscription(Subscription s);

}
