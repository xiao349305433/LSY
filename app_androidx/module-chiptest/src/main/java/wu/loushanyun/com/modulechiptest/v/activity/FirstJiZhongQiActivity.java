package wu.loushanyun.com.modulechiptest.v.activity;

import android.graphics.Color;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundRelativeLayout;
import com.flyco.roundview.RoundTextView;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.m.SaveDataMeter;
import com.wu.loushanyun.basemvp.p.adapter.MeterViewBinder;
import com.wu.loushanyun.basemvp.v.activity.BaseBlueToothActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ByteConvertUtils;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.modulechiptest.R;

@Route(path = K.FirstJiZhongQiActivity)
public class FirstJiZhongQiActivity extends BaseBlueToothActivity implements MeterViewBinder.OnZhiShuListener {


    private ScrollView scrollView;
    private TextView textDangqianSn;
    private RoundTextView bluetoothConn;
    private RoundTextView bluetoothDisconn;
    private RoundTextView oneSetting;
    private ImageView systemStatus;
    private RoundTextView buttonDuquInfo;
    private TextView textDuquInfo;
    private RoundTextView buttonDuquDianya;
    private TextView textDuquDianya;
    private RoundTextView roundTextOpen;
    private RoundTextView roundTextClose;
    private RoundRelativeLayout num;
    private EditText numStart;
    private View divide;
    private EditText numEnd;
    private RoundTextView numSetting;
    private RoundRelativeLayout roundChongzhi;
    private SwitchCompat switchCheckedId;
    private TextView numReadStart;
    private TextView numReadEnd;
    private RoundRelativeLayout meterReadLayout;
    private TextView meterRead;
    private TextView meterReadMiss;
    private TextView accept;
    private TextView disAccept;
    private TextView meterMiss;
    private RecyclerView resultList;
    private RoundTextView roundTextSetTime;
    private RoundTextView roundTextReadTime;
    private TextView textTime;
    private RoundTextView roundTextXiumian;
    private LinearLayout linearBlueTooth;
    private EditText editSearch;
    private RoundTextView resetClear;
    private RoundTextView resetClearList;
    private RecyclerView dialogBlueRecycle;

    private int writeCode;
    private HashMap<String, String> hashMap;

    private int typeJiHuo = 2;//判断底板是否激活 1已激活  0未激活

