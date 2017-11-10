package com.busilinq.casepocket.viewinterface;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/4
 * author: shiquan.lu
 */

public interface IMemberView extends IBaseView{

    void getDataFromBmob();

    void showDeleteDialog(int position);
}
