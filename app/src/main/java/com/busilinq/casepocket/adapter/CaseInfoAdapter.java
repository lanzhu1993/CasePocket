package com.busilinq.casepocket.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.busilinq.casepocket.R;
import com.busilinq.casepocket.bean.CaseInfo;
import com.busilinq.casepocket.bean.ImagePath;
import com.busilinq.casepocket.bean.Relation;
import com.busilinq.casepocket.listener.OnItemClickListener;
import com.busilinq.casepocket.listener.RecyclerItemClickListener;
import com.busilinq.casepocket.ui.PictureActivity;
import com.busilinq.casepocket.utils.AES;
import com.busilinq.casepocket.utils.CloseUtils;
import com.busilinq.casepocket.utils.Constants;
import com.busilinq.casepocket.utils.ToActivityUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/5
 * author: shiquan.lu
 */

public class CaseInfoAdapter extends RecyclerView.Adapter<CaseInfoAdapter.ViewHolder> {

    OnItemClickListener onItemClickListener;

    private Context context;
    private LayoutInflater mLayoutInflater;
    private List<CaseInfo> caseInfoList;
    private List<List<String>> imageList;
   // private CaseImageAdapter caseImageAdapter;

    private List<String> decrypt_list = new ArrayList<>();
    /**
     * 图片存放根目录
     */
    private final String mImageRootDir = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/CasePocket";

    public CaseInfoAdapter(Context context, List<CaseInfo> result,List<List<String>> imageList) {
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.caseInfoList = result;
        this.imageList = imageList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_case_detail,parent,false);
        return new ViewHolder(view,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.item_date.setText(caseInfoList.get(position).getCreateTime());
        holder.item_hospital.setText(caseInfoList.get(position).getHospital());
        holder.item_description.setText(caseInfoList.get(position).getDescription());
        holder.item_recyclerview.setTag(imageList.get(holder.getLayoutPosition()));
        if(imageList.get(position).equals(holder.item_recyclerview.getTag())){
            //getBmobFileList(imageList.get(holder.getLayoutPosition()));
            CaseImageAdapter caseImageAdapter = new CaseImageAdapter(context,imageList.get(holder.getLayoutPosition()));
            holder.item_recyclerview.setAdapter(caseImageAdapter);
            caseImageAdapter.refresh(imageList.get(holder.getLayoutPosition()));
        }
        holder.item_recyclerview.addOnItemTouchListener(new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position1) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.IMG_URL,imageList.get(holder.getLayoutPosition()).get(position1));
                ToActivityUtil.showDataActivity((Activity) context, PictureActivity.class,false,bundle);
            }
        }));
    }

    @Override
    public int getItemCount() {
        return caseInfoList.size();
    }

    public void refresh(List<CaseInfo> caseInfoList, List<List<String>> imagePathList) {
        this.caseInfoList = caseInfoList;
        this.imageList = imagePathList;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        OnItemClickListener listener;

        @Bind(R.id.item_date)
        TextView item_date;
        @Bind(R.id.item_hospital)
        TextView item_hospital;
        @Bind(R.id.item_description)
        TextView item_description;
        @Bind(R.id.item_layout)
        RelativeLayout item_layout;
        @Bind(R.id.item_recyclerview)
        RecyclerView item_recyclerview;

        public ViewHolder(View itemView,OnItemClickListener listener) {
            super(itemView);
            this.listener = listener;
            ButterKnife.bind(this,itemView);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            item_recyclerview.setLayoutManager(linearLayoutManager);
            item_layout.setOnClickListener(this);
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

    private void getBmobFileList(List<String> result){
        for (int i = 0;i<result.size();i++) {
            BmobFile bmobFile = new BmobFile();
            bmobFile.setUrl(result.get(i));
            System.out.println("================="+bmobFile.getFilename());
            String imagePath = result.get(i).split(",")[0];
            String[] split = imagePath.split("/");
            String[] orignSplit = result.get(i).split("/");
            final File decryptFile = new File(mImageRootDir + "/" + split[split.length - 1]);
            final File downloadFile = new File(mImageRootDir + "/" + orignSplit[orignSplit.length - 1]);
            System.out.println("=========decryptFile========"+decryptFile.getAbsolutePath());
            System.out.println("=========downloadFile========"+downloadFile.getAbsolutePath());
            final FileInputStream[] fis = {null};
            final FileOutputStream[] fos = {null};
            bmobFile.download(downloadFile, new DownloadFileListener() {
                @Override
                public void done(String s, BmobException e) {
                    try {
                        fis[0] = new FileInputStream(downloadFile);
                        fos[0] = new FileOutputStream(decryptFile);
                        AES.INSTANCE.decrypt(fis[0], fos[0]);
                        decrypt_list.add(decryptFile.getAbsolutePath());
                        System.out.println("=========="+decryptFile.getAbsolutePath());
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    } finally {
                        CloseUtils.closeQuietly(fos[0], fis[0]);
                    }
                }

                @Override
                public void onProgress(Integer integer, long l) {

                }
            });
        }
    }

}
