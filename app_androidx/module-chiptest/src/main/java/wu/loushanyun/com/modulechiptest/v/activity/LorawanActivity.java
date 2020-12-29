package wu.loushanyun.com.modulechiptest.v.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.icu.text.CaseMap;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.elvishew.xlog.XLog;
import com.flyco.roundview.RoundTextView;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LorawanUtils;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.m.sat.SAtInstructRealizeFactory;
import com.wu.loushanyun.basemvp.m.sat.util.FourSimuBlueToothUtil;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;
import com.wu.loushanyun.basemvp.v.activity.BaseSnBlueToothActivity;

import java.math.BigDecimal;
import java.util.HashMap;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.share.Share2;
import met.hx.com.base.base.share.ShareContentType;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ByteConvertUtils;
import met.hx.com.librarybase.some_utils.TimeUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.modulechiptest.R;
import wu.loushanyun.com.modulechiptest.init.ChipCode;
import wu.loushanyun.com.modulechiptest.m.NewInfo;
import wu.loushanyun.com.modulechiptest.p.runner.MChipGetNewsInfoRunner;
import wu.loushanyun.com.modulechiptest.p.runner.MChipSetRxDelayRunner;

import static com.wu.loushanyun.basemvp.init.SnBlueToothTool.BLUE_SEND_TIME_OUT;

@Route(path = K.LorawanActivity)
public class LorawanActivity extends BaseSnBlueToothActivity {
    private ScrollView mScrollView;
    private TextView mTextPrint;
    private EditText mEditPrintNum;
    private RoundTextView mTextPrintCoon;
    private TextView mTextToken;
    private TextView mTextDangqianSn;
    private RoundTextView mBluetoothConn;
    private RoundTextView mButtonDayin;
    private RoundTextView mBluetoothDisconn;
    private SwitchCompat mSwitchSaveStatus;
    private ImageView mSystemStatus;
    private Spinner mPinlvSelect;
    private RoundTextView mPinlvSetting;
    private EditText mSetEditId;
    private RoundTextView mSetId;
    private Spinner mBeilvSelect;
    private EditText mEditMaichongStart;
    private Spinner mChuanganxinhaoSelect;
    private Spinner mJieruSelect;
    private EditText mFuwuId;
    private EditText mZhizhaoId;
    private RoundTextView mSetDishu;
    private RoundTextView mReadDishu;
    private TextView mTextChuchuangInfo;
    private RoundTextView mReadDianchi;
    private TextView mTextDianchiInfo;
    private RoundTextView mReadBanbenhao;
    private TextView mTextBanbenhaoInfo;
    private RoundTextView mReadLasttime;
    private TextView mTextLasttimeInfo;
    private RoundTextView mReadTongxun;
    private TextView mTextTongxunzhuangtaiInfo;
    private RoundTextView mJiaozhunSetting;
    private RoundTextView mValveSetting;
    private TextView mTextValveInfo;
    private RoundTextView mReadSingle;
    private TextView mTextSingleInfo;
    private RoundTextView mReadAll;
    private TextView mTextAllInfo;
    private EditText mReadEditNum;
    private RoundTextView mReadJizhong;
    private TextView mTextJizhongInfo;
    private EditText mReadEditID;
    private RoundTextView mReadID;
    private TextView mTextIDInfo;
    private Spinner mTimesSelect;
    private RoundTextView mSetTimes;
    private EditText mEditXinhaoqiangduStart;
    private EditText mEditXinhaoqiangduEnd;
    private EditText mEditXinzaobiStart;
    private EditText mEditXinzaobiEnd;
    private TextView mTextHuoqu;
    private RoundTextView mButtonQiangzhifasong;
    private TextView mTextHuoquNew;
    private RoundTextView mButtonDuquNew;
    private RoundTextView mRoundTextXiumian;
    private LinearLayout mLinearBlueTooth;
    private EditText mEditSearch;
    private RoundTextView mResetClear;
    private RoundTextView mResetClearList;
    private RecyclerView mDialogBlueRecycle;


    private TextView textToken;
    private Spinner mAtFasonggonglvSelect;
    private RoundTextView mAtFasonggonglvSetting;
    private Spinner mAtXindaocanshuSelect;
    private RoundTextView mAtXindaocanshuSetting;
    private Spinner mAtKuopinyinziSelect;
    private RoundTextView mAtKuopinyinziSetting;
    private EditText mAtEditRxdelay;
    private RoundTextView mAtRxdelaySetting;
    private RoundTextView mAtOneSetting;
    private RoundTextView mAtOneReading;
    private TextView mAtTextInfo;
    private Spinner mTimes_select;
    private RoundTextView mSet_times;
    private RoundTextView mReadId;
    private EditText mReadEditId;
    private TextView mTextIdInfo;
    private  TextView mTextQiangzhiInfo;
    private RoundTextView mReadQiangzhi;
    private Spinner mJixinSelct;
    private RoundTextView mSetXiuMian;
    private Switch jiHuoSwtich;
    private RoundTextView jiHuoSetting;


    private String[] kuopinyinziArray;

    private SensoroDevice sensoroDeviceChoose;
    private HashMap<String, String> hashMap;

    private int type;
    private SAtInstructRealizeFactory sAtInstructRealizeFactory;
    private boolean isAllSetting;
    private boolean isAllReading;
    private boolean hasAtSetting = false;
    private boolean isJiHuoAndXiuMian = false;//是否交互并休眠
    private boolean isJiHuo = true; //是否交互

