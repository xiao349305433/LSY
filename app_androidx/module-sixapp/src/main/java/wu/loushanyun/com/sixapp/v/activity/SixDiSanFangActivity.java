package wu.loushanyun.com.sixapp.v.activity;

import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.basemvp.v.activity.BaseSnBlueToothActivity;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.glide.GlideUtil;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.sixapp.R;
import wu.loushanyun.com.sixapp.init.LoginParamManager;
import wu.loushanyun.com.sixapp.init.SixCode;
import wu.loushanyun.com.sixapp.m.GoodsInfo;
import wu.loushanyun.com.sixapp.m.ProInfo;
import wu.loushanyun.com.sixapp.m.WorkInfo;
import wu.loushanyun.com.sixapp.p.runner.GetBatchRunner;
import wu.loushanyun.com.sixapp.p.runner.GoodsInfRunner;
import wu.loushanyun.com.sixapp.p.runner.WorkInfRunner;


@Route(path = K.SixDiSanFangActivity)
public class SixDiSanFangActivity extends BaseNoPresenterActivity {
    private TextView diSanfang_tv1;
    private TextView diSanfang_tv2;
    private  WorkInfo workInfo;
    private  GoodsInfo goodsInfo;
    private ProInfo proInfo;
    private ImageView ImgGoods;

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId=R.layout.m_six_activity_disanfang;
        ba.mTitleText = "定形参数";
        ba.mTitleBackgroundColorId=R.color.blue_bg;
        ba.mHasRightView=false;
    }

    @Override
    protected void initView() {
        diSanfang_tv1= (TextView) findViewById(R.id.disanfang_tv1);
        diSanfang_tv2= (TextView) findViewById(R.id.disanfang_tv2);
        ImgGoods= (ImageView) findViewById(R.id.img_goods);
        proInfo=getIntent().getParcelableExtra("ProInfo");
        workInfo=getIntent().getParcelableExtra("WorkInfo");
        pushEvent(SixCode.MSixGoodsInf, proInfo.getData().getGoodsId(),LoginParamManager.getInstance().getLoginInfo().getLoginId(),LoginParamManager.getInstance().getLoginInfo().getId(),proInfo.getData().getBatchNumber(),workInfo.getData().getOrderId());



        String model= "物联设备类型：LORAWAN模组\n"+"产品特性：娄山云模组接入\n";
        String chip= "物联设备类型：LORAWAN芯片\n"+"产品特性：娄山云芯片接入\n";


    }

    private void setworkinfo( WorkInfo workInfo){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append("采集场景："+"民用\n");
        stringBuilder.append("产品形式："+workInfo.getData().getProductForm()+"\n");
        stringBuilder.append("产品名称："+goodsInfo.getDatas().get(0).getGoodsName()+"\n");
        stringBuilder.append("型号："+workInfo.getData().getGoodsModel()+"\n");
        stringBuilder.append("口径："+workInfo.getData().getCaliber()+"\n");

        diSanfang_tv1.setText(stringBuilder.toString());
    }

    private void setGoodsInfo(GoodsInfo goodsInfo){
        StringBuilder sb=new StringBuilder();
        sb.append("倍率/脉冲常数："+goodsInfo.getDatas().get(0).getMagnification()+"\n");
        sb.append("传感信号："+goodsInfo.getDatas().get(0).getSensingSignal()+"\n");
        sb.append("电池："+goodsInfo.getDatas().get(0).getBatteryConfig()+"\n");
        sb.append("复合电容："+goodsInfo.getDatas().get(0).getCompositeCapacitor()+"\n");
        sb.append("蓝牙："+goodsInfo.getDatas().get(0).getBluetooth()+"\n");
        sb.append("表壳："+goodsInfo.getDatas().get(0).getWatchCase()+"\n");
        sb.append("量层比："+goodsInfo.getDatas().get(0).getRangeRatio()+"\n");
        sb.append("机芯类型："+goodsInfo.getDatas().get(0).getMovementType()+"\n");
        sb.append("阀："+goodsInfo.getDatas().get(0).getValve()+"\n");
        diSanfang_tv2.setText(sb.toString());
        GlideUtil.display(this,ImgGoods,goodsInfo.getDatas().get(0).getGoodsPicture());
    }



    @Override
    protected void initEventListener() {
        registerEventRunner(SixCode.MSixGoodsInf, new GoodsInfRunner());
    }

    @Override

    protected void onEventRunEnd(Event event, int code) {
        if(code==SixCode.MSixGoodsInf){
                if(event.isSuccess()){
                     goodsInfo= (GoodsInfo) event.getReturnParamAtIndex(0);
                     if(goodsInfo.getCode()==0){
                         setworkinfo(workInfo);
                         setGoodsInfo(goodsInfo);
                     }
                }

            }
    }
}
