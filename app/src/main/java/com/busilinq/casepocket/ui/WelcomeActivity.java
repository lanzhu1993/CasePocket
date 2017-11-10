package com.busilinq.casepocket.ui;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.busilinq.casepocket.R;
import com.busilinq.casepocket.adapter.SlidePagerAdapter;
import com.busilinq.casepocket.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/2
 * author: shiquan.lu
 */

public class WelcomeActivity extends BaseActivity implements ViewPager.OnPageChangeListener{

    @Bind(R.id.welcome_viewpager)
    ViewPager mWelcomeViewPager;

    @Bind(R.id.welcome_viewGroup)
    LinearLayout mWelcomeViewGroup;

    @Bind(R.id.welcome_point)
    View mWelcomeView;

    @Bind(R.id.welcome_experience_tv)
    TextView mWelcomeExperTv;

    @Bind(R.id.welcome_login_tv)
    TextView mWelcomeLoginTv;

    @Bind(R.id.welcome_regist_tv)
    TextView mWelcomeRegistTv;

    private SlidePagerAdapter slidePagerAdapter;

    /**
     * 装点点的ImageView数组
     */
    private ImageView[] tips;

    /**
     * 装ImageView数组
     */
    private ImageView[] mImageViews;

    /**
     * 图片资源id
     */
    private int[] imgIdArray = {R.mipmap.icon_begin_1,R.mipmap.icon_begin_two};

    /**
     * 小圆点滑动距离
     */
    private int diatance;

    @Override
    public int initContentView() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initData() {
        //将图片装载到数组中
        addItemPoint2ViewGroup();
        mImageViews = new ImageView[imgIdArray.length];
        for(int i=0; i<mImageViews.length; i++){
            ImageView imageView = new ImageView(this);
            mImageViews[i] = imageView;
            imageView.setBackgroundResource(imgIdArray[i]);
        }
        slidePagerAdapter = new SlidePagerAdapter(mImageViews);
        //设置Adapter
        mWelcomeViewPager.setAdapter(slidePagerAdapter);
        mWelcomeViewPager.setCurrentItem(0);
    }

    @Override
    public void initUi() {
        mWelcomeView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                diatance = mWelcomeViewGroup.getChildAt(1).getLeft() - mWelcomeViewGroup.getChildAt(0).getLeft();
            }
        });
        mWelcomeViewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //测出页面滚动时小红点移动的距离，并通过setLayoutParams(params)不断更新其位置
        float leftMargin = diatance * (position + positionOffset);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mWelcomeView.getLayoutParams();
        params.leftMargin = Math.round(leftMargin);
        mWelcomeView.setLayoutParams(params);
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void addItemPoint2ViewGroup() {
        //将点点加入到ViewGroup中
        tips = new ImageView[imgIdArray.length];
        for(int i=0; i<tips.length; i++){
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(10,10));
            tips[i] = imageView;
            tips[i].setBackgroundResource(R.drawable.icon_point_normal);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin =  15;
            layoutParams.rightMargin =  15;
            mWelcomeViewGroup.addView(imageView, layoutParams);
        }

    }

    @OnClick({R.id.welcome_experience_tv,R.id.welcome_login_tv,R.id.welcome_regist_tv})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.welcome_experience_tv:
                break;
            case R.id.welcome_login_tv:
                skipActivity(WelcomeActivity.this,LoginActivity.class);
                break;
            case R.id.welcome_regist_tv:
                showActivity(WelcomeActivity.this,RegistActivity.class);
                break;
        }
    }
}
