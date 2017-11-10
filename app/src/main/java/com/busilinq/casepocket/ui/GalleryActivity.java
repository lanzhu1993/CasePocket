package com.busilinq.casepocket.ui;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.busilinq.casepocket.R;
import com.busilinq.casepocket.base.BaseActivity;
import com.busilinq.casepocket.bean.CaseInfo;
import com.busilinq.casepocket.bean.PopItem;
import com.busilinq.casepocket.bean.Relation;
import com.busilinq.casepocket.presenter.GalleryPresenter;
import com.busilinq.casepocket.ui.fragment.GalleryFragment;
import com.busilinq.casepocket.utils.Constants;
import com.busilinq.casepocket.utils.ScreenUtil;
import com.busilinq.casepocket.viewinterface.IGalleryView;
import com.busilinq.casepocket.widget.HeaderLayoutView;
import com.busilinq.casepocket.widget.MenuPopwindow;
import com.busilinq.casepocket.widget.MtitlePopupWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/8
 * author: shiquan.lu
 */

public class GalleryActivity extends BaseActivity implements IGalleryView,GalleryFragment.ListCallBack,MtitlePopupWindow.OnPopupWindowClickListener {

    @Bind(R.id.header)
    HeaderLayoutView header;

    @Bind(R.id.gallery_name_tv)
    TextView mGalleryNameTv;

    @Bind(R.id.gallery_hospital_tv)
    TextView mGalleryHospitalTv;

    @Bind(R.id.gallery_department_tv)
    TextView mGalleryDepartmentTv;

    @Bind(R.id.gallery_date_tv)
    TextView mGalleryDateTv;

    @Bind(R.id.gallery_frame_layout)
    FrameLayout mGalleryFrameLayout;

    @Bind(R.id.gallery_desc_tv)
    TextView mGalleryDescTv;

    private MenuPopwindow popupWindow;

    private CaseInfo caseInfo;
    private Relation mRelation;

    private GalleryPresenter presenter;
    private List<PopItem> popItemList = new ArrayList<>();

    @Override
    public int initContentView() {
        return R.layout.activity_gallery;
    }

    @Override
    public void initData() {
        caseInfo = (CaseInfo) getIntent().getExtras().get(Constants.CASE_DETAIL);
        mRelation = (Relation) getIntent().getExtras().get(Constants.RELATION);
        presenter = new GalleryPresenter();
        presenter.attachView(this);
        showPopWindow();
        getSupportFragmentManager().beginTransaction().add(R.id.gallery_frame_layout,new GalleryFragment()).commit();
    }


    @Override
    public void initUi() {
        header.setTitle("病历详情");
        header.setRightImage(R.mipmap.icon_selected);
        mGalleryHospitalTv.setText(caseInfo.getHospital());
        mGalleryDepartmentTv.setText(caseInfo.getDepartment());
        mGalleryDateTv.setText(caseInfo.getCreateTime());
        mGalleryDescTv.setText(caseInfo.getDescription());
        mGalleryNameTv.setText(mRelation.getRelationName());
        header.setRightBtnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.showPopupWindow(view);
            }
        });
    }

    @Override
    public List<String> getImagePath() {
        List<String> imageList = new ArrayList<>();
        String[] split = caseInfo.getImagePath().split(",");
        for (String s: split) {
            imageList.add(s);
        }
        return imageList;
    }

    @Override
    public void showPopWindow() {
        PopItem editPopItem = new PopItem();
        editPopItem.setTitle("编辑");
        editPopItem.setImageId(R.mipmap.icon_pop_edit);
        popItemList.add(editPopItem);
        PopItem deletePopItem = new PopItem();
        deletePopItem.setTitle("删除");
        deletePopItem.setImageId(R.mipmap.icon_pop_delete);
        popItemList.add(deletePopItem);
        if(popupWindow == null){
            popupWindow = new MenuPopwindow(GalleryActivity.this,popItemList);
        }
        popupWindow.setOnItemClick(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0://编辑
                        editCase();
                        break;
                    case 1://删除
                        deleteCase();
                        break;
                }
            }
        });
    }

    @Override
    public void deleteCase() {
        showProgressDialog("删除中...");
        presenter.delete(caseInfo, caseInfo.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    toast("删除成功");
                    finish();
                }else {
                    toast("删除失败");
                }
                closeProgressDialog();
            }
        });
    }

    @Override
    public void editCase() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.RELATION,mRelation);
        skipActivity(GalleryActivity.this,AddCaseActivity.class,bundle);
    }

    @Override
    public void onPopupWindowItemClick(int position) {
        switch (position){
            case 0://编辑
                editCase();
                break;
            case 1://删除
                deleteCase();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        presenter.cancel();
        super.onDestroy();
    }
}
