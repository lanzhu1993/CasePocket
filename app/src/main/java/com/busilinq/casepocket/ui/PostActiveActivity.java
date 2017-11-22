package com.busilinq.casepocket.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.busilinq.casepocket.R;
import com.busilinq.casepocket.adapter.GlideImageLoader;
import com.busilinq.casepocket.base.BaseActivity;
import com.busilinq.casepocket.bean.EvaluationInfo;
import com.busilinq.casepocket.bean.User;
import com.busilinq.casepocket.presenter.PostActivePresenter;
import com.busilinq.casepocket.utils.ImageUtils;
import com.busilinq.casepocket.utils.ThreadManager;
import com.busilinq.casepocket.utils.ToastUtils;
import com.busilinq.casepocket.viewinterface.IPostActiveView;
import com.busilinq.casepocket.widget.HeaderLayoutView;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * 描述 ：发布话题
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/11/15
 * author: shiquan.lu
 */

public class PostActiveActivity extends BaseActivity implements IPostActiveView {

    @Bind(R.id.header)
    HeaderLayoutView header;

    @Bind(R.id.post_active_recyclerview)
    RecyclerView mPostActiveRecyclerView;

    @Bind(R.id.post_active_active_et)
    EditText mmPostActiveActiveEt;


    PostActivePresenter presenter;

    private List<String> mPhotoList = new ArrayList<>();
    private GalleryConfig galleryConfig;
    private IHandlerCallBack iHandlerCallBack;

    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private final int UPLOAD_COMPELETED = 9;

    private List<String> compress_path = new ArrayList<>();
    private int currentPosition = 0;
    private String[] split;

    /**
     * 图片存放根目录
     */
    private final String mImageRootDir = Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/CasePocket";

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case UPLOAD_COMPELETED:
                    saveActiveData((String) msg.obj);
                    break;
            }
        }
    };
    private String active;


    @Override
    public int initContentView() {
        return R.layout.activity_post_active;
    }

    @Override
    public void initData() {
        presenter = new PostActivePresenter();
        presenter.attachView(this);
        GridLayoutManager layoutManager = new GridLayoutManager(this,4);
        mPostActiveRecyclerView.setLayoutManager(layoutManager);
        presenter.initAdapter();
        initGallery();
        initGalleryConfig();
    }

    private void initGalleryConfig() {
        galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())    // ImageLoader 加载框架（必填）
                .iHandlerCallBack(iHandlerCallBack)     // 监听接口（必填）
                .provider("com.yancy.gallerypickdemo.fileprovider")   // provider(必填)
                .pathList(mPhotoList)                         // 记录已选的图片
                .multiSelect(true)                      // 是否多选   默认：false
                .multiSelect(true, 9)                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
                .maxSize(9)
                .isShowCamera(true)                     // 是否现实相机按钮  默认：false
                .filePath("/Gallery/Pictures")          // 图片存放路径
                .build();
    }

    @Override
    public void initUi() {
        header.setTitle("发布动态");
        header.setRightTitle("发布");
        header.setRightTvOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发布
                postActiveData();
            }
        });
        presenter.getAdapter().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加图片
                galleryConfig.getBuilder().pathList(mPhotoList).isOpenCamera(false).build();
                initPermissions();
            }
        });
    }


    // 授权管理
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(PostActiveActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(PostActiveActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                toast("请在 设置-应用管理 中开启此应用的储存授权。");
            } else {
                ActivityCompat.requestPermissions(PostActiveActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(PostActiveActivity.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(PostActiveActivity.this);
            } else {

            }
        }
    }


    private void initGallery() {
        iHandlerCallBack = new IHandlerCallBack() {
            @Override
            public void onStart() {
            }

            @Override
            public void onSuccess(List<String> photoList) {
                mPhotoList.clear();
                for (String s : photoList) {
                    mPhotoList.add(s);
                }
                presenter.getAdapter().refresh(mPhotoList);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onFinish() {
            }

            @Override
            public void onError() {
            }
        };

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.cancel();
        presenter.detachView();
    }

    @Override
    public RecyclerView getRecyclerView() {
        return mPostActiveRecyclerView;
    }

    @Override
    public void postActiveData() {
        active = mmPostActiveActiveEt.getEditableText().toString().trim();
        if(TextUtils.isEmpty(active)){
            ToastUtils.showToast("请填写您的记录生活");
            return;
        }
        ThreadManager.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mPhotoList.size(); i++) {
                    Bitmap bitmap = BitmapFactory.decodeFile(mPhotoList.get(i));
                    split = mPhotoList.get(i).split("/");
                    File afterCompressImgFile = new File(mImageRootDir + "/" + split[split.length - 1]);
                    //final String codeString = ImageUtils.compressBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), 40, afterCompressImgFile.getAbsolutePath().getBytes(), true);
                    final String codeString = ImageUtils.compressBitmap(bitmap,afterCompressImgFile.getAbsolutePath());
                    if (codeString.equals("1")) {
                        //byte[] lanzhus = BitmapUtil.encrypt(BitmapUtil.getBytes(afterCompressImgFile.getAbsolutePath()), "lanzhu");
                        //BitmapUtil.getFile(lanzhus,mImageRootDir+"/","new_" + split[split.length - 1]);
                        compress_path.add(afterCompressImgFile.getAbsolutePath());
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgressDialog("正在上传中...");
                        final StringBuilder builder = new StringBuilder();
                        for (int i = 0; i < compress_path.size(); i++) {
                            final BmobFile bmobFile = new BmobFile(new File(compress_path.get(i)));
                            bmobFile.upload(new UploadFileListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        currentPosition++;
                                        builder.append(bmobFile.getFileUrl());
                                        if (currentPosition <= compress_path.size() - 1) {
                                            builder.append(",");
                                        } else {
                                            Message msg = Message.obtain();
                                            msg.obj = builder.toString();
                                            msg.what = UPLOAD_COMPELETED;
                                            mHandler.sendMessage(msg);
                                        }
                                    }
                                }
                            });
                        }
                    }
                });
            }

        });
    }

    @Override
    public void saveActiveData(String attachments) {
        User user = BmobUser.getCurrentUser(User.class);
        EvaluationInfo info = new EvaluationInfo();
        info.setUserName(user.getNickName());
        info.setAvater(user.getAvater());
        info.setAttachments(attachments);
        info.setEvalutionId(user.getObjectId());
        info.setContent(active);
        presenter.saveEvaluationInfo(info);
    }

    @Override
    public void finishActivity() {
        finish();
    }


}
