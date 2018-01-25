package com.busilinq.casepocket.ui;

import android.os.Handler;

import com.busilinq.casepocket.R;
import com.busilinq.casepocket.base.BaseActivity;
import com.busilinq.casepocket.modle.PocketApi;
import com.busilinq.casepocket.utils.ACache;

import butterknife.Bind;
import pl.droidsonroids.gif.GifImageView;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/2
 * author: shiquan.lu
 */

public class SplashActivity extends BaseActivity {

    @Bind(R.id.splash_gif_iv) GifImageView mGifImageView;

    private static final int SHOW_TIME_MIN = 5000;// 最小显示时间
    private static final int SHOW_TIME_MIN_ZERO = 0;// 最小显示时间

    private Handler mHandler = new Handler();

    private PocketApi pocketApi;

    @Override
    public int initContentView() {
        return R.layout.activity_splash;
    }

    @Override
    public void initData() {
        pocketApi =  new PocketApi(ACache.get(SplashActivity.this));
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                skipActivity(SplashActivity.this,WelcomeActivity.class);
            }
        },null == pocketApi.getAccount()?SHOW_TIME_MIN:SHOW_TIME_MIN_ZERO);
    }

    @Override
    public void initUi() {
    }

}
