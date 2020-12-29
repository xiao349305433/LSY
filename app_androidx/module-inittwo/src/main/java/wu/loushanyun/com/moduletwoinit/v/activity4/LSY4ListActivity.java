package wu.loushanyun.com.moduletwoinit.v.activity4;

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
import wu.loushanyun.com.moduletwoinit.m.InsidePublicMeter;
import wu.loushanyun.com.moduletwoinit.m.Refresh4ListEvent;
import wu.loushanyun.com.moduletwoinit.p.adapter.LSY4ListBinderView;

@Route(path = K.LSY4ListActivity)
public class LSY4ListActivity extends BaseNoPresenterActivity {
    private RecyclerView recycleList;

    private MultiTypeAdapter multiTypeAdapter;
    private ArrayList<InsidePublicMeter> arrayList;

    @Override
    protected void initEventListener() {

    }

    @Override
    protected void onEventRunEnd(Event event, int code) {

    }

    @Override
    protected void onInitAttribute(BaseAttribute ba) {
        ba.mActivityLayoutId = R.layout.m_twoinit_activity_lsy4_list;
        ba.mTitleText = "4号模组本地数据列表";
    }

    @Override
    protected void initView() {
        recycleList = (RecyclerView) findViewById(R.id.recycle_list);
        multiTypeAdapter = new MultiTypeAdapter();
        arrayList = new ArrayList<>();
        multiTypeAdapter.setItems(arrayList);
        multiTypeAdapter.register(InsidePublicMeter.class, new LSY4ListBinderView());
        recycleList.setAdapter(multiTypeAdapter);
        getData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refresh(Refresh4ListEvent refreshListEvent) {
        getData();
    }

    private void getData() {
        compositeDisposable.add(Flowable.create(
                (FlowableOnSubscribe<List<InsidePublicMeter>>) emitter -> {
                    LoadingDialogUtil.showByEvent("正在加载", this.getClass().getName());
                    int loginid = LoginFiveParamManager.getInstance().getLoginData().getId();
                    List<InsidePublicMeter> arrayList = LitePal.where("loginid = ?", loginid + "")
                            .find(InsidePublicMeter.class);
                    emitter.onNext(arrayList);
                    LoadingDialogUtil.dismissByEvent(this.getClass().getName());
                }
                , BackpressureStrategy.ERROR)
                .compose(RxSchedulers.io_main())
                .subscribe(list -> {
                    arrayList.clear();
                    arrayList.addAll(list);
                    multiTypeAdapter.notifyDataSetChanged();
                }));
    }
}
