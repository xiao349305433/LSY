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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Chronometer;
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
import com.qrcore.util.QRScannerHelper;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LorawanUtils;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.m.sat.SAtInstructRealizeFactory;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;
import com.wu.loushanyun.basemvp.p.runner.MChipSetRxDelayRunner;
import com.wu.loushanyun.basemvp.v.activity.BaseSnBlueToothActivity;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ByteConvertUtils;
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
import wu.loushanyun.com.sixapp.m.ResponseAnalysisInfo;
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

@Route(path = K.SixChipQianContentActivity)
public class SixChipQianContentActivity extends BaseSnBlueToothActivity {

    private ScrollView qianScroll;
    private EditText blueSnTv;
    private TextView bluetoothConn;
    private TextView bluetoothDisconn;
    private TextView tvParamBlueshow;
    private TextView tvParamConnection;
    private TextView qianAllTv;
    private TextView tvQianTitle;
    private LinearLayout layoutProduction;
    private LinearLayout linearProduction2;
    private Spinner secondParameterCgxhSelect;

    private TextView tvBlueBattery;
    private LinearLayout layoutParameter;
    private TextView mSecondParameterEditFactoryId;
    private EditText secondParameterEditMaichongStart;
    private EditText secondParameterEditMaichongStartValue;
    private EditText secondParameterEditIdStart;

    private EditText textNum;
    private ImageView imgNumSaomiao;
    private TextView tvProductionPeizhi;
    private TextView tvProductionDuqu;
    private TextView tvProductionAll;
    private LinearLayout layoutTiaoshi;
    private TextView mRangeRatioTv;
    private TextView mKouJingTv;
    private TextView mBeiLv;
    private TextView hourValuse;


    private LinearLayout linearBlueCk;
    private CheckBox cbBlueChange;
    private CheckBox cbBlueJump;

    private TextView tvBlueJianyan;
    private TextView tvBlueFinish;

    private CheckBox checkMaichongJump;

    private boolean hasSetting = false;
    private boolean isJiHuoAndXiuMian = false;
    private boolean isJiHuo = true;
    private String[] kuopinyinziArray;
    private int saomiaoType = 0;
    private int type;
    private int writeCode;
    private boolean canPrint;
    private QRScannerHelper mScannerHelper;

    private SensoroDevice sensoroDeviceChoose;
    private PopupWindow myPop;
    private SensoroDevice sensoroDevice;


    private GoodsM goodsM;
    private   ContractProInfo contractProInfo;

    private SAtInstructRealizeFactory sAtInstructRealizeFactory;
    private HashMap<String, String> hashMap;
    private GetModelInfo.DatasBean datasBean;

