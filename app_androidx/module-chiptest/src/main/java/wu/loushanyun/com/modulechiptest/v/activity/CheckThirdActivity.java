package wu.loushanyun.com.modulechiptest.v.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.lidroid.xutils.util.LogUtils;
import com.qrcore.util.QRScannerHelper;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;
import com.wu.loushanyun.basemvp.m.st3.ST3ByteTool;
import com.wu.loushanyun.basemvp.m.st3.ST3Params;
import com.wu.loushanyun.basemvp.v.activity.BaseSnBlueToothActivity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ByteConvertUtils;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.libraryfive.p.adapter.SNDeviceViewBinder;
import wu.loushanyun.com.module_initthree.v.activity.ThirdTestNewActivity;
import wu.loushanyun.com.modulechiptest.R;
import wu.loushanyun.com.modulechiptest.init.ChipCode;
import wu.loushanyun.com.modulechiptest.init.LoginParamManager;
import wu.loushanyun.com.modulechiptest.m.CheckUpData;
import wu.loushanyun.com.modulechiptest.m.ResultJson;
import wu.loushanyun.com.modulechiptest.m.SelectallInfo;
import wu.loushanyun.com.modulechiptest.p.runner.GetUpDataRunner;
import wu.loushanyun.com.modulechiptest.p.runner.MChipSetRxDelayRunner;

@Route(path = K.CheckThirdActivity)
public class CheckThirdActivity extends BaseSnBlueToothActivity {
    private ArrayList<SensoroDevice> deviceArrayList;

    private ScrollView scrollView;
    private TextView textTop;
    private TextView textDangqianSn;
    private RoundTextView bluetoothConn;
    private RoundTextView bluetoothDisconn;
    private LinearLayout layoutYanbiao;
    private RoundTextView roundYanbiao;
    private EditText roundYanbiaoId;
    private LinearLayout linearBiaohao;
    private EditText edtMeternum;
    private Spinner spinnerRatioInit;
    private LinearLayout linearMaichong;
    private TextView pulse;
    private EditText editChushimaichong;
    private RoundTextView btnInitmeter;
    private RoundTextView btnSend2;
    private RoundTextView btnShezhi;
    private RoundTextView btnRead;

    private RoundTextView roundReadAll;
    private EditText textNum;
    private RoundTextView textNumSaomiao;
    private RoundTextView roundUpdata;
    private TextView textReadInfo;
    private RoundTextView roundReadinfo;
    private Spinner xinhaoSelect;

    private SelectallInfo.DataBean dataBean;
    private SensoroDevice sensoroDeviceChoose;
    private SNDeviceViewBinder snDeviceViewBinder;
    private ST3ByteTool st3ByteTool;
    private QRScannerHelper mScannerHelper;
    private int saomiaoType = 0;

    private boolean canSet = false;
    private boolean setSuccess = false;

    private int fromType;//0：更换表单元进来验表获取验表状态的,1:更换表单元进来初始化，2：初始化
    private boolean deviceGenghuanZhuangtai;//验表失败，返回true
    private int biaoHao;//验表失败，返回true
    private String resultBiaoHao;//新设备的表号
    private String resultID;//新设备的id
    private HashMap<String, String> hashMap;
    private boolean isShezhi;
    private boolean isDuqu;

    @Override
    protected int onChildLayout() {
        return R.layout.m_chip_activity_checkthird;
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        super.onInitAttribute(ba);
        getAllData();
        ba.mTitleText = "3号模组质检";
    }

