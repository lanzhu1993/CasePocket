package com.busilinq.casepocket.modle;

import com.busilinq.casepocket.bean.Account;
import com.busilinq.casepocket.utils.ACache;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/3
 * author: shiquan.lu
 */

public class PocketApi {

    private ACache mACache;

    public PocketApi(ACache aCache){
        this.mACache = aCache;
    }

    public void saveAccount(Account account){
        mACache.put(Account.class.getSimpleName(),account);
    }

    public Account getAccount(){
        return (Account) mACache.getAsObject(Account.class.getSimpleName());
    }

    public void clear(){
        mACache.remove(Account.class.getSimpleName());
    }

    public void clear(String key){
        mACache.remove(key);
    }
}
