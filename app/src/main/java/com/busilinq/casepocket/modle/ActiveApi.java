package com.busilinq.casepocket.modle;

import com.busilinq.casepocket.bean.EvaluationInfo;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import rx.Subscription;

/**
 * 描述：活动接口
 * <p>
 * 创建时间： 2017/11/22  10:05
 * author :shiquan.lu
 * email: 1113799552@qq.com
 */

public class ActiveApi {

    private ActiveApi(){};//私有构造函数，避免外界可以通过new 获取对象
    public static ActiveApi getInstance(){
        return SingletonHolder.instance;
    }
    /**
     *静态代码块
     */
    public static class SingletonHolder{
        private static final ActiveApi instance = new ActiveApi();
    }


    /**
     * 保存动态数据
     * @param evaluationInfo
     * @param listener
     */
    public Subscription saveEvaluationInfo(EvaluationInfo evaluationInfo ,SaveListener<String> listener){
        return evaluationInfo.save(listener);
    }

    /**
     * 按照区间查询列表数据
     * @param begin     起始位置
     * @param limit     限制数量
     * @param listener  回掉监听
     * @return
     */
    public Subscription queryEvaluationInfo(int begin, int limit, SQLQueryListener<EvaluationInfo> listener){
        BmobQuery<EvaluationInfo> query = new BmobQuery<>();
        String bql = "select * from EvaluationInfo LIMIT "+begin+","+limit;
        return query.doSQLQuery(bql,listener);
    }
}
