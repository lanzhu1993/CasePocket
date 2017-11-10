package com.busilinq.casepocket.presenter;

import android.content.Context;
import android.content.res.Resources;


import com.busilinq.casepocket.viewinterface.IBaseView;

import rx.Subscription;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;


public class BasePresenter<T extends IBaseView> implements IPresenter<T> {

    protected Context mContext;
    protected Resources mRes;
    protected T mBaseView;
    protected CompositeSubscription mCompositeSubscription;
    private String mFunCode;

    @Override
    public void attachView(T view) {
        this.mBaseView = view;
        this.mContext = view.getContext();
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

    private Action1 mThrowableAction = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {

            //mBaseView.showLoadingError(EmptyLayout.NETWORK_ERROR);
            if(null == mFunCode  || mFunCode.equals("")){
                mBaseView.toast("网络连接失败，请检查网络设置");
            }else{
                mBaseView.toast("("+mFunCode+") "+"网络连接失败，请检查网络设置");
            }
            mBaseView.closeProgressDialog();
            mBaseView.onRefresh(false);
        }
    };

    protected Action1 getThrowableAction(String funCode){
        this.mFunCode = funCode;
        return mThrowableAction;
    }

}
