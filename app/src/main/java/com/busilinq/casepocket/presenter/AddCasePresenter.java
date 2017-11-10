package com.busilinq.casepocket.presenter;

import com.busilinq.casepocket.bean.CaseInfo;
import com.busilinq.casepocket.bean.ImagePath;
import com.busilinq.casepocket.bean.Relation;
import com.busilinq.casepocket.modle.CaseApi;
import com.busilinq.casepocket.modle.RelationApi;
import com.busilinq.casepocket.viewinterface.IAddCaseView;

import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import rx.Subscription;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/4
 * author: shiquan.lu
 */

public class AddCasePresenter extends BasePresenter<IAddCaseView>{

    RelationApi relationApi;
    CaseApi caseApi;

    @Override
    public void attachView(IAddCaseView view) {
        super.attachView(view);
        relationApi = RelationApi.getRelationApi();
        caseApi = CaseApi.getCaseApi();
    }


    public void findOwnerInfo(FindListener<Relation> listener){
        addSubscription(relationApi.findOwnerInfo(listener));
    }

    /**
     * 保存病例文字信息
     * @param objectId
     * @param hospital
     * @param department
     * @param time
     * @param description
     * @param listener
     */
    public void save( CaseInfo caseInfo,String objectId,String hospital,String department, String time,String imagepath,String description,SaveListener<String> listener){
        caseInfo.setCaseObjectID(objectId);
        caseInfo.setCreateTime(time);
        caseInfo.setDepartment(department);
        caseInfo.setHospital(hospital);
        caseInfo.setDescription(description);
        caseInfo.setImagePath(imagepath);
        addSubscription(caseApi.save(caseInfo,listener));
    }

    /**
     * 保存图片信息
     * @param objectId
     * @param path
     * @param listener
     */
    public void saveImage( String objectId,String path,SaveListener<String> listener){
        ImagePath imagePath = new ImagePath();
        imagePath.setCaseObjectID(objectId);
        imagePath.setImagePath(path);
        addSubscription(caseApi.saveImage(imagePath,listener));
    }

}
