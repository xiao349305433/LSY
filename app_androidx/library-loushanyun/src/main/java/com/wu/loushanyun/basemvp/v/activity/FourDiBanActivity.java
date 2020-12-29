package com.wu.loushanyun.basemvp.v.activity;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundTextView;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.R;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.m.sat.SAtInstructRealizeFactory;
import com.wu.loushanyun.basemvp.m.sat.util.FourDiBanNumBlueToothUtil;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;

import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.XHStringUtil;

@Route(path = K.FourDiBanActivity)
public class FourDiBanActivity extends BaseSnBlueToothActivity {
    private TextView textDangqianSn;
    private RoundTextView bluetoothConn;
    private RoundTextView bluetoothDisconn;
    private Spinner chuanganxinhaoSelect;
    private RoundTextView chuanganxinhaoSetting;
    private Spinner beilvSelect;
    private EditText editMaichongStart;
    private RoundTextView leijiSetting;
    private EditText editMaxChuanganStart;
    private RoundTextView maxChuanganSetting;
    private RoundTextView leijiChuanganReading;
    private TextView textLeijiInfo;
    private RoundTextView xianxingChuanganReading;
    private TextView textXianxingInfo;
    private RoundTextView zhuangtaiChuanganReading;
    private TextView textZhuangtaiInfo;
    private RoundTextView gongtongChuanganReading;
    private TextView textGongtongInfo;


    private SensoroDevice sensoroDeviceChoose;
    private int type;
    private SAtInstructRealizeFactory sAtInstructRealizeFactory;
    private HashMap<String, String> hashMap;

    @Override
    protected int onChildLayout() {
        return R.layout.l_loushanyun_activity_four_diban;
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        super.onInitAttribute(ba);
        ba.mTitleText = "4号底板";
    }

    @Override
    protected void initView() {
        super.initView();
        textDangqianSn = (TextView) findViewById(R.id.text_dangqian_sn);
        bluetoothConn = (RoundTextView) findViewById(R.id.bluetooth_conn);
        bluetoothDisconn = (RoundTextView) findViewById(R.id.bluetooth_disconn);
        chuanganxinhaoSelect = (Spinner) findViewById(R.id.chuanganxinhao_select);
        chuanganxinhaoSetting = (RoundTextView) findViewById(R.id.chuanganxinhao_setting);
        beilvSelect = (Spinner) findViewById(R.id.beilv_select);
        editMaichongStart = (EditText) findViewById(R.id.edit_maichong_start);
        leijiSetting = (RoundTextView) findViewById(R.id.leiji_setting);
        editMaxChuanganStart = (EditText) findViewById(R.id.edit_max_chuangan_start);
        maxChuanganSetting = (RoundTextView) findViewById(R.id.max_chuangan_setting);
        leijiChuanganReading = (RoundTextView) findViewById(R.id.leiji_chuangan_reading);
        textLeijiInfo = (TextView) findViewById(R.id.text_leiji_info);
        xianxingChuanganReading = (RoundTextView) findViewById(R.id.xianxing_chuangan_reading);
        textXianxingInfo = (TextView) findViewById(R.id.text_xianxing_info);
        zhuangtaiChuanganReading = (RoundTextView) findViewById(R.id.zhuangtai_chuangan_reading);
        textZhuangtaiInfo = (TextView) findViewById(R.id.text_zhuangtai_info);
        gongtongChuanganReading = (RoundTextView) findViewById(R.id.gongtong_chuangan_reading);
        textGongtongInfo = (TextView) findViewById(R.id.text_gongtong_info);

        hashMap = new HashMap<String, String>();
        bluetoothConn.setOnClickListener(v -> {
            if (textDangqianSn.getText().toString().contains("当前连接的SN设备")) {
                sendMessageToast("当前已连接设备");
                return;
            }
            snBlueToothTool.connectBlueTooth(sensoroDeviceChoose);
        });
        bluetoothDisconn.setOnClickListener(v -> {
            snBlueToothTool.disconnect();
            textDangqianSn.setText("当前未连接SN设备");
        });
        ArrayAdapter<String> arrayAdapterChuan = new ArrayAdapter<String>(this, R.layout.l_loushanyun_item_spinner, FourDiBanNumBlueToothUtil.getAllChuanganxinhao());
        chuanganxinhaoSelect.setAdapter(arrayAdapterChuan);
        ArrayAdapter<String> arrayAdapterBeiLv = new ArrayAdapter<String>(this, R.layout.l_loushanyun_item_spinner, FourDiBanNumBlueToothUtil.getAllBeiLv());
        beilvSelect.setAdapter(arrayAdapterBeiLv);

        initBlueClick();
        sAtInstructRealizeFactory = new SAtInstructRealizeFactory();
    }

