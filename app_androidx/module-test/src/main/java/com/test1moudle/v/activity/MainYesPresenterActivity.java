package com.test1moudle.v.activity;

import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.test1moudle.R;
import com.test1moudle.p.presenter.TestPresenter;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.download.DownloadListener;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.BasePresenter;
import met.hx.com.base.base.activity.BaseYesPresenterActivity;
import met.hx.com.base.baseconfig.C;
import met.hx.com.base.util.DownloadManager;
import met.hx.com.librarybase.some_utils.LogUtils;

/**
 * @author huxu
 */

@Route(path = C.MAIN)
public class MainYesPresenterActivity extends BaseYesPresenterActivity implements View.OnClickListener {
    DownloadListener downloadListener;

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_test_activity_main2;
        ba.mHasTitle = true;
        ba.mTitleText = "中国金融通";
        ba.titleId = R.layout.m_test_item_title;
    }

    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
    }

    @Override
    protected BasePresenter initPresenter() {
        return new TestPresenter();
    }

    @Override
    protected void initView() {
        findViewById(R.id.bt_demo).setOnClickListener(this);
        downloadListener = new DownloadListener() {
            @Override
            public void onDownloadError(int what, Exception exception) {
                LogUtils.i("首页监听onDownloadError+what==" + what);
            }

            @Override
            public void onStart(int what, boolean resume, long range, Headers headers, long size) {
                LogUtils.i("首页监听onStart+what==" + what);
            }

            @Override
            public void onProgress(int what, int progress, long fileCount, long speed) {
                LogUtils.i("首页监听onProgress==" + what + "," + progress + "%");
            }

            @Override
            public void onFinish(int what, String filePath) {
                // 下载完成，参数2为保存在本地的文件路径。
                LogUtils.i("首页监听onFinish==" + what + "," + filePath);

            }

            @Override
            public void onCancel(int what) {
                LogUtils.i("首页监听onCancel==" + what + "," + what);
            }
        };
        DownloadManager.getInstance().registerDownloadListener(downloadListener);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        DownloadManager.getInstance().unRegisterDownloadListener(downloadListener);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.bt_demo) {
            Bundle bundle=new Bundle();
            bundle.putString("string","和萨达哈速度哈接收到哈时间的话");
            ARouter.getInstance().build(C.DEMO).with(bundle).navigation();
        }

    }


}
