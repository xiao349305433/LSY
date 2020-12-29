package wu.loushanyun.com.module_initthree.v.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dayang.device.DYBox;
import com.dayang.device.DYDirectMeter;
import com.dayang.device.DYMeter;
import com.elvishew.xlog.XLog;
import com.xgg.blesdk.Box.OnConnectListener;
import com.xgg.blesdk.Box.OnGetSingleDataListener;
import com.xgg.blesdk.Meter;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.KeyboardUtils;
import met.hx.com.librarybase.some_utils.ToastManager;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.module_initthree.R;

public class ReadActivity extends BaseNoPresenterActivity implements OnClickListener {
    Button btn_send1, btn_resend, btn_send2;
    EditText edt_sn, edt_endsn, edt_endsn2;
    TextView accept, dis_accept, txt_disaccept, blue_state;
    private List<String> str;
    private List<String> fileNname;
    private int size;
    private static int tableLength;
    DYBox box;
    Meter meter;
    JSONObject json;
    private long sn1;
    private int bh, a;
    private String zhuantai;
    private ListView listView;
    private TextView btn_conn_new;
    final int MSG_READSUCCESS = 1;
    final int MSG_READTIMEOUT = 2;
    final int MSG_READFAIL = 3;
    final int MSG_NOSTATE = 4;
    final int MSG_READSUCCESS1 = 5;
    final int MSG_READTIMEOUT1 = 6;
    final int MSG_READFAIL1 = 7;
    final int MSG_NOSTATE1 = 8;
    SimpleAdapter adapter;
    private List<String> tablelist = new ArrayList<String>();
    private List<String> aList = new ArrayList<String>();
    List<Map<String, Object>> data1;
    private SharedPreferences sp;
    String result;
    private View view;
    Toast toast = null;
    private String biaohao;
    private String sn;
    private String dushu;
    private String statue;
    private int SystemVersion;
    private BluetoothDevice mDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取系统版本
        SystemVersion = android.os.Build.VERSION.SDK_INT;
        XLog.i("版本号="+SystemVersion);
        mDevice = getIntent().getExtras().getParcelable("mDevice");
        try {
            Thread.sleep(500);
            setView();//初始化按钮
            //解析手机端存储的BlueName
            sp = this.getSharedPreferences("datasave", MODE_PRIVATE);
            result = sp.getString("BlueName", "");
            if (result != null && result.length() > 0) {
                Connection();
            } else {
                Toast.makeText(this, "请先进行扫码连接！", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_init_dubiao_new;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    //蓝牙连接
    private void Connection() {
        if (mDevice != null) {
            box.mDevice = mDevice;
            box.m_name = mDevice.getName();
            LoadingDialogUtil.showByEvent("正在连接蓝牙",this.getClass().getName());
            box.Connect_gd(10, this, new OnConnectListener() {
                @Override
                public void onConnectSuccess() {
                    LoadingDialogUtil.dismiss(loadingProgress);
                    Toast.makeText(ReadActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                    blue_state.setText("已连接");
                    blue_state.setTextColor(Color.parseColor("#27f35a"));
                }

                @Override
                public void onConnectTimeOut() {
                    LoadingDialogUtil.dismiss(loadingProgress);
                    toast = Toast.makeText(getApplicationContext(),
                            "连接超时,请检查盒子是否正常开启！", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

                @Override
                public void onDisconnect() {
                    LoadingDialogUtil.dismiss(loadingProgress);
                    Toast.makeText(ReadActivity.this, "断开连接", Toast.LENGTH_SHORT).show();
                    blue_state.setText("未连接");
                    btn_conn_new.setVisibility(View.VISIBLE);
                    blue_state.setTextColor(Color.parseColor("#f17e90"));
                    btn_send1.setEnabled(false);
                }

                @Override
                public void onConnectFail(int rtcode) {
                    Toast.makeText(ReadActivity.this, "连接异常,错误码" + rtcode, Toast.LENGTH_SHORT).show();
                    if (rtcode == 4) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, 1);
                    }
                }
            });
        }
    }

    private void setView() {
        btn_send1 = (Button) findViewById(R.id.btn_send1);
        btn_send1.setOnClickListener(this);
        btn_send2 = (Button) findViewById(R.id.btn_send2);
        btn_send2.setOnClickListener(this);
        btn_resend = (Button) findViewById(R.id.btn_resend);
        btn_resend.setOnClickListener(this);
        edt_sn = (EditText) findViewById(R.id.edt_sn);
        edt_endsn = (EditText) findViewById(R.id.edt_endsn);
        edt_endsn2 = (EditText) findViewById(R.id.edt_endsn2);
        accept = (TextView) findViewById(R.id.accept);
        dis_accept = (TextView) findViewById(R.id.dis_accept);
        txt_disaccept = (TextView) findViewById(R.id.txt_disaccept);
        txt_disaccept.setOnClickListener(this);
        blue_state = (TextView) findViewById(R.id.blue_state);
        blue_state.setText("未连接");
        blue_state.setTextColor(Color.parseColor("#f17e90"));
        btn_conn_new = (TextView) findViewById(R.id.btn_conn_new);
        btn_conn_new.setOnClickListener(this);
        str = new ArrayList<String>();
        fileNname = new ArrayList<String>();
        listView = (ListView) findViewById(R.id.listView);
        data1 = new ArrayList<Map<String, Object>>();
        box = new DYBox();
        box.setHasDialog(false);
        CharSequence text = "0块";
        accept.setText(text);
        dis_accept.setText(text);
    }

    @Override
    public void onPause() {
        super.onPause();
        box.Disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        box.Disconnect();
    }

    @Override
    public void onClick(View v) {
        KeyboardUtils.hideSoftInput(this);
        if (v == btn_send1) {
            LinearLayout ll = (LinearLayout) findViewById(R.id.listData);
            ll.setVisibility(View.VISIBLE);
            if (XHStringUtil.isEmpty(edt_sn.getText().toString(), false)) {
                ToastManager.getInstance(this).show("请输入表号");
                return;
            }
            if(blue_state.getText().toString().equals("未连接")){
                ToastManager.getInstance(this).show("请连接蓝牙");
                return;
            }
            str.clear();
            for (int i = Integer.parseInt(edt_sn.getText().toString()); i <= Integer.parseInt(edt_endsn.getText().toString()); i++) {
                str.add(String.valueOf(i));
            }
            CharSequence text = "0块";
            size = str.size();
            accept.setText(text);
            dis_accept.setText(size + "块");
            data1.clear();
            if (size >= 1) {
                tableLength = size;
                if (SystemVersion >= 24) {//判断系统信息，来选择何种方式来抄表
                    //发送表号进行抄表
                    ReadTable(str.get(0));
                } else {
                    ReadTable1(str.get(0));
                }
            } else {
                Toast.makeText(ReadActivity.this, "请重新选择区域！", Toast.LENGTH_SHORT).show();
            }

        } else if (v == btn_resend) {
            if(blue_state.getText().toString().equals("未连接")){
                ToastManager.getInstance(this).show("请连接蓝牙");
                return;
            }
            str.removeAll(tablelist);
            if (str.size() >= 1) {
                tableLength = str.size();
                j = 1;
                btn_send1.setEnabled(false);
                if (SystemVersion >= 24) {//判断系统信息，来选择何种方式来抄表
                    //发送表号进行抄表
                    ReadTable(str.get(0));
                } else {
                    ReadTable1(str.get(0));
                }
            } else {
                Toast.makeText(ReadActivity.this, "抄表完成,请重新选择区域！", Toast.LENGTH_SHORT).show();
            }
        } else if (v == btn_conn_new) {
            box.m_name = result;
            box.mDevice = mDevice;
            LoadingDialogUtil.showByEvent("正在连接蓝牙",this.getClass().getName());
            box.Connect(10, this, new OnConnectListener() {
                @Override
                public void onConnectSuccess() {
                    LoadingDialogUtil.dismiss(loadingProgress);
                    Toast.makeText(ReadActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                    blue_state.setText("已连接");
                    btn_conn_new.setVisibility(View.INVISIBLE);
                    blue_state.setTextColor(Color.parseColor("#27f35a"));
                    btn_send1.setEnabled(true);
                }

                @Override
                public void onConnectTimeOut() {
                    LoadingDialogUtil.dismiss(loadingProgress);
                    toast = Toast.makeText(getApplicationContext(),
                            "连接超时,请检查盒子是否正常开启！", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }

                @Override
                public void onDisconnect() {
                    LoadingDialogUtil.dismiss(loadingProgress);
                    Toast.makeText(ReadActivity.this, "断开连接", Toast.LENGTH_SHORT).show();
                    blue_state.setText("未连接");
                    btn_conn_new.setVisibility(View.VISIBLE);
                    blue_state.setTextColor(Color.parseColor("#f17e90"));

                }

                @Override
                public void onConnectFail(int rtcode) {
                    Toast.makeText(ReadActivity.this, "连接异常,错误码" + rtcode, Toast.LENGTH_SHORT).show();
                    if (rtcode == 4) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, 1);
                    }
                }
            });

        } else if (v == btn_send2) {
            if(blue_state.getText().toString().equals("未连接")){
                ToastManager.getInstance(this).show("请连接蓝牙");
                return;
            }
            String sn = edt_endsn2.getText().toString();
            if (sn.equals("")) {
                Toast.makeText(this, "没有填写表号", Toast.LENGTH_LONG).show();
                return;
            }
            meter = new DYDirectMeter();
            if (SystemVersion >= 24) {
                LoadingDialogUtil.showByEvent("正在读表",this.getClass().getName());
                box.getSingleData(10, this, meter.getQueryBytes(sn), new OnGetSingleDataListener() {
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
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(ReadActivity.this);
                                    dialog.setTitle("读取结果");
                                    dialog.setMessage("表号  :" + bh + "\n" + "ID  :" + id + "\n" + "正脉冲数  :" + dushu + "\n" + "倍率  :" + beil + "\n" + "状态  :" + statue + "\n");
                                    dialog.setNegativeButton("确定", null);
                                    dialog.create().show();
                                } else {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(ReadActivity.this);
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
                        Toast.makeText(ReadActivity.this, "连接超时", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCommFail(int rtcode) {
                        Toast.makeText(ReadActivity.this, "连接异常,错误码" + rtcode, Toast.LENGTH_LONG).show();
                        LoadingDialogUtil.dismiss(loadingProgress);
                    }

                    @Override
                    public void onCommStop() {
                        // TODO Auto-generated method stub
                        LoadingDialogUtil.dismiss(loadingProgress);
                    }
                });
            } else {
                LoadingDialogUtil.showByEvent("正在读表",this.getClass().getName());
                box.getSingleData1(10, this, meter.getQueryBytes(sn), new OnGetSingleDataListener() {
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
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(ReadActivity.this);
                                    dialog.setTitle("读取结果");
                                    dialog.setMessage("表号  :" + bh + "\n" + "ID  :" + id + "\n" + "正脉冲数  :" + dushu + "\n" + "倍率  :" + beil + "\n" + "状态  :" + statue + "\n");
                                    dialog.setNegativeButton("确定", null);
                                    dialog.create().show();
                                } else {
                                    AlertDialog.Builder dialog = new AlertDialog.Builder(ReadActivity.this);
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
                        Toast.makeText(ReadActivity.this, "连接超时", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onCommFail(int rtcode) {
                        LoadingDialogUtil.dismiss(loadingProgress);
                        Toast.makeText(ReadActivity.this, "连接异常,错误码" + rtcode, Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onCommStop() {
                        LoadingDialogUtil.dismiss(loadingProgress);
                        // TODO Auto-generated method stub

                    }
                });
            }


        } else if (v == txt_disaccept) {
            str.removeAll(tablelist);
            if (str.size() == 0) {
                new AlertDialog.Builder(this)
                        .setTitle("漏抄记录")
                        .setItems(new String[]{"没有漏抄记录！"}, null)
                        .setNegativeButton("确定", null)
                        .show();
            } else {
                //实例化SharedPreferences对象（第一步）
                SharedPreferences mySharedPreferences = getSharedPreferences("nocollectdata", Activity.MODE_PRIVATE);
                //实例化SharedPreferences.Editor对象（第二步）
                SharedPreferences.Editor editor = mySharedPreferences.edit();
                int count = mySharedPreferences.getInt("count01", 0);
                editor.putInt("count01", (count + 1));
                //用putString的方法保存数据
                editor.putString("saveCollect2001", str.toString());
                //提交当前数据
                editor.commit();
                //使用toast信息提示框提示成功写入数据
                String dd = " ";
                dd += mySharedPreferences.getString("saveCollect01", null);
            }
        }
    }

    private void ReadTable(final String sn) {
        meter = new DYDirectMeter();
        LoadingDialogUtil.showByEvent("正在读表",this.getClass().getName());
        box.getSingleData(5, this, meter.getQueryBytes(sn), new OnGetSingleDataListener() {
            @Override
            public void onCommSuccess(byte result[]) {
                LoadingDialogUtil.dismiss(loadingProgress);
                json = meter.getQueryObject(result);
                if (json != null) {
                    try {
                        //rtcode = json.getString("rtcode");
                        sn1 = json.getLong("ID");
                        bh = json.getInt("表号");
                        zhuantai = json.getString("状态");
                        if (bh < 0) {
                            bh = 256 + bh;
                        }
                        if (zhuantai.equals("拆卸断线")) {
                            zhuantai = "正常1";
                        }
                        int zmaichong = json.getInt("正脉冲数");
                        String beil = json.getString("倍率");
                        BigDecimal a1 = new BigDecimal(zmaichong);
                        BigDecimal b1 = new BigDecimal(beil);
                        double dushu = a1.multiply(b1).doubleValue();
                        boolean contains = tablelist.contains(String.valueOf(bh));
                        if (!contains) {
                            tablelist.add(String.valueOf(bh));
                            JSONObject json1 = new JSONObject();
                            if (json != null) {
                                json1.put("cotableid", sn1);
                                json1.put("cotablenumber", bh);
                                json1.put("coimpulse", json.getInt("正脉冲数"));
                                json1.put("coreverseimpulse", json.getInt("反脉冲数"));
                                json1.put("coratio", json.getDouble("倍率"));
                                json1.put("costate", json.getString("状态"));
                                aList.add(json1.toString());
                            }
                        }
                        Map<String, Object> item = new HashMap<String, Object>();
                        item.put("表号", bh);
                        item.put("sn号", sn1);
                        item.put("读数", dushu);
                        item.put("状态", zhuantai);
                        data1.add(item);
                        setList();
                        int dis_size = tablelist.size();
                        int i = size - dis_size;
                        accept.setText(dis_size + "块");
                        dis_accept.setText(i + "块");

                        Thread.sleep(200);
                        //以发消息的方式来继续下一块抄表
                        Message msg = handler.obtainMessage();
                        msg.what = MSG_READSUCCESS;
                        msg.sendToTarget();


                    } catch (JSONException e) {
                        e.printStackTrace();
                        try {
                            Thread.sleep(300);
                            Message msg = handler.obtainMessage();
                            msg.what = MSG_NOSTATE;
                            msg.sendToTarget();
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCommTimeOut() {
                LoadingDialogUtil.dismiss(loadingProgress);
                switch (a = 0) {
                    case 0:
                        ReadTable(sn);
                        a++;
                        break;
                    case 1:
                        try {
                            Thread.sleep(200);
                            Message msg = handler.obtainMessage();
                            msg.what = MSG_READTIMEOUT;
                            msg.sendToTarget();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onCommFail(int rtcode) {
                LoadingDialogUtil.dismiss(loadingProgress);
                switch (a = 0) {
                    case 0:
                        ReadTable(sn);
                        a++;
                        break;
                    case 1:
                        try {
                            Thread.sleep(200);
                            Message msg = handler.obtainMessage();
                            msg.what = MSG_READFAIL;
                            msg.sendToTarget();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onCommStop() {
                LoadingDialogUtil.dismiss(loadingProgress);
                Toast.makeText(ReadActivity.this, "请点击漏抄按钮继续抄表！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ReadTable1(final String sn) {
        meter = new DYDirectMeter();
        LoadingDialogUtil.showByEvent("正在读取",this.getClass().getName());
        box.getSingleData1(5, this, meter.getQueryBytes(sn), new OnGetSingleDataListener() {
            @Override
            public void onCommSuccess(byte result[]) {
                LoadingDialogUtil.dismiss(loadingProgress);
                json = meter.getQueryObject(result);
                if (json != null) {
                    try {
                        //rtcode = json.getString("rtcode");
                        sn1 = json.getLong("ID");
                        bh = json.getInt("表号");
                        zhuantai = json.getString("状态");
                        if (bh < 0) {
                            bh = 256 + bh;
                        }
                        if (zhuantai.equals("拆卸断线")) {
                            zhuantai = "正常1";
                        }
                        int zmaichong = json.getInt("正脉冲数");
                        String beil = json.getString("倍率");
                        BigDecimal a1 = new BigDecimal(zmaichong);
                        BigDecimal b1 = new BigDecimal(beil);
                        double dushu = a1.multiply(b1).doubleValue();
                        boolean contains = tablelist.contains(String.valueOf(bh));
                        if (!contains) {
                            tablelist.add(String.valueOf(bh));
                            JSONObject json1 = new JSONObject();
                            if (json != null) {
                                json1.put("cotableid", sn1);
                                json1.put("cotablenumber", bh);
                                json1.put("coimpulse", json.getInt("正脉冲数"));
                                json1.put("coreverseimpulse", json.getInt("反脉冲数"));
                                json1.put("coratio", json.getDouble("倍率"));
                                json1.put("costate", json.getString("状态"));
                                aList.add(json1.toString());
                            }
                        }
                        Map<String, Object> item = new HashMap<String, Object>();
                        item.put("表号", bh);
                        item.put("sn号", sn1);
                        item.put("读数", dushu);
                        item.put("状态", zhuantai);
                        data1.add(item);
                        setList();
                        int dis_size = tablelist.size();
                        int i = size - dis_size;
                        accept.setText(dis_size + "块");
                        dis_accept.setText(i + "块");

                        Thread.sleep(300);
                        //以发消息的方式来继续下一块抄表
                        Message msg = handler.obtainMessage();
                        msg.what = MSG_READSUCCESS1;
                        msg.sendToTarget();


                    } catch (JSONException e) {
                        e.printStackTrace();
                        try {
                            Thread.sleep(200);
                            Message msg = handler.obtainMessage();
                            msg.what = MSG_NOSTATE1;
                            msg.sendToTarget();
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCommTimeOut() {
                LoadingDialogUtil.dismiss(loadingProgress);
                switch (a = 0) {
                    case 0:
                        ReadTable(sn);
                        a++;
                        break;
                    case 1:
                        try {
                            Thread.sleep(200);
                            Message msg = handler.obtainMessage();
                            msg.what = MSG_READTIMEOUT1;
                            msg.sendToTarget();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onCommFail(int rtcode) {
                LoadingDialogUtil.dismiss(loadingProgress);
                switch (a = 0) {
                    case 0:
                        ReadTable(sn);
                        a++;
                        break;
                    case 1:
                        try {
                            Thread.sleep(200);
                            Message msg = handler.obtainMessage();
                            msg.what = MSG_READFAIL1;
                            msg.sendToTarget();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onCommStop() {
                LoadingDialogUtil.dismiss(loadingProgress);
                Toast.makeText(ReadActivity.this, "请点击漏抄按钮继续抄表！", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*
     * 列表显示数据
     */
    private void setList() {
        //创建SimpleAdapter适配器将数据绑定到item显示控件上
        adapter = new SimpleAdapter(this, data1, R.layout.m_init_list_dataitem,
                new String[]{"表号", "sn号", "读数", "状态"}, new int[]{R.id.biaohao,
                R.id.sn, R.id.dushu, R.id.zhuangtai});
        adapter.notifyDataSetChanged();
        //实现列表的显示
        listView.setAdapter(adapter);
        listView.setSelection(listView.getBottom());
    }


    private static int j = 1;
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_READSUCCESS) {
                if (tableLength > 1 && j < tableLength) {
                    String string = str.get(j).toString();
                    j++;
                    ReadTable(string);
                } else {
                    j = 1;
                }
            } else if (msg.what == MSG_READTIMEOUT) {
                if (tableLength > 1 && j < tableLength) {
                    String string = str.get(j).toString();
                    j++;
                    ReadTable(string);
                } else {
                    j = 1;
                }
            } else if (msg.what == MSG_READFAIL) {
                if (tableLength > 1 && j < tableLength) {
                    String string = str.get(j).toString();
                    j++;
                    ReadTable(string);
                } else {
                    j = 1;
                }
            } else if (msg.what == MSG_NOSTATE) {
                if (tableLength > 1 && j < tableLength) {
                    String string = str.get(j).toString();
                    j++;
                    ReadTable(string);
                } else {
                    j = 1;
                }
            } else if (msg.what == MSG_READSUCCESS1) {
                if (tableLength > 1 && j < tableLength) {
                    String string = str.get(j).toString();
                    j++;
                    ReadTable1(string);
                } else {
                    j = 1;
                }
            } else if (msg.what == MSG_READTIMEOUT1) {
                if (tableLength > 1 && j < tableLength) {
                    String string = str.get(j).toString();
                    j++;
                    ReadTable1(string);
                } else {
                    j = 1;
                }
            } else if (msg.what == MSG_READFAIL1) {
                if (tableLength > 1 && j < tableLength) {
                    String string = str.get(j).toString();
                    j++;
                    ReadTable1(string);
                } else {
                    j = 1;
                }
            } else if (msg.what == MSG_NOSTATE1) {
                if (tableLength > 1 && j < tableLength) {
                    String string = str.get(j).toString();
                    j++;
                    ReadTable1(string);
                } else {
                    j = 1;
                }
            }
        }
    };


    public class MyListener implements OnItemClickListener {

        DYBox box;
        private View view;

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position,
                                long id) {
            @SuppressWarnings("unchecked")
            Map<String, Object> mMap = (Map<String, Object>) adapter.getItem(position);
//           Toast.makeText(ReadActivity.this, mMap.get("表号").toString(), Toast.LENGTH_SHORT).show();
            biaohao = mMap.get("表号").toString();
            sn = mMap.get("sn号").toString();
            dushu = mMap.get("读数").toString();
            statue = mMap.get("状态").toString();
            //调用外部方法，实现下发数据
            dialog();
        }

    }


    public void dialog() {
        //保存抄表记录
        //新增数据
        view = View.inflate(ReadActivity.this, R.layout.m_init_setnumber, null);
        //把值赋给文本框中
        ((EditText) view.findViewById(R.id.bh)).setText(biaohao);
        ((EditText) view.findViewById(R.id.bh)).setKeyListener(null);//设置文本框无法编辑
        ((EditText) view.findViewById(R.id.id)).setText(sn);
        ((EditText) view.findViewById(R.id.dushu)).setText(dushu);
        AlertDialog.Builder dlg = new AlertDialog.Builder(ReadActivity.this);
        dlg.setTitle("置数");
        dlg.setIcon(android.R.drawable.ic_dialog_info);
        dlg.setView(view);
        dlg.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String id = ((EditText) view.findViewById(R.id.id)).getText().toString();
                int bh = Integer.parseInt(((EditText) view.findViewById(R.id.bh)).getText().toString());
                int dushu = Integer.parseInt(((EditText) view.findViewById(R.id.dushu)).getText().toString());
                int beil = (Integer) ((Spinner) view.findViewById(R.id.beil)).getSelectedItemPosition();
                int status = (Integer) ((Spinner) view.findViewById(R.id.status)).getSelectedItemPosition();
                int type = 2;
                final DYMeter meter = new DYDirectMeter();
                LoadingDialogUtil.showByEvent("正在读表",this.getClass().getName());
                box.getSingleData(10, ReadActivity.this, box.setNumber(id, bh, beil, dushu, type, status), new OnGetSingleDataListener() {
                    @Override
                    public void onCommSuccess(byte[] result) {
                        LoadingDialogUtil.dismiss(loadingProgress);
                        JSONObject json = meter.SetRateReturn(result);
                        if (json != null) {
                            try {
                                String string = json.getString("状态");
                                AlertDialog.Builder dialog = new AlertDialog.Builder(ReadActivity.this);
                                dialog.setTitle("读取结果");
                                dialog.setMessage(string);
                                dialog.setNegativeButton("确定", null);
                                dialog.create().show();
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onCommTimeOut() {
                        LoadingDialogUtil.dismiss(loadingProgress);
                        Toast.makeText(ReadActivity.this, "连接超时,请尝试重新连接！", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCommFail(int rtcode) {
                        LoadingDialogUtil.dismiss(loadingProgress);
                        Toast.makeText(ReadActivity.this, "连接异常,错误码" + rtcode, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCommStop() {
                        LoadingDialogUtil.dismiss(loadingProgress);
                    }
                });
            }
        });
        dlg.setNegativeButton("取消", null);
        dlg.show();
//	Toast.makeText(ReadActivity.this, "数据保存中，请稍等...", Toast.LENGTH_SHORT).show();

    }


}
