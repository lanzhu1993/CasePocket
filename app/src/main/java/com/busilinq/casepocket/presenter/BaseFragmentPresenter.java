package com.busilinq.casepocket.presenter;

import android.content.Context;
import android.content.res.Resources;

import com.busilinq.casepocket.viewinterface.IBaseFragmentView;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/4/5
 * author: shiquan.lu
 */

public class BaseFragmentPresenter<V extends IBaseFragmentView> implements IFragmentPresenter<V>{
    protected Context mContext;
    protected Resources mRes;
    protected V mBaseView;
    protected CompositeSubscription mCompositeSubscription;
    private String mFunCode;

    @Override
    public void attachView(V view) {
        this.mBaseView = view;
        this.mContext = view.getActivity();
        this.mRes = mContext.getResources();
    }

    @Override
    public void detachView() {
        if(null != mBaseView)
            mBaseView = null;
    }

    @Override
    public void cancel() {
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }

    public boolean isAttached(){
        return mBaseView != null ? true : false;
    }

}
