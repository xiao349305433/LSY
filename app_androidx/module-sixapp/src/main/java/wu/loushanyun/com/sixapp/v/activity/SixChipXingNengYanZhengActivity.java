package wu.loushanyun.com.sixapp.v.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.base.util.LorawanUtils;
import com.wu.loushanyun.base.util.LouShanYunUtils;
import com.wu.loushanyun.base.util.MapParams;
import com.wu.loushanyun.basemvp.init.SnBlueToothListener;
import com.wu.loushanyun.basemvp.m.sat.SAtInstructRealizeFactory;
import com.wu.loushanyun.basemvp.m.sat.util.SAtInstructParams;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.glide.GlideUtil;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import wu.loushanyun.com.sixapp.R;
import wu.loushanyun.com.sixapp.init.SixUtils;
import wu.loushanyun.com.sixapp.m.EnvironmentInfInfo;


@Route(path = K.SixChipXingNengYanZhengActivity)
public class SixChipXingNengYanZhengActivity extends BaseNoPresenterActivity implements SnBlueToothListener {


    private ScrollView testScroll;
    private ImageView tu4ImgGif;
    private LinearLayout tu1Layout;
    private LinearLayout tu1FasongpinlvLayout;
    private EditText tu1FasongpinglvEdit;
    private LinearLayout tu1XindaoLayout;
    private EditText tu1XindaoEdit;
    private LinearLayout tu1KuopinyinziLayout;
    private EditText tu1KuopinyinziEdit;
    private LinearLayout tu1FasonggonglvLayout;
    private EditText tu1FasonggonglvEdit;
    private LinearLayout tu1RxdealyLayout;
    private EditText tu1RxdealyEdit;
    private LinearLayout tu2Layout;
    private LinearLayout tu2BeilvLayout;
    private EditText tu2BeilvEdit;
    private LinearLayout tu3Layout;
    private LinearLayout tu3ServiceLayout;
    private EditText tu3ServiceEdit;
    private LinearLayout tu3ChuanganLayout;
    private EditText tu3ChuanganEdit;
    private LinearLayout tu4GifLayout;
    private ImageView tu4ImgGifOk;

