package com.busilinq.casepocket.presenter;

import com.busilinq.casepocket.bean.Relation;
import com.busilinq.casepocket.bean.UpdateInfo;
import com.busilinq.casepocket.bean.User;
import com.busilinq.casepocket.modle.RelationApi;
import com.busilinq.casepocket.modle.UserApi;
import com.busilinq.casepocket.viewinterface.IMainView;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SQLQueryListener;
import rx.Subscription;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/5
 * author: shiquan.lu
 */

public class MainPresenter extends BasePresenter<IMainView> {

    RelationApi relationApi;
    UserApi userApi;

    @Override
    public void attachView(IMainView view) {
        super.attachView(view);
        relationApi = RelationApi.getRelationApi();
        userApi = UserApi.getUserApi();
    }


    public Subscription findOwnerInfo(FindListener<Relation> listener){
        BmobQuery<Relation> query = new BmobQuery<>();
        query.addWhereEqualTo("userId", BmobUser.getCurrentUser(User.class).getObjectId());
        return query.findObjects(listener);
    }

    public void queryUpdateInfo(SQLQueryListener<UpdateInfo> listener){
        addSubscription(userApi.queryUpdateInfo(listener));
    }



}
