package com.busilinq.casepocket.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;


/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/4/14
 * author: shiquan.lu
 */

public class SlidePagerAdapter extends PagerAdapter {

    private ImageView[] mImageViews;

    public SlidePagerAdapter(ImageView[] imageViews) {
        this.mImageViews = imageViews;
    }

    @Override
    public int getCount() {
        // 返回页面数目实现有限滑动效果
        if (mImageViews != null) {
            return mImageViews.length;
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public Object instantiateItem(View container, int position) {
        ((ViewPager) container).addView(mImageViews[position], 0);
        return mImageViews[position];
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        // 注销父类销毁item的方法，因为此方法并不是使用此方法
//        super.destroyItem(container, position, object);
        ((ViewPager) container).removeView(mImageViews[position]);
    }

}
