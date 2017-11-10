package com.busilinq.casepocket.presenter;

import com.busilinq.casepocket.bean.Relation;
import com.busilinq.casepocket.bean.User;
import com.busilinq.casepocket.modle.RelationApi;
import com.busilinq.casepocket.viewinterface.IAddMembView;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import rx.Subscription;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/4
 * author: shiquan.lu
 */

public class AddMembPresenter extends BasePresenter<IAddMembView> {

    private RelationApi relationApi;

    @Override
    public void attachView(IAddMembView view) {
        super.attachView(view);
        relationApi = RelationApi.getRelationApi();
    }

    public void save(String name ,String sex,String idcard, SaveListener<String> listener){
        Relation relation = new Relation();
        relation.setRelationName(name);
        relation.setIdCard(idcard);
        relation.setSex(sex);
        relation.setUserId(BmobUser.getCurrentUser(User.class).getObjectId());
        addSubscription(relationApi.sava(relation,listener));
    }


    public Subscription update(Relation relation,String name,String sex,String IDCard, UpdateListener listener){
        relation.setSex(sex);
        relation.setRelationName(name);
        relation.setIdCard(IDCard);
        return relation.update(relation.getObjectId(),listener);
    }
}
