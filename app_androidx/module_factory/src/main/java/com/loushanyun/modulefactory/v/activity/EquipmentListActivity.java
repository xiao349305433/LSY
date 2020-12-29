package com.loushanyun.modulefactory.v.activity;

import androidx.recyclerview.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.elvishew.xlog.XLog;
import com.loushanyun.modulefactory.R;
import com.loushanyun.modulefactory.init.LoginParamManager;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.basemvp.m.PrintBean;
import com.wu.loushanyun.basemvp.p.adapter.PrintBeanViewBinder;
import com.wu.loushanyun.basemvp.v.activity.BaseBlueToothActivity;

import java.util.ArrayList;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.XActivityUtils;
import met.hx.com.librarybase.some_utils.XHStringUtil;
import met.hx.com.librarybase.some_utils.util.AbSharedUtil;
import met.hx.com.librarybase.views.CustomPopWindow;
import met.hx.com.librarybase.views.dialog.MDDialog;

@Route(path = K.FEquipmentListActivity)
public class EquipmentListActivity extends BaseBlueToothActivity implements OnClickListener {
    private long mExitTime = 0;
    private MDDialog mdDialog;
    LinearLayout mozu_normorl;
    LinearLayout mozu_high;
    public static final int MO_NORMORL = 1;
    public static final int MO_HIGH = 2;
    public static final String TYPE = "Type";
    private CustomPopWindow popWindow;

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.activity_equipmentlist;
        ba.mTitleText = "设备列表";
        ba.mTitleRightImageIcon = R.drawable.ic_more_vert_black_24dp;
        ba.mTitleLeftRightWidth = 80;

    }

    @Override
    public void onRightClick(View item) {
        popWindow.showAsDropDown(item);
    }

    public void showPrintPopWindow() {
        View contentView = View.inflate(this, com.wu.loushanyun.R.layout.l_loushanyun_print_pop, null);
        popWindow = new CustomPopWindow.PopupWindowBuilder(this)
                .setView(contentView)//显示的布局，还可以通过设置一个View
                .setAnimationStyle(com.wu.loushanyun.R.style.base_showPopupAnimation)
                .size(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT) //设置显示的大小，不设置就默认包裹内容
                .create();//创建PopupWindow
        RecyclerView recyclePrinter = (RecyclerView) contentView.findViewById(com.wu.loushanyun.R.id.recycle_printer);
        ArrayList<PrintBean> arrayList = new ArrayList<>();
        arrayList.add(new PrintBean(R.drawable.ic_description_black_24dp, "切换账号"));
        if (XHStringUtil.isEmpty(AbSharedUtil.getString(EquipmentListActivity.this, "printerUsedStatus"), true)) {
            arrayList.add(new PrintBean(R.drawable.ic_description_black_24dp, "打印机：开"));
            AbSharedUtil.putString(EquipmentListActivity.this, "printerUsedStatus", "printerOpened");
        } else if ("printerClosed".equals(AbSharedUtil.getString(EquipmentListActivity.this, "printerUsedStatus"))) {
            arrayList.add(new PrintBean(R.drawable.ic_description_black_24dp, "打印机：关"));
        } else {
            arrayList.add(new PrintBean(R.drawable.ic_description_black_24dp, "打印机：开"));
        }
        MultiTypeAdapter multiTypeAdapter = new MultiTypeAdapter(arrayList);
        multiTypeAdapter.register(PrintBean.class, new PrintBeanViewBinder(this, new PrintBeanViewBinder.onPrinteListener() {
            @Override
            public void onClickPrint(PrintBean printBean) {
                if ("切换账号".equals(printBean.getTextName())) {
                    XLog.i("跳转");
                    ARouter.getInstance().build(K.LoginActivityPhone).navigation();
                } else if ("打印机：开".equals(printBean.getTextName())) {
                    arrayList.set(1, new PrintBean(R.drawable.ic_description_black_24dp, "打印机：关"));
                    AbSharedUtil.putString(EquipmentListActivity.this, "printerUsedStatus", "printerClosed");
                    multiTypeAdapter.notifyDataSetChanged();
                } else if ("打印机：关".equals(printBean.getTextName())) {
                    arrayList.set(1, new PrintBean(R.drawable.ic_description_black_24dp, "打印机：开"));
                    AbSharedUtil.putString(EquipmentListActivity.this, "printerUsedStatus", "printerOpened");
                    multiTypeAdapter.notifyDataSetChanged();
                }
            }
        }));
        recyclePrinter.setAdapter(multiTypeAdapter);
    }

    @Override
    protected void initView() {
        super.initView();
        View view1 = getLayoutInflater().inflate(R.layout.m_factory_mozu4_dialog, null);
        mdDialog = new MDDialog.Builder(this).setContentView(view1).setShowTitle(false).setShowButtons(false).create();
        mozu_normorl = view1.findViewById(R.id.mozu4_normal);
        mozu_high = view1.findViewById(R.id.mozu4_high);
        mozu_normorl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(K.LSYModule04SimuActivity).withInt(TYPE, MO_NORMORL).navigation();
                //   ARouter.getInstance().build(K.LSYModuleNew04Activity).navigation();
            }
        });

        mozu_high.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouter.getInstance().build(K.LSYModule04SimuActivity).withInt(TYPE, MO_HIGH).navigation();
                ARouter.getInstance().build(K.LSYModuleNew04Activity).navigation();
            }
        });
        showPrintPopWindow();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //1号  1.06以上
            case R.id.item1:
                ARouter.getInstance().build(K.BlueNameActivity).withString("dumpString", K.NewDigitalActivity).navigation();
                break;
            //1号 1.07以上
            case R.id.item1_1:
                ARouter.getInstance().build(K.LSYModule01Activity_107).navigation();
                break;
            //1号协议管理
            case R.id.item1_protocol:
//                intent = new Intent(this, Module1ProtocolActivity.class);
//                intent.putExtra("sensoroDevice", sensoroDevice);
//                startActivity(intent);
                break;
            //2号
            case R.id.item2:
                ARouter.getInstance().build(K.LSYModuleNew02Activity).navigation();
                break;
            //3号
            case R.id.item3:
                ARouter.getInstance().build(K.BlueNameActivity).withString("dumpString", K.LSYModule03Activity).navigation();
                break;
            //4号
            case R.id.item4:
                //   ARouter.getInstance().build(K.LSYModuleNew04Activity).navigation();
                break;
            case R.id.item4_simu:
                mdDialog.show();
                //   ARouter.getInstance().build(K.LSYModule04SimuActivity).navigation();
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                XActivityUtils.finishAllActivities();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onChildConnectSuccess() {
    }

    @Override
    protected void onChildNotify(byte[] results) {
    }

    @Override
    protected void onChildWriteSuccess() {

    }

    @Override
    protected void onChildWriteFailure(int i) {

    }

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onChildConnectFailed(int i) {

    }
}
