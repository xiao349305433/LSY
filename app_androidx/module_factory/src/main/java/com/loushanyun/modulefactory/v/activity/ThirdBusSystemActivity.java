package com.loushanyun.modulefactory.v.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Message;
import androidx.appcompat.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundLinearLayout;
import com.flyco.roundview.RoundTextView;
import com.gpsdk.demo.DeviceConnFactoryManager;
import com.loushanyun.modulefactory.R;
import com.loushanyun.modulefactory.init.FactoryCode;
import com.loushanyun.modulefactory.init.LoginParamManager;
import com.loushanyun.modulefactory.m.ThirdFactoryReturn;
import com.loushanyun.modulefactory.p.runner.IDCheckRunner;
import com.loushanyun.modulefactory.p.runner.LSYModule03UploadRunner;
import com.loushanyun.modulefactory.v.views.CommomDialog;
import com.suke.widget.SwitchButton;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.print.PrintTool;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.basemvp.v.activity.BaseBlueToothActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.baseevent.Event;
import met.hx.com.base.util.NoDoubleClickUtils;
import met.hx.com.librarybase.some_utils.LogUtils;
import met.hx.com.librarybase.some_utils.ToastManager;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.some_utils.util.AbSharedUtil;

@Route(path = K.ThirdBusSystemActivity)
public class ThirdBusSystemActivity extends BaseBlueToothActivity implements OnClickListener, RadioGroup.OnCheckedChangeListener {
    final int MSG_READSUCCESS = 2, MSG_SETFAILURE = 4, MSG_READSUCCESS1 = 5;

    private TextView tvBuleStateThird;
    private TextView tvWifiStateThird;
    private TextView tvEnterpriseName;
    private TextView tvEnterpriseCode;
    private TextView tvProductionTime;
    private SwitchButton selectorLock;
    private RadioGroup radioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton3;
    private TextView tvState1;
    private TextView tvState2;
    private TextView tvState3;
    private EditText edtBasicNum;
    private EditText edtBiaohao;
    private TextView tag1;
    private RadioGroup radioGroupRatio;
    private RadioButton radioButton4;
    private RadioButton radioButton5;
    private RadioButton radioButton6;
    private RadioButton radioButton7;
    private RadioButton radioButton8;
    private RadioButton radioButton9;
    private TextView tag2;
    private RadioGroup radioGroupRatio1;
    private RadioButton radioButton10;
    private RadioButton radioButton11;
    private RadioButton radioButton12;
    private RadioButton radioButton13;
    private RadioButton radioButton14;
    private RadioButton radioButton15;
    private TextView edtTime;
    private RoundTextView roundThirdCheck;
    private RoundTextView btnThirdRead;
    private RoundTextView btnFactorySure;
    private RoundTextView btnBasicNumberRead;
    private ImageView ivReadStatus;
    private TextView tvMeterNum;
    private TextView tvEquipmentID;
    private TextView tvStatue;
    private TextView tvRatio;
    private TextView tvSensoroType;
    private TextView tvVision;
    private TextView tvCompanyName;
    private TextView tvCompanyCode;
    private TextView tvProductionTime1;
    private ImageView ivInfoStatus;
    private TextView tvInfo1;
    private TextView tvInfo2;
    private TextView tvInfo5;
    private TextView tvInfo3;
    private RoundTextView tvSave;
    private RoundTextView tvPrint;
    private FrameLayout shadow;
    private RoundLinearLayout dialog;
    private TextView dialogName;
    private TextView dialogText;
    private View dialogLine;
    private TextView dialogConfirm;

    private String year = "";
    private String month = "";
    private String day = "";
    private int cjbs_wlwd;
    private CharSequence maichongdishu;
    private JSONObject json = new JSONObject();

    private PrintTool printTool;

    private boolean isLocked;
    private int hasReadCount = 0;
    private String readId;


