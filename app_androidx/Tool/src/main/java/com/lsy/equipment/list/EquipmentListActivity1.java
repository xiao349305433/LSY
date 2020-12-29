package com.lsy.equipment.list;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.elvishew.xlog.XLog;
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
import com.wu.loushanyun.basemvp.m.sat.SAtInstructRealizeFactory;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;
import com.wu.loushanyun.basemvp.v.activity.BaseBlueToothActivity;

import org.java_websocket.client.WebSocketClient;

import java.util.ArrayList;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ToastManager;
import met.hx.com.librarybase.views.dialog.MDDialog;

/**
 * 附件设备蓝牙列表
 *
 * @author zhang.q.s
 */
@Route(path = K.EquipmentListActivity)
public class EquipmentListActivity1 extends BaseBlueToothActivity implements DeviceListAdapter.OnTextItemClickListener {
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
    private ArrayList<String> aList = new ArrayList<String>();
    private WebSocketClient client;

    private MDDialog mdDialog;
    private TextView textLeixing;
    private TextView tvToolSendNumber;
    private TextView tvToolReceiveNumber;
    private TextView textChakan;

    private int sendNumber = 0;
    private int reciviceNumber = 0;
    private int totalNum = 10;
    private View view;
    private int typeIntLeiXing;
    private String typeLeiXing;
    private SAtInstructRealizeFactory sAtInstructRealizeFactory;

    private HashMap<String, String> hashMap;

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
        mdDialog.setCanceledOnTouchOutside(false);
        initData();
        hashMap = new HashMap<>();
        sAtInstructRealizeFactory = new SAtInstructRealizeFactory();
        if (sensoroDevice != null) {
            deviceArrayList.add(sensoroDevice);
            deviceListAdapter.notifyDataSetChanged();
            connectBlueTooth(sensoroDevice);
        } else {
            initSDK();
        }
    }

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    private void initSnAgreement(double hardVersion) {
        sAtInstructRealizeFactory.initHardVersion(hardVersion);
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
                if (!EquipmentListActivity1.this.isDestroyed()) {
                    mdDialog.show();
                }
                send2();
            } else if (msg.what == MSG_SENDTEXT) {
                tvToolSendNumber.setText(sendNumber + "");
            } else if (msg.what == MSG_ADDNUMBERSEND) {
                tvToolReceiveNumber.setText(reciviceNumber + "");
            } else if (msg.what == MSG_CONNSUCCESS2) {
                readPinLv();
            } else if (msg.what == MSG_READSUCCESSTONEXT) {
                Intent intent = new Intent(EquipmentListActivity1.this, DetailedInfoActivity.class);
                intent.putExtra("sendNumber", sendNumber + "");
                intent.putExtra("reciviceNumber", reciviceNumber + "");
                intent.putExtra("sensoro", sensoroDevice.getSerialNumber());
                intent.putExtra("moshi", hashMap.get(SAtInstructParams.sAtInstructChannel));
                intent.putStringArrayListExtra("infoList", aList);
                EquipmentListActivity1.this.startActivity(intent);
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
        if (sensoroDeviceSession != null) {
            try {
                sendNumber++;
                handler.sendEmptyMessage(MSG_SENDTEXT);
                write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructForcedSend).getTwoReadBytes("15527919058")
                        , "强制发送", true, BLUE_SEND_TIME_OUT);
            } catch (Exception e) {
                e.printStackTrace();
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
        if (code == 0x50) {
            String resultAt = sAtInstructRealizeFactory.getSAtTypeString(output);
            XLog.i("蓝牙" + resultAt);
            if (resultAt.equals(SAtInstructParams.sAtInstructChannel + "-R")) {
                try {
                    hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).parseTwoRNotifyBytes(output));
                    handler.sendEmptyMessage(MSG_CONNSUCCESS1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultAt.equals(SAtInstructParams.sAtInstructForcedSend + "+SEND2")) {
                try {
                    sendMessageToast("强制发送成功");
                    write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRSSI).getTwoReadBytes(), "读取信号强度");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultAt.equals(SAtInstructParams.sAtInstructForcedSend + "+SEND3")) {
                try {
                    sendMessageToast("强制发送失败");
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (resultAt.equals(SAtInstructParams.sAtInstructRSSI + "-R")) {
                try {
                    hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRSSI).parseTwoRNotifyBytes(output));
                    String[] strings = hashMap.get(SAtInstructParams.sAtInstructRSSI).split(",");
                    int rssi = Integer.valueOf(strings[0]);
                    int snr = Integer.valueOf(strings[1]);
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (code == 0x11) {
            if (output[3] == 0) {
                try {
                    hashMap.putAll(DataParser.getInformationAll(output));
                    initSnAgreement(Double.valueOf(LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.硬件版本)))));
                    typeIntLeiXing = DataParser.getModuleType(output);
                    if (typeIntLeiXing == 2) {
                        typeLeiXing = "远传物联网端";
                        totalNum = 10;
                        try {
                            Double d = Double.valueOf(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
                            if (d >= 1.06) {
                                try {
                                    write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getTwoReadBytes(), "读取信道中...");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else {
                                sendMessageToast("当前物联网端版本低于1.06，请升级版本");
                            }
                        } catch (Exception e) {
                            sendMessageToast("当前物联网端版本解析失败，请升级到1.06版本");
                        }
                    } else if (typeIntLeiXing == 1) {
                        typeLeiXing = "远传表号接入";
                        totalNum = 10;
                        try {
                            Double d = Double.valueOf(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
                            if (d >= 1.07) {
                                try {
                                    write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getTwoReadBytes(), "读取信道中...");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                sendMessageToast("当前版本低于1.07，请升级版本");
                            }
                        } catch (Exception e) {
                            sendMessageToast("当前端版本解析失败，请升级到1.06版本");
                        }

                    }
                } catch (Exception e) {
                    XLog.e(e);
                    ToastManager.getInstance().showLong("检测到设备未进行出厂配置，请重新选择设备");
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
        Message msg = handler.obtainMessage();
        msg.what = MSG_CONNSUCCESS2;
        msg.sendToTarget();
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
        SensoroDevice sensoroDevice = deviceArrayList.get(i);
        this.sensoroDevice = sensoroDevice;
        connectBlueTooth(sensoroDevice);

    }


    private void readPinLv() {
        byte[] bytes = DataParser.CMD_INFO_ALL;
        write(bytes, "正在识别目标设备");
    }

    @Override
    public void onTextMoShiClick(SensoroDevice sensoroDevice, int i) {
    }

}

