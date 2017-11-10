package com.busilinq.casepocket.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.busilinq.casepocket.R;
import com.busilinq.casepocket.bean.Relation;
import com.busilinq.casepocket.listener.OnItemClickListener;
import com.busilinq.casepocket.utils.DateUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/4
 * author: shiquan.lu
 */

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> implements View.OnClickListener{

    OnItemClickListener onItemClickListener;

    private int clickPosition = 0;

    private Context context;
    private LayoutInflater mLayoutInflater;
    private List<Relation> result;

    public MemberAdapter(Context context, List<Relation> result) {
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.result = result;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.item_member, parent, false);
        return new ViewHolder(view,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.item_name.setText(result.get(position).getRelationName());
        holder.item_sex.setText(result.get(position).getSex());
        holder.item_idcard.setText(result.get(position).getIdCard());
        String age = String.valueOf(Integer.valueOf(DateUtil.getDate2yyyy())
                - Integer.valueOf(result.get(position).getIdCard().substring(6,10)));
        holder.item_age.setText(age);
        if(position == 0){
            holder.item_people.setImageResource(R.mipmap.icon_main_people);
        }else{
            holder.item_people.setImageResource(R.mipmap.icon_default_people);
        }
        if (position == clickPosition) {
            holder.item_check_rb.setChecked(true);
        } else {
            holder.item_check_rb.setChecked(false);
        }
        holder.item_check_rb.setTag(position);
        holder.item_check_rb.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public void refresh(List<Relation> mMemberList) {
        this.result = mMemberList;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.item_check_rb:
                clickPosition = (Integer) view.getTag();
                notifyDataSetChanged();
                break;
        }
    }

    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        OnItemClickListener listener;

        @Bind(R.id.item_name_tv)
        TextView item_name;
        @Bind(R.id.item_age_tv)
        TextView item_age;
        @Bind(R.id.item_sex_tv)
        TextView item_sex;
        @Bind(R.id.item_idcard_tv)
        TextView item_idcard;
        @Bind(R.id.item_check_rb)
        RadioButton item_check_rb;
        @Bind(R.id.item_people)
        ImageView item_people;
        @Bind(R.id.item_edit)
        Button item_edit;
        @Bind(R.id.item_delete)
        Button item_delete;

        public ViewHolder(View itemView,OnItemClickListener listener) {
            super(itemView);
            this.listener = listener;
            ButterKnife.bind(this,itemView);
            item_edit.setOnClickListener(this);
            item_delete.setOnClickListener(this);
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

    public int getClickPosition() {
        return clickPosition;
    }
}
