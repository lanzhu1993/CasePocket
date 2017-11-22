package com.busilinq.casepocket.ui.fragment;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.busilinq.casepocket.R;
import com.busilinq.casepocket.adapter.ActiveAdapter;
import com.busilinq.casepocket.base.BaseActivity;
import com.busilinq.casepocket.base.BaseFragment;
import com.busilinq.casepocket.bean.EvaluationInfo;
import com.busilinq.casepocket.bean.User;
import com.busilinq.casepocket.presenter.ActiveFragmentPresenter;
import com.busilinq.casepocket.ui.PersonalActivity;
import com.busilinq.casepocket.ui.PostActiveActivity;
import com.busilinq.casepocket.utils.ToastUtils;
import com.busilinq.casepocket.viewinterface.IActiveFragmentView;
import com.busilinq.casepocket.widget.HeaderLayoutView;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

/**
 * 描述：
 * <p>
 * 创建时间： 2017/11/10.13:15
 * author :shiquan.lu
 * email: 1113799552@qq.com
 */

public class ActiveFragment extends BaseFragment implements IActiveFragmentView,SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.active_refreshlayout) SwipeRefreshLayout mActiveRefreshLayout;
    @Bind(R.id.active_recyclerview) RecyclerView mActiveRecyclerView;
    @Bind(R.id.header) HeaderLayoutView header;

    private int[] colors = new int[]{Color.BLUE, Color.GREEN, Color.RED};

    private List<EvaluationInfo> evaluationInfoList = new ArrayList<>();

    ActiveFragmentPresenter presenter;
    ActiveAdapter adapter;

    private int mBegin = 0;
    private int mLimit = 10;

    @Override
    public int initView() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initData() {
        presenter = new ActiveFragmentPresenter();
        presenter.attachView(this);
        initRefreshUI();
        onRefresh();
    }

    private void initRefreshUI() {
        //设置加载圈颜色
        mActiveRefreshLayout.setColorSchemeColors(colors);
        mActiveRefreshLayout.setOnRefreshListener(this);
        //设置下拉距离
        mActiveRefreshLayout.setDistanceToTriggerSync(70);
        mActiveRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(null == adapter){
            adapter = new ActiveAdapter(mActivity,evaluationInfoList);
            mActiveRecyclerView.setAdapter(adapter);
        }else {
            adapter.setNewData(evaluationInfoList);
        }
        adapter.setOnLoadMoreListener(this,mActiveRecyclerView);
        adapter.setEnableLoadMore(true);
    }

    @Override
    protected void initListener() {
        header.setTitle("动态");
        header.setLeftVisible(View.INVISIBLE);
        header.setRightImage(R.mipmap.icon_right_add);
        header.setRightBtnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发布动态
                intoSendActiveActivity();
            }
        });
    }

    private void intoSendActiveActivity() {
        User user = BmobUser.getCurrentUser(User.class);
        if(null == user.getNickName() || TextUtils.isEmpty(user.getNickName())){
           showCompleteInfoDialog();
        }else{
            ((BaseActivity)mActivity).showActivity(mActivity, PostActiveActivity.class);
        }
    }

    @Override
    public void showCompleteInfoDialog() {
        new MaterialDialog.Builder(mActivity)
                .title("完善个人信息")
                .content("请完善个人信息才能发送动态")
                .positiveColor(getResources().getColor(R.color.colorPrimaryDark))
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        ((BaseActivity)mActivity).showActivity(mActivity, PersonalActivity.class);
                    }
                })
                .show();
    }

    @Override
    public void queryEvaluationInfo() {
        presenter.queryEvaluationInfo(mBegin, mLimit, new SQLQueryListener<EvaluationInfo>() {
            @Override
            public void done(BmobQueryResult<EvaluationInfo> bmobQueryResult, BmobException e) {
               if(null == e){
                    if(bmobQueryResult.getResults().size() == 10){
                        mBegin += 10;
                    }else{
                        mBegin += bmobQueryResult.getResults().size();
                    }
                   evaluationInfoList.addAll(bmobQueryResult.getResults());
                   adapter.setNewData(evaluationInfoList);
                   if(bmobQueryResult.getResults().size() == 0){
                       adapter.loadMoreEnd();
                   }else{
                       adapter.loadMoreComplete();
                   }
               }else{
                   adapter.loadMoreFail();
               }
               mActiveRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        mActiveRefreshLayout.setRefreshing(true);
        mBegin  = 0;
        evaluationInfoList.clear();
        queryEvaluationInfo();

    }

    @Override
    public void onLoadMoreRequested() {
        queryEvaluationInfo();
    }
}
