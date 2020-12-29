package wu.loushanyun.com.sixapp.v.activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.elvishew.xlog.XLog;
import com.gpsdk.demo.DeviceConnFactoryManager;
import com.sensoro.sensor.kit.callback.SensoroDeviceListener;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.print.PrintTool;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LorawanUtils;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.init.SensoroBlueManager;
import com.wu.loushanyun.basemvp.init.SnBlueToothListener;
import com.wu.loushanyun.basemvp.init.SnBlueToothTool;
import com.wu.loushanyun.basemvp.m.SaveDataMeter;
import com.wu.loushanyun.basemvp.m.sat.SAtInstructRealizeFactory;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;
import java.util.ArrayList;
import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.glide.GlideUtil;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.FromHtmlUtil;
import met.hx.com.librarybase.some_utils.KeyboardUtils;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.views.CustomPopWindow;
import met.hx.com.librarybase.views.dialog.MDDialog;
import wu.loushanyun.com.sixapp.R;
import wu.loushanyun.com.sixapp.init.LoginParamManager;
import wu.loushanyun.com.sixapp.init.SixCode;
import wu.loushanyun.com.sixapp.init.SixUtils;
import wu.loushanyun.com.sixapp.m.EnvironmentInfInfo;
import wu.loushanyun.com.sixapp.m.EnvironmentNameInfo;
import wu.loushanyun.com.sixapp.m.GetModelInfo;
import wu.loushanyun.com.sixapp.m.GoodsM;
import wu.loushanyun.com.sixapp.m.ProInfo;
import wu.loushanyun.com.sixapp.m.SNMeter;
import wu.loushanyun.com.sixapp.m.WorkInfo;
import wu.loushanyun.com.sixapp.p.adapter.SNBlueViewBinder;
import wu.loushanyun.com.sixapp.p.runner.EnvironmentInfRunner;
import wu.loushanyun.com.sixapp.p.runner.EnvironmentNameRunner;
import wu.loushanyun.com.sixapp.p.runner.ProInfRunner;
import wu.loushanyun.com.sixapp.p.runner.WorkInfRunner;


@Route(path = K.SixParameterActivity)
public class SixParameterActivity extends BaseNoPresenterActivity implements SnBlueToothListener {
    private TextView dt2tv;
    private ImageView imgCiLun;
    private PopupWindow myPop;
    private RecyclerView paramBlueRv;
    private MultiTypeAdapter multiTypeAdapter;
    private SNBlueViewBinder snBlueViewBinder;
    private ArrayList<SensoroDevice> deviceArrayList;

    private TextView xinghaoTv;
    private RadioGroup kouJingradiogroup;
    private RadioGroup rangeRatioradiogroup;
    private RadioGroup beiLvradiogroup;
    private TextView toParamTv;



    private TextView paramBlueOpen;
    private ScrollView paramScroll;
    private TextView paramType;
    private TextView paramResetClearlistTv;
    private TextView paramResetClearTv;
    private EditText paramEditSearch;
    protected SnBlueToothTool snBlueToothTool;
    private TextView paramWuxianTv;
  //  private EnvironmentInfInfo environmentInfInfo;

    private GetModelInfo.DatasBean datasBean;

    private SensoroDevice mSensoroDevice;

    private SAtInstructRealizeFactory sAtInstructRealizeFactory;
    private int type;
    private MDDialog mdDialog;
    private GoodsM goodsM=new GoodsM();

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mTitleText = LoginParamManager.getInstance().getLoginInfo().getBusinessName();
        ba.mTitleBackgroundColorId = R.color.blue_bg;
        ba.mHasRightView = true;
        ba.mActivityLayoutId = R.layout.m_six_activity_parameter;
    }



    @Override
    protected void initView() {

        dt2tv = (TextView) findViewById(R.id.dt2_tv);
        imgCiLun = (ImageView) findViewById(R.id.img_cilun);
        paramType = (TextView) findViewById(R.id.param_type);
        paramBlueRv = (RecyclerView) findViewById(R.id.param_blue_rv);
        paramResetClearlistTv = (TextView) findViewById(R.id.param_reset_clearlist_tv);
        paramResetClearTv = (TextView) findViewById(R.id.param_reset_clear_tv);
        paramEditSearch = (EditText) findViewById(R.id.param_edit_search);
        paramWuxianTv= (TextView) findViewById(R.id.param_wuxian_tv);
        paramScroll= (ScrollView) findViewById(R.id.param_scroll);
        paramBlueOpen= (TextView) findViewById(R.id.param_blue_open);
        xinghaoTv = (TextView) findViewById(R.id.xinghao_tv);
        kouJingradiogroup = (RadioGroup) findViewById(R.id.koujing_radiogroup);
        toParamTv= (TextView) findViewById(R.id.toparam_tv);

        rangeRatioradiogroup = (RadioGroup) findViewById(R.id.rangeRatio_radiogroup);

        beiLvradiogroup = (RadioGroup) findViewById(R.id.beilv_radiogroup);


        xinghaoTv.setText("型号： "+getIntent().getStringExtra("type"));

        datasBean=getIntent().getParcelableExtra("GetModelInfo");
        sAtInstructRealizeFactory = new SAtInstructRealizeFactory();



        GlideUtil.displayGif(this, imgCiLun, R.drawable.souxin, R.mipmap.param_header);


        toParamTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(SixUtils.DatasBean==null){
                    ToastUtils.showLong("无生产测试环境，请配置");
                }else {
                    ARouter.getInstance().build(K.SixWuxianParamActivity).withAction(getIntent().getAction()).navigation();
                }

            }
        });

