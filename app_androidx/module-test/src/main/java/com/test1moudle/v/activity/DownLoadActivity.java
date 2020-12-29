package com.test1moudle.v.activity;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.test1moudle.R;
import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadRequest;
import com.yanzhenjie.nohttp.download.SimpleDownloadListener;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.baseconfig.C;
import met.hx.com.base.baseconfig.PathConfig;
import met.hx.com.base.baseevent.Event;
import met.hx.com.base.util.DownloadManager;
import met.hx.com.librarybase.some_utils.ImageUtils;
import met.hx.com.librarybase.some_utils.LogUtils;
import met.hx.com.librarybase.views.HorizontalProgressBar;

/**
 * Created by huxu on 2017/12/11.
 */
@Route(path = C.DownLoadActivity)
public class DownLoadActivity extends BaseNoPresenterActivity implements View.OnClickListener {
    private Button mMTestButton3;
    private Button mMTestButton4;
    private Button mMTestButton5;
    private Button mMTestButton6;
    private Button mMTestButton7;
    private HorizontalProgressBar mProgressBar1;
    private HorizontalProgressBar mProgressBar2;
    private HorizontalProgressBar mProgressBar3;
    private SimpleDownloadListener simpleDownloadListener = new SimpleDownloadListener() {

        @Override
        public void onDownloadError(int what, Exception exception) {
            super.onDownloadError(what, exception);
            LogUtils.i("哈哈onDownloadError+what" + what);
        }

        @Override
        public void onStart(int what, boolean resume, long range, Headers headers, long size) {
            // 开始下载，回调的时候说明文件开始下载了。
            // 参数1：what。
            // 参数2：是否是断点续传，从中间开始下载的。
            // 参数3：如果是断点续传，这个参数非0，表示之前已经下载的文件大小。
            // 参数4：服务器响应头。
            // 参数5：文件总大小，可能为0，因为服务器可能不返回文件大小。
            LogUtils.i("哈哈onStart+what" + what);
        }

        @Override
        public void onProgress(int what, int progress, long fileCount, long speed) {
            // 进度发生变化，服务器不返回文件总大小时不回调，因为没法计算进度。
            // 参数1：what。
            // 参数2：进度，[0-100]。
            // 参数3：文件总大小，可能为0，因为服务器可能不返回文件大小。
            // 参数4：下载的速度，含义为1S下载的byte大小，计算下载速度时：
            //        int xKB = (int) speed / 1024; // 单位：xKB/S
            //        int xM = (int) speed / 1024 / 1024; // 单位：xM/S
            LogUtils.i("哈哈onProgress" + what + "," + progress + "%");
            Message message = new Message();
            message.what = what;
            message.arg1 = progress;
            handler.sendMessage(message);
        }

        @Override
        public void onFinish(int what, String filePath) {
            // 下载完成，参数2为保存在本地的文件路径。
            LogUtils.i("哈哈onFinish" + what + "," + filePath);
        }
    };
    String url1 = "http://sw.bos.baidu.com/sw-search-sp/software/eafafc9caba55/360aqws_setup.exe";
    String url2 = "http://sw.bos.baidu.com/sw-search-sp/software/dc1dfbffe7758/ChromeStandalone_63.0.3239.84_Setup.exe";
    String url3 = "http://mpic.tiankong.com/67c/2b2/67c2b2f3c595baa7d9cf88ddc536a728/640.jpg";
    DownloadRequest downloadRequest = new DownloadRequest(url1, RequestMethod.GET, PathConfig.getDownloadFileDir(), true, true);
    private DownloadListener downloadListener;//监听后台下载

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    mProgressBar0.seekTo(msg.arg1 / 100f);
                    break;
                case 2:
                    mProgressBar1.seekTo(msg.arg1 / 100f);
                    break;
                case 3:
                    mProgressBar2.seekTo(msg.arg1 / 100f);
                    break;
                case 4:
                    mProgressBar3.seekTo(msg.arg1 / 100f);
                    break;
            }
        }
    };
    private ImageView mImageView;
    private HorizontalProgressBar mProgressBar0;

    @Override
    protected void initEventListener() {
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_test_down;

    }

    @Override
    protected void initView() {
        mMTestButton3 = (Button) findViewById(R.id.m_test_button3);
        mMTestButton4 = (Button) findViewById(R.id.m_test_button4);
        mMTestButton5 = (Button) findViewById(R.id.m_test_button5);
        mMTestButton6 = (Button) findViewById(R.id.m_test_button6);
        mMTestButton7 = (Button) findViewById(R.id.m_test_button7);
        mProgressBar0 = (HorizontalProgressBar) findViewById(R.id.progressBar0);
        mProgressBar1 = (HorizontalProgressBar) findViewById(R.id.progressBar1);
        mProgressBar2 = (HorizontalProgressBar) findViewById(R.id.progressBar2);
        mProgressBar3 = (HorizontalProgressBar) findViewById(R.id.progressBar3);
        mImageView = (ImageView) findViewById(R.id.image_view);
        mMTestButton3.setOnClickListener(this);
        mMTestButton4.setOnClickListener(this);
        mMTestButton5.setOnClickListener(this);
        mMTestButton6.setOnClickListener(this);
        mMTestButton7.setOnClickListener(this);
        downloadListener = new DownloadListener() {
            @Override
            public void onDownloadError(int what, Exception exception) {
                LogUtils.i("下载页监听onDownloadError+what==" + what);
            }

            @Override
            public void onStart(int what, boolean resume, long range, Headers headers, long size) {
                LogUtils.i("下载页监听onStart+what==" + what);
            }

            @Override
            public void onProgress(int what, int progress, long fileCount, long speed) {
                LogUtils.i("下载页监听onProgress==" + what + "," + progress + "%");
                Message message = new Message();
                message.what = what;
                message.arg1 = progress;
                handler.sendMessage(message);

            }

            @Override
            public void onFinish(int what, String filePath) {
                // 下载完成，参数2为保存在本地的文件路径。
                LogUtils.i("下载页监听onFinish==" + what + "," + filePath);

                if (what == 4) {
                    mImageView.setImageBitmap(ImageUtils.getBitmap(filePath));
                }

            }

            @Override
            public void onCancel(int what) {
                LogUtils.i("下载页监听onCancel==" + what + "," + what);
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
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.m_test_button3) {
            compositeDisposable.add(DownloadManager.downLoad(1, downloadRequest, simpleDownloadListener));
        } else if (i == R.id.m_test_button4) {
            DownloadManager.getInstance().addDownload(2, url1, RequestMethod.GET, PathConfig.getDownloadFileDir(), true, true);
            DownloadManager.getInstance().addDownload(3, url2, RequestMethod.GET, PathConfig.getDownloadFileDir(), true, true);
            DownloadManager.getInstance().addDownload(4, url3, RequestMethod.GET, PathConfig.getDownloadFileDir(), true, true);
        } else if (i == R.id.m_test_button5) {
            DownloadManager.getInstance().cancelByUrl(url1);
        } else if (i == R.id.m_test_button6) {
            DownloadManager.getInstance().cancelByWhat(4);
        } else if (i == R.id.m_test_button7) {
            DownloadManager.getInstance().cancelAll();
        }
    }
}
