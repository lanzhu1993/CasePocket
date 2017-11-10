package com.busilinq.casepocket.viewinterface;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/5
 * author: shiquan.lu
 */

public interface ICaseFragmentView extends IBaseFragmentView{

    void initRefreshUI();

    void getCaseDetailInfo();

    void intoGalleryActivity(int position);

}
