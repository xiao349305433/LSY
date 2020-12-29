package wu.loushanyun.com.modulerepair.v.activity;

import android.content.Intent;
import android.location.Location;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.qrcore.util.QRScannerHelper;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.tianditu.android.maps.GeoPoint;
import com.tianditu.android.maps.MapView;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.tianditu.listener.SimpleTianDiTuLocationListener;
import com.wu.loushanyun.base.tianditu.listener.TLocationOverlay;
import com.wu.loushanyun.base.tianditu.utils.TLocationMap;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.basemvp.v.activity.BaseBlueToothActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.base.rx.FlowableUtil;
import met.hx.com.base.baseevent.Event;
import met.hx.com.base.baseinterface.RxTimerListener;
import met.hx.com.base.basemvp.v.views.RefreshView;
import met.hx.com.librarybase.some_utils.LogUtils;
import met.hx.com.librarybase.some_utils.SelectorFactory;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.views.CustomPopWindow;
import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;
import wu.loushanyun.com.libraryfive.m.RefreshMainRepair;
import wu.loushanyun.com.modulerepair.R;
import wu.loushanyun.com.modulerepair.init.MRepairCode;
import wu.loushanyun.com.modulerepair.m.GetProductData;
import wu.loushanyun.com.modulerepair.m.RepairData;
import wu.loushanyun.com.modulerepair.m.RepairDataMsg;
import wu.loushanyun.com.modulerepair.p.adapter.PopChangJiaViewBinder;
import wu.loushanyun.com.modulerepair.p.adapter.PopShijianViewBinder;
import wu.loushanyun.com.modulerepair.p.adapter.RepairDataViewBinder;
import wu.loushanyun.com.modulerepair.p.runner.GetOffLineEquipmentRunner;
import wu.loushanyun.com.modulerepair.p.runner.GetProducterRunner;

@Route(path = K.MainRepairActivity)
public class MainRepairActivity extends BaseBlueToothActivity {
    private MapView mapview;
    private TextView textLeixing;
    private TextView textShijian;
    private TextView textShaixuan;
    private TextView textJuli;
    private RefreshView refreshView;
    private RecyclerView recyclerview;
    private CustomPopWindow popWindowLeixing;
    private CustomPopWindow popWindowJuLi;
    private CustomPopWindow popWindowShiJian;
    private CustomPopWindow popWindowChangJia;

    private ArrayList<GetProductData.DataBean> dataBeans;

    private TextView textGongyong;
    private TextView textJiayong;
    private LinearLayout linearGongyong;
    private TextView textLiuliangji;
    private LinearLayout linearJiayong;
    private TextView textYuanchuanbiaohao;
    private TextView textYuanchuanwulian;

    private RecyclerView popShijianRecycle;

    private RecyclerView popChangjiaRecycle;
    private MultiTypeAdapter multiTypeAdapterChangJia;
    private MultiTypeAdapter multiTypeAdapterShiJian;
    private ArrayList<String> arrayListShiJian;
    private ArrayList<RepairData> repairDatas;
    private MultiTypeAdapter multiTypeAdapterMain;
    private int pageNum = 1;
    private int pageSize = 10;

    private String gatherScene = "-1";
    private String productForm = "-1";
    private String tradeRegistId = "-1";
    private String offLineStartHour = "-1";
    private String offLineEndtHour = "-1";
    private String manufacturersIdentification = "-1";
    private String lon = "-1";
    private String lat = "-1";
    private String order = "-1";
    private String distance = "-1";
    private GeoPoint geoPoint;
    private RecyclerView popJuLiRecycle;
    private ArrayList<String> arrayListJuLi;
    private MultiTypeAdapter multiTypeAdapterJuLi;
    private boolean enlableLocation = true;
    private QRScannerHelper mScannerHelper;


