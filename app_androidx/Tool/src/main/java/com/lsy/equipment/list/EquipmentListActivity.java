package com.lsy.equipment.list;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.lsy.domain.ResponseAnalysisInfo1;
import com.lsy.login.R;
import com.sensoro.sensor.kit.callback.SensoroDeviceListener;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.init.SensoroBlueManager;
import com.wu.loushanyun.basemvp.v.activity.BaseBlueToothActivity;

import org.java_websocket.client.WebSocketClient;

import java.util.ArrayList;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ByteConvertUtils;
import met.hx.com.librarybase.some_utils.ToastManager;
import met.hx.com.librarybase.views.dialog.MDDialog;

/**
 * 附件设备蓝牙列表
 *
 * @author zhang.q.s
 */
@Route(path = K.EquipmentListActivity2)
public class EquipmentListActivity extends BaseBlueToothActivity implements DeviceListAdapter.OnTextItemClickListener {
    private ArrayList<SensoroDevice> deviceArrayList = new ArrayList<SensoroDevice>();
    private DeviceListAdapter deviceListAdapter;
    private ListView mListView;
    final int MSG_CONN = 1, MSG_CONNSUCCESS1 = 2;
    final int MSG_DIALOG = 5;
    final int MSG_SHOWVIEW = 6;
    final int MSG_CONNSUCCESS2 = 7;
    final int MSG_READSUCCESS = 8;
    final int MSG_READSUCCESSTONEXT = 9;
    final int MSG_ADDNUMBERSEND = 10;
    final int MSG_SENDTEXT = 12;
    private View view1;
    private ArrayList<String> aList = new ArrayList<String>();
    private WebSocketClient client;

    private MDDialog mdDialog;
    private TextView textLeixing;
    private TextView tvToolSendNumber;
    private TextView tvToolReceiveNumber;
    private TextView textChakan;
    private boolean isMoShi = false;

    private MDDialog mdDialog1;
    private TextView textSN;
    private TextView textDangqianmoshi;
    private TextView textQiehuanmoshi;
    private RoundTextView textYes;
    private RoundTextView textNo;
    private String moshi;
    private HashMap<String, String> map;

