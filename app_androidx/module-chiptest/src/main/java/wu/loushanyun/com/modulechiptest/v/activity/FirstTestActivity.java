package wu.loushanyun.com.modulechiptest.v.activity;

import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundTextView;
import com.sensoro.sensor.kit.callback.SensoroDeviceListener;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.url.URLUtils;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.init.SensoroBlueManager;
import com.wu.loushanyun.basemvp.v.activity.BaseBlueToothActivity;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ByteConvertUtils;
import met.hx.com.librarybase.some_utils.KeyboardUtils;
import met.hx.com.librarybase.some_utils.TimeUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.libraryfive.p.adapter.SNDeviceViewBinder;
import wu.loushanyun.com.modulechiptest.R;
import wu.loushanyun.com.modulechiptest.init.ChipCode;
import wu.loushanyun.com.modulechiptest.m.NewInfo;
import wu.loushanyun.com.modulechiptest.p.runner.MChipGetNewsInfoRunner;

@Route(path = K.FirstTestActivity)
public class FirstTestActivity extends BaseBlueToothActivity {
    private ArrayList<SensoroDevice> deviceArrayList;

    private TextView textDangqianSn;
    private RoundTextView bluetoothConn;
    private RoundTextView bluetoothDisconn;
    private TextView textMoshi;
    private Spinner fasonggonglvSelect;
    private RoundTextView fasonggonglvSetting;
    private Spinner xindaocanshuSelect;
    private RoundTextView xindaocanshuSetting;
    private Spinner kuopinyinziSelect;
    private RoundTextView kuopinyinziSetting;
    private RoundTextView maichongSetting;
    private ImageView systemStatus;
    private EditText editXinhaoqiangduStart;
    private EditText editXinhaoqiangduEnd;
    private EditText editXinzaobiStart;
    private EditText editXinzaobiEnd;
    private TextView textHuoqu;
    private RoundTextView buttonQiangzhifasongNew;
    private RoundTextView buttonQiangzhifasongOld;
    private RoundTextView buttonDuquXinhao;
    private TextView textHuoquNew;
    private RoundTextView buttonDuquNew;
    private RoundTextView buttonDuquInfo;
    private TextView textDuquInfo;
    private RoundTextView buttonJizhongqi;
    private RoundTextView button485;
    private LinearLayout linearBlueTooth;
    private EditText editSearch;
    private RoundTextView resetClear;
    private RoundTextView resetClearList;
    private RecyclerView dialogBlueRecycle;


    private SNDeviceViewBinder snDeviceViewBinder;
    private MultiTypeAdapter multiTypeAdapterBlue;
    private int type;
    private HashMap<String, String> hashMap;
    private WebSocketClient client;
    private int writeCode;

