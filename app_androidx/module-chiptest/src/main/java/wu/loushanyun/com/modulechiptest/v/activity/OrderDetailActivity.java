package wu.loushanyun.com.modulechiptest.v.activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.wu.loushanyun.base.config.K;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.DateFormatUtils;
import met.hx.com.librarybase.some_utils.SizeUtils;
import met.hx.com.librarybase.some_utils.TimeUtils;
import wu.loushanyun.com.modulechiptest.R;
import wu.loushanyun.com.modulechiptest.init.ChipCode;
import wu.loushanyun.com.modulechiptest.init.LoginParamManager;
import wu.loushanyun.com.modulechiptest.m.OrderListInfo;
import wu.loushanyun.com.modulechiptest.m.SelecorderInfo;
import wu.loushanyun.com.modulechiptest.m.SelectallInfo;
import wu.loushanyun.com.modulechiptest.p.runner.QueryByOrderListRunner;
import wu.loushanyun.com.modulechiptest.p.runner.SelecorderRunner;
import wu.loushanyun.com.modulechiptest.p.runner.SelectallRunner;

@Route(path = K.OrderDetailActivity)
public class OrderDetailActivity extends BaseNoPresenterActivity {
    private TextView OrderTime;
    private TextView detailAddress;
    private TextView receiveName;
    private TextView receiveNumber;
    private TextView acceptPhone;
    private TextView manufacturerContacter;
    private TextView deliveryTime;
    private TextView trackingCompany;
    private TextView trackingNumber;
    private RelativeLayout item1;
    private ImageView icon11;
    private ImageView logo11;
    private TextView mo1Num;
    private TextView mo1Zhinum;
    private ImageView mo1State;
    private RelativeLayout item2;
    private ImageView icon2;
    private ImageView logo2;
    private TextView mo2Num;
    private TextView mo2Zhinum;
    private ImageView mo2State;
    private RelativeLayout item3;
    private ImageView icon3;
    private ImageView logo3;
    private TextView mo3Num;
    private TextView mo3Zhinum;
    private ImageView mo3State;
    private RelativeLayout item4Simu;
    private ImageView icon4Simu;
    private ImageView logo4Simu;
    private TextView mo4Num;
    private TextView mo4Zhinum;
    private ImageView mo4State;

    private SelecorderInfo selecorderInfo;
    private boolean Mo1StateIsOK;
    private boolean Mo2StateIsOK;
    private boolean Mo3StateIsOK;
    private boolean Mo4StateIsOK;


