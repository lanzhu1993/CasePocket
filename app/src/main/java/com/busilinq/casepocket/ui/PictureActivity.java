package com.busilinq.casepocket.ui;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.busilinq.casepocket.R;
import com.busilinq.casepocket.base.BaseActivity;
import com.busilinq.casepocket.utils.Constants;
import com.busilinq.casepocket.utils.NetworkUtil;
import com.busilinq.casepocket.widget.HeaderLayoutView;

import butterknife.Bind;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/8
 * author: shiquan.lu
 */

public class PictureActivity extends BaseActivity {

    @Bind(R.id.header)
    HeaderLayoutView header;

    @Bind(R.id.picture_image_view)
    ImageView mPictureIv;

    private String imgurl;



    @Override
    public int initContentView() {
        return R.layout.activity_picture;
    }

    @Override
    public void initData() {
        imgurl = getIntent().getStringExtra(Constants.IMG_URL);
        Glide.with(this).load(imgurl).centerCrop().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(mPictureIv);
        new PhotoViewAttacher(mPictureIv);
    }

    @Override
    public void initUi() {
        header.setTitle("图片详情");
    }
}
