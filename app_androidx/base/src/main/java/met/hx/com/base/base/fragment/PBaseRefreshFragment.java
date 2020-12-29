package met.hx.com.base.base.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.CallSuper;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnMultiPurposeListener;

import met.hx.com.base.R;
import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.basemvp.v.views.RefreshView;

/**
 * Created by huxu on 2017/11/18.
 */

public abstract class PBaseRefreshFragment extends BaseYesPresenterFragment implements OnMultiPurposeListener {
    protected SmartRefreshLayout mSmartRefresh;
    protected LinearLayout mLinearLayout;
    protected TextView mTextEmptyData;
    protected RefreshView refreshView;
    protected View viewContent;

    @Override
    public void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.base_activity_refresh;
    }

    @CallSuper
    @Override
    public void initView(View view, Bundle bundle) {
        setRefreshLayout(view);
        addContentView();
        setAllViews();
    }

    private void setRefreshLayout(View view) {
        if (getRefreshViewId() != 0) {
            refreshView = (RefreshView) view.findViewById(getRefreshViewId());
        } else {
            refreshView = (RefreshView) view.findViewById(R.id.base_refresh);
        }
    }

    protected void addContentView() {
        refreshView.addRefreshContentView(initRefreshLayoutId());
    }

    private void setAllViews() {
        mSmartRefresh = refreshView.getmSmartRefresh();
        mLinearLayout = refreshView.getmBaseLinearContent();
        mTextEmptyData = refreshView.getmBaseTextEmptyData();
        viewContent = refreshView.getViewChild();
        mSmartRefresh.setOnMultiPurposeListener(this);
    }

    /**
     * 需要刷新的内容，如果刷新内容包含了就把这个设置为0
     *
     * @return
     */
    protected abstract int initRefreshLayoutId();

    /**
     * 自定义刷新内容，需要RefreshView的id
     *
     * @return
     */
    protected int getRefreshViewId() {
        return 0;
    }


    /**
     * 没有数据时显示
     */
    protected void showEmptyData() {
        refreshView.showEmptyData(null);
    }

    public void showEmptyData(String emptyText) {
        refreshView.showEmptyData(emptyText);
    }

    public void showEmptyData(String emptyText, int gravity, int marginTop) {
        refreshView.showEmptyData(emptyText, gravity, marginTop);
    }

    @Override
    public void onHeaderPulling(RefreshHeader header, float percent, int offset, int headerHeight, int extendHeight) {

    }

    @Override
    public void onHeaderReleasing(RefreshHeader header, float percent, int offset, int headerHeight, int extendHeight) {

    }

    @Override
    public void onHeaderStartAnimator(RefreshHeader header, int headerHeight, int extendHeight) {

    }

    @Override
    public void onHeaderFinish(RefreshHeader header, boolean success) {

    }

    @Override
    public void onFooterPulling(RefreshFooter footer, float percent, int offset, int footerHeight, int extendHeight) {

    }

    @Override
    public void onFooterReleasing(RefreshFooter footer, float percent, int offset, int footerHeight, int extendHeight) {

    }

    @Override
    public void onFooterStartAnimator(RefreshFooter footer, int footerHeight, int extendHeight) {

    }

    @Override
    public void onFooterFinish(RefreshFooter footer, boolean success) {

    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {

    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {

    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {

    }
}
