package com.test1moudle.v.activity;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.test1moudle.R;
import com.wu.loushanyun.base.config.K;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.webview.BaseWebView;
import met.hx.com.base.base.webview.jsbridge.BridgeHandler;
import met.hx.com.base.base.webview.jsbridge.CallBackFunction;
import met.hx.com.base.baseevent.Event;

@Route(path = K.JsBridgeActivity)
public class JsBridgeActivity extends BaseNoPresenterActivity implements View.OnClickListener {
    private final String TAG = "MainActivity";

    BaseWebView webView;

    Button button;
    Button button1;

    int RESULT_CODE = 0;

    ValueCallback<Uri> mUploadMessage;

    ValueCallback<Uri[]> mUploadMessageArray;

    static class Location {
        String address;
    }

    static class User {
        String name;
        Location location;
        String testStr;
    }


    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId=R.layout.m_test_mainactivity;

    }

    @Override
    protected void initView() {
        webView = (BaseWebView) findViewById(R.id.webView);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        //web页打开原生的文件选择器
        webView.setWebChromeClient(new WebChromeClient() {

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
                this.openFileChooser(uploadMsg);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
                this.openFileChooser(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                pickFile();
            }

            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                mUploadMessageArray = filePathCallback;
                pickFile();
                return true;
            }
        });

     webView.loadUrl("file:///android_asset/tianditu3.html");
      //  webView.loadUrl(URLUtils.getIP() + URLUtils.TianDiTuAddress);
        /**
         * 监听web页传来的信息
         */
        webView.registerHandler("submitFromWeb", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                Log.i(TAG, "接收到web页发来的消息 = " + data);
            }

        });


//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                webView.callHandler("functionInJs", "江滩", new CallBackFunction() {
//
//                    @Override
//                    public void onCallBack(String data) {
//                        // TODO Auto-generated method stub
//                        Log.i(TAG, "发送成功 " + data);
//                    }
//
//                });
//            }
//        });
    }

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    public void pickFile() {
        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType("image/*");
        startActivityForResult(chooserIntent, RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
    }

    @Override
    public void onClick(View v) {
        if (button.equals(v)) {

            webView.callHandler("functionInJs", "{\"Key\":武汉江滩}", new CallBackFunction() {

                @Override
                public void onCallBack(String data) {
                    // TODO Auto-generated method stub
                    Log.i(TAG, "发送成功 " + data);
                }

            });

//            webView.callHandler("functionInJs", "{\"Lng\":116.40969,\"Lat\":39.89945}", new CallBackFunction() {
//
//                @Override
//                public void onCallBack(String data) {
//                    // TODO Auto-generated method stub
//                    Log.i(TAG, "发送成功 " + data);
//                }
//
//            });
        }

    }
}
