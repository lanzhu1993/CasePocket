package com.busilinq.casepocket.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.busilinq.casepocket.R;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/18
 * author: shiquan.lu
 */

public class DeleteDialog extends Dialog {

    private TextView tv_certain;
    private TextView tv_cancle;
    private TextView tv_title;
    private TextView tv_message;

    private int position;

    public DeleteDialog(Context context) {
        super(context);
        setParams();
    }




    public void setOnClickListener(android.view.View.OnClickListener listener){
        tv_certain.setOnClickListener(listener);
        tv_cancle.setOnClickListener(listener);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    private void setParams() {
        this.setContentView(R.layout.dialog_delete_layout);
        Window window = this.getWindow();
        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(R.color.colorTransparent);
        findViews(window);
    }

    private void findViews(Window window) {
        tv_certain = (TextView) window.findViewById(R.id.tv_certain);
        tv_cancle = (TextView) window.findViewById(R.id.tv_cancle);
        tv_title = (TextView) window.findViewById(R.id.tv_title);
        tv_message = (TextView) window.findViewById(R.id.tv_message);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setDialogTitle(String title){
        tv_title.setText(title);
    }

    public void setDialogMessage(String message){
        tv_message.setText(message);
    }
}