    @Override
    protected int onChildLayout() {
        return R.layout.m_six_activity_qiancontent;
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
        qianScroll = (ScrollView) findViewById(R.id.qian_scroll);
        blueSnTv = (EditText) findViewById(R.id.blue_sn_tv);
        bluetoothConn = (TextView) findViewById(R.id.bluetooth_conn);
        bluetoothDisconn = (TextView) findViewById(R.id.bluetooth_disconn);
        tvParamBlueshow = (TextView) findViewById(R.id.tv_param_blueshow);
        tvParamConnection = (TextView) findViewById(R.id.tv_param_connection);
        qianAllTv = (TextView) findViewById(R.id.qian_all_tv);
        tvQianTitle = (TextView) findViewById(R.id.tv_qian_title);
        layoutProduction = (LinearLayout) findViewById(R.id.layout_production);
        linearProduction2 = (LinearLayout) findViewById(R.id.linear_production2);
        secondParameterCgxhSelect = (Spinner) findViewById(R.id.second_parameter_cgxh_select);
        tvBlueBattery = (TextView) findViewById(R.id.tv_blue_battery);
        layoutParameter = (LinearLayout) findViewById(R.id.layout_parameter);
        mSecondParameterEditFactoryId = (TextView) findViewById(R.id.mSecondParameterEditFactoryId);

        secondParameterEditMaichongStart = (EditText) findViewById(R.id.second_parameter_edit_maichong_start);
        secondParameterEditMaichongStartValue = (EditText) findViewById(R.id.second_parameter_edit_maichong_start_value);
        secondParameterEditIdStart = (EditText) findViewById(R.id.second_parameter_edit_id_start);
        hourValuse= (TextView) findViewById(R.id.hour_values);

        textNum = (EditText) findViewById(R.id.text_num);
        imgNumSaomiao = (ImageView) findViewById(R.id.img_num_saomiao);
        tvProductionPeizhi = (TextView) findViewById(R.id.tv_production_peizhi);
        tvProductionDuqu = (TextView) findViewById(R.id.tv_production_duqu);
        tvProductionAll = (TextView) findViewById(R.id.tv_production_all);
        layoutTiaoshi = (LinearLayout) findViewById(R.id.layout_tiaoshi);

        linearBlueCk = (LinearLayout) findViewById(R.id.linear_blue_ck);
        cbBlueChange = (CheckBox) findViewById(R.id.cb_blue_change);
        cbBlueJump = (CheckBox) findViewById(R.id.cb_blue_jump);
        tvBlueJianyan = (TextView) findViewById(R.id.tv_blue_jianyan);
        tvBlueFinish = (TextView) findViewById(R.id.tv_blue_finish);
        mRangeRatioTv= (TextView) findViewById(R.id.rangeRatio_tv);
        mKouJingTv= (TextView) findViewById(R.id.koujing_tv);
        mBeiLv= (TextView) findViewById(R.id.beilv_tv);

        checkMaichongJump = (CheckBox) findViewById(R.id.check_maichong_junp);




        hashMap = new HashMap<>();
        sAtInstructRealizeFactory = new SAtInstructRealizeFactory();
        snBlueToothTool.connectBlueTooth(sensoroDevice);
        goodsM=getIntent().getParcelableExtra("GoodsM");
        datasBean=getIntent().getParcelableExtra("GetModelInfo");
        mRangeRatioTv.setText("量程比："+goodsM.getRangeRatio());
        mBeiLv.setText("倍率/脉冲常数："+goodsM.getBeilv());
        mKouJingTv.setText("口径："+goodsM.getKoujing());
        mSecondParameterEditFactoryId.setText(LoginParamManager.getInstance().getLoginInfo().getBusinessName());
        hourValuse.setText("1:00~3:00 \n3:00~6:00 \n6:00~9:00 \n9:00~12:00 \n12:00~15:00 \n15:00~18:00 \n18:00~21:00 \n21:00~24:00 \n");


        ArrayAdapter<String> arrayAdapterchuangan = new ArrayAdapter<String>(this, R.layout.l_loushanyun_item_spinner, LorawanUtils.getAllChuanGan());
        secondParameterCgxhSelect.setAdapter(arrayAdapterchuangan);



        secondParameterEditMaichongStart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (!XHStringUtil.isEmpty(s.toString(), false)) {
                        long maiChongDishu = Long.valueOf(s.toString());
                        String beiLv =goodsM.getBeilv().substring(0, goodsM.getBeilv().indexOf("m"));
                        BigDecimal a1 = new BigDecimal(beiLv);
                        BigDecimal b1 = new BigDecimal(maiChongDishu);
//                        double result1 = a1.multiply(b1).doubleValue();// 相乘结果
                        secondParameterEditMaichongStartValue.setText(a1.multiply(b1).stripTrailingZeros().toPlainString() + "m³");
                    } else {
                        secondParameterEditMaichongStartValue.setText("");
                    }
                } catch (Exception e) {
                    secondParameterEditMaichongStartValue.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        setinitclick();
        initQRScanner();
    }


