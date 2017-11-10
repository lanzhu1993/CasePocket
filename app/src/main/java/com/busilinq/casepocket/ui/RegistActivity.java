package com.busilinq.casepocket.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.busilinq.casepocket.R;
import com.busilinq.casepocket.base.AppManager;
import com.busilinq.casepocket.base.BaseActivity;
import com.busilinq.casepocket.base.BaseApplication;
import com.busilinq.casepocket.bean.User;
import com.busilinq.casepocket.presenter.RegistPresenter;
import com.busilinq.casepocket.utils.ACache;
import com.busilinq.casepocket.utils.Constants;
import com.busilinq.casepocket.utils.PhoneFormatCheckUtils;
import com.busilinq.casepocket.utils.ToastUtils;
import com.busilinq.casepocket.viewinterface.IRegistView;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/2
 * author: shiquan.lu
 */

public class RegistActivity extends BaseActivity implements IRegistView {

    @Bind(R.id.regist_back_iv)
    ImageView mRegistBackIv;

    @Bind(R.id.regist_phone_et)
    EditText mRegistPhoneEt;

    @Bind(R.id.regist_code_et)
    EditText mRegistCodeEt;

    @Bind(R.id.regist_password_et)
    EditText mRegistPasswordEt;

    @Bind(R.id.regist_getcode_tv)
    TextView mRegistGetCodeTv;

    @Bind(R.id.regist_sure_btn)
    Button mRegistSureBtn;

    private int count = 180;
    private String mPhoneNumber;
    private String SMSCode;

    private static final int UPDATA_COUNT = 0;
    private static final int UPDATA_COUNT_ZERO = 1;
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPDATA_COUNT:
                    mRegistGetCodeTv.setText(count + "S后重发");
                    if(count>0){
                        count--;
                        mHandler.sendEmptyMessageDelayed(UPDATA_COUNT, 1000);
                    }else{
                        mHandler.sendEmptyMessageDelayed(UPDATA_COUNT_ZERO, 1000);
                    }
                    break;
                case UPDATA_COUNT_ZERO:
                    mRegistGetCodeTv.setText("获取验证码");
                    mRegistGetCodeTv.setTextColor(getResources().getColor(R.color.color_333333));
                    //重新获取按钮可用
                    mRegistGetCodeTv.setEnabled(true);
                    break;
            }
        }
    };

    private RegistPresenter presenter;

    @Override
    public int initContentView() {
        return R.layout.activity_regist;
    }

    @Override
    public void initData() {
        presenter = new RegistPresenter();
        presenter.attachView(this);
    }

    @Override
    public void initUi() {

    }

    @OnClick({R.id.regist_back_iv,R.id.regist_getcode_tv,R.id.regist_sure_btn})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.regist_back_iv:
                finish();
                break;
            case R.id.regist_getcode_tv:
                getVertifyCode();
                break;
            case R.id.regist_sure_btn:
                registAccount();
                break;
        }
    }

    @Override
    public void registAccount() {
        mPhoneNumber = mRegistPhoneEt.getEditableText().toString().trim();
        SMSCode = mRegistCodeEt.getEditableText().toString().trim();
        final String password = mRegistPasswordEt.getEditableText().toString().trim();
        if (TextUtils.isEmpty(mPhoneNumber)) {
            toast("手机号码不能为空！");
            return;
        }
        if(!PhoneFormatCheckUtils.isPhoneLegal(mPhoneNumber)){
            toast("请输入正确的手机号码！");
            return;
        }
        if(TextUtils.isEmpty(SMSCode)){
            toast("验证码不能为空！");
            return;
        }
        if(TextUtils.isEmpty(password)){
            toast("请输入密码！");
            return;
        }
        showProgressDialog("正在注册中...");
        presenter.verifySmsCode(mPhoneNumber, SMSCode, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    presenter.signUp(mPhoneNumber, password, mPhoneNumber, new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if(e == null){
                                //TODO
                                //注册完成以后的后续操作
                                toast("注册成功");
                                presenter.saveAccount(mPhoneNumber,password);
                                AppManager.getAppManager().finishActivity(WelcomeActivity.class);
                                skipActivity(RegistActivity.this,LoginActivity.class);
                            }else{
                                toast("注册失败！"+e.getMessage());
                            }
                            closeProgressDialog();
                        }
                    });
                }else{
                    toast("请核对验证码");
                    closeProgressDialog();
                    return;
                }
            }
        });
    }

    @Override
    public void getVertifyCode() {
        mPhoneNumber = mRegistPhoneEt.getEditableText().toString().trim();
        if (TextUtils.isEmpty(mPhoneNumber)) {
            toast("手机号码不能为空！");
            return;
        }
        if(!PhoneFormatCheckUtils.isPhoneLegal(mPhoneNumber)){
            toast("请输入正确的手机号码！");
            return;
        }
        showProgressDialog("正在获取验证码");
        presenter.requestSMSCode(mPhoneNumber, "WeGo", new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if(e == null){
                    mHandler.sendEmptyMessage(UPDATA_COUNT);
                    toast("获取验证码成功");
                    mRegistGetCodeTv.setEnabled(false);
                    mRegistGetCodeTv.setTextColor(getResources().getColor(R.color.color_999999));
                }else{
                    toast("获取验证码失败"+e.getMessage());
                }
                closeProgressDialog();
            }
        });
    }


    @Override
    protected void onDestroy() {
        presenter.detachView();
        presenter.cancel();
        super.onDestroy();
    }
}
