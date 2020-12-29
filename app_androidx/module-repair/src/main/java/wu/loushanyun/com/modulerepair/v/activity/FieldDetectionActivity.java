package wu.loushanyun.com.modulerepair.v.activity;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.flyco.roundview.RoundTextView;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.init.LSY2InitTypeCode;
import com.wu.loushanyun.basemvp.v.activity.BaseBlueToothActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.views.MyRadioGroup;
import wu.loushanyun.com.libraryfive.m.RefreshMainRepair;
import wu.loushanyun.com.modulerepair.R;
import wu.loushanyun.com.modulerepair.m.RepairData;

@Route(path = K.FieldDetectionActivity)
public class FieldDetectionActivity extends BaseBlueToothActivity {
    private RoundTextView textChonglian;
    private RoundTextView tvInfoBase;
    private MyRadioGroup radioGroup;
    private RadioButton radioButtonDan;
    private RadioButton radioButtonDuo;
    private EditText editBiaohao;
    private ImageView powerState;
    private RoundTextView tvItem1;
    private ImageView status1;
    private TextView tvInfo;
    private RoundTextView roundDuqu;
    private RoundTextView roundDuquxinxi;
    private RoundTextView roundChushihua;
    private RepairData repairData;
    private boolean readOnce;

    private HashMap<String, String> hashMap;
    private boolean isJianCha;
    private double softVersion;
    private String realMeterReading;

