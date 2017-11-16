package com.busilinq.casepocket.ui.fragment;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.busilinq.casepocket.R;
import com.busilinq.casepocket.base.BaseActivity;
import com.busilinq.casepocket.base.BaseFragment;
import com.busilinq.casepocket.ui.PostActiveActivity;
import com.busilinq.casepocket.widget.HeaderLayoutView;

import butterknife.Bind;

/**
 * 描述：
 * <p>
 * 创建时间： 2017/11/10.13:15
 * author :shiquan.lu
 * email: 1113799552@qq.com
 */

public class ActiveFragment extends BaseFragment {

    @Bind(R.id.active_refreshlayout)
    SwipeRefreshLayout mActiveRefreshLayout;

    @Bind(R.id.active_recyclerview)
    RecyclerView mActiveRecyclerView;

    @Bind(R.id.header)
    HeaderLayoutView header;

    private int[] colors = new int[]{Color.BLUE, Color.GREEN, Color.RED};

    @Override
    public int initView() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initData() {
        initRefreshUI();
    }

    private void initRefreshUI() {

    }

    @Override
    protected void initListener() {
        header.setTitle("动态");
        header.setLeftVisible(View.INVISIBLE);
        header.setRightImage(R.mipmap.icon_right_add);
        header.setRightBtnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发布动态
                intoSendActiveActivity();
            }
        });
    }

    private void intoSendActiveActivity() {
        ((BaseActivity)mActivity).showActivity(mActivity, PostActiveActivity.class);
    }
}
