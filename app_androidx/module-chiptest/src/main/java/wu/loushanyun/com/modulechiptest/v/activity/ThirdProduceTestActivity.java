package wu.loushanyun.com.modulechiptest.v.activity;

import android.content.DialogInterface;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundTextView;
import com.google.gson.Gson;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.init.LouShanYunCode;
import com.wu.loushanyun.basemvp.m.CreateUserIdInfo;
import com.wu.loushanyun.basemvp.m.st3.ST3ByteTool;
import com.wu.loushanyun.basemvp.m.st3.ST3Params;
import com.wu.loushanyun.basemvp.v.activity.BaseSnBlueToothActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import me.zhouzhuo.zzexcelcreator.ColourUtil;
import me.zhouzhuo.zzexcelcreator.ZzExcelCreator;
import me.zhouzhuo.zzexcelcreator.ZzFormatCreator;
import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.base.rx.RxSchedulers;
import met.hx.com.base.base.share.FileUtil;
import met.hx.com.base.base.share.Share2;
import met.hx.com.base.base.share.ShareContentType;
import met.hx.com.base.baseconfig.PathConfig;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.TimeUtils;
import wu.loushanyun.com.modulechiptest.R;
import wu.loushanyun.com.modulechiptest.init.ChipCode;
import wu.loushanyun.com.modulechiptest.init.LoginParamManager;
import wu.loushanyun.com.modulechiptest.m.FactoryProductionInfo;
import wu.loushanyun.com.modulechiptest.m.ResultJson;
import wu.loushanyun.com.modulechiptest.m.ThirdProduceInfo;
import wu.loushanyun.com.modulechiptest.p.adapter.ThirdProduceViewBinder;

import com.wu.loushanyun.basemvp.p.runner.CreateUserIdRunner;

import wu.loushanyun.com.modulechiptest.p.runner.CheckProduceRunner;
import wu.loushanyun.com.modulechiptest.p.runner.FactoryProductionRunner;
import wu.loushanyun.com.modulechiptest.p.util.SnPrintUtil;

@Route(path = K.ThirdProdueTestActivity)
public class ThirdProduceTestActivity extends BaseSnBlueToothActivity {

    private SensoroDevice sensoroDevice;
    private ST3ByteTool st3ByteTool;
    private boolean canSet = false;
    private boolean setSuccess = false;


    private ArrayList<FactoryProductionInfo> factoryProductionInfos;
    private MultiTypeAdapter multiTypeAdapter;
    private ArrayList<ThirdProduceInfo> thirdProduceInfos;
    private ThirdProduceViewBinder thirdProduceViewBinder;

    private boolean deviceGenghuanZhuangtai;//验表失败，返回true
    private int biaoHao;//验表失败，返回true
    private String resultBiaoHao;//新设备的表号
    private String resultID;//新设备的id

    private TextView textDangqianSn;
    private RoundTextView bluetoothConn;
    private RoundTextView bluetoothDisconn;
    private RoundTextView textSum;
    private RoundTextView bluetoothYanbiao;
    private RecyclerView recycleview;
    private String UserId = "";
    private int num;
    private HashMap<String, String> hashMapProduct;

    private String excelFilePath;
    private String excelFileName;

    @Override
    protected int onChildLayout() {
        return R.layout.m_chip_activity_threeproduce;
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        super.onInitAttribute(ba);
        ba.mTitleText = "3号模组生产并导出SN号";
    }

