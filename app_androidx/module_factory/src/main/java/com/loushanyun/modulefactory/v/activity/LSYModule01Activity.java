//package com.loushanyun.modulefactory.v.activity;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.drawable.Drawable;
//import android.util.Log;
//import android.view.View;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.flyco.roundview.RoundLinearLayout;
//import com.flyco.roundview.RoundTextView;
//import com.loushanyun.modulefactory.R;
//import com.loushanyun.modulefactory.init.LoginParamManager;
//import com.wu.loushanyun.base.util.DataParser;
//import com.wu.loushanyun.base.util.LouShanYunUtils;
//import com.wu.loushanyun.base.util.MapParams;
//import com.wu.loushanyun.basemvp.v.activity.BaseBlueToothActivity;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//
//import jrt.cifco.com.base.base.BaseAttribute;
//import jrt.cifco.com.base.baseevent.Event;
//
//public class LSYModule01Activity extends BaseBlueToothActivity implements View.OnClickListener {
//    private final int TYPE_TTL = 0;
//    private final int TYPE_MOD_BUS = 1;
//    private final int TYPE_485 = 2;
//    private final String PERSONAL = "家用(单击切换为公用)";
//    private final String COMMUNITY = "公用(单击切换为家用)";
//
//    private TextView bluetoothName;
//    private TextView itemSelector;
//    private TextView itemGeneralSetting;
//    private TextView tvTypename;
//    private View itemInfoLine1;
//    private LinearLayout itemInfoLl1;
//    private TextView itemInfoTv1;
//    private View itemInfoLine2;
//    private LinearLayout itemInfoLl2;
//    private TextView itemInfoTv2;
//    private View itemInfoLine3;
//    private LinearLayout itemInfoLl3;
//    private TextView itemInfoTv3;
//    private View itemInfoLine4;
//    private LinearLayout itemInfoLl4;
//    private TextView itemInfoTv4;
//    private View itemInfoLine5;
//    private LinearLayout itemInfoLl5;
//    private TextView itemInfoTv5;
//    private RoundLinearLayout itemSetting;
//    private View itemSettingLine1;
//    private LinearLayout itemSettingLl1;
//    private TextView itemSettingTv1;
//    private View itemSettingLine2;
//    private LinearLayout itemSettingLl2;
//    private TextView itemSettingTv2;
//    private View itemSettingLine3;
//    private LinearLayout itemSettingLl3;
//    private TextView itemSettingTv3;
//    private View itemSettingLine4;
//    private LinearLayout itemSettingLl4;
//    private TextView itemSettingTv4;
//    private View itemSettingLine5;
//    private LinearLayout itemSettingLl5;
//    private TextView itemSettingTv5;
//    private View itemSettingLine6;
//    private LinearLayout itemSettingLl6;
//    private TextView itemSettingTv6;
//    private RoundTextView setting;
//    private RoundTextView loading;
//    private FrameLayout shadow;
//    private RoundLinearLayout dialog;
//    private TextView dialogName;
//    private View dialogItemLine0;
//    private TextView dialogItemTv0;
//    private View dialogItemLine1;
//    private TextView dialogItemTv1;
//    private View dialogItemLine2;
//    private TextView dialogItemTv2;
//    private View dialogItemLine3;
//    private TextView dialogItemTv3;
//
//
//    private int type;//0 ttl,1 mod-bus,2 485
//    private int clickItem;//0 1 2 3
//    private HashMap<String, String> values;
//    private String[] 产品形式 = {"流量计", "工业水表", "压力计", "水质分析仪"};
//    private String[] 电源类型 = {"物联电池", "外接电源"};
//    private String[] 扩频因子 = {"SF9", "SF10", "SF11", "SF12"};
//    private String[] 信道参数 = {"模式A", "模式B"};
//    private String[] 发送功率 = {"16dbm", "18dbm", "20dbm"};
//    private String[] 网络交互 = {"带网络反馈", "不带网络反馈"};
//    private Drawable arrow, blank;
//    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//    private Double softVersion;
//    private int typeMoZu;
//
//    @Override
//    public void onInitAttribute(BaseAttribute ba) {
//        type = getIntent().getIntExtra("type", TYPE_TTL);
//        ba.mActivityLayoutId = R.layout.lsy_module01;
//        switch (type) {
//            case TYPE_TTL:
//                ba.mTitleText = "TTL";
//                break;
//            case TYPE_MOD_BUS:
//                ba.mTitleText = "MOD-BUS";
//                break;
//            case TYPE_485:
//                ba.mTitleText = "485";
//                break;
//        }
//    }
//
//    @Override
//    public void initView() {
//        super.initView();
//        //init view
//        bluetoothName = (TextView) findViewById(R.id.bluetooth_name);
//        itemSelector = (TextView) findViewById(R.id.item_selector);
//        itemGeneralSetting = (TextView) findViewById(R.id.item_general_setting);
//        tvTypename = (TextView) findViewById(R.id.tv_typename);
//        itemInfoLine1 = (View) findViewById(R.id.item_info_line1);
//        itemInfoLl1 = (LinearLayout) findViewById(R.id.item_info_ll1);
//        itemInfoTv1 = (TextView) findViewById(R.id.item_info_tv1);
//        itemInfoLine2 = (View) findViewById(R.id.item_info_line2);
//        itemInfoLl2 = (LinearLayout) findViewById(R.id.item_info_ll2);
//        itemInfoTv2 = (TextView) findViewById(R.id.item_info_tv2);
//        itemInfoLine3 = (View) findViewById(R.id.item_info_line3);
//        itemInfoLl3 = (LinearLayout) findViewById(R.id.item_info_ll3);
//        itemInfoTv3 = (TextView) findViewById(R.id.item_info_tv3);
//        itemInfoLine4 = (View) findViewById(R.id.item_info_line4);
//        itemInfoLl4 = (LinearLayout) findViewById(R.id.item_info_ll4);
//        itemInfoTv4 = (TextView) findViewById(R.id.item_info_tv4);
//        itemInfoLine5 = (View) findViewById(R.id.item_info_line5);
//        itemInfoLl5 = (LinearLayout) findViewById(R.id.item_info_ll5);
//        itemInfoTv5 = (TextView) findViewById(R.id.item_info_tv5);
//        itemSetting = (RoundLinearLayout) findViewById(R.id.item_setting);
//        itemSettingLine1 = (View) findViewById(R.id.item_setting_line1);
//        itemSettingLl1 = (LinearLayout) findViewById(R.id.item_setting_ll1);
//        itemSettingTv1 = (TextView) findViewById(R.id.item_setting_tv1);
//        itemSettingLine2 = (View) findViewById(R.id.item_setting_line2);
//        itemSettingLl2 = (LinearLayout) findViewById(R.id.item_setting_ll2);
//        itemSettingTv2 = (TextView) findViewById(R.id.item_setting_tv2);
//        itemSettingLine3 = (View) findViewById(R.id.item_setting_line3);
//        itemSettingLl3 = (LinearLayout) findViewById(R.id.item_setting_ll3);
//        itemSettingTv3 = (TextView) findViewById(R.id.item_setting_tv3);
//        itemSettingLine4 = (View) findViewById(R.id.item_setting_line4);
//        itemSettingLl4 = (LinearLayout) findViewById(R.id.item_setting_ll4);
//        itemSettingTv4 = (TextView) findViewById(R.id.item_setting_tv4);
//        itemSettingLine5 = (View) findViewById(R.id.item_setting_line5);
//        itemSettingLl5 = (LinearLayout) findViewById(R.id.item_setting_ll5);
//        itemSettingTv5 = (TextView) findViewById(R.id.item_setting_tv5);
//        itemSettingLine6 = (View) findViewById(R.id.item_setting_line6);
//        itemSettingLl6 = (LinearLayout) findViewById(R.id.item_setting_ll6);
//        itemSettingTv6 = (TextView) findViewById(R.id.item_setting_tv6);
//        setting = (RoundTextView) findViewById(R.id.setting);
//        loading = (RoundTextView) findViewById(R.id.loading);
//        shadow = (FrameLayout) findViewById(R.id.shadow);
//        dialog = (RoundLinearLayout) findViewById(R.id.dialog);
//        dialogName = (TextView) findViewById(R.id.dialog_name);
//        dialogItemLine0 = (View) findViewById(R.id.dialog_item_line0);
//        dialogItemTv0 = (TextView) findViewById(R.id.dialog_item_tv0);
//        dialogItemLine1 = (View) findViewById(R.id.dialog_item_line1);
//        dialogItemTv1 = (TextView) findViewById(R.id.dialog_item_tv1);
//        dialogItemLine2 = (View) findViewById(R.id.dialog_item_line2);
//        dialogItemTv2 = (TextView) findViewById(R.id.dialog_item_tv2);
//        dialogItemLine3 = (View) findViewById(R.id.dialog_item_line3);
//        dialogItemTv3 = (TextView) findViewById(R.id.dialog_item_tv3);
//        //init listener
//        tvTypename.setOnClickListener(this::onClick);
//        itemInfoLl1.setOnClickListener(this::onClick);
//        itemInfoLl2.setOnClickListener(this::onClick);
//        itemInfoLl3.setOnClickListener(this::onClick);
//        itemInfoLl4.setOnClickListener(this::onClick);
//        itemInfoLl5.setOnClickListener(this::onClick);
//        itemSettingLl1.setOnClickListener(this::onClick);
//        itemSettingLl2.setOnClickListener(this::onClick);
//        itemSettingLl3.setOnClickListener(this::onClick);
//        itemSettingLl4.setOnClickListener(this::onClick);
//        itemSettingLl5.setOnClickListener(this::onClick);
//        itemSettingLl6.setOnClickListener(this::onClick);
//        setting.setOnClickListener(this::onClick);
//        loading.setOnClickListener(this::onClick);
//        tvTypename.setOnClickListener(this::onClick);
//
//        dialogItemTv0.setOnClickListener(this::onClick);
//        dialogItemTv1.setOnClickListener(this::onClick);
//        dialogItemTv2.setOnClickListener(this::onClick);
//        dialogItemTv3.setOnClickListener(this::onClick);
//
//        hideDialog();
//        //init data
//        values = new HashMap<>();
//        arrow = getResources().getDrawable(R.drawable.blue);
//        arrow.setBounds(0, 0, arrow.getMinimumWidth(), arrow.getMinimumHeight());
//        blank = getResources().getDrawable(R.drawable.blank);
//        blank.setBounds(0, 0, blank.getMinimumWidth(), blank.getMinimumHeight());
//        switch (type) {
//            case TYPE_TTL:
//                itemSelector.setText("远传表号接入");
//                tvTypename.setText("物联网集中器");
//                itemInfoTv1.setText("TTL1");
//                Drawable drawable = getResources().getDrawable(R.drawable.ycbhjr);
//                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//                itemSelector.setCompoundDrawables(null, drawable, null, null);
//                itemInfoLine3.setVisibility(View.GONE);
//                itemInfoLl3.setVisibility(View.GONE);
//                itemInfoLine4.setVisibility(View.GONE);
//                itemInfoLl4.setVisibility(View.GONE);
//                itemInfoLine5.setVisibility(View.GONE);
//                itemInfoLl5.setVisibility(View.GONE);
//                itemGeneralSetting.setVisibility(View.GONE);
//                itemSettingLine1.setVisibility(View.GONE);
//                itemSettingLl1.setVisibility(View.GONE);
//                break;
//            case TYPE_MOD_BUS:
//                itemInfoLine2.setVisibility(View.GONE);
//                itemInfoLl2.setVisibility(View.GONE);
//                itemInfoLine3.setVisibility(View.GONE);
//                itemInfoLl3.setVisibility(View.GONE);
//                itemInfoLine4.setVisibility(View.GONE);
//                itemInfoLl4.setVisibility(View.GONE);
//                itemGeneralSetting.setVisibility(View.GONE);
//                itemSetting.setVisibility(View.GONE);
//                break;
//            case TYPE_485:
//                itemSelector.setOnClickListener(this::onClick);
//                itemGeneralSetting.setOnClickListener(this::onClick);
//                itemGeneralSetting.setBackgroundColor(0xffdce2e6);
//                itemSelector.setBackgroundColor(0xfff0f4f7);
//                itemInfoLine2.setVisibility(View.GONE);
//                itemInfoLl2.setVisibility(View.GONE);
//                itemInfoLine3.setVisibility(View.GONE);
//                itemInfoLl3.setVisibility(View.GONE);
//                itemInfoLine4.setVisibility(View.GONE);
//                itemInfoLl4.setVisibility(View.GONE);
//                itemGeneralSetting.setVisibility(View.VISIBLE);
//                itemSetting.setVisibility(View.GONE);
//                itemSettingLine3.setVisibility(View.GONE);
//                itemSettingLl3.setVisibility(View.GONE);
//                itemSettingLine4.setVisibility(View.GONE);
//                itemSettingLl4.setVisibility(View.GONE);
//                itemSettingLine5.setVisibility(View.GONE);
//                itemSettingLl5.setVisibility(View.GONE);
//                itemSettingLine6.setVisibility(View.GONE);
//                itemSettingLl6.setVisibility(View.GONE);
//                break;
//        }
//        connectBlueTooth();
//    }
//
//    @Override
//    public void onClick(View v) {
//        if (v == itemGeneralSetting) {
//            tvTypename.setText(PERSONAL);
//            type = TYPE_485 + 1;
//            itemGeneralSetting.setBackgroundColor(0xfff0f4f7);
//            itemSelector.setBackgroundColor(0xffdce2e6);
//            itemSetting.setVisibility(View.VISIBLE);
//            itemInfoLine1.setVisibility(View.GONE);
//            itemInfoLl1.setVisibility(View.GONE);
//            itemInfoLine2.setVisibility(View.VISIBLE);
//            itemInfoLl2.setVisibility(View.VISIBLE);
//            itemInfoLine3.setVisibility(View.VISIBLE);
//            itemInfoLl3.setVisibility(View.VISIBLE);
//            itemInfoLine4.setVisibility(View.VISIBLE);
//            itemInfoLl4.setVisibility(View.VISIBLE);
//            itemInfoLine5.setVisibility(View.GONE);
//            itemInfoLl5.setVisibility(View.GONE);
//        } else if (v == itemSelector) {
//            tvTypename.setText("数字信号");
//            type = TYPE_485;
//            itemGeneralSetting.setBackgroundColor(0xffdce2e6);
//            itemSelector.setBackgroundColor(0xfff0f4f7);
//            itemInfoLine1.setVisibility(View.VISIBLE);
//            itemInfoLl1.setVisibility(View.VISIBLE);
//            itemInfoLine2.setVisibility(View.GONE);
//            itemInfoLl2.setVisibility(View.GONE);
//            itemInfoLine3.setVisibility(View.GONE);
//            itemInfoLl3.setVisibility(View.GONE);
//            itemInfoLine4.setVisibility(View.GONE);
//            itemInfoLl4.setVisibility(View.GONE);
//            itemInfoLine5.setVisibility(View.VISIBLE);
//            itemInfoLl5.setVisibility(View.VISIBLE);
//        } else if (v == itemSettingLl1) {
//            clickItem = 1;
//            showDialog("产品形式", itemSettingTv1.getText(), 产品形式);
//        } else if (v == itemSettingLl2) {
//            clickItem = 2;
//            showDialog("电源类型", itemSettingTv2.getText(), 电源类型);
//        } else if (v == itemInfoLl5) {
//            clickItem = 2;
//            showDialog("电源类型", itemInfoTv5.getText(), 电源类型);
//        } else if (v == itemSettingLl3) {
//            clickItem = 3;
//            showDialog("扩频因子", itemSettingTv3.getText(), 扩频因子);
//        } else if (v == itemSettingLl4) {
//            clickItem = 4;
//            showDialog("信道参数", itemSettingTv4.getText(), 信道参数);
//        } else if (v == itemSettingLl5) {
//            clickItem = 5;
//            showDialog("发送功率", itemSettingTv5.getText(), 发送功率);
//        } else if (v == itemSettingLl6) {
//            clickItem = 6;
//            showDialog("网络交互", itemSettingTv6.getText(), 网络交互);
//        } else if (v == dialogItemTv0) {
//            changeSettingData(0);
//        } else if (v == dialogItemTv1) {
//            changeSettingData(1);
//        } else if (v == dialogItemTv2) {
//            changeSettingData(2);
//        } else if (v == dialogItemTv3) {
//            changeSettingData(3);
//        } else if (v == shadow) {
//            hideDialog();
//        } else if (v == setting) {
//            if (typeMoZu != 1) {
//                sendMessageToast("识别不是1号模组");
//                return;
//            }
//            if (type == TYPE_TTL) {
//                uploadTTL();
//            } else {
//                sendMessageToast("尚未完成");
//            }
//        } else if (v == loading) {
//            if (typeMoZu != 1) {
//                sendMessageToast("识别不是1号模组");
//                return;
//            }
//            if (softVersion < 1.05) {
//                new AlertDialog.Builder(LSYModule01Activity.this)
//                        .setTitle("提示！！！")
//                        .setMessage("建议把程序和集中器模块升级至1.05版本(1.05抄表更稳定)，再进行出厂配置！！！")
//                        .setPositiveButton("不升级", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Intent intent = new Intent(LSYModule01Activity.this, FirstDetectionActivity.class);
//                                intent.putExtra("sensoroDevice", sensoroDevice);
//                                intent.putExtra("type", type);
//                                startActivity(intent);
//                            }
//                        })
//                        .setNegativeButton("好的，升级", null)
//                        .show();
//            } else {
//                Intent intent = new Intent(this, FirstDetectionActivity.class);
//                intent.putExtra("sensoroDevice", sensoroDevice);
//                intent.putExtra("type", type);
//                startActivity(intent);
//            }
//
//        } else if (v == tvTypename) {
//            if (type == TYPE_485 + 1) {
//                if (PERSONAL.equals(tvTypename.getText())) {
//                    tvTypename.setText(COMMUNITY);
//                } else {
//                    tvTypename.setText(PERSONAL);
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onChildNotify(byte[] result) {
//        Log.i("yunanhao", "onNotify:" + DataParser.byteToString(result));
//        byte code = (byte) (result[2] ^ ((byte) 0x80));
//        switch (code) {
//            case 0x00:
//                if (result[3] == 0x00) {
//                    byte[] bytes = {0x68, 0x02, 0x05, 0x01, 0x08, 0x16};
//                    write(bytes, "正在设置模块工作模式");
//                } else {
//                    sendMessageToast("设置采集场景失败");
//                }
//                break;
//            case 0x01:
//                if (result[3] == 0x00) {
//                    sendMessageToast("设置成功");
//                } else {
//                    sendMessageToast("设置失败");
//                }
//                break;
//            case 0x11:
//                //ttl 需要发频 扩频 信道 信强 网交
//                try {
//                    values.putAll(DataParser.getInformationAll(result));
//                    typeMoZu = DataParser.getModuleType(result);
//                    if (typeMoZu == 1) {
//                        softVersion = Double.valueOf(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(values.get(MapParams.软件版本))));
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                itemSettingTv3.setText(LouShanYunUtils.getKPYZReadStringByCode(values.get(MapParams.扩频因子)));
//                                itemSettingTv4.setText(LouShanYunUtils.getXDCSReadStringByCode(values.get(MapParams.信道参数)));
//                                itemSettingTv5.setText(values.get(MapParams.发送功率) + "dbm");
//                                if ("0".equals(values.get(MapParams.网络交互))) {
//                                    itemSettingTv6.setText(网络交互[1]);
//                                } else {
//                                    itemSettingTv6.setText(网络交互[0]);
//                                }
//                            }
//                        });
//                        write(DataParser.CMD_INFO_BASE, "正在读取设备信息");
//                    } else {
//                        sendMessageToast("识别不是一号模组");
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            case 0x03:
//                if (result[3] == 0x00) {
//                    write(DataParser.setKuoPinYinZi(
//                            LouShanYunUtils.getKPYZWriteCodeByString(
//                                    itemSettingTv3.getText().toString())),
//                            "正在设置扩频因子");
//                } else {
//                    sendMessageToast("设置模块出厂日期失败");
//                }
//                break;
//            case 0x20:
//                if (result[3] == 0x00) {
//                    byte[] bytes = {0x68, 0x02, 0x00, 0x40, 0x42, 0x16};
//                    write(bytes, "正在设置采集场景");
//                } else {
//                    sendMessageToast("设置电源类型失败");
//                }
//                break;
//            case 0x07:
//                if (result[3] == 0x00) {
//                    write(DataParser.setXinDaoCanShu(
//                            "模式A".equals(itemSettingTv4.getText())),
//                            "正在设置信道参数");
//                } else {
//                    sendMessageToast("设置扩频因子失败");
//                }
//                break;
//            case 0x08:
//                if (result[3] == 0x00) {
//                    int num = Integer.parseInt(itemSettingTv5.getText().subSequence(0, 2).toString());
//                    if (softVersion >= 1.03) {
//                        write(DataParser.setFaSongGongLv(num), "正在设置发送功率");
//                    } else {
//                        if (num == 20) {
//                            write(DataParser.setFaSongGongLv(14), "正在设置发送功率");
//                        } else if (num == 18) {
//                            write(DataParser.setFaSongGongLv(12), "正在设置发送功率");
//                        } else if (num == 16) {
//                            write(DataParser.setFaSongGongLv(10), "正在设置发送功率");
//                        }
//                    }
//                } else {
//                    sendMessageToast("设置信道参数失败");
//                }
//                break;
//            case 0x02:
//                if (result[3] == 0x00) {
////                    if (itemSettingTv6.getText().equals("不带网络反馈")) {
////                        byte[] cmd = {0x68, 0x02, 0x04, 0x00, 0x06, 0x16};
////                        write(cmd, "正在设置网络交互");
////                    } else {
//                    byte[] cmd = {0x68, 0x02, 0x04, 0x01, 0x07, 0x16};
//                    write(cmd, "正在设置网络交互");
////                    }
//                } else {
//                    sendMessageToast("设置发送功率失败");
//                }
//                break;
//            case 0x04:
//                if (result[3] == 0x00) {
//                    byte[] d = new byte[7];
//                    d[0] = (byte) 0x68;
//                    d[1] = (byte) 0x03;//有效数据
//                    d[2] = (byte) 0x06;//命令
//                    d[3] = (byte) (LoginParamManager.getInstance().getProductRegister().getManufacturersIdentification() & 0xff);
//                    d[4] = (byte) ((LoginParamManager.getInstance().getProductRegister().getManufacturersIdentification() & 0xff00) >> 8);
//                    byte cs = 0;
//                    for (int i = 1; i < 5; i++) {
//                        cs += d[i];
//                    }
//                    d[5] = cs;//校验和
//                    d[6] = (byte) 0x16;
//                    write(d, "正在设置厂家标识");
//                } else {
//                    sendMessageToast("设置网络交互失败");
//                }
//                break;
//            case 0x30:
//                //ttl 传感 电源
//                values.putAll(DataParser.getInformationBase(result));
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        itemSettingTv2.setText(LouShanYunUtils.getDianYuanLeiXin(values.get(MapParams.电源类型)));
//                        itemInfoTv5.setText(LouShanYunUtils.getDianYuanLeiXin(values.get(MapParams.电源类型)));
//                    }
//                });
//                break;
//            case 0x05:
//                if (result[3] == 0x00) {
//                    String time = sdf.format(new Date());
//                    String[] split = time.split("-");
//                    String year = split[0].substring(split[0].length() - 2, split[0].length());
//                    String month = split[1].toString();
//                    String day = split[2].toString();
//                    byte[] bytes = {0x68, 0x04, 0x03,
//                            (byte) (Integer.parseInt(year) & 0xff),
//                            (byte) (Integer.parseInt(month) & 0xff),
//                            (byte) (Integer.parseInt(day) & 0xff),
//                            0x07, 0x16};
//                    bytes[bytes.length - 2] += bytes[3];
//                    bytes[bytes.length - 2] += bytes[4];
//                    bytes[bytes.length - 2] += bytes[5];
//                    write(bytes, "正在设置模块出厂日期");
//                } else {
//                    sendMessageToast("设置工作模式失败");
//                }
//                break;
//            case 0x06:
//                if (result[3] == 0x00) {
//                    byte[] cmd = {0x68, 0x02, 0x01, 0x30, 0x33, 0x16};
//                    write(cmd, "正在设置发送频率，24小时");
//                } else {
//                    sendMessageToast("设置厂家标识失败");
//                }
//                break;
//
//            default:
//                sendMessageToast("error");
//        }
//    }
//
//    /**
//     * @param title      弹窗标题
//     * @param selectText 选中项
//     * @param items      子项名称 为空时隐藏弹窗
//     */
//    public void showDialog(String title, CharSequence selectText, String... items) {
//        dialogName.setText(title);
//        int index = 0;
//        for (; index < items.length; index++) {
//            if (selectText.equals(items[index])) {
//                break;
//            }
//        }
//        switch (items.length) {
//            case 4:
//                dialogItemTv3.setText(items[3]);
//                if (index == 3) {
//                    dialogItemTv3.setCompoundDrawables(arrow, null, null, null);
//                } else {
//                    dialogItemTv3.setCompoundDrawables(blank, null, null, null);
//                }
//                dialogItemTv3.setVisibility(View.VISIBLE);
//                dialogItemLine3.setVisibility(View.VISIBLE);
//            case 3:
//                dialogItemTv2.setText(items[2]);
//                if (index == 2) {
//                    dialogItemTv2.setCompoundDrawables(arrow, null, null, null);
//                } else {
//                    dialogItemTv2.setCompoundDrawables(blank, null, null, null);
//                }
//                dialogItemTv2.setVisibility(View.VISIBLE);
//                dialogItemLine2.setVisibility(View.VISIBLE);
//            case 2:
//                dialogItemTv1.setText(items[1]);
//                if (index == 1) {
//                    dialogItemTv1.setCompoundDrawables(arrow, null, null, null);
//                } else {
//                    dialogItemTv1.setCompoundDrawables(blank, null, null, null);
//                }
//                dialogItemTv1.setVisibility(View.VISIBLE);
//                dialogItemLine1.setVisibility(View.VISIBLE);
//            case 1:
//                dialogItemTv0.setText(items[0]);
//                if (index == 0) {
//                    dialogItemTv0.setCompoundDrawables(arrow, null, null, null);
//                } else {
//                    dialogItemTv0.setCompoundDrawables(blank, null, null, null);
//                }
//                dialogItemTv0.setVisibility(View.VISIBLE);
//                dialogItemLine0.setVisibility(View.VISIBLE);
//                break;
//            case 0:
//                hideDialog();
//                return;
//            default:
//        }
//        shadow.setVisibility(View.VISIBLE);
//        dialog.setVisibility(View.VISIBLE);
//    }
//
//    /**
//     * 隐藏所有弹窗样式布局
//     */
//    public void hideDialog() {
//        dialogItemTv0.setVisibility(View.GONE);
//        dialogItemLine0.setVisibility(View.GONE);
//        dialogItemTv1.setVisibility(View.GONE);
//        dialogItemLine1.setVisibility(View.GONE);
//        dialogItemTv2.setVisibility(View.GONE);
//        dialogItemLine2.setVisibility(View.GONE);
//        dialogItemTv3.setVisibility(View.GONE);
//        dialogItemLine3.setVisibility(View.GONE);
//        dialog.setVisibility(View.GONE);
//        shadow.setVisibility(View.GONE);
//    }
//
//    /**
//     * 选择弹窗的一项时改变原有项数据并隐藏弹窗
//     */
//    public void changeSettingData(int selectIndex) {
//        switch (clickItem) {
//            case 1:
//                if (selectIndex < 产品形式.length) {
//                    itemSettingTv1.setText(产品形式[selectIndex]);
//                } else {
//                    sendMessageToast("当前操作出现错误");
//                }
//                break;
//            case 2:
//                if (selectIndex < 电源类型.length) {
//                    itemSettingTv2.setText(电源类型[selectIndex]);
//                    itemInfoTv5.setText(电源类型[selectIndex]);
//                } else {
//                    sendMessageToast("当前操作出现错误");
//                }
//                break;
//            case 3:
//                if (selectIndex < 扩频因子.length) {
//                    itemSettingTv3.setText(扩频因子[selectIndex]);
//                } else {
//                    sendMessageToast("当前操作出现错误");
//                }
//                break;
//            case 4:
//                if (selectIndex < 信道参数.length) {
//                    itemSettingTv4.setText(信道参数[selectIndex]);
//                } else {
//                    sendMessageToast("当前操作出现错误");
//                }
//                break;
//            case 5:
//                if (selectIndex < 发送功率.length) {
//                    itemSettingTv5.setText(发送功率[selectIndex]);
//                } else {
//                    sendMessageToast("当前操作出现错误");
//                }
//                break;
//            case 6:
//                if (selectIndex < 网络交互.length) {
//                    itemSettingTv6.setText(网络交互[selectIndex]);
//                } else {
//                    sendMessageToast("当前操作出现错误");
//                }
//                break;
//        }
//        hideDialog();
//    }
//
//    public void uploadTTL() {
//        if (bluetoothName.getText().toString().trim().equals("蓝牙设备:未连接")) {
//            sendMessageToast("未连接蓝牙设备，请先连接");
//        }
//        String time = sdf.format(new Date());
//        String[] split = time.split("-");
//        String year = split[0].substring(split[0].length() - 2, split[0].length());
//        String month = split[1].toString();
//        String day = split[2].toString();
//        HashMap<String, String> map = new HashMap<>();
//        map.put(MapParams.设备ID, LouShanYunUtils.getTimeID());
//        map.put(MapParams.采集场景, String.valueOf(0x40));
//        map.put(MapParams.产品形式, String.valueOf(0x04));
//        map.put(MapParams.传感信号, String.valueOf(0x04));
//        map.put(MapParams.保留字节, String.valueOf(0x00));
//        if (itemInfoLl5.getVisibility() == View.VISIBLE) {
//            if (itemInfoTv5.getText().toString().equals("物联电池")) {
//                map.put(MapParams.电源类型, String.valueOf(0b10000000));
//            } else {
//                map.put(MapParams.电源类型, String.valueOf(0b00100000));
//            }
//        } else {
//            if (itemSettingTv2.getText().toString().equals("物联电池")) {
//                map.put(MapParams.电源类型, String.valueOf(0b10000000));
//            } else {
//                map.put(MapParams.电源类型, String.valueOf(0b00100000));
//            }
//        }
//        map.put(MapParams.出厂时间_年, year);
//        map.put(MapParams.出厂时间_月, month);
//        map.put(MapParams.出厂时间_日, day);
//        map.put(MapParams.工作模式, String.valueOf(0x01));
//        map.put(MapParams.信号类型, String.valueOf(0x01));
//        map.put(MapParams.参数内容, String.valueOf(0x00));//无此属性
//        map.put(MapParams.脉宽, String.valueOf(0x00));//无此属性
//        map.put(MapParams.压力值标定_初始值, String.valueOf(0x00));//无此属性
//        map.put(MapParams.压力值标定_最大值, String.valueOf(0x00));//无此属性
//        map.put(MapParams.底板状态_设备强磁状态, String.valueOf(0x00));//无此属性
//        map.put(MapParams.底板状态_设备拆卸状态, String.valueOf(0x00));//无此属性
//        map.put(MapParams.底板状态_水表倒流状态, String.valueOf(0x00));//无此属性
//        map.put(MapParams.底板状态_自备电池状态, String.valueOf(0x00));
//        map.put(MapParams.底板状态_第三方电池状态, String.valueOf(0x00));
//        map.put(MapParams.底板状态_外接电源220V状态, String.valueOf(0x00));
//
//        write(DataParser.getInformationSettingCMD(map), "正在设置参数");
//    }
//
//    @Override
//    protected void onChildConnectSuccess() {
//        write(DataParser.CMD_INFO_ALL, "正在读取设备信息");
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                bluetoothName.setText("蓝牙设备:" + sensoroDevice.getSerialNumber() + " 已连接");
//            }
//        });
//    }
//
//    @Override
//    protected void onChildWriteSuccess() {
//    }
//
//    @Override
//    protected void onChildWriteFailure(int i) {
//    }
//
//
//    @Override
//    protected void onChildConnectFailed(int i) {
//    }
//
//    @Override
//    protected void initEventListener() {
//    }
//
//    @Override
//    protected void onEventRunEnd(Event event, int code) {
//    }
//
//    /**
//     * 获取发送频率
//     */
//    public String getFSPV() {
//        switch (Integer.parseInt(values.get(MapParams.发送频率))) {
//            case 0x00:
//                return "15分钟";
//            case 0x10:
//                return "待定";
//            case 0x20:
//                return "待定";
//            case 0x30:
//                return "24小时";
//            case 0x40:
//                return "48小时";
//            case 0x50:
//                return "待定";
//            case 0x60:
//                return "待定";
//            case 0x70:
//                return "待定";
//            case 0x80:
//                return "72小时";
//            default:
//                return "待定";
//
//        }
//    }
//}
