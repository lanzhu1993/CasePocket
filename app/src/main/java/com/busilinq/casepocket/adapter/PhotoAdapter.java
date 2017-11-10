package com.busilinq.casepocket.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.busilinq.casepocket.R;
import com.busilinq.casepocket.listener.OnItemClickListener;
import com.busilinq.casepocket.listener.RecyclerItemClickListener;

import java.util.List;


/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/4/14
 * author: shiquan.lu
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    OnItemClickListener onItemClickListener;

    private Context context;
    private LayoutInflater mLayoutInflater;
    private List<String> result;

    public PhotoAdapter(Context context, List<String> result) {
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.result = result;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_photo, parent, false),onItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(context)
                .load(result.get(position))
                .centerCrop()
                .into(holder.ivPhoto);

    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        OnItemClickListener listener;

        private ImageView ivPhoto;
        private Button ivPhotoDelete;

        public ViewHolder(View itemView,OnItemClickListener listener) {
            super(itemView);
            this.listener = listener;
            ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
            ivPhotoDelete = (Button) itemView.findViewById(R.id.iv_photo_delete);
            ivPhotoDelete.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if(listener != null){
                listener.onItemClick(view,getLayoutPosition());
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }





}
