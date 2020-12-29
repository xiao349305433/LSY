package wu.loushanyun.com.sixapp.v.activity;

import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.elvishew.xlog.XLog;
import com.google.gson.Gson;
import com.lsy.domain.ResponseAnalysisInfo1;
import com.sensoro.sensor.kit.entity.SensoroDevice;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.DataParser;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.init.SnBlueToothListener;
import com.wu.loushanyun.basemvp.init.SnBlueToothTool;
import com.wu.loushanyun.basemvp.m.sat.SAtInstructRealizeFactory;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;

import java.math.BigDecimal;
import java.util.ArrayList;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.glide.GlideUtil;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.FromHtmlUtil;
import met.hx.com.librarybase.some_utils.KeyboardUtils;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.sixapp.R;
import wu.loushanyun.com.sixapp.init.LoginParamManager;
import wu.loushanyun.com.sixapp.init.SixCode;
import wu.loushanyun.com.sixapp.init.SixUtils;
import wu.loushanyun.com.sixapp.m.EnvironmentInfInfo;

import static com.wu.loushanyun.basemvp.init.SnBlueToothTool.BLUE_SEND_TIME_OUT;

@Route(path = K.SixLSYXingNengYanZhengActivity)
public class SixLSYXingNengYanZhengActivity  extends BaseNoPresenterActivity implements SnBlueToothListener {
    private ScrollView mTestScroll;
    private LinearLayout mTu4GifLayout;
    private TextView mTvTiaoshiSend;
    private ImageView mImgTiaoshi;
    private EditText mServiceEdit;
    private TextView mTvEnvironmental2;

    final int MSG_CONN = 1, MSG_CONNSUCCESS1 = 2;
    final int MSG_DIALOG = 5, MSG_SHOWVIEW = 6;
    final int MSG_CONNSUCCESS2 = 7, MSG_READSUCCESS = 8;
    final int MSG_READSUCCESSTONEXT = 9, MSG_ADDNUMBERSEND = 10;
    final int MSG_SENDTEXT = 12;
    private ArrayList<ResponseAnalysisInfo1> aList = new ArrayList<>();
    private int sendNumber = 0;
    private int reciviceNumber = 0;
    private int totalNum = 3;

