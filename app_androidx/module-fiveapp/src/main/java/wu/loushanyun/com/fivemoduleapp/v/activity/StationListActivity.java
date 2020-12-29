package wu.loushanyun.com.fivemoduleapp.v.activity;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.fivemoduleapp.R;
import wu.loushanyun.com.fivemoduleapp.m.BaseStation;
import wu.loushanyun.com.fivemoduleapp.p.adapter.StationListAdapter;
import wu.loushanyun.com.fivemoduleapp.p.runner.GetBaseStationRunner;
import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;
import wu.loushanyun.com.libraryfive.m.RefreshStation;
import wu.loushanyun.com.module_initthree.init.InitCode;

public class StationListActivity extends BaseNoPresenterActivity {
    private RecyclerView listContent;
    private List<BaseStation> list;
    private StationListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_five_activity_station_list;
        ba.mTitleText = "基站列表";
    }

    @Override
    protected void initView() {
        listContent = (RecyclerView) findViewById(R.id.list_content);
        list = new ArrayList<>();
        LinearLayoutManager layoutmanager = new LinearLayoutManager(this);
        //设置RecyclerView 布局
        listContent.setLayoutManager(layoutmanager);
        //设置Adapter
        adapter = new StationListAdapter(list, this);
        adapter.notifyDataSetChanged();
        listContent.setAdapter(adapter);
        pushEvent(InitCode.GetBaseStationRunner, String.valueOf(LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getId()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshStation(RefreshStation refreshStation) {
        pushEvent(InitCode.GetBaseStationRunner, String.valueOf(LoginFiveParamManager.getInstance().getLoginData().getTradeRegister().getId()));
    }

    @Override
    protected void initEventListener() {
        registerEventRunner(InitCode.GetBaseStationRunner, new GetBaseStationRunner());
    }

    @Override
    protected void onEventRunEnd(Event event, int code) {
        if (code == InitCode.GetBaseStationRunner) {
            if (event.isSuccess()) {
                list.clear();
                int len = (int) event.getReturnParamAtIndex(0);
                for (int i = 0; i < len; i++) {
                    list.add((BaseStation) event.getReturnParamAtIndex(i + 1));
                }
                adapter.notifyDataSetChanged();
            }
        }
    }
}
