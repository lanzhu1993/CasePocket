package com.busilinq.casepocket.ui;


import android.Manifest;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.view.MenuItem;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.busilinq.casepocket.R;
import com.busilinq.casepocket.adapter.ViewPagerAdapter;
import com.busilinq.casepocket.base.BaseActivity;

import com.busilinq.casepocket.bean.UpdateInfo;
import com.busilinq.casepocket.factory.FragmentFactory;

import com.busilinq.casepocket.presenter.MainPresenter;
import com.busilinq.casepocket.ui.fragment.ActiveFragment;
import com.busilinq.casepocket.ui.fragment.MineFragment;
import com.busilinq.casepocket.ui.fragment.PacketFragment;
import com.busilinq.casepocket.update.UpdateManager;
import com.busilinq.casepocket.utils.AppInfoUtils;

import com.busilinq.casepocket.utils.ToastUtils;
import com.lzy.ninegrid.NineGridView;
import com.tbruyelle.rxpermissions.RxPermissions;

import butterknife.Bind;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import rx.functions.Action1;


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
        initPerMission();
        NineGridView.setImageLoader(new GlideImageLoader());
    }

    /** Glide 加载 */
    private class GlideImageLoader implements NineGridView.ImageLoader {
        @Override
        public void onDisplayImage(Context context, ImageView imageView, String url) {
            Glide.with(context).load(url)//
                    .placeholder(R.drawable.shape_default_bg)//
                    .error(R.drawable.shape_default_bg)//
                    .diskCacheStrategy(DiskCacheStrategy.ALL)//
                    .into(imageView);
        }

        @Override
        public Bitmap getCacheImage(String url) {
            return null;
        }
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

    private void initPerMission(){
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE
        ,Manifest.permission.CALL_PHONE,Manifest.permission.READ_PHONE_STATE,Manifest.permission.CAMERA,Manifest.permission.READ_PHONE_STATE
        ,Manifest.permission.CALL_PHONE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if(!aBoolean){
                            ToastUtils.showToast("请打开相应权限");
                        }
                    }
                });
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
