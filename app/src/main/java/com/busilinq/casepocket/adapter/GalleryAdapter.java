package com.busilinq.casepocket.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.busilinq.casepocket.R;
import com.busilinq.casepocket.ui.PictureActivity;
import com.busilinq.casepocket.utils.Constants;
import com.busilinq.casepocket.utils.ToActivityUtil;


import java.util.List;


public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder> {

    private Activity mContext;
    private List<String>  mList;
    private CardAdapterHelper mCardAdpaterHelper = new CardAdapterHelper();
    public GalleryAdapter(Activity mContext) {
        this.mContext = mContext;
    }

    public GalleryAdapter(Activity mContext, List<String>  mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    public  void setList(List<String>  list){
        this.mList = list;
    }

    @Override
    public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_gallery,parent,false);
        mCardAdpaterHelper.onCreateViewHolder(parent,view);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, final int position) {
        mCardAdpaterHelper.onBindViewHolder(holder.itemView,position,getItemCount());
        Glide.with(mContext).load(mList.get(position))
                .centerCrop()
                .placeholder(R.mipmap.icon_place_holder)
                .error(R.mipmap.icon_place_holder)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .crossFade()
                .into(holder.img);
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.IMG_URL,mList.get(position));
                ToActivityUtil.showDataActivity(mContext, PictureActivity.class,false,bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList  == null ? 0 : mList.size() ;
    }

    static class GalleryViewHolder extends RecyclerView.ViewHolder{

       ImageView img;

        public GalleryViewHolder(View itemView) {
            super(itemView);
           img = (ImageView) itemView.findViewById(R.id.img_item_gallery);
        }
    }
}
