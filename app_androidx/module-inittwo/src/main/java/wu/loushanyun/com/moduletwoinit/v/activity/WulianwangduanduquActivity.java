package wu.loushanyun.com.moduletwoinit.v.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.roundview.RoundTextView;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.url.URLUtils;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.p.runner.GetChangJiaBiaoShiRunner;
import com.wu.loushanyun.basemvp.v.activity.BaseBlueToothActivity;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.base.glide.GlideUtil;
import met.hx.com.base.base.matisse.MatisseUtil;
import met.hx.com.base.baseconfig.C;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.libraryfive.PictureUtil;
import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;
import wu.loushanyun.com.moduletwoinit.R;
import wu.loushanyun.com.moduletwoinit.init.MInitTwoCode;
import wu.loushanyun.com.moduletwoinit.m.OnetoOneConverter;

@Route(path = K.WulianwangduanduquActivity)
public class WulianwangduanduquActivity extends BaseBlueToothActivity implements View.OnClickListener {
    private TextView bluetoothName;
    private TextView factoryInfo;
    private ImageView systemStatus;
    private TextView factoryDuqu;
    private RoundTextView unit;
    private RoundTextView whole;
    private LinearLayout hide;
    private TextView maichongchangshu;
    private Spinner beilv;
    private EditText chushimaichong;
    private TextView chuchidushu;
    private Spinner xindaocanshu;
    private Spinner kuopinyinzi;
    private EditText inputMessage;
    private Spinner caliber;
    private RoundTextView dataSet;
    private RoundTextView dataLoad;
    private ImageView ivSelector;
    private RoundTextView taskTest;
    private RoundTextView dataSave;
    private RoundTextView xiumian;
    private TextView textDuqu;
    /**
     * -----------------------------------------
     */
    //蓝牙
//    private SensoroDevice sensoroDevice;
//    private SensoroDeviceSession sensoroDeviceSession;
    //从设备读取的所有信息
    private HashMap<String, String> hashMap;
    private boolean hasRead = false;
    private AlertDialog alertDialog;
    private EditText etInput;

    private OnetoOneConverter onetoOneConverter;
    private String areaNumber;
    private boolean isAlive = false, isUploadable;
    private boolean canSave = false;//用于更新数据的保存
    private WebSocketClient client;
    private int type;
    private int softVersion;//2号模组第4个版本之后增加了发送功率的配置和读取，还有强制发送改版
    private boolean isJiHuo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        areaNumber = getIntent().getStringExtra("areaNumber");
        sensoroDevice = getIntent().getExtras().getParcelable("sensoroDevice");
        onetoOneConverter = getIntent().getExtras().getParcelable("onetoOneConverter");

