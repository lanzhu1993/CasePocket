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
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.busilinq.casepocket.R;
import com.busilinq.casepocket.adapter.GlideImageLoader;
import com.busilinq.casepocket.base.BaseActivity;
import com.busilinq.casepocket.bean.User;
import com.busilinq.casepocket.db.CPDbApi;
import com.busilinq.casepocket.presenter.PersonalPresenter;
import com.busilinq.casepocket.utils.ImageUtils;
import com.busilinq.casepocket.utils.ThreadManager;
import com.busilinq.casepocket.utils.ToastUtils;
import com.busilinq.casepocket.viewinterface.IPersonalView;
import com.busilinq.casepocket.widget.HeaderLayoutView;
import com.busilinq.casepocket.widget.LoadDialogView;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 描述：个人中心界面
 * <p>
 * 创建时间： 2017/11/16  14:33
 * author :shiquan.lu
 * email: 1113799552@qq.com
 */

public class PersonalActivity extends BaseActivity implements IPersonalView {

    @Bind(R.id.header) HeaderLayoutView header;
    @Bind(R.id.personal_avater_iv) CircleImageView mPersonalAvaterIv;
    @Bind(R.id.personal_name_tv) TextView mPersonalNameTv;
    @Bind(R.id.personal_phone_tv) TextView mPersonalPhoneTv;
    @Bind(R.id.personal_sex_tv) TextView mPersonalSexTv;
    @Bind(R.id.personal_birth_tv) TextView mPersonalBirthTv;



    PersonalPresenter presenter;


    private List<String> mPhotoList = new ArrayList<>();
    private GalleryConfig galleryConfig;
    private IHandlerCallBack iHandlerCallBack;

    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private final int UPLOAD_COMPELETED = 11;

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
                    saveAvater((String) msg.obj);
                    break;
            }
        }
    };
    private View datePickerView;
    private DatePicker datePicker;


    @Override
    public int initContentView() {
        return R.layout.activity_personal;
    }

    @Override
    public void initData() {
        presenter = new PersonalPresenter();
        presenter.attachView(this);
        initGallery();
        initGalleryConfig();
    }

    @Override
    public void initUi() {
        header.setTitle("我的信息");
        showUserInfo();
    }






    @OnClick({R.id.personal_avater_layout,R.id.personal_nickname_layout,R.id.personal_phone_layout,
            R.id.personal_qrcode_layout,R.id.personal_sex_layout,R.id.personal_birth_layout})
    public void OnClick(View view){
        switch (view.getId()){
            case R.id.personal_avater_layout:
                galleryConfig.getBuilder().pathList(mPhotoList).isOpenCamera(false).build();
                initPermissions();
                break;
            case R.id.personal_nickname_layout:
                showInputDialog();
                break;
            case R.id.personal_phone_layout:
                break;
            case R.id.personal_qrcode_layout:
                intoQRCodeActivity();
                break;
            case R.id.personal_sex_layout:
                showSexDialog();
                break;
            case R.id.personal_birth_layout:
                showBirthDialog();
                break;
        }
    }

    private void showSexDialog() {
        new MaterialDialog.Builder(this)
                .title("性別")
                .itemsColor(getResources().getColor(R.color.colorPrimaryDark))
                .positiveColor(getResources().getColor(R.color.colorPrimaryDark))
                .contentColor(getResources().getColor(R.color.colorPrimaryDark))
                .items(R.array.sex_values)
                .positiveText("确定")
                .itemsCallbackSingleChoice(0, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        updateSex(text.toString());
                        return true;
                    }
                })
                .show();
    }


    private void initGalleryConfig() {
        galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())    // ImageLoader 加载框架（必填）
                .iHandlerCallBack(iHandlerCallBack)     // 监听接口（必填）
                .provider("com.yancy.gallerypickdemo.fileprovider")   // provider(必填)
                .pathList(mPhotoList)                         // 记录已选的图片
                .multiSelect(false)                      // 是否多选   默认：false
                .multiSelect(false, 9)                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
                .maxSize(9)
                .isShowCamera(true)                     // 是否现实相机按钮  默认：false
                .filePath("/Gallery/Pictures")          // 图片存放路径
                .crop(true)
                .build();
    }

    // 授权管理
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(PersonalActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(PersonalActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                toast("请在 设置-应用管理 中开启此应用的储存授权。");
            } else {
                ActivityCompat.requestPermissions(PersonalActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(PersonalActivity.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(PersonalActivity.this);
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
                // TODO: 2017/11/16
                try{
                    upLoadAvater();
                }catch (Exception e){
                    e.printStackTrace();
                }
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
    public void saveAvater(String avater) {
        presenter.saveAvater(avater);
    }

    @Override
    public void upLoadAvater() {
        LoadDialogView.showDialog(this,"请稍等...");
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
    public void updateSex(String sex) {
        presenter.updateSex(sex);
    }

    @Override
    public void updateNickName(String nickName) {
        presenter.updateNickName(nickName);
    }

    @Override
    public void updateBirth(String birth) {
        presenter.updateBirth(birth);
    }

    @Override
    public void showUserInfo() {
        User user = CPDbApi.getInstance().getUser();
        if(null !=user.getBirth() && !TextUtils.isEmpty(user.getBirth())){
            mPersonalBirthTv.setText(user.getBirth());
        }
        if(null !=user.getNickName() && !TextUtils.isEmpty(user.getNickName())){
            mPersonalNameTv.setText(user.getNickName());
        }
        if(null !=user.getSex() && !TextUtils.isEmpty(user.getSex())){
            mPersonalSexTv.setText(user.getSex());
        }
        mPersonalPhoneTv.setText(user.getUsername());
        Glide.with(this)
                .load(user.getAvater())
                .placeholder(R.mipmap.icon_add_avater)
                .error(R.mipmap.icon_add_avater)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(mPersonalAvaterIv);
    }

    @Override
    public void showInputDialog() {
        new MaterialDialog.Builder(this)
                .title("修改昵称")
                .itemsColor(getResources().getColor(R.color.colorPrimaryDark))
                .positiveColor(getResources().getColor(R.color.colorPrimaryDark))
                .contentColor(getResources().getColor(R.color.colorPrimaryDark))
                .itemsColorRes(R.color.colorPrimaryDark)
                .inputType(
                        InputType.TYPE_CLASS_TEXT
                                | InputType.TYPE_TEXT_VARIATION_PERSON_NAME
                                | InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .inputRange(2, 16)
                .positiveText("确定")
                .negativeText("取消")
                .input("请填写昵称", "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        updateNickName(input.toString());
                    }
                })
                .show();
    }

    @Override
    public void showBirthDialog() {
        if(null == datePickerView){
            datePickerView = View.inflate(this, R.layout.dialog_datepicker,null);
            datePicker = (DatePicker) datePickerView.findViewById(R.id.datePicker);
        }
        new MaterialDialog.Builder(this)
                .title("请选择生日")
                .itemsColor(getResources().getColor(R.color.colorPrimaryDark))
                .positiveColor(getResources().getColor(R.color.colorPrimaryDark))
                .contentColor(getResources().getColor(R.color.colorPrimaryDark))
                .customView(datePickerView, false)
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String birthday = datePicker.getYear()+"/"+(datePicker.getMonth()+1)+"/"+datePicker.getDayOfMonth();
                        updateBirth(birthday);
                    }
                })
                .show();
    }

    @Override
    public void intoQRCodeActivity() {
        showActivity(this,QRCodeActivity.class);
    }
}
