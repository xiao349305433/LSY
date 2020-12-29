package met.hx.com.base.base.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

import met.hx.com.base.R;
import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseActivity;
import met.hx.com.base.base.statusbar.StatusBarCompat;
import met.hx.com.base.baseinterface.LifeCycleListener;
import met.hx.com.librarybase.some_utils.ImageUtils;
import met.hx.com.librarybase.some_utils.LogUtils;
import met.hx.com.librarybase.some_utils.SizeUtils;
import met.hx.com.librarybase.some_utils.ToastManager;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.some_utils.util.AbViewUtil;
import met.hx.com.librarybase.views.MarqueeTextView;

/**
 * Created by huxu on 2017/11/20.
 */

public abstract class BaseFragment extends Fragment {

    protected BaseAttribute mBaseAttribute;
    protected List<LifeCycleListener> lifeCycleListeners = new ArrayList<>();
    protected View viewContent;
    protected Toolbar toolbar;
    protected View statusBar;
    //百度统计使用标记
    protected String statistic = getClass().getName();
    protected boolean mIsVisible;
    protected boolean mIsPrepare;
    protected boolean mIsImmersion;
    protected ToastManager mToastManager;

    /**
     * 注册生命周期
     */
    protected void registerLifeCycle(LifeCycleListener lifeCycleListener) {
        if (lifeCycleListener != null) {
            lifeCycleListeners.add(lifeCycleListener);
        }
    }

