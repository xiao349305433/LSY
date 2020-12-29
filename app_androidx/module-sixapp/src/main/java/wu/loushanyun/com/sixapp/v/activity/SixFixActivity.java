package wu.loushanyun.com.sixapp.v.activity;

import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.elvishew.xlog.XLog;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.sensoro.sensor.kit.callback.SensoroDeviceListener;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LorawanUtils;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.init.SensoroBlueManager;
import com.wu.loushanyun.basemvp.init.SnBlueToothListener;
import com.wu.loushanyun.basemvp.init.SnBlueToothTool;
import com.wu.loushanyun.basemvp.m.sat.SAtInstructRealizeFactory;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.glide.GlideUtil;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ScreenUtils;
import met.hx.com.librarybase.some_utils.TimeUtils;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.Utils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.views.dialog.MDDialog;
import wu.loushanyun.com.sixapp.R;
import wu.loushanyun.com.sixapp.init.LoginParamManager;
import wu.loushanyun.com.sixapp.init.SixCode;
import wu.loushanyun.com.sixapp.init.SixUtils;
import wu.loushanyun.com.sixapp.m.EnvNameInfo;
import wu.loushanyun.com.sixapp.m.EnvironmentInfInfo;
import wu.loushanyun.com.sixapp.m.ResultJson;
import wu.loushanyun.com.sixapp.m.TestUpData;
import wu.loushanyun.com.sixapp.p.adapter.SNBlueViewBinder;
import wu.loushanyun.com.sixapp.p.runner.EnvNameRunner;
import wu.loushanyun.com.sixapp.p.runner.EnvironmentInfRunner;
import wu.loushanyun.com.sixapp.p.runner.ManuInfRunner;
import wu.loushanyun.com.sixapp.p.runner.SaveFirstRunner;


@Route(path = K.SixFixActivity)
public class SixFixActivity extends BaseNoPresenterActivity implements SnBlueToothListener {

    private ScrollView testScroll;
    private EditText testEditSearch;
    private TextView testResetClearlistTv;
    private TextView testResetClearTv;
    private ImageView testBlueImg;
    private TextView testBlueState;
    private LinearLayout testBlueLayout;
    private RecyclerView testBlueRv;
    private LinearLayout connectLayout;
    private View centerView;
    private TextView blueCloseTv;
    private LinearLayout fixTu2Layout;
    private LinearLayout fixTu3Layout;
    private LinearLayout fixTu1Layout;
    private LinearLayout fixTu4Layout;
    private ImageView fixTu1Img;
    private ImageView fixTu2Img;
    private ImageView fixTu3Img;
    private ImageView fixTu4Img;
    private TextView fixTypeName;
    private TextView fixTu4Tv;
    public static boolean Fix_Tu1=false;
    public static boolean Fix_Tu2=false;
    public static boolean Fix_Tu3=false;
    public static boolean Fix_Tu4=false;

