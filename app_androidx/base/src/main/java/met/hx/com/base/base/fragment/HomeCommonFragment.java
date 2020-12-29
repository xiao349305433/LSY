package met.hx.com.base.base.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.CallSuper;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.flyco.roundview.RoundLinearLayout;
import com.flyco.roundview.RoundRelativeLayout;
import com.flyco.roundview.RoundViewDelegate;

import java.util.Random;

import met.hx.com.base.R;
import met.hx.com.base.base.BaseAttribute;

/**
 * Created by huxu on 2017/11/27.
 */

public abstract class HomeCommonFragment extends BaseNoPresenterFragment implements View.OnClickListener{
    protected RoundLinearLayout mLinearAll;
    protected int[] strokeColor;//随机的颜色数组
    protected int strokeWidth;
    protected int strokeColorAll;
    protected int backColorAll;
    protected int colorTop;
    protected RoundViewDelegate delegateTop;
    protected RoundViewDelegate delegateAll;

    @CallSuper
    @Override
    public void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId= R.layout.base_fragment_home_common;
    }

    @Override
    public void initView(View view, Bundle bundle) {
        if (getStrokeWidth() == -1) {
            strokeWidth = 1;
        } else {
            strokeWidth = getStrokeWidth();
        }
        if (getStrokeColor() == null) {
            strokeColor = new int[5];
            strokeColor[0] = R.color.base_QA;
            strokeColor[1] = R.color.base_QW;
            strokeColor[2] = R.color.base_ED;
            strokeColor[3] = R.color.base_RF;
            strokeColor[4] = R.color.base_TG;
        } else {
            strokeColor = getStrokeColor();
        }
        RoundRelativeLayout mRelativeTop = (RoundRelativeLayout) view.findViewById(R.id.relative_top);
        ImageView mImageIcon = (ImageView) view.findViewById(R.id.image_icon);
        TextView mTextTitle = (TextView) view.findViewById(R.id.text_title);
        TextView mTextMore = (TextView) view.findViewById(R.id.text_more);
        LinearLayout mLinearBottom = (LinearLayout) view.findViewById(R.id.linear_bottom);
        mLinearAll = (RoundLinearLayout) view.findViewById(R.id.linear_all);
        if (hasPadding()) {
            setPadding(strokeWidth, strokeWidth, strokeWidth, strokeWidth);
        } else {
            setPadding(0, 0, 0, 0);
        }
        delegateTop = mRelativeTop.getDelegate();
        delegateAll = mLinearAll.getDelegate();
        View viewContent = addBottomView(getLayoutInflater(), mLinearBottom);
        if (!hasTitle()) {
            mRelativeTop.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(getTextTitle())) {
            mTextTitle.setText(getTextTitle());
        } else {
            mTextTitle.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(getTextMore())) {
            mTextMore.setText(getTextMore());
            if (getRightDrawableResources() != 0) {
                setRightDrawableResources(getRightDrawableResources(), mTextMore);
            }
        } else {
            mTextMore.setVisibility(View.GONE);
        }
        if (isTitleBackGroundRandom()) {
            setTopColorStyle(strokeColor[getRandom(0, strokeColor.length)]);
        } else {
            if (getTopDrawableColor() != 0) {
                setTopColorStyle(getTopDrawableColor());
            }
        }

        if (isStrokeRandom()) {
            if (getCardBackColor() == 0) {
                setBottomStyle(strokeColor[getRandom(0, strokeColor.length)], R.color.base_white);
            } else {
                setBottomStyle(strokeColor[getRandom(0, strokeColor.length)], getCardBackColor());
            }
        } else {
            if (getCardStrokeColor() != 0) {
                if (getCardBackColor() == 0) {
                    setBottomStyle(getCardStrokeColor(), R.color.base_white);
                } else {
                    setBottomStyle(getCardStrokeColor(), getCardBackColor());
                }
            }
        }

        if (getIcon() != null) {
            mImageIcon.setImageDrawable(getIcon());
        }
        onContent(view, viewContent, mLinearBottom, mRelativeTop, mImageIcon, mTextTitle, mTextMore);
    }

    //动态设置padding
    protected void setPadding(int strokeWidth, int strokeWidth1, int strokeWidth2, int strokeWidth3) {
        if (mLinearAll!=null){
            mLinearAll.setPadding(strokeWidth, strokeWidth1, strokeWidth2, strokeWidth3);
        }
    }

    protected int getRandom(int min, int max) {
        if (min > max) {
            return 0;
        }
        if (min == max) {
            return min;
        }
        return min + new Random().nextInt(max - min);
    }

    //设置是否显示卡片标题栏
    protected boolean hasTitle() {
        return true;
    }

    //上下左右各padding为stokeWidth，防止嵌套使用时候padding越积越多，嵌套使用false
    protected boolean hasPadding() {
        return true;
    }

    //添加子类的布局
    protected View addBottomView(LayoutInflater inflater, LinearLayout mLinearBottom) {
        if (getLayoutId() != 0) {
            View viewContent = inflater.inflate(getLayoutId(), null, false);
            mLinearBottom.addView(viewContent, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            return viewContent;
        } else {
            return mLinearBottom;
        }
    }
    protected void addLocalFragment(Fragment fragment, int layoutId, Bundle bundle) {
        FragmentManager manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        fragment.setArguments(bundle);
        transaction.replace(layoutId, fragment);
        transaction.commitAllowingStateLoss();
    }
    //是否随机边框颜色
    protected boolean isStrokeRandom() {
        return false;
    }

    //边框宽（单位：dp）
    public int getStrokeWidth() {
        return -1;
    }

    //是否随机title背景颜色
    protected boolean isTitleBackGroundRandom() {
        return false;
    }

    //随机的颜色数组,返回null为默认颜色
    protected int[] getStrokeColor() {
        return null;
    }

    //可用于刷新改变边框颜色时候用,随机时候color可为0，isColorRandom是否随机
    protected void refreshStrokeColor(int color, boolean isColorRandom) {
        if (isColorRandom) {
            setBottomStyle(strokeColor[getRandom(0, strokeColor.length)], 0);
        } else {
            setBottomStyle(color, 0);
        }

    }

    //可用于刷新改变title背景颜色时候用,随机时候color可为0，isColorRandom是否随机
    protected void refreshTopColor(int color, boolean isColorRandom) {
        if (isColorRandom) {
            setTopColorStyle(strokeColor[getRandom(0, strokeColor.length)]);
        } else {
            setTopColorStyle(color);
        }
    }

    //设置整个布局的样式
    private void setBottomStyle(int color, int backColor) {
        strokeColorAll = color;
        if (backColor != 0) {
            backColorAll = backColor;
            delegateAll.setBackgroundColor(getResources().getColor(backColorAll));
        }
        delegateAll.setStrokeColor(getResources().getColor(strokeColorAll));
        delegateAll.setStrokeWidth(strokeWidth);
    }

    //设置顶部title的样式
    private void setTopColorStyle(int color) {
        colorTop = color;
        delegateTop.setBackgroundColor(getResources().getColor(colorTop));
    }

    //设置布局id
    protected abstract int getLayoutId();


    //为空则不显示，设置标题
    protected abstract String getTextTitle();

    //为空则不显示，设置后面的更多
    protected abstract String getTextMore();

    //设置更多右箭头
    protected int getRightDrawableResources() {
        return 0;
    }

    //是否显示边框
    protected void showStroke(boolean isShow) {
        if (isShow) {
            delegateAll.setStrokeColor(getResources().getColor(strokeColorAll));
            delegateAll.setStrokeWidth(strokeWidth);
        } else {
            delegateAll.setStrokeColor(getResources().getColor(strokeColorAll));
            delegateAll.setStrokeWidth(0);
        }
    }


    //动态设置更多右箭头
    protected void setRightDrawableResources(int drawableResources, TextView mTextMore) {
        Drawable drawable = getResources().getDrawable(drawableResources);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
        mTextMore.setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.base_common_distanceSize_10px));
        mTextMore.setCompoundDrawables(null, null, drawable, null);//画在右边
    }

    //设置上半部分卡片背景
    protected abstract int getTopDrawableColor();

    protected void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    //设置下半部分卡片边框颜色
    protected abstract int getCardStrokeColor();


    //卡片颜色背景
    protected abstract int getCardBackColor();

    //设置icon
    protected abstract Drawable getIcon();
    protected void onContent(View view, View viewContent, LinearLayout mLinearBottom, RelativeLayout mRelativeTop, ImageView mImageIcon, TextView mTextTitle, TextView mTextMore) {
    }
    @Override
    public void onClick(View v) {

    }


}