    @Override
    protected void initLifeCycle() {
        printTool = new PrintTool(1, new PrintTool.PrintListener() {
            @Override
            public void onUsbPermission(Intent intent) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            System.out.println("permission ok for device " + device);
                        }
                    } else {
                        System.out.println("permission denied for device " + device);
                    }
                }
            }

            @Override
            public void onUsbDeviceDetached(Intent intent) {
            }

            @Override
            public void onAclDisconnected(Intent intent) {
            }

            @Override
            public void onConnectionState(Intent intent) {
                int state = intent.getIntExtra(DeviceConnFactoryManager.STATE, -1);
                int deviceId = intent.getIntExtra(DeviceConnFactoryManager.DEVICE_ID, -1);
                switch (state) {
                    case DeviceConnFactoryManager.CONN_STATE_DISCONNECT:
                        tvWifiStateThird.setText("打印机未连接");
                        break;
                    case DeviceConnFactoryManager.CONN_STATE_CONNECTING:
                        sendMessageToast(getString(com.wu.loushanyun.R.string.str_conn_state_connecting));
                        break;
                    case DeviceConnFactoryManager.CONN_STATE_CONNECTED:
                        sendMessageToast(getString(com.wu.loushanyun.R.string.str_conn_state_connected) + "\n" + printTool.getConnDeviceInfo());
                        tvWifiStateThird.setText("打印机已连接");
                        break;
                    case DeviceConnFactoryManager.CONN_STATE_FAILED:
                        sendMessageToast(getString(com.wu.loushanyun.R.string.str_conn_fail));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onConnectionStateDisconnect(Intent intent) {
            }

            @Override
            public void onConnectionStateConnecting(Intent intent) {
            }

            @Override
            public void onConnectionStateConnected(Intent intent) {
            }

            @Override
            public void onConnectionStateFailed(Intent intent) {
            }

            @Override
            public void onQueryPrinterState(Intent intent) {
            }
        });
        registerLifeCycle(printTool);
    }


    @Override
    protected void initView() {
        super.initView();
        connectBlueTooth(sensoroDevice);
        cjbs_wlwd = LoginParamManager.getInstance().getProductRegister().getManufacturersIdentification();
        tvBuleStateThird = (TextView) findViewById(R.id.tv_buleState_third);
        tvWifiStateThird = (TextView) findViewById(R.id.tv_wifiState_third);
        tvEnterpriseName = (TextView) findViewById(R.id.tv_enterpriseName);
        tvEnterpriseCode = (TextView) findViewById(R.id.tv_enterpriseCode);
        tvProductionTime = (TextView) findViewById(R.id.tv_productionTime);
        selectorLock = (SwitchButton) findViewById(R.id.selector_lock);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioButton1 = (RadioButton) findViewById(R.id.radio_button1);
        radioButton2 = (RadioButton) findViewById(R.id.radio_button2);
        radioButton3 = (RadioButton) findViewById(R.id.radio_button3);
        tvState1 = (TextView) findViewById(R.id.tv_state1);
        tvState2 = (TextView) findViewById(R.id.tv_state2);
        tvState3 = (TextView) findViewById(R.id.tv_state3);
        edtBasicNum = (EditText) findViewById(R.id.edt_basicNum);
        edtBiaohao = (EditText) findViewById(R.id.edt_biaohao);
        tag1 = (TextView) findViewById(R.id.tag1);
        radioGroupRatio = (RadioGroup) findViewById(R.id.radioGroup_ratio);
        radioButton4 = (RadioButton) findViewById(R.id.radio_button4);
        radioButton5 = (RadioButton) findViewById(R.id.radio_button5);
        radioButton6 = (RadioButton) findViewById(R.id.radio_button6);
        radioButton7 = (RadioButton) findViewById(R.id.radio_button7);
        radioButton8 = (RadioButton) findViewById(R.id.radio_button8);
        radioButton9 = (RadioButton) findViewById(R.id.radio_button9);
        tag2 = (TextView) findViewById(R.id.tag2);
        radioGroupRatio1 = (RadioGroup) findViewById(R.id.radioGroup_ratio1);
        radioButton10 = (RadioButton) findViewById(R.id.radio_button10);
        radioButton11 = (RadioButton) findViewById(R.id.radio_button11);
        radioButton12 = (RadioButton) findViewById(R.id.radio_button12);
        radioButton13 = (RadioButton) findViewById(R.id.radio_button13);
        radioButton14 = (RadioButton) findViewById(R.id.radio_button14);
        radioButton15 = (RadioButton) findViewById(R.id.radio_button15);
        edtTime = (TextView) findViewById(R.id.edt_time);
        roundThirdCheck = (RoundTextView) findViewById(R.id.round_third_check);
        btnThirdRead = (RoundTextView) findViewById(R.id.btn_thirdRead);
        btnFactorySure = (RoundTextView) findViewById(R.id.btn_factorySure);
        btnBasicNumberRead = (RoundTextView) findViewById(R.id.btn_basicNumberRead);
        ivReadStatus = (ImageView) findViewById(R.id.iv_read_status);
        tvMeterNum = (TextView) findViewById(R.id.tv_meterNum);
        tvEquipmentID = (TextView) findViewById(R.id.tv_equipmentID);
        tvStatue = (TextView) findViewById(R.id.tv_statue);
        tvRatio = (TextView) findViewById(R.id.tv_ratio);
        tvSensoroType = (TextView) findViewById(R.id.tv_sensoroType);
        tvVision = (TextView) findViewById(R.id.tv_vision);
        tvCompanyName = (TextView) findViewById(R.id.tv_companyName);
        tvCompanyCode = (TextView) findViewById(R.id.tv_companyCode);
        tvProductionTime1 = (TextView) findViewById(R.id.tv_productionTime1);
        ivInfoStatus = (ImageView) findViewById(R.id.iv_info_status);
        tvInfo1 = (TextView) findViewById(R.id.tv_info1);
        tvInfo2 = (TextView) findViewById(R.id.tv_info2);
        tvInfo5 = (TextView) findViewById(R.id.tv_info5);
        tvInfo3 = (TextView) findViewById(R.id.tv_info3);
        tvSave = (RoundTextView) findViewById(R.id.tv_save);
        tvPrint = (RoundTextView) findViewById(R.id.tv_print);
        shadow = (FrameLayout) findViewById(R.id.shadow);
        dialog = (RoundLinearLayout) findViewById(R.id.dialog);
        dialogName = (TextView) findViewById(R.id.dialog_name);
        dialogText = (TextView) findViewById(R.id.dialog_text);
        dialogLine = (View) findViewById(R.id.dialog_line);
        dialogConfirm = (TextView) findViewById(R.id.dialog_confirm);


        tvEnterpriseName.setText(LoginParamManager.getInstance().getProductRegister().getCompanyName());
        tvEnterpriseCode.setText(LoginParamManager.getInstance().getProductRegister().getSerialNumber());
        //生成时间戳
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
        String[] split = date.split("-");
        year = split[0].substring(split[0].length() - 2, split[0].length());
        month = split[1];
        day = split[2];
        tvProductionTime.setText(date);
        tvBuleStateThird.setOnClickListener(view -> {
            if (tvBuleStateThird.getText().toString().contains("点击连接")) {
                connectBlueTooth(sensoroDevice);
            }
        });
        tvPrint.setOnClickListener(this::onClick);
        roundThirdCheck.setOnClickListener(this::onClick);
        selectorLock.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if (!isLocked) {
                    lockAllButton(false);
                    selectorLock.setChecked(true);
                    isLocked = true;
                    AbSharedUtil.putInt(ThirdBusSystemActivity.this, "sensoroType", getSensoroType());
                    AbSharedUtil.putInt(ThirdBusSystemActivity.this, "ratio", getRatio());
                    AbSharedUtil.putBoolean(ThirdBusSystemActivity.this, "isLocked", true);
                } else {
                    lockAllButton(true);
                    selectorLock.setChecked(false);
                    isLocked = false;
                    AbSharedUtil.putInt(ThirdBusSystemActivity.this, "sensoroType", -1);
                    AbSharedUtil.putInt(ThirdBusSystemActivity.this, "ratio", -1);
                    AbSharedUtil.putBoolean(ThirdBusSystemActivity.this, "isLocked", false);
                }
            }
        });
        radioGroup.setOnCheckedChangeListener(this::onCheckedChanged);
        radioGroupRatio.setOnCheckedChangeListener(this::onCheckedChanged);
        radioGroupRatio1.setOnCheckedChangeListener(this::onCheckedChanged);

        shadow.setVisibility(View.GONE);
        ivReadStatus.setVisibility(View.INVISIBLE);
        ivInfoStatus.setVisibility(View.INVISIBLE);


        int sensoroType = AbSharedUtil.getInt(this, "sensoroType", -1);
        int ratio = AbSharedUtil.getInt(this, "ratio", -1);
        isLocked = AbSharedUtil.getBoolean(this, "isLocked", false);
        if (isLocked && sensoroType != -1 && ratio != -1) {
            selectorLock.setChecked(true);
            lockAllButton(false);
            XLog.i("阿斯达所大所多" + ratio);
            if (ratio == 0) {
                radioButton4.setChecked(true);
                radioButton10.setChecked(true);
            } else if (ratio == 1) {
                radioButton5.setChecked(true);
                radioButton11.setChecked(true);
            } else if (ratio == 2) {
                radioButton6.setChecked(true);
                radioButton12.setChecked(true);
            } else if (ratio == 3) {
                radioButton7.setChecked(true);
                radioButton13.setChecked(true);
            } else if (ratio == 4) {
                radioButton8.setChecked(true);
                radioButton14.setChecked(true);
            } else if (ratio == 5) {
                radioButton9.setChecked(true);
                radioButton15.setChecked(true);
            }
            if (sensoroType == 3) {
                radioButton1.setChecked(true);
                tvState1.setText("强磁");
                tvState2.setText("欠压");
                tvState3.setText("");
            } else if (sensoroType == 1) {
                radioButton2.setChecked(true);
                tvState1.setText("强磁");
                tvState2.setText("倒流");
                tvState3.setText("欠压");
            } else if (sensoroType == 10) {
                radioButton3.setChecked(true);
                tvState1.setText("强磁");
                tvState2.setText("倒流");
                tvState3.setText("欠压");
            }
        } else {
            selectorLock.setChecked(false);
            radioButton1.setChecked(true);
            radioButton2.setChecked(false);
            radioButton3.setChecked(false);
            tvState1.setText("强磁");
            tvState2.setText("欠压");
            tvState3.setText("");
            radioButton6.setChecked(true);
            radioButton12.setChecked(true);
        }


    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.activity_thirdbussystem;
        ba.mTitleText = "第三方自定义总线系统";
        ba.mTitleRightImageIcon = R.drawable.dy;
    }

    @Override
    protected void initEventListener() {
        registerEventRunner(FactoryCode.IDCheckRunner, new IDCheckRunner());
        registerEventRunner(FactoryCode.LSYModule03UploadRunner, new LSYModule03UploadRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int event_code) {
        if (event_code == FactoryCode.IDCheckRunner) {
            ThirdFactoryReturn thirdFactoryReturn = (ThirdFactoryReturn) event.getReturnParamAtIndex(0);
            String strCode = String.valueOf(thirdFactoryReturn.getCode());
            if ("0".equals(strCode)) {
                edtTime.setText(readId);
                sendMessageToast(thirdFactoryReturn.getMsg()+"可以配置");
            } else if ("-1".equals(strCode)) {
                if (hasReadCount == 0) {
                    new AlertDialog.Builder(this)
                            .setTitle("提示！！！")
                            .setMessage("该设备已出厂配置过了，是否更改其参数？\n该ID原数据：\n" + thirdFactoryReturn.getThirdfactorysettings().printThird())
                            .setPositiveButton("是(覆盖该ID下的原数据)", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    edtTime.setText(readId);
                                }
                            })
                            .setNegativeButton("否", null)
                            .setCancelable(false)
                            .show();
                }
            } else if ("-2".equals(strCode)) {
                if (hasReadCount == 0) {
                    new AlertDialog.Builder(this)
                            .setTitle("提示！！！")
                            .setMessage("该设备ID已被使用在其他厂家账号下，请登录到正确的账号进行出厂配置")
                            .setPositiveButton("好的", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
            }
        } else if (event_code == FactoryCode.LSYModule03UploadRunner) {
            boolean isPrinted = (boolean) event.getParamAtIndex(1);
            String rcode = String.valueOf(event.getReturnParamAtIndex(0));
            String result = String.valueOf(event.getReturnParamAtIndex(1));
            if ("0".equals(rcode)) {
                sendMessageToast("保存成功");
                print();
                hasReadCount = 0;
                btnThirdRead.setText("读取ID");
                clearAllText();
                return;
            } else if ("-1".equals(rcode)) {
                if (isPrinted) {
                    sendMessageToast("设备已经保存，正在打印");
                    print();
                    return;
                }
            }
            sendMessageToast(result, true);
        }
    }

    private void print() {
        if (PrintTool.canPrinted) {
            boolean flag = XHStringUtil.isEmpty(tvMeterNum.getText().toString().trim(), false);
            flag |= XHStringUtil.isEmpty(tvEquipmentID.getText().toString().trim(), false);
            flag |= XHStringUtil.isEmpty(tvStatue.getText().toString().trim(), false);
            flag |= XHStringUtil.isEmpty(tvRatio.getText().toString().trim(), false);
            flag |= XHStringUtil.isEmpty(tvSensoroType.getText().toString().trim(), false);
            flag |= XHStringUtil.isEmpty(tvVision.getText().toString().trim(), false);
            flag |= XHStringUtil.isEmpty(tvCompanyName.getText().toString().trim(), false);
            flag |= XHStringUtil.isEmpty(tvCompanyCode.getText().toString().trim(), false);
            flag |= XHStringUtil.isEmpty(tvProductionTime1.getText().toString().trim(), false);
            if (flag) {
                sendMessageToast("请读取完配置信息");
                return;
            }
            if (printTool.isConnected()) {
                printTool.print3(json.optString("ID"),
                        "20" + year,
                        month, day,
                        LoginParamManager.getInstance().getProductRegister().getCompanyName(), json.optString("传感类型"));
            } else {
                sendMessageToast("请连接打印机");
            }
        }
    }

    @Override
    public void onRightClick(View item) {
        if (PrintTool.canPrinted) {
            printTool.showPrintPopWindow(item);
        }
    }

    private void clearAllText() {
        tvMeterNum.setText("");
        tvEquipmentID.setText("");
        tvStatue.setText("");
        tvRatio.setText("");
        tvSensoroType.setText("");
        tvVision.setText("");
        tvCompanyName.setText("");
        tvCompanyCode.setText("");
        tvProductionTime1.setText("");
        tvInfo1.setText("");
        tvInfo2.setText("");
        tvInfo3.setText("");
        tvInfo5.setText("");
        edtTime.setText("");
    }

    /**
     * 获取倍率
     *
     * @return
     */
    private int getRatio() {
        int ratio = 0;
        if (radioButton4.isChecked()) {
            ratio = 0;
        } else if (radioButton5.isChecked()) {
            ratio = 1;
        } else if (radioButton6.isChecked()) {
            ratio = 2;
        } else if (radioButton7.isChecked()) {
            ratio = 3;
        } else if (radioButton8.isChecked()) {
            ratio = 4;
        } else if (radioButton9.isChecked()) {
            ratio = 5;
        }
        return ratio;
    }

    /**
     * 获取传感信号
     *
     * @return
     */
    private int getSensoroType() {
        int sensoroType = 0;
        if (radioButton1.isChecked()) {
            sensoroType = 3;
        } else if (radioButton2.isChecked()) {
            sensoroType = 1;
        } else if (radioButton3.isChecked()) {
            sensoroType = 10;
        }
        return sensoroType;
    }

    private void lockAllButton(boolean locked) {
        radioButton1.setEnabled(locked);
        radioButton2.setEnabled(locked);
        radioButton3.setEnabled(locked);
        radioButton4.setEnabled(locked);
        radioButton5.setEnabled(locked);
        radioButton6.setEnabled(locked);
        radioButton7.setEnabled(locked);
        radioButton8.setEnabled(locked);
        radioButton9.setEnabled(locked);
    }

    @Override
    public void onClick(View v) {
        if (NoDoubleClickUtils.isDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.tv_print:
                pushToSave(true);
                break;
            case R.id.tv_save://确定按钮点击事件
                pushToSave(false);
                break;
            case R.id.round_third_check:
                if (!XHStringUtil.isEmpty(edtTime.getText().toString(), false)) {
                    pushEvent(FactoryCode.IDCheckRunner, edtTime.getText().toString(), LoginParamManager.getInstance().getProductRegister().getManufacturersIdentification());
                } else {
                    sendMessageToast("请先读取设备中的ID");
                }
                break;
            case R.id.btn_thirdRead://读取按钮点击事件
                if (tvBuleStateThird.getText().toString().trim().equals("抄表盒子未连接,点击连接")) {
                    Toast.makeText(this, "请先连接蓝牙", Toast.LENGTH_SHORT).show();
                    return;
                }
                int biaohao = Integer.valueOf(edtBiaohao.getText().toString());
                byte[] d1 = new byte[11];
                d1[0] = (byte) 0xfe;
                d1[1] = (byte) 0xfe;
                d1[2] = (byte) 0xfe;
                d1[3] = (byte) 0x68;
                d1[4] = (byte) 0x03;//有效数据
                d1[5] = (byte) 0x00;
                d1[6] = (byte) 0x0b;//命令
                d1[7] = (byte) 0x03;//表类型
                d1[8] = (byte) biaohao;//表号
                byte cs1 = 0;
                for (int i = 4; i < 9; i++) {
                    cs1 += d1[i];
                }
                d1[9] = cs1;//校验和
                d1[10] = (byte) 0x16;
                if (sensoroDeviceSession != null) {
                    write(d1, "正在读取配置信息", true, 200);
                }
                break;
            case R.id.btn_factorySure://出厂配置确定按钮点击事件
                if (tvBuleStateThird.getText().toString().trim().equals("抄表盒子未连接,点击连接")) {
                    Toast.makeText(this, "请先连接蓝牙", Toast.LENGTH_SHORT).show();
                    return;
                }
                String equipmentID = edtTime.getText().toString().trim();
                int tableNumber = Integer.valueOf(edtBiaohao.getText().toString());
                String basicNum = edtBasicNum.getText().toString().trim();
                String enterpriseCode = tvEnterpriseCode.getText().toString().trim();
                String substring = enterpriseCode.substring(0, enterpriseCode.length() - 1);
                String substring2 = enterpriseCode.substring(enterpriseCode.length() - 1, enterpriseCode.length());
                String valueOf = String.valueOf(Integer.parseInt(substring2, 16));
                String Code = substring + valueOf;
                if (XHStringUtil.isEmpty(equipmentID, false)) {
                    ToastManager.getInstance(this).show("请输入设备ID");
                    return;
                }
                if (XHStringUtil.isEmpty(basicNum, false)) {
                    ToastManager.getInstance(this).show("请输入脉冲底数");
                    return;
                }
                byte[] d = new byte[37];
                d[0] = (byte) 0xfe;
                d[1] = (byte) 0xfe;
                d[2] = (byte) 0xfe;
                d[3] = (byte) 0x68;
                d[4] = (byte) 0x1d;//有效数据
                d[5] = (byte) 0x00;
                d[6] = (byte) 0x09;//命令
                d[7] = (byte) 0x03;//表类型
                long isn = Long.parseLong(equipmentID);
                for (int i = 0; i < 6; i++) {
                    d[i + 8] = (byte) (isn & 0xff);
                    isn = isn >> 8;
                }
                d[14] = (byte) tableNumber;
                d[15] = (byte) 0xAA;
                d[16] = (byte) 0x02;
                d[17] = (byte) getRatio();
                d[18] = (byte) 0x08;
                d[19] = (byte) getSensoroType();//传感类型
                d[20] = (byte) Integer.parseInt(year);//生产时间
                d[21] = (byte) Integer.parseInt(month);
                d[22] = (byte) Integer.parseInt(day);
                long code = Long.parseLong(Code);//企业代码
                for (int i = 0; i < 6; i++) {
                    d[i + 23] = (byte) (code & 0xff);
                    code = code >> 8;
                }
                d[29] = (byte) 0x07;
                d[30] = (byte) 0x00;
                d[31] = (byte) 0x04;
                d[32] = (byte) (Integer.parseInt(basicNum) & 0xff);
                d[33] = (byte) ((Integer.parseInt(basicNum) & 0xff00) / 0x0100);
                d[34] = (byte) ((Integer.parseInt(basicNum) & 0xff0000) / 0x010000);
                byte cs = 0;
                for (int i = 4; i < 35; i++) {
                    cs += d[i];
                }
                d[35] = cs;//校验和
                d[36] = (byte) 0x16;
                if (sensoroDeviceSession != null) {
                    write(d, "正在写入数据", true, 5000);
                }
                break;
            case R.id.btn_basicNumberRead:
                if (tvBuleStateThird.getText().toString().trim().equals("抄表盒子未连接,点击连接")) {
                    Toast.makeText(this, "请先连接蓝牙", Toast.LENGTH_SHORT).show();
                    return;
                }
                int tableNumber1 = Integer.valueOf(edtBiaohao.getText().toString());
                byte[] d2 = new byte[11];
                d2[0] = (byte) 0xfe;
                d2[1] = (byte) 0xfe;
                d2[2] = (byte) 0xfe;
                d2[3] = (byte) 0x68;
                d2[4] = (byte) 0x03;//有效数据
                d2[5] = (byte) 0x00;
                d2[6] = (byte) 0x0c;//命令
                d2[7] = (byte) 0x03;//表类型
                d2[8] = (byte) tableNumber1;//表号
                byte cs2 = 0;
                for (int i = 4; i < 9; i++) {
                    cs2 += d2[i];
                }
                d2[9] = cs2;//校验和
                d2[10] = (byte) 0x16;
                if (sensoroDeviceSession != null) {
                    write(d2, "正在读取脉冲数", true, 200);
                }
                break;
            default:
                break;
        }
    }

    private void pushToSave(boolean isPrinted) {
        if (PrintTool.canPrinted) {
            if (!printTool.isConnected()) {
                sendMessageToast("请连接打印机");
                return;
            }
        }
        if (XHStringUtil.isEmpty(tvVision.getText().toString(), false)) {
            sendMessageToast("请读取配置信息");
            return;
        }
        if (Double.valueOf(tvVision.getText().toString()) < 1.07) {
            sendMessageToast("只能出厂1.07以上的版本");
            return;
        }
        JSONObject modelInfo = new JSONObject();
        int sensoroCode;
        try {
            boolean flag = XHStringUtil.isEmpty(tvMeterNum.getText().toString().trim(), false);
            flag |= XHStringUtil.isEmpty(tvEquipmentID.getText().toString().trim(), false);
            flag |= XHStringUtil.isEmpty(tvStatue.getText().toString().trim(), false);
            flag |= XHStringUtil.isEmpty(tvRatio.getText().toString().trim(), false);
            flag |= XHStringUtil.isEmpty(tvSensoroType.getText().toString().trim(), false);
            flag |= XHStringUtil.isEmpty(tvInfo5.getText().toString().trim(), false);
            flag |= XHStringUtil.isEmpty(tvVision.getText().toString().trim(), false);
            flag |= XHStringUtil.isEmpty(tvCompanyName.getText().toString().trim(), false);
            flag |= XHStringUtil.isEmpty(tvCompanyCode.getText().toString().trim(), false);
            flag |= XHStringUtil.isEmpty(tvProductionTime1.getText().toString().trim(), false);

            if (flag) {
                ToastManager.getInstance(this).show("请读取完配置信息");
                return;
            }
            String sensoroType = tvSensoroType.getText().toString().trim();
            if (sensoroType.equals("3EV")) {
                sensoroCode = 1;
            } else if (sensoroType.equals("2EV")) {
                sensoroCode = 3;
            } else {
                sensoroCode = 2;
            }
            if ("0".equalsIgnoreCase(tvEquipmentID.getText().toString().trim())) {
                sendMessageToast("设备ID不能为0");
                return;
            }
            modelInfo.put("userid", tvEquipmentID.getText().toString().trim());
            modelInfo.put("sensingsignal", String.valueOf(sensoroCode));
            modelInfo.put("rate", tvRatio.getText().toString().trim().split("/")[0]);
            modelInfo.put("status", tvStatue.getText().toString().trim());
            modelInfo.put("firmwareversion", tvVision.getText().toString().trim());
            modelInfo.put("date", tvProductionTime1.getText().toString().trim());
            modelInfo.put("manufacturersidentification", String.valueOf(cjbs_wlwd));
            float impulseInitial = Float.valueOf(tvInfo5.getText().toString().trim());
            modelInfo.put("impulseInitial", impulseInitial);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        pushEvent(FactoryCode.LSYModule03UploadRunner, modelInfo.toString(), isPrinted);
    }

    @Override
    protected void onLoadingDismissTimeOut() {
    }

    /**
     * 通过handler发送消息来执行相应的操作
     */
    @SuppressLint("HandlerLeak")
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_READSUCCESS) {

                String substring = json.optString("企业代码").substring(0, json.optString("企业代码").length() - 2);
                String substring2 = json.optString("企业代码").substring(json.optString("企业代码").length() - 2, json.optString("企业代码").length());
                char letter = Integer.toHexString(Integer.parseInt(substring2)).charAt(0);//字符格式转换成字母格式
                char letter1 = (char) (letter - 32);
                tvMeterNum.setText(json.optString("表号"));
                tvEquipmentID.setText(json.optString("ID"));
                tvStatue.setText(json.optString("状态"));
                String string = new BigDecimal(1).divide(new BigDecimal(json.optString("倍率"))).stripTrailingZeros().toPlainString();
                tvRatio.setText(json.optString("倍率") + "/" + string);
                tvSensoroType.setText(json.optString("传感类型"));
                tvVision.setText(json.optString("软件版本号"));
                tvCompanyName.setText(LoginParamManager.getInstance().getProductRegister().getCompanyName());
                tvCompanyCode.setText(substring + String.valueOf(letter1));
                tvProductionTime1.setText(json.optString("出厂时间"));
                ivReadStatus.setVisibility(View.VISIBLE);
                ivReadStatus.setImageResource(R.drawable.gg);
            } else if (msg.what == MSG_READSUCCESS1) {
                ToastManager.getInstance(ThirdBusSystemActivity.this).show("读取成功");
                long num = json.optLong("脉冲数");
                tvInfo5.setText(String.valueOf(num));
                if (!XHStringUtil.isEmpty(String.valueOf(maichongdishu), false)) {
                    tvInfo1.setText(maichongdishu);
                    num = num - Long.parseLong(String.valueOf(tvInfo1.getText()));
                }
                tvInfo2.setText(String.valueOf(num));
                if (!XHStringUtil.isEmpty(json.optString("倍率"), false)) {
                    tvInfo3.setText(new BigDecimal(json.optString("脉冲数", "0"))
                            .multiply(new BigDecimal(json.optString("倍率", "0")))
                            .stripTrailingZeros().toPlainString());
                }
                ivInfoStatus.setVisibility(View.VISIBLE);
                ivInfoStatus.setImageResource(R.drawable.gg);
            } else if (msg.what == MSG_SETFAILURE) {
                //弹出提示框
                sendMessageToast("操作失败", true);
            }
        }
    };

    @Override
    public void onChildNotify(byte[] output) {
        String hexString = Integer.toHexString(output[6] & 0xFF);
        if (hexString.equals("89")) {
            if (output[7] == 0) {
                sendMessageToast("设置成功", true);
                maichongdishu = edtBasicNum.getText();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hasReadCount++;
                        btnThirdRead.setText("读取信息");
                        clearAllText();
                    }
                });
            } else {
                sendMessageToast("设置失败");
            }
        } else if (hexString.equals("8b")) {
            if (output[7] == 0) {
                try {
                    //解析 start
                    long tmp = (int) output[8];
                    json.put("表号", tmp);
                    tmp = 0;
                    for (int i = 7; i > 0; i--) {
                        tmp = (tmp * 256) + (int) (output[i + 8] & 0xff);
                    }
                    json.put("ID", tmp);
                    readId = String.valueOf(tmp);
                    tmp = (int) output[16];
                    if (tmp == 0) {
                        json.put("倍率", 0.001);
                    } else if (tmp == 1) {
                        json.put("倍率", 0.01);
                    } else if (tmp == 2) {
                        json.put("倍率", 0.1);
                    } else if (tmp == 3) {
                        json.put("倍率", 1);
                    } else if (tmp == 4) {
                        json.put("倍率", 10);
                    } else if (tmp == 5) {
                        json.put("倍率", 100);
                    } else if (tmp == 6) {
                        json.put("倍率", 1000);
                    } else if (tmp == 7) {
                        json.put("倍率", 0.0001);
                    }

                    tmp = (int) output[17] & 0xff;
                    json.put("状态", LouShanYunUtils.getZTReadStringByCode(String.valueOf(tmp)));
                    tmp = (int) output[18];
                    json.put("传感类型", LouShanYunUtils.getCGXHReadStringByCode(tmp));
                    tmp = (int) output[19];
                    json.put("软件版本号", LouShanYunUtils.getSoftWareVersion((int) tmp));
                    tmp = 0;
                    for (int i = 6; i > 0; i--) {
                        tmp = (tmp * 256) + (int) (output[i + 19] & 0xff);
                    }
                    json.put("企业代码", tmp);
                    String year = String.valueOf(output[26] & 0xff);
                    String month = String.valueOf(output[27] & 0xff);
                    if (month.length() == 1) {
                        month = "0" + month;
                    }
                    String day = String.valueOf(output[28] & 0xff);
                    json.put("出厂时间", "20" + year + "-" + month + "-" + day /*+ " " + "00:00:00"*/);
                    LogUtils.i("数据==" + json.toString());
                    if (hasReadCount == 0) {
                        getIDByTime(readId);
                    } else {
                        //解析 end
                        if (json != null) {
                            Message msg = handler.obtainMessage();
                            msg.what = MSG_READSUCCESS;
                            msg.sendToTarget();
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ivInfoStatus.setVisibility(View.VISIBLE);
                                    ivInfoStatus.setImageResource(R.drawable.ts);
                                }
                            });
                        }
                    }

                } catch (JSONException e) {
                    sendMessageToast("操作失败");
                    e.printStackTrace();
                }
            } else {
                sendMessageToast("操作失败");
            }
        } else if (hexString.equals("8c")) {
            if (output[7] == 0) {
                try {
                    long tmp = 0;
                    for (int i = 3; i > 0; i--) {
                        tmp = tmp * 256 + (int) (output[i + 7] & 0xff);
                    }
                    json.put("脉冲数", tmp);
                    if (json != null) {
                        Message msg = handler.obtainMessage();
                        msg.what = MSG_READSUCCESS1;
                        msg.sendToTarget();
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ivInfoStatus.setVisibility(View.VISIBLE);
                                ivInfoStatus.setImageResource(R.drawable.ts);
                            }
                        });
                    }
                } catch (JSONException e) {
                    sendMessageToast("操作失败");
                    e.printStackTrace();
                }
            } else {
                sendMessageToast("操作失败");
            }
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
                tvBuleStateThird.setText("抄表盒子已连接");
                hasReadCount = 0;
                btnThirdRead.setText("读取ID");
                clearAllText();
            }
        });
    }

    @Override
    protected void onChildWriteSuccess() {

    }

    @Override
    protected void onChildWriteFailure(int i) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new CommomDialog(ThirdBusSystemActivity.this, R.style.dialog, "操作失败，请检查蓝牙是否已连接", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            dialog.dismiss();
                        }
                    }
                }).setTitle("温馨提示").show();
            }
        });
    }

    private String getIDByTime(String readIdLong) {
        if (!"0".equals(readIdLong)) {
            readId = String.valueOf(readIdLong);
            pushEvent(FactoryCode.IDCheckRunner, readId, LoginParamManager.getInstance().getProductRegister().getManufacturersIdentification());
        } else {
            //生成时间戳
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");//设置日期格式
            readId = sdf.format(System.currentTimeMillis());//使用当前时间戳
            pushEvent(FactoryCode.IDCheckRunner, readId, LoginParamManager.getInstance().getProductRegister().getManufacturersIdentification());
        }
        return readId;
    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        if (checkedId == R.id.radio_button1) {
            radioButton1.setChecked(true);
            radioButton2.setChecked(false);
            radioButton3.setChecked(false);
            tvState1.setText("倒流");
            tvState2.setText("欠压");
            tvState3.setText("");
            if (!isLocked) {
                radioButton6.setChecked(true);
                radioButton12.setChecked(true);
            }
        } else if (checkedId == R.id.radio_button2) {
            radioButton1.setChecked(false);
            radioButton2.setChecked(true);
            radioButton3.setChecked(false);
            tvState1.setText("强磁");
            tvState2.setText("倒流");
            tvState3.setText("欠压");
            if (!isLocked) {
                radioButton6.setChecked(true);
                radioButton12.setChecked(true);
            }
        } else if (checkedId == R.id.radio_button3) {
            radioButton1.setChecked(false);
            radioButton2.setChecked(false);
            radioButton3.setChecked(true);
            tvState1.setText("倒流");
            tvState2.setText("欠压");
            tvState3.setText("");
            if (!isLocked) {
                radioButton4.setChecked(true);
                radioButton10.setChecked(true);
            }
        } else if (checkedId == R.id.radio_button4) {
            radioButton10.setChecked(true);
        } else if (checkedId == R.id.radio_button5) {
            radioButton11.setChecked(true);
        } else if (checkedId == R.id.radio_button6) {
            radioButton12.setChecked(true);
        } else if (checkedId == R.id.radio_button7) {
            radioButton13.setChecked(true);
        } else if (checkedId == R.id.radio_button8) {
            radioButton14.setChecked(true);
        } else if (checkedId == R.id.radio_button9) {
            radioButton15.setChecked(true);
        } else if (checkedId == R.id.radio_button10) {
            radioButton4.setChecked(true);
        } else if (checkedId == R.id.radio_button11) {
            radioButton5.setChecked(true);
        } else if (checkedId == R.id.radio_button12) {
            radioButton6.setChecked(true);
        } else if (checkedId == R.id.radio_button13) {
            radioButton7.setChecked(true);
        } else if (checkedId == R.id.radio_button14) {
            radioButton8.setChecked(true);
        } else if (checkedId == R.id.radio_button15) {
            radioButton9.setChecked(true);
        }
    }


}