    private int meterStart;
    private int meterEnd;
    private int meterReadStart;
    private int meterReadEnd;
    private int currentNum;
    private int currentMissIndex;
    private double softVersion;
    private boolean isReadMiss = false;
    private boolean showLog = true;
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
    private SaveDataMeter resetSaveDataMeter;
    private boolean isJiZhongQiOpened = false;

    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        meterViewBinder = new MeterViewBinder(this);
        meterViewBinder.setOnZhiShuListener(this);
        meterViewBinder.setOnChaoBiaoListener(new MeterViewBinder.OnChaoBiaoListener() {
            @Override
            public void onChaoBiao(SaveDataMeter saveDataMeter) {
            }
        });
        registerLifeCycle(meterViewBinder);
    }

    @Override
    protected void onChildConnectFailed(int i) {

    }

    @Override
    protected void onChildConnectSuccess() {
        closeJiZHongQiSuccess();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textDangqianSn.setText("当前连接的SN设备:" + sensoroDevice.getSerialNumber().toUpperCase());
                write(DataParser.CMD_INFO_BASE, "正在读取");
                systemStatus.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onChildNotify(byte[] result) {
        byte code = (byte) (result[2] ^ ((byte) 0x80));
        XLog.i("返回" + code);
        switch (code) {
            //设置基本信息
            case 0x20:
                if (result[3] == 0) {
                    sendMessageToast("设置成功");
                } else {
                    sendMessageToast("设置失败");
                }
                break;
            //设置附加参数
            case 0x21:
                if (result[3] == 0) {
                    meterReadStart = meterStart;
                    meterReadEnd = meterEnd;
                    setDuShu();
                    sendMessageToast("设置成功");
                } else {
                    sendMessageToast("设置失败");
                }
                break;
            //设置激活停用
            case 0x23:
                if (result[3] == 0) {
                    if (typeJiHuo == 1) {
                        typeJiHuo = 0;
                    } else if (typeJiHuo == 0) {
                        typeJiHuo = 1;
                    }
                    setDuShu();
                }
                break;
            //设置休眠
            case 0x24:
                if (result[3] == 0) {

                } else {
                    sendMessageToast("休眠失败");
                }
                break;
            case 0x25:
                if (result[3] == 0) {
                    sendMessageToast("设置时间成功");
                } else {
                    sendMessageToast("设置时间失败");
                }
                break;
            case 0x41:
                if (result[3] == 0) {
                    sendMessageToast("打开集中器成功");
                    openJiZHongQiSuccess();
                } else {
                    sendMessageToast("打开集中器失败");
                }
                break;
            case 0x42:
                if (result[3] == 0) {
                    sendMessageToast("关闭集中器成功");
                    closeJiZHongQiSuccess();
                } else {
                    sendMessageToast("关闭集中器失败");
                }
                break;
            case 0x26:
                if (result[3] == 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                textTime.setText(DataParser.getTimeFromByte(result));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    sendMessageToast("读取失败");
                }
                break;
            case 0x28:
                if (result[3] == 0) {
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
                } else if (result[3] == 1) {
                    sendMessageToast("置数失败，表单元和集中器通讯异常");
                } else if (result[3] == 2) {
                    sendMessageToast("置数失败，集中器和底板通讯异常");
                }
                break;
            //读取底板基本信息
            case 0x30:
                hashMap.putAll(DataParser.getInformationBase(result));
                setDuShu();
                write(DataParser.getDiBanBiaoJiDuQuCMD(), "正在读取激活状态");
                break;
            //读取附加参数
            case 0x31:
                hashMap.putAll(DataParser.getDiBanBiaoJiInfo(result));
                try {
                    meterReadStart = Integer.parseInt(hashMap.get(MapParams.总线起止表号_起));
                    meterReadEnd = Integer.parseInt(hashMap.get(MapParams.总线起止表号_止));
                    if (result[3] == 0) {
                        if ("1".equalsIgnoreCase(hashMap.get(MapParams.系统状态))) {
                            typeJiHuo = 1;
                        } else {
                            typeJiHuo = 0;
                        }
                    }
                    setDuShu();
                } catch (Exception e) {

                }
                break;
            case 0x43:
                if (result[3] == 0) {
                    StringBuffer stringBuffer = new StringBuffer();
                    for (int i = 3; i >= 0; i--) {
                        stringBuffer.append(ByteConvertUtils.conver2BinaryStr(result[i+4]));
                    }
                    hashMap.put(MapParams.电池电压, String.valueOf(ByteConvertUtils.formatNumber(stringBuffer.toString(), 2)));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(Double.valueOf(hashMap.get(MapParams.电池电压))<3.6){
                                textDuquDianya.setText(hashMap.get(MapParams.电池电压)+"\n"+"不合格");
                            }else {
                                textDuquDianya.setText(hashMap.get(MapParams.电池电压)+"\n"+"合格");
                            }
                        }
                    });
                }
                break;
            //读取单表
            case 0x32:
                SaveDataMeter item;
                HashMap<String, String> hashMap = new HashMap<>();
                if (result[3] == 0) {
                    hashMap.putAll(DataParser.getDanBiaoDuQuXinXi(result, softVersion));
                } else if (result[3] == 1) {
                    hashMap.put(MapParams.表号, String.valueOf(currentNum));
                } else if (result[3] == 2) {
                    hashMap.put(MapParams.表号, String.valueOf(currentNum));
                    hashMap.put(MapParams.HUB号, String.valueOf(result[4] & 0xff));
                }
                item = new SaveDataMeter(hashMap, "2");

                int index1 = LouShanYunUtils.containsMeter(meterArrayList, item);
                if (index1 != -1) {
                    meterArrayList.set(index1, item);
                } else {
                    meterArrayList.add(item);
                }
                if (result[3] != 0) {
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
                            meterMissArrayList.get(currentMissIndex).setMeterNumber("-1");//证明抄到表了，置为-1
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
                    Collections.sort(meterMissArrayList, comparator);

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
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        if (currentNum < meterReadEnd) {
                            currentNum++;
                            readBiao(currentNum);
                        }
                    }

                });
                break;
        }
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

    @Override
    protected void onChildWriteSuccess() {

    }

    @Override
    protected void onChildWriteFailure(int i) {

    }

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_chip_activity_jizhongqi;
        ba.mTitleText = "集中器底板测试";
    }

    @Override
    protected void initView() {
        super.initView();
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        textDangqianSn = (TextView) findViewById(R.id.text_dangqian_sn);
        bluetoothConn = (RoundTextView) findViewById(R.id.bluetooth_conn);
        bluetoothDisconn = (RoundTextView) findViewById(R.id.bluetooth_disconn);
        oneSetting = (RoundTextView) findViewById(R.id.one_setting);
        systemStatus = (ImageView) findViewById(R.id.system_status);
        buttonDuquInfo = (RoundTextView) findViewById(R.id.button_duqu_info);
        textDuquInfo = (TextView) findViewById(R.id.text_duqu_info);
        buttonDuquDianya = (RoundTextView) findViewById(R.id.button_duqu_dianya);
        textDuquDianya = (TextView) findViewById(R.id.text_duqu_dianya);
        roundTextOpen = (RoundTextView) findViewById(R.id.round_text_open);
        roundTextClose = (RoundTextView) findViewById(R.id.round_text_close);
        num = (RoundRelativeLayout) findViewById(R.id.num);
        numStart = (EditText) findViewById(R.id.num_start);
        divide = (View) findViewById(R.id.divide);
        numEnd = (EditText) findViewById(R.id.num_end);
        numSetting = (RoundTextView) findViewById(R.id.num_setting);
        roundChongzhi = (RoundRelativeLayout) findViewById(R.id.round_chongzhi);
        switchCheckedId = (SwitchCompat) findViewById(R.id.switch_checked_id);
        numReadStart = (TextView) findViewById(R.id.num_read_start);
        numReadEnd = (TextView) findViewById(R.id.num_read_end);
        meterReadLayout = (RoundRelativeLayout) findViewById(R.id.meter_read_layout);
        meterRead = (TextView) findViewById(R.id.meter_read);
        meterReadMiss = (TextView) findViewById(R.id.meter_read_miss);
        accept = (TextView) findViewById(R.id.accept);
        disAccept = (TextView) findViewById(R.id.dis_accept);
        meterMiss = (TextView) findViewById(R.id.meter_miss);
        resultList = (RecyclerView) findViewById(R.id.result_list);
        roundTextSetTime = (RoundTextView) findViewById(R.id.round_text_set_time);
        roundTextReadTime = (RoundTextView) findViewById(R.id.round_text_read_time);
        textTime = (TextView) findViewById(R.id.text_time);
        roundTextXiumian = (RoundTextView) findViewById(R.id.round_text_xiumian);
        linearBlueTooth = (LinearLayout) findViewById(R.id.linear_blue_tooth);
        editSearch = (EditText) findViewById(R.id.edit_search);
        resetClear = (RoundTextView) findViewById(R.id.reset_clear);
        resetClearList = (RoundTextView) findViewById(R.id.reset_clear_list);
        dialogBlueRecycle = (RecyclerView) findViewById(R.id.dialog_blue_recycle);


        meterArrayList = new ArrayList<>();
        meterMissArrayList = new ArrayList<>();
        hashMap = new HashMap<>();
        multiTypeAdapter = new MultiTypeAdapter();
        multiTypeAdapter.register(SaveDataMeter.class, meterViewBinder);
        multiTypeAdapter.setItems(meterArrayList);
        resultList.setLayoutManager(new LinearLayoutManager(this));
        resultList.setAdapter(multiTypeAdapter);
        resultList.setNestedScrollingEnabled(false);

        bluetoothConn.setOnClickListener(v -> {
            if (textDangqianSn.getText().toString().contains("当前连接的SN设备")) {
                sendMessageToast("当前已连接设备");
                return;
            }
            connectBlueTooth(sensoroDevice);
        });
        bluetoothDisconn.setOnClickListener(v -> {
            sensoroDeviceSession.disconnect();
            textDangqianSn.setText("当前未连接SN设备");
        });
        connectBlueTooth(sensoroDevice);
        initBlueClick();
    }

    private void setDuShu() {
        StringBuilder sb = new StringBuilder();
        sb.append("采集场景:  ");
        sb.append(LouShanYunUtils.getCJCJReadStringByCode(hashMap.get(MapParams.采集场景)));
        sb.append("\n设备ID:  ");
        sb.append(hashMap.get(MapParams.设备ID));
        sb.append("\n产品形式:  ");
        sb.append(LouShanYunUtils.getCPXSReadStringByCode(Long.valueOf(hashMap.get(MapParams.产品形式))));
        sb.append("\n传感信号:  ");
        sb.append(LouShanYunUtils.getCGXHReadStringByCode(Long.valueOf(hashMap.get(MapParams.传感信号))));
        sb.append("\n电源类型:  ");
        sb.append(LouShanYunUtils.getDYLXReadStringByCode(Integer.valueOf(hashMap.get(MapParams.电源类型))));
        sb.append("\n底板出厂时间:  ");
        sb.append("20" + hashMap.get(MapParams.出厂时间_年) + "-" + hashMap.get(MapParams.出厂时间_月) + "-" + hashMap.get(MapParams.出厂时间_日));
        sb.append("\n硬件版本:  ");
        sb.append(LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.硬件版本))));
        sb.append("\n软件版本:  ");
        sb.append(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
        softVersion = Double.valueOf(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                numReadStart.setText(String.valueOf(meterReadStart));
                numReadEnd.setText(String.valueOf(meterReadEnd));
                textDuquInfo.setText(sb.toString());
                if (typeJiHuo == 1) {
                    //已激活
                    systemStatus.setVisibility(View.VISIBLE);
                    systemStatus.setImageDrawable(getResources().getDrawable(R.drawable.l_loushanyun_active_enable));
                } else if (typeJiHuo == 0) {
                    //未激活
                    systemStatus.setVisibility(View.VISIBLE);
                    systemStatus.setImageDrawable(getResources().getDrawable(R.drawable.l_loushanyun_active_disable));
                } else {
                    systemStatus.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onRightClick(View item) {
        if (showLog) {
        }
    }

    /**
     * 各种协议点击事件
     */
    private void initBlueClick() {
        oneSetting.setOnClickListener(this::OnBlueToothClick);
        buttonDuquInfo.setOnClickListener(this::OnBlueToothClick);
        numSetting.setOnClickListener(this::OnBlueToothClick);
        roundChongzhi.setOnClickListener(this::OnBlueToothClick);
        meterRead.setOnClickListener(this::OnBlueToothClick);
        meterReadMiss.setOnClickListener(this::OnBlueToothClick);
        roundTextXiumian.setOnClickListener(this::OnBlueToothClick);
        systemStatus.setOnClickListener(this::OnBlueToothClick);
        roundTextSetTime.setOnClickListener(this::OnBlueToothClick);
        roundTextReadTime.setOnClickListener(this::OnBlueToothClick);
        roundTextOpen.setOnClickListener(this::OnBlueToothClick);
        roundTextClose.setOnClickListener(this::OnBlueToothClick);
        buttonDuquDianya.setOnClickListener(this::OnBlueToothClick);
    }

    private void OnBlueToothClick(View view) {
        if (!isConnect()) {
            sendMessageToast("未连接蓝牙");
            return;
        }
        writeCode = 0;
        switch (view.getId()) {
            case R.id.one_setting:
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String time = sdf.format(new Date());
                String[] split = time.split("-");
                String year = split[0].substring(split[0].length() - 2, split[0].length());
                String month = split[1].toString();
                String day = split[2].toString();
                HashMap<String, String> map = new HashMap<>();
                map.put(MapParams.设备ID, LouShanYunUtils.getTimeID());
                map.put(MapParams.采集场景, String.valueOf(0x40));
                map.put(MapParams.产品形式, String.valueOf(0x04));
                map.put(MapParams.传感信号, String.valueOf(0x04));
                map.put(MapParams.保留字节, String.valueOf(0x00));
                map.put(MapParams.电源类型, String.valueOf(0b10000000));
                map.put(MapParams.出厂时间_年, year);
                map.put(MapParams.出厂时间_月, month);
                map.put(MapParams.出厂时间_日, day);
                map.put(MapParams.工作模式, String.valueOf(0x01));
                map.put(MapParams.信号类型, String.valueOf(0x01));
                map.put(MapParams.参数内容, String.valueOf(0x00));//无此属性
                map.put(MapParams.脉宽, String.valueOf(0x00));//无此属性
                map.put(MapParams.压力值标定_初始值, String.valueOf(0x00));//无此属性
                map.put(MapParams.压力值标定_最大值, String.valueOf(0x00));//无此属性
                map.put(MapParams.底板状态_设备强磁状态, String.valueOf(0x00));//无此属性
                map.put(MapParams.底板状态_设备拆卸状态, String.valueOf(0x00));//无此属性
                map.put(MapParams.底板状态_水表倒流状态, String.valueOf(0x00));//无此属性
                map.put(MapParams.底板状态_自备电池状态, String.valueOf(0x00));
                map.put(MapParams.底板状态_第三方电池状态, String.valueOf(0x00));
                map.put(MapParams.底板状态_外接电源220V状态, String.valueOf(0x00));
                write(DataParser.getInformationSettingCMD(map), "正在设置参数");
                textDuquInfo.setText("");
                break;
            case R.id.button_duqu_info:
                write(DataParser.CMD_INFO_BASE, "正在读取");
                textDuquInfo.setText("");
                break;
            case R.id.round_text_open:
                write(DataParser.openJiZhongQi(), "正在打开集中器");
                break;
            case R.id.round_text_close:
                write(DataParser.closeJiZhongQi(), "正在关闭集中器");
                break;
            case R.id.num_setting:
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
                if (hashMap != null) {
                    //起末表号的设置
                    HashMap<String, String> parmas = new HashMap<>();
                    parmas.put(MapParams.总线起止表号_起, String.valueOf(meterStart));
                    parmas.put(MapParams.总线起止表号_止, String.valueOf(meterEnd));
                    parmas.put(MapParams.仪表通信号, "0");
                    parmas.put(MapParams.初始化表计状态, "0");
                    parmas.put(MapParams.倍率, "0");
                    parmas.put(MapParams.安装脉冲底数, "0");
                    parmas.put(MapParams.口径, "0");
                    parmas.put(MapParams.保留字节, "0");
                    if (softVersion >= 1.07) {
                        parmas.put(MapParams.发送频率, "0");
                        parmas.put(MapParams.设备ID, "0");
                    } else {
                        parmas.put(MapParams.发送频率, "48");
                        parmas.put(MapParams.设备ID, hashMap.get(MapParams.设备ID));
                    }
                    write(DataParser.getDiBanBiaoJiChuShiHuaCMD(parmas), "设置表号");
                }
                break;
            case R.id.round_chongzhi:
                meterArrayList.clear();
                meterMissArrayList.clear();
                meterMisssNum = 0;
                accept.setText(String.valueOf(meterArrayList.size()));
                disAccept.setText(String.valueOf(meterReadEnd - meterArrayList.size()));
                meterMiss.setText(String.valueOf(meterMisssNum));
                meterRead.setEnabled(true);
                meterReadLayout.getDelegate().setBackgroundColor(getResources().getColor(R.color.l_five_Q));
                multiTypeAdapter.notifyDataSetChanged();
                break;
            case R.id.meter_read:
                //读表
                if (!isJiZhongQiOpened) {
                    sendMessageToast("请打开集中器");
                    return;
                }
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
                break;
            case R.id.meter_read_miss:
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
                break;
            case R.id.round_text_xiumian:
                write(DataParser.getSystemSleepCMD(), "正在休眠");
                break;
            case R.id.round_text_set_time:
                if (isJiZhongQiOpened) {
                    sendMessageToast("请关闭集中器电源");
                } else {
                    write(DataParser.getByteForSettingTime(), "正在设置时间");
                }

                break;
            case R.id.round_text_read_time:
                write(DataParser.CMD_READ_TIME, "正在读取时间");
                break;
            case R.id.button_duqu_dianya:
                byte[] DY = {(byte) 0x68, (byte) 0x01, (byte) 0x43, (byte) 0x44, (byte) 0x16};
                write(DY, "正在读取电池电压");
                break;
            case R.id.system_status:
                if (typeJiHuo == 1) {
                    write(DataParser.getSystemStatusSettingCMD(false), "关闭激活中");
                } else if (typeJiHuo == 0) {
                    write(DataParser.getSystemStatusSettingCMD(true), "开启激活中");
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

    private boolean isConnect() {
        if (textDangqianSn.getText().toString().contains("当前连接的SN设备")) {
            return true;
        } else {
            return false;
        }
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
////        zhuangtai[7] = "1";
//        if ("1".equals(map.get(MapParams.表电池状态))) {
//            zhuangtai[4] = "1";
//        }
////        zhuangtai[4] = "1";
//        if ("1".equals(map.get(MapParams.表流向状态))) {
//            zhuangtai[2] = "1";
//        }
////        zhuangtai[2] = "1";
//        if ("1".equals(map.get(MapParams.表拆卸状态))) {
//            zhuangtai[6] = "1";
//        }
////        zhuangtai[6] = "1";
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
