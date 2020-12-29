package wu.loushanyun.com.modulechiptest.v.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundTextView;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.init.LouShanYunCode;
import com.wu.loushanyun.basemvp.m.CreateUserIdInfo;
import com.wu.loushanyun.basemvp.m.st3.ST3ByteTool;
import com.wu.loushanyun.basemvp.m.st3.ST3Params;
import com.wu.loushanyun.basemvp.p.runner.CreateUserIdRunner;
import com.wu.loushanyun.basemvp.v.activity.BaseSnBlueToothActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.modulechiptest.R;
import wu.loushanyun.com.modulechiptest.init.ChipCode;
import wu.loushanyun.com.modulechiptest.p.runner.FactoryProductionRunner;

@Route(path = K.ThirdTestActivity_0X60)
public class ThirdTestActivity_0X60 extends BaseSnBlueToothActivity {
    private ScrollView scrollview;
    private TextView textDangqianSn;
    private RoundTextView bluetoothConn;
    private RoundTextView bluetoothDisconn;
    private Spinner beilvSelect;
    private EditText editMaichongStart;
    private EditText editIdStart;
    private RoundTextView roundCreateId;
    private Spinner xinhaoSelect;
    private EditText editNum;
    private RoundTextView roundSettingHub;
    private TextView textShowCesi;
    private LinearLayout linearCeshi;
    private RoundTextView roundSettingChushihua;
    private RoundTextView buttonDuquMaichong;
    private TextView textMaichong;
    private RoundTextView roundSettingInfo;
    private RoundTextView buttonDuquInfo;
    private TextView textInfo;
    private RoundTextView buttonDuquYanbiao;
    private TextView textYanbiao;
    private RoundTextView buttonOneSettingRead;
    private TextView textSettingReadInfo;
    private RoundTextView buttonDuquYanbiao1;
    private TextView textYanbiao1;

