package com.modulerefresh.v.activity;

import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.test1moudle.R;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.baseconfig.C;
import met.hx.com.base.baseevent.Event;
import met.hx.com.base.basemvp.v.views.RefreshView;
import met.hx.com.librarybase.some_utils.util.AbViewUtil;

/**
 * Created by huxu on 2017/11/9.
 * https://github.com/youth5201314/banner
 *
 * @author huxu
 */
@Route(path = C.REFRESH)
public class MainActivity extends BaseNoPresenterActivity {
    private int mOffset = 0;
    private int mScrollY = 0;
    private int height;
    private int marginTop;
    private FrameLayout.LayoutParams layoutParams ;

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void initTransitionView() {

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_test_refresh_weibo;
        ba.mTitleText = "广告";
        ba.toolBarId = R.id.toolbar;
    }

    @Override
    protected void initView() {

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final View parallax = findViewById(R.id.parallax);
        final View buttonBar = findViewById(R.id.buttonBarLayout);
        final NestedScrollView scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        final RefreshView refreshView = (RefreshView) findViewById(R.id.refresh);
        refreshView.hideEmptyData();
        height = AbViewUtil.getViewHeight(parallax);
        layoutParams= (FrameLayout.LayoutParams) parallax.getLayoutParams();
        marginTop=layoutParams.topMargin;
        findViewById(R.id.attention).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "点击了关注", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.leaveword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "点击了留言", Toast.LENGTH_SHORT).show();
            }
        });

        refreshView.getmSmartRefresh().setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onHeaderPulling(RefreshHeader header, float percent, int offset, int bottomHeight, int extendHeight) {
                mOffset = offset / 2;

                layoutParams.height= (int) (height+height/2*percent);
                layoutParams.topMargin= (int) (marginTop+marginTop/2*percent);

                toolbar.setAlpha(1 - Math.min(percent, 1));
            }

            @Override
            public void onHeaderReleasing(RefreshHeader header, float percent, int offset, int bottomHeight, int extendHeight) {
                mOffset = offset / 2;

                layoutParams.height= (int) (height+height/2*percent);
                layoutParams.topMargin= (int) (marginTop+marginTop/2*percent);

                toolbar.setAlpha(1 - Math.min(percent, 1));
            }
        });

        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            private int lastScrollY = 0;
            private int h = DensityUtil.dp2px(170);
            private int color = ContextCompat.getColor(getApplicationContext(), R.color.base_Q1) & 0x00ffffff;

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (lastScrollY < h) {
                    scrollY = Math.min(h, scrollY);
                    mScrollY = scrollY > h ? h : scrollY;
                    buttonBar.setAlpha(1f * mScrollY / h);
                    toolbar.setBackgroundColor(((255 * mScrollY / h) << 24) | color);
                    parallax.setTranslationY(mOffset - mScrollY);
                }
                lastScrollY = scrollY;
            }
        });
        buttonBar.setAlpha(0);
        toolbar.setBackgroundColor(0);
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }
}