    private void getAllData() {
        fromType = getIntent().getIntExtra("fromType", 2);
        biaoHao = getIntent().getIntExtra("biaoHao", 0);
    }

    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        snBlueToothTool.setWriteTimeDelay(300);
    }

    @Override
    protected void onClickBlueListListener(SensoroDevice sensoroDevice) {
        super.onClickBlueListListener(sensoroDevice);
        this.sensoroDeviceChoose = sensoroDevice;
    }


    @Override
    protected void initView() {
        super.initView();
        scrollView = (ScrollView) findViewById(wu.loushanyun.com.module_initthree.R.id.scroll_view);
        textTop = (TextView) findViewById(wu.loushanyun.com.module_initthree.R.id.text_top);
        textDangqianSn = (TextView) findViewById(wu.loushanyun.com.module_initthree.R.id.text_dangqian_sn);
        bluetoothConn = (RoundTextView) findViewById(wu.loushanyun.com.module_initthree.R.id.bluetooth_conn);
        bluetoothDisconn = (RoundTextView) findViewById(wu.loushanyun.com.module_initthree.R.id.bluetooth_disconn);
        layoutYanbiao = (LinearLayout) findViewById(wu.loushanyun.com.module_initthree.R.id.layout_yanbiao);
        roundYanbiao = (RoundTextView) findViewById(wu.loushanyun.com.module_initthree.R.id.round_yanbiao);
        roundYanbiaoId = (EditText) findViewById(wu.loushanyun.com.module_initthree.R.id.round_yanbiao_id);
        linearBiaohao = (LinearLayout) findViewById(wu.loushanyun.com.module_initthree.R.id.linear_biaohao);
        edtMeternum = (EditText) findViewById(wu.loushanyun.com.module_initthree.R.id.edt_meternum);
        spinnerRatioInit = (Spinner) findViewById(wu.loushanyun.com.module_initthree.R.id.spinner_ratio_init);
        linearMaichong = (LinearLayout) findViewById(wu.loushanyun.com.module_initthree.R.id.linear_maichong);
        pulse = (TextView) findViewById(wu.loushanyun.com.module_initthree.R.id.pulse);
        editChushimaichong = (EditText) findViewById(wu.loushanyun.com.module_initthree.R.id.edit_chushimaichong);
        btnInitmeter = (RoundTextView) findViewById(wu.loushanyun.com.module_initthree.R.id.btn_initmeter);
        btnSend2 = (RoundTextView) findViewById(wu.loushanyun.com.module_initthree.R.id.btn_send2);
        btnShezhi = (RoundTextView) findViewById(R.id.btn_shezhi);
        xinhaoSelect = (Spinner) findViewById(R.id.xinhao_select);
        roundReadAll = (RoundTextView) findViewById(wu.loushanyun.com.module_initthree.R.id.round_readAll);
        textNum = (EditText) findViewById(R.id.text_num);
        btnRead = (RoundTextView) findViewById(R.id.btn_read);
        textNumSaomiao = (RoundTextView) findViewById(R.id.text_num_saomiao);
        textReadInfo = (TextView) findViewById(R.id.text_ReadInfo);

        hashMap = new HashMap<>();

        dataBean = getIntent().getParcelableExtra("order");
        if (fromType == 0) {
            textTop.setVisibility(View.VISIBLE);
            textTop.setText("请连接" + biaoHao + "号表单元进行验表测试!!!");
        } else if (fromType == 1) {
            textTop.setVisibility(View.VISIBLE);
            textTop.setText("请连接新表单元进行验表并初始化!!!");
        } else if (fromType == 2) {
            textTop.setVisibility(View.GONE);
        }
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

        st3ByteTool = new ST3ByteTool();
        initTestClick();
        initQRScanner();
    }


    private void initTestClick() {
        roundYanbiao.setOnClickListener(this::OnBlueToothClick);
        btnInitmeter.setOnClickListener(this::OnBlueToothClick);
        textNumSaomiao.setOnClickListener(this::OnBlueToothClick);
        btnShezhi.setOnClickListener(this::OnBlueToothClick);
        btnRead.setOnClickListener(this::OnBlueToothClick);
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
        int id = view.getId();
        if (id == R.id.round_yanbiao) {
            snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3ReadYanBiao).getThirdReadYanBiaoBytes(), "验表");

        } else if (id == R.id.btn_initmeter) {
            if (isDuqu == false) {
                sendMessageToast("请先读取再上传");
                return;
            }
            updata(hashMap);

        } else if (id == R.id.btn_read) {
            if (isShezhi == false) {
                sendMessageToast("请先设置再读取");
                return;
            }
            snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3ReadMaiChong).getThirdReadBiaoBytes(
                    Integer.valueOf(edtMeternum.getText().toString())), "抄表");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textReadInfo.setText("");
                }
            });


        } else if (id == R.id.roundRepaire) {
            Intent result = new Intent();
            if (fromType == 0) {
                if (!deviceGenghuanZhuangtai) {
                    sendMessageToast("必须验表失败才能更换");
                    return;
                }
                result.putExtra("deviceGenghuanZhuangtai", deviceGenghuanZhuangtai);
                setResult(RESULT_OK, result);
                finish();
            } else if (fromType == 1) {
                if (setSuccess) {
                    try {
                        if (biaoHao == Integer.valueOf(resultBiaoHao)) {
                            result.putExtra("resultBiaoHao", resultBiaoHao);
                            result.putExtra("resultID", resultID);
                            setResult(RESULT_OK, result);
                            finish();
                        } else {
                            ToastUtils.showShort("新设备必须初始化成" + biaoHao + "号表");
                        }
                    } catch (Exception e) {
                        LogUtils.e(e.getMessage());
                        ToastUtils.showShort("新设备初始化失败");
                    }
                } else {
                    ToastUtils.showShort("请点击设置初始化新设备");
                }
            }

        } else if (id == R.id.text_num_saomiao) {
            saomiaoType = 0;
            start();
        } else if (id == R.id.btn_shezhi) {
            if (!canSet) {
                sendMessageToast("验表成功后才能进行设置");
                return;
            }
            if (XHStringUtil.isEmpty(editChushimaichong.getText().toString(), false)) {
                sendMessageToast("请输入初始脉冲数");
                return;
            }
            if (Integer.valueOf(editChushimaichong.getText().toString()) > 16777215) {
                sendMessageToast("最大只能设置到16777215");
                return;
            }
            if (XHStringUtil.isEmpty(edtMeternum.getText().toString(), false)) {
                sendMessageToast("请设置表号");
                return;
            }
            if (fromType == 1) {
                this.resultBiaoHao = edtMeternum.getText().toString();
                this.resultID = roundYanbiaoId.getText().toString();
                if (biaoHao != Integer.valueOf(resultBiaoHao)) {
                    sendMessageToast("新设备必须初始化成" + biaoHao + "号表");
                    return;
                }
            }
            snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3InitBiao).getThirdInitBiaoBytes(
                    Integer.valueOf(edtMeternum.getText().toString())
                    , Long.valueOf(roundYanbiaoId.getText().toString())
                    , spinnerRatioInit.getSelectedItem().toString()
                    , Integer.valueOf(editChushimaichong.getText().toString())
            ), "初始化表单元");

        }
    }


    /**
     * 在onCreate中调用
     */
    private void initQRScanner() {
        mScannerHelper = new QRScannerHelper(this);
        mScannerHelper.setCallBack(new QRScannerHelper.OnScannerCallBack() {
            @Override
            public void onScannerBack(String result) {
                if (saomiaoType == 0) {
                    textNum.setText(result);
                }
            }
        });
    }


    /**
     * 开启扫描界面
     */
    public void start() {
        mScannerHelper.startScanner();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mScannerHelper != null) {
            mScannerHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void initEventListener() {
        registerEventRunner(ChipCode.MChipGetUpdata, new GetUpDataRunner());

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == ChipCode.MChipGetUpdata) {
            ResultJson resultJson = (ResultJson) event.getReturnParamAtIndex(0);
            int resultcode = resultJson.getCode();
            if (resultcode == 0) {

            } else if (resultcode == 1) {

            }
            sendMessageToast(resultJson.getMsg());
        }
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
                sendMessageToast("连接成功");
            }
        });
    }


    @Override
    public void onChildNotify(byte[] result) {
        byte code = (byte) (result[2] ^ ((byte) 0x80));
        switch (code) {
            case 0x60:
                if (result[3] == 0) {
                    if (st3ByteTool.getsT3Type().equals(ST3Params.sT3ReadYanBiao)) {
                        if (result[5] == (byte) 0x0e && result[6] == (byte) 0xfc) {
                            setSuccess = false;
                            try {
                                hashMap.putAll(st3ByteTool.parseThirdReadYanBiaoBytes(result));
                                setTextYanbiao(st3ByteTool.parseThirdReadYanBiaoBytes(result));
                                canSet = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                                canSet = false;
                            }
                            sendMessageToast("验表成功");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textReadInfo.setText("");
                                    textNum.setText("");
                                    isShezhi = false;
                                    isDuqu = false;
                                }
                            });
                        } else {
                            canSet = false;
                            sendMessageToast("验表失败");
                        }
                    } else if (st3ByteTool.getsT3Type().equals(ST3Params.sT3InitBiao)) {
                        if (result[5] == 4 && result[6] == (byte) 0xfc) {
                            sendMessageToast("设置成功");
                            Date date1 = new Date();// new Date()为获取当前系统时间，也可使用当前时间戳
                            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                            String dateF = df1.format(date1);
                            String[] split = dateF.split("-");
                            String year = split[0].substring(split[0].length() - 2, split[0].length());
                            String month = split[1].toString();
                            String day = split[2].toString();
                            snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3SettingInfo).getThirdSettingBiaoBytes(
                                    Integer.valueOf(edtMeternum.getText().toString())
                                    , xinhaoSelect.getSelectedItem().toString()
                                    , Integer.valueOf(year)
                                    , Integer.valueOf(month)
                                    , Integer.valueOf(day)
                                    , 21
                            ), "配置表单元");
                            setSuccess = true;

                        } else {
                            sendMessageToast("设置失败");
                            canSet = true;
                        }
                    } else if (st3ByteTool.getsT3Type().equals(ST3Params.sT3ReadMaiChong)) {
                        if (result[5] == (byte) 0x0e && result[6] == (byte) 0xfc) {
                            sendMessageToast("读取成功");
                            try {
                                hashMap.putAll(st3ByteTool.parseThirdReadBiaoBytes(result));
                                snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3ReadInfo).getThirdReadSettingBytes(
                                        Integer.valueOf(edtMeternum.getText().toString())), "读取配置信息");
                                //      setTextMaiChong(st3ByteTool.parseThirdReadBiaoBytes(result));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            sendMessageToast("读取失败");
                        }
                    } else if (st3ByteTool.getsT3Type().equals(ST3Params.sT3ReadInfo)) {
                        if (result[5] == 0x16 && result[6] == (byte) 0xfd) {
                            try {
                                hashMap.putAll(st3ByteTool.parseThirdReadSettingBytes(result));
                                setTextSettingReadInfo(hashMap);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            sendMessageToast("读取信息失败");
                        }
                    } else if (st3ByteTool.getsT3Type().equals(ST3Params.sT3SettingInfo)) {
                        if (result[5] == 4 && result[6] == (byte) 0xfb) {
                            sendMessageToast("设置成功");
                            isShezhi = true;
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    textReadInfo.setText("");
                                }
                            });
                        } else {
                            sendMessageToast("设置失败");
                        }
                    }
                }

                break;
        }

    }

    @Override
    public void onChildWriteSuccess() {

    }

    @Override
    public void onChildWriteFailure(int i) {

    }


    private void setTextSettingReadInfo(HashMap<String, String> hashMapread) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("表号：");
        stringBuilder.append(hashMapread.get(MapParams.表号));
        stringBuilder.append("\n设备ID：");
        stringBuilder.append(hashMapread.get(MapParams.设备ID));
        stringBuilder.append("\n倍率：");
        stringBuilder.append(LouShanYunUtils.getBLReadStringByCode(hashMapread.get(MapParams.倍率)));
        stringBuilder.append("\n状态：");
        stringBuilder.append(LouShanYunUtils.getZTReadStringByCode(hashMapread.get(MapParams.状态)));
        stringBuilder.append("\n脉冲数：");
        stringBuilder.append(hashMapread.get(MapParams.脉冲数));
        stringBuilder.append("\nHUB号：");
        stringBuilder.append(hashMapread.get(MapParams.HUB号));
        stringBuilder.append("\n生产时间:  ");
        stringBuilder.append("20" + hashMapread.get(MapParams.出厂时间_年) + "-" + hashMapread.get(MapParams.出厂时间_月) + "-" + hashMapread.get(MapParams.出厂时间_日));
        stringBuilder.append("\n传感类型：");
        stringBuilder.append(LouShanYunUtils.getCGXHReadStringByCode(hashMapread.get(MapParams.传感类型)));
        stringBuilder.append("\n软件版本：");
        stringBuilder.append(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMapread.get(MapParams.软件版本))));
        stringBuilder.append("\n企业代码：");
        stringBuilder.append(hashMapread.get(MapParams.企业代码));
        runOnUiThread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void run() {
                isDuqu = true;
                textReadInfo.setText(stringBuilder.toString());
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        //滑动到底部
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });


            }
        });
    }



    private void setTextYanbiao(HashMap<String, String> hashMap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                edtMeternum.setText(hashMap.get(MapParams.表号));
                roundYanbiaoId.setText(hashMap.get(MapParams.设备ID));
                spinnerRatioInit.setSelection(LouShanYunUtils.getIntByValueSelection(hashMap.get(MapParams.倍率)));
                //     editChushimaichong.setText(hashMap.get(MapParams.脉冲数));
                //      roundYanbiaoZhuangtai.setText(LouShanYunUtils.getZTReadStringByCode(hashMap.get(MapParams.状态)));

            }
        });
    }


    private void updata(HashMap<String, String> hashMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("软件版本:");
        sb.append(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
        sb.append(";硬件版本:");
        sb.append(LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
        sb.append(";传感类型:");
        sb.append(LouShanYunUtils.getCGXHReadStringByCode(hashMap.get(MapParams.传感类型)));
        sb.append(";倍率:");
        sb.append(LouShanYunUtils.getBLReadStringByCode(hashMap.get(MapParams.倍率)));
        sb.append(";初始脉冲数:");
        sb.append("5");
        sb.append(";最终脉冲数:");
        int finallynum = Integer.parseInt(hashMap.get(MapParams.脉冲数));
        if (finallynum - 5 < 2) {
            sendMessageToast("请吹两个脉冲数");
            return;
        }
        sb.append(hashMap.get(MapParams.脉冲数));
        //    sb.append("7");
        getZTReadStringByCode(hashMap.get(MapParams.状态));

        sb.append(";模组参数配置读取:正常");
        sb.append(";计数状态:");
        sb.append(LouShanYunUtils.getZTReadStringByCode(hashMap.get(MapParams.状态)));
        sb.append(";流向状态:");
        sb.append(hashMap.get(MapParams.表流向状态).equals("0") ? "正流" : "倒流");
        sb.append(";电池状态:");
        sb.append(hashMap.get(MapParams.表电池状态).equals("0") ? "正常" : "欠压");

        CheckUpData checkUpData = new CheckUpData();
        checkUpData.setOreder(dataBean.getOrderNumber());
        if (XHStringUtil.isEmpty(textNum.getText().toString(), false)) {
            sendMessageToast("请输入表身号");
            return;
        }
        checkUpData.setBodynum(textNum.getText().toString());
        checkUpData.setmLoginFactoryNum(LoginParamManager.getInstance().getLoginInfo().getData().getMloginFactoryNum());
        checkUpData.setModuleType(3);
        checkUpData.setInspectionField(sb.toString());
        checkUpData.setChipNum(hashMap.get(MapParams.设备ID));
        XLog.i("LSYsb====" + new Gson().toJson(checkUpData));
        pushEvent(ChipCode.MChipGetUpdata, new Gson().toJson(checkUpData));
    }

    public String getZTReadStringByCode(String tmpString) {
        StringBuffer result = new StringBuffer();
        result.append("正常，");
        try {
            int tem = (Integer.valueOf(tmpString)) & 0xff;
            byte b = (byte) tem;
            String binary = ByteConvertUtils.conver2BinaryStr(b);
            if (binary.charAt(4) == '1') {
                if (result.toString().contains("正常，")) {
                    result.delete(0, result.length());
                }
                result.append("欠压，");

            }
            hashMap.put(MapParams.表电池状态, String.valueOf(binary.charAt(4)));
            if (binary.charAt(2) == '1') {
                if (result.toString().contains("正常，")) {
                    result.delete(0, result.length());
                }
                result.append("倒流，");
            }
            hashMap.put(MapParams.表流向状态, String.valueOf(binary.charAt(2)));
            if (binary.charAt(7) == '1') {
                if (result.toString().contains("正常，")) {
                    result.delete(0, result.length());
                }
                result.append("强磁，");
            }
            if (binary.charAt(6) == '1') {
                if (result.toString().contains("正常，")) {
                    result.delete(0, result.length());
                }
                result.append("拆卸，");
            }

        } catch (Exception e) {
            result.append("，");
        }
        return result.toString().substring(0, result.length() - 1);
    }


}
