package wu.loushanyun.com.modulechiptest.v.activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.elvishew.xlog.XLog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.wu.loushanyun.base.config.K;

import java.util.ArrayList;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.baseevent.Event;
import met.hx.com.base.basemvp.v.views.RefreshView;
import met.hx.com.librarybase.views.CustomPopWindow;
import wu.loushanyun.com.modulechiptest.R;
import wu.loushanyun.com.modulechiptest.init.ChipCode;
import wu.loushanyun.com.modulechiptest.init.LoginParamManager;
import wu.loushanyun.com.modulechiptest.m.SelectallInfo;
import wu.loushanyun.com.modulechiptest.p.adapter.OrderViewBinder;
import wu.loushanyun.com.modulechiptest.p.adapter.PopStateViewBinder;
import wu.loushanyun.com.modulechiptest.p.runner.SelectallRunner;

@Route(path = K.CheckActivity)
public class CheckActivity extends BaseNoPresenterActivity {
    private TextView textState;
    private TextView textTime;
    private TextView textMo;
    private RefreshView refreshView;
    ;
    private RecyclerView orderRv;
    private OrderViewBinder orderViewBinder;
    private MultiTypeAdapter multiTypeAdapter;
    private ArrayList<SelectallInfo.DataBean> dataBeans;
    private PopupWindow myPop;
    private int Inspectiontype = -1;
    private int Timesort = 1;
    private int ModuleType = 0;
    private int Pagenum = 1;
    private int PageSize = 5;
    private SmartRefreshLayout smartRefreshLayout;
    private CustomPopWindow PopupState;
    private CustomPopWindow PopupTime;
    private CustomPopWindow PopupMo;


    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        orderViewBinder = new OrderViewBinder();
        registerLifeCycle(orderViewBinder);
    }

    @Override
    protected void initEventListener() {
        registerEventRunner(ChipCode.MChipSelectall, new SelectallRunner());
    }


    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_chip_activity_check;
        ba.mTitleText = "订单质检";
        if (LoginParamManager.getInstance().getLoginInfo().getData().getMloginHomeType() == 3) {
            ba.mTitleRightImageIcon = R.drawable.ic_more_vert_black_24dp;
            ba.mTitleLeftRightWidth = 80;
        }
    }

    @Override
    public void onRightClick(View item) {
        showPicSelect(item);
    }

    @Override
    protected void initView() {
        orderRv = (RecyclerView) findViewById(R.id.order_rv);
        refreshView = (RefreshView) findViewById(R.id.refresh);
        textState = (TextView) findViewById(R.id.text_state);
        textTime = (TextView) findViewById(R.id.text_time);
        textMo = (TextView) findViewById(R.id.text_mo);
        smartRefreshLayout = refreshView.getmSmartRefresh();

        orderRv.setLayoutManager(new LinearLayoutManager(this));
        multiTypeAdapter = new MultiTypeAdapter();
        dataBeans = new ArrayList<>();
        multiTypeAdapter.register(SelectallInfo.DataBean.class, orderViewBinder);
        multiTypeAdapter.setItems(dataBeans);
        orderRv.setAdapter(multiTypeAdapter);


//        setSelectAll();
        textState.setOnClickListener(v -> {
            if (!PopupState.getPopupWindow().isShowing()) {
                textState.setCompoundDrawables(null, null, getTextDrawable(R.drawable.m_repair_collect), null);
                PopupState.showAsDropDown(textState);
            }
        });
        textTime.setOnClickListener(v -> {
            if (!PopupTime.getPopupWindow().isShowing()) {
                textTime.setCompoundDrawables(null, null, getTextDrawable(R.drawable.m_repair_collect), null);
                PopupTime.showAsDropDown(textTime);
            }
        });
        textMo.setOnClickListener(v -> {
            if (!PopupMo.getPopupWindow().isShowing()) {
                textMo.setCompoundDrawables(null, null, getTextDrawable(R.drawable.m_repair_collect), null);
                PopupMo.showAsDropDown(textMo);
            }
        });
        initPopState();
        initPopTime();
        initPopMo();


        smartRefreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshView.getmSmartRefresh().finishLoadmore(3000);
                Pagenum++;
                PsuhSelectall();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshView.getmSmartRefresh().finishRefresh(3000);
                Pagenum = 1;
                PsuhSelectall();
            }
        });
        PsuhSelectall();
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == ChipCode.MChipSelectall) {
            if (event.isSuccess()) {
                refreshView.hideEmptyData();
                SelectallInfo selectallInfo = (SelectallInfo) event.getReturnParamAtIndex(0);
                if (selectallInfo.getCode() == 0) {
                    if (selectallInfo.getData().size() == 0) {
                        if (Pagenum == 1) {
                            refreshView.showEmptyData();
                        } else {
                            refreshView.showLoadMoreFinished();
                        }
                    } else {
                        dataBeans.addAll(selectallInfo.getData());
                        multiTypeAdapter.notifyDataSetChanged();
                    }
                    smartRefreshLayout.finishLoadmore();
                    smartRefreshLayout.finishRefresh();

                }

            }
        }
    }


    private void initPopState() {
        View view = getLayoutInflater().inflate(R.layout.m_chip_pop_state, null);
        PopupState = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(view)//显示的布局，还可以通过设置一个View
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();//创建PopupWindow
        PopupState.getPopupWindow().setOnDismissListener(() -> {
            textState.setCompoundDrawables(null, null, getTextDrawable(R.drawable.m_repair_open), null);
            PopupState.dissmiss();
        });
        RecyclerView recyclerState = (RecyclerView) view.findViewById(R.id.pop_state_recycle);
        ArrayList StateList = new ArrayList<>();
        StateList.add("全部");
        StateList.add("未质检订单");
        StateList.add("正在质检订单");
        StateList.add("已质检订单");
        MultiTypeAdapter StateAdapter = new MultiTypeAdapter();
        StateAdapter.register(String.class, new PopStateViewBinder((position, item) -> {
            PopupState.dissmiss();
            textState.setText(item);
            Inspectiontype = position - 1;
            Pagenum = 1;
            PsuhSelectall();
        }));
        StateAdapter.setItems(StateList);
        recyclerState.setAdapter(StateAdapter);
    }

    private void initPopTime() {
        View view = getLayoutInflater().inflate(R.layout.m_chip_pop_time, null);
        PopupTime = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(view)//显示的布局，还可以通过设置一个View
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();//创建PopupWindow
        PopupTime.getPopupWindow().setOnDismissListener(() -> {
            textTime.setCompoundDrawables(null, null, getTextDrawable(R.drawable.m_repair_open), null);
            PopupTime.dissmiss();
        });
        RecyclerView recyclerState = (RecyclerView) view.findViewById(R.id.pop_time_recycle);
        ArrayList StateList = new ArrayList<>();
        StateList.add("时间由远及近");
        StateList.add("时间由近及远");
        MultiTypeAdapter StateAdapter = new MultiTypeAdapter();
        StateAdapter.register(String.class, new PopStateViewBinder((position, item) -> {
            textTime.setText(item);
            PopupTime.dissmiss();
            Timesort = position;
            Pagenum = 1;
            PsuhSelectall();
        }));
        StateAdapter.setItems(StateList);
        recyclerState.setAdapter(StateAdapter);
    }

    private void initPopMo() {
        View view = getLayoutInflater().inflate(R.layout.m_chip_pop_mo, null);
        PopupMo = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(view)//显示的布局，还可以通过设置一个View
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();//创建PopupWindow
        PopupMo.getPopupWindow().setOnDismissListener(() -> {
            textMo.setCompoundDrawables(null, null, getTextDrawable(R.drawable.m_repair_open), null);
            PopupMo.dissmiss();
        });
        RecyclerView recyclerState = (RecyclerView) view.findViewById(R.id.pop_mo_recycle);
        ArrayList StateList = new ArrayList<>();
        StateList.add("全部");
        StateList.add("包含1号模组");
        StateList.add("包含2号模组");
        StateList.add("包含3号模组");
        StateList.add("包含4号模组");
        MultiTypeAdapter StateAdapter = new MultiTypeAdapter();
        StateAdapter.register(String.class, new PopStateViewBinder((position, item) -> {
            PopupMo.dissmiss();
            textMo.setText(item);
            ModuleType = position;
            Pagenum = 1;
            PsuhSelectall();

        }));
        StateAdapter.setItems(StateList);
        recyclerState.setAdapter(StateAdapter);
    }


    private void PsuhSelectall() {
        if (Pagenum == 1) {
            refreshView.resetLoadMoreFinished();
            dataBeans.clear();
        }
        pushEvent(ChipCode.MChipSelectall, LoginParamManager.getInstance().getLoginInfo().getData().getMloginFactoryNum(), Inspectiontype, Timesort, ModuleType, Pagenum, PageSize);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (myPop != null) {
            myPop.dismiss();
        }
    }


    /**
     * 照片选择器
     */
    @SuppressLint("InflateParams")
    private void showPicSelect(View item) {
        View view = LayoutInflater.from(this).inflate(R.layout.m_chip_view_morepop, null, false);
        TextView pop_out = view.findViewById(R.id.pop_out);
        TextView pop_check = view.findViewById(R.id.pop_check);
        TextView pop_produce = view.findViewById(R.id.pop_produce);
        TextView pop_test = view.findViewById(R.id.pop_test);
        myPop = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myPop.setBackgroundDrawable(new ColorDrawable());
        myPop.setOutsideTouchable(true);
        myPop.showAsDropDown(item, (int) item.getX(), (int) item.getY());


        String[] types = LoginParamManager.getInstance().getLoginInfo().getData().getMloginType().split(",");
        for (int i = 0; i < types.length; i++) {
            if (types[i].equals("1")) {
                pop_test.setVisibility(View.VISIBLE);//显示测试类型
            }
            if (types[i].equals("2")) {
                pop_produce.setVisibility(View.VISIBLE);//显示生产类型
            }
            if (types[i].equals("3")) {
                pop_check.setVisibility(View.VISIBLE);//显示质检类型
            }
        }
        pop_check.setVisibility(View.GONE);//

        pop_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build(K.LoginActivityPhone).navigation();

                finish();
            }
        });
        pop_produce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build(K.ProduceHomeActivity).navigation();
            }
        });
        pop_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build(K.CheckActivity).navigation();
            }
        });

        pop_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build(K.ChipHomeActivity).navigation();
            }
        });
    }


}
