package wu.loushanyun.com.fivemoduleapp.v.activity;

import android.app.AlertDialog;
import android.graphics.Color;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundLinearLayout;
import com.flyco.roundview.RoundRelativeLayout;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.url.URLUtils;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.m.SaveDataMeter;
import com.wu.loushanyun.basemvp.p.adapter.MeterViewBinder;
import com.wu.loushanyun.basemvp.p.runner.GetChangJiaBiaoShiRunner;
import com.wu.loushanyun.basemvp.v.activity.BaseBlueToothActivity;
import com.yanzhenjie.nohttp.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.litepal.LitePal;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.base.rx.RxSchedulers;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ByteConvertUtils;
import met.hx.com.librarybase.some_utils.LogUtils;
import met.hx.com.librarybase.some_utils.TimeUtils;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.fivemoduleapp.R;
import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;
import wu.loushanyun.com.libraryfive.m.RefreshMainRepair;
import wu.loushanyun.com.libraryfive.m.RepairUpdateData;
import wu.loushanyun.com.libraryfive.m.SaveDataConverter;
import wu.loushanyun.com.libraryfive.m.YuanChuanSaveData;
import wu.loushanyun.com.module_initthree.init.InitCode;

/**
 * 该界面是1号模组初始化的界面，有2种方式
 * 1.从远传表号接入》...》附近新蓝牙设备》该界面 不带oldSN 最终跳转的界面是 LocationActivity
 * 2.从服务》检修》替换远传表号接》该界面 带oldSN 最终跳转的界面是 LocationDetailActivity
 * 3.带有isOnlyRead只进行读取检查，把保存和设置隐藏
 */
@Route(path = K.YuanChenBiaoHaoActivity)
public class YuanChenBiaoHaoActivity extends BaseBlueToothActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, MeterViewBinder.OnZhiShuListener {


    private NestedScrollView scrollView;
    private RoundTextView bluetoothConn;
    private RoundTextView bluetoothDisconn;
    private TextView bluetoothStatus;
    private TextView deviceName;
    private ImageView systemStatus;
    private RoundTextView loadFactoryConfig;
    private TextView tvDialog;
    private RoundRelativeLayout num;
    private EditText numStart;
    private View divide;
    private EditText numEnd;
    private RoundTextView numSetting;
    private RoundLinearLayout layoutTime;
    private RoundTextView roundTextSetTime;
    private TextView textTime;
    private RoundRelativeLayout roundChongzhi;
    private SwitchCompat switchCheckedId;
    private RoundTextView roundTextOpen;
    private RoundTextView roundTextClose;
    private TextView numA;
    private TextView numZ;
    private RoundRelativeLayout meterReadLayout;
    private TextView meterRead;
    private TextView meterReadMiss;
    private TextView accept;
    private TextView disAccept;
    private TextView meterMiss;
    private RecyclerView resultList;
    private RoundLinearLayout linearParamSetting;
    private TextView kuopingyinzi;
    private Spinner kuopingyinziSelect;
    private RoundTextView kuopingyinziSetting;
    private TextView xinhaoqiangdu;
    private Spinner xinhaoqiangduSelect;
    private RoundTextView xinhaoqiangduSetting;
    private TextView xindaocanshu;
    private Spinner xindaocanshuSelect;
    private RoundTextView xindaocanshuSetting;
    private TextView paramSettingInfo;
    private RoundTextView sendData;
    private RoundTextView location;
    private RoundTextView roundTextXiumian;

    private EditText etInput;

    //属性键值对
    private HashMap<String, String> map;
    private int kuopinyinziData = 9, xinhaoqiangduData = 18, xindaocanshuData;
    private AlertDialog alertDialog;
    private String factoryName = "";

    private int meterStart;
    private int meterEnd;
    private int meterReadStart;
    private int meterReadEnd;
    private int currentNum;
    private int currentMissIndex;
    private double softVersion;
    private boolean isReadMiss = false;
    private boolean scrollToBottom = true;
    Comparator comparator = (Comparator<SaveDataMeter>) (o1, o2) -> {
        int a = Integer.parseInt(o1.getMeterNumber());
        int b = Integer.parseInt(o2.getMeterNumber());
        if (a > b) {
            return 1;
        }
        if (a < b) {
            return -1;
        }
        return 0;
    };
    private ArrayList<SaveDataMeter> meterArrayList;
    private ArrayList<SaveDataMeter> meterMissArrayList;
    private MeterViewBinder meterViewBinder;
    private MultiTypeAdapter multiTypeAdapter;
    private int meterMisssNum = 0;

    private WebSocketClient client;
    private boolean sendJiZhan = false;
    private String oldSn;
    private boolean isLoadInfo;
    private boolean isSleep = false;
    private boolean isOnlyRead = false;
    private boolean hasSetTime = false;
    private SaveDataMeter resetSaveDataMeter;
    private int typeJiHuo;
    private int typeMoZu;
    private boolean isJiZhongQiOpened = false;
    private boolean isSave = false;

