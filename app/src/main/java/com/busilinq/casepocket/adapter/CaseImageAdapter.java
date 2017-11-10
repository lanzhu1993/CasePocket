package com.busilinq.casepocket.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.model.ImageVideoWrapper;
import com.bumptech.glide.load.resource.bitmap.BitmapDecoder;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapper;
import com.bumptech.glide.load.resource.gifbitmap.GifBitmapWrapperResourceEncoder;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.busilinq.casepocket.R;
import com.busilinq.casepocket.bean.ImagePath;
import com.busilinq.casepocket.listener.OnItemClickListener;
import com.busilinq.casepocket.utils.AES;
import com.busilinq.casepocket.utils.BitmapUtil;
import com.busilinq.casepocket.utils.CloseUtils;
import com.busilinq.casepocket.widget.pixelate.OnPixelateListener;
import com.busilinq.casepocket.widget.pixelate.Pixelate;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;


/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/4/14
 * author: shiquan.lu
 */
public class CaseImageAdapter extends RecyclerView.Adapter<CaseImageAdapter.ViewHolder> {

    OnItemClickListener onItemClickListener;

    private Context context;
    private LayoutInflater mLayoutInflater;
    private List<String> result;

    public CaseImageAdapter(Context context, List<String> result) {
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.result = result;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mLayoutInflater.inflate(R.layout.item_case_image, parent, false),onItemClickListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        System.out.println("==============="+position);
        Glide.with(context)
                .load(result.get(position))
                .asBitmap()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.ivPhoto);

      /*  byte[] decrypt = BitmapUtil.decrypt(BitmapUtil.Bitmap2Bytes(source), "lanzhu");
        Bitmap bitmap = BitmapUtil.Bytes2Bimap(decrypt);*/
    }


    @Override
    public int getItemCount() {
        return result.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        OnItemClickListener listener;
        private ImageView ivPhoto;


        public ViewHolder(View itemView,OnItemClickListener listener) {
            super(itemView);
            this.listener = listener;
            ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
            ivPhoto.setOnClickListener(this);
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


    public void refresh(List<String> imageList) {
        this.result = imageList;
        notifyDataSetChanged();
    }



}