    @Override
    protected int onChildLayout() {
        return R.layout.m_chip_activity_lorawan;
    }


    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        super.onInitAttribute(ba);
        ba.mTitleText = "LORAWAN基站民用";
    }

    @Override
    protected void initView() {
        super.initView();

        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mTextPrint = (TextView) findViewById(R.id.text_print);
        mEditPrintNum = (EditText) findViewById(R.id.edit_print_num);
        mTextPrintCoon = (RoundTextView) findViewById(R.id.text_print_coon);
        mTextToken = (TextView) findViewById(R.id.text_token);
        mTextDangqianSn = (TextView) findViewById(R.id.text_dangqian_sn);
        mBluetoothConn = (RoundTextView) findViewById(R.id.bluetooth_conn);
        mButtonDayin = (RoundTextView) findViewById(R.id.button_dayin);
        mBluetoothDisconn = (RoundTextView) findViewById(R.id.bluetooth_disconn);
        mSwitchSaveStatus = (SwitchCompat) findViewById(R.id.switch_save_status);
        mSystemStatus = (ImageView) findViewById(R.id.system_status);
        mPinlvSelect = (Spinner) findViewById(R.id.pinlv_select);
        mPinlvSetting = (RoundTextView) findViewById(R.id.pinlv_setting);
        mSetEditId = (EditText) findViewById(R.id.set_edit_id);
        mSetId = (RoundTextView) findViewById(R.id.set_id);
        mBeilvSelect = (Spinner) findViewById(R.id.beilv_select);
        mEditMaichongStart = (EditText) findViewById(R.id.edit_maichong_start);
        mChuanganxinhaoSelect = (Spinner) findViewById(R.id.chuanganxinhao_select);
        mJieruSelect = (Spinner) findViewById(R.id.jieru_select);
        mFuwuId = (EditText) findViewById(R.id.fuwu_id);
        mZhizhaoId = (EditText) findViewById(R.id.zhizhao_id);
        mJixinSelct= (Spinner) findViewById(R.id.jixin_select);
        mSetDishu = (RoundTextView) findViewById(R.id.set_dishu);
        mReadDishu = (RoundTextView) findViewById(R.id.read_dishu);
        mTextChuchuangInfo = (TextView) findViewById(R.id.text_chuchuang_info);
        mReadDianchi = (RoundTextView) findViewById(R.id.read_dianchi);
        mTextDianchiInfo = (TextView) findViewById(R.id.text_dianchi_info);
        mReadBanbenhao = (RoundTextView) findViewById(R.id.read_banbenhao);
        mTextBanbenhaoInfo = (TextView) findViewById(R.id.text_banbenhao_info);
        mReadLasttime = (RoundTextView) findViewById(R.id.read_lasttime);
        mTextLasttimeInfo = (TextView) findViewById(R.id.text_lasttime_info);
        mReadTongxun = (RoundTextView) findViewById(R.id.read_tongxun);
        mTextTongxunzhuangtaiInfo = (TextView) findViewById(R.id.text_tongxunzhuangtai_info);
        mJiaozhunSetting = (RoundTextView) findViewById(R.id.jiaozhun_setting);
        mValveSetting = (RoundTextView) findViewById(R.id.valve_setting);
        mTextValveInfo = (TextView) findViewById(R.id.text_valve_info);
        mReadSingle = (RoundTextView) findViewById(R.id.read_single);
        mTextSingleInfo = (TextView) findViewById(R.id.text_single_info);
        mReadAll = (RoundTextView) findViewById(R.id.read_all);
        mTextAllInfo = (TextView) findViewById(R.id.text_all_info);
        mReadEditNum = (EditText) findViewById(R.id.read_edit_num);
        mReadJizhong = (RoundTextView) findViewById(R.id.read_jizhong);
        mTextJizhongInfo = (TextView) findViewById(R.id.text_jizhong_info);
        mReadEditID = (EditText) findViewById(R.id.read_edit_ID);
        mReadID = (RoundTextView) findViewById(R.id.read_ID);
        mTextIDInfo = (TextView) findViewById(R.id.text_ID_info);
        mTimesSelect = (Spinner) findViewById(R.id.times_select);
        mSetTimes = (RoundTextView) findViewById(R.id.set_times);
        mEditXinhaoqiangduStart = (EditText) findViewById(R.id.edit_xinhaoqiangdu_start);
        mEditXinhaoqiangduEnd = (EditText) findViewById(R.id.edit_xinhaoqiangdu_end);
        mEditXinzaobiStart = (EditText) findViewById(R.id.edit_xinzaobi_start);
        mEditXinzaobiEnd = (EditText) findViewById(R.id.edit_xinzaobi_end);
        mTextHuoqu = (TextView) findViewById(R.id.text_huoqu);
        mButtonQiangzhifasong = (RoundTextView) findViewById(R.id.button_qiangzhifasong);
        mTextHuoquNew = (TextView) findViewById(R.id.text_huoqu_new);
        mButtonDuquNew = (RoundTextView) findViewById(R.id.button_duqu_new);
        mRoundTextXiumian = (RoundTextView) findViewById(R.id.round_text_xiumian);
        mLinearBlueTooth = (LinearLayout) findViewById(R.id.linear_blue_tooth);
        mEditSearch = (EditText) findViewById(R.id.edit_search);
        mResetClear = (RoundTextView) findViewById(R.id.reset_clear);
        mResetClearList = (RoundTextView) findViewById(R.id.reset_clear_list);
        mDialogBlueRecycle = (RecyclerView) findViewById(R.id.dialog_blue_recycle);

        mAtFasonggonglvSelect = (Spinner) findViewById(R.id.at_fasonggonglv_select);
        mAtFasonggonglvSetting = (RoundTextView) findViewById(R.id.at_fasonggonglv_setting);
        mAtXindaocanshuSelect = (Spinner) findViewById(R.id.at_xindaocanshu_select);
        mAtXindaocanshuSetting = (RoundTextView) findViewById(R.id.at_xindaocanshu_setting);
        mAtKuopinyinziSelect = (Spinner) findViewById(R.id.at_kuopinyinzi_select);
        mAtKuopinyinziSetting = (RoundTextView) findViewById(R.id.at_kuopinyinzi_setting);
        mAtEditRxdelay = (EditText) findViewById(R.id.at_edit_rxdelay);
        mAtRxdelaySetting = (RoundTextView) findViewById(R.id.at_rxdelay_setting);
        mAtOneSetting = (RoundTextView) findViewById(R.id.at_one_setting);
        mAtOneReading = (RoundTextView) findViewById(R.id.at_one_reading);
        mAtTextInfo = (TextView) findViewById(R.id.at_text_info);
        mTextQiangzhiInfo= (TextView) findViewById(R.id.text_qiangzhi_info);
        mReadQiangzhi= (RoundTextView) findViewById(R.id.read_qiangzhi);
        mSetXiuMian= (RoundTextView) findViewById(R.id.set_xiumian);

        mTimes_select= (Spinner) findViewById(R.id.times_select);
        mSet_times= (RoundTextView) findViewById(R.id.set_times);
        jiHuoSetting= (RoundTextView) findViewById(R.id.jihuo_setting);
        jiHuoSwtich= (Switch) findViewById(R.id.jihuo_switch);


        hashMap = new HashMap<>();
        sAtInstructRealizeFactory = new SAtInstructRealizeFactory();

        ArrayAdapter<String> arrayAdapterchuangan = new ArrayAdapter<String>(LorawanActivity.this, R.layout.l_loushanyun_item_spinner, LorawanUtils.getAllChuanGan());
        mChuanganxinhaoSelect.setAdapter(arrayAdapterchuangan);
        ArrayAdapter<String> arrayAdapterjieru = new ArrayAdapter<String>(LorawanActivity.this, R.layout.l_loushanyun_item_spinner, LorawanUtils.getJieRu());
        mJieruSelect.setAdapter(arrayAdapterjieru);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(LorawanActivity.this, R.layout.l_loushanyun_item_spinner, sAtInstructRealizeFactory.getAllMoShi());
        mAtXindaocanshuSelect.setAdapter(arrayAdapter);
        ArrayAdapter<String> arrayAdapterJiaoHu = new ArrayAdapter<String>(LorawanActivity.this, R.layout.l_loushanyun_item_spinner, LorawanUtils.getAllWangLuoJiaoHu());
        mTimes_select.setAdapter(arrayAdapterJiaoHu);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(LorawanActivity.this, R.layout.l_loushanyun_item_spinner, LorawanUtils.getAllPinLv());
        mPinlvSelect.setAdapter(arrayAdapter1);
        ArrayAdapter<String> arrayAdapterBeiLv = new ArrayAdapter<String>(this, R.layout.l_loushanyun_item_spinner, LorawanUtils.getAllBeiLv());
        mBeilvSelect.setAdapter(arrayAdapterBeiLv);
        ArrayAdapter<String> arrayAdapterJiXin = new ArrayAdapter<String>(this, R.layout.l_loushanyun_item_spinner, LorawanUtils.getAllJiXin());
        mJixinSelct.setAdapter(arrayAdapterJiXin);


        mBluetoothConn.setOnClickListener(v -> {
            if (mTextDangqianSn.getText().toString().contains("当前连接的SN设备")) {
                sendMessageToast("当前已连接设备");
                return;
            }
            snBlueToothTool.connectBlueTooth(sensoroDeviceChoose);
            //  clearAllText();
        });
        mBluetoothDisconn.setOnClickListener(v -> {
            snBlueToothTool.disconnect();
            mTextDangqianSn.setText("当前未连接SN设备");
        });

        initTestClick();
    }



    @Override
    protected void onClickBlueListListener(SensoroDevice sensoroDevice) {
        super.onClickBlueListListener(sensoroDevice);
        this.sensoroDeviceChoose = sensoroDevice;
    }

    private void initTestClick() {
        mAtXindaocanshuSetting.setOnClickListener(this::OnBlueToothClick);
        mAtKuopinyinziSetting.setOnClickListener(this::OnBlueToothClick);
        mAtFasonggonglvSetting.setOnClickListener(this::OnBlueToothClick);
        mAtRxdelaySetting.setOnClickListener(this::OnBlueToothClick);
        mAtOneSetting.setOnClickListener(this::OnBlueToothClick);
        mAtOneReading.setOnClickListener(this::OnBlueToothClick);
        mJiaozhunSetting.setOnClickListener(this::OnBlueToothClick);

        mButtonQiangzhifasong.setOnClickListener(this::OnBlueToothClick);
        mSystemStatus.setOnClickListener(this::OnBlueToothClick);
        mValveSetting.setOnClickListener(this::OnBlueToothClick);
        mReadSingle.setOnClickListener(this::OnBlueToothClick);
        mReadJizhong.setOnClickListener(this::OnBlueToothClick);
        mRoundTextXiumian.setOnClickListener(this::OnBlueToothClick);
        mSet_times.setOnClickListener(this::OnBlueToothClick);
        mTextPrintCoon.setOnClickListener(this::OnClick);
        //     mTextToken.setOnClickListener(this::OnClick);
        mButtonDuquNew.setOnClickListener(this::OnClick);
        mPinlvSetting.setOnClickListener(this::OnBlueToothClick);
        mReadAll.setOnClickListener(this::OnBlueToothClick);
      //  mReadId.setOnClickListener(this::OnBlueToothClick);
        mReadDishu.setOnClickListener(this::OnBlueToothClick);
        mSetId.setOnClickListener(this::OnBlueToothClick);
        mSetDishu.setOnClickListener(this::OnBlueToothClick);
        mReadDianchi.setOnClickListener(this::OnBlueToothClick);
        mReadBanbenhao.setOnClickListener(this::OnBlueToothClick);
        mReadLasttime.setOnClickListener(this::OnBlueToothClick);
        mReadTongxun.setOnClickListener(this::OnBlueToothClick);
        mReadQiangzhi.setOnClickListener(this::OnBlueToothClick);
        mTextToken.setOnClickListener(this::OnBlueToothClick);
        mSetXiuMian.setOnClickListener(this::OnBlueToothClick);
        jiHuoSetting.setOnClickListener(this::OnBlueToothClick);
    }


    @Override
    public void onChildConnectFailed(int i) {

    }


    @Override
    public void onChildConnectSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextDangqianSn.setText("当前连接的SN设备:" + sensoroDeviceChoose.getSerialNumber().toUpperCase());
                try {
                    isAllReading = true;
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).getFourWriteBytes(), "识别模块中...", true, 10000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.text_print_coon:

                break;
            case R.id.button_duqu_new:
                pushEventNoProgress(ChipCode.MChipGetNewsInfoRunner, sensoroDeviceChoose.sn, "1", "5");
                break;
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
//        if (type != 6) {
//            sendMessageToast("识别不是2号模组，请选择正确的模组");
//            return;
//        }
//        writeCode = 0;
//        canPrint = false;
        switch (view.getId()) {
            case R.id.text_token:
                String text = "SN:" + sensoroDeviceChoose.sn + ";Token:" + hashMap.get(SAtInstructParams.sAtInstructToken);
                //获取剪贴板管理器：
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 创建普通字符型ClipData
                ClipData mClipData = ClipData.newPlainText("Label", text);
                // 将ClipData内容放到系统剪贴板里。
                cm.setPrimaryClip(mClipData);
                sendMessageToast("已复制到剪切板");
                new Share2.Builder(LorawanActivity.this)
                        .setContentType(ShareContentType.TEXT)
                        .setTitle("打印SN")
                        .setTextContent(text)
                        .setOnActivityResult(1000)
                        .build()
                        .shareBySystem();
                break;

            case R.id.at_xindaocanshu_setting:
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getFourWriteBytes(mAtXindaocanshuSelect.getSelectedItem().toString().trim()), "设置信道");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isAllSetting = false;
                break;
            case R.id.at_kuopinyinzi_setting:
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).getFourWriteBytes(mAtKuopinyinziSelect.getSelectedItem().toString().trim()), "设置扩频因子");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isAllSetting = false;
                break;
            case R.id.at_fasonggonglv_setting:
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).getTwoWriteBytes(mAtFasonggonglvSelect.getSelectedItem().toString().trim()), "设置发送功率");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isAllSetting = false;

                break;
            case R.id.at_rxdelay_setting:
                if (Integer.valueOf(mAtEditRxdelay.getText().toString().trim()) > 5 || Integer.valueOf(mAtEditRxdelay.getText().toString().trim()) < 1) {
                    sendMessageToast("只能设置1-5范围内");
                    return;
                }
                pushEvent(ChipCode.MChipSetRxDelayRunner, sensoroDeviceChoose.sn, mAtEditRxdelay.getText().toString().trim());
                break;
            case R.id.at_one_setting:
                try {
                    if (Integer.valueOf(mAtEditRxdelay.getText().toString().trim()) > 5 || Integer.valueOf(mAtEditRxdelay.getText().toString().trim()) < 1) {
                        sendMessageToast("只能设置1-5范围内");
                        return;
                    }
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getTwoWriteBytes(mAtXindaocanshuSelect.getSelectedItem().toString().trim()), "设置信道");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isAllSetting = true;
                break;
            case R.id.at_one_reading:
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getFourReadBytes(), "读取信道中...");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isAllReading = true;
                mAtTextInfo.setText("");
                break;
            case R.id.jiaozhun_setting:
                snBlueToothTool.write(LorawanUtils.getjiaozhun(), "正在校准模组时间");
                break;
            case R.id.jihuo_setting:
                snBlueToothTool.write(LorawanUtils.getjihuo(jiHuoSwtich.isChecked()), "正在激活或停用设备");
                break;
            case R.id.button_qiangzhifasong:

                clearAllText();
                try {
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructForcedSend).getTwoReadBytes("15527919058")
                            , "强制发送", true, BLUE_SEND_TIME_OUT);
                } catch (Exception e) {
                    e.printStackTrace();
                }

              //  snBlueToothTool.write(LorawanUtils.getQiangZhiSend(), "正在强制发送");
                break;
            case R.id.read_qiangzhi:
                mTextQiangzhiInfo.setText("");
                snBlueToothTool.write(LorawanUtils.getQiangZhi()
                        , "发送0x04命令", true, BLUE_SEND_TIME_OUT);

                break;
            case R.id.system_status:
