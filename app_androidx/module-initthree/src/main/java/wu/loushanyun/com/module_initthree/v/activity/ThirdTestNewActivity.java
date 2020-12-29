package wu.loushanyun.com.module_initthree.v.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.roundview.RoundTextView;
import com.lidroid.xutils.util.LogUtils;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.libraryfive.p.adapter.SNDeviceViewBinder;
import wu.loushanyun.com.module_initthree.R;


@Route(path = K.ThirdTestNewActivity)
public class ThirdTestNewActivity extends BaseSnBlueToothActivity {
    private ArrayList<SensoroDevice> deviceArrayList;

    private ScrollView scrollView;
    private TextView textTop;
    private TextView textDangqianSn;
    private RoundTextView bluetoothConn;
    private RoundTextView bluetoothDisconn;
    private LinearLayout layoutYanbiao;
    private RoundTextView roundYanbiao;
    private TextView roundYanbiaoBiaohao;
    private TextView roundYanbiaoLeixing;
    private TextView roundYanbiaoId;
    private TextView roundYanbiaoBeilv;
    private TextView roundYanbiaoZhengmaichong;
    private TextView roundYanbiaoFanmaichong;
    private TextView roundYanbiaoZhuangtai;
    private TextView roundYanbiaoDianya;
    private LinearLayout linearBiaohao;
    private EditText edtMeternum;
    private LinearLayout linearBeilv;
    private Spinner spinnerRatioInit;
    private LinearLayout linearMaichong;
    private TextView pulse;
    private EditText editChushimaichong;
    private RoundTextView btnInitmeter;
    private RoundTextView btnSend2;
    private RoundTextView roundRepaire;
    private RoundTextView roundReadAll;
    private TextView roundYanbiaoDushu;

    private SensoroDevice sensoroDeviceChoose;
    private SNDeviceViewBinder snDeviceViewBinder;
    private HashMap<String, String> hashMapProduct;
    private ST3ByteTool st3ByteTool;

    private boolean canSet = false;
    private boolean setSuccess = false;

    private int fromType;//0：更换表单元进来验表获取验表状态的,1:更换表单元进来初始化，2：初始化
    private boolean deviceGenghuanZhuangtai;//验表失败，返回true
    private int biaoHao;//验表失败，返回true
    private String resultBiaoHao;//新设备的表号
    private String resultID;//新设备的id

