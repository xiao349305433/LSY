package met.hx.com.base.base.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import met.hx.com.base.R;
import met.hx.com.librarybase.some_utils.XHStringUtil;


/**
 * Created by huxu on 2017/8/4.
 */

public abstract class OaCommonFragment extends HomeCommonFragment {

    protected RelativeLayout mOa_relative_top_title;
    protected ImageView mOa_img_tag;
    protected TextView mOa_center_title;
    protected TextView mOa_top_title;
    protected TextView mOa_text_little_more;
    protected TextView mOa_bottom_title;
    private CardView mOa_card_view;
    private TextView mOa_text_left;


    @Override
    public void initView(View view, Bundle bundle) {
        super.initView(view, bundle);
        mOa_relative_top_title = (RelativeLayout) view.findViewById(R.id.oa_relative_top_title);
        mOa_img_tag = (ImageView) view.findViewById(R.id.oa_img_tag);
        mOa_center_title = (TextView) view.findViewById(R.id.oa_center_title);
        mOa_top_title = (TextView) view.findViewById(R.id.oa_top_title);
        mOa_text_little_more = (TextView) view.findViewById(R.id.oa_text_little_more);
        mOa_bottom_title = (TextView) view.findViewById(R.id.oa_bottom_title);
        mOa_text_left = (TextView) view.findViewById(R.id.text_icon);
        mOa_card_view = (CardView) view.findViewById(R.id.card_view);
        if (!XHStringUtil.isEmpty(getOaTextCenter(), false)) {
            mOa_center_title.setText(getOaTextCenter());
        } else {
            mOa_center_title.setVisibility(View.GONE);
        }
        if (!XHStringUtil.isEmpty(getOaTextTop(), false)) {
            mOa_top_title.setText(getOaTextTop());
        } else {
            mOa_top_title.setVisibility(View.GONE);
        }
        if (!XHStringUtil.isEmpty(getOaTextBottom(), false)) {
            mOa_bottom_title.setText(getOaTextBottom());
        } else {
            mOa_bottom_title.setVisibility(View.GONE);
        }
        if (!XHStringUtil.isEmpty(getOaTextMore(), false)) {
            mOa_text_little_more.setText(getOaTextMore());
            if (getOaRightDrawableResources() != 0) {
                setRightDrawableResources(getOaRightDrawableResources(), mOa_text_little_more);
            }
        } else {
            mOa_text_little_more.setVisibility(View.GONE);
        }
        if(getLeftCardColorId() != 0 && (!XHStringUtil.isEmpty(getLeftText(),false))){
            mOa_card_view.setVisibility(View.VISIBLE);
            mOa_text_left.setVisibility(View.VISIBLE);
            mOa_card_view.setCardBackgroundColor(getResources().getColor(getLeftCardColorId()));
            mOa_text_left.setText(getLeftText());
            mOa_img_tag.setVisibility(View.GONE);
        }else {
            if (getOaImage() != null) {
                mOa_img_tag.setVisibility(View.VISIBLE);
                mOa_img_tag.setImageDrawable(getOaImage());
                mOa_card_view.setVisibility(View.GONE);
                mOa_text_left.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected boolean hasTitle() {
        return false;
    }

    @Override
    protected void onContent(View view, View viewContent, LinearLayout mLinearBottom, RelativeLayout mRelativeTop, ImageView mImageIcon, TextView mTextTitle, TextView mTextMore) {

    }


    @Override
    protected View addBottomView(LayoutInflater inflater, LinearLayout mLinearBottom) {
        View oaTitleView = inflater.inflate(R.layout.base_item_oa_title, null, false);
        mLinearBottom.addView(oaTitleView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if (getLayoutId() != 0) {
            View viewContent = inflater.inflate(getLayoutId(), null, false);
            mLinearBottom.addView(viewContent, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        return mLinearBottom;
    }

    @Override
    protected Drawable getIcon() {
        return null;
    }

    @Override
    protected int getCardBackColor() {
        return 0;
    }

    @Override
    protected int getTopDrawableColor() {
        return 0;
    }

    @Override
    protected String getTextMore() {
        return null;
    }

    @Override
    protected String getTextTitle() {
        return null;
    }

    //中间的文字
    protected abstract String getOaTextCenter();

    //上面的文字
    protected abstract String getOaTextTop();

    //更多文字
    protected abstract String getOaTextMore();

    //下面的文字
    protected abstract String getOaTextBottom();

    //设置左边的图标
    protected abstract Drawable getOaImage();

    //左侧图标的颜色
    protected  int getLeftCardColorId(){
        return 0;
    }

    //左侧图标的文字
    protected  String getLeftText(){
        return null;
    }

    //设置更多右箭头
    protected int getOaRightDrawableResources() {
        return 0;
    }

}
