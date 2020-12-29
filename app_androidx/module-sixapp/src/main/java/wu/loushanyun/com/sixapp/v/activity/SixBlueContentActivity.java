package wu.loushanyun.com.sixapp.v.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Chronometer;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSpinner;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.lsy.domain.ResponseAnalysisInfo1;
import com.qrcore.util.QRScannerHelper;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.m.sat.SAtInstructRealizeFactory;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;
import com.wu.loushanyun.basemvp.p.runner.MChipSetRxDelayRunner;
import com.wu.loushanyun.basemvp.v.activity.BaseSnBlueToothActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.FromHtmlUtil;
import met.hx.com.librarybase.some_utils.TimeUtils;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.sixapp.R;
import wu.loushanyun.com.sixapp.init.LoginParamManager;
import wu.loushanyun.com.sixapp.init.SixCode;
import wu.loushanyun.com.sixapp.init.SixUtils;
import wu.loushanyun.com.sixapp.m.EnvironmentInfInfo;
import wu.loushanyun.com.sixapp.m.GetModelInfo;
import wu.loushanyun.com.sixapp.m.GoodsInfo;
import wu.loushanyun.com.sixapp.m.GoodsM;
import wu.loushanyun.com.sixapp.m.ManuInfo;
import wu.loushanyun.com.sixapp.m.ProInfo;
import wu.loushanyun.com.sixapp.m.ResultJson;
import wu.loushanyun.com.sixapp.m.TestUpData;
import wu.loushanyun.com.sixapp.m.WorkInfo;
import wu.loushanyun.com.sixapp.p.runner.ContractProInfo;
import wu.loushanyun.com.sixapp.p.runner.ContractProRunner;
import wu.loushanyun.com.sixapp.p.runner.GoodsInfRunner;
import wu.loushanyun.com.sixapp.p.runner.MSixSetRxDelayRunner;
import wu.loushanyun.com.sixapp.p.runner.ManuInfRunner;
import wu.loushanyun.com.sixapp.p.runner.SaveInfoRunner;
import wu.loushanyun.com.sixapp.p.runner.SaveSecondRunner;

import static com.wu.loushanyun.basemvp.init.SnBlueToothTool.BLUE_SEND_TIME_OUT;

@Route(path = K.SixBlueContentActivity)
public class SixBlueContentActivity extends BaseSnBlueToothActivity {

    private EditText mBlueSnTv;
    private TextView mTvParamBlueshow;
    private TextView mTvParamConnection;

    private LinearLayout mLinearProduction2;
    private TextView mTvProductionXiuzheng;
    private TextView mTvBlueBattery;
    private TextView mTvBluePingduan;
    private EditText mSecondParameterEditMaichongStart;
    private EditText mSecondParameterEditMaichongStartValue;
    private EditText mSecondParameterEditIdStart;
    private EditText mTextNum;
    private ImageView mImgNumSaomiao;
    private TextView mTvProductionPeizhi;
    private LinearLayout mLinearEnvironmental2;

    private LinearLayout mLinearBlueCk;


    private TextView mTvBlueJianyan;
    private TextView mTvBlueFinish;
    private LinearLayout mLayoutTiaoshi;
    private LinearLayout mLayoutProduction;
    private LinearLayout mLayoutParamter;

    private ScrollView mBlueScroll;

    private EditText mEditFeedBack;
    private EditText mEditFeedBackNum;
    private TextView bluetoothConn;
    private TextView bluetoothDisconn;
    private ImageView mImgTiaoShi;
    private TextView mSecondParameterEditService;
    private TextView mBlueAllTv;
    private TextView mTvProductionAll;
    private TextView mTvProductionDuqu;
    private EditText mBluecaliber;
    private CheckBox mCheckMaichongJump;
    private TextView mRangeRatioTv;
    private TextView mKouJingTv;
    private TextView mBeiLv;
    private Spinner mSecondParameterCgxhSelect;


