package com.busilinq.casepocket.ui;

import android.view.View;

import com.busilinq.casepocket.R;
import com.busilinq.casepocket.base.BaseActivity;
import com.busilinq.casepocket.widget.HeaderLayoutView;

import butterknife.Bind;

/**
 * 描述 ：发送话题界面
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/11/10
 * author: shiquan.lu
 */

public class SendActiveActivity extends BaseActivity {

    @Bind(R.id.header)
    HeaderLayoutView header;
    @Override
    public int initContentView() {
        return R.layout.activity_send_active;
    }

    @Override
    public void initData() {

    }

    @Override
    public void initUi() {
        header.setTitle("发布话题");
        header.setRightTitle("发布");
        header.setRightTvOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存数据
            }
        });
    }
}
