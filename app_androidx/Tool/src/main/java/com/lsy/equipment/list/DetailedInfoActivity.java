package com.lsy.equipment.list;


import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lsy.domain.ResponseAnalysisInfo1;
import com.lsy.login.R;

import java.util.ArrayList;
import java.util.List;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.baseevent.Event;

/**
 * 强制发送数据返回 详细信息
 *
 * @author zhang.q.s
 */
public class DetailedInfoActivity extends BaseNoPresenterActivity {
    private ListView detailedInfo_List = null;
    DetailedInfoDeviceListAdapter detailedInfoDeviceListAdapter = null;
    private String sensoro;
    private TextView tv_totalNumber, tv_percentPass;
    ArrayList<String> infoList = new ArrayList<String>();
    private Gson gson;
    private String sendNumber;
    private String reciviceNumber;
    private String moshi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getView();
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        infoList = getIntent().getStringArrayListExtra("infoList");
        sensoro = getIntent().getStringExtra("sensoro");
        moshi = getIntent().getStringExtra("moshi");
        sendNumber = getIntent().getStringExtra("sendNumber");
        reciviceNumber = getIntent().getStringExtra("reciviceNumber");
        ba.mActivityLayoutId = R.layout.activity_detailed_info;
        ba.mTitleText="物联sn：" + getIntent().getStringExtra("sensoro");
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    private void getView() {
        tv_totalNumber = (TextView) findViewById(R.id.tv_totalNumber);
        tv_percentPass = (TextView) findViewById(R.id.tv_percentPass);
        tv_totalNumber.setText(reciviceNumber);
        tv_percentPass.setText(String.valueOf((int)(Double.valueOf(reciviceNumber)*100 / Double.valueOf(sendNumber)))+"%");
        gson = new Gson();
        //向容器里写入数据
        List<ItemBean> list = new ArrayList<ItemBean>();
        for (int i = 0; i < infoList.size(); i++) {
            ResponseAnalysisInfo1 aLoginAnalysis = gson.fromJson(infoList.get(i),
                    ResponseAnalysisInfo1.class);
            String frequency2 = aLoginAnalysis.getFrequency();
            if ("0".equals(frequency2)) {
                frequency2 = "15分钟";
            } else if ("3".equals(frequency2)) {
                frequency2 = "24小时";
            } else if ("3".equals(frequency2)) {
                frequency2 = "48小时";
            } else {
                frequency2 = "72小时";
            }
            list.add(new ItemBean(sensoro,
                    moshi,
                    aLoginAnalysis.getSendPower(),
                    aLoginAnalysis.getBand(),
                    frequency2,
                    aLoginAnalysis.getSnr(),
                    aLoginAnalysis.getRssi(),
                    aLoginAnalysis.getSf()
            ));
        }
        detailedInfo_List = (ListView) findViewById(R.id.detailedInfo_List);
        detailedInfoDeviceListAdapter = new DetailedInfoDeviceListAdapter(list, DetailedInfoActivity.this);
        detailedInfo_List.setAdapter(detailedInfoDeviceListAdapter);


    }

}
