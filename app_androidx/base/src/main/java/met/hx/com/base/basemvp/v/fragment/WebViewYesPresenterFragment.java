package met.hx.com.base.basemvp.v.fragment;

import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;

import met.hx.com.base.R;
import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.BasePresenter;
import met.hx.com.base.base.fragment.BaseYesPresenterFragment;
import met.hx.com.base.base.webview.BaseWebChromeClient;
import met.hx.com.base.base.webview.BaseWebView;
import met.hx.com.base.base.webview.BaseWebViewClient;
import met.hx.com.base.baseconfig.C;
import met.hx.com.librarybase.some_utils.LogUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.views.MarqueeTextView;

/**
 * Created by huxu on 2017/12/22.
 */
@Route(path = C.WebFragment)
public class WebViewYesPresenterFragment<P extends BasePresenter> extends BaseYesPresenterFragment {
    protected String title;
    private int titleColor;
    private int statusColor;
    private int titleTextColor;
    public String url;
    private boolean hasTitle = true;
    private boolean hasProgress = true;

    protected BaseWebView mWebView;
    protected ProgressBar mProgressBar;
    protected MarqueeTextView marqueeTextView;
    protected P mPresenter;
    private boolean canBack = true;
    //progressBar下面的颜色
    private int belowProgressColor;
    //progressBar上面的颜色
    private int onProgressColor;
    private ImageView webImage;
    private TextView webTextLeft;
    private int titleLeftTextColor;
    private int titleLeftImage;
    private boolean loadBusiness;

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
    public void onInitAttribute(BaseAttribute ba) {
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
        title = getArguments().getString("title");
        titleColor = getArguments().getInt("titleColor", 0);
        titleTextColor = getArguments().getInt("titleTextColor", 0);
        titleLeftTextColor = getArguments().getInt("titleLeftTextColor", 0);
        titleLeftImage = getArguments().getInt("titleLeftImage", 0);
        statusColor = getArguments().getInt("statusColor", 0);
        belowProgressColor = getArguments().getInt("belowProgressColor", 0);
        onProgressColor = getArguments().getInt("onProgressColor", 0);
        url = getArguments().getString("url");
        hasTitle = getArguments().getBoolean("hasTitle", true);
        hasProgress = getArguments().getBoolean("hasProgress", true);
        canBack = getArguments().getBoolean("canBack", true);
        loadBusiness = getArguments().getBoolean("loadBusiness", false);
    }

    @Override
    public void initView(View view, Bundle bundle) {
        mWebView = (BaseWebView) view.findViewById(R.id.webview);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
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
        marqueeTextView = (MarqueeTextView) view.findViewById(R.id.base_text_middle);
        if (mBaseAttribute.mTitleTextColorId != 0 && marqueeTextView != null) {
            marqueeTextView.setTextColor(getResources().getColor(mBaseAttribute.mTitleTextColorId));
        }
    }


    @Override
    protected void initData(Bundle bundle) {
        if (viewContent.findViewById(R.id.web_image) != null) {
            webImage = (ImageView) viewContent.findViewById(R.id.web_image);
            if (titleLeftImage != 0) {
                webImage.setImageDrawable(getResources().getDrawable(titleLeftImage));
            }
            webImage.setOnClickListener(this::onClick);
        }
        if (viewContent.findViewById(R.id.web_text_left) != null) {
            webTextLeft = (TextView) viewContent.findViewById(R.id.web_text_left);
            if (titleLeftTextColor != 0) {
                webTextLeft.setTextColor(getResources().getColor(titleLeftTextColor));
            }
            webTextLeft.setOnClickListener(this::onClick);
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
                if (XHStringUtil.isEmpty(WebViewYesPresenterFragment.this.title, false)) {
                    LogUtils.i("标题" + title);
                    if (marqueeTextView != null) {
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


    // 设置回退
    public void webBack() {
        if (canBack) {
            if (mWebView != null) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    getActivity().finish();
                }
            }

        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mWebView!=null){
            mWebView.removeAllViews();
            mWebView.destroy();
        }
    }
    @Override
    protected void onLeftClick(RelativeLayout relativeLayoutLeft) {
        webBack();
    }

    public void onClick(View v) {
        if (v.getId() == R.id.web_image) {
            webBack();
        } else if (v.getId() == R.id.web_text_left) {
            getActivity().finish();
        }

    }
}
