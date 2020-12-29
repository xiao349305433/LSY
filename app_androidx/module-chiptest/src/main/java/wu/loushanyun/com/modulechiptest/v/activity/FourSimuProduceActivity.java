package wu.loushanyun.com.modulechiptest.v.activity;

import android.content.DialogInterface;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.m.sat.SAtInstructRealizeFactory;
import com.wu.loushanyun.basemvp.m.sat.util.FourSimuBlueToothUtil;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;
import com.wu.loushanyun.basemvp.v.activity.BaseSnBlueToothActivity;

import java.io.File;
import java.util.ArrayList;
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
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.some_utils.util.AbSharedUtil;
import wu.loushanyun.com.modulechiptest.R;
import wu.loushanyun.com.modulechiptest.init.ChipCode;
import wu.loushanyun.com.modulechiptest.init.LoginParamManager;
import wu.loushanyun.com.modulechiptest.m.FactoryProductionInfo;
import wu.loushanyun.com.modulechiptest.m.ResultJson;
import wu.loushanyun.com.modulechiptest.m.SnPrintInfo;
import wu.loushanyun.com.modulechiptest.p.adapter.SnPrintViewBinder;
import wu.loushanyun.com.modulechiptest.p.runner.FactoryProductionRunner;
import wu.loushanyun.com.modulechiptest.p.runner.MChipSetRxDelayRunner;
import wu.loushanyun.com.modulechiptest.p.util.SnPrintUtil;

import static com.wu.loushanyun.basemvp.init.SnBlueToothTool.BLUE_SEND_TIME_OUT;

@Route(path = K.FourSimuProduceActivity)
public class FourSimuProduceActivity extends BaseSnBlueToothActivity {
    private Spinner xindaocanshuSelect;
    private RecyclerView recycleview;
    private TextView textSum;
    private RoundTextView text_out;
    private SnPrintViewBinder snPrintViewBinder;
    private MultiTypeAdapter multiTypeAdapter;
    private ArrayList<SnPrintInfo> snPrintInfos;
    private ArrayList<FactoryProductionInfo> factoryProductionInfos;
    private int num;

    private String excelFilePath;
    private String excelFileName;
    private SensoroDevice sensoroDevice;
    private SAtInstructRealizeFactory sAtInstructRealizeFactory;

    private HashMap<String, String> hashMap;
    private SnPrintInfo clickSnPrintInfo;

    private byte chaiXie = 0;//拆卸   无
    private byte qiangCi = 1;//强磁   无
    private byte chuanGanQiZhuangTai = 0;//传感器状态   无
    private byte daoLiu = 0;//倒流   无
    private byte faMenZhuangTai = 0;//阀门状态   无
    private byte faSongPinLv = (byte) 0x30;//发送频率   默认24小时

    private boolean isOnlySet = false;
    private int type;


    @Override
    protected int onChildLayout() {
        return R.layout.m_chip_activity_foursimuproduce;
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        super.onInitAttribute(ba);
        ba.mTitleText = "生产4号模组";
    }

    @Override
    protected void onClickBlueListListener(SensoroDevice sensoroDevice) {
        super.onClickBlueListListener(sensoroDevice);
        this.sensoroDevice = sensoroDevice;
    }

