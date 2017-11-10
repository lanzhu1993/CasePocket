package com.busilinq.casepocket.update;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;

import com.busilinq.casepocket.R;
import com.busilinq.casepocket.base.AppManager;
import com.busilinq.casepocket.base.BaseActivity;
import com.busilinq.casepocket.bean.UpdateInfo;
import com.busilinq.casepocket.utils.AppInfoUtils;
import com.busilinq.casepocket.widget.DeleteDialog;

import java.util.List;


/**
 * Created by dingyi on 2016/12/21.
 */

public class UpdateManager {
    private Context context;
    private UpdateInfo mUpdateInfo;
    private ApkDownloadManager manager;
    private DeleteDialog mAlertDialog;

    public UpdateManager(Context context) {
        this.context = context;
        this.manager = new ApkDownloadManager(context);
    }

    public void getUpdateInfo(final BaseActivity activity, final boolean isLogin) {

    }

    public void setmUpdateInfo(UpdateInfo mUpdateInfo) {
        this.mUpdateInfo = mUpdateInfo;
    }


    public void download(String url) {
        if (!canDownloadState()) {
            Toast.makeText(context, "下载服务停用,请您启用", Toast.LENGTH_SHORT).show();
            showDownloadSetting();
            return;
        }
        manager.download(context, url, context.getResources().getString(R.string.app_name));

    }

    private void showDownloadSetting() {
        String packageName = "com.android.providers.downloads";
        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + packageName));
        if (intentAvailable(intent)) {
            context.startActivity(intent);
        }
    }

    private boolean intentAvailable(Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    private boolean canDownloadState() {
        try {
            int state = context.getPackageManager().getApplicationEnabledSetting("com.android.providers.downloads");

            if (state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER
                    || state == PackageManager.COMPONENT_ENABLED_STATE_DISABLED_UNTIL_USED) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 检查是否能够进行版本更新
     *
     * @return false：不能，true：能
     */
    private boolean versionCheck() {
        boolean bRet = false;
        if (null == mUpdateInfo)
            return bRet;
        String versionName = AppInfoUtils.getVersionName(context);
        int versionCode = AppInfoUtils.getVersionCode(context);
        if (Double.parseDouble(String.valueOf(versionCode)) < Double.parseDouble(mUpdateInfo.getNewVersion()))
            bRet = true;
        return bRet;
    }

    public void updateAlertDialog(String msg) {
        if (mAlertDialog == null) {
            mAlertDialog = new DeleteDialog(context);
            mAlertDialog.setOnClickListener(onClickListener);
            mAlertDialog.setDialogTitle("版本更新");
        }
        mAlertDialog.setDialogMessage(msg);
        mAlertDialog.show();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.tv_cancle:
                    mAlertDialog.dismiss();
                    break;
                case R.id.tv_certain:
                    mAlertDialog.dismiss();
                    download(mUpdateInfo.getApkUrl());
                    break;
            }
        }
    };

}