    @Override
    protected void initEventListener() {
        registerEventRunner(MRepairCode.GetProducterRunner, new GetProducterRunner());
        registerEventRunner(MRepairCode.GetOffLineEquipmentRunner, new GetOffLineEquipmentRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == MRepairCode.GetProducterRunner) {
            if (event.isSuccess()) {
                GetProductData getProductData = (GetProductData) event.getReturnParamAtIndex(0);
                dataBeans.addAll(getProductData.getData());
                GetProductData.DataBean g = new GetProductData.DataBean();
                g.setSupplier("不限");
                g.setManufacturersIdentification(-1);
                dataBeans.add(0, g);
                multiTypeAdapterChangJia.notifyDataSetChanged();
            }
        } else if (code == MRepairCode.GetOffLineEquipmentRunner) {
            refreshView.getmSmartRefresh().finishRefresh();
            refreshView.getmSmartRefresh().finishLoadmore();
            if (event.isSuccess()) {
                RepairDataMsg repairDataMsg = (RepairDataMsg) event.getReturnParamAtIndex(0);
                if (pageNum == 1) {
                    repairDatas.clear();
                    refreshView.resetLoadMoreFinished();
                }
                if(repairDataMsg.getData()!=null){
                    if (repairDataMsg.getData().size() > 0) {
                        repairDatas.addAll(repairDataMsg.getData());
                        refreshView.hideEmptyData();
                    } else {
                        refreshView.showLoadMoreFinished();
                    }
                    multiTypeAdapterMain.notifyDataSetChanged();
                }

            }
        }

    }

    private void readPinLv() {
        byte[] bytes = DataParser.CMD_INFO_ALL;
        write(bytes, "正在识别更换设备与目标设备是否相符");
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_repair_activity_main;
        ba.mTitleText="现场检修";
        ba.mTitleLeftRightWidth=100;
    }

    @Override
    public void onRightClick(View item) {
    }
    @Override
    protected void initView() {
        super.initView();
        mapview = (MapView) findViewById(R.id.mapview);
        textLeixing = (TextView) findViewById(R.id.text_leixing);
        textShijian = (TextView) findViewById(R.id.text_shijian);
        textShaixuan = (TextView) findViewById(R.id.text_shaixuan);
        textJuli = (TextView) findViewById(R.id.text_juli);
        refreshView = (RefreshView) findViewById(R.id.refresh_view);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);