    @Override
    protected void initView() {
        super.initView();

        xindaocanshuSelect = (Spinner) findViewById(R.id.xindaocanshu_select);
        recycleview = (RecyclerView) findViewById(R.id.recycleview);
        textSum = (TextView) findViewById(R.id.text_sum);
        text_out= (RoundTextView) findViewById(R.id.text_out);

        multiTypeAdapter = new MultiTypeAdapter();
        snPrintInfos = new ArrayList<>();
        factoryProductionInfos=new ArrayList<>();
        multiTypeAdapter.register(SnPrintInfo.class, snPrintViewBinder);
        multiTypeAdapter.setItems(snPrintInfos);
        recycleview.setAdapter(multiTypeAdapter);
        text_out.setOnClickListener(v -> {
            createExcel();
        });
        sAtInstructRealizeFactory = new SAtInstructRealizeFactory();
        sAtInstructRealizeFactory.initHardVersion(1.06);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(FourSimuProduceActivity.this, R.layout.l_loushanyun_item_spinner, sAtInstructRealizeFactory.getAllMoShi());
        xindaocanshuSelect.setAdapter(arrayAdapter);
        xindaocanshuSelect.setSelection(80);
        xindaocanshuSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                AbSharedUtil.putString(FourSimuProduceActivity.this,"snmoshi",xindaocanshuSelect.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        if(!XHStringUtil.isEmpty(AbSharedUtil.getString(this,"模式A"),true)){
            xindaocanshuSelect.setSelection(sAtInstructRealizeFactory.getMoShiIndex(AbSharedUtil.getString(this,"模式A")));
        }
    }

    @Override
    public void onChildConnectFailed(int i) {

    }

    private void createExcel() {
        compositeDisposable.add(Flowable.create(
                (FlowableOnSubscribe<String>) emitter ->
                {
                    LoadingDialogUtil.showByEvent("正在检查", loadingTag);
                    boolean canSave = true;
                    String tishi = "";
//                    for (int i = 0; i < snPrintInfos.size(); i++) {
//                        if ("重复SN".equals(snPrintInfos.get(i).getTishi())) {
//                            tishi = "有重复的SN，请去掉后再试";
//                            canSave = false;
//                            break;
//                        }
//                    }



                    for (int i = 0; i < snPrintInfos.size(); i++) {
                        if ("不合格".equals(snPrintInfos.get(i).getSendStatus())) {
                            tishi = "有强制发送不合格的模组，无法导出";
                            canSave = false;
                            break;
                        }
                    }
                    for (int i = 0; i < snPrintInfos.size(); i++) {
                        if(!snPrintInfos.get(i).isSend()){
                            tishi = "有强制发送失败的模组，无法导出";
                            canSave = false;
                            break;
                        }
                    }
                    for (int i = 0; i < snPrintInfos.size(); i++) {
                        if(!snPrintInfos.get(i).isUpData()){
                            tishi = "有未上传的模组，无法导出";
                            canSave = false;
                            break;
                        }
                    }


                    if (!canSave) {
                        if (!XHStringUtil.isEmpty(tishi, false)) {
                            sendMessageToast(tishi, true);
                        }
                        LoadingDialogUtil.dismissByEvent(loadingTag);
                        return;
                    }
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
                    zzExcelCreator.fillContent(2, 1, "SN号", cellFormat);
                    zzExcelCreator.fillContent(3, 1, "Token", cellFormat);
                    zzExcelCreator.fillContent(4, 1, "RxDelay", cellFormat);
                    zzExcelCreator.fillContent(5, 1, "RSSI", cellFormat);
                    zzExcelCreator.fillContent(6, 1, "SNR", cellFormat);
                    zzExcelCreator.fillContent(7, 1, "状态", cellFormat);
                    zzExcelCreator.fillContent(8, 1, "备注", cellFormat);
                    for (int i = 0; i < snPrintInfos.size(); i++) {
                        zzExcelCreator.fillContent(col, row, snPrintInfos.get(i).getNum() + "", cellFormat);
                        zzExcelCreator.fillContent(col + 1, row, snPrintInfos.get(i).getSensoroDevice().sn + "", cellFormat);
                        zzExcelCreator.fillContent(col + 2, row, snPrintInfos.get(i).getToken() + "", cellFormat);
                        zzExcelCreator.fillContent(col + 3, row, snPrintInfos.get(i).getRxDelay() + "", cellFormat);
                        zzExcelCreator.fillContent(col + 4, row, snPrintInfos.get(i).getRssi() + "", cellFormat);
                        zzExcelCreator.fillContent(col + 5, row, snPrintInfos.get(i).getSnr() + "", cellFormat);
                        zzExcelCreator.fillContent(col + 6, row, snPrintInfos.get(i).getSendStatus() + "", cellFormat);
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
                    for (int i = 0; i < snPrintInfos.size(); i++) {
                        zzExcelCreator.fillContent(col + 7, row, snPrintInfos.get(i).getTishi() + "", cellFormat1);
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
                                    Uri uri = FileProvider.getUriForFile(FourSimuProduceActivity.this, "wu.loushanyun.com.modulechiptest.fileprovider", file);
                                    new Share2.Builder(FourSimuProduceActivity.this)
                                            .setContentType(ShareContentType.FILE)
                                            .setShareFileUri(FileUtil.getFileUri(FourSimuProduceActivity.this, ShareContentType.FILE, file))
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


    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        snPrintViewBinder = new SnPrintViewBinder(new SnPrintViewBinder.OnSnPrintListener() {
            @Override
            public void onUpData(int position) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sendMessageToast("上传");
                        pushEvent(ChipCode.MChipModuleFactoryProduction,  new Gson().toJson(factoryProductionInfos.get(position)),1,
                                LoginParamManager.getInstance().getLoginInfo().getData().getMloginPhoneNumber() );
                    }
                });

            }

            @Override
            public void onSend(int position) {
                clickSnPrintInfo = snPrintInfos.get(position);
                if (clickSnPrintInfo.getSensoroDevice().equals(sensoroDevice)) {
                    try {
                        snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructForcedSend).getTwoReadBytes(String.valueOf(LoginParamManager.getInstance().getLoginInfo().getData().getMloginPhoneNumber()))
                                , "强制发送", true, BLUE_SEND_TIME_OUT);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    sendMessageToast("当前蓝牙连接的是：" + sensoroDevice.sn + "请选择正确的sn！！！", true);
                }
            }

            @Override
            public void onSetXinDao(int position) {
                clickSnPrintInfo = snPrintInfos.get(position);
                if (clickSnPrintInfo.getSensoroDevice().equals(sensoroDevice)) {
                    isOnlySet = true;
                    try {
                        snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getTwoWriteBytes(xindaocanshuSelect.getSelectedItem().toString().trim()), "设置信道");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    sendMessageToast("当前蓝牙连接的是：" + sensoroDevice.sn + "请选择正确的sn！！！", true);
                }
            }

            @Override
            public void onSetRxDelay(int position) {
                clickSnPrintInfo = snPrintInfos.get(position);
                if (clickSnPrintInfo.getSensoroDevice().equals(sensoroDevice)) {
                    try {
                        pushEvent(ChipCode.MChipSetRxDelayRunner, sensoroDevice.sn, "1");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    sendMessageToast("当前蓝牙连接的是：" + sensoroDevice.sn + "请选择正确的sn！！！", true);
                }
            }
        },"上传");
        registerLifeCycle(snPrintViewBinder);
    }


    @Override
    public void onChildConnectSuccess() {
        try {
            hashMap = new HashMap<>();
            isOnlySet = false;
            try {
                snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).getFourWriteBytes(), "识别模块中...", true, 6000);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onChildNotify(byte[] result) {
        byte code = (byte) (result[2] ^ ((byte) 0x80));
        XLog.i("蓝牙code=" + code);
        switch (code) {
            case 0x50:
                String resultAt = sAtInstructRealizeFactory.getSAtTypeString(result);
                XLog.i("蓝牙" + resultAt);
                if (resultAt.equals(SAtInstructParams.sAtInstructChannel + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).parseFourRNotifyBytes(result));

                        snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).getFourReadBytes(), "读取发送功率");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructSendingPower + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).parseFourRNotifyBytes(result));

                        snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructFirmwareVersion).getFourReadBytes(), "读取固件版本");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructFirmwareVersion + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructFirmwareVersion).parseFourRNotifyBytes(result));
                        snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructForcedSend).getTwoReadBytes(String.valueOf(LoginParamManager.getInstance().getLoginInfo().getData().getMloginPhoneNumber()))
                                , "强制发送", true, BLUE_SEND_TIME_OUT);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructRSSI + "-R")) {

                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRSSI).parseFourRNotifyBytes(result));
                        String[] strings = hashMap.get(SAtInstructParams.sAtInstructRSSI).split(",");
                        int rssi = Integer.valueOf(strings[0]);
                        int snr = Integer.valueOf(strings[1]);
                        int rssiStart = Integer.valueOf("-90");
                        int rssiEnd = Integer.valueOf("127");
                        int snrStart = Integer.valueOf("0");
                        int snrEnd = Integer.valueOf("127");
                        String status;
                        if (rssi >= rssiStart && rssi <= rssiEnd && snr >= snrStart && snr <= snrEnd) {
                            status = "合格";
                        } else {
                            status = "不合格";
                        }

                        clickSnPrintInfo.setRssi(rssi + "");
                        clickSnPrintInfo.setSnr(snr + "");
                        clickSnPrintInfo.setSendStatus(status);
                        snPrintInfos.set(snPrintInfos.indexOf(clickSnPrintInfo), clickSnPrintInfo);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    multiTypeAdapter.notifyDataSetChanged();
                                    setDuQu(hashMap,clickSnPrintInfo);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } catch (Exception e) {

                    }