    @Override
    protected void initEventListener() {
        registerEventRunner(InitCode.GetChangJiaBiaoShiRunner, new GetChangJiaBiaoShiRunner());
    }

    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        meterViewBinder = new MeterViewBinder(this);
        registerLifeCycle(meterViewBinder);
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == InitCode.GetChangJiaBiaoShiRunner) {
            if (event.isSuccess()) {
                String name = (String) event.getReturnParamAtIndex(0);
                if (XHStringUtil.isEmpty(name, true)) {
                    return;
                }
                String message = tvDialog.getText().toString();
                factoryName = name;
                tvDialog.setText(message.replaceAll("厂家标识：- ", "厂家标识：" + name));
            }
        }
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        getAllData();
        ba.mActivityLayoutId = R.layout.m_five_activity_yuancheng1;
        ba.mTitleText = "远传表号接入";
    }

    private void getAllData() {
        oldSn = getIntent().getStringExtra("oldSn");
        isOnlyRead = getIntent().getBooleanExtra("isOnlyRead", false);//是否是只读取检查
    }

    @Override
    protected void initView() {
        super.initView();
        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        bluetoothConn = (RoundTextView) findViewById(R.id.bluetooth_conn);
        bluetoothDisconn = (RoundTextView) findViewById(R.id.bluetooth_disconn);
        bluetoothStatus = (TextView) findViewById(R.id.bluetooth_status);
        deviceName = (TextView) findViewById(R.id.device_name);
        systemStatus = (ImageView) findViewById(R.id.system_status);
        loadFactoryConfig = (RoundTextView) findViewById(R.id.load_factory_config);
        tvDialog = (TextView) findViewById(R.id.tv_dialog);
        num = (RoundRelativeLayout) findViewById(R.id.num);
        numStart = (EditText) findViewById(R.id.num_start);
        divide = (View) findViewById(R.id.divide);
        numEnd = (EditText) findViewById(R.id.num_end);
        numSetting = (RoundTextView) findViewById(R.id.num_setting);
        layoutTime = (RoundLinearLayout) findViewById(R.id.layout_time);
        roundTextSetTime = (RoundTextView) findViewById(R.id.round_text_set_time);
        textTime = (TextView) findViewById(R.id.text_time);
        roundChongzhi = (RoundRelativeLayout) findViewById(R.id.round_chongzhi);
        switchCheckedId = (SwitchCompat) findViewById(R.id.switch_checked_id);
        roundTextOpen = (RoundTextView) findViewById(R.id.round_text_open);
        roundTextClose = (RoundTextView) findViewById(R.id.round_text_close);
        numA = (TextView) findViewById(R.id.num_a);
        numZ = (TextView) findViewById(R.id.num_z);
        meterReadLayout = (RoundRelativeLayout) findViewById(R.id.meter_read_layout);
        meterRead = (TextView) findViewById(R.id.meter_read);
        meterReadMiss = (TextView) findViewById(R.id.meter_read_miss);
        accept = (TextView) findViewById(R.id.accept);
        disAccept = (TextView) findViewById(R.id.dis_accept);
        meterMiss = (TextView) findViewById(R.id.meter_miss);
        resultList = (RecyclerView) findViewById(R.id.result_list);
        linearParamSetting = (RoundLinearLayout) findViewById(R.id.linear_param_setting);
        kuopingyinzi = (TextView) findViewById(R.id.kuopingyinzi);
        kuopingyinziSelect = (Spinner) findViewById(R.id.kuopingyinzi_select);
        kuopingyinziSetting = (RoundTextView) findViewById(R.id.kuopingyinzi_setting);
        xinhaoqiangdu = (TextView) findViewById(R.id.xinhaoqiangdu);
        xinhaoqiangduSelect = (Spinner) findViewById(R.id.xinhaoqiangdu_select);
        xinhaoqiangduSetting = (RoundTextView) findViewById(R.id.xinhaoqiangdu_setting);
        xindaocanshu = (TextView) findViewById(R.id.xindaocanshu);
        xindaocanshuSelect = (Spinner) findViewById(R.id.xindaocanshu_select);
        xindaocanshuSetting = (RoundTextView) findViewById(R.id.xindaocanshu_setting);
        paramSettingInfo = (TextView) findViewById(R.id.param_setting_info);
        sendData = (RoundTextView) findViewById(R.id.send_data);
        location = (RoundTextView) findViewById(R.id.location);
        roundTextXiumian = (RoundTextView) findViewById(R.id.round_text_xiumian);


        kuopingyinziSelect.setSelection(3);
        if (isOnlyRead) {
            linearParamSetting.setVisibility(View.GONE);
            location.setVisibility(View.GONE);
        } else {
            linearParamSetting.setVisibility(View.VISIBLE);
            location.setVisibility(View.VISIBLE);
        }
        map = new HashMap<>();
        if (!XHStringUtil.isEmpty(oldSn, false)) {
            location.setText("保存");
        }
        View view1 = getLayoutInflater().inflate(R.layout.m_five_test_upload_dialog, null);
        etInput = (EditText) view1.findViewById(R.id.input);
        etInput.setText(LoginFiveParamManager.getInstance().getLoginData().getRegisterPhone());
        view1.findViewById(R.id.send).setOnClickListener(this::onClick);
        alertDialog = new AlertDialog.Builder(this)
                .setView(view1)
                .create();

        deviceName.setText(sensoroDevice.getSerialNumber());
        meterArrayList = new ArrayList<>();
        meterMissArrayList = new ArrayList<>();

        multiTypeAdapter = new MultiTypeAdapter(meterArrayList);
        multiTypeAdapter.register(SaveDataMeter.class, meterViewBinder);
        resultList.setLayoutManager(new LinearLayoutManager(this));
        resultList.setAdapter(multiTypeAdapter);
        resultList.setNestedScrollingEnabled(false);
        multiTypeAdapter.notifyDataSetChanged();

        bluetoothConn.setOnClickListener(this::onBlueClick);
        bluetoothDisconn.setOnClickListener(this::onBlueClick);
        loadFactoryConfig.setOnClickListener(this::onClick);
        numSetting.setOnClickListener(this::onClick);
        kuopingyinziSetting.setOnClickListener(this::onClick);
        xinhaoqiangduSetting.setOnClickListener(this::onClick);
        xindaocanshuSetting.setOnClickListener(this::onClick);
        meterRead.setOnClickListener(this::onClick);
        meterReadMiss.setOnClickListener(this::onClick);
        roundChongzhi.setOnClickListener(this::onClick);
        roundTextSetTime.setOnClickListener(this::onClick);
        roundTextOpen.setOnClickListener(this::onClick);
        roundTextClose.setOnClickListener(this::onClick);
        systemStatus.setOnClickListener(this::onClick);
        roundTextXiumian.setOnClickListener(this::OnBlueToothClick);
        sendData.setOnClickListener(this);
        location.setOnClickListener(this);
        kuopingyinziSelect.setOnItemSelectedListener(this);
        xinhaoqiangduSelect.setOnItemSelectedListener(this);
        xindaocanshuSelect.setOnItemSelectedListener(this);
        xinhaoqiangduSelect.setSelection(0);
        kuopingyinziSelect.setSelection(3);

        connectBlueTooth(sensoroDevice);
    }

    private void OnBlueToothClick(View view) {
        if (!isConnect()) {
            sendMessageToast("未连接蓝牙");
            return;
        }
        switch (view.getId()) {
            case R.id.round_text_xiumian:
                write(DataParser.getSystemSleepCMD(), "正在休眠");
                break;
        }
    }

    private boolean isConnect() {
        if (bluetoothStatus.getText().toString().equals("未连接")) {
            return false;
        } else {
            return true;
        }
    }

    private void onBlueClick(View view) {
        int id = view.getId();
        if (id == R.id.bluetooth_conn) {
            //蓝牙连接
            if (bluetoothStatus.getText().toString().equals("未连接")) {
                if (sensoroDeviceSession != null) {
                    connectBlueTooth(sensoroDevice);
                }
            } else {
                sendMessageToast("蓝牙已连接");
            }
        } else if (id == R.id.bluetooth_disconn) {
            //蓝牙断开
            sensoroDeviceSession.disconnect();
            bluetoothStatus.setText("未连接");
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (typeMoZu != 1) {
            ToastUtils.showShort("识别不是1号模组");
            return;
        }
        if (id == R.id.load_setting_config) {
            if (map == null || map.size() == 0 || "未连接".equalsIgnoreCase(bluetoothStatus.getText().toString())) {
                sendMessageToast("设备未连接或者未读取到设备信息");
                return;
            }
            //参数设置信息的读取
            write(new byte[]{0x68, 0x01, 0x31, 0x32, 0x16}, "正在读取参数信息请稍后");
        } else if (id == R.id.round_chongzhi) {
            meterArrayList.clear();
            meterMissArrayList.clear();
            meterMisssNum = 0;
            accept.setText(String.valueOf(meterArrayList.size()));
            disAccept.setText(String.valueOf(meterReadEnd - meterArrayList.size()));
            meterMiss.setText(String.valueOf(meterMisssNum));
            meterRead.setEnabled(true);
            meterReadLayout.getDelegate().setBackgroundColor(getResources().getColor(R.color.l_five_Q));
            multiTypeAdapter.notifyDataSetChanged();
        } else if (id == R.id.load_factory_config) {
            //出厂设置信息的读取
            if ("未连接".equalsIgnoreCase(bluetoothStatus.getText().toString())) {
                sendMessageToast("设备未连接或者未读取到设备信息");
                return;
            }
            write(DataParser.CMD_INFO_ALL, "正在读取出厂设置信息");
        } else if (id == R.id.round_text_open) {
            write(DataParser.openJiZhongQi(), "正在打开集中器");
        } else if (id == R.id.round_text_close) {
            write(DataParser.closeJiZhongQi(), "正在关闭集中器");
        } else if (id == R.id.num_setting) {
            if (map == null || map.size() == 0 || "未连接".equalsIgnoreCase(bluetoothStatus.getText().toString())) {
                sendMessageToast("设备未连接或者未读取到设备信息");
                return;
            }
            if (XHStringUtil.isEmpty(numStart.getText().toString(), false)) {
                sendMessageToast("请输入起表号");
                return;
            }
            if (XHStringUtil.isEmpty(numEnd.getText().toString(), false)) {
                sendMessageToast("请输入止表号");
                return;
            }
            meterStart = Integer.valueOf(numStart.getText().toString());
            meterEnd = Integer.valueOf(numEnd.getText().toString());
            if (map != null) {
                //起末表号的设置
                HashMap<String, String> parmas = new HashMap<>();
                parmas.put(MapParams.总线起止表号_起, String.valueOf(meterStart));
                parmas.put(MapParams.总线起止表号_止, String.valueOf(meterEnd));
                parmas.put(MapParams.仪表通信号, "0");
                parmas.put(MapParams.初始化表计状态, "0");
                parmas.put(MapParams.倍率, "0");
                parmas.put(MapParams.安装脉冲底数, "0");
                parmas.put(MapParams.口径, "0");
                parmas.put(MapParams.发送频率, "48");
                parmas.put(MapParams.保留字节, "0");
                parmas.put(MapParams.设备ID, map.get(MapParams.设备ID));
                write(DataParser.getDiBanBiaoJiChuShiHuaCMD(parmas), "设置表号");
            }
        } else if (id == R.id.kuopingyinzi_setting) {
            if (map == null || map.size() == 0 || "未连接".equalsIgnoreCase(bluetoothStatus.getText().toString())) {
                sendMessageToast("设备未连接或者未读取到设备信息");
                return;
            }
            //扩频因子的设置
            kuopinyinziData = LouShanYunUtils.getKPYZWriteCodeByString(getResources().getStringArray(R.array.m_init_kuopinyinzi)[kuopingyinziSelect.getSelectedItemPosition()]);
            byte[] d = {0x68, 0x02, 0x07, (byte) kuopinyinziData, (byte) (0x09 + (byte) kuopinyinziData), 0x16};
            write(d, "设置扩频因子");
        } else if (id == R.id.xinhaoqiangdu_setting) {
            if (map == null || map.size() == 0 || "未连接".equalsIgnoreCase(bluetoothStatus.getText().toString())) {
                sendMessageToast("设备未连接或者未读取到设备信息");
                return;
            }
            //信号强度的设置
            xinhaoqiangduData = Integer.parseInt(getResources().getStringArray(
                    R.array.m_init_xinhaoqiangdu)[xinhaoqiangduSelect
                    .getSelectedItemPosition()].replaceAll("dbm", ""));
            if (softVersion >= 1.03) {
                write(DataParser.setFaSongGongLv(xinhaoqiangduData), "正在设置发送功率");
            } else {
                if (xinhaoqiangduData == 20) {
                    write(DataParser.setFaSongGongLv(14), "正在设置发送功率");
                } else if (xinhaoqiangduData == 18) {
                    write(DataParser.setFaSongGongLv(12), "正在设置发送功率");
                } else if (xinhaoqiangduData == 16) {
                    write(DataParser.setFaSongGongLv(10), "正在设置发送功率");
                }
            }
        } else if (id == R.id.xindaocanshu_setting) {
            if (map == null || map.size() == 0 || "未连接".equalsIgnoreCase(bluetoothStatus.getText().toString())) {
                sendMessageToast("设备未连接或者未读取到设备信息");
                return;
            }
            //信道参数的设置
            if ("模式A".equalsIgnoreCase(getResources().getStringArray(R.array.m_init_xindaocanshu)[xindaocanshuSelect.getSelectedItemPosition()])) {
                xindaocanshuData = 0;
                byte[] d = {0x68, 0x02, 0x08, 0x00, 0x0a, 0x16};//模式A
                write(d, "设置模式A");
            } else {
                xindaocanshuData = 1;
                byte[] d = {0x68, 0x02, 0x08, 0x01, 0x0b, 0x16};//模式B
                write(d, "设置模式B");
            }
        } else if (id == R.id.meter_read) {
            if (map == null || map.size() == 0 || "未连接".equalsIgnoreCase(bluetoothStatus.getText().toString())) {
                sendMessageToast("设备未连接或者未读取到设备信息");
                return;
            }
            //读表
            if (!isJiZhongQiOpened) {
                sendMessageToast("请打开集中器");
                return;
            }
            //读表
            meterRead.setEnabled(false);
            meterReadLayout.getDelegate().setBackgroundColor(getResources().getColor(R.color.base_global_neutral_gray_c6c9d4));
            currentNum = meterReadStart;
            meterArrayList.clear();
            meterMissArrayList.clear();
            multiTypeAdapter.notifyDataSetChanged();
            isReadMiss = false;
            meterMisssNum = 0;
            accept.setText(String.valueOf(meterArrayList.size()));
            disAccept.setText(String.valueOf(meterReadEnd - meterArrayList.size()));
            meterMiss.setText(String.valueOf(meterMisssNum));
            readBiao(currentNum);
            scrollToBottom = true;
        } else if (id == R.id.meter_read_miss) {
            if (map == null || map.size() == 0 || "未连接".equalsIgnoreCase(bluetoothStatus.getText().toString())) {
                sendMessageToast("设备未连接或者未读取到设备信息");
                return;
            }
            //读表
            if (!isJiZhongQiOpened) {
                sendMessageToast("请打开集中器");
                return;
            }
            if (meterMissArrayList.size() == 0) {
                sendMessageToast("没有漏抄的表");
                return;
            }
            isReadMiss = true;
            currentMissIndex = 0;
            currentNum = Integer.valueOf(meterMissArrayList.get(currentMissIndex).getMeterNumber());
            readBiao(currentNum);
            scrollToBottom = false;
        } else if (id == R.id.text__round_louchao) {
            if (map == null || map.size() == 0 || "未连接".equalsIgnoreCase(bluetoothStatus.getText().toString())) {
                sendMessageToast("设备未连接或者未读取到设备信息");
                return;
            }
            if (meterMissArrayList.size() == 0) {
                sendMessageToast("没有漏抄的表");
                return;
            }
            isReadMiss = true;
            currentMissIndex = 0;
            currentNum = Integer.valueOf(meterMissArrayList.get(currentMissIndex).getMeterNumber());
            readBiao(currentNum);
            scrollToBottom = false;
        } else if (id == R.id.send_data) {
            if (map == null || map.size() == 0 || "未连接".equalsIgnoreCase(bluetoothStatus.getText().toString())) {
                sendMessageToast("设备未连接或者未读取到设备信息");
                return;
            }
            //模拟数据上送测试
            alertDialog.show();
        } else if (id == R.id.round_text_set_time) {
            write(DataParser.getByteForSettingTime(), "正在设置时间");
        } else if (id == R.id.system_status) {
            if (map == null || map.size() == 0 || "未连接".equalsIgnoreCase(bluetoothStatus.getText().toString())) {
                sendMessageToast("请先连接设备");
                return;
            }
            isSave = false;
            if (typeJiHuo == 1) {
                write(DataParser.getSystemStatusSettingCMD(false), "关闭激活中");
            } else if (typeJiHuo == 0) {
                write(DataParser.getSystemStatusSettingCMD(true), "开启激活中");
            }
        } else if (id == R.id.location) {
            if (!switchCheckedId.isChecked()) {
                sendMessageToast("请打开抄表按钮进行抄表，可过滤重复id");
                return;
            }
            if (map == null || map.size() == 0 || "未连接".equalsIgnoreCase(bluetoothStatus.getText().toString())) {
                sendMessageToast("设备未连接或者未读取到设备信息");
                return;
            }
            if (meterMisssNum != 0) {
                sendMessageToast("存在漏抄的表，无法上传");
                return;
            }
            //保存
            if (!sendJiZhan) {
                sendMessageToast("请进行强制发送");
                return;
            }
            if (!isLoadInfo) {
                sendMessageToast("请读取出厂设置信息");
                return;
            }
            if (softVersion >= 1.04) {
                if (!hasSetTime) {
                    sendMessageToast("请校准时间");
                    return;
                }
            }
            if (meterArrayList.size() == 0) {
                sendMessageToast("该设备未读取总线表单元信息，请读取之后再保存");
                return;
            }
            isSave = true;
            typeJiHuo = 0;
            write(DataParser.getSystemStatusSettingCMD(true), "开启激活中");

        } else if (id == R.id.send) {
            etInput.getText();
            alertDialog.dismiss();
            if (softVersion >= 1.04) {
                String phone = etInput.getText().toString().trim();
                String ph1 = phone.substring(0, 1);
                String ph2 = phone.substring(1, 2);
                String ph3 = phone.substring(2, 3);
                String ph4 = phone.substring(3, 4);
                String ph5 = phone.substring(4, 5);
                String ph6 = phone.substring(5, 6);
                String ph7 = phone.substring(6, 7);
                String ph8 = phone.substring(7, 8);
                String ph9 = phone.substring(8, 9);
                String ph10 = phone.substring(9, 10);
                String ph11 = phone.substring(10, 11);
                byte[] d1 = new byte[16];
                d1[0] = (byte) 0x68;
                d1[1] = (byte) 0x0c;//有效数据
                d1[2] = (byte) 0x27;//命令
                d1[3] = (byte) Integer.parseInt(ph1);
                d1[4] = (byte) Integer.parseInt(ph2);
                d1[5] = (byte) Integer.parseInt(ph3);
                d1[6] = (byte) Integer.parseInt(ph4);
                d1[7] = (byte) Integer.parseInt(ph5);
                d1[8] = (byte) Integer.parseInt(ph6);
                d1[9] = (byte) Integer.parseInt(ph7);
                d1[10] = (byte) Integer.parseInt(ph8);
                d1[11] = (byte) Integer.parseInt(ph9);
                d1[12] = (byte) Integer.parseInt(ph10);
                d1[13] = (byte) Integer.parseInt(ph11);
                byte cs = 0;
                for (int i = 1; i < 14; i++) {
                    cs += d1[i];
                }
                d1[14] = cs;//校验和
                d1[15] = (byte) 0x16;
                if (sensoroDeviceSession != null) {
                    write(d1, "正在强制发送，请稍后……", true, 20000);
                }
            } else {
                try {
                    LoadingDialogUtil.showByEvent(true, 5000, loadingTag, "连接服务器中，请稍后……");
                    String url = URLUtils.getSocketIP() + "/1/" + sensoroDevice.getSerialNumber().toUpperCase();
                    if (client != null) {
                        client.close();
                        client = null;
                    }
                    client = new WebSocketClient(new URI(url), new Draft_17()) {

                        @Override
                        public void onClose(int arg0, String arg1, boolean arg2) {
                            LoadingDialogUtil.dismissByEvent(this.getClass().getName());
                        }

                        @Override
                        public void onError(Exception arg0) {
                            LoadingDialogUtil.dismissByEvent(this.getClass().getName());
                            sendMessageToast("服务器出错，网络状况不佳，已跳过该步骤");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    sendData.getDelegate().setBackgroundColor(getResources().getColor(R.color.m_five_gray));
                                    sendData.setClickable(false);
                                    sendJiZhan = true;
                                    client.close();
                                }
                            });
                            LogUtils.e(arg0);
                        }

                        @Override
                        public void onMessage(final String arg0) {
                            LoadingDialogUtil.dismissByEvent(this.getClass().getName());
                            sendMessageToast(arg0);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    sendData.getDelegate().setBackgroundColor(getResources().getColor(R.color.m_five_gray));
                                    sendData.setClickable(false);
                                    sendJiZhan = true;
                                    client.close();
                                }
                            });
                        }

                        @Override
                        public void onOpen(ServerHandshake arg0) {
                            String phone = etInput.getText().toString().trim();
                            String ph1 = phone.substring(0, 1);
                            String ph2 = phone.substring(1, 2);
                            String ph3 = phone.substring(2, 3);
                            String ph4 = phone.substring(3, 4);
                            String ph5 = phone.substring(4, 5);
                            String ph6 = phone.substring(5, 6);
                            String ph7 = phone.substring(6, 7);
                            String ph8 = phone.substring(7, 8);
                            String ph9 = phone.substring(8, 9);
                            String ph10 = phone.substring(9, 10);
                            String ph11 = phone.substring(10, 11);
                            byte[] d = new byte[16];
                            d[0] = (byte) 0x68;
                            d[1] = (byte) 0x0c;//有效数据
                            d[2] = (byte) 0x40;//命令
                            d[3] = (byte) Integer.parseInt(ph1);
                            d[4] = (byte) Integer.parseInt(ph2);
                            d[5] = (byte) Integer.parseInt(ph3);
                            d[6] = (byte) Integer.parseInt(ph4);
                            d[7] = (byte) Integer.parseInt(ph5);
                            d[8] = (byte) Integer.parseInt(ph6);
                            d[9] = (byte) Integer.parseInt(ph7);
                            d[10] = (byte) Integer.parseInt(ph8);
                            d[11] = (byte) Integer.parseInt(ph9);
                            d[12] = (byte) Integer.parseInt(ph10);
                            d[13] = (byte) Integer.parseInt(ph11);
                            byte cs = 0;
                            for (int i = 1; i < 14; i++) {
                                cs += d[i];
                            }
                            d[14] = cs;//校验和
                            d[15] = (byte) 0x16;
                            if (sensoroDeviceSession != null) {
                                write(d, "正在发送命令，请稍后……", true, BLUE_SEND_TIME_OUT);
                            }
                        }
                    };
                    client.connect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void save() {
        YuanChuanSaveData yuanChuanSaveData = new YuanChuanSaveData();
        yuanChuanSaveData.setSn(map.get(MapParams.物联SN).toUpperCase());

        yuanChuanSaveData.setProductForm("" + LouShanYunUtils.getCPXSUploadIntByValue(LouShanYunUtils.getCPXSReadStringByCode(map.get(MapParams.产品形式))));
        yuanChuanSaveData.setManufacturersIdentification(map.get(MapParams.厂家标识));
        yuanChuanSaveData.setGatherScene("" + LouShanYunUtils.getCJCJUploadCodeByString(LouShanYunUtils.getCJCJReadStringByCode(map.get(MapParams.采集场景))));
        yuanChuanSaveData.setFactoryName(factoryName);
        SaveDataConverter saveDataConverter = new SaveDataConverter();
        saveDataConverter.setStartMeterNumber(map.get(MapParams.总线起止表号_起));
        saveDataConverter.setFrequency(LouShanYunUtils.getFSPLReadStringByCode(Long.parseLong(map.get(MapParams.发送频率))).replaceAll("上传一次", ""));
        saveDataConverter.setEndMeterNumber(map.get(MapParams.总线起止表号_止));
        saveDataConverter.setSoftVersion(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(map.get(MapParams.软件版本))));
        saveDataConverter.setHardwareVersion(LouShanYunUtils.getHardWareVersion(Integer.valueOf(map.get(MapParams.硬件版本))));
        saveDataConverter.setEquipmentPower("0");
        saveDataConverter.setPowerType("" + LouShanYunUtils.getDYLXUploadCodeByString(LouShanYunUtils.getDianYuanLeiXin(map.get(MapParams.电源类型))));
        saveDataConverter.setPowerState(map.get(MapParams.底板状态_自备电池状态));
        saveDataConverter.setSensingSignal("" + LouShanYunUtils.getCGXHUploadIntByValue(LouShanYunUtils.getCGXHReadStringByCode(Long.valueOf(map.get(MapParams.传感信号)))));
        saveDataConverter.setEquipmentTime("20" + map.get(MapParams.出厂时间_年) + "-" + (map.get(MapParams.出厂时间_月).length() == 1 ? "0" + map.get(MapParams.出厂时间_月) : map.get(MapParams.出厂时间_月)) + "-" + (map.get(MapParams.出厂时间_日).length() == 1 ? "0" + map.get(MapParams.出厂时间_日) : map.get(MapParams.出厂时间_日)));
        saveDataConverter.setEquipmentType("0");
        saveDataConverter.setPulseConstant("0");
        saveDataConverter.setRssi(map.get(MapParams.信号强度));
        saveDataConverter.setSnr(map.get(MapParams.信噪比));
        saveDataConverter.setSendingPower(map.get(MapParams.发送功率) + "dbm");
        saveDataConverter.setSf(LouShanYunUtils.getKPYZReadStringByCode(map.get(MapParams.扩频因子)));
        saveDataConverter.setChannel("0".equalsIgnoreCase(map.get(MapParams.信道参数)) ? "模式A" : "模式B");
        Gson gson = new Gson();
        yuanChuanSaveData.setJsonSaveDataConverter(gson.toJson(saveDataConverter));
        yuanChuanSaveData.setJsonMeter(gson.toJson(meterArrayList));
        yuanChuanSaveData.setAttrMapJson(DataParser.getJSONObject(map));
        yuanChuanSaveData.setLoginId(String.valueOf(LoginFiveParamManager.getInstance().getLoginData().getId()));
        yuanChuanSaveData.setBusinessLicense(String.valueOf(LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getBusinessLicense()));
        yuanChuanSaveData.setTime(TimeUtils.getCurTimeString());

        if (!XHStringUtil.isEmpty(oldSn, false)) {
            saveUpdateData(gson.toJson(yuanChuanSaveData));
        } else {
            saveUpdateDataNew(yuanChuanSaveData);
        }
        Logger.i("传过去的数据==" + yuanChuanSaveData.toString());
    }

    private void openJiZHongQiSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                roundTextOpen.getDelegate().setBackgroundColor(Color.GRAY);
                roundTextClose.getDelegate().setBackgroundColor(getResources().getColor(R.color.l_loushanyun_Q));
                isJiZhongQiOpened = true;
            }
        });


    }

    private void closeJiZHongQiSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                roundTextClose.getDelegate().setBackgroundColor(Color.GRAY);
                roundTextOpen.getDelegate().setBackgroundColor(getResources().getColor(R.color.l_loushanyun_Q));
                isJiZhongQiOpened = false;
            }
        });

    }

    private void saveUpdateDataNew(YuanChuanSaveData yuanChuanSaveData) {
        compositeDisposable.add(Flowable.create(
                (FlowableOnSubscribe<Boolean>) emitter ->
                {
                    LoadingDialogUtil.showByEvent("正在保存", this.getClass().getName());
                    long id = 0;
                    int loginid = LoginFiveParamManager.getInstance().getLoginData().getId();
                    List<YuanChuanSaveData> arrayList = LitePal.where("sn = ? and loginid = ?", yuanChuanSaveData.getSn(), loginid + "")
                            .find(YuanChuanSaveData.class);
                    if (arrayList.size() > 0) {
                        id = arrayList.get(0).getBaseObjId();
                    }
                    if (id == 0) {
                        Logger.i("只保存=" + id);
                        yuanChuanSaveData.assignBaseObjId(0);
                        yuanChuanSaveData.saveAsync().listen(success -> {
                            if (success) {
                                emitter.onNext(true);
                            } else {
                                emitter.onNext(false);
                            }

                        });
                    } else {
                        Logger.i("删除然后保存=" + id);
                        LitePal.delete(YuanChuanSaveData.class, id);
                        yuanChuanSaveData.assignBaseObjId(0);
                        yuanChuanSaveData.saveAsync().listen(success -> {
                            if (success) {
                                emitter.onNext(true);
                            } else {
                                emitter.onNext(false);
                            }
                        });
                    }
                    LoadingDialogUtil.dismissByEvent(this.getClass().getName());
                }
                , BackpressureStrategy.ERROR)
                .compose(RxSchedulers.io_main())
                .subscribe(success -> {
                    if (success) {
                        sendMessageToast("保存成功，请到网络好的地方同步");
                        write(DataParser.getSystemSleepCMD(), "正在休眠");
                    } else {
                        sendMessageToast("保存失败");
                    }
                }));
    }


    private String getRepeadStr(ArrayList<SaveDataMeter> meterArrayList) {
        StringBuffer result = new StringBuffer();
        ArrayList<SaveDataMeter> array = new ArrayList<>();
        for (int i = 0; i < meterArrayList.size(); i++) {
            SaveDataMeter out = meterArrayList.get(i);
            for (int j = i + 1; j < meterArrayList.size(); j++) {
                SaveDataMeter inside = meterArrayList.get(j);
                if (!XHStringUtil.isEmpty(out.getUserId(), false) && !XHStringUtil.isEmpty(inside.getUserId(), false)) {
                    if (i != j && out.getUserId().equals(inside.getUserId())) {
                        if (!array.contains(out)) {
                            array.add(out);
                        }
                        array.add(inside);
                        continue;
                    }
                }
            }
            if (array.size() != 0) {
                break;
            }
        }
        if (array.size() != 0) {
            for (int i = 0; i < array.size(); i++) {
                SaveDataMeter saveDataMeter = array.get(i);
                if (i == array.size() - 1) {
                    result.append(saveDataMeter.getMeterNumber() + "号表的id重复了，都是" + saveDataMeter.getUserId());
                } else {
                    result.append(saveDataMeter.getMeterNumber() + "号表,");
                }
            }
        }
        return result.toString();
    }


    /**
     * 保存更换的数据到本地
     *
     * @param json
     */
    private void saveUpdateData(String json) {
//TODO
        compositeDisposable.add(Flowable.create(
                (FlowableOnSubscribe<Boolean>) emitter ->
                {
                    LoadingDialogUtil.showByEvent("正在保存", this.getClass().getName());
                    long id = 0;
                    int loginid = LoginFiveParamManager.getInstance().getLoginData().getId();
                    RepairUpdateData repairUpdateData = new RepairUpdateData(sensoroDevice.getSerialNumber(), loginid + "", oldSn, "远传表号接入", json);
                    List<RepairUpdateData> arrayList = LitePal.where("oldSn = ? and loginid = ?", oldSn, loginid + "")
                            .find(RepairUpdateData.class);
                    XLog.i("数据==" + arrayList.toString());
                    if (arrayList.size() > 0) {
                        id = arrayList.get(0).getBaseObjId();
                    }
                    if (id == 0) {
                        repairUpdateData.assignBaseObjId(0);
                        repairUpdateData.saveAsync().listen(success -> {
                            if (success) {
                                emitter.onNext(true);
                            } else {
                                emitter.onNext(false);
                            }

                        });
                    } else {
                        LitePal.delete(RepairUpdateData.class, id);
                        repairUpdateData.assignBaseObjId(0);
                        repairUpdateData.saveAsync().listen(success -> {
                            if (success) {
                                emitter.onNext(true);
                            } else {
                                emitter.onNext(false);
                            }
                        });
                    }
                    LoadingDialogUtil.dismissByEvent(this.getClass().getName());
                }
                , BackpressureStrategy.ERROR)
                .compose(RxSchedulers.io_main())
                .subscribe(success -> {
                    if (success) {
                        sendMessageToast("保存成功，请到网络好的地方同步");
                        write(DataParser.getSystemSleepCMD(), "正在休眠");
                        EventBus.getDefault().post(new RefreshMainRepair());
                    } else {
                        sendMessageToast("保存失败");
                    }
                }));
    }

    private void loadSettingConfig() {
        StringBuilder sb = new StringBuilder();
        sb.append("表号:    ");
        sb.append(map.get(MapParams.总线起止表号_起));
        sb.append("-");
        sb.append(map.get(MapParams.总线起止表号_止));
        if (!XHStringUtil.isEmpty(map.get(MapParams.信号强度), false)) {
            sb.append("\n信号强度(Rssi):    ");
            sb.append(map.get(MapParams.信号强度));
        }
        if (!XHStringUtil.isEmpty(map.get(MapParams.信噪比), false)) {
            sb.append("\n信噪比(Snr):    ");
            sb.append(map.get(MapParams.信噪比));
        }
        if (!XHStringUtil.isEmpty(map.get(MapParams.信道参数), false)) {
            sb.append("\n");
            sb.append("信道参数:    ");
            sb.append(("0".equalsIgnoreCase(map.get(MapParams.信道参数)) ? "信道A" : "信道B"));
        }
        if (!XHStringUtil.isEmpty(map.get(MapParams.扩频因子), false)) {
            sb.append("\n");
            sb.append("扩频因子:    ");
            sb.append(LouShanYunUtils.getKPYZReadStringByCode(map.get(MapParams.扩频因子)));
        }
        if (!XHStringUtil.isEmpty(map.get(MapParams.发送功率), false)) {
            sb.append("\n");
            sb.append("发送功率:    ");
            sb.append(map.get(MapParams.发送功率) + "dbm");
        }
        runOnUiThread(() -> paramSettingInfo.setText(sb.toString()));
    }


    private void refreshText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String v = LouShanYunUtils.getDianYuanLeiXin(map.get(MapParams.电源类型));
                String dianchizhuantai = ("0".equalsIgnoreCase(map.get(MapParams.底板状态_自备电池状态)) ? "正常" : "异常");
                if (!"物联电池".equalsIgnoreCase(v)) {
                    dianchizhuantai = ("0".equalsIgnoreCase(map.get(MapParams.底板状态_外接电源220V状态)) ? "正常" : "异常");
                }
                tvDialog.setText("物联SN：" + map.get(MapParams.物联SN) + "\n" +
                        "使用类型：" + LouShanYunUtils.getCJCJReadStringByCode(map.get(MapParams.采集场景)) + "\n" +
                        "产品形式：" + LouShanYunUtils.getCPXSReadStringByCode(map.get(MapParams.产品形式)) + "\n" +
                        "传感信号：" + LouShanYunUtils.getCGXHReadStringByCode(Long.parseLong(map.get(MapParams.传感信号))) + "\n" +
                        "无线频率：" + LouShanYunUtils.getWuXianPinLv(map.get(MapParams.无线频率)) + "\n" +
                        "发送频率：" + LouShanYunUtils.getFSPLReadStringByCode(Long.parseLong(map.get(MapParams.发送频率))) + "\n" +
                        "发送功率：" + map.get(MapParams.发送功率) + "dbm" + "\n" +
                        "出厂时间：20" + map.get(MapParams.出厂时间_年) + "-" +
                        map.get(MapParams.出厂时间_月) + "-" +
                        map.get(MapParams.出厂时间_日) + " 00:00:00" + "\n" +
                        "扩频因子：" + LouShanYunUtils.getKPYZReadStringByCode(map.get(MapParams.扩频因子)) + "\n" +
                        "信道参数：" + ("0".equalsIgnoreCase(map.get(MapParams.信道参数)) ? "信道A" : "信道B") + "\n" +
                        "电源类型：" + v + "\n" +
                        v + "：" + dianchizhuantai + "\n" +
                        "通讯状态：" + "正常" + "\n" +
                        "厂家标识：- " + "\n" +
                        "硬件版本号：" + LouShanYunUtils.getHardWareVersion(Integer.valueOf(map.get(MapParams.硬件版本))) + "\n" +
                        "软件版本号：" + LouShanYunUtils.getSoftWareVersion(Integer.valueOf(map.get(MapParams.软件版本))));
                pushEvent(InitCode.GetChangJiaBiaoShiRunner, map.get(MapParams.厂家标识));
                isLoadInfo = true;
            }
        });

    }

    @Override
    public void onChildNotify(byte[] results) {
        Log.i("yunanhao", "onNotify:" + DataParser.byteToString(results));
        byte code = (byte) (results[2] ^ ((byte) 0x80));
        switch (code) {
            case 0x30:
                map.putAll(DataParser.getInformationBase(results));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshText();
                        loadSettingConfig();
                        write(new byte[]{0x68, 0x01, 0x31, 0x32, 0x16}, "正在读取激活状态请稍后");
                    }
                });
                break;
            case 0x11:
                try {
                    map.putAll(DataParser.getInformationAll(results));
                    softVersion = Double.valueOf(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(map.get(MapParams.软件版本))));
                    typeMoZu = DataParser.getModuleType(results);
                    if (typeMoZu == 1) {

                        //读取当前信道参数
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (softVersion >= 1.07) {
                                    ARouter.getInstance().build(K.YuanChenBiaoHaoActivity_107).withParcelable("sensoroDevice", sensoroDevice).withString("oldSn", oldSn).withBoolean("isOnlyRead", isOnlyRead).navigation();
                                } else {
                                    if (softVersion >= 1.04) {
                                        layoutTime.setVisibility(View.VISIBLE);
                                        meterViewBinder.setOnZhiShuListener(YuanChenBiaoHaoActivity.this);
                                    }
                                    if (softVersion >= 1.05) {
                                        roundTextOpen.setVisibility(View.VISIBLE);
                                        roundTextClose.setVisibility(View.VISIBLE);
                                    } else {
                                        roundTextOpen.setVisibility(View.GONE);
                                        roundTextClose.setVisibility(View.GONE);
                                    }
                                    write(DataParser.CMD_INFO_BASE, "正在读取出厂设置信息");
                                }

                            }
                        });

                    } else {
                        ToastUtils.showShort("识别不是1号模组");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 0x08:
                if (results[3] == 0) {
                    sendMessageToast("设置信道成功");
                    map.put(MapParams.信道参数, String.valueOf(xindaocanshuData));
                    refreshText();
                    loadSettingConfig();
                } else {
                    sendMessageToast("设置信道失败");
                }
                break;
            case 0x02:
                if (results[3] == 0) {
                    map.put(MapParams.发送功率, String.valueOf(xinhaoqiangduData));
                    sendMessageToast("设置发送功率成功");
                    refreshText();
                    loadSettingConfig();
                } else if (results[3] == 4) {
                    sendMessageToast("无效参数");
                } else {
                    sendMessageToast("设置发送功率失败");
                }
                break;
            case 0x07:
                if (results[3] == 0) {
                    sendMessageToast("设置扩频因子成功");
                    map.put(MapParams.扩频因子, String.valueOf(kuopinyinziData));
                    refreshText();
                    loadSettingConfig();
                } else {
                    sendMessageToast("设置扩频因子失败");
                }
                break;
            case 0x41:
                if (results[3] == 0) {
                    sendMessageToast("打开集中器成功");
                    openJiZHongQiSuccess();
                } else {
                    sendMessageToast("打开集中器失败");
                }
                break;
            case 0x42:
                if (results[3] == 0) {
                    sendMessageToast("关闭集中器成功");
                    closeJiZHongQiSuccess();
                } else {
                    sendMessageToast("关闭集中器失败");
                }
                break;
            case 0x21:
                if (results[3] == 0) {
                    sendMessageToast("设置起止抄表号成功");
                    meterReadStart = meterStart;
                    meterReadEnd = meterEnd;
                    map.put(MapParams.总线起止表号_起, String.valueOf(meterReadStart));
                    map.put(MapParams.总线起止表号_止, String.valueOf(meterReadEnd));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            numA.setText(String.valueOf(meterReadStart));
                            numZ.setText(String.valueOf(meterReadEnd));
                        }
                    });
                    loadSettingConfig();
                } else {
                    sendMessageToast("设置起止抄表号失败");
                }
                break;
            //设置休眠
            case 0x24:
                if (results[3] == 0) {
                    sendMessageToast("休眠成功");
                    isSleep = true;
                } else {
                    sendMessageToast("休眠失败");
                }
                break;
            case 0x12:
                //读取当前信号强度 RSSI SNR
                int rssi = ByteConvertUtils.parseByteToSignedString(results[4]);
                int snr = ByteConvertUtils.parseByteToSignedString(results[5]);

                map.put(MapParams.信号强度, rssi + "");
                map.put(MapParams.信噪比, snr + "");
                loadSettingConfig();
                sendMessageToast("读取信息成功");
                break;
            case 0x31:
                //起止表号
                map.putAll(DataParser.getDiBanBiaoJiInfo(results));
                meterReadStart = Integer.parseInt(map.get(MapParams.总线起止表号_起));
                meterReadEnd = Integer.parseInt(map.get(MapParams.总线起止表号_止));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if ("1".equalsIgnoreCase(map.get(MapParams.系统状态))) {
                            systemStatus.setVisibility(View.VISIBLE);
                            systemStatus.setImageDrawable(getResources().getDrawable(R.drawable.l_loushanyun_active_enable));
                            typeJiHuo = 1;
                        } else {
                            systemStatus.setVisibility(View.VISIBLE);
                            systemStatus.setImageDrawable(getResources().getDrawable(R.drawable.l_loushanyun_active_disable));
                            typeJiHuo = 0;
                        }
                        numA.setText(String.valueOf(meterReadStart));
                        numZ.setText(String.valueOf(meterReadEnd));
                        write(DataParser.CMD_RSSI_SNR, "读取信号强度和信噪比");
                    }
                });
                break;
            case 0x28:
                if (results[3] == 0) {
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(MapParams.表号, resetSaveDataMeter.getMeterNumber());
                    hashMap.put(MapParams.HUB号, resetSaveDataMeter.getHub());
                    SaveDataMeter item = new SaveDataMeter(hashMap, resetSaveDataMeter.getParamOrUnit());
                    int index1 = LouShanYunUtils.containsMeter(meterArrayList, item);
                    if (index1 != -1) {
                        meterArrayList.set(index1, item);
                    } else {
                        meterArrayList.add(item);
                    }
                    int index = LouShanYunUtils.containsMeter(meterMissArrayList, item);
                    if (index != -1) {
                        meterMissArrayList.set(index, item);
                    } else {
                        meterMissArrayList.add(item);
                        meterMisssNum++;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int hasReadNum = meterArrayList.size();
                            int notReadNum = meterReadEnd - meterArrayList.size();
                            accept.setText(String.valueOf(hasReadNum));
                            disAccept.setText(String.valueOf(notReadNum));
                            meterMiss.setText(String.valueOf(meterMisssNum));
                            Collections.sort(meterArrayList, comparator);
                            Collections.sort(meterMissArrayList, comparator);
                            multiTypeAdapter.notifyDataSetChanged();
                        }
                    });
                    sendMessageToast("置数成功");
                    sendMessageToast("置数成功");
                } else if (results[3] == 1) {
                    sendMessageToast("置数失败，表单元和集中器通讯异常");
                } else if (results[3] == 2) {
                    sendMessageToast("置数失败，集中器和底板通讯异常");
                }
                break;
            case 0x32:
                SaveDataMeter item;
                HashMap<String, String> hashMap = new HashMap<>();
                if (results[3] == 0) {
                    hashMap.putAll(DataParser.getDanBiaoDuQuXinXi(results, softVersion));
                } else if (results[3] == 1) {
                    hashMap.put(MapParams.表号, String.valueOf(currentNum));
                } else if (results[3] == 2) {
                    hashMap.put(MapParams.表号, String.valueOf(currentNum));
                    hashMap.put(MapParams.HUB号, String.valueOf(results[4] & 0xff));
                }
                item = new SaveDataMeter(hashMap, "2");
                int index1 = LouShanYunUtils.containsMeter(meterArrayList, item);
                if (index1 != -1) {
                    meterArrayList.set(index1, item);
                } else {
                    meterArrayList.add(item);
                }
                if (results[3] != 0) {
                    int index = LouShanYunUtils.containsMeter(meterMissArrayList, item);
                    if (index != -1) {
                        meterMissArrayList.set(index, item);
                    } else {
                        meterMissArrayList.add(item);
                        meterMisssNum++;
                    }
                } else {
                    if (meterMissArrayList.size() > currentMissIndex) {
                        if (meterMissArrayList.get(currentMissIndex).getMeterNumber().equals(item.getMeterNumber())) {
                            meterMissArrayList.get(currentMissIndex).setMeterNumber("-1");
                            meterMisssNum--;
                        }
                    }
                }
                if (isReadMiss) {
                    currentMissIndex++;
                }

                runOnUiThread(() -> {
                    int hasReadNum = meterArrayList.size();
                    int notReadNum = meterReadEnd - meterArrayList.size();
                    accept.setText(String.valueOf(hasReadNum));
                    disAccept.setText(String.valueOf(notReadNum));
                    meterMiss.setText(String.valueOf(meterMisssNum));
                    Collections.sort(meterArrayList, comparator);
                    multiTypeAdapter.notifyDataSetChanged();
                    if (scrollToBottom) {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                    if (isReadMiss) {
                        if (currentMissIndex < meterMissArrayList.size()) {
                            if (!("-1".equals(meterMissArrayList.get(currentMissIndex).getMeterNumber()))) {
                                currentNum = Integer.valueOf(meterMissArrayList.get(currentMissIndex).getMeterNumber());
                                readBiao(currentNum);
                            }
                        } else {
                            for (int i = meterMissArrayList.size() - 1; i >= 0; i--) {
                                if ("-1".equals(meterMissArrayList.get(i).getMeterNumber())) {
                                    meterMissArrayList.remove(i);
                                }
                            }
                        }
                        if (meterMisssNum == 0) {
                            write(DataParser.closeJiZhongQi(), "正在关闭集中器");
                        }
                    } else {
                        if (notReadNum == 0) {
                            write(DataParser.closeJiZhongQi(), "正在关闭集中器");
                        }
                        if (currentNum < meterReadEnd) {
                            currentNum++;
                            readBiao(currentNum);
                        }
                    }
                });
                break;
            case 0x23:
                if (results[3] == 0) {
                    if (typeJiHuo == 1) {
                        typeJiHuo = 0;
                    } else if (typeJiHuo == 0) {
                        typeJiHuo = 1;
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (typeJiHuo == 1) {
                                if (isSave) {
                                    save();
                                }
                                //已激活
                                systemStatus.setVisibility(View.VISIBLE);
                                systemStatus.setImageDrawable(getResources().getDrawable(R.drawable.l_loushanyun_active_enable));

                            } else if (typeJiHuo == 0) {
                                //未激活
                                systemStatus.setVisibility(View.VISIBLE);
                                systemStatus.setImageDrawable(getResources().getDrawable(R.drawable.l_loushanyun_active_disable));
                            }

                        }
                    });
                } else {
                    sendMessageToast("激活失败");
                }
                break;
            case 0x25:
                if (results[3] == 0) {
                    sendMessageToast("设置时间成功");
                    hasSetTime = true;
                    write(DataParser.CMD_READ_TIME, "正在读取时间");
                } else {
                    sendMessageToast("设置时间失败");
                }
                break;
            case 0x26:
                if (results[3] == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                textTime.setText(DataParser.getTimeFromByte(results));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    sendMessageToast("读取失败");
                }
                break;

            case 0x40:
                if (results[3] == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sendMessageToast("发送命令成功");
                            if (alertDialog.isShowing()) {
                                alertDialog.dismiss();
                            }
                        }
                    });
                } else {
                    sendMessageToast("发送命令失败");
                }
                break;
            case 0x27:
                if (results[3] == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            write(DataParser.CMD_RSSI_SNR, "读取信号强度和信噪比");
                            sendData.getDelegate().setBackgroundColor(getResources().getColor(R.color.m_five_gray));
                            sendData.setClickable(false);
                            sendJiZhan = true;
                            sendMessageToast("强制发送成功");
                        }
                    });

                } else {
                    sendMessageToast("强制发送失败(反馈)");
                }
                break;
        }
    }


    private void readBiao(int num) {
        if (switchCheckedId.isChecked()) {
            String str = getRepeadStr(meterArrayList);
            if (!XHStringUtil.isEmpty(str, false)) {
                ToastUtils.showLong(str);
            } else {
                write(DataParser.getDanBiaoDuQuXinXiCMD(num), "正在读" + num + "号表", true, READ_BIAO_TIME_OUT);
            }
        } else {
            write(DataParser.getDanBiaoDuQuXinXiCMD(num), "正在读" + num + "号表", true, READ_BIAO_TIME_OUT);
        }

    }

    @Override
    protected void onChildConnectFailed(int i) {
        sendMessageToast("连接失败");
    }

    @Override
    protected void onChildConnectSuccess() {
        closeJiZHongQiSuccess();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                bluetoothStatus.setText("已连接");
                tvDialog.setText("");
                paramSettingInfo.setText("");
                isLoadInfo = false;
                write(DataParser.CMD_INFO_ALL, "识别模块中...", true, 6000);
            }
        });
    }

    @Override
    protected void onChildWriteSuccess() {
        Log.i("yunanhao", "onWriteSuccess");
    }

    @Override
    protected void onChildWriteFailure(int i) {
        Log.i("yunanhao", "onWriteFailure" + i);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshMainRepair refreshMainRepair) {
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onZhiShu(SaveDataMeter saveDataMeter) {
        HashMap<String, String> map = saveDataMeter.getMeterMap();
        String[] zhuangtai = new String[8];
        for (int i = 0; i < zhuangtai.length; i++) {
            zhuangtai[i] = "0";
        }
//        if ("1".equals(map.get(MapParams.表强磁状态))) {
//            zhuangtai[7] = "1";
//        }
//        if ("1".equals(map.get(MapParams.表电池状态))) {
//            zhuangtai[4] = "1";
//        }
//        if ("1".equals(map.get(MapParams.表流向状态))) {
//            zhuangtai[2] = "1";
//        }
//        if ("1".equals(map.get(MapParams.表拆卸状态))) {
//            zhuangtai[6] = "1";
//        }
        String s = "";
        for (int i = 0; i < zhuangtai.length; i++) {
            s = s + zhuangtai[i];
        }
        byte b = (byte) (ByteConvertUtils.binaryToDecimal(s) & 0xff);
        map.put(MapParams.状态, String.valueOf(b));
        write(DataParser.getZhiShuData(map), "正在置数");
        this.resetSaveDataMeter = saveDataMeter;
    }


}
