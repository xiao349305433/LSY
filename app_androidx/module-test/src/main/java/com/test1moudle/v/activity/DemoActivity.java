package com.test1moudle.v.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.isseiaoki.simplecropview.util.Utils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.test1moudle.R;
import com.test1moudle.init.FileProviderUtil;
import com.test1moudle.p.presenter.MainPresenter;
import com.wu.loushanyun.base.config.K;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import java.util.ArrayList;
import java.util.List;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseYesPresenterActivity;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.base.matisse.MatisseUtil;
import met.hx.com.base.baseconfig.C;
import met.hx.com.librarybase.some_utils.FileUtils;
import met.hx.com.librarybase.some_utils.LogUtils;
import met.hx.com.librarybase.some_utils.ToastManager;
import met.hx.com.librarybase.views.dialog.MDDialog;

/**
 * Created by huxu on 2017/12/1.
 */
@Route(path = C.DEMO)
public class DemoActivity extends BaseYesPresenterActivity<MainPresenter> implements
        View.OnClickListener {
    private RxPermissions rxPermissions;
    private static final int REQUEST_CODE_CHOOSE = 10001;
    private static final int REQUEST_CHOOSE_PHOTO = 10002;
    private static final int REQUEST_CODE_CHOOSE_ONE = 10003;
    private static final int REQUEST_CODE_OPEN_PHOTO = 10004;
    private static final int REQUEST_CODE_DELETE_PHOTO = 10005;

    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
    }

    @Override
    protected MainPresenter initPresenter() {
        return new MainPresenter();
    }


    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_test_activity_demo;
        ba.mHasTitle = true;
        ba.mTitleText = "demo列表";
    }

    @Override
    protected void initView() {
        rxPermissions = new RxPermissions(this);
        findViewById(R.id.test1).setOnClickListener(this);
        findViewById(R.id.test_jsBridge).setOnClickListener(this);
        findViewById(R.id.webs).setOnClickListener(this);
        findViewById(R.id.webs_fragment).setOnClickListener(this);
        findViewById(R.id.banner).setOnClickListener(this);
        findViewById(R.id.refresh).setOnClickListener(this);
        findViewById(R.id.refresh_group).setOnClickListener(this);
        findViewById(R.id.permission).setOnClickListener(this);
        findViewById(R.id.btn_tab).setOnClickListener(this);
        findViewById(R.id.btn_tab_style).setOnClickListener(this);
        findViewById(R.id.show_dialog).setOnClickListener(this);
        findViewById(R.id.show_dialog1).setOnClickListener(this);
        findViewById(R.id.show_dialog2).setOnClickListener(this);
        findViewById(R.id.diss_dialog).setOnClickListener(this);
        findViewById(R.id.go_fragment).setOnClickListener(this);
        findViewById(R.id.choose_image).setOnClickListener(this);
        findViewById(R.id.top_tab).setOnClickListener(this);
        findViewById(R.id.button_location).setOnClickListener(this);
        findViewById(R.id.button_choose).setOnClickListener(this);
        findViewById(R.id.button_crop).setOnClickListener(this);
        findViewById(R.id.button_photo).setOnClickListener(this);
        findViewById(R.id.button_recycle_player).setOnClickListener(this);
        findViewById(R.id.button_player).setOnClickListener(this);
        findViewById(R.id.button_big_image).setOnClickListener(this);
        findViewById(R.id.button_limit_player).setOnClickListener(this);
        findViewById(R.id.button_download).setOnClickListener(this);
        findViewById(R.id.button_erweima).setOnClickListener(this);
        findViewById(R.id.button_lottie).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.test1) {
            ARouter.getInstance().build(C.PrintBitmapActivity).navigation();
        } else if (i == R.id.webs) {
            ARouter.getInstance().build(C.WEB).withString("url", "https://mcpc6pje8.lightyy.com/importantNews.html?v=1.0&n=getDate()&h=0&p=HSJY_1056&u=866655034247100&pulldownrefresh=1&rolltopflag=1&bannerflag=1&&moreflag=1")
                    .withBoolean("hasTitle", true)
                    .withBoolean("hasProgress", true)
                    .withInt("onProgressColor", R.color.base_Q1)
                    .withBoolean("canBack", true)
                    .navigation();
        } else if (i == R.id.webs_fragment) {
            Bundle bundle = new Bundle();
            bundle.putString("url", "https://www.baidu.com/");
            bundle.putBoolean("hasTitle", true);
            bundle.putBoolean("hasProgress", true);
            ARouter.getInstance().build(C.SingleFragmentActivity).withBundle("bundle", bundle).withString("path", C.WebFragment).navigation();
        } else if (i == R.id.banner) {
            ARouter.getInstance().build(C.BANNER).navigation();

        } else if (i == R.id.test_jsBridge) {
            ARouter.getInstance().build(C.JsBridgeActivity).navigation();

        } else if (i == R.id.refresh) {
            ARouter.getInstance().build(C.REFRESHTEST0).navigation();

        } else if (i == R.id.refresh_group) {
            ARouter.getInstance().build(C.REFRESHTEST1).navigation();

        } else if (i == R.id.btn_tab) {
            ARouter.getInstance().build(C.TAB).navigation();

        } else if (i == R.id.btn_tab_style) {
            ARouter.getInstance().build(C.STYLE).navigation();

        } else if (i == R.id.permission) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 启动系统相机
            if (rxPermissions.isRevoked(Manifest.permission.CAMERA)) {
                startActivity(intent);
            } else {
                rxPermissions.request(Manifest.permission.CAMERA).subscribe(aBoolean -> {
                    if (aBoolean) {
                        startActivity(intent);
                    } else {
                        ToastManager.getInstance(this).showToast("没有权限");
                    }
                });
            }
        } else if (i == R.id.show_dialog) {
            ARouter.getInstance().build(K.ScrollingActivity).navigation();

        } else if (i == R.id.show_dialog1) {
            LoadingDialogUtil.showByEvent("加载加载加载加载", this.getClass().getName());

        } else if (i == R.id.show_dialog2) {
            MDDialog mdDialog = new MDDialog.Builder(this)
                    .setContentView(R.layout.m_test_content_dialog)
                    .setContentViewOperator(new MDDialog.ContentViewOperator() {
                        @Override
                        public void operate(View contentView) {//这里的contentView就是上面代码中传入的自定义的View或者layout资源inflate出来的view
                        }
                    })
                    .setTitle("添加")
                    .setNegativeButton("确定", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setPositiveButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setShowTitle(true)
                    .setShowButtons(true)
                    .create();
            mdDialog.show();

        } else if (i == R.id.top_tab) {
            ARouter.getInstance().build(C.TestTopTab).navigation();
        } else if (i == R.id.choose_image) {
            MatisseUtil.choosePicture(this, MimeType.ofImage(), 5, REQUEST_CODE_CHOOSE, FileProviderUtil.TestProvider);
        } else if (i == R.id.go_fragment) {
            Bundle b = new Bundle();
            b.putString("aaa", "nbvcs");

            ARouter.getInstance().build(C.SingleFragmentActivity).withBundle("bundle", b).withString("path", C.TestRoundFragment)
                    .navigation();
        } else if (i == R.id.button_location) {
            ARouter.getInstance().build(C.LocationActivity).navigation();
        } else if (i == R.id.button_choose) {
            FileUtils.chooseFile(this, REQUEST_CHOOSE_PHOTO, null, null);
//            FileUtils.chooseFile(this, REQUEST_CHOOSE_PHOTO, "image/*", null);
        } else if (i == R.id.button_crop) {
            MatisseUtil.choosePicture(this, MimeType.ofImage(), 1, REQUEST_CODE_CHOOSE_ONE, FileProviderUtil.TestProvider);
        } else if (i == R.id.discover) {
//            ARouter.getInstance().build(C.DISCOVER).navigation();
        } else if (i == R.id.button_photo) {
            MatisseUtil.choosePicture(this, MimeType.ofImage(), 9, REQUEST_CODE_OPEN_PHOTO, FileProviderUtil.TestProvider);
        } else if (i == R.id.button_player) {
            Bundle b = new Bundle();
            b.putString("aaa", "nbvcs");
            ARouter.getInstance().build(C.SingleFragmentActivity).withBundle("bundle", b).withString("path", C.TestRoundFragment).navigation();
        } else if (i == R.id.button_recycle_player) {
            ARouter.getInstance().build(C.RecyclePlayerActivity).navigation();
        } else if (i == R.id.button_big_image) {
            ARouter.getInstance().build(C.REFRESH).navigation();
        } else if (i == R.id.button_limit_player) {
            ARouter.getInstance().build(C.TinyWindowPlayActivity).navigation();
        } else if (i == R.id.button_download) {
            ARouter.getInstance().build(C.DownLoadActivity).navigation();
        } else if (i == R.id.button_erweima) {
            ARouter.getInstance().build(C.ZxingActivity).navigation();
        } else if (i == R.id.button_lottie) {
            ARouter.getInstance().build(C.LottieActivity).navigation();
        }

    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String data= (String) msg.obj;
            mPresenter.test(data);
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            LogUtils.i("第一数据", Matisse.obtainResult(data).toString());
            LogUtils.i("第二数据", Matisse.obtainPathResult(data).toString());
            Message message=new Message();
            message.obj=Matisse.obtainPathResult(data).get(0).toString();
            handler.sendMessage(message);

//            imagePicture.setImageBitmap(ImageUtils.getBitmap(Matisse.obtainPathResult(data).get(0)));
        }
        if (requestCode == REQUEST_CODE_CHOOSE_ONE && resultCode == RESULT_OK) {
            LogUtils.i("第一数据", Matisse.obtainResult(data).toString());
            LogUtils.i("第二数据", Matisse.obtainPathResult(data).toString());
            List<Uri> uris = Matisse.obtainResult(data);
            ArrayList<Integer> list = new ArrayList<>();
            list.add(7);
            list.add(5);
            ARouter.getInstance().build(C.CropViewActivity)
                    .withString("mStringUri", uris.get(0).toString())
                    .withInt("type", 9)
                    .withIntegerArrayList("custom", list)
                    .navigation(this, REQUEST_CHOOSE_PHOTO);
        }
        if (requestCode == REQUEST_CHOOSE_PHOTO && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            Uri uri = (Uri) bundle.get("crop_uri");
            LogUtils.i("剪裁的图片地址", Utils.getFileFromUri(this, uri).getAbsolutePath());
        }
        if (requestCode == REQUEST_CODE_OPEN_PHOTO && resultCode == RESULT_OK) {
            List<String> strings = Matisse.obtainPathResult(data);
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.addAll(strings);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("paths", arrayList);
            LogUtils.i("第二数据", Matisse.obtainPathResult(data).toString());
            ARouter.getInstance().build(C.FullViewPictureActivity)
                    .withInt("num", 2)
                    .withStringArrayList("paths", arrayList)
                    .navigation(this, REQUEST_CODE_DELETE_PHOTO);
        }
        if (requestCode == REQUEST_CODE_DELETE_PHOTO && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            int num = bundle.getInt("num");
            LogUtils.i("删除的图片第" + (num + 1) + "张");
        }

    }

}
