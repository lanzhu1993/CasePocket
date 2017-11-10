package com.busilinq.casepocket.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.busilinq.casepocket.R;
import com.busilinq.casepocket.bean.PopItem;

import java.util.Collection;
import java.util.List;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/8
 * author: shiquan.lu
 */

public class PopAdapter extends BasicAdapter {

    private List<PopItem> itemList;
    private int mItemLayoutId;

    public PopAdapter(AbsListView view, Collection mDatas, int itemLayoutId) {
        super(view, mDatas, itemLayoutId);
        this.mItemLayoutId = itemLayoutId;
        this.itemList = (List<PopItem>) mDatas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AdapterHolder adapterHolder = AdapterHolder.get(convertView,parent,mItemLayoutId,position);
        adapterHolder.setImageResource(R.id.popup_item_iv,itemList.get(position).getImageId());
        adapterHolder.setText(R.id.popup_item_tv,itemList.get(position).getTitle());
        return adapterHolder.getConvertView();
    }

    @Override
    public void refresh(Collection datas) {
        super.refresh(datas);
        this.itemList = (List<PopItem>) mDatas;
    }
}
