package met.hx.com.base.basemvp.v.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import met.hx.com.base.R;
import met.hx.com.librarybase.some_utils.SizeUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
/**
 * Created by huxu on 2017/11/21.
 * 拥有“暂无数据”的刷新组件，可到处用，统一显示暂无数据
 */

public class RefreshView extends LinearLayout {
    private TextView mBaseRefreshTextHasnoData;
    private LayoutInflater layoutInflater;
    private SmartRefreshLayout mBaseSmartRefresh;
    private LinearLayout mBaseLinearContent;
    private LinearLayout mBaseLinearContentAll;
    private TextView mBaseTextEmptyData;
    private View viewContent;
    private View viewChild;

    public RefreshView(Context context) {
        this(context, null);
    }

    public RefreshView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(LinearLayout.VERTICAL);
        this.layoutInflater = LayoutInflater.from(context);
        viewContent = layoutInflater.inflate(R.layout.base_refresh_view, null);
        mBaseSmartRefresh = (SmartRefreshLayout) viewContent.findViewById(R.id.base_refresh_smartRefresh);
        mBaseLinearContent = (LinearLayout) viewContent.findViewById(R.id.base_refresh_linear_content);
        mBaseLinearContentAll = (LinearLayout) viewContent.findViewById(R.id.base_refresh_linear_content_all);
        mBaseTextEmptyData = (TextView) viewContent.findViewById(R.id.base_refresh_text_empty_data);
        mBaseRefreshTextHasnoData = (TextView) viewContent.findViewById(R.id.base_refresh_text_hasno_data);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            viewChild = getChildAt(0);
            this.removeAllViews();
            mBaseLinearContent.addView(viewChild);
        }
        this.addView(viewContent, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /**
     * 添加自定义布局
     *
     * @param layoutId
     */
    public void addRefreshContentView(@LayoutRes int layoutId) {
        if (layoutId != 0) {
            this.removeAllViews();
            viewChild = layoutInflater.inflate(layoutId, null);
            mBaseLinearContent.addView(viewChild);
            this.addView(viewContent, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }

    /**
     * 显示上拉加载没有更多数据
     */
    public void showLoadMoreFinished(){
        mBaseSmartRefresh.setLoadmoreFinished(true);
        mBaseSmartRefresh.finishLoadmore();
    }

    /**
     * 重置上拉加载
     */
    public void resetLoadMoreFinished(){
        mBaseSmartRefresh.setLoadmoreFinished(false);
    }
    /**
     * 显示空数据布局
     */
    public void showEmptyData() {
        showEmptyData(null, 0, SizeUtils.dp2px(10));
    }

    /**
     * 显示没有更多数据布局
     */
    public void showLoadMoreData() {
        if(mBaseTextEmptyData.getVisibility()==GONE){
            mBaseTextEmptyData.setVisibility(VISIBLE);
        }
    }

    public void hideLoadMoreData() {
        if(mBaseTextEmptyData.getVisibility()==VISIBLE){
            mBaseTextEmptyData.setVisibility(GONE);
        }
    }

    public void showEmptyData(String emptyText) {
        showEmptyData(emptyText, 0, SizeUtils.dp2px(10));
    }

    public void showEmptyData(String emptyText, int gravity, int marginTop) {
        showEmptyData(emptyText, gravity, 0, marginTop, 0, 0);
    }

    public void showEmptyData(String emptyText, int gravity, int left, int top, int right, int bottom) {
        if (!XHStringUtil.isEmpty(emptyText, false)) {
            mBaseRefreshTextHasnoData.setText(emptyText);
        } else {
            mBaseRefreshTextHasnoData.setText("暂无数据");
        }
        if (gravity != 0) {
            mBaseRefreshTextHasnoData.setGravity(gravity);
        }
        mBaseRefreshTextHasnoData.setPadding(left, top, right, bottom);
        mBaseRefreshTextHasnoData.setVisibility(View.VISIBLE);
        mBaseLinearContentAll.setVisibility(GONE);
    }

    /**
     * 隐藏空数据布局
     */
    public void hideEmptyData() {
        if (mBaseRefreshTextHasnoData.getVisibility() == VISIBLE) {
            mBaseRefreshTextHasnoData.setVisibility(GONE);
            mBaseLinearContentAll.setVisibility(VISIBLE);
        }
    }


    public SmartRefreshLayout getmSmartRefresh() {
        return mBaseSmartRefresh;
    }

    public LinearLayout getmBaseLinearContent() {
        return mBaseLinearContent;
    }

    public TextView getmBaseTextEmptyData() {
        return mBaseTextEmptyData;
    }

    /**
     * 整个父布局
     *
     * @return
     */
    public View getViewContent() {
        return viewContent;
    }

    public View getViewChild() {
        return viewChild;
    }
}
