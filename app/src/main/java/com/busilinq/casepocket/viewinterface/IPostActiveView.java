package com.busilinq.casepocket.viewinterface;

import android.support.v7.widget.RecyclerView;

/**
 * 描述：
 * <p>
 * 创建时间： 2017/11/16  10:36
 * author :shiquan.lu
 * email: 1113799552@qq.com
 */

public interface IPostActiveView extends IBaseView{

    RecyclerView getRecyclerView();

    void postActiveData();

    void saveActiveData();
}
