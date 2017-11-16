package com.busilinq.casepocket.db;

import com.busilinq.casepocket.base.BaseApplication;
import com.busilinq.casepocket.bean.User;
import com.busilinq.casepocket.utils.ACache;

/**
 * 描述：口袋本地缓存
 * <p>
 * 创建时间： 2017/11/16  11:58
 * author :shiquan.lu
 * email: 1113799552@qq.com
 */

public class CPDbApi {

    private ACache mACache = ACache.get(BaseApplication.getInstance());

    private CPDbApi(){};//私有构造函数，避免外界可以通过new 获取对象
    public static CPDbApi getInstance(){
        return CPDbApiHolder.instance;
    }
    /**
     *静态代码块
     */
    public static class CPDbApiHolder{
        private static final CPDbApi instance = new CPDbApi();
    }


    public void saveUser(User user){
        mACache.put(User.class.getSimpleName(),user);
    }

    public User getUser(){
        return (User) mACache.getAsObject(User.class.getSimpleName());
    }
}
