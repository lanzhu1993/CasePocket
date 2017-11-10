package com.busilinq.casepocket.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.busilinq.casepocket.utils.ToastUtils;

import butterknife.ButterKnife;

/**
 * 描述：Fragment基类
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/4/5
 * author: shiquan.lu
 */

public abstract class BaseFragment extends Fragment {

    public Activity mActivity;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(initView(),container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        initListener();

    }

    public abstract int initView();

    protected void initData(){}

    protected void initListener() {
    }

    protected void toast(String msg){
        ToastUtils.showToast(msg);
    }

}