    private EnvNameInfo envNameInfo;
    private MultiTypeAdapter multiTypeAdapter;
    private SNBlueViewBinder snBlueViewBinder;
    private ArrayList<SensoroDevice> deviceArrayList;
    protected SnBlueToothTool snBlueToothTool;
    private SensoroDevice mSensoroDevice;
    private SAtInstructRealizeFactory sAtInstructRealizeFactory;
    private int type;
    private PopupWindow myPop;
    private MDDialog mdDialog;
    private String DivceID;

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mTitleText = "生产测试";
        ba.mTitleBackgroundColorId = R.color.blue_bg;
        ba.mHasRightView = true;
        ba.mActivityLayoutId = R.layout.m_six_activity_fix;
        ba.mTitleRightImageIcon=R.mipmap.test_wxhj;
    }


    @Override
    protected void initView() {
        testScroll = (ScrollView) findViewById(R.id.test_scroll);
        testEditSearch = (EditText) findViewById(R.id.test_edit_search);
        testResetClearlistTv = (TextView) findViewById(R.id.test_reset_clearlist_tv);
        testResetClearTv = (TextView) findViewById(R.id.test_reset_clear_tv);
        testBlueImg = (ImageView) findViewById(R.id.test_blue_img);
        testBlueState = (TextView) findViewById(R.id.test_blue_state);
        testBlueLayout = (LinearLayout) findViewById(R.id.test_blue_layout);
        testBlueRv = (RecyclerView) findViewById(R.id.test_blue_rv);
        connectLayout = (LinearLayout) findViewById(R.id.connect_layout);
        testBlueLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ScreenUtils.getScreenHeight()- DensityUtil.dp2px(185)));
        centerView = findViewById(R.id.centerView);
        blueCloseTv = (TextView) findViewById(R.id.blue_close_tv);
        fixTu2Layout = (LinearLayout) findViewById(R.id.fix_tu2_layout);
        fixTu3Layout = (LinearLayout) findViewById(R.id.fix_tu3_layout);
        fixTu1Layout = (LinearLayout) findViewById(R.id.fix_tu1_layout);
        fixTu4Layout = (LinearLayout) findViewById(R.id.fix_tu4_layout);
        fixTu1Img= (ImageView) findViewById(R.id.fix_tu1_img);
        fixTu2Img= (ImageView) findViewById(R.id.fix_tu2_img);
        fixTu3Img= (ImageView) findViewById(R.id.fix_tu3_img);
        fixTu4Img= (ImageView) findViewById(R.id.fix_tu4_img);
        fixTypeName= (TextView) findViewById(R.id.fix_typename);
        fixTu4Tv= (TextView) findViewById(R.id.fix_tu4_tv);
        sAtInstructRealizeFactory = new SAtInstructRealizeFactory();


        blueCloseTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type==2){
                    snBlueToothTool.write(DataParser.close(), "休眠中...", true, 6000);
                }else if(type==6){
                    snBlueToothTool.write(LorawanUtils.getxiumian(), "休眠中...", true, 6000);
                }

            }
        });
        inblelist();
        inonclick();

        setdialog();
    }

    private void setdialog(){

        View view1 = getLayoutInflater().inflate(R.layout.m_six_view_dialog_home, null);
        mdDialog = new MDDialog.Builder(this).setContentView(view1).setShowTitle(false).setShowButtons(false).create();
        TextView tvNext=  view1.findViewById(R.id.tv_next);
        TextView tvClose= view1.findViewById(R.id.tv_close);

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type==2){
                    testBlueState.setText("已连接");
                    testBlueImg.setImageResource(R.mipmap.test_ok);
                    testEditSearch.setText(mSensoroDevice.sn);
                    connectLayout.setVisibility(View.VISIBLE);
                    testBlueLayout.setVisibility(View.GONE);
                    fixTypeName.setText("模组");
                    mdDialog.dismiss();
                }else if(type==6){
                    testBlueState.setText("已连接");
                    testBlueImg.setImageResource(R.mipmap.test_ok);
                    testEditSearch.setText(mSensoroDevice.sn);
                    connectLayout.setVisibility(View.VISIBLE);
                    testBlueLayout.setVisibility(View.GONE);
                    fixTypeName.setText("芯片");
                    mdDialog.dismiss();
                }

            }
        });

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(type==2){
                    snBlueToothTool.write(DataParser.close(), "休眠中...", true, 6000);
                }else if(type==6){
                    snBlueToothTool.write(LorawanUtils.getxiumian(), "休眠中...", true, 6000);
                }

            }
        });


    }



    private void inonclick(){
        fixTu1Layout.setOnClickListener(this::OnBlueToothClick);
        fixTu2Layout.setOnClickListener(this::OnBlueToothClick);
        fixTu3Layout.setOnClickListener(this::OnBlueToothClick);
        fixTu4Layout.setOnClickListener(this::OnBlueToothClick);

    }


    private void inblelist(){
        SensoroBlueManager.searchBlueTooth(sensoroDeviceListener);
        testEditSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String search = s.toString().toUpperCase();
                if (!XHStringUtil.isEmpty(search, false)) {
                    snBlueViewBinder.setKeyWord(search);
                } else {
                    snBlueViewBinder.setKeyWord(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        testResetClearTv.setOnClickListener(v -> {
            testEditSearch.setText("");
        });
        testResetClearlistTv.setOnClickListener(v -> {
            SensoroBlueManager.searchBlueTooth(sensoroDeviceListener);
            deviceArrayList.clear();
            multiTypeAdapter.notifyDataSetChanged();
        });
        multiTypeAdapter = new MultiTypeAdapter();
        deviceArrayList = new ArrayList<>();
        multiTypeAdapter.register(SensoroDevice.class, snBlueViewBinder);
        multiTypeAdapter.setItems(deviceArrayList);
        testBlueRv.setAdapter(multiTypeAdapter);
        testBlueRv.setNestedScrollingEnabled(false);


    }



    protected SensoroDeviceListener sensoroDeviceListener=new SensoroDeviceListener<SensoroDevice>() {
        @Override
        public void onNewDevice(SensoroDevice sensoroDevice) {
            XLog.i("蓝牙onNewDevice" + sensoroDevice.toString());
            if (!deviceArrayList.contains(sensoroDevice)) {
                deviceArrayList.add(sensoroDevice);
            } else {
                deviceArrayList.set(deviceArrayList.indexOf(sensoroDevice), sensoroDevice);
            }

        }

        @Override
        public void onGoneDevice(SensoroDevice sensoroDevice) {
            XLog.i("蓝牙onGoneDevice" + sensoroDevice.toString());
        }

        @Override
        public void onUpdateDevices(ArrayList<SensoroDevice> arrayList) {
            XLog.i("蓝牙onUpdateDevices" + arrayList.toString());
            for (int i = 0; i < arrayList.size(); i++) {
                SensoroDevice sensoroDevice = arrayList.get(i);
                if (deviceArrayList.contains(sensoroDevice)) {
                    deviceArrayList.set(deviceArrayList.indexOf(sensoroDevice), sensoroDevice);
                } else {
                    deviceArrayList.add(sensoroDevice);
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    multiTypeAdapter.notifyDataSetChanged();
                }
            });
        }
    };

    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        snBlueToothTool = new SnBlueToothTool(this);
        registerLifeCycle(snBlueToothTool);

        snBlueViewBinder=new SNBlueViewBinder(new SNBlueViewBinder.OnSensoroDeviceListener() {
            @Override
            public void onSensoroDevice(SensoroDevice sensoroDevice) {
                snBlueToothTool.connectBlueTooth(sensoroDevice);
                mSensoroDevice=sensoroDevice;
            }

            @Override
            public void showPop(SensoroDevice sensoroDevice) {

            }
        });
        registerLifeCycle(snBlueViewBinder);
    }


    @Override
    public void onRightClick(View item) {
        super.onRightClick(item);
        ARouter.getInstance().build(K.SixFixEnvironmentActivity).navigation();

        //  showpop(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(Fix_Tu1){
        GlideUtil.display(this,fixTu1Img,R.mipmap.fix_rignt);
        }
        if(Fix_Tu2){
            fixTu2Img.setImageResource(R.mipmap.fix_rignt);
        }
        if(Fix_Tu3){
            fixTu3Img.setImageResource(R.mipmap.fix_rignt);
        }

        if(type==2){
            if(Fix_Tu1&&Fix_Tu3){

                setDefaultParams();
            }
        }else if(type==6){
            if(Fix_Tu1&&Fix_Tu3&&Fix_Tu2){

                DivceID=String.valueOf(LoginParamManager.getInstance().getLoginInfo().getAccountId())+ TimeUtils.getCurTimeMills()/1000;
                XLog.i("DivceID===="+DivceID);
                snBlueToothTool.write(LorawanUtils.getIDSetting(DivceID), "正在设置设备ID");
            }
        }
        if(Fix_Tu4){
            fixTu4Img.setImageResource(R.mipmap.fix_rignt);
        }


    }


    private void setDefaultParams() {
        String id = String.valueOf(LoginParamManager.getInstance().getLoginInfo().getAccountId())+ (TimeUtils.getCurTimeMills()/1000);
        XLog.i("id===="+id);
        byte[] input2 = DataParser.getBiaoXinxiChuShiHuaCMD(
                Long.parseLong(id),
                0,
                String.valueOf("0.001"));
        snBlueToothTool.write(input2, "设置2参数中...");
    }


    @Override
    public void onChildConnectFailed(int i) {

    }

    @Override
    public void onChildConnectSuccess() {
        try {
            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).getTwoWriteBytes(), "识别模块中...", true, 6000);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            case R.id.fix_tu1_layout:
                if(!SixUtils.EnvName.equals("")){
                    if(type==2){
                        ARouter.getInstance().build(K.SixLSYWuLianTiaoShiActivity).withParcelable("sensoroDevice",mSensoroDevice).navigation();
                    }else if(type==6){
                        ARouter.getInstance().build(K.SixChipWuLianTiaoShiActivity).withParcelable("sensoroDevice",mSensoroDevice).navigation();
                    }
                }else {
                    ToastUtils.showLong("无生产测试环境，请配置");
                }
                break;
            case R.id.fix_tu2_layout:
                if(!SixUtils.EnvName.equals("")) {
                    if(type==2){
                        ToastUtils.showLong("模组不需要配置");
                     //   ARouter.getInstance().build(K.SixLSYMoKuaiPeiZhiActivity).withParcelable("sensoroDevice", mSensoroDevice).withParcelable("environmentInfInfo", environmentInfInfo).navigation();
                    }else if(type==6){
                        ARouter.getInstance().build(K.SixChipMoKuaiPeiZhiActivity).withParcelable("sensoroDevice", mSensoroDevice).navigation();
                    }
                }else {
                    ToastUtils.showLong("无生产测试环境，请配置");
                }
                break;
            case R.id.fix_tu3_layout:
                if(!SixUtils.EnvName.equals("")) {
                    if(type==2){
                        ARouter.getInstance().build(K.SixLSYXingNengYanZhengActivity).withParcelable("sensoroDevice", mSensoroDevice).navigation();
                    }else if(type==6){
                        ARouter.getInstance().build(K.SixChipChanPinShuXingActivity).withParcelable("sensoroDevice", mSensoroDevice).navigation();
                    }
                }else {
                    ToastUtils.showLong("无生产测试环境，请配置");
                }
                break;