    @Override
    protected void initView() {
        super.initView();
        repairData = getIntent().getParcelableExtra("repairData");
        realMeterReading = getIntent().getStringExtra("realMeterReading");
        isJianCha = getIntent().getBooleanExtra("isJianCha", false);

        textChonglian = (RoundTextView) findViewById(R.id.text_chonglian);
        tvInfoBase = (RoundTextView) findViewById(R.id.tv_info_base);
        radioGroup = (MyRadioGroup) findViewById(R.id.radio_group);
        radioButtonDan = (RadioButton) findViewById(R.id.radio_button_dan);
        radioButtonDuo = (RadioButton) findViewById(R.id.radio_button_duo);
        editBiaohao = (EditText) findViewById(R.id.edit_biaohao);
        powerState = (ImageView) findViewById(R.id.power_state);
        tvItem1 = (RoundTextView) findViewById(R.id.tv_item1);
        status1 = (ImageView) findViewById(R.id.status1);
        tvInfo = (TextView) findViewById(R.id.tv_info);
        roundDuqu = (RoundTextView) findViewById(R.id.round_duqu);
        roundDuquxinxi = (RoundTextView) findViewById(R.id.round_duquxinxi);
        roundChushihua = (RoundTextView) findViewById(R.id.round_chushihua);

        if (isJianCha) {
            roundChushihua.setVisibility(View.GONE);
        }
        if (repairData.getProductForm().equals("远传表号接入")) {
            radioGroup.setVisibility(View.VISIBLE);
        } else if (repairData.getProductForm().equals("远传物联网端")) {
            radioGroup.setVisibility(View.GONE);
        }
        hashMap = new HashMap<>();
        setData(sensoroDevice.getSerialNumber() + "未连接");
        connectBlueTooth();
        roundChushihua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String status = tvInfo.getText().toString();
                if (repairData.getProductForm().equals("远传表号接入")) {
                    if ("设备连接正常！".equals(status)) {
                        ARouter.getInstance().build(K.YuanChenBiaoHaoActivity).withString("oldSn", repairData.getSn()).withParcelable("sensoroDevice", sensoroDevice).navigation();
                    } else {
                        sendMessageToast("新设备连接异常请检查后重新读取状态信息");
                    }
                } else if (repairData.getProductForm().equals("远传物联网端")) {
                    if ("设备连接正常！".equals(status)) {
                        ARouter.getInstance().build(K.LSY2InitActivity)
                                .withString("oldSn", repairData.getSn())
                                .withParcelable("sensoroDevice", sensoroDevice)
                                .withString("realMeterReading", realMeterReading)
                                .withInt("jumpType", LSY2InitTypeCode.TypeFromReplace)
                                .navigation();
                    } else {
                        sendMessageToast("新设备连接异常请检查后重新读取状态信息");
                    }
                }
            }
        });
        roundDuqu.setOnClickListener(v -> {
            read();
        });
        roundDuquxinxi.setOnClickListener(v -> {
            if (repairData.getProductForm().equals("远传表号接入")) {
                ARouter.getInstance().build(K.YuanChenBiaoHaoActivity).withParcelable("sensoroDevice", sensoroDevice).withBoolean("isOnlyRead", true).navigation();
            } else if (repairData.getProductForm().equals("远传物联网端")) {
                ARouter.getInstance().build(K.LSY2InitActivity)
                        .withParcelable("sensoroDevice", sensoroDevice)
                        .withInt("jumpType", LSY2InitTypeCode.TypeFromRead)
                        .navigation();
            }
        });
        textChonglian.setOnClickListener(v -> {
            connectBlueTooth(sensoroDevice);
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshMainRepair refreshMainRepair) {
        finish();
    }

    private void setData(String lanya) {
        StringBuilder sb = new StringBuilder();
        sb.append("蓝牙：" + lanya);
        sb.append("\n原物联SN：");
        sb.append(repairData.getSn());
        sb.append("\n现物联SN：");
        if (!XHStringUtil.isEmpty(repairData.getNewSn(), false)) {
            sb.append(repairData.getNewSn());
        } else {
            sb.append(repairData.getSn());
        }
        sb.append("\n产品形式：");
        sb.append(repairData.getProductForm());
        SpannableString ss = new SpannableString(sb);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(0xff941e23);
        ss.setSpan(colorSpan, 3, sensoroDevice.getSerialNumber().length() + 3, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvInfoBase.setText(ss);
            }
        });
    }


    @Override
    protected void onChildNotify(byte[] results) {
        byte code = (byte) (results[2] ^ ((byte) 0x80));
        switch (code) {
            case 0x11:
                try {
                    HashMap<String, String> map = DataParser.getInformationAll(results);
                    int typeMoZu = DataParser.getModuleType(results);
                    if (typeMoZu == 1) {
                        if("远传表号接入".equals(repairData.getProductForm())){
                            softVersion = Double.valueOf(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(map.get(MapParams.软件版本))));
                            read();
                        }else {
                            sendMessageToast("识别不是" + repairData.getProductForm());
                        }
                    } else if (typeMoZu == 2) {
                        if("远传物联网端".equals(repairData.getProductForm())){
                            read();
                        }else {
                            sendMessageToast("识别不是" + repairData.getProductForm());
                        }
                        read();
                    } else {
                        sendMessageToast("识别不是" + repairData.getProductForm());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 0x22:
                if (results[3] == 0) {
                    hashMap.putAll(DataParser.getMoudleNo2(results));
                    String dianChi = hashMap.get(MapParams.表电池状态);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if ("0".equals(dianChi)) {
                                powerState.setImageDrawable(getResources().getDrawable(R.drawable.m_repair_normal));
                                powerState.setVisibility(View.VISIBLE);
                            } else if ("1".equals(dianChi)) {
                                powerState.setImageDrawable(getResources().getDrawable(R.drawable.m_repair_undervoltage));
                                powerState.setVisibility(View.VISIBLE);
                            }
                            status1.setImageDrawable(getResources().getDrawable(R.drawable.m_repair_gou));
                            tvInfo.setText("设备连接正常！");
                            tvInfo.setVisibility(View.VISIBLE);
                        }
                    });

                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            powerState.setImageDrawable(getResources().getDrawable(R.drawable.m_repair_undervoltage));
                            powerState.setVisibility(View.VISIBLE);
                            status1.setImageDrawable(getResources().getDrawable(R.drawable.m_repair_abnormal));
                            tvInfo.setText("设备连接异常！");
                            tvInfo.setVisibility(View.VISIBLE);
                        }
                    });
                }
                break;
            case 0x32:
                if (results[3] == 0) {
                    HashMap<String, String> hashMap = DataParser.getDanBiaoDuQuXinXi(results, softVersion);
                    if (hashMap != null) {
                        String biaoID = hashMap.get(MapParams.设备ID);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                powerState.setImageDrawable(getResources().getDrawable(R.drawable.m_repair_normal));
                                powerState.setVisibility(View.VISIBLE);
                                status1.setImageDrawable(getResources().getDrawable(R.drawable.m_repair_gou));
                                tvInfo.setText("设备连接正常！");
                                tvInfo.setVisibility(View.VISIBLE);
                                if (softVersion >= 1.05) {
                                    write(DataParser.closeJiZhongQi(), "正在关闭集中器");
                                }
                            }
                        });
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            powerState.setImageDrawable(getResources().getDrawable(R.drawable.m_repair_undervoltage));
                            powerState.setVisibility(View.VISIBLE);
                            status1.setImageDrawable(getResources().getDrawable(R.drawable.m_repair_abnormal));
                            tvInfo.setText("设备连接异常！");
                            tvInfo.setVisibility(View.VISIBLE);
                        }
                    });
                    if (readOnce) {
                        readDanBiao(1);
                        readOnce = false;
                    } else {
                        sendMessageToast("读取失败，请连检查是否连接了表");
                    }
                }
                break;
            case 0x31:
                if (results[3] == 0) {
                    int start = Integer.parseInt(DataParser.getDiBanBiaoJiInfo(results).get(MapParams.总线起止表号_起));
                    readDanBiao(start);
                } else {
                    sendMessageToast("读取表号失败");
                }
                break;
            case 0x41:
                if (results[3] == 0) {
                    sendMessageToast("打开集中器成功");
                    if (radioButtonDan.isChecked()) {
                        readDanBiao(1);
                    }
                    if (radioButtonDuo.isChecked()) {
                        String num = editBiaohao.getText().toString().trim();
                        readDanBiao(Integer.valueOf(num));
                    }
                } else {
                    sendMessageToast("打开集中器失败");
                }
                break;
            case 0x42:
                if (results[3] == 0) {
                    sendMessageToast("关闭集中器成功");
                } else {
                    sendMessageToast("关闭集中器失败");
                }
                break;
        }
    }

    protected void readDanBiao(int biaoHao) {
        byte[] d = DataParser.getDanBiaoDuQuXinXiCMD(biaoHao);
        if (sensoroDeviceSession != null) {
            write(d, "正在验表，请稍后");
        }
    }

    protected void readBiaoHao() {
        readOnce = true;
        write(new byte[]{0x68, 0x01, 0x31, 0x32, 0x16}, "正在读取表号");
    }

    @Override
    protected void onChildWriteSuccess() {

    }

    @Override
    protected void onChildConnectFailed(int i) {

    }

    @Override
    protected void onChildConnectSuccess() {
        setData(sensoroDevice.getSerialNumber() + "已连接");
        write(DataParser.CMD_INFO_ALL, "识别模块中...", true, 6000);
    }

    private void read() {
        if (repairData.getProductForm().equals("远传表号接入")) {
            if (softVersion >= 1.05) {
                write(DataParser.openJiZhongQi(), "正在打开集中器");
            } else {
                if (radioButtonDan.isChecked()) {
                    readDanBiao(1);
                }
                if (radioButtonDuo.isChecked()) {
                    String num = editBiaohao.getText().toString().trim();
                    readDanBiao(Integer.valueOf(num));
                }
            }
        } else if (repairData.getProductForm().equals("远传物联网端")) {
            write(DataParser.CMD_METER_INFO, "正在读取状态");
        }
    }

    @Override
    protected void onChildWriteFailure(int i) {

    }

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_repair_activity_field_detection;
        ba.mTitleText = "检测";
    }

}
