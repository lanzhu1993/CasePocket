package com.busilinq.casepocket.presenter;

import com.busilinq.casepocket.adapter.SelectPhotoAdapter;
import com.busilinq.casepocket.viewinterface.IPostActiveView;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * <p>
 * 创建时间： 2017/11/16  10:37
 * author :shiquan.lu
 * email: 1113799552@qq.com
 */

public class PostActiveViewPresenter extends BasePresenter<IPostActiveView> {

    private SelectPhotoAdapter adapter;
    private List<String> mPhotoList = new ArrayList<>();

    @Override
    public void attachView(IPostActiveView view) {
        super.attachView(view);
    }


    public void initAdapter(){
        if(null == adapter){
            adapter = new SelectPhotoAdapter(mBaseView.getContext(),mPhotoList);
            mBaseView.getRecyclerView().setAdapter(adapter);
        }else{
            adapter.refresh(mPhotoList);
        }
    }


    public SelectPhotoAdapter getAdapter(){
        return adapter;
    }
}
