package wu.loushanyun.com.module_initthree.v.activity;

import android.app.AlertDialog;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundTextView;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.m.SaveDataMeter;
import com.wu.loushanyun.basemvp.m.sat.util.FourSimuBlueToothUtil;
import com.wu.loushanyun.basemvp.m.st3.ST3ByteTool;
import com.wu.loushanyun.basemvp.m.st3.ST3Params;
import com.wu.loushanyun.basemvp.p.adapter.MeterViewBinder;
import com.wu.loushanyun.basemvp.v.activity.BaseSnBlueToothActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.basebinder.BaseBinderModel;
import met.hx.com.base.base.multitype.ClassLinker;
import met.hx.com.base.base.multitype.ItemViewBinder;
import met.hx.com.base.base.multitype.Items;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ByteConvertUtils;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.module_initthree.R;
import wu.loushanyun.com.module_initthree.m.ThirdHandData;
import wu.loushanyun.com.module_initthree.p.adapter.ThirdHandViewBinder;
import wu.loushanyun.com.module_initthree.p.adapter.ThirdMeterViewBinder;


@Route(path = K.ThirdReadActivity)
public class ThirdReadActivity extends BaseSnBlueToothActivity implements MeterViewBinder.OnZhiShuListener, ThirdHandViewBinder.OnChongZhiListener,ThirdHandViewBinder.OnLouChaoListener,ThirdHandViewBinder.OnDuBiaoListener,ThirdHandViewBinder.OnSwtichListener{


    private TextView textDangqianSn;
    private RoundTextView bluetoothConn;
    private RoundTextView bluetoothDisconn;

    private RecyclerView resultList;

    private HashMap<String, String> hashMap;
    private SensoroDevice sensoroDeviceChoose;
    private ArrayList<SaveDataMeter> meterArrayList;
    private ArrayList<SaveDataMeter> meterMissArrayList;
    private ThirdMeterViewBinder meterViewBinder;
    private MultiTypeAdapter multiTypeAdapter;
    private int meterMisssNum = 0;
    private int currentNum;
    private boolean isReadMiss = false;
    private String StartStr;
    private String EndStr;
    private int meterStart;
    private int meterEnd;
    private int meterReadStart;
    private int meterReadEnd;
    private ST3ByteTool st3ByteTool;
    private int currentMissIndex;
    private SaveDataMeter resetSaveDataMeter;
    private SaveDataMeter zhishuSaveDataMeter;
    private ThirdHandViewBinder thirdHandViewBinder;
    private Items items;
    private ThirdHandData handData;
    private Boolean IsOpen=false;//表ID是否可以重复


    Comparator comparator = (Comparator<SaveDataMeter>) (o1, o2) -> {
        int a = Integer.parseInt(o1.getMeterNumber());
        int b = Integer.parseInt(o2.getMeterNumber());
        if (a > b) {
            return 1;
        }
        if (a < b) {
            return -1;
        }
        return 0;
    };

    @Override
    protected int onChildLayout() {
        return R.layout.m_chip_activity_thirdread;
    }