//        if (getIntent().getAction().equals("chip")) {
//            paramType.setText("LORAWAN 芯片植入企业");
//        } else {
//            paramType.setText("LORAWAN 模组植入企业");
//        }

        paramType.setText("授权手机号："+LoginParamManager.getInstance().getLoginInfo().getTel()+"\n\n工号:"+LoginParamManager.getInstance().getLoginInfo().getJobNumber());
        if(SixUtils.DatasBean!=null){
            paramWuxianTv.setText("无线环境：\n"+SixUtils.DatasBean.getEnvName());
        }

        inblelist();
        setdialog();
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    multiTypeAdapter.notifyDataSetChanged();
                    paramScroll.post(new Runnable() {
                        @Override
                        public void run() {
                            //滑动到底部
                            paramScroll.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });


                }
            });
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

    private void inblelist(){
        SensoroBlueManager.searchBlueTooth(sensoroDeviceListener);
        paramEditSearch.addTextChangedListener(new TextWatcher() {
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

        paramResetClearTv.setOnClickListener(v -> {
            paramEditSearch.setText("");
        });
        paramResetClearlistTv.setOnClickListener(v -> {
            SensoroBlueManager.searchBlueTooth(sensoroDeviceListener);
            deviceArrayList.clear();
            multiTypeAdapter.notifyDataSetChanged();
        });
        multiTypeAdapter = new MultiTypeAdapter();
        deviceArrayList = new ArrayList<>();
        multiTypeAdapter.register(SensoroDevice.class, snBlueViewBinder);
        multiTypeAdapter.setItems(deviceArrayList);
        paramBlueRv.setAdapter(multiTypeAdapter);
        paramBlueRv.setNestedScrollingEnabled(false);

    }


    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        snBlueToothTool = new SnBlueToothTool(this);
        registerLifeCycle(snBlueToothTool);

        snBlueViewBinder=new SNBlueViewBinder(new SNBlueViewBinder.OnSensoroDeviceListener() {
            @Override
            public void onSensoroDevice(SensoroDevice sensoroDevice) {


                RadioButton kouJingradioButton= (RadioButton) findViewById(kouJingradiogroup.getCheckedRadioButtonId());
                RadioButton rangeRatioradioButton= (RadioButton) findViewById(rangeRatioradiogroup.getCheckedRadioButtonId());
                RadioButton beiLvradioButton= (RadioButton) findViewById(beiLvradiogroup.getCheckedRadioButtonId());
                if(beiLvradioButton!=null){
                    goodsM.setBeilv(beiLvradioButton.getText().toString());
                }else {
                    ToastUtils.showLong("请选择倍率");
                    return;
                }
                if(kouJingradioButton!=null){
                    goodsM.setKoujing(kouJingradioButton.getText().toString());
                }else {
                    ToastUtils.showLong("请选择口径");
                    return;
                }
                if(rangeRatioradioButton!=null){
                    goodsM.setRangeRatio(rangeRatioradioButton.getText().toString());
                }else {
                    ToastUtils.showLong("请选择量程比");
                    return;
                }

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
    protected void onRestart() {
        super.onRestart();

    }


    private void setdialog(){

        View view1 = getLayoutInflater().inflate(R.layout.m_six_view_dialog_home, null);
        mdDialog = new MDDialog.Builder(this).setContentView(view1).setShowTitle(false).setShowButtons(false).create();
        TextView tvNext=  view1.findViewById(R.id.tv_next);
        TextView tvClose= view1.findViewById(R.id.tv_close);

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (type == 2) {
                    sendMessageToast("2号模组");
                    ARouter.getInstance().build(K.SixBlueContentActivity).withParcelable("sensoroDevice",mSensoroDevice).withParcelable("GoodsM",goodsM).withParcelable("GetModelInfo",datasBean).navigation();
                    mdDialog.dismiss();
                } else if(type==6){
                    sendMessageToast("千宝通芯片");
                    ARouter.getInstance().build(K.SixChipQianContentActivity).withParcelable("sensoroDevice",mSensoroDevice).withParcelable("GoodsM",goodsM).withParcelable("GetModelInfo",datasBean).navigation();
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




    @Override
    public void onChildNotify(byte[] result) {
        byte code = (byte) (result[2] ^ ((byte) 0x80));
        XLog.i("蓝牙code=" + code);
        switch (code) {
            case 0x11:
                try {
                    type = DataParser.getModuleType(result);
                    if (type == 2) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                           sendMessageToast("2号模组");
                                mdDialog.show();

                            }
                        });

                    } else if(type==6){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                sendMessageToast("千宝通芯片");
                                mdDialog.show();
                            }
                        });

                    }else  {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                AlertDialog.Builder builder = new AlertDialog.Builder(SixParameterActivity.this);
//                                builder.setTitle("蓝牙识别").setMessage("此蓝牙不是二号模组")
//                                        .setPositiveButton("断开连接", new DialogInterface.OnClickListener() {
//                                            @Override
//                                            public void onClick(DialogInterface dialog, int which) {
//                                                snBlueToothTool.disconnect();
//                                                dialog.dismiss();
//                                            }
//                                        }).show();
//                            }
//                        });

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
                            snBlueToothTool.disconnect();
                            if(mdDialog.isShowing()){
                                mdDialog.dismiss();
                            }
                            paramResetClearlistTv.callOnClick();
                        }
                    });

                } else {
                    sendMessageToast("休眠失败");
                }
                break;
        }
    }



    @Override
    public void onChildConnectFailed(int i) {
        ToastUtils.showLong("连接失败，请重新连接");
    }

    @Override
    public void onChildConnectSuccess() {
        try {
            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).getTwoWriteBytes(), "识别模块中...", true, 6000);
        } catch (Exception e) {
            e.printStackTrace();
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
}
