package com.busilinq.casepocket.viewinterface;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public interface IBaseView {
    void toast(String msg);
    void showLoading(int visibility);
    void showLoadingError(int errorType);
    Context getContext();

    void showProgressDialog(String msg);
    void closeProgressDialog();

    void onRefresh(boolean bRefresh);

    void showActivity(Activity activity, Class<?> cls, Bundle bundle);
    void showActivity(Activity activity, Class<?> cls);
    void showActivity(Activity activity, Intent intent);
    void skipActivity(Activity activity, Class<?> cls, Bundle bundle);
    void skipActivity(Activity activity, Class<?> cls);
    void skipActivity(Activity activity, Intent intent);

}
