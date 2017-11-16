package com.busilinq.casepocket.presenter;

import com.busilinq.casepocket.bean.User;
import com.busilinq.casepocket.db.CPDbApi;
import com.busilinq.casepocket.viewinterface.IPersonalView;

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

    User user;
    @Override
    public void attachView(IPersonalView view) {
        super.attachView(view);
        user =  CPDbApi.getInstance().getUser();
    }

    public void saveAvater(String avater){
        user.setAvater(avater);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(null == e){
                    System.out.println("====上传成功了===");
                }else{
                    System.out.println("======="+e.getMessage());
                }

            }
        });
    }


}
