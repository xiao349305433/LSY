package wu.loushanyun.com.modulechiptest.v.activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.elvishew.xlog.XLog;
import com.wu.loushanyun.base.config.K;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.SizeUtils;
import wu.loushanyun.com.modulechiptest.R;
import wu.loushanyun.com.modulechiptest.init.LoginParamManager;

@Route(path = K.ProduceHomeActivity)
public class ProduceHomeActivity extends BaseNoPresenterActivity {
    private ImageView back;
    private TextView title;
    private ImageView more;
    private RelativeLayout item1;
    private ImageView icon11;
    private ImageView logo11;
    private RelativeLayout item2;
    private ImageView icon2;
    private ImageView logo2;
    private RelativeLayout item3;
    private ImageView icon3;
    private ImageView logo3;
    private RelativeLayout item4Simu;
    private ImageView icon4Simu;
    private ImageView logo4Simu;
    private  PopupWindow myPop;


    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_chip_activity_producehome;
        ba.mTitleText = "模组生产";
        if(LoginParamManager.getInstance().getLoginInfo().getData().getMloginHomeType()==2){
            ba.mTitleRightImageIcon = R.drawable.ic_more_vert_black_24dp;
            ba.mTitleLeftRightWidth = 80;
        }
    }


    @Override
    public void onRightClick(View item) {
        showPicSelect(item);
    }
    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }


    @Override
    protected void initView() {
        item1 = (RelativeLayout) findViewById(R.id.item1);
        icon11 = (ImageView) findViewById(R.id.icon1_1);
        logo11 = (ImageView) findViewById(R.id.logo1_1);
        item2 = (RelativeLayout) findViewById(R.id.item2);
        icon2 = (ImageView) findViewById(R.id.icon2);
        logo2 = (ImageView) findViewById(R.id.logo2);
        item3 = (RelativeLayout) findViewById(R.id.item3);
        icon3 = (ImageView) findViewById(R.id.icon3);
        logo3 = (ImageView) findViewById(R.id.logo3);
        item4Simu = (RelativeLayout) findViewById(R.id.item4_simu);
        icon4Simu = (ImageView) findViewById(R.id.icon4_simu);
        logo4Simu = (ImageView) findViewById(R.id.logo4_simu);
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
        item1.setOnClickListener(this::OnClick);
        item2.setOnClickListener(this::OnClick);
        item3.setOnClickListener(this::OnClick);
        item4Simu.setOnClickListener(this::OnClick);

    }

    private void OnClick(View view) {
        switch (view.getId()) {
            case R.id.item1:
                ARouter.getInstance().build(K.FirstProduceTestActivity).navigation();
                break;
            case R.id.item2:
                ARouter.getInstance().build(K.SnPrintListActivity).navigation();
                break;
            case R.id.item3:
                ARouter.getInstance().build(K.ThirdProdueTestActivity).navigation();
                break;
            case R.id.item4_simu:
                ARouter.getInstance().build(K.FourSimuProduceActivity).navigation();
                break;
        }
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
        pop_produce.setVisibility(View.GONE);//

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
                finish();
            }
        });
    }


}
