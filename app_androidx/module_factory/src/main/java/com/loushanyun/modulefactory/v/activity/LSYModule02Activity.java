package com.loushanyun.modulefactory.v.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.loushanyun.modulefactory.R;
import com.loushanyun.modulefactory.init.LoginParamManager;
import com.loushanyun.modulefactory.v.views.CommomDialog;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.v.activity.BaseBlueToothActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ToastManager;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.views.MyRadioGroup;

@Route(path = K.LSYModule02Activity)
public class LSYModule02Activity extends BaseBlueToothActivity implements View.OnClickListener {
    private TextView tvConnStateWlwd;
    private TextView textCpxsWlwd;
    private Spinner spinnerCgxhWlwd;
    private TextView textSfplWlwd;
    private Spinner spinnerSpreadingFactor;
    private Spinner spinnerFasonggonglv;
    private MyRadioGroup radioGroup;
    private RadioButton radioShuangmoshi;
    private Spinner spinnerChannel;
    private RadioButton radioDanmoshi;
    private Spinner spinnerDuoChannel;
    private TextView textChuangganqizhuangtai;
    private TextView textCichangzhuangtai;
    private Spinner spinnerCaixie;
    private TextView textDaoliu;
    private EditText edtTime;
    private ImageView ivProduceTime;
    private EditText edtBasicNum;
    private Spinner spinnerRatioInit;
    private TextView pulse;
    private TextView tvTimeWlwd;
    private Button btnTerminal;
    private Button btnTerminalRead;
    private TextView textChushizhi;

    final int MSG_CONN = 1, MSG_SETSUCCESS = 2, MSG_READ = 3, MSG_CONNSUCCESS = 4, MSG_WRITESTATE = 5, MSG_WRITETOAST = 6, MSG_TOASTFAILED = 7, MSG_TOASTSUCCESS = 8, MSG_TOASTSUCCESS1 = 9;
    private JSONObject json;
    private String redata = "";

