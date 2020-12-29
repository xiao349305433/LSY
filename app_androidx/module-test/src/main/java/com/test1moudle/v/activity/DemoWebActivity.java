package com.test1moudle.v.activity;

import android.view.View;
import android.webkit.WebView;


import met.hx.com.base.base.webview.BaseWebChromeClient;
import met.hx.com.base.base.webview.BaseWebViewClient;
import met.hx.com.base.basemvp.v.activity.WebViewYesPresenterActivity;
import met.hx.com.librarybase.some_utils.LogUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;


//@Route(path = C.DemoWebActivity)
public class DemoWebActivity extends WebViewYesPresenterActivity{


    @Override
    protected BaseWebChromeClient getDefaultWebChromeClient() {
        BaseWebChromeClient baseWebChromeClient = new BaseWebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (XHStringUtil.isEmpty(DemoWebActivity.this.title, false)) {
                    if (marqueeTextView != null && !title.contains("admin.jrtoo.com")) {
                        marqueeTextView.setText(title);
                    }
                }
            }
        };
        return baseWebChromeClient;

    }

    @Override
    protected BaseWebViewClient getDefaultWebViewClient() {
        BaseWebViewClient baseWebViewClient = new BaseWebViewClient(mWebView) {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (webTextLeft != null) {
                    if (view.canGoBack()) {
                        webTextLeft.setVisibility(View.VISIBLE);
                    } else {
                        webTextLeft.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtils.i("shouldOverrideUrlLoading1111111111111"+url);
                return super.shouldOverrideUrlLoading(view, url);
            }

        };
        return baseWebViewClient;
    }
}