    @Override
    protected int onChildLayout() {
        return R.layout.m_chip_activity_thirdtestnew;
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        super.onInitAttribute(ba);
        getAllData();
        if (fromType == 0) {
            ba.mTitleText = biaoHao + "号验表";
        } else if (fromType == 1) {
            ba.mTitleText = "新单元初始化成" + biaoHao + "号表";
        } else {
            ba.mTitleText = "3号模组初始化（出厂配置抄表盒子）";
        }
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


        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        textTop = (TextView) findViewById(R.id.text_top);
        textDangqianSn = (TextView) findViewById(R.id.text_dangqian_sn);
        bluetoothConn = (RoundTextView) findViewById(R.id.bluetooth_conn);
        bluetoothDisconn = (RoundTextView) findViewById(R.id.bluetooth_disconn);
        layoutYanbiao = (LinearLayout) findViewById(R.id.layout_yanbiao);
        roundYanbiao = (RoundTextView) findViewById(R.id.round_yanbiao);
        roundYanbiaoBiaohao = (TextView) findViewById(R.id.round_yanbiao_biaohao);
        roundYanbiaoLeixing = (TextView) findViewById(R.id.round_yanbiao_leixing);
        roundYanbiaoId = (TextView) findViewById(R.id.round_yanbiao_id);
        roundYanbiaoBeilv = (TextView) findViewById(R.id.round_yanbiao_beilv);
        roundYanbiaoZhengmaichong = (TextView) findViewById(R.id.round_yanbiao_zhengmaichong);
        roundYanbiaoDushu = (TextView) findViewById(R.id.round_yanbiao_dushu);
        roundYanbiaoFanmaichong = (TextView) findViewById(R.id.round_yanbiao_fanmaichong);
        roundYanbiaoZhuangtai = (TextView) findViewById(R.id.round_yanbiao_zhuangtai);
        roundYanbiaoDianya = (TextView) findViewById(R.id.round_yanbiao_dianya);
        linearBiaohao = (LinearLayout) findViewById(R.id.linear_biaohao);
        edtMeternum = (EditText) findViewById(R.id.edt_meternum);
        linearBeilv = (LinearLayout) findViewById(R.id.linear_beilv);
        spinnerRatioInit = (Spinner) findViewById(R.id.spinner_ratio_init);
        linearMaichong = (LinearLayout) findViewById(R.id.linear_maichong);
        pulse = (TextView) findViewById(R.id.pulse);
        editChushimaichong = (EditText) findViewById(R.id.edit_chushimaichong);
        btnInitmeter = (RoundTextView) findViewById(R.id.btn_initmeter);
        btnSend2 = (RoundTextView) findViewById(R.id.btn_send2);
        roundRepaire = (RoundTextView) findViewById(R.id.roundRepaire);
        roundReadAll = (RoundTextView) findViewById(R.id.round_readAll);


        spinnerRatioInit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                BigDecimal decimal = new BigDecimal(Float.valueOf(spinnerRatioInit.getSelectedItem().toString()));
         //       new BigDecimal(1).divide(Float.valueOf(spinnerRatioInit.getSelectedItem().toString())

                pulse.setText( new BigDecimal(1).divide(decimal,3,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()+"个/m³");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (fromType == 0) {
            roundRepaire.setVisibility(View.VISIBLE);
            textTop.setVisibility(View.VISIBLE);
            textTop.setText("请连接" + biaoHao + "号表单元进行验表测试!!!");
        } else if (fromType == 1) {
            roundRepaire.setVisibility(View.VISIBLE);
            textTop.setVisibility(View.VISIBLE);
            textTop.setText("请连接新表单元进行验表并初始化!!!");
        } else if (fromType == 2) {
            roundRepaire.setVisibility(View.GONE);
            textTop.setVisibility(View.GONE);
        }
        hashMapProduct = new HashMap<>();
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

        roundReadAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build(K.ThirdReadActivity).withParcelable("SensoroDevice", sensoroDeviceChoose).navigation();
            }
        });

        st3ByteTool = new ST3ByteTool();
        initTestClick();

    }




    private void initTestClick() {
        roundYanbiao.setOnClickListener(this::OnBlueToothClick);
        btnInitmeter.setOnClickListener(this::OnBlueToothClick);
        btnSend2.setOnClickListener(this::OnBlueToothClick);
        roundRepaire.setOnClickListener(this::OnBlueToothClick);
        // roundReadAll.setOnClickListener(this::OnBlueToothClick);
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
        } else if (id == R.id.btn_send2) {
            if (XHStringUtil.isEmpty(edtMeternum.getText().toString(), false)) {
                sendMessageToast("请输入起表号");
                return;
            }
            snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3ReadMaiChong).getThirdReadBiaoBytes(
                    Integer.valueOf(edtMeternum.getText().toString())), "抄表");
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
                                setTextYanbiao(st3ByteTool.parseThirdReadYanBiaoBytes(result));
                                canSet = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                                canSet = false;
                            }
                            if (fromType == 0) {
                                ToastUtils.showShort("验表成功，该设备不能更换");
                                deviceGenghuanZhuangtai = false;
                            } else if (fromType == 1) {

                            } else if (fromType == 2) {
                                sendMessageToast("验表成功");
                            }
                        } else {
                            canSet = false;
                            setNullText();
                            if (fromType == 0) {
                                ToastUtils.showShort("验表失败，该设备可以更换");
                                deviceGenghuanZhuangtai = true;
                            } else if (fromType == 1) {

                            } else if (fromType == 2) {
                                sendMessageToast("验表失败");
                            }
                        }
                    } else if (st3ByteTool.getsT3Type().equals(ST3Params.sT3InitBiao)) {
                        if (result[5] == 4 && result[6] == (byte) 0xfc) {
                            sendMessageToast("设置成功");
                            canSet = false;
                            setNullText();
                            setSuccess = true;
//                                snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3ReadMaiChong).getThirdReadBiaoBytes(
//                                        Integer.valueOf(edtMeternum.getText().toString())), "抄表");
                        } else {
                            canSet = true;
                        }
                    } else if (st3ByteTool.getsT3Type().equals(ST3Params.sT3ReadMaiChong)) {
                        if (result[5] == (byte) 0x0e && result[6] == (byte) 0xfc) {
                            sendMessageToast("读取成功");
                            try {
                                setTextMaiChong(st3ByteTool.parseThirdReadBiaoBytes(result));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            sendMessageToast("读取失败");
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


    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(ThirdTestNewActivity.this);
                dialog.setTitle("读取结果");
                dialog.setMessage("表号  :" + hashMap.get(MapParams.表号) + "\n" + "ID  :" + hashMap.get(MapParams.设备ID) + "\n" + "正脉冲数  :" + hashMap.get(MapParams.脉冲数) + "\n" + "倍率  :" + LouShanYunUtils.getBLReadStringByCode(hashMap.get(MapParams.倍率)) + "\n" + "状态  :" + LouShanYunUtils.getZTReadStringByCode(hashMap.get(MapParams.状态)) + "\n");
                dialog.setNegativeButton("确定", null);
                dialog.create().show();

            }
        });
    }

    private void setTextYanbiao(HashMap<String, String> hashMap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                roundYanbiaoBiaohao.setText(hashMap.get(MapParams.表号));
                roundYanbiaoLeixing.setText("总线采集");
                roundYanbiaoId.setText(hashMap.get(MapParams.设备ID));
                roundYanbiaoBeilv.setText(LouShanYunUtils.getBLReadStringByCode(hashMap.get(MapParams.倍率)));
                roundYanbiaoZhengmaichong.setText(hashMap.get(MapParams.脉冲数));
                BigDecimal decimal = new BigDecimal(LouShanYunUtils.getBLReadStringByCode(Integer.parseInt(hashMap.get(MapParams.倍率))));
                roundYanbiaoDushu.setText( new BigDecimal(hashMap.get(MapParams.脉冲数)).multiply(decimal).stripTrailingZeros().toPlainString());
                roundYanbiaoZhuangtai.setText(LouShanYunUtils.getZTReadStringByCode(hashMap.get(MapParams.状态)));

            }
        });
    }

    private void setNullText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                roundYanbiaoBiaohao.setText("");
                roundYanbiaoLeixing.setText("");
                roundYanbiaoId.setText("");
                roundYanbiaoBeilv.setText("");
                roundYanbiaoZhengmaichong.setText("");
                roundYanbiaoDushu.setText("");
                roundYanbiaoZhuangtai.setText("");

            }
        });
    }

}