//                        try {
//                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRSSI).parseFourRNotifyBytes(result));
//                        String[] strings = hashMap.get(SAtInstructParams.sAtInstructRSSI).split(",");
//                        int rssi = Integer.valueOf(strings[0]);
//                        int snr = Integer.valueOf(strings[1]);
//                        int rssiStart = Integer.valueOf("-90");
//                        int rssiEnd = Integer.valueOf("127");
//                        int snrStart = Integer.valueOf("0");
//                        int snrEnd = Integer.valueOf("127");
//                        String status;
//                        if (rssi > rssiStart && rssi < rssiEnd && snr > snrStart && snr < snrEnd) {
//                            status = "合格";
//                        } else {
//                            status = "不合格";
//                        }
//                        runOnUiThread(() -> {
//                            String sb = "信号强度：" + rssi + "\n" + "信噪比：" + snr + "\n" + "状态：" + status;
//                            pushEventNoProgress(ChipCode.MChipGetNewsInfoRunner, sensoroDevice.sn, "1", "5");
//                        });
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructSpreadingFactor + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).parseFourRNotifyBytes(result));

                        snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRxDelay).getFourReadBytes(), "读取RxDelay");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructRxDelay + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRxDelay).parseFourRNotifyBytes(result));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructToken + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructToken).parseFourRNotifyBytes(result));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                num++;
                                SnPrintInfo snPrintInfo = new SnPrintInfo(num, sensoroDevice, hashMap.get(SAtInstructParams.sAtInstructToken), "1s");
                                clickSnPrintInfo = snPrintInfo;
                                if (snPrintInfos.contains(clickSnPrintInfo)) {
                                    num--;
                                    SnPrintInfo snPrintInfo1=snPrintInfos.get(snPrintInfos.indexOf(clickSnPrintInfo));
                                    clickSnPrintInfo.setNum(snPrintInfo1.getNum());
                                    snPrintInfos.set(snPrintInfos.indexOf(clickSnPrintInfo), clickSnPrintInfo);
                                } else {
                                    snPrintInfos.add(clickSnPrintInfo);
                                }
                                //         multiTypeAdapter.notifyDataSetChanged();
                                textSum.setText(snPrintInfos.size() + "");
                                try {

                                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructFirmwareVersion).getFourReadBytes(), "读取固件版本");

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else if (resultAt.equals(SAtInstructParams.sAtInstructChannel + "-W")) {
                    try {
                        sendMessageToast("信道设置成功");
                        try {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).getFourWriteBytes("20dbm"), "设置发送功率");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructSendingPower + "-W")) {
                    try {
                        sendMessageToast("设置发送功率成功");
                        try {
                           snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructToken).getFourReadBytes(), "读取Token...");

                      //      snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).getFourWriteBytes(xindaocanshuSelect.getSelectedItem().toString().trim()), "设置扩频因子");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructForcedSend + "+SEND2")) {

                    clickSnPrintInfo.setSend(true);
                    sendMessageToast("强制发送成功");
                    snPrintInfos.set(snPrintInfos.indexOf(clickSnPrintInfo), clickSnPrintInfo);
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    multiTypeAdapter.notifyDataSetChanged();
                                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRSSI).getFourReadBytes(), "读取信号强度");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (resultAt.equals(SAtInstructParams.sAtInstructForcedSend + "+SEND3")) {

                    clickSnPrintInfo.setSend(false);
                    sendMessageToast("强制发送失败");
                    snPrintInfos.set(snPrintInfos.indexOf(clickSnPrintInfo), clickSnPrintInfo);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            multiTypeAdapter.notifyDataSetChanged();
                        }
                    });

                }  else if (resultAt.equals(SAtInstructParams.sAtInstructRxDelay + "-W")) {
                    try {
                        sendMessageToast("设置RxDelay成功");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    sendMessageToast(resultAt);
                }
                break;

            case 0x11:
                try {
                    type = DataParser.getModuleType(result);
                    //  type = 5;
                    //TODO 先用type=4 的模组 进行测试
                    initSnAgreement(Double.valueOf(LouShanYunUtils.getHardWareVersion(Integer.valueOf("6"))));
                    if (type == 5) {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).parseFourRNotifyBytes(result));

                        snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getFourWriteBytes(xindaocanshuSelect.getSelectedItem().toString().trim()), "设置信道");

                    } else {
                        sendMessageToast("识别不是4号模拟信号模组，请选择正确的模组");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    sendMessageToast("识别失败");
                }
                break;
        }
    }


    private void setDuQu(HashMap<String, String> hashMap,SnPrintInfo snPrintInfo) {
        FactoryProductionInfo factoryProductionInfo=new FactoryProductionInfo();
        factoryProductionInfo.setChipNum(snPrintInfo.getSensoroDevice().sn);
        factoryProductionInfo.setHardwareVersion(hashMap.get(MapParams.硬件版本));
        factoryProductionInfo.setSoftVersion(hashMap.get(MapParams.软件版本));
        factoryProductionInfo.setMLoginPhoneNumber(String.valueOf(LoginParamManager.getInstance().getLoginInfo().getData().getMloginPhoneNumber()));
        String[] strings = hashMap.get(SAtInstructParams.sAtInstructRSSI).split(",");
        factoryProductionInfo.setRssi(Integer.valueOf(strings[0]));
        factoryProductionInfo.setSnr(Integer.valueOf(strings[1]));
        factoryProductionInfo.setLOROWANVersion(hashMap.get(SAtInstructParams.sAtInstructFirmwareVersion));
        factoryProductionInfo.setModuleType(type);
        factoryProductionInfo.setRxDelay(1);
        factoryProductionInfo.setToken(hashMap.get(SAtInstructParams.sAtInstructToken));
        factoryProductionInfo.setChannel(xindaocanshuSelect.getSelectedItem().toString().trim());
        factoryProductionInfo.setCollectionScene(1);//采集场景
        factoryProductionInfo.setEquipmentType(2);//1号对 1，2号对 2，4号对4
        factoryProductionInfo.setMLoginFactoryNum(Integer.parseInt(LoginParamManager.getInstance().getLoginInfo().getData().getMloginFactoryNum()));
        factoryProductionInfo.setMLoginCode(LoginParamManager.getInstance().getLoginInfo().getData().getMloginCode());
        factoryProductionInfo.setMLoginSupplier(LoginParamManager.getInstance().getLoginInfo().getData().getMloginSupplier());
        int index = SnPrintUtil.containsSecond(factoryProductionInfos, factoryProductionInfo);
        if(index!=-1){
            factoryProductionInfos.set(index,factoryProductionInfo);
            pushEvent(ChipCode.MChipModuleFactoryProduction,  new Gson().toJson(factoryProductionInfo) ,snPrintInfo.getNum(),
                    LoginParamManager.getInstance().getLoginInfo().getData().getMloginPhoneNumber());
        }else {
            factoryProductionInfos.add(factoryProductionInfo);
            pushEvent(ChipCode.MChipModuleFactoryProduction,  new Gson().toJson(factoryProductionInfo),snPrintInfo.getNum(),
                    LoginParamManager.getInstance().getLoginInfo().getData().getMloginPhoneNumber() );
        }

        XLog.i("蓝牙： factoryProductionInfo " + new Gson().toJson(factoryProductionInfo));
    }


    private void initSnAgreement(double hardVersion) {
        sAtInstructRealizeFactory.initHardVersion(hardVersion);
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
        registerEventRunner(ChipCode.MChipSetRxDelayRunner, new MChipSetRxDelayRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == ChipCode.MChipModuleFactoryProduction) {
            if (event.isSuccess()) {
                int num= (int) event.getParamAtIndex(1);
                ResultJson resultJson= (ResultJson) event.getReturnParamAtIndex(0);
                if(resultJson.getCode()==1){
                    snPrintInfos.get(num-1).setUpData(true);
                }else if(resultJson.getCode()==2){
                    snPrintInfos.get(num-1).setUpData(true);
                }else {
                    snPrintInfos.get(num-1).setUpData(false);
                }
                multiTypeAdapter.notifyDataSetChanged();
                sendMessageToast(resultJson.getMsg());
            }
        }else if (code == ChipCode.MChipSetRxDelayRunner) {
            if (event.isSuccess()) {
                int codeReturn = (int) event.getReturnParamAtIndex(0);
                String msg = (String) event.getReturnParamAtIndex(1);
                if (codeReturn == 0) {
                    try {
                        snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRxDelay).getFourWriteBytes("1"), "设置RxDelay");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    sendMessageToast(msg);
                }
            }
        }
    }
}
