package com.busilinq.casepocket.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.busilinq.casepocket.R;
import com.busilinq.casepocket.adapter.CaseImageAdapter;
import com.busilinq.casepocket.adapter.GlideImageLoader;
import com.busilinq.casepocket.adapter.PhotoAdapter;
import com.busilinq.casepocket.base.BaseActivity;
import com.busilinq.casepocket.bean.CaseInfo;
import com.busilinq.casepocket.bean.Relation;
import com.busilinq.casepocket.listener.OnItemClickListener;
import com.busilinq.casepocket.presenter.AddCasePresenter;
import com.busilinq.casepocket.utils.AES;
import com.busilinq.casepocket.utils.BitmapUtil;
import com.busilinq.casepocket.utils.CloseUtils;
import com.busilinq.casepocket.utils.Constants;
import com.busilinq.casepocket.utils.DateUtil;
import com.busilinq.casepocket.utils.ImageUtils;
import com.busilinq.casepocket.utils.ThreadManager;
import com.busilinq.casepocket.viewinterface.IAddCaseView;
import com.busilinq.casepocket.widget.HeaderLayoutView;
import com.busilinq.casepocket.widget.pixelate.OnPixelateListener;
import com.busilinq.casepocket.widget.pixelate.Pixelate;
import com.yancy.gallerypick.config.GalleryConfig;
import com.yancy.gallerypick.config.GalleryPick;
import com.yancy.gallerypick.inter.IHandlerCallBack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * 描述：
 * <p>
 * 邮箱：shiquan.lu@busilinq.com
 * 创建时间：2017/5/3
 * author: shiquan.lu
 */

public class AddCaseActivity extends BaseActivity implements IAddCaseView{

    @Bind(R.id.header)
    HeaderLayoutView header;

    @Bind(R.id.addcase_date_layout)
    RelativeLayout mAddCaseDateLayout;

    @Bind(R.id.addcase_date_tv)
    TextView mAddCaseDateTv;

    @Bind(R.id.addcase_recylerview)
    RecyclerView mAddCaseRecyclerView;

    @Bind(R.id.addcase_photo_layout)
    RelativeLayout mAddCasePhotoLayout;

    @Bind(R.id.addcase_default_layout)
    RelativeLayout mAddCaseDefaultLayout;

    @Bind(R.id.addcase_add_layout)
    RelativeLayout mAddCaseAddLayout;

    @Bind(R.id.addcase_show_name_tv)
    TextView mAddCaseShowNameTv;

    @Bind(R.id.addcase_hospital_et)
    EditText mAddCaseHospitalEt;

    @Bind(R.id.addcase_department_et)
    EditText mAddCaseDepartmentEt;

    @Bind(R.id.addcase_desc_et)
    EditText mAddCaseDescEt;

    /**
     * 时间选择相关
     */
    private String appiontTime;
    private int curyear, curmonth, curday;
    private Calendar mincalendar;
    private Calendar maxcalendar;
    private DatePickerDialog dialog;

    /**
     * 图片选择相关
     */
    private PhotoAdapter photoAdapter;
    private List<String> path = new ArrayList<>();
    private List<String> compress_path = new ArrayList<>();
    private GalleryConfig galleryConfig;
    private IHandlerCallBack iHandlerCallBack;

    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 8;
    private final int UPLOAD_COMPELETED = 9;