    private String loadingTag;
    private String equipmentID;
    private String basicNumber;
    private String pinLv = "";
    private int type;
    /**
     * 通过handler发送消息来执行相应的操作
     */
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_CONN)//蓝牙连接的方法
            {
                connectBlueTooth(sensoroDevice);
            } else if (msg.what == MSG_READ) {
            } else if (msg.what == MSG_WRITESTATE) {
                new CommomDialog(LSYModule02Activity.this, R.style.dialog, "出厂设置失败，请检查蓝牙设备", new CommomDialog.OnCloseListener() {
                    @Override
                    public void onClick(Dialog dialog, boolean confirm) {
                        if (confirm) {
                            dialog.dismiss();
                            tvConnStateWlwd.setText("未连接,点击重连");
                        }
                    }
                }).setTitle("温馨提示").show();
            } else if (msg.what == MSG_CONNSUCCESS) {
                readPinLv();
                tvConnStateWlwd.setText(sensoroDevice.getSerialNumber());
            } else if (msg.what == MSG_WRITETOAST) {
                ToastManager.getInstance(LSYModule02Activity.this).show("设置成功");
            } else if (msg.what == MSG_TOASTFAILED) {
                ToastManager.getInstance(LSYModule02Activity.this).show("连接失败");
            } else if (msg.what == MSG_TOASTSUCCESS) {
                ToastManager.getInstance(LSYModule02Activity.this).show("连接成功");
            } else if (msg.what == MSG_SETSUCCESS) {
                equipmentID = edtTime.getText().toString().trim();
                if (XHStringUtil.isEmpty(equipmentID, false)) {
                    ToastManager.getInstance(LSYModule02Activity.this).show("请输入设备ID");
                    return;
                }
                basicNumber = edtBasicNum.getText().toString().trim();
                if (XHStringUtil.isEmpty(basicNumber, false)) {
                    ToastManager.getInstance(LSYModule02Activity.this).show("请输入表底数");
                    return;
                }
                if (XHStringUtil.isEmpty(equipmentID, false)) {
                    return;
                }
                if (XHStringUtil.isEmpty(basicNumber, false)) {
                    return;
                }
                String meterRatio = spinnerRatioInit.getSelectedItem().toString().trim();
                byte[] d = new byte[17];
                d[0] = (byte) 0x68;
                d[1] = (byte) 0x0d;//有效数据
                d[2] = (byte) 0x21;//命令
                d[3] = (byte) 0x00;//表号
                long isn = Long.parseLong(equipmentID);//设备ID
                for (int i = 0; i < 5; i++) {
                    d[i + 4] = (byte) (isn & 0xff);
                    isn = isn >> 8;
                }
                d[9] = (byte) 0x00;//脉冲底数，第一个字节判断正负标识
                d[10] = (byte) (Integer.parseInt(basicNumber) & 0xff);
                d[11] = (byte) ((Integer.parseInt(basicNumber) & 0xff00) / 0x0100);
                d[12] = (byte) ((Integer.parseInt(basicNumber) & 0xff0000) / 0x010000);
                d[13] = (byte) ((Integer.parseInt(basicNumber) & 0xff000000) / 0x01000000);
                d[14] = (byte) LouShanYunUtils.getBLWriteIntByValue(meterRatio);//倍率
                byte cs = 0;
                for (int i = 1; i < 15; i++) {
                    cs += d[i];
                }
                d[15] = cs;
                d[16] = (byte) 0x16;
                write(d, "正在写入第二数据...");
            }

        }
    };
    private String softVersion;

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.activity_lsymodule02;
        ba.mTitleText = "娄山云2号模组";
    }

    @Override
    protected void initView() {
        super.initView();
        tvConnStateWlwd = (TextView) findViewById(R.id.tv_connState_wlwd);
        textCpxsWlwd = (TextView) findViewById(R.id.text_cpxs_wlwd);
        spinnerCgxhWlwd = (Spinner) findViewById(R.id.spinner_cgxh_wlwd);
        textSfplWlwd = (TextView) findViewById(R.id.text_sfpl_wlwd);
        spinnerSpreadingFactor = (Spinner) findViewById(R.id.spinner_SpreadingFactor);
        spinnerFasonggonglv = (Spinner) findViewById(R.id.spinner_fasonggonglv);
        radioGroup = (MyRadioGroup) findViewById(R.id.radio_group);
        radioShuangmoshi = (RadioButton) findViewById(R.id.radio_shuangmoshi);
        spinnerChannel = (Spinner) findViewById(R.id.spinner_channel);
        radioDanmoshi = (RadioButton) findViewById(R.id.radio_danmoshi);
        spinnerDuoChannel = (Spinner) findViewById(R.id.spinner_duo_channel);
        textChuangganqizhuangtai = (TextView) findViewById(R.id.text_chuangganqizhuangtai);
        textCichangzhuangtai = (TextView) findViewById(R.id.text_cichangzhuangtai);
        spinnerCaixie = (Spinner) findViewById(R.id.spinner_caixie);
        textDaoliu = (TextView) findViewById(R.id.text_daoliu);
        edtTime = (EditText) findViewById(R.id.edt_time);
        ivProduceTime = (ImageView) findViewById(R.id.iv_produceTime);
        edtBasicNum = (EditText) findViewById(R.id.edt_basicNum);
        spinnerRatioInit = (Spinner) findViewById(R.id.spinner_ratio_init);
        pulse = (TextView) findViewById(R.id.pulse);
        tvTimeWlwd = (TextView) findViewById(R.id.tv_time_wlwd);
        textChushizhi = (TextView) findViewById(R.id.text_chushizhi);
        btnTerminal = (Button) findViewById(R.id.btn_terminal);
        btnTerminalRead = (Button) findViewById(R.id.btn_terminalRead);
        spinnerRatioInit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String m = (String) spinnerRatioInit.getSelectedItem();
                pulse.setText(LouShanYunUtils.getPulseConstant(m));
                if (!XHStringUtil.isEmpty(edtBasicNum.getText().toString(), false)) {
                    long maiChongDishu = Long.valueOf(edtBasicNum.getText().toString());
                    String beiLv = (String) spinnerRatioInit.getSelectedItem();
                    BigDecimal a1 = new BigDecimal(beiLv);
                    BigDecimal b1 = new BigDecimal(maiChongDishu);
                    double result1 = a1.multiply(b1).doubleValue();// 相乘结果
                    textChushizhi.setText(String.valueOf(result1) + "m³");
                } else {
                    textChushizhi.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        edtBasicNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (!XHStringUtil.isEmpty(charSequence.toString(), false)) {
                        long maiChongDishu = Long.valueOf(charSequence.toString());
                        String beiLv = (String) spinnerRatioInit.getSelectedItem();
                        BigDecimal a1 = new BigDecimal(beiLv);
                        BigDecimal b1 = new BigDecimal(maiChongDishu);
                        double result1 = a1.multiply(b1).doubleValue();// 相乘结果
                        textChushizhi.setText(String.valueOf(result1) + "m³");
                    } else {
                        textChushizhi.setText("");
                    }
                } catch (Exception e) {
                    textChushizhi.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 1; i <= 88; i++) {
            arrayList.add(String.valueOf(i));
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(LSYModule02Activity.this, R.layout.item_spinner, arrayList);
        spinnerDuoChannel.setAdapter(arrayAdapter);

        btnTerminal.setOnClickListener(v -> {
            if (!radioShuangmoshi.isChecked()) {
                sendMessageToast("暂不支持多模式");
                return;
            }
            if (tvConnStateWlwd.getText().toString().trim().equals("未连接,点击重连")) {
                ToastManager.getInstance(LSYModule02Activity.this).show("未连接蓝牙设备，请先连接");
                return;
            }
            if(type!=2){
                sendMessageToast("识别不是2号模组，请选择正确的模组");
                return;
            }
            String cpxs_wlwd = textCpxsWlwd.getText().toString().trim();
            String cgxh = spinnerCgxhWlwd.getSelectedItem().toString().trim();
            String sfpl_wlwd = textSfplWlwd.getText().toString().trim();
            int cjbs_wlwd = LoginParamManager.getInstance().getProductRegister().getManufacturersIdentification();
            String cgqzt_wlwd = textChuangganqizhuangtai.getText().toString().trim();
            String ccgr_wlwd = textCichangzhuangtai.getText().toString().trim();
            String cx_wlwd = spinnerCaixie.getSelectedItem().toString().trim();
            String dl_wlwd = textDaoliu.getText().toString().trim();
            String SpreadingFactor = spinnerSpreadingFactor.getSelectedItem().toString().trim();
            String channel = spinnerChannel.getSelectedItem().toString().trim();
            String time = tvTimeWlwd.getText().toString().trim();
            String[] split = time.split("-");
            String year = split[0].substring(split[0].length() - 2, split[0].length());
            String month = split[1].toString();
            String day = split[2].toString();
            equipmentID = edtTime.getText().toString().trim();
            if (XHStringUtil.isEmpty(equipmentID, false)) {
                ToastManager.getInstance(LSYModule02Activity.this).show("请输入设备ID");
                return;
            }
            basicNumber = edtBasicNum.getText().toString().trim();
            if (XHStringUtil.isEmpty(basicNumber, false)) {
                ToastManager.getInstance(LSYModule02Activity.this).show("请输入表底数");
                return;
            }

            //开始封装设置数据
            byte[] d = new byte[21];
            d[0] = (byte) 0x68;
            d[1] = (byte) 0x11;//有效数据
            d[2] = (byte) 0x20;//命令
            if (cpxs_wlwd.equals("远传物联网端")) {
                d[3] = (byte) 0x03;//产品形式
            }
            d[4] = (byte) 0x00;//信号类型，全部是模拟信号
            if (cgxh.equals("单EV")) {
                d[5] = (byte) 0x09;//传感信号 。针对2ev 0x03，无磁正反脉冲0x0a
            } else if (cgxh.equals("2EV")) {
                d[5] = (byte) 0x03;//传感信号 。针对2ev 0x03，无磁正反脉冲0x0a
            } else if (cgxh.equals("3EV")) {
                d[5] = (byte) 0x01;//传感信号 。针对2ev 0x03，无磁正反脉冲0x0a
            } else if (cgxh.equals("无磁正反脉冲")) {
                d[5] = (byte) 0x0a;//传感信号 。针对2ev 0x03，无磁正反脉冲0x0a
            } else {
                d[5] = (byte) 0x05;//传感信号 。针对2ev 0x03，无磁正反脉冲0x0a
            }
            if (cx_wlwd.equals("无")) {
                d[6] = (byte) 0x00;//拆卸
            } else {
                d[6] = (byte) 0x01;//拆卸
            }
            if (ccgr_wlwd.equals("无")) {
                d[7] = (byte) 0x00;//强磁
            } else {
                d[7] = (byte) 0x01;//强磁
            }
            if (cgqzt_wlwd.equals("无")) {
                d[8] = (byte) 0x00;//传感器状态
            } else {
                d[8] = (byte) 0x01;//传感器状态
            }
            if (dl_wlwd.equals("无")) {
                d[9] = (byte) 0x00;//倒流
            } else {
                d[9] = (byte) 0x01;//倒流
            }
            d[10] = (byte) 0x00;//阀门状态
            d[11] = (byte) 0x30; // 发送频率 固定 24小时上传一次

            d[12] = (byte) Integer.parseInt(year);//出厂时间设置
            d[13] = (byte) Integer.parseInt(month);
            d[14] = (byte) Integer.parseInt(day);
            d[15] = (byte) (cjbs_wlwd & 0xff);
            d[16] = (byte) ((cjbs_wlwd & 0xff00)>>8);
            d[17] = (byte) LouShanYunUtils.getKPYZWriteCodeByString(SpreadingFactor);//扩频因子
            d[18] = (byte) LouShanYunUtils.getXDCSWriteStringByCode02(channel);//信道
            byte cs = 0;
            for (int i = 1; i < 19; i++) {
                cs += d[i];
            }
            d[19] = cs;//校验和
            d[20] = (byte) 0x16;
            write(d, "正在写入第一数据...");
        });
        btnTerminalRead.setOnClickListener(v -> {
            if (tvConnStateWlwd.getText().toString().trim().equals("未连接,点击重连")) {
                ToastManager.getInstance(LSYModule02Activity.this).show("未连接蓝牙设备，请先连接");
                return;
            }
            if(type!=2){
                sendMessageToast("识别不是2号模组，请选择正确的模组");
                return;
            }
            Intent intent = new Intent(LSYModule02Activity.this, ReadInfoWlwdActivity.class);
            intent.putExtra("sensoroDevice", sensoroDevice);
            LSYModule02Activity.this.startActivity(intent);
        });
        tvConnStateWlwd.setOnClickListener(v -> {
            if (tvConnStateWlwd.getText().toString().equals("未连接,点击重连")) {
                Message msg = handler.obtainMessage();
                msg.what = MSG_CONN;
                msg.sendToTarget();
                LoadingDialogUtil.showByEvent("重新连接中", loadingTag);
            }
        });
        loadingTag = this.getClass().getName();
        Message msg = handler.obtainMessage();
        msg.what = MSG_CONN;
        msg.sendToTarget();
        //生成时间戳
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String date = df.format(new Date());// new Date()为获取当前系统时间，也可使用当前时间戳
        tvTimeWlwd.setText(date);
        spinnerCgxhWlwd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                String data = (String) spinnerCgxhWlwd.getItemAtPosition(position);//从spinner中获取被选择的数据
                //绑定值
                if (data.equals("3EV")) {
                    spinnerRatioInit.setSelection(2);
                    textCichangzhuangtai.setText("有");
                    textDaoliu.setText("有");
                } else if (data.equals("无磁正反脉冲")) {
                    spinnerRatioInit.setSelection(0);
                    textCichangzhuangtai.setText("无");
                    textDaoliu.setText("有");
                } else if (data.equals("2EV")) {
                    spinnerRatioInit.setSelection(1);
                    textCichangzhuangtai.setText("有");
                    textDaoliu.setText("无");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        ivProduceTime.setOnClickListener(v -> {
            //生成时间戳
            String year;
            String month;
            String day;
            String hours;
            String minutes;
            String seconds;
            Date date1 = new Date();// new Date()为获取当前系统时间，也可使用当前时间戳
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
            String dateF = df1.format(date1);
            String[] split = dateF.split("-");
            year = split[0].substring(split[0].length() - 2, split[0].length());
            month = split[1].toString();
            day = split[2].toString();

            SimpleDateFormat df2 = new SimpleDateFormat("HH:mm:ss");//设置日期格式
            String dateF1 = df2.format(date1);// new Date()为获取当前系统时间，也可使用当前时间戳
            String[] split1 = dateF1.split(":");
            hours = split1[0].substring(split1[0].length() - 2, split1[0].length());
            minutes = split1[1].toString();
            seconds = split1[2].toString();
            edtTime.setText(year + month + day + hours + minutes + seconds);
        });
    }

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    public void onChildConnectFailed(int arg0) {
        LoadingDialogUtil.dismissByEvent(loadingTag);
        Message msg = handler.obtainMessage();
        msg.what = MSG_TOASTFAILED;
        msg.sendToTarget();
    }

    @Override
    public void onChildConnectSuccess() {
        Message msg = handler.obtainMessage();
        msg.what = MSG_CONNSUCCESS;
        msg.sendToTarget();
    }

    @Override
    public void onChildNotify(byte[] output) {
        //当连接770e时返回104 2 -96 0 -94 22
        String hexString = Integer.toHexString(output[2] & 0xFF);
        json = new JSONObject();
        if (hexString.equals("a0")) {
            //解析使用类型
            if (output[3] == 0) {
                if (json != null) {
                    LoadingDialogUtil.showByEvent("正在写入第二数据...", loadingTag);
                    handler.sendEmptyMessageDelayed(MSG_SETSUCCESS, 2500);
                }
                return;
            }
        } else if (hexString.equals("82")) {
            sendMessageToast("设置成功");
        } else if (hexString.equals("91")) {//解析使用类型
            if (output[3] == 0) {
                try {
                    HashMap<String, String> hashMap = DataParser.getInformationAll(output);
                    pinLv = LouShanYunUtils.getFSPLReadStringByCode(Long.valueOf(hashMap.get(MapParams.发送频率)));
                    type = DataParser.getModuleType(output);
                    if (type == 2) {
                        sendMessageToast("识别成功");
//                        write(DataParser.CMD_METER_INFO, "读取中...", true, 6000);
                    } else {
                        sendMessageToast("识别不是2号模组，请选择正确的模组");
                    }
                } catch (Exception e) {
                    pinLv = "";
                    sendMessageToast("识别不是2号模组，请选择正确的模组");
                }

            }
        } else if (hexString.equals("a1")) {
            if (output[3] == 0) {
                try {
                    json.put("状态", "成功");
                    sendMessageToast("设置成功");
//                    int fspl = Integer.valueOf(spinnerFasonggonglv.getSelectedItem().toString().trim().replaceAll("dbm", ""));
//                    byte[] bytes = {0x68, 0x02, 0x02, (byte) fspl, (byte) (fspl + 4), 0x16};
//                    write(bytes, "正在设置发送功率...");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onChildWriteSuccess() {

    }

    @Override
    protected void onChildWriteFailure(int i) {

    }

    private void readPinLv() {
        byte[] bytes = DataParser.CMD_INFO_ALL;
        write(bytes, "正在识别...");
    }

    @Override
    public void onClick(View view) {

    }
}
