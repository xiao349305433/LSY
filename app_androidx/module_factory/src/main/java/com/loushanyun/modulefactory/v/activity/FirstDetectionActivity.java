//package com.loushanyun.modulefactory.v.activity;
//
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Handler;
//import android.os.Message;
//import androidx.recyclerview.widget.RecyclerView;
//import android.util.Log;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.alibaba.android.arouter.facade.annotation.Route;
//import com.flyco.roundview.RoundLinearLayout;
//import com.flyco.roundview.RoundTextView;
//import com.lidroid.xutils.HttpUtils;
//import com.lidroid.xutils.exception.HttpException;
//import com.lidroid.xutils.http.RequestParams;
//import com.lidroid.xutils.http.ResponseInfo;
//import com.lidroid.xutils.http.callback.RequestCallBack;
//import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
//import com.loushanyun.modulefactory.R;
//import com.loushanyun.modulefactory.init.FactoryCode;
//import com.loushanyun.modulefactory.init.LoginParamManager;
//import com.loushanyun.modulefactory.m.FactoryData;
//import com.loushanyun.modulefactory.p.runner.UploadDataRunner;
//import com.wu.loushanyun.base.config.K;
//import com.wu.loushanyun.base.url.URLUtils;
//import com.wu.loushanyun.base.util.DataParser;
//import com.wu.loushanyun.base.util.LouShanYunUtils;
//import com.wu.loushanyun.base.util.MapParams;
//import com.wu.loushanyun.basemvp.m.SaveDataMeter;
//import com.wu.loushanyun.basemvp.p.adapter.MeterViewBinder;
//import com.wu.loushanyun.basemvp.p.runner.GetChangJiaBiaoShiRunner;
//import com.wu.loushanyun.basemvp.v.activity.BaseBlueToothActivity;
//import com.wu.loushanyun.basemvp.v.activity.MainActivityPrinter;
//
//import org.java_websocket.client.WebSocketClient;
//import org.java_websocket.drafts.Draft_17;
//import org.java_websocket.handshake.ServerHandshake;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.math.BigDecimal;
//import java.net.URI;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.HashMap;
//
//import jrt.cifco.com.base.base.BaseAttribute;
//import jrt.cifco.com.base.base.dialog.LoadingDialogUtil;
//import jrt.cifco.com.base.base.multitype.MultiTypeAdapter;
//import jrt.cifco.com.base.baseevent.Event;
//import jrt.cifco.com.base.util.NoDoubleClickUtils;
//import jrt.cifco.com.librarybase.some_utils.ByteConvertUtils;
//import jrt.cifco.com.librarybase.some_utils.LogUtils;
//import jrt.cifco.com.librarybase.some_utils.RegexUtils;
//import jrt.cifco.com.librarybase.some_utils.XHStringUtil;
//
//@Route(path = K.DetectionActivity)
//public class FirstDetectionActivity extends BaseBlueToothActivity implements View.OnClickListener {
//    private final int TYPE_TTL = 0;
//    private final int TYPE_MOD_BUS = 1;
//    private final int TYPE_485 = 2;
//    private final int TYPE_485_CONFIG = 3;
//    private final int TYPE_LSY03 = 4;
//    private final int FRESH = 8;
//    private final int RSSI = 10;
//    private final int DATA_ADD = 0x0a;
//    private final int DATA_DEL = 0x0d;
//
//    private TextView tvModuleType;
//    private TextView tvConfigName;
//    private TextView tvConfigValue;
//    private ImageView systemStatus;
//    private TextView tvModuleName;
//    private TextView tvConfigInfoName;
//    private TextView tvConfigInfoValue;
//    private LinearLayout llStatus;
//    private TextView tvConfigStatusName;
//    private TextView tvConfigStatusValue;
//    private RoundLinearLayout meterLoading;
//    private RoundTextView roundTextOpen;
//    private RoundTextView roundTextClose;
//    private EditText meterLoadingNumStart;
//    private View meterLoadingGap;
//    private EditText meterLoadingNumEnd;
//    private RoundTextView meterLoadingNumTest;
//    private RecyclerView meterLoadingNumResult;
//    private RoundLinearLayout connTest;
//    private EditText connTestPhoneNumber;
//    private RoundTextView connTestSend;
//    private RoundTextView tvUploadCopy;
//    private RelativeLayout controlPane;
//    private RoundTextView tvBluetoothConn;
//    private RoundTextView tvUpload;
//
//
//    private Handler handler;
//    private int type;//0 ttl,1 mod-bus,2 485
//    private boolean isTestConn;//是否测试过基站连接
//    private boolean isActive;//是否激活
//    private boolean isTestMeter;//是否进行过表号测试
//    private HashMap<String, String> map;
//    private ArrayList<String> keys;
//
//    private int meterReadStart;
//    private int meterReadEnd;
//    private int currentNum;
//    private double softVersion;
//
//    private boolean isJiZhongQiOpened = false;
//
//    Comparator comparator = (Comparator<SaveDataMeter>) (o1, o2) -> {
//        int a = Integer.parseInt(o1.getMeterNumber());
//        int b = Integer.parseInt(o2.getMeterNumber());
//        if (a > b) {
//            return 1;
//        }
//        if (a < b) {
//            return -1;
//        }
//        return 0;
//    };
//    private MultiTypeAdapter multiTypeAdapter;
//    private MeterViewBinder meterViewBinder;
//
//    private ArrayList<SaveDataMeter> meterArrayList;
//
//
//    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//    @Override
//    protected void initLifeCycle() {
//        super.initLifeCycle();
//        meterViewBinder = new MeterViewBinder(this);
//        registerLifeCycle(meterViewBinder);
//    }
//
//    /**
//     * 设置出厂设置信息
//     */
//    private void setFactoryInfo(String moduleName, StringBuilder sb) {
//        tvModuleName.setText(moduleName);
//        String value;
//        sb.append("物联SN\n");
//        sb.append("采集场景\n");
//        sb.append("工作模式\n");
//        sb.append("无线频率\n");
//        sb.append("发送功率\n");
//        sb.append("出厂时间\n");
//        sb.append("网络交互\n");
//        sb.append("扩频因子\n");
//        sb.append("信道参数\n");
//        sb.append("软件版本\n");
//        sb.append("硬件版本\n");
//        sb.append("厂家标识");
//        tvConfigName.setText(sb);
//        sb.delete(0, sb.length());
//        if ((value = map.get(MapParams.物联SN)) != null) {
//            if (keys.indexOf(MapParams.物联SN) == -1) {
//                keys.add(MapParams.物联SN);
//            }
//            sb.append(value);
//        } else {
//            sb.append(sensoroDevice.getSerialNumber());
//        }
//        sb.append('\n');
//        if ((value = map.get(MapParams.采集场景)) != null) {
//            if (keys.indexOf(MapParams.采集场景) == -1) {
//                keys.add(MapParams.采集场景);
//            }
//            sb.append(LouShanYunUtils.getCJCJReadStringByCode(value));
//        }
//        sb.append('\n');
//        if ((value = map.get(MapParams.工作模式)) != null) {
//            if (keys.indexOf(MapParams.工作模式) == -1) {
//                keys.add(MapParams.工作模式);
//            }
//            if ("1".equals(value)) {
//                sb.append("从机模式");
//            } else if ("2".equals(value)) {
//                sb.append("主机模式");
//            } else {
//                sb.append("无效参数");
//            }
//        }
//        sb.append('\n');
//        if ((value = map.get(MapParams.无线频率)) != null) {
//            if (keys.indexOf(MapParams.无线频率) == -1) {
//                keys.add(MapParams.无线频率);
//            }
//            sb.append(LouShanYunUtils.getWuXianPinLv(value));
//        }
//        sb.append('\n');
//        if ((value = map.get(MapParams.发送功率)) != null) {
//            if (keys.indexOf(MapParams.发送功率) == -1) {
//                keys.add(MapParams.发送功率);
//            }
//            sb.append(value);
//            sb.append("dbm");
//        }
//        sb.append('\n');
//        if ((value = map.get(MapParams.出厂时间_年)) != null) {
//            if (keys.indexOf(MapParams.出厂时间_年) == -1) {
//                keys.add(MapParams.出厂时间_年);
//            }
//            sb.append("20");
//            for (int i = value.length(); i < 2; i++) {
//                sb.append(0);
//            }
//            sb.append(value);
//            sb.append("-");
//            if ((value = map.get(MapParams.出厂时间_月)) != null) {
//                if (keys.indexOf(MapParams.出厂时间_月) == -1) {
//                    keys.add(MapParams.出厂时间_月);
//                }
//                for (int i = value.length(); i < 2; i++) {
//                    sb.append(0);
//                }
//                sb.append(value);
//            } else {
//                sb.append("00");
//            }
//            sb.append("-");
//            if ((value = map.get(MapParams.出厂时间_日)) != null) {
//                if (keys.indexOf(MapParams.出厂时间_日) == -1) {
//                    keys.add(MapParams.出厂时间_日);
//                }
//                for (int i = value.length(); i < 2; i++) {
//                    sb.append(0);
//                }
//                sb.append(value);
//            } else {
//                sb.append("00");
//            }
//            sb.append(" 00:00:00");
//        }
//        sb.append('\n');
//        if ((value = map.get(MapParams.网络交互)) != null) {
//            if (keys.indexOf(MapParams.网络交互) == -1) {
//                keys.add(MapParams.网络交互);
//            }
//            sb.append("0".equals(value) ? "不带网络反馈" : "带网络反馈");
//        }
//        sb.append('\n');
//        if ((value = map.get(MapParams.扩频因子)) != null) {
//            if (keys.indexOf(MapParams.扩频因子) == -1) {
//                keys.add(MapParams.扩频因子);
//            }
//            sb.append(LouShanYunUtils.getKPYZReadStringByCode(value));
//        }
//        sb.append('\n');
//        if ((value = map.get(MapParams.信道参数)) != null) {
//            if (keys.indexOf(MapParams.信道参数) == -1) {
//                keys.add(MapParams.信道参数);
//            }
//            sb.append("0".equals(value) ? "模式A" : "模式B");
//        }
//        sb.append('\n');
//        if ((value = map.get(MapParams.硬件版本)) != null) {
//            if (keys.indexOf(MapParams.硬件版本) == -1) {
//                keys.add(MapParams.硬件版本);
//            }
//            sb.append(LouShanYunUtils.getHardWareVersion(Integer.parseInt(value)));
//        }
//        sb.append('\n');
//        if ((value = map.get(MapParams.软件版本)) != null) {
//            if (keys.indexOf(MapParams.软件版本) == -1) {
//                keys.add(MapParams.软件版本);
//            }
//            sb.append(LouShanYunUtils.getSoftWareVersion(Integer.parseInt(value)));
//        }
//        sb.append('\n');
//        if ((value = map.get(MapParams.厂家标识)) != null) {
//            if (keys.indexOf(MapParams.厂家标识) == -1) {
//                keys.add(MapParams.厂家标识);
//            }
//            sb.append("未注册的厂家标识");
//            pushEventNoProgress(FactoryCode.GetChangJiaBiaoShiRunner, value);
//        }
//        tvConfigValue.setText(sb);
//        sb.delete(0, sb.length());
//    }
//
//    private void setTTLConfigData() {
//        String value;
//        StringBuilder sb = new StringBuilder();
//        setFactoryInfo("远传表号接入（物联网集中器）", sb);
//        //信息
//        sb.append("传感信号\n");
//        sb.append("发送频率\n");
//        sb.append("电源类型");
//        tvConfigInfoName.setText(sb);
//        sb.delete(0, sb.length());
//        if ((value = map.get(MapParams.传感信号)) != null) {
//            if (keys.indexOf(MapParams.传感信号) == -1) {
//                keys.add(MapParams.传感信号);
//            }
//            sb.append(LouShanYunUtils.getCGXHReadStringByCode(Long.parseLong(value)));
//        }
//        sb.append('\n');
//        if ((value = map.get(MapParams.发送频率)) != null) {
//            if (keys.indexOf(MapParams.发送频率) == -1) {
//                keys.add(MapParams.发送频率);
//            }
//            sb.append(getFSPV(value));
//        }
//        sb.append('\n');
//        if ((value = map.get(MapParams.电源类型)) != null) {
//            if (keys.indexOf(MapParams.电源类型) == -1) {
//                keys.add(MapParams.电源类型);
//            }
//            sb.append(LouShanYunUtils.getDianYuanLeiXin(value));
//        }
//        tvConfigInfoValue.setText(sb);
//        sb.delete(0, sb.length());
//        //状态
//        llStatus.setVisibility(View.VISIBLE);
//        sb.append("通讯状态\n");
//        sb.append("电池状态");
//        tvConfigStatusName.setText(sb);
//        sb.delete(0, sb.length());
//        sb.append("正常\n");
//        if ((value = map.get(MapParams.电源类型)) != null) {
//            if (keys.indexOf(MapParams.电源类型) == -1) {
//                keys.add(MapParams.电源类型);
//            }
//            String type = LouShanYunUtils.getDianYuanLeiXin(value);
//            if ("物联电池".equalsIgnoreCase(type)) {
//                sb.append("0".equalsIgnoreCase(map.get(MapParams.底板状态_自备电池状态)) ? "正常" : "异常");
//            } else {
//                sb.append("0".equalsIgnoreCase(map.get(MapParams.底板状态_外接电源220V状态)) ? "正常" : "异常");
//            }
//        }
//        tvConfigStatusValue.setText(sb);
//        sb.delete(0, sb.length());
//    }
//
//    private void setMODBUS485ConfigData() {
//        String value;
//        StringBuilder sb = new StringBuilder();
//        setFactoryInfo("流量计（数字信号）", sb);
//        //信息
//        sb.append("协议\n");
//        sb.append("传感信号\n");
//        sb.append("发送频率\n");
//        sb.append("电源类型\n");
//        sb.append("读数\n");
//        sb.append("其他参数类型\n");
//        sb.append("其他参数\n");
//        sb.append("瞬时流量");
//        tvConfigInfoName.setText(sb);
//        sb.delete(0, sb.length());
//        sb.append("协议1\n");
//        sb.append("RS485\n");
//        sb.append("15分钟\n");
//        sb.append("物联电池\n");
//        sb.append("1m³\n");
//        sb.append("水温\n");
//        sb.append("0℃\n");
//        sb.append("0m³/h");
//        tvConfigInfoValue.setText(sb);
//        sb.delete(0, sb.length());
//        //状态
//        llStatus.setVisibility(View.VISIBLE);
//        sb.append("物联电池\n");
//        sb.append("流向\n");
//        sb.append("传感器\n");
//        sb.append("计量状态\n");
//        sb.append("通讯状态\n");
//        sb.append("倒流值");
//        tvConfigStatusName.setText(sb);
//        sb.delete(0, sb.length());
//        sb.append("正常\n");
//        sb.append("正常\n");
//        sb.append("正常\n");
//        sb.append("异常(空管 超载 传感器故障 倒流)\n");
//        sb.append("异常(485电源欠压 485中断)\n");
//        sb.append("0");
//        tvConfigStatusValue.setText(sb);
//        sb.delete(0, sb.length());
//    }
//
//    private void set485ConfigData() {
//        String value;
//        StringBuilder sb = new StringBuilder();
//        setFactoryInfo("家用", sb);
//        //信息
//        sb.append("产品形式\n");
//        sb.append("电源类型\n");
//        sb.append("发送频率\n");
//        sb.append("接入形式\n");
//        sb.append("行业类型");
//        tvConfigInfoName.setText(sb);
//        sb.delete(0, sb.length());
//        sb.append("第三方表号\n");
//        sb.append("物联电池\n");
//        sb.append("24小时\n");
//        sb.append("一对多\n");
//        sb.append("智慧水务");
//        tvConfigInfoValue.setText(sb);
//        sb.delete(0, sb.length());
//        //状态
//        llStatus.setVisibility(View.GONE);
//    }
//
//    private void setLSY03ConfigData() {
//        tvModuleType.setText("LSY-M03");
//        String value;
//        StringBuilder sb = new StringBuilder();
//        sb.append("设备ID\n");
//        sb.append("设备表号\n");
//        sb.append("传感器定义\n");
//        sb.append("注册企业名称\n");
//        sb.append("注册企业代码\n");
//        sb.append("固态版本号\n");
//        sb.append("出厂时间");
//        tvConfigName.setText(sb);
//        sb.delete(0, sb.length());
//        if ((value = map.get(MapParams.设备ID)) != null) {
//            if (keys.indexOf(MapParams.设备ID) == -1) {
//                keys.add(MapParams.设备ID);
//            }
//            sb.append(value);
//        }
//        sb.append('\n');
//        if ((value = map.get(MapParams.表号)) != null) {
//            if (keys.indexOf(MapParams.表号) == -1) {
//                keys.add(MapParams.表号);
//            }
//            sb.append(value);
//        }
//        sb.append('\n');
//        if ((value = map.get(MapParams.传感类型)) != null) {
//            if (keys.indexOf(MapParams.传感类型) == -1) {
//                keys.add(MapParams.传感类型);
//            }
//            if ("1".equals(value)) {
//                sb.append("3EV");
//            } else if ("3".equals(value)) {
//                sb.append("2EV");
//            } else {
//                sb.append("正反脉冲");
//            }
//        }
//        sb.append('\n');
//        sb.append(LoginParamManager.getInstance().getProductRegister().getCompanyName());
//        sb.append('\n');
//        if ((value = map.get(MapParams.企业代码)) != null) {
//            if (keys.indexOf(MapParams.企业代码) == -1) {
//                keys.add(MapParams.企业代码);
//            }
//            String str = value.substring(value.length() - 2, value.length());
//            char letter = Integer.toHexString(Integer.parseInt(str)).charAt(0);//字符格式转换成字母格式
//            sb.append(value.substring(0, value.length() - 2));
//            sb.append((char) (letter - 32));
//        }
//        sb.append('\n');
//        if ((value = map.get(MapParams.软件版本)) != null) {
//            if (keys.indexOf(MapParams.软件版本) == -1) {
//                keys.add(MapParams.软件版本);
//            }
//            sb.append(value);
//        }
//        sb.append('\n');
//        if ((value = map.get(MapParams.出厂时间_年)) != null) {
//            if (keys.indexOf(MapParams.出厂时间_年) == -1) {
//                keys.add(MapParams.出厂时间_年);
//            }
//            sb.append("20");
//            for (int i = value.length(); i < 2; i++) {
//                sb.append(0);
//            }
//            sb.append(value);
//            sb.append("-");
//            if ((value = map.get(MapParams.出厂时间_月)) != null) {
//                if (keys.indexOf(MapParams.出厂时间_月) == -1) {
//                    keys.add(MapParams.出厂时间_月);
//                }
//                for (int i = value.length(); i < 2; i++) {
//                    sb.append(0);
//                }
//                sb.append(value);
//            } else {
//                sb.append("00");
//            }
//            sb.append("-");
//            if ((value = map.get(MapParams.出厂时间_日)) != null) {
//                if (keys.indexOf(MapParams.出厂时间_日) == -1) {
//                    keys.add(MapParams.出厂时间_日);
//                }
//                for (int i = value.length(); i < 2; i++) {
//                    sb.append(0);
//                }
//                sb.append(value);
//            } else {
//                sb.append("00");
//            }
//        }
//        tvConfigValue.setText(sb);
//        sb.delete(0, sb.length());
//        //信息
//        sb.append("传感信号\n");
//        sb.append("脉冲值\n");
//        sb.append("倍率(m³/ev)\n");
//        sb.append("脉冲常数(个/m³)");
//        tvConfigInfoName.setText(sb);
//        sb.delete(0, sb.length());
//        sb.append("TTL1(一对多)\n");
//        sb.append("正在读取...\n");
//        if ((value = map.get(MapParams.倍率)) != null) {
//            if (keys.indexOf(MapParams.倍率) == -1) {
//                keys.add(MapParams.倍率);
//            }
//            value = getBeiLV(value);
//            sb.append(value);
//            sb.append("\n");
//            if (value.equals("0")) {
//                sb.append(value);
//            } else {
//                sb.append(new BigDecimal(1).divide(new BigDecimal(value)).stripTrailingZeros().toPlainString());
//            }
//        } else {
//            sb.append("\n");
//        }
//        tvConfigInfoValue.setText(sb);
//        sb.delete(0, sb.length());
//        //状态
//        llStatus.setVisibility(View.VISIBLE);
//        sb.append("强磁\n");
//        sb.append("欠压\n");
//        sb.append("倒流\n");
//        sb.append("拆卸");
//        tvConfigStatusName.setText(sb);
//        sb.delete(0, sb.length());
//        if ((value = map.get(MapParams.状态)) != null) {
//            if (keys.indexOf(MapParams.状态) == -1) {
//                keys.add(MapParams.状态);
//            }
//            int status = Integer.parseInt(value);
//            if ((status & 0b00000001) == 0) {
//                sb.append("正常");
//            } else {
//                sb.append("强磁");
//            }
//            sb.append("\n");
//
//            if ((status & 0b00001000) == 0) {
//                sb.append("正常");
//            } else {
//                sb.append("欠压");
//            }
//            sb.append("\n");
//            if ((status & 0b00010000) == 0) {
//                sb.append("正常");
//            } else {
//                sb.append("倒流");
//            }
//            sb.append("\n");
//            if ((status & 0b00000010) == 0) {
//                sb.append("正常");
//            } else {
//                sb.append("拆卸");
//            }
//        } else {
//            sb.append("\n\n\n");
//        }
//        tvConfigStatusValue.setText(sb);
//        sb.delete(0, sb.length());
//    }
//
//    @Override
//    protected void initView() {
//        super.initView();
//        tvModuleType = (TextView) findViewById(R.id.tv_moduleType);
//        tvConfigName = (TextView) findViewById(R.id.tv_config_name);
//        tvConfigValue = (TextView) findViewById(R.id.tv_config_value);
//        systemStatus = (ImageView) findViewById(R.id.system_status);
//        tvModuleName = (TextView) findViewById(R.id.tv_moduleName);
//        tvConfigInfoName = (TextView) findViewById(R.id.tv_config_info_name);
//        tvConfigInfoValue = (TextView) findViewById(R.id.tv_config_info_value);
//        llStatus = (LinearLayout) findViewById(R.id.ll_status);
//        tvConfigStatusName = (TextView) findViewById(R.id.tv_config_status_name);
//        tvConfigStatusValue = (TextView) findViewById(R.id.tv_config_status_value);
//        meterLoading = (RoundLinearLayout) findViewById(R.id.meter_loading);
//        roundTextOpen = (RoundTextView) findViewById(R.id.round_text_open);
//        roundTextClose = (RoundTextView) findViewById(R.id.round_text_close);
//        meterLoadingNumStart = (EditText) findViewById(R.id.meter_loading_num_start);
//        meterLoadingGap = (View) findViewById(R.id.meter_loading_gap);
//        meterLoadingNumEnd = (EditText) findViewById(R.id.meter_loading_num_end);
//        meterLoadingNumTest = (RoundTextView) findViewById(R.id.meter_loading_num_test);
//        meterLoadingNumResult = (RecyclerView) findViewById(R.id.meter_loading_num_result);
//        connTest = (RoundLinearLayout) findViewById(R.id.conn_test);
//        connTestPhoneNumber = (EditText) findViewById(R.id.conn_test_phone_number);
//        connTestSend = (RoundTextView) findViewById(R.id.conn_test_send);
//        tvUploadCopy = (RoundTextView) findViewById(R.id.tv_upload_copy);
//        controlPane = (RelativeLayout) findViewById(R.id.control_pane);
//        tvBluetoothConn = (RoundTextView) findViewById(R.id.tv_bluetooth_conn);
//        tvUpload = (RoundTextView) findViewById(R.id.tv_upload);
//
//
//        meterLoadingNumTest.setOnClickListener(this::onClick);
//        connTestSend.setOnClickListener(this::onClick);
//        tvUploadCopy.setOnClickListener(this::onClick);
//        roundTextOpen.setOnClickListener(this::onClick);
//        roundTextClose.setOnClickListener(this::onClick);
//        tvBluetoothConn.setOnClickListener(this::onClick);
//        tvUpload.setOnClickListener(this::onClick);
//        if (getIntent().getBooleanExtra("isActiveAble", true)) {
//            systemStatus.setOnClickListener(this::onClick);//点击激活可以响应
//        }
//        switch (type) {
//            case TYPE_LSY03:
//                tvModuleName.setVisibility(View.GONE);
//                meterLoading.setVisibility(View.GONE);
//                connTest.setVisibility(View.GONE);
//                controlPane.setVisibility(View.GONE);
//                break;
//            case TYPE_485:
//            case TYPE_MOD_BUS:
//                tvBluetoothConn.setText("瞬时流量读取");
//                meterLoading.setVisibility(View.GONE);
//                connTest.setVisibility(View.GONE);
//            case TYPE_TTL:
//                tvUploadCopy.setVisibility(View.GONE);
//                break;
//            case TYPE_485_CONFIG:
//                llStatus.setVisibility(View.GONE);//隐藏状态布局
//                meterLoading.setVisibility(View.GONE);
//                connTest.setVisibility(View.GONE);
//                controlPane.setVisibility(View.GONE);
//                break;
//        }
//        systemStatus.setVisibility(View.GONE);//默认隐藏系统激活状态
//        connTestPhoneNumber.setText(LoginParamManager.getInstance().getProductRegister().getCompanyPhone());
//
//        map = new HashMap<>();
//        keys = new ArrayList<>();
//        meterArrayList = new ArrayList<>();
//
//        multiTypeAdapter = new MultiTypeAdapter();
//        multiTypeAdapter.register(SaveDataMeter.class, meterViewBinder);
//        multiTypeAdapter.setItems(meterArrayList);
//        meterLoadingNumResult.setLayoutManager(new LinearLayoutManager(this));
//        meterLoadingNumResult.setAdapter(multiTypeAdapter);
//        meterLoadingNumResult.setNestedScrollingEnabled(false);
//        multiTypeAdapter.notifyDataSetChanged();
//
//        comparator = (Comparator<SaveDataMeter>) (o1, o2) -> {
//            int a = Integer.parseInt(o1.getMeterNumber());
//            int b = Integer.parseInt(o2.getMeterNumber());
//            if (a > b) {
//                return 1;
//            }
//            if (a < b) {
//                return -1;
//            }
//            return 0;
//        };
//        handler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                if (msg.what == TYPE_TTL) {
//                    Log.i("yunanhao", map.toString());
//                    setTTLConfigData();
//                } else if (msg.what == TYPE_MOD_BUS || msg.what == TYPE_485) {
//                    Log.i("yunanhao", map.toString());
//                    setMODBUS485ConfigData();
//                } else if (msg.what == TYPE_485_CONFIG) {
//                    Log.i("yunanhao", map.toString());
//                    set485ConfigData();
//                } else if (msg.what == TYPE_LSY03) {
//                    Log.i("yunanhao", map.toString());
//                    setLSY03ConfigData();
//                } else if (msg.what == FRESH) {
//                    if ("1".equalsIgnoreCase(map.get(MapParams.系统状态))) {
//                        //已激活
//                        systemStatus.setVisibility(View.VISIBLE);
//                        isActive = true;
//                        systemStatus.setImageDrawable(getResources().getDrawable(R.drawable.l_loushanyun_active_enable));
//                    } else {
//                        //未激活
//                        systemStatus.setVisibility(View.VISIBLE);
//                        isActive = false;
//                        systemStatus.setImageDrawable(getResources().getDrawable(R.drawable.l_loushanyun_active_disable));
//                    }
//                } else if (msg.what == DATA_ADD) {
//                    SaveDataMeter meterData = (SaveDataMeter) msg.obj;
//                    if (meterData != null) {
//                        meterArrayList.add(meterData);
//                        Collections.sort(meterArrayList, comparator);
//                        multiTypeAdapter.notifyDataSetChanged();
//                    }
//                } else if (msg.what == DATA_DEL) {
//                    meterArrayList.remove(msg.obj);
//                    multiTypeAdapter.notifyDataSetChanged();
//                } else if (msg.what == RSSI) {
//                    write(DataParser.CMD_RSSI_SNR, "读取信号强度");
//                }
//            }
//        };
//        connectBlueTooth();
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (v == meterLoadingNumTest) {
//            try {
//                if (softVersion >= 1.05) {
//                    if (!isJiZhongQiOpened) {
//                        sendMessageToast("请打开集中器电源");
//                        return;
//                    }
//                }
//                meterReadStart = Integer.parseInt(meterLoadingNumStart.getText().toString());
//                meterReadEnd = Integer.parseInt(meterLoadingNumEnd.getText().toString());
//                if (meterReadStart > meterReadEnd) {
//                    sendMessageToast("结束表号不可以小于起始表号");
//                    return;
//                }
//                currentNum = meterReadStart;
//                isTestMeter = true;
//                meterArrayList.clear();
//                multiTypeAdapter.notifyDataSetChanged();
//                readBiao(currentNum);
//            } catch (Exception e) {
//                sendMessageToast("表号存在错误");
//            }
//        } else if (v == connTestSend) {
//            String phone = String.valueOf(connTestPhoneNumber.getText());
//            if (XHStringUtil.isEmpty(phone, false)) {
//                sendMessageToast("请输入手机号");
//                return;
//            }
//            if (!RegexUtils.isMobileSimple(phone)) {
//                sendMessageToast("请输入正确的手机号");
//                return;
//            }
//            LoadingDialogUtil.showByEvent("正在查找sn号，请稍后……", this.getClass().getName());
//            if (!NoDoubleClickUtils.isDoubleClick()) {
//                testWebSocket();
//            }
//        } else if (v == tvBluetoothConn) {
//            if ("蓝牙重连".equals(tvBluetoothConn.getText())) {
//                sendMessageToast("蓝牙重连");
//                connectBlueTooth(sensoroDevice);
//            } else {
//                //瞬时读取
//                sendMessageToast("读取瞬时流量");
//            }
//        } else if (v == roundTextOpen) {
//            write(DataParser.openJiZhongQi(), "正在打开集中器");
//        } else if (v == roundTextClose) {
//            write(DataParser.closeJiZhongQi(), "正在关闭集中器");
//        } else if (v == tvUpload || v == tvUploadCopy) {
//            if (NoDoubleClickUtils.isDoubleClick()) {
//                return;
//            }
//            if (!isTestMeter) {
//                sendMessageToast("请先进行表号测试，测试通过后可上传");
//                return;
//            }
//            if (!isTestConn) {
//                sendMessageToast("请先进行基站数据测试，测试通过后可上传");
//                return;
//            }
//            if (isActive) {
//                sendMessageToast("请点击右上角已激活图标，设置成未激活状态，否则无法保存");
//                return;
//            }
//            LoadingDialogUtil.showByEvent(true, 5000, "上传中", loadingTag);
//            beforeUploadToCloud();
//        } else if (v == systemStatus) {
//            write(DataParser.getSystemStatusSettingCMD(!isActive), isActive ? "取消激活" : "正在激活");
//        }
//    }
//
//    @Override
//    protected void onChildNotify(byte[] results) {
//        Log.i("yunanhao", "onNotify:" + DataParser.byteToString(results));
//        byte code = (byte) (results[2] ^ ((byte) 0x80));
//        switch (code) {
//            case 0x11:
//                try {
//                    map.putAll(DataParser.getInformationAll(results));
//                    softVersion = Double.valueOf(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(map.get(MapParams.软件版本))));
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (softVersion >= 1.05) {
//                                roundTextOpen.setVisibility(View.VISIBLE);
//                                roundTextClose.setVisibility(View.VISIBLE);
//                            } else {
//                                roundTextOpen.setVisibility(View.GONE);
//                                roundTextClose.setVisibility(View.GONE);
//                            }
//                        }
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                write(DataParser.CMD_INFO_BASE, "正在读取");
//                break;
//            case 0x12:
//                if (results[3] == 0) {
//                    int rssi = ByteConvertUtils.parseByteToSignedString(results[4]);
//                    int snr = ByteConvertUtils.parseByteToSignedString(results[5]);
//                    String sb = "信号强度：" + rssi + "\n" + "信噪比：" + snr;
//                    sendMessageToast(sb);
//                }
//                break;
//            case 0x23:
//                if (results[3] == 0) {
//                    isActive = !isActive;
//                    if (isActive) {
//                        map.put(MapParams.系统状态, "1");
//                    } else {
//                        map.put(MapParams.系统状态, "0");
//                    }
//                    handler.sendEmptyMessage(FRESH);
//                } else {
//                    sendMessageToast("设置激活状态失败");
//                }
//                break;
//            case 0x27:
//                if (results[3] == 0) {
//                    sendMessageToast("强制发送成功");
//                    isTestConn = true;
//                    write(DataParser.CMD_RSSI_SNR, "读取信号强度和信噪比");
//                } else {
//                    sendMessageToast("强制发送失败(反馈)");
//                }
//                break;
//            case 0x30:
//                String v = map.get(MapParams.采集场景);
//                map.putAll(DataParser.getInformationBase(results));
//                if (v != null) {
//                    map.put(MapParams.采集场景, v);
//                }
//                write(DataParser.CMD_SYSTEM_STATUS_NORMAL, "正在读取");
//                break;
//            case 0x31:
//                map.putAll(DataParser.getDiBanBiaoJiInfo(results));
//                handler.sendEmptyMessage(type);
//                handler.sendEmptyMessage(FRESH);
//                break;
//            case 0x32:
//                SaveDataMeter item;
//                HashMap<String, String> hashMap = new HashMap<>();
//                if (results[3] == 0) {
//                    hashMap.putAll(DataParser.getDanBiaoDuQuXinXi(results, softVersion));
//                } else if (results[3] == 1) {
//                    hashMap.put(MapParams.表号, String.valueOf(currentNum));
//                } else if (results[3] == 2) {
//                    hashMap.put(MapParams.表号, String.valueOf(currentNum));
//                    hashMap.put(MapParams.HUB号, String.valueOf(results[4] & 0xff));
//                }
//                item = new SaveDataMeter(hashMap, "2");
//                if (meterArrayList.contains(item)) {
//                    meterArrayList.set(meterArrayList.indexOf(item), item);
//                } else {
//                    meterArrayList.add(item);
//                }
//                runOnUiThread(() -> {
//                    Collections.sort(meterArrayList, comparator);
//                    multiTypeAdapter.notifyDataSetChanged();
//                    if (currentNum < meterReadEnd) {
//                        currentNum++;
//                        readBiao(currentNum);
//                    } else {
//                        write(DataParser.closeJiZhongQi(), "正在关闭集中器");
//                    }
//
//                });
//                break;
//            case 0x40:
//                if (results[3] == 0) {
//                    LoadingDialogUtil.showByEvent(true, "正在读取", loadingTag);
//                }
//                break;
//            case 0x41:
//                if (results[3] == 0) {
//                    sendMessageToast("打开集中器成功");
//                    openJiZHongQiSuccess();
//                } else {
//                    sendMessageToast("打开集中器失败");
//                }
//                break;
//            case 0x42:
//                if (results[3] == 0) {
//                    sendMessageToast("关闭集中器成功");
//                    closeJiZHongQiSuccess();
//                } else {
//                    sendMessageToast("关闭集中器失败");
//                }
//                break;
//            //设置休眠
//            case 0x24:
//                if (results[3] == 0) {
//                    sendMessageToast("休眠成功");
//                } else {
//                    sendMessageToast("休眠失败");
//                }
//                break;
//            case 0x7e:
//                if (results[6] == (byte) 0x8b) {
//                    if (results[7] == 0) {
//                        map.putAll(DataParser.getMeterConfigInfo(results));
//                    }
//                    handler.sendEmptyMessage(TYPE_LSY03);
//                    int meter_code = Integer.parseInt(map.get(MapParams.表号));
//                    byte[] bytes = {(byte) 0xfe, (byte) 0xfe, (byte) 0xfe, (byte) 0x68, (byte) 0x03,
//                            (byte) 0x00, (byte) 0x0c, (byte) 0x03,
//                            (byte) (meter_code & 0xff), (byte) 0x12, (byte) 0x16};
//                    bytes[9] += bytes[8];
//                    write(bytes, "读取当前脉冲数");
//                } else if (results[6] == (byte) 0x8c) {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            String message = tvConfigInfoValue.getText().toString();
//                            int x = results[8] + (results[9] << 8) + (results[10] << 16);
//                            tvConfigValue.setText(message.replaceAll("正在读取...", String.valueOf(x)));
//                        }
//                    });
//                }
//                break;
//
//        }
//    }
//
//    private void openJiZHongQiSuccess() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                roundTextOpen.getDelegate().setBackgroundColor(Color.GRAY);
//                roundTextClose.getDelegate().setBackgroundColor(getResources().getColor(R.color.base_Q1));
//                isJiZhongQiOpened = true;
//            }
//        });
//
//
//    }
//
//    private void closeJiZHongQiSuccess() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                roundTextClose.getDelegate().setBackgroundColor(Color.GRAY);
//                roundTextOpen.getDelegate().setBackgroundColor(getResources().getColor(R.color.base_Q1));
//                isJiZhongQiOpened = false;
//            }
//        });
//
//    }
//
//    private void readBiao(int num) {
//        write(DataParser.getDanBiaoDuQuXinXiCMD(num), "正在读" + num + "号表",true, READ_BIAO_TIME_OUT);
//    }
//
//    public void testWebSocket() {
//        if (softVersion >= 1.04) {
//            String phone = String.valueOf(connTestPhoneNumber.getText());
//            String ph1 = phone.substring(0, 1);
//            String ph2 = phone.substring(1, 2);
//            String ph3 = phone.substring(2, 3);
//            String ph4 = phone.substring(3, 4);
//            String ph5 = phone.substring(4, 5);
//            String ph6 = phone.substring(5, 6);
//            String ph7 = phone.substring(6, 7);
//            String ph8 = phone.substring(7, 8);
//            String ph9 = phone.substring(8, 9);
//            String ph10 = phone.substring(9, 10);
//            String ph11 = phone.substring(10, 11);
//            byte[] d1 = new byte[16];
//            d1[0] = (byte) 0x68;
//            d1[1] = (byte) 0x0c;//有效数据
//            d1[2] = (byte) 0x27;//命令
//            d1[3] = (byte) Integer.parseInt(ph1);
//            d1[4] = (byte) Integer.parseInt(ph2);
//            d1[5] = (byte) Integer.parseInt(ph3);
//            d1[6] = (byte) Integer.parseInt(ph4);
//            d1[7] = (byte) Integer.parseInt(ph5);
//            d1[8] = (byte) Integer.parseInt(ph6);
//            d1[9] = (byte) Integer.parseInt(ph7);
//            d1[10] = (byte) Integer.parseInt(ph8);
//            d1[11] = (byte) Integer.parseInt(ph9);
//            d1[12] = (byte) Integer.parseInt(ph10);
//            d1[13] = (byte) Integer.parseInt(ph11);
//            byte cs = 0;
//            for (int i = 1; i < 14; i++) {
//                cs += d1[i];
//            }
//            d1[14] = cs;//校验和
//            d1[15] = (byte) 0x16;
//            if (sensoroDeviceSession != null) {
//                write(d1, "正在发送命令，请稍后……", true, BLUE_SEND_TIME_OUT);
//            }
//        } else {
//            try {
//                LoadingDialogUtil.show(loadingProgress, "连接服务器中，请稍后……");
//                final StringBuilder sb = new StringBuilder();
//                sb.append(URLUtils.getSocketIP());
//                sb.append("/1/");
//                sb.append(sensoroDevice.getSerialNumber());
//                URI uri = new URI(sb.toString());
//                sb.delete(0, sb.length());
//                sb.append("sn号：");
//                sb.append(sensoroDevice.getSerialNumber());
//                sb.append(" 已成功发送数据");
//                new WebSocketClient(uri, new Draft_17()) {
//
//                    @Override
//                    public void onClose(int arg0, String arg1, boolean arg2) {
//                        LoadingDialogUtil.dismissByEvent(FirstDetectionActivity.this.getClass().getName());
//                    }
//
//                    @Override
//                    public void onError(Exception arg0) {
//                        LoadingDialogUtil.dismissByEvent(FirstDetectionActivity.this.getClass().getName());
//                        sendMessageToast("服务器出错");
//                        LogUtils.e(arg0);
//                    }
//
//                    @Override
//                    public void onMessage(final String arg0) {
//                        sendMessageToast(arg0);
//                        isTestConn = true;
//                        close();
//                        handler.sendEmptyMessageDelayed(RSSI, 5000);
//                    }
//
//                    @Override
//                    public void onOpen(ServerHandshake arg0) {
//                        byte[] d = new byte[16];
//                        d[0] = (byte) 0x68;
//                        d[1] = (byte) 0x0c;//有效数据
//                        d[2] = (byte) 0x40;//命令
//                        int i = 0;
//                        String phone = String.valueOf(connTestPhoneNumber.getText());
//                        d[3] = (byte) Integer.parseInt(String.valueOf(phone.charAt(i++)));
//                        d[4] = (byte) Integer.parseInt(String.valueOf(phone.charAt(i++)));
//                        d[5] = (byte) Integer.parseInt(String.valueOf(phone.charAt(i++)));
//                        d[6] = (byte) Integer.parseInt(String.valueOf(phone.charAt(i++)));
//                        d[7] = (byte) Integer.parseInt(String.valueOf(phone.charAt(i++)));
//                        d[8] = (byte) Integer.parseInt(String.valueOf(phone.charAt(i++)));
//                        d[9] = (byte) Integer.parseInt(String.valueOf(phone.charAt(i++)));
//                        d[10] = (byte) Integer.parseInt(String.valueOf(phone.charAt(i++)));
//                        d[11] = (byte) Integer.parseInt(String.valueOf(phone.charAt(i++)));
//                        d[12] = (byte) Integer.parseInt(String.valueOf(phone.charAt(i++)));
//                        d[13] = (byte) Integer.parseInt(String.valueOf(phone.charAt(i++)));
//                        byte cs = 0;
//                        for (i = 1; i < 14; i++) {
//                            cs += d[i];
//                        }
//                        d[14] = cs;//校验和
//                        d[15] = (byte) 0x16;
//                        write(d, "正在发送命令，请稍后……", true, BLUE_SEND_TIME_OUT);
//                    }
//                }.connect();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
//
//
//    /**
//     * 获取发送频率
//     */
//    public String getFSPV(String fspv) {
//        switch (Integer.parseInt(fspv)) {
//            case 0x00:
//                return "15分钟";
//            case 0x10:
//                return "待定";
//            case 0x20:
//                return "待定";
//            case 0x30:
//                return "24小时";
//            case 0x40:
//                return "48小时";
//            case 0x50:
//                return "待定";
//            case 0x60:
//                return "待定";
//            case 0x70:
//                return "待定";
//            case 0x80:
//                return "72小时";
//            default:
//                return "待定";
//
//        }
//    }
//
//    public String getBeiLV(String bl) {
//        int l = Integer.parseInt(bl);
//        if ((l ^ 0B0000) == 0) {
//            return "0.001";
//        } else if ((l ^ 0B0001) == 0) {
//            return "0.01";
//        } else if ((l ^ 0B0010) == 0) {
//            return "0.1";
//        } else if ((l ^ 0B0011) == 0) {
//            return "1";
//        } else if ((l ^ 0B0100) == 0) {
//            return "10";
//        } else if ((l ^ 0B0101) == 0) {
//            return "100";
//        } else if ((l ^ 0B0110) == 0) {
//            return "1000";
//        } else if ((l ^ 0B0111) == 0) {
//            return "0.0001";
//        } else {
//            return "0";
//        }
//    }
//
//    @Override
//    protected void onChildWriteSuccess() {
//
//    }
//
//    @Override
//    protected void onChildWriteFailure(int i) {
//
//    }
//
//    @Override
//    protected void onChildConnectFailed(int i) {
//
//    }
//
//    @Override
//    protected void onChildConnectSuccess() {
//        sendMessageToast("蓝牙连接成功");
//        if (type == TYPE_LSY03) {
//            byte[] cmd = {(byte) 0xfe, (byte) 0xfe, (byte) 0xfe, (byte) 0x68, (byte) 0x03, (byte) 0x00, (byte) 0x0b, (byte) 0x03, (byte) 0x01, (byte) 0x12, (byte) 0x16};
//            write(cmd, "正在读取配置信息", true, 200);
//        } else {
//            write(DataParser.CMD_INFO_ALL, "正在读取");
//        }
//    }
//
//    @Override
//    protected void initEventListener() {
//        registerEventRunner(FactoryCode.GetChangJiaBiaoShiRunner, new GetChangJiaBiaoShiRunner());
//        registerEventRunner(FactoryCode.UploadDataRunner, new UploadDataRunner());
//    }
//
//    public void beforeUploadToCloud() {
//        JSONObject factoryData = new FactoryData().getFactoryData(sensoroDevice.getSerialNumber(),
//                "2",
//                "0", "1", "3",
//                "0".equals(map.get(MapParams.无线频率)) ? "433 MHz" : "470 MHz",
//                map.get(MapParams.发送功率) + "dbm",
//                sdf.format(new Date()), "0".equals(map.get(MapParams.网络交互)) ? "不带网络反馈" : "带网络反馈",
//                String.valueOf(LouShanYunUtils.getHardWareVersion(Integer.valueOf(map.get(MapParams.硬件版本)))),
//                String.valueOf(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(map.get(MapParams.软件版本)))),
//                "0",
//                map.get(MapParams.底板状态_设备强磁状态),
//                map.get(MapParams.底板状态_设备拆卸状态),
//                map.get(MapParams.底板状态_水表倒流状态),
//                "0",
//                "0",
//                map.get(MapParams.底板状态_第三方电池状态),
//                map.get(MapParams.底板状态_外接电源220V状态),
//                LouShanYunUtils.getCGXHUploadIntByValue(LouShanYunUtils.getCGXHReadStringByCode(Integer.valueOf(map.get(MapParams.传感信号)))),
//                LouShanYunUtils.getCSNRUploadIntByValue(LouShanYunUtils.getCSNRReadStringByCode(Integer.valueOf(map.get(MapParams.参数内容)))),
//                LouShanYunUtils.getCPXSUploadIntByValue(LouShanYunUtils.getCPXSReadStringByCode(Integer.valueOf(map.get(MapParams.产品形式)))),
//                LouShanYunUtils.getMKReadStringByCode(Integer.valueOf(map.get(MapParams.脉宽))), "1",
//                String.valueOf(LoginParamManager.getInstance().getProductRegister().getManufacturersIdentification()),
//                LouShanYunUtils.getDYLXUploadCodeByString(LouShanYunUtils.getDYLXReadStringByCode(Integer.valueOf(map.get(MapParams.电源类型)))),
//                LouShanYunUtils.getKPYZReadStringByCode(map.get(MapParams.扩频因子)),
//                "0".equals(map.get(MapParams.信道参数)) ? "模式A" : "模式B", 0, 0);
//        try {
//            HttpUtils utils = new HttpUtils();
//            RequestParams params = new RequestParams();
//            params.addBodyParameter("factorySetting", factoryData.toString());
//            String url = URLUtils.getIP() + URLUtils.FSFactorySetting;
//            utils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
//                // 访问成功
//                @SuppressWarnings("rawtypes")
//                @Override
//                public void onSuccess(ResponseInfo responseInfo) {
//                    String result = (String) responseInfo.result;
//                    try {
//                        int code = new JSONObject(result).optInt("code");
//                        String msg = new JSONObject(result).optString("msg");
//                        if (code == 0) {
//                            LoadingDialogUtil.showByEvent("保存中", loadingTag);
//                            uploadToCloud();
//                        } else {
//                            LoadingDialogUtil.dismissByEvent(loadingTag);
//                            sendMessageToast(msg, true);
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                // 访问失败
//                @Override
//                public void onFailure(HttpException error, String msg) {
//                    LoadingDialogUtil.dismissByEvent(loadingTag);
//                    Toast.makeText(FirstDetectionActivity.this, "访问失败，无法连接服务器", Toast.LENGTH_SHORT).show();
//                    error.printStackTrace();
//                }
//            });
//        } catch (Exception e) {
//            LoadingDialogUtil.dismissByEvent(loadingTag);
//            sendMessageToast("保存失败，数据不完整");
//        }
//    }
//
//
//    public void uploadToCloud() {
//        JSONObject factoryData = new FactoryData().getFactoryData(sensoroDevice.getSerialNumber(),
//                "2",
//                "1", "1", "3",
//                "0".equals(map.get(MapParams.无线频率)) ? "433 MHz" : "470 MHz",
//                map.get(MapParams.发送功率) + "dbm",
//                sdf.format(new Date()), "0".equals(map.get(MapParams.网络交互)) ? "不带网络反馈" : "带网络反馈",
//                String.valueOf(LouShanYunUtils.getHardWareVersion(Integer.valueOf(map.get(MapParams.硬件版本)))),
//                String.valueOf(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(map.get(MapParams.软件版本)))),
//                "0",
//                map.get(MapParams.底板状态_设备强磁状态),
//                map.get(MapParams.底板状态_设备拆卸状态),
//                map.get(MapParams.底板状态_水表倒流状态),
//                "0",
//                "0",
//                map.get(MapParams.底板状态_第三方电池状态),
//                map.get(MapParams.底板状态_外接电源220V状态),
//                LouShanYunUtils.getCGXHUploadIntByValue(LouShanYunUtils.getCGXHReadStringByCode(Integer.valueOf(map.get(MapParams.传感信号)))),
//                LouShanYunUtils.getCSNRUploadIntByValue(LouShanYunUtils.getCSNRReadStringByCode(Integer.valueOf(map.get(MapParams.参数内容)))),
//                LouShanYunUtils.getCPXSUploadIntByValue(LouShanYunUtils.getCPXSReadStringByCode(Integer.valueOf(map.get(MapParams.产品形式)))),
//                LouShanYunUtils.getMKReadStringByCode(Integer.valueOf(map.get(MapParams.脉宽))), "2",
//                String.valueOf(LoginParamManager.getInstance().getProductRegister().getManufacturersIdentification()),
//                LouShanYunUtils.getDYLXUploadCodeByString(LouShanYunUtils.getDYLXReadStringByCode(Integer.valueOf(map.get(MapParams.电源类型)))),
//                LouShanYunUtils.getKPYZReadStringByCode(map.get(MapParams.扩频因子)),
//                "0".equals(map.get(MapParams.信道参数)) ? "模式A" : "模式B", 0, 0);
//        LogUtils.i("上传的数据==" + factoryData.toString());
//        pushEvent(FactoryCode.UploadDataRunner, factoryData.toString());
//    }
//
//    @Override
//    protected void onEventRunEnd(Event event, int code) {
//        if (code == FactoryCode.GetChangJiaBiaoShiRunner) {
//            if (event.isSuccess()) {
//                String name = (String) event.getReturnParamAtIndex(0);
//                if (XHStringUtil.isEmpty(name, true)) {
//                    return;
//                }
//                String message = tvConfigValue.getText().toString();
//                tvConfigValue.setText(message.replaceAll("未注册的厂家标识", name));
//            }
//        } else if (code == FactoryCode.UploadDataRunner) {
//            int codeReturn = (int) event.getReturnParamAtIndex(0);
//            sendMessageToast((String) event.getReturnParamAtIndex(1), true);
//            write(DataParser.getSystemSleepCMD(), "正在休眠");
//        }
//    }
//
//    @Override
//    protected void onInitAttribute(BaseAttribute ba) {
//        type = getIntent().getIntExtra("type", 0);
//        ba.mActivityLayoutId = R.layout.activity_detection;
//        ba.mTitleRightImageIcon = R.drawable.dy;
//        ba.mHasRightView = true;
//        ba.mTitleText = "读取";
//    }
//
//    @Override
//    public void onRightClick(View item) {
//        super.onRightClick(item);
//        Intent intent = new Intent(this, MainActivityPrinter.class);
//        intent.putExtra(MainActivityPrinter.物联SN, "SN：" + sensoroDevice.getSerialNumber());
//        intent.putExtra(MainActivityPrinter.设备ID, "ID：01C11117C6B6C61A");
//        intent.putExtra(MainActivityPrinter.传感, "传感信号：2EV");
//        StringBuilder sb = new StringBuilder("出厂时间：");
//        String value;
//        if ((value = map.get(MapParams.出厂时间_年)) != null) {
//            sb.append("20");
//            for (int i = value.length(); i < 2; i++) {
//                sb.append(0);
//            }
//            sb.append(value);
//            sb.append("/");
//            if ((value = map.get(MapParams.出厂时间_月)) != null) {
//                for (int i = value.length(); i < 2; i++) {
//                    sb.append(0);
//                }
//                sb.append(value);
//            } else {
//                sb.append("00");
//            }
//            sb.append("/");
//            if ((value = map.get(MapParams.出厂时间_日)) != null) {
//                for (int i = value.length(); i < 2; i++) {
//                    sb.append(0);
//                }
//                sb.append(value);
//            } else {
//                sb.append("00");
//            }
//        }
//        intent.putExtra(MainActivityPrinter.出厂时间, sb.toString());
//        intent.putExtra(MainActivityPrinter.厂家名称, "贵州云通曙光技术服务有限公司");
//        startActivity(intent);
//    }
//}