    @Override
    protected void onClickBlueListListener(SensoroDevice sensoroDevice) {
        super.onClickBlueListListener(sensoroDevice);
        this.sensoroDevice = sensoroDevice;
    }

    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        snBlueToothTool.setWriteTimeDelay(300);
        thirdProduceViewBinder = new ThirdProduceViewBinder(new ThirdProduceViewBinder.ThirdProduceListener() {
            @Override
            public void onUpData(int position) {
                sendMessageToast("上传");

                pushEvent(ChipCode.MChipModuleFactoryProduction, new Gson().toJson(factoryProductionInfos.get(position)), position + 1,
                        LoginParamManager.getInstance().getLoginInfo().getData().getMloginPhoneNumber());
            }

            @Override
            public void onSend(int position) {
                sendMessageToast("设置");
                pushEvent(LouShanYunCode.MChipCreateUserId);

            }
        });
        registerLifeCycle(thirdProduceViewBinder);
        registerLifeCycle(snBlueToothTool);
    }

    @Override
    protected void initView() {
        super.initView();
        textDangqianSn = (TextView) findViewById(R.id.text_dangqian_sn);
        bluetoothConn = (RoundTextView) findViewById(R.id.bluetooth_conn);
        bluetoothDisconn = (RoundTextView) findViewById(R.id.bluetooth_disconn);
        recycleview = (RecyclerView) findViewById(R.id.recycleview);
        textSum = (RoundTextView) findViewById(R.id.text_sum);
        bluetoothYanbiao = (RoundTextView) findViewById(R.id.bluetooth_yanbiao);
        multiTypeAdapter = new MultiTypeAdapter();
        thirdProduceInfos = new ArrayList<>();
        factoryProductionInfos = new ArrayList<>();
        multiTypeAdapter.register(ThirdProduceInfo.class, thirdProduceViewBinder);
        multiTypeAdapter.setItems(thirdProduceInfos);
        recycleview.setAdapter(multiTypeAdapter);
        hashMapProduct = new HashMap<>();

        bluetoothConn.setOnClickListener(v -> {
            if (textDangqianSn.getText().toString().contains("当前连接的SN设备")) {
                sendMessageToast("当前已连接设备");
                return;
            }
            snBlueToothTool.connectBlueTooth(sensoroDevice);
        });

        bluetoothDisconn.setOnClickListener(v -> {
            snBlueToothTool.disconnect();
            textDangqianSn.setText("当前未连接SN设备");
        });

        textSum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createExcel();
            }
        });

        st3ByteTool = new ST3ByteTool();
        initTestClick();
    }

    @Override
    public void onChildConnectFailed(int i) {

    }

    @Override
    public void onChildConnectSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                sendMessageToast("连接成功");
                textDangqianSn.setText("当前连接的SN设备:" + sensoroDevice.getSerialNumber().toUpperCase());
            }
        });

    }

    private void createExcel() {
        compositeDisposable.add(Flowable.create(
                (FlowableOnSubscribe<String>) emitter ->
                {
                    LoadingDialogUtil.showByEvent("正在检查", loadingTag);
                    boolean canSave = true;
                    String tishi = "";

                    LoadingDialogUtil.showByEvent("正在导出", loadingTag);

                    excelFilePath = PathConfig.getROOT();
                    excelFileName = TimeUtils.getCurTimeString();
                    ZzExcelCreator zzExcelCreator = ZzExcelCreator
                            .getInstance()
                            .createExcel(excelFilePath, excelFileName)
                            .createSheet("SN号+编号");
                    int col = 1;
                    int row = 2;
                    WritableCellFormat cellFormat = ZzFormatCreator
                            .getInstance()
                            .createCellFont(WritableFont.ARIAL)
                            .setAlignment(Alignment.CENTRE, VerticalAlignment.CENTRE)
                            .setFontSize(12)
                            .setFontColor(Colour.BLACK)
                            .setBackgroundColor(ColourUtil.getCustomColor1("#D3D3D3"))
                            .setBorder(Border.ALL, BorderLineStyle.THIN, ColourUtil.getCustomColor2("#dddddd")).getCellFormat();

                    zzExcelCreator.fillContent(1, 1, "编号", cellFormat);
                    zzExcelCreator.fillContent(2, 1, "设备ID号", cellFormat);

                    for (int i = 0; i < thirdProduceInfos.size(); i++) {
                        zzExcelCreator.fillContent(col, row, thirdProduceInfos.get(i).getNum() + "", cellFormat);
                        zzExcelCreator.fillContent(col + 1, row, thirdProduceInfos.get(i).getUserId() + "", cellFormat);
                        row++;
                    }
                    WritableCellFormat cellFormat1 = ZzFormatCreator
                            .getInstance()
                            .createCellFont(WritableFont.ARIAL)
                            .setAlignment(Alignment.CENTRE, VerticalAlignment.CENTRE)
                            .setFontSize(12)
                            .setFontColor(Colour.RED)
                            .setBackgroundColor(ColourUtil.getCustomColor1("#D3D3D3"))
                            .setBorder(Border.ALL, BorderLineStyle.THIN, ColourUtil.getCustomColor2("#dddddd")).getCellFormat();
                    row = 2;
                    for (int i = 0; i < thirdProduceInfos.size(); i++) {
                        zzExcelCreator.fillContent(col + 7, row, thirdProduceInfos.get(i).getTishi() + "", cellFormat1);
                        row++;
                    }
                    zzExcelCreator.close();
                    LoadingDialogUtil.dismissByEvent(loadingTag);
                    emitter.onNext(excelFilePath + "/" + excelFileName);
                }
                , BackpressureStrategy.ERROR)
                .compose(RxSchedulers.io_main())
                .subscribe(s -> {
                    new AlertDialog.Builder(this)
                            .setTitle("提示！！！")
                            .setMessage("导出成功请在" + s + "中查看")
                            .setPositiveButton("去分享", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    File file = new File(s + ".xls");
                                    Uri uri = FileProvider.getUriForFile(ThirdProduceTestActivity.this, "wu.loushanyun.com.modulechiptest.fileprovider", file);
                                    new Share2.Builder(ThirdProduceTestActivity.this)
                                            .setContentType(ShareContentType.FILE)
                                            .setShareFileUri(FileUtil.getFileUri(ThirdProduceTestActivity.this, ShareContentType.FILE, file))
                                            .setTitle("打印SN")
                                            .setOnActivityResult(1000)
                                            .build()
                                            .shareBySystem();
                                }
                            })
                            .setNegativeButton("好的", null)
                            .show();
                }));

    }


    private void initTestClick() {
        bluetoothYanbiao.setOnClickListener(this::OnBlueToothClick);
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
        if (id == R.id.bluetooth_yanbiao) {
            snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3ReadYanBiao).getThirdReadYanBiaoBytes(), "验表");
        }
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
                                hashMapProduct.putAll(st3ByteTool.parseThirdReadYanBiaoBytes(result));
                                setCheckproduct(st3ByteTool.parseThirdReadYanBiaoBytes(result));
                                canSet = true;
                            } catch (Exception e) {
                                e.printStackTrace();
                                canSet = false;
                            }
                            sendMessageToast("验表成功");
                        } else {
                            canSet = false;
                            sendMessageToast("验表失败");
                        }
                    } else if (st3ByteTool.getsT3Type().equals(ST3Params.sT3InitBiao)) {
                        if (result[5] == 4 && result[6] == (byte) 0xfc) {
                            sendMessageToast("设置成功");
                            canSet = false;
                            setSuccess = true;
                            snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3ReadMaiChong).getThirdReadBiaoBytes(
                                    0), "抄表");
                        } else {
                            canSet = true;
                        }
                    } else if (st3ByteTool.getsT3Type().equals(ST3Params.sT3ReadMaiChong)) {
                        if (result[5] == (byte) 0x0e && result[6] == (byte) 0xfc) {
                            sendMessageToast("读取成功");
                            try {
                                hashMapProduct.putAll(st3ByteTool.parseThirdReadBiaoBytes(result));
                                snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3ReadInfo).getThirdReadSettingBytes(
                                        0), "读取配置信息");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            sendMessageToast("读取失败");
                        }
                    } else if (st3ByteTool.getsT3Type().equals(ST3Params.sT3InitHub)) {
                        if (result[4] == 1 && result[5] == 4 && result[6] == (byte) 0xfe && result[7] == (byte) 0xd0) {
                            sendMessageToast("Hub号设置成功");

                            snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3InitBiao).getThirdInitBiaoBytes(
                                    0
                                    , Long.valueOf(UserId) //设置设备ID
                                    , "2EV"  //设置默认传感类型 2EV
                                    , 5//设置初始脉冲
                            ), "初始化表单元");

                        }
                    } else if (st3ByteTool.getsT3Type().equals(ST3Params.sT3InitBiao)) {
                        if (result[5] == 4 && result[6] == (byte) 0xfc) {
                            sendMessageToast("设置成功");

                            //生成时间戳
                            Date date1 = new Date();// new Date()为获取当前系统时间，也可使用当前时间戳
                            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
                            String dateF = df1.format(date1);
                            String[] split = dateF.split("-");
                            String year = split[0].substring(split[0].length() - 2, split[0].length());
                            String month = split[1].toString();
                            String day = split[2].toString();
                            snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3SettingInfo).getThirdSettingBiaoBytes(
                                    0
                                    , "2EV"  //设置默认传感类型 2EV
                                    , Integer.valueOf(year)
                                    , Integer.valueOf(month)
                                    , Integer.valueOf(day)
                                    , Integer.parseInt(LoginParamManager.getInstance().getLoginInfo().getData().getMloginFactoryNum())  //设置厂家
                            ), "配置表单元");

                        }
                    } else if (st3ByteTool.getsT3Type().equals(ST3Params.sT3ReadInfo)) {
                        if (result[5] == 0x16 && result[6] == (byte) 0xfd) {

                            try {
                                hashMapProduct.putAll(st3ByteTool.parseThirdReadSettingBytes(result));
                                setTextMaiChong(hashMapProduct);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            sendMessageToast("读取信息失败");
                        }
                    }
                }

                break;

        }

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
                thirdProduceInfos.get(num - 1).setUserId(UserId);
                multiTypeAdapter.notifyDataSetChanged();
                setDuQu(hashMap, thirdProduceInfos.get(num - 1).getNum());
            }
        });
    }

    private void setCheckproduct(HashMap<String, String> hashMap) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < thirdProduceInfos.size(); i++) {
                    if (thirdProduceInfos.get(i).getUserId() .equals(hashMap.get(MapParams.设备ID)) ) {
                        sendMessageToast("该设备在列表，请不要重复生产");
                        return;
                    }
                }
                num++;
                ThirdProduceInfo thirdProduceInfo = new ThirdProduceInfo();
                thirdProduceInfo.setNum(num);
                thirdProduceInfo.setUserId(hashMap.get(MapParams.设备ID));
                thirdProduceInfos.add(thirdProduceInfo);
                pushEvent(ChipCode.MChipCheckproduct, hashMap.get(MapParams.设备ID),num-1);

            }
        });
    }

    private void setCreatId(int ProduceInfosnum) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                thirdProduceInfos.get(ProduceInfosnum).setTishi("未设置设备ID");
                thirdProduceInfos.get(ProduceInfosnum).setSave(false);
                XLog.i("LSY thirdProduceInfos.size() =====" + thirdProduceInfos.size());
                multiTypeAdapter.notifyDataSetChanged();
                textSum.setText(thirdProduceInfos.size() + "");
                pushEvent(LouShanYunCode.MChipCreateUserId);
            }
        });
    }

    private void setShowId(HashMap<String, String> hashMap,int ProduceInfosnum) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                thirdProduceInfos.get(ProduceInfosnum).setUserId(hashMap.get(MapParams.设备ID));
                thirdProduceInfos.get(ProduceInfosnum).setTishi("该设备以生产");
                thirdProduceInfos.get(ProduceInfosnum).setSave(true);
                XLog.i("LSY thirdProduceInfos.size() =====" + thirdProduceInfos.size());
                multiTypeAdapter.notifyDataSetChanged();
                textSum.setText(thirdProduceInfos.size() + "");
            }
        });
    }


    private void setDuQu(HashMap<String, String> hashMap, int num) {
        FactoryProductionInfo factoryProductionInfo = new FactoryProductionInfo();
        factoryProductionInfo.setChipNum(hashMap.get(MapParams.设备ID));
        factoryProductionInfo.setHardwareVersion(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
        factoryProductionInfo.setSoftVersion(LouShanYunUtils.getSoftWareVersion(Integer.valueOf(hashMap.get(MapParams.软件版本))));
        factoryProductionInfo.setMLoginPhoneNumber(String.valueOf(LoginParamManager.getInstance().getLoginInfo().getData().getMloginPhoneNumber()));
        factoryProductionInfo.setModuleType(3);
        factoryProductionInfo.setCollectionScene(1);//采集场景
        factoryProductionInfo.setEquipmentType(2);//1号对 1，2号对 2，4号对4
        factoryProductionInfo.setMLoginFactoryNum(Integer.parseInt(LoginParamManager.getInstance().getLoginInfo().getData().getMloginFactoryNum()));
        factoryProductionInfo.setMLoginCode(LoginParamManager.getInstance().getLoginInfo().getData().getMloginCode());
        factoryProductionInfo.setMLoginSupplier(LoginParamManager.getInstance().getLoginInfo().getData().getMloginSupplier());
        factoryProductionInfos.add(factoryProductionInfo);

//        int index = SnPrintUtil.containsSecond(factoryProductionInfos, factoryProductionInfo);
        XLog.i("LSY： factoryProductionInfo " + new Gson().toJson(factoryProductionInfo));
        pushEvent(ChipCode.MChipModuleFactoryProduction, new Gson().toJson(factoryProductionInfo), num, LoginParamManager.getInstance().getLoginInfo().getData().getMloginPhoneNumber());
//        if(index!=-1){
//            factoryProductionInfos.set(index,factoryProductionInfo);
//            pushEvent(ChipCode.MChipModuleFactoryProduction,  new Gson().toJson(factoryProductionInfo) ,snPrintInfo.getNum());
//        }else {
//            factoryProductionInfos.add(factoryProductionInfo);
//            pushEvent(ChipCode.MChipModuleFactoryProduction,  new Gson().toJson(factoryProductionInfo),snPrintInfo.getNum() );
//        }


        XLog.i("蓝牙： factoryProductionInfo " + new Gson().toJson(factoryProductionInfo));
    }


    @Override
    public void onChildWriteSuccess() {

    }

    @Override
    public void onChildWriteFailure(int i) {

    }

    @Override
    protected void initEventListener() {
        registerEventRunner(ChipCode.MChipModuleFactoryProduction, new FactoryProductionRunner());
        registerEventRunner(LouShanYunCode.MChipCreateUserId, new CreateUserIdRunner());
        registerEventRunner(ChipCode.MChipCheckproduct, new CheckProduceRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == ChipCode.MChipModuleFactoryProduction) {
            if (event.isSuccess()) {
                int num = (int) event.getParamAtIndex(1);
                ResultJson resultJson = (ResultJson) event.getReturnParamAtIndex(0);
                if (resultJson.getCode() == 1) {
                    thirdProduceInfos.get(num - 1).setUpData(true);
                } else if (resultJson.getCode() == 2) {
                    thirdProduceInfos.get(num - 1).setUpData(true);
                } else {
                    thirdProduceInfos.get(num - 1).setUpData(false);
                }
                multiTypeAdapter.notifyDataSetChanged();
                sendMessageToast(resultJson.getMsg());
            }
        } else if (code == LouShanYunCode.MChipCreateUserId) {
            if (event.isSuccess()) {
                CreateUserIdInfo createUserIdInfo = (CreateUserIdInfo) event.getReturnParamAtIndex(0);
                if (createUserIdInfo.getCode() == 0) {
                    UserId = createUserIdInfo.getUserId();
                    snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3InitHub).getThirdInitBubBytes(), "初始化Bub号");
                }

            }
        } else if (code == ChipCode.MChipCheckproduct) {
            if (event.isSuccess()) {
                int ProduceInfosnum = (int) event.getParamAtIndex(1);
                ResultJson resultJson = (ResultJson) event.getReturnParamAtIndex(0);
                if (resultJson.getCode() == 1) {
                    setCreatId(ProduceInfosnum);
                } else {
                    sendMessageToast(resultJson.getMsg() + "");
                    setShowId(hashMapProduct,ProduceInfosnum);
                    //     snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3InitHub).getThirdInitBubBytes(), "初始化Bub号");
                }

            }

        }
    }
}
