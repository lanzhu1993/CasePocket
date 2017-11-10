package com.busilinq.casepocket.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.busilinq.casepocket.R;
import com.busilinq.casepocket.ui.ForgetActivity;
import com.busilinq.casepocket.ui.LoginActivity;
import com.busilinq.casepocket.ui.RegistActivity;
import com.busilinq.casepocket.ui.SplashActivity;
import com.busilinq.casepocket.ui.WelcomeActivity;
import com.busilinq.casepocket.utils.ToastUtils;
import com.busilinq.casepocket.viewinterface.IBaseView;
import com.busilinq.casepocket.widget.LoadDialogView;
import com.zhy.autolayout.AutoLayoutActivity;

import butterknife.ButterKnife;


public abstract class BaseActivity extends AutoLayoutActivity implements IBaseView {

    protected Context mContext;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(this instanceof  SplashActivity ||  this instanceof WelcomeActivity ||  this instanceof LoginActivity
            || this instanceof RegistActivity ||  this instanceof ForgetActivity){
            //无title
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            //全屏
            getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                    WindowManager.LayoutParams. FLAG_FULLSCREEN);
        }
        setContentView(initContentView());
        mContext = this.getContext();
        initInjector();
        initData();
        initUi();
        AppManager.getAppManager().addActivity(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * 设置View
     *
     * @return
     */
    public abstract int initContentView();

    /**
     * 注入Injector
     */
    public void initInjector() {
        ButterKnife.bind(this);
    }

    ;

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 初始化UI
     */
    public abstract void initUi();

    @Override
    public void toast(String msg) {
        ToastUtils.showToast(msg);
    }

    @Override
    public void showProgressDialog(String msg) {
        LoadDialogView.showDialog(mContext,msg);
    }

    @Override
    public void closeProgressDialog() {
        LoadDialogView.dismssDialog();
    }

    @Override
    public void showLoading(int visibility) {

    }

    @Override
    public void showLoadingError(int errorType) {

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onRefresh(boolean bRefresh) {

    }

    @Override
    public void showActivity(Activity activity, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(activity, cls);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

    }

    @Override
    public void showActivity(Activity activity, Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        startActivity(intent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    public void showActivity(Activity activity, Intent intent) {
        activity.startActivity(intent);
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    public void skipActivity(Activity activity, Class<?> cls, Bundle bundle) {
        showActivity(activity, cls, bundle);
        activity.finish();
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    public void skipActivity(Activity activity, Class<?> cls) {
        showActivity(activity, cls);
        activity.finish();
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    @Override
    public void skipActivity(Activity activity, Intent intent) {
        showActivity(activity, intent);
        activity.finish();
        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    protected  View.OnClickListener mArrowListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    };

}
