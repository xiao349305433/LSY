package met.hx.com.base.base.webview;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import met.hx.com.base.base.webview.jsbridge.BridgeWebView;

/**
 * https://github.com/lzyzsd/JsBridge
 */
public class BaseWebView extends BridgeWebView {
    public Context mContext;
    public ProgressBar progressBar;
    private Handler handler = new Handler();

    private Runnable runnable;

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initSon(context);
    }

    public BaseWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initSon(context);
    }

    public BaseWebView(Context context) {
        super(context);
        initSon(context);
    }


    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    private void initSon(Context context) {
        this.mContext = context;
        WebSettings setting = getSettings();
        setting.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        setting.setJavaScriptEnabled(true);//支持JS
        setting.setSupportZoom(true);//支持缩放
        setting.setBuiltInZoomControls(true); // 原网页基础上缩放
        setting.setUseWideViewPort(true);//将图片调整到适合WebView的大小
        setting.setLoadWithOverviewMode(true);//缩放至屏幕大小
        setting.setDomStorageEnabled(true);
        setting.setDisplayZoomControls(false);//隐藏原生的缩放控件
        setting.setBlockNetworkImage(false);
        setting.setBlockNetworkLoads(false);
        setting.setTextZoom(100);//使得用户设置字体大小不干扰到web内部的字体
        // webview 从Lollipop(5.0)开始 webview默认不允许混合模式，https当中不能加载http资源，如果要加载，需单独设置开启。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //增加统一的代理串：前端根据代理串判断访问者身份
        String userAgent = setting.getUserAgentString();
        setting.setUserAgentString(userAgent + " jrtoo");
        setHorizontalScrollBarEnabled(false);// 水平不显示
        setVerticalScrollBarEnabled(false); // 垂直不显示
        setWebViewClient(new BaseWebViewClient(this));
        addJavascriptInterface(this, "Resize");
    }

    @Override
    public void destroy() {
        if (null != handler) {
            handler.removeCallbacks(runnable);
            handler = null;
        }
        if (null != runnable) {
            runnable = null;
        }
        if (null != mContext) {
            mContext = null;
        }
        super.destroy();
    }


    /**
     * 获取网页高度
     */
    public void computeHeight() {
        loadUrl("javascript:Resize.fetchHeight(document.body.getBoundingClientRect().height)");
    }

    @JavascriptInterface
    public void fetchHeight(final float height) {
        final int newHeight = (int) (height * getResources().getDisplayMetrics().density);
        runnable = new Runnable() {
            @Override
            public void run() {
                if (getLayoutParams() instanceof LinearLayout.LayoutParams) {
                    LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) getLayoutParams();
                    linearParams.height = newHeight;
                    setLayoutParams(linearParams);
                }
            }
        };
        if (null != handler) {
            handler.postDelayed(runnable, 50);
        }
    }

}


