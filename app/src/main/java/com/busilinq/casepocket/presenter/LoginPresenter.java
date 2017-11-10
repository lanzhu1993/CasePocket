package com.busilinq.casepocket.presenter;

import com.busilinq.casepocket.bean.Account;
import com.busilinq.casepocket.bean.User;
import com.busilinq.casepocket.modle.PocketApi;
import com.busilinq.casepocket.modle.UserApi;
import com.busilinq.casepocket.utils.ACache;
import com.busilinq.casepocket.viewinterface.ILoginView;

import cn.bmob.v3.listener.SaveListener;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/3
 * author: shiquan.lu
 */

public class LoginPresenter extends BasePresenter<ILoginView> {

    private UserApi userApi;
    private PocketApi pocketApi;

    @Override
    public void attachView(ILoginView view) {
        super.attachView(view);
        userApi = UserApi.getUserApi();
        pocketApi = new PocketApi(ACache.get(mBaseView.getContext()));
    }

    public void login(String account, String password, SaveListener<User> listener){
        addSubscription(userApi.login(account,password,listener));
    }
    public void saveAccount(String phone,String passwsord){
        Account account = new Account();
        account.setPhone(phone);
        account.setPassword(passwsord);
        pocketApi.saveAccount(account);
    }

    public Account getAccount(){
        return pocketApi.getAccount();
    }

}