    private void setinitclick() {

        tvProductionPeizhi.setOnClickListener(this::OnBlueToothClick);
        imgNumSaomiao.setOnClickListener(this::OnBlueToothClick);
        tvBlueFinish.setOnClickListener(this::OnClick);
        bluetoothConn.setOnClickListener(this::OnClick);
        bluetoothDisconn.setOnClickListener(this::OnClick);
        tvProductionDuqu.setOnClickListener(this::OnBlueToothClick);
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
                //   showUploadpop();
                break;

            case R.id.bluetooth_disconn:
                snBlueToothTool.disconnect();
                blueSnTv.setText("");
                tvParamConnection.setText("未连接");
                break;
            case R.id.bluetooth_conn:
                if (tvParamConnection.getText().toString().contains("已连接")) {
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
        writeCode = 0;
        canPrint = false;
        switch (view.getId()) {
            case R.id.tv_production_duqu:
                if (!hasSetting) {
                    sendMessageToast("请先设置再读取");
                    return;
                }
                tvProductionAll.setText("");
                snBlueToothTool.write(LorawanUtils.getSingleReading(), "正在读取表信息");
                break;
            case R.id.img_num_saomiao:
                saomiaoType = 0;
                start();
                break;
            case R.id.tv_production_peizhi:
                if (XHStringUtil.isEmpty(secondParameterEditMaichongStart.getText().toString(), false)) {
                    sendMessageToast("请填入脉冲数");
                    return;
                }
                if (XHStringUtil.isEmpty(secondParameterEditIdStart.getText().toString(), false)) {
                    sendMessageToast("请填入设备ID");
                    return;
                }
                if (XHStringUtil.isEmpty(textNum.getText().toString(), false)) {
                    sendMessageToast("请扫描表身号");
                    return;
                }
                snBlueToothTool.write(LorawanUtils.getChuChangWrite(secondParameterEditMaichongStart.getText().toString(),  goodsM.getBeilv().replace("m³/ev", ""), hashMap.get(MapParams.传感信号), "一对一", 0, LoginParamManager.getInstance().getLoginInfo().getId(), hashMap.get(MapParams.机芯类型)), "正在设置出厂设置");
                hasSetting = true;
                break;

        }
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
                tvQianTitle.setText("LORAWAN模组无线性能调试");
                layoutProduction.setVisibility(View.GONE);
                layoutParameter.setVisibility(View.GONE);
                layoutTiaoshi.setVisibility(View.VISIBLE);
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


    @Override
    public void onChildConnectFailed(int i) {

    }

    @Override
    public void onChildConnectSuccess() {
        textNum.setText("");
        blueSnTv.setText(sensoroDeviceChoose.getSerialNumber().toUpperCase());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvParamConnection.setText("已连接");

            }
        });
        hashMap.clear();

