package com.busilinq.casepocket.presenter;

import com.afollestad.materialdialogs.MaterialDialog;
import com.busilinq.casepocket.R;
import com.busilinq.casepocket.bean.User;
import com.busilinq.casepocket.db.CPDbApi;
import com.busilinq.casepocket.modle.UserApi;
import com.busilinq.casepocket.utils.ToastUtils;
import com.busilinq.casepocket.viewinterface.IPersonalView;
import com.busilinq.casepocket.widget.LoadDialogView;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 描述：
 * <p>
 * 创建时间： 2017/11/16  15:26
 * author :shiquan.lu
 * email: 1113799552@qq.com
 */

public class PersonalPresenter extends BasePresenter<IPersonalView> {


    UserApi userApi;
    @Override
    public void attachView(IPersonalView view) {
        super.attachView(view);
        userApi = UserApi.getUserApi();
    }

    public void saveAvater(String avater){
        LoadDialogView.showDialog(mBaseView.getContext(),"请稍等...");
        userApi.saveAvater(avater, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(null == e){
                    //更新UI
                    saveUser(BmobUser.getCurrentUser(User.class));
                    mBaseView.showUserInfo();
                    ToastUtils.showToast("修改成功");
                }else{
                    ToastUtils.showToast("修改失败");
                }
                LoadDialogView.dismssDialog();
            }
        });
    }


    public void updateSex(String sex) {
        LoadDialogView.showDialog(mBaseView.getContext(),"请稍等...");
        userApi.updateSex(sex, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(null == e){
                    saveUser(BmobUser.getCurrentUser(User.class));
                    mBaseView.showUserInfo();
                    ToastUtils.showToast("修改成功");
                }else{
                    ToastUtils.showToast("修改失败");
                }
                LoadDialogView.dismssDialog();
            }
        });
    }

    public void updateNickName(String nickname){
        LoadDialogView.showDialog(mBaseView.getContext(),"请稍等...");
        userApi.updateNickNmae(nickname, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(null == e){
                    saveUser(BmobUser.getCurrentUser(User.class));
                    mBaseView.showUserInfo();
                    ToastUtils.showToast("修改成功");
                }else{
                    ToastUtils.showToast("修改失败");
                }
                LoadDialogView.dismssDialog();
            }
        });
    }

    public void updateBirth(String birth){
        LoadDialogView.showDialog(mBaseView.getContext(),"请稍等...");
        userApi.updateBirth(birth, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(null == e){
                    saveUser(BmobUser.getCurrentUser(User.class));
                    mBaseView.showUserInfo();
                    ToastUtils.showToast("修改成功");
                }else{
                    ToastUtils.showToast("修改失败");
                }
                LoadDialogView.dismssDialog();
            }
        });
    }

    public void saveUser(User user){
        CPDbApi.getInstance().saveUser(user);
    }
}
