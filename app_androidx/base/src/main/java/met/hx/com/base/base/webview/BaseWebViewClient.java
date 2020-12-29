package met.hx.com.base.base.webview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.widget.ProgressBar;

import androidx.annotation.CallSuper;

import java.lang.ref.WeakReference;

import met.hx.com.base.base.webview.jsbridge.BridgeWebViewClient;
import met.hx.com.base.baseinterface.ISmoothTarget;
import met.hx.com.librarybase.some_utils.LogUtils;

public class BaseWebViewClient extends BridgeWebViewClient implements ISmoothTarget {
    private ProgressBar mProgressBar;
    private SmoothHandler smoothHandler;
        private BaseWebView baseWebView;
        public boolean needComputeHeight = true;

        public BaseWebViewClient(BaseWebView webView) {
            super(webView);
            this.baseWebView=webView;
            this.mProgressBar = webView.progressBar;
        }

        public void setNeedComputeHeight(boolean needComputeHeight) {
            this.needComputeHeight = needComputeHeight;
        }

        @CallSuper
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
        }

        @CallSuper
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (mProgressBar != null) {
                setSmoothPercent(0);
                setSmoothPercent(0.9f, 800);
            }
        }

        @CallSuper
        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            return super.shouldOverrideKeyEvent(view, event);
        }

        @CallSuper
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (needComputeHeight) {
                baseWebView.computeHeight();
            }
            if (url.contains("tel:")) { //拨打电话处理逻辑
                String[] num = url.split(":");
                if (num.length == 2) {
                    callPhoneNum(num[1]);
                }
                return true;
            } else { //内部跳转逻辑
                return super.shouldOverrideUrlLoading(view, url);
            }
        }


        /**
         * 拨打电话
         *
         * @param phoneNum 号码
         */
        private void callPhoneNum(String phoneNum) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNum));
            if (null != baseWebView.mContext)
                baseWebView.mContext.startActivity(intent);
        }

        @CallSuper
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (mProgressBar != null) {
                setSmoothPercent(1, 200);
            }
        }

        @CallSuper
        @Override
        public void onReceivedError(WebView webView, int i, String s, String s1) {
            super.onReceivedError(webView, i, s, s1);
        }

        @CallSuper
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            handler.proceed();  // 接受所有网站的证书
        }


        @Override
        public float getPercent() {
            return mProgressBar.getProgress() / (float) mProgressBar.getMax();
        }

        @Override
        public void setPercent(float percent) {
            LogUtils.i(percent + "......");
            if (percent >= 0.99) {
                mProgressBar.setVisibility(View.GONE);
            }
            if (percent == 0) {
                mProgressBar.setVisibility(View.VISIBLE);
            }
            mProgressBar.setProgress((int) Math.ceil(percent * mProgressBar.getMax()));
        }


        @Override
        public void setSmoothPercent(float percent) {
            getSmoothHandler().loopSmooth(percent);
        }

        @Override
        public void setSmoothPercent(float percent, long durationMillis) {
            getSmoothHandler().loopSmooth(percent, durationMillis);
        }

        private SmoothHandler getSmoothHandler() {
            if (smoothHandler == null) {
                smoothHandler = new SmoothHandler(new WeakReference<ISmoothTarget>(this));
            }
            return smoothHandler;
        }
    }