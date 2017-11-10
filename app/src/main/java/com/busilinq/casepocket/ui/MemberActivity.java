package com.busilinq.casepocket.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.busilinq.casepocket.R;
import com.busilinq.casepocket.adapter.MemberAdapter;
import com.busilinq.casepocket.base.BaseActivity;
import com.busilinq.casepocket.bean.Relation;
import com.busilinq.casepocket.listener.OnItemClickListener;
import com.busilinq.casepocket.presenter.MembePresenter;
import com.busilinq.casepocket.utils.Constant;
import com.busilinq.casepocket.utils.Constants;
import com.busilinq.casepocket.viewinterface.IMemberView;
import com.busilinq.casepocket.widget.DeleteDialog;
import com.busilinq.casepocket.widget.HeaderLayoutView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/4
 * author: shiquan.lu
 */

public class MemberActivity extends BaseActivity implements IMemberView,SwipeRefreshLayout.OnRefreshListener,OnItemClickListener,View.OnClickListener{

    @Bind(R.id.header)
    HeaderLayoutView header;

    @Bind(R.id.member_refresh_layout)
    SwipeRefreshLayout mMemberRefreshLayout;

    @Bind(R.id.merber_recyclerview)
    RecyclerView mMemberRecyclerView;

    @Bind(R.id.member_add_layout)
    RelativeLayout mMemberAddLayout;

    private MembePresenter presenter;
    private MemberAdapter adapter;

    private DeleteDialog deleteDialog;

    private int[] colors = new int[]{Color.BLUE, Color.GREEN, Color.RED};
    private List<Relation> mMemberList = new ArrayList<>();

    @Override
    public int initContentView() {
        return R.layout.activity_member;
    }

    @Override
    public void initData() {
        presenter = new MembePresenter();
        presenter.attachView(this);
        //设置加载圈颜色
        mMemberRefreshLayout.setColorSchemeColors(colors);
        mMemberRefreshLayout.setOnRefreshListener(this);
        //设置下拉距离
        mMemberRefreshLayout.setDistanceToTriggerSync(70);
        mMemberRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mMemberRefreshLayout.setRefreshing(true);
        if(adapter == null){
            adapter = new MemberAdapter(this,mMemberList);
            mMemberRecyclerView.setAdapter(adapter);
        }else {
            adapter.refresh(mMemberList);
        }
        adapter.setOnItemClickListener(this);
        getDataFromBmob();
    }

    @Override
    public void initUi() {
        header.setTitle("就诊人");
        header.setRightTitle("确认");
        header.setRightTvOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(mMemberList.size()>0){
                   Bundle bundle = new Bundle();
                   bundle.putSerializable(Constants.RELATION,mMemberList.get(adapter.getClickPosition()));
                   Intent intent = new Intent();
                   intent.putExtras(bundle);
                   setResult(101,intent);
                   finish();
               }
            }
        });
    }

    @OnClick(R.id.member_add_layout)
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.member_add_layout:
                showActivity(MemberActivity.this,AddMemberActivity.class);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        presenter.cancel();
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        getDataFromBmob();
    }

    @Override
    public void getDataFromBmob() {
        presenter.findOwnerInfo(new FindListener<Relation>() {
            @Override
            public void done(List<Relation> list, BmobException e) {
                if(e == null && list.size() > 0){
                    mMemberList = list;
                    adapter.refresh(mMemberList);
                }
                mMemberRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void showDeleteDialog(int position) {
        if(deleteDialog == null){
            deleteDialog = new DeleteDialog(MemberActivity.this);
        }
        deleteDialog.setOnClickListener(this);
        deleteDialog.setPosition(position);
        deleteDialog.show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mMemberRefreshLayout.setRefreshing(true);
        getDataFromBmob();
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (view.getId()){
            case R.id.item_edit:
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.RELATION,mMemberList.get(position));
                showActivity(MemberActivity.this,AddMemberActivity.class,bundle);
                break;
            case R.id.item_delete:
                showDeleteDialog(position);
                break;

        }
    }

    private void deleteData(final int position) {
        showProgressDialog("正在删除中...");
        presenter.delete(mMemberList.get(position), mMemberList.get(position).getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    mMemberList.remove(position);
                    adapter.notifyItemRemoved(position);
                    toast("删除成功");
                }else {
                    toast("删除失败");
                }
                closeProgressDialog();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_certain:
                deleteData(deleteDialog.getPosition());
                deleteDialog.dismiss();
                break;
            case R.id.tv_cancle:
                deleteDialog.dismiss();
                break;
        }
    }
}