        ba.mActivityLayoutId = R.layout.m_twoinit_activity_wulianwangduan_duqu;
        ba.mTitleText = "物联网端-读取";
    }

    @Override
    protected void initView() {
        super.initView();
        bluetoothName = (TextView) findViewById(R.id.bluetooth_name);
        factoryInfo = (TextView) findViewById(R.id.factory_info);
        systemStatus = (ImageView) findViewById(R.id.system_status);
        factoryDuqu = (TextView) findViewById(R.id.factory_duqu);
        unit = (RoundTextView) findViewById(R.id.unit);
        whole = (RoundTextView) findViewById(R.id.whole);
        hide = (LinearLayout) findViewById(R.id.hide);
        maichongchangshu = (TextView) findViewById(R.id.maichongchangshu);
        beilv = (Spinner) findViewById(R.id.beilv);
        chushimaichong = (EditText) findViewById(R.id.chushimaichong);
        chuchidushu = (TextView) findViewById(R.id.chuchidushu);
        xindaocanshu = (Spinner) findViewById(R.id.xindaocanshu);
        kuopinyinzi = (Spinner) findViewById(R.id.kuopinyinzi);
        inputMessage = (EditText) findViewById(R.id.input_message);
        caliber = (Spinner) findViewById(R.id.caliber);
        dataSet = (RoundTextView) findViewById(R.id.data_set);
        dataLoad = (RoundTextView) findViewById(R.id.data_load);
        ivSelector = (ImageView) findViewById(R.id.iv_selector);
        taskTest = (RoundTextView) findViewById(R.id.task_test);
        dataSave = (RoundTextView) findViewById(R.id.data_save);
        xiumian = (RoundTextView) findViewById(R.id.xiumian);
        textDuqu = (TextView) findViewById(R.id.text_duqu);


        systemStatus.setVisibility(View.GONE);//默认隐藏系统激活状态
        unit.setTextColor(getResources().getColor(R.color.base_white));//将单元文字置白
        unit.getDelegate().setBackgroundColor(getResources().getColor(R.color.l_five_Q));//将单元背景置红
        whole.setTextColor(getResources().getColor(R.color.l_five_Q));//将整表文字置红
        whole.getDelegate().setBackgroundColor(getResources().getColor(R.color.base_global_neutral_gray_c6c9d4));//将整表背景置白
        hide.setVisibility(View.VISIBLE);
        //模拟数据上送数据的弹窗
        View view = getLayoutInflater().inflate(R.layout.m_twoinit_test_upload_dialog, null);
        etInput = (EditText) view.findViewById(R.id.input);
        etInput.setText(LoginFiveParamManager.getInstance().getLoginData().getRegisterPhone());
        view.findViewById(R.id.send).setOnClickListener(this::onClick);
        alertDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        unit.setOnClickListener(this::onClick);
        whole.setOnClickListener(this::onClick);
        dataSet.setOnClickListener(this::onClick);
        dataLoad.setOnClickListener(this::onClick);
        dataSave.setOnClickListener(this::onClick);
        taskTest.setOnClickListener(this::onClick);
        xiumian.setOnClickListener(this::onClick);
        systemStatus.setOnClickListener(this::onClick);
        ivSelector.setOnClickListener(this::onClick);
        ivSelector.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onetoOneConverter.getImagePath() != null) {
                    ArrayList list = new ArrayList(1);
                    list.add(onetoOneConverter.getImagePath());
                    ARouter.getInstance().build(C.FullViewPictureActivity)
                            .withStringArrayList("paths", list)
                            .withBoolean("hasDelete", false)
                            .navigation(WulianwangduanduquActivity.this);
                }
                return true;
            }
        });
        chushimaichong.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (XHStringUtil.isEmpty(s.toString(), true)) {
                    chuchidushu.setText("");
                    return;
                }
                BigDecimal input = new BigDecimal(s.toString());
                String str = String.valueOf(beilv.getSelectedItem());
                BigDecimal decimal = new BigDecimal(str);
                decimal = decimal.multiply(input).setScale(4);
                String result = String.valueOf(decimal.stripTrailingZeros().toPlainString());
                chuchidushu.setText(result);
            }
        });
        beilv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = String.valueOf(beilv.getSelectedItem());
                BigDecimal decimal = new BigDecimal(str);
                maichongchangshu.setText(String.valueOf(new BigDecimal(1).divide(decimal).stripTrailingZeros().toPlainString()));
                String input = chushimaichong.getText().toString();
                if (XHStringUtil.isEmpty(input, true)) {
                    chuchidushu.setText("");
                    return;
                }
                decimal = decimal.multiply(new BigDecimal(input)).setScale(4);
                String result = String.valueOf(decimal.stripTrailingZeros().toPlainString());
                chuchidushu.setText(result);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        hashMap = new HashMap<>();
        if (onetoOneConverter != null) {
            GlideUtil.display(this, ivSelector, onetoOneConverter.getImagePath());
            canSave = true;
            StringBuilder sb = new StringBuilder(onetoOneConverter.getEquipmentTime());//2010-02-11
            setFactoryInfo(
                    onetoOneConverter.getSn(),
                    "1".equals(onetoOneConverter.getJoinForm()) ? "0" : "1",
                    onetoOneConverter.getSensingSignal(),
                    onetoOneConverter.getFrequency() + "上传一次",
                    sb.substring(2, 4),
                    sb.substring(5, 7),
                    sb.substring(8, 10),
                    onetoOneConverter.getManufacturersIdentification(),
                    onetoOneConverter.getFlowDirection(),
                    onetoOneConverter.getDisassemblyState(),
                    onetoOneConverter.getMagneticDisturb(),
                    "正常"
            );
            inputMessage.setText(onetoOneConverter.getRemark());

            if ("模式A".equalsIgnoreCase(onetoOneConverter.getChannel())) {
                xindaocanshu.setSelection(0);
            } else if ("模式B".equalsIgnoreCase(onetoOneConverter.getChannel())) {
                xindaocanshu.setSelection(1);
            } else if ("模式16".equalsIgnoreCase(onetoOneConverter.getChannel())) {
                xindaocanshu.setSelection(2);
            } else if ("模式32".equalsIgnoreCase(onetoOneConverter.getChannel())) {
                xindaocanshu.setSelection(3);
            } else if ("模式48".equalsIgnoreCase(onetoOneConverter.getChannel())) {
                xindaocanshu.setSelection(4);
            }
            kuopinyinzi.setSelection(LouShanYunUtils.getKPYZWriteCodeByString(onetoOneConverter.getSf()));
            try {
                Log.i("yunanhao", onetoOneConverter.toString());
                chushimaichong.setText(onetoOneConverter.getImpulseInitial());
                BigDecimal decimal = new BigDecimal(onetoOneConverter.getPulseConstant());
                String str = new BigDecimal(1).divide(decimal).stripTrailingZeros().toPlainString();
                String[] arr = getResources().getStringArray(R.array.m_twoinit_pulseconstant);
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i].equals(str)) {
                        beilv.setSelection(i);
                        break;
                    }
                }
                arr = getResources().getStringArray(R.array.m_twoinit_caliber);
                str = onetoOneConverter.getCaliber();
                for (int i = 0; i < arr.length; i++) {
                    if (arr[i].replaceAll("\\D+", "").equals(str)) {
                        caliber.setSelection(i);
                        break;
                    }
                }
            } catch (Exception e) {
            }
        } else {
            onetoOneConverter = new OnetoOneConverter();
            setFactoryInfo();
        }
        if (sensoroDevice != null) {
            canSave = false;
//            sensoroDeviceSession = new SensoroDeviceSession(this, sensoroDevice);
//            sensoroDeviceSession.startSession("eCkRN[r&E}ta.1kn", this);
            connectBlueTooth();
        }
    }

    /**
     * 设置出厂设置信息
     * 如果args为null则清空旧数据
     */
    private void setFactoryInfo(String... args) {
        StringBuilder sb = new StringBuilder();
        sb.append("物联SN: ");
        if (args != null && args.length > 0) {
            if (args[0] != null) {
                sb.append(args[0].toUpperCase());
            }
        }
        sb.append('\n');
        sb.append("接入形式: ");
        if (args != null && args.length > 1) {
            if ("0".equals(args[1])) {
                sb.append("模拟信号");
            } else if ("1".equals(args[1])) {
                sb.append("数字通讯");
            } else {

            }
        }
        sb.append('\n');
        sb.append("传感信号: ");
        if (args != null && args.length > 2) {
            sb.append(LouShanYunUtils.getCGXHReadStringByCode(Long.parseLong(args[2])));
        }
        sb.append('\n');
        sb.append("发送频率: ");
        if (args != null && args.length > 3) {
            try {
                sb.append(LouShanYunUtils.getFSPLReadStringByCode(Long.parseLong(args[3])));
            } catch (Exception e) {
                sb.append(args[3]);
            }
        }
        sb.append('\n');
        sb.append("出厂日期: ");
        if (args != null && args.length > 6) {
            sb.append("20");
            for (int i = args[4].length(); i < 2; i++) {
                sb.append("0");
            }
            sb.append(args[4]);
            sb.append("-");
            for (int i = args[5].length(); i < 2; i++) {
                sb.append("0");
            }
            sb.append(args[5]);
            sb.append("-");
            for (int i = args[6].length(); i < 2; i++) {
                sb.append("0");
            }
            sb.append(args[6]);
            sb.append(" 00:00:00");
        }
        sb.append('\n');
        sb.append("厂家标识: -");
        if (args != null && args.length > 7) {
            pushEventNoProgress(MInitTwoCode.GetChangJiaBiaoShiRunner, args[7]);
        }
        sb.append('\n');
        sb.append("流向: ");
        if (args != null && args.length > 8) {
            if ("0".equals(args[8])) {
                sb.append("正常");
            } else {
                sb.append("倒流");
            }
        }
        sb.append('\n');
        sb.append("传感器: ");
        if (args != null && args.length > 9) {
            if ("0".equals(args[9])) {
                sb.append("正常");
            } else {
                sb.append("无");
            }
        }
        sb.append('\n');
        sb.append("计量状态: ");
        if (args != null && args.length > 10) {
            if ("0".equals(args[10])) {
                sb.append("正常");
            }
        }
        sb.append('\n');
        sb.append("通讯状态: ");
        if (args != null && args.length > 11) {
            sb.append(args[11]);
        }
        sb.append('\n');
        sb.append("硬件版本号: ");
        if (args != null && args.length > 12) {
            sb.append(LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.硬件版本))));
        }
        sb.append('\n');
        sb.append("软件版本号: ");
        if (args != null && args.length > 13) {
            sb.append(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
        }
        factoryInfo.setText(sb);
    }

    @Override
    public void onClick(View v) {
        if (v == null) {
            return;
        }
        if (type != 2) {
            sendMessageToast("识别不是2号模组，请选择正确的模组");
            return;
        }
        int id = v.getId();
        if (id == R.id.unit) {
            unit.setTextColor(getResources().getColor(R.color.base_white));//将单元文字置白
            unit.getDelegate().setBackgroundColor(getResources().getColor(R.color.l_five_Q));//将单元背景置红
            whole.setTextColor(getResources().getColor(R.color.l_five_Q));//将整表文字置红
            whole.getDelegate().setBackgroundColor(getResources().getColor(R.color.base_global_neutral_gray_c6c9d4));//将整表背景置白
            hide.setVisibility(View.VISIBLE);
        } else if (id == R.id.whole) {
            unit.setTextColor(getResources().getColor(R.color.l_five_Q));//将单元文字置红
            unit.getDelegate().setBackgroundColor(getResources().getColor(R.color.base_global_neutral_gray_c6c9d4));//将单元背景置白
            whole.setTextColor(getResources().getColor(R.color.base_white));//将整表文字置白
            whole.getDelegate().setBackgroundColor(getResources().getColor(R.color.l_five_Q));//将整表背景置红
            hide.setVisibility(View.GONE);
        } else if (id == R.id.data_set) {
        } else if (id == R.id.iv_selector) {
            MatisseUtil.choosePicture(this, MimeType.ofImage(), 1, 99, "wu.loushanyun.com.fivemoduleapp.fileprovider");
        } else if (id == R.id.system_status) {
            if (isJiHuo) {
                closeJiHuo();
            } else {
                openJiHuo();
            }
        } else if (id == R.id.xiumian) {
            write(new byte[]{(byte) 0x68, (byte) 0x02, (byte) 0x25, (byte) 0x00, (byte) 0x27, (byte) 0x16}, "正在让设备进入休眠状态");
        } else if (id == R.id.task_test) {
            if (bluetoothName.getText().toString().equals("蓝牙名称：未连接")) {
                sendMessageToast("蓝牙未连接");
                return;
            }
            alertDialog.show();
        } else if (id == R.id.send) {
            alertDialog.dismiss();
            if (softVersion >= 4) {
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
                    write(d1, "正在发送命令，请稍后……", true, BLUE_SEND_TIME_OUT);
                }
            } else {
                try {
                    LoadingDialogUtil.show(loadingProgress, "连接服务器中，请稍后……");
                    String url = URLUtils.getSocketIP() + "/1/" + hashMap.get(MapParams.物联SN).toUpperCase();
                    Log.i("yunanhao", url);
                    client = new WebSocketClient(new URI(url), new Draft_17()) {

                        @Override
                        public void onClose(int arg0, String arg1, boolean arg2) {
                            Log.i("yunanhao", "onClose:" + arg0 + ";" + arg1 + ";" + arg2);
                            LoadingDialogUtil.dismissByEvent(WulianwangduanInitActivity.class.getName());
                        }

                        @Override
                        public void onError(Exception arg0) {
                            Log.i("yunanhao", "onError:" + arg0);
                            LoadingDialogUtil.dismissByEvent(WulianwangduanInitActivity.class.getName());
                            sendMessageToast("服务器出错，网络状况不佳，已跳过该步骤");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //点击后将按钮背景设置为灰色
                                    isUploadable = true;
                                    taskTest.getDelegate().setBackgroundColor(0x26000000);
                                    taskTest.setEnabled(false);
                                    client.close();
                                }
                            });
                        }

                        @Override
                        public void onMessage(final String arg0) {
                            Log.i("yunanhao", "onMessage:" + arg0);
                            sendMessageToast("服务器:" + arg0);
                            isUploadable = true;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //点击后将按钮背景设置为灰色
                                    taskTest.getDelegate().setBackgroundColor(0x26000000);
                                    taskTest.setEnabled(false);
                                    client.close();
                                }
                            });
                            LoadingDialogUtil.dismissByEvent(WulianwangduanInitActivity.class.getName());
                        }

                        @Override
                        public void onOpen(ServerHandshake arg0) {
                            Log.i("yunanhao", "onOpen:" + arg0);
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
                    Log.i("yunanhao", e.getMessage());
                }
            }

        }
    }

    private void closeJiHuo() {
        byte[] d = new byte[6];
        d[0] = (byte) 0x68;
        d[1] = (byte) 0x02;//有效数据
        d[2] = (byte) 0x24;//命令,设置成未激活
        d[3] = (byte) 0x00;//
        byte cs = 0;
        for (int i = 1; i < 4; i++) {
            cs += d[i];
        }
        d[4] = cs;//校验和
        d[5] = (byte) 0x16;
        if (sensoroDeviceSession != null) {
            write(d, "正在停用无线上传");
        }
    }

    private void openJiHuo() {
        byte[] d = new byte[6];
        d[0] = (byte) 0x68;
        d[1] = (byte) 0x02;//有效数据
        d[2] = (byte) 0x24;//命令,设置成未激活
        d[3] = (byte) 0x01;//
        byte cs = 0;
        for (int i = 1; i < 4; i++) {
            cs += d[i];
        }
        d[4] = cs;//校验和
        d[5] = (byte) 0x16;
        if (sensoroDeviceSession != null) {
            write(d, "正在激活无线上传");
        }
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == MInitTwoCode.GetChangJiaBiaoShiRunner) {
            if (event.isSuccess()) {
                String name = (String) event.getReturnParamAtIndex(0);
                if (XHStringUtil.isEmpty(name, true)) {
                    return;
                }
                String message = factoryInfo.getText().toString();
                onetoOneConverter.setFactoryName(name);
                factoryInfo.setText(message.replaceAll("厂家标识: -", "厂家标识: " + name));
            }
        }
    }

    @Override
    protected void initEventListener() {
        registerEventRunner(MInitTwoCode.GetChangJiaBiaoShiRunner, new GetChangJiaBiaoShiRunner());
    }

    @Override
    protected void onChildConnectFailed(int i) {
        Log.i("yunanhao", "onConnectFailed:" + i);
    }

    @Override
    protected void onChildConnectSuccess() {
        Log.i("yunanhao", "onConnectSuccess");
        runOnUiThread(() -> {
            SpannableString ss = new SpannableString("蓝牙名称：" + sensoroDevice.getSerialNumber() + " 已连接");
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(getResources().getColor(R.color.l_five_Q));
            ss.setSpan(colorSpan, 5, ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            bluetoothName.setText(ss);
        });
        Log.i("yunanhao", "send:" + DataParser.byteToString(DataParser.CMD_METER_INFO));
        write(DataParser.CMD_INFO_ALL, "识别模块中...", true, 6000);
    }

    @Override
    protected void onChildNotify(byte[] result) {
        String code = Integer.toHexString((result[2] ^ 0x80) & 0xFF);
        Log.i("yunanhao", "onNotify:" + DataParser.byteToString(result));
        switch (code) {
            case "5":
                //延时发送避免卡死
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int kpyz = LouShanYunUtils.getKPYZWriteCodeByString(String.valueOf(kuopinyinzi.getSelectedItem()));
                byte[] input = {0x68, 0x02, 0x04, (byte) kpyz, (byte) (0x06 + kpyz), 0x16};
                Log.i("yunanhao", "send:" + DataParser.byteToString(input));
                write(input, "设置中...");
                break;
            case "12":
                if (result[3] == 0) {
                    hashMap.put(MapParams.信号强度, String.valueOf(result[4]));
                    hashMap.put(MapParams.信噪比, String.valueOf(result[5]));
                }
                break;
            case "27":
                if (result[3] == 0) {
                    sendMessageToast("强制发送成功");
                    isUploadable = true;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //点击后将按钮背景设置为灰色
                            taskTest.getDelegate().setBackgroundColor(0x26000000);
                            taskTest.setEnabled(false);
                        }
                    });
                } else {
                    sendMessageToast("强制发送失败");
                }
                break;
            case "11":
                try {
                    type = DataParser.getModuleType(result);
                    if (type == 2) {
                        write(DataParser.CMD_METER_INFO, "读取设备信息");
                    } else {
                        sendMessageToast("识别不是2号模组，请选择正确的模组");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    sendMessageToast("识别失败");
                }
                break;
            case "2":
                //信道参数的设置
                int model = xindaocanshu.getSelectedItemPosition();
                if (model == 0) {
                    model = 0;
                } else if (model == 1) {
                    model = 1;
                } else if (model == 2) {
                    model = 0x11;
                } else if (model == 3) {
                    model = 0x21;
                } else if (model == 4) {
                    model = 0x31;
                }
                byte[] input1 = {0x68, 0x02, 0x05, (byte) model, (byte) (0x07 + model), 0x16};
                write(input1, "设置信道参数...");
                break;
            case "15":
                int gonglv = result[4] & 0xff;
                hashMap.put(MapParams.发送功率, String.valueOf(gonglv));
                setChuShiHuaText();
                write(DataParser.CMD_SYSTEM_STATUS, "正在读取设备状态");
                textDuqu.setVisibility(View.GONE);
                break;
            case "21":
                sendMessageToast("单元设置成功");
                break;
            case "22":
                hashMap.putAll(DataParser.getMoudleNo2(result));
                softVersion = Integer.parseInt(hashMap.get(MapParams.软件版本));
                runOnUiThread(() -> {
                    setChuShiHuaText();
                    setFactoryInfo(
                            hashMap.get(MapParams.物联SN),
                            hashMap.get(MapParams.信号类型),
                            hashMap.get(MapParams.传感信号),
                            hashMap.get(MapParams.发送频率),
                            hashMap.get(MapParams.出厂时间_年),
                            hashMap.get(MapParams.出厂时间_月),
                            hashMap.get(MapParams.出厂时间_日),
                            hashMap.get(MapParams.厂家标识),
                            hashMap.get(MapParams.表流向状态),
                            hashMap.get(MapParams.表拆卸状态),
                            hashMap.get(MapParams.表强磁状态),
                            "正常",
                            hashMap.get(MapParams.硬件版本),
                            hashMap.get(MapParams.软件版本)
                    );
                    Log.i("yunanhao", "send:" + DataParser.byteToString(DataParser.CMD_SYSTEM_STATUS));
                    if (softVersion >= 4) {
                        byte[] d = {0x68, 0x01, 0x15, 0x16, 0x16};
                        write(d, "读取发送功率");
                    } else {
                        hashMap.put(MapParams.发送功率, "");
                        write(DataParser.CMD_SYSTEM_STATUS, "正在读取设备状态");
                        textDuqu.setVisibility(View.GONE);
                    }
                });
                break;
            case "25":
                if (result[3] == 0) {
                    sendMessageToast("休眠成功");
                } else {
                    sendMessageToast("休眠失败");
                }
                break;
            case "24":
                if (result[3] == 0) {
                    if (isJiHuo) {
                        runOnUiThread(() -> {
                            //已激活
                            systemStatus.setVisibility(View.VISIBLE);
                            systemStatus.setImageDrawable(getResources().getDrawable(R.drawable.l_loushanyun_active_disable));
                            isJiHuo = false;
                        });
                    } else {
                        runOnUiThread(() -> {
                            //已激活
                            systemStatus.setVisibility(View.VISIBLE);
                            systemStatus.setImageDrawable(getResources().getDrawable(R.drawable.l_loushanyun_active_enable));
                            isJiHuo = true;
                        });
                    }
                }
                break;
            case "26":
                if (result[3] == 0) {
                    if ("1".equals(String.valueOf(result[4] & 0xff))) {
                        runOnUiThread(() -> {
                            //已激活
                            systemStatus.setVisibility(View.VISIBLE);
                            systemStatus.setImageDrawable(getResources().getDrawable(R.drawable.l_loushanyun_active_enable));
                            isJiHuo = true;
                        });
                    } else {
                        runOnUiThread(() -> {
                            //未激活
                            systemStatus.setVisibility(View.VISIBLE);
                            systemStatus.setImageDrawable(getResources().getDrawable(R.drawable.l_loushanyun_active_disable));
                            isJiHuo = false;
                        });
                    }
                } else {
                    runOnUiThread(() -> systemStatus.setVisibility(View.GONE));
                }
                break;
            case "40":
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (alertDialog.isShowing()) {
                            alertDialog.dismiss();
                        }
                    }
                });
                break;
            default:
        }
    }

    private void setChuShiHuaText() {
        StringBuilder sb = new StringBuilder();
        sb.append("脉冲常数(个/m³): ");
        BigDecimal decimal = new BigDecimal(LouShanYunUtils.getBLReadStringByCode(Integer.parseInt(hashMap.get(MapParams.倍率))));
        sb.append(new BigDecimal(1).divide(decimal).stripTrailingZeros().toPlainString());
        sb.append("\n倍率(m³/ev): ");
        sb.append(decimal.stripTrailingZeros().toPlainString());
        sb.append("\n初始脉冲(个): ");
        sb.append(hashMap.get(MapParams.当前脉冲读数));
        sb.append("\n初始读数(m³): ");
        sb.append(new BigDecimal(hashMap.get(MapParams.当前脉冲读数)).multiply(decimal).stripTrailingZeros().toPlainString());
        sb.append("\n信道参数: ");
        sb.append(LouShanYunUtils.getXDCSReadStringByCode02(hashMap.get(MapParams.信道参数)));
        sb.append("\n扩频因子: ");
        sb.append(LouShanYunUtils.getKPYZReadStringByCode(hashMap.get(MapParams.扩频因子)));
        if (softVersion >= 4) {
            sb.append("\n发送功率:  ");
            if (!XHStringUtil.isEmpty(hashMap.get(MapParams.发送功率), false)) {
                if (!XHStringUtil.isEmpty(hashMap.get(MapParams.发送功率), false)) {
                    sb.append(hashMap.get(MapParams.发送功率) + "dbm");
                }
            }

        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                factoryDuqu.setText(sb.toString());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 99) {
                String imagePath = Matisse.obtainPathResult(intent).get(0);
                GlideUtil.display(this, ivSelector, imagePath);
                String data = PictureUtil.bitmapToString(imagePath);
                onetoOneConverter.setImage(data);
                onetoOneConverter.setImagePath(imagePath);
                Log.i("yunanhao", "onActivityResult: " + imagePath);
                Log.i("yunanhao", "onActivityResult: " + data.length());

            }
        }
    }

}
