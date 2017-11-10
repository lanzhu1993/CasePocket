package com.busilinq.casepocket.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.busilinq.casepocket.R;
import com.busilinq.casepocket.base.BaseActivity;


public class HeaderLayoutView extends RelativeLayout {
	
	private BaseActivity mContext;
	private LayoutInflater mInflater;
	private TextView mTitleTv;
	private ImageView mLeftIv;
	private TextView mRightTv;
	private ImageView mRightBtn;
	private View view;
	private RelativeLayout search_layout;

	public HeaderLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public HeaderLayoutView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = (BaseActivity) context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = mInflater.inflate(R.layout.common_titlebar, this);
		mRightTv = (TextView) view.findViewById(R.id.right);
		mLeftIv = (ImageView) view.findViewById(R.id.left);
		search_layout = (RelativeLayout) view.findViewById(R.id.main_search_layout);
		mLeftIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mContext.onBackPressed();
			}
		});

		mTitleTv = (TextView) view.findViewById(R.id.title);
		mRightBtn = (ImageView) view.findViewById(R.id.title_right_iv);

	}

	public HeaderLayoutView(Context context) {
		super(context);
	}
	
	
	/**
	 * 设置标题
	 * @param title
	 */
	public void setTitle(String title){
		mTitleTv.setVisibility(View.VISIBLE);
		mTitleTv.setText(title);
	}
	
	/**
	 * 设置左边返回键是否可见
=	 */
	public void setLeftVisible(int visible){
		mLeftIv.setVisibility(visible);
	}



	/**
	 * 设置右边标题
	 * @param title
	 */
	public void setRightTitle(String title){
		mRightTv.setVisibility(View.VISIBLE);
		mRightTv.setText(title);
	}
	
	public void setRightImage(int res){
		mRightBtn.setVisibility(View.VISIBLE);
		mRightBtn.setImageResource(res);
	}


	public void setRightTvOnClickListener(OnClickListener onClickListener){
		mRightTv.setOnClickListener(onClickListener);
	}


	public void setRightBtnOnClickListener(OnClickListener onClickListener){
		mRightBtn.setVisibility(View.VISIBLE);
		mRightBtn.setOnClickListener(onClickListener);
	}


	public void setSearchLayoutOnClickListener(OnClickListener onClickListener){
		search_layout.setVisibility(View.VISIBLE);
		search_layout.setOnClickListener(onClickListener);
	}

	public void setSearchLayoutInVisible(){
		search_layout.setVisibility(GONE);
	}

}