    private boolean isAllSetting;
    private double softVersion;

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == ChipCode.MChipGetNewsInfoRunner) {
            if (event.isSuccess()) {
                NewInfo newInfo = (NewInfo) event.getReturnParamAtIndex(0);
                textHuoquNew.setText("");
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < newInfo.getData().size(); i++) {
                    if (i < 2) {
                        stringBuffer.append("时间:" + TimeUtils.milliseconds2String(newInfo.getData().get(i).getSendTime()) + "；");
                        stringBuffer.append("信号强度:" + newInfo.getData().get(i).getRssi() + "；");
                        stringBuffer.append("信噪比:" + newInfo.getData().get(i).getSnr() + "；\n");
                    }
                }
                textHuoquNew.setText(stringBuffer.toString());
            }
        }
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_chip_activity_first;
        ba.mTitleText = "1号模组测试";
        ba.mTitleRightText = "附近蓝牙";
        ba.mTitleLeftRightWidth = 100;
    }

    @Override
    protected boolean onSwipeBackEnable() {
        return true;
    }

    @Override
    public void onRightClick(View item) {
        linearBlueTooth.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onLoadingDismissTimeOut() {
        if (writeCode == 0x27) {
            runOnUiThread(() -> textHuoqu.setText("强制发送失败(超时)"));
        } else if (writeCode == 0x40) {
            runOnUiThread(() -> textHuoqu.setText("强制发送失败(超时)"));
        } else {
            super.onLoadingDismissTimeOut();
        }
    }

    @Override
    protected void onChildConnectFailed(int i) {

    }

    @Override
    protected void onChildConnectSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textDangqianSn.setText("当前连接的SN设备:" + sensoroDevice.getSerialNumber().toUpperCase());
                write(DataParser.CMD_INFO_ALL, "识别模块中...", true, 6000);
            }
        });

    }

    @Override
    protected void onChildNotify(byte[] result) {
        byte code = (byte) (result[2] ^ ((byte) 0x80));
        XLog.i("返回" + code);
        switch (code) {
            case 0x00:
                if (result[3] == 0) {
                    if (isAllSetting) {
                        byte[] bytes = {0x68, 0x02, 0x01, 0x30, 0x33, 0x16};
                        write(bytes, "设置发送频率，24小时");
                    } else {

                    }

                } else {
                    sendMessageToast("设置采集场景失败");
                }
                break;
            case 0x27:
                if (result[3] == 0) {
                    runOnUiThread(() -> textHuoqu.setText("强制发送成功"));
                    sendMessageToast("强制发送成功");
                    write(DataParser.CMD_RSSI_SNR, "读取信号强度和信噪比");
                } else {
                    runOnUiThread(() -> textHuoqu.setText("强制发送失败(反馈)"));
                    sendMessageToast("强制发送失败(反馈)");
                }
                break;
            case 0x01:
                if (result[3] == 0) {
                    if (isAllSetting) {
                        int xinhaoqiangduData = Integer.parseInt(getResources().getStringArray(
                                R.array.m_chip_xinhaoqiangdu)[fasonggonglvSelect
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
                    } else {

                    }

                } else {
                    sendMessageToast("设置发送频率失败");
                }
                break;
            case 0x02:
                if (result[3] == 0) {
                    if (isAllSetting) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String time = sdf.format(new Date());
                        String[] split = time.split("-");
                        String year = split[0].substring(split[0].length() - 2, split[0].length());
                        String month = split[1].toString();
                        String day = split[2].toString();
                        byte[] bytes = {0x68, 0x04, 0x03,
                                (byte) (Integer.parseInt(year) & 0xff),
                                (byte) (Integer.parseInt(month) & 0xff),
                                (byte) (Integer.parseInt(day) & 0xff),
                                0x07, 0x16};
                        bytes[bytes.length - 2] += bytes[3];
                        bytes[bytes.length - 2] += bytes[4];
                        bytes[bytes.length - 2] += bytes[5];
                        write(bytes, "正在设置模组出厂日期");
                    } else {
                        sendMessageToast("设置成功");
                    }
                } else {
                    sendMessageToast("设置发送功率失败");
                }
                break;
            case 0x03:
                if (result[3] == 0) {
                    if (isAllSetting) {
                        byte[] cmd = {0x68, 0x02, 0x04, 0x01, 0x07, 0x16};
                        write(cmd, "正在设置网络交互");
                    } else {

                    }

                } else {
                    sendMessageToast("设置出厂日期失败");
                }
                break;
            case 0x04:
                if (result[3] == 0) {
                    if (isAllSetting) {
                        byte[] cmd = {0x68, 0x02, 0x05, 0x01, 0x08, 0x16};
                        write(cmd, "正在设置工作模式");
                    } else {

                    }

                } else {
                    sendMessageToast("设置网络交互失败");
                }
                break;
            case 0x05:
                if (result[3] == 0) {
                    if (isAllSetting) {
                        byte[] d = new byte[7];
                        d[0] = (byte) 0x68;
                        d[1] = (byte) 0x03;//有效数据
                        d[2] = (byte) 0x06;//命令
                        d[3] = (byte) (0 & 0xff);
                        d[4] = (byte) (0 & 0xff00);
                        byte cs = 0;
                        for (int i = 1; i < 5; i++) {
                            cs += d[i];
                        }
                        d[5] = cs;//校验和
                        d[6] = (byte) 0x16;
                        write(d, "正在设置厂家标识");
                    } else {

                    }

                } else {
                    sendMessageToast("设置工作模式失败");
                }
                break;
            case 0x06:
                if (result[3] == 0) {
                    if (isAllSetting) {
                        int kpyz = LouShanYunUtils.getKPYZWriteCodeByString(String.valueOf(kuopinyinziSelect.getSelectedItem()));
                        byte[] input1 = {0x68, 0x02, 0x07, (byte) kpyz, (byte) (0x09 + kpyz), 0x16};
                        write(input1, "设置扩频因子中...");
                    } else {

                    }

                } else {
                    sendMessageToast("设置厂家标识失败");
                }
                break;
            case 0x07:
                if (result[3] == 0) {
                    if (isAllSetting) {
                        String moshi = (String) xindaocanshuSelect.getSelectedItem();
                        if (moshi.equals("模式A")) {
                            byte[] d = DataParser.setXinDaoCanShu(true);//模式A
                            write(d, "设置模式中");
                        } else if (moshi.equals("模式B")) {
                            byte[] d = DataParser.setXinDaoCanShu(false);//模式B
                            write(d, "设置模式中");
                        }
                    } else {
                        sendMessageToast("设置成功");
                    }

                } else {
                    sendMessageToast("设置失败");
                }
                break;
            case 0x08:
                if (result[3] == 0) {
                    write(DataParser.CMD_INFO_ALL, "识别模块中...", true, 6000);
                    sendMessageToast("设置成功");
                } else {
                    sendMessageToast("设置失败");
                }
                break;
            case 0x11:
                try {
                    type = DataParser.getModuleType(result);
                    hashMap.putAll(DataParser.getInformationAll(result));
                    softVersion = Double.valueOf(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
                    if (type == 1) {
                        if (softVersion >= 1.04) {
                            byte[] d1 = {0x68, 0x01, 0x15, 0x16, 0x16};
                            write(d1, "读取固件版本号");
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    buttonQiangzhifasongNew.setVisibility(View.GONE);
                                }
                            });

                        }

                    } else {
                        sendMessageToast("识别不是1号模组，请选择正确的模组");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    sendMessageToast("识别失败");
                }
                setDuQu();
                break;
            case 0x31:
                hashMap.putAll(DataParser.getDiBanBiaoJiInfo(result));
                if (result[3] == 0) {
                    if ("1".equalsIgnoreCase(hashMap.get(MapParams.系统状态))) {
                        //已激活
                        systemStatus.setVisibility(View.VISIBLE);
                        systemStatus.setImageDrawable(getResources().getDrawable(R.drawable.l_loushanyun_active_enable));
                    } else {
                        //未激活
                        systemStatus.setVisibility(View.VISIBLE);
                        systemStatus.setImageDrawable(getResources().getDrawable(R.drawable.l_loushanyun_active_disable));
                    }
                } else {
                    runOnUiThread(() -> systemStatus.setVisibility(View.GONE));
                }
                break;
            case 0x40:
                if (result[3] == 0) {
                    LoadingDialogUtil.showByEvent(true, 10000, "正在接收", loadingTag);
                }
                break;
            case 0x15:
                String banben = DataParser.getASCIIbyByte(result);
                hashMap.put(MapParams.固件版本号, banben);
                setDuQu();
                break;
            case 0x12:
                if (result[3] == 0) {
                    int rssi = ByteConvertUtils.parseByteToSignedString(result[4]);
                    int snr = ByteConvertUtils.parseByteToSignedString(result[5]);
                    int rssiStart = Integer.valueOf(editXinhaoqiangduStart.getText().toString());
                    int rssiEnd = Integer.valueOf(editXinhaoqiangduEnd.getText().toString());
                    int snrStart = Integer.valueOf(editXinzaobiStart.getText().toString());
                    int snrEnd = Integer.valueOf(editXinzaobiEnd.getText().toString());
                    String status;
                    if (rssi > rssiStart && rssi < rssiEnd && snr > snrStart && snr < snrEnd) {
                        status = "合格";
                    } else {
                        status = "不合格";
                    }
                    runOnUiThread(() -> {
                        String sb = "信号强度：" + rssi + "\n" + "信噪比：" + snr + "\n" + "状态：" + status;
                        textHuoqu.setText(sb);
                        pushEventNoProgress(ChipCode.MChipGetNewsInfoRunner, sensoroDevice.sn, "1", "5");
                    });
                }
                break;

            case 0x24:
                if (result[3] == 0) {
                    sendMessageToast("休眠成功");
                } else {
                    sendMessageToast("休眠失败");
                }
                break;
        }

    }

    private void setDuQu() {
        StringBuilder sb = new StringBuilder();
        sb.append("发送功率:  ");
        if (!XHStringUtil.isEmpty(hashMap.get(MapParams.发送功率), false)) {
            sb.append(hashMap.get(MapParams.发送功率) + "dbm");
        }
        sb.append("\n发送频率:  ");
        sb.append(LouShanYunUtils.getFSPLReadStringByCode(Long.valueOf(hashMap.get(MapParams.发送频率))));
        sb.append("\n网络交互:  ");
        sb.append(LouShanYunUtils.getWLJHReadStringByCode(hashMap.get(MapParams.网络交互)));
        sb.append("\n信道参数:  ");
        sb.append("0".equalsIgnoreCase(hashMap.get(MapParams.信道参数)) ? "信道A" : "信道B");
        sb.append("\n扩频因子:  ");
        sb.append(LouShanYunUtils.getKPYZReadStringByCode(hashMap.get(MapParams.扩频因子)));
        sb.append("\n采集场景:  ");
        sb.append(LouShanYunUtils.getCJCJReadStringByCode(hashMap.get(MapParams.采集场景)));
        sb.append("\n硬件版本:  ");
        sb.append(LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.硬件版本))));
        sb.append("\n软件版本:  ");
        sb.append(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
        sb.append("\nLSY-IOT版本:  ");
        sb.append(hashMap.get(MapParams.固件版本号));
        sb.append("\n模组出厂时间:  ");
        sb.append("20" + hashMap.get(MapParams.出厂时间_年) + "-" + hashMap.get(MapParams.出厂时间_月) + "-" + hashMap.get(MapParams.出厂时间_日));
        runOnUiThread(() -> {
            if (!XHStringUtil.isEmpty(hashMap.get(MapParams.发送功率), false)) {
                if ("20".equals(hashMap.get(MapParams.发送功率))) {
                    fasonggonglvSelect.setSelection(0);
                } else if ("18".equals(hashMap.get(MapParams.发送功率))) {
                    fasonggonglvSelect.setSelection(1);
                } else if ("16".equals(hashMap.get(MapParams.发送功率))) {
                    fasonggonglvSelect.setSelection(2);
                }
            }
            if (!XHStringUtil.isEmpty(hashMap.get(MapParams.扩频因子), false)) {
                if ("0".equals(hashMap.get(MapParams.扩频因子))) {
                    kuopinyinziSelect.setSelection(0);
                } else if ("1".equals(hashMap.get(MapParams.扩频因子))) {
                    kuopinyinziSelect.setSelection(1);
                } else if ("2".equals(hashMap.get(MapParams.扩频因子))) {
                    kuopinyinziSelect.setSelection(2);
                } else if ("3".equals(hashMap.get(MapParams.扩频因子))) {
                    kuopinyinziSelect.setSelection(3);
                } else if ("4".equals(hashMap.get(MapParams.扩频因子))) {
                    kuopinyinziSelect.setSelection(4);
                } else if ("5".equals(hashMap.get(MapParams.扩频因子))) {
                    kuopinyinziSelect.setSelection(5);
                }
            }
            textMoshi.setText("0".equalsIgnoreCase(hashMap.get(MapParams.信道参数)) ? "当前设备模式：信道A" : "当前设备模式：信道B");
            xindaocanshuSelect.setSelection("0".equalsIgnoreCase(hashMap.get(MapParams.信道参数)) ? 0 : 1);
            textDuquInfo.setText(sb.toString());
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
        registerEventRunner(ChipCode.MChipGetNewsInfoRunner, new MChipGetNewsInfoRunner());
    }

    @Override
    protected void initLifeCycle() {
        snDeviceViewBinder = new SNDeviceViewBinder(sensoroDevice -> {
            this.sensoroDevice = sensoroDevice;
            linearBlueTooth.setVisibility(View.GONE);
            connectBlueTooth(sensoroDevice);
            KeyboardUtils.hideSoftInput(this);
        });
    }

    @Override
    protected void initView() {
        super.initView();
        textDangqianSn = (TextView) findViewById(R.id.text_dangqian_sn);
        bluetoothConn = (RoundTextView) findViewById(R.id.bluetooth_conn);
        bluetoothDisconn = (RoundTextView) findViewById(R.id.bluetooth_disconn);
        textMoshi = (TextView) findViewById(R.id.text_moshi);
        fasonggonglvSelect = (Spinner) findViewById(R.id.fasonggonglv_select);
        fasonggonglvSetting = (RoundTextView) findViewById(R.id.fasonggonglv_setting);
        xindaocanshuSelect = (Spinner) findViewById(R.id.xindaocanshu_select);
        xindaocanshuSetting = (RoundTextView) findViewById(R.id.xindaocanshu_setting);
        kuopinyinziSelect = (Spinner) findViewById(R.id.kuopinyinzi_select);
        kuopinyinziSetting = (RoundTextView) findViewById(R.id.kuopinyinzi_setting);
        maichongSetting = (RoundTextView) findViewById(R.id.maichong_setting);
        systemStatus = (ImageView) findViewById(R.id.system_status);
        editXinhaoqiangduStart = (EditText) findViewById(R.id.edit_xinhaoqiangdu_start);
        editXinhaoqiangduEnd = (EditText) findViewById(R.id.edit_xinhaoqiangdu_end);
        editXinzaobiStart = (EditText) findViewById(R.id.edit_xinzaobi_start);
        editXinzaobiEnd = (EditText) findViewById(R.id.edit_xinzaobi_end);
        textHuoqu = (TextView) findViewById(R.id.text_huoqu);
        buttonQiangzhifasongNew = (RoundTextView) findViewById(R.id.button_qiangzhifasong_new);
        buttonQiangzhifasongOld = (RoundTextView) findViewById(R.id.button_qiangzhifasong_old);
        buttonDuquXinhao = (RoundTextView) findViewById(R.id.button_duqu_xinhao);
        textHuoquNew = (TextView) findViewById(R.id.text_huoqu_new);
        buttonDuquNew = (RoundTextView) findViewById(R.id.button_duqu_new);
        buttonDuquInfo = (RoundTextView) findViewById(R.id.button_duqu_info);
        textDuquInfo = (TextView) findViewById(R.id.text_duqu_info);
        buttonJizhongqi = (RoundTextView) findViewById(R.id.button_jizhongqi);
        button485 = (RoundTextView) findViewById(R.id.button_485);
        linearBlueTooth = (LinearLayout) findViewById(R.id.linear_blue_tooth);
        editSearch = (EditText) findViewById(R.id.edit_search);
        resetClear = (RoundTextView) findViewById(R.id.reset_clear);
        resetClearList = (RoundTextView) findViewById(R.id.reset_clear_list);
        dialogBlueRecycle = (RecyclerView) findViewById(R.id.dialog_blue_recycle);


        hashMap = new HashMap<>();
        initBlueList();
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
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String search = s.toString().toUpperCase();
                if (!XHStringUtil.isEmpty(search, false)) {
                    snDeviceViewBinder.setKeyWord(search);
                } else {
                    snDeviceViewBinder.setKeyWord(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        resetClear.setOnClickListener(v -> {
            editSearch.setText("");
        });
        resetClearList.setOnClickListener(v -> {
            SensoroBlueManager.searchBlueTooth(sensoroDeviceListener);
            deviceArrayList.clear();
            multiTypeAdapterBlue.notifyDataSetChanged();
        });
        initBlueClick();
        SensoroBlueManager.searchBlueTooth(sensoroDeviceListener);
    }

    /**
     * 各种协议点击事件
     */
    private void initBlueClick() {
        buttonDuquInfo.setOnClickListener(this::OnBlueToothClick);
        xindaocanshuSetting.setOnClickListener(this::OnBlueToothClick);
        fasonggonglvSetting.setOnClickListener(this::OnBlueToothClick);
        buttonQiangzhifasongOld.setOnClickListener(this::OnBlueToothClick);
        buttonQiangzhifasongNew.setOnClickListener(this::OnBlueToothClick);
        buttonDuquXinhao.setOnClickListener(this::OnBlueToothClick);
        kuopinyinziSetting.setOnClickListener(this::OnBlueToothClick);
        maichongSetting.setOnClickListener(this::OnBlueToothClick);
        buttonJizhongqi.setOnClickListener(this::OnBlueToothClick);
        buttonDuquNew.setOnClickListener(this::OnBlueToothClick);
    }

    /**
     * 需要判断蓝牙连接状态的点击事件
     *
     * @param view
     */
    private void OnBlueToothClick(View view) {
        if (!isConnect()) {
            sendMessageToast("未连接蓝牙");
            return;
        }
        if (type != 1) {
            sendMessageToast("识别不是1号模组，请选择正确的模组");
            return;
        }
        writeCode = 0;
        isAllSetting = false;
        switch (view.getId()) {
            case R.id.button_duqu_info:
                textDuquInfo.setText("");
                write(DataParser.CMD_INFO_ALL, "识别模块中...", true, 6000);
                break;
            case R.id.maichong_setting:
                isAllSetting = true;
                byte[] input2 = {0x68, 0x02, 0x00, 0x40, 0x42, 0x16};
                write(input2, "设置采集场景中...");
                break;
            case R.id.kuopinyinzi_setting:
                textDuquInfo.setText("");
                int kpyz = LouShanYunUtils.getKPYZWriteCodeByString(String.valueOf(kuopinyinziSelect.getSelectedItem()));
                byte[] input1 = {0x68, 0x02, 0x07, (byte) kpyz, (byte) (0x09 + kpyz), 0x16};
                write(input1, "设置扩频因子中...");
                break;
            case R.id.button_duqu_new:
                pushEvent(ChipCode.MChipGetNewsInfoRunner, sensoroDevice.sn, "1", "5");
                break;
            case R.id.xindaocanshu_setting:
                textDuquInfo.setText("");
                String moshi = (String) xindaocanshuSelect.getSelectedItem();
                if (moshi.equals("模式A")) {
                    byte[] d = DataParser.setXinDaoCanShu(true);//模式A
                    write(d, "设置模式中");
                } else if (moshi.equals("模式B")) {
                    byte[] d = DataParser.setXinDaoCanShu(false);//模式B
                    write(d, "设置模式中");
                }
                break;
            case R.id.button_qiangzhifasong_new:
                if (softVersion >= 1.04) {
                    textHuoqu.setText("");
                    writeCode = 0x27;
                    String phone = "15527919058";
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
                        write(d1, "正在发送命令，请稍后……", true, BLUE_SEND_TIME_OUT);
                    }
                } else {
                    sendMessageToast("请使用老版");
                }

                break;
            case R.id.fasonggonglv_setting:
                textHuoqu.setText("");
                textDuquInfo.setText("");
                int xinhaoqiangduData = Integer.parseInt(getResources().getStringArray(
                        R.array.m_chip_xinhaoqiangdu)[fasonggonglvSelect
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
                break;
            case R.id.button_qiangzhifasong_old: {
                textHuoqu.setText("");
                try {
                    LoadingDialogUtil.show(loadingProgress, "连接服务器中，请稍后……");
                    String url = URLUtils.getSocketIP() + "/1/" + sensoroDevice.getSerialNumber();
                    XLog.i("url=" + url);
                    client = new WebSocketClient(new URI(url), new Draft_17()) {

                        @Override
                        public void onClose(int arg0, String arg1, boolean arg2) {
                            LoadingDialogUtil.dismissByEvent(loadingTag);
                        }

                        @Override
                        public void onError(Exception arg0) {
                            LoadingDialogUtil.dismissByEvent(loadingTag);
                            sendMessageToast("服务器出错");
                        }

                        @Override
                        public void onMessage(final String arg0) {
                            sendMessageToast(arg0);
                            LoadingDialogUtil.dismissByEvent(loadingTag);
                            runOnUiThread(() -> {
                                textHuoqu.setText("强制发送成功");
                                client.close();
                            });
                        }

                        @Override
                        public void onOpen(ServerHandshake arg0) {
                            readPhoneNumber();
                        }
                    };
                    client.connect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            break;
            case R.id.button_duqu_xinhao:
                textHuoqu.setText("");
                write(DataParser.CMD_RSSI_SNR, "读取信号强度和信噪比");
                break;
            case R.id.button_jizhongqi:
                ARouter.getInstance().build(K.FirstJiZhongQiActivity).withParcelable("sensoroDevice", sensoroDevice).navigation();
                break;
        }
    }

    private void readPhoneNumber() {
        writeCode = 0x40;
        String phone = "15527919058";
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

    private boolean isConnect() {
        if (textDangqianSn.getText().toString().contains("当前连接的SN设备")) {
            return true;
        } else {
            return false;
        }
    }

    private SensoroDeviceListener sensoroDeviceListener = new SensoroDeviceListener<SensoroDevice>() {
        @Override
        public void onNewDevice(SensoroDevice sensoroDevice) {
            XLog.i("蓝牙onNewDevice" + sensoroDevice.toString());
            if (!deviceArrayList.contains(sensoroDevice)) {
                deviceArrayList.add(sensoroDevice);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        multiTypeAdapterBlue.notifyDataSetChanged();
                    }
                });
            } else {
                XLog.i("蓝牙sdaasasdasdasdasd" + sensoroDevice.toString());
            }

        }

        @Override
        public void onGoneDevice(SensoroDevice sensoroDevice) {
            XLog.i("蓝牙onGoneDevice" + sensoroDevice.toString());
        }

        @Override
        public void onUpdateDevices(ArrayList<SensoroDevice> arrayList) {
            XLog.i("蓝牙onUpdateDevices" + arrayList.toString());
            for (int i = 0; i < arrayList.size(); i++) {
                SensoroDevice sensoroDevice = arrayList.get(i);
                if (deviceArrayList.contains(sensoroDevice)) {
                    deviceArrayList.set(deviceArrayList.indexOf(sensoroDevice), sensoroDevice);
                } else {
                    deviceArrayList.add(sensoroDevice);
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    multiTypeAdapterBlue.notifyDataSetChanged();
                }
            });
        }
    };

    private void initBlueList() {
        multiTypeAdapterBlue = new MultiTypeAdapter();
        deviceArrayList = new ArrayList<>();
        multiTypeAdapterBlue.register(SensoroDevice.class, snDeviceViewBinder);
        multiTypeAdapterBlue.setItems(deviceArrayList);
        dialogBlueRecycle.setAdapter(multiTypeAdapterBlue);

    }
}
