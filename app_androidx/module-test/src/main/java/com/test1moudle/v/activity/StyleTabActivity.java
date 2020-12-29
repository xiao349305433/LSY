package com.test1moudle.v.activity;

import android.graphics.Typeface;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.test1moudle.R;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.baseconfig.C;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.views.BottomNavigationViewEx;

/**
 * Created by huxu on 2017/11/11.
 */
@Route(path = C.STYLE)
public class StyleTabActivity extends BaseNoPresenterActivity {
    private BottomNavigationViewEx bnveNormal;
    private BottomNavigationViewEx bnveNoAnimation;
    private BottomNavigationViewEx bnveNoShiftingMode;
    private BottomNavigationViewEx bnveNoItemShiftingMode;
    private BottomNavigationViewEx bnveNoText;
    private BottomNavigationViewEx bnveNoIcon;
    private BottomNavigationViewEx bnveNoAnimationShiftingMode;
    private BottomNavigationViewEx bnveNoAnimationItemShiftingMode;
    private BottomNavigationViewEx bnveNoAnimationShiftingModeItemShiftingMode;
    private BottomNavigationViewEx bnveNoShiftingModeItemShiftingModeText;
    private BottomNavigationViewEx bnveNoAnimationShiftingModeItemShiftingModeText;
    private BottomNavigationViewEx bnveNoShiftingModeItemShiftingModeAndIcon;
    private BottomNavigationViewEx bnveNoItemShiftingModeIcon;
    private BottomNavigationViewEx bnveNoAnimationShiftingModeItemShiftingModeIcon;
    private BottomNavigationViewEx bnveWithPadding;
    private BottomNavigationViewEx bnveCenterIconOnly;
    private BottomNavigationViewEx bnveSmallerText;
    private BottomNavigationViewEx bnve_biggerIcon;
    private BottomNavigationViewEx bnveCustomTypeface;
    private BottomNavigationViewEx bnveIconSelector;
    private BottomNavigationViewEx bnveIconMarginTop;
    private BottomNavigationViewEx bnveUncheckedFirstTime;

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void initTransitionView() {

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId= R.layout.m_test_activity_style;
        ba.mTitleText="所有的样式";

    }

    @Override
    protected void initView() {

        bnveNormal =  (BottomNavigationViewEx)findViewById(R.id.bnve_normal);
        bnveNoAnimation =  (BottomNavigationViewEx)findViewById(R.id.bnve_no_animation);
        bnveNoShiftingMode =  (BottomNavigationViewEx)findViewById(R.id.bnve_no_shifting_mode);
        bnveNoItemShiftingMode =  (BottomNavigationViewEx)findViewById(R.id.bnve_no_item_shifting_mode);
        bnveNoText =  (BottomNavigationViewEx)findViewById(R.id.bnve_no_text);
        bnveNoIcon =  (BottomNavigationViewEx)findViewById(R.id.bnve_no_icon);
        bnveNoAnimationShiftingMode =  (BottomNavigationViewEx)findViewById(R.id.bnve_no_animation_shifting_mode);
        bnveNoAnimationItemShiftingMode =  (BottomNavigationViewEx)findViewById(R.id.bnve_no_animation_item_shifting_mode);
        bnveNoAnimationShiftingModeItemShiftingMode =  (BottomNavigationViewEx)findViewById(R.id.bnve_no_animation_shifting_mode_item_shifting_mode);
        bnveNoShiftingModeItemShiftingModeText =  (BottomNavigationViewEx)findViewById(R.id.bnve_no_shifting_mode_item_shifting_mode_text);
        bnveNoAnimationShiftingModeItemShiftingModeText =  (BottomNavigationViewEx)findViewById(R.id.bnve_no_animation_shifting_mode_item_shifting_mode_text);
        bnveNoShiftingModeItemShiftingModeAndIcon =  (BottomNavigationViewEx)findViewById(R.id.bnve_no_shifting_mode_item_shifting_mode_and_icon);
        bnveNoItemShiftingModeIcon =  (BottomNavigationViewEx)findViewById(R.id.bnve_no_item_shifting_mode_icon);
        bnveNoAnimationShiftingModeItemShiftingModeIcon =  (BottomNavigationViewEx)findViewById(R.id.bnve_no_animation_shifting_mode_item_shifting_mode_icon);
        bnveWithPadding =  (BottomNavigationViewEx)findViewById(R.id.bnve_with_padding);
        bnveCenterIconOnly =  (BottomNavigationViewEx)findViewById(R.id.bnve_center_icon_only);
        bnveSmallerText =  (BottomNavigationViewEx)findViewById(R.id.bnve_smaller_text);
        bnve_biggerIcon =  (BottomNavigationViewEx)findViewById(R.id.bnve_bigger_icon);
        bnveCustomTypeface =  (BottomNavigationViewEx)findViewById(R.id.bnve_custom_typeface);
        bnveIconSelector =  (BottomNavigationViewEx)findViewById(R.id.bnve_icon_selector);
        bnveIconMarginTop =  (BottomNavigationViewEx)findViewById(R.id.bnve_icon_margin_top);
        bnveUncheckedFirstTime =  (BottomNavigationViewEx)findViewById(R.id.bnve_unchecked_first_time);
        
        
        bnveNoAnimation.enableAnimation(false);

        bnveNoShiftingMode.enableShiftingMode(false);

        bnveNoItemShiftingMode.enableItemShiftingMode(false);

        bnveNoText.setTextVisibility(false);

        bnveNoIcon.setIconVisibility(false);

        bnveNoAnimationShiftingMode.enableAnimation(false);
        bnveNoAnimationShiftingMode.enableShiftingMode(false);

        bnveNoAnimationItemShiftingMode.enableAnimation(false);
        bnveNoAnimationItemShiftingMode.enableItemShiftingMode(false);

        disableAllAnimation(bnveNoAnimationShiftingModeItemShiftingMode);

        bnveNoShiftingModeItemShiftingModeText.enableShiftingMode(false);
        bnveNoShiftingModeItemShiftingModeText.enableItemShiftingMode(false);
        bnveNoShiftingModeItemShiftingModeText.setTextVisibility(false);


        disableAllAnimation(bnveNoAnimationShiftingModeItemShiftingModeText);
        bnveNoAnimationShiftingModeItemShiftingModeText.setTextVisibility(false);

        bnveNoShiftingModeItemShiftingModeAndIcon.enableShiftingMode(false);
        bnveNoShiftingModeItemShiftingModeAndIcon.enableItemShiftingMode(false);
        bnveNoShiftingModeItemShiftingModeAndIcon.setIconVisibility(false);

        bnveNoItemShiftingModeIcon.enableItemShiftingMode(false);
        bnveNoItemShiftingModeIcon.setIconVisibility(false);

        disableAllAnimation(bnveNoAnimationShiftingModeItemShiftingModeIcon);
        bnveNoAnimationShiftingModeItemShiftingModeIcon.setIconVisibility(false);

        disableAllAnimation(bnveWithPadding);
        bnveWithPadding.setIconVisibility(false);

        initCenterIconOnly();

        initSmallerText();

        initBiggerIcon();

        initCustomTypeface();

        bnveIconSelector.enableAnimation(false);

        initMargin();

        initUncheckedFirstTime();
    }
    private void disableAllAnimation(BottomNavigationViewEx bnve) {
        bnve.enableAnimation(false);
        bnve.enableShiftingMode(false);
        bnve.enableItemShiftingMode(false);
    }