    private void initBlueClick() {
        chuanganxinhaoSetting.setOnClickListener(this::OnBlueToothClick);
        leijiSetting.setOnClickListener(this::OnBlueToothClick);
        maxChuanganSetting.setOnClickListener(this::OnBlueToothClick);
        leijiChuanganReading.setOnClickListener(this::OnBlueToothClick);
        xianxingChuanganReading.setOnClickListener(this::OnBlueToothClick);
        zhuangtaiChuanganReading.setOnClickListener(this::OnBlueToothClick);
        gongtongChuanganReading.setOnClickListener(this::OnBlueToothClick);
    }

    /**
     * 需要判断蓝牙连接状态的点击事件
     *
     * @param view
     */
    private void OnBlueToothClick(View view) {
        if (!snBlueToothTool.isConnected()) {
            sendMessageToast("未连接蓝牙");
            return;
        }
        if (type != 4) {
            sendMessageToast("识别不是4号模组，请选择正确的模组");
            return;
        }
        int i = view.getId();
        if (i == R.id.chuanganxinhao_setting) {
            snBlueToothTool.write(FourDiBanNumBlueToothUtil.getChuanGanSetting(chuanganxinhaoSelect.getSelectedItem().toString()), "正在设置传感信号");
        } else if (i == R.id.leiji_setting) {
            snBlueToothTool.write(FourDiBanNumBlueToothUtil.getLeiJiSetting(editMaichongStart.getText().toString(), beilvSelect.getSelectedItem().toString()), "初始化累计脉冲");
        } else if (i == R.id.max_chuangan_setting) {
            snBlueToothTool.write(FourDiBanNumBlueToothUtil.getxianxingSetting(editMaxChuanganStart.getText().toString()), "初始化线性传感");
        } else if (i == R.id.leiji_chuangan_reading) {
            snBlueToothTool.write(FourDiBanNumBlueToothUtil.getleijiZhengReading(), "读取正向体积");
            clearAllText();
        } else if (i == R.id.xianxing_chuangan_reading) {
            snBlueToothTool.write(FourDiBanNumBlueToothUtil.getXianxingYaliReading(), "读取压力值");
            clearAllText();
        } else if (i == R.id.zhuangtai_chuangan_reading) {
            snBlueToothTool.write(FourDiBanNumBlueToothUtil.getZhuangtaiReading(), "读取状态传感数据");
            clearAllText();
        } else if (i == R.id.gongtong_chuangan_reading) {
            snBlueToothTool.write(FourDiBanNumBlueToothUtil.getChuanganxinhaoReading(), "读取传感信号");
            clearAllText();
        }
    }

    @Override
    protected void onClickBlueListListener(SensoroDevice sensoroDevice) {
        super.onClickBlueListListener(sensoroDevice);
        this.sensoroDeviceChoose = sensoroDevice;
    }

