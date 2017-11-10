package com.busilinq.casepocket.modle;

import com.busilinq.casepocket.bean.CaseInfo;
import com.busilinq.casepocket.bean.ImagePath;
import com.busilinq.casepocket.bean.Relation;
import com.busilinq.casepocket.bean.User;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import rx.Subscription;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/5
 * author: shiquan.lu
 */

public class  CaseApi {

    private static CaseApi caseApi = null;

    private CaseApi(){}

    public static CaseApi getCaseApi(){
        if(null == caseApi){
            caseApi = new CaseApi();
        }
        return caseApi;
    }

    /**
     * 添加病例文字
     * @param caseInfo
     * @param listener
     * @return
     */
    public Subscription save(CaseInfo caseInfo,SaveListener<String> listener){
        return caseInfo.save(listener);
    }

    /**
     * 添加病例图片
     * @param imagePath
     * @param listener
     * @return
     */
    public Subscription saveImage(ImagePath imagePath, SaveListener<String> listener){
        return imagePath.save(listener);
    }

    /**
     * 查询病例文字信息列表
     * @param caseObjectId
     * @param listener
     * @return
     */
    public Subscription findCaseInfo(String caseObjectId ,FindListener<CaseInfo> listener){
        BmobQuery<CaseInfo> query = new BmobQuery<>();
        query.addWhereEqualTo("caseObjectID", caseObjectId);
        return query.findObjects(listener);
    }

    /**
     * 查询病例文字列表
     * @param caseObjectId
     * @param listener
     * @return
     */
    public Subscription findImageInfo(String caseObjectId ,FindListener<ImagePath> listener){
        BmobQuery<ImagePath> query = new BmobQuery<>();
        query.addWhereEqualTo("caseObjectID", caseObjectId);
        return query.findObjects(listener);
    }

    /**
     * 删除病历信息
     * @param caseInfo
     * @param caseObjectId
     * @param listener
     * @return
     */
    public Subscription delete(CaseInfo caseInfo, String caseObjectId, UpdateListener listener){
        return caseInfo.delete(caseObjectId,listener);
    }

    /**
     * 跟新
     * @param caseInfo
     * @param caseObjectId
     * @param listener
     * @return
     */
    public Subscription update(CaseInfo caseInfo, String caseObjectId, UpdateListener listener){
        return caseInfo.update(caseObjectId,listener);
    }

}
