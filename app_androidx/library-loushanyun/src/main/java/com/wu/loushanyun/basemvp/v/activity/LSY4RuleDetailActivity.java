package com.wu.loushanyun.basemvp.v.activity;

import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.flyco.roundview.RoundTextView;
import com.wu.loushanyun.R;
import com.wu.loushanyun.base.config.K;
import com.wu.loushanyun.basemvp.init.LouShanYunCode;
import com.wu.loushanyun.basemvp.m.ModuleRuleDetail;
import com.wu.loushanyun.basemvp.p.adapter.LSY4RuleDetailViewBinder;
import com.wu.loushanyun.basemvp.p.runner.GetModule4ParseRuleDetailRunner;

import java.util.ArrayList;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.baseevent.Event;
import met.hx.com.librarybase.some_utils.ByteConvertUtils;

@Route(path = K.LSY4RuleDetailActivity)
public class LSY4RuleDetailActivity extends BaseNoPresenterActivity {
    private RecyclerView recycleRuleDetail;
    private RoundTextView roundtextSure;
    private LSY4RuleDetailViewBinder lsy4RuleDetailViewBinder;
    private MultiTypeAdapter multiTypeAdapter;
    private ArrayList<ModuleRuleDetail.DataBean> dataBeanArrayList;
    private int parseId;
    private String manufacturersIdentification;

    @Override
    protected void initEventListener() {
        registerEventRunner(LouShanYunCode.GetModule4ParseRuleDetailRunner,new GetModule4ParseRuleDetailRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if(event.getEventCode()==LouShanYunCode.GetModule4ParseRuleDetailRunner){
            if(event.isSuccess()){
                ModuleRuleDetail moduleRuleDetail= (ModuleRuleDetail) event.getReturnParamAtIndex(0);
                if(moduleRuleDetail.getCode()==0){
                    dataBeanArrayList.addAll(moduleRuleDetail.getData());
                    multiTypeAdapter.notifyDataSetChanged();
                }else {
                    sendMessageToast(moduleRuleDetail.getMsg());
                }
            }
        }

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        getAllData();
        ba.mActivityLayoutId= R.layout.l_loushanyun_lsy_4_rule_detail;
        ba.mTitleText="协议详情";
    }

    private void getAllData() {
        parseId=getIntent().getIntExtra("parseId",0);
        manufacturersIdentification=getIntent().getStringExtra("manufacturersIdentification");
    }

    @Override
    protected void initView() {
        recycleRuleDetail = (RecyclerView) findViewById(R.id.recycle_rule_detail);
        roundtextSure = (RoundTextView) findViewById(R.id.roundtext_sure);

        dataBeanArrayList=new ArrayList<>();
        multiTypeAdapter=new MultiTypeAdapter(dataBeanArrayList);
        lsy4RuleDetailViewBinder=new LSY4RuleDetailViewBinder(new LSY4RuleDetailViewBinder.ModuleRuleDetailListener() {

            @Override
            public void onDelete(ModuleRuleDetail.DataBean item) {

            }
        });
        multiTypeAdapter.register(ModuleRuleDetail.DataBean.class,lsy4RuleDetailViewBinder);
        recycleRuleDetail.setAdapter(multiTypeAdapter);
        pushEvent(LouShanYunCode.GetModule4ParseRuleDetailRunner,manufacturersIdentification,parseId+"");

        roundtextSure.setOnClickListener(v -> {
            StringBuffer stringCommand=new StringBuffer();
            for(int i=0;i<dataBeanArrayList.size();i++){
                String s=dataBeanArrayList.get(i).getCommand().replaceAll(" ","")+ ByteConvertUtils.intToHex(Integer.valueOf(dataBeanArrayList.get(i).getMaxByte()));
                if(i<dataBeanArrayList.size()-1){
                    s=s+"\n";
                }
                stringCommand.append(s);
            }
            Intent intent = new Intent();
            intent.putExtra("stringCommand", stringCommand.toString());
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}
