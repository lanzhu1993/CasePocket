package com.busilinq.casepocket.ui;

import android.text.TextUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.busilinq.casepocket.R;
import com.busilinq.casepocket.base.BaseActivity;
import com.busilinq.casepocket.bean.User;
import com.busilinq.casepocket.viewinterface.IQRCodeView;
import com.busilinq.casepocket.widget.HeaderLayoutView;
import com.busilinq.casepocket.widget.RxQRCode;

import butterknife.Bind;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 描述：二维码界面
 * <p>
 * 创建时间： 2017/11/21  16:22
 * author :shiquan.lu
 * email: 1113799552@qq.com
 */

public class QRCodeActivity extends BaseActivity implements IQRCodeView{

    @Bind(R.id.header) HeaderLayoutView header;
    @Bind(R.id.qrcode_name_tv) TextView mQRCodeNameTv;
    @Bind(R.id.qrcode_avater_iv) CircleImageView mQRCodeAvaterIv;
    @Bind(R.id.qrcode_code_iv) ImageView mQRCodeCodeIv;
    private User currentUser;


    @Override
    public int initContentView() {
        return R.layout.activity_qrcode;
    }

    @Override
    public void initData() {
        showUserInfo();
        createQRCode();
    }

    @Override
    public void initUi() {
        header.setTitle("个人名片");
    }

    @Override
    public void showUserInfo() {
        currentUser = BmobUser.getCurrentUser(User.class);
        if(null != currentUser.getNickName() && !TextUtils.isEmpty(currentUser.getNickName())){
            mQRCodeNameTv.setText(currentUser.getNickName());
        }
        Glide.with(this)
                .load(currentUser.getAvater())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.icon_add_avater)
                .error(R.mipmap.icon_add_avater)
                .into(mQRCodeAvaterIv);
    }

    @Override
    public void createQRCode() {
        String pocketId = "口袋ID : "+currentUser.getObjectId() + "\n";
        String phoneNumber = "电话号码 : "+currentUser.getUsername();
        RxQRCode.createQRCode(pocketId + phoneNumber, 800, 800, mQRCodeCodeIv);

    }
}
