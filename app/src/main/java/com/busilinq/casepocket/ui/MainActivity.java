package com.busilinq.casepocket.ui;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.busilinq.casepocket.R;
import com.busilinq.casepocket.adapter.ViewPagerAdapter;
import com.busilinq.casepocket.base.BaseActivity;
import com.busilinq.casepocket.bean.Relation;
import com.busilinq.casepocket.bean.UpdateInfo;
import com.busilinq.casepocket.presenter.MainPresenter;
import com.busilinq.casepocket.update.UpdateManager;
import com.busilinq.casepocket.utils.AppInfoUtils;
import com.busilinq.casepocket.utils.Constants;
import com.busilinq.casepocket.viewinterface.IMainView;
import com.busilinq.casepocket.widget.HeaderLayoutView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends BaseActivity implements IMainView{

    @Bind(R.id.header)
    HeaderLayoutView header;

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

    MainPresenter presenter;

    ViewPagerAdapter adapter;


    private List<Relation> relationList = new ArrayList<>();


    @Override
    public int initContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        presenter = new MainPresenter();
        presenter.attachView(this);
        findMember();

        update();
    }

    /**
     * 检查更新
     */
    private void update() {
        presenter.queryUpdateInfo(new SQLQueryListener<UpdateInfo>() {
            @Override
            public void done(BmobQueryResult<UpdateInfo> bmobQueryResult, BmobException e) {
                if(e == null && bmobQueryResult.getResults().size()>0){
                    UpdateInfo updateInfo = bmobQueryResult.getResults().get(0);
                    if(Integer.valueOf(updateInfo.getNewVersion()) > AppInfoUtils.getVersionCode(MainActivity.this)){
                        UpdateManager manager = new UpdateManager(MainActivity.this);
                        manager.setmUpdateInfo(updateInfo);
                        manager.updateAlertDialog(updateInfo.getMessage());
                    }
                }
            }
        });
    }

    @Override
    public void initUi() {
        header.setLeftVisible(View.INVISIBLE);
        header.setTitle("我的病历");
        header.setRightImage(R.mipmap.icon_add_case);
        header.setRightBtnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intoAddCaseActivity();
            }
        });
        setupViewPager();
      /*  header.setSearchLayoutOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        });*/
    }


    @OnClick(R.id.main_add_btn)
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.main_add_btn:
                intoAddCaseActivity();
                break;
        }
    }

    @Override
    public void intoAddCaseActivity() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.RELATION,new Relation());
        showActivity(MainActivity.this,AddCaseActivity.class,bundle);
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
                    header.setSearchLayoutInVisible();
                    mMainHaveDataLayout.setVisibility(View.INVISIBLE);
                    mMainNoDataLayout.setVisibility(View.VISIBLE);
                }
                closeProgressDialog();
            }
        });
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        findMember();
        mMainViewPager.removeAllViews();
        mMainViewPager.setAdapter(adapter);
        mMainTabLayout.setupWithViewPager(mMainViewPager);
    }

    @Override
    public void setupViewPager() {
        mMainTabLayout.setTabTextColors(getResources().getColor(R.color.color_ffffff),getResources().getColor(R.color.color_ffffff));
        if(adapter == null){
            adapter = new ViewPagerAdapter(getSupportFragmentManager(),relationList);
        }else{
            adapter.refresh(relationList);
        }
        mMainViewPager.setAdapter(adapter);
        mMainViewPager.setOffscreenPageLimit(0);
        mMainTabLayout.setupWithViewPager(mMainViewPager);
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        presenter.cancel();
        super.onDestroy();
    }

}
