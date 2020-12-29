package wu.loushanyun.com.module_initthree.v.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.dayang.device.DYBox;
import com.dayang.device.DYDirectMeter;
import com.dayang.device.DYWirelessMeter;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundTextView;
import com.lidroid.xutils.util.LogUtils;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.xgg.blesdk.Box;
import com.xgg.blesdk.Meter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.libraryfive.Utils;
import wu.loushanyun.com.module_initthree.R;
import wu.loushanyun.com.module_initthree.p.adapter.BleDeviceListAdapter;

@Route(path = K.MCchushihuaResultActivity)
public class MCchushihuaResultActivity extends BaseNoPresenterActivity implements OnClickListener {
    DYBox box;
    Meter meter;
    ListView blue_listView;
    JSONArray m_meters;
    private int SystemVersion;
    BluetoothAdapter mBluetoothAdapter;
    private LeScanCallback mLeScanCallback;
    BleDeviceListAdapter mBleDeviceListAdapter;
    private AlertDialog mAlertDialog = null;
    LayoutInflater inflater;
    SharedPreferences sp;
    private final static int SCANNIN_GREQUEST_CODE = 1;

    private TextView textTop;
    private TextView blueState;
    private TextView blueState1;
    private ImageButton Scanconn;
    private ImageButton handConn;
    private ImageButton btnDisconn;
    private TextView roundYanbiaoBiaohao;
    private TextView roundYanbiaoLeixing;
    private TextView roundYanbiaoId;
    private TextView roundYanbiaoBeilv;
    private TextView roundYanbiaoZhengmaichong;
    private TextView roundYanbiaoFanmaichong;
    private TextView roundYanbiaoZhuangtai;
    private TextView roundYanbiaoDianya;
    private RoundTextView roundYanbiao;
    private LinearLayout linearShezhi;
    private LinearLayout linearBiaohao;
    private EditText edtMeternum;
    private LinearLayout linearBeilv;
    private Spinner spinnerRatioInit;
    private LinearLayout linearMaichong;
    private TextView pulse;
    private EditText editChushimaichong;
    private LinearLayout linearDishu;
    private TextView textChushizhi;
    private RoundTextView btnInitmeter;
    private RoundTextView btnSend2;
    private Button btnInitdataNew;

    private HashMap<String, String> map;
    private String loading = MCchushihuaResultActivity.this.getClass().getName();

