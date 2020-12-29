package wu.loushanyun.com.moduletwoinit.v.activity;

import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wu.loushanyun.base.config.K;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableOnSubscribe;
import met.hx.com.base.base.BaseAttribute;
import met.hx.com.base.base.activity.BaseNoPresenterActivity;
import met.hx.com.base.base.dialog.LoadingDialogUtil;
import met.hx.com.base.base.multitype.MultiTypeAdapter;
import met.hx.com.base.base.rx.RxSchedulers;
import met.hx.com.base.baseevent.Event;
import wu.loushanyun.com.libraryfive.init.LoginFiveParamManager;
import wu.loushanyun.com.moduletwoinit.R;
import wu.loushanyun.com.moduletwoinit.m.RefreshListEvent;
import wu.loushanyun.com.moduletwoinit.m.WuLianUploadData;
import wu.loushanyun.com.moduletwoinit.p.adapter.WuLianUploadDataBinder;

@Route(path = K.WuLianListActivity)
public class WuLianListActivity extends BaseNoPresenterActivity {
    private RecyclerView recyclerview;
    private MultiTypeAdapter multiTypeAdapter;
    private ArrayList<WuLianUploadData> wuLianUploadData;
    private WuLianUploadDataBinder wuLianUploadDataBinder;

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }


    @Override
    protected void initLifeCycle() {
        super.initLifeCycle();
        wuLianUploadDataBinder = new WuLianUploadDataBinder();
        registerLifeCycle(wuLianUploadDataBinder);
    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_twoinit_activity_wulianlist;
        ba.mTitleText = "物联网端数据";
    }

    @Override
    protected void initView() {
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        multiTypeAdapter = new MultiTypeAdapter();

        multiTypeAdapter.register(WuLianUploadData.class, wuLianUploadDataBinder);
        wuLianUploadData = new ArrayList<>();
        multiTypeAdapter.setItems(wuLianUploadData);
        recyclerview.setAdapter(multiTypeAdapter);
        getData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(RefreshListEvent refreshListEvent) {
        getData();
    }

    private void getData() {
        compositeDisposable.add(Flowable.create(
                (FlowableOnSubscribe<List<WuLianUploadData>>) emitter -> {
                    LoadingDialogUtil.showByEvent("正在加载", this.getClass().getName());
                    int loginid = LoginFiveParamManager.getInstance().getLoginData().getId();
                    List<WuLianUploadData> arrayList = LitePal.where("loginid = ?", loginid + "").find(WuLianUploadData.class);
                    emitter.onNext(arrayList);
                    LoadingDialogUtil.dismissByEvent(this.getClass().getName());
                }
                , BackpressureStrategy.ERROR)
                .compose(RxSchedulers.io_main())
                .subscribe(list -> {
                    wuLianUploadData.clear();
                    wuLianUploadData.addAll(list);
                    multiTypeAdapter.notifyDataSetChanged();
                }));
    }

}
