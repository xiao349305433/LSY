package wu.loushanyun.com.modulechiptest.v.activity;


import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.init.SensoroBlueManager;
import com.wu.loushanyun.basemvp.v.activity.BaseBlueToothActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.KeyboardUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.libraryfive.p.adapter.SNDeviceViewBinder;
import wu.loushanyun.com.modulechiptest.R;

@Route(path = K.ThirdTestActivity)
public class ThirdTestActivity extends BaseBlueToothActivity {
    private ArrayList<SensoroDevice> deviceArrayList;

    private TextView textDangqianSn;
    private RoundTextView bluetoothConn;
    private RoundTextView bluetoothDisconn;
    private Spinner xindaocanshuSelect;
    private Spinner beilvSelect;
    private EditText editMaichongStart;
    private EditText editIdStart;
    private RoundTextView roundCreateId;
    private RoundTextView roundSetting;
    private RoundTextView buttonDuquInfo;
    private TextView textDuquInfo;
    private RoundTextView buttonDuquMaichong;
    private TextView textDuquMaichong;
    private RoundTextView buttonYanbiao;
    private LinearLayout linearBlueTooth;
    private EditText editSearch;
    private RoundTextView resetClear;
    private RoundTextView resetClearList;
    private RecyclerView dialogBlueRecycle;