    @Override
    protected void initView() {
        super.initView();

        textDangqianSn = (TextView) findViewById(R.id.text_dangqian_sn);
        bluetoothConn = (RoundTextView) findViewById(R.id.bluetooth_conn);
        bluetoothDisconn = (RoundTextView) findViewById(R.id.bluetooth_disconn);
        resultList = (RecyclerView) findViewById(R.id.result_list);

        meterArrayList = new ArrayList<>();
        meterMissArrayList = new ArrayList<>();
        hashMap = new HashMap<>();
        multiTypeAdapter = new MultiTypeAdapter();
        handData=new ThirdHandData();

        multiTypeAdapter.register(BaseBinderModel.class).to(thirdHandViewBinder, meterViewBinder).withClassLinker(new ClassLinker<BaseBinderModel>() {
            @NonNull
            @Override
            public Class<? extends ItemViewBinder<BaseBinderModel, ?>> index(@NonNull BaseBinderModel baseBinderModel) {
                if( baseBinderModel.content.getClass().equals(ThirdHandData.class)){//判断条件
                    return ThirdHandViewBinder.class;//返回需要加载的binderView
                }else {
                    return ThirdMeterViewBinder.class;
                }
            }
        });

        items=new Items();
        multiTypeAdapter.setItems(items);
        items.add(new BaseBinderModel(handData));
        resultList.setLayoutManager(new LinearLayoutManager(this));
        resultList.setAdapter(multiTypeAdapter);
        sensoroDeviceChoose = getIntent().getParcelableExtra("SensoroDevice");
        if(sensoroDeviceChoose!=null){
            textDangqianSn.setText("当前连接的SN设备:" + sensoroDeviceChoose.getSerialNumber().toUpperCase());
            onClickBlueListListener(sensoroDeviceChoose);
        }

        st3ByteTool = new ST3ByteTool();
        bluetoothConn.setOnClickListener(v -> {
            if (textDangqianSn.getText().toString().contains("当前连接的SN设备")) {
                sendMessageToast("当前已连接设备");
                return;
            }
            snBlueToothTool.connectBlueTooth(sensoroDeviceChoose);

        });
        bluetoothDisconn.setOnClickListener(v -> {
            snBlueToothTool.disconnect();
            textDangqianSn.setText("当前未连接SN设备");
        });
    }


    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        super.onInitAttribute(ba);
        ba.mTitleText = "网络读表";
    }

    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        snBlueToothTool.setWriteTimeDelay(300);
        thirdHandViewBinder=new ThirdHandViewBinder(this);
        thirdHandViewBinder.setChongZhiListener(this);
        thirdHandViewBinder.setDuBiaoListener(this);
        thirdHandViewBinder.setLouChaoListener(this);
        thirdHandViewBinder.setSwtichListener(this);
        thirdHandViewBinder.setOnStartTextListener(new ThirdHandViewBinder.OnStartTextListener() {
            @Override
            public void onTextChanged(String str) {
                StartStr=str;
            }
        });
        thirdHandViewBinder.setOnEndTextListener(new ThirdHandViewBinder.OnEndTextListener() {
            @Override
            public void onTextChanged(String str) {
              EndStr=str;
            }
        });

        meterViewBinder = new ThirdMeterViewBinder(this);
        registerLifeCycle(meterViewBinder);
        registerLifeCycle(thirdHandViewBinder);
    }


    @Override
    protected void onClickBlueListListener(SensoroDevice sensoroDevice) {
        super.onClickBlueListListener(sensoroDevice);
        this.sensoroDeviceChoose = sensoroDevice;
    }


    @Override
    public void onChildConnectFailed(int i) {

    }

    @Override
    public void onChildConnectSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textDangqianSn.setText("当前连接的SN设备:" + sensoroDeviceChoose.getSerialNumber().toUpperCase());
                sendMessageToast("连接成功");
            }
        });

    }

    @Override
    public void onChildNotify(byte[] result) {
        byte code = (byte) (result[2] ^ ((byte) 0x80));
        switch (code) {
            case 0x60:
                if (result[3] == 0) {
                    if (st3ByteTool.getsT3Type().equals(ST3Params.sT3ReadMaiChong)) {
                        if (result[5] == (byte) 0x0e && result[6] == (byte) 0xfc) {
                            sendMessageToast("读取成功");
                            try {
                                SaveDataMeter item;
                                HashMap<String, String> hashMap = st3ByteTool.parseThirdReadBiaoBytes(result);
                                item = new SaveDataMeter(hashMap, "2");
                                item.setType("3");
                              //  int index1 = LouShanYunUtils.containsMeter(meterArrayList, item);
                            //    items.add(new BaseBinderModel(item));

                                int index1 = LouShanYunUtils.containsMeter(meterArrayList, item);
                                if (index1 != -1) {
                                    meterArrayList.set(index1, item);
                                } else {
                                    meterArrayList.add(item);
                                }



                                if (meterMissArrayList.size() > currentMissIndex) {
                                    if (meterMissArrayList.get(currentMissIndex).getMeterNumber().equals(item.getMeterNumber())) {
                                        meterMissArrayList.get(currentMissIndex).setMeterNumber("-1");//证明抄到表了，置为-1
                                        meterMisssNum--;
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            SaveDataMeter item;
                            HashMap<String, String> hashMap = new HashMap<>();
                            sendMessageToast("读取失败");
                            item = new SaveDataMeter(hashMap, "2");
                            item.setMeterNumber( String.valueOf(currentNum));
                            item.setType("3");

                            int index1 = LouShanYunUtils.containsMeter(meterArrayList, item);
                            if (index1 != -1) {
                                meterArrayList.set(index1, item);
                            } else {
                                meterArrayList.add(item);
                            }

                             int index = LouShanYunUtils.containsMeter(meterMissArrayList, item);
                            if (index != -1) {
                                meterMissArrayList.set(index, item);
                            } else {
                                meterMissArrayList.add(item);
                                meterMisssNum++;
                            }
                        }

                        if (isReadMiss) {
                            currentMissIndex++;
                        }
                        runOnUiThread(() -> {
                            int hasReadNum = meterArrayList.size();
                            int notReadNum = meterReadEnd - meterArrayList.size();
                            ThirdHandData thirdHandData=new ThirdHandData();
                            thirdHandData.setAccept(String.valueOf(hasReadNum));
                            thirdHandData.setDisAccept(String.valueOf(notReadNum));
                            thirdHandData.setMeterMiss(String.valueOf(meterMisssNum));

                            Collections.sort(meterArrayList, comparator);
                            Collections.sort(meterMissArrayList, comparator);
                            setItemsData(thirdHandData);

                            multiTypeAdapter.notifyDataSetChanged();
//                           resultList.smoothScrollToPosition(hasReadNum);
                            if (isReadMiss) {

                                if (currentMissIndex < meterMissArrayList.size()) {
                                    if (!("-1".equals(meterMissArrayList.get(currentMissIndex).getMeterNumber()))) {
                                        currentNum = Integer.valueOf(meterMissArrayList.get(currentMissIndex).getMeterNumber());
                                        readBiao(currentNum);
                                    }
                                } else {
                                    for (int i = meterMissArrayList.size() - 1; i >= 0; i--) {
                                        if ("-1".equals(meterMissArrayList.get(i).getMeterNumber())) {
                                            meterMissArrayList.remove(i);
                                        }
                                    }
                                }
                            } else {
                                if (currentNum < meterReadEnd) {
                                    currentNum++;
                                    XLog.i("蓝牙 表号  "+currentNum);
                                    readBiao(currentNum);
                                }

                            }

                        });

                    }else if(st3ByteTool.getsT3Type().equals(ST3Params.sT3ReadZhiShu)){
                            if(result[5]==(byte) 0x04 && result[6] == (byte) 0xfc){
                                sendMessageToast("置数成功");
                                int index = LouShanYunUtils.containsMeter(meterArrayList, zhishuSaveDataMeter);
                                meterArrayList.set(index,zhishuSaveDataMeter);
                                runOnUiThread(() -> {
                                    multiTypeAdapter.notifyDataSetChanged();
                                });
                            }else {
                                sendMessageToast("置数失败");
                            }
                    }
                    else {
                        sendMessageToast("读取失败");
                    }
                }
                break;
        }

    }


    private void readBiao(int num) {
        if(IsOpen){
            String str = getRepeadStr(meterArrayList);
            if (!XHStringUtil.isEmpty(str, false)) {
                ToastUtils.showLong(str);
            } else {
                snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3ReadMaiChong).getThirdReadBiaoBytes(
                        Integer.valueOf(num)), "抄表");
            }
        }else {
            snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3ReadMaiChong).getThirdReadBiaoBytes(
                    Integer.valueOf(num)), "抄表");
        }

    }

    private String getRepeadStr(ArrayList<SaveDataMeter> meterArrayList) {
        StringBuffer result = new StringBuffer();
        ArrayList<SaveDataMeter> array = new ArrayList<>();
        for (int i = 0; i < meterArrayList.size(); i++) {
            SaveDataMeter out = meterArrayList.get(i);
            for (int j = i + 1; j < meterArrayList.size(); j++) {
                SaveDataMeter inside = meterArrayList.get(j);
                if (!XHStringUtil.isEmpty(out.getUserId(), false) && !XHStringUtil.isEmpty(inside.getUserId(), false)) {
                    if (i != j && out.getUserId().equals(inside.getUserId())) {
                        if (!array.contains(out)) {
                            array.add(out);
                        }
                        array.add(inside);
                        continue;
                    }
                }
            }
            if (array.size() != 0) {
                break;
            }
        }
        if (array.size() != 0) {
            for (int i = 0; i < array.size(); i++) {
                SaveDataMeter saveDataMeter = array.get(i);
                if (i == array.size() - 1) {
                    result.append(saveDataMeter.getMeterNumber() + "号表的id重复了，都是" + saveDataMeter.getUserId());
                } else {
                    result.append(saveDataMeter.getMeterNumber() + "号表,");
                }
            }
        }
        return result.toString();
    }


    /**
     * 设置Items数据
     * @param thirdHandData
     */
    private void setItemsData(ThirdHandData thirdHandData){
        items.clear();
        items.add(new BaseBinderModel(thirdHandData));
        for (int i = 0; i < meterArrayList.size(); i++) {
            items.add(new BaseBinderModel(meterArrayList.get(i)));
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(ThirdReadActivity.this);
                dialog.setTitle("读取结果");
                dialog.setMessage("表号  :" + hashMap.get(MapParams.表号) + "\n" + "ID  :" + hashMap.get(MapParams.设备ID) + "\n" + "正脉冲数  :" + hashMap.get(MapParams.脉冲数) + "\n" + "倍率  :" + LouShanYunUtils.getBLReadStringByCode(hashMap.get(MapParams.倍率)) + "\n" + "状态  :" + LouShanYunUtils.getZTReadStringByCode(hashMap.get(MapParams.状态)) + "\n");
                dialog.setNegativeButton("确定", null);
                dialog.create().show();

            }
        });
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

    @Override
    public void onZhiShu(SaveDataMeter saveDataMeter) {
        HashMap<String, String> map = saveDataMeter.getMeterMap();
        String[] zhuangtai = new String[8];
        for (int i = 0; i < zhuangtai.length; i++) {
            zhuangtai[i] = "0";
        }
        String s = "";
        for (int i = 0; i < zhuangtai.length; i++) {
            s = s + zhuangtai[i];
        }
        byte b = (byte) (ByteConvertUtils.binaryToDecimal(s) & 0xff);
        map.put(MapParams.状态, String.valueOf(b));

        XLog.i("蓝牙 saveDataMeter: "+saveDataMeter.toString() );
        XLog.i("蓝牙 MapParams.倍率: "+FourSimuBlueToothUtil.getBLReadStringByCode(map.get(MapParams.倍率)) );
        XLog.i("蓝牙 MapParams.脉冲数: "+Integer.valueOf(map.get(MapParams.脉冲数)) );
        snBlueToothTool.write(st3ByteTool.setsAtParams(ST3Params.sT3ReadZhiShu).getThirdZhiShuBytes(
                Integer.valueOf(map.get(MapParams.表号))
                , Long.valueOf(map.get(MapParams.设备ID) )
                , FourSimuBlueToothUtil.getBLReadStringByCode(map.get(MapParams.倍率))
                , Integer.valueOf(map.get(MapParams.脉冲数))
        ), "置数");
        zhishuSaveDataMeter=saveDataMeter;

    }

    @Override
    public void onChongZhi(ThirdHandData thirdHandData) {
                items.clear();
                meterArrayList.clear();
                meterMissArrayList.clear();
                meterMisssNum = 0;
                thirdHandData.setAccept(String.valueOf(meterArrayList.size()));
                thirdHandData.setDisAccept(String.valueOf(meterReadEnd - meterArrayList.size()));
                thirdHandData.setMeterMiss(String.valueOf(meterMisssNum));
                items.add(new BaseBinderModel(thirdHandData));
                multiTypeAdapter.notifyDataSetChanged();
                sendMessageToast("重置读表状态");
    }

    @Override
    public void onDuBiao(ThirdHandData thirdHandData) {
                if (XHStringUtil.isEmpty(StartStr, false)) {
                    sendMessageToast("请输入起表号");
                    return;
                }
                if (XHStringUtil.isEmpty(EndStr, false)) {
                    sendMessageToast("请输入止表号");
                    return;
                }
                meterStart=Integer.valueOf(StartStr);
                meterEnd=Integer.valueOf(EndStr);
                meterReadStart = meterStart;
                meterReadEnd = meterEnd;
                currentNum = meterReadStart;
                meterArrayList.clear();
                meterMissArrayList.clear();
                multiTypeAdapter.notifyDataSetChanged();
                isReadMiss = false;
                meterMisssNum = 0;
                thirdHandData.setAccept(String.valueOf(meterArrayList.size()));
                thirdHandData.setDisAccept(String.valueOf(meterReadEnd - meterArrayList.size()));
                thirdHandData.setMeterMiss(String.valueOf(meterMisssNum));
                items.set(0,new BaseBinderModel(thirdHandData));
                readBiao(currentNum);
    }

    @Override
    public void onLouChao(ThirdHandData thirdHandData) {
                if (meterMissArrayList.size() == 0) {
                    sendMessageToast("没有漏抄的表");
                    return;
                }
                isReadMiss = true;
                XLog.i("蓝牙 meterMissArrayList  "+meterMissArrayList.toString());
                currentMissIndex = 0;
                currentNum = Integer.valueOf(meterMissArrayList.get(currentMissIndex).getMeterNumber());
                readBiao(currentNum);

    }

    @Override
    public void IsOpen(boolean IsOpen) {
       this.IsOpen=IsOpen;
    }
}