    private void initCenterIconOnly() {
        disableAllAnimation(bnveCenterIconOnly);
        int centerPosition = 2;
        // attention: you must ensure the center menu item title is empty
        // make icon bigger at centerPosition
        bnveCenterIconOnly.setIconSizeAt(centerPosition, 48, 48);
        bnveCenterIconOnly.setItemBackground(centerPosition, R.color.m_test_colorGreen);
        bnveCenterIconOnly.setIconTintList(centerPosition,
                getResources().getColorStateList(R.color.m_test_selector_item_gray_color));
        bnveCenterIconOnly.setIconMarginTop(centerPosition, BottomNavigationViewEx.dp2px(this, 4));
        // you could set a listener for bnve. and return false when click the center item so that it won't be checked.
        bnveCenterIconOnly.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_add) {
                    Toast.makeText(StyleTabActivity.this, "add", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return true;
            }
        });
    }

    private void initSmallerText() {
        disableAllAnimation(bnveSmallerText);
        // set text size
        bnveSmallerText.setTextSize(8);
    }

    private void initBiggerIcon() {
        disableAllAnimation(bnve_biggerIcon);
        // hide text
        bnve_biggerIcon.setTextVisibility(false);
        // set icon size
        int iconSize = 36;
        bnve_biggerIcon.setIconSize(iconSize, iconSize);
        // set item height
        bnve_biggerIcon.setItemHeight(BottomNavigationViewEx.dp2px(this, iconSize + 16));
    }

    private void initCustomTypeface() {
        disableAllAnimation(bnveCustomTypeface);
        // set typeface : bold
        bnveCustomTypeface.setTypeface(Typeface.DEFAULT_BOLD);
        // you also could set typeface from file.
//        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/custom.ttf");
//        bnveCustomTypeface.setTypeface(typeface);
    }

    private void initMargin() {
        disableAllAnimation(bnveIconMarginTop);
        bnveIconMarginTop.setTextVisibility(false);
        bnveIconMarginTop.setItemHeight(BottomNavigationViewEx.dp2px(this, 56));
        bnveIconMarginTop.setIconsMarginTop(BottomNavigationViewEx.dp2px(this, 16));
//        bnveIconMarginTop.setIconTintList(0, getResources()
//                .getColorStateList(R.color.colorGray));

    }

    /**
     * There is no idea to set no check item first time.
     * But we can let user think it is unchecked first time by control the color
     */
    private void initUncheckedFirstTime() {
        disableAllAnimation(bnveUncheckedFirstTime);
        // use the unchecked color for first item
        bnveUncheckedFirstTime.setIconTintList(0, getResources()
                .getColorStateList(R.color.m_test_colorGray));
        bnveUncheckedFirstTime.setTextTintList(0, getResources()
                .getColorStateList(R.color.m_test_colorGray));
        bnveUncheckedFirstTime.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            private boolean firstClick = true;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // restore the color when click
                if (firstClick) {
                    int position = bnveUncheckedFirstTime.getMenuItemPosition(item);
                    if (0 == position) {
                        firstClick = false;
                        bnveUncheckedFirstTime.setIconTintList(0, getResources()
                                .getColorStateList(R.color.m_test_selector_item_primary_color));
                        bnveUncheckedFirstTime.setTextTintList(0, getResources()
                                .getColorStateList(R.color.m_test_selector_item_primary_color));
                    }
                }
                // do other
                return true;
            }
        });
    }
    @Override
    protected void onEventRunEnd(Event event, int code) {

    }
}
