package com.test1moudle.v.fragment;


import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.test1moudle.R;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.download.DownloadListener;


import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.BasePresenter;
import met.hx.com.base.base.fragment.BaseYesPresenterFragment;
import met.hx.com.base.baseconfig.C;
import met.hx.com.base.util.DownloadManager;
import met.hx.com.librarybase.some_utils.LogUtils;

@Route(path=C.MainYesPresenterFragment)
public class MainYesPresenterFragment extends BaseYesPresenterFragment implements
        View.OnClickListener{
    DownloadListener downloadListener;

    @Override
    public void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_test_activity_main2;
        ba.mHasTitle = true;
        ba.mTitleText = "金融通";
        ba.titleId = R.layout.m_test_item_title;
    }


    @Override
    public void initView(View view, Bundle bundle) {
        view.findViewById(R.id.bt_demo).setOnClickListener(this);

    }

    @Override
    protected void initData(Bundle bundle) {
//       List<LitePalTest> litePalTests= DataSupport.findAll(LitePalTest.class);
//        if(litePalTests.size()>0){
//            for(int i=0;i<litePalTests.size();i++){
//                LitePalTest litePalTest= (LitePalTest) litePalTests.get(i);
//                LogUtils.i("查询到的数据11111111"+litePalTest.toString());
//            }
//        }
//        LitePalTest.deleteAll(LitePalTest.class);
//            for(int i=0;i<20;i++){
//                LitePalTest litePalTest=new LitePalTest(i,"item"+i);
//                LogUtils.i("删除完了开始添加");
//                litePalTest.save ();
//            }
//            FindMultiExecutor findMultiExecuto1r= DataSupport.findAllAsync(LitePalTest.class);
//            findMultiExecuto1r.listen(new FindMultiCallback() {
//                @Override
//                public <T> void onFinish(List<T> t) {
//                    if(t.size()>0){
//                        for(int i=0;i<t.size();i++){
//                            LitePalTest litePalTest= (LitePalTest) t.get(i);
//                            LogUtils.i("查询到的数据2222222"+litePalTest.toString());
//                        }
//                    }
//
//                }
//            });


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
    public void onDestroy() {
        super.onDestroy();
        DownloadManager.getInstance().unRegisterDownloadListener(downloadListener);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.bt_demo) {
            ARouter.getInstance().build(C.DEMO).navigation();
        }

    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }
}