    private boolean hasYanBiao = false;
    //发送设置命令重试的次数
    private int count_send;
    private boolean isYanBiao;//是否进来验表获取验表状态的
    private boolean deviceGenghuanZhuangtai;//验表失败，返回true
    private int biaoHao;//验表失败，返回true
    private String resultBiaoHao;//新设备的表号
    private String resultID;//新设备的id

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        getAllData();
        ba.mActivityLayoutId = R.layout.m_init_chushihua_result;
        if (isYanBiao) {
            ba.mTitleText = biaoHao + "号验表";
        } else {
            ba.mTitleText = "新单元初始化成"+biaoHao+"号表";
        }
        ba.mStatusBackgroundColorId = R.color.l_five_Q;
    }

    private void getAllData() {
        isYanBiao = getIntent().getBooleanExtra("isYanBiao", false);
        biaoHao = getIntent().getIntExtra("biaoHao", 0);
    }

    @Override
    protected void initView() {
        //获取系统版本
        SystemVersion = android.os.Build.VERSION.SDK_INT;
        XLog.i("版本号="+SystemVersion);

        textTop = (TextView) findViewById(R.id.text_top);
        blueState = (TextView) findViewById(R.id.blue_state);
        blueState1 = (TextView) findViewById(R.id.blue_state1);
        Scanconn = (ImageButton) findViewById(R.id.Scanconn);
        handConn = (ImageButton) findViewById(R.id.hand_conn);
        btnDisconn = (ImageButton) findViewById(R.id.btn_disconn);
        roundYanbiaoBiaohao = (TextView) findViewById(R.id.round_yanbiao_biaohao);
        roundYanbiaoLeixing = (TextView) findViewById(R.id.round_yanbiao_leixing);
        roundYanbiaoId = (TextView) findViewById(R.id.round_yanbiao_id);
        roundYanbiaoBeilv = (TextView) findViewById(R.id.round_yanbiao_beilv);
        roundYanbiaoZhengmaichong = (TextView) findViewById(R.id.round_yanbiao_zhengmaichong);
        roundYanbiaoFanmaichong = (TextView) findViewById(R.id.round_yanbiao_fanmaichong);
        roundYanbiaoZhuangtai = (TextView) findViewById(R.id.round_yanbiao_zhuangtai);
        roundYanbiaoDianya = (TextView) findViewById(R.id.round_yanbiao_dianya);
        roundYanbiao = (RoundTextView) findViewById(R.id.round_yanbiao);
        linearShezhi = (LinearLayout) findViewById(R.id.linear_shezhi);
        linearBiaohao = (LinearLayout) findViewById(R.id.linear_biaohao);
        edtMeternum = (EditText) findViewById(R.id.edt_meternum);
        linearBeilv = (LinearLayout) findViewById(R.id.linear_beilv);
        spinnerRatioInit = (Spinner) findViewById(R.id.spinner_ratio_init);
        linearMaichong = (LinearLayout) findViewById(R.id.linear_maichong);
        pulse = (TextView) findViewById(R.id.pulse);
        editChushimaichong = (EditText) findViewById(R.id.edit_chushimaichong);
        linearDishu = (LinearLayout) findViewById(R.id.linear_dishu);
        textChushizhi = (TextView) findViewById(R.id.text_chushizhi);
        btnInitmeter = (RoundTextView) findViewById(R.id.btn_initmeter);
        btnSend2 = (RoundTextView) findViewById(R.id.btn_send2);
        btnInitdataNew = (Button) findViewById(R.id.btn_initdata_new);


        if (isYanBiao) {
            linearShezhi.setVisibility(View.GONE);
            textTop.setText("请连接" + biaoHao + "号表单元进行验表测试!!!");
        } else {
            linearShezhi.setVisibility(View.VISIBLE);
            textTop.setText("请连接新表单元进行验表并初始化!!!");
        }

        editChushimaichong.addTextChangedListener(new TextWatcher() {
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
//                        double result1 = a1.multiply(b1).doubleValue();// 相乘结果
                        textChushizhi.setText(a1.multiply(b1).stripTrailingZeros().toPlainString() + "m³");
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
        pulse = (TextView) findViewById(R.id.pulse);
        btnInitmeter.setOnClickListener(this);
        btnInitdataNew.setOnClickListener(this);
        btnDisconn.setOnClickListener(this);
        btnDisconn.setEnabled(false);
        Scanconn.setOnClickListener(this);
        btnSend2.setOnClickListener(this);
        handConn.setOnClickListener(this);
        map = new HashMap<>();
        spinnerRatioInit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String m = (String) spinnerRatioInit.getSelectedItem();
                pulse.setText(LouShanYunUtils.getPulseConstant(m));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerRatioInit.setSelection(1);
        roundYanbiao.setOnClickListener(view -> {
            if (blueState1.getText().toString().equals("未连接")) {
                sendMessageToast("蓝牙未连接");
                return;
            }
            LoadingDialogUtil.showByEvent("正在验表", loading);
            box.getSingleData(5, MCchushihuaResultActivity.this, DataParser.CMD_CHECK, new Box.OnGetSingleDataListener() {
                @Override
                public void onCommSuccess(byte[] result) {
                    hasYanBiao = true;
                    Log.i("yunanhao", "接收：" + DataParser.byteToString(result));
                    LoadingDialogUtil.dismiss(loadingProgress);
                    try {
                        map = DataParser.check(map, result);
                        Log.i("yunanhao", "解析map：" + map.toString());
                        setData();
                        if (isYanBiao) {
                            ToastUtils.showShort("验表成功，该设备不能更换");
                            deviceGenghuanZhuangtai = false;
                        } else {
                            sendMessageToast("验表成功");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        if (isYanBiao) {
                            ToastUtils.showShort("验表失败，该设备可以更换");
                            deviceGenghuanZhuangtai = true;
                        } else {
                            sendMessageToast("验表失败");
                        }
                    }
                }

                @Override
                public void onCommTimeOut() {
                    LoadingDialogUtil.dismiss(loadingProgress);
                    Toast.makeText(MCchushihuaResultActivity.this, "连接超时", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCommFail(int rtcode) {
                    LoadingDialogUtil.dismiss(loadingProgress);
                    Toast.makeText(MCchushihuaResultActivity.this, "连接异常,错误码" + rtcode, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCommStop() {
                    LoadingDialogUtil.dismiss(loadingProgress);
                }
            });
        });
        blueState.setTextColor(Color.parseColor("#f17e90"));
        blueState1.setTextColor(Color.parseColor("#f17e90"));
        box = new DYBox();
        box.setHasDialog(false);
        EnableUI(false);
        blueState.setText("未连接");
        blueState1.setText("未连接");
    }


    private void setData() {
        String biaohao = map.get(MapParams.表号);
        this.resultBiaoHao = biaohao;
        String leiixng = map.get(MapParams.表类型);
        String id = map.get(MapParams.设备ID);
        this.resultID = id;
        String beilv = map.get(MapParams.倍率);
        String zhengmaichong = map.get(MapParams.正脉冲数);
        String fanmaichong = map.get(MapParams.反脉冲数);
        String zhuangtai = map.get(MapParams.状态);
        String dianya = map.get(MapParams.电压);
        if (XHStringUtil.isEmpty(biaohao, false)) {
            biaohao = "";
        }
        if (XHStringUtil.isEmpty(leiixng, false)) {
            leiixng = "";
        }
        if (XHStringUtil.isEmpty(id, false)) {
            id = "";
        }
        if (XHStringUtil.isEmpty(beilv, false)) {
            beilv = "";
        }
        if (XHStringUtil.isEmpty(zhengmaichong, false)) {
            zhengmaichong = "";
        }
        if (XHStringUtil.isEmpty(fanmaichong, false)) {
            fanmaichong = "";
        }
        if (XHStringUtil.isEmpty(zhuangtai, false)) {
            zhuangtai = "";
        } else {
            zhuangtai = LouShanYunUtils.getZTReadStringByCode(map.get(MapParams.状态));
        }
        if (XHStringUtil.isEmpty(dianya, false)) {
            dianya = "";
        }
        if (leiixng.equals("1")) {
            roundYanbiaoLeixing.setText("无线远程");
        } else if (leiixng.equals("2")) {
            roundYanbiaoLeixing.setText("光电直读");
        } else if (leiixng.equals("3")) {
            roundYanbiaoLeixing.setText("总线采集");
        }
        roundYanbiaoBiaohao.setText(biaohao);
        roundYanbiaoId.setText(id);
        spinnerRatioInit.setSelection(getSelectionPosition(beilv));
        roundYanbiaoBeilv.setText(beilv);
        roundYanbiaoZhengmaichong.setText(zhengmaichong);
        roundYanbiaoFanmaichong.setText(fanmaichong);
        roundYanbiaoZhuangtai.setText(zhuangtai);
        roundYanbiaoDianya.setText(dianya);
    }

    /**
     * <item>0.0001</item>
     * <item>0.001</item>
     * <item>0.01</item>
     * <item>0.1</item>
     * <item>1</item>
     * <item>10</item>
     * <item>100</item>
     * <item>1000</item>
     *
     * @return
     */
    private int getSelectionPosition(String beilv) {
        int position = 0;
        if (beilv.equals("0.0001")) {
            position = 0;
        } else if (beilv.equals("0.001")) {
            position = 1;
        } else if (beilv.equals("0.01")) {
            position = 2;
        } else if (beilv.equals("0.1")) {
            position = 3;
        } else if (beilv.equals("1")) {
            position = 4;
        } else if (beilv.equals("10")) {
            position = 5;
        } else if (beilv.equals("100")) {
            position = 6;
        } else if (beilv.equals("1000")) {
            position = 7;
        }
        return position;
    }

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }


    @Override
    public void onPause() {
        super.onPause();
        if (btnDisconn.isEnabled()) {
            box.Disconnect();
            EnableUI(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        box.Disconnect();
    }

    @Override
    public void onClick(View arg0) {
        if (arg0 == btnDisconn) {
            box.Disconnect();
            EnableUI(false);
            blueState.setText("未连接");
            blueState1.setText("未连接");
            blueState.setTextColor(Color.parseColor("#f17e90"));
            blueState1.setTextColor(Color.parseColor("#f17e90"));
        } else if (arg0 == Scanconn) {//蓝牙扫描
            handler.sendEmptyMessage(0);
        } else if (arg0 == handConn) {
//            Intent intent = new Intent();
//            intent.setClass(MCchushihuaResultActivity.this, MipcaActivityCapture.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
        } else if (arg0 == btnInitmeter) {
            if (blueState1.getText().toString().equals("未连接")) {
                sendMessageToast("蓝牙未连接");
                return;
            }
            if (!hasYanBiao) {
                sendMessageToast("请先验表");
                return;
            }
            String stringBiaohao = edtMeternum.getText().toString();
            if (XHStringUtil.isEmpty(stringBiaohao, false)) {
                sendMessageToast("请设置表号");
                return;
            }
            String stringMaiChong = editChushimaichong.getText().toString();
            if (XHStringUtil.isEmpty(stringMaiChong, false)) {
                sendMessageToast("请设置脉冲底数");
                return;
            }
            map.put(MapParams.脉冲底数, stringMaiChong);
            String stringBiaoBeiLv = (String) spinnerRatioInit.getSelectedItem();
            map.put(MapParams.表号, stringBiaohao);
            map.put(MapParams.倍率, stringBiaoBeiLv);
            byte[] input = DataParser.getInitCMD(map);
            count_send = 0;
            sendSetOrder(input);

        } else if (arg0 == btnInitdataNew) {
            if (hasYanBiao) {
                box.Disconnect();
                Intent result = new Intent();
                if (isYanBiao) {
                    result.putExtra("deviceGenghuanZhuangtai", deviceGenghuanZhuangtai);
                    setResult(RESULT_OK, result);
                    finish();
                } else {
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
                }

            } else {
                ToastUtils.showLong("请验表之后再返回");
            }

        } else if (arg0 == btnSend2) {
            String bh = edtMeternum.getText().toString().trim();
            if (XHStringUtil.isEmpty(bh, false)) {
                Toast.makeText(this, "没有填写表号", Toast.LENGTH_LONG).show();
                return;
            }
            meter = new DYDirectMeter();
            LoadingDialogUtil.showByEvent("正在读表", loading);
            if (SystemVersion >= 24) {
                box.getSingleData(10, this, meter.getQueryBytes(bh), new Box.OnGetSingleDataListener() {
                    @Override
                    public void onCommSuccess(byte result[]) {
                        LoadingDialogUtil.dismiss(loadingProgress);
                        JSONObject json = meter.getQueryObject(result);
                        if (json != null) {
                            try {
                                String rt = json.getString("rtcode");
                                if (rt.equals("ok")) {
                                    int bh = json.getInt("表号");
                                    if (bh < 0) {
                                        bh = 256 + bh;
                                    }
                                    long id = json.getLong("ID");
                                    int zmaichong = json.getInt("正脉冲数");
                                    String beil = json.getString("倍率");
                                    BigDecimal a1 = new BigDecimal(zmaichong);
                                    BigDecimal b1 = new BigDecimal(beil);
                                    double dushu = a1.multiply(b1).doubleValue();
                                    String statue = json.getString("状态");
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(MCchushihuaResultActivity.this);
                                    dialog.setTitle("读取结果");
                                    dialog.setMessage("表号  :" + bh + "\n" + "ID  :" + id + "\n" + "正脉冲数  :" + dushu + "\n" + "倍率  :" + beil + "\n" + "状态  :" + statue + "\n");
                                    dialog.setNegativeButton("确定", null);
                                    dialog.create().show();
                                } else {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(MCchushihuaResultActivity.this);
                                    dialog.setTitle("读取结果");
                                    dialog.setMessage("读取失败！");
                                    dialog.setNegativeButton("确定", null);
                                    dialog.create().show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onCommTimeOut() {
                        LoadingDialogUtil.dismiss(loadingProgress);
                        Toast.makeText(MCchushihuaResultActivity.this, "连接超时", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCommFail(int rtcode) {
                        LoadingDialogUtil.dismiss(loadingProgress);
                        Toast.makeText(MCchushihuaResultActivity.this, "连接异常,错误码" + rtcode, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCommStop() {
                        LoadingDialogUtil.dismiss(loadingProgress);
                        // TODO Auto-generated method stub

                    }
                });
            } else {
                box.getSingleData1(10, this, meter.getQueryBytes(bh), new Box.OnGetSingleDataListener() {
                    @Override
                    public void onCommSuccess(byte result[]) {
                        LoadingDialogUtil.dismiss(loadingProgress);
                        JSONObject json = meter.getQueryObject(result);
                        if (json != null) {
                            try {
                                String rt = json.getString("rtcode");
                                if (rt.equals("ok")) {
                                    int bh = json.getInt("表号");
                                    if (bh < 0) {
                                        bh = 256 + bh;
                                    }
                                    long id = json.getLong("ID");
                                    int zmaichong = json.getInt("正脉冲数");
                                    String beil = json.getString("倍率");
                                    BigDecimal a1 = new BigDecimal(zmaichong);
                                    BigDecimal b1 = new BigDecimal(beil);
                                    double dushu = a1.multiply(b1).doubleValue();
                                    String statue = json.getString("状态");
                                    if (statue.equals("拆卸断线")) {
                                        statue = "正常1";
                                    }
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(MCchushihuaResultActivity.this);
                                    dialog.setTitle("读取结果");
                                    dialog.setMessage("表号  :" + bh + "\n" + "ID  :" + id + "\n" + "正脉冲数  :" + dushu + "\n" + "倍率  :" + beil + "\n" + "状态  :" + statue + "\n");
                                    dialog.setNegativeButton("确定", null);
                                    dialog.create().show();
                                } else {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(MCchushihuaResultActivity.this);
                                    dialog.setTitle("读取结果");
                                    dialog.setMessage("读取失败！");
                                    dialog.setNegativeButton("确定", null);
                                    dialog.create().show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onCommTimeOut() {
                        LoadingDialogUtil.dismiss(loadingProgress);
                        Toast.makeText(MCchushihuaResultActivity.this, "连接超时", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCommFail(int rtcode) {
                        LoadingDialogUtil.dismiss(loadingProgress);
                        Toast.makeText(MCchushihuaResultActivity.this, "连接异常,错误码" + rtcode, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCommStop() {
                        LoadingDialogUtil.dismiss(loadingProgress);
                        // TODO Auto-generated method stub

                    }
                });
            }
        }
    }

    private void sendSetOrder(byte[] input) {
        LoadingDialogUtil.showByEvent("正在设置", loading);
        box.getSingleData2(count_send == 0 ? 5 : 4, this, input, new Box.OnGetSingleDataListener() {
            @Override
            public void onCommSuccess(byte[] result) {
                Log.i("test", "onCommSuccess");
                Log.i("test", DataParser.byteToString(result));
                sendMessageToast("设置成功");
                LoadingDialogUtil.dismiss(loadingProgress);
            }

            @Override
            public void onCommTimeOut() {
                if (count_send < 4) {
                    count_send++;
                    LoadingDialogUtil.show(loadingProgress, "重试" + count_send + "次");
                    sendSetOrder(input);
                } else {
                    LoadingDialogUtil.dismiss(loadingProgress);
                    sendMessageToast("连接超时");
                }
            }

            @Override
            public void onCommFail(int rtcode) {
                LoadingDialogUtil.dismiss(loadingProgress);
            }

            @Override
            public void onCommStop() {
                LoadingDialogUtil.dismiss(loadingProgress);
            }
        });
    }

    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (m_meters == null)
                    return;
                final int index = msg.arg1;
                if (index >= m_meters.length())    //超出了
                    return;
                final Meter meter = new DYWirelessMeter();        //只有无线
                try {
                    final int uid = m_meters.getInt(index);

                    if (uid == -1) {
                        return;
                    }
                    box.getSingleData(5, MCchushihuaResultActivity.this, meter.getQueryBytes(uid + ""), new Box.OnGetSingleDataListener() {
                        @Override
                        public void onCommSuccess(byte result[]) {
                            LoadingDialogUtil.dismiss(loadingProgress);
                            JSONObject json = meter.getQueryObject(result);
                            if (json != null) {
                                AlertDialog.Builder dialog = new AlertDialog.Builder(MCchushihuaResultActivity.this);
                                dialog.setTitle("读取结果" + uid);
                                dialog.setMessage(json.toString());
                                dialog.setNegativeButton(uid + "", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        Message newmsg = handler.obtainMessage();
                                        newmsg.what = 1;
                                        newmsg.arg1 = index + 1;
                                        newmsg.sendToTarget();
                                    }
                                });
                                dialog.create().show();
                            }
                        }

                        @Override
                        public void onCommTimeOut() {
                            LoadingDialogUtil.dismiss(loadingProgress);
                            Toast.makeText(MCchushihuaResultActivity.this, "连接超时", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCommFail(int rtcode) {
                            LoadingDialogUtil.dismiss(loadingProgress);
                            Toast.makeText(MCchushihuaResultActivity.this, "连接异常,错误码" + rtcode, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCommStop() {
                            LoadingDialogUtil.dismiss(loadingProgress);
                            // TODO Auto-generated method stub

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (msg.what == 0) {
                setView_init();//初始化蓝牙listView
                getBleAdapter();
                getScanResualt();
                new Thread(new Runnable() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void run() {
                        mBluetoothAdapter.startLeScan(mLeScanCallback);
                    }
                }).start();

            }
        }
    };

    void EnableUI(boolean enable) {
        btnInitmeter.setEnabled(enable);
        btnDisconn.setEnabled(enable);
    }

    @SuppressLint("NewApi")
    private void getScanResualt() {//扫描结果回调
        mLeScanCallback = new LeScanCallback() {
            @Override
            public void onLeScan(final BluetoothDevice device, final int rssi,
                                 final byte[] scanRecord) {
                MCchushihuaResultActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        mBleDeviceListAdapter.addDevice(device, rssi,
                                Utils.bytesToHex(scanRecord));
                        mBleDeviceListAdapter.notifyDataSetChanged();
                        invalidateOptionsMenu();
                    }
                });
            }
        };
    }

    @SuppressLint("NewApi")
    private void getBleAdapter() {
        final BluetoothManager bluetoothManager = (BluetoothManager) this
                .getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
    }

    private void setView_init() {
        //初始化控件
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.m_init_blue_scan, null);
        mAlertDialog = new AlertDialog.Builder(MCchushihuaResultActivity.this)
                .setView(view)
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @SuppressWarnings("deprecation")
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mBluetoothAdapter.stopLeScan(mLeScanCallback);
                        mBleDeviceListAdapter.clear();
                        mBluetoothAdapter.cancelDiscovery();
                        dialog.dismiss();
                    }
                }).create();
        mAlertDialog.setTitle("可用设备蓝牙名称");
        mAlertDialog.setCanceledOnTouchOutside(false);
        mAlertDialog.show();
        blue_listView = (ListView) view.findViewById(R.id.new_devices);
        blue_listView.setEmptyView(view.findViewById(R.id.pb_empty));
        mBleDeviceListAdapter = new BleDeviceListAdapter(MCchushihuaResultActivity.this);
        blue_listView.setAdapter(mBleDeviceListAdapter);
        setListItemListener();

    }

    /**
     * 条目点击事件，点击一个item获取蓝牙名称连接
     */
    private void setListItemListener() {
        blue_listView.setOnItemClickListener(new OnItemClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
//				View view1=v.findViewById(R.id.tv_devicelist_name);
//				final String name= ((TextView) view1).getText().toString().trim();
                BluetoothDevice device = mBleDeviceListAdapter.getDevice(position);
                final String name = device.getName();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(MCchushihuaResultActivity.this, "未选中蓝牙设备,请选择有名字的蓝牙", Toast.LENGTH_SHORT).show();
                    return;
                }
                //获取SharedPreferences对象
                sp = getSharedPreferences("datasave", Activity.MODE_PRIVATE);
                // 获取Editor对象
                Editor editor = sp.edit();
                editor.putString("BlueName", name);
                editor.commit();
//				box.m_name = name;
                box.mDevice = device;
                LoadingDialogUtil.showByEvent("正在连接蓝牙", loading);
                mAlertDialog.dismiss();
                box.Connect(10, MCchushihuaResultActivity.this, new Box.OnConnectListener() {
                    @Override
                    public void onConnectSuccess() {
                        LoadingDialogUtil.dismiss(loadingProgress);
                        map.clear();
                        setData();
                        hasYanBiao = false;
                        sendMessageToast("连接成功");
                        EnableUI(true);
                        blueState.setText("已连接");
                        blueState1.setText(name);
                        blueState.setTextColor(Color.parseColor("#27f35a"));
                        blueState1.setTextColor(Color.parseColor("#27f35a"));
                        mAlertDialog.dismiss();
                    }

                    @Override
                    public void onConnectTimeOut() {
                        LoadingDialogUtil.dismiss(loadingProgress);
                        sendMessageToast("连接超时,重启手机蓝牙尝试重新连接");
                    }

                    @Override
                    public void onDisconnect() {
                        LoadingDialogUtil.dismiss(loadingProgress);
                        sendMessageToast("断开连接");
                        blueState.setText("未连接");
                        blueState1.setText("未连接");
                        blueState.setTextColor(Color.parseColor("#f17e90"));
                        blueState1.setTextColor(Color.parseColor("#f17e90"));
                    }

                    @Override
                    public void onConnectFail(int rtcode) {
                        LoadingDialogUtil.dismiss(loadingProgress);
                        sendMessageToast("连接异常");
                        if (rtcode == 4) {
                            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(enableBtIntent, 1);
                        }
                    }
                });
            }
        });


    }
}
