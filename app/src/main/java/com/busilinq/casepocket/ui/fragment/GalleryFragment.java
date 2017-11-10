package com.busilinq.casepocket.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.busilinq.casepocket.R;
import com.busilinq.casepocket.adapter.CardScaleHelper;
import com.busilinq.casepocket.adapter.GalleryAdapter;
import com.busilinq.casepocket.base.BaseFragment;
import com.busilinq.casepocket.presenter.GalleryFragmentPresenter;
import com.busilinq.casepocket.viewinterface.IGalleryFragmentView;
import com.busilinq.casepocket.widget.SpeedRecyclerView;

import java.util.List;

import butterknife.Bind;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/4/5
 * author: shiquan.lu
 */

public class GalleryFragment extends BaseFragment implements IGalleryFragmentView {

    @Bind(R.id.speed_recycler)
    SpeedRecyclerView mRecycler;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    private ListCallBack mCallBack;
    private GalleryFragmentPresenter presenter;

    public LinearLayoutManager mLinearLayoutManager;
    public GalleryAdapter mGalleryAdapter;
    private CardScaleHelper mCardScaleHepler = null;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallBack = (ListCallBack) activity;
    }


    @Override
    public int initView() {
        return R.layout.fragment_gallery;
    }

    @Override
    protected void initData() {
        setUpView();
        setUpData();
    }

    @Override
    public void setUpView() {
        //初始化相册布局
        mGalleryAdapter = new GalleryAdapter(getActivity());
        mLinearLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.HORIZONTAL,false);
        mRecycler.setLayoutManager(mLinearLayoutManager);
        mRecycler.setHasFixedSize(true);
        mRecycler.setAdapter(mGalleryAdapter);
        //recyclerview绑定scale效果
        mCardScaleHepler = new CardScaleHelper();
        mCardScaleHepler.setCurrentItemPos(0);
        mCardScaleHepler.attachToRecyclerView(mRecycler);
    }

    @Override
    public void setUpData() {
        //初始化相册数据
        presenter = new GalleryFragmentPresenter();
        presenter.attachView(this);
        presenter.requestData(mCallBack.getImagePath());
        presenter.addScrollistener();
    }

    @Override
    public void setDataRefresh(boolean refresh) {
        if (refresh){
            mSwipeRefresh.setRefreshing(true);
        }else {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mRecycler;
    }

    @Override
    public GalleryAdapter getGalleryAdapter() {
        return mGalleryAdapter == null ? new GalleryAdapter(getActivity()) : mGalleryAdapter;
    }

    @Override
    public LinearLayoutManager getLayoutManager() {
        return mLinearLayoutManager ==  null ? new LinearLayoutManager(getActivity(), OrientationHelper.HORIZONTAL,false) : mLinearLayoutManager ;
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        presenter.cancel();
        super.onDestroy();
    }

    public interface ListCallBack{
        List<String> getImagePath();
    }

}
