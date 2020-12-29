package met.hx.com.base.base;


import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StringRes;

import met.hx.com.base.R;

public class BaseAttribute {
    /**
     * layout文件
     */
    @LayoutRes
    public int mActivityLayoutId;
    /**
     * 有无title
     */
    public boolean mHasTitle = true;
    /**
     * 是否隐藏toolbar的默认标题
     */
    public boolean displayShowTitleEnabled = false;
    /**
     * toolbar的背景资源（可以传颜色id，drawable的id等，由于用的太多，名字没法改了）
     */
    @ColorRes
    public int mTitleBackgroundColorId = 0;
    /**
     * 状态栏的背景色
     */
    @ColorRes
    public int mStatusBackgroundColorId = R.color.base_H;
    /**
     * 状态栏透明度
     */
    public int mStatusBackgroundAlpha = 0;
    /**
     * 是否内容入侵状态栏，慎用（因为入侵之后软键盘弾不起来，需要自己处理软键盘）
     */
    public boolean mStatusTranslucent = false;
    /**
     * 是否开启activity沉浸式,默认不开启
     */
    public boolean mStatusBarActivityEnabled = false;
    /**
     * 是否开启fragment沉浸式,默认不开启
     */
    public boolean mStatusBarFragmentEnabled = false;

    /**
     * 如果要自定义toolbar，传入id
     */
    @IdRes
    public int toolBarId;
    /**
     * 如果要自定义状态栏，传入id
     */
    @IdRes
    public int statusBarId;
    /**
     * 标题栏布局id
     */
    @LayoutRes
    public int titleId;
    /**
     * 标题栏文字
     */
    public String mTitleText;
    /**
     * 标题栏文字
     */
    @StringRes
    public int mTitleTextStringId;
    /**
     * 是否显示返回键
     */
    public boolean mAddBackButton = true;
    /**
     * 是否显示右边布局
     */
    public boolean mHasRightView = true;
    /**
     * 是否显示左边的图标
     */
    public boolean mHasLeftDrawable = true;
    /**
     * 是否显示标题文字
     */
    public boolean mShowTitleText = true;
    /**
     * 返回按钮的drawable
     */
    @DrawableRes
    public int mBackButtonResource = 0;

    /**
     * title的颜色
     */
    @ColorRes
    public int mTitleTextColorId = 0;
    /**
     * 右边字体颜色
     */
    @ColorRes
    public int mTitleRightTextButtonColorId = 0;
    @ColorRes
    public int mTitleLeftTextButtonColorId = 0;
    /**
     * 右边文字
     */
    public String mTitleRightText;
    public String mTitleLeftText;
    /**
     * 右边文字
     */
    @StringRes
    public int mTitleRightTextId;
    @StringRes
    public int mTitleLeftTextId;
    /**
     * 右边字体大小
     */
    @DimenRes
    public int mTitleRightTextSizeId = 0;
    @DimenRes
    public int mTitleLeftTextSizeId = 0;
    /**
     * 右边布局
     */
    @LayoutRes
    public int mTitleRightLayoutId = 0;
    /**
     * 左边布局
     */
    @LayoutRes
    public int mTitleLeftLayoutId = 0;
    /**
     * 右边图片
     */
    @DrawableRes
    public int mTitleRightImageIcon = 0;
    /**
     * 右边图片的宽(dp),用于图片过大微调，0默认是图片的宽度
     */
    public int mTitleRightImageIconWidth = 0;
    /**
     * 左边图片的宽(dp),用于图片过大微调，0默认是图片的宽度
     */
    public int mTitleLeftImageIconWidth = 0;
    /**
     * 右边图片的高(dp),用于图片过大微调，0默认是图片的高度
     */
    public int mTitleRightImageIconHeight = 0;
    /**
     * 左边图片的高(dp),用于图片过大微调，0默认是图片的高度
     */
    public int mTitleLeftImageIconHeight = 0;

    /**
     * 标题栏左右布局的宽度(dp)，根据实际情况微调,属于左右2边的布局占用的宽度
     * 比如 右边4个文字占不下，可以把这个值调大
     */
    public int mTitleLeftRightWidth = 50;

}