        try {
            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).getTwoWriteBytes(), "识别模块中...", true, 6000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    public void onChildNotify(byte[] result) {
        byte code = (byte) (result[2] ^ ((byte) 0x80));
        XLog.i("蓝牙code=" + code);
        switch (code) {
            case 0x11:
                try {
                    type = LorawanUtils.getModuleType(result);
                    XLog.i("Type====" + type);
                    hashMap.putAll(LorawanUtils.getLorawanInfoAll(result));
                    initSnAgreement(Double.valueOf("1.07"));
                    if (type == 6) {
                        snBlueToothTool.write(LorawanUtils.getjiaozhun(), "正在校准模组时间");
                    } else {
                        sendMessageToast("识别不是LORAWAN模组，请选择正确的模组");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 0x01:
                if (result[3] == 0) {
                    sendMessageToast("设置成功");
                } else if (result[3] == 1) {
                    sendMessageToast("设置失败");
                }
                break;
            case 0x02:
                if (result[3] == 0) {
                    sendMessageToast("休眠成功");
                } else {
                    sendMessageToast("休眠失败");
                }
                break;
            case 0x03:
                if (result[3] == 0) {
                    sendMessageToast("设置成功");
                    snBlueToothTool.write(LorawanUtils.getChuChangWrite(secondParameterEditMaichongStart.getText().toString(), String.valueOf(goodsM.getBeilv()).substring(0, goodsM.getBeilv().indexOf("m")), hashMap.get(MapParams.传感信号), "一对一", 0,LoginParamManager.getInstance().getLoginInfo().getAccountId() , hashMap.get(MapParams.机芯类型)), "正在设置出厂设置");
                } else {
                    sendMessageToast("设置失败");
                }
                break;
            case 0x05:
                if (result[3] == 0) {
                    sendMessageToast("校准时间成功");
                    try {
                        snBlueToothTool.write(LorawanUtils.getChuChangReading(), "正在读取出厂设置");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    sendMessageToast("校准时间失败");
                }
                break;
            case 0x06:
                if (result[3] == 0) {
                    sendMessageToast("设置频率成功");
                } else if (result[3] == 1) {
                    sendMessageToast("设置频率失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                }
                break;
            case 0x07:
                if (result[3] == 0) {
                    sendMessageToast("读取电池电压成功");
                    StringBuffer stringBuffer = new StringBuffer();
                    StringBuffer stringBuffer2 = new StringBuffer();
                    for (int i = 3; i >= 0; i--) {
                        stringBuffer2.append(Integer.toHexString(result[i + 4]));
                        stringBuffer.append(ByteConvertUtils.conver2BinaryStr(result[i + 4]));

                    }
                    int zhi = Integer.parseInt(stringBuffer2.toString(), 16);
                    hashMap.put(MapParams.电池电压, String.valueOf((float) zhi / 100));

                } else if (result[3] == 1) {
                    sendMessageToast("读取电池电压失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                }
                break;
            case 0x08:
                if (result[3] == 0) {
                    sendMessageToast("设置设备ID成功");
                } else if (result[3] == 1) {
                    sendMessageToast("设置设备ID失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                }
                break;
            case 0x12:
                if (result[3] == 0) {
                    sendMessageToast("设置出厂设置成功");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tvProductionAll.setText("");
                            //   mTextBanbenhaoInfo.setText("产品固件版本号  :"+hashMap.get(MapParams.产品固件版本号));
                        }
                    });
                    snBlueToothTool.write(LorawanUtils.getSingleReading(), "正在读取表信息");
                 //   snBlueToothTool.write(LorawanUtils.getIDSetting(secondParameterEditIdStart.getText().toString()), "正在设置设备ID");
                } else if (result[3] == 1) {
                    sendMessageToast("设置出厂设置失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                }
                break;
            case 0x13:
                if (result[3] == 0) {
                    sendMessageToast("读取版本号成功");
                    hashMap.put(MapParams.产品固件版本号, String.valueOf(result[4]));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //   mTextBanbenhaoInfo.setText("产品固件版本号  :"+hashMap.get(MapParams.产品固件版本号));
                        }
                    });

                } else if (result[3] == 1) {
                    sendMessageToast("读取版本号失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                }
                break;
            case 0x14:
                if (result[3] == 0) {
                    sendMessageToast("读取最后上送时间成功");
                    hashMap.put(MapParams.数据性质, LorawanUtils.getShuJuTypeByCode(String.valueOf(result[4])));

                    long tmp;
                    tmp = 0;
                    for (int i = 6; i > 0; i--) {
                        tmp = (tmp * 256) + (result[i + 4] & 0xff);
                    }
                    hashMap.put(MapParams.最后一次采样时间, String.valueOf(tmp / 1000));

                } else if (result[3] == 1) {
                    sendMessageToast("读取最后上送时间失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                }
                break;

            case 0x16:
                if (result[3] == 0) {
                    sendMessageToast("读取出厂设置成功");
                    try {
                        hashMap.putAll(LorawanUtils.getReadChuChangSetting(result));
                        setChuChangText(hashMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (result[3] == 1) {
                    sendMessageToast("读取出厂设置失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                }
                break;

            case 0x22:
                if (result[3] == 0) {
                    try {
                        sendMessageToast("读取表信息成功");
                        hashMap.putAll(LorawanUtils.getLorawanSingle(result));
                        setSingleText(hashMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (result[3] == 1) {
                    sendMessageToast("读取失败");
                }
                break;
            case 0x23:
                if (result[3] == 0) {
                    sendMessageToast("操作成功");
                    hashMap.put(MapParams.阀门状态, String.valueOf(result[4] & 0xff) == "0" ? "开" : "关");

                } else if (result[3] == 1) {
                    sendMessageToast("操作失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                } else if (result[3] == 4) {
                    sendMessageToast("没有阀门，无法配置");
                }
                break;
        }
    }

    private void initSnAgreement(double hardVersion) {
        sAtInstructRealizeFactory.initHardVersion(hardVersion);
    }


    @Override
    public void onChildWriteSuccess() {

    }

    @Override
    public void onChildWriteFailure(int i) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvParamConnection.setText("未连接");
                ToastUtils.showLong("请重新连接");
            }
        });

    }


    private void setChuChangText(HashMap<String, String> hashMap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                snBlueToothTool.write(LorawanUtils.getSingleReading(), "正在读取表信息");
            }
        });
    }


    private void setSingleText(HashMap<String, String> hashMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n零点至三点的脉冲数:  ");
        sb.append(hashMap.get(MapParams.零点至三点的脉冲数));
        sb.append("\n\n三点至六点的脉冲数:  ");
        sb.append(hashMap.get(MapParams.三点至六点的脉冲数));
        sb.append("\n\n六点至九点的脉冲数:  ");
        sb.append(hashMap.get(MapParams.六点至九点的脉冲数));
        sb.append("\n\n九点至十二点的脉冲数:  ");
        sb.append(hashMap.get(MapParams.九点至十二点的脉冲数));
        sb.append("\n\n十二点至十五点的脉冲数:  ");
        sb.append(hashMap.get(MapParams.十二点至十五点的脉冲数));
        sb.append("\n\n十五点至十八点的脉冲数:  ");
        sb.append(hashMap.get(MapParams.十五点至十八点的脉冲数));
        sb.append("\n\n十八点至二十一点的脉冲数:  ");
        sb.append(hashMap.get(MapParams.十八点至二十一点的脉冲数));
        sb.append("\n\n二十一点至二十四点的脉冲数:  ");
        sb.append(hashMap.get(MapParams.二十一点至二十四点的脉冲数));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hourValuse.setText(sb.toString());
                secondParameterEditIdStart.setText(hashMap.get(MapParams.设备ID));
                tvBlueBattery.setText("电池 :"+LorawanUtils.getQianYaStateStringByCode(hashMap.get(MapParams.欠压状态)));
                secondParameterEditMaichongStart.setText(hashMap.get(MapParams.正计数));

                if(hasSetting){
                    pushEvent(SixCode.MSixContractPro, sensoroDeviceChoose.sn,3,LoginParamManager.getInstance().getLoginInfo().getLoginId(),LoginParamManager.getInstance().getLoginInfo().getAccountId());
                }

            }
        });
    }



    private void updata(HashMap<String, String> hashMap) {
        StringBuilder sb = new StringBuilder();
//        if (hashMap.get(MapParams.传感信号) == null) {
//            sendMessageToast(" 请进行第二步一键读取");
//            return;
//        }
//        if (hashMap.get(SAtInstructParams.sAtInstructSendingPower) == null) {
//            sendMessageToast(" 请进行第一步一键读取");
//            return;
//        }


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

        // sb.append(new BigDecimal(hashMap.get(MapParams.当前脉冲读数)).multiply(decimal).stripTrailingZeros().toPlainString());
//        if (hashMap.get(SAtInstructParams.sAtInstructRSSI) == null) {
//            sendMessageToast("请完成第三步强制发送");
//            return;
//        }


        TestUpData testUpData = new TestUpData();
        testUpData.setTest_time(TimeUtils.getCurTimeMills());

        testUpData.setBatchNumber(contractProInfo.getData().getBatchNumber());
        testUpData.setLoginId(LoginParamManager.getInstance().getLoginInfo().getLoginId());
        testUpData.setAuthId(LoginParamManager.getInstance().getLoginInfo().getId());
        testUpData.setIsStandard("1");
        testUpData.setCollectScene("2");
        testUpData.setProductForm("3");
        testUpData.setDataSource("0");

        testUpData.setBodyNumber(textNum.getText().toString());
        testUpData.setRangeRatio(goodsM.getRangeRatio());
        testUpData.setManufacturerId(goodsM.getBeilv());
        testUpData.setCaliber(goodsM.getKoujing());
//        testUpData.setGoodsName(goodsInfo.getDatas().get(0).getGoodsName());
//        testUpData.setModel(goodsInfo.getDatas().get(0).getGoodsModel());
//        testUpData.setCaliber(goodsInfo.getDatas().get(0).getCaliber());
        testUpData.setChipNumber("1" + hashMap.get(MapParams.设备ID) + sensoroDeviceChoose.sn);

        testUpData.setLotType("LORAWAN_S");
        testUpData.setJobNumber(LoginParamManager.getInstance().getLoginInfo().getJobNumber());
        testUpData.setGoodsId(datasBean.getId());
        testUpData.setTestId(contractProInfo.getData().getTestId());
        testUpData.setLotSn(sensoroDeviceChoose.sn);
        testUpData.setDeviceId(hashMap.get(MapParams.设备ID));
//        testUpData.setBusinessName(manuInfo.getDatas().get(0).getBusinessName());
        testUpData.setSubBusinessName(LoginParamManager.getInstance().getLoginInfo().getBusinessName());


        testUpData.setBatteryStatus(LorawanUtils.getQianYaStateStringByCode(hashMap.get(MapParams.欠压状态)));
//        testUpData.setSpreadFactor(hashMap.get(MapParams.扩频因子));
//        testUpData.setChannelParam(hashMap.get(MapParams.信道参数));
//        testUpData.setSendPower(hashMap.get(MapParams.发送功率));
        testUpData.setFactoryDication(hashMap.get(MapParams.正计数));
        testUpData.setMagnification(decimal.stripTrailingZeros().toPlainString());
//        testUpData.setSoftwareVersion(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
//        testUpData.setHardwareVersion(LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.硬件版本))));
        testUpData.setSenseSignal(hashMap.get(MapParams.传感信号));
        testUpData.setSendFrequency(LouShanYunUtils.getFSPLReadStringByCode(Long.valueOf(hashMap.get(MapParams.发送频率))));
        testUpData.setSignalType("数字信号");
        //  testUpData.setBattery();
        testUpData.setBluetooth("有");
//        testUpData.setComplexCapacity(goodsInfo.getDatas().get(0).getCompositeCapacitor());
//
//        testUpData.setWatchcase(goodsInfo.getDatas().get(0).getWatchCase());
//        testUpData.setRangeRatio(goodsInfo.getDatas().get(0).getRangeRatio());
//        testUpData.setValve(goodsInfo.getDatas().get(0).getValve());

        pushEvent(SixCode.MSixSaveSecond, new Gson().toJson(testUpData));
        XLog.i("testUpData" + new Gson().toJson(testUpData));
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
                            textNum.setText(result.substring(1, result.length()));
                        } else {
                            textNum.setText(result);
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


    @Override
    protected void initEventListener() {
        registerEventRunner(SixCode.MSixSaveSecond, new SaveSecondRunner());
        registerEventRunner(SixCode.MSixManuInf, new ManuInfRunner());
        registerEventRunner(SixCode.MSixContractPro, new ContractProRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
       if (code == SixCode.MSixSaveInf) {
            if (event.isSuccess()) {
                ResultJson resultJson = (ResultJson) event.getReturnParamAtIndex(0);
                if (resultJson.getCode() == 0) {
                    showUploadpop();
                } else {
                    ToastUtils.showLong(resultJson.getMessage());
                }
            }
        } else if (code == SixCode.MSixSaveSecond) {
           if (event.isSuccess()) {
               ResultJson resultJson = (ResultJson) event.getReturnParamAtIndex(0);
               if (resultJson.getCode() == 0) {
                   showUploadpop();
               } else {
                   ToastUtils.showLong(resultJson.getMessage());
               }
           }

        }else if (code == SixCode.MSixContractPro) {
           if (event.isSuccess()) {
                contractProInfo= (ContractProInfo) event.getReturnParamAtIndex(0);
               if (contractProInfo.getCode() == 0) {
                   tvBlueJianyan.setText("批次号："+contractProInfo.getData().getBatchNumber()+"\n\n设备休眠");
               } else {
                   tvBlueJianyan.setText(contractProInfo.getMessage());
                   ToastUtils.showLong(contractProInfo.getMessage());
               }

           }
       }
    }


}
