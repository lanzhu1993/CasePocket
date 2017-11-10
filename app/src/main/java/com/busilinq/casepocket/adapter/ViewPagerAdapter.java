package com.busilinq.casepocket.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.busilinq.casepocket.bean.Relation;
import com.busilinq.casepocket.factory.FragmentFactory;
import com.busilinq.casepocket.ui.fragment.CaseFragment;

import java.util.List;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/5
 * author: shiquan.lu
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Relation> mList;
    public ViewPagerAdapter(FragmentManager fm, List<Relation> list) {
        super(fm);
        this.mList = list;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = FragmentFactory.createFragment(CaseFragment.class,false);
        Bundle bundle = new Bundle();
        bundle.putSerializable("type",mList.get(position));
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return mList ==  null ? 0 : mList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mList.get(position).getRelationName();
    }

    public void refresh(List<Relation> list){
        this.mList = list;
        notifyDataSetChanged();
    }

}