    private AddCasePresenter presenter;
    private Relation relation;
    private CaseInfo caseInfo = new CaseInfo();
    private boolean isActivityForResult = false;


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
                    save(caseInfo, relation.getObjectId(), hospital, department, date, (String) msg.obj, description, new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                toast("添加成功");
                                finish();
                            } else {
                                toast("添加失败");
                            }
                            compress_path.clear();
                            closeProgressDialog();
                        }
                    });
                    break;
            }
        }
    };
    private String date;
    private String hospital;
    private String department;
    private String description;

    private int currentPosition = 0;
    private String[] split;

    @Override
    public int initContentView() {
        return R.layout.activity_addcase;
    }

    @Override
    public void initData() {
        relation = (Relation) getIntent().getExtras().getSerializable(Constants.RELATION);
        presenter = new AddCasePresenter();
        presenter.attachView(this);
        initRelationData();
        initCalendar();
        initGallery();
        init();

    }

    private void init() {
        galleryConfig = new GalleryConfig.Builder()
                .imageLoader(new GlideImageLoader())    // ImageLoader 加载框架（必填）
                .iHandlerCallBack(iHandlerCallBack)     // 监听接口（必填）
                .provider("com.yancy.gallerypickdemo.fileprovider")   // provider(必填)
                .pathList(path)                         // 记录已选的图片
                .multiSelect(true)                      // 是否多选   默认：false
                .multiSelect(true, 9)                   // 配置是否多选的同时 配置多选数量   默认：false ， 9
                .maxSize(9)
                .isShowCamera(true)                     // 是否现实相机按钮  默认：false
                .filePath("/Gallery/Pictures")          // 图片存放路径
                .build();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mAddCaseRecyclerView.setLayoutManager(linearLayoutManager);

        photoAdapter = new PhotoAdapter(this, path);
        mAddCaseRecyclerView.setAdapter(photoAdapter);

        photoAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_photo_delete:
                        path.remove(position);
                        photoAdapter.notifyItemRemoved(position);
                        break;
                }
            }
        });

    }

    @Override
    public void initUi() {
        header.setTitle("上传病历");
        header.setRightTitle("保存");
        header.setRightTvOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });
        mAddCaseDateTv.setText(DateUtil.getDateNow("yyyy-MM-dd"));
    }

    @SuppressLint("WrongConstant")
    @Override
    public void showDateDialog() {
        dialog = new DatePickerDialog(AddCaseActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                appiontTime = year + "-" + (month + 1) + "-" + day;
                mAddCaseDateTv.setText(appiontTime);
            }
        }, curyear, curmonth, curday);
        DatePicker datepicker = dialog.getDatePicker();
        mincalendar.add(Calendar.YEAR, -1);
        datepicker.setMinDate(mincalendar.getTimeInMillis());
        datepicker.setMaxDate(maxcalendar.getTimeInMillis());
        datepicker.init(curyear, curmonth, curday, null);
        dialog.show();
    }

    @SuppressLint("WrongConstant")
    @Override
    public void initCalendar() {
        mincalendar = Calendar.getInstance();
        maxcalendar = Calendar.getInstance();
        curyear = maxcalendar.get(Calendar.YEAR);
        curmonth = maxcalendar.get(Calendar.MONTH);
        curday = maxcalendar.get(Calendar.DAY_OF_MONTH);

    }

    @Override
    public void initRelationData() {
        if (null == relation || TextUtils.isEmpty(relation.getRelationName())) {
            getDataFromServer();
        } else {
            mAddCaseAddLayout.setVisibility(View.INVISIBLE);
            mAddCaseDefaultLayout.setVisibility(View.VISIBLE);
            mAddCaseShowNameTv.setText(relation.getRelationName());
        }
    }

    @Override
    public void uploadImage() {
        if (TextUtils.isEmpty(relation.getRelationName())) {
            showActivity(AddCaseActivity.this, AddMemberActivity.class);
            return;
        }
        date = mAddCaseDateTv.getText().toString().trim();
        hospital = mAddCaseHospitalEt.getEditableText().toString().toString();
        department = mAddCaseDepartmentEt.getEditableText().toString().toString();
        description = mAddCaseDescEt.getEditableText().toString().toString();
        if (TextUtils.isEmpty(hospital)) {
            toast("请输入医院");
            return;
        }
        if (TextUtils.isEmpty(department)) {
            toast("请输入科室");
            return;
        }
        if (path.size() <= 0) {
            toast("请添加图片");
            return;
        }
        if (TextUtils.isEmpty(description)) {
            toast("请输入描述信息");
            return;
        }
        showProgressDialog("图片加密中...");
        ThreadManager.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < path.size(); i++) {
                    Bitmap bitmap = BitmapFactory.decodeFile(path.get(i));
                    split = path.get(i).split("/");
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
    public void getDataFromServer() {
        showProgressDialog("加载中...");
        presenter.findOwnerInfo(new FindListener<Relation>() {
            @Override
            public void done(List<Relation> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        relation = list.get(0);
                        mAddCaseAddLayout.setVisibility(View.INVISIBLE);
                        mAddCaseDefaultLayout.setVisibility(View.VISIBLE);
                        mAddCaseShowNameTv.setText(list.get(0).getRelationName());
                    } else {
                        mAddCaseAddLayout.setVisibility(View.VISIBLE);
                        mAddCaseDefaultLayout.setVisibility(View.INVISIBLE);
                    }
                } else {
                    mAddCaseAddLayout.setVisibility(View.VISIBLE);
                    mAddCaseDefaultLayout.setVisibility(View.INVISIBLE);
                }
                closeProgressDialog();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(!isActivityForResult){
            getDataFromServer();
        }
    }

    @Override
    public void save(CaseInfo caseInfo, String objectId, String hospital, String department, String date, String imagepath, String description, SaveListener<String> listener) {
        presenter.save(caseInfo, objectId, hospital, department, date, imagepath, description, listener );
    }


    @OnClick({R.id.addcase_date_layout, R.id.addcase_photo_layout, R.id.addcase_default_layout, R.id.addcase_add_layout})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.addcase_date_layout:
                showDateDialog();
                break;
            case R.id.addcase_photo_layout:
                galleryConfig.getBuilder().pathList(path).isOpenCamera(false).build();
                initPermissions();
                break;
            case R.id.addcase_default_layout:
                Intent intent = new Intent(AddCaseActivity.this, MemberActivity.class);
                startActivityForResult(intent,100);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                //skipActivity(AddCaseActivity.this, MemberActivity.class);
                break;
            case R.id.addcase_add_layout:
                showActivity(AddCaseActivity.this, AddMemberActivity.class);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode ==101 && requestCode==100 && data!= null){
             relation = (Relation) data.getExtras().get(Constants.RELATION);
            mAddCaseAddLayout.setVisibility(View.INVISIBLE);
            mAddCaseDefaultLayout.setVisibility(View.VISIBLE);
            mAddCaseShowNameTv.setText(relation.getRelationName());
            isActivityForResult = true;
        }
    }

    // 授权管理
    private void initPermissions() {
        if (ContextCompat.checkSelfPermission(AddCaseActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddCaseActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                toast("请在 设置-应用管理 中开启此应用的储存授权。");
            } else {
                ActivityCompat.requestPermissions(AddCaseActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        } else {
            GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(AddCaseActivity.this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                GalleryPick.getInstance().setGalleryConfig(galleryConfig).open(AddCaseActivity.this);
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
                path.clear();
                for (String s : photoList) {
                    path.add(s);
                }
                photoAdapter.notifyDataSetChanged();
                if (path.size() > 0) {
                    mAddCaseDescEt.setVisibility(View.VISIBLE);
                } else {
                    mAddCaseDescEt.setVisibility(View.GONE);
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
        presenter.detachView();
        presenter.cancel();
        super.onDestroy();
        if (path.size() > 0) {
            path.clear();
            path = null;
        }
    }



}
