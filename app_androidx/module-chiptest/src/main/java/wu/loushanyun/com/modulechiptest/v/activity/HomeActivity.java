package wu.loushanyun.com.modulechiptest.v.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.elvishew.xlog.XLog;
import com.gpsdk.demo.DeviceConnFactoryManager;
import com.gpsdk.demo.Utils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.bugly.crashreport.CrashReport;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.print.PrintTool;
import com.wu.loushanyun.base.url.URLUtils;
import com.wu.loushanyun.base.url.UrlController;
import com.wu.loushanyun.basemvp.m.HomeDataListener;

import org.json.JSONObject;
import org.lzh.framework.updatepluginlib.UpdateBuilder;
import org.lzh.framework.updatepluginlib.UpdateConfig;
import org.lzh.framework.updatepluginlib.base.UpdateParser;
import org.lzh.framework.updatepluginlib.model.Update;

import java.util.ArrayList;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.baseevent.Event;
import met.hx.com.base.util.DefaultChecker;
import met.hx.com.base.util.NoDoubleClickUtils;
import met.hx.com.base.util.OutOfTimeJumpManager;
import met.hx.com.base.util.Strategy;
import met.hx.com.librarybase.some_utils.AppUtils;
import met.hx.com.librarybase.some_utils.SizeUtils;
import met.hx.com.librarybase.some_utils.ToastManager;
import met.hx.com.librarybase.some_utils.XActivityUtils;
import met.hx.com.librarybase.views.MyRadioGroup;
import wu.loushanyun.com.libraryfive.BuildConfig;
import wu.loushanyun.com.modulechiptest.R;
import wu.loushanyun.com.modulechiptest.init.ChipCode;
import wu.loushanyun.com.modulechiptest.init.LoginParamManager;
import wu.loushanyun.com.modulechiptest.m.HomeData;
import wu.loushanyun.com.modulechiptest.p.adapter.HomeDataViewBinder;

@Route(path = K.ChipHomeActivity)
public class HomeActivity extends BaseNoPresenterActivity {


    private TextView textPrint;
    private RecyclerView recyclerview;
    private HomeDataViewBinder homeDataViewBinder;
    private PrintTool printTool;
    private ArrayList<HomeData> arrayList;
    private PopupWindow myPop;


