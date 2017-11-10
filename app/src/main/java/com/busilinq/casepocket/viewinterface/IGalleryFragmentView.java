package com.busilinq.casepocket.viewinterface;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.busilinq.casepocket.adapter.GalleryAdapter;

import java.util.List;


/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/4/5
 * author: shiquan.lu
 */

public interface IGalleryFragmentView extends IBaseFragmentView{

    void setDataRefresh(boolean refresh);

    RecyclerView getRecyclerView();

    GalleryAdapter getGalleryAdapter();

    LinearLayoutManager getLayoutManager();

    void setUpView();

    void setUpData();

}
