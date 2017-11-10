package com.busilinq.casepocket.ui;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.busilinq.casepocket.R;
import com.busilinq.casepocket.base.BaseActivity;
import com.busilinq.casepocket.bean.Relation;
import com.busilinq.casepocket.presenter.AddMembPresenter;
import com.busilinq.casepocket.presenter.BasePresenter;
import com.busilinq.casepocket.utils.Constants;
import com.busilinq.casepocket.utils.IDCardUtils;
import com.busilinq.casepocket.viewinterface.IAddMembView;
import com.busilinq.casepocket.widget.HeaderLayoutView;

import butterknife.Bind;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 描述：添加新的就诊人
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/4
 * author: shiquan.lu
 */

public class AddMemberActivity extends BaseActivity implements IAddMembView{

    @Bind(R.id.header)
    HeaderLayoutView header;

    @Bind(R.id.addmemb_sex_rg)
    RadioGroup mAddMembRg;

    @Bind(R.id.addmemb_name_et)
    EditText mAddMembNameEt;

    @Bind(R.id.addmemb_idcard_et)
    EditText mAddMembIDCardEt;

    @Bind(R.id.addmemb_sex_male)
    RadioButton mAddMembMale;

    @Bind(R.id.addmemb_sex_female)
    RadioButton mAddMembFemale;


    private String sex = "男";

    private AddMembPresenter presenter;

    private Relation relation;


    @Override
    public int initContentView() {
        return R.layout.activity_addmember;
    }

    @Override
    public void initData() {
        try {
            relation = (Relation) getIntent().getExtras().getSerializable(Constants.RELATION);
        }catch (Exception e){
            e.printStackTrace();
        }
        presenter = new AddMembPresenter();
        presenter.attachView(this);
    }

    private void saveRelationData() {
        String name = mAddMembNameEt.getEditableText().toString().trim();
        String IDNumber = mAddMembIDCardEt.getEditableText().toString().trim();
        if(TextUtils.isEmpty(name)){
            toast("请输入姓名");
            return;
        }
        if(TextUtils.isEmpty(sex)){
            toast("请选择性别");
            return;
        }
        if(TextUtils.isEmpty(IDNumber)){
            toast("请输入身份证号");
            return;
        }
        if(!TextUtils.isEmpty(IDCardUtils.IDCardValidate(IDNumber))){
            toast(IDCardUtils.IDCardValidate(IDNumber));
            return;
        }
        showProgressDialog("正在上传中...");
        presenter.save(name, sex, IDNumber, new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    toast("添加成功");
                    finish();
                }else{
                    if(e.getErrorCode() == 401){
                        toast("就诊人已存在！");
                    }else{
                        toast("添加失败");
                    }
                }
                closeProgressDialog();
            }
        });
    }

    @Override
    public void initUi() {
        header.setTitle("添加新的就诊人");
        header.setRightTitle("保存");
        header.setRightTvOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(relation == null ){
                    saveRelationData();
                }else{
                    updateRelationData();
                }
            }
        });

        if(null != relation){
            mAddMembNameEt.setText(relation.getRelationName());
            mAddMembIDCardEt.setText(relation.getIdCard());
            if(relation.getSex() != null && relation.getSex().equals("女")){
                mAddMembFemale.setChecked(true);
                sex = "女";
            }else {
                mAddMembMale.setChecked(true);
                sex = "男";
            }
        }

        mAddMembRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.addmemb_sex_male:
                        sex = "男";
                        break;
                    case R.id.addmemb_sex_female:
                        sex = "女";
                        break;
                }
            }
        });
    }

    private void updateRelationData() {
        String name = mAddMembNameEt.getEditableText().toString().trim();
        String IDNumber = mAddMembIDCardEt.getEditableText().toString().trim();
        if(TextUtils.isEmpty(name)){
            toast("请输入姓名");
            return;
        }
        if(TextUtils.isEmpty(sex)){
            toast("请选择性别");
            return;
        }
        if(TextUtils.isEmpty(IDNumber)){
            toast("请输入身份证号");
            return;
        }
        if(!TextUtils.isEmpty(IDCardUtils.IDCardValidate(IDNumber))){
            toast(IDCardUtils.IDCardValidate(IDNumber));
            return;
        }
        showProgressDialog("正在修改中...");
        presenter.update(relation, name , sex, IDNumber, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    toast("修改成功");
                    finish();
                }else{
                    toast("修改失败");
                }
                closeProgressDialog();
            }
        });
    }


}
