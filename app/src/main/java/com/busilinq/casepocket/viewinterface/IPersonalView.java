package com.busilinq.casepocket.viewinterface;

/**
 * 描述：
 * <p>
 * 创建时间： 2017/11/16  15:25
 * author :shiquan.lu
 * email: 1113799552@qq.com
 */

public interface IPersonalView extends IBaseView {

    void saveAvater(String avater);

    void upLoadAvater();

    void updateSex(String sex);

    void updateNickName(String nickName);

    void updateBirth(String birth);

    void showUserInfo();

    void showInputDialog();

    void showBirthDialog();

    void intoQRCodeActivity();

}