//                isJiHuoAndXiuMian = false;
//                if (isJiHuo) {
//                    closeJiHuo();
//                } else {
//                    openJiHuo();
//                }
                break;

            case R.id.valve_setting:
                snBlueToothTool.write(LorawanUtils.getvavle(), "正在设置阀门");
                break;
            case R.id.read_single:
                mTextSingleInfo.setText("");
                snBlueToothTool.write(LorawanUtils.getSingleReading(), "正在读取表信息");
                break;
            case R.id.read_jizhong:
                if (XHStringUtil.isEmpty(mReadEditNum.getText().toString(), true)) {
                    sendMessageToast("请填写表号");
                    return;
                }
                snBlueToothTool.write(LorawanUtils.getJizhongReading(Integer.valueOf(mReadEditNum.getText().toString())), "正在读取表信息");
                break;
            case R.id.round_text_xiumian:
                snBlueToothTool.write(LorawanUtils.getxiumian(), "正在休眠");
                break;
            case R.id.set_times:
                snBlueToothTool.write(LorawanUtils.getLorawanTimes(mTimes_select.getSelectedItem().toString().replaceAll("最大发送", "").replaceAll("次", "")), "正在最大发送次数");
                break;
            case R.id.pinlv_setting:

                snBlueToothTool.write(LorawanUtils.getPinlvBytes(mPinlvSelect.getSelectedItem().toString().trim()), "设置发送频率" + mPinlvSelect.getSelectedItem().toString().trim());
                break;
            case R.id.read_ID:
                if (XHStringUtil.isEmpty(mReadEditId.getText().toString(), true)) {
                    sendMessageToast("请填写设备ID");
                    return;
                }
                snBlueToothTool.write(LorawanUtils.getIDReading(Integer.valueOf(mReadEditId.getText().toString())), "通过表设备ID读取表的信息");
                break;
            case R.id.set_id:
                if (XHStringUtil.isEmpty(mSetEditId.getText().toString(), true)) {
                    sendMessageToast("请填写设备ID");
                    return;
                }
                snBlueToothTool.write(LorawanUtils.getIDSetting(mSetEditId.getText().toString()), "正在设置设备ID");
                break;
            case R.id.set_dishu:
                if (XHStringUtil.isEmpty(mEditMaichongStart.getText().toString(), true)) {
                    sendMessageToast("请填写底数");
                    return;
                }
                if (XHStringUtil.isEmpty(mFuwuId.getText().toString(), true)) {
                    sendMessageToast("请填写服务商");
                    return;
                }
                if (XHStringUtil.isEmpty(mZhizhaoId.getText().toString(), true)) {
                    sendMessageToast("请填写制造商");
                    return;
                }

                snBlueToothTool.write(LorawanUtils.getChuChangWrite(mEditMaichongStart.getText().toString(), mBeilvSelect.getSelectedItem().toString(),mChuanganxinhaoSelect.getSelectedItem().toString(),mJieruSelect.getSelectedItem().toString(),Integer.parseInt(mFuwuId.getText().toString()),Integer.parseInt(mZhizhaoId.getText().toString()),mJixinSelct.getSelectedItem().toString()), "正在设置出厂设置");
                break;
            case R.id.read_dishu:
                snBlueToothTool.write(LorawanUtils.getChuChangReading(), "正在读取出厂设置");
                break;
            case R.id.read_dianchi:
                snBlueToothTool.write(LorawanUtils.getDianchiReading(), "正在读取电池电压");
                break;
            case R.id.read_all:
                snBlueToothTool.write(LorawanUtils.getALLReading(), "正在串表连接个数");
                break;
            case R.id.read_banbenhao:
                snBlueToothTool.write(LorawanUtils.getBanbenReading(), "正在读取版本号");
                break;
            case R.id.read_lasttime:
                snBlueToothTool.write(LorawanUtils.getLastTimeReading(), "正在读取最后上送时间");
                break;
            case R.id.read_tongxun:
                snBlueToothTool.write(LorawanUtils.getTongXuStateReading(), "正在读取通讯状态");
                break;
            case R.id.set_xiumian:
                snBlueToothTool.write(LorawanUtils.getxiumian(), "正在设置设备休眠");
                break;

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
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).parseTwoRNotifyBytes(result));
                        if (isAllReading) {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).getTwoReadBytes(), "读取发送功率");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructSendingPower + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).parseTwoRNotifyBytes(result));
                        if (isAllReading) {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructFirmwareVersion).getTwoReadBytes(), "读取固件版本");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructFirmwareVersion + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructFirmwareVersion).parseTwoRNotifyBytes(result));
                        if (isAllReading) {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).getTwoReadBytes(), "读取扩频因子");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructSpreadingFactor + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).parseTwoRNotifyBytes(result));
                        if (isAllReading) {
                            snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRxDelay).getTwoReadBytes(), "读取RxDelay");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructRxDelay + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRxDelay).parseTwoRNotifyBytes(result));
                        if (isAllReading) {
                            setAtDuShu(hashMap);
                            //      snBlueToothTool.write(DataParser.CMD_SYSTEM_STATUS, "正在读取设备激活状态");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructToken + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructToken).parseTwoRNotifyBytes(result));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mTextToken.setText("当前设备Token和SN(点击复制到剪贴板):\n" + "SN:" + sensoroDeviceChoose.sn + "\nToken:" + hashMap.get(SAtInstructParams.sAtInstructToken));
                                try {
                                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructChannel).getTwoReadBytes(), "读取信道中...");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
