package com.busilinq.casepocket.presenter;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.busilinq.casepocket.adapter.GalleryAdapter;
import com.busilinq.casepocket.viewinterface.IGalleryFragmentView;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/4/5
 * author: shiquan.lu
 */

public class GalleryFragmentPresenter extends BaseFragmentPresenter<IGalleryFragmentView> {

    public RecyclerView mRecycler;
    public GalleryAdapter mGalleryAdapter;
    public LinearLayoutManager mLinearLayoutManager;

    private int page = 1;
    private boolean isLoadMore = false;
    private int mLastVisibleItem;
    private List<String> mList = new ArrayList<>();

    @Override
    public void attachView(IGalleryFragmentView view) {
        super.attachView(view);
        this.mRecycler = mBaseView.getRecyclerView();
        this.mGalleryAdapter = mBaseView.getGalleryAdapter();
        this.mLinearLayoutManager = mBaseView.getLayoutManager();
    }

    //请求数据
    public void requestData(List<String> list){
        mList = list;
        if (isLoadMore) page = page + 1;
        mBaseView.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                displayData(mList);
            }
        });
    }



    /**
     * 展示相册图片
     * @param list
     */
    private void displayData(List<String> list){
        mBaseView.setDataRefresh(true);
        if (list == null){
            return;
        }
        mGalleryAdapter.setList(mList);
        mGalleryAdapter.notifyDataSetChanged();
        mBaseView.setDataRefresh(false);
    }

    /**
     * recyclerview的监听，加载到最后一个条目时，请求下一页数据
     */
    public void addScrollistener(){
        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    //获取最后一个可见条目的角标
                    mLastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
                    //当显示本页最后一个条目时，加载下一页
                    if (mLastVisibleItem + 1 == mLinearLayoutManager.getItemCount()){
                        mBaseView.setDataRefresh(true);
                        isLoadMore = true;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                requestData(mList);
                            }
                        },1000);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mLastVisibleItem = mLinearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

}