    private void clearAllText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textLeijiInfo.setText("");
                textXianxingInfo.setText("");
                textZhuangtaiInfo.setText("");
                textGongtongInfo.setText("");
            }
        });
    }

    @Override
    public void onChildConnectFailed(int i) {

    }

    @Override
    public void onChildConnectSuccess() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textDangqianSn.setText("当前连接的SN设备:" + sensoroDeviceChoose.getSerialNumber().toUpperCase());
                }
            });
            clearAllText();
            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).getTwoWriteBytes(), "识别模块中...", true, 6000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onChildNotify(byte[] bytes) {
        byte[] result = new byte[bytes.length - 5];
        for (int i = 0; i < result.length; i++) {
            result[i] = bytes[i + 4];
        }
        byte code = (byte) (result[2] ^ ((byte) 0x60));
        byte code1 = (byte) (bytes[2] ^ ((byte) 0x80));
        XLog.i("蓝牙code=" + code);
        if (code1 == 0x11) {
            try {
                type = DataParser.getModuleType(bytes);
                hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).parseFourRNotifyBytes(bytes));
                XLog.i("版本：" + hashMap.get(MapParams.硬件版本));
                if (type == 4) {
                } else {
                    sendMessageToast("识别不是4号模组，请选择正确的模组");
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendMessageToast("识别失败");
            }
        }
        switch (code) {
            case 0x01:
                if (result[3] == 0) {
                    sendMessageToast("配置成功");
                } else {
                    sendMessageToast("配置失败");
                }
                break;
            case 0x02:
                if (result[3] == 0) {
                    sendMessageToast("配置成功");
                } else if (result[3] == 1) {
                    sendMessageToast("配置失败");
                } else if (result[3] == 2) {
                    sendMessageToast("未配置累计脉冲相关的传感器");
                }
                break;
            case 0x03:
                if (result[3] == 0) {
                    sendMessageToast("配置成功");
                } else if (result[3] == 1) {
                    sendMessageToast("配置失败");
                } else if (result[3] == 2) {
                    sendMessageToast("未配置线性传感相关的传感器");
                }
                break;
            case 0x04:
                if (result[3] == 0) {
                    hashMap.putAll(FourDiBanNumBlueToothUtil.getParseZhengReading(result));
                    snBlueToothTool.write(FourDiBanNumBlueToothUtil.getleijiFuReading(), "读取反向体积");
                    setLeiJiText();
                } else if (result[3] == 1) {
                    sendMessageToast("读取失败");
                } else if (result[3] == 2) {
                    sendMessageToast("未配置累计脉冲相关的传感器");
                }
                break;
            case 0x05:
                if (result[3] == 0) {
                    hashMap.putAll(FourDiBanNumBlueToothUtil.getParseFuReading(result));
                    snBlueToothTool.write(FourDiBanNumBlueToothUtil.getleijiZhengNumReading(), "读取正向脉冲数");
                    setLeiJiText();
                } else if (result[3] == 1) {
                    sendMessageToast("读取失败");
                } else if (result[3] == 2) {
                    sendMessageToast("未配置累计脉冲相关的传感器");
                }
                break;
            case 0x06:
                if (result[3] == 0) {
                    hashMap.putAll(FourDiBanNumBlueToothUtil.getParseZhengNumReading(result));
                    snBlueToothTool.write(FourDiBanNumBlueToothUtil.getleijiFuNumReading(), "读取反向脉冲数");
                    setLeiJiText();
                } else if (result[3] == 1) {
                    sendMessageToast("读取失败");
                } else if (result[3] == 2) {
                    sendMessageToast("未配置累计脉冲相关的传感器");
                }
                break;
            case 0x07:
                if (result[3] == 0) {
                    hashMap.putAll(FourDiBanNumBlueToothUtil.getParseFuNumReading(result));
                    snBlueToothTool.write(FourDiBanNumBlueToothUtil.getleijiBeilvReading(), "读取倍率");
                    setLeiJiText();
                } else if (result[3] == 1) {
                    sendMessageToast("读取失败");
                } else if (result[3] == 2) {
                    sendMessageToast("未配置累计脉冲相关的传感器");
                }
                break;
            case 0x08:
                if (result[3] == 0) {
                    hashMap.putAll(FourDiBanNumBlueToothUtil.getParseBeiLVReading(result));
                    snBlueToothTool.write(FourDiBanNumBlueToothUtil.getleijiLiuLiangReading(), "读取瞬时流量");
                    setLeiJiText();
                } else if (result[3] == 1) {
                    sendMessageToast("读取失败");
                } else if (result[3] == 2) {
                    sendMessageToast("未配置累计脉冲相关的传感器");
                }
                break;
            case 0x09:
                if (result[3] == 0) {
                    hashMap.putAll(FourDiBanNumBlueToothUtil.getParseShunShiReading(result));
                    snBlueToothTool.write(FourDiBanNumBlueToothUtil.getleijiChaixieReading(), "读取拆卸状态");
                    setLeiJiText();
                } else if (result[3] == 1) {
                    sendMessageToast("读取失败");
                } else if (result[3] == 2) {
                    sendMessageToast("未配置累计脉冲相关的传感器");
                }
                break;
            case 0x0a:
                if (result[3] == 0) {
                    hashMap.putAll(FourDiBanNumBlueToothUtil.getParseChaixieReading(result));
                    snBlueToothTool.write(FourDiBanNumBlueToothUtil.getleijiLiuXiangReading(), "读取流向状态");
                    setLeiJiText();
                } else if (result[3] == 1) {
                    sendMessageToast("读取失败");
                } else if (result[3] == 2) {
                    sendMessageToast("未配置累计脉冲相关的传感器");
                }
                break;
            case 0x0b:
                if (result[3] == 0) {
                    hashMap.putAll(FourDiBanNumBlueToothUtil.getParseLiuXiangReading(result));
                    setLeiJiText();
                } else if (result[3] == 1) {
                    sendMessageToast("读取失败");
                } else if (result[3] == 2) {
                    sendMessageToast("未配置累计脉冲相关的传感器");
                }
                break;
            case 0x0c:
                if (result[3] == 0) {
                    hashMap.putAll(FourDiBanNumBlueToothUtil.getParseYaLiReading(result));
                    snBlueToothTool.write(FourDiBanNumBlueToothUtil.getXianxingYaliZtReading(), "读取压力传感器状态");
                    setXianXingText();
                } else if (result[3] == 1) {
                    sendMessageToast("读取失败");
                } else if (result[3] == 2) {
                    sendMessageToast("未配置线性传感的传感器");
                }
                break;
            case 0x0d:
                if (result[3] == 0) {
                    hashMap.putAll(FourDiBanNumBlueToothUtil.getParseYaliZhuangtaiReading(result));
                    snBlueToothTool.write(FourDiBanNumBlueToothUtil.getXianxingYaliZhiReading(), "读取压力值");
                    setXianXingText();
                } else if (result[3] == 1) {
                    sendMessageToast("读取失败");
                } else if (result[3] == 2) {
                    sendMessageToast("未配置线性传感的传感器");
                }
                break;
            case 0x11:
                hashMap.putAll(FourDiBanNumBlueToothUtil.getParseYaLiZhiReading(result));
                snBlueToothTool.write(FourDiBanNumBlueToothUtil.getXianxingMaxZhiReading(), "读取压力值");
                setXianXingText();
                break;
            case 0x12:
                hashMap.putAll(FourDiBanNumBlueToothUtil.getParseYaLiMaxZhiReading(result));
                setXianXingText();
                break;
            case 0x0e:
                if (result[3] == 0) {
                    hashMap.putAll(FourDiBanNumBlueToothUtil.getParseShiChangZhuangtaiReading(result));
                    setZhuangtai();
                } else if (result[3] == 1) {
                    sendMessageToast("读取失败");
                } else if (result[3] == 2) {
                    sendMessageToast("未配置状态传感的传感器");
                }
                break;
            case 0x10:
                if (result[3] == 0) {
                    hashMap.putAll(FourDiBanNumBlueToothUtil.getParseDianyuanZhuangtaiReading(result));
                    setGongtongText();
                } else if (result[3] == 1) {
                    sendMessageToast("读取失败");
                } else if (result[3] == 2) {
                    sendMessageToast("未配置任何传感器");
                }
                break;
            case 0x13:
                if (result[3] == 0) {
                    hashMap.putAll(FourDiBanNumBlueToothUtil.getParseChuanganqiReading(result));
                    snBlueToothTool.write(FourDiBanNumBlueToothUtil.getDianYuanZhuangtaiReading(), "读取电源状态");
                } else if (result[3] == 1) {
                    sendMessageToast("读取失败");
                }
                break;

        }
    }

    private void setGongtongText() {
        StringBuffer sb = new StringBuffer();
        sb.append("传感信号:  ");
        sb.append(hashMap.get(MapParams.传感信号));
        sb.append("\n电池状态:  ");
        sb.append(hashMap.get(MapParams.电池状态));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textGongtongInfo.setText(sb.toString());
            }
        });
    }

    private void setZhuangtai() {
        StringBuffer sb = new StringBuffer();
        sb.append("状态位:  ");
        sb.append(hashMap.get(MapParams.状态位));
        sb.append("\n状态时长:  ");
        sb.append(hashMap.get(MapParams.状态时长));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textZhuangtaiInfo.setText(sb.toString());
            }
        });
    }

    private void setXianXingText() {
        StringBuffer sb = new StringBuffer();
        sb.append("压力读数:  ");
        sb.append(!XHStringUtil.isEmpty(hashMap.get(MapParams.压力读数), false) ? hashMap.get(MapParams.压力读数) : "");
        sb.append("\n压力传感器状态:  ");
        sb.append(!XHStringUtil.isEmpty(hashMap.get(MapParams.压力传感器状态), false) ? hashMap.get(MapParams.压力传感器状态) : "");
        sb.append("\n压力值:  ");
        sb.append(!XHStringUtil.isEmpty(hashMap.get(MapParams.压力值), false) ? hashMap.get(MapParams.压力值) : "");
        sb.append("\n最大值:  ");
        sb.append(!XHStringUtil.isEmpty(hashMap.get(MapParams.最大值), false) ? hashMap.get(MapParams.最大值) : "");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textXianxingInfo.setText(sb.toString());
            }
        });
    }

    private void setLeiJiText() {
        StringBuffer sb = new StringBuffer();
        sb.append("正向体积:  ");
        sb.append(!XHStringUtil.isEmpty(hashMap.get(MapParams.正向体积), false) ? hashMap.get(MapParams.正向体积) : "");
        sb.append("\n反向体积:  ");
        sb.append(!XHStringUtil.isEmpty(hashMap.get(MapParams.反向体积), false) ? hashMap.get(MapParams.反向体积) : "");
        sb.append("\n正向脉冲数:  ");
        sb.append(!XHStringUtil.isEmpty(hashMap.get(MapParams.正向脉冲数), false) ? hashMap.get(MapParams.正向脉冲数) : "");
        sb.append("\n反向脉冲数:  ");
        sb.append(!XHStringUtil.isEmpty(hashMap.get(MapParams.反向脉冲数), false) ? hashMap.get(MapParams.反向脉冲数) : "");
        sb.append("\n倍率:  ");
        sb.append(!XHStringUtil.isEmpty(hashMap.get(MapParams.倍率), false) ? hashMap.get(MapParams.倍率) : "");
        sb.append("\n瞬时流量:  ");
        sb.append(!XHStringUtil.isEmpty(hashMap.get(MapParams.瞬时流量), false) ? hashMap.get(MapParams.瞬时流量) : "");
        sb.append("\n拆卸状态:  ");
        sb.append(!XHStringUtil.isEmpty(hashMap.get(MapParams.表拆卸状态), false) ? hashMap.get(MapParams.表拆卸状态) : "");
        sb.append("\n流向状态:  ");
        sb.append(!XHStringUtil.isEmpty(hashMap.get(MapParams.流向状态), false) ? hashMap.get(MapParams.流向状态) : "");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textLeijiInfo.setText(sb.toString());
            }
        });
    }

    @Override
    public void onChildWriteSuccess() {

    }

    @Override
    public void onChildWriteFailure(int i) {

    }

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }
}
