package met.hx.com.base.util;


import android.util.SparseArray;

import com.yanzhenjie.nohttp.Headers;
import com.yanzhenjie.nohttp.NoHttp;
import com.yanzhenjie.nohttp.RequestMethod;
import com.yanzhenjie.nohttp.download.DownloadListener;
import com.yanzhenjie.nohttp.download.DownloadQueue;
import com.yanzhenjie.nohttp.download.DownloadRequest;
import com.yanzhenjie.nohttp.download.SimpleDownloadListener;
import com.yanzhenjie.nohttp.download.SyncDownloadExecutor;
import com.yanzhenjie.nohttp.error.NetworkError;
import com.yanzhenjie.nohttp.error.NotFoundCacheError;
import com.yanzhenjie.nohttp.error.TimeoutError;
import com.yanzhenjie.nohttp.error.URLError;
import com.yanzhenjie.nohttp.error.UnKnownHostError;

import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.disposables.Disposable;
import met.hx.com.base.base.application.AppContext;
import met.hx.com.base.base.rx.RxSchedulers;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.LogUtils;
import met.hx.com.librarybase.some_utils.ToastManager;


/**
 * Created by huxu on 2017/12/11.
 * 下载器管理类,可用于后台下载,可在任意地方注册监听器监听全局下载线程，
 */

public class DownloadManager {

    private DownloadListener downloadListener;
    private List<DownloadListener> downloadListenerList;
    private HashMap<String, DownloadRequest> downloadRequests;
    private SparseArray<String> stringSparseArray;
    private volatile static DownloadManager instance;
    private DownloadQueue mDownloadQueue;
    private ToastManager toastManager;

    public static DownloadManager getInstance() {
        if (instance == null) {
            synchronized (DownloadManager.class) {
                if (instance == null) {
                    instance = new DownloadManager();
                }
            }
        }
        return instance;
    }


    public void addDownloadRequest(int code, String url, DownloadRequest downloadRequest) {
        downloadRequests.put(url, downloadRequest);
        stringSparseArray.put(code, url);
    }

    public DownloadRequest getDownloadRequest(String url) {
        return downloadRequests.get(url);
    }

    /**
     * 注册外部监听器
     *
     * @param downloadListener
     */
    public void registerDownloadListener(DownloadListener downloadListener) {
        downloadListenerList.add(downloadListener);
    }

    /**
     * 注销外部监听器
     *
     * @param downloadListener
     */
    public void unRegisterDownloadListener(DownloadListener downloadListener) {
        downloadListenerList.remove(downloadListener);
    }

    private DownloadManager() {
        mDownloadQueue = NoHttp.newDownloadQueue(3);
        downloadRequests = new HashMap<>();
        stringSparseArray = new SparseArray<>();
        downloadListenerList = new ArrayList<>();
        toastManager = ToastManager.getInstance(AppContext.getInstance().getApplication());
        downloadListener = new DownloadListener() {
            @Override
            public void onDownloadError(int what, Exception exception) {
                LogUtils.i("onDownloadError+what==" + what);
                if (exception instanceof NetworkError) {// 网络不好
                } else if (exception instanceof TimeoutError) {// 请求超时
                } else if (exception instanceof UnKnownHostError) {// 找不到服务器
                } else if (exception instanceof URLError) {// URL是错的
                } else if (exception instanceof NotFoundCacheError) {// 这个异常只会在仅仅查找缓存时没有找到缓存时返回
                } else if (exception instanceof ProtocolException) {//未知错误
                    toastManager.showToast("当前网络不太好，点击继续下载吧");
                }
                for (DownloadListener downloadListener : downloadListenerList) {
                    downloadListener.onDownloadError(what, exception);
                }
                cancelByWhat(what);
            }

            @Override
            public void onStart(int what, boolean resume, long range, Headers headers, long size) {
                // 开始下载，回调的时候说明文件开始下载了。
                // 参数1：what。
                // 参数2：是否是断点续传，从中间开始下载的。
                // 参数3：如果是断点续传，这个参数非0，表示之前已经下载的文件大小。
                // 参数4：服务器响应头。
                // 参数5：文件总大小，可能为0，因为服务器可能不返回文件大小。
                for (DownloadListener downloadListener : downloadListenerList) {
                    downloadListener.onStart(what, resume, range, headers, size);
                }
                LogUtils.i("onStart+what==" + what);
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
                for (DownloadListener downloadListener : downloadListenerList) {
                    downloadListener.onProgress(what, progress, fileCount, speed);
                }
                LogUtils.i("onProgress==" + what + "," + progress + "%");
            }

            @Override
            public void onFinish(int what, String filePath) {
                // 下载完成，参数2为保存在本地的文件路径。
                LogUtils.i("onFinish==" + what + "," + filePath);
                for (DownloadListener downloadListener : downloadListenerList) {
                    downloadListener.onFinish(what, filePath);
                }
                cancelByWhat(what);

            }

            @Override
            public void onCancel(int what) {
                LogUtils.i("onCancel==" + what + "," + what);
                for (DownloadListener downloadListener : downloadListenerList) {
                    downloadListener.onCancel(what);
                }
                cancelByWhat(what);
            }
        };
    }

