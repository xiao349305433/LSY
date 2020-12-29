package wu.loushanyun.com.moduletwoinit.v.activity;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.wu.loushanyun.base.config.K;

import java.util.ArrayList;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseRefreshActivity;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;
import wu.loushanyun.com.moduletwoinit.R;
import wu.loushanyun.com.moduletwoinit.init.MInitTwoCode;
import wu.loushanyun.com.moduletwoinit.m.WuLianUploadData;
import wu.loushanyun.com.moduletwoinit.p.adapter.OnCloudWuLianUploadDataBinder;
import wu.loushanyun.com.moduletwoinit.p.runner.GetAreaListRunner;

@Route(path = K.OnCloudWuLianListActivity)
public class OnCloudWuLianListActivity extends BaseRefreshActivity {
    private RecyclerView recyclerview;
    private MultiTypeAdapter multiTypeAdapter;
    private ArrayList<WuLianUploadData> wuLianUploadDataArrayList;
    private OnCloudWuLianUploadDataBinder wuLianUploadDataBinder;

    private int pageNum = 1;
    private int pageSize = 10;

    @Override
    protected void initEventListener() {
        registerEventRunner(MInitTwoCode.GetAreaListRunner, new GetAreaListRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == MInitTwoCode.GetAreaListRunner) {
            if (event.isSuccess()) {
                refreshView.hideEmptyData();
                ArrayList<WuLianUploadData> wuLianUploadData = (ArrayList<WuLianUploadData>) event.getReturnParamAtIndex(1);
                if (wuLianUploadData.size() == 0 && pageNum != 1) {
                    refreshView.showLoadMoreFinished();
                } else if (wuLianUploadData.size() == 0 && pageNum == 1) {
                    refreshView.showEmptyData();
                } else {
                    wuLianUploadDataArrayList.addAll(wuLianUploadData);
                    multiTypeAdapter.notifyDataSetChanged();
                    mSmartRefresh.finishLoadmore();
                    mSmartRefresh.finishRefresh();
                }

            }
        }

    }

    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        wuLianUploadDataBinder = new OnCloudWuLianUploadDataBinder();
        registerLifeCycle(wuLianUploadDataBinder);
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        super.onInitAttribute(ba);
        ba.mTitleText = "物联网端.云数据";
    }

    @Override
    protected void initView() {
        super.initView();
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        multiTypeAdapter = new MultiTypeAdapter();
        multiTypeAdapter.register(WuLianUploadData.class, wuLianUploadDataBinder);
        wuLianUploadDataArrayList = new ArrayList<>();
        multiTypeAdapter.setItems(wuLianUploadDataArrayList);
        recyclerview.setAdapter(multiTypeAdapter);
        refreshView.showEmptyData();
        pushEvent(MInitTwoCode.GetAreaListRunner, LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getId() + "", pageNum + "", pageSize + "");
    }

    @Override
    protected int initRefreshLayoutId() {
        return R.layout.m_twoinit_activity_on_cloud_wulist;
    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        super.onRefresh(refreshlayout);
        refreshView.resetLoadMoreFinished();
        pageNum = 1;
        wuLianUploadDataArrayList.clear();
        pushEventNoProgress(MInitTwoCode.GetAreaListRunner, LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getId() + "", pageNum + "", pageSize + "");
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        super.onLoadmore(refreshlayout);
        pageNum++;
        pushEventNoProgress(MInitTwoCode.GetAreaListRunner, LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getId() + "", pageNum + "", pageSize + "");
    }
}
