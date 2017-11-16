package com.busilinq.casepocket.ui.fragment;

import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.busilinq.casepocket.R;
import com.busilinq.casepocket.base.BaseActivity;
import com.busilinq.casepocket.base.BaseFragment;
import com.busilinq.casepocket.bean.User;
import com.busilinq.casepocket.db.CPDbApi;
import com.busilinq.casepocket.ui.PersonalActivity;

import butterknife.Bind;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 描述：
 * <p>
 * 创建时间： 2017/11/10.13:15
 * author :shiquan.lu
 * email: 1113799552@qq.com
 */

public class MineFragment extends BaseFragment {

    @Bind(R.id.mine_name_tv) TextView mMineNameTv;
    @Bind(R.id.mine_avater_iv) CircleImageView mMineAvaterIv;
    @Bind(R.id.mine_user_layout) RelativeLayout mMineUserLayout;

    User mMineUser;

    @Override
    public int initView() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initData() {
        mMineUser = CPDbApi.getInstance().getUser();
    }

    @Override
    protected void initListener() {
        Glide.with(mActivity)
                .load(mMineUser.getAvater())
                .placeholder(R.mipmap.icon_add_avater)
                .error(R.mipmap.icon_add_avater)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(mMineAvaterIv);
        String nick_name = CPDbApi.getInstance().getUser().getNickName();
        if(null == nick_name || TextUtils.isEmpty(nick_name)){
            mMineNameTv.setText("");
        }else{
            mMineNameTv.setText(nick_name);
        }
    }

    @OnClick({R.id.mine_user_layout})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.mine_user_layout:
                ((BaseActivity)mActivity).showActivity(mActivity, PersonalActivity.class);
                break;
        }
    }
}
