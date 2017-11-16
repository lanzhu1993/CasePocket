package com.busilinq.casepocket.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.busilinq.casepocket.R;
import com.busilinq.casepocket.base.AppManager;
import com.busilinq.casepocket.base.BaseActivity;
import com.busilinq.casepocket.bean.User;
import com.busilinq.casepocket.db.CPDbApi;
import com.busilinq.casepocket.presenter.LoginPresenter;
import com.busilinq.casepocket.utils.PhoneFormatCheckUtils;
import com.busilinq.casepocket.viewinterface.ILoginView;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/2
 * author: shiquan.lu
 */

public class LoginActivity extends BaseActivity  implements ILoginView{


    @Bind(R.id.login_back_iv)
    ImageView mLoginBackIv;

    @Bind(R.id.login_phone_et)
    EditText mLoginPhoneEt;

    @Bind(R.id.login_password_et)
    EditText mLoginPasswordEt;

    @Bind(R.id.login_forget_tv)
    TextView mLoginForgetTv;

    @Bind(R.id.login_regist_tv)
    TextView mLoginRegistTv;

    @Bind(R.id.login_sure_btn)
    Button mLoginSureBtn;

    private LoginPresenter presenter;



    @Override
    public int initContentView() {
        return R.layout.activity_login;
    }

    @Override
    public void initData() {
        presenter = new LoginPresenter();
        presenter.attachView(this);
    }

    @Override
    public void initUi() {
        if(null != presenter.getAccount()){
            mLoginPhoneEt.setText(presenter.getAccount().getPhone());
            //密码显示隐藏
           // mLoginPasswordEt.setText(presenter.getAccount().getPassword());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(null != presenter.getAccount()){
            mLoginPhoneEt.setText(presenter.getAccount().getPhone());
            //密码显示隐藏
            //mLoginPasswordEt.setText(presenter.getAccount().getPassword());
        }
    }

    @OnClick({R.id.login_back_iv,R.id.login_sure_btn,R.id.login_forget_tv,R.id.login_regist_tv})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.login_back_iv:
                finish();
                break;
            case R.id.login_sure_btn:
                loginAccount();
                break;
            case R.id.login_forget_tv:
                intoForgetActivity();
                break;
            case R.id.login_regist_tv:
                intoRegistActivity();
                break;

        }
    }

    @Override
    public void loginAccount() {
        final String username = mLoginPhoneEt.getEditableText().toString().trim();
        final String password = mLoginPasswordEt.getEditableText().toString().trim();
        if(TextUtils.isEmpty(username)){
            toast("请输入手机号码");
            return;
        }
        if(!PhoneFormatCheckUtils.isChinaPhoneLegal(username)){
            toast("手机号码不正确");
            return;
        }
        if(TextUtils.isEmpty(password)){
            toast("请输入密码");
            return;
        }
        showProgressDialog("登录中...");
        presenter.login(username, password, new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e == null){
                    toast("登录成功"+user.getUsername());
                    presenter.saveAccount(username,password);
                    CPDbApi.getInstance().saveUser(user);
                    intoMainActivity();
                }else{
                    toast("登录失败"+e.getMessage());
                }
                closeProgressDialog();
            }
        });
    }

    @Override
    public void intoRegistActivity() {
        showActivity(LoginActivity.this,RegistActivity.class);
        AppManager.getAppManager().finishActivity(WelcomeActivity.class);
    }

    @Override
    public void intoForgetActivity() {
        showActivity(LoginActivity.this,ForgetActivity.class);
        AppManager.getAppManager().finishActivity(WelcomeActivity.class);
    }

    @Override
    public void intoMainActivity() {
        skipActivity(LoginActivity.this,MainActivity.class);
        AppManager.getAppManager().finishActivity(WelcomeActivity.class);
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        presenter.cancel();
        super.onDestroy();
    }
}