//                                isAllReading = true;
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructChannel + "-W")) {
                    try {
                        sendMessageToast("信道设置成功");
                        if (isAllSetting) {
                            try {
                                snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSendingPower).getTwoWriteBytes(mAtFasonggonglvSelect.getSelectedItem().toString().trim()), "设置发送功率");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructSendingPower + "-W")) {
                    try {
                        sendMessageToast("设置发送功率成功");
                        if (isAllSetting) {
                            try {
                                snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructSpreadingFactor).getTwoWriteBytes(mAtKuopinyinziSelect.getSelectedItem().toString().trim()), "设置扩频因子");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructSpreadingFactor + "-W")) {
                    try {
                        sendMessageToast("设置扩频因子成功");
                        pushEvent(ChipCode.MChipSetRxDelayRunner, sensoroDeviceChoose.sn, mAtEditRxdelay.getText().toString().trim());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructRxDelay + "-W")) {
                    try {
                        sendMessageToast("设置RxDelay成功");
                        hasAtSetting = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructRSSI + "-R")) {
                    try {
                        hashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRSSI).parseFourRNotifyBytes(result));
                        String[] strings = hashMap.get(SAtInstructParams.sAtInstructRSSI).split(",");
                        int rssi = Integer.valueOf(strings[0]);
                        int snr = Integer.valueOf(strings[1]);
                        int rssiStart = Integer.valueOf(mEditXinhaoqiangduStart.getText().toString());
                        int rssiEnd = Integer.valueOf(mEditXinhaoqiangduEnd.getText().toString());
                        int snrStart = Integer.valueOf(mEditXinzaobiStart.getText().toString());
                        int snrEnd = Integer.valueOf(mEditXinzaobiEnd.getText().toString());
                        String status;
                        if (rssi > rssiStart && rssi < rssiEnd && snr > snrStart && snr < snrEnd) {
                            status = "合格";
                        } else {
                            status = "不合格";
                        }
                        runOnUiThread(() -> {
                            String sb = "信号强度：" + rssi + "\n" + "信噪比：" + snr + "\n" + "状态：" + status;
                            mTextHuoquNew.setText(sb);
                         //   pushEventNoProgress(ChipCode.MChipGetNewsInfoRunner, sensoroDeviceChoose.sn, "1", "5");
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructForcedSend + "+SEND2")) {
                    try {
                        sendMessageToast("强制发送成功");
                        snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRSSI).getTwoReadBytes(), "读取信号强度");
                        isAllReading = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructForcedSend + "+SEND3")) {
                    try {
                        sendMessageToast("强制发送失败");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    sendMessageToast(resultAt);
                }
                break;
            case 0x11:
                try {
                    type = LorawanUtils.getModuleType(result);
                    XLog.i("Type====" + type);
                    hashMap.putAll(LorawanUtils.getLorawanInfoAll(result));
                    initSnAgreement(Double.valueOf("1.07"));
                    if (type == 6) {
                        snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructToken).getTwoReadBytes(), "读取Token...");
                    } else {
                        sendMessageToast("识别不是LORAWAN模组，请选择正确的模组");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case 0x01:
                if (result[4] == 0) {
                    sendMessageToast("系统停用");
                } else if (result[4] == 1) {
                    sendMessageToast("系统激活");
                }
                break;
            case 0x02:
                if (result[3] == 0) {
                    sendMessageToast("休眠成功");
                } else {
                    sendMessageToast("休眠失败");
                }
                break;
            case 0x03:
                if (result[3] == 0) {
                    sendMessageToast("设置成功");
                } else {
                    sendMessageToast("设置失败");
                }
                break;
            case 0x04:
                if (result[3] == 0) {
                    sendMessageToast("0x04命令发送成功");
                    try {
                        hashMap.putAll(LorawanUtils.getQiangZhiSend(result));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                StringBuilder sb = new StringBuilder();
                                sb.append("强制发送是否成功:  ");
                                sb.append(LorawanUtils.getQiangzhifasongStringByCode(hashMap.get(MapParams.强制发送成功)));
                                sb.append("   最大发送次数:  ");
                                sb.append(hashMap.get(MapParams.最大发送次数));
                                sb.append("   发送次数:  ");
                                sb.append(hashMap.get(MapParams.发送次数));
                                mTextQiangzhiInfo.setText(sb);
                                try {
                                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRSSI).getTwoReadBytes(), "读取信号强度");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    sendMessageToast("强制发送失败");
                }
                break;
            case 0x05:
                if (result[3] == 0) {
                    sendMessageToast("校准时间成功");
                } else {
                    sendMessageToast("校准时间失败");
                }
                break;
            case 0x06:
                if (result[3] == 0) {
                    sendMessageToast("设置频率成功");
                } else if (result[3] == 1) {
                    sendMessageToast("设置频率失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                }
                break;
            case 0x07:
                if (result[3] == 0) {
                    sendMessageToast("读取电池电压成功");
                    StringBuffer stringBuffer = new StringBuffer();
                    StringBuffer stringBuffer2 = new StringBuffer();
                    for (int i = 3; i >= 0; i--) {
                        stringBuffer2.append(Integer.toHexString(result[i + 4]));
                        stringBuffer.append(ByteConvertUtils.conver2BinaryStr(result[i + 4]));

                    }
                    XLog.i("stringBuffer2==="+stringBuffer2.toString());
                    XLog.i("stringBuffer2Int====="+Integer.valueOf(stringBuffer.toString()));
                    int zhi=Integer.parseInt(stringBuffer2.toString(),16);
                    hashMap.put(MapParams.电池电压, String.valueOf ((float)zhi/100));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (Double.valueOf(hashMap.get(MapParams.电池电压)) < 3.5) {
                                mTextDianchiInfo.setText(hashMap.get(MapParams.电池电压)+"V" + "\n" + "不合格");
                            } else {
                                mTextDianchiInfo.setText(hashMap.get(MapParams.电池电压)+"V" + "\n" + "合格");
                            }
                        }
                    });



                } else if (result[3] == 1) {
                    sendMessageToast("读取电池电压失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                }
                break;
            case 0x08:
                if (result[3] == 0) {
                    sendMessageToast("设置设备ID成功");
                } else if (result[3] == 1) {
                    sendMessageToast("设置设备ID失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                }
                break;
            case 0x09:
                if (result[3] == 0) {
                    sendMessageToast("设置倍率成功");
                } else if (result[3] == 1) {
                    sendMessageToast("设置频率失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                }
                break;

            case 0x12:
                if (result[3] == 0) {
                    sendMessageToast("设置出厂设置成功");
                } else if (result[3] == 1) {
                    sendMessageToast("设置出厂设置失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                }
                break;
            case 0x13:
                if (result[3] == 0) {
                    sendMessageToast("读取版本号成功");
                    hashMap.put(MapParams.产品固件版本号, String.valueOf(result[4]));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTextBanbenhaoInfo.setText("产品固件版本号  :"+hashMap.get(MapParams.产品固件版本号));
                        }
                    });

                } else if (result[3] == 1) {
                    sendMessageToast("读取版本号失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                }
                break;
            case 0x14:
                if (result[3] == 0) {
                    sendMessageToast("读取最后上送时间成功");
                    hashMap.put(MapParams.数据性质, LorawanUtils.getShuJuTypeByCode(String.valueOf(result[4])));

                    long tmp;
                    tmp = 0;
                    for (int i = 6; i > 0; i--) {
                        tmp = (tmp * 256) + (result[i + 4] & 0xff);
                    }
                    hashMap.put(MapParams.最后一次采样时间, String.valueOf(tmp/1000));

//                        StringBuffer stringBuffer = new StringBuffer();
//                        for (int i = 6; i >= 0; i--) {
//                            stringBuffer.append(result[i + 5]);
//                        }

                  //  hashMap.put(MapParams.最后一次采样时间, String.valueOf(ByteConvertUtils.formatNumber(stringBuffer.toString(),2)));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTextLasttimeInfo.setText("数据性质  :"+hashMap.get(MapParams.数据性质)+"\n"+
                                    "最后上送时间"+TimeUtils.timeStamp2Date(hashMap.get(MapParams.最后一次采样时间),null)
                            );
                        }
                    });

                } else if (result[3] == 1) {
                    sendMessageToast("读取最后上送时间失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                }
                break;
            case 0x15:
                if (result[3] == 0) {
                    sendMessageToast("读取通讯状态成功");
                    if(result[4]==0x00){
                        hashMap.put(MapParams.通讯状态,"正常");
                    }else {
                        hashMap.put(MapParams.通讯状态,"测试");
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mTextTongxunzhuangtaiInfo.setText("通讯状态  :"+hashMap.get(MapParams.通讯状态));
                        }
                    });

                } else if (result[3] == 1) {
                    sendMessageToast("读取通讯状态失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                }
                break;
            case 0x16:
                if (result[3] == 0) {
                    sendMessageToast("读取出厂设置成功");
                    try {
                        hashMap.putAll(LorawanUtils.getReadChuChangSetting(result));
                        setChuChangText(hashMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (result[3] == 1) {
                    sendMessageToast("读取出厂设置失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                }
                break;

            case 0x22:
                if (result[3] == 0) {
                    try {
                        hashMap.putAll(LorawanUtils.getLorawanSingle(result));
                        setSingleText(hashMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (result[3] == 1) {
                    sendMessageToast("读取失败");
                }
                break;
            case 0x23:
                if (result[3] == 0) {
                    sendMessageToast("操作成功");
                    hashMap.put(MapParams.阀门状态, String.valueOf(result[4] & 0xff) == "0" ? "开" : "关");
                    mTextValveInfo.setText(hashMap.get(MapParams.阀门状态));


                } else if (result[3] == 1) {
                    sendMessageToast("操作失败");
                } else if (result[3] == 2) {
                    sendMessageToast("无效命令字");
                } else if (result[3] == 3) {
                    sendMessageToast("无效参数");
                } else if (result[3] == 4) {
                    sendMessageToast("没有阀门，无法配置");
                }
                break;
            case 0x33:
                if (result[3] == 0) {
                    try {
                        hashMap.putAll(LorawanUtils.getLorawanJiZhong(result));
                        setJiZhongText(hashMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (result[3] == 1) {
                    sendMessageToast("读取失败");
                }

                break;
            case 0x34:
                if (result[3] == 0) {
                    try {
                        hashMap.putAll(LorawanUtils.getLorawanID(result));
                        setIDText(hashMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (result[3] == 1) {
                    sendMessageToast("读取失败");
                }
                break;
            case 0x35:
                if (result[3] == 0) {
                    try {
                        hashMap.putAll(LorawanUtils.getLorawanALL(result));
                        setAllText(hashMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (result[3] == 1) {
                    sendMessageToast("读取失败");
                }
                break;

        }
    }

    private void clearAllText() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextHuoquNew.setText("");
            }
        });

    }


    private void initSnAgreement(double hardVersion) {
        sAtInstructRealizeFactory.initHardVersion(hardVersion);
    }


    @Override
    public void onChildWriteSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
              //  textNum.setText("");
             //   blueSnTv.setText(sensoroDeviceChoose.getSerialNumber().toUpperCase());
              //  tvParamConnection.setText("已连接");
            //    hashMap.clear();
             //   secondParameterEditIdStart.setText(LouShanYunUtils.getTimeID());
//                try {
//                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sInstructDiscern).getTwoWriteBytes(), "识别模块中...", true, 6000);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }


            }
        });
    }

    @Override
    public void onChildWriteFailure(int i) {

    }

    @Override
    protected void initEventListener() {
        registerEventRunner(ChipCode.MChipSetRxDelayRunner, new MChipSetRxDelayRunner());
        registerEventRunner(ChipCode.MChipGetNewsInfoRunner, new MChipGetNewsInfoRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == ChipCode.MChipSetRxDelayRunner) {
            if (event.isSuccess()) {
                int codeReturn = (int) event.getReturnParamAtIndex(0);
                String msg = (String) event.getReturnParamAtIndex(1);
                if (codeReturn == 0) {
                    try {
                        snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRxDelay).getFourWriteBytes(mAtEditRxdelay.getText().toString().trim()), "设置RxDelay");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    sendMessageToast(msg);
                }
            }
        }else  if (code == ChipCode.MChipGetNewsInfoRunner) {
            if (event.isSuccess()) {
                NewInfo newInfo = (NewInfo) event.getReturnParamAtIndex(0);
                mTextHuoquNew.setText("");
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < newInfo.getData().size(); i++) {
                    if (i < 2) {
                        stringBuffer.append("时间:" + TimeUtils.milliseconds2String(newInfo.getData().get(i).getSendTime()) + "；");
                        stringBuffer.append("信号强度:" + newInfo.getData().get(i).getRssi() + "；");
                        stringBuffer.append("信噪比:" + newInfo.getData().get(i).getSnr() + "；\n");
                    }
                }
                mTextHuoquNew.setText(stringBuffer.toString());
            }
        }

    }


    private void setDianchiText(HashMap<String, String> hashMap) {

        StringBuilder sb = new StringBuilder();
        sb.append("电池电压:  ");
        sb.append(String.format("%.2f", Double.valueOf(hashMap.get(MapParams.电池电压))) + "V");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextDianchiInfo.setText(sb.toString());
            }
        });
    }

    private void setChuChangText(HashMap<String, String> hashMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("脉冲底数:  ");
        sb.append(hashMap.get(MapParams.脉冲底数));
        sb.append("\n倍率:  ");
        sb.append(hashMap.get(MapParams.倍率));
        sb.append("\n传感信号:  ");
        sb.append(hashMap.get(MapParams.传感信号));
        sb.append("\n接入类型:  ");
        sb.append(hashMap.get(MapParams.接入类型));
        sb.append("\n服务商ID:  ");
        sb.append(hashMap.get(MapParams.服务商ID));
        sb.append("\n制造商ID:  ");
        sb.append(hashMap.get(MapParams.制造商ID));
        sb.append("\n机芯:  ");
        sb.append(hashMap.get(MapParams.机芯类型));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextChuchuangInfo.setText(sb.toString());
            }
        });
    }


    private void setAllText(HashMap<String, String> hashMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("连接数:  ");
        sb.append(hashMap.get(MapParams.连接数));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextAllInfo.setText(sb.toString());
            }
        });
    }

    private void setIDText(HashMap<String, String> hashMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("表磁场外干扰:  ");
        sb.append(LorawanUtils.getBiaoCiChangGanRaoStringByCode(hashMap.get(MapParams.表磁场外干扰)));
        sb.append("\n表拆卸状态:  ");
        sb.append(LorawanUtils.getCaiXieStateStringByCode(hashMap.get(MapParams.表拆卸状态)));
        sb.append("\n表读表状态:  ");
        sb.append(LorawanUtils.getBiaoDuBiaoStateStringByCode(hashMap.get(MapParams.表读表状态)));
        sb.append("\n表电源状态:  ");
        sb.append(LorawanUtils.getDianYuanStateStringByCode(hashMap.get(MapParams.表电源状态)));
        sb.append("\n表倒流状态:  ");
        sb.append(LorawanUtils.getBiaoDaoLiuStateStringByCode(hashMap.get(MapParams.表倒流状态)));
        sb.append("\n寻址方式:  ");
        sb.append(LorawanUtils.getXuanZhiFangShiStringByCode(hashMap.get(MapParams.寻址方式)));
        sb.append("\n连接的表个数:  ");
        sb.append(hashMap.get(MapParams.连接的表个数));
        sb.append("\n表ID:  ");
        sb.append(hashMap.get(MapParams.表ID));
        sb.append("\n表计数:  ");
        sb.append(hashMap.get(MapParams.表计数));
        sb.append("\n表号:  ");
        sb.append(hashMap.get(MapParams.表号));
        sb.append("\n表倍率:  ");
        sb.append(LorawanUtils.getBiaoBeilvStringByCode(hashMap.get(MapParams.表倍率)));
        sb.append("\n表累积倒流数:  ");
        sb.append(hashMap.get(MapParams.表累积倒流数));
        sb.append("\n表传感信号:  ");
        sb.append(LorawanUtils.getChuanGanXinHaoByString(hashMap.get(MapParams.表传感信号)));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextIdInfo.setText(sb.toString());
            }
        });
    }

    private void setSingleText(HashMap<String, String> hashMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("设备ID:  ");
        sb.append(hashMap.get(MapParams.设备ID));
        sb.append("复合电容:  ");
        sb.append(LorawanUtils.getFuHedianrongStringByCode(hashMap.get(MapParams.复合电容)));
        sb.append("\n欠压状态:  ");
        sb.append(LorawanUtils.getQianYaStateStringByCode(hashMap.get(MapParams.欠压状态)));
        sb.append("\n流向状态:  ");
        sb.append(LorawanUtils.getLiuXiangstateStringByCode(hashMap.get(MapParams.流向状态)));
        sb.append("\n拆卸状态:  ");
        sb.append(LorawanUtils.getCaiXieStateStringByCode(hashMap.get(MapParams.拆卸状态)));
        sb.append("\n阀门有无:  ");
        sb.append(LorawanUtils.getValveStringByCode(hashMap.get(MapParams.阀门有无)));
        sb.append("\n阀门状态:  ");
        sb.append(LorawanUtils.getValvestateStringByCode(hashMap.get(MapParams.阀门状态)));
        sb.append("\n磁干扰:  ");
        sb.append(LorawanUtils.getCiStateStringByCode(hashMap.get(MapParams.磁干扰)));
        sb.append("\n模拟信号:  ");
        sb.append(LorawanUtils.getXinHaoStateStringByCode(hashMap.get(MapParams.模拟信号)));
        sb.append("\n机芯类型:  ");
        sb.append(LorawanUtils.getJiXinStringByCode(hashMap.get(MapParams.机芯类型)));
        sb.append("\n最后计数时间:  ");
        sb.append(LorawanUtils.getLastTimeStringByCode(hashMap.get(MapParams.最后计数时间)));
        sb.append("\n正计数:  ");
        sb.append(hashMap.get(MapParams.正计数));
        sb.append("\n反计数:  ");
        sb.append(hashMap.get(MapParams.反计数));
        sb.append("\n倍率:  ");//TODO 如是数字信号，配置小数点位 这里还未完善
        sb.append(LorawanUtils.getBiaoBeilvStringByCode(hashMap.get(MapParams.倍率)));
        sb.append("\n预警电压值:  ");
        sb.append(hashMap.get(MapParams.预警电压值));


//        if(hashMap.get(MapParams.模拟信号).equals("1")){
//            sb.append("\n六点的读数:  ");
//            sb.append(hashMap.get(MapParams.六点的读数));
//            sb.append("\n十二点的读数:  ");
//            sb.append(hashMap.get(MapParams.十二点的读数));
//            sb.append("\n十八点的读数:  ");
//            sb.append(hashMap.get(MapParams.十八点的读数));
//            sb.append("\n二十四点的读数:  ");
//            sb.append(hashMap.get(MapParams.二十四点的读数));
//        }else {
            sb.append("\n零点至三点的脉冲数:  ");
            sb.append(hashMap.get(MapParams.零点至三点的脉冲数));
            sb.append("\n三点至六点的脉冲数:  ");
            sb.append(hashMap.get(MapParams.三点至六点的脉冲数));
            sb.append("\n六点至九点的脉冲数:  ");
            sb.append(hashMap.get(MapParams.六点至九点的脉冲数));
            sb.append("\n九点至十二点的脉冲数:  ");
            sb.append(hashMap.get(MapParams.九点至十二点的脉冲数));
            sb.append("\n十二点至十五点的脉冲数:  ");
            sb.append(hashMap.get(MapParams.十二点至十五点的脉冲数));
            sb.append("\n十五点至十八点的脉冲数:  ");
            sb.append(hashMap.get(MapParams.十五点至十八点的脉冲数));
            sb.append("\n十八点至二十一点的脉冲数:  ");
            sb.append(hashMap.get(MapParams.十八点至二十一点的脉冲数));
            sb.append("\n二十一点至二十四点的脉冲数:  ");
            sb.append(hashMap.get(MapParams.二十一点至二十四点的脉冲数));
//        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextSingleInfo.setText(sb.toString());
            }
        });
    }


    private void setJiZhongText(HashMap<String, String> hashMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("第一块表磁场外干扰:  ");
        sb.append(LorawanUtils.getCiGanraoStringByCode(hashMap.get(MapParams.第一块表磁场外干扰)));
        sb.append("第一块表拆卸状态:  ");
        sb.append(LorawanUtils.getCaiXieStateStringByCode(hashMap.get(MapParams.第一块表拆卸状态)));
        sb.append("第一块表读表状态:  ");
        sb.append(LorawanUtils.getDuquStateStringByCode(hashMap.get(MapParams.第一块表读表状态)));
        sb.append("第一块表电源状态:  ");
        sb.append(LorawanUtils.getDianYuanStateStringByCode(hashMap.get(MapParams.第一块表电源状态)));
        sb.append("第一块表倒流状态:  ");
        sb.append(LorawanUtils.getLiuXiangStateStringByCode(hashMap.get(MapParams.第一块表倒流状态)));
        sb.append("寻址方式:  ");
        sb.append(LorawanUtils.getFindWayStringByCode(hashMap.get(MapParams.寻址方式)));
        sb.append("是否有第二块表:  ");
        sb.append(LorawanUtils.getIsCunZaiTwoStringByCode(hashMap.get(MapParams.是否有第二块表)));
        sb.append("\n连接数:  ");
        sb.append(hashMap.get(MapParams.连接数));
        sb.append("\n第一块表ID:  ");
        sb.append(hashMap.get(MapParams.第一块表ID));
        sb.append("\n第一块表计数:  ");
        sb.append(hashMap.get(MapParams.第一块表计数));
        sb.append("第二块表磁场外干扰:  ");
        sb.append(LorawanUtils.getCiGanraoStringByCode(hashMap.get(MapParams.第二块表磁场外干扰)));
        sb.append("第二块表拆卸状态:  ");
        sb.append(LorawanUtils.getCaiXieStateStringByCode(hashMap.get(MapParams.第二块表拆卸状态)));
        sb.append("第二块表读表状态:  ");
        sb.append(LorawanUtils.getDuquStateStringByCode(hashMap.get(MapParams.第二块表读表状态)));
        sb.append("第二块表电源状态:  ");
        sb.append(LorawanUtils.getDianYuanStateStringByCode(hashMap.get(MapParams.第二块表电源状态)));
        sb.append("第二块表倒流状态:  ");
        sb.append(LorawanUtils.getLiuXiangStateStringByCode(hashMap.get(MapParams.第二块表倒流状态)));
        sb.append("\n第二块表ID:  ");
        sb.append(hashMap.get(MapParams.第二块表ID));
        sb.append("\n第二块表计数:  ");
        sb.append(hashMap.get(MapParams.第二块表计数));
        sb.append("\n第一块表表号:  ");
        sb.append(hashMap.get(MapParams.第一块表表号));
        sb.append("\n第二块表表号:  ");
        sb.append(hashMap.get(MapParams.第二块表表号));
        sb.append("\n第一块表倍率:  ");
        sb.append(hashMap.get(MapParams.第一块表倍率));
        sb.append("\n第二块表倍率:  ");
        sb.append(hashMap.get(MapParams.第二块表倍率));

        sb.append("\n第一块表累积倒流数:  ");
        sb.append(hashMap.get(MapParams.第一块表累积倒流数));
        sb.append("\n第二块表累积倒流数:  ");
        sb.append(hashMap.get(MapParams.第二块表累积倒流数));
        sb.append("\n第一块表传感信号:  ");
        sb.append(hashMap.get(MapParams.第一块表传感信号));
        sb.append("\n第二块表传感信号:  ");
        sb.append(hashMap.get(MapParams.第二块表传感信号));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextJizhongInfo.setText(sb.toString());
            }
        });
    }


    private void setAtDuShu(HashMap<String, String> hashMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("发送功率:  ");
        sb.append(hashMap.get(SAtInstructParams.sAtInstructSendingPower));
        sb.append("\n信道参数:  ");
        sb.append(hashMap.get(SAtInstructParams.sAtInstructChannel));
        sb.append("\nRxDelay:  ");
        sb.append(hashMap.get(SAtInstructParams.sAtInstructRxDelay) + "s");
        sb.append("\n扩频因子:  ");
        sb.append(hashMap.get(SAtInstructParams.sAtInstructSpreadingFactor));
        sb.append("\nLSY-IOT版本号:  ");
        sb.append(hashMap.get(SAtInstructParams.sAtInstructFirmwareVersion));
        runOnUiThread(() -> {
            if (!mSwitchSaveStatus.isChecked()) {
                if (!XHStringUtil.isEmpty(hashMap.get(MapParams.发送功率), false)) {
                    if ("20dbm".equals(hashMap.get(SAtInstructParams.sAtInstructSendingPower))) {
                        mAtFasonggonglvSelect.setSelection(0);
                    } else if ("18dbm".equals(hashMap.get(SAtInstructParams.sAtInstructSendingPower))) {
                        mAtFasonggonglvSelect.setSelection(1);
                    } else if ("16dbm".equals(hashMap.get(SAtInstructParams.sAtInstructSendingPower))) {
                        mAtFasonggonglvSelect.setSelection(2);
                    }
                }
                kuopinyinziArray=    getResources().getStringArray(R.array.l_loushanyun_kuopinyinzi) ;
                if (!XHStringUtil.isEmpty(hashMap.get(SAtInstructParams.sAtInstructSpreadingFactor), false)) {
                    for (int i = 0; i < kuopinyinziArray.length; i++) {
                        if (kuopinyinziArray[i].equals(hashMap.get(SAtInstructParams.sAtInstructSpreadingFactor))) {
                            mAtKuopinyinziSelect.setSelection(i);
                        }
                    }
                }
                mAtXindaocanshuSelect.setSelection(sAtInstructRealizeFactory.getMoShiIndex(hashMap.get(SAtInstructParams.sAtInstructChannel)));
            }
            mAtTextInfo.setText(sb.toString());
        });
    }

    private void closeJiHuo() {
        byte[] d = new byte[6];
        d[0] = (byte) 0x68;
        d[1] = (byte) 0x02;//有效数据
        d[2] = (byte) 0x02;//命令,设置成未激活
        d[3] = (byte) 0x00;//停用
        byte cs = 0;
        for (int i = 1; i < 4; i++) {
            cs += d[i];
        }
        d[4] = cs;//校验和
        d[5] = (byte) 0x16;
        snBlueToothTool.write(d, "正在停用无线上传");
    }
    /**
     * 设备激活
     */
//    private void openJiHuo() {
//        byte[] d = new byte[6];
//        d[0] = (byte) 0x68;
//        d[1] = (byte) 0x02;//有效数据
//        d[2] = (byte) 0x02;//命令,设置成未激活
//        d[3] = (byte) 0x01;//激活
//        byte cs = 0;
//        for (int i = 1; i < 4; i++) {
//            cs += d[i];
//        }
//        d[4] = cs;//校验和
//        d[5] = (byte) 0x16;
//        snBlueToothTool.write(d, "正在激活无线上传");
//    }

}