        mScannerHelper = new QRScannerHelper(this);
        mScannerHelper.setCallBack(new QRScannerHelper.OnScannerCallBack() {
            @Override
            public void onScannerBack(String result) {
                LogUtils.i("扫描结果"+result);
                ToastUtils.showShort(result);
            }
        });
        refreshView.showEmptyData("请在上方选择使用类型");
        refreshView.getmSmartRefresh().setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshView.getmSmartRefresh().finishLoadmore(3000);
                pageNum++;
                pushMain();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshView.getmSmartRefresh().finishRefresh(3000);
                repairDatas.clear();
                refreshView.resetLoadMoreFinished();
                pageNum = 1;
                pushMain();
            }
        });
        tradeRegistId = LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getId() + "";
        setMapData();
        initPopLeiXing();
        initPopJuLi();
        initPopShiJian();
        initPopChangJia();
        initMain();
        textLeixing.setOnClickListener(v -> {
            if (!popWindowLeixing.getPopupWindow().isShowing()) {
                textLeixing.setCompoundDrawables(null, null, getTextDrawable(R.drawable.m_repair_collect), null);
                popWindowLeixing.showAsDropDown(textLeixing);
            }
        });
        textJuli.setOnClickListener(v -> {
            if (!popWindowJuLi.getPopupWindow().isShowing()) {
                textJuli.setCompoundDrawables(null, null, getTextDrawable(R.drawable.m_repair_collect), null);
                popWindowJuLi.showAsDropDown(textJuli);
            }
        });
        textShijian.setOnClickListener(v -> {
            if (!popWindowShiJian.getPopupWindow().isShowing()) {
                textShijian.setCompoundDrawables(null, null, getTextDrawable(R.drawable.m_repair_collect), null);
                popWindowShiJian.showAsDropDown(textShijian);
            }
        });
        textShaixuan.setOnClickListener(v -> {
            if (!popWindowChangJia.getPopupWindow().isShowing()) {
                textShaixuan.setCompoundDrawables(null, null, getTextDrawable(R.drawable.m_repair_collect), null);
                popWindowChangJia.showAsDropDown(textShaixuan);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mScannerHelper != null) {
            mScannerHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setMapData() {
        compositeDisposable.add(FlowableUtil.intervalRxTimer(10000, new RxTimerListener() {
            @Override
            public void onNext(@NonNull Long number) {
                enlableLocation = true;
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onComplete() {

            }
        }));
        TLocationOverlay mLocation = new TLocationOverlay(this, mapview, new SimpleTianDiTuLocationListener() {
            @Override
            public void onLocationChanged(Location location, GeoPoint p) {
                super.onLocationChanged(location, p);
                if (enlableLocation) {
                    geoPoint = p;
                    enlableLocation = false;
                }
            }
        });
        mLocation.enableMyLocation();
        mapview.addOverlay(mLocation);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshMainRepair refreshMainRepair) {
        pageNum = 1;
        pushMain();
    }


    private void initMain() {
        repairDatas = new ArrayList<>();
        multiTypeAdapterMain = new MultiTypeAdapter();
        multiTypeAdapterMain.register(RepairData.class, new RepairDataViewBinder(new RepairDataViewBinder.OnRepairDataListener() {
            @Override
            public void onJianYanRepairData(RepairData repairData) {
                ARouter.getInstance().build(K.NeighborActivity).navigation();
            }

            @Override
            public void onTiHuanRepairData(RepairData repairData) {
                ARouter.getInstance().build(K.ChooseNewEquipmentActivity).withString("productName",repairData.getProductForm()).withString("oldSn",repairData.getSn()).navigation();
            }
        }, this));
        multiTypeAdapterMain.setItems(repairDatas);
        recyclerview.setAdapter(multiTypeAdapterMain);
    }

    private void initPopChangJia() {
        dataBeans = new ArrayList<>();
        View view = getLayoutInflater().inflate(R.layout.m_repair_pop_changjia, null);
        popWindowChangJia = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(view)//显示的布局，还可以通过设置一个View
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();//创建PopupWindow
        popWindowChangJia.getPopupWindow().setOnDismissListener(() -> {
            if (!textShaixuan.getText().toString().equals("厂家")) {
                textShaixuan.setTextColor(getResources().getColor(R.color.base_H));
            }
            textShaixuan.setCompoundDrawables(null, null, getTextDrawable(R.drawable.m_repair_open), null);
            popWindowChangJia.dissmiss();
        });
        popChangjiaRecycle = (RecyclerView) view.findViewById(R.id.pop_changjia_recycle);

        multiTypeAdapterChangJia = new MultiTypeAdapter();
        multiTypeAdapterChangJia.register(GetProductData.DataBean.class, new PopChangJiaViewBinder((position, dataBean) -> {
            popWindowChangJia.dissmiss();
            textShaixuan.setTextColor(getResources().getColor(R.color.base_H));
            textShaixuan.setText(dataBean.getSupplier());
            manufacturersIdentification = dataBean.getManufacturersIdentification() + "";
            pageNum = 1;
            pushMain();
        }));
        multiTypeAdapterChangJia.setItems(dataBeans);
        popChangjiaRecycle.setAdapter(multiTypeAdapterChangJia);
        pushEvent(MRepairCode.GetProducterRunner, LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getId() + "");
    }

    private void initPopShiJian() {
        View view = getLayoutInflater().inflate(R.layout.m_repair_pop_shijian, null);
        popWindowShiJian = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(view)//显示的布局，还可以通过设置一个View
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();//创建PopupWindow
        popWindowShiJian.getPopupWindow().setOnDismissListener(() -> {
            if (!textShijian.getText().toString().equals("时间")) {
                textShijian.setTextColor(getResources().getColor(R.color.base_H));
            }
            textShijian.setCompoundDrawables(null, null, getTextDrawable(R.drawable.m_repair_open), null);
            popWindowShiJian.dissmiss();
        });
        popShijianRecycle = (RecyclerView) view.findViewById(R.id.pop_shijian_recycle);
        arrayListShiJian = new ArrayList<>();
        multiTypeAdapterShiJian = new MultiTypeAdapter();
        multiTypeAdapterShiJian.register(String.class, new PopShijianViewBinder((position, item) -> {
            popWindowShiJian.dissmiss();
            textShijian.setTextColor(getResources().getColor(R.color.base_H));
            textShijian.setText(item);
            String result = item.replace("小时", "").replace("以外", "");
            String[] strings = result.split("-");
            if (strings.length == 1) {
                if ("不限".equals(strings[0])) {
                    offLineStartHour = "-1";
                    offLineEndtHour = "-1";
                } else {
                    offLineStartHour = strings[0];
                    offLineEndtHour = "-1";
                }
            } else if (strings.length == 2) {
                offLineStartHour = strings[0];
                offLineEndtHour = strings[1];
            }
            pageNum = 1;
            pushMain();
        }));
        multiTypeAdapterShiJian.setItems(arrayListShiJian);
        popShijianRecycle.setAdapter(multiTypeAdapterShiJian);
    }

    private void initPopJuLi() {
        View view = getLayoutInflater().inflate(R.layout.m_repair_pop_shijian, null);
        popWindowJuLi = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(view)//显示的布局，还可以通过设置一个View
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();//创建PopupWindow
        popWindowJuLi.getPopupWindow().setOnDismissListener(() -> {
            if (!textJuli.getText().toString().equals("位置")) {
                textJuli.setTextColor(getResources().getColor(R.color.base_H));
            }
            textJuli.setCompoundDrawables(null, null, getTextDrawable(R.drawable.m_repair_open), null);
            popWindowJuLi.dissmiss();
        });
        popJuLiRecycle = (RecyclerView) view.findViewById(R.id.pop_shijian_recycle);
        arrayListJuLi = new ArrayList<>();
        arrayListJuLi.add("默认（选择距离默认由近到远）");
        arrayListJuLi.add("由近到远");
        arrayListJuLi.add("由远到近");
        arrayListJuLi.add("100米");
        arrayListJuLi.add("500米");
        arrayListJuLi.add("1000米");
        multiTypeAdapterJuLi = new MultiTypeAdapter();
        multiTypeAdapterJuLi.register(String.class, new PopShijianViewBinder((position, item) -> {
            if ("默认（选择距离默认由近到远）".equals(item)) {
                return;
            } else if ("由近到远".equals(item)) {
                order = "asc";
                distance = "-1";
            } else if ("由远到近".equals(item)) {
                order = "desc";
                distance = "-1";
            } else if ("100米".equals(item)) {
                order = "asc";
                distance = "100";
            } else if ("500米".equals(item)) {
                order = "asc";
                distance = "500";
            } else if ("1000米".equals(item)) {
                order = "asc";
                distance = "1000";
            }
            if (geoPoint != null) {
                lon = String.valueOf(TLocationMap.getLon(geoPoint));
                lat = String.valueOf(TLocationMap.getLat(geoPoint));
            } else {
                sendMessageToast("定位失败");
                return;
            }
            pageNum = 1;
            popWindowJuLi.dissmiss();
            textJuli.setTextColor(getResources().getColor(R.color.base_H));
            textJuli.setText(item);
            pushMain();
        }));
        multiTypeAdapterJuLi.setItems(arrayListJuLi);
        popJuLiRecycle.setAdapter(multiTypeAdapterJuLi);
    }

    private void initPopLeiXing() {
        View view = getLayoutInflater().inflate(R.layout.m_repair_pop_leixing, null);
        popWindowLeixing = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(view)//显示的布局，还可以通过设置一个View
                .size(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();//创建PopupWindow
        popWindowLeixing.getPopupWindow().setOnDismissListener(() -> {
            if (!textLeixing.getText().toString().equals("类型")) {
                textLeixing.setTextColor(getResources().getColor(R.color.base_H));
            }
            textLeixing.setCompoundDrawables(null, null, getTextDrawable(R.drawable.m_repair_open), null);
            popWindowLeixing.dissmiss();
        });
        textGongyong = (TextView) view.findViewById(R.id.text_gongyong);
        textJiayong = (TextView) view.findViewById(R.id.text_jiayong);
        linearGongyong = (LinearLayout) view.findViewById(R.id.linear_gongyong);
        textLiuliangji = (TextView) view.findViewById(R.id.text_liuliangji);
        linearJiayong = (LinearLayout) view.findViewById(R.id.linear_jiayong);
        textYuanchuanbiaohao = (TextView) view.findViewById(R.id.text_yuanchuanbiaohao);
        textYuanchuanwulian = (TextView) view.findViewById(R.id.text_yuanchuanwulian);
        textGongyong.setBackground(SelectorFactory.newShapeSelector()
                .setDefaultBgColor(getResources().getColor(R.color.m_repair_gray_gray))
                .setSelectedBgColor(getResources().getColor(R.color.m_repair_gray_write))
                .create());
        textGongyong.setSelected(true);
        textJiayong.setBackground(SelectorFactory.newShapeSelector()
                .setDefaultBgColor(getResources().getColor(R.color.m_repair_gray_gray))
                .setSelectedBgColor(getResources().getColor(R.color.m_repair_gray_write))
                .create());
        textGongyong.setOnClickListener(v -> {
            textGongyong.setSelected(true);
            textJiayong.setSelected(false);
            linearGongyong.setVisibility(View.VISIBLE);
            linearJiayong.setVisibility(View.GONE);
        });
        textJiayong.setOnClickListener(v -> {
            textGongyong.setSelected(false);
            textJiayong.setSelected(true);
            linearGongyong.setVisibility(View.GONE);
            linearJiayong.setVisibility(View.VISIBLE);
        });
        textYuanchuanbiaohao.setOnClickListener(v -> {
            popWindowLeixing.dissmiss();
            textLeixing.setTextColor(getResources().getColor(R.color.base_H));
            textLeixing.setText("远传表号接入");
            arrayListShiJian.clear();
            arrayListShiJian.add("不限");
            arrayListShiJian.add("24-48小时");
            arrayListShiJian.add("48-72小时");
            arrayListShiJian.add("72小时以外");
            multiTypeAdapterShiJian.notifyDataSetChanged();
            pageNum = 1;
            gatherScene = LouShanYunUtils.getCJCJUploadCodeByString("基站民用") + "";
            productForm = LouShanYunUtils.getCPXSUploadIntByValue("远传表号接入") + "";
            offLineStartHour = "-1";
            offLineEndtHour = "-1";
            manufacturersIdentification = "-1";
            pushMain();
        });
        textYuanchuanwulian.setOnClickListener(v -> {
            popWindowLeixing.dissmiss();
            textLeixing.setTextColor(getResources().getColor(R.color.base_H));
            textLeixing.setText("远传物联网端");
            arrayListShiJian.clear();
            arrayListShiJian.add("不限");
            arrayListShiJian.add("72-148小时");
            arrayListShiJian.add("148小时以外");
            multiTypeAdapterShiJian.notifyDataSetChanged();
            pageNum = 1;
            gatherScene = LouShanYunUtils.getCJCJUploadCodeByString("基站民用") + "";
            productForm = LouShanYunUtils.getCPXSUploadIntByValue("远传物联网端") + "";
            offLineStartHour = "-1";
            offLineEndtHour = "-1";
            manufacturersIdentification = "-1";
            pushMain();
        });
        textLiuliangji.setOnClickListener(v -> {
            popWindowLeixing.dissmiss();
            textLeixing.setTextColor(getResources().getColor(R.color.base_H));
            textLeixing.setText("流量计");
            arrayListShiJian.clear();
            arrayListShiJian.add("不限");
            arrayListShiJian.add("24-48小时");
            arrayListShiJian.add("48-72小时");
            arrayListShiJian.add("72小时以外");
            multiTypeAdapterShiJian.notifyDataSetChanged();
            pageNum = 1;
            gatherScene = LouShanYunUtils.getCJCJUploadCodeByString("基站公用") + "";
            productForm = LouShanYunUtils.getCPXSUploadIntByValue("流量计") + "";
            offLineStartHour = "-1";
            offLineEndtHour = "-1";
            manufacturersIdentification = "-1";
            pushMain();
        });
    }

    private void pushMain() {
        pushEvent(MRepairCode.GetOffLineEquipmentRunner
                , gatherScene
                , productForm
                , tradeRegistId
                , offLineStartHour, offLineEndtHour, manufacturersIdentification, pageNum + ""
                , pageSize + ""
                , lon + ""
                , lat + ""
                , order + ""
                , distance + ""
        );
    }


    @Override
    protected void onChildConnectFailed(int i) {

    }

    @Override
    protected void onChildConnectSuccess() {
        readPinLv();
    }

    @Override
    protected void onChildNotify(byte[] bytes) {
    }

    @Override
    protected void onChildWriteSuccess() {

    }

    @Override
    protected void onChildWriteFailure(int i) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapview != null) {
            mapview.onDestroy();
        }

    }
}
