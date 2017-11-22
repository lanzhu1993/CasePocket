package com.busilinq.casepocket.presenter;

import com.busilinq.casepocket.adapter.SelectPhotoAdapter;
import com.busilinq.casepocket.bean.EvaluationInfo;
import com.busilinq.casepocket.modle.ActiveApi;
import com.busilinq.casepocket.utils.ToastUtils;
import com.busilinq.casepocket.viewinterface.IPostActiveView;
import com.busilinq.casepocket.widget.LoadDialogView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import rx.Subscription;

/**
 * 描述：
 * <p>
 * 创建时间： 2017/11/16  10:37
 * author :shiquan.lu
 * email: 1113799552@qq.com
 */

public class PostActivePresenter extends BasePresenter<IPostActiveView> {

    private SelectPhotoAdapter adapter;
    private List<String> mPhotoList = new ArrayList<>();

    ActiveApi activeApi;

    @Override
    public void attachView(IPostActiveView view) {
        super.attachView(view);
        activeApi = ActiveApi.getInstance();
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


    public void saveEvaluationInfo(EvaluationInfo evaluationInfo){
        Subscription subscription = activeApi.saveEvaluationInfo(evaluationInfo, new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(null == e){
                    ToastUtils.showToast("发布成功");
                    mBaseView.finishActivity();
                }else {
                    ToastUtils.showToast("发布失败");
                }
                LoadDialogView.dismssDialog();
            }
        });
        addSubscription(subscription);
    }


}
