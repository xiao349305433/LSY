//package wu.loushanyun.com.fivemoduleapp.v.activity;
//
//import android.app.AlertDialog;
//import androidx.recyclerview.widget.RecyclerView;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.EditText;
//import android.widget.ScrollView;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import com.alibaba.android.arouter.facade.annotation.Route;
//import com.flyco.roundview.RoundRelativeLayout;
//import com.flyco.roundview.RoundTextView;
//import com.wu.loushanyun.base.config.K;
//import com.wu.loushanyun.base.url.URLUtils;
//import com.wu.loushanyun.base.util.DataParser;
//import com.wu.loushanyun.base.util.LouShanYunUtils;
//import com.wu.loushanyun.base.util.MapParams;
//import com.wu.loushanyun.basemvp.m.SaveDataMeter;
//import com.wu.loushanyun.basemvp.p.adapter.MeterViewBinder;
//import com.wu.loushanyun.basemvp.p.runner.GetChangJiaBiaoShiRunner;
//import com.wu.loushanyun.basemvp.v.activity.BaseBlueToothActivity;
//import com.wu.loushanyun.basemvp.v.views.DrawableTextView;
//
//import org.java_websocket.client.WebSocketClient;
//import org.java_websocket.drafts.Draft_17;
//import org.java_websocket.handshake.ServerHandshake;
//
//import java.net.URI;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import jrt.cifco.com.base.base.BaseAttribute;
//import jrt.cifco.com.base.base.dialog.LoadingDialogUtil;
//import jrt.cifco.com.base.base.multitype.MultiTypeAdapter;
//import jrt.cifco.com.base.baseevent.Event;
//import jrt.cifco.com.librarybase.some_utils.LogUtils;
//import jrt.cifco.com.librarybase.some_utils.ToastUtils;
//import jrt.cifco.com.librarybase.some_utils.XHStringUtil;
//import jrt.cifco.com.librarybase.views.dialog.MDDialog;
//import wu.loushanyun.com.fivemoduleapp.R;
//import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;
//import wu.loushanyun.com.module_initthree.init.InitCode;
//
///**
// * 该界面是1号模组读取配置的界面，
// */
//@Route(path = K.YuanChenBiaoHaoDuQuActivity)
//public class YuanChenBiaoHaoDuQuActivity extends BaseBlueToothActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
//    private NestedScrollView scrollView;
//    private RoundTextView bluetoothConn;
//    private RoundTextView bluetoothDisconn;
//    private TextView bluetoothStatus;
//    private TextView deviceName;
//    private TextView systemStatus;
//    private RoundTextView loadFactoryConfig;
//    private RoundRelativeLayout num;
//    private EditText numStart;
//    private View divide;
//    private EditText numEnd;
//    private RoundTextView numSetting;
//    private TextView kuopingyinzi;
//    private Spinner kuopingyinziSelect;
//    private RoundTextView kuopingyinziSetting;
//    private TextView xinhaoqiangdu;
//    private Spinner xinhaoqiangduSelect;
//    private RoundTextView xinhaoqiangduSetting;
//    private TextView xindaocanshu;
//    private Spinner xindaocanshuSelect;
//    private RoundTextView xindaocanshuSetting;
//    private RoundTextView loadSettingConfig;
//    private TextView paramSettingInfo;
//    private RoundRelativeLayout roundChongzhi;
//    private TextView numA;
//    private TextView numZ;
//    private RoundRelativeLayout meterReadLayout;
//    private TextView meterRead;
//    private TextView meterReadMiss;
//    private TextView accept;
//    private TextView disAccept;
//    private RecyclerView resultList;
//    private RoundTextView sendData;
//    private RoundTextView wakeup;
//    private RoundTextView location;
//    private TextView textDuqu;
//
//
//    private TextView tvDialog;
//    private EditText etInput;
//
//    private MeterViewBinder meterViewBinder;
//    private MultiTypeAdapter multiTypeAdapter;
//    //属性键值对
//    private HashMap<String, String> map = new HashMap<>();
//    private int kuopinyinziData = 9, xinhaoqiangduData = 18, xindaocanshuData;
//    //读取失败的表号
//    private List<SaveDataMeter> faillist = new ArrayList<>();
//    //执行漏抄
//    private boolean isFailDo;
//    private boolean isLoadInfo;
//
//    //读表用 存储变量
//    private int start, end, current;
//    private String rssi, snr, kpyz, xd, meter_a, meter_b;
//    private MDDialog mdDialog;
//    private AlertDialog alertDialog;
//    private ArrayList<SaveDataMeter> meterArrayList;
//    private String factoryName = "";
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
//    private WebSocketClient client;
//    private boolean sendJiZhan = false;
//    private int acceptNum=0;
//
//    @Override
//    protected void initEventListener() {
//        registerEventRunner(InitCode.GetChangJiaBiaoShiRunner, new GetChangJiaBiaoShiRunner());
//    }
//
//    @Override
//    protected void initLifeCycle() {
//        super.initLifeCycle();
//        registerLifeCycle(meterViewBinder);
//    }
//
//    @Override
//    protected void onEventRunEnd(Event event, int code) {
//        if (code == InitCode.GetChangJiaBiaoShiRunner) {
//            if (event.isSuccess()) {
//                String name = (String) event.getReturnParamAtIndex(0);
//                if (XHStringUtil.isEmpty(name, true)) {
//                    return;
//                }
//                String message = tvDialog.getText().toString();
//                factoryName = name;
//                tvDialog.setText(message.replaceAll("厂家标识：- ", "厂家标识：" + name));
//            }
//        }
//    }
//
//    @Override
//    protected void onInitAttribute(BaseAttribute ba) {
//        ba.mActivityLayoutId = R.layout.m_five_activity_yuancheng_duqu;
//        ba.mTitleText = "远传表号接入-读取";
//    }
//
//
//    @Override
//    protected void initView() {
//        super.initView();
//        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
//        bluetoothConn = (RoundTextView) findViewById(R.id.bluetooth_conn);
//        bluetoothDisconn = (RoundTextView) findViewById(R.id.bluetooth_disconn);
//        bluetoothStatus = (TextView) findViewById(R.id.bluetooth_status);
//        deviceName = (TextView) findViewById(R.id.device_name);
//        systemStatus = (TextView) findViewById(R.id.system_status);
//        loadFactoryConfig = (RoundTextView) findViewById(R.id.load_factory_config);
//        num = (RoundRelativeLayout) findViewById(R.id.num);
//        numStart = (EditText) findViewById(R.id.num_start);
//        divide = (View) findViewById(R.id.divide);
//        numEnd = (EditText) findViewById(R.id.num_end);
//        numSetting = (RoundTextView) findViewById(R.id.num_setting);
//        kuopingyinzi = (TextView) findViewById(R.id.kuopingyinzi);
//        kuopingyinziSelect = (Spinner) findViewById(R.id.kuopingyinzi_select);
//        kuopingyinziSetting = (RoundTextView) findViewById(R.id.kuopingyinzi_setting);
//        xinhaoqiangdu = (TextView) findViewById(R.id.xinhaoqiangdu);
//        xinhaoqiangduSelect = (Spinner) findViewById(R.id.xinhaoqiangdu_select);
//        xinhaoqiangduSetting = (RoundTextView) findViewById(R.id.xinhaoqiangdu_setting);
//        xindaocanshu = (TextView) findViewById(R.id.xindaocanshu);
//        xindaocanshuSelect = (Spinner) findViewById(R.id.xindaocanshu_select);
//        xindaocanshuSetting = (RoundTextView) findViewById(R.id.xindaocanshu_setting);
//        loadSettingConfig = (RoundTextView) findViewById(R.id.load_setting_config);
//        paramSettingInfo = (TextView) findViewById(R.id.param_setting_info);
//        roundChongzhi = (RoundRelativeLayout) findViewById(R.id.round_chongzhi);
//        numA = (TextView) findViewById(R.id.num_a);
//        numZ = (TextView) findViewById(R.id.num_z);
//        meterReadLayout = (RoundRelativeLayout) findViewById(R.id.meter_read_layout);
//        meterRead = (TextView) findViewById(R.id.meter_read);
//        meterReadMiss = (TextView) findViewById(R.id.meter_read_miss);
//        accept = (TextView) findViewById(R.id.accept);
//        disAccept = (TextView) findViewById(R.id.dis_accept);
//        resultList = (RecyclerView) findViewById(R.id.result_list);
//        sendData = (RoundTextView) findViewById(R.id.send_data);
//        wakeup = (RoundTextView) findViewById(R.id.wakeup);
//        location = (RoundTextView) findViewById(R.id.location);
//        textDuqu = (TextView) findViewById(R.id.text_duqu);
//
//
//
//        View view = getLayoutInflater().inflate(R.layout.m_five_textview_dialog, null);
//        mdDialog = new MDDialog.Builder(this)
//                .setTitle("出厂信息")
//                .setContentView(view)
//                .setShowNegativeButton(false)
//                .create();
//        tvDialog = (TextView) view.findViewById(R.id.tv_dialog);
//        tvDialog.setText("物联SN：\n" +
//                "使用类型：\n" +
//                "产品形式：\n" +
//                "传感信号：\n" +
//                "无线频率：\n" +
//                "发送频率：\n" +
//                "出厂时间：\n" +
//                "扩频因子：\n" +
//                "信道参数：\n" +
//                "电源类型：\n" +
//                "物联电池：\n" +
//                "通讯状态：\n" +
//                "厂家标识：- \n" +
//                "硬件版本号：\n" +
//                "软件版本号：");
//        View view1 = getLayoutInflater().inflate(R.layout.m_five_test_upload_dialog, null);
//        etInput = (EditText) view1.findViewById(R.id.input);
//        etInput.setText(LoginFiveParamManager.getInstance().getLoginData().getRegisterPhone());
//        view1.findViewById(R.id.send).setOnClickListener(this::onClick);
//        alertDialog = new AlertDialog.Builder(this)
//                .setView(view1)
//                .create();
//
//        deviceName.setText(sensoroDevice.getSerialNumber());
//        meterArrayList = new ArrayList<>();
//        multiTypeAdapter = new MultiTypeAdapter(meterArrayList);
//        meterViewBinder = new MeterViewBinder(this);
//        multiTypeAdapter.register(SaveDataMeter.class, meterViewBinder);
//        resultList.setLayoutManager(new LinearLayoutManager(this));
//        resultList.setAdapter(multiTypeAdapter);
//        resultList.setNestedScrollingEnabled(false);
//        multiTypeAdapter.notifyDataSetChanged();
//
//        bluetoothConn.setOnClickListener(this::onClick);
//        bluetoothDisconn.setOnClickListener(this::onClick);
//        loadFactoryConfig.setOnClickListener(this::onClick);
//        numSetting.setOnClickListener(this::onClick);
//        kuopingyinziSetting.setOnClickListener(this::onClick);
//        xinhaoqiangduSetting.setOnClickListener(this::onClick);
//        xindaocanshuSetting.setOnClickListener(this::onClick);
//        loadSettingConfig.setOnClickListener(this::onClick);
//        meterRead.setOnClickListener(this::onClick);
//        meterReadMiss.setOnClickListener(this::onClick);
//        roundChongzhi.setOnClickListener(this::onClick);
//        sendData.setOnClickListener(this);
//        wakeup.setOnClickListener(this);
//        location.setOnClickListener(this);
//        kuopingyinziSelect.setOnItemSelectedListener(this);
//        xinhaoqiangduSelect.setOnItemSelectedListener(this);
//        xindaocanshuSelect.setOnItemSelectedListener(this);
//        xinhaoqiangduSelect.setSelection(0);
//        kuopingyinziSelect.setSelection(3);
//
//        connectBlueTooth(sensoroDevice);
//    }
//
//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        if (id == R.id.bluetooth_conn) {
//            //蓝牙连接
//            if (bluetoothStatus.getText().toString().equals("未连接")) {
//                if (sensoroDeviceSession != null) {
//                    connectBlueTooth(sensoroDevice);
//                }
//            } else {
//                sendMessageToast("蓝牙已连接");
//            }
//        } else if (id == R.id.bluetooth_disconn) {
//            //蓝牙断开
//            sensoroDeviceSession.disconnect();
//            bluetoothStatus.setText("未连接");
//            map = null;
//        } else if (id == R.id.load_factory_config) {
//            //出厂设置信息的读取
//            if ("未连接".equalsIgnoreCase(bluetoothStatus.getText().toString())) {
//                sendMessageToast("设备未连接或者未读取到设备信息");
//                return;
//            }
//            write(DataParser.CMD_INFO_BASE, "正在读取出厂设置信息");
//        } else if (id == R.id.num_setting) {
//            if (map == null || map.size() == 0 || "未连接".equalsIgnoreCase(bluetoothStatus.getText().toString())) {
//                sendMessageToast("设备未连接或者未读取到设备信息");
//                return;
//            }
//            meter_a = numStart.getText().toString();
//            meter_b = numEnd.getText().toString();
//            try {
//                start = Integer.parseInt(meter_a);
//                end = Integer.parseInt(meter_b);
//            } catch (Exception e) {
//                sendMessageToast("输入表号不正确");
//                return;
//            }
//            meterRead.setEnabled(true);
//            meterReadLayout.getDelegate().setBackgroundColor(getResources().getColor(R.color.l_five_Q));
//            meterArrayList.clear();
//            multiTypeAdapter.notifyDataSetChanged();
//            faillist.clear();
//            accept.setText("0");
//            disAccept.setText("");
//            if (map != null) {
//                //起末表号的设置
//                HashMap<String, String> parmas = new HashMap<>();
//                parmas.put(MapParams.总线起止表号_起, meter_a);
//                parmas.put(MapParams.总线起止表号_止, meter_b);
//                parmas.put(MapParams.仪表通信号, "0");
//                parmas.put(MapParams.初始化表计状态, "0");
//                parmas.put(MapParams.倍率, "0");
//                parmas.put(MapParams.安装脉冲底数, "0");
//                parmas.put(MapParams.口径, "0");
//                parmas.put(MapParams.发送频率, "48");
//                parmas.put(MapParams.保留字节, "0");
//                parmas.put(MapParams.设备ID, map.get(MapParams.设备ID));
//                write(DataParser.getDiBanBiaoJiChuShiHuaCMD(parmas), "设置表号");
//            }
//        } else if (id == R.id.kuopingyinzi_setting) {
//            if (map == null || map.size() == 0 || "未连接".equalsIgnoreCase(bluetoothStatus.getText().toString())) {
//                sendMessageToast("设备未连接或者未读取到设备信息");
//                return;
//            }
//            //扩频因子的设置
//            kuopinyinziData = LouShanYunUtils.getKPYZWriteCodeByString(getResources().getStringArray(R.array.m_init_kuopinyinzi)[kuopingyinziSelect.getSelectedItemPosition()]);
//            byte[] d = {0x68, 0x02, 0x07, (byte) kuopinyinziData, (byte) (0x09 + (byte) kuopinyinziData), 0x16};
//            write(d, "设置扩频因子");
//        } else if (id == R.id.xinhaoqiangdu_setting) {
//            if (map == null || map.size() == 0 || "未连接".equalsIgnoreCase(bluetoothStatus.getText().toString())) {
//                sendMessageToast("设备未连接或者未读取到设备信息");
//                return;
//            }
//            //信号强度的设置
//            xinhaoqiangduData = Integer.parseInt(getResources().getStringArray(
//                    R.array.m_init_xinhaoqiangdu)[xinhaoqiangduSelect
//                    .getSelectedItemPosition()].replaceAll("dbm", ""));
//            byte[] d = {0x68, 0x02, 0x02, (byte) xinhaoqiangduData, (byte) (0x04 + (byte) xinhaoqiangduData), 0x16};
//            write(d, "设置发送功率");
//        } else if (id == R.id.xindaocanshu_setting) {
//            if (map == null || map.size() == 0 || "未连接".equalsIgnoreCase(bluetoothStatus.getText().toString())) {
//                sendMessageToast("设备未连接或者未读取到设备信息");
//                return;
//            }
//            //信道参数的设置
//            if ("模式A".equalsIgnoreCase(getResources().getStringArray(R.array.m_init_xindaocanshu)[xindaocanshuSelect.getSelectedItemPosition()])) {
//                xindaocanshuData = 0;
//                byte[] d = {0x68, 0x02, 0x08, 0x00, 0x0a, 0x16};//模式A
//                write(d, "设置模式A");
//            } else {
//                xindaocanshuData = 1;
//                byte[] d = {0x68, 0x02, 0x08, 0x01, 0x0b, 0x16};//模式B
//                write(d, "设置模式B");
//            }
//        } else if (id == R.id.load_setting_config) {
//            if (map == null || map.size() == 0 || "未连接".equalsIgnoreCase(bluetoothStatus.getText().toString())) {
//                sendMessageToast("设备未连接或者未读取到设备信息");
//                return;
//            }
//            //参数设置信息的读取
//            loadSettingConfig();
//        } else if (id == R.id.round_chongzhi) {
//            meterRead.setEnabled(true);
//            meterReadLayout.getDelegate().setBackgroundColor(getResources().getColor(R.color.l_five_Q));
//            meterArrayList.clear();
//            multiTypeAdapter.notifyDataSetChanged();
//            faillist.clear();
//            accept.setText("0");
//            disAccept.setText("");
//        } else if (id == R.id.meter_read) {
//            if (map == null || map.size() == 0 || "未连接".equalsIgnoreCase(bluetoothStatus.getText().toString())) {
//                sendMessageToast("设备未连接或者未读取到设备信息");
//                return;
//            }
//            //读表
//            meterRead.setEnabled(false);
//            meterReadLayout.getDelegate().setBackgroundColor(getResources().getColor(R.color.base_global_neutral_gray_c6c9d4));
//            meterArrayList.clear();
//            multiTypeAdapter.notifyDataSetChanged();
//            try {
//                start = Integer.parseInt(meter_a);
//                end = Integer.parseInt(meter_b);
//                current = start;
//                faillist.clear();
//                accept.setText("0");
//                acceptNum=0;
//                disAccept.setText(String.valueOf(end));
//                readBiao(current);
//            } catch (Exception e) {
//                sendMessageToast("输入表号不正确");
//            }
//        } else if (id == R.id.meter_read_miss) {
//            if (map == null || map.size() == 0 || "未连接".equalsIgnoreCase(bluetoothStatus.getText().toString())) {
//                sendMessageToast("设备未连接或者未读取到设备信息");
//                return;
//            }
//            //漏表
//            if (faillist.size() > 0) {
//                isFailDo = true;
//                start = 0;
//                current = start;
//                end = faillist.size() - 1;
//                readBiao(Integer.parseInt(faillist.get(current).getMeterNumber()));
//            } else {
//                sendMessageToast("没有漏表，不需要漏抄");
//            }
//        } else if (id == R.id.send_data) {
//            if (map == null || map.size() == 0 || "未连接".equalsIgnoreCase(bluetoothStatus.getText().toString())) {
//                sendMessageToast("设备未连接或者未读取到设备信息");
//                return;
//            }
//            //模拟数据上送测试
//            alertDialog.show();
//        } else if (id == R.id.location) {
//        } else if (id == R.id.wakeup) {
//            if (map == null || map.size() == 0 || "未连接".equalsIgnoreCase(bluetoothStatus.getText().toString())) {
//                sendMessageToast("请先连接设备");
//                return;
//            }
//            //激活
//            if ("未激活".equalsIgnoreCase(systemStatus.getText().toString())) {
//                byte[] b = {0x68, 0x02, 0x23, 0x01, 0x26, 0x16};
//                write(b, "激活中");
//            } else {
//                sendMessageToast("设备已被激活");
//            }
//        } else if (id == R.id.send) {
//            etInput.getText();
//            alertDialog.dismiss();
//            try {
//                LoadingDialogUtil.showByEvent(true, 5000, loadingTag, "连接服务器中，请稍后……");
//                String url = URLUtils.getSocketIP() + "/1/" + sensoroDevice.getSerialNumber().toUpperCase();
//                if (client != null) {
//                    client.close();
//                    client = null;
//                }
//                client = new WebSocketClient(new URI(url), new Draft_17()) {
//
//                    @Override
//                    public void onClose(int arg0, String arg1, boolean arg2) {
//                        LoadingDialogUtil.dismissByEvent(this.getClass().getName());
//                    }
//
//                    @Override
//                    public void onError(Exception arg0) {
//                        LoadingDialogUtil.dismissByEvent(this.getClass().getName());
//                        sendMessageToast("服务器出错");
//                        LogUtils.e(arg0);
//                    }
//
//                    @Override
//                    public void onMessage(final String arg0) {
//                        LoadingDialogUtil.dismissByEvent(this.getClass().getName());
//                        sendMessageToast(arg0);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                sendData.getDelegate().setBackgroundColor(getResources().getColor(R.color.m_five_gray));
//                                sendData.setClickable(false);
//                                sendJiZhan = true;
//                                client.close();
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onOpen(ServerHandshake arg0) {
//                        String phone = etInput.getText().toString().trim();
//                        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
//                        Pattern p = Pattern.compile(regex);
//                        Matcher m = p.matcher(phone);
//                        boolean isMatch = m.matches();
//                        if (isMatch) {
//                            String ph1 = phone.substring(0, 1);
//                            String ph2 = phone.substring(1, 2);
//                            String ph3 = phone.substring(2, 3);
//                            String ph4 = phone.substring(3, 4);
//                            String ph5 = phone.substring(4, 5);
//                            String ph6 = phone.substring(5, 6);
//                            String ph7 = phone.substring(6, 7);
//                            String ph8 = phone.substring(7, 8);
//                            String ph9 = phone.substring(8, 9);
//                            String ph10 = phone.substring(9, 10);
//                            String ph11 = phone.substring(10, 11);
//                            byte[] d = new byte[16];
//                            d[0] = (byte) 0x68;
//                            d[1] = (byte) 0x0c;//有效数据
//                            d[2] = (byte) 0x40;//命令
//                            d[3] = (byte) Integer.parseInt(ph1);
//                            d[4] = (byte) Integer.parseInt(ph2);
//                            d[5] = (byte) Integer.parseInt(ph3);
//                            d[6] = (byte) Integer.parseInt(ph4);
//                            d[7] = (byte) Integer.parseInt(ph5);
//                            d[8] = (byte) Integer.parseInt(ph6);
//                            d[9] = (byte) Integer.parseInt(ph7);
//                            d[10] = (byte) Integer.parseInt(ph8);
//                            d[11] = (byte) Integer.parseInt(ph9);
//                            d[12] = (byte) Integer.parseInt(ph10);
//                            d[13] = (byte) Integer.parseInt(ph11);
//                            byte cs = 0;
//                            for (int i = 1; i < 14; i++) {
//                                cs += d[i];
//                            }
//                            d[14] = cs;//校验和
//                            d[15] = (byte) 0x16;
//                            if (sensoroDeviceSession != null) {
//                                write(d, "正在发送命令，请稍后……");
//                            }
//                        } else {
//                            sendMessageToast("请输入正确的手机号");
//                        }
//                    }
//                };
//                client.connect();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void loadSettingConfig() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("表号：");
//        sb.append(map.get(MapParams.总线起止表号_起));
//        sb.append("-");
//        sb.append(map.get(MapParams.总线起止表号_止));
//        sb.append(" \n信号强度(Rssi):");
//        sb.append(map.get(MapParams.信号强度));
//        sb.append("\n");
//        sb.append(" 信噪比(Snr):");
//        sb.append(map.get(MapParams.信噪比));
//        paramSettingInfo.setText(sb.toString());
//    }
//
//    @Override
//    public void onChildNotify(byte[] results) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                textDuqu.setVisibility(View.GONE);
//            }
//        });
//        Log.i("yunanhao", "onNotify:" + DataParser.byteToString(results));
//        byte code = (byte) (results[2] ^ ((byte) 0x80));
//        if (Byte.compare(code, (byte) 0x30) == 0) {
//            map.putAll(DataParser.getInformationBase(results));
//            //一次性读取模块相关的所有信息
//            write(DataParser.CMD_INFO_ALL, "正在读取出厂设置信息");
//        } else if (Byte.compare(code, (byte) 0x11) == 0) {
//            try {
//                map.putAll(DataParser.getInformationAll(results));
//
//                String v = LouShanYunUtils.getDianYuanLeiXin(map.get(MapParams.电源类型));
//                String dianchizhuantai = ("0".equalsIgnoreCase(map.get(MapParams.底板状态_自备电池状态)) ? "正常" : "异常");
//                if (!"物联电池".equalsIgnoreCase(v)) {
//                    dianchizhuantai = ("0".equalsIgnoreCase(map.get(MapParams.底板状态_外接电源220V状态)) ? "正常" : "异常");
//                }
//                tvDialog.setText("物联SN：" + map.get(MapParams.物联SN) + "\n" +
//                        "使用类型：" + LouShanYunUtils.getCJCJReadStringByCode(map.get(MapParams.采集场景)) + "\n" +
//                        "产品形式：" + LouShanYunUtils.getCPXSReadStringByCode(map.get(MapParams.产品形式)) + "\n" +
//                        "传感信号：" + LouShanYunUtils.getCGXHReadStringByCode(Long.parseLong(map.get(MapParams.传感信号))) + "\n" +
//                        "无线频率：" + LouShanYunUtils.getWuXianPinLv(map.get(MapParams.无线频率)) + "\n" +
//                        "发送频率：" + LouShanYunUtils.getFSPLReadStringByCode(Long.parseLong(map.get(MapParams.发送频率))) + "\n" +
//                        "发送功率：" + map.get(MapParams.发送功率) + "dbm" + "\n" +
//                        "出厂时间：20" + map.get(MapParams.出厂时间_年) + "-" +
//                        map.get(MapParams.出厂时间_月) + "-" +
//                        map.get(MapParams.出厂时间_日) + " 00:00:00" + "\n" +
//                        "扩频因子：" + LouShanYunUtils.getKPYZReadStringByCode(map.get(MapParams.扩频因子)) + "\n" +
//                        "信道参数：" + ("0".equalsIgnoreCase(map.get(MapParams.信道参数)) ? "信道A" : "信道B") + "\n" +
//                        "电源类型：" + v + "\n" +
//                        v + "：" + dianchizhuantai + "\n" +
//                        "通讯状态：" + "正常" + "\n" +
//                        "厂家标识：- " + "\n" +
//                        "硬件版本号：" + LouShanYunUtils.getHardWareVersion(Integer.valueOf(map.get(MapParams.硬件版本))) + "\n" +
//                        "软件版本号：" + LouShanYunUtils.getSoftWareVersion(Integer.valueOf(map.get(MapParams.软件版本))));
//                kpyz = map.get(MapParams.扩频因子);//读取当前扩频因子
//                pushEvent(InitCode.GetChangJiaBiaoShiRunner, map.get(MapParams.厂家标识));
//                //读取当前信道参数
//                xd = "0".equalsIgnoreCase(map.get(MapParams.信道参数)) ? "模式A" : "模式B";
//                Log.i("yunanhao", map.toString());
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (!YuanChenBiaoHaoDuQuActivity.this.isDestroyed()) {
//                            mdDialog.show();
//                            isLoadInfo = true;
//                        }
//
//                    }
//                });
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else if (Byte.compare(code, (byte) 0x08) == 0) {
//            sendMessageToast("设置信道成功");
//            map.put(MapParams.信道参数, String.valueOf(xindaocanshuData));
//        } else if (Byte.compare(code, (byte) 0x02) == 0) {
//            sendMessageToast("设置发送功率成功");
//        } else if (Byte.compare(code, (byte) 0x07) == 0) {
//            sendMessageToast("设置扩频因子成功");
//            map.put(MapParams.扩频因子, String.valueOf(kuopinyinziData));
//        } else if (Byte.compare(code, (byte) 0x21) == 0) {
//            sendMessageToast("设置起止抄表号成功");
//            map.put(MapParams.总线起止表号_起, meter_a);
//            map.put(MapParams.总线起止表号_止, meter_b);
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    numA.setText(meter_a);
//                    numZ.setText(meter_b);
//                }
//            });
//        } else if (Byte.compare(code, (byte) 0x12) == 0) {
//            //读取当前信号强度 RSSI SNR
//            rssi = String.valueOf(results[4]);
//            snr = String.valueOf(results[5]);
//            map.put(MapParams.信号强度, rssi);
//            map.put(MapParams.信噪比, snr);
//            sendMessageToast("读取信息成功");
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    loadSettingConfig();
//                }
//            });
//        } else if (Byte.compare(code, (byte) 0x31) == 0) {
//            //起止表号
//            map.putAll(DataParser.getDiBanBiaoJiInfo(results));
//            meter_a = map.get(MapParams.总线起止表号_起);
//            meter_b = map.get(MapParams.总线起止表号_止);
//            if ("1".equalsIgnoreCase(map.get(MapParams.系统状态))) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        numA.setText(meter_a);
//                        numZ.setText(meter_b);
//                        systemStatus.setText("已激活");
//                        wakeup.getDelegate().setBackgroundColor(0x26000000);
////                        loadSettingConfig();
//                    }
//                });
//            } else {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        numA.setText(meter_a);
//                        numZ.setText(meter_b);
//                        systemStatus.setText("未激活");
////                        loadSettingConfig();
//                    }
//                });
//            }
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    write(DataParser.CMD_RSSI_SNR, "读取信号强度和信噪比");
//                }
//            });
//        } else if (Byte.compare(code, (byte) 0x32) == 0) {
//            Log.i("yunanhao", "读表成功" + current);
//            loadMeter(results);
//        } else if (Byte.compare(code, (byte) 0x23) == 0) {
//            sendMessageToast("激活成功");
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    systemStatus.setText("已激活");
//                    wakeup.getDelegate().setBackgroundColor(0x26000000);
//                }
//            });
//        } else if (Byte.compare(code, (byte) 0x40) == 0) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    sendMessageToast("发送命令成功");
//                    if (alertDialog.isShowing()) {
//                        alertDialog.dismiss();
//                    }
//                }
//            });
//        }
//    }
//
//    @Override
//    protected void onChildConnectFailed(int i) {
//        sendMessageToast("连接失败");
//    }
//
//    @Override
//    protected void onChildConnectSuccess() {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                bluetoothStatus.setText("已连接");
//                write(new byte[]{0x68, 0x01, 0x31, 0x32, 0x16}, "正在读取激活状态请稍后");
//            }
//        });
//    }
//
//    @Override
//    protected void onChildWriteSuccess() {
//        Log.i("yunanhao", "onWriteSuccess");
//    }
//
//    @Override
//    protected void onChildWriteFailure(int i) {
//        Log.i("yunanhao", "onWriteFailure" + i);
//    }
//
//    private void loadMeter(byte[] results) {
//        SaveDataMeter item = new SaveDataMeter();
//        item.setParamOrUnit("2");
//        if (results[3] == 0) {
//            HashMap<String, String> result = DataParser.getDanBiaoDuQuXinXi(results,);
//            Log.i("yunanhao", result.toString());
//            item.setMeterNumber(result.get(MapParams.表号));
//            item.setUserId(result.get(MapParams.设备ID));
//            item.setImpulseInitial(result.get(MapParams.脉冲数));
//            item.setDisassemblyState(result.get(MapParams.表拆卸状态));
//            item.setPowerState(result.get(MapParams.表电池状态));
//            item.setFlowDirection(result.get(MapParams.表流向状态));
//            item.setMagneticDisturb(result.get(MapParams.表强磁状态));
//            try {
//                item.setPulseConstant(String.valueOf(1.0 / Double.valueOf(LouShanYunUtils.getBLReadStringByCode(Long.valueOf(result.get(MapParams.倍率))))));
//            } catch (Exception e) {
//                Log.e("yunanhao", e.getMessage());
//            }
//            item.setZhenDuanMa(result.get(MapParams.汇中表诊断码));
//            acceptNum++;
//        } else {
//            if (current < faillist.size()) {
//                item.setMeterNumber(faillist.get(current).getMeterNumber());
//            } else {
//                item.setMeterNumber(String.valueOf(current));
//            }
//            faillist.add(item);
//        }
//        runOnUiThread(() -> {
//            if (meterArrayList.contains(item)) {
//                meterArrayList.set(meterArrayList.indexOf(item), item);
//            } else {
//                meterArrayList.add(item);
//            }
//            Collections.sort(meterArrayList, comparator);
//            multiTypeAdapter.notifyDataSetChanged();
//            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
//            accept.setText(String.valueOf(acceptNum));
//            disAccept.setText(String.valueOf(Integer.parseInt(meter_b) - acceptNum));
//            if (isFailDo) {
//                if (++current <= end) {
//                    readBiao(Integer.parseInt(faillist.get(current).getMeterNumber()));
//                } else {
//                    if (current == faillist.size()) {
//                        faillist.clear();
//                    } else {
//                        for (int i = end; i >= 0; i--) {
//                            faillist.remove(i);
//                        }
//                    }
//                    isFailDo = false;
//                }
//            } else {
//                if (++current <= end) {
//                    readBiao(current);
//                }
//            }
//        });
//    }
//    private void readBiao(int num) {
//        String str = getRepeadStr(meterArrayList);
//        if (!XHStringUtil.isEmpty(str, false)) {
//            ToastUtils.showLong(str);
//        } else {
//            write(DataParser.getDanBiaoDuQuXinXiCMD(num), "正在读" + num + "号表");
//        }
//    }
//
//    private String getRepeadStr(ArrayList<SaveDataMeter> meterArrayList) {
//        StringBuffer result = new StringBuffer();
//        ArrayList<SaveDataMeter> array = new ArrayList<>();
//        for (int i = 0; i < meterArrayList.size(); i++) {
//            SaveDataMeter out = meterArrayList.get(i);
//            for (int j = i + 1; j < meterArrayList.size(); j++) {
//                SaveDataMeter inside = meterArrayList.get(j);
//                if (!XHStringUtil.isEmpty(out.getUserId(), false) && !XHStringUtil.isEmpty(inside.getUserId(), false)) {
//                    if (i != j && out.getUserId().equals(inside.getUserId())) {
//                        if (!array.contains(out)) {
//                            array.add(out);
//                        }
//                        array.add(inside);
//                        continue;
//                    }
//                }
//            }
//            if (array.size() != 0) {
//                break;
//            }
//        }
//        if (array.size() != 0) {
//            for (int i = 0; i < array.size(); i++) {
//                SaveDataMeter saveDataMeter = array.get(i);
//                if (i == array.size() - 1) {
//                    result.append(saveDataMeter.getMeterNumber() + "号表的id重复了，都是" + saveDataMeter.getUserId());
//                } else {
//                    result.append(saveDataMeter.getMeterNumber() + "号表,");
//                }
//            }
//        }
//        return result.toString();
//    }
//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }
//}
