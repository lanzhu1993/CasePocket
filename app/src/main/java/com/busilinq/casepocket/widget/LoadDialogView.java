package com.busilinq.casepocket.widget;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.busilinq.casepocket.R;
import com.busilinq.casepocket.utils.ResourcesUtils;


public class LoadDialogView {

    private static Dialog dialog;
    private static View dialogView;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void showDialog(Context context, String message) {
        //防止快速显示,新对象覆盖旧对象,导致旧对象的关闭不了
        if (dialog != null) {
            if (dialog.isShowing()) {
                dismssDialog();
            }
        }
        dialogView = ResourcesUtils.findViewById(context, R.layout.load_dialog);
        TextView tvMessage = (TextView) dialogView.findViewById(R.id.tv_dialog_msg);
        if (!TextUtils.isEmpty(message)) {
            tvMessage.setText(message);
        }
        dialog = new AlertDialog.Builder(context, R.style.Dialog).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        dialog.setContentView(dialogView);
    }

    public static void dismssDialog() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

}
