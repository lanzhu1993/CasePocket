package com.busilinq.casepocket.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.busilinq.casepocket.R;
import com.busilinq.casepocket.utils.Constants;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 描述：选择图片适配器
 * <p>
 * 创建时间： 2017/11/16  9:59
 * author :shiquan.lu
 * email: 1113799552@qq.com
 */

public class SelectPhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> photoPaths = new ArrayList<String>();
    private LayoutInflater inflater;

    private Context mContext;
    private View.OnClickListener mOnClickListener;



    public SelectPhotoAdapter(Context mContext, List<String> photoPaths) {
        this.photoPaths = photoPaths;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);

    }


    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case Constants.TYPE_ADD:
                itemView = inflater.inflate(R.layout.layout_add_img_item, parent, false);
                holder = new PhotoAddViewHolder(itemView);
                break;
            case Constants.TYPE_PHOTO:
                itemView = inflater.inflate(R.layout.layout_select_img_item, parent, false);
                holder = new PhotoViewHolder(itemView);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == Constants.TYPE_PHOTO) {
            Glide.with(mContext)
                    .load(photoPaths.get(position))
                    .placeholder(R.mipmap.icon_photo_default_bg)
                    .error(R.mipmap.icon_photo_error_bg)
                    .thumbnail(0.1f)
                    .into(((PhotoViewHolder)holder).ivPhoto);
        }else {
            ((PhotoAddViewHolder)holder).ivAdd.setOnClickListener(mOnClickListener);
        }
    }



    @Override public int getItemCount() {
        int count = photoPaths.size() + 1;
        if (count > Constants.PHOTO_MAX) {
            count = Constants.PHOTO_MAX;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == photoPaths.size() && position != Constants.PHOTO_MAX) ? Constants.TYPE_ADD : Constants.TYPE_PHOTO;
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto   = (ImageView) itemView.findViewById(R.id.item_select_iv);
        }
    }

    public  class PhotoAddViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivAdd;
        public PhotoAddViewHolder(View itemView) {
            super(itemView);
            ivAdd   = (ImageView) itemView.findViewById(R.id.item_add_iv);
        }
    }


    public void refresh(List<String> mData){
        this.photoPaths = mData;
        this.notifyDataSetChanged();
    }

    public void setOnClickListener(View.OnClickListener onClickListener){
        this.mOnClickListener = onClickListener;
    }
}