    /**
     * 绑定生命周期下载器
     * 必须绑定使用compositeDisposable.add(DownloadUtil.downLoad())，不然内存溢出
     * 如果继承的基类里面一般都有CompositeDisposable对象，如果没有继承则需要自己绑定生命周期
     * protected CompositeDisposable compositeDisposable = new CompositeDisposable();
     * 在onDestroy里面添加 compositeDisposable.dispose();
     *
     * @param what
     * @param downloadRequest
     * @param simpleDownloadListener
     * @return
     */
    public static Disposable downLoad(int what, DownloadRequest downloadRequest, SimpleDownloadListener simpleDownloadListener) {
        return Flowable.create(
                (FlowableOnSubscribe<Event>) emitter ->
                {
                    if (simpleDownloadListener != null && downloadRequest != null) {
                        SyncDownloadExecutor.INSTANCE.execute(what, downloadRequest, simpleDownloadListener);
                        downloadRequest.cancel();
                    }
                }
                , BackpressureStrategy.ERROR)
                .compose(RxSchedulers.io_main())
                .subscribe();
    }

    /**
     * 添加全局下载器
     *
     * @param code
     * @param url
     * @param requestMethod
     * @param fileFolder
     * @param filename
     * @param isRange       是否断点续传
     * @param isDeleteOld   是否删除旧文件
     */
    public void addDownload(int code, String url, RequestMethod requestMethod, String fileFolder, String filename, boolean isRange, boolean isDeleteOld) {
        if (stringSparseArray.get(code) != null) {
            LogUtils.e("对不起，该code正在被使用");
            return;
        }
        if (getDownloadRequest(url) == null) {
            DownloadRequest downloadRequest = new DownloadRequest(url, requestMethod, fileFolder, filename, isRange, isDeleteOld);
            downloadRequest.setCancelSign(url);
            addDownloadRequest(code, url, downloadRequest);
            mDownloadQueue.add(code, downloadRequest, downloadListener);
        } else {
            LogUtils.e("该任务正在下载");
        }
    }

    /**
     * 添加全局下载器
     *
     * @param code
     * @param url
     * @param requestMethod
     * @param fileFolder
     * @param isRange       是否断点续传
     * @param isDeleteOld   是否删除旧文件
     */
    public void addDownload(int code, String url, RequestMethod requestMethod, String fileFolder, boolean isRange, boolean isDeleteOld) {
        if (stringSparseArray.get(code) != null) {
            LogUtils.e("对不起，该code正在被使用");
            return;
        }
        if (getDownloadRequest(url) == null) {
            DownloadRequest downloadRequest = new DownloadRequest(url, requestMethod, fileFolder, isRange, isDeleteOld);
            downloadRequest.setCancelSign(url);
            addDownloadRequest(code, url, downloadRequest);
            mDownloadQueue.add(code, downloadRequest, downloadListener);
        } else {
            LogUtils.e("该任务正在下载");
        }
    }

    /**
     * 根据url获取request
     *
     * @param url
     */
    public void getDownloadRequestByUrl(String url) {
        downloadRequests.get(url);
    }

    /**
     * 根据code获取request
     *
     * @param code
     */
    public void getDownloadRequestCode(int code) {
        if (stringSparseArray.get(code) != null) {
            downloadRequests.get(stringSparseArray.get(code));
        }
    }

    /**
     * 根据url取消
     *
     * @param url
     */
    public void cancelByUrl(String url) {
        mDownloadQueue.cancelBySign(url);
        downloadRequests.remove(url);
        stringSparseArray.remove(stringSparseArray.indexOfValue(url));
    }

    /**
     * 根据code取消
     *
     * @param code
     */
    public void cancelByWhat(int code) {
        if (stringSparseArray.get(code) != null) {
            mDownloadQueue.cancelBySign(stringSparseArray.get(code));
            downloadRequests.remove(stringSparseArray.get(code));
            stringSparseArray.remove(code);
        }
    }

    /**
     * 取消所有
     *
     * @param
     */
    public void cancelAll() {
        mDownloadQueue.cancelAll();
        downloadRequests.clear();
        stringSparseArray.clear();
    }

}
