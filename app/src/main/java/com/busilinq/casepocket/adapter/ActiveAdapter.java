package com.busilinq.casepocket.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.busilinq.casepocket.R;
import com.busilinq.casepocket.bean.EvaluationInfo;
import com.busilinq.casepocket.bean.User;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * 描述：动态适配器
 * <p>
 * 创建时间： 2017/11/22  11:21
 * author :shiquan.lu
 * email: 1113799552@qq.com
 */

public class ActiveAdapter extends BaseQuickAdapter<EvaluationInfo,BaseViewHolder> {

    private Context mContext;

    public ActiveAdapter(Context context,@Nullable List<EvaluationInfo> data) {
        super(R.layout.layout_active_item,data);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, EvaluationInfo item) {
        helper.setText(R.id.item_active_content_tv,item.getContent());
        helper.setText(R.id.item_active_name_tv,item.getUserName());
        helper.setText(R.id.item_active_time_tv,item.getCreatedAt());
        Glide.with(mContext)
                .load(item.getAvater())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.mipmap.icon_add_avater)
                .into((ImageView) helper.getView(R.id.item_active_avater_iv));
        User currentUser = BmobUser.getCurrentUser(User.class);
        if(currentUser.getObjectId().equals(item.getEvalutionId())){
            helper.getView(R.id.item_active_delete_tv).setVisibility(View.VISIBLE);
        }else{
            helper.getView(R.id.item_active_delete_tv).setVisibility(View.INVISIBLE);
        }

        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
        String attachments = item.getAttachments();
        if (attachments != null) {
            String[] split = attachments.split(",");
            for(int j=0;j<split.length;j++){
                ImageInfo info = new ImageInfo();
                System.out.println("======"+split[j]);
                info.setThumbnailUrl(split[j]);
                info.setBigImageUrl(split[j]);
                imageInfo.add(info);
            }
        }
        NineGridView nineGridView = helper.getView(R.id.item_active_nineview);
        nineGridView.setAdapter(new NineGridViewClickAdapter(mContext,imageInfo));
    }
}
