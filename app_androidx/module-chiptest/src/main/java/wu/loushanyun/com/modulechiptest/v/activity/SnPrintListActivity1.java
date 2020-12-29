package wu.loushanyun.com.modulechiptest.v.activity;

import android.content.DialogInterface;
import android.net.Uri;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundTextView;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.basemvp.m.sat.SAtInstructRealizeFactory;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;
import com.wu.loushanyun.basemvp.v.activity.BaseSnBlueToothActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
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
import wu.loushanyun.com.modulechiptest.R;
import wu.loushanyun.com.modulechiptest.m.SnPrintInfo;
import wu.loushanyun.com.modulechiptest.p.adapter.SnPrintViewBinder;

@Route(path = K.SnPrintListActivity1)
public class SnPrintListActivity1 extends BaseSnBlueToothActivity {
    private Spinner xindaocanshuSelect;
    private RecyclerView recycleview;
    private RoundTextView textSum;
    private SnPrintViewBinder snPrintViewBinder;
    private MultiTypeAdapter multiTypeAdapter;
    private ArrayList<SnPrintInfo> snPrintInfos;
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

    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        snPrintViewBinder = new SnPrintViewBinder(new SnPrintViewBinder.OnSnPrintListener() {
            @Override
            public void onUpData(int position) {
                snPrintInfos.remove(position);
                multiTypeAdapter.notifyItemRemoved(position);
                textSum.setText(snPrintInfos.size() + "");
            }

            @Override
            public void onSend(int position) {
                sendMessageToast("不支持");
            }

            @Override
            public void onSetXinDao(int position) {
                sendMessageToast("不支持");
            }

            @Override
            public void onSetRxDelay(int position) {
                sendMessageToast("不支持");
            }
        },"删除");
        registerLifeCycle(snPrintViewBinder);
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        super.onInitAttribute(ba);
        ba.mTitleText = "扫描2号SN号和Token";
    }


    @Override
    protected void onClickBlueListListener(SensoroDevice sensoroDevice) {
        super.onClickBlueListListener(sensoroDevice);
        this.sensoroDevice = sensoroDevice;
    }

    @Override
    protected int onChildLayout() {
        return R.layout.m_chip_activity_snlist;
    }

    @Override
    protected void initView() {
        super.initView();
        xindaocanshuSelect = (Spinner) findViewById(R.id.xindaocanshu_select);
        recycleview = (RecyclerView) findViewById(R.id.recycleview);
        textSum = (RoundTextView) findViewById(R.id.text_sum);


        multiTypeAdapter = new MultiTypeAdapter();
        snPrintInfos = new ArrayList<>();
        multiTypeAdapter.register(SnPrintInfo.class, snPrintViewBinder);
        multiTypeAdapter.setItems(snPrintInfos);
        recycleview.setAdapter(multiTypeAdapter);
        textSum.setOnClickListener(v -> {
            createExcel();
        });
        sAtInstructRealizeFactory = new SAtInstructRealizeFactory();
        sAtInstructRealizeFactory.initHardVersion(1.06);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(SnPrintListActivity1.this, R.layout.l_loushanyun_item_spinner, sAtInstructRealizeFactory.getAllMoShi());
        xindaocanshuSelect.setAdapter(arrayAdapter);
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
//                    for (int i = 0; i < snPrintInfos.size(); i++) {
//                        if ("不合格".equals(snPrintInfos.get(i).getSendStatus())) {
//                            tishi = "有强制发送不合格的模组，无法导出";
//                            canSave = false;
//                            break;
//                        }
//                    }
//                    for (int i = 0; i < snPrintInfos.size(); i++) {
//                        if ("强制发送失败".equals(snPrintInfos.get(i).getSendStatus())) {
//                            tishi = "有强制发送失败的模组，无法导出";
//                            canSave = false;
//                            break;
//                        }
//                    }
//                    if (!canSave) {
//                        if (!XHStringUtil.isEmpty(tishi, false)) {
//                            sendMessageToast(tishi, true);
//                        }
//                        LoadingDialogUtil.dismissByEvent(loadingTag);
//                        return;
//                    }
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
                    zzExcelCreator.fillContent(5, 1, "固件版本号", cellFormat);
                    zzExcelCreator.fillContent(6, 1, "备注", cellFormat);
                    for (int i = 0; i < snPrintInfos.size(); i++) {
                        zzExcelCreator.fillContent(col, row, snPrintInfos.get(i).getNum() + "", cellFormat);
                        zzExcelCreator.fillContent(col + 1, row, snPrintInfos.get(i).getSensoroDevice().sn + "", cellFormat);
                        zzExcelCreator.fillContent(col + 2, row, snPrintInfos.get(i).getToken() + "", cellFormat);
                        zzExcelCreator.fillContent(col + 3, row, snPrintInfos.get(i).getRxDelay() + "", cellFormat);
                        zzExcelCreator.fillContent(col + 4, row, snPrintInfos.get(i).getFirmwareVersion() + "", cellFormat);
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
                                    Uri uri = FileProvider.getUriForFile(SnPrintListActivity1.this, "wu.loushanyun.com.modulechiptest.fileprovider", file);
                                    new Share2.Builder(SnPrintListActivity1.this)
                                            .setContentType(ShareContentType.FILE)
                                            .setShareFileUri(FileUtil.getFileUri(SnPrintListActivity1.this, ShareContentType.FILE, file))
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
    public void onChildConnectFailed(int i) {

    }

    @Override
    public void onChildConnectSuccess() {
        try {
            hashMap = new HashMap<>();
            isOnlySet = false;
            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRxDelay).getTwoReadBytes(), "读取RxDelay");
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
                if (resultAt.equals(SAtInstructParams.sAtInstructToken + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructToken).parseTwoRNotifyBytes(result));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                num++;
                                SnPrintInfo snPrintInfo = new SnPrintInfo(num, sensoroDevice, hashMap.get(SAtInstructParams.sAtInstructToken), hashMap.get(SAtInstructParams.sAtInstructRxDelay),hashMap.get(SAtInstructParams.sAtInstructFirmwareVersion));
                                clickSnPrintInfo = snPrintInfo;
                                if (snPrintInfos.contains(clickSnPrintInfo)) {
                                    snPrintInfos.set(snPrintInfos.indexOf(clickSnPrintInfo), clickSnPrintInfo);
                                } else {
                                    snPrintInfos.add(clickSnPrintInfo);
                                }
                                multiTypeAdapter.notifyDataSetChanged();
                                textSum.setText(snPrintInfos.size() + "");
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructRxDelay + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRxDelay).parseTwoRNotifyBytes(result));
                        snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructFirmwareVersion).getTwoReadBytes(), "读取固件版本");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructFirmwareVersion + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructFirmwareVersion).parseTwoRNotifyBytes(result));
                        snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructToken).getTwoReadBytes(), "读取Token...");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 0x20:
                if (result[3] == 0) {
                    setDefaultParams();
                }
                break;
            case 0x21:
                if (result[3] == 0) {
                    sendMessageToast("设置成功");
                    try {
                        snBlueToothTool.write(DataParser.CMD_METER_INFO, "读取中...", true, 6000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    sendMessageToast("设置失败");
                }
                break;
            case 0x22:
                hashMap.putAll(DataParser.getMoudleNo2(result));
                try {
                    closeJiHuo();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 0x26:
                if (result[3] == 0) {
                    try {
                        snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRxDelay).getTwoReadBytes(), "读取RxDelay");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;
            case 0x24:
                if (result[3] == 0) {
                    snBlueToothTool.write(DataParser.CMD_SYSTEM_STATUS, "正在读取设备激活状态");
                }
                break;
        }
    }

    private void closeJiHuo() {
        byte[] d = new byte[6];
        d[0] = (byte) 0x68;
        d[1] = (byte) 0x02;//有效数据
        d[2] = (byte) 0x24;//命令,设置成未激活
        d[3] = (byte) 0x00;//
        byte cs = 0;
        for (int i = 1; i < 4; i++) {
            cs += d[i];
        }
        d[4] = cs;//校验和
        d[5] = (byte) 0x16;
        snBlueToothTool.write(d, "正在停用无线上传");
    }

    private void setDefaultParams() {
        String id = LouShanYunUtils.getTimeID();
        byte[] input2 = DataParser.getBiaoXinxiChuShiHuaCMD(
                Long.parseLong(id),
                Long.parseLong(String.valueOf("5")),
                String.valueOf(0.001));
        snBlueToothTool.write(input2, "设置2参数中...");
    }

    private void setDefaultFactoryParams() {
        snBlueToothTool.write(DataParser.getFactoryParams(
                "无磁正反脉冲", chaiXie, qiangCi, chuanGanQiZhuangTai, daoLiu, faMenZhuangTai, faSongPinLv, Integer.valueOf("98"),
                "0", "0"
        ), "设置1参数中...");
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
