package com.busilinq.casepocket.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.busilinq.casepocket.R;
import com.busilinq.casepocket.adapter.ViewPagerAdapter;
import com.busilinq.casepocket.base.BaseActivity;
import com.busilinq.casepocket.base.BaseFragment;
import com.busilinq.casepocket.bean.Relation;
import com.busilinq.casepocket.presenter.FragmentPacketPresenter;
import com.busilinq.casepocket.ui.AddCaseActivity;
import com.busilinq.casepocket.ui.MainActivity;
import com.busilinq.casepocket.utils.Constants;
import com.busilinq.casepocket.viewinterface.IFragmentPacketView;
import com.busilinq.casepocket.widget.HeaderLayoutView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by ThinkPad on 2017/11/10.
 */

public class PacketFragment extends BaseFragment implements IFragmentPacketView{


    @Bind(R.id.main_add_btn)
    Button mMainAddBtn;

    @Bind(R.id.main_no_data_layout)
    LinearLayout mMainNoDataLayout;

    @Bind(R.id.main_have_data_layout)
    RelativeLayout mMainHaveDataLayout;

    @Bind(R.id.main_viewpager)
    ViewPager mMainViewPager;

    @Bind(R.id.main_tab_layout)
    TabLayout mMainTabLayout;

    @Bind(R.id.main_fb)
    ImageView mMainFb;

    ViewPagerAdapter adapter;
    FragmentPacketPresenter presenter;


    private List<Relation> relationList = new ArrayList<>();

    @Override
    public int initView() {
        return R.layout.fragment_packet;
    }



    @Override
    protected void initData() {
        presenter = new FragmentPacketPresenter();
        presenter.attachView(this);
        setupViewPager();
        findMember();
    }

    @Override
    protected void initListener() {

    }

    @Override
    public void intoAddCaseActivity() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.RELATION,new Relation());
        ((BaseActivity)mActivity).showActivity(mActivity,AddCaseActivity.class,bundle);
    }

    @Override
    public void findMember() {
        showProgressDialog("加载中...");
        presenter.findOwnerInfo(new FindListener<Relation>() {
            @Override
            public void done(List<Relation> list, BmobException e) {
                if(e == null){
                    relationList = list;
                }
                if(relationList.size() > 0){
                    mMainHaveDataLayout.setVisibility(View.VISIBLE);
                    mMainNoDataLayout.setVisibility(View.INVISIBLE);
                    for (int i = 0; i <relationList.size() ; i++) {
                        mMainTabLayout.addTab(mMainTabLayout.newTab().setText(relationList.get(i).getRelationName()));
                    }
                    adapter.refresh(relationList);
                }else{
                    mMainHaveDataLayout.setVisibility(View.INVISIBLE);
                    mMainNoDataLayout.setVisibility(View.VISIBLE);
                }
                closeProgressDialog();
            }
        });
    }

    @OnClick({R.id.main_fb})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.main_fb:
                intoAddCaseActivity();
                break;
        }
    }


    @Override
    public void setupViewPager() {
        mMainTabLayout.setTabTextColors(getResources().getColor(R.color.color_ffffff),getResources().getColor(R.color.color_ffffff));
        if(adapter == null){
            adapter = new ViewPagerAdapter(getChildFragmentManager(),relationList);
        }else{
            adapter.refresh(relationList);
        }
        mMainViewPager.setAdapter(adapter);
        mMainViewPager.setOffscreenPageLimit(0);
        mMainTabLayout.setupWithViewPager(mMainViewPager);
    }
}
