package com.busilinq.casepocket.viewinterface;

import com.busilinq.casepocket.bean.EvaluationInfo;

import cn.bmob.v3.listener.SQLQueryListener;

/**
 * 描述：
 * <p>
 * 创建时间： 2017/11/22  9:44
 * author :shiquan.lu
 * email: 1113799552@qq.com
 */

public interface IActiveFragmentView extends IBaseFragmentView {

    void showCompleteInfoDialog();

    void queryEvaluationInfo();
}
