package com.loushanyun.modulefactory.v.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.roundview.RoundTextView;
import com.gpsdk.demo.DeviceConnFactoryManager;
import com.loushanyun.modulefactory.R;
import com.loushanyun.modulefactory.init.FactoryCode;
import com.loushanyun.modulefactory.init.LoginParamManager;
import com.loushanyun.modulefactory.m.FactoryData;
import com.loushanyun.modulefactory.p.runner.GetManufacturersIdentificationRunner;
import com.loushanyun.modulefactory.p.runner.UploadDataRunner;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.print.PrintTool;
import com.wu.loushanyun.base.url.URLUtils;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.basemvp.v.activity.BaseBlueToothActivity;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.net.URI;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.baseevent.Event;
import met.hx.com.base.util.NoDoubleClickUtils;
import met.hx.com.librarybase.some_utils.KeyboardUtils;
import met.hx.com.librarybase.some_utils.LogUtils;
import met.hx.com.librarybase.some_utils.RegexUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;

//二号模组
@Route(path = K.ReadInfoWlwdActivity)
public class ReadInfoWlwdActivity extends BaseBlueToothActivity implements View.OnClickListener {
    final int MSG_CONN = 1, MSG_SETSUCCESS = 2, MSG_READ = 3, MSG_SEND = 4, MSG_CLOSESUCCESS = 5, MSG_SEND_AFTER = 6;
    TextView tv_mnxh_read, tv_wldc_read, tv_ccgr_read, tv_cx_read, tv_dl_read, tv_qy_read, tv_cgxh_read, tv_fsgl_read_wlwd,
            tv_clfs_read, tv_mk_read, tv_cpsx_read, tv_wlsn_read, tv_time_read, tv_yjbb_read, tv_rjbb_read, tv_power_type, tv_cjbs_read, tv_fmzt_read_wlwd, tv_fspl_read_wlwd, tv_wxpl_read_wlwd, tv_spreadingFactor, tv_channel, tv_basicNumber;
    private LinearLayout ll_totalstateInfo, ll_powerType, ll_changjiabiaoshi, ll_maikuang;
    JSONObject json;
    Button btn_read;
    private String redata = "";
    private int sensingSignal = 0;
    private TextView tvRSS;
    private TextView tvSNR;
    private TextView tvMultiplyingPower;
    private RoundTextView tvPrint;
    private RoundTextView tvHome;
    private ImageView iv_system_status;
    private boolean isJiHuo = true;
    private TextView text1;
    private EditText edtCellphonenumber;
    private boolean jiZhanCeShi = false;
    private boolean hidecontrolview;
    private ScrollView scrollview;
    private WebSocketClient client;
    private int softVersion;
    private String year, month, day;
    private TextView tv_gjbb_read_wlwd;
    private String gjBanBen;
    private TextView tvPrintState;
    private PrintTool printTool;
    private boolean canPrintRepeat;
    private RoundTextView round_text_maichong;

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
                        tvPrintState.setText("打印机未连接");
                        break;
                    case DeviceConnFactoryManager.CONN_STATE_CONNECTING:
                        sendMessageToast(getString(com.wu.loushanyun.R.string.str_conn_state_connecting));
                        break;
                    case DeviceConnFactoryManager.CONN_STATE_CONNECTED:
                        sendMessageToast(getString(com.wu.loushanyun.R.string.str_conn_state_connected) + "\n" + printTool.getConnDeviceInfo());
                        tvPrintState.setText("打印机已连接");
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView();
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.activity_industryreadinfo_wlwd;
        hidecontrolview = getIntent().getBooleanExtra("hidecontrol", false);
        if (!hidecontrolview) {
            ba.mTitleText = "读取";
            ba.mTitleRightImageIcon = R.drawable.dy;
        }
    }

    @Override
    protected void initEventListener() {
        registerEventRunner(FactoryCode.UploadDataRunner, new UploadDataRunner());
        registerEventRunner(FactoryCode.GetManufacturersIdentificationRunner, new GetManufacturersIdentificationRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == FactoryCode.UploadDataRunner) {
            int codeReturn = (int) event.getReturnParamAtIndex(0);
            if (codeReturn == 0) {
                canPrintRepeat = true;
                print();
                Message msg = handler.obtainMessage();
                msg.what = MSG_CLOSESUCCESS;
                msg.sendToTarget();
            }
            sendMessageToast((String) event.getReturnParamAtIndex(1), true);
        } else if (code == FactoryCode.GetManufacturersIdentificationRunner) {
            if (event.isSuccess()) {
                String str = (String) event.getReturnParamAtIndex(0);
                try {
                    json.put("厂家标识", str);
                    tv_cjbs_read.setText(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 让设备休眠
     */
    private void close() {
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

    private void open() {
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
    public void onRightClick(View item) {
        if (NoDoubleClickUtils.isDoubleClick()) {
            return;
        }
        printTool.showPrintPopWindow(item);
    }

    private void setView() {
        connectBlueTooth(sensoroDevice);
        tv_mnxh_read = (TextView) findViewById(R.id.tv_mnxh_read_wlwd);
        tvPrintState = (TextView) findViewById(R.id.tv_printState);
        tv_wldc_read = (TextView) findViewById(R.id.tv_wldc_read);
        tv_ccgr_read = (TextView) findViewById(R.id.tv_ccgr_read_wlwd);
        tv_cx_read = (TextView) findViewById(R.id.tv_cx_read_wlwd);
        tv_dl_read = (TextView) findViewById(R.id.tv_dl_read_wlwd);
        tv_qy_read = (TextView) findViewById(R.id.tv_qy_read_wlwd);
        tv_cgxh_read = (TextView) findViewById(R.id.tv_cgxh_read_wlwd);
        tv_fsgl_read_wlwd = (TextView) findViewById(R.id.tv_fsgl_read_wlwd);
        tv_gjbb_read_wlwd = (TextView) findViewById(R.id.tv_gjbb_read_wlwd);
        tv_clfs_read = (TextView) findViewById(R.id.tv_clfs_read_wlwd);
        tv_mk_read = (TextView) findViewById(R.id.tv_mk_read_wlwd);
        tv_cpsx_read = (TextView) findViewById(R.id.tv_cpsx_read_wlwd);
        tv_wlsn_read = (TextView) findViewById(R.id.tv_wlsn_read_wlwd);
        tv_time_read = (TextView) findViewById(R.id.tv_time_read_wlwd);
        tv_yjbb_read = (TextView) findViewById(R.id.tv_yjbb_read_wlwd);
        tv_rjbb_read = (TextView) findViewById(R.id.tv_rjbb_read_wlwd);
        tv_power_type = (TextView) findViewById(R.id.tv_power_type);
        tv_cjbs_read = (TextView) findViewById(R.id.tv_cjbs_read_wlwd);
        tv_fmzt_read_wlwd = (TextView) findViewById(R.id.tv_fmzt_read_wlwd);
        tv_fspl_read_wlwd = (TextView) findViewById(R.id.tv_fspl_read_wlwd);
        tv_wxpl_read_wlwd = (TextView) findViewById(R.id.tv_wxpl_read_wlwd);
        round_text_maichong = (RoundTextView) findViewById(R.id.round_text_maichong);
        tv_spreadingFactor = (TextView) findViewById(R.id.tv_spreadingFactor);
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        tv_channel = (TextView) findViewById(R.id.tv_channel);
        tv_basicNumber = (TextView) findViewById(R.id.tv_basicNumber);
        tvRSS = (TextView) findViewById(R.id.tv_RSS);
        tvSNR = (TextView) findViewById(R.id.tv_SNR);
        tvMultiplyingPower = (TextView) findViewById(R.id.tv_multiplying_power);
        tvPrint = (RoundTextView) findViewById(R.id.tv_print);
        tvHome = (RoundTextView) findViewById(R.id.tv_home);
        ll_totalstateInfo = (LinearLayout) findViewById(R.id.ll_totalstateInfo);
        ll_powerType = (LinearLayout) findViewById(R.id.ll_powerType);
        ll_changjiabiaoshi = (LinearLayout) findViewById(R.id.ll_changjiabiaoshi);
        ll_maikuang = (LinearLayout) findViewById(R.id.ll_maikuang);
        text1 = (TextView) findViewById(R.id.text_1);
        edtCellphonenumber = (EditText) findViewById(R.id.edt_cellphonenumber);
        iv_system_status = (ImageView) findViewById(R.id.iv_system_status);
        round_text_maichong.setOnClickListener(this);
        edtCellphonenumber.setText(LoginParamManager.getInstance().getProductRegister().getCompanyPhone());
        if (hidecontrolview) {
            findViewById(R.id.control).setVisibility(View.GONE);
        }

        text1.setOnClickListener(v -> {
            if (softVersion >= 4) {
                String phone = edtCellphonenumber.getText().toString().trim();
                if (XHStringUtil.isEmpty(phone, false)) {
                    sendMessageToast("请输入手机号");
                    return;
                }
                if (!RegexUtils.isMobileSimple(phone)) {
                    sendMessageToast("请输入正确的手机号");
                    return;
                }
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
                    String url = URLUtils.getSocketIP() + "/1/" + sensoroDevice.getSerialNumber();
                    LogUtils.i("发送的url=" + url);
                    client = new WebSocketClient(new URI(url), new Draft_17()) {

                        @Override
                        public void onClose(int arg0, String arg1, boolean arg2) {
                            LoadingDialogUtil.dismissByEvent(ReadInfoWlwdActivity.this.getClass().getName());
                        }

                        @Override
                        public void onError(Exception arg0) {
                            LoadingDialogUtil.dismissByEvent(ReadInfoWlwdActivity.this.getClass().getName());
                            sendMessageToast("服务器出错");
                            LogUtils.e(arg0);
                        }

                        @Override
                        public void onMessage(final String arg0) {
                            sendMessageToast(arg0);
                            LoadingDialogUtil.dismissByEvent(ReadInfoWlwdActivity.this.getClass().getName());
                            jiZhanCeShi = true;
                            client.close();
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
        });
        if (!hidecontrolview) {
            iv_system_status.setOnClickListener(v -> {
                if (isJiHuo) {
                    close();
                } else {
                    open();
                }
            });
        }
    }

    private void readPhoneNumber() {
        String phone = edtCellphonenumber.getText().toString().trim();
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

    @Override
    public void onChildNotify(byte[] output) {
        LoadingDialogUtil.dismissByEvent(this.getClass().getName());
        //当连接的1号模组，返回的-94
        String hexString = Integer.toHexString(output[2] & 0xFF);
        if (json == null) {
            json = new JSONObject();
        }
        if (hexString.equals("a2")) {
            if (output[3] == 0) {
                try {
                    long tmp = 0;
                    tmp = (int) output[4] & 0xff;
                    if (tmp == 3) {
                        json.put("产品形式", "远传物联网端");
                    }
                    tmp = (int) output[5] & 0xff;
                    if (tmp == 0) {
                        json.put("信号类型", "模拟信号");
                    } else {
                        json.put("信号类型", "数字通讯");
                    }
                    tmp = (int) output[6] & 0xff;
                    sensingSignal = (int) tmp;
                    json.put("传感信号", LouShanYunUtils.getCGXHReadStringByCode(Long.valueOf(tmp)));
                    tmp = (int) output[7] & 0xff;
                    if (tmp == 0) {
                        json.put("拆卸", "正常");
                    } else {
                        json.put("拆卸", "拆卸");
                    }
                    tmp = (int) output[8] & 0xff;
                    if (tmp == 0) {
                        json.put("强磁", "正常");
                    } else {
                        json.put("强磁", "强磁");
                    }
                    tmp = (int) output[9] & 0xff;
                    if (tmp == 0) {
                        json.put("流向", "正流");
                    } else {
                        json.put("流向", "倒流");
                    }


                    tmp = (int) output[10] & 0xff;
                    json.put("发送频率", LouShanYunUtils.getFSPLReadStringByCode(Long.valueOf(tmp)));
                    year = String.valueOf(output[11] & 0xff);
                    month = String.valueOf(output[12] & 0xff);
                    if (month.length() == 1) {
                        month = "0" + month;
                    }
                    day = String.valueOf(output[13] & 0xff);
                    if (day.length() == 1) {
                        day = "0" + day;
                    }
                    json.put("出厂时间", "20" + year + "-" + month + "-" + day + " " + "00:00:00");
                    tmp = ((output[14] & 0xff) + ((output[15] & 0xff) << 8));
                    pushEvent(FactoryCode.GetManufacturersIdentificationRunner, tmp);
                    tmp = (int) output[16] & 0xff;
                    json.put("扩频因子", LouShanYunUtils.getKPYZReadStringByCode(String.valueOf(tmp)));
                    tmp = (int) output[17] & 0xff;
                    if (tmp == 0x00) {
                        json.put("信道", "模式A");
                    } else if (tmp == 0x01) {
                        json.put("信道", "模式B");
                    } else if (tmp == 0x11) {
                        json.put("信道", "模式16");
                    } else if (tmp == 0x21) {
                        json.put("信道", "模式32");
                    } else if (tmp == 0x31) {
                        json.put("信道", "模式48");
                    }

                    //第18个字节为表号，物联网端无表号，空出

                    tmp = 0;
                    for (int i = 5; i > 0; i--) {
                        tmp = (tmp * 256) + (int) (output[i + 18] & 0xff);
                    }
                    json.put("ID", tmp);
                    tmp = 0;
                    for (int i = 4; i > 0; i--) {
                        tmp = (tmp * 256) + (int) (output[i + 24] & 0xff);
                    }
                    if (((output[24] & 0xff) != 0)) {
                        tmp = -tmp;
                    }
                    json.put("初始脉冲底数", new BigInteger(String.valueOf(tmp)).toString(10));
                    tmp = (int) output[29] & 0xff;
                    json.put("倍率", LouShanYunUtils.getBLReadStringByCode(Long.valueOf(tmp)));
                    tmp = (int) output[30] & 0xff;
                    if (tmp == 0) {
                        json.put("电池状态", "正常");
                    } else if (tmp == 1) {
                        json.put("电池状态", "欠压");
                    }
                    redata = "";
                    for (int i = 1; i <= 8; i++) {
                        String hexString2 = Integer.toHexString(output[i + 30] & 0xFF);
                        if (hexString2.length() == 1) {
                            hexString2 = "0" + hexString2;
                        }
                        redata = redata + hexString2;
                    }
                    json.put("物联SN", redata);
                    tmp = (int) output[39] & 0xff;
                    if (tmp == 1) {
                        json.put("无线频率", "470MHZ");
                    }
                    tmp = (int) output[40] & 0xff;
                    json.put("硬件版本", LouShanYunUtils.getHardWareVersion((int) tmp));
                    tmp = (int) output[41] & 0xff;
                    softVersion = (int) tmp;
                    json.put("软件版本", LouShanYunUtils.getSoftWareVersion((int) tmp));
                    if (json != null) {
                        Message msg = handler.obtainMessage();
                        msg.what = MSG_READ;
                        msg.sendToTarget();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return;
            }
        } else if (hexString.equals("a4")) {
            if (isJiHuo) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //未激活
                        iv_system_status.setImageDrawable(getResources().getDrawable(R.drawable.wjhts));
                        isJiHuo = false;
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //已激活
                        iv_system_status.setImageDrawable(getResources().getDrawable(R.drawable.yjhts));
                        isJiHuo = true;
                    }
                });
            }
        } else if (hexString.equals("a5")) {
            if (output[3] == 0) {
                sendMessageToast("设备休眠成功");
            } else {
                sendMessageToast("设备休眠失败");
            }
        } else if (hexString.equals("c0")) {
            if (output[3] == 0) {
                LoadingDialogUtil.showByEvent(false, "正在接收", loadingTag);
            }
        } else if (hexString.equals("a7")) {
            if (output[3] == 0) {
                jiZhanCeShi = true;
                isRed = true;
                byte[] cmd = {0x68, 0x01, 0x12, 0x13, 0x16};
                write(cmd, "读取信号强度");
                sendMessageToast("强制发送成功");
            } else {
                sendMessageToast("强制发送失败");
            }
        } else if (hexString.equals("95")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_fsgl_read_wlwd.setText(String.valueOf(output[4]) + "dbm");
                    byte[] d1 = {0x68, 0x01, 0x16, 0x17, 0x16};
                    write(d1, "读取固件版本号");
                }
            });
        } else if (hexString.equals("96")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    gjBanBen = DataParser.getASCIIbyByte(output);
                    tv_gjbb_read_wlwd.setText(gjBanBen);
                    byte[] cmd = {0x68, 0x01, 0x12, 0x13, 0x16};
                    write(cmd, "读取信号强度");
                }
            });
        } else if (hexString.equals("92")) {
            if (output[3] == 0) {
                String RSS = getData(output[4]).toString();
                String SNR = getData(output[5]).toString();
                try {
                    json.put("RSS", RSS);
                    json.put("SNR", SNR);
                    if (json != null) {
                        Message msg = handler.obtainMessage();
                        msg.what = MSG_SEND_AFTER;
                        msg.sendToTarget();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else if (hexString.equals("a6")) {
            if (output[3] == 0) {
                Message msg = handler.obtainMessage();
                msg.what = MSG_SEND;
                msg.sendToTarget();
                if ("1".equals(String.valueOf(output[4] & 0xff))) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //已激活
                            iv_system_status.setImageDrawable(getResources().getDrawable(R.drawable.yjhts));
                            isJiHuo = true;
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //未激活
                            iv_system_status.setImageDrawable(getResources().getDrawable(R.drawable.wjhts));
                            isJiHuo = false;
                        }
                    });
                }
            }
        }
    }

    private boolean isRed;

    public String getData(byte b) {
        StringBuffer stringBuffer = new StringBuffer();
        if (b > 0) {
            stringBuffer.append("+");
        }
        stringBuffer.append(b);
        return stringBuffer.toString();
    }

    @Override
    protected void onChildConnectFailed(int i) {

    }

    @Override
    protected void onChildConnectSuccess() {
//获取激活状态
        if (sensoroDeviceSession != null) {
            write(new byte[]{(byte) 0x68, (byte) 0x01, (byte) 0x26, (byte) 0x27, (byte) 0x16}, "正在读取激活信息");
        }
    }

    @Override
    protected void onChildWriteSuccess() {

    }

    @Override
    protected void onChildWriteFailure(int i) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (client != null) {
            client.close();
        }
    }

    /**
     * 通过handler发送消息来执行相应的操作
     */
    @SuppressLint("HandlerLeak")
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_SEND) {
                byte[] d = new byte[5];
                d[0] = (byte) 0x68;
                d[1] = (byte) 0x01;//有效数据
                d[2] = (byte) 0x22;//命令
                byte cs = 0;
                for (int i = 1; i < 3; i++) {
                    cs += d[i];
                }
                d[3] = cs;//校验和
                d[4] = (byte) 0x16;
                if (sensoroDeviceSession != null) {
                    write(d, "正在读取表信息");
                }
            } else if (msg.what == MSG_READ) {//设置是否成功回调显示
                tv_mnxh_read.setText(json.optString("信号类型"));
//					tv_wldc_read.setText(json.optString("电源类型"));
                tv_ccgr_read.setText(json.optString("强磁"));
                tv_cx_read.setText(json.optString("拆卸"));
                tv_dl_read.setText(json.optString("流向"));
                tv_qy_read.setText(json.optString("电池状态"));

                tv_cgxh_read.setText(json.optString("传感信号"));
//					tv_clfs_read.setText(json.optString("参数内容"));
//					tv_mk_read.setText(json.optString("脉宽"));
                tv_cpsx_read.setText(json.optString("产品形式"));
                tv_wlsn_read.setText(sensoroDevice.getSerialNumber());
                tv_time_read.setText(json.optString("出厂时间"));
                tv_yjbb_read.setText(json.optString("硬件版本"));
                tv_rjbb_read.setText(json.optString("软件版本"));
                tv_cjbs_read.setText(json.optString("厂家标识"));
//					tv_fmzt_read_wlwd.setText(json.optString("阀门状态"));
                tv_fspl_read_wlwd.setText(json.optString("发送频率"));
                tv_wxpl_read_wlwd.setText(json.optString("无线频率"));
                tv_spreadingFactor.setText(json.optString("扩频因子"));
                tv_channel.setText(json.optString("信道"));
                String result = json.optString("倍率");
                tvMultiplyingPower.setText(LouShanYunUtils.getMultiplyingPower(result));
                tv_basicNumber.setText(LouShanYunUtils.getBasicNumber(json.optString("倍率"), json.optString("初始脉冲底数")));
                if (softVersion >= 4) {
                    byte[] bytes = {0x68, 0x01, 0x15, 0x16, 0x16};
                    write(bytes, "读取发送功率");
                }
            } else if (msg.what == MSG_CLOSESUCCESS) {
                write(new byte[]{(byte) 0x68, (byte) 0x02, (byte) 0x25, (byte) 0x00, (byte) 0x27, (byte) 0x16}, "正在让设备进入休眠状态");
            } else if (msg.what == MSG_SEND_AFTER) {
                String RSS = json.optString("RSS");
                String SNR = json.optString("SNR");
                if (isRed) {
                    tvRSS.setTextColor(0xffff0000);
                    tvSNR.setTextColor(0xffff0000);
                }
                tvRSS.setText(RSS);
                tvSNR.setText(SNR);
            }
        }
    };


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_save) {
            KeyboardUtils.hideSoftInput(this);
            boolean isHigher = LouShanYunUtils.isHigherGuJian(gjBanBen);
            if (!isHigher) {
                sendMessageToast("固件版本过低，不能进行保存");
                return;
            }
            String gonglv = tv_fsgl_read_wlwd.getText().toString();
            if (XHStringUtil.isEmpty(gonglv, false)) {
                sendMessageToast("没读取到发送功率，无法保存");
                return;
            }
            int batteryState = 0, magneticInterferenceState = 0, disassemblyState = 0, backflowState = 0, thirdPowerState = 0, circumscribedPowerState = 0, measuringMode = 0, productForm = 0;
            if (json == null) {
                return;
            }
            if (isJiHuo) {
                sendMessageToast("请点击右上角激活按钮，设置成未激活状态，否则无法保存");
                scrollview.scrollTo(0, 0);
                return;
            }
            if (!jiZhanCeShi) {
                sendMessageToast("请测试基站通讯是否正常");
                return;
            }

            if (json.optString("电池状态").equals("正常")) {
                batteryState = 0;
            } else if (json.optString("电池状态").equals("欠压")) {
                batteryState = 1;
            }

            if (json.optString("强磁").equals("正常")) {
                magneticInterferenceState = 0;
            } else if (json.optString("强磁").equals("强磁")) {
                magneticInterferenceState = 1;
            }
            if (json.optString("拆卸").equals("正常")) {
                disassemblyState = 0;
            } else if (json.optString("拆卸").equals("拆卸")) {
                disassemblyState = 1;
            }
            if (json.optString("流向").equals("正流")) {
                backflowState = 0;
            } else if (json.optString("流向").equals("倒流")) {
                backflowState = 1;
            }
            thirdPowerState = 2;
            circumscribedPowerState = 2;

            measuringMode = 0;

            productForm = LouShanYunUtils.getCPXSUploadIntByValue(json.optString("产品形式"));
            String string = "2";//基站民用2  公用1
            String string5 = "";
            String string2 = json.optString("出厂时间");
            String string3 = json.optString("硬件版本");
            String string4 = json.optString("软件版本");
            String sf = json.optString("扩频因子");
            String channel = json.optString("信道");
            float impulseInitial = Float.valueOf(json.optString("初始脉冲底数"));
            double MultiplyingPower = Double.valueOf(json.optString("倍率"));
            float pulseConstant = Float.valueOf(String.valueOf(1.0 / MultiplyingPower));
            int cjbs_wlwd = LoginParamManager.getInstance().getProductRegister().getManufacturersIdentification();
            JSONObject factoryData = new FactoryData().getFactoryData(sensoroDevice.getSerialNumber(),
                    string, 2, 1, 3, "470MHZ",
                    gonglv, string2, "无", string3, string4, batteryState,
                    magneticInterferenceState, disassemblyState, backflowState, 0, 0,
                    thirdPowerState, circumscribedPowerState, sensingSignal, measuringMode,
                    productForm, string5, 1, cjbs_wlwd, 0, sf, channel, impulseInitial, pulseConstant);
            pushEvent(FactoryCode.UploadDataRunner, factoryData.toString());
        } else if (v.getId() == R.id.tv_print) {
            if (!canPrintRepeat) {
                sendMessageToast("请先保存再打印");
            }
            print();
        } else if (v.getId() == R.id.tv_home) {
            ARouter.getInstance().build(K.BlueNameActivity).navigation();

        } else if (v.getId() == R.id.round_text_maichong) {
            write(DataParser.CMD_METER_INFO, "正在读取表信息...", true, 2000);
        }
    }

    private void print() {
        if (printTool.canPrinted) {
            if (!printTool.isConnected()) {
                sendMessageToast("请连接打印机");
                return;
            }
            printTool.print2(sensoroDevice.getSerialNumber(), json.optString("传感信号"), LoginParamManager.getInstance().getProductRegister().getCompanyName(),
                    json.optString("ID"), year, month, day
            );
        }


    }
}