    private int sendNumber = 0;
    private int reciviceNumber = 0;
    private int totalNum = 10;
    private View view;
    private int typeIntLeiXing;
    private String typeLeiXing;
    private SensoroDevice sensoroDeviceChoose;

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.activity_near_equipmentlist;
        ba.mTitleText = "附近设备";
    }

    @Override
    protected void initView() {
        super.initView();
        view = getLayoutInflater().inflate(R.layout.tool_activity_sendnumber, null);
        textLeixing = (TextView) view.findViewById(R.id.text_leixing);
        tvToolSendNumber = (TextView) view.findViewById(R.id.tv_tool_sendNumber);
        tvToolReceiveNumber = (TextView) view.findViewById(R.id.tv_tool_receiveNumber);
        textChakan = (TextView) view.findViewById(R.id.text_chakan);


        textChakan.setOnClickListener(v -> {
            mdDialog.dismiss();
            handler.sendEmptyMessage(MSG_READSUCCESSTONEXT);
        });
        mdDialog = new MDDialog.Builder(this)
                .setContentView(view)
                .setShowTitle(false)
                .setShowButtons(false)
                .create();
        view1 = getLayoutInflater().inflate(R.layout.m_tool_dialog_fujin_moshi, null);
        mdDialog1 = new MDDialog.Builder(this).setContentView(view1)
                .setShowTitle(false)
                .setShowButtons(false)
                .create();
        textSN = (TextView) view1.findViewById(R.id.text_SN);
        textDangqianmoshi = (TextView) view1.findViewById(R.id.text_dangqianmoshi);
        textQiehuanmoshi = (TextView) view1.findViewById(R.id.text_qiehuanmoshi);
        textYes = (RoundTextView) view1.findViewById(R.id.text_yes);
        textNo = (RoundTextView) view1.findViewById(R.id.text_no);
        textNo.setOnClickListener(v -> {
            mdDialog1.dismiss();
        });
        textYes.setOnClickListener(v -> {
            setXinDao();
        });
        mdDialog.setCanceledOnTouchOutside(false);
        mdDialog1.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        });
        initData();
        initSDK();
        map = new HashMap<>();
    }

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }


    private void initSDK() {
        SensoroBlueManager.searchBlueTooth(sensoroDeviceListener);
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
                        deviceListAdapter.notifyDataSetChanged();
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
                    deviceListAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    private void initData() {
        deviceListAdapter = new DeviceListAdapter(this, this);
        mListView = (ListView) findViewById(R.id.equipment_list);
        mListView.setAdapter(deviceListAdapter);
        deviceListAdapter.setData(deviceArrayList);
    }

    /**
     * 通过handler发送消息来执行相应的操作
     */
    protected Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_CONN)//蓝牙连接的方法
            {

            } else if (msg.what == MSG_CONNSUCCESS1) {
                textLeixing.setText("当前设备:" + typeLeiXing);
                if (!EquipmentListActivity.this.isDestroyed()) {
                    mdDialog.show();
                }
                send2();
            } else if (msg.what == MSG_SENDTEXT) {
                tvToolSendNumber.setText(sendNumber + "");
            } else if (msg.what == MSG_ADDNUMBERSEND) {
                tvToolReceiveNumber.setText(reciviceNumber + "");
            } else if (msg.what == MSG_CONNSUCCESS2) {
                readPinLv();
            } else if (msg.what == MSG_READSUCCESS) {
                setDialogText(LouShanYunUtils.getXDCSReadStringByCode(map.get(MapParams.信道参数)));
                mdDialog1.show();
            } else if (msg.what == MSG_READSUCCESSTONEXT) {
                Intent intent = new Intent(EquipmentListActivity.this, DetailedInfoActivity.class);
                intent.putExtra("sendNumber", sendNumber + "");
                intent.putExtra("reciviceNumber", reciviceNumber + "");
                intent.putExtra("sensoro", sensoroDevice.getSerialNumber());
                intent.putExtra("moshi", LouShanYunUtils.getXDCSReadStringByCode(map.get(MapParams.信道参数)));
                intent.putStringArrayListExtra("infoList", aList);
                EquipmentListActivity.this.startActivity(intent);
                tvToolSendNumber.setText("0");
                tvToolReceiveNumber.setText("0");
                textChakan.setVisibility(View.GONE);
            } else if (msg.what == MSG_DIALOG) {
                mdDialog.show();
            } else if (msg.what == MSG_SHOWVIEW) {
                if (client != null) {
                    client.close();
                }
                textChakan.setVisibility(View.VISIBLE);
            }
        }
    };

    private void send2() {
        if (!mdDialog.isShowing()) {
            handler.sendEmptyMessage(MSG_DIALOG);
        }
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
            sendNumber++;
            handler.sendEmptyMessage(MSG_SENDTEXT);
            write(d1, "正在发送命令，请稍后……", true, BLUE_SEND_TIME_OUT);
        }
    }

    private void setDialogText(String moshi) {
        this.moshi = moshi;
        textSN.setText("物联SN:" + sensoroDevice.getSerialNumber());
        textDangqianmoshi.setText("当前模式:" + moshi);
        if (moshi.equals("模式A")) {
            textQiehuanmoshi.setText("是否切换为模式B?");
        } else if (moshi.equals("模式B")) {
            textQiehuanmoshi.setText("是否切换为模式A?");
        }
    }


    private void setXinDao() {
        mdDialog1.dismiss();
        if (typeIntLeiXing == 2) {
            if (moshi.equals("模式A")) {
                byte[] d = DataParser.setXinDaoCanShuWu(false);//模式A
                write(d);
            } else if (moshi.equals("模式B")) {
                byte[] d = DataParser.setXinDaoCanShuWu(true);//模式B
                write(d);
            }
        } else if (typeIntLeiXing == 1) {
            if (moshi.equals("模式A")) {
                byte[] d = DataParser.setXinDaoCanShu(false);//模式A
                write(d);
            } else if (moshi.equals("模式B")) {
                byte[] d = DataParser.setXinDaoCanShu(true);//模式B
                write(d);
            }
        }

    }


    @Override
    public void onDestroy() {
        if (client != null) {
            client.close();
        }
        handler.removeMessages(MSG_CONNSUCCESS1);
        super.onDestroy();
    }

    @Override
    public void onChildNotify(byte[] output) {
        byte code = (byte) (output[2] ^ ((byte) 0x80));
        if (code == 0x11) {
            if (output[3] == 0) {
                try {
                    HashMap<String, String> hashMap = DataParser.getInformationAll(output);
                    typeIntLeiXing = DataParser.getModuleType(output);
                    if (typeIntLeiXing == 2) {
                        typeLeiXing = "远传物联网端";
                        totalNum = 10;
                        try {
                            Double d = Double.valueOf(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
                            if (d >= 1.04) {
                                write(DataParser.CMD_METER_INFO, "读取设备信息");
                            } else {
                                sendMessageToast("当前物联网端版本低于1.04，请升级版本");
                            }
                        } catch (Exception e) {
                            sendMessageToast("当前物联网端版本解析失败，请升级到1.04版本");
                        }
                    } else if (typeIntLeiXing == 1) {
                        typeLeiXing = "远传表号接入";
                        totalNum = 10;
                        try {
                            Double d = Double.valueOf(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
                            if (d >= 1.04) {
                                map.put(MapParams.信道参数, hashMap.get(MapParams.信道参数));
                                if (isMoShi) {
                                    handler.sendEmptyMessage(MSG_READSUCCESS);
                                } else {
                                    handler.sendEmptyMessage(MSG_CONNSUCCESS1);
                                }
                            } else {
                                sendMessageToast("当前版本低于1.04，请升级版本");
                            }
                        } catch (Exception e) {
                            sendMessageToast("当前端版本解析失败，请升级到1.04版本");
                        }

                    }
                } catch (Exception e) {
                    ToastManager.getInstance().showLong("检测到设备未进行出厂配置，请重新选择设备");
                }
            }
        } else if (code == 0x08) {
            if (output[3] == 0) {
                sendMessageToast("设置信道成功");
                mdDialog1.dismiss();
            } else {
                sendMessageToast("设置信道失败");
            }
        } else if (code == 0x05) {
            if (output[3] == 0) {
                sendMessageToast("设置信道成功");
                mdDialog1.dismiss();
            } else {
                sendMessageToast("设置信道失败");
            }
        } else if (code == 0x22) {
            map.putAll(DataParser.getMoudleNo2(output));
            if (isMoShi) {
                handler.sendEmptyMessage(MSG_READSUCCESS);
            } else {
                handler.sendEmptyMessage(MSG_CONNSUCCESS1);
            }
        } else if (code == 0x27) {
            if (output[3] == 0) {
                sendMessageToast("强制发送成功");
                write(DataParser.CMD_RSSI_SNR, "读取信号强度和信噪比");
            } else {
                sendMessageToast("强制发送失败(反馈)");
                ResponseAnalysisInfo1 responseAnalysisInfo1 = new ResponseAnalysisInfo1();
                responseAnalysisInfo1.setRssi("-");
                responseAnalysisInfo1.setSnr("-");
                Gson gson = new Gson();
                aList.add(gson.toJson(responseAnalysisInfo1));
                if (sendNumber == totalNum) {
                    handler.sendEmptyMessage(MSG_SHOWVIEW);
                } else {
                    send2();
                }
            }
        } else if (code == 0x12) {
            if (output[3] == 0) {
                sendMessageToast("读取成功");
                int rssi = output[4];
                int snr = output[5];
                if (typeIntLeiXing == 1) {
                    rssi = ByteConvertUtils.parseByteToSignedString(output[4]);
                    snr = ByteConvertUtils.parseByteToSignedString(output[5]);
                } else if (typeIntLeiXing == 2) {

                }
                ResponseAnalysisInfo1 responseAnalysisInfo1 = new ResponseAnalysisInfo1();
                responseAnalysisInfo1.setRssi(rssi + "");
                responseAnalysisInfo1.setSnr(snr + "");
                Gson gson = new Gson();
                aList.add(gson.toJson(responseAnalysisInfo1));
                reciviceNumber++;
                handler.sendEmptyMessage(MSG_ADDNUMBERSEND);
                if (sendNumber == totalNum) {
                    handler.sendEmptyMessage(MSG_SHOWVIEW);
                } else {
                    send2();
                }

            }

        }

    }

    @Override
    protected void onChildConnectFailed(int i) {
        sendMessageToast("蓝牙连接失败,请检查蓝牙设备");
        mdDialog.dismiss();
    }

    @Override
    protected void onChildConnectSuccess() {
        this.sensoroDevice = sensoroDeviceChoose;
        Message msg = handler.obtainMessage();
        msg.what = MSG_CONNSUCCESS2;
        msg.sendToTarget();
    }

    private void readPinLv() {
        byte[] bytes = DataParser.CMD_INFO_ALL;
        write(bytes, "正在识别目标设备");
    }

    @Override
    protected void onChildWriteSuccess() {

    }

    @Override
    protected void onChildWriteFailure(int i) {

    }

    @Override
    public void onTextCheckClick(int i) {
        if (aList != null) {
            aList.clear();
        }
        textChakan.setVisibility(View.GONE);
        this.sendNumber = 0;
        this.reciviceNumber = 0;
        isMoShi = false;
        SensoroDevice sensoroDevice = deviceArrayList.get(i);
        this.sensoroDeviceChoose = sensoroDevice;
        if (this.sensoroDevice != null) {
            if (this.sensoroDevice.getSerialNumber().equals(sensoroDevice.getSerialNumber())) {
                Message msg = handler.obtainMessage();
                msg.what = MSG_CONNSUCCESS2;
                msg.sendToTarget();
            } else {
                connectBlueTooth(sensoroDevice);
            }
        } else {
            connectBlueTooth(sensoroDevice);
        }


    }

    @Override
    public void onTextMoShiClick(SensoroDevice sensoroDevice, int i) {
        isMoShi = true;
        this.sensoroDeviceChoose = sensoroDevice;
        if (this.sensoroDevice != null) {
            if (this.sensoroDevice.getSerialNumber().equals(sensoroDevice.getSerialNumber())) {
                Message msg = handler.obtainMessage();
                msg.what = MSG_CONNSUCCESS2;
                msg.sendToTarget();
            } else {
                connectBlueTooth(sensoroDevice);
            }
        } else {
            connectBlueTooth(sensoroDevice);
        }
    }

}

