package com.busilinq.casepocket.presenter;

import com.busilinq.casepocket.bean.Account;
import com.busilinq.casepocket.modle.PocketApi;
import com.busilinq.casepocket.modle.UserApi;
import com.busilinq.casepocket.utils.ACache;
import com.busilinq.casepocket.viewinterface.IForgetView;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/3
 * author: shiquan.lu
 */

public class ForgetPresenter extends BasePresenter<IForgetView>{

    private UserApi userApi;
    private PocketApi pocketApi;

    @Override
    public void attachView(IForgetView view) {
        super.attachView(view);
        userApi = UserApi.getUserApi();
        pocketApi = new PocketApi(ACache.get(mBaseView.getContext()));
    }



    public void verifySmsCode(String phoneNumber, String smsCode, UpdateListener listener){
        addSubscription(userApi.verifySmsCode(phoneNumber,smsCode,listener));
    }



    public void resetPasswordBySMSCode(String smsCode, String newPassword, UpdateListener listener){
        addSubscription(userApi.resetPasswordBySMSCode(smsCode,newPassword,listener));
    }

    public void requestSMSCode(String phone, String template, QueryListener<Integer> listener){
        addSubscription(userApi.requestSMSCode(phone,template,listener));
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
