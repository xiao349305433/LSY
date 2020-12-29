package met.hx.com.base.base.activity;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.launcher.ARouter;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import met.hx.com.base.R;
import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.base.player.NiceVideoPlayerManager;
import met.hx.com.base.base.statusbar.StatusBarCompat;
import met.hx.com.base.baseevent.EventBusUtils;
import met.hx.com.base.baseinterface.LifeCycleListener;
import met.hx.com.base.basemvp.eventbus.DialogId;
import met.hx.com.base.util.OutOfTimeJumpManager;
import met.hx.com.librarybase.some_utils.ImageUtils;
import met.hx.com.librarybase.some_utils.KeyboardUtils;
import met.hx.com.librarybase.some_utils.LogUtils;
import met.hx.com.librarybase.some_utils.ScreenUtils;
import met.hx.com.librarybase.some_utils.SizeUtils;
import met.hx.com.librarybase.some_utils.ToastManager;
import met.hx.com.librarybase.some_utils.XActivityUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.some_utils.util.AbViewUtil;
import met.hx.com.librarybase.views.MarqueeTextView;
import me.imid.swipebacklayout.lib.SwipeBackLayout;

/**
 * Created by huxu on 2017/11/20.
 */

public abstract class BaseActivity extends SwipeBackActivity {
    protected BaseAttribute mBaseAttribute;
    public View rootView;
    public FrameLayout frameLayoutRoot;
    protected List<LifeCycleListener> lifeCycleListeners = new ArrayList<>();
    protected Toolbar toolbar;
    protected View statusBar;
    protected RelativeLayout loadingProgress;
    public  RelativeLayout relativeLayoutRight;
    protected Handler handlerBase = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    onLoadingDismissTimeOut();
                    LoadingDialogUtil.dismiss(loadingProgress);
                    break;
                case 3:
                    onLoadingDismissTimeOut();
                    LoadingDialogUtil.dismiss(loadingProgress);
                    break;
            }


        }
    };

    /**
     * 加载框超时消失时
     */
    protected void onLoadingDismissTimeOut() {
    
    }

    protected ToastManager mToastManager;
    private FrameLayout.LayoutParams loadingLayoutParams;
    private String fragmentTag;

    @CallSuper
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onEnterAnim();
        if (mToastManager == null) {
            mToastManager = ToastManager.getInstance(this);
        }
        initLifeCycle();
        for (LifeCycleListener lifeCycleListener : lifeCycleListeners) {
            lifeCycleListener.onCreate(savedInstanceState, this);
        }
        ARouter.getInstance().inject(this);
        onInitAttribute(mBaseAttribute = new BaseAttribute());
        EventBusUtils.register(this);
        setLayout();
        if (frameLayoutRoot != null) {
            setContentView(frameLayoutRoot);
            initTransitionView();
            initToolBar();
            initImmersionBar();
            if (toolbar != null) {
                setToolbarHeight(AbViewUtil.getViewHeight(toolbar), null);
            }
        }
        initSwipeBack();
        initView();
    }

    /**
     * 主线程吐司
     *
     * @param string
     */
    public void sendMessageToast(String string) {
        ToastManager.getInstance().showLong(string);
    }

    public void sendMessageToast(String string, boolean isDialog) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(BaseActivity.this)
                        .setTitle("提示！！！")
                        .setMessage(string)
                        .setPositiveButton("好的", null)
                        .show();
            }
        });

    }

    /**
     * 启动界面时动画
     */
    protected void onEnterAnim() {
        overridePendingTransition(R.anim.base_enter_anim, R.anim.base_exit_anim);
    }

    /**
     * 退出界面时动画
     */
    protected void onExitAnim() {
        overridePendingTransition(R.anim.base_close_enter_anim, R.anim.base_close_exit_anim);
    }

    /**
     * 初始化侧滑返回
     */
    private void initSwipeBack() {
        getSwipeBackLayout().setEdgeSize(ScreenUtils.getScreenWidth() / 3);
        setSwipeBackEdgeFlags(onSwipeBackEdgeFlags());
        setSwipeBackEnable(onSwipeBackEnable());
    }

    /**
     * 设置侧滑返回参数，默认侧滑左边返回
     */
    protected void setSwipeBackEdgeFlags(int edgeFlags) {
        getSwipeBackLayout().setEdgeTrackingEnabled(edgeFlags);
    }

    /**
     * 获取侧滑返回参数
     *
     * @return
     */
    protected int onSwipeBackEdgeFlags() {
        return SwipeBackLayout.EDGE_LEFT;
    }

    /**
     * 是否支持侧滑返回
     *
     * @return
     */
    protected boolean onSwipeBackEnable() {
        return false;
    }

    /**
     * 子类要用生命周期需要重写这个方法
     */
    protected void initLifeCycle() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getDialogId(DialogId dialogId) {
        if (dialogId.getActivityTag().equals(this.getClass().getName())) {
            if (OutOfTimeJumpManager.getInstance().getShowDialog()) {
                if (dialogId.isShowTypeFirst()) {
                    handlerBase.sendEmptyMessageDelayed(1, 10 * 1000);
                } else {
                    if (dialogId.isShowTypeSecond()) {
                        handlerBase.sendEmptyMessageDelayed(3, dialogId.getShowTime());
                    }
                }
            } else {
                if (dialogId.isShowTypeSecond()) {
                    handlerBase.sendEmptyMessageDelayed(3, dialogId.getShowTime());
                }
            }

            if (!XHStringUtil.isEmpty(fragmentTag, false)) {
                if (dialogId.getType() == DialogId.TYPE_SHOW) {
                    if (!isDestroyed()) {
                        if (!XHStringUtil.isEmpty(dialogId.getText(), false)) {
                            LoadingDialogUtil.show(loadingProgress, dialogId.getText());
                        } else {
                            LoadingDialogUtil.show(loadingProgress, "加载中...");
                        }
                    }

                } else {
                    LoadingDialogUtil.dismiss(loadingProgress);
                    handlerBase.removeMessages(1);
                    handlerBase.removeMessages(3);
                }
            } else {
                if (dialogId.getType() == DialogId.TYPE_SHOW) {
                    if (!isDestroyed()) {
                        if (!XHStringUtil.isEmpty(dialogId.getText(), false)) {
                            LoadingDialogUtil.show(loadingProgress, dialogId.getText());
                        } else {
                            LoadingDialogUtil.show(loadingProgress, "加载中...");
                        }

                    }
                } else {
                    LoadingDialogUtil.dismiss(loadingProgress);
                    handlerBase.removeMessages(1);
                    handlerBase.removeMessages(3);
                }
            }

        }
    }

    /**
     * 给加载框设置marginTop，避免遮住返回键
     *
     * @param toolbarHeight
     */
    public void setToolbarHeight(int toolbarHeight, String fragmentTag) {
        loadingLayoutParams.topMargin = toolbarHeight;
        this.fragmentTag = fragmentTag;
    }

    /**
     * 判断当前显示的activity是否在栈顶
     *
     * @return
     */
    protected boolean isTopTaskAcitivity() {
        if (this.getClass().getName().equals(XActivityUtils.getTopActivity().getClass().getName())) {
            return true;
        } else {
            return false;
        }
    }

    private void initToolBar() {
        if (mBaseAttribute.mHasTitle) {
            boolean isDefineToolbar = false;
            if (mBaseAttribute.toolBarId != 0) {
                toolbar = (Toolbar) findViewById(mBaseAttribute.toolBarId);
                isDefineToolbar = true;
            } else if (findViewById(R.id.base_m_toolbar) != null) {
                toolbar = (Toolbar) findViewById(R.id.base_m_toolbar);
                isDefineToolbar = false;
            } else {
                if (mBaseAttribute.mHasTitle) {
                    LogUtils.w("没指定ToolBar");
                }
                return;
            }
            if (mBaseAttribute.statusBarId != 0) {
                statusBar = findViewById(mBaseAttribute.statusBarId);
            } else if (findViewById(R.id.base_status_bar) != null) {
                statusBar = findViewById(R.id.base_status_bar);
            } else {
                statusBar = null;
            }
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(mBaseAttribute.displayShowTitleEnabled);//隐藏默认标题
            if (!isDefineToolbar) {
                RelativeLayout relativeLayoutLeft = (RelativeLayout) findViewById(R.id.base_relative_left);
                TextView baseTextLeft = (TextView) findViewById(R.id.base_text_left);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) relativeLayoutLeft.getLayoutParams();
                layoutParams.width = SizeUtils.dp2px(mBaseAttribute.mTitleLeftRightWidth);
                if (mBaseAttribute.mAddBackButton) {
                    relativeLayoutLeft.setOnClickListener(view -> {
                        onLeftClick(relativeLayoutLeft);
                    });
                    relativeLayoutLeft.setVisibility(View.VISIBLE);
                    if (mBaseAttribute.mTitleLeftLayoutId != 0) {
                        baseTextLeft.setVisibility(View.GONE);
                        getLayoutInflater().inflate(mBaseAttribute.mTitleLeftLayoutId, relativeLayoutLeft);
                    } else {
                        baseTextLeft.setVisibility(View.VISIBLE);
                        if (mBaseAttribute.mHasLeftDrawable) {
                            if (mBaseAttribute.mBackButtonResource != 0) {
                                baseTextLeft.setCompoundDrawables(getTextDrawable(mBaseAttribute.mBackButtonResource, mBaseAttribute.mTitleLeftImageIconWidth, mBaseAttribute.mTitleLeftImageIconHeight), null, null, null);
                            } else {
                                baseTextLeft.setCompoundDrawables(getTextDrawable(R.drawable.base_qh_sy_fh), null, null, null);
                            }
                        }
                        if (!XHStringUtil.isEmpty(mBaseAttribute.mTitleLeftText, false)) {
                            baseTextLeft.setText(mBaseAttribute.mTitleLeftText);
                        }
                        if (mBaseAttribute.mTitleLeftTextSizeId != 0) {
                            baseTextLeft.setTextSize(getResources().getDimension(mBaseAttribute.mTitleLeftTextSizeId));
                        }
                        if (mBaseAttribute.mTitleLeftTextId != 0) {
                            baseTextLeft.setText(getResources().getString(mBaseAttribute.mTitleLeftTextId));
                        }
                        if (mBaseAttribute.mTitleLeftTextButtonColorId != 0) {
                            baseTextLeft.setTextColor(getResources().getColor(mBaseAttribute.mTitleLeftTextButtonColorId));
                        }
                    }
                } else {
                    relativeLayoutLeft.setVisibility(View.INVISIBLE);
                }
            }
            if (!isDefineToolbar) {
                MarqueeTextView marqueeTextView = null;
                if (findViewById(R.id.base_text_middle) != null) {
                    marqueeTextView = (MarqueeTextView) findViewById(R.id.base_text_middle);
                }
                if (marqueeTextView != null) {
                    if (mBaseAttribute.mShowTitleText) {
                        if (!XHStringUtil.isEmpty(mBaseAttribute.mTitleText, false) && marqueeTextView != null) {
                            marqueeTextView.setText(mBaseAttribute.mTitleText);
                            if (mBaseAttribute.mTitleTextColorId != 0) {
                                marqueeTextView.setTextColor(getResources().getColor(mBaseAttribute.mTitleTextColorId));
                            }
                        }
                        if (mBaseAttribute.mTitleTextStringId != 0) {
                            marqueeTextView.setText(getResources().getString(mBaseAttribute.mTitleTextStringId));
                            if (mBaseAttribute.mTitleTextColorId != 0) {
                                marqueeTextView.setTextColor(getResources().getColor(mBaseAttribute.mTitleTextColorId));
                            }
                        }
                        if (mBaseAttribute.titleId != 0) {
                            FrameLayout frameLayout = (FrameLayout) findViewById(R.id.base_title_frame);
                            getLayoutInflater().inflate(mBaseAttribute.titleId, frameLayout);
                            marqueeTextView.setVisibility(View.GONE);
                        } else {
                            marqueeTextView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        marqueeTextView.setVisibility(View.GONE);
                    }
                }
            }
            if (mBaseAttribute.mTitleBackgroundColorId != 0) {
                toolbar.setBackgroundResource(mBaseAttribute.mTitleBackgroundColorId);
            }
            if (!isDefineToolbar) {
                relativeLayoutRight = (RelativeLayout) findViewById(R.id.base_relative_right);
                TextView baseTextRight = (TextView) findViewById(R.id.base_text_right);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) relativeLayoutRight.getLayoutParams();
                layoutParams.width = SizeUtils.dp2px(mBaseAttribute.mTitleLeftRightWidth);
                if (mBaseAttribute.mHasRightView) {
                    relativeLayoutRight.setOnClickListener(view -> {
                        onRightClick(relativeLayoutRight);
                    });
                    relativeLayoutRight.setVisibility(View.VISIBLE);
                    if (mBaseAttribute.mTitleRightLayoutId != 0) {
                        baseTextRight.setVisibility(View.GONE);
                        getLayoutInflater().inflate(mBaseAttribute.mTitleRightLayoutId, relativeLayoutRight);
                    } else {
                        baseTextRight.setVisibility(View.VISIBLE);
                        if (!XHStringUtil.isEmpty(mBaseAttribute.mTitleRightText, false)) {
                            baseTextRight.setText(mBaseAttribute.mTitleRightText);
                        }
                        if (mBaseAttribute.mTitleRightTextSizeId != 0) {
                            baseTextRight.setTextSize(getResources().getDimension(mBaseAttribute.mTitleRightTextSizeId));
                        }
                        if (mBaseAttribute.mTitleRightTextId != 0) {
                            baseTextRight.setText(getResources().getString(mBaseAttribute.mTitleRightTextId));
                        }
                        if (mBaseAttribute.mTitleRightTextButtonColorId != 0) {
                            baseTextRight.setTextColor(getResources().getColor(mBaseAttribute.mTitleRightTextButtonColorId));
                        }
                        if (mBaseAttribute.mTitleRightImageIcon != 0) {
                            baseTextRight.setCompoundDrawables(getTextDrawable(mBaseAttribute.mTitleRightImageIcon, mBaseAttribute.mTitleRightImageIconWidth, mBaseAttribute.mTitleRightImageIconHeight), null, null, null);
                        }
                    }
                } else {
                    relativeLayoutRight.setVisibility(View.INVISIBLE);
                }
            }

        } else {
            if (findViewById(R.id.base_bar) != null) {
                findViewById(R.id.base_status_bar).setVisibility(View.GONE);
                findViewById(R.id.base_m_toolbar).setVisibility(View.GONE);
            }
            if (mBaseAttribute.statusBarId != 0) {
                statusBar = findViewById(mBaseAttribute.statusBarId);
            } else if (findViewById(R.id.base_status_bar) != null) {
                statusBar = findViewById(R.id.base_status_bar);
            } else {
                statusBar = null;
            }
        }

    }

    /**
     * 为了设置TextView的drawable
     *
     * @param id
     * @return
     */

    public Drawable getTextDrawable(@DrawableRes int id) {
        Drawable drawable = ImageUtils.getDrawableFromId(this, id, R.drawable.base_qh_sy_fh);
        setTextDrawableBound(drawable, 0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    public Drawable getTextDrawable(@DrawableRes int id, int width, int height) {
        Drawable drawable = ImageUtils.getDrawableFromId(this, id, R.drawable.base_qh_sy_fh);
        if (width == 0) {
            width = drawable.getMinimumWidth();
        } else {
            width = SizeUtils.dp2px(width);
        }
        if (height == 0) {
            height = drawable.getMinimumHeight();
        } else {
            height = SizeUtils.dp2px(height);
        }
        setTextDrawableBound(drawable, 0, 0, width, height);
        return drawable;
    }

    private Drawable setTextDrawableBound(Drawable drawable, int left, int top, int right, int bottom) {
        drawable.setBounds(left, top, right, bottom);
        return drawable;
    }


    public void onRightClick(View item) {
        LogUtils.i("点击了右边按钮");
    }

    private void initImmersionBar() {
        if (mBaseAttribute != null) {
            if (mBaseAttribute.mStatusBarActivityEnabled) {
                if (!mBaseAttribute.mStatusTranslucent) {
                    StatusBarCompat.setStatusBarColor(this, getResources().getColor(mBaseAttribute.mStatusBackgroundColorId), mBaseAttribute.mStatusBackgroundAlpha);
                } else {
                    StatusBarCompat.translucentStatusBar(this);
                }
            }
        }
    }


    protected void initTransitionView() {//在这里给转场view副值
    }

    /**
     * 为了viewBinder设置生命周期用的，如果要使用recycleview框架的生命周期，必须先注册
     */
    public void registerLifeCycle(LifeCycleListener lifeCycleListener) {
        if (lifeCycleListener != null) {
            lifeCycleListeners.add(lifeCycleListener);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        KeyboardUtils.hideSoftInput(this);
    }

    /**
     * 左边图标点击事件
     */
    protected void onLeftClick(RelativeLayout relativeLayoutLeft) {
        onBackPressed();
    }

    private void setLayout() {
        if (mBaseAttribute.mActivityLayoutId != 0) {
            View linear = getLayoutInflater().inflate(R.layout.base_content, null);
            rootView = getLayoutInflater().inflate(mBaseAttribute.mActivityLayoutId, null);
            frameLayoutRoot = (FrameLayout) linear.findViewById(R.id.frame_content);
            frameLayoutRoot.addView(rootView);
            getLayoutInflater().inflate(R.layout.base_progress, frameLayoutRoot);
            loadingProgress = (RelativeLayout) frameLayoutRoot.findViewById(R.id.base_relative_progress);
            loadingLayoutParams = (FrameLayout.LayoutParams) loadingProgress.getLayoutParams();
        }
    }

    protected abstract void onInitAttribute(BaseAttribute ba);

    protected abstract void initView();


    @CallSuper
    @Override
    protected void onStop() {
        super.onStop();
        for (LifeCycleListener lifeCycleListener : lifeCycleListeners) {
            lifeCycleListener.onStop();
        }
    }

    @CallSuper
    @Override
    protected void onPause() {
        super.onPause();
        for (LifeCycleListener lifeCycleListener : lifeCycleListeners) {
            lifeCycleListener.onPause();
        }
    }

    @CallSuper
    @Override
    protected void onResume() {
        super.onResume();
        for (LifeCycleListener lifeCycleListener : lifeCycleListeners) {
            lifeCycleListener.onResume();
        }
    }

    @CallSuper
    @Override
    protected void onStart() {
        super.onStart();
        for (LifeCycleListener lifeCycleListener : lifeCycleListeners) {
            lifeCycleListener.onStart();
        }
    }

    @CallSuper
    @Override
    protected void onRestart() {
        super.onRestart();
        for (LifeCycleListener lifeCycleListener : lifeCycleListeners) {
            lifeCycleListener.onRestart();
        }
    }


    @CallSuper
    @Override
    protected void onDestroy() {
        if (XActivityUtils.getTopActivity() != null) {
            LoadingDialogUtil.dismissByEvent(XActivityUtils.getTopActivity().getClass().getName());
        }
        EventBusUtils.unregister(this);
        for (LifeCycleListener lifeCycleListener : lifeCycleListeners) {
            lifeCycleListener.onDestroy();
        }
        super.onDestroy();

    }


    /**
     * 碎片添加
     *
     * @param fragment 碎片
     * @param layoutId 碎片容器
     * @param bundle   碎片数据绑定
     */
    public void addLocalFragment(Fragment fragment, int layoutId, Bundle bundle) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (bundle != null) {
            fragment.setArguments(bundle);
        }
        transaction.replace(layoutId, fragment);
        transaction.commitAllowingStateLoss();
    }


    /**
     * 碎片删除
     *
     * @param fragment     碎片
     * @param ifViewDelete 是否有需要删除的布局 （不在fragment里面）
     * @param layoutId     布局上需要删除的部分（不在fragment里面）
     */
    public void deleteLocalFragment(Fragment fragment, boolean ifViewDelete, int layoutId) {
        if (ifViewDelete) {//删除布局
            if (findViewById(layoutId) != null) {
                findViewById(layoutId).setVisibility(View.GONE);
            }
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(fragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        if (NiceVideoPlayerManager.instance().onBackPressd()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
        onExitAnim();
    }
}