    private SensoroDevice sensoroDeviceChoose;
    private PopupWindow myPop;
    private SensoroDevice sensoroDevice;
    //默认参数
    private byte chaiXie = 0;//拆卸   无
    private byte qiangCi = 1;//强磁   无
    private byte chuanGanQiZhuangTai = 0;//传感器状态   无
    private byte daoLiu = 1;//倒流   无
    private byte faMenZhuangTai = 0;//阀门状态   无
    private byte faSongPinLv = (byte) 0x30;//发送频率   默认24小时
    private boolean isAllSetting;
    private boolean isAllReading;
    private boolean hasSetting = false;
    private boolean isJiHuoAndXiuMian = false;
    private boolean isJiHuo = true;
    private String[] kuopinyinziArray;
    private int saomiaoType = 0;
    private int type;
    private int writeCode;
    private boolean canPrint;
    private QRScannerHelper mScannerHelper;

    private SAtInstructRealizeFactory sAtInstructRealizeFactory;
    private HashMap<String, String> hashMap;

    private GoodsM goodsM;
    private ContractProInfo contractProInfo;
    private GetModelInfo.DatasBean datasBean;


    @Override
    protected int onChildLayout() {
        return R.layout.m_six_activity_bluecontent;
    }


    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        super.onInitAttribute(ba);
        sensoroDevice = getIntent().getParcelableExtra("sensoroDevice");
        sensoroDeviceChoose = sensoroDevice;
        ba.mTitleText = sensoroDevice.sn;
        ba.mTitleBackgroundColorId = R.color.blue_bg;
        ba.mHasRightView = true;
    }


    @Override
    protected void initView() {
        super.initView();
        mBlueSnTv = (EditText) findViewById(R.id.blue_sn_tv);
        mTvParamBlueshow = (TextView) findViewById(R.id.tv_param_blueshow);
        mTvParamConnection = (TextView) findViewById(R.id.tv_param_connection);
        //  mTvBlueTitle = (TextView) findViewById(R.id.tv_blue_title);
        mLinearProduction2 = (LinearLayout) findViewById(R.id.linear_production2);

        mTvBlueBattery = (TextView) findViewById(R.id.tv_blue_battery);
        mSecondParameterEditMaichongStart = (EditText) findViewById(R.id.second_parameter_edit_maichong_start);
        mSecondParameterEditMaichongStartValue = (EditText) findViewById(R.id.second_parameter_edit_maichong_start_value);
        mSecondParameterCgxhSelect = (Spinner) findViewById(R.id.second_parameter_cgxh_select);
        mSecondParameterEditIdStart = (EditText) findViewById(R.id.second_parameter_edit_id_start);
        mTextNum = (EditText) findViewById(R.id.text_num);
        mImgNumSaomiao = (ImageView) findViewById(R.id.img_num_saomiao);
        mTvProductionPeizhi = (TextView) findViewById(R.id.tv_production_peizhi);
        mLinearBlueCk = (LinearLayout) findViewById(R.id.linear_blue_ck);
        mTvBlueJianyan = (TextView) findViewById(R.id.tv_blue_jianyan);
        mTvBlueFinish = (TextView) findViewById(R.id.tv_blue_finish);
        mLayoutTiaoshi = (LinearLayout) findViewById(R.id.layout_tiaoshi);
        mLayoutProduction = (LinearLayout) findViewById(R.id.layout_production);
        mLayoutParamter = (LinearLayout) findViewById(R.id.layout_parameter);

        mBlueScroll = (ScrollView) findViewById(R.id.blue_scroll);
        mEditFeedBack = (EditText) findViewById(R.id.edit_feedback);
        mEditFeedBackNum = (EditText) findViewById(R.id.edit_feedbacknum);
        bluetoothDisconn = (TextView) findViewById(R.id.bluetooth_disconn);
        bluetoothConn = (TextView) findViewById(R.id.bluetooth_conn);
        mSecondParameterEditService = (TextView) findViewById(R.id.second_parameter_edit_service);

        mTvProductionAll = (TextView) findViewById(R.id.tv_production_all);
        mTvProductionDuqu = (TextView) findViewById(R.id.tv_production_duqu);
        mCheckMaichongJump = (CheckBox) findViewById(R.id.check_maichong_junp);
        mRangeRatioTv = (TextView) findViewById(R.id.rangeRatio_tv);
        mKouJingTv = (TextView) findViewById(R.id.koujing_tv);
        mBeiLv = (TextView) findViewById(R.id.beilv_tv);

        goodsM = getIntent().getParcelableExtra("GoodsM");
        datasBean=getIntent().getParcelableExtra("GetModelInfo");
        mRangeRatioTv.setText("量程比：" + goodsM.getRangeRatio());
        mBeiLv.setText("倍率/脉冲常数：" + goodsM.getBeilv());
        mKouJingTv.setText("口径：" + goodsM.getKoujing());
        mSecondParameterEditService.setText(LoginParamManager.getInstance().getLoginInfo().getBusinessName());
        snBlueToothTool.connectBlueTooth(sensoroDevice);
        hashMap = new HashMap<>();
        kuopinyinziArray = getResources().getStringArray(R.array.l_loushanyun_kuopinyinzi);
        initlinear();
        initTestClick();
        initQRScanner();
        sAtInstructRealizeFactory = new SAtInstructRealizeFactory();
        mSecondParameterEditMaichongStart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (!XHStringUtil.isEmpty(s.toString(), false)) {
                        long maiChongDishu = Long.valueOf(s.toString());
                        String beiLv = goodsM.getBeilv().substring(0, goodsM.getBeilv().indexOf("m"));
                        BigDecimal a1 = new BigDecimal(beiLv);
                        BigDecimal b1 = new BigDecimal(maiChongDishu);
//                        double result1 = a1.multiply(b1).doubleValue();// 相乘结果
                        mSecondParameterEditMaichongStartValue.setText(a1.multiply(b1).stripTrailingZeros().toPlainString() + "m³");
                    } else {
                        mSecondParameterEditMaichongStartValue.setText("");
                    }
                } catch (Exception e) {
                    mSecondParameterEditMaichongStartValue.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }


    private void initlinear() {
        relativeLayoutRight.setVisibility(View.GONE);
        mTvParamBlueshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayoutRight.callOnClick();
            }
        });
    }


    private void showNextpop() {
        View view = getLayoutInflater().inflate(R.layout.m_six_view_pop_next, null);
        TextView popNextSure = view.findViewById(R.id.pop_next_sure);
        TextView popNextChanel = view.findViewById(R.id.pop_next_chanel);
        TextView popNextTv = view.findViewById(R.id.pop_next_tv);
        myPop = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myPop.setBackgroundDrawable(new ColorDrawable());
        myPop.setOutsideTouchable(true);
        myPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(lp);
            }
        });
        popNextChanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPop.dismiss();
            }
        });

        popNextSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //     mTvBlueTitle.setText("LORAWAN模组无线性能调试");
                mLayoutProduction.setVisibility(View.GONE);
                mLayoutParamter.setVisibility(View.GONE);
                mLayoutTiaoshi.setVisibility(View.VISIBLE);
                myPop.dismiss();
            }
        });
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        myPop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);

    }


    private void showUploadpop() {
        View view = getLayoutInflater().inflate(R.layout.m_six_view_pop_upload, null);
        Chronometer popUploadSure = view.findViewById(R.id.pop_upload_sure);
        TextView popNextTv = view.findViewById(R.id.pop_upload_tv);

        myPop = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myPop.setBackgroundDrawable(new ColorDrawable());
        myPop.setOutsideTouchable(true);
        myPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1.0f;
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(lp);
            }
        });
        popUploadSure.setBase(SystemClock.elapsedRealtime());
        popUploadSure.start();
        int second = 3;
        popUploadSure.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                int second = (int) ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000);
                chronometer.setText("倒计时" + (3 - second) + "S");
                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) > 3 * 1000) {
                    popUploadSure.stop();
                    myPop.dismiss();
                    finish();
                }
            }
        });

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        myPop.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }


    private void initTestClick() {

        mTvProductionPeizhi.setOnClickListener(this::OnBlueToothClick);
        mImgNumSaomiao.setOnClickListener(this::OnBlueToothClick);
        mTvBlueFinish.setOnClickListener(this::OnClick);
        bluetoothConn.setOnClickListener(this::OnClick);
        bluetoothDisconn.setOnClickListener(this::OnClick);
        mTvProductionDuqu.setOnClickListener(this::OnBlueToothClick);
    }


    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_blue_finish:
                if(hasSetting&&contractProInfo!=null){
                    if(contractProInfo.getCode()==0){
                        updata(hashMap);
                    }
                }else {
                    ToastUtils.showLong("请配置后再提交");
                }
                break;
            case R.id.bluetooth_disconn:
                snBlueToothTool.disconnect();
                mBlueSnTv.setText("");
                mTvParamConnection.setText("未连接");
                break;
            case R.id.bluetooth_conn:
                if (mTvParamConnection.getText().toString().contains("已连接")) {
                    sendMessageToast("当前已连接设备");
                    return;
                }
                snBlueToothTool.connectBlueTooth(sensoroDeviceChoose);
                break;
        }
    }


    /**
     * 需要判断蓝牙连接状态的点击事件
     *
     * @param view
     */
    private void OnBlueToothClick(View view) {
        if (!snBlueToothTool.isConnected()) {
            sendMessageToast("未连接蓝牙");
            return;
        }
        if (type != 2) {
            sendMessageToast("识别不是2号模组，请选择正确的模组");
            return;
        }
        writeCode = 0;
        canPrint = false;
        switch (view.getId()) {
            case R.id.tv_production_duqu:
                if (!hasSetting) {
                    sendMessageToast("请先设置再读取");
                    return;
                }
                mTvProductionAll.setText("");
                snBlueToothTool.write(DataParser.CMD_METER_INFO, "读取参数中...", true, 2000);
                break;
            case R.id.img_num_saomiao:
                saomiaoType = 0;
                start();
                break;

            case R.id.tv_tiaoshi_send:
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructForcedSend).getTwoReadBytes("15527919058")
                            , "强制发送", true, BLUE_SEND_TIME_OUT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.second_parameter_round_create_id:
                mSecondParameterEditIdStart.setText(LouShanYunUtils.getTimeID());
                break;
            case R.id.tv_production_peizhi:
                if (XHStringUtil.isEmpty(mSecondParameterEditService.getText().toString(), false)) {
                    sendMessageToast("没有服务商");
                    return;
                }

                if (XHStringUtil.isEmpty(mSecondParameterEditMaichongStart.getText().toString(), false)) {
                    sendMessageToast("请填入脉冲数");
                    return;
                }
                if (XHStringUtil.isEmpty(mSecondParameterEditIdStart.getText().toString(), false)) {
                    sendMessageToast("请填入设备ID");
                    return;
                }
                if (XHStringUtil.isEmpty(mTextNum.getText().toString(), false)) {
                    sendMessageToast("请扫描表身号");
                    return;
                }
                setDefaultFactoryParams();
                break;
        }
    }


    @Override
    public void onChildConnectFailed(int i) {
        ToastUtils.showLong("连接失败，请重新连接");
    }

    @Override
    public void onChildConnectSuccess() {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextNum.setText("");
                mBlueSnTv.setText(sensoroDeviceChoose.getSerialNumber().toUpperCase());
                mTvParamConnection.setText("已连接");
                hashMap.clear();
                mSecondParameterEditIdStart.setText(LouShanYunUtils.getTimeID());
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).getTwoWriteBytes(), "识别模块中...", true, 6000);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

    }


    @Override
    public void onChildNotify(byte[] result) {
        byte code = (byte) (result[2] ^ ((byte) 0x80));
        XLog.i("蓝牙code=" + code);
        switch (code) {
            case 0x25:
                isJiHuoAndXiuMian = false;
                if (result[3] == 0) {
                    sendMessageToast("休眠成功");
                } else {
                    sendMessageToast("休眠失败");
                }
                break;
            case 0x11:
                try {
                    type = DataParser.getModuleType(result);
                    hashMap.putAll(DataParser.getInformationAll(result));
                    XLog.i("版本：" + hashMap.get(MapParams.硬件版本));
                    initSnAgreement(Double.valueOf(LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.硬件版本)))));
                    snBlueToothTool.write(DataParser.CMD_METER_INFO, "读取参数中...", true, 2000);

                } catch (Exception e) {
                    e.printStackTrace();
                    sendMessageToast("识别失败");
                }
                break;
            case 0x20:
                if (result[3] == 0) {
                    setDefaultParams();
                }
                break;
            case 0x21:
                if (result[3] == 0) {
                    sendMessageToast("设置成功");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mSecondParameterEditIdStart.setText(LouShanYunUtils.getTimeID());

                            hasSetting = true;
                            mTvProductionDuqu.callOnClick();//配置完后直接读取一次

                        }
                    });
                } else {
                    sendMessageToast("设置失败");
                }
                break;
            case 0x22:
                hashMap.putAll(DataParser.getMoudleNo2(result));
                setDuQu(hashMap);
                break;
            case 0x26:
                if (result[3] == 0) {
                    if ("1".equals(String.valueOf(result[4] & 0xff))) {
                        runOnUiThread(() -> {
                            isJiHuo = true;
                            //已激活
                        });
                    } else {
                        runOnUiThread(() -> {
                            //未激活
                            isJiHuo = false;
                        });
                    }
                } else {
//                    runOnUiThread(() -> systemStatus.setVisibility(View.GONE));
                }
                break;
            case 0x24:
                if (result[3] == 0) {
                    if (isJiHuo) {
                        runOnUiThread(() -> {
                            //改成未激活
                            isJiHuo = false;
                            if (isJiHuoAndXiuMian) {
                                close();
                            }
                        });
                    } else {
                        runOnUiThread(() -> {
                            //改成已激活
                            isJiHuo = true;
                            if (isJiHuoAndXiuMian) {
                                close();
                            }
                        });
                    }
                }
                break;
            default:
                sendMessageToast("命令与模块不对应，请重新选择模块");
        }
    }

    @Override
    public void onChildWriteSuccess() {

    }

    @Override
    public void onChildWriteFailure(int i) {
        mTvParamConnection.setText("未连接");
        ToastUtils.showLong("请重新连接");
    }

    @Override
    protected void initEventListener() {
        // registerEventRunner(SixCode.MChipSetRxDelayRunner, new MSixSetRxDelayRunner());
        // registerEventRunner(SixCode.MSixGoodsInf, new GoodsInfRunner());
        registerEventRunner(SixCode.MSixSaveSecond, new SaveSecondRunner());
        registerEventRunner(SixCode.MSixContractPro, new ContractProRunner());
        // registerEventRunner(SixCode.MSixManuInf, new ManuInfRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

        if (code == SixCode.MSixSaveSecond) {
            if (event.isSuccess()) {
                ResultJson resultJson = (ResultJson) event.getReturnParamAtIndex(0);
                if (resultJson.getCode() == 0) {
                    showUploadpop();
                } else {
                    ToastUtils.showLong(resultJson.getMessage());
                }
            }
        } else if (code == SixCode.MSixContractPro) {
            if (event.isSuccess()) {
                 contractProInfo= (ContractProInfo) event.getReturnParamAtIndex(0);
                if (contractProInfo.getCode() == 0) {
                    mTvBlueJianyan.setText("批次号："+contractProInfo.getData().getBatchNumber()+"\n\n设备休眠");
                } else {
                    mTvBlueJianyan.setText(contractProInfo.getMessage());
                    ToastUtils.showLong(contractProInfo.getMessage());
                }

            }
        }
    }

    private void initSnAgreement(double hardVersion) {
        sAtInstructRealizeFactory.initHardVersion(hardVersion);
    }

    private void setDefaultParams() {
        String id = mSecondParameterEditIdStart.getText().toString().trim();
        byte[] input2 = DataParser.getBiaoXinxiChuShiHuaCMD(
                Long.parseLong(id),
                Long.parseLong(String.valueOf(mSecondParameterEditMaichongStart.getText())),
             //   goodsM.getBeilv().indexOf("m")


                String.valueOf(goodsM.getBeilv()).substring(0, goodsM.getBeilv().indexOf("m")));
        snBlueToothTool.write(input2, "设置2参数中...");
    }


    private void setDuQu(HashMap<String, String> hashMap) {
        StringBuilder sb = new StringBuilder();

        sb.append("\n脉冲常数(个/m³):  ");
        BigDecimal decimal = new BigDecimal(LouShanYunUtils.getBLReadStringByCode(Integer.parseInt(hashMap.get(MapParams.倍率))));
        sb.append(new BigDecimal(1).divide(decimal).stripTrailingZeros().toPlainString());
        sb.append("\n倍率(m³/ev):  ");
        sb.append(decimal.stripTrailingZeros().toPlainString());
        sb.append("\n脉冲数(个):  ");
        sb.append(hashMap.get(MapParams.当前脉冲读数));

        runOnUiThread(() -> {

            mSecondParameterEditIdStart.setText(hashMap.get(MapParams.设备ID));
            mTvBlueBattery.setText("电池  ：" + (hashMap.get(MapParams.表电池状态).equals("0") ? "正常" : "欠压"));
            if (!(hashMap.get(MapParams.表电池状态).equals("0"))) {
                ToastUtils.showLong("电池欠压，设备不合格");
                finish();
            }

            mTvProductionAll.setText(sb.toString());
            mBlueScroll.fullScroll(ScrollView.FOCUS_DOWN);

            if (hasSetting) {
                mTvProductionAll.setText(sb.toString());
                pushEvent(SixCode.MSixContractPro, sensoroDeviceChoose.sn,3,LoginParamManager.getInstance().getLoginInfo().getId(),LoginParamManager.getInstance().getLoginInfo().getAccountId());
//                int finallynum = Integer.parseInt(hashMap.get(MapParams.当前脉冲读数));
//                sb.append("\n最终脉冲数:");
//                sb.append(hashMap.get(MapParams.当前脉冲读数));
//
//                if (mCheckMaichongJump.isChecked()) {//跳过读脉冲
//                    showNextpop();
//                } else {
//                    if (finallynum - 5 < 2) {
//                        sendMessageToast("请吹两个脉冲数");
//                        return;
//                    } else {
//                        showNextpop();
//                    }
//                }
            }


        });
    }


    private void updata(HashMap<String, String> hashMap) {
        StringBuilder sb = new StringBuilder();

        sb.append(";传感信号:");
        sb.append(LouShanYunUtils.getCGXHReadStringByCode(Long.valueOf(hashMap.get(MapParams.传感信号))));


        BigDecimal decimal = new BigDecimal(LouShanYunUtils.getBLReadStringByCode(Integer.parseInt(hashMap.get(MapParams.倍率))));
        sb.append(";倍率:  ");
        sb.append(decimal.stripTrailingZeros().toPlainString());
        sb.append(";初始脉冲数:");
        sb.append("5");
        sb.append(";最终脉冲数:");
        //TODO测试用需改回
//        int finallynum = Integer.parseInt(hashMap.get(MapParams.当前脉冲读数));
//        if (finallynum - 5 < 2) {
//            sendMessageToast("请吹两个脉冲数");
//            return;
//        }
        sb.append(hashMap.get(MapParams.当前脉冲读数));


        TestUpData testUpData = new TestUpData();
        testUpData.setTest_time(TimeUtils.getCurTimeMills());

        testUpData.setLoginId(LoginParamManager.getInstance().getLoginInfo().getLoginId());
        testUpData.setAuthId(LoginParamManager.getInstance().getLoginInfo().getId());
        testUpData.setIsStandard("1");
        testUpData.setCollectScene("2");
        testUpData.setProductForm("3");
        testUpData.setDataSource("0");

        testUpData.setBodyNumber(mTextNum.getText().toString());
        testUpData.setBatchNumber(contractProInfo.getData().getBatchNumber());

        testUpData.setCaliber(goodsM.getKoujing());
        testUpData.setChipNumber("2" + hashMap.get(MapParams.设备ID) + sensoroDeviceChoose.sn);
        testUpData.setRangeRatio(goodsM.getRangeRatio());
        testUpData.setManufacturerId(goodsM.getBeilv());
        testUpData.setLotType("LORAWAN_S");
        testUpData.setJobNumber(LoginParamManager.getInstance().getLoginInfo().getJobNumber());
        testUpData.setGoodsId(datasBean.getId());
        testUpData.setTestId(contractProInfo.getData().getTestId());
//        testUpData.setProductFeatures(goodsInfo.getDatas().get(0).getProductFeatures());
        testUpData.setLotSn(sensoroDeviceChoose.sn);
        testUpData.setDeviceId(hashMap.get(MapParams.设备ID));
//        testUpData.setBusinessName(manuInfo.getDatas().get(0).getBusinessName());
        testUpData.setSubBusinessName(LoginParamManager.getInstance().getLoginInfo().getBusinessName());

        testUpData.setBatteryStatus(hashMap.get(MapParams.表电池状态).equals("0") ? "正常" : "欠压");
        testUpData.setFactoryDication(hashMap.get(MapParams.当前脉冲读数));
        testUpData.setMagnification(decimal.stripTrailingZeros().toPlainString());

        testUpData.setSignalType("数字信号");
        //  testUpData.setBattery();
//        testUpData.setComplexCapacity(goodsInfo.getDatas().get(0).getCompositeCapacitor());
        testUpData.setBluetooth("有");
//        testUpData.setValve(goodsInfo.getDatas().get(0).getValve());

        pushEvent(SixCode.MSixSaveSecond, new Gson().toJson(testUpData));
        XLog.i("testUpData" + new Gson().toJson(testUpData));

    }


    private void setDefaultFactoryParams() {

        snBlueToothTool.write(DataParser.getFactoryParams(
                mSecondParameterCgxhSelect.getSelectedItem().toString(), chaiXie, qiangCi, chuanGanQiZhuangTai, daoLiu, faMenZhuangTai, faSongPinLv, LoginParamManager.getInstance().getLoginInfo().getId(),

                "0", "0"
        ), "设置1参数中...");
    }

    /**
     * 在onCreate中调用
     */
    private void initQRScanner() {
        mScannerHelper = new QRScannerHelper(this);
        mScannerHelper.setCallBack(new QRScannerHelper.OnScannerCallBack() {
            @Override
            public void onScannerBack(String result) {
                if (!XHStringUtil.isEmpty(result, false)) {
                    if (saomiaoType == 0) {
                        if (result.substring(0, 1).equals("0")) {
                            mTextNum.setText(result.substring(1, result.length()));
                        } else {
                            mTextNum.setText(result);
                        }


                    }
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mScannerHelper != null && data != null) {
            mScannerHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void close() {
        snBlueToothTool.write(new byte[]{(byte) 0x68, (byte) 0x02, (byte) 0x25, (byte) 0x00, (byte) 0x27, (byte) 0x16}, "正在让设备进入休眠状态");
    }

    /**
     * 开启扫描界面
     */
    public void start() {
        mScannerHelper.startScanner();
    }

    @Override
    protected void onClickBlueListListener(SensoroDevice sensoroDevice) {
        super.onClickBlueListListener(sensoroDevice);
        this.sensoroDeviceChoose = sensoroDevice;
    }
}