    protected void sendMessageToast(String toast){
        if(getActivity()!=null){
            ((BaseActivity)getActivity()).sendMessageToast(toast);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        for (LifeCycleListener lifeCycleListener : lifeCycleListeners) {
            lifeCycleListener.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        for (LifeCycleListener lifeCycleListener : lifeCycleListeners) {
            lifeCycleListener.onStop();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        for (LifeCycleListener lifeCycleListener : lifeCycleListeners) {
            lifeCycleListener.onResume();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        for (LifeCycleListener lifeCycleListener : lifeCycleListeners) {
            lifeCycleListener.onStart();
        }
    }


    @CallSuper
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the item_main_grid_layout for this fragment
        onInitAttribute(mBaseAttribute = new BaseAttribute());
        viewContent = inflater.inflate(mBaseAttribute.mActivityLayoutId, container, false);
        initView(viewContent, getArguments());
        if (mToastManager == null) {
            mToastManager = ToastManager.getInstance(getActivity());
        }
        return viewContent;
    }

    @CallSuper
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initLifeCycle();
        for (LifeCycleListener lifeCycleListener : lifeCycleListeners) {
            lifeCycleListener.onCreate(savedInstanceState, getActivity());
        }
        initToolBar(viewContent);
        initImmersionBar();
        if(toolbar!=null){
            BaseActivity baseActivity= (BaseActivity) getActivity();
            baseActivity.setToolbarHeight(AbViewUtil.getViewHeight(toolbar),this.getClass().getName());
        }
        if (isLazyLoad()) {
            mIsPrepare = true;
            mIsImmersion = true;
            onLazyLoad();
        } else {
            initData(getArguments());
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            mIsVisible = true;
            onVisible();
        } else {
            mIsVisible = false;
            onInvisible();
        }
    }

    private void onLazyLoad() {
        if (mIsVisible && mIsPrepare) {
            mIsPrepare = false;
            LogUtils.i("执行了绑定数据的操作");
            initData(getArguments());
        }
    }

    /**
     * 用户可见时执行的操作
     */
    @CallSuper
    protected void onVisible() {
        onLazyLoad();
        if (mBaseAttribute != null) {
            initImmersionBar();
        }
        LogUtils.i("用户可见" + this.getClass().getName());
    }

    /**
     * 用户不可见执行
     */
    @CallSuper
    protected void onInvisible() {
        LogUtils.i("用户不可见" + this.getClass().getName());
    }

    /**
     * 是否懒加载
     *
     * @return the boolean
     */
    protected boolean isLazyLoad() {
        return false;
    }

    /**
     * 子类要用生命周期需要重写这个方法
     */
    protected void initLifeCycle() {
    }

    private void initToolBar(View viewContent) {
        if (mBaseAttribute.mHasTitle) {
            boolean isDefineToolbar = false;
            if (mBaseAttribute.toolBarId != 0) {
                toolbar = (Toolbar) viewContent.findViewById(mBaseAttribute.toolBarId);
                isDefineToolbar = true;
            } else if (viewContent.findViewById(R.id.base_m_toolbar) != null) {
                toolbar = (Toolbar) viewContent.findViewById(R.id.base_m_toolbar);
                isDefineToolbar = false;
            } else {
                if (mBaseAttribute.mHasTitle) {
                    LogUtils.w("没指定ToolBar");
                }
                return;
            }
            if (mBaseAttribute.statusBarId != 0) {
                statusBar = viewContent.findViewById(mBaseAttribute.statusBarId);
            } else if (viewContent.findViewById(R.id.base_status_bar) != null) {
                statusBar = viewContent.findViewById(R.id.base_status_bar);
            } else {
                statusBar = null;
            }
            if (!isDefineToolbar) {
                RelativeLayout relativeLayoutLeft = (RelativeLayout) viewContent.findViewById(R.id.base_relative_left);
                TextView baseTextLeft = (TextView) viewContent.findViewById(R.id.base_text_left);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) relativeLayoutLeft.getLayoutParams();
                layoutParams.width = SizeUtils.dp2px(mBaseAttribute.mTitleLeftRightWidth);
                if (mBaseAttribute.mAddBackButton) {
                    relativeLayoutLeft.setOnClickListener(view -> onLeftClick(relativeLayoutLeft));
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
                if (viewContent.findViewById(R.id.base_text_middle) != null) {
                    marqueeTextView = (MarqueeTextView) viewContent.findViewById(R.id.base_text_middle);
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
                            FrameLayout frameLayout = (FrameLayout) viewContent.findViewById(R.id.base_title_frame);
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
                RelativeLayout relativeLayoutRight = (RelativeLayout) viewContent.findViewById(R.id.base_relative_right);
                TextView baseTextRight = (TextView) viewContent.findViewById(R.id.base_text_right);
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) relativeLayoutRight.getLayoutParams();
                layoutParams.width = SizeUtils.dp2px(mBaseAttribute.mTitleLeftRightWidth);
                if (mBaseAttribute.mHasRightView) {
                    relativeLayoutRight.setOnClickListener(view -> onRightClick(relativeLayoutRight));
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
            if (viewContent.findViewById(R.id.base_bar) != null) {
                viewContent.findViewById(R.id.base_status_bar).setVisibility(View.GONE);
                viewContent.findViewById(R.id.base_m_toolbar).setVisibility(View.GONE);
            }
            if (mBaseAttribute.statusBarId != 0) {
                statusBar = viewContent.findViewById(mBaseAttribute.statusBarId);
            } else if (viewContent.findViewById(R.id.base_status_bar) != null) {
                statusBar = viewContent.findViewById(R.id.base_status_bar);
            } else {
                statusBar = null;
            }
        }

    }

    protected void onLeftClick(RelativeLayout relativeLayout) {
        getActivity().onBackPressed();
    }


    /**
     * 为了设置TextView的drawable
     *
     * @param id
     * @return
     */
    public Drawable getTextDrawable(@DrawableRes int id) {
        Drawable drawable = ImageUtils.getDrawableFromId(getContext(), id, R.drawable.base_qh_sy_fh);
        setTextDrawableBound(drawable, 0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        return drawable;
    }

    public Drawable getTextDrawable(@DrawableRes int id, int width, int height) {
        Drawable drawable = ImageUtils.getDrawableFromId(getContext(), id, R.drawable.base_qh_sy_fh);
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

    private void initImmersionBar() {
        if (mBaseAttribute != null) {
            if(mBaseAttribute.mStatusBarFragmentEnabled){
                if (!mBaseAttribute.mStatusTranslucent) {
                    StatusBarCompat.setStatusBarColor(getActivity(), getResources().getColor(mBaseAttribute.mStatusBackgroundColorId), mBaseAttribute.mStatusBackgroundAlpha);
                } else {
                    StatusBarCompat.translucentStatusBar(getActivity());
                }
            }
        }
    }


    /**
     * 右边点击事件
     *
     * @param view
     */
    protected void onRightClick(View view) {

    }


    public abstract void onInitAttribute(BaseAttribute ba);

    public abstract void initView(View view, Bundle bundle);

    protected abstract void initData(Bundle bundle);

    @Override
    public void onDestroy() {
        for (LifeCycleListener lifeCycleListener : lifeCycleListeners) {
            lifeCycleListener.onDestroy();
        }
        lifeCycleListeners.clear();
        super.onDestroy();
    }


    /**
     * 动态删除碎片
     *
     * @param fragment 碎片
     */
    public void deleteLocalFragment(Fragment fragment, boolean ifViewDelete, int layoutId) {
        if (ifViewDelete) {
            if (getActivity() != null && getActivity().findViewById(layoutId) != null) {
                getActivity().findViewById(layoutId).setVisibility(View.GONE);
            }
        }
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(fragment);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 动态添加碎片
     *
     * @param fragment 碎片
     * @param layoutId 碎片容器
     * @param bundle   向碎片传值
     */
    protected void addLocalFragment(Fragment fragment, int layoutId, Bundle bundle) {
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        fragment.setArguments(bundle);
        transaction.replace(layoutId, fragment);
        transaction.commitAllowingStateLoss();
    }

    protected void addFragments(Fragment f, Bundle bundle, int layoutId) {
        f.setArguments(bundle);
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(layoutId, f);
        transaction.commitAllowingStateLoss();
    }
}