    protected SnBlueToothTool snBlueToothTool;
    private SensoroDevice sensoroDevice;
    private SAtInstructRealizeFactory sAtInstructRealizeFactory;
    private EnvironmentInfInfo environmentInfInfo;

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mTitleText = "性能验证";
        ba.mTitleBackgroundColorId = R.color.blue_bg;
        ba.mActivityLayoutId = R.layout.m_six_activity_lsy_xingnengyanzheng;
    }


    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        snBlueToothTool = new SnBlueToothTool(this);
        registerLifeCycle(snBlueToothTool);
    }



    @Override
    protected void initView() {
        mTestScroll = (ScrollView) findViewById(R.id.test_scroll);
        mTestScroll = (ScrollView) findViewById(R.id.test_scroll);
        mTu4GifLayout = (LinearLayout) findViewById(R.id.tu4_gif_layout);
        mTvTiaoshiSend = (TextView) findViewById(R.id.tv_tiaoshi_send);
        mImgTiaoshi = (ImageView) findViewById(R.id.img_tiaoshi);
        mServiceEdit = (EditText) findViewById(R.id.service_edit);
        mServiceEdit.setEnabled(false);
        mTvEnvironmental2 = (TextView) findViewById(R.id.tv_environmental2);
        environmentInfInfo = getIntent().getParcelableExtra("environmentInfInfo");
        sAtInstructRealizeFactory = new SAtInstructRealizeFactory();
        mServiceEdit.setText(LoginParamManager.getInstance().getLoginInfo().getBusinessName());
        sensoroDevice = getIntent().getParcelableExtra("sensoroDevice");
        snBlueToothTool.connectBlueTooth(sensoroDevice);

        inclick();
    }


    private void inclick(){
        mTvTiaoshiSend.setOnClickListener(this::OnBlueToothClick);
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
            case R.id.tv_tiaoshi_send:
                try {
                    mTvEnvironmental2.setText("");
                    sendNumber=0;
                    aList.clear();
                    snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructForcedSend).getTwoReadBytes("15527919058")
                            , "强制发送", true, BLUE_SEND_TIME_OUT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }


    private void setlisttv() {
        String rssi= SixUtils.DatasBean.getSignalIntensity();;

        rssi= rssi.substring(1,rssi.length());

        String snr=SixUtils.DatasBean.getSignalRatio();

        snr=snr.substring(1,snr.length());


        XLog.i("LSY rssi====="+rssi);
        XLog.i("LSY snr====="+snr);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < aList.size(); i++) {
            ResponseAnalysisInfo1 aLoginAnalysis = aList.get(i);
            sb.append("强制" + FromHtmlUtil.get3Prehtml() + (i + 1) + "次");
            if (!aLoginAnalysis.getRssi().equals("-")) {
                sb.append(FromHtmlUtil.get3Prehtml() + "成功");
                sb.append(FromHtmlUtil.get3Prehtml() + "信噪比：" + ((Integer.parseInt(aLoginAnalysis.getSnr())>Integer.parseInt(snr))?aLoginAnalysis.getSnr():FromHtmlUtil.getRedhtml(aLoginAnalysis.getSnr()))
                );
                sb.append(FromHtmlUtil.get3Prehtml() + "信号强度：" + ((Integer.parseInt(aLoginAnalysis.getRssi())>Integer.parseInt(rssi))?aLoginAnalysis.getRssi():FromHtmlUtil.getRedhtml(aLoginAnalysis.getRssi()))
                );

                aLoginAnalysis.setIssendok(true);
                aLoginAnalysis.setSnrisok((Integer.parseInt(aLoginAnalysis.getSnr())>Integer.parseInt(snr))?true:false);
                aLoginAnalysis.setRssiisok((Integer.parseInt(aLoginAnalysis.getRssi())>Integer.parseInt(rssi))?true:false);
                if(aLoginAnalysis.isRssiisok()==false||aLoginAnalysis.isSnrisok()==false){
                    aLoginAnalysis.setIsok(false);
                }else {
                    aLoginAnalysis.setIsok(true);
                }

            } else {
                sb.append(FromHtmlUtil.get3Prehtml() + FromHtmlUtil.getRedhtml("失败"));
                sb.append(FromHtmlUtil.get3Prehtml() + "信噪比：-"  );
                sb.append(FromHtmlUtil.get3Prehtml() + "信号强度：-");
                aLoginAnalysis.setIssendok(false);
                aLoginAnalysis.setRssiisok(false);
                aLoginAnalysis.setSnrisok(false);
                aLoginAnalysis.setIsok(false);
            }

            sb.append(FromHtmlUtil.getBrhtml());
        }
        mTvEnvironmental2.setText(Html.fromHtml(sb.toString()));


        if (sb.toString().contains("失败")&&sb.toString().indexOf("失败")!=sb.toString().lastIndexOf("失败")) {
            mImgTiaoshi.setImageResource(R.mipmap.ic_wtg);
        } else if(sb.toString().contains("失败")&&sb.toString().indexOf("失败")==sb.toString().lastIndexOf("失败")){
            mImgTiaoshi.setImageResource(R.mipmap.ic_wtg);
        } else {
            mImgTiaoshi.setImageResource(R.mipmap.ic_ytg_right);
            SixFixActivity.Fix_Tu3=true;
        }

    }


    /**
     * 通过handler发送消息来执行相应的操作
     */
    protected Handler handler = new Handler() {


        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_CONN)//蓝牙连接的方法
            {

            } else if (msg.what == MSG_CONNSUCCESS1) {
                send2();
            } else if (msg.what == MSG_SENDTEXT) {
                //发送失败
                setlisttv();
            } else if (msg.what == MSG_ADDNUMBERSEND) {
                //发送命令成功数
                setlisttv();
            } else if (msg.what == MSG_CONNSUCCESS2) {
                //识别设备第一次发送
            } else if (msg.what == MSG_READSUCCESSTONEXT) {
                //全部发送完后，显示数据
            } else if (msg.what == MSG_DIALOG) {
                //显示dialog
            } else if (msg.what == MSG_SHOWVIEW) {
                //全部发送完后
                showview();

            }
        }
    };


    private void showview(){
        int oknum=0;
        for (int i = 0; i <aList.size() ; i++) {
            if(!aList.get(i).isIsok()){
                oknum++;
            }
        }
        if(oknum==0){
            mImgTiaoshi.setImageResource(R.mipmap.ic_ytg_right);
            SixFixActivity.Fix_Tu3=true;
        }else if(oknum==1){
            mImgTiaoshi.setImageResource(R.mipmap.ic_wtg);
        }else if(oknum>1){
            mImgTiaoshi.setImageResource(R.mipmap.ic_wtg);
        }

    }


    /**
     * 多次强制发送
     */
    private void send2() {
        if (sensoroDevice != null) {
            try {
                sendNumber++;
                //  handler.sendEmptyMessage(MSG_SENDTEXT);
                snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructForcedSend).getTwoReadBytes("15527919058")
                        , "强制发送", true, BLUE_SEND_TIME_OUT);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }


    @Override
    public void onChildConnectFailed(int i) {

    }

    private void initSnAgreement(double hardVersion) {
        sAtInstructRealizeFactory.initHardVersion(hardVersion);
    }
    @Override
    public void onChildConnectSuccess() {
        initSnAgreement(Double.valueOf(LouShanYunUtils.getHardWareVersion(Integer.valueOf(SixUtils.SecondHashMap.get(MapParams.硬件版本)))));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtils.showLong("连接成功");
                    mTvEnvironmental2.setText("");
                    aList.clear();
                    try {
                        snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructForcedSend).getTwoReadBytes("15527919058")
                                , "强制发送", true, BLUE_SEND_TIME_OUT);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
    }


    @Override
    public void onChildNotify(byte[] result) {
        byte code = (byte) (result[2] ^ ((byte) 0x80));
        XLog.i("蓝牙code=" + code);
        switch (code) {
            case 0x50:
                String resultAt = sAtInstructRealizeFactory.getSAtTypeString(result);
                XLog.i("蓝牙" + resultAt);
             if (resultAt.equals(SAtInstructParams.sAtInstructRSSI + "-R")) {
                    try {
                        SixUtils.SecondHashMap.putAll(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRSSI).parseTwoRNotifyBytes(result));
                        String[] strings =  SixUtils.SecondHashMap.get(SAtInstructParams.sAtInstructRSSI).split(",");
                        int rssi = Integer.valueOf(strings[0]);
                        int snr = Integer.valueOf(strings[1]);

                        ResponseAnalysisInfo1 responseAnalysisInfo1 = new ResponseAnalysisInfo1();
                        responseAnalysisInfo1.setRssi(rssi + "");
                        responseAnalysisInfo1.setSnr(snr + "");
                        Gson gson = new Gson();
                        aList.add(responseAnalysisInfo1);
                        reciviceNumber++;
                        handler.sendEmptyMessage(MSG_ADDNUMBERSEND);
                        if (sendNumber == totalNum) {
                            handler.sendEmptyMessage(MSG_SHOWVIEW);
                        } else {
                            send2();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }   else if (resultAt.equals(SAtInstructParams.sAtInstructForcedSend + "+SEND2")) {
                    try {
                        sendMessageToast("强制发送成功");
                        snBlueToothTool.write(sAtInstructRealizeFactory.setsAtParams(SAtInstructParams.sAtInstructRSSI).getTwoReadBytes(), "读取信号强度");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (resultAt.equals(SAtInstructParams.sAtInstructForcedSend + "+SEND3")) {
                    try {
                        sendMessageToast("强制发送失败");
                        ResponseAnalysisInfo1 responseAnalysisInfo1 = new ResponseAnalysisInfo1();
                        responseAnalysisInfo1.setRssi("-");
                        responseAnalysisInfo1.setSnr("-");
                        Gson gson = new Gson();
                        aList.add(responseAnalysisInfo1);
                        handler.sendEmptyMessage(MSG_SENDTEXT);//发送失败

                        if (sendNumber == totalNum) {
                            handler.sendEmptyMessage(MSG_SHOWVIEW);
                        } else {
                            send2();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    sendMessageToast(resultAt);
                }
                break;

            default:
                sendMessageToast("命令与模块不对应，请重新选择模块");
        }
    }



    @Override
    public void onChildWriteSuccess() {

    }

    @Override
    public void onChildWriteFailure(int i) {

    }
}