    private SensoroDevice sensoroDeviceChoose;
    private ST3ByteTool st3ByteTool;
    private boolean ceShi = false;
    private boolean danDuYanBiao = false;
    private HashMap<String, String> hashMapProduct;

    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        snBlueToothTool.setWriteTimeDelay(300);
    }

    @Override
    protected int onChildLayout() {
        return R.layout.m_chip_activity_third_0x60;
    }

    @Override
    protected void onClickBlueListListener(SensoroDevice sensoroDevice) {
        super.onClickBlueListListener(sensoroDevice);
        this.sensoroDeviceChoose = sensoroDevice;
    }


    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        super.onInitAttribute(ba);
        ba.mTitleText = "3号模组测试(新式抄表盒子)";
    }

    @Override
    protected void initView() {
        super.initView();
        scrollview = (ScrollView) findViewById(R.id.scrollview);
        textDangqianSn = (TextView) findViewById(R.id.text_dangqian_sn);
        bluetoothConn = (RoundTextView) findViewById(R.id.bluetooth_conn);
        bluetoothDisconn = (RoundTextView) findViewById(R.id.bluetooth_disconn);
        beilvSelect = (Spinner) findViewById(R.id.beilv_select);
        editMaichongStart = (EditText) findViewById(R.id.edit_maichong_start);
        editIdStart = (EditText) findViewById(R.id.edit_id_start);
        roundCreateId = (RoundTextView) findViewById(R.id.round_create_id);
        xinhaoSelect = (Spinner) findViewById(R.id.xinhao_select);
        editNum = (EditText) findViewById(R.id.edit_num);
        roundSettingHub = (RoundTextView) findViewById(R.id.round_setting_hub);
        textShowCesi = (TextView) findViewById(R.id.text_show_cesi);
        linearCeshi = (LinearLayout) findViewById(R.id.linear_ceshi);
        roundSettingChushihua = (RoundTextView) findViewById(R.id.round_setting_chushihua);
        buttonDuquMaichong = (RoundTextView) findViewById(R.id.button_duqu_maichong);
        textMaichong = (TextView) findViewById(R.id.text_maichong);
        roundSettingInfo = (RoundTextView) findViewById(R.id.round_setting_info);
        buttonDuquInfo = (RoundTextView) findViewById(R.id.button_duqu_info);
        textInfo = (TextView) findViewById(R.id.text_info);
        buttonDuquYanbiao = (RoundTextView) findViewById(R.id.button_duqu_yanbiao);
        textYanbiao = (TextView) findViewById(R.id.text_yanbiao);
        buttonOneSettingRead = (RoundTextView) findViewById(R.id.button_one_setting_read);
        textSettingReadInfo = (TextView) findViewById(R.id.text_setting_read_info);
        buttonDuquYanbiao1 = (RoundTextView) findViewById(R.id.button_duqu_yanbiao_1);
        textYanbiao1 = (TextView) findViewById(R.id.text_yanbiao_1);


        hashMapProduct = new HashMap<>();
        bluetoothConn.setOnClickListener(v -> {
            if (textDangqianSn.getText().toString().contains("当前连接的SN设备")) {
                sendMessageToast("当前已连接设备");
                return;
            }
            snBlueToothTool.connectBlueTooth(sensoroDeviceChoose);
            clearAllText();
        });
        bluetoothDisconn.setOnClickListener(v -> {
            snBlueToothTool.disconnect();
            textDangqianSn.setText("当前未连接SN设备");
        });
        st3ByteTool = new ST3ByteTool();
        initTestClick();
    }

    private void initTestClick() {
        roundSettingHub.setOnClickListener(this::OnBlueToothClick);
        roundSettingChushihua.setOnClickListener(this::OnBlueToothClick);
        roundSettingInfo.setOnClickListener(this::OnBlueToothClick);
        buttonDuquInfo.setOnClickListener(this::OnBlueToothClick);
        buttonDuquMaichong.setOnClickListener(this::OnBlueToothClick);
        buttonDuquYanbiao.setOnClickListener(this::OnBlueToothClick);
        buttonDuquYanbiao1.setOnClickListener(this::OnBlueToothClick);
        roundCreateId.setOnClickListener(this::OnBlueToothClick);

        textShowCesi.setOnClickListener(this::OnBlueToothClick);
        buttonOneSettingRead.setOnClickListener(this::OnBlueToothClick);
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
        switch (view.getId()) {
            case R.id.round_setting_hub:
                snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3InitHub).getThirdInitBubBytes(), "初始化Bub号");
                break;
            case R.id.round_setting_chushihua:
                if (Integer.valueOf(editMaichongStart.getText().toString()) > 16777215) {
                    sendMessageToast("最大只能设置到16777215");
                    return;
                }
                snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3InitBiao).getThirdInitBiaoBytes(
                        Integer.valueOf(editNum.getText().toString())
                        , Long.valueOf(editIdStart.getText().toString())
                        , beilvSelect.getSelectedItem().toString()
                        , Integer.valueOf(editMaichongStart.getText().toString())
                ), "初始化表单元");
                break;
            case R.id.round_setting_info:
                //生成时间戳
                Date date1 = new Date();// new Date()为获取当前系统时间，也可使用当前时间戳
                SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                String dateF = df1.format(date1);
                String[] split = dateF.split("-");
                String year = split[0].substring(split[0].length() - 2, split[0].length());
                String month = split[1].toString();
                String day = split[2].toString();
                snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3SettingInfo).getThirdSettingBiaoBytes(
                        Integer.valueOf(editNum.getText().toString())
                        , xinhaoSelect.getSelectedItem().toString()
                        , Integer.valueOf(year)
                        , Integer.valueOf(month)
                        , Integer.valueOf(day)
                        , 21
                ), "配置表单元");
                break;
            case R.id.round_create_id:
                pushEvent(LouShanYunCode.MChipCreateUserId);

                break;
            case R.id.button_duqu_info:
                snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3ReadInfo).getThirdReadSettingBytes(
                        Integer.valueOf(editNum.getText().toString())), "读取配置信息");
                textInfo.setText("");
                break;
            case R.id.button_duqu_maichong:
                snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3ReadMaiChong).getThirdReadBiaoBytes(
                    Integer.valueOf(editNum.getText().toString())), "抄表");
                textMaichong.setText("");
                break;
            case R.id.button_duqu_yanbiao:
                danDuYanBiao = false;
                snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3ReadYanBiao).getThirdReadYanBiaoBytes(), "验表");
                textYanbiao.setText("");
                break;
            case R.id.button_duqu_yanbiao_1:
                danDuYanBiao = true;
                snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3ReadYanBiao).getThirdReadYanBiaoBytes(), "验表");
                textYanbiao1.setText("");
                break;
            case R.id.text_show_cesi:
                if (!ceShi) {
                    linearCeshi.setVisibility(View.VISIBLE);
                    textShowCesi.setText("关闭");
                    ceShi = true;
                    roundCreateId.setVisibility(View.VISIBLE);
                    scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                } else {
                    linearCeshi.setVisibility(View.GONE);
                    textShowCesi.setText("展开");
                    ceShi = false;
                    roundCreateId.setVisibility(View.GONE);
                }
                break;
            case R.id.button_one_setting_read:
                if (Integer.valueOf(editMaichongStart.getText().toString()) > 16777215) {
                    sendMessageToast("最大只能设置到16777215");
                    return;
                }
                linearCeshi.setVisibility(View.GONE);
                textShowCesi.setText("展开");
                roundCreateId.setVisibility(View.GONE);
                editIdStart.setText("0");
                textSettingReadInfo.setText("");
                ceShi = false;
                snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3InitHub).getThirdInitBubBytes(), "初始化Bub号");
                break;
        }
    }

    private void clearAllText() {
        textInfo.setText("");
        textMaichong.setText("");
        textYanbiao.setText("");
    }

    @Override
    public void onChildConnectFailed(int i) {

    }

    @Override
    public void onChildConnectSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textDangqianSn.setText("当前连接的SN设备:" + sensoroDeviceChoose.getSerialNumber().toUpperCase());
                clearAllText();
            }
        });
    }

    @Override
    public void onChildNotify(byte[] result) {
        byte code = (byte) (result[2] ^ ((byte) 0x80));
        XLog.i("蓝牙code=" + code);
        switch (code) {
            case 0x60:
                if (result[3] == 0) {
                    if (st3ByteTool.getsT3Type().equals(ST3Params.sT3InitHub)) {
                        if (result[4] == 1 && result[5] == 4 && result[6] == (byte) 0xfe && result[7] == (byte) 0xd0) {
                            sendMessageToast("Hub号设置成功");
                            if (!ceShi) {
                                snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3InitBiao).getThirdInitBiaoBytes(
                                        Integer.valueOf(editNum.getText().toString())
                                        , Long.valueOf(editIdStart.getText().toString())
                                        , beilvSelect.getSelectedItem().toString()
                                        , Integer.valueOf(editMaichongStart.getText().toString())
                                ), "初始化表单元");
                            }
                        }
                    } else if (st3ByteTool.getsT3Type().equals(ST3Params.sT3InitBiao)) {
                        if (result[5] == 4 && result[6] == (byte) 0xfc) {
                            sendMessageToast("设置成功");
                            if (!ceShi) {
                                //生成时间戳
                                Date date1 = new Date();// new Date()为获取当前系统时间，也可使用当前时间戳
                                SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                                String dateF = df1.format(date1);
                                String[] split = dateF.split("-");
                                String year = split[0].substring(split[0].length() - 2, split[0].length());
                                String month = split[1].toString();
                                String day = split[2].toString();
                                snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3SettingInfo).getThirdSettingBiaoBytes(
                                        Integer.valueOf(editNum.getText().toString())
                                        , xinhaoSelect.getSelectedItem().toString()
                                        , Integer.valueOf(year)
                                        , Integer.valueOf(month)
                                        , Integer.valueOf(day)
                                        , 21
                                ), "配置表单元");
                            } else {
                                snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3ReadMaiChong).getThirdReadBiaoBytes(
                                        Integer.valueOf(editNum.getText().toString())), "抄表");
                            }
                        }
                    } else if (st3ByteTool.getsT3Type().equals(ST3Params.sT3SettingInfo)) {
                        if (result[5] == 4 && result[6] == (byte) 0xfb) {
                            sendMessageToast("设置成功");
                            if (!ceShi) {
                                snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3ReadMaiChong).getThirdReadBiaoBytes(
                                        Integer.valueOf(editNum.getText().toString())), "抄表");
                            } else {
                                snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3ReadInfo).getThirdReadSettingBytes(
                                        Integer.valueOf(editNum.getText().toString())), "读取配置信息");
                            }
                        } else {
                            sendMessageToast("设置失败");
                        }
                    } else if (st3ByteTool.getsT3Type().equals(ST3Params.sT3ReadMaiChong)) {
                        if (result[5] == (byte) 0x0e && result[6] == (byte) 0xfc) {
                            sendMessageToast("读取成功");
                            if (!ceShi) {
                                snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3ReadInfo).getThirdReadSettingBytes(
                                        Integer.valueOf(editNum.getText().toString())), "读取配置信息");
                                try {
                                    hashMapProduct.putAll(st3ByteTool.parseThirdReadBiaoBytes(result));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    setTextMaiChong(st3ByteTool.parseThirdReadBiaoBytes(result));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        } else {
                            sendMessageToast("读取失败");
                        }
                    } else if (st3ByteTool.getsT3Type().equals(ST3Params.sT3ReadInfo)) {
                        if (result[5] == 0x16 && result[6] == (byte) 0xfd) {
                            if (!ceShi) {
                                try {
                                    hashMapProduct.putAll(st3ByteTool.parseThirdReadSettingBytes(result));
                                    setTextSettingReadInfo(hashMapProduct);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    setTextInfo(st3ByteTool.parseThirdReadSettingBytes(result));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            sendMessageToast("读取信息失败");
                        }
                    } else if (st3ByteTool.getsT3Type().equals(ST3Params.sT3ReadYanBiao)) {
                        if (result[5] == (byte) 0x0e && result[6] == (byte) 0xfc) {
                            if (!ceShi) {
                                try {
                                    setTextYanbiao(st3ByteTool.parseThirdReadYanBiaoBytes(result));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    setTextYanbiao(st3ByteTool.parseThirdReadYanBiaoBytes(result));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            sendMessageToast("验表失败");
                        }
                    }
                } else {
                    sendMessageToast("操作失败");
                }

                break;
        }
    }


    @Override
    protected void initEventListener() {
        registerEventRunner(LouShanYunCode.MChipCreateUserId, new CreateUserIdRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == LouShanYunCode.MChipCreateUserId) {
            if (event.isSuccess()) {
                CreateUserIdInfo createUserIdInfo = (CreateUserIdInfo) event.getReturnParamAtIndex(0);
                if(createUserIdInfo.getCode()==0){
                    editIdStart.setText(createUserIdInfo.getUserId());
                }

            }
        }
    }

    private void setTextInfo(HashMap<String, String> hashMap) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("表号：");
        stringBuilder.append(hashMap.get(MapParams.表号));
        stringBuilder.append("\n设备ID：");
        stringBuilder.append(hashMap.get(MapParams.设备ID));
        stringBuilder.append("\n倍率：");
        stringBuilder.append(LouShanYunUtils.getBLReadStringByCode(hashMap.get(MapParams.倍率)));
        stringBuilder.append("\n状态：");
        stringBuilder.append(LouShanYunUtils.getZTReadStringByCode(hashMap.get(MapParams.状态)));
        stringBuilder.append("\n传感类型：");
        stringBuilder.append(LouShanYunUtils.getCGXHReadStringByCode(hashMap.get(MapParams.传感类型)));
        stringBuilder.append("\n软件版本：");
        stringBuilder.append(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
        stringBuilder.append("\n企业代码：");
        stringBuilder.append(hashMap.get(MapParams.企业代码));
        stringBuilder.append("\nHUB号：");
        stringBuilder.append(hashMap.get(MapParams.HUB号));
        stringBuilder.append("\n出厂时间:  ");
        stringBuilder.append("20" + hashMap.get(MapParams.出厂时间_年) + "-" + hashMap.get(MapParams.出厂时间_月) + "-" + hashMap.get(MapParams.出厂时间_日));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textInfo.setText(stringBuilder.toString());
            }
        });
    }

    private void setTextMaiChong(HashMap<String, String> hashMap) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("表号：");
        stringBuilder.append(hashMap.get(MapParams.表号));
        stringBuilder.append("\n设备ID：");
        stringBuilder.append(hashMap.get(MapParams.设备ID));
        stringBuilder.append("\n倍率：");
        stringBuilder.append(LouShanYunUtils.getBLReadStringByCode(hashMap.get(MapParams.倍率)));
        stringBuilder.append("\n状态：");
        stringBuilder.append(LouShanYunUtils.getZTReadStringByCode(hashMap.get(MapParams.状态)));
        stringBuilder.append("\n脉冲数：");
        stringBuilder.append(hashMap.get(MapParams.脉冲数));
        stringBuilder.append("\nHUB号：");
        stringBuilder.append(hashMap.get(MapParams.HUB号));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textMaichong.setText(stringBuilder.toString());
            }
        });
    }

    private void setTextYanbiao(HashMap<String, String> hashMap) {
        StringBuilder stringBuilder = new StringBuilder();
        if (danDuYanBiao) {
            stringBuilder.append("倍率：");
            stringBuilder.append(LouShanYunUtils.getBLReadStringByCode(hashMap.get(MapParams.倍率)));
            stringBuilder.append("\n脉冲数：");
            stringBuilder.append(hashMap.get(MapParams.脉冲数));
            stringBuilder.append("\n状态：");
            stringBuilder.append(LouShanYunUtils.getZTReadStringByCode(hashMap.get(MapParams.状态)));
        }else {
            stringBuilder.append("表号：");
            stringBuilder.append(hashMap.get(MapParams.表号));
            stringBuilder.append("\n设备ID：");
            stringBuilder.append(hashMap.get(MapParams.设备ID));
            stringBuilder.append("\n倍率：");
            stringBuilder.append(LouShanYunUtils.getBLReadStringByCode(hashMap.get(MapParams.倍率)));
            stringBuilder.append("\n状态：");
            stringBuilder.append(LouShanYunUtils.getZTReadStringByCode(hashMap.get(MapParams.状态)));
            stringBuilder.append("\n脉冲数：");
            stringBuilder.append(hashMap.get(MapParams.脉冲数));
            stringBuilder.append("\nHUB号：");
            stringBuilder.append(hashMap.get(MapParams.HUB号));
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (danDuYanBiao) {
                    textYanbiao1.setText(stringBuilder.toString());
                    scrollview.fullScroll(ScrollView.FOCUS_DOWN);
                }else {
                    textYanbiao.setText(stringBuilder.toString());
                }
            }
        });
    }

    private void setTextSettingReadInfo(HashMap<String, String> hashMap) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("表号：");
        stringBuilder.append(hashMap.get(MapParams.表号));
        stringBuilder.append("\n设备ID：");
        stringBuilder.append(hashMap.get(MapParams.设备ID));
        stringBuilder.append("\n倍率：");
        stringBuilder.append(LouShanYunUtils.getBLReadStringByCode(hashMap.get(MapParams.倍率)));
        stringBuilder.append("\n状态：");
        stringBuilder.append(LouShanYunUtils.getZTReadStringByCode(hashMap.get(MapParams.状态)));
        stringBuilder.append("\n脉冲数：");
        stringBuilder.append(hashMap.get(MapParams.脉冲数));
        stringBuilder.append("\nHUB号：");
        stringBuilder.append(hashMap.get(MapParams.HUB号));
        stringBuilder.append("\n生产时间:  ");
        stringBuilder.append("20" + hashMap.get(MapParams.出厂时间_年) + "-" + hashMap.get(MapParams.出厂时间_月) + "-" + hashMap.get(MapParams.出厂时间_日));
        stringBuilder.append("\n传感类型：");
        stringBuilder.append(LouShanYunUtils.getCGXHReadStringByCode(hashMap.get(MapParams.传感类型)));
        stringBuilder.append("\n软件版本：");
        stringBuilder.append(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
        stringBuilder.append("\n企业代码：");
        stringBuilder.append(hashMap.get(MapParams.企业代码));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textSettingReadInfo.setText(stringBuilder.toString());
                scrollview.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    @Override
    public void onChildWriteSuccess() {

    }

    @Override
    public void onChildWriteFailure(int i) {

    }




}
