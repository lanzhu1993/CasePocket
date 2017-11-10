package com.busilinq.casepocket.modle;

import com.busilinq.casepocket.bean.Relation;
import com.busilinq.casepocket.bean.User;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import rx.Subscription;

/**
 * 描述：用户关系表
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/4
 * author: shiquan.lu
 */

public class RelationApi {

    private static RelationApi relationApi = null;

    private RelationApi(){}

    public static RelationApi getRelationApi(){
        if(null == relationApi){
            relationApi = new RelationApi();
        }
        return relationApi;
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


    /**
     * 添加用户到数据库
     * @param relation
     * @param listener
     * @return
     */
    public Subscription sava(Relation relation, SaveListener<String> listener){
        return relation.save(listener);
    }


    /**
     * 修改就诊人的信息
     * @param relation
     * @param objectId
     * @param listener
     * @return
     */
    public Subscription update(Relation relation,String objectId, UpdateListener listener){
        return relation.update(objectId,listener);
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
}
