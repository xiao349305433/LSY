package wu.loushanyun.com.sixapp.v.activity;

import android.widget.TextView;

import com.wu.loushanyun.basemvp.v.activity.BaseSnBlueToothActivity;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.sixapp.R;

public class SixYuanChuanActivity extends BaseSnBlueToothActivity {
    private TextView yuanchuantv1;
    private TextView yuanchuantv2;
    @Override
    protected int onChildLayout() {
        return R.layout.m_six_activity_yuanchuan;
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        super.onInitAttribute(ba);
        ba.mTitleText = "定形参数";
        ba.mTitleBackgroundColorId=R.color.blue_bg;
        ba.mHasRightView=false;
    }

    @Override
    protected void initView() {
        super.initView();
        yuanchuantv1= (TextView) findViewById(R.id.yuanchuan_tv1);
        yuanchuantv2= (TextView) findViewById(R.id.yuanchuan_tv2);
        yuanchuantv1.setText("采集场景：民用\n" +
                "产品形式：远传物联网端\n" +
                "芯片号：01C******0F2B\n" +
                "表身号：01C02323230120F2B\n" +
                "产品名称：智慧水务智能表\n" +
                "型号：LXSG-LORAWAN\n" +
                "口径：15mm\n" +
                "备注：XXXXXXXXXXXX（制造商填写的备注）\n" +
                "物联设备类型：LORAWAN\n" +
                "产品特性：娄山云模组接入\n" +
                "设备ID：15248515416111245\n" +
                "物联SN：01C02323230120F2B");
        yuanchuantv2.setText("生产标准\n" +
                "信号强度：-80\n" +
                "信噪比：0\n" +
                "扩频因子：SF9\n" +
                "延时：XXXXXXXXXX\n" +
                "发送功率 ：20dbm\n" +
                "出厂读数：12345m³\n" +
                "倍率/脉冲常数 ：0.1/10（可为空）\n" +
                "软件版本号：1.0\n" +
                "硬件版本号：1.0\n" +
                "传感信号：XXXXXXXXXXXXX\n" +
                "发送频率：24小时\n" +
                "信号类型：XXXXXXXXXX\n" +
                "倍率：0.1\n" +
                "电池：XXXXXXX\n" +
                "复合电容：XXXXXXXXX\n" +
                "蓝牙：有\n" +
                "表壳：XXXXXXXXXX\n" +
                "量程比：XXXXXXXXX\n" +
                "机芯类型：XXXXXXXXX\n" +
                "阀：XXXXXXX");
    }


    @Override
    public void onChildConnectFailed(int i) {

    }

    @Override
    public void onChildConnectSuccess() {

    }

    @Override
    public void onChildNotify(byte[] bytes) {

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
