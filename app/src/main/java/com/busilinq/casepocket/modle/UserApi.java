package com.busilinq.casepocket.modle;

import android.content.Context;

import com.busilinq.casepocket.bean.UpdateInfo;
import com.busilinq.casepocket.bean.User;

import java.io.File;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import rx.Subscription;

/**
 * Created by Administrator on 2017/4/1.
 */

public class UserApi {

    private static UserApi userApi = null;

    private UserApi(){}

    public static UserApi getUserApi(){
        if(null == userApi){
            userApi = new UserApi();
        }
        return userApi;
    }

    /**
     * 获取短信验证码
     * @param phone
     * @param template
     * @param listener
     */
    public Subscription requestSMSCode(String phone, String template, QueryListener<Integer> listener){
       return BmobSMS.requestSMSCode(phone,template,listener);
    }

    /**
     * 注册用户
     * @param username
     * @param password
     * @param phone
     * @param code
     * @param listener
     */
    public Subscription signOrLogin(String username, String password, String phone, String code, SaveListener<User> listener){
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setPhone(phone);
        return user.signOrLogin(code,listener);
    }

    public Subscription signUp(String username , String password, String phone , SaveListener<User> listener){
        User user = new User();
        user.setMobilePhoneNumber(phone);
        user.setUsername(username);
        user.setPassword(password);
        return user.signUp(listener);

    }
    /**
     * 验证短信验证码
     * @param phoneNumber
     * @param smsCode
     * @param listener
     */
    public Subscription verifySmsCode(String phoneNumber, String smsCode, UpdateListener listener){
        return BmobSMS.verifySmsCode(phoneNumber,smsCode,listener);
    }


    /**
     * 用户登录
     * @param account
     * @param password
     * @param listener
     */
    public Subscription login(String account, String password, SaveListener<User> listener){
        User user = new User();
        user.setUsername(account);
        user.setPassword(password);
        return user.login(listener);
    }


    /**
     * 通过短信重置密码
     * @param smsCode
     * @param newPassword
     * @param listener
     * @return
     */
    public Subscription resetPasswordBySMSCode(String smsCode, String newPassword, UpdateListener listener){
        return BmobUser.resetPasswordBySMSCode(smsCode,newPassword,listener);
    }


    /**
     * 更新用户头像
     * @param path
     * @param listener
     * @return
     */
   /* public void uploadAvater(final Context context , String path, final UpdateListener listener){
        new BitmapCompressUtil(context).bitmapCompress(path, new BitmapCompressUtil.BitmapCompressCallback() {
            @Override
            public void onCompressSuccess(String fileOutputPath) {
                final BmobFile bmobFile = new BmobFile(new File(fileOutputPath));
                bmobFile.uploadblock(new UploadFileListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e == null ){
                            User user = BmobUser.getCurrentUser(User.class);
                            user.setAvater(bmobFile);
                            user.update(ACache.get(context).getAsString(Constant.OBJECT_ID),listener);
                        }
                    }
                });
            }

            @Override
            public void onCompressFailure(String t) {

            }
        });
    }
*/
    /**查询用户
     * @param username
     * @param limit
     * @param listener
     */
    public Subscription queryUsers(String username, int limit, final FindListener<User> listener){
        BmobQuery<User> query = new BmobQuery<>();
        //去掉当前用户
        try {
            BmobUser user = BmobUser.getCurrentUser(User.class);
            query.addWhereNotEqualTo("username",user.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
        }
        query.addWhereContains("username", username);
        query.setLimit(limit);
        query.order("-createdAt");
        return query.findObjects(listener);
    }

    /**查询用户信息
     * @param objectId
     * @param listener
     */
    public Subscription queryUserInfo(String objectId, final QueryListener<User> listener){
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("objectId", objectId);
        return query.getObject(objectId, listener);
    }


    public Subscription queryUpdateInfo(SQLQueryListener<UpdateInfo> listener){
        BmobQuery<UpdateInfo> query = new BmobQuery<>();
        return query.doSQLQuery("select * from UpdateInfo",listener);
    }

}