    private SNDeviceViewBinder snDeviceViewBinder;
    private MultiTypeAdapter multiTypeAdapterBlue;
    private HashMap<String, String> hashMap;

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_chip_activity_third;
        ba.mTitleText = "3号模组测试";
        ba.mTitleRightText = "附近蓝牙";
        ba.mTitleLeftRightWidth = 100;
    }

    @Override
    public void onRightClick(View item) {
        linearBlueTooth.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onLoadingDismissTimeOut() {
    }

    @Override
    protected void onChildConnectFailed(int i) {

    }

    @Override
    protected void onChildConnectSuccess() {
        runOnUiThread(() -> textDangqianSn.setText("当前连接的SN设备:" + sensoroDevice.getSerialNumber().toUpperCase()));
    }

    @Override
    protected void onChildNotify(byte[] output) {
        byte code = (byte) (output[6] ^ ((byte) 0x80));
        switch (code) {
            case 0x09:
                if (output[7] == 0) {
                    sendMessageToast("设置成功");
                } else {
                    sendMessageToast("设置失败");
                }
                break;
            case 0x0b:
                if (output[7] == 0) {
                    //解析 start
                    long tmp = (int) output[8];
                    hashMap.put(MapParams.表号, String.valueOf(tmp));
                    tmp = 0;
                    for (int i = 7; i > 0; i--) {
                        tmp = (tmp * 256) + (int) (output[i + 8] & 0xff);
                    }
                    hashMap.put(MapParams.设备ID, String.valueOf(tmp));
                    tmp = (int) output[16];
                    hashMap.put(MapParams.倍率, String.valueOf(tmp));

                    hashMap.put(MapParams.状态, String.valueOf(output[17]&0xff));
                    tmp = (int) output[18];
                    hashMap.put(MapParams.传感类型, String.valueOf(tmp));
                    tmp = (int) output[19];
                    hashMap.put(MapParams.软件版本, String.valueOf(tmp));
                    tmp = 0;
                    for (int i = 6; i > 0; i--) {
                        tmp = (tmp * 256) + (int) (output[i + 19] & 0xff);
                    }
                    hashMap.put(MapParams.企业代码, String.valueOf(tmp));
                    String year = String.valueOf(output[26] & 0xff);
                    String month = String.valueOf(output[27] & 0xff);
                    if (month.length() == 1) {
                        month = "0" + month;
                    }
                    String day = String.valueOf(output[28] & 0xff);
                    hashMap.put(MapParams.出厂时间_年, year);
                    hashMap.put(MapParams.出厂时间_月, month);
                    hashMap.put(MapParams.出厂时间_日, day);
                    setDuQu();
                } else {
                    sendMessageToast("操作失败");
                }
                break;
            case 0x0c:
                if (output[7] == 0) {
                    long tmp = 0;
                    for (int i = 3; i > 0; i--) {
                        tmp = tmp * 256 + (int) (output[i + 7] & 0xff);
                    }
                    hashMap.put(MapParams.脉冲底数, String.valueOf(tmp));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            if (!XHStringUtil.isEmpty(hashMap.get(MapParams.倍率), false)) {
                                String beilv = LouShanYunUtils.getBLReadStringByCode(hashMap.get(MapParams.倍率));
                                String dushu = LouShanYunUtils.getBasicNumber(beilv, hashMap.get(MapParams.脉冲底数));
                                StringBuilder sb = new StringBuilder();
                                sb.append("脉冲底数(个)/初始值(m³):  ");
                                sb.append(dushu);
                                textDuquMaichong.setText(sb.toString());
                            }

                        }
                    });
                } else {
                    sendMessageToast("操作失败");
                }
                break;
        }
    }

    private void setDuQu() {
        StringBuilder sb = new StringBuilder();
        sb.append("表号:  ");
        sb.append(hashMap.get(MapParams.表号));
        sb.append("\n设备ID:  ");
        sb.append(hashMap.get(MapParams.设备ID));
        sb.append("\n倍率(m³/ev)/脉冲常数(个/m³):  ");
        sb.append(LouShanYunUtils.getMultiplyingPower(LouShanYunUtils.getBLReadStringByCode(hashMap.get(MapParams.倍率))));
        sb.append("\n传感类型:  ");
        sb.append(LouShanYunUtils.getCGXHReadStringByCode(Integer.valueOf(hashMap.get(MapParams.传感类型))));
        sb.append("\n软件版本:  ");
        sb.append(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
        sb.append("\n状态:  ");
        String zhuangtai=LouShanYunUtils.getZTReadStringByCode(hashMap.get(MapParams.状态));
        sb.append(zhuangtai);
        sb.append("\n企业代码:  ");
        sb.append(hashMap.get(MapParams.企业代码));
        sb.append("\n模组出厂时间:  ");
        sb.append("20" + hashMap.get(MapParams.出厂时间_年) + "-" + hashMap.get(MapParams.出厂时间_月) + "-" + hashMap.get(MapParams.出厂时间_日));
//        SpannableString ss = new SpannableString(sb);
//        ForegroundColorSpan colorSpan = new ForegroundColorSpan(0xff941e23);
//        ss.setSpan(colorSpan, sb.indexOf("状态: "), sensoroDevice.getSerialNumber().length()+3, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        runOnUiThread(() -> {
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
        xindaocanshuSelect = (Spinner) findViewById(R.id.xindaocanshu_select);
        beilvSelect = (Spinner) findViewById(R.id.beilv_select);
        editMaichongStart = (EditText) findViewById(R.id.edit_maichong_start);
        editIdStart = (EditText) findViewById(R.id.edit_id_start);
        roundCreateId = (RoundTextView) findViewById(R.id.round_create_id);
        roundSetting = (RoundTextView) findViewById(R.id.round_setting);
        buttonDuquInfo = (RoundTextView) findViewById(R.id.button_duqu_info);
        textDuquInfo = (TextView) findViewById(R.id.text_duqu_info);
        buttonDuquMaichong = (RoundTextView) findViewById(R.id.button_duqu_maichong);
        textDuquMaichong = (TextView) findViewById(R.id.text_duqu_maichong);
        buttonYanbiao = (RoundTextView) findViewById(R.id.button_yanbiao);
        linearBlueTooth = (LinearLayout) findViewById(R.id.linear_blue_tooth);
        editSearch = (EditText) findViewById(R.id.edit_search);
        resetClear = (RoundTextView) findViewById(R.id.reset_clear);
        resetClearList = (RoundTextView) findViewById(R.id.reset_clear_list);
        dialogBlueRecycle = (RecyclerView) findViewById(R.id.dialog_blue_recycle);


        xindaocanshuSelect.setSelection(2);
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
        buttonYanbiao.setOnClickListener(view -> {
            ARouter.getInstance().build(K.ThirdTestNewActivity).navigation();
        });
        initBlueClick();
    }

    /**
     * 各种协议点击事件
     */
    private void initBlueClick() {
        buttonDuquInfo.setOnClickListener(this::OnBlueToothClick);
        roundSetting.setOnClickListener(this::OnBlueToothClick);
        buttonDuquMaichong.setOnClickListener(this::OnBlueToothClick);
        roundCreateId.setOnClickListener(this::OnBlueToothClick);
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
        switch (view.getId()) {
            case R.id.round_create_id:
                editIdStart.setText(LouShanYunUtils.getTimeID());
                break;
            case R.id.round_setting:
                if (XHStringUtil.isEmpty(editIdStart.getText().toString(), false)) {
                    sendMessageToast("请填入设备ID");
                    return;
                }
                String basicNum = editMaichongStart.getText().toString().trim();
                if (XHStringUtil.isEmpty(basicNum, false)) {
                    sendMessageToast("请输入脉冲底数");
                    return;
                }
                textDuquInfo.setText("");
                textDuquMaichong.setText("");
                int Ratio = beilvSelect.getSelectedItemPosition();
                int sensoroCode = 0;
                long equipmentID = Long.valueOf(editIdStart.getText().toString());
                int tableNumber = 1;
                //生成时间戳
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
                String[] split = date.split("-");
                String year = split[0].substring(split[0].length() - 2, split[0].length());
                String month = split[1];
                String day = split[2];
                String sensoroType = xindaocanshuSelect.getSelectedItem().toString().trim();

                if (sensoroType.equals("3EV")) {
                    sensoroCode = 1;
                } else if (sensoroType.equals("2EV")) {
                    sensoroCode = 3;
                } else {
                    sensoroCode = 10;
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
                long isn = equipmentID;
                for (int i = 0; i < 6; i++) {
                    d[i + 8] = (byte) (isn & 0xff);
                    isn = isn >> 8;
                }
                d[14] = (byte) tableNumber;
                d[15] = (byte) 0xAA;
                d[16] = (byte) 0x02;
                d[17] = (byte) Ratio;
                d[18] = (byte) 0x08;
                d[19] = (byte) sensoroCode;//传感类型
                d[20] = (byte) Integer.parseInt(year);//生产时间
                d[21] = (byte) Integer.parseInt(month);
                d[22] = (byte) Integer.parseInt(day);
                long code = 0;//企业代码
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
            case R.id.button_duqu_maichong:
                textDuquMaichong.setText("");
                byte[] d2 = new byte[11];
                d2[0] = (byte) 0xfe;
                d2[1] = (byte) 0xfe;
                d2[2] = (byte) 0xfe;
                d2[3] = (byte) 0x68;
                d2[4] = (byte) 0x03;//有效数据
                d2[5] = (byte) 0x00;
                d2[6] = (byte) 0x0c;//命令
                d2[7] = (byte) 0x03;//表类型
                d2[8] = (byte) 0x01;//表号
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
            case R.id.button_duqu_info:
                textDuquInfo.setText("");
                byte[] d1 = new byte[11];
                d1[0] = (byte) 0xfe;
                d1[1] = (byte) 0xfe;
                d1[2] = (byte) 0xfe;
                d1[3] = (byte) 0x68;
                d1[4] = (byte) 0x03;//有效数据
                d1[5] = (byte) 0x00;
                d1[6] = (byte) 0x0b;//命令
                d1[7] = (byte) 0x03;//表类型
                d1[8] = (byte) 0x01;//表号
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
        }
    }


    private boolean isConnect() {
        if (textDangqianSn.getText().toString().contains("当前连接的SN设备")) {
            return true;
        } else {
            return false;
        }
    }


    private void initBlueList() {
        multiTypeAdapterBlue = new MultiTypeAdapter();
        deviceArrayList = new ArrayList<>();
        multiTypeAdapterBlue.register(SensoroDevice.class, snDeviceViewBinder);
        multiTypeAdapterBlue.setItems(deviceArrayList);
        dialogBlueRecycle.setAdapter(multiTypeAdapterBlue);
    }
    protected SensoroDeviceListener sensoroDeviceListener=new SensoroDeviceListener<SensoroDevice>() {
        @Override
        public void onNewDevice(SensoroDevice sensoroDevice) {
            XLog.i("蓝牙onNewDevice"+sensoroDevice.toString());
            if(!deviceArrayList.contains(sensoroDevice)){
                deviceArrayList.add(sensoroDevice);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        multiTypeAdapterBlue.notifyDataSetChanged();
                    }
                });
            }else {
                XLog.i("蓝牙sdaasasdasdasdasd"+sensoroDevice.toString());
            }

        }

        @Override
        public void onGoneDevice(SensoroDevice sensoroDevice) {
            XLog.i("蓝牙onGoneDevice"+sensoroDevice.toString());
        }

        @Override
        public void onUpdateDevices(ArrayList<SensoroDevice> arrayList) {
            XLog.i("蓝牙onUpdateDevices"+arrayList.toString());
            for(int i=0;i<arrayList.size();i++){
                SensoroDevice sensoroDevice=arrayList.get(i);
                if(deviceArrayList.contains(sensoroDevice)){
                    deviceArrayList.set(deviceArrayList.indexOf(sensoroDevice),sensoroDevice);
                }else {
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
}
