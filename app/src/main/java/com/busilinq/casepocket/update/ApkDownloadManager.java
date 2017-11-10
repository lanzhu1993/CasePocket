package com.busilinq.casepocket.update;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

import com.busilinq.casepocket.utils.ACache;


/**
 * Created by dingyi on 2016/12/21.
 */

public class ApkDownloadManager {
    private final String KEY_DOWNLOAD_ID = "downloadId";
    private DownloadManager dm;
    private Context context;

    public ApkDownloadManager(Context context) {
        this.context = context;
        dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    public void download(Context context, String url, String title) {
        ACache cache = ACache.get(context);
        long downloadId = -1L;
        try {
            downloadId = Long.parseLong(cache.getAsString(KEY_DOWNLOAD_ID));
        }catch (Exception e){
            downloadId = -1L;
        }
        if (downloadId != -1L) {
            int status = getDownloadStatus(downloadId);
            if (status == DownloadManager.STATUS_SUCCESSFUL) {
                //启动更新界面
                Uri uri = getDownloadUri(downloadId);
                if (uri != null) {
                    if (compare(getApkInfo(context, uri.getPath()), context)) {
                        startInstall(context, uri);
                        return;
                    } else {
                        getDm().remove(downloadId);
                        cache.remove(KEY_DOWNLOAD_ID);
                    }
                }
                start(context, url, title);
            } else if (status == DownloadManager.STATUS_FAILED) {
                start(context, url, title);
            } else {
                cache.remove(KEY_DOWNLOAD_ID);
            }
        } else {
            start(context, url, title);
        }
    }



    private void start(Context context, String url, String title) {
        long id = startDownload(url,
                title, "下载完成后点击打开");
        ACache.get(context).put(KEY_DOWNLOAD_ID, Long.toString(id));
    }

    /**
     * @param uri
     * @param title
     * @param description
     * @return download id
     */
    private long startDownload(String uri, String title, String description) {
        DownloadManager.Request req = new DownloadManager.Request(Uri.parse(uri));

        //req.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE);
        req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        req.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, "update.apk");
        req.setTitle(title);
        req.setDescription(description);
        return dm.enqueue(req);
    }

    /**
     * 获取文件保存的路径
     *
     * @param downloadId an ID for the download, unique across the system.
     *                   This ID is used to make future calls related to this download.
     * @return file path
     * @see ApkDownloadManager#getDownloadUri(long)
     */
    private String getDownloadPath(long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = dm.query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getString(c.getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI));
                }
            } finally {
                c.close();
            }
        }
        return null;
    }

    /**
     * 获取保存文件的地址
     *
     * @param downloadId an ID for the download, unique across the system.
     *                   This ID is used to make future calls related to this download.
     * @see ApkDownloadManager#getDownloadPath(long)
     */
    private Uri getDownloadUri(long downloadId) {
        return dm.getUriForDownloadedFile(downloadId);
    }


    private DownloadManager getDm() {
        return dm;
    }

    private int getDownloadStatus(long downloadId) {
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor c = dm.query(query);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));

                }
            } finally {
                c.close();
            }
        }
        return -1;
    }

    /**
     * 获取apk程序信息[packageName,versionName...]
     *
     * @param context Context
     * @param path    apk path
     */
    private static PackageInfo getApkInfo(Context context, String path) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            //String packageName = info.packageName;
            //String version = info.versionName;
            //Log.d(TAG, "packageName:" + packageName + ";version:" + version);
            //String appName = pm.getApplicationLabel(appInfo).toString();
            //Drawable icon = pm.getApplicationIcon(appInfo);//得到图标信息
            return info;
        }
        return null;
    }

    /**
     * 下载的apk和当前程序版本比较
     *
     * @param apkInfo apk file's packageInfo
     * @param context Context
     * @return 如果当前应用版本小于apk的版本则返回true
     */
    private boolean compare(PackageInfo apkInfo, Context context) {
        if (apkInfo == null) {
            return false;
        }
        String localPackage = context.getPackageName();
        if (apkInfo.packageName.equals(localPackage)) {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(localPackage, 0);
                if (apkInfo.versionCode > packageInfo.versionCode) {
                    return true;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void startInstall(Context context, Uri uri) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.setDataAndType(uri, "application/vnd.android.package-archive");
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(install);
    }
}
