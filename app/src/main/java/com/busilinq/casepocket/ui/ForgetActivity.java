package com.busilinq.casepocket.ui;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.busilinq.casepocket.R;
import com.busilinq.casepocket.base.BaseActivity;
import com.busilinq.casepocket.presenter.ForgetPresenter;
import com.busilinq.casepocket.utils.PhoneFormatCheckUtils;
import com.busilinq.casepocket.viewinterface.IForgetView;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/3
 * author: shiquan.lu
 */

public class ForgetActivity extends BaseActivity implements IForgetView{


    @Bind(R.id.forget_back_iv)
    ImageView mForgetBackIv;

    @Bind(R.id.forget_phone_et)
    EditText mForgetPhoneEt;

    @Bind(R.id.forget_code_et)
    EditText mForgetCodeEt;

    @Bind(R.id.forget_password_et)
    EditText mForgetPasswordEt;

    @Bind(R.id.forget_getcode_tv)
    TextView mForgetGetCodeTv;

    @Bind(R.id.forget_sure_btn)
    Button mForgetSureBtn;

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
                    mForgetGetCodeTv.setText(count + "S后重发");
                    if(count>0){
                        count--;
                        mHandler.sendEmptyMessageDelayed(UPDATA_COUNT, 1000);
                    }else{
                        mHandler.sendEmptyMessageDelayed(UPDATA_COUNT_ZERO, 1000);
                    }
                    break;
                case UPDATA_COUNT_ZERO:
                    mForgetGetCodeTv.setText("获取验证码");
                    //重新获取按钮可用
                    mForgetGetCodeTv.setEnabled(true);
                    mForgetGetCodeTv.setTextColor(getResources().getColor(R.color.color_333333));
                    break;
            }
        }
    };

    private ForgetPresenter presenter;


    @Override
    public int initContentView() {
        return R.layout.activity_forget;
    }

    @Override
    public void initData() {
        presenter = new ForgetPresenter();
        presenter.attachView(this);
    }

    @Override
    public void initUi() {

    }


    @OnClick({R.id.forget_back_iv,R.id.forget_getcode_tv,R.id.forget_sure_btn})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.forget_back_iv:
                finish();
                break;
            case R.id.forget_getcode_tv:
                getVertifyCode();
                break;
            case R.id.forget_sure_btn:
                findPassword();
                break;
        }
    }


    @Override
    public void getVertifyCode() {
        String mPhoneNumber = mForgetPhoneEt.getEditableText().toString().trim();
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
                    mForgetGetCodeTv.setEnabled(false);
                    mForgetGetCodeTv.setTextColor(getResources().getColor(R.color.color_999999));
                }else{
                    toast("获取验证码失败");
                }
                closeProgressDialog();
            }
        });
    }

    @Override
    public void findPassword() {
        final String mPhoneNumber = mForgetPhoneEt.getEditableText().toString().trim();
        final String smsCode = mForgetCodeEt.getEditableText().toString().trim();
        final String password = mForgetPasswordEt.getEditableText().toString().trim();
        if (TextUtils.isEmpty(mPhoneNumber)) {
            toast("手机号码不能为空！");
            return;
        }
        if(!PhoneFormatCheckUtils.isPhoneLegal(mPhoneNumber)){
            toast("请输入正确的手机号码！");
            return;
        }
        if(TextUtils.isEmpty(smsCode)){
            toast("请输入验证码");
            return;
        }
        if(TextUtils.isEmpty(password)){
            toast("请输入密码");
            return;
        }
        showProgressDialog("提交数据中...");
        presenter.resetPasswordBySMSCode(smsCode,password, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    presenter.saveAccount(mPhoneNumber,password);
                    skipActivity(ForgetActivity.this,LoginActivity.class);
                    toast("密码找回成功");
                }else{
                    toast("密码找回失败");
                }
                closeProgressDialog();
            }
        });
    }


}
