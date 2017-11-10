package com.busilinq.casepocket.presenter;

import com.busilinq.casepocket.bean.Relation;
import com.busilinq.casepocket.bean.User;
import com.busilinq.casepocket.modle.RelationApi;
import com.busilinq.casepocket.viewinterface.IMemberView;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import rx.Subscription;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/4
 * author: shiquan.lu
 */

public class MembePresenter extends BasePresenter<IMemberView> {

    private RelationApi relationApi;

    @Override
    public void attachView(IMemberView view) {
        super.attachView(view);
        relationApi = RelationApi.getRelationApi();
    }


    /**
     *删除就诊人信息
     * @param relation
     * @param objectId
     * @param listener
     * @return
     */
    public Subscription delete(Relation relation,String objectId, UpdateListener listener){
        return relation.delete(objectId,listener);
    }

    /**
     * 查询当前用户的子账户
     * @param listener
     * @return
     */
    public Subscription findOwnerInfo(FindListener<Relation> listener){
        BmobQuery<Relation> query = new BmobQuery<>();
        query.addWhereEqualTo("userId", BmobUser.getCurrentUser(User.class).getObjectId());
        return query.findObjects(listener);
    }
}
