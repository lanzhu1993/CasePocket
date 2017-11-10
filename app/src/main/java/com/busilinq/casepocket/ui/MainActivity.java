package com.busilinq.casepocket.ui;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.busilinq.casepocket.R;
import com.busilinq.casepocket.adapter.ViewPagerAdapter;
import com.busilinq.casepocket.base.BaseActivity;
import com.busilinq.casepocket.base.BaseFragment;
import com.busilinq.casepocket.bean.Relation;
import com.busilinq.casepocket.bean.UpdateInfo;
import com.busilinq.casepocket.factory.FragmentFactory;
import com.busilinq.casepocket.presenter.FragmentPacketPresenter;
import com.busilinq.casepocket.presenter.MainPresenter;
import com.busilinq.casepocket.ui.fragment.ActiveFragment;
import com.busilinq.casepocket.ui.fragment.MineFragment;
import com.busilinq.casepocket.ui.fragment.PacketFragment;
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

public class MainActivity extends BaseActivity{

    @Bind(R.id.tab_bottom)
    BottomNavigationView mBottomNavigationView;

    MainPresenter presenter;

    ViewPagerAdapter adapter;


    private Fragment[] fragments;
    private int index;
    //当前fragment的index
    private int currentTabIndex;
    private PacketFragment packetFragment;
    private ActiveFragment activeFragment;
    private MineFragment mineFragment;

    @Override
    public int initContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void initData() {
        presenter = new MainPresenter();
        initFragmentData();
        mBottomNavigationView.setOnNavigationItemSelectedListener(mTabItemListener);
    }

    private void initFragmentData() {
        packetFragment = (PacketFragment) FragmentFactory.createFragment(PacketFragment.class);
        activeFragment = (ActiveFragment) FragmentFactory.createFragment(ActiveFragment.class);
        mineFragment = (MineFragment) FragmentFactory.createFragment(MineFragment.class);
        fragments = new Fragment[]{packetFragment,activeFragment,mineFragment};
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container,packetFragment)
                .add(R.id.fragment_container,activeFragment)
                .hide(activeFragment).show(packetFragment)
                .commit();
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
      /*  header.setSearchLayoutOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO
            }
        });*/
    }





    @Override
    protected void onDestroy() {
        presenter.detachView();
        presenter.cancel();
        super.onDestroy();
    }


    BottomNavigationView.OnNavigationItemSelectedListener  mTabItemListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.item_msg:
                    index = 0;
                    break;
                case R.id.item_find:
                    index = 1;
                    break;
                case R.id.item_mine:
                    index = 2;
                    break;
            }
            if (currentTabIndex != index) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.hide(fragments[currentTabIndex]);
                if (!fragments[index].isAdded()) {
                    transaction.add(R.id.fragment_container, fragments[index]);
                }
                transaction.show(fragments[index]).commit();
            }
            currentTabIndex = index;
            return true;
        }
    };

}
