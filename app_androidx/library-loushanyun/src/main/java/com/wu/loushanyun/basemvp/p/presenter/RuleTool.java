package com.wu.loushanyun.basemvp.p.presenter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.wu.loushanyun.R;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.basemvp.init.LouShanYunCode;
import com.wu.loushanyun.basemvp.m.ModuleRule;
import com.wu.loushanyun.basemvp.m.ModuleRuleDetail;
import com.wu.loushanyun.basemvp.p.adapter.LSY4RuleViewBinder;
import com.wu.loushanyun.basemvp.p.runner.GetModule4ParseRuleDetailRunner;
import com.wu.loushanyun.basemvp.p.runner.GetModule4ParseRuleRunner;

import java.util.ArrayList;

import met.hx.com.base.base.PushNetWork;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ByteConvertUtils;
import met.hx.com.librarybase.some_utils.ToastUtils;
import met.hx.com.librarybase.views.dialog.MDDialog;

import static android.app.Activity.RESULT_OK;

public class RuleTool extends PushNetWork {

    public static final int RULECODE = 10001;
    private MDDialog mdDialog;
    private MultiTypeAdapter multiTypeAdapter;
    private ArrayList<ModuleRule.DataBean> dataBeanArrayList;
    private OnRuleCallBack onRuleCallBack;
    private int manufacturersIdentification;
    private ModuleRule.DataBean jumpDataBean;
    private int themeColor;


    public RuleTool(OnRuleCallBack onRuleCallBack, int themeColor) {
        this.onRuleCallBack = onRuleCallBack;
        this.themeColor = themeColor;
    }

    @Override
    public void onCreate(Bundle savedInstanceState, Activity activity) {
        super.onCreate(savedInstanceState, activity);
        View view = getActivity().getLayoutInflater().inflate(R.layout.l_loushanyun_dialog_rule, null);
        this.mdDialog = new MDDialog.Builder(getActivity()).setContentView(view)
                .setShowTitle(false)
                .setShowButtons(false)
                .create();
        RecyclerView recycleRule = (RecyclerView) view.findViewById(R.id.recycle_rule);
        LSY4RuleViewBinder lsy4RuleViewBinder = new LSY4RuleViewBinder(new LSY4RuleViewBinder.OnLSY4RuleListener() {
            @Override
            public void onPitch(int position, ModuleRule.DataBean dataBean) {
                mdDialog.dismiss();
                pushEvent(LouShanYunCode.GetModule4ParseRuleDetailRunner, manufacturersIdentification + "", dataBean.getId() + "", dataBean);
            }

            @Override
            public void onDetails(int position, ModuleRule.DataBean dataBean) {
                RuleTool.this.jumpDataBean = dataBean;
                ARouter.getInstance().build(K.LSY4RuleDetailActivity).withInt("parseId", dataBean.getId())
                        .withString("manufacturersIdentification", manufacturersIdentification + "")
                        .navigation(getActivity(), RuleTool.RULECODE);
            }
        }, themeColor);
        multiTypeAdapter = new MultiTypeAdapter();
        dataBeanArrayList = new ArrayList<>();
        multiTypeAdapter.register(ModuleRule.DataBean.class, lsy4RuleViewBinder);
        multiTypeAdapter.setItems(dataBeanArrayList);
        recycleRule.setAdapter(multiTypeAdapter);
    }

    public interface OnRuleCallBack {
        void onRuleBack(String result);

        void onVersionBack(ModuleRule.DataBean dataBean);
    }


    public void showRuleDialog(int manufacturersIdentification) {
        this.manufacturersIdentification = manufacturersIdentification;
        pushEvent(LouShanYunCode.GetModule4ParseRuleRunner, manufacturersIdentification + "");
    }

    /**
     * 该方法需要再activity的onActivityResult中调用获取返回的信息
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (onRuleCallBack == null) return;
        String result;
        if (resultCode == RESULT_OK) {
            if (requestCode == RULECODE) {
                result = data.getStringExtra("stringCommand");
                mdDialog.dismiss();
                onRuleCallBack.onVersionBack(jumpDataBean);
                onRuleCallBack.onRuleBack(result);
            }
        }
    }

    @Override
    protected void initEventRunner() {
        registerEventRunner(LouShanYunCode.GetModule4ParseRuleRunner, new GetModule4ParseRuleRunner());
        registerEventRunner(LouShanYunCode.GetModule4ParseRuleDetailRunner, new GetModule4ParseRuleDetailRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == LouShanYunCode.GetModule4ParseRuleRunner) {
            if (event.isSuccess()) {
                ModuleRule moduleRule = (ModuleRule) event.getReturnParamAtIndex(0);
                if (moduleRule.getCode() == 0) {
                    dataBeanArrayList.clear();
                    dataBeanArrayList.addAll(moduleRule.getData());
                    multiTypeAdapter.notifyDataSetChanged();
                    mdDialog.show();
                } else {
                    ToastUtils.showShort(moduleRule.getMsg());
                }
            }
        } else if (event.getEventCode() == LouShanYunCode.GetModule4ParseRuleDetailRunner) {
            if (event.isSuccess()) {
                ModuleRuleDetail moduleRuleDetail = (ModuleRuleDetail) event.getReturnParamAtIndex(0);
                if (moduleRuleDetail.getCode() == 0) {
                    StringBuffer stringCommand = new StringBuffer();
                    for (int i = 0; i < moduleRuleDetail.getData().size(); i++) {
                        String s = moduleRuleDetail.getData().get(i).getCommand().replaceAll(" ", "") + ByteConvertUtils.intToHex(Integer.valueOf(moduleRuleDetail.getData().get(i).getMaxByte()));
                        if (i < moduleRuleDetail.getData().size() - 1) {
                            s = s + "\n";
                        }
                        stringCommand.append(s);
                    }
                    onRuleCallBack.onVersionBack((ModuleRule.DataBean) event.getParamAtIndex(2));
                    onRuleCallBack.onRuleBack(stringCommand.toString());
                } else {
                    ToastUtils.showShort(moduleRuleDetail.getMsg());
                }
            }
        }

    }
}
