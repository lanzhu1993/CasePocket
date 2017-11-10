package com.busilinq.casepocket.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.busilinq.casepocket.R;
import com.busilinq.casepocket.adapter.CaseInfoAdapter;
import com.busilinq.casepocket.base.BaseFragment;
import com.busilinq.casepocket.bean.CaseInfo;
import com.busilinq.casepocket.bean.ImagePath;
import com.busilinq.casepocket.bean.Relation;
import com.busilinq.casepocket.listener.OnItemClickListener;
import com.busilinq.casepocket.presenter.CaseFragmentPresenter;
import com.busilinq.casepocket.ui.GalleryActivity;
import com.busilinq.casepocket.ui.MainActivity;
import com.busilinq.casepocket.utils.Constants;
import com.busilinq.casepocket.utils.ToActivityUtil;
import com.busilinq.casepocket.viewinterface.ICaseFragmentView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/5
 * author: shiquan.lu
 */

public class CaseFragment extends BaseFragment implements ICaseFragmentView, SwipeRefreshLayout.OnRefreshListener ,OnItemClickListener{

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    @Bind(R.id.recycler)
    RecyclerView mRecycler;


    private List<List<String>> imagePathList = new ArrayList<>();
    private List<CaseInfo> caseInfoList = new ArrayList<>();

    private CaseInfoAdapter caseInfoAdapter;

    private Relation mRelation;


    private int[] colors = new int[]{Color.BLUE, Color.GREEN, Color.RED};
    private CaseFragmentPresenter presenter;

    @Override
    public int initView() {
        return R.layout.fragment_case;
    }

    @Override
    protected void initData() {
        mRelation = (Relation) getArguments().get("type");
        presenter = new CaseFragmentPresenter();
        presenter.attachView(this);
        initRefreshUI();
        mSwipeRefresh.setRefreshing(true);
        getCaseDetailInfo();
    }

    @Override
    protected void initListener() {
        if (null == caseInfoAdapter) {
            caseInfoAdapter = new CaseInfoAdapter(getActivity(), caseInfoList, imagePathList);
            mRecycler.setAdapter(caseInfoAdapter);
        } else {
            caseInfoAdapter.refresh(caseInfoList, imagePathList);
        }
        caseInfoAdapter.setOnItemClickListener(this);
    }


    @Override
    public void onDestroy() {
        presenter.detachView();
        presenter.cancel();
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        getCaseDetailInfo();
    }

    @Override
    public void initRefreshUI() {
        //设置加载圈颜色
        mSwipeRefresh.setColorSchemeColors(colors);
        mSwipeRefresh.setOnRefreshListener(this);
        //设置下拉距离
        mSwipeRefresh.setDistanceToTriggerSync(70);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void getCaseDetailInfo() {
        mSwipeRefresh.setRefreshing(true);
        presenter.findCaseInfo(mRelation.getObjectId(), new FindListener<CaseInfo>() {
            @Override
            public void done(List<CaseInfo> list, BmobException e) {
                if (e == null) {
                    caseInfoList = list;
                    for(int i =0;i<caseInfoList.size();i++){
                        String imagePath = caseInfoList.get(i).getImagePath();
                        String[] split = imagePath.split(",");
                        List<String> imageList = new ArrayList<String>();
                        for(int j=0;j<split.length;j++){
                            imageList.add(split[j]);
                        }
                        imagePathList.add(imageList);
                        caseInfoAdapter.refresh(caseInfoList, imagePathList);
                    }
                }
                mSwipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    public void intoGalleryActivity(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.CASE_DETAIL,caseInfoList.get(position));
        bundle.putSerializable(Constants.RELATION,mRelation);
        ToActivityUtil.showDataActivity(getActivity(), GalleryActivity.class,false,bundle);
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()){
            case R.id.item_layout:
                intoGalleryActivity(position);
                break;
        }
    }

}