    private SAtInstructRealizeFactory sAtInstructRealizeFactory;
    private EnvironmentInfInfo environmentInfInfo;

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

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mTitleText = "性能验证";
        ba.mTitleBackgroundColorId = R.color.blue_bg;
        ba.mActivityLayoutId = R.layout.m_six_activity_lsy_xingnengyanzheng;
    }

    @Override
    protected void initView() {
        testScroll = (ScrollView) findViewById(R.id.test_scroll);
        tu4ImgGif = (ImageView) findViewById(R.id.tu4_img_gif);
        tu1Layout = (LinearLayout) findViewById(R.id.tu1_layout);
        tu1FasongpinlvLayout = (LinearLayout) findViewById(R.id.tu1_fasongpinlv_layout);
        tu1FasongpinglvEdit = (EditText) findViewById(R.id.tu1_fasongpinglv_edit);
        tu1XindaoLayout = (LinearLayout) findViewById(R.id.tu1_xindao_layout);
        tu1XindaoEdit = (EditText) findViewById(R.id.tu1_xindao_edit);
        tu1KuopinyinziLayout = (LinearLayout) findViewById(R.id.tu1_kuopinyinzi_layout);
        tu1KuopinyinziEdit = (EditText) findViewById(R.id.tu1_kuopinyinzi_edit);
        tu1FasonggonglvLayout = (LinearLayout) findViewById(R.id.tu1_fasonggonglv_layout);
        tu1FasonggonglvEdit = (EditText) findViewById(R.id.tu1_fasonggonglv_edit);
        tu1RxdealyLayout = (LinearLayout) findViewById(R.id.tu1_rxdealy_layout);
        tu1RxdealyEdit = (EditText) findViewById(R.id.tu1_rxdealy_edit);
        tu2Layout = (LinearLayout) findViewById(R.id.tu2_layout);
        tu2BeilvLayout = (LinearLayout) findViewById(R.id.tu2_beilv_layout);
        tu2BeilvEdit = (EditText) findViewById(R.id.tu2_beilv_edit);
        tu3Layout = (LinearLayout) findViewById(R.id.tu3_layout);
        tu3ServiceLayout = (LinearLayout) findViewById(R.id.tu3_service_layout);
        tu3ServiceEdit = (EditText) findViewById(R.id.tu3_service_edit);
        tu3ChuanganLayout = (LinearLayout) findViewById(R.id.tu3_chuangan_layout);
        tu3ChuanganEdit = (EditText) findViewById(R.id.tu3_chuangan_edit);
        tu4GifLayout= (LinearLayout) findViewById(R.id.tu4_gif_layout);
        tu4ImgGifOk= (ImageView) findViewById(R.id.tu4_img_gif_ok);

        sAtInstructRealizeFactory = new SAtInstructRealizeFactory();
        environmentInfInfo = getIntent().getParcelableExtra("environmentInfInfo");
        GlideUtil.displayGif(this, tu4ImgGif, R.mipmap.fix_error, R.mipmap.fix_error);
        GlideUtil.displayGif(this, tu4ImgGifOk, R.mipmap.fix_ok, R.mipmap.fix_ok);
        setdata();
    }


    private void setdata(){
        if (!XHStringUtil.isEmpty(SixUtils.LorawanHashMap.get(SAtInstructParams.sAtInstructChannel), false)) {
            if(!SixUtils.LorawanHashMap.get(SAtInstructParams.sAtInstructChannel).equals(environmentInfInfo.getDatas().get(0).getRemarks())){
                tu1XindaoEdit.setText(SixUtils.LorawanHashMap.get(SAtInstructParams.sAtInstructChannel));
            }else {
                tu1XindaoLayout.setVisibility(View.GONE);
            }
        }else {
            ToastUtils.showLong("请去配置物联调试");
        }



        if (!XHStringUtil.isEmpty(SixUtils.LorawanHashMap.get(SAtInstructParams.sAtInstructSpreadingFactor), false)) {
            if(!SixUtils.LorawanHashMap.get(SAtInstructParams.sAtInstructSpreadingFactor).equals(environmentInfInfo.getDatas().get(0).getSpreadFactor())){
                tu1KuopinyinziEdit.setText(SixUtils.LorawanHashMap.get(SAtInstructParams.sAtInstructSpreadingFactor));
            }else {
                tu1KuopinyinziLayout.setVisibility(View.GONE);
            }
        }

        if (!XHStringUtil.isEmpty(SixUtils.LorawanHashMap.get(SAtInstructParams.sAtInstructRxDelay), false)) {
            if(Integer.valueOf(SixUtils.LorawanHashMap.get(SAtInstructParams.sAtInstructRxDelay))!=environmentInfInfo.getDatas().get(0).getDelayParameter()){
                tu1RxdealyEdit.setText(SixUtils.LorawanHashMap.get(SAtInstructParams.sAtInstructRxDelay)+"S");
            }else {
                tu1RxdealyLayout.setVisibility(View.GONE);
            }
        }

//        BigDecimal decimal = new BigDecimal(LouShanYunUtils.getBLReadStringByCode(Integer.parseInt(SixUtils.SecondHashMap.get(MapParams.倍率))));
//        tu2BeilvEdit.setText(decimal.stripTrailingZeros().toPlainString());

        tu3ServiceEdit.setText(SixUtils.LorawanHashMap.get(MapParams.厂家标识));

//        if (!XHStringUtil.isEmpty(SixUtils.LorawanHashMap.get(MapParams.传感信号), false)) {
//            if(!SixUtils.LorawanHashMap.get(MapParams.传感信号).equals(environmentInfInfo.getDatas().get(0).getDelayParameter())){
//                tu3ChuanganEdit.setText(LorawanUtils.getChuanGanXinHaoByCode(SixUtils.LorawanHashMap.get(MapParams.传感信号)));
//            }else {
//
//            }
//            tu3ChuanganEdit.setText(LorawanUtils.getChuanGanXinHaoByCode(SixUtils.LorawanHashMap.get(MapParams.传感信号)));
//        }else {
//            ToastUtils.showLong("请去配置产品属性");
//        }
        tu3ChuanganLayout.setVisibility(View.GONE);

        if(tu1XindaoLayout.getVisibility()==View.GONE&&tu1FasonggonglvLayout.getVisibility()==View.GONE&&tu1RxdealyLayout.getVisibility()==View.GONE){
            tu1Layout.setVisibility(View.GONE);
        }
        if(tu3ChuanganLayout.getVisibility()==View.GONE){
            tu3Layout.setVisibility(View.GONE);
        }

        if(tu3Layout.getVisibility()==View.GONE&&tu1Layout.getVisibility()==View.GONE){
            testScroll.setVisibility(View.GONE);
            SixFixActivity.Fix_Tu4=true;
        }else {
            tu4GifLayout.setVisibility(View.GONE);
        }


    }
}