    private SelectallInfo.DataBean dataBean;


    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mTitleText = LoginParamManager.getInstance().getLoginInfo().getData().getMloginSupplier();
        ba.mActivityLayoutId = R.layout.m_chip_activity_orderdetail;
    }


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
        pushEvent(ChipCode.MChipSelecorder, dataBean.getOrderNumber());
        pushEvent(ChipCode.MChipOrderList, dataBean.getOrderNumber());
    }

    @Override
    protected void initEventListener() {
        registerEventRunner(ChipCode.MChipSelecorder, new SelecorderRunner());
        registerEventRunner(ChipCode.MChipOrderList, new QueryByOrderListRunner());
    }


    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == ChipCode.MChipSelecorder) {
            if (event.isSuccess()) {
                selecorderInfo = (SelecorderInfo) event.getReturnParamAtIndex(0);
                if (selecorderInfo.getCode() == 0) {
                    int size = selecorderInfo.getData().size();
                    for (int i = 0; i < size; i++) {
                        int goodsid = selecorderInfo.getData().get(i).getGoodsId();
                        switch (goodsid) {
                            case 1:
                                item1.setVisibility(View.VISIBLE);
                                mo1Num.setText("数量 :" + selecorderInfo.getData().get(i).getModuleordernum() + "个");
                                mo1Zhinum.setText("已质检 :" + selecorderInfo.getData().get(i).getModulinspectionnum() + "个");
                                switch (selecorderInfo.getData().get(i).getInspectiontype()) {
                                    //质检状态（0：未质检，1：正在质检，2：已质检）
                                    case 0:
                                        mo1State.setImageDrawable(getResources().getDrawable(R.drawable.wzj));
                                        break;
                                    case 1:
                                        mo1State.setImageDrawable(getResources().getDrawable(R.drawable.zzzj));
                                        break;
                                    case 2:
                                        mo1State.setImageDrawable(getResources().getDrawable(R.drawable.yzj));
                                        Mo1StateIsOK = true;
                                        break;
                                }
                                break;
                            case 2:
                                item2.setVisibility(View.VISIBLE);
                                mo2Num.setText("数量 :" + selecorderInfo.getData().get(i).getModuleordernum() + "个");
                                mo2Zhinum.setText("已质检 :" + selecorderInfo.getData().get(i).getModulinspectionnum() + "个");
                                switch (selecorderInfo.getData().get(i).getInspectiontype()) {
                                    //质检状态（0：未质检，1：正在质检，2：已质检）
                                    case 0:
                                        mo2State.setImageDrawable(getResources().getDrawable(R.drawable.wzj));
                                        break;
                                    case 1:
                                        mo2State.setImageDrawable(getResources().getDrawable(R.drawable.zzzj));

                                        break;
                                    case 2:
                                        mo2State.setImageDrawable(getResources().getDrawable(R.drawable.yzj));
                                        Mo2StateIsOK = true;
                                        break;
                                }
                                break;
                            case 3:
                                item3.setVisibility(View.VISIBLE);
                                mo3Num.setText("数量 :" + selecorderInfo.getData().get(i).getModuleordernum() + "个");
                                mo3Zhinum.setText("已质检 :" + selecorderInfo.getData().get(i).getModulinspectionnum() + "个");
                                switch (selecorderInfo.getData().get(i).getInspectiontype()) {
                                    //质检状态（0：未质检，1：正在质检，2：已质检）
                                    case 0:
                                        mo3State.setImageDrawable(getResources().getDrawable(R.drawable.wzj));

                                        break;
                                    case 1:
                                        mo3State.setImageDrawable(getResources().getDrawable(R.drawable.zzzj));

                                        break;
                                    case 2:
                                        mo3State.setImageDrawable(getResources().getDrawable(R.drawable.yzj));
                                        Mo3StateIsOK = true;
                                        break;
                                }
                                break;
                            case 4:
                                item4Simu.setVisibility(View.VISIBLE);
                                mo4Num.setText("数量 :" + selecorderInfo.getData().get(i).getModuleordernum() + "个");
                                mo4Zhinum.setText("已质检 :" + selecorderInfo.getData().get(i).getModulinspectionnum() + "个");
                                switch (selecorderInfo.getData().get(i).getInspectiontype()) {
                                    //质检状态（0：未质检，1：正在质检，2：已质检）
                                    case 0:
                                        mo4State.setImageDrawable(getResources().getDrawable(R.drawable.wzj));

                                        break;
                                    case 1:
                                        mo4State.setImageDrawable(getResources().getDrawable(R.drawable.zzzj));

                                        break;
                                    case 2:
                                        mo4State.setImageDrawable(getResources().getDrawable(R.drawable.yzj));
                                        Mo4StateIsOK = true;
                                        break;
                                }
                                break;
                        }

                    }
                }
            }
        } else if (code == ChipCode.MChipOrderList) {
            if (event.isSuccess()) {
                OrderListInfo orderListInfo = (OrderListInfo) event.getReturnParamAtIndex(0);
                if (orderListInfo.getCode() == 0) {
                    OrderTime.setText("订单时间：" + DateFormatUtils.formatMdHmss(orderListInfo.getData().getOrderTime()));
                    detailAddress.setText("收货地址：" + orderListInfo.getData().getDetailAddress());
                    if (orderListInfo.getData().getReceiveName() != null) {
                        receiveName.setText("收货人姓名：" + orderListInfo.getData().getReceiveName());
                    } else {
                        receiveName.setVisibility(View.GONE);
                    }
                    if (orderListInfo.getData().getReceiveNumber() != null) {
                        receiveNumber.setText("收货人手机号：" + orderListInfo.getData().getReceiveNumber());
                    } else {
                        receiveNumber.setVisibility(View.GONE);
                    }

                    if (orderListInfo.getData().getAcceptPhone() != null) {
                        acceptPhone.setText("厂家手机号：" + orderListInfo.getData().getAcceptPhone());
                    } else {
                        acceptPhone.setVisibility(View.GONE);
                    }
                    if (orderListInfo.getData().getManufacturerContacter() != null) {
                        manufacturerContacter.setText("厂家联系人：" + orderListInfo.getData().getManufacturerContacter());
                    } else {
                        manufacturerContacter.setVisibility(View.GONE);
                    }
                    if (DateFormatUtils.formatMdHmss(orderListInfo.getData().getDeliveryTime()) != null) {
                        deliveryTime.setText("发货时间：" + TimeUtils.milliseconds2String(orderListInfo.getData().getDeliveryTime()));
                    } else {
                        deliveryTime.setVisibility(View.GONE);
                    }
                    if (orderListInfo.getData().getTrackingCompany() != null) {
                        trackingCompany.setText("快递公司：" + orderListInfo.getData().getTrackingCompany());
                    } else {
                        trackingCompany.setVisibility(View.GONE);
                    }
                    if (orderListInfo.getData().getTrackingNumber() != null) {
                        trackingNumber.setText("运单号：" + orderListInfo.getData().getTrackingNumber());
                    } else {
                        trackingNumber.setVisibility(View.GONE);
                    }
                }

            }


        }
    }

    @Override
    protected void initView() {
        OrderTime = (TextView) findViewById(R.id.OrderTime);
        detailAddress = (TextView) findViewById(R.id.detailAddress);
        receiveName = (TextView) findViewById(R.id.receiveName);
        receiveNumber = (TextView) findViewById(R.id.receiveNumber);
        acceptPhone = (TextView) findViewById(R.id.acceptPhone);
        manufacturerContacter = (TextView) findViewById(R.id.manufacturerContacter);
        deliveryTime = (TextView) findViewById(R.id.deliveryTime);
        trackingCompany = (TextView) findViewById(R.id.trackingCompany);
        trackingNumber = (TextView) findViewById(R.id.trackingNumber);
        item1 = (RelativeLayout) findViewById(R.id.item1);
        icon11 = (ImageView) findViewById(R.id.icon1_1);
        logo11 = (ImageView) findViewById(R.id.logo1_1);
        mo1Num = (TextView) findViewById(R.id.mo1_num);
        mo1Zhinum = (TextView) findViewById(R.id.mo1_zhinum);
        mo1State = (ImageView) findViewById(R.id.mo1_state);
        item2 = (RelativeLayout) findViewById(R.id.item2);
        icon2 = (ImageView) findViewById(R.id.icon2);
        logo2 = (ImageView) findViewById(R.id.logo2);
        mo2Num = (TextView) findViewById(R.id.mo2_num);
        mo2Zhinum = (TextView) findViewById(R.id.mo2_zhinum);
        mo2State = (ImageView) findViewById(R.id.mo2_state);
        item3 = (RelativeLayout) findViewById(R.id.item3);
        icon3 = (ImageView) findViewById(R.id.icon3);
        logo3 = (ImageView) findViewById(R.id.logo3);
        mo3Num = (TextView) findViewById(R.id.mo3_num);
        mo3Zhinum = (TextView) findViewById(R.id.mo3_zhinum);
        mo3State = (ImageView) findViewById(R.id.mo3_state);
        item4Simu = (RelativeLayout) findViewById(R.id.item4_simu);
        icon4Simu = (ImageView) findViewById(R.id.icon4_simu);
        logo4Simu = (ImageView) findViewById(R.id.logo4_simu);
        mo4Num = (TextView) findViewById(R.id.mo4_num);
        mo4Zhinum = (TextView) findViewById(R.id.mo4_zhinum);
        mo4State = (ImageView) findViewById(R.id.mo4_state);

        dataBean = getIntent().getParcelableExtra("order");


        initTestClick();
    }

    private void initTestClick() {
        item1.setOnClickListener(this::OnClick);
        item2.setOnClickListener(this::OnClick);
        item3.setOnClickListener(this::OnClick);
        item4Simu.setOnClickListener(this::OnClick);
    }

    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.item1:
//                if (Mo1StateIsOK) {
//                    sendMessageToast("该订单下的1号模组已经质检完成，无需重复质检");
//                } else {
//
//
//                }
                ARouter.getInstance().build(K.CheckFirstActivity).withParcelable("order", dataBean).navigation();
                break;
            case R.id.item2:
//                if (Mo2StateIsOK) {
//                    sendMessageToast("该订单下的2号模组已经质检完成，无需重复质检");
//                } else {
//
//                }
                ARouter.getInstance().build(K.CheckSecondActivity).withParcelable("order", dataBean).navigation();
                break;
            case R.id.item3:
//                if (Mo3StateIsOK) {
//                    sendMessageToast("该订单下的3号模组已经质检完成，无需重复质检");
//                } else {
//
//
//                }
                ARouter.getInstance().build(K.CheckThirdActivity).withParcelable("order", dataBean).navigation();
                break;
            case R.id.item4_simu:
                break;

        }
    }


}