    @Override
    protected void initLifeCycle() {
        printTool = new PrintTool(1, new PrintTool.PrintListener() {
            @Override
            public void onUsbPermission(Intent intent) {
                synchronized (this) {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            System.out.println("permission ok for device " + device);
                        }
                    } else {
                        System.out.println("permission denied for device " + device);
                    }
                }
            }

            @Override
            public void onUsbDeviceDetached(Intent intent) {
            }

            @Override
            public void onAclDisconnected(Intent intent) {
            }

            @Override
            public void onConnectionState(Intent intent) {
                int state = intent.getIntExtra(DeviceConnFactoryManager.STATE, -1);
                int deviceId = intent.getIntExtra(DeviceConnFactoryManager.DEVICE_ID, -1);
                switch (state) {
                    case DeviceConnFactoryManager.CONN_STATE_DISCONNECT:
                        textPrint.setText("打印机未连接");
                        break;
                    case DeviceConnFactoryManager.CONN_STATE_CONNECTING:
                        sendMessageToast(getString(com.wu.loushanyun.R.string.str_conn_state_connecting));
                        break;
                    case DeviceConnFactoryManager.CONN_STATE_CONNECTED:
                        sendMessageToast(getString(com.wu.loushanyun.R.string.str_conn_state_connected) + "\n" + printTool.getConnDeviceInfo());
                        textPrint.setText("打印机已连接");
                        break;
                    case DeviceConnFactoryManager.CONN_STATE_FAILED:
                        sendMessageToast(getString(com.wu.loushanyun.R.string.str_conn_fail));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onConnectionStateDisconnect(Intent intent) {
            }

            @Override
            public void onConnectionStateConnecting(Intent intent) {
            }

            @Override
            public void onConnectionStateConnected(Intent intent) {
            }

            @Override
            public void onConnectionStateFailed(Intent intent) {
            }

            @Override
            public void onQueryPrinterState(Intent intent) {
            }
        });
        registerLifeCycle(printTool);

    }

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_chip_activity_home;
        ba.mTitleText = "模组测试";
        if(LoginParamManager.getInstance().getLoginInfo().getData().getMloginHomeType()==1){
            ba.mTitleRightImageIcon = R.drawable.ic_more_vert_black_24dp;
            ba.mTitleLeftRightWidth = 80;
        }


    }


    @Override
    public void onRightClick(View item) {
        super.onRightClick(item);
        showPicSelect(item);
    }


    @Override
    protected void initView() {
        OutOfTimeJumpManager.getInstance().setShowDialog(false);

        textPrint = (TextView) findViewById(R.id.text_print);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);

        homeDataViewBinder = new HomeDataViewBinder();
        arrayList = new ArrayList<>();

        //设置授权类型
        String[] types = LoginParamManager.getInstance().getLoginInfo().getData().getMloginType().split(",");
        for (int i = 0; i < types.length; i++) {
            if (types[i] .equals("1") ) {
                setType_1();
            }
//            if (types[i] .equals( "2")) {
//                setType_2();
//            }
//            if (types[i] .equals("3") ) {
//                setType_3();
//            }
        }

        MultiTypeAdapter multiTypeAdapter = new MultiTypeAdapter(arrayList);
        multiTypeAdapter.register(HomeData.class, homeDataViewBinder);
        recyclerview.setAdapter(multiTypeAdapter);

        initTestClick();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(myPop!=null){
            myPop.dismiss();
        }
    }

    private void initTestClick() {

    }





    /**
     * 照片选择器
     */
    @SuppressLint("InflateParams")
    private void showPicSelect(View item) {
        View view = LayoutInflater.from(this).inflate(R.layout.m_chip_view_morepop, null, false);
        TextView pop_out=view.findViewById(R.id.pop_out);
        TextView pop_check=view.findViewById(R.id.pop_check);
        TextView pop_produce=view.findViewById(R.id.pop_produce);
        TextView pop_test=view.findViewById(R.id.pop_test);
        myPop = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        myPop.setBackgroundDrawable(new ColorDrawable());
        myPop.setOutsideTouchable(true);
        myPop.showAsDropDown(item, (int) item.getX(), (int) item.getY());


        String[] types = LoginParamManager.getInstance().getLoginInfo().getData().getMloginType().split(",");
        for (int i = 0; i < types.length; i++) {
            if (types[i] .equals("1") ) {
                pop_test.setVisibility(View.VISIBLE);//显示测试类型
            }
            if (types[i] .equals( "2")) {
                pop_produce.setVisibility(View.VISIBLE);//显示生产类型
            }
            if (types[i] .equals("3") ) {
                pop_check.setVisibility(View.VISIBLE);//显示质检类型
            }
        }
        pop_test.setVisibility(View.GONE);//



        pop_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build(K.LoginActivityPhone).navigation();

                finish();
            }
        });
        pop_produce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build(K.ProduceHomeActivity).navigation();
            }
        });
        pop_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build(K.CheckActivity).navigation();
            }
        });

        pop_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ARouter.getInstance().build(K.ChipHomeActivity).navigation();
            }
        });
    }

    /**
     * 测试人员
     */
    private void setType_1() {
        arrayList.add(new HomeData("2号模组导出SN号和Token", K.SnPrintListActivity1));
        arrayList.add(new HomeData("开发人员工具", K.ToolTestActivity));
        arrayList.add(new HomeData("开发人员工具(验证AT指令)", K.ATTestActivity));
        arrayList.add(new HomeData("开发人员工具(设置AT指令)", K.ATSettingActivity));
        arrayList.add(new HomeData("开发人员工具(验证TTL指令)", K.TTLTestActivity));
        arrayList.add(new HomeData("强制发送10条(用于测试环境信号质量)", K.EquipmentListActivity));
        arrayList.add(new HomeData("强制发送10条(用于测试环境信号质量,适用于1.06版本的集中器)", K.EquipmentListActivity2));
        arrayList.add(new HomeData("测试模组自动上传功能(带打印标签功能)", K.SecondUploadInfoActivity));
        arrayList.add(new HomeData("1号模组物联网集中器测试（1.06以下）", K.FirstTestActivity));
//        arrayList.add(new HomeData("1号模组物联网集中器生产（1.06以下）", K.JiZhongQiProductTestActivity));
        arrayList.add(new HomeData("1号模组物联网集中器测试（1.07以上）", K.FirstTestActivity1));
        arrayList.add(new HomeData("1号模组物联网集中器生产（1.07以上）", new HomeDataListener() {
            @Override
            public void onHomeData() {
                ARouter.getInstance().build(K.FirstTestActivity1).withInt("jumpType", ChipCode.TypeFromFactory).navigation();
            }
        }));
        arrayList.add(new HomeData("2号模组测试(1.07以上)", K.NewSecondTestActivity1));
        arrayList.add(new HomeData("3号模组测试（老式抄表盒子）", K.MCchushihuaActivity));
        arrayList.add(new HomeData("3号模组测试（新版出厂配置抄表盒子）", K.ThirdTestActivity_0X60));
//        arrayList.add(new HomeData("3号模组测试（初始化抄表盒子）", K.MCchushihuaActivity));
        arrayList.add(new HomeData("3号模组测试（新版出厂配置抄表盒子）", K.ThirdTestNewActivity));
        arrayList.add(new HomeData("4号模组模拟信号测试", K.FourTestSimulationDataActivity));
        arrayList.add(new HomeData("LORAWAN芯片基站民用信号测试", K.LorawanActivity));

    }






}
