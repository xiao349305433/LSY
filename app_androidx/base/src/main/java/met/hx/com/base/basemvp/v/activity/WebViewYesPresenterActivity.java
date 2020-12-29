package met.hx.com.base.basemvp.v.activity;

import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;

import met.hx.com.base.R;
import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.BasePresenter;
import met.hx.com.base.base.activity.BaseYesPresenterActivity;
import met.hx.com.base.base.webview.BaseWebChromeClient;
import met.hx.com.base.base.webview.BaseWebView;
import met.hx.com.base.base.webview.BaseWebViewClient;
import met.hx.com.base.baseconfig.C;
import met.hx.com.librarybase.some_utils.LogUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.views.MarqueeTextView;

/**
 * Created by huxu on 2017/3/15.
 */
@Route(path = C.WEB)
public class WebViewYesPresenterActivity<P extends BasePresenter> extends BaseYesPresenterActivity {

    protected String title;
    private int titleColor;
    private int statusColor;
    private int titleTextColor;
    //progressBar下面的颜色
    private int belowProgressColor;
    //progressBar上面的颜色
    private int onProgressColor;
    public String url;
    private boolean hasTitle = true;
    private boolean hasProgress = true;
    private boolean canBack = true;

    protected BaseWebView mWebView;
    protected ProgressBar mProgressBar;
    protected MarqueeTextView marqueeTextView;
    protected P mPresenter;
    private ImageView webImage;
    protected TextView webTextLeft;
    private int titleLeftTextColor;
    private int titleLeftImage;

    @Override
    protected void initLifeCycle() {
        mPresenter = (P) initPresenter();
        registerLifeCycle(mPresenter);
    }

    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        getAllData();
        ba.mActivityLayoutId = R.layout.base_activity_webview;
        ba.mHasTitle = hasTitle;
        ba.mTitleText = title;
        ba.mTitleTextColorId = titleTextColor;
        ba.mTitleBackgroundColorId = titleColor;
        ba.mStatusBackgroundColorId = statusColor;
        ba.mTitleLeftLayoutId = R.layout.base_web_left_layout;
        ba.mTitleLeftRightWidth = 100;
    }

    private void getAllData() {
        title = getIntent().getStringExtra("title");
        titleColor = getIntent().getIntExtra("titleColor", 0);
        titleLeftTextColor = getIntent().getIntExtra("titleLeftTextColor", 0);
        titleLeftImage = getIntent().getIntExtra("titleLeftImage", 0);
        titleTextColor = getIntent().getIntExtra("titleTextColor", 0);
        statusColor = getIntent().getIntExtra("statusColor", 0);
        belowProgressColor = getIntent().getIntExtra("belowProgressColor", 0);
        onProgressColor = getIntent().getIntExtra("onProgressColor", 0);
        url = getIntent().getStringExtra("url");
        hasTitle = getIntent().getBooleanExtra("hasTitle", true);
        hasProgress = getIntent().getBooleanExtra("hasProgress", true);
        canBack = getIntent().getBooleanExtra("canBack", true);
    }

    @Override
    protected void initView() {
        if (findViewById(R.id.web_image) != null) {
            webImage = (ImageView) findViewById(R.id.web_image);
            if (titleLeftImage != 0) {
                webImage.setImageDrawable(getResources().getDrawable(titleLeftImage));
            }
            webImage.setOnClickListener(this::onClick);
        }
        if (findViewById(R.id.web_text_left) != null) {
            webTextLeft = (TextView) findViewById(R.id.web_text_left);
            if (titleLeftTextColor != 0) {
                webTextLeft.setTextColor(getResources().getColor(titleLeftTextColor));
            }
            webTextLeft.setOnClickListener(this::onClick);
        }
        mWebView = (BaseWebView) findViewById(R.id.webview);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        if (onProgressColor != 0) {
            ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(onProgressColor));
            ClipDrawable clipDrawable = new ClipDrawable(colorDrawable, Gravity.LEFT, ClipDrawable.HORIZONTAL);
            Drawable drawable;
            if (belowProgressColor != 0) {
                drawable = getResources().getDrawable(belowProgressColor);
            } else {
                drawable = getResources().getDrawable(R.color.base_trans);
            }
            Drawable[] layers = new Drawable[]{drawable, clipDrawable};
            LayerDrawable layerDrawable = new LayerDrawable(layers);
            layerDrawable.setDrawableByLayerId(0, drawable);
            layerDrawable.setDrawableByLayerId(1, clipDrawable);
            mProgressBar.setProgressDrawable(clipDrawable);
        }
        marqueeTextView = (MarqueeTextView) findViewById(R.id.base_text_middle);
        if (mBaseAttribute.mTitleTextColorId != 0 && marqueeTextView != null) {
            marqueeTextView.setTextColor(getResources().getColor(mBaseAttribute.mTitleTextColorId));
        }
        if (hasProgress) {
            mWebView.setProgressBar(mProgressBar);
        }
        mWebView.setBackgroundColor(0);
        setWebChromeClient();
        loadUrl();
    }

    protected void loadUrl() {
        if (!XHStringUtil.isEmpty(url, false)) {
            if (!url.startsWith("http")) {
                url = "http://" + url;
            }
            LogUtils.i("url="+url);
            mWebView.loadUrl(url);
        }
    }

    /***
     * 重写此方法设置webview的监听
     */
    public void setWebChromeClient() {
        mWebView.setWebChromeClient(getDefaultWebChromeClient());
        mWebView.setWebViewClient(getDefaultWebViewClient());
    }

    protected BaseWebChromeClient getDefaultWebChromeClient() {
        BaseWebChromeClient baseWebChromeClient = new BaseWebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (XHStringUtil.isEmpty(WebViewYesPresenterActivity.this.title, false)) {
                    LogUtils.i("标题" + title);
                    if (marqueeTextView != null && !title.contains("admin.jrtoo.com")) {
                        marqueeTextView.setText(title);
                    }
                }
            }
        };
        return baseWebChromeClient;
    }

    protected BaseWebViewClient getDefaultWebViewClient() {
// 设置关闭按钮显示和隐藏
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
        };
        return baseWebViewClient;
    }

    @Override
    public void onBackPressed() {
        if (canBack) {
            if (mWebView != null) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                    return;
                }
            }
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mWebView!=null){
            mWebView.removeAllViews();
            mWebView.destroy();
        }

    }

    public void onClick(View v) {
        if (v.getId() == R.id.web_image) {
            onBackPressed();
        } else if (v.getId() == R.id.web_text_left) {
            finish();
        }

    }
}