//            case R.id.fix_tu4_layout:
//                if(environmentInfInfo!=null) {
//                    if(type==2){
//                        ARouter.getInstance().build(K.SixLSYXingNengYanZhengActivity).withParcelable("sensoroDevice", mSensoroDevice).withParcelable("environmentInfInfo", environmentInfInfo).navigation();
//                    }else if(type==6){
//                        ARouter.getInstance().build(K.SixChipXingNengYanZhengActivity).withParcelable("sensoroDevice", mSensoroDevice).withParcelable("environmentInfInfo", environmentInfInfo).navigation();
//                    }
//                }else {
//                    ToastUtils.showLong("无生产测试环境，请配置");
//                }
//                break;



        }

    }


    @Override
    public void onChildNotify(byte[] result) {
        byte code = (byte) (result[2] ^ ((byte) 0x80));
        XLog.i("蓝牙code=" + code);
        switch (code) {
            case 0x11:
                try {
                    type = DataParser.getModuleType(result);
                    if (type == 2) {
                        sendMessageToast("2号模组");
                        blueCloseTv.setVisibility(View.VISIBLE);
                        SixUtils.SecondHashMap.putAll(DataParser.getInformationAll(result));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mdDialog.show();
                            }
                        });
                    } else if(type==6){
                        sendMessageToast("千宝通芯片");
                        blueCloseTv.setVisibility(View.GONE);
                        SixUtils.LorawanHashMap.putAll(LorawanUtils.getLorawanInfoAll(result));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mdDialog.show();
                            }
                        });
                    }else  {
                        //  sendMessageToast("识别不是2号模组，请选择正确的模组");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    sendMessageToast("识别失败");
                }
                break;
            case 0x25:
                if (result[3] == 0) {
                    sendMessageToast("休眠成功");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            SixUtils.SecondHashMap.clear();
                            SixUtils.LorawanHashMap.clear();
                            Fix_Tu1=false;
                            Fix_Tu2=false;
                            Fix_Tu3=false;
                            Fix_Tu4=false;

                            connectLayout.setVisibility(View.GONE);
                            testBlueLayout.setVisibility(View.VISIBLE);
                            snBlueToothTool.disconnect();
                            testEditSearch.setText("");
                            testResetClearlistTv.callOnClick();
                            testBlueState.setText("搜索中");
                            testBlueImg.setImageResource(R.mipmap.test_sousuo);
                            if(mdDialog.isShowing()){
                                mdDialog.dismiss();
                            }

                        }
                    });

                } else {
                    sendMessageToast("休眠失败");
                }
                break;
            case 0x02:
                if (result[3] == 0) {
                    sendMessageToast("休眠成功");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            SixUtils.SecondHashMap.clear();
                            SixUtils.LorawanHashMap.clear();
                            Fix_Tu1=false;
                            Fix_Tu2=false;
                            Fix_Tu3=false;
                            Fix_Tu4=false;
                            connectLayout.setVisibility(View.GONE);
                            testBlueLayout.setVisibility(View.VISIBLE);
                            snBlueToothTool.disconnect();
                            testEditSearch.setText("");
                            testResetClearlistTv.callOnClick();
                            testBlueState.setText("搜索中");
                            if(mdDialog.isShowing()){
                                mdDialog.dismiss();
                            }

                        }
                    });

                } else {
                    sendMessageToast("休眠失败");
                }
                break;
            case 0x21:
                if (result[3] == 0) {
                    sendMessageToast("设置成功");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            snBlueToothTool.write(DataParser.CMD_METER_INFO, "读取参数中...", true, 2000);

                        }
                    });
                } else {
                    sendMessageToast("设置失败");
                }
                break;
            case 0x22:
                if(type==2){
                    SixUtils.SecondHashMap.putAll(DataParser.getMoudleNo2(result));
                    setDuQu(SixUtils.SecondHashMap);
                }else {
                    if (result[3] == 0) {
                        try {
                            SixUtils.LorawanHashMap.putAll(LorawanUtils.getLorawanSingle(result));
                             setSingleText(SixUtils.LorawanHashMap);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (result[3] == 1) {
                        sendMessageToast("读取失败");
                    }
                }
                break;

            case 0x08:
                if (result[3] == 0) {
                    sendMessageToast("设置设备ID成功");
                    snBlueToothTool.write(LorawanUtils.getSingleReading(), "正在读取表信息");
                } else if (result[3] == 1) {
                    sendMessageToast("设置设备ID失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                }
                break;
        }
    }

    private void setSingleText(HashMap<String, String> hashMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("设备ID:  ");
        sb.append(LorawanUtils.getFuHedianrongStringByCode(hashMap.get(MapParams.设备ID)));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fixTu4Tv.setText("通过\n"+"设备ID："+hashMap.get(MapParams.设备ID));
                Fix_Tu4=true;
                if(Fix_Tu4){
                    fixTu4Img.setImageResource(R.mipmap.fix_rignt);
                }
                updata2(SixUtils.LorawanHashMap);
            }
        });
    }



    private void setDuQu(HashMap<String, String> hashMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("设备ID:  ");
        sb.append(hashMap.get(MapParams.设备ID));
        sb.append("\n脉冲常数(个/m³):  ");
        BigDecimal decimal = new BigDecimal(LouShanYunUtils.getBLReadStringByCode(Integer.parseInt(hashMap.get(MapParams.倍率))));
        sb.append(new BigDecimal(1).divide(decimal).stripTrailingZeros().toPlainString());
        sb.append("\n倍率(m³/ev):  ");
        sb.append(decimal.stripTrailingZeros().toPlainString());
        sb.append("\n脉冲数(个):  ");
        sb.append(hashMap.get(MapParams.当前脉冲读数));
        sb.append("\n初始读数(m³):  ");
        sb.append(new BigDecimal(hashMap.get(MapParams.当前脉冲读数)).multiply(decimal).stripTrailingZeros().toPlainString());
        sb.append("\n发送频率:  ");
        sb.append(LouShanYunUtils.getFSPLReadStringByCode(Long.valueOf(hashMap.get(MapParams.发送频率))));
        sb.append("\n传感信号:  ");
        sb.append(LouShanYunUtils.getCGXHReadStringByCode(Long.valueOf(hashMap.get(MapParams.传感信号))));
        sb.append("\n厂家标识:  ");
        sb.append(hashMap.get(MapParams.厂家标识));
        sb.append("\n硬件版本:  ");
        sb.append(LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.硬件版本))));
        sb.append("\n软件版本:  ");
        sb.append(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
        sb.append("\n表拆卸状态:  ");
        sb.append(hashMap.get(MapParams.表拆卸状态).equals("0") ? "无" : "有");
        sb.append("\n表强磁状态:  ");
        sb.append(hashMap.get(MapParams.表强磁状态).equals("0") ? "正常" : "强磁");
        sb.append("\n表流向状态:  ");
        sb.append(hashMap.get(MapParams.表流向状态).equals("0") ? "正流" : "倒流");
        sb.append("\n表电池状态:  ");
        sb.append(hashMap.get(MapParams.表电池状态).equals("0") ? "正常" : "欠压");
        runOnUiThread(() -> {
            fixTu4Tv.setText("通过\n"+"设备ID："+hashMap.get(MapParams.设备ID));
            Fix_Tu4=true;
            if(Fix_Tu4){
                fixTu4Img.setImageResource(R.mipmap.fix_rignt);
            }
            updata1(SixUtils.SecondHashMap);
        });
    }


    private void updata1(HashMap<String, String> hashMap) {


        TestUpData testUpData = new TestUpData();
        testUpData.setTest_time(TimeUtils.getCurTimeMills());
      //  testUpData.setManufacturerId(manuInfo.getDatas().get(0).getAccountId());

        testUpData.setLoginId(LoginParamManager.getInstance().getLoginInfo().getLoginId());
        testUpData.setAuthId(LoginParamManager.getInstance().getLoginInfo().getId());
        testUpData.setIsStandard("1");
        testUpData.setCollectScene("2");
        testUpData.setProductForm("3");
        testUpData.setDataSource("0");
        testUpData.setEnvId(SixUtils.DatasBean.getId());

//        testUpData.setGoodsName(goodsInfo.getDatas().get(0).getGoodsName());
//        testUpData.setModel(goodsInfo.getDatas().get(0).getGoodsModel());
//        testUpData.setCaliber(goodsInfo.getDatas().get(0).getCaliber());
        testUpData.setChipNumber("1" + hashMap.get(MapParams.设备ID) + mSensoroDevice.sn);

        testUpData.setLotType("LORAWAN_S");
        testUpData.setJobNumber(LoginParamManager.getInstance().getLoginInfo().getJobNumber());

        testUpData.setLotSn(mSensoroDevice.sn);
        testUpData.setDeviceId(hashMap.get(MapParams.设备ID));
        testUpData.setSubBusinessName(LoginParamManager.getInstance().getLoginInfo().getBusinessName());


        testUpData.setBatteryStatus(LorawanUtils.getQianYaStateStringByCode(hashMap.get(MapParams.欠压状态)));
        testUpData.setSpreadFactor(hashMap.get(MapParams.扩频因子));
        testUpData.setChannelParam(hashMap.get(MapParams.信道参数));
        testUpData.setSendPower(hashMap.get(MapParams.发送功率));
        testUpData.setFactoryDication(hashMap.get(MapParams.正计数));
     //   testUpData.setMagnification(decimal.stripTrailingZeros().toPlainString());
        testUpData.setSoftwareVersion(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
        testUpData.setHardwareVersion(LouShanYunUtils.getHardWareVersion(Integer.valueOf(hashMap.get(MapParams.硬件版本))));
        testUpData.setSenseSignal(hashMap.get(MapParams.传感信号));
        testUpData.setSendFrequency(LouShanYunUtils.getFSPLReadStringByCode(Long.valueOf(hashMap.get(MapParams.发送频率))));
        testUpData.setSignalType("数字信号");
        //  testUpData.setBattery();
        testUpData.setBluetooth("有");
        String[] strings =  hashMap.get(SAtInstructParams.sAtInstructRSSI).split(",");
        int rssi = Integer.valueOf(strings[0]);
        int snr = Integer.valueOf(strings[1]);

        testUpData.setSignalIntensity(rssi+"");
        testUpData.setSignalRatio(snr+"");
//        testUpData.setComplexCapacity(goodsInfo.getDatas().get(0).getCompositeCapacitor());
//
//        testUpData.setWatchcase(goodsInfo.getDatas().get(0).getWatchCase());
//        testUpData.setRangeRatio(goodsInfo.getDatas().get(0).getRangeRatio());
//        testUpData.setValve(goodsInfo.getDatas().get(0).getValve());
        testUpData.setDelayParameter(Integer.parseInt(hashMap.get(SAtInstructParams.sAtInstructRxDelay)));
        pushEvent(SixCode.MSixSaveFirst, new Gson().toJson(testUpData));
        XLog.i("testUpData" + new Gson().toJson(testUpData));
    }



    private void updata2(HashMap<String, String> hashMap) {

        TestUpData testUpData = new TestUpData();
        testUpData.setTest_time(TimeUtils.getCurTimeMills());

        testUpData.setLoginId(LoginParamManager.getInstance().getLoginInfo().getLoginId());
        testUpData.setAuthId(LoginParamManager.getInstance().getLoginInfo().getId());
        testUpData.setIsStandard("1");
        testUpData.setCollectScene("2");
        testUpData.setProductForm("3");
        testUpData.setDataSource("0");
        testUpData.setEnvId(SixUtils.DatasBean.getId());
//        testUpData.setGoodsName(goodsInfo.getDatas().get(0).getGoodsName());
//        testUpData.setModel(goodsInfo.getDatas().get(0).getGoodsModel());
//        testUpData.setCaliber(goodsInfo.getDatas().get(0).getCaliber());
        testUpData.setChipNumber("2" + hashMap.get(MapParams.设备ID) + mSensoroDevice.sn);

        testUpData.setLotType("LORAWAN_S");
        testUpData.setJobNumber(LoginParamManager.getInstance().getLoginInfo().getJobNumber());
        testUpData.setLotSn(mSensoroDevice.sn);
        testUpData.setDeviceId(hashMap.get(MapParams.设备ID));

       testUpData.setSubBusinessName(LoginParamManager.getInstance().getLoginInfo().getBusinessName());

        String[] strings =  hashMap.get(SAtInstructParams.sAtInstructRSSI).split(",");
        int rssi = Integer.valueOf(strings[0]);
        int snr = Integer.valueOf(strings[1]);

        testUpData.setSignalIntensity(rssi+"");
        testUpData.setSignalRatio(snr+"");

        testUpData.setSpreadFactor(hashMap.get(MapParams.扩频因子));
        testUpData.setChannelParam(hashMap.get(MapParams.信道参数));
        testUpData.setSendPower(hashMap.get(MapParams.发送功率));

        testUpData.setSoftwareVersion(hashMap.get(MapParams.固件版本号));
        testUpData.setHardwareVersion("1.07");
        testUpData.setFeedback("是");
        testUpData.setFeedbackNum(hashMap.get(MapParams.最大发送次数));
        testUpData.setSenseSignal(LouShanYunUtils.getCGXHReadStringByCode(Long.valueOf(hashMap.get(MapParams.传感信号))));
        testUpData.setSendFrequency(LouShanYunUtils.getFSPLReadStringByCode(Long.valueOf(hashMap.get(MapParams.发送频率))));
        testUpData.setSignalType("数字信号");
        //  testUpData.setBattery();
//        testUpData.setComplexCapacity(goodsInfo.getDatas().get(0).getCompositeCapacitor());
        testUpData.setBluetooth("有");


        pushEvent(SixCode.MSixSaveFirst, new Gson().toJson(testUpData));
        XLog.i("testUpData" + new Gson().toJson(testUpData));

    }



    @Override
    public void onChildWriteSuccess() {

    }

    @Override
    public void onChildWriteFailure(int i) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                testBlueState.setText("已断开");
                testBlueImg.setImageResource(R.mipmap.test_error);
                ToastUtils.showLong("连接失败，请重新连接");
            }
        });

    }

    @Override
    protected void initEventListener() {


        registerEventRunner(SixCode.MSixSaveFirst, new SaveFirstRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
     if(code==SixCode.MSixSaveFirst){
            if(event.isSuccess()){
            ResultJson resultJson  = (ResultJson) event.getReturnParamAtIndex(0);
                ToastUtils.showLong(resultJson.getMessage());
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SixUtils.SecondHashMap.clear();
        SixUtils.LorawanHashMap.clear();
        Fix_Tu1=false;
        Fix_Tu2=false;
        Fix_Tu3=false;
        Fix_Tu4=false;
    }
}
